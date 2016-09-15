package com.akuacom.pss2.openadr2.event;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.VersionedEntity;


@Entity
@Table(name="eventrequest")
@NamedQueries({ 
	@NamedQuery(name = "EventRequest.findByEventEntityId", query = "SELECT e FROM EventRequest e WHERE e.eventId = :eventId"),
	@NamedQuery(name = "EventRequest.findByRequestAndVen", query = "SELECT e FROM EventRequest e WHERE e.requestId = :requestId and e.venId = :venId order by creationTime desc"),
	@NamedQuery(name = "EventRequest.findByRequestId", query = "SELECT e FROM EventRequest e WHERE e.requestId = :requestId"),
	@NamedQuery(name = "EventRequest.findByRequestIdAndEventId", query = "SELECT e FROM EventRequest e WHERE e.requestId = :requestId and e.eventId = :eventId "),
	@NamedQuery(name = "EventRequest.findByRequestAndEventAndVen", query = "SELECT e FROM EventRequest e WHERE e.requestId = :requestId and e.venId = :venId and e.eventId = :eventId order by e.eventModificationNumber desc")	
})


public class EventRequest extends VersionedEntity  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7728889238613427756L;
	private String venId;
	private String requestId;
	private String eventId; 
	private String clientId; 
	private long eventModificationNumber;
	private String clientUuid; 	
	private String eventStatus;
	private String clientOperationModeValue;
	
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public long getEventModificationNumber() {
		return eventModificationNumber;
	}
	public void setEventModificationNumber(long eventModNumber) {
		this.eventModificationNumber = eventModNumber;
	}
	public String getClientUuid() {
		return clientUuid;
	}
	public void setClientUuid(String clientUuid) {
		this.clientUuid = clientUuid;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getClientOperationModeValue() {
		return clientOperationModeValue;
	}
	public void setClientOperationModeValue(String clientOperationModeValue) {
		this.clientOperationModeValue = clientOperationModeValue;
	}
	
	
	

}
