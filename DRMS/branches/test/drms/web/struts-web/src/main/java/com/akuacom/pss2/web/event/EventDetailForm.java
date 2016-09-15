/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.EventDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.web.util.OptionUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * The Class EventDetailForm.
 */
public class EventDetailForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The event date. */
    private String eventDate;
    
    /** The start hour. */
    private String startHour;
    
    /** The start min. */
    private String startMin;
    
    /** The start sec. */
    private String startSec;
    
    /** The end hour. */
    private String endHour;
    
    /** The end min. */
    private String endMin;
    
    /** The end sec. */
    private String endSec;
    
    /** The operator message. */
    private String operatorMessage;
    
    /** The participants. */
    private List<EventParticipant> participants;

    /** The hour list. */
    private List hourList = OptionUtil.getHoursList();
    
    /** The min list. */
    private List minList = OptionUtil.getMinSecList();
    
    /** The sec list. */
    private List secList = OptionUtil.getMinSecList();

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        final ActionErrors errors = new ActionErrors();

        final String dispatch = request.getParameter("dispatch");
        if( !"save".equals(dispatch)) {
            return errors;
        }

        DateFormat format = getParser();
        try {
            format.parse(eventDate);
        } catch (ParseException e) {
            ActionMessage message;
            if ("".equals(eventDate)) {
                message = new ActionMessage("pss2.event.create.eventDate.empty");
            } else {
                message = new ActionMessage("pss2.event.create.eventDate.parser", eventDate, "MM/dd/yyyy");
            }
            errors.add("eventDate", message);
        }

        return errors;
    }

    /**
     * Gets the parser.
     * 
     * @return the parser
     */
    private SimpleDateFormat getParser() {
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    /**
     * Gets the end hour.
     * 
     * @return the end hour
     */
    public String getEndHour() {
        return endHour;
    }

    /**
     * Sets the end hour.
     * 
     * @param endHour the new end hour
     */
    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    /**
     * Gets the event date.
     * 
     * @return the event date
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets the event date.
     * 
     * @param eventDate the new event date
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Gets the start hour.
     * 
     * @return the start hour
     */
    public String getStartHour() {
        return startHour;
    }

    /**
     * Sets the start hour.
     * 
     * @param startHour the new start hour
     */
    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     * 
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the operator message.
     * 
     * @return the operator message
     */
    public String getOperatorMessage() {
        return operatorMessage;
    }

    /**
     * Sets the operator message.
     * 
     * @param operatorMessage the new operator message
     */
    public void setOperatorMessage(String operatorMessage) {
        this.operatorMessage = operatorMessage;
    }

    /**
     * Gets the participants.
     * 
     * @return the participants
     */
    public List<EventParticipant> getParticipants() {
        return participants;
    }

    /**
     * Sets the participants.
     * 
     * @param participants the new participants
     */
    public void setParticipants(List<EventParticipant> participants) {
        this.participants = participants;
    }

    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the hour list.
     * 
     * @return the hour list
     */
    public List getHourList() {
        return hourList;
    }

    /**
     * Gets the min list.
     * 
     * @return the min list
     */
    public List getMinList() {
        return minList;
    }

    /**
     * Gets the sec list.
     * 
     * @return the sec list
     */
    public List getSecList() {
        return secList;
    }

    /**
     * Gets the end min.
     * 
     * @return the end min
     */
    public String getEndMin() {
        return endMin;
    }

    /**
     * Sets the end min.
     * 
     * @param endMin the new end min
     */
    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    /**
     * Gets the end sec.
     * 
     * @return the end sec
     */
    public String getEndSec() {
        return endSec;
    }

    /**
     * Sets the end sec.
     * 
     * @param endSec the new end sec
     */
    public void setEndSec(String endSec) {
        this.endSec = endSec;
    }

    /**
     * Gets the start min.
     * 
     * @return the start min
     */
    public String getStartMin() {
        return startMin;
    }

    /**
     * Sets the start min.
     * 
     * @param startMin the new start min
     */
    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    /**
     * Gets the start sec.
     * 
     * @return the start sec
     */
    public String getStartSec() {
        return startSec;
    }

    /**
     * Sets the start sec.
     * 
     * @param startSec the new start sec
     */
    public void setStartSec(String startSec) {
        this.startSec = startSec;
    }
}
