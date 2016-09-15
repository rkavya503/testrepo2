package com.honeywell.dras.vtn.api.report;

public class ResourceStatus {
	
	private Boolean online;
	private Boolean manualOverride;
	private Capacity capacity;
	private LevelOffset levelOffset;
	private PercentOffset percentOffset;
	private SetPoint setPoint;
	/**
	 * @return the online
	 */
	public Boolean getOnline() {
		return online;
	}
	/**
	 * @param online the online to set
	 */
	public void setOnline(Boolean online) {
		this.online = online;
	}
	/**
	 * @return the manualOverride
	 */
	public Boolean getManualOverride() {
		return manualOverride;
	}
	/**
	 * @param manualOverride the manualOverride to set
	 */
	public void setManualOverride(Boolean manualOverride) {
		this.manualOverride = manualOverride;
	}
	/**
	 * @return the capacity
	 */
	public Capacity getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Capacity capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the levelOffset
	 */
	public LevelOffset getLevelOffset() {
		return levelOffset;
	}
	/**
	 * @param levelOffset the levelOffset to set
	 */
	public void setLevelOffset(LevelOffset levelOffset) {
		this.levelOffset = levelOffset;
	}
	/**
	 * @return the percentOffset
	 */
	public PercentOffset getPercentOffset() {
		return percentOffset;
	}
	/**
	 * @param percentOffset the percentOffset to set
	 */
	public void setPercentOffset(PercentOffset percentOffset) {
		this.percentOffset = percentOffset;
	}
	/**
	 * @return the setPoint
	 */
	public SetPoint getSetPoint() {
		return setPoint;
	}
	/**
	 * @param setPoint the setPoint to set
	 */
	public void setSetPoint(SetPoint setPoint) {
		this.setPoint = setPoint;
	}

	

}
