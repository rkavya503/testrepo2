package com.akuacom.pss2.data.usage.calcimpl;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.BaselineConfig;

public interface BaselineCalculator {
    @Remote
    public interface R extends BaselineCalculator {}
    @Local
    public interface L extends BaselineCalculator {}

    /**
     * @param baselist
     * @param bc
     * @param date
     * @param dr
     * @return
     */
    List<PDataEntry> calculate(List<PDataEntry> baselist, BaselineConfig bc, Date date, DateRange dr);
    
    List<PDataEntry> calculate(List<PDataEntry> baselist, BaselineConfig bc, Date date, DateRange dr, double missingDataThreshold, PDataSet dataSet);
}