package com.akuacom.pss2.data.usage.calcimpl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;


public interface DaysSelector {
    @Remote
    public interface R extends DaysSelector {}
    @Local
    public interface L extends DaysSelector {}
    public DateRange getBaseDateRange(List<String> datasourceUUIDs, Date date, BaselineConfig bc);
    
    public DateRange getBaseDateRange(List<PDataSource> dataSourceList, Date date, BaselineConfig bc, Set<Date> holidays, String[] excludedPrograms);
}