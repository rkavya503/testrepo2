package com.honeywell.dras.vtn.api.report;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;

@XmlRootElement
public class CreateReport extends BaseClass{

	private List<ReportRequest> reportRequestList;

	public List<ReportRequest> getReportRequestList() {
		return reportRequestList;
	}

	public void setReportRequestList(List<ReportRequest> reportRequestList) {
		this.reportRequestList = reportRequestList;
	}
	
}
