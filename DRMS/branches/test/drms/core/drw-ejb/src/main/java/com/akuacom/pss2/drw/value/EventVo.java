package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String uuid;
	private String parentUuid;
	private String program;
	private String product;
	private Date startTime;
	private Date endTime;
	private String locationId;
	private String locationType;
	private String locationNumber;
	private String locationBlock;
	private Date issueTime;
	private boolean active;
	private boolean estimated;
	
	private List<EventLoaction> locations;
	
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getParentUuid() {
		return parentUuid;
	}
	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	public Date getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
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
	public String getLocationBlock() {
		return locationBlock;
	}
	public void setLocationBlock(String locationBlock) {
		this.locationBlock = locationBlock;
	}
	public boolean isEstimated() {
		return estimated;
	}
	public void setEstimated(boolean estimated) {
		this.estimated = estimated;
	}
	public List<EventLoaction> getLocations() {
		if(locations==null) locations = new ArrayList<EventLoaction>();
		return locations;
	}
	public void setLocations(List<EventLoaction> locations) {
		this.locations = locations;
	}
	
}
