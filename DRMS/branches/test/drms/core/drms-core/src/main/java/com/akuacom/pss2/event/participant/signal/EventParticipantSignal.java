package com.akuacom.pss2.event.participant.signal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
//@BatchSize(size=100)
@Table(name = "event_participant_signal")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)

public class EventParticipantSignal extends VersionedEntity implements Signal {

	private static final long serialVersionUID = 3890452856551494924L;
	
	/** The signal. */
    @ManyToOne
    @JoinColumn(name = "signal_def_uuid")
    private SignalDef signalDef;
    
    /**
     * Event Participant
     */
    @ManyToOne
    @JoinColumn(name = "event_participant_uuid")
    private EventParticipant eventParticipant;
    
    /**
     * Signal Entries
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "eventParticipantSignal")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<EventParticipantSignalEntry> eventParticipantSignalEntries;

    /**
     * Fetches the event participant
     * @return EventParticipant
     */
	public EventParticipant getEventParticipant() {
		return eventParticipant;
	}

    /**
     * Sets the EventParticipant
     * @param eventParticipant event participant
     */
	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;

	}

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

	

    public Set<EventParticipantSignalEntry> getEventParticipantSignalEntries() {
		return this.eventParticipantSignalEntries;
	}

	public void setEventParticipantSignalEntries(Set<EventParticipantSignalEntry> eventParticipantSignalEntries) {
		this.eventParticipantSignalEntries = eventParticipantSignalEntries;
		if (this.eventParticipant != null) {
			this.eventParticipant.setEventModNumber(this.eventParticipant.getEventModNumber() + 1);
		}
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
		Set<EventParticipantSignalEntry> newSE = new HashSet<EventParticipantSignalEntry>();
		
		for (SignalEntry se : eventParticipantSignalEntries) {
			if (se instanceof EventParticipantSignalEntry) {
				newSE.add((EventParticipantSignalEntry)se);
			} else {
				throw new RuntimeException("Only EventParticipantSignalEntry may be added to EventParticipantSignal signal entries");
			}
		}
		
		setEventParticipantSignalEntries(newSE);
	}
}
