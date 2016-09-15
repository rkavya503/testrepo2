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

/**
 * the entity Event
 */
@Entity
@Table(name = "event")
@NamedQueries( {
	@NamedQuery(name = "Event.getByEventName", 
		query = "select e from Event e where e.eventName = :eventName")
})
public class Event extends AbstractApplicationEntity {

	private static final long serialVersionUID = -4591142128980053717L;

	private Date startTime;
	private Date issuedTime;
	private String product;
	private String comment;
	
    private String programName;
	
    private String eventName;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<EventDetail> details=new HashSet<EventDetail>();
    
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Set<EventDetail> getDetails() {
		return details;
	}
	public void setDetails(Set<EventDetail> details) {
		this.details = details;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}
	
}
