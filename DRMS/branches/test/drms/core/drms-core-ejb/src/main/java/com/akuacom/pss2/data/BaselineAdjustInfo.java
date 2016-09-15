package com.akuacom.pss2.data;

import java.io.Serializable;

public class BaselineAdjustInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int startTime;
	private int endTime;
	private boolean maEnabled;
	private float minMARate;
	private float maxMARate;
	private String eventName;
	
	public boolean isMaEnabled() {
		return maEnabled;
	}
	public void setMaEnabled(boolean maEnabled) {
		this.maEnabled = maEnabled;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int starTime) {
		if(starTime<=0) starTime = 0;
		if(starTime>=86400000) starTime = 86400000;
		this.startTime = starTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int enTime) {
		if(enTime<=0) enTime = 0;
		if(enTime>=86400000) enTime = 86400000;
		this.endTime = enTime;
	}
	public float getMinMARate() {
		return minMARate;
	}
	public void setMinMARate(float minMARate) {
		this.minMARate = minMARate;
	}
	public float getMaxMARate() {
		return maxMARate;
	}
	public void setMaxMARate(float maxMARate) {
		this.maxMARate = maxMARate;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
