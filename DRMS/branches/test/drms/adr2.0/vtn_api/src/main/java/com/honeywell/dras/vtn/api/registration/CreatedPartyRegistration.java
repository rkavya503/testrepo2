package com.honeywell.dras.vtn.api.registration;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;
import com.honeywell.dras.vtn.api.common.Response;

@XmlRootElement
public class CreatedPartyRegistration extends BaseClass{
	
	private Response response;
	private String registrationId;
	private String vtnId;
	private Long pollFrequency;
	private List<Profile> profiles;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}


	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}


	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public Long getPollFrequency() {
		return pollFrequency;
	}

	/**
	 * Poll Frequency in milliseconds
	 * @param pollFrequency
	 */
	public void setPollFrequency(Long pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public List<Profile> getProfiles() {
		if (profiles == null) {
			profiles = new ArrayList<Profile>();
		}
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

}
