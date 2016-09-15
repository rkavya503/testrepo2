/**
 * 
 */
package com.akuacom.pss2.drw.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.pss2.drw.Event;
import com.akuacom.pss2.drw.entry.Category;

public class EventStatusCache {

	private Map<EventCategory, Date> statusMap = new HashMap<EventCategory, Date>();

	private static EventStatusCache instance = new EventStatusCache();

	private EventStatusCache() {
	}

	public EventStatusCache initCacheValue() {
		Date now = new Date();
		for (EventCategory category : EventCategory.values()) {
			setCacheStatus(category, now);
		}
		return this;
	}

	// update cacheStatus also need to trigger event change
	public void setCacheStatus(EventCategory catogary, Date date) {
		statusMap.put(catogary, date);
	}

	public static EventStatusCache getInstance() {
		return instance;
	}

	public Date getCacheStatus(EventCategory catogary) {
		return statusMap.get(catogary);
	}

	public Map<EventCategory, Date> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<EventCategory, Date> statusMap) {
		this.statusMap = statusMap;
	}

	public void setCacheStatus(Event event, Date date, Boolean active) {
		// event.getProgram(), event.getProduct() event.getStartTime()
		List<Category> categoryList = DrEventCache.getInstance().getCategory(event.getProgram(), Arrays.asList(event.getProduct()), active);
		List<EventCategory> categories = new ArrayList<EventCategory>();
		for(Category cate : categoryList){
			categories.add(EventCategory.valueOf(cate.getName()));
		}
				
		setCacheStatus(categories, date);
	}

	public void setCacheStatus(List<EventCategory> catogaryList, Date date) {
		for (EventCategory catogary : catogaryList) {
			setCacheStatus(catogary, date);
		}
	}

}
