package com.akuacom.pss2.query;

import java.io.Serializable;

public class EvtClientCandidate implements Serializable{

	private static final long serialVersionUID = -6014804181644083804L;
	
	private String clientName;
	private String clientUUID;
	private String parent;
	private String account;
	private boolean client = true;
	
	public EvtClientCandidate() {
		
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	
	public String getClientUUID() {
		return clientUUID;
	}

	public void setClientUUID(String clientUUID) {
		this.clientUUID = clientUUID;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}
	
}
