package com.akuacom.pss2.openadr2.report;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.akuacom.ejb.VersionedEntity;
@Entity
@Table(name = "report")
@NamedQueries({ 
})
@FilterDef(name="reportDescriptionCreatedNotCancelledFilter",
		   parameters = {@ParamDef(name ="isCreated", type="boolean"),
						 @ParamDef(name="isCancelled", type="boolean")})
@XmlRootElement
public class Report extends VersionedEntity {
	
	private static final long serialVersionUID = 1L;

	@Temporal( TemporalType.TIMESTAMP)
	protected Date start = new Date();
	
	protected long duration;
	
    protected String reportId;
    
	protected String reportName;

    protected String reportRequestId;
    
    protected String reportSpecifierId;   

    @Column(columnDefinition = "boolean default false")
    private boolean cancelled=false;
    @Column(columnDefinition = "boolean default false")
    private boolean created = false;    
  
    //Columns added for UI
    @Column(columnDefinition = "boolean default false")
    private boolean toCancel=false;
    @Column(columnDefinition = "boolean default false")
    private boolean toCreate = false;
    
    @Temporal( TemporalType.TIMESTAMP)
   	protected Date createdDate = new Date();
   	
    @OneToMany(fetch=FetchType.EAGER,cascade= CascadeType.ALL)
    @JoinColumn(name="report_uuid" ,referencedColumnName="uuid")
    @Filter(
   		name = "reportDescriptionCreatedNotCancelledFilter",
   		condition="created = :isCreated and cancelled = :isCancelled"
    )
   	private Set<ReportDescription> reportDescription;
    
	//private List<ReportInterval> intervalList;
    
    //private ReportRequest reportRequest;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

	public String getReportSpecifierId() {
		return reportSpecifierId;
	}

	public void setReportSpecifierId(String reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Set<ReportDescription> getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(
			Set<ReportDescription> reportDescription) {
		this.reportDescription = reportDescription;
	}

	/*public List<ReportInterval> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(List<ReportInterval> intervalList) {
		this.intervalList = intervalList;
	} */   
    
}
