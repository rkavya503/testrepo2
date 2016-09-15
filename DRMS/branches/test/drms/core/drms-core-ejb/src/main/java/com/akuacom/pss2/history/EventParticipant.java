package com.akuacom.pss2.history;

import java.io.Serializable;
import java.util.Date;

public class EventParticipant implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String uuid;
	private String eventName;
	private Date startTime;
	private Date endTime;
	private String participant_uuid;
	private String participantName;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getParticipant_uuid() {
		return participant_uuid;
	}
	public void setParticipant_uuid(String participant_uuid) {
		this.participant_uuid = participant_uuid;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
}