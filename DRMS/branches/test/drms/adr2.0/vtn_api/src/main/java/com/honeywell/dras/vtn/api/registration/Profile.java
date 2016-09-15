package com.honeywell.dras.vtn.api.registration;

import java.util.ArrayList;
import java.util.List;

public class Profile {

	private ProfileName profileName;
	private List<TransportName> transports;
	
	public ProfileName getProfileName() {
		return profileName;
	}
	public void setProfileName(ProfileName profileName) {
		this.profileName = profileName;
	}
	public List<TransportName> getTransports() {
		if (transports == null) {
			transports = new ArrayList<TransportName>();
		}
		return transports;
	}
	public void setTransports(List<TransportName> transports) {
		this.transports = transports;
	}
}
