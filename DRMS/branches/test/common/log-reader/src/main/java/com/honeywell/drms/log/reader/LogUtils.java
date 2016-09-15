package com.honeywell.drms.log.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Priority;

public class LogUtils {
	
	public static int  getLineTerminatorlength(){
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
			return 2;
		}else
			return 1;
	}
	
	//the following patterns must be aligned with log4j configuration 
	protected static Pattern LOG_FILE_PATTERN = Pattern.compile("(server\\.log)(\\.(\\d{4}-\\d{2}-\\d{2}))?");
	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final long MSEC_IN_DAY = 24 * 60 * 60 * 1000;
	  
	
	public static Date  getLogFileDate(String fileName){
		Matcher m =LOG_FILE_PATTERN.matcher(fileName);
		Date date = null;
		  while(m.find()){
			String log = m.group(1);
	        String time1 = m.group(2);
	        String time = m.group(3);
	        if(time1==null) {
	        	if(log!=null) date = new Date();
	        }else{
	        	try {
	        		date = dateFormatter.parse(time);
	        	} catch (ParseException e) {}
	        }
		  }
		  return date;
	}
	
	public static boolean isValidLogFileName(String fileName){
		Matcher m =LOG_FILE_PATTERN.matcher(fileName);
		return m.find();
	}
	
	public static Date getStartOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTime();
	}
	  
	public static Date getEndOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.AM_PM, Calendar.PM);
		return cal.getTime();
	}
	
	public static int getIntLevel(String level){
		if(level.equals("DEBUG")){
			return Priority.DEBUG_INT;
		}
		else if(level.equals("INFO")){
			return Priority.INFO_INT;
		}
		else if(level.equals("WARN")){
			return Priority.WARN_INT;
		}
		else if(level.equals("ERROR")){
			return Priority.ERROR_INT;
		}
		else if(level.equals("FATAL")){
			return Priority.FATAL_INT;
		}
		return Priority.ALL_INT;
	}
	
	public static String getStrLogLevel(int logLevel){
		switch(logLevel){
		case Priority.DEBUG_INT:
			return "DEBUG";
		case Priority.INFO_INT:
			return "INFO";
		case Priority.WARN_INT:
			return "WARN";
		case Priority.ERROR_INT:
			return "ERROR";
		case Priority.FATAL_INT:
			return "FATAL";
		}
		return "ALL";
	}
	
	
	
}
