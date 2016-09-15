/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.Demo.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

// TODO: add SSL after we get the basic event issuing working

import org.openadr.dras.akuadrasclientdata.ClientData;
import org.openadr.dras.akuadrasclientdata.ListofClientData;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.openadr.dras.drasclient.ListOfCommsStatus;
import org.openadr.dras.drasclient.ListOfDRASClients;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.participantoperator.ParticipantOperator;
import org.openadr.dras.participantoperator.ParticipantOperator_Service;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperator;
import org.openadr.dras.akuautilityoperator.AkuaUtilityOperatorService;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ListOfPrograms;
import org.openadr.dras.utilityprogram.ParticipantList;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The Class Demo.
 */
public class Demo
{
	
	/**
	 * Instantiates a new demo.
	 */
	public Demo()
	{
       try
        {
        	// String host = "http://192.168.149.13:8080";
        	String host = "http://localhost:8080";
        	// String host = "http://caiso.openadr.com";

        	// TODO: add SSL after we get the basic event issuing working
         	Authenticator.setDefault( new SimpleAuthenticator() );

        	UtilityOperator service =
        		new UtilityOperator_Service(new URL(host +
        			"/UtilityOperatorWS/UtilityOperatorWS" + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/UtilityOperator/",
        		"UtilityOperatorWSService")).getUtilityOperatorPort();

        	ParticipantOperator partOpService =
        		new ParticipantOperator_Service(new URL(host +
        			"/ParticipantOperatorWS/ParticipantOperatorWS" + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/ParticipantOperator/",
        			"ParticipantOperatorWSService")).getParticipantOperatorPort();

            AkuaUtilityOperator akuaService =
                new AkuaUtilityOperatorService(new URL(host +
                    "/AkuaUtilityOperatorWS/AkuaUtilityOperatorWS" + "?wsdl"),
                new QName("http://www.openadr.org/DRAS/AkuaUtilityOperator/",
                "AkuaUtilityOperatorWSService")).getAkuaUtilityOperatorPort();
            

            UtilityDREvent event = new UtilityDREvent();

	        // event timing
			EventTiming eventTiming = new EventTiming();

			// set the notification time to 12/31/2008 16:00
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			cal.set(Calendar.HOUR_OF_DAY, 16);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));

			// set the start time to 12/31/2008 18:00
			cal.set(Calendar.HOUR_OF_DAY, 18);
			eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));

			// set the end time to 12/31/2008 20:00
			cal.set(Calendar.HOUR_OF_DAY, 20);
			eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

			event.setEventTiming(eventTiming);

			EventInformation eventInfo = new EventInformation();

			EventInfoInstance eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("LOAD_LEVEL")
			eventInfoInstance.setEventInfoTypeName("OperationModeValue");
			Values values = new Values();
			eventInfoInstance.setValues(values);
			List<EventInfoValue> eventInfoValues = values.getValue();
			EventInfoValue eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0); // normal
			eventInfoValues.add(eventInfoValue);
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0); // moderate
			eventInfoValues.add(eventInfoValue);
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0); // high
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
            values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
            // eventInfoInstance.setEventInfoTypeID("PRICE_ABSOLUTE")
			eventInfoInstance.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE.name());
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
            eventInfoValues.add(eventInfoValue);
            eventInfoValue = new EventInfoValue();
            eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
            eventInfoValues.add(eventInfoValue);
            eventInfoValue = new EventInfoValue();
            eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
            eventInfoValues.add(eventInfoValue);

			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("PRICE_RELATIVE")
			eventInfoInstance.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE.name());
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("PRICE_MULTIPLE")
			eventInfoInstance.setEventInfoTypeName("PRICE_MULTIPLE");
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("LOAD_LEVEL")
			eventInfoInstance.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL.name());
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("LOAD_AMOUNT")
			eventInfoInstance.setEventInfoTypeName("LOAD_AMOUNT");
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("LOAD_PERCENTAGE")
			eventInfoInstance.setEventInfoTypeName("LOAD_PERCENTAGE");
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			eventInfoInstance = new EventInfoInstance();
			// eventInfoInstance.setEventInfoTypeID("GRID_RELIABILITY")
			eventInfoInstance.setEventInfoTypeName("GRID_RELIABILITY");
			eventInfoValue = new EventInfoValue();
			eventInfoValue.setStartTime(0.0);
			eventInfoValue.setValue(1.0);
			eventInfoValue.setStartTime(60.0);
			eventInfoValue.setValue(2.0);
			eventInfoValue.setStartTime(120.0);
			eventInfoValue.setValue(3.0);
			values = new Values();
			eventInfoInstance.setValues(values);
			eventInfoValues = values.getValue();
			eventInfoValues.add(eventInfoValue);
			eventInfo.getEventInfoInstance().add(eventInfoInstance);

			event.setEventInformation(eventInfo);

			// create OPENADR event
	        event.setProgramName("DEMO");
	        event.setEventIdentifier(Long.toString(System.currentTimeMillis()));
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            Holder<String> holderForRetValue = new Holder<String>();
            service.initiateDREvent(event, holderForRetValue, holderForConstraintFilter);
            ConstraintFilter rv = holderForConstraintFilter.value;
            System.out.println("DEMO initiateDREvent rv:\n" + rv);

			// delete OPENADR EMERGENCY event
