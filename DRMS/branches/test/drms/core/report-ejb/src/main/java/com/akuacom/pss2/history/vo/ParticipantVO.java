package com.akuacom.pss2.history.vo;

import java.io.Serializable;

public class ParticipantVO implements Serializable{

	private static final long serialVersionUID = -4415337735442404292L;
	
	private String UUID;
	
	private String participantName;

	public String getUUID() {
		return UUID;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
}
