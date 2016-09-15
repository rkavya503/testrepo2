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
@Table(name = "weather_category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
		@NamedQuery(name = "WeatherCategory.findByType.Single", query = "SELECT w FROM WeatherCategory w where w.description = :des")
})
public class WeatherCategory extends BaseEntity {
	private static final long serialVersionUID = 248908982747967636L;
	
	private String description;
	private String category;
	private String icon;
	private Boolean fixed;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Boolean getFixed() {
		return fixed;
	}
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}
}
