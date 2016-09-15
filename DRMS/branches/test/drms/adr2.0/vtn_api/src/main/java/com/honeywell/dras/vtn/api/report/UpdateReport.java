package com.honeywell.dras.vtn.api.report;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;

@XmlRootElement
public class UpdateReport extends BaseClass{
	
	private List<Report> reportList;

	/**
	 * @return the reportList
	 */
	public List<Report> getReportList() {
		return reportList;
	}

	/**
	 * @param reportList the reportList to set
	 */
	public void setReportList(List<Report> reportList) {
		this.reportList = reportList;
	}

}
