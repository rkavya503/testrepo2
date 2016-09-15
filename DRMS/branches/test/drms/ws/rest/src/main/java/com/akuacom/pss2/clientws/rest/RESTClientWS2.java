/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.rest.RESTClientWS2.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;
import org.openadr.dras.eventstate.ObjectFactory;

import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.OperationModeValue;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * The Class RESTClientWS2.
 */
public class RESTClientWS2 extends HttpServlet {

    /** The program manager. */
    private EventManager _eventManager = EJBFactory.getBean(EventManager.class);

    /** The participant manager. */
    private ClientManager clientManager = EJBFactory
            .getBean(ClientManager.class);
    private ClientEAO clientEAO = EJBFactory
            .getBean(ClientEAO.class);

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(RESTClientWS2.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            org.openadr.dras.eventstate.ListOfEventStates wsListOfEventStates = new org.openadr.dras.eventstate.ListOfEventStates();
            JAXBContext jc = JAXBContext
                    .newInstance("org.openadr.dras.eventstate");
            Marshaller marshaller = jc.createMarshaller();

            
            Principal principal =   request.getUserPrincipal();
            if(principal != null){
                String participantName = principal.getName();
                

                List<org.openadr.dras.eventstate.EventState> es = clientManager.getClientDrasEventStates(participantName);
                if(es != null){
                	for(org.openadr.dras.eventstate.EventState e: es){
                		wsListOfEventStates.getEventStates().add(e);
                	}
                	if(es.size()>0){
                    //work around for JACE confirmation info error
                    clientManager.updateParticipantCommunications(participantName,
                            new Date(), true, es.get(0).getSimpleDRModeData().getEventStatus(), 
                            es.get(0).getSimpleDRModeData().getOperationModeValue());
                	}else{
                		String eventStatus = EventStatus.NONE.toString();
                		String operationModeValue = OperationModeValue.UNKNOWN.toString();
                    	String pendingSignalValue = "";
            			String modeSignalValue = "";
            			Participant client = clientEAO.getClient(participantName);
            			if (client.isManualControl()) {
            				pendingSignalValue = clientManager.getClientManualSignalValueAsString(client, "pending");
            				modeSignalValue = clientManager.getClientManualSignalValueAsString(client, "mode");
            			} else {
            				pendingSignalValue = "none";
            				modeSignalValue = "normal";
            			}

						if (pendingSignalValue.equals("active")) {
                        	eventStatus =EventStatus.ACTIVE.toString();
                        } else if (pendingSignalValue.equals("near")) {
                        	eventStatus=EventStatus.NEAR.toString();
                        } else if (pendingSignalValue.equals("far")) {
                        	eventStatus=EventStatus.FAR.toString();
                        } else {
                        	eventStatus=EventStatus.NONE.toString();
                        }
                         operationModeValue = modeSignalValue.toUpperCase();
                    	 clientManager.updateParticipantCommunications(participantName, new Date(), true, eventStatus, operationModeValue);
                	}

                }
            	
            }
            
            JAXBElement<org.openadr.dras.eventstate.ListOfEventStates> wsListOfEventStatesElement = (new ObjectFactory())
                    .createListOfEventState(wsListOfEventStates);
            java.io.StringWriter sw = new StringWriter();
            NamespacePrefixMapper npm = new PMapper();
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                    npm);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://openadr.lbl.gov/src/1/EventState.xsd");
            marshaller.marshal(wsListOfEventStatesElement, sw);

            PrintWriter out = response.getWriter();
            out.println(sw.toString());
        } catch (PropertyException e) {
            log.debug(LogUtils.createExceptionLogEntry("",
                    LogUtils.CATAGORY_COMMS, e));
        } catch (JAXBException e) {
            log.debug(LogUtils.createExceptionLogEntry("",
                    LogUtils.CATAGORY_COMMS, e));
        }
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