//			UtilityDREvent eventDelete = new UtilityDREvent();
//            String eventType = "CANCEL";
//            rv = service.modifyDREvent(
//            	event.getEventIdentifier(), eventType, eventDelete);
//            System.out.println("CANCEL rv:\n" + rv);

			// get programs
            Holder<ListOfPrograms> holderForListOfPrograms = new Holder<ListOfPrograms>();
            holderForRetValue = new Holder<String>();
            service.getPrograms(new ListOfProgramNames(),
				new ParticipantList(), holderForListOfPrograms, holderForRetValue);
            ListOfPrograms progs = holderForListOfPrograms.value;
            PrintTool.printPrograms(progs);

            Holder<ListOfParticipantAccounts> holderForListOfParticipantAccounts = new Holder<ListOfParticipantAccounts>();
            holderForRetValue = new Holder<String>();

	        service.getParticipantAccounts(new ParticipantList(), "DEMO", holderForListOfParticipantAccounts, holderForRetValue);
            ListOfParticipantAccounts pas = holderForListOfParticipantAccounts.value;
            if (pas != null && pas.getParticipantAccount() != null
				&& pas.getParticipantAccount().size() > 0)
			{
				for (ParticipantAccount participantAccount : pas.getParticipantAccount())
				{
					PrintTool.printParticipantAccount(participantAccount);
					ListOfParticipantAccountIDs listOfParticipants =
						new ListOfParticipantAccountIDs();
					listOfParticipants.getParticipantAccountID().
						add(participantAccount.getAccountID());
                    Holder<ListOfDRASClients> holderForListOfDRASClients = new Holder<ListOfDRASClients>();
                    holderForRetValue = new Holder<String>();

			        	partOpService.getDRASClientInfo(
			        	new ListOfIDs(),
			        	listOfParticipants,
			        	"", holderForListOfDRASClients, holderForRetValue);
                        ListOfDRASClients dRASClientInfo = holderForListOfDRASClients.value;
                        PrintTool.printListOfDRASClients(dRASClientInfo);
				}
			}
			else
			{
				System.out.println("Return 0 result;");
			}
            Holder<ListOfCommsStatus> holderForListOfCommsStatus = new Holder<ListOfCommsStatus>();
            holderForRetValue = new Holder<String>();

	        	service.getDRASClientCommsStatus(new ParticipantList(),
	        	new ListOfProgramNames(), holderForListOfCommsStatus, holderForRetValue);
            ListOfCommsStatus listOfStuts = holderForListOfCommsStatus.value;
            PrintTool.printListOfCommsStatus(listOfStuts);

            Holder<ListofClientData> holderForListofClientData = new Holder<ListofClientData>();
            holderForRetValue = new Holder<String>();

	        akuaService.getDRASClientData(new ParticipantList(),
	        	new ListOfProgramNames(), holderForListofClientData, holderForRetValue);
            ListofClientData listOfData = holderForListofClientData.value;
            for(ClientData data: listOfData.getData())
	        {
	        	System.out.print("client: " + data.getClientID());
	        	System.out.print(", pending: " + data.getPending());
	        	System.out.print(", mode: " + data.getMode());
	        	System.out.print(", last contact: " + data.getLastContact());
	        	System.out.print(", ave shed / hr: " + data.getAveShedPerHour());
	        	System.out.print(", status: " + data.getStatus());
	        	System.out.print(", mySiteURL: " + data.getMySiteURL());
	        	System.out.print(", graphURL: " + data.getGraphURL());
	        	System.out.print(", latitude: " + data.getParam1());
	        	System.out.println(", longitude: " + data.getParam2());
	        }
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
		new Demo();
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
