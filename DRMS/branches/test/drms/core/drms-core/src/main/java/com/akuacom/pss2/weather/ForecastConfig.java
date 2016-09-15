package com.akuacom.pss2.weather;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

@Entity
@Table(name = "forecast_config")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
		@NamedQuery(name = "ForecastConfig.findConfig.Single", query = "SELECT c FROM ForecastConfig c")
})
public class ForecastConfig extends BaseEntity{
	private static final long serialVersionUID = 7700138348510386660L;
	
	private String country;
	private String zipcode;
	private String city;
	private String state;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
