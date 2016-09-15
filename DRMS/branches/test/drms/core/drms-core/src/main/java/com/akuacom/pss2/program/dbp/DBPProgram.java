/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.io.Serializable;
import java.util.GregorianCalendar;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.program.Program;

/**
 * The Class DBPProgram.
 */
@Entity
@DiscriminatorValue("DBPProgram")
public class DBPProgram extends Program implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5396179578514628110L;

	/**
	 * Gets the pending lead ms.
	 * 
	 * @param event
	 *            the event
	 * 
	 * @return the pending lead ms
	 */
	@Override
	public long getPendingLeadMS(EventTiming event) {
		if (!isMustIssueBDBE()) {
			// DO events
			// TODO: this should be cached since it never changes
			GregorianCalendar startCal = new GregorianCalendar();
			startCal.setTime(event.getStartTime());
			GregorianCalendar respondByCal = new GregorianCalendar();
			respondByCal.setTime(((DBPEventTiming) event).getRespondBy());
			return startCal.getTimeInMillis() - respondByCal.getTimeInMillis();
		}
		// DA events
		return super.getPendingLeadMS(event);
	}

	@Override
	public Program getNewInstance() {
		return new DBPProgram();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.core.model.Program#toString()
	 */
	public String toString() {
		StringBuilder rv = new StringBuilder("DBPProgram: ");
		rv.append(super.toString());
		return rv.toString();
	}

}
