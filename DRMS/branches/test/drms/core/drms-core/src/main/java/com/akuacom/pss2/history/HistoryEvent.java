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
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
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
 * The Entity Bean HistoryEvent.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "history_event")
@NamedQueries( {
	@NamedQuery(name = "HistoryEvent.findByDate",
		        query = "select e from HistoryEvent e where (e.startTime >= :startTime and e.endTime <= :endTime)",
		        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),     
    @NamedQuery(name = "HistoryEvent.findByProgramAndDate",
        	query = "select e from HistoryEvent e where (e.startTime >= :startTime and e.endTime <= :endTime) and e.programName= :programName",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "HistoryEvent.findByParticipantAndDate",
        	query = "select e from HistoryEvent e, in (e.eventParticipants) ep where e.startTime >= :startTime and e.endTime <= :endTime and ep.participantName in (:participantNames)",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "HistoryEvent.findByParticipantProgramAndDate",
        	query = "select e from HistoryEvent e, in (e.eventParticipants) ep where ep.participation<>40 and e.startTime >= :startTime and e.endTime <= :endTime and ep.participantName in (:participantNames) and e.programName not in (:programNames) order by e.startTime",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "HistoryEvent.findByAggregatorProgramAndDate",
        	query = "select e from HistoryEvent e, in (e.eventParticipants) ep where ep.participation<>40 and e.startTime >= :startTime and e.endTime <= :endTime and ep.participantName in (:participantNames) and e.programName in (:programNames) order by e.startTime",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "HistoryEvent.findByEventName.single",
            query = "select e from HistoryEvent e where e.eventName=:eventName")})
public class HistoryEvent extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "program_uuid")
	private String program;
	private String programName;
	
	private Date issueTime;
    
	private String eventName;

	// the actual start time
	private Date startTime;
	
	// the actual end time
	private Date endTime;
	
	// the scheduled start time
	private Date scheduledStartTime;
	
	// the scheduled end time
	private Date scheduledEndTime;
	
	// the canceled flag
	boolean cancelled;
	
	private Integer numProgramParticipants;
	
	private Double registeredShed;
	
	private Integer numUsageEnabled;
	
	private Double registeredUsageShed;
	
    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<HistoryEventParticipant> eventParticipants = new HashSet<HistoryEventParticipant>();

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Date getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(Date scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public Date getScheduledEndTime() {
		return scheduledEndTime;
	}

	public void setScheduledEndTime(Date scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
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

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Set<HistoryEventParticipant> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(Set<HistoryEventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	public Integer getNumProgramParticipants() {
		return numProgramParticipants;
	}

	public void setNumProgramParticipants(Integer numProgramParticipants) {
		this.numProgramParticipants = numProgramParticipants;
	}

	public Double getRegisteredShed() {
		return registeredShed;
	}

	public void setRegisteredShed(Double registeredShed) {
		this.registeredShed = registeredShed;
	}

	public Integer getNumUsageEnabled() {
		return numUsageEnabled;
	}

	public void setNumUsageEnabled(Integer numUsageEnabled) {
		this.numUsageEnabled = numUsageEnabled;
	}

	public Double getRegisteredUsageShed() {
		return registeredUsageShed;
	}

	public void setRegisteredUsageShed(Double registeredUsageShed) {
		this.registeredUsageShed = registeredUsageShed;
	}
}
