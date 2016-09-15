package com.akuacom.pss2.history;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

public interface HistoryDataManager {
	@Remote
	public interface R extends HistoryDataManager {
	}

	@Local
	public interface L extends HistoryDataManager {
	}
	
	 void createBaselineDataEntries(Set<PHistoryBaselineDataentry> dataEntryList);
	 void clear(String pdatasources,DateRange dateRange);
//	 void generateShedForEventParticipant(Date date);
	 void createBaseline(String pdatasources, Date date);
	 void timerServiceHandler();	
	 List<PDataEntry> generateBaseline(String participantName, Date startDate);
//	 void generateBaseline(List<String> participantNames, Date startDate, Date endDate);
//	 void interpolationTimerServiceHandler();
//	 void updateUsageInterpolation(PDataSource pdatasource);
	 void updateUsageInterpolation(PDataSource pdatasource, Date date);
     void interpolateMissingUsage(PDataSource usageSource, PDataSet usageSet, List<PDataEntry> usageEntries, List<PDataEntry> baselineEntries);
     
     void moveTodayUsageToHistory();
     List<PDataEntry> maAdjust(List<PDataEntry> temp, String participantName, Date date,String dataSetuuid, Boolean isIndividual, int start, int end);
     void onBaselineChange(String dataSourceId,Date date);
     void onUsageChange(String dataSourceId,Date date,Boolean refreshBaseline);
     void calculateShedForParticipant(PDataSource daSource, Date date);
     
     void baselineTimerServiceHandler();
     
   /**
    * Adjust baseline with the given time range. Also will store adjusted result.
    * when start == end, just store the given data with out any adjusting. 
    * @param participantName
    * @param date
    * @param start
    * @param end
    * @param eventName
    * @param baselineEntries
    */
     void onBaselineAdjust(String participantName, Date date, Integer start, Integer end, String eventName, List<PDataEntry> baselineEntries);
     
}

