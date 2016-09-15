package com.akuacom.pss2.drw.service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.Event;
import com.akuacom.pss2.drw.EventDAO;
import com.akuacom.pss2.drw.EventDAOImpl;
import com.akuacom.pss2.drw.EventStore;
import com.akuacom.pss2.drw.RTPWeather;
import com.akuacom.pss2.drw.SlapBlocks;
import com.akuacom.pss2.drw.cache.DrCache;
import com.akuacom.pss2.drw.cache.DrEventCache;
import com.akuacom.pss2.drw.cache.EventCategory;
import com.akuacom.pss2.drw.cache.LocationCache;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.entry.Category;
import com.akuacom.pss2.drw.entry.HistoryResultEntry;
import com.akuacom.pss2.drw.entry.ResultList;
import com.akuacom.pss2.drw.util.ActiveEventPredicateFilter;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.util.EventPredicateFilter;
import com.akuacom.pss2.drw.util.Md5Util;
import com.akuacom.pss2.drw.util.PredicateFilter;
import com.akuacom.pss2.drw.util.ScheduleIREventPredicateFilter;
import com.akuacom.pss2.drw.value.EventLoaction;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.utils.lang.DateUtil;
import com.akuacom.pss2.drw.entry.AlertHistoryEntry;
import com.akuacom.pss2.drw.entry.AlertsList;
import com.akuacom.pss2.drw.value.AlertValue;

@Path("/services")
public class EventsResource {
	
	private static final Logger log = Logger.getLogger(EventsResource.class);
	EventDAO evtDao = new EventDAOImpl();
	//TODO: issue time enhancement
	
	@Path("/getEventStore")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public EventStore getEventStore( @QueryParam("categoryNames")  List<String> categoryNames, @QueryParam("rtp") boolean rtp){
		EventStore es = new EventStore();
		for(String name:categoryNames){
			Category cate = DrEventCache.getInstance().getCategory(name);
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
				events = (ArrayList<Event>) groupingConsolidation(true, events, Event.class,"product","startDateTime","endDateTime");
			}else if("IR".equalsIgnoreCase(cate.getEventType())){
				events = (ArrayList<Event>) groupingConsolidation(false, events, Event.class,"product","startDateTime","endDateTime");
			}
			
			Collections.sort(events, new EventComparator());
			es.getEvents().put(name, events);
			es.getLastUpdateTime().put(name, DrEventCache.getInstance().getStatus().getCacheStatus(EventCategory.valueOf(name)).getTime());
			
		}
		
		es.setUpdateTime(new Date());
		if(rtp){
			es.setTodayWeather(DrCache.getInstance().getCurrentRTP());
			es.setForecastWeather(DrCache.getInstance().getForcastRTPEvents());
		}
	
