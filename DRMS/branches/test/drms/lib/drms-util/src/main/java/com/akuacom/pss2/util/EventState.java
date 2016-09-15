/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.EventState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.akuacom.utils.DateUtil;

/**
 * NOTE: This class is an object on business layer and above, together with ModeSlot.
 * 
 * @see ModeSlot
 */
public class EventState implements Serializable
{
	
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 1458686072358151728L;


    public static final String EVENT_STATUS_FAR = "FAR";
	public static final String EVENT_STATUS_NEAR = "NEAR";
	public static final String EVENT_STATUS_ACTIVE = "ACTIVE";
	public static final String EVENT_STATUS_NONE = "NONE";
	public static final String MANUAL_SUFFIX = "(man)";
	public static final String EVENT_STATUS_OPT_OUT = "OPT OUT";
	
	/** The dras client id. */
	private String drasClientID;
	
	/** The event identifier. */
	private String eventIdentifier;
	
	/** The event mod number. */
	private int eventModNumber;
	
	/** The event state id. */
	private long eventStateID;
	
	/** The test event. */
	private boolean testEvent;
	
	/** The offline. */
	private boolean offline;
	
	/** The program name. */
	private String programName;
	
	/** The event status. */
	private EventStatus eventStatus;
	
	/** The operation mode value. */
	private OperationModeValue operationModeValue;
	
	/** The current time s. */
	private double currentTimeS;
	
	/** The operation mode schedule. */
	private List<ModeSlot> operationModeSchedule;
	
	/** The notification time. */
	private Date notificationTime;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
    
    /** The manual control. */
    private boolean manualControl;
	
	/** The event info instances. */
	private List<EventInfoInstance> eventInfoInstances;

	private List<String> locations;
	
	private Date nearTime;
    /**
     * Checks if is manual control.
     * 
     * @return true, if is manual control
     */
    public boolean isManualControl()
    {
        return manualControl;
    }

    /**
     * Sets the manual control.
     * 
     * @param manualControl the new manual control
     */
    public void setManualControl(boolean manualControl)
    {
        this.manualControl = manualControl;
    }

    /**
     * Instantiates a new event state.
     */
    public EventState()
	{
	}

	/**
	 * Gets the dras client id.
	 * 
	 * @return the dras client id
	 */
	public String getDrasClientID()
	{
		return drasClientID;
	}

	/**
	 * Sets the dras client id.
	 * 
	 * @param drasClientID the new dras client id
	 */
	public void setDrasClientID(String drasClientID)
	{
		this.drasClientID = drasClientID;
	}

	/**
	 * Gets the event identifier.
	 * 
	 * @return the event identifier
	 */
	public String getEventIdentifier()
	{
		return eventIdentifier;
	}

	/**
	 * Sets the event identifier.
	 * 
	 * @param eventIdentifier the new event identifier
	 */
	public void setEventIdentifier(String eventIdentifier)
	{
		this.eventIdentifier = eventIdentifier;
	}

	/**
	 * Gets the event mod number.
	 * 
	 * @return the event mod number
	 */
	public int getEventModNumber()
	{
		return eventModNumber;
	}

	/**
	 * Sets the event mod number.
	 * 
	 * @param eventModNumber the new event mod number
	 */
	public void setEventModNumber(int eventModNumber)
	{
		this.eventModNumber = eventModNumber;
	}

	/**
	 * Gets the event state id.
	 * 
	 * @return the event state id
	 */
	public long getEventStateID()
	{
		return eventStateID;
	}

