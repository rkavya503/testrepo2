package com.akuacom.pss2.openadr2.report;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name = "reportrequest")
@NamedQueries({ 
	@NamedQuery(name="ReportRequest.findByVenIdandReportRequestId", query="SELECT rq FROM ReportRequest rq , Report re WHERE rq.venId=:venId AND re.reportRequestId=:reportRequestId"),
	@NamedQuery(name="ReportRequest.findByVenIdandReportRequestIds", query="SELECT rq FROM ReportRequest rq , Report re WHERE rq.venId=:venId AND re.reportRequestId IN (:reportRequestId)"),
	@NamedQuery(name="ReportRequest.findByVenId", query="SELECT rq FROM ReportRequest rq WHERE rq.venId=:venId"),
	@NamedQuery(name="ReportRequest.findCreateReportMessagesByVenId", query="SELECT rq FROM ReportRequest rq JOIN rq.report re JOIN re.reportDescription rd  where rq.venId = :venId AND re.toCreate = true AND rd.toCreate = true"),
	@NamedQuery(name="ReportRequest.findCancelReportMessagesByVenId", query="SELECT rq FROM ReportRequest rq JOIN rq.report re JOIN re.reportDescription rd  where rq.venId = :venId AND re.toCancel = true AND rd.toCancel = true"),
	@NamedQuery(name="ReportRequest.findCreatedButNotCancelledReportByVenId", query="SELECT rq FROM ReportRequest rq JOIN rq.report re  JOIN re.reportDescription rd" +
			" WHERE rq.venId=:venId AND re.created = :isCreated AND re.cancelled = :isCancelled AND rd.created = :isCreated AND rd.cancelled = :isCancelled"),
	@NamedQuery(name="ReportRequest.findOnlyReportRequestByVenId", query="SELECT rq FROM ReportRequest rq where rq.venId = :venId ")
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "ReportRequest.findCreateReportMessagesByVenIdNativeSql",query="select * from reportrequest rq " +
			   " join report re on re.reportrequest_uuid = rq.uuid " + 
			   " join reportdescription rd on rd.report_uuid = re.uuid " +
			   " where rq.venid = :venId " +
			   " and re.toCreate = 1 and rd.toCreate = 1 " ,resultClass=ReportRequest.class),
	@NamedNativeQuery(name = "ReportRequest.findCancelReportMessagesByVenIdNativeSql",query="select * from reportrequest rq " +
			   " join report re on re.reportrequest_uuid = rq.uuid " + 
			   " join reportdescription rd on rd.report_uuid = re.uuid " +
			   " where rq.venid = :venId " +
			   " and re.toCancel = 1 and rd.toCancel = 1 " ,resultClass=ReportRequest.class),
	@NamedNativeQuery(name = "ReportRequest.findCreatedReportsNotCancelledByVenIdNativeSql",query="select * from reportrequest rq " +
			   " join report re on re.reportrequest_uuid = rq.uuid " + 
			   " join reportdescription rd on rd.report_uuid = re.uuid " +
			   " where rq.venid = :venId " +
			   " and ((re.created= 'true' and re.cancelled = 'false' )  "+
			   " and ( rd.created = 'true' and  rd.cancelled = 'false' ))  " ,resultClass=ReportRequest.class),
	@NamedNativeQuery(name="ReportRequest.findOnlyReportByReportRequest_uuid", query="SELECT * from report re where reportrequest_uuid = :reportRequest_uuid" +
			  " AND re.created = :isCreated AND re.cancelled = :isCancelled ",resultClass=Report.class),
	@NamedNativeQuery(name="ReportRequest.findOnlyReportDescriptionByReport_uuid", query="SELECT * from reportdescription rd where report_uuid = :report_uuid" +
			  " AND rd.created = :isCreated AND rd.cancelled = :isCancelled",resultClass= ReportDescription.class),
})
@FilterDef(name = "reportCreatedNotCancelledFilter", parameters = {@ParamDef(name = "isCreated", type = "boolean"), @ParamDef(name = "isCancelled", type = "boolean")})
@XmlRootElement
public class ReportRequest extends VersionedEntity {

	private static final long serialVersionUID = 1L;
	private String requestId;
	private String venId;
	
	@OneToMany(fetch=FetchType.EAGER,cascade= CascadeType.ALL)
    @JoinColumn(name="reportrequest_uuid" ,referencedColumnName="uuid")
    @Filter(
    		name = "reportCreatedNotCancelledFilter",
    		condition="created = :isCreated and cancelled = :isCancelled"
    )
	private Set<Report> report;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public Set<Report> getReport() {
		return report;
	}
	public void setReport(Set<Report> report) {
		this.report = report;
	}		
}
