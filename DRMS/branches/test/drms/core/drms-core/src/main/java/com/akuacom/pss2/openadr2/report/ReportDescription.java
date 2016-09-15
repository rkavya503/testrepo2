package com.akuacom.pss2.openadr2.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name ="reportdescription")
@NamedQueries({ 
	@NamedQuery(name="ReportDescription.findResourceIdfromReportDataSourceByrId", 
			query="SELECT rs.device FROM ReportRequest rr join rr.report re join  re.reportDescription rd join rd.reportDataSource rs " +
				  "where re.reportSpecifierId = :reportSpecifierId AND  rd.rId = :rId  AND rr.venId = :venId")
})
@XmlRootElement
public class ReportDescription extends VersionedEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String marketContext;
	private String readingType;
	private String reportType;
	private String rId;
	private String reportId;
	private Long samplingMaxPeriod;
	private Long samplingMinPeriod;
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name="reportdescription_uuid" ,referencedColumnName="uuid")
  	private List<ReportDataSource> reportDataSource;
	
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name="reportdescription_uuid" ,referencedColumnName="uuid")
  	private List<ReportSubject> reportSubject;
	
	@OneToOne(cascade= CascadeType.ALL,fetch=FetchType.EAGER)
	private ItemBase itemBase;
	
	
	@Column(columnDefinition = "boolean default false")
	private boolean cancelled=false;
    @Column(columnDefinition = "boolean default false")
    private boolean created = false;
    
    //Columns added for UI
    @Column(columnDefinition = "boolean default false")
    private boolean toCancel=false;
    @Column(columnDefinition = "boolean default false")
    private boolean toCreate = false;
    
    protected long uiGranularity;	
	protected long uiReportBackDuration;	
	protected Date uiDtStart;	
	protected long uiDuration;
	public String getMarketContext() {
		return marketContext;
	}
	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}
	public String getReadingType() {
		return readingType;
	}
	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getrId() {
		return rId;
	}
	public void setrId(String rId) {
		this.rId = rId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Long getSamplingMaxPeriod() {
		return samplingMaxPeriod;
	}
	public void setSamplingMaxPeriod(Long samplingMaxPeriod) {
		this.samplingMaxPeriod = samplingMaxPeriod;
	}
	public Long getSamplingMinPeriod() {
		return samplingMinPeriod;
	}
	public void setSamplingMinPeriod(Long samplingMinPeriod) {
		this.samplingMinPeriod = samplingMinPeriod;
	}
	public List<ReportDataSource> getReportDataSource() {
		return reportDataSource;
	}
	public void setReportDataSource(List<ReportDataSource> reportDataSource) {
		this.reportDataSource = reportDataSource;
	}
	public List<ReportSubject> getReportSubject() {
		return reportSubject;
	}
	public void setReportSubject(List<ReportSubject> reportSubject) {
		this.reportSubject = reportSubject;
	}
	public ItemBase getItemBase() {
		return itemBase;
	}
	public void setItemBase(ItemBase itemBase) {
		this.itemBase = itemBase;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public boolean isCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}
	public boolean isToCancel() {
		return toCancel;
	}
	public void setToCancel(boolean toCancel) {
		this.toCancel = toCancel;
	}
	public boolean isToCreate() {
		return toCreate;
	}
	public void setToCreate(boolean toCreate) {
		this.toCreate = toCreate;
	}
	public long getUiGranularity() {
		return uiGranularity;
	}
	public void setUiGranularity(long uiGranularity) {
		this.uiGranularity = uiGranularity;
	}
	public long getUiReportBackDuration() {
		return uiReportBackDuration;
	}
	public void setUiReportBackDuration(long uiReportBackDuration) {
		this.uiReportBackDuration = uiReportBackDuration;
	}
	public Date getUiDtStart() {
		return uiDtStart;
	}
	public void setUiDtStart(Date uiDtStart) {
		this.uiDtStart = uiDtStart;
	}
	public long getUiDuration() {
		return uiDuration;
	}
	public void setUiDuration(long uiDuration) {
		this.uiDuration = uiDuration;
	}
}
