/**
 * 
 */
package com.akuacom.pss2.history;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.utils.lang.TimingUtil;

/**
 * The Entity Bean ClientStatus.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "client_status")
@NamedQueries( {
        @NamedQuery(name = "ClientStatus.findPreviousRecords",
                query = "select c from ClientStatus c where c.client=:client and c.status=:status and endTime is null order by startTime"),
        @NamedQuery(name = "ClientStatus.findOpenRecords",
                        query = "select c from ClientStatus c where c.client=:client and endTime is null order by startTime"),
        @NamedQuery(name = "ClientStatus.findClientStatus",
                query = "select c from ClientStatus c where c.client=:client and c.status=:status and (c.endTime>:start or c.endTime is null) and c.startTime<:end order by c.startTime")})
public class ClientStatus extends BaseEntity {

	private static final long serialVersionUID = 1L;

    @Column(name = "client_uuid")
	private String client;
	
    private String clientName;
    
    private boolean status;
    
    /** The start time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date startTime;
    
    /** The end time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date endTime;
    
    private int duration;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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
		
		duration= (int) ((endTime.getTime()-startTime.getTime())/TimingUtil.MINUTE_MS);		
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
