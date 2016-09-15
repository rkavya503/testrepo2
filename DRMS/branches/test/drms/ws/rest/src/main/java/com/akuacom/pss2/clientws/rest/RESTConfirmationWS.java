/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.rest.RESTConfirmationWS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.contact.ConfirmationLevel;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * The Class RESTConfirmationWS.
 */
public class RESTConfirmationWS extends HttpServlet {

    /** The program manager. */
    private EventManager eventManager = 
    	EJBFactory.getBean(EventManager.class);

    private ClientManager clientManager = 
    	EJBFactory.getBean(ClientManager.class);

    private static final Logger log = Logger
            .getLogger(RESTConfirmationWS.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EventState eventState = new EventState();
        eventState.setEventStateID(-1);
        boolean success = true;

        SystemManager sm = EJB3Factory.getLocalBean(SystemManager.class);
        PSS2Properties props = sm.getPss2Properties();
        boolean optOut = false;
        if (props.getConfirmationLevel() != ConfirmationLevel.NONE) {
            try {
                // The payload needs to be trimmed
                StringBuilder sb = new StringBuilder();
                String line;

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(request.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String payload = sb.toString();
                payload = payload.trim();

                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();

                Document document = builder.parse(new ByteArrayInputStream(payload.getBytes()));
                Element root = document.getDocumentElement();
                NamedNodeMap children = root.getAttributes();
                for (int h = 0; h < children.getLength(); h++) {
                    Node listChild = children.item(h);
                    if (listChild.getNodeName().equals("drasClientID")) {
                        eventState.setDrasClientID(listChild.getNodeValue());
                    } else if (listChild.getNodeName().equals("eventIdentifier")) {
                        eventState.setEventIdentifier(listChild.getNodeValue());
                    } else if (listChild.getNodeName().equals("eventModNumber")) {
                        try {
                            eventState.setEventModNumber(Integer.parseInt(listChild.getNodeValue()));
                        } catch (NumberFormatException e) {
                            eventState.setEventModNumber(-1);
                        }
                    } else if (listChild.getNodeName().equals("eventStateID")) {
                        try {
                            eventState.setEventStateID(Long.parseLong(listChild
                                    .getNodeValue()));
                        } catch (NumberFormatException e) {
                            eventState.setEventStateID(-1);
                        }
                    } else if (listChild.getNodeName().equals("operationModeValue")) {
                        eventState.setOperationModeValue(listChild.getNodeValue());
                    } else if (listChild.getNodeName().equals("programName")) {
                        eventState.setProgramName(listChild.getNodeValue());
                    } else if (listChild.getNodeName().equals("optInStatus")) {
                        if(listChild.getNodeValue().equals("false"))
                        {
			                optOut = true;   	
                        }
                    }
                }
            } catch (ParserConfigurationException e1) {
                // couldn't even get a builder from the factory
                success = false;
                log.debug(LogUtils.createExceptionLogEntry("", LogUtils.CATAGORY_COMMS, e1));
            } catch (SAXException e2) {
                // bad client XML
                success = false;
                log.debug(LogUtils.createExceptionLogEntry("", LogUtils.CATAGORY_COMMS, e2));
            }

            
            
            
            if (success && eventState.getDrasClientID() != null) {
                ClientConversationState state = clientManager.getClientConversationState(eventState.getEventStateID());
                if (state != null) {
                    success = state.getDrasClientId().equals(eventState.getDrasClientID());
                }
            }


        }


        
        Principal principal =   request.getUserPrincipal();
        if(principal != null){
        	String clientName = principal.getName();
        	Participant client = null;
        	
            if (!success) {
            	client = clientManager.getClientOnly(clientName);
            	success = client.getClientCanFailConfirmation();
            }

            if (!success) {
                FireLogEntry logEntry = new FireLogEntry();
                logEntry.setCategory(LogUtils.CATAGORY_COMMS);
                logEntry.setUserName(clientName);
                logEntry.setDescription("Received REST EventStateConfirmation. Result: FAILURE");
                log.warn(logEntry);
            }
            
    		if (optOut) {
    			SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
    			PSS2Features features = systemManager.getPss2Features();
    			if (features.isClientOptOut()) {
    				if (client == null) {
    					client = clientManager.getClientOnly(clientName);
    				}
    				if (client.getClientAllowedToOptOut()) {
    					// TODO move to reports
    					log.debug(LogUtils.createLogEntryUser(
                                eventState.getProgramName(), clientName,
                                LogUtils.CATAGORY_COMMS, "client opt out received",
                                ""));

    					eventManager.removeParticipantFromEvent(
    							eventState.getEventIdentifier(), client.getParent());
    				} else {
    					// not logging this for now to avoid flooding logs.
    				}
    			}
    		}
        }

        
        

        PrintWriter out = response.getWriter();
        if (success)
            out.println("SUCCESS");
        else
            out.println("FAILURE");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}