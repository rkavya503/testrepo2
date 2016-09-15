/**
 * 
 */
package com.akuacom.pss2.drw.value;

import java.io.Serializable;

/**
 * the class LocationValue
 */
public class LocationValue implements Serializable {

	private static final long serialVersionUID = -5615126426778242401L;

	String block;
	String zipCode;
	String cityName;
	String countyNo;
	String countyName;
	String eventKey;
	boolean isall;
	
	//for Kiosk view -> group event with start time & end time
	String dispatchType;
	String dispatchLocation;
	
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
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
	public String getCountyNo() {
		return countyNo;
	}
	public void setCountyNo(String countyNo) {
		this.countyNo = countyNo;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public boolean isIsall() {
		return isall;
	}
	public void setIsall(boolean isall) {
		this.isall = isall;
	}
	public String getDispatchType() {
		return dispatchType;
	}
	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}
	public String getDispatchLocation() {
		return dispatchLocation;
	}
	public void setDispatchLocation(String dispatchLocation) {
		this.dispatchLocation = dispatchLocation;
	}


	private String eventID;

	/**
	 * @return the eventID
	 */
	public String getEventID() {
		return eventID;
	}
	/**
	 * @param eventID the eventID to set
	 */
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	
}
