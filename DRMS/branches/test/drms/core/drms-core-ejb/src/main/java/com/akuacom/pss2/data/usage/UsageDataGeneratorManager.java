package com.akuacom.pss2.data.usage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;


public interface UsageDataGeneratorManager {
    @Remote
    public interface R extends UsageDataGeneratorManager {}
    @Local
    public interface L extends UsageDataGeneratorManager {}

    void generateData() throws Exception;

    void generateData(List<String> partNames, List<String> dataSourceNames,
            Date startTime, Date endTime) throws Exception;

    List<PDataEntry> generateDataForPart(Date startTime, Date endTime,
            String participantName, String dataSourceName) throws Exception;
    
	List<PDataEntry> generateDataForParticpant(Map<String, List<PDataEntry>> map, PDataSource datasource, PDataSet dataSet);
}
