/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.EventListForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.Program;

/**
 * The Class EventListForm.
 */


public class EventListForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The program list. */
    private  List<Program> programList;
    
    /** The event name. */
    private String eventName;

        /** The # of hits submitting EventListForm */
    private String hit;
    
    /** The event list. */
    private List<Event> eventList;
    
    /** The event names. */
    private String[] eventNames;
    
    /** The program names. */
    private String[] programNames;
    
    /** The user. */
    private String user;
    
     /** The participants that enrolled in program */
    private Map<String,List> participantInProgram = new HashMap();

         /** The program name Delete Msg */
    private String programDeleteStatus;


     /**
     * Gets the participantInProgram
     *
     * @return the participantInProgram
     */
    public Map getParticipantInProgram()
    {
        return participantInProgram;
    }

    /**
     * Sets the participantInProgram.
     *
     * @param participantInProgram
     */
    public void setParticipantInProgram(Map participantInProgram)
    {
        this.participantInProgram = participantInProgram;
    }


    /**
     * Gets the program names.
     * 
     * @return the program names
     */
    public String[] getProgramNames()
    {
        return programNames;
    }

    /**
     * Sets the program names.
     * 
     * @param programNames the new program names
     */
    public void setProgramNames(String[] programNames)
    {
        this.programNames = programNames;
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


    public String getHit() {
        return hit;
    }


    public void setHit(String hit) {
        this.hit = hit;
    }


    /**
     * Gets the program list.
     * 
     * @return the program list
     */
    public List<Program> getProgramList() {
        return programList;
    }

    /**
     * Sets the program list.
     * 
     * @param programList the new program list
     */
    public void setProgramList(List<Program> programList) {
        this.programList = programList;
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
     * Gets the event list.
     * 
     * @return the event list
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * Sets the event list.
     * 
     * @param eventList the new event list
     */
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user.
     * 
     * @param user the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the event names.
     * 
     * @return the event names
     */
    public String[] getEventNames() {
        return eventNames;
    }

    /**
     * Sets the event names.
     * 
     * @param eventNames the new event names
     */
    public void setEventNames(String[] eventNames) {
        this.eventNames = eventNames;
    }

     /**
     * Gets the program name delete.
     *
     * @return the program name delete
     */
    public String getProgramDeleteStatus() {
        return programDeleteStatus;
    }

    /**
     * Sets the program name delete.
     *
     * @param programName the new program name delete
      */
    public void setProgramDeleteStatus(String programDeleteStatus) {
        this.programDeleteStatus = programDeleteStatus;
    }

}
