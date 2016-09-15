/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.BEC.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

// TODO: add SSL after we get the basic event issuing working

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The Class BEC.
 */
public class BEC
{
	
	/**
	 * Instantiates a new bEC.
	 */
	public BEC()
	{
       try 
        {
        	String endPoint = "http://localhost:8080/UtilityOperatorWS/UtilityOperatorWS";
        	//String endPoint = "http://pge2.openadr.com/UtilityOperatorWS/UtilityOperatorWS";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	UtilityOperator service = 
        		new UtilityOperator_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/UtilityOperator/", 
        		"UtilityOperatorWSService")).getUtilityOperatorPort();

	        UtilityDREvent event = new UtilityDREvent();
	
	        // event timing
			EventTiming eventTiming = new EventTiming();

			// set the notification time to 1/27/2008 9:00
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 27);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));
			
			// set the start time to 1/27/2008 12:00
			cal.set(Calendar.HOUR_OF_DAY, 12);
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
			
			// set the end time to 1/27/2008 20:00			
			cal.set(Calendar.HOUR_OF_DAY, 20);
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			event.setEventTiming(eventTiming);
			
			// create BEC event
	        event.setProgramName("BEC");
	        event.setEventIdentifier("1"); // must be unique
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            Holder<String> holderForRetValue = new Holder<String>();
            service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);
            ConstraintFilter rv = holderForConstraintFilter.value;
            System.out.println("BEC rv:\n" + rv);
			
			// delete BEC event
//			UtilityDREvent eventDelete = new UtilityDREvent();
//            String eventType = "CANCEL";
//            rv = service.modifyDREvent(
//            	event.getEventIdentifier(), eventType, eventDelete);
//            System.out.println("CANCEL rv:\n" + rv);

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
		new BEC();
    }
    
	// TODO: add SSL after we get the basic event issuing working
    /**
	 * The Class SimpleAuthenticator.
	 */
	private class SimpleAuthenticator extends Authenticator
	{
		
		/* (non-Javadoc)
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		public PasswordAuthentication getPasswordAuthentication()
		{
			System.out.println("Authenticating");
			return new PasswordAuthentication("u", "v"
				.toCharArray());
		}
	} 
}
