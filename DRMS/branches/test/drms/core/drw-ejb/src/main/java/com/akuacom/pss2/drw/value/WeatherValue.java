/**
 * 
 */
package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.Date;

/**
 * the class WeatherValue
 * 
 */
public class WeatherValue implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Date date;
	double temperature;
	String pricingCategory;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public String getPricingCategory() {
		return pricingCategory;
	}
	public void setPricingCategory(String pricingCategory) {
		this.pricingCategory = pricingCategory;
	}
}
