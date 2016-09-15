package com.akuacom.pss2.drw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.pss2.drw.value.EventLoaction;
import com.akuacom.utils.DateUtil;

@XmlRootElement
public class Event extends Object{
	private String eventKey;// used to identify consolidation result
	private String uuid;
	private String program;
	private String product;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private Boolean ir;
    private Date startDateTime;
	private Date endDateTime;
    private boolean active;
    private long updateTime;
    private String parentUuid;
    private Date issueTime;
    
    private String locationType;
	private String locationNumber;
	private String locationBlock;
	private int minBlock;
	private int maxBlock;
	private boolean estimated;
	private List<EventLoaction> locations;	
	private String zipCodes;
	private String cityNames;
	private String countyNames;
	
	public String getZipCodes() {
		return zipCodes;
	}
	public void setZipCodes(String zipCodes) {
		this.zipCodes = zipCodes;
	}
	public String getCityNames() {
		return cityNames;
	}
	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
	}
	public String getCountyNames() {
		return countyNames;
	}
	public void setCountyNames(String countyNames) {
		this.countyNames = countyNames;
	}
    public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	private List<String> aggNames;
	public List<String> getAggNames() {
		if(aggNames==null) aggNames = new ArrayList<String>();
		return aggNames;
	}

	public void setAggNames(List<String> aggNames) {
		this.aggNames = aggNames;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getStartDate() {
		return startDate;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
		this.setStartDate(DateUtil.format(startDateTime,"MM/dd/yyyy"));
		this.setStartTime(DateUtil.format(startDateTime,"hh:mm a"));
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
		this.setEndDate(endDateTime!=null?DateUtil.format(endDateTime,"MM/dd/yyyy"):"TBD");
		this.setEndTime(endDateTime!=null?DateUtil.format(endDateTime,"hh:mm a"):"TBD");
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getIr() {
		return ir;
	}

	public void setIr(Boolean ir) {
		this.ir = ir;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String toString(){
		
		return "{ Product: "+this.getProduct()+", Active: "+this.isActive()+",  Start: "+this.getStartDate()+",  End: "+this.getEndDate()+"}";
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
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

	public boolean equals(Object arg0){
		 if(!(arg0 instanceof Event)){
			 return false;
		 }
		return this.getUuid().equals(((Event)arg0).getUuid());
	 }
	 
	 public int hashCode(){
		 return this.getUuid().hashCode();
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

	public int getMinBlock() {
		return minBlock;
	}

	public void setMinBlock(int minBlock) {
		this.minBlock = minBlock;
	}

	public int getMaxBlock() {
		return maxBlock;
	}

	public void setMaxBlock(int maxBlock) {
		this.maxBlock = maxBlock;
	}
	 
	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public List<EventLoaction> getLocations() {
		return locations;
	}
	public void setLocations(List<EventLoaction> locations) {
		this.locations = locations;
	}
	
}
