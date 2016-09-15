package com.akuacom.pss2.richsite.program.configure.rtp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class RTPLine {
	private List<String> values = new ArrayList<String>();
	private XMLGregorianCalendar endTime;
	private String endTimeString="";
	
	public String getEndTimeString() {
		if(endTimeString.equalsIgnoreCase("")){
			setEndTimeString("Start Temperature");
		}
		return endTimeString;
	}

	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}

	public void setEndTime(XMLGregorianCalendar endTime) {
		this.endTime = endTime;
		if(endTime!=null){
			if(endTime.toString().equalsIgnoreCase("")){
				this.setEndTimeString("Start Temperature");
			}else{
				Date endTimeDate = transferXMLGregorianCalendarToDate(endTime);
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				this.setEndTimeString(format.format(endTimeDate));
			}
		}else{
			this.setEndTimeString("Start Temperature");
		}
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public XMLGregorianCalendar getEndTime() {
		return endTime;
	}

	public static Date transferXMLGregorianCalendarToDate(XMLGregorianCalendar calendar){
    	int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        int hour = calendar.getHour();
        int minute = calendar.getMinute();
        int second = calendar.getSecond();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, second);
        Date date = c.getTime();
        return date;
    }
}
