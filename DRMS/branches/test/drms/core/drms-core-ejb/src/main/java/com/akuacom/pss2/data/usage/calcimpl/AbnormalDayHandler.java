package com.akuacom.pss2.data.usage.calcimpl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.DailyUsage;


public interface AbnormalDayHandler {
    @Remote
    public interface R extends AbnormalDayHandler {}
    @Local
    public interface L extends AbnormalDayHandler {}

    List<DailyUsage> filterByDay(List<PDataEntry> deList, DateRange dr);

    
    List<DailyUsage> filterByDay(List<PDataEntry> deList, DateRange dr, double missingDataThreshold, PDataSet dataSet);
}