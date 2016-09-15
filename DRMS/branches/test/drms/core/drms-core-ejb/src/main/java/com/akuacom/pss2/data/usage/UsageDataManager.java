package com.akuacom.pss2.data.usage;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.event.Event;
import com.akuacom.utils.lang.TimingUtil;

public interface UsageDataManager {
    @Remote
    public interface R extends UsageDataManager {}
    @Local
    public interface L extends UsageDataManager {}

    /** The Constant MA_START_TIME. */
    final long MA_START_TIME = 12 * TimingUtil.HOUR_MS;

    /** The Constant MA_END_TIME. */
    final long MA_END_TIME = 20 * TimingUtil.HOUR_MS;
    
    BaselineModel getBaselineModel();
    Set<Date> getHolidays();
    String[] getExcludedPrograms();
    List<BaselineConfig> getBaselineConfig(BaselineModel mb);
    double getMissingDataThreshold();
    
    List<PDataEntry> findRealTimeEntryListForParticipant(String participantName, String dataSetuuid, Date date, Boolean isIndividual, Boolean showRawData, Boolean isUiShowRawData);
    
    /**
     * 
     * @param eventName
     * @param isHistorical
     * @param isIndividual
     * @param showRawData
     * @return
     */
    List<PDataEntry> findRealTimeEntryListForEvent(String eventName, Boolean isHistorical, Boolean isIndividual, Boolean showRawData);
    
    /**
     * 
     * @param participantName
     * @param date
     * @param dataSetuuid
     * @param isIndividual
     * @param showRawData
     * @return
     */
    List<PDataEntry> findBaselineEntryListForParticipant(String participantName, Date date, String dataSetuuid, Boolean isIndividual, Boolean showRawData);

    List<PDataEntry> findBaselineEntryListForEvent(String eventName, String dataSetuuid, Boolean isHistorical, Boolean isIndividual, Boolean showRawData);
    
    UsageSummary getShedSumForEvent(String eventName, String partName, Date currentTime);
    UsageSummary calculateShed(
			List<PDataEntry> baseline, List<PDataEntry> usage, Date currentTime, Date startTime,
			Date endTime);
    
    Event getEventByDateAndParticipant(String partName, Date date); 
    
    DateRange getPickableDateRange(String participantname, boolean individual);
    
    Date getLastActualTimeByDate(String partname, Date start);
    
    /**
     * Determine the given participant is a individual participant or an aggregation 
     * @param participantName
     * @return
     */
    Boolean isIndividualparticipant(String participantName);
    
    List<String> findContributedParticipantNames(String participantName, Date date);
    
    List<String> findAllParticipantNames(String participantName, Date date);
    

    List<PDataEntry> findDefaultEntryListForParticipant(String participantName, String dataSetuuid, Date date);
    
    Event getEventByDateAndAggreagator(String partName, Date date); 
    
    List<String> findPrograms(String participantName, List<String> exculude);
    
	/**
	 * Find baseline from DB for a given date and participants
	 * @param participantNames
	 * @param date
	 * @return
	 */
	List<PDataEntry> findForecastUsageDataEntryList(String setUUId,List<String> participantNames,
            Date date);
	
	/**
	 * Find usage line from DB for a given date and participants
	 * @param participantNames
	 * @param date
	 * @return
	 */
	List<PDataEntry> findRealTimeUsageDataEntryList(String setUUId,List<String> participantNames,
            Date date);
	
}
