/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.dbp.DbpEventDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.dbp;

import com.akuacom.pss2.web.util.OptionUtil;
import com.akuacom.pss2.event.participant.EventParticipant;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * The Class DbpEventDetailForm.
 */
public class DbpEventDetailForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The event date. */
    private String eventDate;
    
    /** The start hour. */
    private String startHour;
    
    /** The end hour. */
    private String endHour;
    
    /** The respond by date. */
    private String respondByDate;
    
    /** The respond by hour. */
    private String respondByHour;
    
    /** The respond by min. */
    private String respondByMin;
    
    /** The participants. */
    private List<EventParticipant> participants;

    /** The hour list. */
    private List hourList = OptionUtil.getHoursList();
    
    /** The min list. */
    private List minList = OptionUtil.getMinSecList();

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
        try {
            format.parse(respondByDate);
        } catch (ParseException e) {
            ActionMessage message;
            if ("".equals(respondByDate)) {
                message = new ActionMessage("pss2.event.create.respondByDate.empty");
            } else {
                message = new ActionMessage("pss2.event.create.respondByDate.parser", respondByDate, "MM/dd/yyyy");
            }
            errors.add("respondByDate", message);
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

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        super.reset(actionMapping, request);
        startHour = "12";
        endHour = "20";
        respondByHour = "17";
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
     * Gets the respond by date.
     * 
     * @return the respond by date
     */
    public String getRespondByDate() {
        return respondByDate;
    }

    /**
     * Sets the respond by date.
     * 
     * @param respondByDate the new respond by date
     */
    public void setRespondByDate(String respondByDate) {
        this.respondByDate = respondByDate;
    }

    /**
     * Gets the respond by hour.
     * 
     * @return the respond by hour
     */
    public String getRespondByHour() {
        return respondByHour;
    }

    /**
     * Sets the respond by hour.
     * 
     * @param respondByHour the new respond by hour
     */
    public void setRespondByHour(String respondByHour) {
        this.respondByHour = respondByHour;
    }

    /**
     * Gets the respond by min.
     * 
     * @return the respond by min
     */
    public String getRespondByMin() {
        return respondByMin;
    }

    /**
     * Sets the respond by min.
     * 
     * @param respondByMin the new respond by min
     */
    public void setRespondByMin(String respondByMin) {
        this.respondByMin = respondByMin;
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
}
