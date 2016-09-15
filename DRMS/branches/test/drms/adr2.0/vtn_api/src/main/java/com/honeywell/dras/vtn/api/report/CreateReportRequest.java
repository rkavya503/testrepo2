package com.honeywell.dras.vtn.api.report;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.PushProfile;

@XmlRootElement
public class CreateReportRequest {
	
	private CreateReport createReport;
	private PushProfile pushProfile;
	
	public CreateReport getCreateReport() {
		return createReport;
	}
	public void setCreateReport(CreateReport createReport) {
		this.createReport = createReport;
	}
	public PushProfile getPushProfile() {
		return pushProfile;
	}
	public void setPushProfile(PushProfile pushProfile) {
		this.pushProfile = pushProfile;
	}
	
	

}
