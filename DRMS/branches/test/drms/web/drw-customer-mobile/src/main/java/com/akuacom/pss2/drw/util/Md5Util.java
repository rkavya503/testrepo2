package com.akuacom.pss2.drw.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.akuacom.pss2.drw.Event;
import com.akuacom.pss2.drw.EventDAO;
import com.akuacom.pss2.drw.EventDAOImpl;
import com.akuacom.pss2.drw.cache.DrEventCache;
import com.akuacom.pss2.drw.cache.EventCategory;
import com.akuacom.pss2.drw.entry.Category;
import com.akuacom.pss2.drw.service.EventsResource;

public class Md5Util {
	public static String getMD5Hashing(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(input.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}
	
	public static String getMD5Hashing(List<String> input) {
		java.util.Collections.sort(input);
		//keep in order
		StringBuilder sb = new StringBuilder();
		for(String in:input){
			sb.append(in);
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(sb.toString().getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb2.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb2.toString();
	}
	static EventDAO evtDao = new EventDAOImpl();
	public static List<String> getEventDetailUuids(String eventKey){
		
		for(EventCategory cateName:EventCategory.values()){
			Category cate = DrEventCache.getInstance().getCategory(cateName.name());
			PredicateFilter predicate = null; 
			List<String> products = (cate.getProducts()==null)? new ArrayList<String>():cate.getProducts().getProductsList();
			predicate = new EventPredicateFilter(Arrays.asList(cate.getProgramName()), products);
			if(cate.isActive()){
				predicate.add(new ActiveEventPredicateFilter());
			}else{
				predicate.add(new ScheduleIREventPredicateFilter());
			}
			
			ArrayList<Event> events = (ArrayList<Event>) evtDao.getEvents(predicate);
			
			if("CBP".equalsIgnoreCase(cate.getProgramName())){
				events = (ArrayList<Event>) EventsResource.groupingConsolidation(true, events, Event.class,"product","startDateTime","endDateTime");
			}else if("IR".equalsIgnoreCase(cate.getEventType())){
				events = (ArrayList<Event>) EventsResource.groupingConsolidation(false, events, Event.class,"parentUuid","startDateTime","endDateTime");
			}
			
			for(Event eve:events){
				if(eventKey.equalsIgnoreCase(eve.getEventKey())){
					return eve.getAggNames();
				}
			}
		}
		return new ArrayList<String>();
	}

}
