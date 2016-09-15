/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.CAISO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ListOfPrograms;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.UtilityProgram;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The Class CAISO.
 */
public class CAISO
{
	
	/**
	 * Instantiates a new cAISO.
	 */
	public CAISO()
	{
       try 
        {
        	String endPoint = "http://localhost:8080/UtilityOperatorWS/UtilityOperatorWS";
        	// String endPoint = "http://caiso.openadr.com/UtilityOperatorWS/UtilityOperatorWS";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	UtilityOperator service =
        		new UtilityOperator_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/UtilityOperator/",
        		"UtilityOperatorWSService")).getUtilityOperatorPort();

	        UtilityDREvent event = new UtilityDREvent();
	
	        // event timing
			EventTiming eventTiming = new EventTiming();

			// set the notification time to 11/25/2008 16:00
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, 2008);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 26);
			cal.set(Calendar.HOUR_OF_DAY, 16);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));
			
			// set the start time to 11/25/2008 18:00
			cal.set(Calendar.HOUR_OF_DAY, 18);
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));
			
			// set the end time to 11/25/2008 20:00			
			cal.set(Calendar.HOUR_OF_DAY, 20);
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			event.setEventTiming(eventTiming);
			
			// create OPENADR event
	        event.setProgramName("OPENADR");
	        event.setEventIdentifier("1"); // must be unique
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            Holder<String> holderForRetValue = new Holder<String>();
            service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);
            ConstraintFilter rv = holderForConstraintFilter.value;
            System.out.println("OPENADR rv:\n" + rv);

			// create OPENADR EMERGENCY event
	        event.setProgramName("OPENADR EMERGENCY");
	        event.setEventIdentifier("2"); // must be unique
            holderForConstraintFilter = new Holder<ConstraintFilter>();
            holderForRetValue = new Holder<String>();
            service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);
            rv = holderForConstraintFilter.value; 
            System.out.println("OPENADR EMERGENCY rv:\n" + rv);
			
			// delete OPENADR EMERGENCY event
			UtilityDREvent eventDelete = new UtilityDREvent();
            String eventType = "CANCEL";
            eventDelete.setEventIdentifier(event.getEventIdentifier());
            eventDelete.setProgramName(event.getProgramName());
            holderForConstraintFilter = new Holder<ConstraintFilter>();
            holderForRetValue = new Holder<String>();
            service.modifyDREvent(event.getEventIdentifier(), eventType, event, holderForRetValue, holderForConstraintFilter );
            rv = holderForConstraintFilter.value;
            System.out.println("CANCEL rv:\n" + rv);

            // get programs
            Holder<ListOfPrograms> holderForListOfPrograms = new Holder<ListOfPrograms>();
            holderForRetValue = new Holder<String>();
            service.getPrograms(new ListOfProgramNames(),
				new ParticipantList(), holderForListOfPrograms, holderForRetValue);
            ListOfPrograms progs = holderForListOfPrograms.value;
            printPrograms(progs);
       } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
	}
	
    /**
     * Prints the program.
     * 
     * @param pa the pa
     */
    private void printProgram(UtilityProgram pa)
    {
        System.out.println("pa.getName :" + pa.getName());
        System.out.println("pa.getPriority :" + pa.getPriority().intValue());

        if(pa.getParticipants() != null && pa.getParticipants().getAccounts() != null &&
                pa.getParticipants().getAccounts().getParticipantID() != null)
        {
            for(int i=0; i< pa.getParticipants().getAccounts().getParticipantID().size(); i++)
            {
                System.out.println("UtilityProgram.getParticipant : " + pa.getParticipants().getAccounts().getParticipantID().get(i));
            }
        }
    }

    /**
     * Prints the programs.
     * 
     * @param pas the pas
     */
    private void printPrograms(ListOfPrograms pas)
    {
        if(pas != null && pas.getProgram() != null && pas.getProgram().size() > 0)
        {
            List<UtilityProgram> list = pas.getProgram();
            for (UtilityProgram aList : list)
            {
                printProgram(aList);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args)
	{	
		new CAISO();
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
