package com.honeywell.dras.vtn.api.event;

import java.util.ArrayList;
import java.util.List;

import com.honeywell.dras.vtn.api.SignalType;
import com.honeywell.dras.vtn.api.common.EiTarget;
import com.honeywell.dras.vtn.api.report.ItemBase;

public class Signal {
	
	private String name;
	private SignalType  type;
	private String id;
	private Float currentValue;
	private List<EventInterval> intervals;
	private EiTarget target;
	private ItemBase itemBase;
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public SignalType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(SignalType type) {
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the currentValue
	 */
	public Float getCurrentValue() {
		return currentValue;
	}
	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(Float currentValue) {
		this.currentValue = currentValue;
	}
	/**
	 * @return the intervals
	 */
	public List<EventInterval> getIntervals() {
		if (intervals == null) {
			intervals = new ArrayList<EventInterval>();
		}
		return intervals;
	}
	/**
	 * @param intervals the intervals to set
	 */
	public void setIntervals(List<EventInterval> intervals) {
		this.intervals = intervals;
	}
	/**
	 * @return the target
	 */
	public EiTarget getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(EiTarget target) {
		this.target = target;
	}
	/**
	 * @return the itemBase
	 */
	public ItemBase getItemBase() {
		return itemBase;
	}
	/**
	 * @param itemBase the itemBase to set
	 */
	public void setItemBase(ItemBase itemBase) {
		this.itemBase = itemBase;
	}

}
