package com.akuacom.pss2.drw;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RTPWeather {
	private String dayOfWeeK;
	private String date;
	private String pricingCategory;
	private String modifiedTime;
	
	public String getDayOfWeeK() {
		return dayOfWeeK;
	}
	public void setDayOfWeeK(String dayOfWeeK) {
		this.dayOfWeeK = dayOfWeeK;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPricingCategory() {
		return pricingCategory;
	}
	public void setPricingCategory(String pricingCategory) {
		this.pricingCategory = pricingCategory;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	

}
