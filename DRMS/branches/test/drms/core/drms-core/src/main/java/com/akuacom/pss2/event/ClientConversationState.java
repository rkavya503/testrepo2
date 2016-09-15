/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ClientConversationStateEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.event;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;

/*
 * ClientConversationState is persisted when an ClientConversationState is sent to the client
 * AND a Confirmation is expected.  
 * 
 * This ClientConversationState information is retrieved when the Confirmation comes in
 * and is used to validate the Confirmation message.
 * 
 * @see <a href="http://openadr.lbl.gov/src/1/ClientConversationState.xsd>ClientConversationState XSD</a>
 */

/**
 * The Class ClientConversationStateDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name="client_conversation_state")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "ClientConversationState.findByConversationStateId",
        	query = "select es from ClientConversationState es where es.conversationStateId = :stateId",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ClientConversationState.findByDrasClientId",
            query = "select es from ClientConversationState es where es.drasClientId = :clientId",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ClientConversationState.findByTimedOut",
            query = "select es from ClientConversationState es where es.commTime < :timeOut",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ClientConversationState.findPushClientByTimedOut",
            query = "select es from ClientConversationState es where es.push = :push and es.commTime < :timeOut",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class ClientConversationState extends BaseEntity {

	private static final long serialVersionUID = -3806168535222814312L;

	/** 
	 * Identifier for the DR event that was created when the DR event was first 
	 * issued. If the EventStatus field is NONE indicating that there is no 
	 * pending or active DR event then this field is not applicable.
	 */
	private String eventIdentifier;
	
	/** 
	 * Identifier of the program that this DR event was issued for.If the 
	 * EventStatus field is NONE indicating that there is no pending or active 
	 * DR event then this field is not applicable.
	 */
	private String programName;
	
	/** 
	 * Identifier of the DRAS client that this ClientConversationState is being sent to.
	 */
	private String drasClientId;
    
    /** 
     * Modification number of the DR event.  Used to indicate if the DR Event 
     * has been modified by the Utility.  Each time it is modified this 
     * number is incremented. If the EventStatus field is NONE indicating that 
     * there is no pending or active DR event then this field is not applicable.
     */
    private int eventModNumber;
    
    /** 
     * This is a transaction ID that is guaranteed to be unique for each 
     * instance of the ClientConversationState that is generated and sent to the DRAS Client.
     * It is used by the DRAS Client to confirm reception of the ClientConversationState from the DRAS.
     */
    private int conversationStateId;
    @Transient 
    private  String compactEventIds;
    
    @Transient 
    private  String compactEventStatus;
    
    @Transient 
    private  String compactOperationModes;
    
    /** The comm time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date commTime;

    /**
     * Indicates that the record was for a push
     */
    private boolean push;

	/** The event status. */
	private EventStatus eventStatus;

	/** The operation mode value. */
	private OperationModeValue operationModeValue;
    

	/**
	 * Gets the event identifier.
	 * 
	 * @return the event identifier
	 */
	public String getEventIdentifier() {
		return eventIdentifier;
	}
	
	/**
	 * Sets the event identifier.
	 * 
	 * @param eventIdentifier the new event identifier
	 */
	public void setEventIdentifier(String eventIdentifier) {
		this.eventIdentifier = eventIdentifier;
	}
	
	/**
	 * Gets the program name.
	 * 
	 * @return the program name
	 */
	public String getProgramName() {
		return programName;
	}
	
	/**
	 * Sets the program name.
	 * 
	 * @param programName the new program name
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	/**
	 * Gets the dras client id.
	 * 
	 * @return the dras client id
	 */
	public String getDrasClientId() {
		return drasClientId;
	}
	
	/**
	 * Sets the dras client id.
	 * 
	 * @param drasClientId the new dras client id
	 */
	public void setDrasClientId(String drasClientId) {
		this.drasClientId = drasClientId;
	}
	
	/**
	 * Gets the event mod number.
	 * 
	 * @return the event mod number
	 */
	public int getEventModNumber() {
		return eventModNumber;
	}
	
	/**
	 * Sets the event mod number.
	 * 
	 * @param eventModNumber the new event mod number
	 */
	public void setEventModNumber(int eventModNumber) {
		this.eventModNumber = eventModNumber;
	}
	
	/**
	 * Gets the event state id.
	 * 
	 * @return the event state id
	 */
	public int getConversationStateId() {
		return conversationStateId;
	}
	
	/**
	 * Sets the event state id.
	 * 
	 * @param conversationStateId the new event state id
	 */
	public void setConversationStateId(int conversationStateId) {
		this.conversationStateId = conversationStateId;
	}
	
	/**
	 * The time the ClientConversationState was created and sent
	 * 
	 * @return creation time
	 */
	public Date getCommTime() {
		return commTime;
	}

	/**
	 * The time the ClientConversationState was created and sent
	 * 
	 * @param commTime comm time
	 */
	public void setCommTime(Date commTime) {
		this.commTime = commTime;
	}
	
	public boolean isPush()
	{
		return push;
	}

	public void setPush(boolean push)
	{
		this.push = push;
	}

	/**
	 * Gets the event status.
	 *
	 * @return the event status
	 */
	public EventStatus getEventStatus()
	{
		return eventStatus;
	}

	/**
	 * Sets the event status.
	 *
	 * @param eventStatus the new event status
	 */
	public void setEventStatus(EventStatus eventStatus)
	{
		this.eventStatus = eventStatus;
	}

	/**
	 * Gets the operation mode value.
	 *
	 * @return the operation mode value
	 */
	public OperationModeValue getOperationModeValue()
	{
		return operationModeValue;
	}

	/**
	 * Sets the operation mode value.
	 *
	 * @param operationModeValue the new operation mode value
	 */
	public void setOperationModeValue(OperationModeValue operationModeValue)
	{
		this.operationModeValue = operationModeValue;
	}
	public String getCompactEventStatus() {
		return compactEventStatus;
	}

	public void setCompactEventStatus(String conpactEventStatus) {
		this.compactEventStatus = conpactEventStatus;
	}

	public String getCompactOperationModes() {
		return compactOperationModes;
	}

	public void setCompactOperationModes(String compactOperationModes) {
		this.compactOperationModes = compactOperationModes;
	}

	public String getCompactEventIds() {
		return compactEventIds;
	}

	public void setCompactEventIds(String compactEventIds) {
		this.compactEventIds = compactEventIds;
	}

	@Override
	public String toString() {
		return "ClientConversationState [eventIdentifier=" + eventIdentifier
				+ ", programName=" + programName + ", drasClientId="
				+ drasClientId + ", eventModNumber=" + eventModNumber
				+ ", conversationStateId=" + conversationStateId
				+ ", compactEventIds=" + compactEventIds
				+ ", compactEventStatus=" + compactEventStatus
				+ ", compactOperationModes=" + compactOperationModes
				+ ", commTime=" + commTime + ", push=" + push
				+ ", eventStatus=" + eventStatus + ", operationModeValue="
				+ operationModeValue + "]";
	}
}
