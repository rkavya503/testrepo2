package com.honeywell.dras.vtn.api.report;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;

@XmlRootElement
public class CancelReport extends BaseClass{
	
	private boolean reportToFollow;
	private List<String> reportRequestId;

	/**
	 * @return the reportRequestId
	 */
	public List<String> getReportRequestId() {
		return reportRequestId;
	}

	/**
	 * @param reportRequestId the reportRequestId to set
	 */
	public void setReportRequestId(List<String> reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

	public boolean isReportToFollow() {
		return reportToFollow;
	}

	public void setReportToFollow(boolean reportToFollow) {
		this.reportToFollow = reportToFollow;
	}

}
