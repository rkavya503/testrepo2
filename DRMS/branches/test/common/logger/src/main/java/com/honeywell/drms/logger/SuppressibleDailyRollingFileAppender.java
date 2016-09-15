package com.honeywell.drms.logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.spi.LoggingEvent;
import org.jboss.logging.appender.DailyRollingFileAppender;

public class SuppressibleDailyRollingFileAppender extends DailyRollingFileAppender {

	private Map<String,Date> suppressed= new HashMap<String,Date>();
	
	public SuppressibleDailyRollingFileAppender() {
		super();
	}
	
	protected synchronized boolean shouldSuppressed(SuppressibleLogEntry evt){
		Date suppressedFrom=suppressed.get(evt.getSupressId());
		if(suppressedFrom==null){
			suppressed.put(evt.getSupressId(),new Date());
			return false;
		}
		int duration = (int) ((new Date().getTime()-suppressedFrom.getTime())/1000);
		if(duration>evt.getSuppressDuration()){
			suppressed.put(evt.getSupressId(),new Date());
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void append(LoggingEvent event) {
		if(event.getMessage() instanceof SuppressibleLogEntry){
			if(shouldSuppressed((SuppressibleLogEntry)event.getMessage())){
				//suppressed the logging
				return;
			}
		}
		super.append(event);
	}
}
