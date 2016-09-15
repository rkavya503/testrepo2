package com.akuacom.pss2.drw.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventValue;


public class EventListViewManagerImpl {
	/** logger **/
	private static final Logger log = Logger.getLogger(EventListViewManagerImpl.class);
	/** The class singleton instance **/
	private static EventListViewManagerImpl instance = new EventListViewManagerImpl();

	

	private EventListViewManagerImpl(){
		super();
	}
	
	public static EventListViewManagerImpl getInstance(){
		return instance;
	}
	public void initialize(){
		
	}
	
	public List<String> retrieveCountyList(){
		List<String> countyList = new ArrayList<String>();
		try{
			StateMachineManagerImpl.getInstance().setGetCountysSuccessFlag(false);
			if(DRWUtil.getCFEventManager()!=null){
				countyList = DRWUtil.getCFEventManager().getCountyName();
				StateMachineManagerImpl.getInstance().setGetCountysSuccessFlag(true);
			}
		}catch(Exception e){
			log.error("DR webstie event list view manager get countys error: "+e.getMessage());
		}
		return countyList;
	}
	public List<String> retrieveCityList(){
		List<String> result = new ArrayList<String>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				result = DRWUtil.getCFEventManager().getCityName();
			}
		}catch(Exception e){
			log.error("DR webstie event list view manager get citys by county name error: "+e.getMessage());
		}
		return result;
	}
	
	public List<String> retrieveCityList(String countyName){
		List<String> result = new ArrayList<String>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				result = DRWUtil.getCFEventManager().getCity(countyName);
			}
		}catch(Exception e){
			log.error("DR webstie event list view manager get citys by county name error: "+e.getMessage());
		}
		return result;
	}
	
	public synchronized List<String> searchCityByCounty(String county){
		
		List<String> result = new ArrayList<String>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				result = DRWUtil.getCFEventManager().getCity(county);
			}
		}catch(Exception e){
			log.error("DR webstie event list view manager get citys by county name error: "+e.getMessage());
		}
		return result;
	}
	
	public synchronized List<EventValue> search(List<String> productList,String county,String city,String zipCode){
		List<String> zipCodes = new ArrayList<String>();
		if(zipCode==null||zipCode.trim().equalsIgnoreCase("")){
			zipCodes = null;
		}else{
			zipCodes.add(zipCode);
		}
		if(county==null||county.trim().equalsIgnoreCase("")||county.trim().equalsIgnoreCase("All")||county.trim().equalsIgnoreCase("Select")){
			county = null;
		}
		if(city==null||city.trim().equalsIgnoreCase("")||city.trim().equalsIgnoreCase("All")||city.trim().equalsIgnoreCase("Select")){
			city = null;
		}
		List<EventValue> eventValueList = new ArrayList<EventValue>();
		try{
			eventValueList =DRWUtil.getCFEventManager().getListView(productList, county, city, zipCodes);
		}catch(Exception e){
			log.error("DR webstie event list view manager search results by filter error: "+e.getMessage());
		}
		return eventValueList;
	}
	
	


}
