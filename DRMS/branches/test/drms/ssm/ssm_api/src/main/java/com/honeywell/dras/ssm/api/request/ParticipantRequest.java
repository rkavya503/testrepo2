package com.honeywell.dras.ssm.api.request;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ParticipantRequest {

	private String participantName;
	private String accountNumber;
	private List<String> participantList;
	private List<String> serviceAccList;
	private boolean reqIdExists;
	
	public List<String> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<String> participantList) {
		this.participantList = participantList;
	}
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<String> getServiceAccList() {
		return serviceAccList;
	}

	public void setServiceAccList(List<String> serviceAccList) {
		this.serviceAccList = serviceAccList;
	}

	public boolean isReqIdExists() {
		return reqIdExists;
	}

	public void setReqIdExists(boolean reqIdExists) {
		this.reqIdExists = reqIdExists;
	}
	
}
