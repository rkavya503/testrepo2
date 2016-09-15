package com.honeywell.dras.vtn.api;

import java.util.Date;
import java.util.List;

/**
 * Event
 * @author sunil
 *
 */
public interface Oadr2Event {

	public String getEventID();
	
	public long getModificationNumber();
	
	public String getMarketContext();
	
	public Date getStartTime();
	
	public long getDuration();
	
	public long getNotification();
	
	public List<Oadr2Signal> getSignals();
	
	public Long getPriority();
	
	public EventStatus getEventStatus();
	
	public boolean isTest();
	
	public String getVtnComment();
	
	public Target getTarget();
	
	public ResponseRequired getResponseRequired();
	
	public long getRampUp();
	
	public long getRecovery();
	
	public long getToleranceStartBefore();
	
	public long getToleranceStartAfter();
	
	public boolean isHeatingEvent();
	
	
}
