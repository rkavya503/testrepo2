package com.honeywell.dras.vtn.api.common;

import java.util.List;

public class Target {
	
	private List<String> groupIdList;
	
	private List<String> partyIdList;
	
	private List<String> resourceIdList;
	
	private List<String> venIdList;

	public List<String> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}

	public List<String> getPartyIdList() {
		return partyIdList;
	}

	public void setPartyIdList(List<String> partyIdList) {
		this.partyIdList = partyIdList;
	}

	public List<String> getResourceIdList() {
		return resourceIdList;
	}

	public void setResourceIdList(List<String> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}

	public List<String> getVenIdList() {
		return venIdList;
	}

	public void setVenIdList(List<String> venIdList) {
		this.venIdList = venIdList;
	}
	

}
