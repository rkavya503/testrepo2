package com.akuacom.pss2.data.usage.calcimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.DailyUsage;
import com.akuacom.pss2.data.usage.UsageDataEntry;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.lang.DateUtil;

@Stateless
public class DefaultAbnormalDayHandlerBean implements AbnormalDayHandler.R,
        AbnormalDayHandler.L {

    @EJB
    SystemManager.L sysManager;
    @EJB(beanName="DataManagerBean") 
    DataManager.L dataManager;

    
    public List<DailyUsage> filterByDay(List<PDataEntry> deList, DateRange dr) {
    	double missingDataThreshold = sysManager.getPss2Properties().getMissingDataThreshold();
    	PDataSet dataSet = dataManager.getDataSetByName("Usage");
    	return filterByDay(deList, dr, missingDataThreshold, dataSet);
    	
    }
    
    public List<DailyUsage> filterByDay(List<PDataEntry> deList, DateRange dr, double missingDataThreshold, PDataSet dataSet) {
        if (deList == null)
            return null;
        
        //final PDataSet dataSet = dataManager.getDataSetByName("Usage");

        Map<Date, DailyUsage> logMap = new HashMap<Date, DailyUsage>();
        for (PDataEntry de : deList) {
            if (dr.getExcludedDays().contains(DateUtil.stripTime(de.getTime()))) {
                continue;
            }
            Date date = DateUtil.stripTime(de.getTime());
            DailyUsage dl;
            if (logMap.get(date) == null) {
            	dl = new DailyUsage(dataSet.getPeriod()*1000);
                logMap.put(date, dl);
            } else {
                dl = logMap.get(date);
            }
            UsageDataEntry ul = new UsageDataEntry(de);
            ul.setDailyLogs(dl);
            dl.getUsages().add(ul);
        }

        List<DailyUsage> dailyUsageList = new ArrayList<DailyUsage>();

        for (Date dateKey : logMap.keySet()) {
            DailyUsage dl = logMap.get(dateKey);
            if (filterMissingData(dl, missingDataThreshold)) {
                continue;
            }
            dl.setDate(dateKey);
           //don't need mock up here
           // dailyUsageList.add(padMockData(dl));
            dailyUsageList.add(dl);
        }

        return dailyUsageList;
    }

    
    private boolean filterMissingData(DailyUsage du, double missingDataThreshold) {
        List<UsageDataEntry> list = du.getUsages();
        PDataSet dataSet = dataManager.getDataSetByName("Usage"); 
        return UsageUtil.isExceedMaxGap(missingDataThreshold, list, dataSet);
        
    }
  
}