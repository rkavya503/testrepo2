package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

public class EventTiming implements Serializable {
	
	private static final long serialVersionUID = 5842884387113815529L;

    private Date startDate;
    
    private Date endDate;
    
    private int startHour;

    private int startMin;

    private int startSec;

    private int endHour;

    private int endMin;

    private int endSec;
    
    private Date issuedDate;
    
    private int issuedHour;

    private int issuedMin;

    private int issuedSec;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartTime() {
		return construct(startDate,this.startHour,this.startMin,this.startSec);
	}

	public Date getEndTime() {
		return construct(endDate,this.endHour,this.endMin,this.endSec);
	}
	
	public Date getIssuedTime() {
		return construct(issuedDate,this.issuedHour,this.issuedMin,this.issuedSec);
	}
	
	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public int getIssuedHour() {
		return issuedHour;
	}

	public void setIssuedHour(int issuedHour) {
		this.issuedHour = issuedHour;
	}

	public int getIssuedMin() {
		return issuedMin;
	}

	public void setIssuedMin(int issuedMin) {
		this.issuedMin = issuedMin;
	}

	public int getIssuedSec() {
		return issuedSec;
	}

	public void setIssuedSec(int issuedSec) {
		this.issuedSec = issuedSec;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getStartMin() {
		return startMin;
	}

	public void setStartMin(int startMin) {
		this.startMin = startMin;
	}

	public int getStartSec() {
		return startSec;
	}

	public void setStartSec(int startSec) {
		this.startSec = startSec;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMin() {
		return endMin;
	}

	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}

	public int getEndSec() {
		return endSec;
	}

	public void setEndSec(int endSec) {
		this.endSec = endSec;
	}
	
	private static String formatTime(int i){
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}
	
	private static Date construct(Date date,int hour,int min, int sec){
		if(date==null) return null;
		final Calendar calendar =  Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	// option list for time & hour selection
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
	private static List<SelectItem> secList;

	    
	public List<SelectItem> getHourList() {
		return hourList;
	}

	public List<SelectItem> getMinList() {
		return minList;
	}

	public List<SelectItem> getSecList() {
		return secList;
	}
	
	static {
		hourList = new ArrayList<SelectItem>();
        for (int i = 0; i < 24; i++) {
        	hourList.add(new SelectItem(i,formatTime(i)));
        }
		
        minList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	minList.add(new SelectItem(i,formatTime(i)));
        }
        
        secList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	secList.add(new SelectItem(i,formatTime(i)));
        }
	}

}
