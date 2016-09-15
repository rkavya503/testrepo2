/**
 * 
 */
package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.query.EvtClientCandidate;

/**
 *
 */
public class ReportEvent implements Serializable {

	private static final long serialVersionUID = -2451558092801240687L;
	
	private List<ReportEventParticipant> participants;
	String programName;
	String eventName;
	Date startTime;
	Date endTime;
	Date scheduledStartTime;
	Date scheduledEndTime;
	Boolean cancelled;
	private Integer rowCount;
	
	public ReportEvent(){
		
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
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public List<ReportEventParticipant> getParticipants() {
		if(participants==null)
			participants = new ArrayList<ReportEventParticipant>();
		return participants;
	}

	public void setParticipants(List<ReportEventParticipant> participants) {
		this.participants = participants;
	}
	
}
