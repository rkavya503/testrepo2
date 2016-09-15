package com.akuacom.pss2.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.utils.DateUtil;

public class BIPEventUtil {
	
	public static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public static List<String> getAllEventLocations(Event event){
		return new ArrayList<String>( getAllEventLocationAndEndTime(event).keySet());
	}
	
	public static List<String> getAllActiveEventLocations(Event event){
		Map<String,Date> endTimes= getAllEventLocationAndEndTime(event);
		List<String> locations = new ArrayList<String>();
		for(String loc:endTimes.keySet()){
			Date endTime1 =  endTimes.get(loc);
			if(endTime1==null || endTime1.after(new Date()))
				locations.add(loc);
		}
		return locations;
	}
	
	public static Map<String, Date> getAllEventLocationAndEndTime(Event event){
		String locationInfo=event.getLocationInfo();
		List<String> arrays = Arrays.asList(locationInfo.split(","));
		Map<String,Date> results = new HashMap<String,Date>();
		//locationInfo 1105[2014-02-14 00:00:00]
		for(String str:arrays){
			Date endTime = event.getEndTime();
			String locNum = str;
			locNum=locNum.trim();
			if(str.contains("[") && str.contains("]")){
				locNum = str.substring(0,str.indexOf("["));
				String dateStr = str.substring(str.indexOf("[")+1,str.indexOf("]"));
				endTime = DateUtil.parse(dateStr, DATE_FORMAT);
			}
			results.put(locNum, endTime);
		}
		return results;
	}
	
	public static void updateEventLocationStr(Event event,List<String> locations, Date endTime){
		Map<String, Date> endTimes = getAllEventLocationAndEndTime(event);
		for(String loc:endTimes.keySet()){
			if(locations.contains(loc)){
				endTimes.put(loc, endTime);
			}
		}
		String locationInfo=null;
		for(String loc:endTimes.keySet()){
			if(locationInfo==null)
				locationInfo =loc;
			else locationInfo+=","+loc;
			Date locEndTime = endTimes.get(loc);
			if(locEndTime!=null){
					locationInfo+="["+DateUtil.format(locEndTime, DATE_FORMAT)+"]";
			}
		}
		event.setLocationInfo(locationInfo);
	}
	
	public static Date getBiggestEndTime(Event event){
		Map<String,Date> locationEndTimes= getAllEventLocationAndEndTime(event);
		Date max = null;
		for(String loc:locationEndTimes.keySet()){
			Date locEndTime = locationEndTimes.get(loc);
			if(locEndTime==null) return null;
			if(locEndTime!=null){
				if(max==null)
					max=locEndTime;
				else if(locEndTime.after(max))
					max = locEndTime;
			}
		}
		return max;
	}
	
	public static boolean isSameEndTimeForAllLocations(Event event){
		if(event.getEndTime()==null) return false;
		Map<String,Date> locationEndTimes= getAllEventLocationAndEndTime(event);
		for(String loc:locationEndTimes.keySet()){
			Date locEndTime = locationEndTimes.get(loc);
			long ms= locEndTime==null? 0:locEndTime.getTime();
			long ms1 = event.getEndTime()==null?0:event.getEndTime().getTime();
			if(ms!=ms1)
				return false;
		}
		return true;
	}
}
