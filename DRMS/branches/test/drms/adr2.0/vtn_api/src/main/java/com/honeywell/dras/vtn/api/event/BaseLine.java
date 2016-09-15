package com.honeywell.dras.vtn.api.event;

import java.util.Date;
import java.util.List;

import com.honeywell.dras.vtn.api.report.ItemBase;

public class BaseLine {
	
	private String id;
	private String name;
	private Date  start;
	private Long duration;
	private List<EventInterval> intervals;
	private ItemBase itemBase;
	private List<String> resourceIdList;
	
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
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}
	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * @return the intervals
	 */
	public List<EventInterval> getIntervals() {
		return intervals;
	}
	/**
	 * @param intervals the intervals to set
	 */
	public void setIntervals(List<EventInterval> intervals) {
		this.intervals = intervals;
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
	/**
	 * @return the resourceIdList
	 */
	public List<String> getResourceIdList() {
		return resourceIdList;
	}
	/**
	 * @param resourceIdList the resourceIdList to set
	 */
	public void setResourceIdList(List<String> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}

}
