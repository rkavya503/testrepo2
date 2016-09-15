package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import com.akuacom.utils.DateUtil;

public class DateTimeStructure implements Serializable{

	private static final long serialVersionUID = 589612731239238636L;
	
	private Date date;
	
	private int hour = 0;
	
	private int min =  0;
	
	private int sec =  0;
	
	public Date getDateTime(){
		final Calendar calendar =  Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public Date getDate() {
		return DateUtil.stripTime(date);
	}
	
	public void setDate(Date date) {
		this.date = DateUtil.stripTime(date);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}
	
}
