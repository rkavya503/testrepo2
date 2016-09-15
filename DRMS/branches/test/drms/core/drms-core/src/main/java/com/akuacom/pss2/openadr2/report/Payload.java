package com.akuacom.pss2.openadr2.report;


public class Payload {
	
	private String rId;
	private ResourceStatus resourceStatus;
	private String dataQuality;
	private Long confidence;
	private Float accuracy;
	private Float value;
	
	public String getrId() {
		return rId;
	}
	public void setrId(String rId) {
		this.rId = rId;
	}
	public ResourceStatus getResourceStatus() {
		return resourceStatus;
	}
	public void setResourceStatus(ResourceStatus resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
	public String getDataQuality() {
		return dataQuality;
	}
	public void setDataQuality(String dataQuality) {
		this.dataQuality = dataQuality;
	}
	public Long getConfidence() {
		return confidence;
	}
	public void setConfidence(Long confidence) {
		this.confidence = confidence;
	}
	public Float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
}
