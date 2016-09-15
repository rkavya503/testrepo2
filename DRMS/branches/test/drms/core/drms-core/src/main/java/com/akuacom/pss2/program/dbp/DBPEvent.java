/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.DBPEventEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.pss2.event.Event;

/**
 * The Class DBPEvent
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "dbpevent")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = "DBPEvent.findAll",
        query = "select e from DBPEvent e",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "DBPEvent.findByProgramName",
        query = "select distinct(e) from DBPEvent e where e.programName = :programName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "DBPEvent.findByEventName.Single",
        query = "select distinct(e) from DBPEvent e where e.eventName = :eventName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "DBPEvent.findByEventNameProgramName",
        query = "select distinct(e) from DBPEvent e where e.eventName = :eventName and e.programName = :programName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class DBPEvent extends Event implements DBPEventTiming{

	private static final long serialVersionUID = 7376530794959858845L;

	private Date respondBy;
	private Date drasRespondBy;

	@Enumerated(value = EnumType.STRING)
	private BidState currentBidState;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "event")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private List<EventBidBlock> bidBlocks;

	public Date getRespondBy() {
		return respondBy;
	}

	public void setRespondBy(Date respondBy) {
		this.respondBy = respondBy;
	}

	public Date getDrasRespondBy() {
		return drasRespondBy;
	}

	public void setDrasRespondBy(Date drasRespondBy) {
		this.drasRespondBy = drasRespondBy;
	}

	public BidState getCurrentBidState() {
		return currentBidState;
	}

	public void setCurrentBidState(BidState currentBidState) {
		this.currentBidState = currentBidState;
	}

	public List<EventBidBlock> getBidBlocks() {
		return bidBlocks;
	}

	public void setBidBlocks(List<EventBidBlock> bidBlocks) {
		this.bidBlocks = bidBlocks;
	}
}
