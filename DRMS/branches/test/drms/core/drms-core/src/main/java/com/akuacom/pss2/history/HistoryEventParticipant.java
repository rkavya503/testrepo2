/**
 * 
 */
package com.akuacom.pss2.history;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.akuacom.ejb.BaseEntity;

/**
 * The Entity Bean HistoryEventParticipant.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "history_event_participant")
@NamedQueries( {
        @NamedQuery(name = "HistoryEventParticipant.findProgramOptoutParticipant",
                query = "select p from HistoryEventParticipant p where p.eventName=:eventName and p.event is null"),
        @NamedQuery(name = "HistoryEventParticipant.findAllByProgramName",
                query = "select p from HistoryEventParticipant p where p.eventName like :programName"),
        @NamedQuery(name = "HistoryEventParticipant.findByParticipantDate",
        		query = "select p from HistoryEventParticipant p where p.participantName = :participantName and p.client=false " +
         	 	 	 	"and p.startTime >=:startTime and p.startTime <=:endTime"),
        @NamedQuery(name = "HistoryEventParticipant.findParticipantByEventName",
        		query = "select p from HistoryEventParticipant p where p.eventName=:eventName and p.client=false")})
public class HistoryEventParticipant extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "history_event_uuid")
    @NotFound(action=NotFoundAction.IGNORE)
    private HistoryEvent event=null;
	
	/** The event name. */
    private String eventName;

    @Column(name = "participant_uuid")
	private String participant;

    private String participantName;
    
    private Integer participation;

    private Double percentage;
    private Double offlinePerEvent;

	private Date startTime;
	private Date endTime;

    private Double averageShed;
    private Double totalShed;
    
    private Double registeredShed;
    
	private Boolean client;
	
	private String parent;
    
    @OneToMany(mappedBy = "eventParticipant", cascade = {CascadeType.ALL})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<HistoryEventParticipantSignal> signals = new HashSet<HistoryEventParticipantSignal>();
    
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

	public Boolean getClient() {
		return client;
	}

	public void setClient(Boolean client) {
		this.client = client;
	}

	public HistoryEvent getEvent() {
		return event;
	}

	public void setEvent(HistoryEvent event) {
		this.event = event;
	}

	public Set<HistoryEventParticipantSignal> getSignals() {
		return signals;
	}

	public void setSignals(Set<HistoryEventParticipantSignal> signals) {
		this.signals = signals;
	}

	public Integer getParticipation() {
		return participation;
	}

	public void setParticipation(Integer participation) {
		this.participation = participation;
		
		if (participation.intValue() == ClientParticipationStatus.EVENT_COMPLETED.getValue() ||
				participation.intValue() == ClientParticipationStatus.ACTIVE_EVENT_CANCELLED.getValue())
			percentage=1.00;
		else if (participation.intValue() != ClientParticipationStatus.ACTIVE_EVENT_OPT_OUT.getValue())
			percentage=0.00;
	}
	
	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getAverageShed() {
		return averageShed;
	}

	public void setAverageShed(Double averageShed) {
		this.averageShed = averageShed;
	}

	public Double getTotalShed() {
		return totalShed;
	}

	public void setTotalShed(Double totalShed) {
		this.totalShed = totalShed;
	}
	

	public Double getOfflinePerEvent() {
		return offlinePerEvent;
	}
	public void setOfflinePerEvent(Double offlinePerEvent) {
		this.offlinePerEvent = offlinePerEvent;
	}
	
	public Double getRegisteredShed() {
		return registeredShed;
	}

	public void setRegisteredShed(Double registeredShed) {
		this.registeredShed = registeredShed;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
}
