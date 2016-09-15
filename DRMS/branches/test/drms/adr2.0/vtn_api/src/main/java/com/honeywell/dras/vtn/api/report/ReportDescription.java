package com.honeywell.dras.vtn.api.report;

public class ReportDescription {
	
	private String marketContext;
	private String readingType;
	private String reportType;
	private String rId;
	private String reportId;
	private Long samplingMaxPeriod;
	private Long samplingMinPeriod;
	private ReportDataSource reportDataSource;
	private ReportSubject reportSubject;
	private ItemBase itemBase;
	
	
	/**
	 * @return the marketContext
	 */
	public String getMarketContext() {
		return marketContext;
	}
	/**
	 * @param marketContext the marketContext to set
	 */
	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}
	/**
	 * @return the readingType
	 */
	public String getReadingType() {
		return readingType;
	}
	/**
	 * @param readingType the readingType to set
	 */
	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}
	/**
	 * @return the rId
	 */
	public String getrId() {
		return rId;
	}
	/**
	 * @param rId the rId to set
	 */
	public void setrId(String rId) {
		this.rId = rId;
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
	 * @return the samplingMaxPeriod
	 */
	public Long getSamplingMaxPeriod() {
		return samplingMaxPeriod;
	}
	/**
	 * @param samplingMaxPeriod the samplingMaxPeriod to set
	 */
	public void setSamplingMaxPeriod(Long samplingMaxPeriod) {
		this.samplingMaxPeriod = samplingMaxPeriod;
	}
	/**
	 * @return the samplingMinPeriod
	 */
	public Long getSamplingMinPeriod() {
		return samplingMinPeriod;
	}
	/**
	 * @param samplingMinPeriod the samplingMinPeriod to set
	 */
	public void setSamplingMinPeriod(Long samplingMinPeriod) {
		this.samplingMinPeriod = samplingMinPeriod;
	}
	/**
	 * @return the reportDataSource
	 */
	public ReportDataSource getReportDataSource() {
		return reportDataSource;
	}
	/**
	 * @param reportDataSource the reportDataSource to set
	 */
	public void setReportDataSource(ReportDataSource reportDataSource) {
		this.reportDataSource = reportDataSource;
	}
	/**
	 * @return the reportSubject
	 */
	public ReportSubject getReportSubject() {
		return reportSubject;
	}
	/**
	 * @param reportSubject the reportSubject to set
	 */
	public void setReportSubject(ReportSubject reportSubject) {
		this.reportSubject = reportSubject;
	}
	/**
	 * @return the itemBase
	 */
	public ItemBase getItemBase() {
		return itemBase;
	}
	/**
	 * @param itemBase the itemBase to set
	 */
	public void setItemBase(ItemBase itemBase) {
		this.itemBase = itemBase;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

}
