package com.akuacom.pss2.weather;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

@Entity
@Table(name = "forecast_wmo_mapping")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
		@NamedQuery(name = "ForecastWmo.findByCity.Single", query = "SELECT w FROM ForecastWmo w where w.city = :city")
})
public class ForecastWmo extends VersionedEntity {
	private static final long serialVersionUID = 3774093986209382677L;
	
	private String city;
	private String wmo;
	private Integer priority;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWmo() {
		return wmo;
	}
	public void setWmo(String wmo) {
		this.wmo = wmo;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
