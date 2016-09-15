/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.RDS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * The Class RDS.
 */
public class RDS
{
	
	/**
	 * Instantiates a new rDS.
	 */
	public RDS()
	{
       try
        {
         	// String host = "http://localhost:8080";
        	String host = "https://www.rds.openadr.com";
        	String endPoint = host + "/UtilityOperatorWS/UtilityOperatorWS";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	UtilityOperator service =
        		new UtilityOperator_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/UtilityOperator/",
        		"UtilityOperatorWSService")).getUtilityOperatorPort();

         	// force the endpoint since what comes back from the server isn't correct
			final BindingProvider bp = (BindingProvider) service;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
			  endPoint);

			UtilityDREvent event = new UtilityDREvent();

	        // event timing
			EventTiming eventTiming = new EventTiming();

			// set the notification time to 12/31/2008 16:00
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());

			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 0);
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			event.setEventTiming(eventTiming);

			EventInformation eventInfo = new EventInformation();

			EventInfoInstance modeEventInfoInstance = new EventInfoInstance();
			modeEventInfoInstance.setEventInfoTypeName("OperationModeValue");
			Values modeValues = new Values();
			modeEventInfoInstance.setValues(modeValues);
			List<EventInfoValue> modeEventInfoValues = modeValues.getValue();

			EventInfoInstance priceEventInfoInstance = new EventInfoInstance();
			priceEventInfoInstance.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE.name());
			Values priceValues = new Values();
			priceEventInfoInstance.setValues(priceValues);
			List<EventInfoValue> priceEventInfoValues = priceValues.getValue();

			for(int i = 0; i < 24; i++)
			{
				EventInfoValue eventInfoValue = new EventInfoValue();
				eventInfoValue.setStartTime(i * 3600.0);
				eventInfoValue.setValue(3.0); // high
				modeEventInfoValues.add(eventInfoValue);

				eventInfoValue = new EventInfoValue();
				eventInfoValue.setStartTime(i * 3600.0);
				eventInfoValue.setValue((double)i*2);
	            priceEventInfoValues.add(eventInfoValue);
			}
			
			eventInfo.getEventInfoInstance().add(modeEventInfoInstance);
			eventInfo.getEventInfoInstance().add(priceEventInfoInstance);
			event.setEventInformation(eventInfo);

			// create OPENADR event
	        event.setProgramName("RDS");
	        event.setEventIdentifier(Long.toString(System.currentTimeMillis()));
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            Holder<String> holderForRetValue = new Holder<String>();

            //service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);

            // TODO: rememeber to change the event id
            service.modifyDREvent("1250290482781", "MODIFY", event, 
           	holderForRetValue, holderForConstraintFilter);

            ConstraintFilter rv = holderForConstraintFilter.value;
            System.out.println("RDS rv:\n" + rv);

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
		new RDS();
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
			return new PasswordAuthentication("utilopws", "KanaEki7"
//			return new PasswordAuthentication("u", "v"
				.toCharArray());
		}
	} 

}
