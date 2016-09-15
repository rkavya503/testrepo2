/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.PrintTool.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

import org.openadr.dras.bid.Bid;
import org.openadr.dras.bid.BidBlock;
import org.openadr.dras.bid.ListOfBids;
import org.openadr.dras.drasclient.CommsStatus;
import org.openadr.dras.drasclient.DRASClient;
import org.openadr.dras.drasclient.ListOfCommsStatus;
import org.openadr.dras.drasclient.ListOfDRASClients;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.ListOfEventInfoType;
import org.openadr.dras.feedback.FeedBack;
import org.openadr.dras.feedback.ListOfFeedback;
import org.openadr.dras.logs.DRASClientAlarm;
import org.openadr.dras.logs.ListOfDRASClientAlarms;
import org.openadr.dras.logs.ListOfTransactionLogs;
import org.openadr.dras.logs.TransactionLog;
import org.openadr.dras.optoutstate.ListOfOptOutStates;
import org.openadr.dras.optoutstate.OptOutState;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.programconstraint.ListOfProgramConstraints;
import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.responseschedule.ResponseSchedule;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfPrograms;
import org.openadr.dras.utilityprogram.UtilityProgram;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * The Class PrintTool.
 */
public class PrintTool
{
    
    /**
     * Prints the list of feedback.
     * 
     * @param listOfFeedback the list of feedback
     */
    static public void printListOfFeedback(ListOfFeedback listOfFeedback)
    {
        if(listOfFeedback != null && listOfFeedback.getFeedback().size() > 0)
        {
            for(FeedBack feedback: listOfFeedback.getFeedback())
            {
                printFeedback(feedback);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the feedback.
     * 
     * @param feedback the feedback
     */
    static public void printFeedback(FeedBack feedback)
    {
        System.out.println("FeedBack.getDRASClientID :" + feedback.getDRASClientID());
        System.out.println("FeedBack.getEventID :" + feedback.getEventID());
        System.out.println("FeedBack.getParticipantID :" + feedback.getParticipantID());
        System.out.println("FeedBack.getProgramName :" + feedback.getProgramName());
        System.out.println("FeedBack.getTimeStamp :" + feedback.getTimeStamp());
        System.out.println("FeedBack.getFeedback().getName :" + feedback.getFeedback().getName());
        System.out.println("FeedBack.getFeedback().getValue :" + feedback.getFeedback().getValue());
        System.out.println("FeedBack.getSchemaVersion :" + feedback.getSchemaVersion());
    }

    /**
     * Prints the participant account.
     * 
     * @param pa the pa
     */
    static public void printParticipantAccount(ParticipantAccount pa)
    {
        System.out.println("ParticipantAccount.getAccountID :"
			+ pa.getAccountID());
		System.out.println("ParticipantAccount.getUserName :"
			+ pa.getUserName());
		//System.out.println("ParticipantAccount.getPassword :"
		//	+ pa.getPassword());
		if (pa.getPrograms() != null && pa.getPrograms().getProgram() != null)
		{
			for (int i = 0; i < pa.getPrograms().getProgram().size(); i++)
			{
				System.out.println("ParticipantAccount.getProgram : "
					+ pa.getPrograms().getProgram().get(i).getProgramName());
			}
		}
		if (pa.getContactInformation() != null
			&& pa.getContactInformation().getVoiceNumbers() != null
			&& pa.getContactInformation().getVoiceNumbers().getNumber() != null
            && pa.getContactInformation().getVoiceNumbers().getNumber().size() > 0)
		{
			System.out.println("ParticipantAccount.getVoiceNumbers :"
				+ pa.getContactInformation().getVoiceNumbers().getNumber().get(
					0));
		}
		if (pa.getContactInformation() != null
			&& pa.getContactInformation().getFaxNumbers() != null
			&& pa.getContactInformation().getFaxNumbers().getNumber() != null
            && pa.getContactInformation().getFaxNumbers().getNumber().size() > 0)
		{
			System.out
				.println("ParticipantAccount.getFaxNumbers :"
					+ pa.getContactInformation().getFaxNumbers().getNumber()
						.get(0));
		}
		if (pa.getContactInformation() != null
			&& pa.getContactInformation().getPagerNumbers() != null
			&& pa.getContactInformation().getPagerNumbers().getNumber() != null
            && pa.getContactInformation().getPagerNumbers().getNumber().size() > 0)
		{
			System.out.println("ParticipantAccount.getPagerNumbers :"
				+ pa.getContactInformation().getPagerNumbers().getNumber().get(
					0));
		}
		if (pa.getContactInformation() != null
			&& pa.getContactInformation().getEmailAddresses() != null
			&& pa.getContactInformation().getEmailAddresses().getAddress() != null
            && pa.getContactInformation().getEmailAddresses().getAddress().size() > 0)
		{
			System.out.println("ParticipantAccount.getEmailAddresses :"
				+ pa.getContactInformation().getEmailAddresses().getAddress()
					.get(0));
		}
		if (pa.getDRASClients() != null
			&& pa.getDRASClients().getClientID() != null)
		{
			for (int i = 0; i < pa.getDRASClients().getClientID().size(); i++)
			{
				System.out.println("ParticipantAccount.getDRASClient : "
					+ pa.getDRASClients().getClientID().get(i));
			}
		}
    }

    /**
     * Prints the participant accounts.
     * 
     * @param pas the pas
     */
    static public void printParticipantAccounts(ListOfParticipantAccounts pas)
    {
        if(pas != null && pas.getParticipantAccount() != null && pas.getParticipantAccount().size() > 0)
        {
            List<ParticipantAccount> list = pas.getParticipantAccount();
            for (ParticipantAccount aList : list)
            {
                printParticipantAccount(aList);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }

    }

    /**
     * Prints the program.
     * 
     * @param pa the pa
     */
    static public void printProgram(UtilityProgram pa)
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
    static public void printPrograms(ListOfPrograms pas)
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
     * Prints the list of dras client alarms.
     * 
     * @param listOfAlarms the list of alarms
     */
    static public void printListOfDRASClientAlarms(ListOfDRASClientAlarms listOfAlarms)
    {
        if(listOfAlarms != null && listOfAlarms.getDrasClientAlarm().size() > 0)
        {
            for(DRASClientAlarm alarm: listOfAlarms.getDrasClientAlarm())
            {
                printDRASClientAlarm(alarm);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the dras client alarm.
     * 
     * @param alarm the alarm
     */
    static public void printDRASClientAlarm(DRASClientAlarm alarm)
    {
        System.out.println("DRASClientAlarm.getDRASClientID :" + alarm.getDRASClientID());
        System.out.println("DRASClientAlarm.getDescription :" + alarm.getDescription());
        System.out.println("DRASClientAlarm.getStatus :" + alarm.getStatus());
        System.out.println("DRASClientAlarm.getTimeStamp :" + alarm.getTimeStamp());
        System.out.println("DRASClientAlarm.getSchemaVersion :" + alarm.getSchemaVersion());
    }


    /**
     * Prints the list of comms status.
     * 
     * @param listCommsStatus the list comms status
     */
    static public void printListOfCommsStatus(ListOfCommsStatus listCommsStatus)
    {
        if(listCommsStatus != null && listCommsStatus.getStatus() != null)
        {
            List<CommsStatus> list = listCommsStatus.getStatus();
            for(CommsStatus commsStatus: list)
            {
                System.out.println("commsStatus.getDRASClientID :" + commsStatus.getDRASClientID());
                System.out.println("commsStatus.getStatus :" + commsStatus.getStatus());
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the transaction logs.
     * 
     * @param logs the logs
     */
    static public void printTransactionLogs(ListOfTransactionLogs logs)
    {
        if(logs != null && logs.getTransactionLog() != null)
        {
            List<TransactionLog> list = logs.getTransactionLog();
            for(TransactionLog log: list)
            {
                printTransactionLog(log);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the transaction log.
     * 
     * @param log the log
     */
    static public void printTransactionLog(TransactionLog log)
    {
        if(log == null) return;
        System.out.println("log.getDescription :" + log.getDescription());
        System.out.println("log.getRole :" + log.getRole());
        System.out.println("log.getTimeStamp :" + log.getTimeStamp().toString());
        System.out.println("log.getUserName :" + log.getUserName());

    }

    /**
     * Conver date to xml gregorian calendar.
     * 
     * @param date the date
     * 
     * @return the xML gregorian calendar
     */
    static public XMLGregorianCalendar converDateToXMLGregorianCalendar(Date date)
    {
        try
        {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            return xmlCal;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * Prints the list of program constraints.
     * 
     * @param constraints the constraints
     */
    static public void printListOfProgramConstraints(ListOfProgramConstraints constraints)
    {
        if(constraints == null && constraints.getProgramConstraint() != null && constraints.getProgramConstraint().size() > 0)
        {
            System.out.println("Return 0 result;");
            return;
        }
        for(ProgramConstraint constraint: constraints.getProgramConstraint())
        {
            printProgramConstraint(constraint);
        }
    }

    /**
     * Prints the program constraint.
     * 
     * @param constraint the constraint
     */
    static public void printProgramConstraint(ProgramConstraint constraint)
    {
        if(constraint == null) return;
        System.out.println("constraint.getProgramName :" + constraint.getProgramName());
        System.out.println("constraint.getMaxEventDuration :" + constraint.getMaxEventDuration());
    }

    /**
     * Prints the list of utility dr events.
     * 
     * @param events the events
     */
    static public void printListOfUtilityDREvents(ListOfUtilityDREvents events)
    {
        if(events == null && events.getDREvent() != null && events.getDREvent().size() > 0)
        {

            System.out.println("Return 0 result;");
            return;
        }
        for(UtilityDREvent event: events.getDREvent())
        {
            printUtilityDREvent(event);
        }
    }

    /**
     * Prints the utility dr event.
     * 
     * @param event the event
     */
    static public void printUtilityDREvent(UtilityDREvent event)
    {
        if(event == null) return;
        System.out.println("event.getProgramName :" + event.getProgramName());
        System.out.println("event.getEventIdentifier :" + event.getEventIdentifier());
        if(event.getEventTiming()!=null && event.getEventTiming().getStartTime()!=null) System.out.println("event.getEventTiming().getStartTime() :" + event.getEventTiming().getStartTime().toString());
        if(event.getEventTiming()!=null && event.getEventTiming().getEndTime()!=null) System.out.println("event.getEventTiming().getEndTime() :" + event.getEventTiming().getEndTime().toString());
        if(event.getBiddingInformation()!=null && event.getBiddingInformation().getClosingTime()!=null) System.out.println("event.getBiddingInformation().getClosingTime() :" + event.getBiddingInformation().getClosingTime().toString());
        if(event.getBiddingInformation()!=null && event.getBiddingInformation().getOpeningTime()!=null) System.out.println("event.getBiddingInformation().getOpeningTime() :" + event.getBiddingInformation().getOpeningTime().toString());
        System.out.println("event.getUtilityITName :" + event.getUtilityITName());
        System.out.println("event.getSchemaVersion :" + event.getSchemaVersion());
        System.out.println("event.getEventModNumber :" + event.getEventModNumber());
        printEventInformation(event.getEventInformation());

    }

    /**
     * Prints the event information.
     * 
     * @param eventInfo the event info
     */
    static public void printEventInformation(UtilityDREvent.EventInformation eventInfo)
    {
        if(eventInfo != null)
        {
            List<EventInfoInstance> eventInfos =  eventInfo.getEventInfoInstance();

            for(EventInfoInstance eventInfoInst : eventInfos)
            {
                printEventInformation(eventInfoInst);
            }
        }
    }

    /**
     * Prints the event information.
     * 
     * @param eventInfo the event info
     */
    static public void printEventInformation(EventInfoInstance eventInfo)
    {
        if(eventInfo == null) return;
        System.out.println("eventInfo.getEndTime :" + eventInfo.getEndTime());
        System.out.println("eventInfo.getEventInfoTypeName :" + eventInfo.getEventInfoTypeName());
        System.out.println("eventInfo.getSchemaVersion :" + eventInfo.getSchemaVersion());
    }

    /**
     * Prints the list of bids.
     * 
     * @param bids the bids
     */
    static public void printListOfBids(ListOfBids bids)
    {
        if(bids == null &&  bids.getBids().size() > 0)
        {
            System.out.println("Return 0 result;");
            return;
        }

        for(Bid event: bids.getBids())
        {
            printBid(event);
        }
    }

    /**
     * Prints the bid.
     * 
     * @param bid the bid
     */
    static public void printBid(Bid bid)
    {
        if(bid == null) return;
        System.out.println("bid.getProgramName :" + bid.getProgramName());
        System.out.println("bid.getEventID :" + bid.getEventID());
        System.out.println("bid.getOptions :" + bid.getOptions());
        System.out.println("bid.getParticipantAccount :" + bid.getParticipantAccount());
        System.out.println("bid.getSignature :" + bid.getSignature());
        Bid.BidBlocks bidBlocks = bid.getBidBlocks();
        if(bidBlocks != null && bidBlocks.getBlock() != null)
        {
            for(BidBlock block : bidBlocks.getBlock())
            {
                System.out.println("block.getDuration :" + block.getDuration());
                System.out.println("block.getLoad :" + block.getLoad());
                System.out.println("block.getPrice :" + block.getPrice());
                System.out.println("block.getTimePeriod().getStartTime() :" + block.getTimePeriod().getStartTime());
                System.out.println("block.getTimePeriod().getEndTime() :" + block.getTimePeriod().getEndTime());
            }
        }
    }

    /**
     * Prints the list of i ds.
     * 
     * @param ids the ids
     */
    static public void printListOfIDs(ListOfIDs ids)
    {
        if(ids != null && ids.getIdentifier().size() > 0)
        {
            for(String id: ids.getIdentifier())
            {
                System.out.println("ID :" + id);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the list of event info type.
     * 
     * @param list the list
     */
    static public void printListOfEventInfoType(ListOfEventInfoType list)
    {
        if(list != null && list.getEventInfoType() != null )
        {

        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the response schedule.
     * 
     * @param responseSchedule the response schedule
     */
    static public void printResponseSchedule(ResponseSchedule responseSchedule)
    {
        if(responseSchedule == null) return;
        System.out.println("responseSchedule.getDRASClientID :" + responseSchedule.getDRASClientID());
        System.out.println("responseSchedule.getIdentifier :" + responseSchedule.getIdentifier());
        System.out.println("responseSchedule.getNearTransitionTime :" + responseSchedule.getNearTransitionTime());
        System.out.println("responseSchedule.getProgramName :" + responseSchedule.getProgramName());
        System.out.println("responseSchedule.getSchemaVersion :" + responseSchedule.getSchemaVersion());
    }

    /**
     * Prints the list of dras clients.
     * 
     * @param list the list
     */
    static public void printListOfDRASClients(ListOfDRASClients list)
    {
        if(list != null && list.getDrasClient() != null )
        {
            for(DRASClient client: list.getDrasClient())
            {
                printDRASClient(client);
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the dras client.
     * 
     * @param client the client
     */
    static public void printDRASClient(DRASClient client)
    {
        System.out.println("client.getClientType :" + client.getClientType());
        System.out.println("client.getIdentifier :" + client.getIdentifier());
        System.out.println("client.getParticipantID :" + client.getParticipantID());
        System.out.println("client.getSchemaVersion :" + client.getSchemaVersion());
        if(client.getLocationInformation() != null)
        {
	        System.out.println("client.getAddress :" + 
	        	client.getLocationInformation().getAddress());
	        System.out.println("client.getGridLocation :" + 
	        	client.getLocationInformation().getGridLocation());
	        if(client.getLocationInformation().getCoordinate() != null)
	        {
		        System.out.println("client.getLatitude :" + 
		        	client.getLocationInformation().getCoordinate().getLatitude());
		        System.out.println("client.getLongitude :" + 
		        	client.getLocationInformation().getCoordinate().getLongitude());
	        }
	        else
	        {
	        	System.out.println("client.getCoordinate : null");
	        }
        }
        else
        {
 	        System.out.println("client.getLocationInformation : null");       	
        }
    }

    /**
     * Prints the list of opt out states.
     * 
     * @param list the list
     */
    static public void printListOfOptOutStates(ListOfOptOutStates list)
    {
        if(list != null && list.getOptOutState() != null )
        {
            List<OptOutState> opts = list.getOptOutState();
            for(OptOutState opt: opts)
            {
                printOptOutState(opt);    
            }
        }
        else
        {
            System.out.println("Return 0 result;");
        }
    }

    /**
     * Prints the opt out state.
     * 
     * @param opt the opt
     */
    static public void printOptOutState(OptOutState opt)
    {
        System.out.println("opt.getEventID :" + opt.getEventID());
        System.out.println("opt.getIdentifier :" + opt.getIdentifier());
        System.out.println("opt.getParticipantID :" + opt.getParticipantID());
        System.out.println("opt.getProgramName :" + opt.getProgramName());
        System.out.println("opt.getSchedule().getTimeElement :" + opt.getSchedule().getTimeElement());
            for(String drasID: opt.getDRASClients().getDRASClientID())
            {
                System.out.println("opt.getDRASClients.drasID :" + drasID);
            }
    }

}
