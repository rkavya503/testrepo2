package com.akuacom.pss2.query;

import java.io.Serializable;

public class EventParticipantSummary implements Serializable {
	
	private static final long serialVersionUID = 8810534662749603727L;
	
	private String participantName;
	private double totalShed;
	private String account;
	private int clientCount;
	private int valieClientCount;
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public double getAvailableShed() {
		if(totalShed==0 || clientCount == 0 || valieClientCount==0)
			return 0;
		return totalShed*valieClientCount/clientCount;
	}
	public double getTotalShed() {
		return totalShed;
	}
	public void setTotalShed(double totalShed) {
		this.totalShed = totalShed;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	public int getValieClientCount() {
		return valieClientCount;
	}
	public void setValieClientCount(int valieClientCount) {
		this.valieClientCount = valieClientCount;
	}
	
}
