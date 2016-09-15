package com.akuacom.pss2.drw.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import scala.actors.threadpool.Arrays;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.DRLocationManager;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.LocationCache;
import com.akuacom.pss2.drw.value.EventLegend;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.system.SystemManager;

public class DRWUtil {
	
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

	public static <T> T findHandler(Class<T> ServiceType, String serviceName) {
		try {
			Context namingContext = new InitialContext();
			@SuppressWarnings("unchecked")
			T ret = (T) namingContext.lookup(serviceName);
			return ret;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private static DREventManager dREventManager;

	public static DREventManager getEvtManager() {
		if (dREventManager == null) {
			dREventManager = findHandler(DREventManager.class,
					"dr-pro/DREventManager/remote");
		}
		return dREventManager;
	}

	private static DRLocationManager dRLocationManager;

	public static DRLocationManager getLocationManager() {
		if (dRLocationManager == null) {
			dRLocationManager = findHandler(DRLocationManager.class,
					"dr-pro/DRLocationManager/remote");
		}
		return dRLocationManager;
	}

	private static CFEventManager cfEventManager;

	public static CFEventManager getCFEventManager() {
		if (cfEventManager == null) {
			cfEventManager = findHandler(CFEventManager.class,
					"dr-pro/CFEventManager/remote");
		}
		return cfEventManager;
	}
	private static DREvent2013Manager drEvent2013Manager;

	public static DREvent2013Manager getDREvent2013Manager() {
		if (drEvent2013Manager == null) {
			drEvent2013Manager = findHandler(DREvent2013Manager.class,
					"dr-pro/DREvent2013Manager/remote");
		}
		return drEvent2013Manager;
	}
	private static SystemManager systemManager;

	public static SystemManager getSystemManager() {
		if (systemManager == null) {
			systemManager = findHandler(SystemManager.class,
					"pss2/SystemManagerBean/remote");
		}
		return systemManager;
	}

	@SuppressWarnings("unchecked")
	public static List<String> listByComma(String expression) {
		List<String> result = new ArrayList<String>();
		if (expression != null && (!expression.trim().equalsIgnoreCase(""))) {
			result = Arrays.asList(expression.split(","));
		}
		return result;
	}

	static public Object getJSFSessionBackingBean(String beanName) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(beanName);
	}

	static public void setJSFSessionBackingBean(String beanName, Object bean) {
		if (bean != null) {
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put(beanName, bean);
		}
	}

	public static List<String> trimList(List<String> list) {
		List<String> result = new ArrayList<String>();
		for (String tmp : list) {
            String trimmed = tmp.trim();
            if (!trimmed.equalsIgnoreCase("")) {
				result.add(trimmed);
			}
		}
		return result;
	}

	public static List<String> concatList(List<List<String>> list) {
		List<String> result = new ArrayList<String>();

		for (List<String> tmp : list) {
			tmp = trimList(tmp);
			for (String string : tmp) {
				if (!result.contains(string.trim())) {
					result.add(string.trim());
				}
			}
		}
		sortList(result);
		return result;
	}

	public static void sortList(List<String> aItems) {
		if(aItems!=null){
			Collections.sort(aItems, String.CASE_INSENSITIVE_ORDER);
		}
	}
	public static Number toNumber(String s){		
		try {
			NumberFormat format = NumberFormat.getInstance(Locale.US);

	        Number number = format.parse(s);
	        return number;
		} catch (ParseException e) {
			return null;
		}
	}

	public static void sortNumberList(List<Number> aItems) {
		if(aItems!=null){
			Collections.sort(aItems,new Comparator<Number>() {
			    @Override
			    public int compare(Number o1, Number o2) {
			         Double d1= (o1 == null) ? Double.POSITIVE_INFINITY : o1.doubleValue();
			         Double d2= (o2 == null) ? Double.POSITIVE_INFINITY : o2.doubleValue();
			        return  d1.compareTo(d2);
			    }
			});
		}
	}

	public static void sortEventList(List<EventValue> aItems) {
		if(aItems!=null){
			EventValueComparator comparator = new EventValueComparator();
			
			Collections.sort(aItems, comparator);
		}
	}
	public static void sortEventModelList(List<BaseEventDataModel> aItems) {
		if(aItems!=null){
			EventModelComparator comparator = new EventModelComparator();
			
			Collections.sort(aItems, comparator);
		}
	}
	public static Map<String, String> sortMapByKey(Map<String, String> aItems) {
		TreeMap<String, String> result = new TreeMap<String, String>(
				String.CASE_INSENSITIVE_ORDER);
		result.putAll(aItems);
		return result;
	}
	
	public static boolean isAllBlocks(List<String> blocks){
		boolean result = false;
		
		List<String> allBlocks = EventCache.getInstance().getAllBlocks();
		int size = allBlocks.size();
		int count=0;
		for(int i=0;i<blocks.size();i++){
			String block = blocks.get(i);
			for(String b:allBlocks){
				if(block.equalsIgnoreCase(b)){
					count++;
					break;
				}
			}
		}
		if(size==count){
			result = true;
		}
		return result;
	}
	
	public static Map<String,List<Number>> getSlapBlockMap(){
		return EventCache.getInstance().getSlapBlockMap();
	}

	public static String SCEC="SCEC";
	public static String SCEN="SCEN";
	public static String SCNW="SCNW";
	public static String SCEW="SCEW";
	public static String SCHD="SCHD";
	public static String SCLD="SCLD";
	
	public static String getSlapDisplayString(String slapName){
		String result ="";
		boolean flag = getSlapBlockMap().containsKey(slapName);
		if(flag){
			List<Number> n = getSlapBlockMap().get(slapName);
			int size = n.size();
			if(size>0){
				Long first = (Long) n.get(0);
				Long last = (Long) n.get(size-1);
				result = first +" - " + last;
			}
		}
		return result;
	}
	public static String getBlockDisplayString(List<Number> block){
		StringBuffer result = new StringBuffer();
		List<Number> cloneList = new ArrayList<Number>();
		for(Number b:block){
			cloneList.add(b);
		}
		List<Number> scec = getSlapBlockMap().get(SCEC);
		List<Number> scen = getSlapBlockMap().get(SCEN);
		List<Number> scnw = getSlapBlockMap().get(SCNW);
		List<Number> scew = getSlapBlockMap().get(SCEW);
		List<Number> schd = getSlapBlockMap().get(SCHD);
		List<Number> scld = getSlapBlockMap().get(SCLD);
		if(cloneList.containsAll(scec)){
			result = result.append(getSlapDisplayString(SCEC) +", ");
			cloneList.removeAll(scec);
		}else{
			for(Number b:cloneList){
				if(scec.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}
		if(cloneList.containsAll(schd)){
			result = result.append(getSlapDisplayString(SCHD) +", ");
			cloneList.removeAll(schd);
		}else{
			for(Number b:cloneList){
				if(schd.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}
		if(cloneList.containsAll(scld)){
			result = result.append(getSlapDisplayString(SCLD) +", ");
			cloneList.removeAll(scld);
		}else{
			for(Number b:cloneList){
				if(scld.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}
		if(cloneList.containsAll(scen)){
			result = result.append(getSlapDisplayString(SCEN) +", ");
			cloneList.removeAll(scen);
		}else{
			for(Number b:cloneList){
				if(scen.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}
		if(cloneList.containsAll(scnw)){
			result = result.append(getSlapDisplayString(SCNW) +", ");
			cloneList.removeAll(scnw);
		}else{
			for(Number b:cloneList){
				if(scnw.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}
		if(cloneList.containsAll(scew)){
			result = result.append(getSlapDisplayString(SCEW) +", ");
			cloneList.removeAll(scew);
		}else{
			for(Number b:cloneList){
				if(scew.contains(b)){
					result = result.append(String.valueOf(b)).append(", ");
				}
			}
		}


//		for(Number b:cloneList){
//			result = result.append(String.valueOf(b)).append(", ");
//		}
		return result.toString();
	}
	public static List<String> sortListAsNumber(List<String> list) {
		List<String> result = new ArrayList<String>();
		List<Number> listN = new ArrayList<Number>();
		if (list!=null&&list.size()>0) {
			for (String b:list){
				
				Number num = DRWUtil.toNumber(b);
				if(num!=null){
					listN.add(num);
				}
			}
		}
		DRWUtil.sortNumberList(listN);
		for (Number b:listN){
			if(b!=null){
				result.add(String.valueOf(b));
			}
		}
		return result;
	}
	
	public static boolean filterScheduleEvent(EventValue ev){
		boolean result = false;
		Date issueTime = ev.getIssueTime();
		if(issueTime==null){
			result = true;
		}else{
			if(issueTime.before(new Date())){
				result = true;
			}
		}
		return result;
	}
	public static boolean filterScheduleLegend(EventLegend ev){
		boolean result = false;
		Date issueTime = ev.getIssueTime();
		if(issueTime==null){
			result = true;
		}else{
			if(issueTime.before(new Date())){
				result = true;
			}
		}
		return result;
	}
	public static List<EventLegend> getFilterScheduleLegend(List<EventLegend> list){
		List<EventLegend> result = new ArrayList<EventLegend>();
		for(EventLegend el:list){
			if(filterScheduleLegend(el)){
				result.add(el);
			}
		}
		return result;
	}
	
	public static List<EventLegend> sortEventLegends(List<EventLegend> aItems) {
		if(aItems!=null){
			EventLegendComparator comparator = new EventLegendComparator();
			
			Collections.sort(aItems, comparator);
		}
		
		return aItems;
	}
}