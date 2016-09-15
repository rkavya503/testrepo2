package com.honeywell.dras.vtn.api.registration;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class RequestReRegistrationRequest {

	private ReRegistration reRegistration;
	private PushProfile pushProfile;
	
	
	public ReRegistration getReRegistration() {
		return reRegistration;
	}
	public void setReRegistration(ReRegistration reRegistration) {
		this.reRegistration = reRegistration;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	
}
