package com.akuacom.pss2.cache;

import java.io.Serializable;

import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;

public class ConfirmationResult implements Serializable {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = -8362780626121096572L;

	private boolean confirmation;
	
	private String eventState = EventStatus.NONE.toString();
	
	private String operationModeValue = OperationModeValue.UNKNOWN.toString();

	public ConfirmationResult(boolean confirmation, String eventState,
			String operationModeValue) {
		super();
		this.confirmation = confirmation;
		if(eventState != null){
			this.eventState = eventState;
		}
		if(operationModeValue != null){
			this.operationModeValue = operationModeValue;
		}
		
	}

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public String getOperationModeValue() {
		return operationModeValue;
	}

	public void setOperationModeValue(String operationModeValue) {
		this.operationModeValue = operationModeValue;
	}
}
