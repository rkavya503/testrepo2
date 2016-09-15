package com.honeywell.dras.vtn.api.event;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class DistributeEventRequest {
	
	private DistributeEvent distributeEvent;
	private PushProfile pushProfile;
	
	
	public DistributeEvent getDistributeEvent() {
		return distributeEvent;
	}
	public void setDistributeEvent(DistributeEvent distributeEvent) {
		this.distributeEvent = distributeEvent;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	
	

}
