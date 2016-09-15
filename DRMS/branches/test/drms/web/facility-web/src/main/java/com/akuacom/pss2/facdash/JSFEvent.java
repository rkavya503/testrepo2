/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFEvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class JSFEvent.
 */
public class JSFEvent implements Serializable {

	/** The name. */
	private String name;

	/** The program name. */
	private String programName;

	/** The clients string. */
	private String clientsString;

	/** The notification. */
	private Date notification;

	/** The start. */
	private Date start;

	/** The end. */
	private Date end;

	/** The remaining time to the event */
	private String remainingTime;

	/** The status. */
	private String status;

	/** The bid. */
	private boolean bid;

    private boolean optOut = true;

    private boolean optIn;

	private static final long MS_IN_MIN = 1000 * 60;
	private static final long MIN_IN_DAY = 24 * 60;
	private static final long MIN_IN_HR = 60;
	//private static final long SEC_IN_MIN = 60;

    private String dateTimeForEvent;
    private boolean dayOfAdjustment;
    private boolean disableDayAdjustment;

	/**
	 * Load.
	 *
     * @param event
     *            the event
     */
	public void load(Event event) {
		this.name = event.getEventName();
		this.programName = event.getProgramName();
		this.notification = event.getIssuedTime();
		this.start = event.getStartTime();
		this.end = event.getEndTime();
		if(event.getEventStatus() == com.akuacom.pss2.util.EventStatus.FAR || 
			event.getEventStatus() == com.akuacom.pss2.util.EventStatus.NEAR)
		{
			this.status = "ISSUED";
		}
		else
		{
			this.status = event.getEventStatus().toString();
		}

		try {
            StringBuilder clientsSB = new StringBuilder();
            ClientManager cm = EJBFactory.getBean(ClientManager.class);
            String participantName = FDUtils.getParticipantName();

            EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
            EventParticipant eventParticipant = eventParticipantManager.getEventParticipant(name, participantName, false);
            if (eventParticipant == null) {
                optIn = true;
                optOut = false;
            } else {
            	this.dayOfAdjustment = eventParticipant.getApplyDayOfBaselineAdjustment() == 0 ? false : true;
            }
            
            EventManager eventManager = EJBFactory.getBean(EventManager.class);
            DBPEvent dbpEvent = (DBPEvent)eventManager.getEvent(programName, name);

            if ( dbpEvent != null && dbpEvent.getDrasRespondBy() != null ) {
            	disableDayAdjustment = new Date().after(dbpEvent.getDrasRespondBy());
            }
            
            
            for(Participant client: cm.getClients(participantName))
                {
                    Set<String> programNames = new HashSet<String>();
                    for(ProgramParticipant programClient: client.getProgramParticipants())
                    {
                        programNames.add(programClient.getProgramName());
                    }

                    for(EventParticipant ep: client.getEventParticipants()){
                        if (this.name.equalsIgnoreCase(ep.getEvent().getEventName()) && (programNames.contains(this.programName)||
                        		this.programName.equals(TestProgram.PROGRAM_NAME))){
                            clientsSB.append(client.getParticipantName());
                            clientsSB.append(",");
                        }
                    }
                }
                
                if(clientsSB.length() == 0)
                {
                    clientsString = "";
                }
                else
                {
                    clientsString =  clientsSB.substring(0, clientsSB.length()-1);
                }


        } catch (Exception e) { // TODO: ignore the exception?
        }


		// determine if the bid action should be available
        checkBiddingWindow(event);
	}

    private void checkBiddingWindow(Event event) {
        bid = false;
        final ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);

        String programName = event.getProgramName();
        
