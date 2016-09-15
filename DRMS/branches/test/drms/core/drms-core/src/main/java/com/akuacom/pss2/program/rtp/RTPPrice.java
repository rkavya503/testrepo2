package com.akuacom.pss2.program.rtp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "rtp_remote_price")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "RTPPrice.findLatest", 
        query = "select r from RTPPrice r where r.programName = :program order by r.intervalTime desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "RTPPrice.findCurrent",
    	query = "select r from RTPPrice r where r.programName = :program and r.intervalTime > :now order by r.intervalTime desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})

public class RTPPrice extends VersionedEntity {

	private static final long serialVersionUID = -2686133270300640134L;

	private Double price;

    @Temporal( TemporalType.TIMESTAMP)
	private Date intervalTime;

	private String programName;

	private String eventName;

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Date intervalTime) {
		this.intervalTime = intervalTime;
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
}
