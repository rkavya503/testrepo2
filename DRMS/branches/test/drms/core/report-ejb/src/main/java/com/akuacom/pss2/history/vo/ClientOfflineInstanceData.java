package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.Date;

public class ClientOfflineInstanceData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String participantName;
	private Date startTime;
	private Integer duration;
	private int offlineTillNow = 0;
	
	public ClientOfflineInstanceData(){
		
	}
	
	public ClientOfflineInstanceData(String participantName, Date startTime,
			Integer duration) {
		super();
		this.participantName = participantName;
		this.startTime = startTime;
		this.duration = duration;
	}
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public boolean isOfflineNow() {
		return  (offlineTillNow == 1);
	}

	public void setOfflineTillNow(int offlineTillNow) {
		this.offlineTillNow =offlineTillNow;
	}

	public int getOfflineTillNow() {
		return offlineTillNow;
	}
	
}
