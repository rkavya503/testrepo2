package com.akuacom.pss2.event.signal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "event_signal")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventSignal extends VersionedEntity implements Signal {
	
	private static final long serialVersionUID = 3955096206103841416L;

	@ManyToOne
	@JoinColumn(name="event_uuid", nullable=false)
	private Event event;
	
    @ManyToOne
	@JoinColumn(name="signal_def_uuid", nullable=false)
	private SignalDef signalDef;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "eventSignal")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<EventSignalEntry> eventSignalEntries;
	
	//bi-directional many-to-one association to Event
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	//bi-directional many-to-one association to SignalDef
	@Override
	public SignalDef getSignalDef() {
		return this.signalDef;
	}

    @Override
	public void setSignalDef(SignalDef signalDef) {
		this.signalDef = signalDef;
	}

	//bi-directional many-to-one association to EventSignalEntry
    public Set<EventSignalEntry> getEventSignalEntries() {
		return this.eventSignalEntries;
	}

	public void setEventSignalEntries(Set<EventSignalEntry> eventSignalEntries) {
		this.eventSignalEntries = eventSignalEntries;
	}
    
	   
	@Override
	public Set<? extends SignalEntry> getSignalEntries()
	{
		return this.getEventSignalEntries();
	}

	@Override
	public void setSignalEntries(Set<? extends SignalEntry> entries)
	{
		Set<EventSignalEntry> newSE = new HashSet<EventSignalEntry>();
		
		for (SignalEntry se : entries) {
			if (se instanceof EventSignalEntry) {
				newSE.add((EventSignalEntry)se);
			} else {
				throw new RuntimeException("Only EventSignalEntry may be added to EventSignal signal entries");
			}
		}
		
		this.setEventSignalEntries(newSE);
	}
 

}
