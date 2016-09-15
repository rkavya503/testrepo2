package com.akuacom.pss2.data.usage.calcimpl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.BaselineModel;


public interface BaselineMod {
    @Remote
    public interface R extends BaselineFiveTenModel {}
    @Local
    public interface L extends BaselineFiveTenModel {}

    List<PDataEntry> calculate(List<String> datasourceUUIDs, Date date);
    
    List<PDataEntry> calculate(List<PDataSource> dataSourceList, Date date, Set<Date> holidays, String[] excludedPrograms, BaselineModel mb, List<BaselineConfig> bcs, double missingDataThreshold, PDataSet dataSet);
}