		String programClassName =  programManager.getProgramClassByName(programName);
		if(programClassName != null && programClassName.contains("DBP")){
            Boolean isDrasBidding = programManager.isDrasBiddingByProgramName(programName);
            if(isDrasBidding != null && isDrasBidding){
                bid = true;
            }
        }
    }

    public String remainingTime(Date eventStartTime) {

		Date useDate = eventStartTime;
		Date today = new Date();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(useDate);
		calendar2.setTime(today);

		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();

		long diffMS = milliseconds1 - milliseconds2;
        if (diffMS > 0){
            long diffMinutes = diffMS / MS_IN_MIN + 1;
            long diffDays = diffMinutes / MIN_IN_DAY;
            diffMinutes -= diffDays * MIN_IN_DAY;
            long diffHours = diffMinutes / MIN_IN_HR;
            diffMinutes -= diffHours * MIN_IN_HR;
            // long diffSec = diffMinutes / SEC_IN_MIN;
            	return diffDays + " Days, " + diffHours + " Hours, " + diffMinutes
				+ " Minutes";
        }else{
            	return "Event is active";
        }

		
	}

	public String DateTimeForEvent(Date eventStartTime, Date eventEndTime) {
		int durDiff = DateUtil.minuteOffset(eventStartTime, eventEndTime);
		if (durDiff > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String day = sdf.format(eventStartTime);
			sdf = new SimpleDateFormat("HH:mm");
			String startT = sdf.format(eventStartTime);
			String endT = sdf.format(eventEndTime);

			int hours = durDiff / 60;
			int min = durDiff % 60;
			StringBuilder sb = new StringBuilder();
			sb.append(hours);
			if (hours == 0) {
				sb = new StringBuilder(); 
			} else if (hours == 1) {
				sb.append(" Hour, ");
			} else {
				sb.append(" Hours, ");
			}
			sb.append(min);
			if (min == 1) {
				sb.append(" Minute");
			} else {
				sb.append(" Minutes");
			}

			return "from " + startT + " to " + endT + " ("
					+ sb.toString() + ")";
		} else {
			return "Event is active";

		}
	}

	public void editRules() {
		EventManager pm = EJB3Factory.getLocalBean(EventManager.class);
		EventParticipant eventPart = pm.getEventParticipant(name, FDUtils
				.getJSFClient().getName(), true);
		if (eventPart.getEventRules() != null) {
			FDUtils.setEventRuleTable(new EventRuleTable(eventPart
					.getEventRules()));
		} else {
			FDUtils.setEventRuleTable(new EventRuleTable());
		}
	}

	public String updateRules(List<JSFEventRule> jsfRules) {
		EventManager pm = EJB3Factory.getLocalBean(EventManager.class);
		EventParticipant eventPart = pm.getEventParticipant(name, FDUtils
				.getJSFClient().getName(), true);
		if (eventPart.getEventRules() == null) {
			eventPart.setEventRules(new HashSet<EventParticipantRule>());
		} else {
			eventPart.getEventRules().clear();
		}
		int index = 0;
		for (JSFEventRule jsfRule : jsfRules) {
			jsfRule.getRule().setSortOrder(index++);
			eventPart.getEventRules().add(jsfRule.getRule());
		}
		// TODO: pattern should change in EventManager
		pm.setEventParticipant(eventPart);

		return "saveRules";
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the start.
	 * 
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 * 
	 * @param start
	 *            the new start
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * Gets the end.
	 * 
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Sets the end.
	 * 
	 * @param end
	 *            the new end
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @param programName
	 *            the new program name
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/**
	 * Gets the notification.
	 * 
	 * @return the notification
	 */
	public Date getNotification() {
		return notification;
	}

	/**
	 * Sets the notification.
	 * 
	 * @param notification
	 *            the new notification
	 */
	public void setNotification(Date notification) {
		this.notification = notification;
	}

	/**
	 * Gets the clients string.
	 * 
	 * @return the clients string
	 */
	public String getClientsString() {
		return clientsString;
	}

	/**
	 * Checks if is bid.
	 * 
	 * @return true, if is bid
	 */
	public boolean isBid() {
		return bid;
	}

	public String getRemainingTime() {
		remainingTime = this.remainingTime(this.getStart());
		return remainingTime;
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}

    public String getDateTimeForEvent() {
    	if(null == this.getStart() || null == this.end){
    		return "";
    	}
        dateTimeForEvent = this.DateTimeForEvent(this.getStart(),this.end);
        return dateTimeForEvent;
    }

    public void setDateTimeForEvent(String dateTimeForEvent) {
        this.dateTimeForEvent = dateTimeForEvent;
    }

    public boolean isOptOut() {
        return optOut;
    }

    public boolean isOptIn() {
        return optIn;
    }

    @Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof JSFEvent) {
			// an event can not have a null event name or program name so not checking for null
			JSFEvent jsfev = (JSFEvent)obj;
			result = jsfev.getName().equals(this.getName()) 
				&& jsfev.getProgramName().equals(this.getProgramName());
		}
		return result;
	}

	public boolean isDayOfAdjustment() {
		return dayOfAdjustment;
	}

	public void setDayOfAdjustment(boolean dayOfAdjustment) {
		this.dayOfAdjustment = dayOfAdjustment;
	}

	public boolean isDisableDayAdjustment() {
		return disableDayAdjustment;
	}

	public void setDisableDayAdjustment(boolean disableDayAdjustment) {
		this.disableDayAdjustment = disableDayAdjustment;
	}
	
	

}
