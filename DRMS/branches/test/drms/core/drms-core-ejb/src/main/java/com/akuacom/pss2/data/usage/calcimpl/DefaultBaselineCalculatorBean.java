package com.akuacom.pss2.data.usage.calcimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataEntryEAO;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaseLine;
import com.akuacom.pss2.data.usage.BaseLineType;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.DailyUsage;
import com.akuacom.pss2.data.usage.UsageDataEntry;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.utils.lang.DateUtil;

@Stateless
public class DefaultBaselineCalculatorBean implements BaselineCalculator.R,
        BaselineCalculator.L {
	
    @EJB
	BaseLineMA baseLineMA;
	 	 
	@EJB protected PDataEntryEAO.L dataEntryEAO;

	
    public List<PDataEntry> calculate(List<PDataEntry> deList,
            BaselineConfig bc, Date date, DateRange dr) {
    	// don't calculate base line in DST switch day
		if(isDSTSwitchDay(date)){
			return new ArrayList<PDataEntry>();
		}
        List<DailyUsage> dailyUsageList = ImplFactory.instance()
                .getAbnormalDayHandler(bc.getExcludeAbnormalDayImplClass())
                .filterByDay(deList, dr);
        return calculate(dailyUsageList, deList, bc, date, dr);
    	
    }

	

    public List<PDataEntry> calculate(List<PDataEntry> deList,
            BaselineConfig bc, Date date, DateRange dr, double missingDataThreshold, PDataSet dataSet) {
    	// don't calculate base line in DST switch day
		if(isDSTSwitchDay(date)){
			return new ArrayList<PDataEntry>();
		}
    	List<DailyUsage> dailyUsageList = ImplFactory.instance()
                .getAbnormalDayHandler(bc.getExcludeAbnormalDayImplClass())
                .filterByDay(deList, dr, missingDataThreshold, dataSet);
    	 return calculate(dailyUsageList, deList, bc, date, dr);
    }
	
	
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.data.usage.calcimpl.BaselineCalculator#calculate(java
     * .util.List, com.akuacom.pss2.data.usage.BaselineConfig, java.util.Date,
     * com.akuacom.pss2.data.DateRange)
     */
    public List<PDataEntry> calculate(List<DailyUsage> dailyUsageList, List<PDataEntry> deList,
            BaselineConfig bc, Date date, DateRange dr) {
    	// don't calculate base line in DST switch day
		if(isDSTSwitchDay(date)){
			return new ArrayList<PDataEntry>();
		}
        // BaseLineType blType = BaseLineType.THREE_TEN;
        BaseLineType blType = BaseLineType.fromNiceName(bc.getBaselineModel()
                .getName());
        if (blType == BaseLineType.UNKNOWN_UNKNOWN) {
            blType = BaseLineType.THREE_TEN;
        }
        
        //calculate the avg value ,and make it order by the avg value
        Map<Double, DailyUsage> avgs = new HashMap<Double, DailyUsage>();
        List<Double> highestUsage = new LinkedList<Double>();
        
        DailyUsage curr = null;
        for (Iterator<DailyUsage> i = dailyUsageList.iterator(); i
                .hasNext();) {
            curr = i.next();
            double avg = curr.getAvgUsage(bc.getEventStartTime(),bc.getEventEndTime());
            avgs.put(avg, curr);
            highestUsage.add(avg);
        }
        Collections.sort(highestUsage);
        //get the highest items.
        if (highestUsage.size() > blType.sampleCount()) {
        	highestUsage = highestUsage.subList((highestUsage.size()-blType.sampleCount()), highestUsage.size());
        }
        
        dailyUsageList = new ArrayList<DailyUsage>();
        for (Iterator<Double> i = highestUsage.iterator(); i
        .hasNext();) {
        	dailyUsageList.add(avgs.get(i.next()));
        }

        List<DailyUsage> dailyUsageTempList = new ArrayList<DailyUsage>();
        for(DailyUsage du : dailyUsageList){
        	if(du!=null){
        		long usageSlot_msec = du.getUsageSlot_msec();
            	
            	if(du.getUsages().size()==(DateUtil.MSEC_IN_DAY/usageSlot_msec)){
            		dailyUsageTempList.add(du);
            	}
        	}
        }
        
        if(dailyUsageTempList.isEmpty()){
        	 for(DailyUsage du : dailyUsageList){
        		 du = padMockData(du);
        		 if(du!=null){
        			 dailyUsageTempList.add(du);
        		 }
             }
        }
        
       if(dailyUsageTempList.isEmpty()){
    	   return new ArrayList<PDataEntry>();
       }
        BaseLine bline = averageLog(blType, dailyUsageTempList);
        List<PDataEntry> ret = new ArrayList<PDataEntry>();
        if (bline != null && bline.getUsages() != null) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(date);

            for (int i = 0; i < bline.getUsages().size(); i++) {
                PDataEntry de = bline.getUsages().get(i);

                Calendar baseTime = Calendar.getInstance();
                baseTime.setTime(de.getTime());

                Calendar entryCal = Calendar.getInstance();
                entryCal.setTime(selectedDate.getTime());
                entryCal.set(Calendar.HOUR_OF_DAY,
                        baseTime.get(Calendar.HOUR_OF_DAY));
                entryCal.set(Calendar.MINUTE, baseTime.get(Calendar.MINUTE));
                entryCal.set(Calendar.SECOND, baseTime.get(Calendar.SECOND));
                entryCal.set(Calendar.MILLISECOND,
                        baseTime.get(Calendar.MILLISECOND));

                de.setTime(entryCal.getTime());
                de.setActual(false);
                ret.add(de);
            }
        }
        
        //if(withoutMAAdjust(ret, bc, date)) 
        return ret;
		