	/**
	 * Sets the event state id.
	 * 
	 * @param eventStateID the new event state id
	 */
	public void setEventStateID(long eventStateID)
	{
		this.eventStateID = eventStateID;
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

	/**
	 * Gets the event status.
	 * 
	 * @return the event status
	 */
	public EventStatus getEventStatus()
	{
		return eventStatus;
	}

	/**
	 * Sets the event status.
	 * 
	 * @param eventStatus the new event status
	 */
	public void setEventStatus(EventStatus eventStatus)
	{
		this.eventStatus = eventStatus;
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
		else if(modeSignalState.equalsIgnoreCase("unknown"))
		{
			operationModeValue = OperationModeValue.UNKNOWN;	
		}
		else 
		{
			operationModeValue = OperationModeValue.NORMAL;	
		}
	}

	/**
	 * Gets the current time s.
	 * 
	 * @return the current time s
	 */
	public double getCurrentTimeS()
	{
		return currentTimeS;
	}

	/**
	 * Sets the current time s.
	 * 
	 * @param currentTimeS the new current time s
	 */
	public void setCurrentTimeS(double currentTimeS)
	{
		this.currentTimeS = currentTimeS;
	}

	/**
	 * Gets the operation mode schedule.
	 * 
	 * @return the operation mode schedule
	 */
	public List<ModeSlot> getOperationModeSchedule()
	{
		return operationModeSchedule;
	}

	/**
	 * Sets the operation mode schedule.
	 * 
	 * @param operationModeSchedule the new operation mode schedule
	 */
	public void setOperationModeSchedule(List<ModeSlot> operationModeSchedule)
	{
		Collections.sort(operationModeSchedule);
		this.operationModeSchedule = operationModeSchedule;
	}

	/**
	 * Gets the notification time.
	 * 
	 * @return the notification time
	 */
	public Date getNotificationTime()
	{
		return notificationTime;
	}

	/**
	 * Sets the notification time.
	 * 
	 * @param notificationTime the new notification time
	 */
	public void setNotificationTime(Date notificationTime)
	{
		this.notificationTime = notificationTime;
	}

	/**
	 * Gets the start time.
	 * 
	 * @return the start time
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the start time.
	 * 
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 * 
	 * @return the end time
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * Sets the end time.
	 * 
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * Checks if is test event.
	 * 
	 * @return true, if is test event
	 */
	public boolean isTestEvent()
	{
		return testEvent;
	}

	/**
	 * Sets the test event.
	 * 
	 * @param testEvent the new test event
	 */
	public void setTestEvent(boolean testEvent)
	{
		this.testEvent = testEvent;
	}

	/**
	 * Checks if is offline.
	 * 
	 * @return true, if is offline
	 */
	public boolean isOffline()
	{
		return offline;
	}

	/**
	 * Sets the offline.
	 * 
	 * @param offline the new offline
	 */
	public void setOffline(boolean offline)
	{
		this.offline = offline;
	}
	
    public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}	
	

	public Date getNearTime() {
		return nearTime;
	}

	public void setNearTime(Date nearTime) {
		this.nearTime = nearTime;
	}

	/**
     * Convert mode to value.
     * 
     * @param operationModeValue the operation mode value
     * 
     * @return the int
     */
    public static int convertModeToValue(OperationModeValue operationModeValue)
    {
    	switch(operationModeValue)
    	{
    	case MODERATE:
    		return 1;
    	case HIGH:
    		return 2;
    	case SPECIAL:
    		return 3;
    	case NORMAL:
    	default:
    		return 0;
    	}
    }

    /**
     * Convert mode to multiple.
     * 
     * @param operationModeValue the operation mode value
     * 
     * @return the double
     */
    public static double convertModeToMultiple(OperationModeValue operationModeValue)
    {
    	switch(operationModeValue)
    	{
    	case MODERATE:
    		return 3.0;
    	case HIGH:
    		return 5.0;
    	case SPECIAL:
    		return 7.0;
    	case NORMAL:
    	default:
    		return 1.0;
    	}
    }

    public static String loadEventStatus(EventStatus eventStatus) {

        String eStatus = "";

        if(eventStatus != null)
        {

	     	switch(eventStatus)
	     	{
	      	case FAR:
	      		eStatus = EVENT_STATUS_FAR;
	     		break;
	      	case NEAR:
	      		eStatus = EVENT_STATUS_NEAR;
	     		break;
	     	case ACTIVE:
	     		eStatus = EVENT_STATUS_ACTIVE;
	     		break;
	     	default:
	     		eStatus = EVENT_STATUS_NONE;
	     		break;
	     	}

        }
       return  eStatus;
    }
    
