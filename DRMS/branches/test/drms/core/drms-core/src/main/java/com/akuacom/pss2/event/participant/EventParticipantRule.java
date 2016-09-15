/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.EventRuleEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event.participant;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.pss2.rule.Rule;

/**
 * The Class EventRule.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
//@BatchSize(size=50)
@Table(name = "event_participant_rule")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventParticipantRule extends Rule
{

	private static final long serialVersionUID = -6899514191849482383L;
	
	/** The event participant. */
	@ManyToOne
    @JoinColumn(name = "event_participant_uuid")
	private EventParticipant eventParticipant;

	/**
	 * Default constructor
	 */
	public EventParticipantRule()
	{
	}
	
	/**
	 * Create a EventRuleEAO constructor from a RuleEAO and a date
	 */
	@SuppressWarnings("deprecation")
	public EventParticipantRule(Rule ruleEAO, Date eventDate)
	{
		GregorianCalendar calender = new GregorianCalendar();
		calender.setTime(eventDate);
		if (ruleEAO.getStart() != null) {
			calender.set(Calendar.HOUR_OF_DAY, ruleEAO.getStart().getHours());
			calender.set(Calendar.MINUTE, ruleEAO.getStart().getMinutes());
			calender.set(Calendar.SECOND, ruleEAO.getStart().getSeconds());
			calender.set(Calendar.MILLISECOND, 0);
		}
		setStart(calender.getTime());
		
		calender = new GregorianCalendar();
		calender.setTime(eventDate);
        Date end = ruleEAO.getEnd();
        if (end != null) {
            if (end.getHours() == 0 && end.getMinutes() == 0 && end.getSeconds() == 0) {
                // convert 0:0:0 to 23:59:59
                calender.set(Calendar.HOUR_OF_DAY, 23);
                calender.set(Calendar.MINUTE, 59);
                calender.set(Calendar.SECOND, 59);
            } else {
                calender.set(Calendar.HOUR_OF_DAY, end.getHours());
                calender.set(Calendar.MINUTE, end.getMinutes());
                calender.set(Calendar.SECOND, end.getSeconds());
            }

            if (end.getMinutes() == 59 &&
                    end.getSeconds() == 59) {
                calender.set(Calendar.MILLISECOND, 999);
            } else {
                calender.set(Calendar.MILLISECOND, 0);
            }
        }
		setEnd(calender.getTime());

		setMode(ruleEAO.getMode());
		setVariable(ruleEAO.getVariable());
		setOperator(ruleEAO.getOperator());
		setValue(ruleEAO.getValue());
		setThreshold(ruleEAO.getThreshold());
		setNotifyAction(ruleEAO.getNotifyAction());
		setSignalAction(ruleEAO.getSignalAction());
	}
	
	/**
	 * Gets the event participant.
	 * 
	 * @return the event participant
	 */
    public EventParticipant getEventParticipant() {
		return eventParticipant;
	}

	/**
	 * Sets the event participant.
	 * 
	 * @param eventParticipant the new event participant
	 */
	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}
}
