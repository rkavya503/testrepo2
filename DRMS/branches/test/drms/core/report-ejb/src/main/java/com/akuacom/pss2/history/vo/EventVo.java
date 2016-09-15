package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.Date;

import com.akuacom.utils.lang.DateUtil;

public class EventVo implements Serializable{
	
	private static final long serialVersionUID = -8208210080551355992L;
	
	private String eventName;
	private Date startTime;
	private Date endTime;
	private String strStartTime;
	private String strEndTime;
	
	public String getEventName() {
		return eventName;
	}
	public String getStrStartTime() {
		 return DateUtil.format(startTime, "yyyy-MM-dd-HH-mm-ss");
	}
	public String getStrEndTime() {
		 return DateUtil.format(endTime, "yyyy-MM-dd-HH-mm-ss");
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}
	
}