    public String toString()
    {
        StringBuilder rv = new StringBuilder("EventState: ");
        rv.append("\ndrasClientID: ").append(drasClientID);
        rv.append("\neventIdentifier: ").append(eventIdentifier);
        rv.append("\neventModNumber: ").append(eventModNumber);
        rv.append("\neventStateID: ").append(eventStateID);
        rv.append("\ntestEvent: ").append(testEvent);
        rv.append("\noffline: ").append(offline);
        rv.append("\nprogramName: ").append(programName);
        rv.append("\neventStatus: ").append(eventStatus);
        rv.append("\noperationModeValue: ").append(operationModeValue);
        rv.append("\ncurrentTimeS: ").append(currentTimeS);
        if(operationModeSchedule != null)
        {
	        rv.append("\noperationModeSchedule: ");
	        for(ModeSlot modeSlot: operationModeSchedule)
	        {
	        	rv.append("\n");
	        	rv.append(modeSlot);
	        }
         }
        else
        {
        	rv.append("\nnoperationModeSchedule: null");        	
        }
        rv.append("\nnotificationTime: " + notificationTime);
        rv.append("\nstartTime: " + startTime);
        rv.append("\nendTime: " + endTime);        
        return rv.toString();
    }

	/**
	 * Gets the event info instances.
	 * 
	 * @return the event info instances
	 */
	public List<EventInfoInstance> getEventInfoInstances()
	{
		return eventInfoInstances;
	}

	/**
	 * Sets the event info instances.
	 * 
	 * @param eventInfoInstances the new event info instances
	 */
	public void setEventInfoInstances(List<EventInfoInstance> eventInfoInstances)
	{
		this.eventInfoInstances = eventInfoInstances;
	}    
	
	public static class PriorityComparator implements Comparator<EventState>
	{
		private Map<String, Integer> progPriority;
		
		public PriorityComparator() {
			
		}
		
		public PriorityComparator(Map<String, Integer> programPriorities) {
			progPriority = programPriorities;
		}
		
		public int compare(EventState o1, EventState o2)
		{
			// active state overlap matters. 
			// If active overlap, sort by program priority. Else by startTime
			// This means an event that is FAR could replace an active event
			// if their active times overlap at any point in the future.
			
			// TODO: "merge", e.g. PG&E CPP and DBP
			if (o1.getStartTime().before(new Date())
					&& o2.getStartTime().before(new Date())) {
				// sort by program priority
				Integer o1Pri = progPriority.get(o1.getProgramName());
				if (o1Pri == null)
					return -1;
				Integer o2Pri = progPriority.get(o2.getProgramName());
				
				return o1Pri.compareTo(o2Pri);
			} else {
				// sort by start date.  Should never be null
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
		}
		
		private boolean activePeriodsOverlap(EventState o1, EventState o2) {
			if(o1.getEndTime()==null) o1.setEndTime(DateUtil.getBigEnoughDate());
			if(o2.getEndTime()==null) o2.setEndTime(DateUtil.getBigEnoughDate());
			
			boolean startOverlap1 = o1.getStartTime().getTime() <= o2.getStartTime().getTime() 
					&& o2.getStartTime().getTime() <= o1.getEndTime().getTime();
			
			boolean endOverlap1 = o1.getStartTime().getTime() <= o2.getEndTime().getTime() 
				&& o2.getEndTime().getTime() <= o1.getEndTime().getTime();
			
			boolean startOverlap2 = o2.getStartTime().getTime() <= o1.getStartTime().getTime() 
				&& o1.getStartTime().getTime() <= o2.getEndTime().getTime();
	
			boolean endOverlap2 = o2.getStartTime().getTime() <= o1.getEndTime().getTime() 
				&& o1.getEndTime().getTime() <= o2.getEndTime().getTime();
			
			return startOverlap1 || endOverlap1 || startOverlap2 || endOverlap2;
		}
	}
}
