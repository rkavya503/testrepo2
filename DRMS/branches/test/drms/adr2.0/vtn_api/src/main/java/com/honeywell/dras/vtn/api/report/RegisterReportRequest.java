package com.honeywell.dras.vtn.api.report;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class RegisterReportRequest {

	private RegisterReport registerReport;
	private PushProfile pushProfile;
	
	public RegisterReport getRegisterReport() {
		return registerReport;
	}
	public void setRegisterReport(RegisterReport registerReport) {
		this.registerReport = registerReport;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	
}
