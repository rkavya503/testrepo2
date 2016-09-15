package com.akuacom.pss2.drw.core;

import java.io.Serializable;
import java.util.Date;

public class HistoryEvtVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String uuid;
	private String programName;
	private Date startTime;
	private Date issuedTime;
	private Date endTime;
	
	private String product;
	private String comment;
	private String locationID;
	private String allLocationType;
	private String blockNames;
	
	private String locationType;
	private String locationNumber;
	private String locationName;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLocationID() {
		return locationID;
	}
	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
	public String getAllLocationType() {
		return allLocationType;
	}
	public void setAllLocationType(String allLocationType) {
		this.allLocationType = allLocationType;
	}
	public String getBlockNames() {
		return blockNames;
	}
	public void setBlockNames(String blockNames) {
		this.blockNames = blockNames;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getLocationNumber() {
		return locationNumber;
	}
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