//		PDataEntry entry = deList.get(0);
//		
//		List<PDataEntry> usageList = null;
//		try {
//			usageList = dataEntryEAO.getDataEntryList(entry.getDataSet().getUUID(), entry.getDatasource().getUUID(), UsageUtil.generateDateRange(date));
//		} catch (EntityNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		ret = baseLineMA.adjust(bc, ret, usageList, date);
//		
//        return ret;

    }
    
    private DailyUsage padMockData(DailyUsage dl ){
    	if(dl==null) return null;
    	
    	long usageSlot_msec = dl.getUsageSlot_msec();
    	
    	if(dl.getUsages().size()==(DateUtil.MSEC_IN_DAY/usageSlot_msec)){
    		return dl;
    	}
    	
    	Date date = DateUtil.stripTime(dl.getUsages().get(0).getTime());
        List <UsageDataEntry> usages = new ArrayList<UsageDataEntry>();
        long max = DateUtil.MSEC_IN_DAY + date.getTime();
        PDataSet dataSet = null;
        PDataSource dataSource = null;
        if(null!=dl.getUsages()&&dl.getUsages().size()>0) {
        	UsageDataEntry actualEntry = dl.getUsages().iterator().next();
        	dataSet = actualEntry.getDataSet();
        	dataSource = actualEntry.getDatasource();
        }
        
        if(dataSet==null||dataSource==null) return null;// don't mock up any fabricated data in case of can't obtain actual dataset and datasource
        	
        
        for (long i = date.getTime(); i < max; i += usageSlot_msec) {
        	UsageDataEntry temp = new UsageDataEntry(i, null, null);
        	temp.setDataSet(dataSet);
        	temp.setDatasource(dataSource);
            usages.add(temp);
        }
        List <UsageDataEntry> usageSrc= dl.getUsages();
        TreeMap<Long, UsageDataEntry> usageSrcMap = new TreeMap<Long, UsageDataEntry>();
        for(UsageDataEntry dataEntrySrc: usageSrc){
        	usageSrcMap.put(dataEntrySrc.getTime().getTime(), dataEntrySrc);
        }
        
        for(UsageDataEntry ul: usages){
        	if(usageSrcMap.containsKey(ul.getTime().getTime())){
        		UsageDataEntry dataEntrySrc = usageSrcMap.get(ul.getTime().getTime());
        		ul.addUsage(dataEntrySrc.getValue());
                ul.setTime(dataEntrySrc.getTime());
                ul.setValue(dataEntrySrc.getValue());
                
                continue;
        	}else{
        		long pre = ul.getTime().getTime()-usageSlot_msec;
         		long next = ul.getTime().getTime()+usageSlot_msec;
         		UsageDataEntry dataEntrySrc = getRelace(usageSrcMap, pre, next, usageSlot_msec);
         		if(dataEntrySrc==null){
         			return null;
         		}
         		ul.addUsage(dataEntrySrc.getValue());
                ul.setTime(ul.getTime());
                ul.setValue(dataEntrySrc.getValue());
        		
        	}
        }
        dl.setUsages(usages);
    	return dl;
    }
    
    private UsageDataEntry getRelace(TreeMap<Long, UsageDataEntry> usageSrcMap, long pre, long next, long usageSlot_msec){
    	if((pre<usageSrcMap.firstKey())&&(next>usageSrcMap.lastKey())){
    		return null;
    	}
   		if(usageSrcMap.containsKey(pre)){
 			return usageSrcMap.get(pre);
 		}else if(usageSrcMap.containsKey(next)){
 			return usageSrcMap.get(next);
 		}else{
 			return getRelace(usageSrcMap, pre-usageSlot_msec, next+usageSlot_msec, usageSlot_msec);
 		}
    }


    private boolean withoutMAAdjust(List<PDataEntry> ret, BaselineConfig bc, Date date) {
//		Date current = new Date();
//		Date maStartTime = DateUtil.stripTime(new Date());
//		
//		maStartTime = DateUtils.addMilliseconds(DateUtil.stripTime(current), bc.getMaStartTime());
//		
//		if(DateUtils.isSameDay(date, new Date())&&maStartTime.after(current)){
//			return true;
//		}
//		
//		if(ret.isEmpty()){
//			return true;
//		}
		
		return true;//don't calculate ma,  at insert time
	}


	private boolean isDSTSwitchDay(Date date) {
		Calendar zeroOclock = generateTime(date, 0);
		Calendar twoOclock = generateTime(date, 2);
		
		return (zeroOclock.get(Calendar.DST_OFFSET)!=twoOclock.get(Calendar.DST_OFFSET));
	}


	private Calendar generateTime(Date date, int hour) {
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		time.set(Calendar.HOUR_OF_DAY, hour);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		
		return time;
	}

    
    protected boolean addToHighestList(List<Double> highestUsage, double value) {
        if (!highestUsage.isEmpty()) {
            if (highestUsage.get(highestUsage.size() - 1) < value) {
                highestUsage.add(value);
                Collections.sort(highestUsage);
                return true;
            }
        } else {
            highestUsage.add(value);
            return true;
        }
        return false;
    }

    protected BaseLine averageLog(BaseLineType type,
            Collection<DailyUsage> usageList) {
    	// Modified by Frank for DRMS-2561
        if (usageList == null||usageList.size()<=0) {
            return null;
        }

        BaseLine bl = null;

        for (DailyUsage aUsageList : usageList) {
            if (bl == null) {
                bl = new BaseLine(type, aUsageList.getDate(),
                        aUsageList.getUsageSlot_msec(), true);
            }
            bl.plusUsageLog(aUsageList);
        }

        bl.multUsageLog(1.0 / usageList.size());

        return bl;
    }
}