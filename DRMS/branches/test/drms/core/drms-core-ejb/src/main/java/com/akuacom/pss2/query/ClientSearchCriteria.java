package com.akuacom.pss2.query;

import java.util.List;

import com.akuacom.pss2.util.EventStatus;

public class ClientSearchCriteria implements java.io.Serializable{

	private static final long serialVersionUID = 6537702420071421279L;
	
	private List<String> exactProgramNames;
	
	private String clientNameLeadingStr;
	
	private String parenLeadingStr;
	
	private List<String> programLeadingStr;
	private List<String> clientNameList;
	private List<String> participantNameList;
	
	private EventStatus eventStatus;
	
	//whether to fetch contacts of clients
	private boolean fetchContacts = false;
	
	//MIN_VALUE means all status
	private Integer commsStatus;
	
	//MIN_VALUE means all types
	private Integer clientType;

	public List<String> getExactProgramName() {
		return exactProgramNames;
	}

	public void setExactProgramName(List<String> exactProgramNames) {
		this.exactProgramNames = exactProgramNames;
	}

	public String getClientNameLeadingStr() {
		return clientNameLeadingStr;
	}

	public void setClientNameLeadingStr(String clientNameLeadingStr) {
		this.clientNameLeadingStr = clientNameLeadingStr;
	}

	public String getParenLeadingStr() {
		return parenLeadingStr;
	}

	public void setParenLeadingStr(String parenLeadingStr) {
		this.parenLeadingStr = parenLeadingStr;
	}

	public List<String> getProgramLeadingStr() {
		return programLeadingStr;
	}

	public void setProgramLeadingStr(List<String> programLeadingStr) {
		this.programLeadingStr = programLeadingStr;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public Integer getCommsStatus() {
		return commsStatus;
	}

	public void setCommsStatus(Integer commsStatus) {
		this.commsStatus = commsStatus;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public boolean isFetchContacts() {
		return fetchContacts;
	}

	public void setFetchContacts(boolean fetchContacts) {
		this.fetchContacts = fetchContacts;
	}

	/**
	 * @return the clientNameList
	 */
	public List<String> getClientNameList() {
		return clientNameList;
	}

	/**
	 * @param clientNameList the clientNameList to set
	 */
	public void setClientNameList(List<String> clientNameList) {
		this.clientNameList = clientNameList;
	}

	/**
	 * @return the participantNameList
	 */
	public List<String> getParticipantNameList() {
		return participantNameList;
	}

	/**
	 * @param participantNameList the participantNameList to set
	 */
	public void setParticipantNameList(List<String> participantNameList) {
		this.participantNameList = participantNameList;
	}
	
}
