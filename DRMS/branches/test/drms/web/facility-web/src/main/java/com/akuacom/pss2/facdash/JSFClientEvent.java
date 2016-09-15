/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFEvent.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantRule;
import java.io.Serializable;

/**
 * The Class JSFEvent.
 */
public class JSFClientEvent implements Serializable {

	/** The name. */
	private String name;

	/** The program name. */
	private String programName;

	/** The clients string. */
	private String clientName;

	/** The notification. */
	private Date notification;

	/** The start. */
	private Date start;

	/** The end. */
	private Date end;

	/** The status. */
	private String status;

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
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
