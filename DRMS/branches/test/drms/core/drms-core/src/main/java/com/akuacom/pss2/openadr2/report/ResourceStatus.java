package com.akuacom.pss2.openadr2.report;

public class ResourceStatus {
	
	private Boolean online;
	private Boolean manualOverride;
	private Capacity capacity;
	private LevelOffset levelOffset;
	private PercentOffset percentOffset;
	private SetPoint setPoint;
	
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
	public Boolean getManualOverride() {
		return manualOverride;
	}
	public void setManualOverride(Boolean manualOverride) {
		this.manualOverride = manualOverride;
	}
	public Capacity getCapacity() {
		return capacity;
	}
	public void setCapacity(Capacity capacity) {
		this.capacity = capacity;
	}
	public LevelOffset getLevelOffset() {
		return levelOffset;
	}
	public void setLevelOffset(LevelOffset levelOffset) {
		this.levelOffset = levelOffset;
	}
	public PercentOffset getPercentOffset() {
		return percentOffset;
	}
	public void setPercentOffset(PercentOffset percentOffset) {
		this.percentOffset = percentOffset;
	}
	public SetPoint getSetPoint() {
		return setPoint;
	}
	public void setSetPoint(SetPoint setPoint) {
		this.setPoint = setPoint;
	}
}
