package com.honeywell.dras.vtn.api.event;

import com.honeywell.dras.vtn.api.OptType;

public class EventResponse {
	
	private String responseCode;
	private OptType optType;
	private String description;
	private String requestID;
	private String eventID;
	private long modificationNumber;
	
	
	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the optType
	 */
	public OptType getOptType() {
		return optType;
	}
	/**
	 * @param optType the optType to set
	 */
	public void setOptType(OptType optType) {
		this.optType = optType;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the requestID
	 */
	public String getRequestID() {
		return requestID;
	}
	/**
	 * @param requestID the requestID to set
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	/**
	 * @return the eventID
	 */
	public String getEventID() {
		return eventID;
	}
	/**
	 * @param eventID the eventID to set
	 */
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	/**
	 * @return the modificationNumber
	 */
	public long getModificationNumber() {
		return modificationNumber;
	}
	/**
	 * @param modificationNumber the modificationNumber to set
	 */
	public void setModificationNumber(long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

}
