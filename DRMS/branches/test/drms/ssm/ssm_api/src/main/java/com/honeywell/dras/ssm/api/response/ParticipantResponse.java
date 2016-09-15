package com.honeywell.dras.ssm.api.response;

import java.util.List;

public class ParticipantResponse {

	private boolean participantExist;
	private boolean accountNumberExist;
	private List<String> participantList;
	private List<String> accountNumberList;
	
	public boolean isParticipantExist() {
		return participantExist;
	}
	public void setParticipantExist(boolean participantExist) {
		this.participantExist = participantExist;
	}
	public boolean isAccountNumberExist() {
		return accountNumberExist;
	}
	public void setAccountNumberExist(boolean accountNumberExist) {
		this.accountNumberExist = accountNumberExist;
	}
	public List<String> getParticipantList() {
		return participantList;
	}
	public void setParticipantList(List<String> participantList) {
		this.participantList = participantList;
	}
	public List<String> getAccountNumberList() {
		return accountNumberList;
	}
	public void setAccountNumberList(List<String> accountNumberList) {
		this.accountNumberList = accountNumberList;
	}
	
	
}
