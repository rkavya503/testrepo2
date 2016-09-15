package com.honeywell.dras.vtn.api.report;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class UpdateReportRequest {
	
	private UpdateReport updateReport;
	private PushProfile pushProfile;
	
	public UpdateReport getUpdateReport() {
		return updateReport;
	}
	public void setUpdateReport(UpdateReport updateReport) {
		this.updateReport = updateReport;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	

}
