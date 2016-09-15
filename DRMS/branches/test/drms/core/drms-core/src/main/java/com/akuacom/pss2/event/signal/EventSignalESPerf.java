package com.akuacom.pss2.event.signal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.event.EventESPerf;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;

/**
 * ESPerf family designed to optimize client polling for Event State
 *
 */

@Entity
@Table(name = "event_signal")

public class EventSignalESPerf extends VersionedEntity implements Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6345991110428584329L;

	@ManyToOne
	@JoinColumn(name="signal_def_uuid", nullable=false)
	private SignalDef signalDef;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "eventSignal")
	private Set<EventSignalEntryESPerf> eventSignalEntries;
	
	@ManyToOne
	@JoinColumn(name="event_uuid", nullable=false)
	private EventESPerf event;
	
	public EventESPerf getEvent() {
		return event;
	}

	public void setEvent(EventESPerf event) {
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
    public Set<EventSignalEntryESPerf> getEventSignalEntries() {
		return this.eventSignalEntries;
	}

	public void setEventSignalEntries(Set<EventSignalEntryESPerf> eventSignalEntries) {
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
		Set<EventSignalEntryESPerf> newSE = new HashSet<EventSignalEntryESPerf>();
		
		for (SignalEntry se : entries) {
			if (se instanceof EventSignalEntryESPerf) {
				newSE.add((EventSignalEntryESPerf)se);
			} else {
				throw new RuntimeException("Only EventSignalEntry may be added to EventSignal signal entries");
			}
		}
		
		this.setEventSignalEntries(newSE);
	}
 
}
