/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.ObjectPrintWriter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws;

import org.openadr.dras.feedback.ListOfFeedback;
import org.openadr.dras.feedback.FeedBack;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;

import java.util.List;

import org.openadr.dras.utilityprogram.UtilityProgram;
import org.openadr.dras.utilityprogram.ListOfPrograms;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.logs.ListOfDRASClientAlarms;
import org.openadr.dras.logs.DRASClientAlarm;
import org.openadr.dras.logs.ListOfTransactionLogs;
import org.openadr.dras.logs.TransactionLog;
import org.openadr.dras.drasclient.ListOfCommsStatus;
import org.openadr.dras.drasclient.CommsStatus;
import org.openadr.dras.drasclient.ListOfDRASClients;
import org.openadr.dras.drasclient.DRASClient;

import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.programconstraint.ListOfProgramConstraints;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.ListOfEventInfoType;
import org.openadr.dras.bid.ListOfBids;
import org.openadr.dras.bid.Bid;
import org.openadr.dras.bid.BidBlock;
import org.openadr.dras.responseschedule.ResponseSchedule;
import org.openadr.dras.optoutstate.ListOfOptOutStates;
import org.openadr.dras.optoutstate.OptOutState;

/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 20, 2008
 * Time: 4:11:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObjectPrintWriter
{
    
    /**
     * Prints the list of feedback.
     * 
     * @param listOfFeedback the list of feedback
     * 
     * @return the string
     */
    static public String printListOfFeedback(ListOfFeedback listOfFeedback)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(listOfFeedback != null && listOfFeedback.getFeedback().size() > 0)
        {
            for(FeedBack feedback: listOfFeedback.getFeedback())
            {
                builder.append(printFeedback(feedback));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the feedback.
     * 
     * @param feedback the feedback
     * 
     * @return the string
     */
    static public String printFeedback(FeedBack feedback)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" FeedBack.getDRASClientID:" + feedback.getDRASClientID());
        builder.append(" FeedBack.getEventID:" + feedback.getEventID());
        builder.append(" FeedBack.getParticipantID:" + feedback.getParticipantID());
        builder.append(" FeedBack.getProgramName:" + feedback.getProgramName());
        builder.append(" FeedBack.getTimeStamp:" + feedback.getTimeStamp());
        builder.append(" FeedBack.getFeedback().getName:" + feedback.getFeedback().getName());
        builder.append(" FeedBack.getFeedback().getValue:" + feedback.getFeedback().getValue());
        builder.append(" FeedBack.getSchemaVersion:" + feedback.getSchemaVersion());
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the participant account.
     * 
     * @param pa the pa
     * 
     * @return the string
     */
    static public String printParticipantAccount(ParticipantAccount pa)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" ParticipantAccount.getAccountID:" + pa.getAccountID());
        builder.append(" ParticipantAccount.getUserName:" + pa.getUserName());
        //builder.append(" ParticipantAccount.getPassword:" + pa.getPassword());
        if(pa.getPrograms() != null && pa.getPrograms().getProgram() != null)
        {
            for(int i=0; i< pa.getPrograms().getProgram().size(); i++)
            {
                builder.append(" ParticipantAccount.getProgram: " + pa.getPrograms().getProgram().get(i).getProgramName());
            }
        }
        if(pa.getContactInformation() != null && pa.getContactInformation().getVoiceNumbers() != null &&
                pa.getContactInformation().getVoiceNumbers().getNumber() != null)
        {
            builder.append(" ParticipantAccount.getAccountID:" + pa.getContactInformation().getVoiceNumbers().getNumber().get(0));
        }
        if(pa.getContactInformation() != null && pa.getContactInformation().getFaxNumbers() != null &&
                pa.getContactInformation().getFaxNumbers().getNumber() != null)
        {
            builder.append(" ParticipantAccount.getAccountID:" + pa.getContactInformation().getFaxNumbers().getNumber().get(0));
        }
        if(pa.getContactInformation() != null && pa.getContactInformation().getPagerNumbers() != null &&
                pa.getContactInformation().getPagerNumbers().getNumber() != null)
        {
            builder.append(" ParticipantAccount.getAccountID:" + pa.getContactInformation().getPagerNumbers().getNumber().get(0));
        }
        if(pa.getContactInformation() != null && pa.getContactInformation().getEmailAddresses() != null &&
                pa.getContactInformation().getEmailAddresses().getAddress() != null)
        {
            builder.append(" ParticipantAccount.getAccountID:" + pa.getContactInformation().getEmailAddresses().getAddress().get(0));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the participant accounts.
     * 
     * @param pas the pas
     * 
     * @return the string
     */
    static public String printParticipantAccounts(ListOfParticipantAccounts pas)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(pas != null && pas.getParticipantAccount() != null && pas.getParticipantAccount().size() > 0)
        {
            List<ParticipantAccount> list = pas.getParticipantAccount();
            for (ParticipantAccount aList : list)
            {
                builder.append(printParticipantAccount(aList));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the program.
     * 
     * @param pa the pa
     * 
     * @return the string
     */
    static public String printProgram(UtilityProgram pa)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" pa.getName:" + pa.getName());
        builder.append(" pa.getPriority:" + pa.getPriority().intValue());

        if(pa.getParticipants() != null && pa.getParticipants().getAccounts() != null &&
                pa.getParticipants().getAccounts().getParticipantID() != null)
        {
            for(int i=0; i< pa.getParticipants().getAccounts().getParticipantID().size(); i++)
            {
                builder.append(" UtilityProgram.getParticipant : " + pa.getParticipants().getAccounts().getParticipantID().get(i));
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the programs.
     * 
     * @param pas the pas
     * 
     * @return the string
     */
    static public String printPrograms(ListOfPrograms pas)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(pas != null && pas.getProgram() != null && pas.getProgram().size() > 0)
        {
            List<UtilityProgram> list = pas.getProgram();
            for (UtilityProgram aList : list)
            {
                builder.append(printProgram(aList));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of dras client alarms.
     * 
     * @param listOfAlarms the list of alarms
     * 
     * @return the string
     */
    static public String printListOfDRASClientAlarms(ListOfDRASClientAlarms listOfAlarms)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(listOfAlarms != null && listOfAlarms.getDrasClientAlarm().size() > 0)
        {
            for(DRASClientAlarm alarm: listOfAlarms.getDrasClientAlarm())
            {
                builder.append(printDRASClientAlarm(alarm));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the dras client alarm.
     * 
     * @param alarm the alarm
     * 
     * @return the string
     */
    static public String printDRASClientAlarm(DRASClientAlarm alarm)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" DRASClientAlarm.getDRASClientID:" + alarm.getDRASClientID());
        builder.append(" DRASClientAlarm.getDescription:" + alarm.getDescription());
        builder.append(" DRASClientAlarm.getStatus:" + alarm.getStatus());
        builder.append(" DRASClientAlarm.getTimeStamp:" + alarm.getTimeStamp());
        builder.append(" DRASClientAlarm.getSchemaVersion:" + alarm.getSchemaVersion());
        builder.append("]");
        return builder.toString();
    }


    /**
     * Prints the list of comms status.
     * 
     * @param listCommsStatus the list comms status
     * 
     * @return the string
     */
    static public String printListOfCommsStatus(ListOfCommsStatus listCommsStatus)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(listCommsStatus != null && listCommsStatus.getStatus() != null)
        {
            List<CommsStatus> list = listCommsStatus.getStatus();
            for(CommsStatus commsStatus: list)
            {
                builder.append(" commsStatus.getDRASClientID:" + commsStatus.getDRASClientID());
                builder.append(" commsStatus.getStatus:" + commsStatus.getStatus());
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the transaction logs.
     * 
     * @param logs the logs
     * 
     * @return the string
     */
    static public String printTransactionLogs(ListOfTransactionLogs logs)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(logs != null && logs.getTransactionLog() != null)
        {
            List<TransactionLog> list = logs.getTransactionLog();
            for(TransactionLog log: list)
            {
                builder.append(printTransactionLog(log));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the transaction log.
     * 
     * @param log the log
     * 
     * @return the string
     */
    static public String printTransactionLog(TransactionLog log)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(log == null)
        {
            builder.append("]");
            return builder.toString();
        }
        builder.append(" log.getDescription:" + log.getDescription());
        //builder.append(" log.getResult:" + log.getResult());
        builder.append(" log.getRole:" + log.getRole());
        builder.append(" log.getTimeStamp:" + log.getTimeStamp().toString());
        builder.append(" log.getUserName:" + log.getUserName());
        //builder.append(" log.getTransactionName:" + log.getTransactionName());
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of program constraints.
     * 
     * @param constraints the constraints
     * 
     * @return the string
     */
    static public String printListOfProgramConstraints(ListOfProgramConstraints constraints)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(constraints == null && constraints.getProgramConstraint() != null && constraints.getProgramConstraint().size() > 0)
        {
            builder.append(" Return 0 result;");
            builder.append("]");
            return builder.toString();
        }
        for(ProgramConstraint constraint: constraints.getProgramConstraint())
        {
            builder.append(printProgramConstraint(constraint));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the program constraint.
     * 
     * @param constraint the constraint
     * 
     * @return the string
     */
    static public String printProgramConstraint(ProgramConstraint constraint)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(constraint == null)
        {
            builder.append("]");
            return builder.toString();
        }
        builder.append(" constraint.getProgramName:" + constraint.getProgramName());
        builder.append(" constraint.getMaxEventDuration:" + constraint.getMaxEventDuration());
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of utility dr events.
     * 
     * @param events the events
     * 
     * @return the string
     */
    static public String printListOfUtilityDREvents(ListOfUtilityDREvents events)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(events == null && events.getDREvent() != null && events.getDREvent().size() > 0)
        {

            builder.append(" Return 0 result;");
            builder.append("]");
            return builder.toString();
        }
        for(UtilityDREvent event: events.getDREvent())
        {
            builder.append(printUtilityDREvent(event));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the utility dr event.
     * 
     * @param event the event
     * 
     * @return the string
     */
    static public String printUtilityDREvent(UtilityDREvent event)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(event == null)
        {
            builder.append("]");
            return builder.toString();
        }
        builder.append(" event.getProgramName:" + event.getProgramName());
        builder.append(" event.getEventIdentifier:" + event.getEventIdentifier());
        if(event.getEventTiming() != null)
        {
        	if(event.getEventTiming().getStartTime() != null)
        	{
        		builder.append(" event.getEventTiming().getStartTime():" + 
        			event.getEventTiming().getStartTime().toString());
        	}
        	else
        	{
        		builder.append(" event.getEventTiming().getStartTime(): null");
        	}
        	if(event.getEventTiming().getEndTime() != null)
        	{
        		builder.append(" event.getEventTiming().getEndTime():" + 
        			event.getEventTiming().getEndTime().toString());
        	}
        	else
        	{
        		builder.append(" event.getEventTiming().getEndTime(): null");
        	}
        }
        else
        {
        	builder.append(" event.getEventTiming(): null");
        }
        // TODO: this should be pushed down to the dbp program beans
//        builder.append(" event.getBiddingInformation().getClosingTime():" + event.getBiddingInformation().getClosingTime().toString());
//        builder.append(" event.getBiddingInformation().getOpeningTime():" + event.getBiddingInformation().getOpeningTime().toString());
        builder.append(" event.getUtilityITName:" + event.getUtilityITName());
        builder.append(" event.getSchemaVersion:" + event.getSchemaVersion());
        builder.append(" event.getEventModNumber:" + event.getEventModNumber());
        builder.append(printEventInformation(event.getEventInformation()));
        builder.append("]");
        return builder.toString();

    }

    /**
     * Prints the event information.
     * 
     * @param eventInfo the event info
     * 
     * @return the string
     */
    static public String printEventInformation(UtilityDREvent.EventInformation eventInfo)
    {
	    StringBuilder builder = new StringBuilder();
    	if(eventInfo != null)
    	{
	        builder.append("[");
	        for(EventInfoInstance eventInfoInst : eventInfo.getEventInfoInstance())
	        {
	            builder.append(printEventInformation(eventInfoInst));
	        }
	        builder.append("]");
    	}
	    return builder.toString();
    }

    /**
     * Prints the event information.
     * 
     * @param eventInfo the event info
     * 
     * @return the string
     */
    static public String printEventInformation(EventInfoInstance eventInfo)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(eventInfo == null)
        {
            builder.append("]");
            return builder.toString();
        }
        builder.append(" eventInfo.getEndTime:" + eventInfo.getEndTime());
        builder.append(" eventInfo.getEventInfoTypeName:" + eventInfo.getEventInfoTypeName());
        builder.append(" eventInfo.getSchemaVersion:" + eventInfo.getSchemaVersion());
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of bids.
     * 
     * @param bids the bids
     * 
     * @return the string
     */
    static public String printListOfBids(ListOfBids bids)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(bids == null &&  bids.getBids().size() > 0)
        {
            builder.append(" Return 0 result;");
            builder.append("]");
            return builder.toString();
        }

        for(Bid event: bids.getBids())
        {
            builder.append(printBid(event));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the bid.
     * 
     * @param bid the bid
     * 
     * @return the string
     */
    static public String printBid(Bid bid)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(bid == null)
        {
            builder.append("]");
            return builder.toString();
        }
        builder.append(" bid.getProgramName:" + bid.getProgramName());
        builder.append(" bid.getEventID:" + bid.getEventID());
        builder.append(" bid.getOptions:" + bid.getOptions());
        builder.append(" bid.getParticipantAccount:" + bid.getParticipantAccount());
        builder.append(" bid.getSignature:" + bid.getSignature());
        Bid.BidBlocks bidBlocks = bid.getBidBlocks();
        if(bidBlocks != null && bidBlocks.getBlock() != null)
        {
            for(BidBlock block : bidBlocks.getBlock())
            {
                builder.append(" block.getDuration:" + block.getDuration());
                builder.append(" block.getLoad:" + block.getLoad());
                builder.append(" block.getPrice:" + block.getPrice());
                builder.append(" block.getTimePeriod().getStartTime():" + block.getTimePeriod().getStartTime());
                builder.append(" block.getTimePeriod().getEndTime():" + block.getTimePeriod().getEndTime());
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of i ds.
     * 
     * @param ids the ids
     * 
     * @return the string
     */
    static public String printListOfIDs(ListOfIDs ids)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(ids != null && ids.getIdentifier().size() > 0)
        {
            for(String id: ids.getIdentifier())
            {
                builder.append(" ID:" + id);
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of event info type.
     * 
     * @param list the list
     * 
     * @return the string
     */
    static public String printListOfEventInfoType(ListOfEventInfoType list)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(list != null && list.getEventInfoType() != null )
        {
            //TODO lin add imple
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the response schedule.
     * 
     * @param responseSchedule the response schedule
     * 
     * @return the string
     */
    static public String printResponseSchedule(ResponseSchedule responseSchedule)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" responseSchedule.getDRASClientID:" + responseSchedule.getDRASClientID());
        builder.append(" responseSchedule.getIdentifier:" + responseSchedule.getIdentifier());
        builder.append(" responseSchedule.getNearTransitionTime:" + responseSchedule.getNearTransitionTime());
        builder.append(" responseSchedule.getProgramName:" + responseSchedule.getProgramName());
        builder.append(" responseSchedule.getSchemaVersion:" + responseSchedule.getSchemaVersion());
        //ToDO lin: print operation states
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of dras clients.
     * 
     * @param list the list
     * 
     * @return the string
     */
    static public String printListOfDRASClients(ListOfDRASClients list)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(list != null && list.getDrasClient() != null )
        {
            for(DRASClient client: list.getDrasClient())
            {
                builder.append(printDRASClient(client));
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the dras client.
     * 
     * @param client the client
     * 
     * @return the string
     */
    static public String printDRASClient(DRASClient client)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" client.getClientType:" + client.getClientType());
        builder.append(" client.getIdentifier:" + client.getIdentifier());
        builder.append(" client.getParticipantID:" + client.getParticipantID());
        builder.append(" client.getSchemaVersion:" + client.getSchemaVersion());
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the list of opt out states.
     * 
     * @param list the list
     * 
     * @return the string
     */
    static public String printListOfOptOutStates(ListOfOptOutStates list)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if(list != null && list.getOptOutState() != null )
        {
            List<OptOutState> opts = list.getOptOutState();
            for(OptOutState opt: opts)
            {
                builder.append(printOptOutState(opt));    
            }
        }
        else
        {
            builder.append(" Return 0 result;");
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Prints the opt out state.
     * 
     * @param opt the opt
     * 
     * @return the string
     */
    static public String printOptOutState(OptOutState opt)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(" opt.getEventID:" + opt.getEventID());
            builder.append(" opt.getIdentifier:" + opt.getIdentifier());
            builder.append(" opt.getParticipantID:" + opt.getParticipantID());
            builder.append(" opt.getProgramName:" + opt.getProgramName());
            builder.append(" opt.getSchedule().getTimeElement:" + opt.getSchedule().getTimeElement());
            for(String drasID: opt.getDRASClients().getDRASClientID())
            {
                builder.append(" opt.getDRASClients.drasID:" + drasID);
            }
        builder.append("]");
        return builder.toString();
    }


    /**
     * Prints the object.
     * 
     * @param obj the obj
     * 
     * @return the string
     */
    static public String printObject(Object obj)
    {
        if(obj instanceof ListOfParticipantAccounts)
        {
            return printParticipantAccounts((ListOfParticipantAccounts) obj);
        }
        else if(obj instanceof ListOfDRASClients)
        {
            return printListOfDRASClients((ListOfDRASClients) obj);
        }
        else if(obj instanceof ListOfProgramConstraints)
        {
            return printListOfProgramConstraints((ListOfProgramConstraints) obj);
        }
        else if(obj instanceof ResponseSchedule)
        {
            return printResponseSchedule((ResponseSchedule) obj);
        }
        else if(obj instanceof Bid)
        {
            return printBid((Bid) obj);
        }
        else if(obj instanceof ListOfOptOutStates)
        {
            return printListOfOptOutStates((ListOfOptOutStates) obj);
        }
        else if(obj instanceof ListOfCommsStatus)
        {
            return printListOfCommsStatus((ListOfCommsStatus) obj);
        }
        else if(obj instanceof ListOfTransactionLogs)
        {
            return printTransactionLogs((ListOfTransactionLogs) obj);
        }
        else if(obj instanceof ListOfDRASClientAlarms)
        {
            return printListOfDRASClientAlarms((ListOfDRASClientAlarms) obj);
        }
        else if(obj instanceof ListOfDRASClientAlarms)
        {
            return printListOfDRASClientAlarms((ListOfDRASClientAlarms) obj);
        }
        else if(obj instanceof ListOfFeedback)
        {
            return printListOfFeedback((ListOfFeedback) obj);
        }
        else if(obj != null)
        {
            return obj.toString();
        }
        else
        {
            return "";
        }
    }
}
