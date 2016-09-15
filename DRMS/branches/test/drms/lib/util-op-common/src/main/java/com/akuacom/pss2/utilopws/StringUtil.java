/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.StringUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws;


import com.akuacom.pss2.core.ErrorResourceUtil;

import java.util.List;
import java.util.ArrayList;

import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.UtilityProgram;
import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.programconstraint.DateTimeWindow;
import org.openadr.dras.drasclient.DRASClient;
import org.openadr.dras.feedback.FeedBack;
import org.openadr.dras.responseschedule.ResponseSchedule;
import org.openadr.dras.bid.Bid;
import org.openadr.dras.optoutstate.OptOutState;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.ListOfEventIDs;


/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 22, 2008
 * Time: 11:24:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil
{
    
    /**
     * Creates the long desc.
     * 
     * @param list the list
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccountIDs list, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param list the list
     * @param programName the program name
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccountIDs list, String programName, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();

        params.add(list.getParticipantAccountID().toString());
        params.add(programName);
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param list the list
     * @param programConstraint the program constraint
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccountIDs list, ProgramConstraint programConstraint, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());
        params.add(ObjectPrintWriter.printProgramConstraint(programConstraint));
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param list the list
     * @param programConstraint the program constraint
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs, ListOfParticipantAccountIDs list, ProgramConstraint programConstraint, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());
        params.add(ObjectPrintWriter.printProgramConstraint(programConstraint));
        params.add(participantGroup);
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param list the list
     * @param programName the program name
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs, ListOfParticipantAccountIDs list, String programName, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());
        params.add(programName);
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param list the list
     * @param timeInterval the time interval
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs, ListOfIDs list, DateTimeWindow timeInterval, String template)
    {
        List<String> params = new ArrayList<String>();
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        if(list == null || list.getIdentifier() == null)
            list = new ListOfIDs();
        params.add(list.getIdentifier().toString());
        params.add(timeInterval.getStartDateTime() != null ? timeInterval.getStartDateTime().toString() : "" + " to " + timeInterval.getStartDateTime() != null ? timeInterval.getEndDateTime().toString() : "");        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param list the list
     * @param programName the program name
     * @param timeInterval the time interval
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs, ListOfIDs list, String programName, DateTimeWindow timeInterval, String template)
    {
        List<String> params = new ArrayList<String>();
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        if(list == null )
            list = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        params.add(programName);
        params.add(timeInterval.getStartDateTime() != null ? timeInterval.getStartDateTime().toString() : "" + " to " + timeInterval.getStartDateTime() != null ? timeInterval.getEndDateTime().toString() : "");        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param list the list
     * @param participantGroup the participant group
     * @param partAccount the part account
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccountIDs list, String participantGroup, ParticipantAccount partAccount, String template)
    {
        List<String> params = new ArrayList<String>();
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());

        params.add(participantGroup);
        params.add(ObjectPrintWriter.printParticipantAccount(partAccount));
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param list the list
     * @param participantGroup the participant group
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs, ListOfParticipantAccountIDs list, String participantGroup, String template)
    {
        List<String> params = new ArrayList<String>();
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        if(list == null || list.getParticipantAccountID() == null)
            list = new ListOfParticipantAccountIDs();
        params.add(list.getParticipantAccountID().toString());
        params.add(participantGroup);
        params.add(participantGroup);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClient the dras client
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(DRASClient drasClient, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printDRASClient(drasClient));
        
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param feedback the feedback
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(FeedBack feedback, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printFeedback(feedback));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param bid the bid
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(Bid bid, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printBid(bid));
        
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param optOutState the opt out state
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(OptOutState optOutState, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printOptOutState(optOutState));
        
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param responseSchedule the response schedule
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ResponseSchedule responseSchedule, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printResponseSchedule(responseSchedule));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }  

    /**
     * Creates the long desc.
     * 
     * @param drasClient the dras client
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String drasClient, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(drasClient);

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param participants the participants
     * @param programNames the program names
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ParticipantList participants, ListOfProgramNames programNames, String template)
    {
        List<String> params = new ArrayList<String>();
        if(participants.getAccounts() != null && participants.getAccounts().getParticipantID() != null)
        {
            params.add(participants.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            participants = new ParticipantList();
        }
        if(programNames == null) programNames = new ListOfProgramNames();
        params.add(programNames.getProgramID().toString());
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param programNames the program names
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfProgramNames programNames, String template)
    {
        List<String> params = new ArrayList<String>();
        if(programNames == null) programNames = new ListOfProgramNames();
        params.add(programNames.getProgramID().toString());

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }


    /**
     * Creates the long desc.
     * 
     * @param drasClientID the dras client id
     * @param programName the program name
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String drasClientID, String programName, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(drasClientID);
        params.add(programName);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param value3 the value3
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String value1, String value2, String value3, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(value1);
        params.add(value2);
        params.add(value3);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param drasClientIDs the dras client i ds
     * @param participantIDs the participant i ds
     * @param programNames the program names
     * @param name the name
     * @param timeInterval the time interval
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs drasClientIDs,ListOfIDs participantIDs,ListOfIDs programNames,String name,DateTimeWindow timeInterval, String template)
    {
        List<String> params = new ArrayList<String>();
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        if(participantIDs == null || participantIDs.getIdentifier() == null)
            participantIDs = new ListOfIDs();
        params.add(participantIDs.getIdentifier().toString());
        if(programNames == null || programNames.getIdentifier() == null)
            programNames = new ListOfIDs();
        params.add(programNames.getIdentifier().toString());
        params.add(name);
        params.add(timeInterval.getStartDateTime() != null ? timeInterval.getStartDateTime().toString() : "" + " to " + timeInterval.getStartDateTime() != null ? timeInterval.getEndDateTime().toString() : "");        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    

    /**
     * Creates the long desc.
     * 
     * @param userNames the user names
     * @param drasClientIDs the dras client i ds
     * @param participantIDs the participant i ds
     * @param methodName the method name
     * @param timeInterval the time interval
     * @param resultCode the result code
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfIDs userNames,ListOfIDs drasClientIDs,ListOfIDs participantIDs,String methodName,DateTimeWindow timeInterval,String resultCode, String template)
    {
        List<String> params = new ArrayList<String>();
        if(userNames == null || userNames.getIdentifier() == null)
            userNames = new ListOfIDs();
        params.add(userNames.getIdentifier().toString());
        if(drasClientIDs == null || drasClientIDs.getIdentifier() == null)
            drasClientIDs = new ListOfIDs();
        params.add(drasClientIDs.getIdentifier().toString());
        if(participantIDs == null || participantIDs.getIdentifier() == null)
            participantIDs = new ListOfIDs();
        params.add(participantIDs.getIdentifier().toString());
        params.add(methodName);
        
        params.add(timeInterval.getStartDateTime() != null ? timeInterval.getStartDateTime().toString() : "" + " to " + timeInterval.getStartDateTime() != null ? timeInterval.getEndDateTime().toString() : "");
        params.add(resultCode);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }


    /**
     * Creates the long desc.
     * 
     * @param drasClientID the dras client id
     * @param participantID the participant id
     * @param programName the program name
     * @param optOutID the opt out id
     * @param eventID the event id
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String drasClientID, String participantID, String programName, String optOutID, String eventID, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(drasClientID);
        params.add(participantID);
        params.add(programName);
        params.add(optOutID);
        params.add(eventID);
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param event the event
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String value1, String value2, UtilityDREvent event, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(value1);
        params.add(value2);
        params.add(ObjectPrintWriter.printUtilityDREvent(event));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param event the event
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(UtilityDREvent event, String template)
    {
        List<String> params = new ArrayList<String>();
        
        params.add(ObjectPrintWriter.printUtilityDREvent(event));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param value3 the value3
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfEventIDs value1, ListOfProgramNames value2, ParticipantList value3, String template)
    {
        List<String> params = new ArrayList<String>();
        if(value1 == null || value1.getEventID() == null) value1 = new ListOfEventIDs();
        params.add(value1.getEventID().toString());
        if(value2 == null || value2.getProgramID() == null) value2 = new ListOfProgramNames();
        params.add(value2.getProgramID().toString());
        if(value3.getAccounts() != null && value3.getAccounts().getParticipantID() != null)
        {
            params.add(value3.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            value3 = new  ParticipantList();
        }

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param value3 the value3
     * @param value4 the value4
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfEventIDs value1, ListOfProgramNames value2, ParticipantList value3, ListOfIDs value4, String template)
    {
        List<String> params = new ArrayList<String>();
        if(value1!=null && value1.getEventID() != null)
        {
            params.add(value1.getEventID().toString());
        }
        else
        {
            params.add("");
            value1 = new ListOfEventIDs();
        }
        if(value2!=null && value2.getProgramID() != null)
        {
            params.add(value2.getProgramID().toString());
        }
        else
        {
            params.add("");
            value2 = new  ListOfProgramNames();
        }
       
        if(value3!=null && value3.getAccounts() != null && value3.getAccounts().getParticipantID() != null)
        {
            params.add(value3.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            value3 = new ParticipantList();
        }
        
        if(value4 != null && value4.getIdentifier() != null)
        {
            params.add(value4.getIdentifier().toString());
        }
        else
        {
            params.add("");
            value4 = new ListOfIDs();
        }

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccounts value1, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printParticipantAccounts(value1));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfParticipantAccountIDs value1, String template)
    {
        List<String> params = new ArrayList<String>();
        if(value1 == null || value1.getParticipantAccountID() == null)
            value1 = new ListOfParticipantAccountIDs();
        params.add(value1.getParticipantAccountID().toString());

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ParticipantList value1, String value2, String template)
    {
        List<String> params = new ArrayList<String>();
        if(value1!=null && value1.getAccounts() != null && value1.getAccounts().getParticipantID() != null)
        {
            params.add(value1.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            value1 = new ParticipantList();
        }
        params.add(value2);

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(UtilityProgram value1, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printProgram(value1));

        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(ListOfProgramNames value1, ParticipantList value2, String template)
    {
        List<String> params = new ArrayList<String>();
        
        if(value1!=null && value1.getProgramID() != null )
        {
            params.add(value1.getProgramID().toString());
        }
        else
        {
            params.add("");
            value1 = new ListOfProgramNames();
        }
        if(value2!=null && value2.getAccounts() != null && value2.getAccounts().getParticipantID() != null)
        {
            params.add(value2.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            value2 = new ParticipantList();
        }
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String value1, ParticipantList value2, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(value1);
        if(value2!=null && value2.getAccounts() != null && value2.getAccounts().getParticipantID() != null)
        {
            params.add(value2.getAccounts().getParticipantID().toString());
        }
        else
        {
            params.add("");
            value2 = new ParticipantList();
        }
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param value1 the value1
     * @param value2 the value2
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String value1, ProgramConstraint value2, String template)
    {
        List<String> params = new ArrayList<String>();
        params.add(value1);
        params.add(ObjectPrintWriter.printProgramConstraint(value2));
        String message = ErrorResourceUtil.getErrorMessage(template, params);
        return message;
    }

    /**
     * Creates the long desc.
     * 
     * @param template the template
     * 
     * @return the string
     */
    static public String createLongDesc(String template)
    {

        String message = ErrorResourceUtil.getErrorMessage(template);
        return message;
    }

    /**
     * Append long desc.
     * 
     * @param input the input
     * @param result the result
     * @param retCode the ret code
     * 
     * @return the string
     */
    static public String appendLongDesc(String input, Object result, String retCode)
    {
        List<String> params = new ArrayList<String>();
        params.add(ObjectPrintWriter.printObject(result));
        params.add(retCode);
        return ErrorResourceUtil.getLocalizedStringFromRawString(input, params );
    }

    /**
     * Append long desc.
     * 
     * @param input the input
     * @param retCode the ret code
     * 
     * @return the string
     */
    static public String appendLongDesc(String input, String retCode)
    {
        List<String> params = new ArrayList<String>();
        params.add(retCode);
        return ErrorResourceUtil.getLocalizedStringFromRawString(input, params );
    }


}
