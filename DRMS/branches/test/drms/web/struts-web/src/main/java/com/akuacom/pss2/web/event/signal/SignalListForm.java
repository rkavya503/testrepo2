/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.signal.SignalListForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.signal;

import org.apache.struts.action.ActionForm;

/**
 * The Class SignalListForm.
 */
public class SignalListForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The participant name. */
    private String participantName;
    
    /** The signal entry ids. */
    private String[] signalEntryIds;

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
     * Gets the participant name.
     * 
     * @return the participant name
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the participant name.
     * 
     * @param participantName the new participant name
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
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
     * Gets the signal entry ids.
     * 
     * @return the signal entry ids
     */
    public String[] getSignalEntryIds() {
        return signalEntryIds;
    }

    /**
     * Sets the signal entry ids.
     * 
     * @param signalEntryIds the new signal entry ids
     */
    public void setSignalEntryIds(String[] signalEntryIds) {
        this.signalEntryIds = signalEntryIds;
    }
}
