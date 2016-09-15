/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.rest.RESTClientWS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.contact.ConfirmationLevel;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.LogUtils;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * The Class RESTClientWS.
 */
public class RESTClientWS extends HttpServlet {

    /** The participant manager. */
    private ClientManager clientManager = EJBFactory
            .getBean(ClientManager.class);

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(RESTClientWS.class);

    /** The Constant operationModeSchedule. */
    private static final String operationModeSchedule = "<p:operationModeSchedule/>";

    /** The Constant drEventData. */
    private static final String drEventData = "<p:drEventData/>";

    /** The Constant customData. */
    private static final String customData = "<p:customData/>";

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
            JAXBContext jc = JAXBContext
                    .newInstance("org.openadr.dras.eventstate");
            Marshaller marshaller = jc.createMarshaller();

            String participantName = request.getUserPrincipal().getName();
	        SystemManager sm = EJB3Factory.getLocalBean(SystemManager.class);
	        PSS2Properties props = sm.getPss2Properties();
            List<com.akuacom.pss2.util.EventState> pss2EventStates = clientManager
                    .getClientEventStates(participantName, 
                    props.getConfirmationLevel() == ConfirmationLevel.NONE);
            com.akuacom.pss2.util.EventState pss2EventState = pss2EventStates
                    .get(0);

            clientManager.createClientConversationState(pss2EventState);

            org.openadr.dras.eventstate.EventState wsEventState =            
                clientManager.parseEventState(participantName, pss2EventState);
            
            //work around for JACE confirmation info error
//            if (props.getConfirmationLevel()!=ConfirmationLevel.FULL) {
	            clientManager.updateParticipantCommunications(participantName,
	                    new Date(), true, wsEventState.getSimpleDRModeData().getEventStatus(), wsEventState.getSimpleDRModeData().getOperationModeValue());
//            }
            
            JAXBElement<org.openadr.dras.eventstate.EventState> wsEventStateElement = (new ObjectFactory())
                    .createEventState(wsEventState);

            java.io.StringWriter sw = new StringWriter();
            NamespacePrefixMapper npm = new PMapper();
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                    npm);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://openadr.lbl.gov/src/1/EventState.xsd");
            marshaller.marshal(wsEventStateElement, sw);

            // strip out empty lists for backward compatibility
            // TODO: is there a flag in the marshaller for this?
            StringBuffer sb = sw.getBuffer();
            int start = sb.indexOf(operationModeSchedule);
            if (start != -1) {
                sb.delete(start, start + operationModeSchedule.length());
            }
            start = sb.indexOf(drEventData);
            if (start != -1) {
                sb.delete(start, start + drEventData.length());
            }
            start = sb.indexOf(customData);
            if (start != -1) {
                sb.delete(start, start + customData.length());
            }

            PrintWriter out = response.getWriter();
            out.println(sb);
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
