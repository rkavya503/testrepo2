package com.honeywell.dras.vtn.api.registration;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;

@XmlRootElement
public class CreatePartyRegistration extends BaseClass{

	private ProfileName profileName;
	private List<TransportName> transports;
	private String transportAddress;
	private Boolean reportOnly;
	private Boolean xmlSignature;
	private Boolean httpPullMode;
	private String venName;
	private String registrationId;
	
	public ProfileName getProfileName() {
		return profileName;
	}
	public void setProfileName(ProfileName profileName) {
		this.profileName = profileName;
	}
	public List<TransportName> getTransports() {
		return transports;
	}
	public void setTransports(List<TransportName> transports) {
		this.transports = transports;
	}
	public String getTransportAddress() {
		return transportAddress;
	}
	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}
	public Boolean getReportOnly() {
		return reportOnly;
	}
	public void setReportOnly(Boolean reportOnly) {
		this.reportOnly = reportOnly;
	}
	public Boolean getXmlSignature() {
		return xmlSignature;
	}
	public void setXmlSignature(Boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}
	public Boolean getHttpPullMode() {
		return httpPullMode;
	}
	public void setHttpPullMode(Boolean httpPullMode) {
		this.httpPullMode = httpPullMode;
	}
	public String getVenName() {
		return venName;
	}
	public void setVenName(String venName) {
		this.venName = venName;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
}
