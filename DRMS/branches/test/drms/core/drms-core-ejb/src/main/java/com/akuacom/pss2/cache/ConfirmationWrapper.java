package com.akuacom.pss2.cache;

import java.util.List;

public class ConfirmationWrapper {
	

	private long creationTime = 0;
	
	private String participantName = null;
	
	private List<Long> eventStateID = null;

	private static int OLD_THRESHOLD = 60 * 60 * 1000;
	
	public List<Long> getEventStateID() {
		return eventStateID;
	}

	public void setEventStateID(List<Long> eventStateID) {
		this.eventStateID = eventStateID;
	}

	public ConfirmationWrapper(String participantName,
			List<Long> eventStateID) {
		super();
		this.creationTime = System.currentTimeMillis();
		this.participantName = participantName;
		this.eventStateID = eventStateID;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	
	public boolean containsEventID(long eventID){
		boolean result = false;
		if(eventStateID != null && !eventStateID.isEmpty()){
			if(eventStateID.contains(eventID)){
				result = true;
			}
		}
		return result;
	}
	
	public boolean isOld(){
		boolean result = false;
		if(getCreationTime() < System.currentTimeMillis() - OLD_THRESHOLD){
			result = true;
		}
		return result;
	}


}
