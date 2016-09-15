package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.akuacom.ejb.VersionedEntity;


@Entity
@Table(name = "venresourcestatus")
@NamedQueries({
	@NamedQuery(name="VenResourceStatus.findAll", query="SELECT vrs FROM VenResourceStatus vrs"),
	@NamedQuery(name="VenResourceStatus.findByResourceName", query="SELECT vrs FROM VenResourceStatus vrs WHERE vrs.resourceName=:resName"),
	@NamedQuery(name="VenResourceStatus.findLatestByVenIdAndResourceName", query="SELECT vrs FROM VenResourceStatus vrs WHERE vrs.venId=:venId AND vrs.resourceName=:resName ORDER BY vrs.reportedTime DESC"),
	@NamedQuery(name="VenResourceStatus.findByVenIdResourceNameAndEntryTimeRange", query="SELECT vrs FROM VenResourceStatus vrs WHERE vrs.venId=:venId AND vrs.resourceName=:resName AND vrs.reportedTime BETWEEN :start AND :end"),
	@NamedQuery(name="VenResourceStatus.deleteByVenID", query="DELETE FROM VenResourceStatus vrs WHERE vrs.venId=:id"),
})
public class VenResourceStatus extends VersionedEntity{
	private static final long serialVersionUID = 1L;
	
	private String resourceName;
	private String participantName;
	private String venId;
	private boolean isResourceOnline;
	private Date reportedTime;
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public boolean isResourceOnline() {
		return isResourceOnline;
	}
	public void setResourceOnline(boolean isResourceOnline) {
		this.isResourceOnline = isResourceOnline;
	}
	public Date getReportedTime() {
		return reportedTime;
	}
	public void setReportedTime(Date reportedTime) {
		this.reportedTime = reportedTime;
	}
}
