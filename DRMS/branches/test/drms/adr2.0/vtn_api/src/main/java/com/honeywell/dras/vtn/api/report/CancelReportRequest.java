package com.honeywell.dras.vtn.api.report;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class CancelReportRequest {
	
	private CancelReport cancelReport;
	private PushProfile pushProfile;
	
	
	public CancelReport getCancelReport() {
		return cancelReport;
	}
	public void setCancelReport(CancelReport cancelReport) {
		this.cancelReport = cancelReport;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	

}
