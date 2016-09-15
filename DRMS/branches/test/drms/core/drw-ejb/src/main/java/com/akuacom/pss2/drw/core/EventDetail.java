/**
 * 
 */
package com.akuacom.pss2.drw.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * the entity EventDetail
 */
@Entity
@Table(name = "event_detail")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@NamedQueries( {
	@NamedQuery(name = "EventDetail.getActiveEvent", 
			query = "select e from EventDetail e where e.event.programName = :programName and (e.actualEndTime = null or e.actualEndTime > :endTime)"),
	@NamedQuery(name = "EventDetail.getHistoryEvent", 
			query = "select e from EventDetail e where e.event.programName = :programName " +
					"and (e.actualEndTime != null and e.actualEndTime >= :start and e.actualEndTime <= :end)"),
	@NamedQuery(name = "EventDetail.getActiveEventByRate", 
			query = "select e from EventDetail e where e.event.programName = :programName " +
					"and e.event.product in (:product) and (e.actualEndTime = null or e.actualEndTime > :endTime)"),
	@NamedQuery(name = "EventDetail.getHistoryEventByEnd", 
			query = "select e from EventDetail e where e.event.programName = :programName " +
					"and (e.actualEndTime is not null and date(e.actualEndTime) = date(:endTime))"),	
	@NamedQuery(name = "EventDetail.getHistoryEventByStart", 
			query = "select e from EventDetail e where e.event.programName = :programName " +
					"and date(e.event.startTime) = date(:startTime) and e.actualEndTime is not null and e.actualEndTime <= current_timestamp"),	
	@NamedQuery(name = "EventDetail.getEventByEventDetail", 
			query = "select ed.event from EventDetail ed where ed.UUID IN (:eventDetails) "),	
	@NamedQuery(name = "EventDetail.getActiveEventByEventDetail", 
			query = "select ed.event from EventDetail ed where ed.UUID IN (:eventDetails) AND (ed.actualEndTime = null OR ed.actualEndTime > NOW()) "),	
	@NamedQuery(name = "EventDetail.getActiveEventCountsByPrograms", 
			query = "select count(e) from EventDetail e where e.event.programName IN (:programNames) and (e.actualEndTime = null or e.actualEndTime > :endTime)"),
	@NamedQuery(name = "EventDetail.getHistoryEventByRate", 
			query = "select e from EventDetail e where e.event.programName = :programName " +
					"and e.event.product in (:product) and (e.actualEndTime != null and e.actualEndTime >= :start and e.actualEndTime <= :end)"),
	@NamedQuery(name = "EventDetail.getEventByEventName", 
			query = "select e.UUID from EventDetail e where e.event.eventName = :eventName")
	})
public class EventDetail extends AbstractApplicationEntity {

	private static final long serialVersionUID = -5670031218905720226L;

	private Date estimatedEndTime;
	private Date actualEndTime;
	
	@Temporal( TemporalType.TIMESTAMP)
	private Date lastModifiedTime;
	
    @OneToOne
    @JoinColumn(name = "locationID")
	private Location location;
    
    private String allLocationType;
    
	private String blockNames;
	
    @ManyToOne
    @JoinColumn(name = "event_uuid")
    private Event event;
	
    @OneToMany(mappedBy = "eventDetail", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<ZipCodeEntry> zipCodeEntries=new HashSet<ZipCodeEntry>();
    
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Date getEstimatedEndTime() {
		return estimatedEndTime;
	}
	public void setEstimatedEndTime(Date estimatedEndTime) {
		this.estimatedEndTime = estimatedEndTime;
	}
	public Date getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public String getAllLocationType() {
		return allLocationType;
	}
	public void setAllLocationType(String allLocationType) {
		this.allLocationType = allLocationType;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getBlockNames() {
		return blockNames;
	}
	public void setBlockNames(String blockNames) {
		this.blockNames = blockNames;
	}
	public Set<ZipCodeEntry> getZipCodeEntries() {
		return zipCodeEntries;
	}
	public void setZipCodeEntries(Set<ZipCodeEntry> zipCodeEntries) {
		this.zipCodeEntries = zipCodeEntries;
	}
}
