package com.honeywell.dras.vtn.api.report;

public class ReportRequest {
	
	private String reportRequestId;
	private ReportSpecifier reportSpecifier;
	/**
	 * @return the reportRequestId
	 */
	public String getReportRequestId() {
		return reportRequestId;
	}
	/**
	 * @param reportRequestId the reportRequestId to set
	 */
	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}
	/**
	 * @return the reportSpecifier
	 */
	public ReportSpecifier getReportSpecifier() {
		return reportSpecifier;
	}
	/**
	 * @param reportSpecifier the reportSpecifier to set
	 */
	public void setReportSpecifier(ReportSpecifier reportSpecifier) {
		this.reportSpecifier = reportSpecifier;
	}

}
