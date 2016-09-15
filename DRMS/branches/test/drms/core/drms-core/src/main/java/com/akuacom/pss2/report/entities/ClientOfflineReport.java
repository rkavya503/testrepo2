/**
 * 
 */
package com.akuacom.pss2.report.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.VersionedEntity;

/**
 * the entity Event
 */
@Entity
@Table(name = "report_client_offline")
@NamedQueries( {
	@NamedQuery(name = "ClientOfflineReport.findAll", query = "select distinct(p) from ClientOfflineReport p order by p.generateTime", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	@NamedQuery(name = "ClientOfflineReport.findByDate",query = "select m from ClientOfflineReport m where generateTime >= :start and m.generateTime <= :end order by m.generateTime")
})
public class ClientOfflineReport extends BaseEntity {

	private static final long serialVersionUID = -4591142128980053717L;

	private Date generateTime;
    private String reportName;
    private String comments;
    
    @OneToMany(mappedBy = "report", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @NotFound(action=NotFoundAction.IGNORE)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Set<ClientOfflineReportEntity> details=new HashSet<ClientOfflineReportEntity>();

	/**
	 * @return the generateTime
	 */
	public Date getGenerateTime() {
		return generateTime;
	}

	/**
	 * @param generateTime the generateTime to set
	 */
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
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
	 * @return the details
	 */
	public Set<ClientOfflineReportEntity> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(Set<ClientOfflineReportEntity> details) {
		this.details = details;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	public static ClientOfflineReport generateReport(List<ClientOfflineReportEntity> reportDetails,Date generateTime){
		ClientOfflineReport report = new ClientOfflineReport();
		Set<ClientOfflineReportEntity> details=new HashSet<ClientOfflineReportEntity>();
		report.setGenerateTime(generateTime);
		for(ClientOfflineReportEntity detail:reportDetails){
			detail.setReport(report);
			details.add(detail);
		}
		report.setDetails(details);
		return report;
	}
}
