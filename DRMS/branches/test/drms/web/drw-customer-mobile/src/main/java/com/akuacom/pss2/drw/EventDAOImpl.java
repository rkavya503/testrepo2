package com.akuacom.pss2.drw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jboss.logging.Logger;

import com.akuacom.pss2.drw.cache.DrEventCache;
import com.akuacom.pss2.drw.cache.EventCategory;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.service.EventsResource;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.util.PredicateFilter;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.pss2.drw.value.AlertValue;

public class EventDAOImpl implements EventDAO {

	@Override
	public List<Event> getEvents(PredicateFilter filter) {
		List<Event> eventsAll = DrEventCache.getInstance().getList();
		List<Event> result = filter.select(eventsAll);
		
        List<Event> filteredEvent = new ArrayList<Event>();
        
        for (Event event : result)  {
        	Date issueTime = event.getIssueTime();
        	        	
        	if(issueTime != null) {
        		if(issueTime.before(new Date())) {
        			filteredEvent.add(event);
        		}
        	}
        }
        return filteredEvent;

	}

	@Override
	public Date getLastUpdateTime(String category) {
		return DrEventCache.getInstance().getStatus().getCacheStatus(EventCategory.valueOf(category));
	}
	
	@Override
	public List<Program> getPrograms() {
		List<Program> result = DRWUtil.getDREvent2013Manager().getAllProgram();
		return result;
	}
	@Override
	public List<String> getProducts(String programName,boolean isIRProgram) {
		List<String> result = new ArrayList<String>();
		if("RTP".equalsIgnoreCase(programName)){
			result.add("RTP");
		}else if("CPP".equalsIgnoreCase(programName)){
			result.add("Residential");
			result.add("Commercial");
		}else{
			result = DRWUtil.getDREvent2013Manager().getProducts(programName, isIRProgram);
		}
		return result;
	}

	@Override
	public List<EventValue> getHistoryEvents(String programName,
			String product, List<String> zipcode, String start, String end) {
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(start);
			endDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if("ALL".equalsIgnoreCase(product)){
			product = null;
		}
		return DRWUtil.getCFEventManager().getHistoryEvent(programName, product, startDate, endDate, zipcode);
	}

	@Override
	public List<WeatherValue> getHistoryTems(Date start, Date end) {
		
		return DRWUtil.getCFEventManager().getHistoryTems("RTP", start, end);
	}
	
	@Override
	public List<AlertValue> getAlertHistory(String deviceKey) {
		//return null;
		return DRWUtil.getCFEventManager().getAlertHistory(deviceKey);
	}
}
