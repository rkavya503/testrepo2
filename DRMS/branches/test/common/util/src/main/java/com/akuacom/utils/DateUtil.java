/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.DateUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * General extensions to functions against the {@link java.util.Date} and {@link java.util.Calendar} classes.
 * 
 * Consider looking in apache commons for existing functions too.
 * 
 */
public class DateUtil {

    public static final long MSEC_IN_DAY = 24 * 60 * 60 * 1000;
    public static final long MSEC_IN_MIN = 60 * 1000;
    public static final long MSEC_IN_SEC = 1000;
    /** The Constant DEFAULT_TIMEZONE. */
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("America/Los_Angeles");

    static {
    	getDateFormatter().setTimeZone(DEFAULT_TIMEZONE);
    	getDateTimeFormatter().setTimeZone(DEFAULT_TIMEZONE);
    }

    /**
     * Merge two date objects, one which hold just a time (timeRef) and
     * one which holds just a date (dateRef).
     * 
     * @param timeRef the time ref
     * @param dateRef the date ref
     * 
     * @return the merged date
     */
    public static Date mergeDate(Date timeRef, Date dateRef) {
        // get date reference cal object
        final Calendar dDate = Calendar.getInstance();
        dDate.setTime(dateRef);
        // get time reference
        final Calendar tDate = Calendar.getInstance();
        tDate.setTime(timeRef);
        // overwrite time reference' date parts
        tDate.set(dDate.get(Calendar.YEAR), dDate.get(Calendar.MONTH), dDate.get(Calendar.DAY_OF_MONTH));
        return tDate.getTime();
    }
    
    public static Date stripDate(Date date)
    {
     	Calendar cal = new GregorianCalendar();
    	cal.setTime(date);
    	cal.set(Calendar.YEAR, 0);
    	cal.set(Calendar.DAY_OF_YEAR, 1);
    	return cal.getTime();
    }
    
    public static Date stripTime(Date date)
    {
     	Calendar cal = new GregorianCalendar();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal.getTime();
    }
    
    public static Date endOfDay(Date date)
    {
     	Calendar cal = new GregorianCalendar();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 23);
    	cal.set(Calendar.MINUTE, 59);
    	cal.set(Calendar.SECOND, 59);
    	cal.set(Calendar.MILLISECOND, 999);
    	return cal.getTime();
    }
    
    public static Calendar endOfDay(Calendar date)
    {
    	date.set(Calendar.HOUR_OF_DAY, 23);
    	date.set(Calendar.MINUTE, 59);
    	date.set(Calendar.SECOND, 59);
    	date.set(Calendar.MILLISECOND, 999);
    	return date;
    }
    
    /**Given the date this will return a date without the milliseconds.*/
    public static Date stripMillis(Date date){
    	long millis = date.getTime();
    	int factor = 1000;
		millis /= factor;
    	millis *= factor;
    	return new Date(millis);
    }
    
    public static String format(Date time, String format)
    {
    	if(time == null) return "";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	return sdf.format(time);
    }
    
    public static String formatDate(Date time)
    {
       if(time == null) return "";
        String format = "MMM dd, yyyy";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	return sdf.format(time);
    }
    
    public static String format(Date time)
    {
    	if(time == null) return "";
    	String format = "MMM dd, yyyy HH:mm:ss";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	return sdf.format(time);
    }

    /**
     * Gets the offset of day.
     *
     * @param date time to be calculated
     *
     * @return offset of the starting of the day determined by date in milli second
     */
    public static long getOffsetOfDay(Date date) {
        final Date start = getStartOfDay(date);
        return date.getTime() - start.getTime();
    }

    /**
     * Gets the start of day.
     *
     * @param date time to be calculated
     *
     * @return the starting point of the day determined by date
     */
    public static Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getStartOfDay(cal);
    }

    /**
     * Gets the start of day.
     *
     * @param cal the cal
     *
     * @return the start of day
     */
    public static Date getStartOfDay(Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTime();
    }

    /**
     * Gets the end of day.
     *
     * @param date the date
     *
     * @return the end of day
     */
    public static Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getEndOfDay(cal);
    }

    /**
     * Gets the end of day.
     *
     * @param cal the cal
     *
     * @return the end of day
     */
    public static Date getEndOfDay(Calendar cal) {
        cal.set(Calendar.HOUR, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.set(Calendar.AM_PM, Calendar.PM);
        return cal.getTime();
    }

    /**
     * Gets the date string.
     *
     * @param date the date
     *
     * @return the date string
     */
    public static synchronized String getDateString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * Gets the date formatter.
     *
     * @return the date formatter
     */
    public static synchronized DateFormat getDateFormatter() {
    	return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Gets the date time formatter.
     *
     * @return the date time formatter
     */
    public static synchronized DateFormat getDateTimeFormatter() {
    	return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }
    
    public static Date parse(String time, String format)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	try {
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * get a time that is enough big
     * @return
     */
    public static Date getBigEnoughDate(){
    	//10 year later 
    	//sometimes a null time means a time big enough in the future
    	return new Date(System.currentTimeMillis() + 10*365*MSEC_IN_DAY);
    }
    
    public static Date add(Date date, int field, int offset)
    {
     	Calendar cal = new GregorianCalendar();
    	cal.setTime(date);
    	cal.add(field, offset);

    	return cal.getTime();
    }
    
    /**
     * Returns -1 if there was an error, other wise 2.5 if two and half hours between
     * dates.
     * 
     * @param dateStart - earliest date
     * @param dateEnd - latest date
     * @return
     */
    public static double getHoursBetweenDates(Date dateStart, Date dateEnd) {
    	double res = -1.0;
    	if (dateStart == null || dateEnd == null) {
    		return res;
    	}
    	
    	if (!dateStart.before(dateEnd)) {
    		return res;
    	}
    		
		// I am also ignoring daylight savings time  for now
		double diffInHours = (dateEnd.getTime() - dateStart.getTime()) / (60.0 * 60.0 * 1000.0);
    	long rounded = Math.round(diffInHours * 100);
		res = rounded / 100.0;
		
    	return res;
    }
    
    public static Calendar stripCalendarTime(Date date)
    {
     	Calendar cal = new GregorianCalendar();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal;
    }

    public static String getServerTimeZoneInfo() {
        SimpleDateFormat f1 = new SimpleDateFormat("z");
        SimpleDateFormat f2 = new SimpleDateFormat("zzzz");
        Date date = new Date();
        return f1.format(date) + ", " + f2.format(date);
    }
}
