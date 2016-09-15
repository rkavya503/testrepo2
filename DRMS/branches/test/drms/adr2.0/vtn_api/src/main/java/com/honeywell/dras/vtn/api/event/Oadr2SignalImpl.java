package com.honeywell.dras.vtn.api.event;

import java.util.List;

import com.honeywell.dras.vtn.api.Interval;
import com.honeywell.dras.vtn.api.Oadr2Signal;
import com.honeywell.dras.vtn.api.SignalType;

public class Oadr2SignalImpl implements Oadr2Signal{
	
	private String name;
	
	private SignalType  type;
	
	private String id;
	
	private Float currentValue;
	
	private List<Interval> intervals;

	public List<Interval> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<Interval> intervals) {
		this.intervals = intervals;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SignalType  getType() {
		return type;
	}

	public void setType(SignalType  type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Float currentValue) {
		this.currentValue = currentValue;
	}
	
	public String getValueAsString() {
		return currentValue==null ? null : String.format("%f", currentValue);
	}

}
