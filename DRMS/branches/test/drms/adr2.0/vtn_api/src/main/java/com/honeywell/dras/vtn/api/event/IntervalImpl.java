package com.honeywell.dras.vtn.api.event;

import com.honeywell.dras.vtn.api.Interval;
import com.honeywell.dras.vtn.api.IntervalDurationResolution;

public class IntervalImpl implements Interval {

	private long durationInResolution;
	private IntervalDurationResolution intervalDurationResolution;
	private String offsetId;
	private float value;
	private String stringValue;



	@Override
	public long getDurationInResolution() {
		return durationInResolution;
	}
	
	public void setDurationInResolution(long durationInResolution){
		this.durationInResolution = durationInResolution;
	}

	@Override
	public IntervalDurationResolution getResolution() {
		return intervalDurationResolution;
	}
	
	public void setResolution(IntervalDurationResolution intervalDurationResolution){
		this.intervalDurationResolution = intervalDurationResolution;
	}

	@Override
	public String getOffsetId() {
		return offsetId;
	}
	
	public void setOffsetId(String offsetId){
		this.offsetId = offsetId;
	}

	@Override
	public float getValue() {
		return value;
	}
	
	public void setValue(float value){
		this.value = value;
	}

	@Override
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue){
		this.stringValue = stringValue;
	}

}
