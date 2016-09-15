package com.honeywell.dras.vtn.api.report;

import java.util.Date;
import java.util.List;

public class Report {

	private Date start;
	private Long duration;
	private String reportId;
	private String reportName;
	private String reportRequestId;
	private String reportSpecifierId;
	private List<ReportInterval> intervalList;
	private List<ReportDescription> reportDescriptionList;
	private Date createdDate;
	

	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * @return the reportId
	 */
	public String getReportId() {
		return reportId;
	}
	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
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
	 * @return the reportSpecifierId
	 */
	public String getReportSpecifierId() {
		return reportSpecifierId;
	}
	/**
	 * @param reportSpecifierId the reportSpecifierId to set
	 */
	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}
	/**
	 * @return the intervalList
	 */
	public List<ReportInterval> getIntervalList() {
		return intervalList;
	}
	/**
	 * @param intervalList the intervalList to set
	 */
	public void setIntervalList(List<ReportInterval> intervalList) {
		this.intervalList = intervalList;
	}
	/**
	 * @return the reportDescriptionList
	 */
	public List<ReportDescription> getReportDescriptionList() {
		return reportDescriptionList;
	}
	/**
	 * @param reportDescriptionList the reportDescriptionList to set
	 */
	public void setReportDescriptionList(List<ReportDescription> reportDescriptionList) {
		this.reportDescriptionList = reportDescriptionList;
	}
	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
