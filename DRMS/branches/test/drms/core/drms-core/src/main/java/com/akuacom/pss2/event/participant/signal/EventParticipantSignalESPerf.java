package com.akuacom.pss2.event.participant.signal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.event.participant.EventParticipantESPerf;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;

/**
 * ESPerf family designed to optimize client polling for Event State
 *
 */
@Entity
@Table(name = "event_participant_signal")
public class EventParticipantSignalESPerf extends VersionedEntity implements Signal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1382197971883541248L;


	/** The signal. */
    @ManyToOne
    @JoinColumn(name = "signal_def_uuid")
    private SignalDef signalDef;
    
        
    /**
     * Signal Entries
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "eventParticipantSignal")
    private Set<EventParticipantSignalEntryESPerf> eventParticipantSignalEntries;

    /**
     * Gets the signal.
     * 
     * @return the signal
     */
    public SignalDef getSignalDef() {
        return signalDef;
    }

    /**
     * Sets the signal.
     * 
     * @param signal the new signal
     */
    public void setSignalDef(SignalDef signalDef) {
        this.signalDef = signalDef;
    }

    @ManyToOne
    @JoinColumn(name = "event_participant_uuid")
    private EventParticipantESPerf eventParticipant;
	

    public EventParticipantESPerf getEventParticipant() {
		return eventParticipant;
	}

	public void setEventParticipant(EventParticipantESPerf eventParticipant) {
		this.eventParticipant = eventParticipant;
	}

	public Set<EventParticipantSignalEntryESPerf> getEventParticipantSignalEntries() {
		return this.eventParticipantSignalEntries;
	}

	public void setEventParticipantSignalEntries(Set<EventParticipantSignalEntryESPerf> eventParticipantSignalEntries) {
		this.eventParticipantSignalEntries = eventParticipantSignalEntries;
	}
    
    @Override
    public Set<? extends SignalEntry> getSignalEntries() {
		return this.getEventParticipantSignalEntries();
	}


    /**
     * Sets the signal entries
     */
    @Override
    public void setSignalEntries(Set<? extends SignalEntry> eventParticipantSignalEntries) {
		Set<EventParticipantSignalEntryESPerf> newSE = new HashSet<EventParticipantSignalEntryESPerf>();
		
		for (SignalEntry se : eventParticipantSignalEntries) {
			if (se instanceof EventParticipantSignalEntryESPerf) {
				newSE.add((EventParticipantSignalEntryESPerf)se);
			} else {
				throw new RuntimeException("Only EventParticipantSignalEntry may be added to EventParticipantSignal signal entries");
			}
		}
		
		setEventParticipantSignalEntries(newSE);
	}
}
