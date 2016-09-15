package com.akuacom.pss2.data.usage.calcimpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.akuacom.utils.DateUtil;

public class AkuaDaySelectorPredicate implements Predicate {
	
	public static int ELIMINATE_WEEKEND = 1;
	public static int ELIMINATE_HOLIDAY = 1<<1;
	public static int ELIMINATE_EVENTDAY = 1<<2;
    private Set<Date> eventdays;
    private Set<Date> holidaydays;
    private int eliminateConditions;
    private List<String> filterMethods;
    
    public AkuaDaySelectorPredicate(int conditions,Set<Date> eventdays,Set<Date> holidaydays) {
    	this.eliminateConditions = conditions;
    	this.eventdays = eventdays;
    	this.holidaydays = holidaydays;
        filterMethods = new ArrayList<String>();
        
    	if((this.eliminateConditions&ELIMINATE_WEEKEND)!=0){
			filterMethods.add("isWeekEnd");
		}
		
		if((this.eliminateConditions&ELIMINATE_HOLIDAY)!=0){
			filterMethods.add("isHoliday");
		}
		
		if((this.eliminateConditions&ELIMINATE_EVENTDAY)!=0){
			filterMethods.add("isEventDay");
		}
    }
 
    public boolean evaluate(Object object) {
        boolean satisfies = false;
        if( object instanceof Date) {
        	Date cur = (Date) object;
        	if(isValidDay(cur)){
        		 satisfies = true;
        	}
        }
        return satisfies;
    }
    private boolean isValidDay(Date cur) {
    	
    	Iterator<String> item = filterMethods.iterator();
    	while(item.hasNext()){
    		String methodName = item.next();
    		try {
				Method curMethod = this.getClass().getMethod(methodName, Date.class);
				if((Boolean)curMethod.invoke(this, cur)){
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	return true;
    	
    }
    public boolean isWeekEnd(Date date){
    	Calendar calen = Calendar.getInstance();
    	calen.setTime(date);
    	
    	 if (calen.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
 				|| calen.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
    		 return true;
    	 }
    	return false;
    }
    
    public boolean isHoliday(Date cal){
    	
    	return holidaydays.contains(DateUtil.stripTime(cal));
    }

    public boolean isEventDay(Date cal){
		
		return eventdays.contains(DateUtil.stripTime(cal));
	}
	
	
	
	
}