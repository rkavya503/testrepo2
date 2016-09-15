package com.akuacom.pss2.drw.value;

import java.io.Serializable;

public class EventLoaction implements Serializable{
	private static final long serialVersionUID = 1L;
	private String zipCode;
	private String cityName;
	private String countyName;
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	

}
