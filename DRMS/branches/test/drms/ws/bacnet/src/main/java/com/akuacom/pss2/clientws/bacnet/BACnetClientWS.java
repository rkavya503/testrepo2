/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.bacnet.BACnetClientWS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.bacnet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import services.web.bacnet.ArrayOfstring;
import services.web.bacnet.BacnetPortType;

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

/**
 * The Class BACnetClientWS.
 */
@WebService(name = "bacnet", portName = "bacnetPortType", serviceName = "bacnet", targetNamespace = "urn:bacnet-web-services")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class BACnetClientWS implements BacnetPortType {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(BACnetClientWS.class);

    /** The program manager. */
    //@EJB
    private EventManager eventManager = EJBFactory.getBean(EventManager.class);

    private ClientManager clientManager = EJBFactory
            .getBean(ClientManager.class);

    private ThreadLocal<WebServiceContext> threadSafeContext = new ThreadLocal<WebServiceContext>();

    @Resource
    public void setContext(WebServiceContext context){
    	threadSafeContext.set(context);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see services.web.bacnet.BacnetPortType#getValue(java.lang.String,
     * java.lang.String)
     */
    @WebMethod
    @WebResult(name = "result", partName = "result")
    public String getValue(
            @WebParam(name = "options", partName = "options") String options,
            @WebParam(name = "path", partName = "path") String path) {
        String rv = "";

        if (path.equals("/DRAS/EventState")) {
            // this is not in the for statement so that is an exception occurs
            // there won't be anything in rvSB
            String participantName = threadSafeContext.get().getUserPrincipal().getName();
	        SystemManager sm = EJB3Factory.getLocalBean(SystemManager.class);
	        PSS2Properties props = sm.getPss2Properties();
            List<EventState> eventStates = clientManager
                    .getClientEventStates(participantName, 
                    props.getConfirmationLevel() == ConfirmationLevel.NONE);
            for (EventState eventState : eventStates)
                clientManager.createClientConversationState(eventState);

            rv = BACnetClientUtil.getBACnetData(participantName, eventStates);
        }
        return rv;
    }

    /*
     * (non-Javadoc)
     * 
     * @see services.web.bacnet.BacnetPortType#setValue(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @WebMethod
    @WebResult(name = "result", partName = "result")
    public String setValue(
            @WebParam(name = "options", partName = "options") String options,
            @WebParam(name = "path", partName = "path") String path,
            @WebParam(name = "value", partName = "value") String value) {
    	ClientConversationState es = null;
		EventState confES = new EventState();
        confES.setEventStateID(-1);
        boolean success = true;

        SystemManager sm = EJB3Factory.getLocalBean(SystemManager.class);
        PSS2Properties props = sm.getPss2Properties();
        boolean optOut = false;
        if (props.getConfirmationLevel() != ConfirmationLevel.NONE) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                byte[] bytes = value.getBytes();
                InputStream is = new ByteArrayInputStream(bytes);
                Document document = builder.parse(is);

                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();

                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child.getNodeName().equals("Sequence")) {
                        NodeList seqChildren = child.getChildNodes();
                        for (int j = 0; j < seqChildren.getLength(); j++) {
                            Node sepChild = seqChildren.item(j);

                            NamedNodeMap children2 = sepChild.getAttributes();
                            if (children2 != null) {
                                for (int h = 0; h < children2.getLength(); h++) {
                                    Node listChild = children2.item(h);
                                    if (listChild == null
                                            || listChild.getNodeValue() == null
                                            || listChild.getNodeName() == null)
                                        continue;
                                    if (listChild.getNodeValue().equals(
                                            "eventStateID")) {
                                        try {
                                            confES.setEventStateID(Integer
                                                    .parseInt(children2.item(
                                                            h + 1)
                                                            .getNodeValue()));
                                        } catch (NumberFormatException nfex) {
                                            confES.setEventModNumber(-1);
                                        }
                                        break;
                                    } else if (listChild.getNodeValue().equals(
                                            "drasClientID")) {
                                        confES.setDrasClientID(children2.item(
                                                h + 1).getNodeValue());
                                        break;
                                    } else if (listChild.getNodeValue().equals(
                                            "operationModeValue")) {
                                        confES.setOperationModeValue(children2.item(
                                                h + 1).getNodeValue());
                                        break;
                                    } else if (listChild.getNodeValue().equals(
                                            "eventIdentifier")) {
                                        confES.setEventIdentifier(children2.item(
                                                h + 1).getNodeValue());
                                        break;
                                    } else if (listChild.getNodeValue().equals(
                                            "programName")) {
                                        confES.setProgramName(children2.item(
                                                h + 1).getNodeValue());
                                        break;
                                    } else if (listChild.getNodeValue().equals(
                                            "optInStatus")) {
				                        if(children2.item(h + 1).getNodeValue().
				                        	equals("false"))
				                        {
							                optOut = true;   	
				                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException pcex) {
                // couldn't even get a builder from the factory
                success = false;
                log.debug(LogUtils.createExceptionLogEntry("",
                        LogUtils.CATAGORY_COMMS, pcex));
            } catch (SAXException sex) {
                // bad client XML
                success = false;
                log.debug(LogUtils.createExceptionLogEntry("",
                        LogUtils.CATAGORY_COMMS, sex));
            } catch (IOException ioex) {
                // bad client XML
                success = false;
                log.debug(LogUtils.createExceptionLogEntry("",
                        LogUtils.CATAGORY_COMMS, ioex));
            }

            if (success && confES.getDrasClientID() != null) {
                // got this far without an error. Now find matching ConversationState
                es = clientManager.getClientConversationState(confES
                        .getEventStateID());
                success = es != null
                        && confES.getDrasClientID()
                                .equals(es.getDrasClientId());
            }

            if (success) {
                // good confirmation. Will return success but need to remove the
                // ConversationState since a ConversationState should only get one
                // confirmation
                try {
                    clientManager.removeClientConversationState(confES.getEventStateID());
                } catch(EJBException ejbx) {
                    // Because of concurrent processes, it's possible
                    // the event state could already be deleted.
                    // So we won't panic if the delete fails.
                    log.warn("EJB exception deleting confirmed ClientConversationState: "+ejbx.getMessage());
                } 
            }
        }

        if (props.getConfirmationLevel() != ConfirmationLevel.FULL) {
            success = true;
        }

        String clientName = threadSafeContext.get().getUserPrincipal().getName();

        clientManager.updateParticipantCommunications(clientName,
                new Date(), success, es);
        
        if(optOut) 
        {
            SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
			PSS2Features features = systemManager.getPss2Features();
			if (features.isClientOptOut()) 
			{
	             Participant client = clientManager.getClientOnly(clientName);
	             if (client.getClientAllowedToOptOut()) {
	            	// TODO move to reports
	             	log.debug(LogUtils.createLogEntryUser(confES.getProgramName(),
	                      clientName, LogUtils.CATAGORY_COMMS, "client opt out received", ""));

		             eventManager.removeParticipantFromEvent(
		            	 confES.getEventIdentifier(), client.getParent());
	             } else {
	            	 // not logging this for now to avoid flooding logs.
	             }
			}
        }

        return success ? "SUCCESS" : "FAILURE";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * services.web.bacnet.BacnetPortType#getDefaultLocale(java.lang.String)
     */
    @WebMethod
    @WebResult(name = "result", partName = "result")
    public String getDefaultLocale(
            @WebParam(name = "options", partName = "options") String options) {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * services.web.bacnet.BacnetPortType#getSupportedLocals(java.lang.String)
     */
    @WebMethod
    @WebResult(name = "results", partName = "results")
    public ArrayOfstring getSupportedLocals(
            @WebParam(name = "options", partName = "options") String options) {
        return new ArrayOfstring();
    }
}
