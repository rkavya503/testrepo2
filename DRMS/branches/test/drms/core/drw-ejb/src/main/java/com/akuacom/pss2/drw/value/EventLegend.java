package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.Date;

public class EventLegend implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	private String event_uuid;
	private Date startTime;
	private Date estimatedEndTime;
	private Date actualEndTime;
	private Date issueTime;
	private String eventlocations;
	private String startTimeStr;
	private String endtime;
	private String eventKey;
//	public String getEvent_uuid() {
//		return event_uuid;
//	}
//	public void setEvent_uuid(String event_uuid) {
//		this.event_uuid = event_uuid;
//	}
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
	public String getEventlocations() {
		return eventlocations;
	}
	public void setEventlocations(String eventlocations) {
		this.eventlocations = eventlocations;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	/**
	 * @return the issueTime
	 */
	public Date getIssueTime() {
		return issueTime;
	}
	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

}
