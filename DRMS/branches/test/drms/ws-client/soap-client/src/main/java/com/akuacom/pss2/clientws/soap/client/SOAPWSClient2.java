/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.soap.client.SOAPWSClient2.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.soap.client;

import java.io.StringWriter;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.openadr.dras.drasclientsoap.DRASClientSOAP;
import org.openadr.dras.drasclientsoap.DRASClientSOAP_Service;
import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.EventStateConfirmation;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.eventstate.ObjectFactory;

/**
 * The Class SOAPWSClient2.
 */
public class SOAPWSClient2 
{
	static private final String drasClientId = "test.test";
    
    /**
     * Instantiates a new SOAPWS client2.
     */
    private SOAPWSClient2()
    {
        try 
        {
        	String endPoint = "http://localhost:8080/SOAPClientWS/nossl/soap2";
        	// String endPoint = "https://cdp.openadr.com/SOAPClientWS/soap2";

         	Authenticator.setDefault( new SimpleAuthenticator() );

        	DRASClientSOAP service = 
        		new DRASClientSOAP_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/DRASClientSOAP/", 
        		"SOAPWS2Service")).getDRASClientSOAPPort();
        	
         	// force the endpoint since what comes back from the server isn't correct
			final BindingProvider bp = (BindingProvider) service;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
			  endPoint);

			// get the EventState
        	Holder<String> holderRV = new Holder<String>();
        	Holder<ListOfEventStates> eventStates = new Holder<ListOfEventStates>();
			service.getEventStates("", holderRV, eventStates);
			System.out.println("\ngetEventStates returned: " + holderRV.value);

			JAXBContext jc =
				JAXBContext.newInstance("org.openadr.dras.eventstate");
	    	JAXBElement<org.openadr.dras.eventstate.ListOfEventStates> 
	    		wsEventStatesElement = (new ObjectFactory()).
	    		createListOfEventState(eventStates.value);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			java.io.StringWriter sw = new StringWriter();
			marshaller.marshal(wsEventStatesElement, sw);
			System.out.println("\neventStates: " + sw);
			
			List<EventState> esList = wsEventStatesElement.getValue().getEventStates();

			// a more advanced client should check for multiple event states
			EventStateConfirmation confirm = new EventStateConfirmation();
			confirm.setDrasClientID(drasClientId);
			confirm.setEventStateID(BigInteger.valueOf(esList.get(0).getEventStateID()));
			confirm.setProgramName(esList.get(0).getProgramName());
			confirm.setEventIdentifier(esList.get(0).getEventIdentifier());
			confirm.setOperationModeValue(esList.get(0).getSimpleDRModeData().getOperationModeValue());
			confirm.setOptInStatus(true);
			String rv = service.setEventStateConfirmation(confirm);
			System.out.println("\nEventStateConfirmation returned: " + rv);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }    
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) 
    {
       new SOAPWSClient2();
    }    

    /**
     * The Class SimpleAuthenticator.
     */
    static class SimpleAuthenticator extends Authenticator
	{
		
		/* (non-Javadoc)
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		public PasswordAuthentication getPasswordAuthentication()
		{
			System.out.println("Authenticating");
			return new PasswordAuthentication(drasClientId, "Test_1234"
				.toCharArray());
		}
	} 
}