		return es;
	}
	
	class EventComparator implements Comparator<Event> {
	    @Override
	    public int compare(Event o1, Event o2) {
	    	int result = o1.getStartDateTime().compareTo(o2.getStartDateTime());
	    	if(result!=0){
	    		return result;
	    	}
	    	if(o1.getEndDateTime()==null){
	    		return -1;
	    	}
	    	if(o2.getEndDateTime()==null){
	    		return 1;
	    	}
 	
	        return o1.getEndDateTime().compareTo(o2.getEndDateTime());
//	        Returns:
//	        	the value 0 if the argument Date is equal to this Date; a value less than 0 if this Date is before the Date argument; and a value greater than 0 if this Date is after the Date argument. 

	    }
	}
	@Path("/getAllEvents")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Event> getAllEvents(@QueryParam("program") String program, @QueryParam("product")  List<String> product, @QueryParam("active") boolean active) {
		if(product==null) product = new ArrayList<String>();
		Category cate = null;
		List<Category> categories = DrEventCache.getInstance().getCategory(program, product, active);
		if(categories==null||categories.isEmpty()) return null;
		if(categories.size()>0) cate = categories.get(0);
		
		PredicateFilter predicate = null; 
		predicate = new EventPredicateFilter(Arrays.asList(program), product);
		if(cate.isActive()){
			predicate.add(new ActiveEventPredicateFilter());
		}else{
			predicate.add(new ScheduleIREventPredicateFilter());
		}
		
		List<Event> events = evtDao.getEvents(predicate);
		
		if("CBP".equalsIgnoreCase(program)){
			events = groupingConsolidation(true, events, Event.class,"startDateTime","endDateTime");
		}else if("IR".equalsIgnoreCase(cate.getEventType())){
			events = groupingConsolidation(false, events, Event.class,"startDateTime","endDateTime");
		}
		
		return events;
	}
	
	/**
	 * It takes 3 parameters
	 * @param events the list will be merged
	 * @param clazz  The Class of the element in your list
	 * @param fieldNames The group by conditions
	 * @return
	 */
	public static List<Event> groupingConsolidation(Boolean isCBP, List<Event> events,Class clazz,String... fieldNames){
		List<Event> list = new ArrayList<Event>();
		Map<String,List<Event>> eventsGroupMap = new LinkedHashMap<String,List<Event>>();
		for (Event Event : events) {
		    List eventsForGroup = eventsGroupMap.get(getHashKey(Event, clazz, fieldNames));
		    if (eventsForGroup == null) {
		        eventsForGroup = new ArrayList<Event>();
		        eventsGroupMap.put(getHashKey(Event, clazz, fieldNames), eventsForGroup);
		    }
		    eventsForGroup.add(Event);
		}
			
		for(Entry<String,List<Event>> element:eventsGroupMap.entrySet()){
		
			List<Event> itemsInner = element.getValue();
			Event temp = null;
			Boolean mergeBlocks = true;
			int[][] array = new int[itemsInner.size()][2];
			Set<String> aggSet = new HashSet<String>();
			
			List<EventLoaction> locations = new ArrayList<EventLoaction>();
			int index = 0;
			for(Event event: itemsInner){
				if(temp==null){
					temp = event;
					temp.getAggNames().clear();
					aggSet.clear();aggSet.add(event.getUuid());
					locations.clear();
					if(event.getLocations()!=null&&(!event.getLocations().isEmpty())){
						locations.addAll(event.getLocations());
					}
//					temp.getAggNames().add(event.getUuid());
					if(temp.getMaxBlock()>0){
						array[index][0] = event.getMinBlock();
						array[index++][1] = event.getMaxBlock();
					}
					continue;
				}

				aggSet.add(event.getUuid());				//temp.getAggNames().add(event.getUuid());
				if(event.getLocations()!=null&&(!event.getLocations().isEmpty())){
					locations.addAll(event.getLocations());
				}
				if(mergeBlocks&&event.getMaxBlock()>0){
					if(event.getMaxBlock()==Integer.MAX_VALUE&&event.getMinBlock()==0){
						//event is all
						array = new int[1][2];
						array[0][0] = event.getMinBlock();
						array[0][1] = event.getMaxBlock();
						mergeBlocks = false;
						continue;
					}
					
					boolean goReturn = false;
					for(int[] item:array){
						
						if(item[1]>=event.getMaxBlock()&&item[0]<=event.getMinBlock()){
							//temp is exist or is a subset
							goReturn = true;
							break;
						}
						
						if(item[1]<=event.getMaxBlock()&&item[0]>=event.getMinBlock()){
							//temp is exist or is a subset
							item[1]=event.getMaxBlock();
							item[0]=event.getMinBlock();
							goReturn = true;
							break;
						}
						
					}
					if(goReturn) continue;
//					
//					for(int[] item:array){
//						//keep insert operation by order
//						if(item[1]>event.getMaxBlock()){
//							//temp is exist or is a subset
//							array[index][0] = item[0];
//							array[index++][1] = item[1];
//							
//							item[0] = event.getMinBlock();
//							item[1] = event.getMaxBlock();
//							goReturn = true;
//							break;
//						}
//					}
//					if(goReturn) continue;
					// append in the end of array
					array[index][0] = event.getMinBlock();
					array[index++][1] = event.getMaxBlock();
				}
			}//end of loop
			bubbleSort(array);
			
			Set<String> zipcodeSet = new HashSet<String>();
			Set<String> countySet = new HashSet<String>();
			Set<String> citySet = new HashSet<String>();
			
			for(EventLoaction lo : locations){
				if(!StringUtils.isEmpty(lo.getZipCode())){
					zipcodeSet.add(lo.getZipCode());
				}
				
				if(!StringUtils.isEmpty(lo.getCountyName())){
					countySet.add(lo.getCountyName());
				}
				
				if(!StringUtils.isEmpty(lo.getCityName())){
					citySet.add(lo.getCityName());
				}
				
			}
			
			List<String> zipcodes = new ArrayList<String>(zipcodeSet);
			java.util.Collections.sort(zipcodes);
			temp.setZipCodes(zipcodes.toString());
			
			List<String> counties = new ArrayList<String>(countySet);
			java.util.Collections.sort(counties);
			temp.setCountyNames(counties.toString());
			
			
			List<String> cities = new ArrayList<String>(citySet);
			java.util.Collections.sort(cities);
			temp.setCityNames(cities.toString());
			
			List<String> agg = new ArrayList<String>(aggSet);
			java.util.Collections.sort(agg);
			temp.setAggNames(agg);
			
			boolean goReturn = false;
			String blocks = "";
			for(int[] item:array){
				if(item[1]==Integer.MAX_VALUE&&item[0]==0){
					goReturn = true;
					blocks = "All Blocks";
					continue;
				}
			}
			if(!goReturn){
				for(int[] item:array){
					if(item[0]==0) continue;
					if(item[1]==item[0]){
						blocks =blocks+item[0]+", ";
					}else{
						blocks =blocks+item[0]+"-"+item[1]+", ";
					}
				}
			}
			if(blocks.endsWith(",")){
				blocks = blocks.substring(0, blocks.length()-1);
			}
			
			/*if("BIP".equalsIgnoreCase(temp.getProgram()))
			{
				StringTokenizer tokenizer = new StringTokenizer(blocks,",");
				List<String> selectedBlocks = new ArrayList<String>(tokenizer.countTokens());
				while (tokenizer.hasMoreTokens()) {
					String block = tokenizer.nextToken().trim();
					if(block.length() > 0)
						selectedBlocks.add(block);
				}
				CFEventManager cfEventManager = DRWUtil.getCFEventManager();
				List<String> availableBlocks = cfEventManager.getAllBlock();
				boolean isAll = DRWUtil.isAllBlocks(selectedBlocks, availableBlocks);
				if(isAll)
					blocks =  "All Blocks";
				
			}*/
				
			temp.setLocationBlock(blocks);
			if(isCBP){
				temp.setEventKey(Md5Util.getMD5Hashing(temp.getProduct()+temp.getStartDateTime().getTime()+temp.getEndDateTime()));
			}else{
				temp.setEventKey(Md5Util.getMD5Hashing(temp.getProduct()+temp.getStartDateTime().getTime()+temp.getEndDateTime()));
			}
			list.add(temp);
		}
		
		return list;
	}
	
	private static void bubbleSort(int[][] array) {
	    boolean swapped = true;
	    int j = 0;
	    int[] tmp;
	    while (swapped) {
	        swapped = false;
	        j++;
	        for (int i = 0; i < array.length - j; i++) {
	            if (array[i][0] > array[i + 1][0]) {
	                tmp = array[i];
	                array[i] = array[i + 1];
	                array[i + 1] = tmp;
	                swapped = true;
	            }
	        }
	    }
	}

	public static String getHashKey(Event Event,Class clazz,String... fieldNames){
		StringBuffer key = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		for(String fieldName : fieldNames){
			Field field = null;
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			Object obj = null;
			try {
				field.setAccessible(true);
				obj = field.get(Event);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(obj==null){obj="null";}
			objs.add(obj);
		}
		for (Object s : objs) {
			key.append(s.hashCode());
	    }
		return key.toString();
	}
	
	
	@Path("/getLastUpdateTime")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getLastUpdateTime(@QueryParam("category")  List<String> categorys) {
		
		if(categorys==null) return "";
		StringBuilder sb = new StringBuilder();
		sb.append("{\"timeStamp\":{");
		sb.append("\"systemTime\""+":" +"\"");
		sb.append(DateUtil.format(new Date(), "MM/dd/yyyy hh:mma")+"\"");
		for(String paraName : categorys){
			try{
				Date date = evtDao.getLastUpdateTime(paraName);
				long result = -1L;
				if(date!=null) result = date.getTime();
				sb.append(",");
				sb.append("\""+paraName+"\""+":");
				sb.append("\""+result+"\"");
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		sb.append("}}");
		
		return sb.toString();
		
	}
	
	/**
	 * Function to provide program list for the HISTORY page  
	 * @return program JSON list
	 */
	@Path("/getPrograms")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Program> getPrograms() {
		return evtDao.getPrograms();
	}
	
	/**
	 * Function to provide product list for the HISTORY page 
	 * @param programName
	 * @param irProgram
	 * @return product JSON list
	 */
	@Path("/getProducts")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getProducts(@QueryParam("programName")  String programName,@QueryParam("irProgram")  boolean irProgram) {
		if(programName==null) return "";
		List<String> result = evtDao.getProducts(programName, irProgram);
		
		//[{"product":"BIP-15"},{"product":"BIP-30"},{"product":"BIP-50"}]
		StringBuilder sb = new StringBuilder();
		sb.append("[{\"product\":\"ALL\"}");
		for(String product : result){
			sb.append(",");
			sb.append("{\"product\":");
			sb.append("\""+product+"\"");
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
		
	}

	/**
	 * Function to provide product list for the HISTORY page 
	 * @param programName
	 * @param irProgram
	 * @return product JSON list
	 */
	@Path("/getHistoryEvents")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ResultList getHistoryEvents(@QueryParam("program") String programName,@QueryParam("product") String product,@QueryParam("zipcode") List<String> zipcode,@QueryParam("start") String start,@QueryParam("end") String end,@QueryParam("index") int index,@QueryParam("offset") int offset) {		
		List<EventValue> result = evtDao.getHistoryEvents(programName,product,zipcode,start,end);
		DRWUtil.sortEventList(result);
		List<HistoryResultEntry> entries = new ArrayList<HistoryResultEntry>();
		for(EventValue ev:result){
			HistoryResultEntry entry = new HistoryResultEntry();
			if("BIP".equalsIgnoreCase(programName)){
				ev.setProduct("BIP");
			}
			entry.setEventValue(ev);
			entries.add(entry);
		}
		ResultList model = new ResultList();
		model.setUpdateTime(new Date());
		model.setCategoryList(entries);
		model.setIndexFrom(index);
		model.setOffset(offset);
		model.build();
		return model;
	}
	
	/**
	 * Function to provide product list for the HISTORY page 
	 * @param programName
	 * @param irProgram
	 * @return product JSON list
	 */
	@Path("/getHistoryTems")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public ResultList getHistoryTems(@QueryParam("start") String start,@QueryParam("end") String end,@QueryParam("index") int index,@QueryParam("offset") int offset) {
		List<WeatherValue> result = new ArrayList<WeatherValue>();
		ResultList model = new ResultList();
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(start);
			endDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(end);
			
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		result = evtDao.getHistoryTems(startDate,endDate);
		List<HistoryResultEntry> entries = new ArrayList<HistoryResultEntry>();
		for(WeatherValue ev:result){
			HistoryResultEntry entry = new HistoryResultEntry();
			entry.setWeatherValue(ev);
			entries.add(entry);
		}
		model.setUpdateTime(new Date());
		model.setCategoryList(entries);
		model.setIndexFrom(index);
		model.setOffset(offset);
		model.build();
		return model;
	}
	
	@Path("/getBlocks")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SlapBlocks getBlocks() {
		SlapBlocks sb = new SlapBlocks();
		sb.setScecBlocks(LocationCache.getInstance().getSlapBlocks().get("SCEC"));
		sb.setScenBlocks(LocationCache.getInstance().getSlapBlocks().get("SCEN"));
		sb.setScewBlocks(LocationCache.getInstance().getSlapBlocks().get("SCEW"));
		sb.setSchdBlocks(LocationCache.getInstance().getSlapBlocks().get("SCHD"));
		sb.setScldBlocks(LocationCache.getInstance().getSlapBlocks().get("SCLD"));
		sb.setScnwBlocks(LocationCache.getInstance().getSlapBlocks().get("SCNW"));
		
		return sb;
	}
	
	@Path("/getAlertHistory")
	@GET
	@Produces("application/json")
	public AlertsList getAlertHistory(@QueryParam("device") String deviceKey,@QueryParam("index") int index,@QueryParam("offset") int offset) {		
		log.info("Get Alert History of deviceKey = "+deviceKey+"with index = "+index+" and offset = "+offset);
		List<AlertValue> result = evtDao.getAlertHistory(deviceKey);
		//DRWUtil.sortEventList(result);
		List<AlertHistoryEntry> entries = new ArrayList<AlertHistoryEntry>();
		for(AlertValue ev:result){
			AlertHistoryEntry entry = new AlertHistoryEntry();
			
			entry.setAlertHistory(ev);
			entries.add(entry);
		}
		AlertsList model = new AlertsList();
		model.setUpdateTime(new Date());
		model.setCategoryList(entries);
		model.setIndexFrom(index);
		model.setOffset(offset);
		model.build();
		return model;
	}
	
}
