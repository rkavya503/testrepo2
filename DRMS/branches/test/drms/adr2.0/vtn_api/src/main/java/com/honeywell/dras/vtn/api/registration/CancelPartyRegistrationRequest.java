package com.honeywell.dras.vtn.api.registration;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class CancelPartyRegistrationRequest {
	private CancelPartyRegistration cancelPartyRegistration;
	private PushProfile pushProfile;
	
	
	public CancelPartyRegistration getCancelPartyRegistration() {
		return cancelPartyRegistration;
	}
	public void setCancelPartyRegistration(CancelPartyRegistration cancelPartyRegistration) {
		this.cancelPartyRegistration = cancelPartyRegistration;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	
}
