/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.EventInfo.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.io.Serializable;

/**
 * TODO: To be replaced by one to many relationship between program and event.
 * In most cases, simply replace the usage with the new event should work.
 */
/**
 * This represents the key fields for an event
 */
public class EventInfo implements Serializable
{
	
	/** The program name. */
	String programName;
	
	/** The event name. */
	String eventName;
	
	/**
	 * Instantiates a new event info.
	 */
	public EventInfo()
	{
		super();
	}
	
	/**
	 * Gets the event name.
	 * 
	 * @return the event name
	 */
	public String getEventName()
	{
		return eventName;
	}
	
	/**
	 * Sets the event name.
	 * 
	 * @param eventName the new event name
	 */
	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}
	
	/**
	 * Gets the program name.
	 * 
	 * @return the program name
	 */
	public String getProgramName()
	{
		return programName;
	}
	
	/**
	 * Sets the program name.
	 * 
	 * @param programName the new program name
	 */
	public void setProgramName(String programName)
	{
		this.programName = programName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
    {
        StringBuilder rv = new StringBuilder("EventInfo: ");
        rv.append("eventName: " + eventName);
        rv.append(", programName: " + programName);
        rv.append("\n");
        return rv.toString();
    }
}
