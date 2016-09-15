package com.akuacom.pss2.data.usage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.utils.DateUtil;

public class UsageUtil {
	public static DateRange generateDateRange(String strDate) {
		Date date = DateUtil.parse(strDate, "yyyy-MM-dd");
    	DateRange dateRange = new DateRange();
    	dateRange.setStartTime(DateUtil.stripTime(date));
    	dateRange.setEndTime(DateUtil.endOfDay(date));
		return dateRange;
	}

    public static List<String> getDRASDateRanges(List<Date> dates)
    {
        if(dates == null || dates.size() <= 0) return null;
        
        List<String> drs = new ArrayList<String>();

        for(Date date : dates)
        {
            drs.add(DateUtil.format(date, "yyyy-MM-dd"));
        }

        return drs;
    }
    
	public static DateRange generateDateRange(Date date) {
    	DateRange dateRange = new DateRange();
    	dateRange.setStartTime(DateUtil.stripTime(date));
    	dateRange.setEndTime(DateUtil.endOfDay(date));
		return dateRange;
	}
	
	public static long getCurrentTime(Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		long curTime = (cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000)
				+ (cal.get(Calendar.MINUTE) * 60 * 1000)
				+ (cal.get(Calendar.SECOND) * 1000);
		return curTime;
	}
	
    //******************************************
    // Returns the proportion of a 24 hour period of the largest gap in an otherwise
    // regularly-spaced list of data entries.  For example, if a dataset is at a 15
    // minute interval, but there are no records between noon and one pm, then that's
    // a gap of 1 hour, or 1/24th of a day, or .042
    // Gaps at the beginning or end of the 24 hour period count, with the exception
    // of any missing entries for the future part of the present day
    //******************************************    
	public static double getMaxGap (List<? extends PDataEntry> entriesForDay, PDataSet dataSet) {
		// requirement change:DRAS system shouldn't show telemetry data actual graph 
		// if the gap between any 2 consecutive data points exceeds max gap defined in DRAS system
		// So, we only consider the gap between 2 middle day points, don't calculate the beginning and the end gap any more.

		long sampleInterval = dataSet.getPeriod()*1000;
		long maxGap = 0;
        
        if(entriesForDay==null||entriesForDay.isEmpty()){
        	return 1;  // Nothing was collected at all
        }
//        PDataEntry first = UsageUtil.getFirstActualDot(entriesForDay);
//        PDataEntry last = UsageUtil.getLastActualDot(entriesForDay);
//        if(first==null||last==null){// No actual dot for the whole day
//        	return 1;
//        }
//        long beginOfDay = DateUtil.stripTime(first.getTime()).getTime() ;
//        long endOfDay = DateUtil.endOfDay(first.getTime()).getTime();
//        long nowTime = new Date().getTime();
//        if (nowTime < endOfDay) {
//            // end of day is right now for present unfinished day
//            endOfDay = nowTime;
//        }
//        
//        // Start comparing from beginning of day
        long previousTime = 0;       
        // Now loop through entries for the day looking for the largest gap
        for (PDataEntry dataEntry : entriesForDay) {       	
        	if (dataEntry.isActual()) {
        		if(previousTime==0){// the first node of list
        			previousTime = dataEntry.getTime().getTime();
        			continue;
        		}
        		
                long timeSinceLast = dataEntry.getTime().getTime() - previousTime;        		 
                 if (timeSinceLast > sampleInterval && timeSinceLast > maxGap) {
                     maxGap = timeSinceLast;
                 }        		
                previousTime = dataEntry.getTime().getTime(); 
            }
        }
//        // check any gap between last entry and end of day
//        long lastGap = endOfDay-previousTime;
//        if (lastGap > sampleInterval && lastGap > maxGap) {
//            maxGap = lastGap;
//        }
        
        // convert to fraction of a day for return
        return (double)maxGap /(24*60*60*1000.0);
    }
    
	//assume the list is ordinal
	public static boolean isExceedMaxGap (double ratio, List<? extends PDataEntry> list, PDataSet dataSet) {
        double maxGap = getMaxGap(list, dataSet);
        return (maxGap > ratio) ;
    }

	
	/**
	 * Return the end of the given day in a general way,
	 * 
	 * @param startTime
	 * @return
	 */
	public static boolean existsInList(Date time, List<PDataEntry> list){
		if(list==null||list.isEmpty()||time==null){
			return false;
		}
		
		for(PDataEntry de: list){
			if(de==null) continue;
			
			if((time.getTime())==(de.getTime().getTime())){
				return true;
			}
		}
		
		return false;
		
	}
	
   public static long getPoints(Date start, Date end, PDataSet dset) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(end);

        if (calendar.get(Calendar.HOUR_OF_DAY) == 0
                && calendar.get(Calendar.MINUTE) == 0
                && calendar.get(Calendar.SECOND) == 0) {
            calendar.add(Calendar.MINUTE, -15);
        }

        return (calendar.getTime().getTime() - start.getTime())
                / (DateUtil.MSEC_IN_SEC * dset.getPeriod());
    }
   
   public static PDataEntry getFirstActualDot(List<? extends PDataEntry> list){
	   if(null==list||list.isEmpty()) return null;
	   
	   for(int i=0; i< list.size(); i++){
		   
		   if(list.get(i).isActual()) return list.get(i);
	   }
	   
	   return null;
	   
   }
   
   public static PDataEntry getLastActualDot(List<? extends PDataEntry> list){
	   if(null==list||list.isEmpty()) return null;
	   
	   for(int i=list.size()-1; i>=0; i--){
		   
		   if(list.get(i).isActual()) return list.get(i);
	   }
	   
	   return null;
	   
   }
   
   public static boolean isAllFabricated(List<? extends PDataEntry> list) {
       if(list==null||list.size()==0){
       	return true;
       }

       for (PDataEntry ude : list) {
       	
       	if (ude.isActual()) {
              return false;
           }
       }
       return true;
   }
	

}
