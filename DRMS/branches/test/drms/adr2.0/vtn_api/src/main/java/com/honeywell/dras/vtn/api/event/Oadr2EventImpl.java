package com.honeywell.dras.vtn.api.event;

import java.util.Date;
import java.util.List;

import com.honeywell.dras.vtn.api.EventStatus;
import com.honeywell.dras.vtn.api.Oadr2Event;
import com.honeywell.dras.vtn.api.Oadr2Signal;
import com.honeywell.dras.vtn.api.ResponseRequired;
import com.honeywell.dras.vtn.api.Target;

public class Oadr2EventImpl implements Oadr2Event{

	private String eventID = null;

	private long modificationNumber = 0;

	private String marketContext;

	private Date createdTime;

	private Date startTime;

	private long duration;

	private long notification;
	
	private long rampUp;
	
	private long recovery;

	private List<Oadr2Signal> signals;


	private Long priority;

	private boolean test = false;
	
	private boolean heatingEvent = false;

	private String vtnComment;

	private Target target;

	private ResponseRequired responseRequired;
	
	private long toleranceStartBefore;
	
	private long toleranceStartAfter;
	
	private EventStatus eventStatus;
	


	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public long getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(long modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public void updateModificationNumber() {
		++modificationNumber;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getNotification() {
		return notification;
	}

	public void setNotification(long notification) {
		this.notification = notification;
	}

	public List<Oadr2Signal> getSignals() {
		return signals;
	}

	public void setSignals(List<Oadr2Signal> signals) {
		this.signals = signals;
	}



	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getVtnComment() {
		return vtnComment;
	}

	public void setVtnComment(String vtnComment) {
		this.vtnComment = vtnComment;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public ResponseRequired getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(ResponseRequired responseRequired) {
		this.responseRequired = responseRequired;
	}


	public long getRampUp() {
		return rampUp;
	}

	public void setRampUp(long rampUp) {
		this.rampUp = rampUp;
	}

	public long getRecovery() {
		return recovery;
	}

	public void setRecovery(long recovery) {
		this.recovery = recovery;
	}

	public long getToleranceStartBefore() {
		return toleranceStartBefore;
	}

	public void setToleranceStartBefore(long toleranceStartBefore) {
		this.toleranceStartBefore = toleranceStartBefore;
	}

	public long getToleranceStartAfter() {
		return toleranceStartAfter;
	}

	public void setToleranceStartAfter(long toleranceStartAfter) {
		this.toleranceStartAfter = toleranceStartAfter;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	@Override
	public boolean isHeatingEvent() {
		return heatingEvent;
	}

	public void setHeatingEvent(boolean heatingEvent) {
		this.heatingEvent = heatingEvent;
	}

}
