package com.honeywell.dras.vtn.api.report;

import java.util.List;

import com.honeywell.dras.vtn.api.common.WsCalanderInterval;

public class ReportSpecifier {
	
	private Long granularity;
	private Long reportBackDuration;
	private WsCalanderInterval reportInterval;
	private String reportSpecifierId;
	private List<SpecifierPayload> specifierPayloadList;
	
	
	/**
	 * @return the granularity
	 */
	public Long getGranularity() {
		return granularity;
	}
	/**
	 * @param granularity the granularity to set
	 */
	public void setGranularity(Long granularity) {
		this.granularity = granularity;
	}
	/**
	 * @return the reportBackDuration
	 */
	public Long getReportBackDuration() {
		return reportBackDuration;
	}
	/**
	 * @param reportBackDuration the reportBackDuration to set
	 */
	public void setReportBackDuration(Long reportBackDuration) {
		this.reportBackDuration = reportBackDuration;
	}
	/**
	 * @return the reportInterval
	 */
	public WsCalanderInterval getReportInterval() {
		return reportInterval;
	}
	/**
	 * @param reportInterval the reportInterval to set
	 */
	public void setReportInterval(WsCalanderInterval reportInterval) {
		this.reportInterval = reportInterval;
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
	 * @return the specifierPayloadList
	 */
	public List<SpecifierPayload> getSpecifierPayloadList() {
		return specifierPayloadList;
	}
	/**
	 * @param specifierPayloadList the specifierPayloadList to set
	 */
	public void setSpecifierPayloadList(List<SpecifierPayload> specifierPayloadList) {
		this.specifierPayloadList = specifierPayloadList;
	}
	
	
	

}
