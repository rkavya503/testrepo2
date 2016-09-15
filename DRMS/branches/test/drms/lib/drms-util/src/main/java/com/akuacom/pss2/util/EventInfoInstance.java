/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.EventInfoInstance.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;
import java.util.List;

/**
 * The Class EventInfoInstance.
 */
public class EventInfoInstance implements Serializable
{
	
	/**
	 * The Enum SignalType.
	 */
	public enum SignalType 
	{
	    
    	/** The PRIC e_ absolute. */
    	PRICE_ABSOLUTE,
	    
    	/** The PRIC e_ relative. */
    	PRICE_RELATIVE,
	    
    	/** The PRIC e_ multiple. */
    	PRICE_MULTIPLE,
	    
    	/** The LOA d_ level. */
    	LOAD_LEVEL,
	    
    	/** The LOA d_ amount. */
    	LOAD_AMOUNT,
	    
    	/** The LOA d_ percentage. */
    	LOAD_PERCENTAGE,
	    
    	/** The GRI d_ reliability. */
    	GRID_RELIABILITY
    }
	
	/** The signal type. */
	private SignalType signalType;
	
	/** The signal name. */
	private String signalName;
	
	/** The event info values. */
	private List<EventInfoValue> eventInfoValues;
	
    /**
     * Instantiates a new event info instance.
     */
    public EventInfoInstance()
    {
    }

	/**
	 * Gets the signal type.
	 * 
	 * @return the signal type
	 */
	public SignalType getSignalType()
	{
		return signalType;
	}

	/**
	 * Sets the signal type.
	 * 
	 * @param signalType the new signal type
	 */
	public void setSignalType(SignalType signalType)
	{
		this.signalType = signalType;
	}

	/**
	 * Gets the signal name.
	 * 
	 * @return the signal name
	 */
	public String getSignalName()
	{
		return signalName;
	}

	/**
	 * Sets the signal name.
	 * 
	 * @param signalName the new signal name
	 */
	public void setSignalName(String signalName)
	{
		this.signalName = signalName;
	}

	/**
	 * Gets the event info values.
	 * 
	 * @return the event info values
	 */
	public List<EventInfoValue> getEventInfoValues()
	{
		return eventInfoValues;
	}

	/**
	 * Sets the event info values.
	 * 
	 * @param eventInfoValues the new event info values
	 */
	public void setEventInfoValues(List<EventInfoValue> eventInfoValues)
	{
		this.eventInfoValues = eventInfoValues;
	}

}
