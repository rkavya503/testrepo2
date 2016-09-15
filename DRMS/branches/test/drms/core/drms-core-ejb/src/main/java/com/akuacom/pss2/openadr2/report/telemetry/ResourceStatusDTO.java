package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.Date;

public class ResourceStatusDTO {
	private String participantName;
	private String resourceName;
	private String venId;
	private Date reportedTime;
	private Boolean onlineStatus;
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public Date getReportedTime() {
		return reportedTime;
	}
	public void setReportedTime(Date reportedTime) {
		this.reportedTime = reportedTime;
	}
	public Boolean getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
}
