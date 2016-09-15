/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.DateUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils.lang;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * General extensions to functions against the {@link Date} and {@link Calendar}
 * classes.
 * 
 * Consider looking in apache commons for existing functions too.
 * 
 */
public class DateUtil {

    public static final long MSEC_IN_DAY = 24 * 60 * 60 * 1000;
    public static final long MSEC_IN_MIN = 60 * 1000;
    public static final long MSEC_IN_SEC = 1000;

    private static final Map<String, ThreadLocal<SoftReference<DateFormat>>> formatters = Collections
            .synchronizedMap(new HashMap<String, ThreadLocal<SoftReference<DateFormat>>>());

    public static DateFormat dateFormatHHmm() {
        return dateFormat("HH:mm");
    }

    private static DateFormat dateFormat(String fmt) {
        ThreadLocal<SoftReference<DateFormat>> fmts;
        synchronized (formatters) {
            fmts = formatters.get(fmt);
            if (fmts == null) {
                fmts = new ThreadLocal<SoftReference<DateFormat>>();
                formatters.put(fmt, fmts);
            }
        }
        SoftReference<DateFormat> ref = fmts.get();
        if (ref != null) {
            DateFormat result = ref.get();
            if (result != null) {
                return result;
            }
        }
        DateFormat result = new SimpleDateFormat(fmt);
        ref = new SoftReference<DateFormat>(result);
        fmts.set(ref);
        return result;
    }

    public static DateFormat dateFormatMMdd() {
        return dateFormat("MM/dd");
    }

    /** The Constant DEFAULT_TIMEZONE. */
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone
            .getTimeZone("America/Los_Angeles");

    /** The Constant DATEFORMATER. */
    /*
     * DateFormats are inherently unsafe for multithreaded use. Sharing a single
     * instance across thread boundaries without proper synchronization will
     * result in erratic behavior of the application. You may also experience
     * serialization problems. Using an instance field is recommended.
     */
    public static DateFormat dateFormatter() {
        return dateFormat("yyyy-MM-dd");
    }
    
    public static DateFormat shortDateFormatter() {
        return dateFormat("yyMMdd");
    }


    public static DateFormat dateTimeFormatter() {
        return dateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    /**
     * Merge two date objects, one which hold just a time (timeRef) and one
     * which holds just a date (dateRef).
     * 
     * @param timeRef
     *            the time ref
     * @param dateRef
     *            the date ref
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
        tDate.set(dDate.get(Calendar.YEAR), dDate.get(Calendar.MONTH),
                dDate.get(Calendar.DAY_OF_MONTH));
        return tDate.getTime();
    }

    public static Date stripDate(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date stripTime(long date) {
        return stripTime(new Date(date));
    }
    
    public static Date stripTime(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date endOfDay(long date) {
        return endOfDay(new Date(date));
    }
    
    public static Date endOfDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /** Given the date this will return a date without the milliseconds. */
    public static Date stripMillis(Date date) {
        long millis = date.getTime();
        int factor = 1000;
        millis /= factor;
        millis *= factor;
        return new Date(millis);
    }

    public static String format(Date time, String format) {
    	if(null==time) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(time);
    }

    public static Date parseDate(String dateStr,String pattern,TimeZone zone) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	sdf.setTimeZone(zone);
    	return sdf.parse(dateStr);
    }
    
    public static String format(Date time,String pattern,TimeZone zone){
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	sdf.setTimeZone(zone);
    	return sdf.format(time);
    }
    
    
    public static String formatDate(Date time) {
        String format = "MMM dd, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(time);
    }

    public static String format(Date time) {
        if (time == null)
            return "";
        String format = "MMM dd, yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(time);
    }

    /**
     * Gets the offset of day.
     * 
     * @param date
     *            time to be calculated
     * 
     * @return offset of the starting of the day determined by date in milli
     *         second
     */
    public static long getOffsetOfDay(Date date) {
        final Date start = getStartOfDay(date);
        return date.getTime() - start.getTime();
    }

    /**
     * Gets the start of day.
     * 
     * @param date
     *            time to be calculated
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
     * @param cal
     *            the cal
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
     * Get the first day of the year
     * 
     * @param cal
     * @return
     */
    public static Date getFirstDayOfYear(Calendar cal) {
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTime();
    }

    /**
     * Get the last day of the year
     * 
     * @param cal
     * @return
     */
    public static Date getLastDayOfYear(Calendar cal) {
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DATE, 31);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTime();
    }

    /**
     * Get the first day of the year
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getFirstDayOfYear(cal);
    }

    /**
     * Get the last day of the year
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getLastDayOfYear(cal);
    }

    /**
     * Get the date from input date and day interval parameter
     * 
     * @param date
     * @param intervalDay
     * @return
     */
    public static Date getDate(Date date, int intervalDay) {
        if (date != null) {
            long diff = intervalDay * 24 * 60 * 60 * 1000;
            return new Date(date.getTime() + diff);
        } else {
            return date;
        }
    }

    /**
     * Gets the end of day.
     * 
     * @param date
     *            the date
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
     * @param cal
     *            the cal
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
     * @param date
     *            the date
     * 
     * @return the date string
     */
    public static String getDateString(Date date) {
        return dateFormatter().format(date);
    }

    /**
     * return minute difference between two dates
     */
    public static int minuteOffset(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / MSEC_IN_MIN);
    }

    /**
     * return a new Date time offset to one date by given minutes
     * 
     * @param date
     */
    public static Date offSetBy(Date date, int min) {
        return new Date(date.getTime() + min * MSEC_IN_MIN);
    }

    public static Date stripTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getNextDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }

    public static Date getNext(Date date,int days){
    	Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    
    public static Date parseStringToDate(String value, DateFormat pattern) {
        if (null == pattern) {
            pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            return pattern.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getPreviousDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }
   
    public static Date add(Date date, int field, int offset) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(field, offset);

        return cal.getTime();
    }
    
    public static Calendar endOfDay(Calendar date) {
    	date.set(Calendar.HOUR_OF_DAY, 23);
    	date.set(Calendar.MINUTE, 59);
    	date.set(Calendar.SECOND, 59);
    	date.set(Calendar.MILLISECOND, 999);
    	
    	return date;
    }
   
}
