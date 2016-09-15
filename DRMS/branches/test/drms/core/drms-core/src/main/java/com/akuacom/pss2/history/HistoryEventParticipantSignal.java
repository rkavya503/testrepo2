/**
 * 
 */
package com.akuacom.pss2.history;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.akuacom.ejb.BaseEntity;

/**
 * The Entity Bean HistoryEventParticipantSignal.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "history_event_participant_signal")
public class HistoryEventParticipantSignal extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "history_event_participant_uuid")
    @NotFound(action=NotFoundAction.IGNORE)
    private HistoryEventParticipant eventParticipant=null;
    
	private String eventName;
	
    @Column(name = "participant_uuid")
	private String participant;

    private String participantName;
    
    private String signalName;
    
    private Date signalTime;
    
    private Date signalEndTime;
    
    private String signalValue;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public Date getSignalTime() {
		return signalTime;
	}

	public void setSignalTime(Date signalTime) {
		this.signalTime = signalTime;
	}

	public Date getSignalEndTime() {
		return signalEndTime;
	}

	public void setSignalEndTime(Date signalEndTime) {
		this.signalEndTime = signalEndTime;
	}

	public String getSignalValue() {
		return signalValue;
	}

	public void setSignalValue(String signalValue) {
		this.signalValue = signalValue;
	}

	public HistoryEventParticipant getEventParticipant() {
		return eventParticipant;
	}

	public void setEventParticipant(HistoryEventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}
}
