package com.honeywell.dras.vtn.api.common;

import java.util.Date;

public class WsCalanderInterval {
	
	private Date start;
	private Long duration;
	private Long toleranceStartAfter;
	private Long notification;
	private Long rampUp;
	private Long recovery;
	
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
	 * @return the toleranceStartAfter
	 */
	public Long getToleranceStartAfter() {
		return toleranceStartAfter;
	}
	/**
	 * @param toleranceStartAfter the toleranceStartAfter to set
	 */
	public void setToleranceStartAfter(Long toleranceStartAfter) {
		this.toleranceStartAfter = toleranceStartAfter;
	}
	/**
	 * @return the notification
	 */
	public Long getNotification() {
		return notification;
	}
	/**
	 * @param notification the notification to set
	 */
	public void setNotification(Long notification) {
		this.notification = notification;
	}
	/**
	 * @return the rampUp
	 */
	public Long getRampUp() {
		return rampUp;
	}
	/**
	 * @param rampUp the rampUp to set
	 */
	public void setRampUp(Long rampUp) {
		this.rampUp = rampUp;
	}
	/**
	 * @return the recovery
	 */
	public Long getRecovery() {
		return recovery;
	}
	/**
	 * @param recovery the recovery to set
	 */
	public void setRecovery(Long recovery) {
		this.recovery = recovery;
	}
	

}
