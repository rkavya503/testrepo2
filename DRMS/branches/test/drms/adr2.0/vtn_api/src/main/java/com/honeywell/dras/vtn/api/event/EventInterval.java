package com.honeywell.dras.vtn.api.event;

import java.util.Date;

public class EventInterval {
	
	private Date start;
	private Long duration;
	private String offsetId;
	private Float value;
	private String stringValue;
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
	 * @return the offsetId
	 */
	public String getOffsetId() {
		return offsetId;
	}
	/**
	 * @param offsetId the offsetId to set
	 */
	public void setOffsetId(String offsetId) {
		this.offsetId = offsetId;
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
	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}
	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
