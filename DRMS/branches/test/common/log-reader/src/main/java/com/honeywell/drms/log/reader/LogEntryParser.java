package com.honeywell.drms.log.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogEntryParser {
	
	//IMPORTANT following patterns should be aligned with log4j configuration
	protected static Pattern pStart = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d{3}");
	protected  SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss','SSS");
	
	private static final Logger log = Logger.getLogger(LogEntryParser.class);
	
	private static Pattern pAll   = Pattern.compile(
			"^(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d{3})\\s*(\\w*)\\s*\\[([-\\w]*(\\.\\S*)*)\\]\\s*(e\\$\\{[^}]*})?\\s*((.*)(\\r?\\n.*)*)",
			Pattern.MULTILINE
	);
	
	private static Pattern pEvent = Pattern.compile(
			"(\\w):([^;]*)"
	);
	
	public boolean isNewRecordEntry(String str){
		if(str.length()<23) return false;
		String subStr= str.substring(0,23);
		Matcher m =pStart.matcher(subStr);
		return (m.find());
	}
	
	public Date getLogTime(String str){
		if(str.length()<23) return null;
		String subStr= str.substring(0,23);
		Date date = null;
		try {
			date = dateFormatter.parse(subStr);
		} catch (ParseException e) {
		}
		return date;
	}
	
	public FireLogEntry parse(String logStr){
		try{
			FireLogEntry entry=doParse(logStr);
			return entry;
		}catch(Exception e){
			log.error("Failed to parse log "+logStr, e);
			return null;
		}
	}
	
	public FireLogEntry doParse(String logStr){
		Matcher m =pAll.matcher(logStr);
		if(m.find()){
			FireLogEntry entry = new FireLogEntry();
        	String time = m.group(1);
        	try{
        		Date date = dateFormatter.parse(time);
        		entry.setLogDate(date);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	int level = LogUtils.getIntLevel(m.group(2));
        	entry.setLogLevel(level);
        	entry.setStrLogLevel(m.group(2));
        	String category = m.group(3);
        	entry.setCategory(category);
        	String event = m.group(5);
        	
        	if(event!=null){
        		event = event.substring(2,event.length()-1);
        		Matcher eParser =pEvent.matcher(event);
	        	while(eParser.find()){
	        		char att = eParser.group(1).charAt(0);
	            	String value = eParser.group(2);
	            	switch(att){
	            		case 'p':
	            			entry.setUserParam1(value);
	            			break;
	            		case 'l':
	            			entry.setLineNum(Integer.parseInt(value));
	            			break;
	            		case 'u':
	            			entry.setUserName(value);
	            			break;
	            		case 'm':
	            			entry.setMethodName(value);
	            			break;
	            		case 'r':
	            			entry.setUserRole(value);
	            			break;
	            		case 's':
	            			entry.setSessionID(value);
	            			break;
	            		case 'd':
	            			entry.setDescription(value);
	            			break;
	            		case 'f':
	            			entry.setFileName(value);
	            			break;
	            		case 'i':
	            			entry.setUserParam3(value);
	            			break;
	            	}
	        	}
        	}
        	String longdescription = m.group(6);
        	String description = m.group(7);
        	String shortDescription = description;
        	//long message
        	String additional = m.group(8);
        	if(shortDescription.length()>128){
        		shortDescription= shortDescription.substring(0,128);
        	}
        	if(additional==null){
        		if(entry.getDescription()==null){
        			entry.setDescription(shortDescription);
        		}
        		entry.setLongDescr(longdescription);
        	}else{
        		if(entry.getDescription()==null){
        			entry.setDescription(shortDescription);
        		}
        		entry.setLongDescr(longdescription);
        	}
        	return entry;
        }
		return null;
	}
}
