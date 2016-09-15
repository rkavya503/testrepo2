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
@Table(name = "forecast_weather")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
		@NamedQuery(name = "ForecastWeather.findByCity.Single", query = "SELECT w FROM ForecastWeather w where w.city = :city")
})
public class ForecastWeather extends VersionedEntity{
	private static final long serialVersionUID = 2858546376398578585L;
	
	private String city;
	private String weather_0;
	private String weather_1;
	private String weather_icon_0;
	private String weather_icon_1;
	private String temp;
	private String hum;
	private String min_0;
	private String max_0;
	private String min_1;
	private String max_1;
	private String unit;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWeather_0() {
		return weather_0;
	}
	public void setWeather_0(String weather_0) {
		this.weather_0 = weather_0;
	}
	public String getWeather_1() {
		return weather_1;
	}
	public void setWeather_1(String weather_1) {
		this.weather_1 = weather_1;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getHum() {
		return hum;
	}
	public void setHum(String hum) {
		this.hum = hum;
	}
	public String getMin_0() {
		return min_0;
	}
	public void setMin_0(String min_0) {
		this.min_0 = min_0;
	}
	public String getMax_0() {
		return max_0;
	}
	public void setMax_0(String max_0) {
		this.max_0 = max_0;
	}
	public String getMin_1() {
		return min_1;
	}
	public void setMin_1(String min_1) {
		this.min_1 = min_1;
	}
	public String getMax_1() {
		return max_1;
	}
	public void setMax_1(String max_1) {
		this.max_1 = max_1;
	}
	public String getWeather_icon_0() {
		return weather_icon_0;
	}
	public void setWeather_icon_0(String weather_icon_0) {
		this.weather_icon_0 = weather_icon_0;
	}
	public String getWeather_icon_1() {
		return weather_icon_1;
	}
	public void setWeather_icon_1(String weather_icon_1) {
		this.weather_icon_1 = weather_icon_1;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
