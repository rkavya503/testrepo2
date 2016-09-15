/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.ModeSlot.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;

import com.akuacom.pss2.util.OperationModeValue;

/**
 * The Class ModeSlot.
 */
public class ModeSlot implements Serializable, Comparable<ModeSlot>
{
	
	/** The operation mode value. */
	private OperationModeValue operationModeValue;
	
	/** The time slot s. */
	private double timeSlotS;
	
	/**
	 * Instantiates a new mode slot.
	 */
	public ModeSlot()
	{
	}

	/**
	 * Instantiates a new mode slot.
	 * 
	 * @param operationModeValue the operation mode value
	 * @param timeSlotS the time slot s
	 */
	public ModeSlot(OperationModeValue operationModeValue, double timeSlotS)
	{
		super();
		this.operationModeValue = operationModeValue;
		this.timeSlotS = timeSlotS;
	}

	/**
	 * Gets the operation mode value.
	 * 
	 * @return the operation mode value
	 */
	public OperationModeValue getOperationModeValue()
	{
		return operationModeValue;
	}

	/**
	 * Sets the operation mode value.
	 * 
	 * @param operationModeValue the new operation mode value
	 */
	public void setOperationModeValue(OperationModeValue operationModeValue)
	{
		this.operationModeValue = operationModeValue;
	}

	/**
	 * Sets the operation mode value.
	 * 
	 * @param modeSignalState the new operation mode value
	 */
	public void setOperationModeValue(String modeSignalState)
	{
		if(modeSignalState.equalsIgnoreCase("moderate"))
		{
			operationModeValue = OperationModeValue.MODERATE;	
		}
		else if(modeSignalState.equalsIgnoreCase("high"))
		{
			operationModeValue = OperationModeValue.HIGH;	
		}
		else if(modeSignalState.equalsIgnoreCase("special"))
		{
			operationModeValue = OperationModeValue.SPECIAL;
		}
		else
		{
			operationModeValue = OperationModeValue.NORMAL;	
		}
	}

	/**
	 * Gets the time slot s.
	 * 
	 * @return the time slot s
	 */
	public double getTimeSlotS()
	{
		return timeSlotS;
	}

	/**
	 * Sets the time slot s.
	 * 
	 * @param timeSlotS the new time slot s
	 */
	public void setTimeSlotS(double timeSlotS)
	{
		this.timeSlotS = timeSlotS;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
    public String toString()
    {
        StringBuilder rv = new StringBuilder("ModeSlot: ");
        rv.append("operationModeValue: " + operationModeValue);
        rv.append(", timeSlotS: " + timeSlotS);        
        return rv.toString();
    }    
	
	public int compareTo(ModeSlot o) {
		if (o == null) {
			return 1;
		}

		return new Double(this.getTimeSlotS()).compareTo(new Double(o
				.getTimeSlotS()));
	}
}
