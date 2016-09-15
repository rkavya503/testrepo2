/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.SCEDBPEvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;
// $Revision$ $Date$

// TODO: add SSL after we get the basic event issuing working

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.eventinfo.EventInfoInstance.Participants;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.BiddingInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The Class SCEDBPEvent.
 */
public class SCEDBPEvent
{
	
	/**
	 * Instantiates a new sCEDBP event.
	 */
	public SCEDBPEvent()
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
			cal.set(Calendar.MONTH, 7);
			cal.set(Calendar.DAY_OF_MONTH, 27);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));
			
			// set the start time to 1/27/2008 12:00
			cal.set(Calendar.DAY_OF_MONTH, 28);
			cal.set(Calendar.HOUR_OF_DAY, 12);
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
			
			// set the end time to 1/27/2008 20:00			
			cal.set(Calendar.HOUR_OF_DAY, 20);
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			event.setEventTiming(eventTiming);
			
			BiddingInformation biddingInformation = new BiddingInformation();
			biddingInformation.setOpeningTime(eventTiming.getStartTime());
			biddingInformation.setClosingTime(eventTiming.getStartTime());			
			event.setBiddingInformation(biddingInformation);
			
			// create bids
			EventInformation eventInformation = new EventInformation();
			for(int p = 1; p < 2; p++)
			{
				String participantName = "test" + p;
				EventInfoInstance eventInfoInstance = new EventInfoInstance();
				Participants participants = new Participants();
				eventInfoInstance.setParticipants(participants);
				Values values = new Values();
				eventInfoInstance.setValues(values);
				eventInfoInstance.getParticipants().getAccountID().add(participantName);
				for(int b = 0; b < 8; b++)
				{
					EventInfoValue value = new EventInfoValue();
					value.setStartTime(b * 3600.0);
					value.setValue(100.0);
					eventInfoInstance.getValues().getValue().add(value);
				}
				eventInfoInstance.setEndTime(8 * 3600.0);
				eventInformation.getEventInfoInstance().add(eventInfoInstance);
			}
			event.setEventInformation(eventInformation);
			
			// create BEC event
	        event.setProgramName("DBP DA");
	        event.setEventIdentifier("1"); // must be unique
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            Holder<String> holderForRetValue = new Holder<String>();
            service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);
            String rv1 = holderForRetValue.value;
            ConstraintFilter rv2 = holderForConstraintFilter.value;
            System.out.println("rv1:\n" + rv1);
            System.out.println("rv2:\n" + rv2);
			
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
		new SCEDBPEvent();
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
