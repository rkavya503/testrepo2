package com.akuacom.pss2.openadr2.opt;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akuacom.ejb.VersionedEntity;


@Entity
@Table(name="optavailability")
public class OptAvailability extends VersionedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="optrequestuuid")
	private OptRequest optRequest;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	private Long duration;	
			
	public OptRequest getOptRequest() {
		return optRequest;
	}
	public void setOptRequest(OptRequest optRequest) {
		this.optRequest = optRequest;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	

}
