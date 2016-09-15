package com.akuacom.pss2.history.vo;

import java.io.Serializable;

public class ReportEventParticipant implements Serializable {

	private static final long serialVersionUID = 4632152368079195295L;
	private String participantName;
	private Integer participation;
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public Integer getParticipation() {
		return participation;
	}
	public void setParticipation(Integer participation) {
		this.participation = participation;
	}

}
