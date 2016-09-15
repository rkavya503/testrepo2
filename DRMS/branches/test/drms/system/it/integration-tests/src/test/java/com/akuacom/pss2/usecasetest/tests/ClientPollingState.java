package com.akuacom.pss2.usecasetest.tests;

public class ClientPollingState {
	private String eventStatus = "NOT SET";
	private String operationMode = "NOT SET";
	private String confirmationStatus = "NOT SET";
	
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getConfirmationStatus() {
		return confirmationStatus;
	}
	public void setConfirmationStatus(String confirmationStatus) {
		this.confirmationStatus = confirmationStatus;
	}

	public boolean isGoodNormal() {
		return "NONE".equals(getEventStatus()) 
			&& "NORMAL".equals(getOperationMode()) 
			&& "SUCCESS".equals(getConfirmationStatus());
	}
	
	@Override
	public String toString() {
		return "ClientPollingState [eventStatus=" + eventStatus
				+ ", operationMode=" + operationMode + ", confirmationStatus="
				+ confirmationStatus + "]";
	}
}
