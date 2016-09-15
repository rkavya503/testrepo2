package com.honeywell.dras.vtn.api.report;

public class Payload {
	
	private String rId;
	private ResourceStatus resourceStatus;
	private String dataQuality;
	private Long confidence;
	private Float accuracy;
	private Float value;
	
	
	/**
	 * @return the rId
	 */
	public String getrId() {
		return rId;
	}
	/**
	 * @param rId the rId to set
	 */
	public void setrId(String rId) {
		this.rId = rId;
	}
	/**
	 * @return the resourceStatus
	 */
	public ResourceStatus getResourceStatus() {
		return resourceStatus;
	}
	/**
	 * @param resourceStatus the resourceStatus to set
	 */
	public void setResourceStatus(ResourceStatus resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
	/**
	 * @return the dataQuality
	 */
	public String getDataQuality() {
		return dataQuality;
	}
	/**
	 * @param dataQuality the dataQuality to set
	 */
	public void setDataQuality(String dataQuality) {
		this.dataQuality = dataQuality;
	}
	/**
	 * @return the confidence
	 */
	public Long getConfidence() {
		return confidence;
	}
	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(Long confidence) {
		this.confidence = confidence;
	}
	/**
	 * @return the accuracy
	 */
	public Float getAccuracy() {
		return accuracy;
	}
	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}
	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		this.value = value;
	}
	
	
	

}
