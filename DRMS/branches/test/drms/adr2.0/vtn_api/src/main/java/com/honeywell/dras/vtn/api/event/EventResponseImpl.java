package com.honeywell.dras.vtn.api.event;

import com.honeywell.dras.vtn.api.EventResponse;
import com.honeywell.dras.vtn.api.OptType;

public class EventResponseImpl implements EventResponse {
	
	private String responseCode;
	
	private OptType optType;
	
	private String description;
	
	private String requestID;

	private String eventID;

	private long modificationNumber;
	

	public long getModificationNumber() {
		return modificationNumber;
	}
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public OptType getOptType() {
		return optType;
	}

	public void setOptType(OptType optType) {
		this.optType = optType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public void setModificationNumber(long modificationNumber) {
		this.modificationNumber = modificationNumber; 
	}
	
	

}
