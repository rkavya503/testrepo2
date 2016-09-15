package com.honeywell.dras.vtn.api.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honeywell.dras.vtn.api.EventStatus;
import com.honeywell.dras.vtn.api.ResponseRequired;
import com.honeywell.dras.vtn.api.common.EiTarget;

public class Event {
	
	private String eventID = null;
	private Long modificationNumber = 0L;
	private Date modificationDate;
	private String modificationReason;
	private String marketContext;
	private Date createdTime;
	private Date startTime;
	private Long duration;
	private Long notification;
	private Long rampUp;
	private Long recovery;
	private List<Signal> signals;
	private BaseLine baseline;
	private Long priority;
	private Boolean test = false;
	private Boolean heatingEvent = false;
	private String vtnComment;
	private EiTarget target;
	private ResponseRequired responseRequired;
	private Long toleranceStartAfter;
	private EventStatus eventStatus;
	/**
	 * @return the eventID
	 */
	public String getEventID() {
		return eventID;
	}
	/**
	 * @param eventID the eventID to set
	 */
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	/**
	 * @return the modificationNumber
	 */
	public Long getModificationNumber() {
		return modificationNumber;
	}
	/**
	 * @param modificationNumber the modificationNumber to set
	 */
	public void setModificationNumber(Long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}
	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}
	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	/**
	 * @return the modificationReason
	 */
	public String getModificationReason() {
		return modificationReason;
	}
	/**
	 * @param modificationReason the modificationReason to set
	 */
	public void setModificationReason(String modificationReason) {
		this.modificationReason = modificationReason;
	}
	/**
	 * @return the marketContext
	 */
	public String getMarketContext() {
		return marketContext;
	}
	/**
	 * @param marketContext the marketContext to set
	 */
	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	/**
	 * @return the signals
	 */
	public List<Signal> getSignals() {
		if (signals == null) {
			signals = new ArrayList<Signal>();
		}
		return signals;
	}
	/**
	 * @param signals the signals to set
	 */
	public void setSignals(List<Signal> signals) {
		this.signals = signals;
	}
	/**
	 * @return the baseline
	 */
	public BaseLine getBaseline() {
		return baseline;
	}
	/**
	 * @param baseline the baseline to set
	 */
	public void setBaseline(BaseLine baseline) {
		this.baseline = baseline;
	}
	/**
	 * @return the priority
	 */
	public Long getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	/**
	 * @return the test
	 */
	public Boolean getTest() {
		return test;
	}
	/**
	 * @param test the test to set
	 */
	public void setTest(Boolean test) {
		this.test = test;
	}
	/**
	 * @return the heatingEvent
	 */
	public Boolean getHeatingEvent() {
		return heatingEvent;
	}
	/**
	 * @param heatingEvent the heatingEvent to set
	 */
	public void setHeatingEvent(Boolean heatingEvent) {
		this.heatingEvent = heatingEvent;
	}
	/**
	 * @return the vtnComment
	 */
	public String getVtnComment() {
		return vtnComment;
	}
	/**
	 * @param vtnComment the vtnComment to set
	 */
	public void setVtnComment(String vtnComment) {
		this.vtnComment = vtnComment;
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
	 * @return the responseRequired
	 */
	public ResponseRequired getResponseRequired() {
		return responseRequired;
	}
	/**
	 * @param responseRequired the responseRequired to set
	 */
	public void setResponseRequired(ResponseRequired responseRequired) {
		this.responseRequired = responseRequired;
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
	 * @return the eventStatus
	 */
	public EventStatus getEventStatus() {
		return eventStatus;
	}
	/**
	 * @param eventStatus the eventStatus to set
	 */
	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventID == null) ? 0 : eventID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventID == null) {
			if (other.eventID != null)
				return false;
		} else if (!eventID.equals(other.eventID))
			return false;
		return true;
	}
}
