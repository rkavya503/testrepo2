/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.soap.client.SOAPWSClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.soap.client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.openadr.dras.drasclientsoap.DRASClientSOAP;
import org.openadr.dras.drasclientsoap.DRASClientSOAP_Service;
import org.openadr.dras.eventstate.EventStateConfirmation;
import org.openadr.dras.eventstate.ListOfEventStates;


/**
 * The Class SOAPWSClient.
 */
public class SOAPWSClient 
{
    
    /**
     * Instantiates a new sOAPWS client.
     */
    private SOAPWSClient()
    {
        try 
        {
        	String endPoint = "http://cdp.openadr.com/SOAPClientWS/nossl/soap";
        	// String endPoint = "https://cdp.openadr.com/SOAPClientWS/soap";

         	Authenticator.setDefault( new SimpleAuthenticator() );

        	DRASClientSOAP service = 
        		new DRASClientSOAP_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/DRASClientSOAP/", 
        		"SOAPWSService")).getDRASClientSOAPPort();
        	
         	// force the endpoint since what comes back from the server isn't correct
			final BindingProvider bp = (BindingProvider) service;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
			  endPoint);

			// get the EventState
        	Holder<String> holderRV = new Holder<String>();
        	Holder<ListOfEventStates> eventStates = new Holder<ListOfEventStates>();
			service.getEventStates("", holderRV, eventStates);
			System.out.println("\ngetEventStates returned: " + holderRV.value);

			EventStateConfirmation confirm = new EventStateConfirmation();
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
       new SOAPWSClient();
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
			return new PasswordAuthentication("akua1", "Test_1234"
				.toCharArray());
		}
	} 
}
