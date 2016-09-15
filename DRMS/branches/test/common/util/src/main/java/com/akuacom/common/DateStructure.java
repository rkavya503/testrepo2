package com.akuacom.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * DateStructure breaks the date into two parts <li>Pure Date part, no hours,
 * minutes ...</li> <li>Time Part, only hour and minute. other parts are
 * always zero</li> 
 * 
 */
public class DateStructure implements Serializable,Cloneable {

	private static final long serialVersionUID = 6441220225469893492L;

	private Date date;

	private int hour = -1;
	private int min = -1;

	public DateStructure(Date date) {
		initFromDate(date);
	}
	
	public static String formateTime(int hour, int min){
		String str1 = hour+"";
		if(hour<10) str1= "0"+str1;
		String str2 = min+"";
		if(min<10) str2 = "0"+min;
		return str1+":"+str2;
	}

	public DateStructure() {
		final Calendar current = Calendar.getInstance();
		hour = current.get(Calendar.HOUR_OF_DAY);
		current.set(Calendar.HOUR_OF_DAY, hour);
		min = current.get(Calendar.MINUTE);
		current.set(Calendar.MINUTE, min);
		current.set(Calendar.SECOND, 0);
		current.set(Calendar.MILLISECOND, 0);
		this.date = current.getTime();
	}

	private void initFromDate(Date date) {
		final Calendar dDate = Calendar.getInstance();
		dDate.setTime(date);
		// hour and min
		hour = dDate.get(Calendar.HOUR_OF_DAY);
		min = dDate.get(Calendar.MINUTE);
		// clear others
		dDate.set(Calendar.SECOND, 0);
		dDate.set(Calendar.MILLISECOND, 0);

		this.date = dDate.getTime();
	}

	public String toString() {
		return this.date.toString();
	}

	/**
	 * return time part, format HH:MM
	 * 
	 * @return
	 */
	public String getTimeStr() {
		return formateTime(hour, min);
	}

	/**
	 * set Time part, format HH:MM
	 * 
	 * @param time
	 */
	public void setTimeStr(String time) {
		if (time == null || time.trim().length() == 0)
			return;
		String strs[] = time.split(":");
		if (strs.length == 2) {
			hour = Integer.parseInt(strs[0]);
			min = Integer.parseInt(strs[1]);
		} else if (strs.length == 1) {
			hour = Integer.parseInt(strs[0]);
			min = 0;
		}
	}

	/**
	 * return date
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 
	 */
	public Date getDateOnly(){
		final Calendar dDate = Calendar.getInstance();
		dDate.setTime(date);
		dDate.set(Calendar.HOUR_OF_DAY, 0);
		dDate.set(Calendar.MINUTE, 0);
		dDate.set(Calendar.SECOND, 0);
		dDate.set(Calendar.MILLISECOND, 0);
		return dDate.getTime();
	}
	
	/**
	 * Set its date part, time part is ignored
	 * 
	 * @param date
	 */
	public void setDateOnly(Date date) {
		if (date != null) {
			final Calendar dDate = Calendar.getInstance();
			dDate.setTime(date);
			dDate.set(Calendar.HOUR_OF_DAY, hour);
			dDate.set(Calendar.MINUTE, min);
			dDate.set(Calendar.SECOND, 0);
			dDate.set(Calendar.MILLISECOND, 0);
			this.date = dDate.getTime();
		}
	}

	/**
	 * set both date part and time part, seconds and milliseconds are ignored
	 * 
	 * @param date
	 */
	public void setTime(Date date) {
		initFromDate(date);
	}

	/**
	 * get the time represented by this structure
	 * 
	 * @return
	 */
	public Date getTime() {
		final Calendar dDate = Calendar.getInstance();
		dDate.setTime(date);
		dDate.set(Calendar.HOUR_OF_DAY, hour);
		dDate.set(Calendar.MINUTE, min);
		dDate.set(Calendar.SECOND, 0);
		dDate.set(Calendar.MILLISECOND, 0);
		dDate.set(Calendar.MILLISECOND, 0);
		return dDate.getTime();
	}
	
	public Object clone(){
	  try{
		  DateStructure cloned = (DateStructure)super.clone();
		  return cloned;
	  }catch(Exception e){
		  return null;
	  }
	}
	
}
