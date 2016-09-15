/**
 * 
 */
package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the class EventVal
 */
public class EventValue implements Serializable {

	private static final long serialVersionUID = 2070205949837070210L;
	String uuid;
	String programClass;
	String longProgramName;
	String utilityProgramName;
	
	String product;
	String block;
	Date startTime;
	Date endTime;
	Date issueTime;
	
	String dispatchType;
	String dispatchLocation;
	
	String tbdFlag;
	
	String eventName;
	
	Map<String, LocationValue> locationMap=new HashMap<String, LocationValue>();
	
	List<String> counties=new ArrayList<String>();
	List<String> cities=new ArrayList<String>();
	List<String> zipCodes=new ArrayList<String>();
	List<String> blocks=new ArrayList<String>();
	List<String> dispatchTypes=new ArrayList<String>();
	List<String> dispatchLocations=new ArrayList<String>();
	
	
	
	public String getProgramClass() {
		return programClass;
	}
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}
	public String getLongProgramName() {
		return longProgramName;
	}
	public void setLongProgramName(String longProgramName) {
		this.longProgramName = longProgramName;
	}
	public String getUtilityProgramName() {
		return utilityProgramName;
	}
	public void setUtilityProgramName(String utilityProgramName) {
		this.utilityProgramName = utilityProgramName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getBlock() {
		if (blocks.size()>0) {
			block=null;
			for (String b:blocks){
				if((b!=null)&&(!b.trim().equalsIgnoreCase(""))){
					if (block==null)
						block=b;
					else
						block+=", "+b;
				}
			}
		}
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
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
	public List<String> getCounties() {
		return counties;
	}
	public void setCounties(List<String> counties) {
		this.counties = counties;
	}
	public List<String> getCities() {
		return cities;
	}
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	public List<String> getZipCodes() {
		return zipCodes;
	}
	public void setZipCodes(List<String> zipCodes) {
		this.zipCodes = zipCodes;
	}
	public Map<String, LocationValue> getLocationMap() {
		return locationMap;
	}
	public void setLocationMap(Map<String, LocationValue> locationMap) {
		this.locationMap = locationMap;
	}
	public List<String> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<String> blocks) {
		this.blocks = blocks;
	}
	public List<String> getDispatchTypes() {
		return dispatchTypes;
	}
	public void setDispatchTypes(List<String> dispatchTypes) {
		this.dispatchTypes = dispatchTypes;
	}
	public List<String> getDispatchLocations() {
		return dispatchLocations;
	}
	public void setDispatchLocations(List<String> dispatchLocations) {
		this.dispatchLocations = dispatchLocations;
	}
	/**
	 * @return the dispatchType
	 */
	public String getDispatchType() {
		return dispatchType;
	}
	/**
	 * @param dispatchType the dispatchType to set
	 */
	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}
	/**
	 * @return the dispatchLocation
	 */
	public String getDispatchLocation() {
		return dispatchLocation;
	}
	/**
	 * @param dispatchLocation the dispatchLocation to set
	 */
	public void setDispatchLocation(String dispatchLocation) {
		this.dispatchLocation = dispatchLocation;
	}
	/**
	 * @return the tbdFlag
	 */
	public String getTbdFlag() {
		return tbdFlag;
	}
	/**
	 * @param tbdFlag the tbdFlag to set
	 */
	public void setTbdFlag(String tbdFlag) {
		this.tbdFlag = tbdFlag;
	}
	
	
	private List<String> eventIDList = new ArrayList<String>();
	private String eventKey;
	private String blockType;//SLAP or ABANK
	/**
	 * @return the eventIDList
	 */
	public List<String> getEventIDList() {
		return eventIDList;
	}
	/**
	 * @param eventIDList the eventIDList to set
	 */
	public void setEventIDList(List<String> eventIDList) {
		this.eventIDList = eventIDList;
	}
	/**
	 * @return the eventKey
	 */
	public String getEventKey() {
		return eventKey;
	}
	/**
	 * @param eventKey the eventKey to set
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	/**
	 * @return the blockType
	 */
	public String getBlockType() {
		return blockType;
	}
	/**
	 * @param blockType the blockType to set
	 */
	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
	
}
