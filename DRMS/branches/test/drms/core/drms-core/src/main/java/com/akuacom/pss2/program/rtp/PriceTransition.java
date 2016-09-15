package com.akuacom.pss2.program.rtp;

import java.text.DateFormat;
import java.util.Date;

public class PriceTransition {
	private Date time;
	private double price;
	private long duration;
	private String locationID;
	
	public Date getTime() { return time; }
	public void setTime(Date startTime) 
	{ 
		this.time = new Date(startTime.getTime());
	}
	
	public double getPrice() { return price; }
	public void setPrice(Double price) 
	{
		this.price = price;
	}
	
	public long getDuration() { return duration; }
	public void setDurationSeconds(long duration)
	{
		this.duration = duration;
	}
	
	public String getLocationID() { return locationID; }
	public void setLocationID( String locationID )
	{
		this.locationID = locationID;
	}
	
	public String toString()
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		String dateString = df.format(getTime());
		
		return ("PriceTransition "+locationID+" "+dateString+" "+price+" for "+duration+" seconds");
	}
}
