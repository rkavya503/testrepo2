package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;

public class ListEvent implements Serializable, Comparable<ListEvent>{

    private boolean deleted = false;
	private String eventName;
	private String programName;
	private Date startTime;
    private Date endTime;
    private Date receivedTime;
    private Date issuedTime;
    private transient String state;
    private boolean manualTerminate;
    private boolean isDemoEvent;
    
    private boolean canDeleteEvent;
    
    public boolean getCanDeleteEvent(){
    	return this.canDeleteEvent;
    }
    
    public void setCanDeleteEvent(boolean value){
    	this.canDeleteEvent = value;
    }
    
    
    public ListEvent(Event event) {
    	this.setEndTime(event.getEndTime());
    	this.setEventName(event.getEventName());
    	this.setProgramName(event.getProgramName());
    	this.setReceivedTime(event.getReceivedTime());
    	this.setIssuedTime(event.getIssuedTime());
    	this.setStartTime(event.getStartTime());
    	this.setState(event.getState());
    	this.setDeleted(false);
    }
    
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}
	public Date getIssuedTime() {
		return issuedTime;
	}
	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public boolean isManualTerminate() {
		return manualTerminate;
	}

	public void setManualTerminate(boolean manualTerminate) {
		this.manualTerminate = manualTerminate;
	}

	public String getReceivedTimeStr() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(receivedTime);
	}
	
	public int compareTo(ListEvent o) {
		if (o == null) {
			return -1;
		}
		
		int res = this.getStartTime().compareTo(o.getStartTime());
		return res;
	}
	public boolean isDemoEvent(){
		return this.isDemoEvent;
	}
	public void setDemoEvent(boolean value){
		this.isDemoEvent = value;
	}
}
