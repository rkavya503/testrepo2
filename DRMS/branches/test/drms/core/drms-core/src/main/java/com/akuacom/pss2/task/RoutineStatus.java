package com.akuacom.pss2.task;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "routine_status", uniqueConstraints = {@UniqueConstraint(columnNames={"name", "date"})})
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
			@NamedQuery(
				name="RoutineStatus.findByNameAndDate",
				query="select distinct(t) from RoutineStatus t where t.name =:name and t.date=:date",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
		})
public class RoutineStatus extends BaseEntity {
	
	private static final long serialVersionUID = -659374934827518205L;
	
	//task name
	private String name;
	//date
	private Date date;
	//status
	private Boolean status = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
