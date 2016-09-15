/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.dbp.DbpNoBidEventDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * The Class DbpNoBidEventDetailForm.
 */
public class DbpNoBidEventDetailForm extends ActionForm {
    
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
    
    /** The participants. */
    private List<EventParticipant> participants;
    
    /** The data file. */
    private FormFile dataFile;

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

        if ("".equals(eventDate)) 
        {
            errors.add("eventDate", new ActionMessage("pss2.event.create.eventDate.empty"));
        }
        else
        {
	        DateFormat format = getParser();
	        try 
	        {
	            format.parse(eventDate);
	        } 
	        catch (ParseException e) 
	        {
	            errors.add("eventDate", new ActionMessage(
	            	"pss2.event.create.eventDate.parser", eventDate, "MM/dd/yyyy"));
	        }
        }
        
        if ("".equals(dataFile.getFileName())) 
        {
            errors.add("dataFile", new ActionMessage("pss2.event.create.dataFile.empty"));
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
     * Gets the data file.
     * 
     * @return the data file
     */
    public FormFile getDataFile() {
        return dataFile;
    }

    /**
     * Sets the data file.
     * 
     * @param dataFile the new data file
     */
    public void setDataFile(FormFile dataFile) {
        this.dataFile = dataFile;
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
