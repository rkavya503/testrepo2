package com.honeywell.dras.vtn.api.common;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.registration.ProfileName;
import com.honeywell.dras.vtn.api.registration.TransportName;

@XmlRootElement
public class PushProfile {
	
	private ProfileName profileName;
	private TransportName transport;
	private String pushUrl;
	private String xmppJid;
	
	public ProfileName getProfileName() {
		return profileName;
	}
	public void setProfileName(ProfileName profileName) {
		this.profileName = profileName;
	}
	public TransportName getTransport() {
		return transport;
	}
	public void setTransport(TransportName transport) {
		this.transport = transport;
	}
	public String getPushUrl() {
		return pushUrl;
	}
	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}
	public String getXmppJid() {
		return xmppJid;
	}
	public void setXmppJid(String xmppJid) {
		this.xmppJid = xmppJid;
	}

}
