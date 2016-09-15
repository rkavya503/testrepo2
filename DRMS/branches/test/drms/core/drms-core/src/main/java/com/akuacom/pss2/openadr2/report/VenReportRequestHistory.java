package com.akuacom.pss2.openadr2.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.ejb.VersionedEntity;

/**
 * Entity implementation class for Entity: Party
 */
@Entity
@Table(name="venreportrequesthistory")
@NamedQueries({ 
	@NamedQuery(name="VenReportRequestHistory.findByVenIdandReportRequestId", query="SELECT vr FROM VenReportRequestHistory vr  WHERE vr.venId=:venId AND vr.reportRequestId=:reportRequestId and vr.cancelled = false "),
	//@NamedQuery(name="VenReportRequestHistory.findByVenIdandReportRequestIds", query="SELECT vr FROM VenReportRequestHistory vr  WHERE vr.venId=:venId AND vr.reportRequestId IN (:reportRequestId and vr.cancelled = false)"),
	@NamedQuery(name="VenReportRequestHistory.findByVenIdandCancelRequestId", query="SELECT vr FROM VenReportRequestHistory vr  WHERE vr.venId=:venId AND vr.cancelRequestId=:cancelRequestId and vr.cancelled = false ")
})

@XmlRootElement
public class VenReportRequestHistory extends VersionedEntity {

	private String venId;
	
	protected String reportRequestId;

	protected String requestId;
	
	@Column(columnDefinition = "boolean default false")
	private boolean cancelled=false;
	
	@Column(columnDefinition = "boolean default false")
	private boolean created = false;

	protected String cancelRequestId;
	
	public String getCancelRequestId() {
		return cancelRequestId;
	}

	public void setCancelRequestId(String cancelRequestId) {
		this.cancelRequestId = cancelRequestId;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getReportRequestId() {
		return reportRequestId;
	}

	public void setReportRequestId(String reportRequestId) {
		this.reportRequestId = reportRequestId;
	}

	public String getRequestId() {
		return requestId;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	private static final long serialVersionUID = 1L;


	public boolean isCreated() {
		return created;
	}

	public void setCreated(boolean created) {
		this.created = created;
	}
}
