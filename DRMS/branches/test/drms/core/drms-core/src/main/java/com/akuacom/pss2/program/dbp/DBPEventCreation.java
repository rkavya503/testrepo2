/**
 * 
 */
package com.akuacom.pss2.program.dbp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

/**
 * The Class DBPEventCreationItem
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@Table(name = "dbp_event_creation")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = "DBPEventCreation.findAll",
        query = "select e from DBPEventCreation e order by e.date desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "DBPEventCreation.findByDate",
                query = "select e from DBPEventCreation e where e.date = :date order by e.date desc",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "DBPEventCreation.findByDateRange",
                query = "select e from DBPEventCreation e where e.date >= :start and e.date <= :end order by e.date desc",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class DBPEventCreation extends BaseEntity {
	
	private static final long serialVersionUID = 6621974662196127671L;
	
	private Date date;
	private boolean status;
	private String fileName;
	
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	private boolean autoCreation;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isAutoCreation() {
		return autoCreation;
	}
	public void setAutoCreation(boolean autoCreation) {
		this.autoCreation = autoCreation;
	}
	
}
