package com.akuacom.pss2.data.usage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.data.DataManagerBean;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.calcimpl.DateEntrySelectPredicate;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.history.HistoryEventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.Event;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.utils.lang.DateUtil;

@Stateless
public class UsageDataManagerBean extends DataManagerBean implements
        UsageDataManager.R, UsageDataManager.L {

    private static final Logger log = Logger
            .getLogger(UsageDataManagerBean.class);
    public static final String USAGE_LINE = "usageLine";
    public static final String BASE_LINE = "baseLine";

    @EJB
    protected ReportManager.L report;
    @EJB
    protected EventManager.L eventManager;
    @EJB
    protected SystemManager.L sysManager;
    @EJB
    BaselineModelManager.L bmManager;
    @EJB
    EventParticipantManager.L epManager;
    @EJB
    BaselineConfigManager.L bcManager;
    
	@Override
	public List<PDataEntry> findRealTimeEntryListForParticipant(
			String participantName, String dataSetuuid, Date date,
			Boolean isIndividual, Boolean showRawData, Boolean isUiShowRawData) {
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = findRealTimeEntryListForParticipantSql(params,participantName,
				dataSetuuid, date, isIndividual, showRawData, isUiShowRawData);
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL =SQLBuilder.buildNamedParameterSQL(sql,params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (report==null?new ArrayList<PDataEntry>():report);
	}

	private String  findRealTimeEntryListForParticipantSql(Map<String,Object> params,
			String participantName, String dataSetuuid, Date date,
			Boolean isIndividual, Boolean showRawData, Boolean isUiShowRawData) {
		if(showRawData){
			//return the actual usage point when show raw data checkbox is selected
			if(isUiShowRawData){
			  return findRawRealTimeEntryListForParticipantForUI(params,participantName, date);
			} else {
			 return findRawRealTimeEntryListForParticipant(params,participantName, date);
			}
		}
		if(isIndividual==null){
			// determine if it is an individual participant or aggregation
			isIndividual = isIndividualparticipant(participantName);
		}
		if(isIndividual){
			return findRealTimeEntryListForIndiviParticipant(params,participantName, date, dataSetuuid);
		}
		
		return findRealTimeEntryListForParticipant(params,participantName, date, dataSetuuid);
	}


	@Override
	public List<PDataEntry> findRealTimeEntryListForEvent(String eventName,
			Boolean isHistorical, Boolean isIndividual, Boolean showRawData) {
		Event event = getEvent(eventName, null);
		if(isHistorical==null){
		// determine if it is a active event or a historical event
			if(event==null) return new ArrayList<PDataEntry>();// event not exist
			isHistorical = event.getHistoricalEvent();
		}
		if(isIndividual==null){
			// determine if it is a individual participant event or a multi participant event
			if(event.getEventParticipants()!=null&&event.getEventParticipants().size()==1){
				isIndividual = true;
			}else{
				isIndividual = false;
			}
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		String sql = findRealTimeEntryListForEventSql(params,eventName, isHistorical,
				isIndividual, showRawData, event.getStartTime());
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		
		return (report==null?new ArrayList<PDataEntry>():report);
	}

	private String findRealTimeEntryListForEventSql(Map<String,Object> params,String eventName,
			Boolean isHistorical, Boolean isIndividual, Boolean showRawData, Date startTime) {
		if(showRawData){
			if(isHistorical){
				return findRawRealTimeEntryListForHistoricalEvent(params,eventName, startTime);
			}
			return findRawRealTimeEntryListForEvent(params,eventName, startTime);
		}
		if(isHistorical){
			return findRealTimeEntryListForHistoricalEvent(params,eventName, isIndividual, startTime);
		}
		return findRealTimeEntryListForEvent(params,eventName, isIndividual, startTime);
	}


	@Override
	public List<PDataEntry> findBaselineEntryListForParticipant(
			String participantName, Date date, String dataSetuuid, Boolean isIndividual,
			Boolean showRawData) {
		String sql = null;
//		if(showRawData){
//			sql = findRawBaselineEntryListForParticipant(participantName, date);
//		}
		Map<String,Object> params = new HashMap<String,Object>();
		
		sql = findBaselineEntryListForParticipant(params,participantName,date, dataSetuuid, isIndividual, showRawData);
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		if (report==null){
			return new ArrayList<PDataEntry>();
		}
		return report;
	}

	@Override
	public List<PDataEntry> findBaselineEntryListForEvent(String eventName, String dataSetuuid,
			Boolean isHistorical, Boolean isIndividual, Boolean showRawData) {
		showRawData = false;
		List<PDataEntry> report = null;
		Event event = getEvent(eventName, null);
		if(isHistorical == null){
			if(event==null) return new ArrayList<PDataEntry>();// event not exist
			isHistorical = event.getHistoricalEvent();
		}
//		if(showRawData){
//			report = findRawBaselineEntryListForEvent(eventName, isHistorical);
//		}else 
		if(isHistorical){// finished
			report = findBaselineEntryListForHistoricalEvent(eventName, event.getStartTime(), showRawData);
		}
		report = findBaselineEntryListForEvent(eventName, event.getStartTime(), showRawData);// active or pending
		
		if (report==null){
			return new ArrayList<PDataEntry>();
		}
		return report;
	}
    
    
    @Override
    public double getMissingDataThreshold(){
    	double m = 0;
		try {
			m = sysManager.getPropertyByName(PSS2Properties.PropertyName.BASELINE_MISSINGDATA_THRESHOLD.toString()).getDoubleValue();
		} catch (EntityNotFoundException ignore) {}
    	
    	return m;
    }
    
    
    @Override
    public String[] getExcludedPrograms(){
		String exludePrograms = null;
		try {
			exludePrograms = sysManager.getPropertyByName(PSS2Properties.PropertyName.EXCLUDED_PROGRAMS_FOR_BASELINE.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}

		String[] programs = null;
		if (!(exludePrograms == null || exludePrograms.trim().equals(""))) {
			programs = exludePrograms.split(",");
		}
		
		return programs;
    }
    @Override
    public Set<Date> getHolidays() {
		Set<Date> holidaydays = new HashSet<Date>();
		String holiday = null;
		try {
			holiday = sysManager.getPropertyByName(PSS2Properties.PropertyName.HOLIDAYS.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}
		if(holiday!=null&&holiday.trim().length()>0){
			String[] holidayArray = holiday.split(",");
			
			for(String day: holidayArray){
				holidaydays.add(DateUtil.stripTime(com.akuacom.utils.DateUtil.parse(day,"MM/dd/yyyy")));
			}
		}
		
		return holidaydays;
	}    
    @Override
    public BaselineModel getBaselineModel(){
    	
		String baselineModel = null;
		try {
			baselineModel = sysManager.getPropertyByName(PSS2Properties.PropertyName.BASELINE_MODEL.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}
        BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
        return mb;

    }
    
    @Override
    public List<BaselineConfig> getBaselineConfig(BaselineModel mb){
    	return bcManager
                .getBaselineConfigByBaselineModel(mb);
    }
    
	@Override
	public UsageSummary getShedSumForEvent(String eventName,
			String partName, Date currentTime) {
		PDataSet usageset = dataSetEAO.getDataSetByName("usage");
		PDataSet baselineset = dataSetEAO.getDataSetByName("Baseline");
		Event event = getEvent(eventName, partName);
		List<PDataEntry> baseline = null;
		List<PDataEntry> usage = null;
		//
		boolean individual = false;
		List<String> participants = event.getEventParticipants();
		if(participants!=null&&participants.size()==1){
			individual = true;
		}
		if(partName==null){
			baseline = findBaselineEntryListForEvent(event.getEventName(), baselineset.getUUID(), event.getHistoricalEvent(),individual, false);
			usage = findRealTimeEntryListForEvent(event.getEventName(), event.getHistoricalEvent(), individual, false);
		}else{
			usage = this.findRealTimeEntryListForParticipant(partName, usageset.getUUID(), event.getStartTime(), true, false, false);
			baseline = this.findBaselineEntryListForParticipant(partName, event.getStartTime(),  baselineset.getUUID(), true, false);
		}
		
		
		Date startTime = event.getStartTime();
		Date endTime = event.getEndTime();
		
		return calculateShed(baseline, usage, currentTime, startTime, endTime);
	}
	
	@Override
	public UsageSummary calculateShed(
			List<PDataEntry> baseline, List<PDataEntry> usage, Date currentTime, Date startTime,
			Date endTime) {
		if(startTime.after(currentTime)){
			return new UsageSummary();// not start yet
		}
		
		endTime = endTime.after(currentTime)?currentTime:endTime;
		
		UsageSummary usageSum = getUsageSummaryFromList4Event(usage, startTime, endTime);
		UsageSummary baselineSum = getUsageSummaryFromList4Event(baseline, startTime, endTime);
		UsageSummary sheSum = new UsageSummary();
		if(usageSum.getAverage()>0&&baselineSum.getAverage()>0){
			sheSum.setAverage(convertNumber(baselineSum.getAverage())-convertNumber(usageSum.getAverage()));
			sheSum.setTotal(convertNumber(baselineSum.getTotal())-convertNumber(usageSum.getTotal()));
		}
		return sheSum;
	}
	private static Double convertNumber(Double in){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(2);
		
		return getDoubleValue(in, nf);
	}
	private static Double getDoubleValue(Double in, NumberFormat nf){
	   	
	   	if(Double.isNaN(in)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(in));
	}
	@Override
	public Date getLastActualTimeByDate(String partname, Date start) {
		List<String> participantNames = new ArrayList<String>();
		participantNames.add(partname);
		DateRange range = UsageUtil.generateDateRange(start);
		if(DateUtils.isSameDay(start, new Date())){
			List<CurrentUsageDataEntry> entries = currentUsageEao.findLastActualTimeByDate(getDataSourceIds(participantNames),range.getStartTime(), range.getEndTime());
			if(entries!=null&&!entries.isEmpty()){
				Object obj = entries.get(0);
				
				return (Date)obj;
			}
			return null;
		}
		
		return dataEntryEAO.getLastActualByDate(getDataSourceIds(participantNames), range);
	}

    /** The Constant FOUR_HOURS_IN_MS. */

    private Event getEvent(String eventName, String partName) {
    	Event event = null;
    	HistoryEvent hisEvent = null;
    	try {
			hisEvent = eventManager.getByEventName(eventName);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			log.debug("Event "+eventName+" not found"+e.getMessage());
		}
        List<String> participantNames = new ArrayList<String>();
        if (partName != null && !partName.isEmpty()) {
            participantNames.add(partName);
        }
        if (hisEvent != null) {
        	event = new Event();
            if (partName == null || partName.isEmpty()) {
            	List<HistoryEventParticipant> eps = heParticipantEAO.findParticipantByEventName(eventName);
		        for (HistoryEventParticipant ep : eps) {
		        	participantNames.add(ep.getParticipantName());
		        }
            }
            event.setEventParticipants(participantNames);
            event.setHistoricalEvent(true);
            event.setEndTime(hisEvent.getEndTime());
            event.setEventName(hisEvent.getEventName());
            event.setProgramName(hisEvent.getProgramName());
            event.setStartTime(hisEvent.getStartTime());
            
        } else {
            // check for active event
            com.akuacom.pss2.event.Event ev = eventManager.getEventPerf(eventName);
            
            if (ev != null) {
                if (partName == null || partName.isEmpty()) {
                	participantNames.addAll(epManager.getParticipantsForEvent(eventName, false));
                }
                event = new Event();
                event.setEndTime(ev.getEndTime());
                event.setEventName(ev.getEventName());
                event.setProgramName(ev.getProgramName());
                event.setStartTime(ev.getStartTime());
                event.setEventParticipants(participantNames);
                event.setHistoricalEvent(false);
            }
        }
        return event;
    }

	
	private List<String> getDataSourceIds(List<String> participantNames)
	{
		List<String> dsUUIDs = new ArrayList<String>();
		   	
		Map<String, String> dsMap = this.dataSourceEAO.getDataSourcesByPartNames(participantNames);
		if(dsMap != null && dsMap.size() > 0)
		{
			for(String key : dsMap.keySet())
			{
				dsUUIDs.add(key);
			}
		}
		return dsUUIDs;
	}
	
	private String findRealTimeEntryListForEvent(Map<String,Object> params,String eventName, 
			boolean isIndividual, Date startTime) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
		if(DateUtils.isSameDay(startTime, new Date())||new Date().before(startTime)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																									\n");
		if(isIndividual){
			sql.append("	 	(SELECT * FROM datasource_usage WHERE maxgap <${para_maxGap}) du,												\n");
		}else{
			sql.append("	 	(SELECT * FROM datasource_usage WHERE baseline_state=1 and maxgap <${para_maxGap}) du,							\n");
		}
		
		sql.append("	 	participant p ,event_participant ep, event e																		\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.event_uuid = e.uuid																							\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("	 	AND (ep.optOutTime is null or ep.optOutTime>e.startTime)															\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	AND YEAR(du.DATE)= YEAR(e.startTime)																				\n");
		sql.append("	 	AND MONTH(du.DATE)= MONTH(e.startTime)																				\n");
		sql.append("	 	AND DAY(du.DATE)= DAY(e.startTime)																					\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		return sql.toString();
	}

	private String findRealTimeEntryListForParticipant(Map<String,Object> params,
			String participantName, Date date, String dataSetuuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(ROUND(entry.value,2)) value, MIN(entry.actual) actual 														\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																			\n");
    	sql.append(" (SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du,				\n");
		sql.append("	 	(																													\n");
		sql.append("	 		SELECT DISTINCT p.participantName FROM participant p INNER JOIN 															\n");
		sql.append("	 		(	SELECT pp3.participant_uuid FROM program_participant pp3,													\n");
		sql.append("	 			(	SELECT pp.uuid FROM program_participant pp WHERE pp.participant_uuid = 									\n");
		sql.append("	 				(SELECT UUID FROM participant WHERE participantName = ${para_participantname})							\n");
		sql.append("	 			) AS pp2																									\n");
		sql.append("	 			WHERE LOCATE(pp2.uuid, pp3.ancestry) > 0																	\n");
		sql.append("			) AS pp4 																										\n");
		sql.append("	 		ON p.uuid = pp4.participant_uuid																				\n");
		sql.append("	 		UNION ALL 																										\n");
		sql.append("	 		SELECT ${para_participantname} 																					\n");
		sql.append("		) AS participant  																									\n");
		sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
		sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND source.ownerid = participant.participantName  																	\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_datasetuuid", dataSetuuid);
		DateRange dr = initDataRange(date);
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_participantname", participantName);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		return sql.toString();
	}
	
	
	private List<PDataEntry> findBaselineEntryListForEvent(
			String eventName, Date startTime, Boolean showRawData) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();		
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
        sql.append("    FROM baseline_dataentry_temp AS entry                                                                                   \n");
        sql.append("    , datasource source,                                                                                                    \n");
		sql.append("	 	(SELECT * FROM datasource_usage WHERE baseline_state=1 and maxgap <${para_maxGap}) du,															\n");
		sql.append("	 	participant p ,event_participant ep, event e																		\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.event_uuid = e.uuid																							\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("	 	AND (ep.optOutTime is null or ep.optOutTime>e.startTime)															\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	AND YEAR(du.DATE)= YEAR(e.startTime)																				\n");
		sql.append("	 	AND MONTH(du.DATE)= MONTH(e.startTime)																				\n");
		sql.append("	 	AND DAY(du.DATE)= DAY(e.startTime)																					\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND entry.actual=1 																							        \n");
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql.toString(), params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		
		return (report==null?new ArrayList<PDataEntry>():report);
	}
	
	private String findBaselineEntryListForParticipant(Map<String,Object> params,
			String participantName, Date date, String dataSetuuid, Boolean isIndividual, Boolean showRawData) {
		StringBuffer sql = new StringBuffer();
		if(isIndividual){
			sql.append("	SELECT	 																												\n");
			sql.append("	entry.time time, ROUND(entry.value,2) value, entry.actual actual 																\n");
			
			if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
				sql.append("	FROM baseline_dataentry_temp AS entry				 																\n");
			}else{
				sql.append("	FROM history_baseline_dataentry AS entry				 															\n");
			}
			sql.append("	, datasource source,  																									\n");
			sql.append("	 	 (SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1) du		\n");
			sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
			sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
			sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
			sql.append("	 	AND source.ownerid = ${para_participantname}   																		\n");
			sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
			if(showRawData){
				sql.append("	 	AND entry.actual=0																							\n");
			}else{
				sql.append("	 	AND entry.actual=1																							\n");
			}
			sql.append("	 	GROUP BY entry.time																									\n");
			
		}else{//get baseline for aggregation
			sql.append("	SELECT	 																												\n");
			sql.append("	entry.time time, SUM(ROUND(entry.value,2)) value, MIN(entry.actual) actual 														\n");
			if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
				sql.append("	FROM baseline_dataentry_temp AS entry				 																\n");
			}else{
				sql.append("	FROM history_baseline_dataentry AS entry				 															\n");
			}
			
			sql.append("	, datasource source,  															\n");
			sql.append("	 	 (SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du,		\n");
			sql.append("	 	(																													\n");
			sql.append("	 		SELECT DISTINCT p.participantName FROM participant p INNER JOIN 															\n");
			sql.append("	 		(	SELECT pp3.participant_uuid FROM program_participant pp3,													\n");
			sql.append("	 			(	SELECT pp.uuid FROM program_participant pp WHERE pp.participant_uuid = 									\n");
			sql.append("	 				(SELECT UUID FROM participant WHERE participantName = ${para_participantname})							\n");
			sql.append("	 			) AS pp2																									\n");
			sql.append("	 			WHERE LOCATE(pp2.uuid, pp3.ancestry) > 0																	\n");
			sql.append("			) AS pp4 																										\n");
			sql.append("	 		ON p.uuid = pp4.participant_uuid																				\n");
			sql.append("	 		UNION ALL 																										\n");
			sql.append("	 		SELECT ${para_participantname} 																					\n");
			sql.append("		) AS participant  																									\n");
			sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
			sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
			if(showRawData){
				sql.append("	 	AND entry.actual=0																							\n");
			}else{
				sql.append("	 	AND entry.actual=1																							\n");
			}
			sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
			sql.append("	 	AND source.ownerid = participant.participantName  																	\n");
			sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
			sql.append("	 	GROUP BY entry.time																									\n");
			
		}
		
		DateRange dr = initDataRange(date);
		params.put("para_datasetuuid", dataSetuuid);
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		if(!isIndividual){
			params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		}
		params.put("para_participantname", participantName);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		
		return sql.toString();
	}

	private String findRealTimeEntryListForHistoricalEvent(Map<String,Object> params,
			String eventName, boolean isIndividual, Date startTime) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
		if(DateUtils.isSameDay(startTime, new Date())||new Date().before(startTime)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																									\n");
		if(isIndividual){
			sql.append("	 	(SELECT * FROM datasource_usage WHERE maxgap <${para_maxGap}) du,												\n");
		}else{
			sql.append("	 	(SELECT * FROM datasource_usage WHERE baseline_state=1 and maxgap <${para_maxGap}) du,							\n");
		}
		
		sql.append("	 	participant p ,history_event_participant ep, history_event e														\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.history_event_uuid = e.uuid																					\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	AND YEAR(du.DATE)= YEAR(e.startTime)																				\n");
		sql.append("	 	AND MONTH(du.DATE)= MONTH(e.startTime)																				\n");
		sql.append("	 	AND DAY(du.DATE)= DAY(e.startTime)																					\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		return sql.toString();
	}

	private List<PDataEntry> findBaselineEntryListForHistoricalEvent(
			String eventName, Date startTime, Boolean showRawData) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();		
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
		
		
        if(DateUtils.isSameDay(startTime, new Date())||new Date().before(startTime)){
                sql.append("    FROM baseline_dataentry_temp AS entry                                                                                                                                           \n");
        }else{
                sql.append("    FROM history_baseline_dataentry AS entry                                                                                                                                                        \n");
        }
        sql.append("    , datasource source,                                                                                                                                                                                                    \n");
		sql.append("	 	(SELECT * FROM datasource_usage WHERE baseline_state=1 and maxgap <${para_maxGap}) du,								\n");
		sql.append("	 	participant p ,history_event_participant ep, history_event e														\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.history_event_uuid = e.uuid																					\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	AND YEAR(du.DATE)= YEAR(e.startTime)																				\n");
		sql.append("	 	AND MONTH(du.DATE)= MONTH(e.startTime)																				\n");
		sql.append("	 	AND DAY(du.DATE)= DAY(e.startTime)																					\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND entry.actual=1 																							\n");
		
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		List<PDataEntry>report = null;
		
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql.toString(), params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e) {
			log.error(e);
			throw new EJBException(e);
		}
		return (report==null?new ArrayList<PDataEntry>():report);
	}

	@Override
	public Boolean isIndividualparticipant(String participantName) {
		StringBuffer sql = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();		
		sql.append("	SELECT count(pp3.participant_uuid) FROM program_participant pp3,			 				\n");
		sql.append("	(	SELECT pp.uuid FROM program_participant pp WHERE pp.participant_uuid = 		 			\n");
		sql.append("	(SELECT UUID FROM participant WHERE participantName = ${para_participantname})		 		\n");
		sql.append("	) AS pp2	 																				\n");
		sql.append("	WHERE LOCATE(pp2.uuid, pp3.ancestry) > 0	 												\n");
		
		params.put("para_participantname", participantName);
		long count = 0;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql.toString(), params);
			count = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new CellConverter<Long>(Long.class));
		} catch (Exception e) {
			log.error(e);
			throw new EJBException(e);
		}		
		return (count==0);
	}

	private String findRawRealTimeEntryListForParticipant(Map<String,Object> params,
			String participantName, Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, ROUND(entry.value,2) value, entry.actual actual							 										\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source				 																						\n");
		sql.append("	, dataset 																												\n");
		sql.append(" 	WHERE entry.dataset_uuid=dataset.uuid and dataset.name='Usage'															\n");
		sql.append("	 	AND entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 													\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND entry.actual=1																									\n");
		sql.append("	 	AND source.ownerid = ${para_participantname}  																		\n");
		sql.append("	 	ORDER BY entry.time																									\n");
		
		DateRange dr = initDataRange(date);
		params.put("para_participantname", participantName);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		return sql.toString();
	}
	
	private String findRawRealTimeEntryListForParticipantForUI(Map<String,Object> params,
			String participantName, Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.rawTime time, ROUND(entry.value,2) value, entry.actual actual							 										\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source				 																						\n");
		sql.append("	, dataset 																												\n");
		sql.append(" 	WHERE entry.dataset_uuid=dataset.uuid and dataset.name='Usage'															\n");
		sql.append("	 	AND entry.rawTime >=${para_startTime}  AND  entry.rawTime <=${para_endTime} 													\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND entry.actual=1																									\n");
		sql.append("	 	AND source.ownerid = ${para_participantname}  																		\n");
		sql.append("	 	ORDER BY entry.rawTime																									\n");
		
		DateRange dr = initDataRange(date);
		params.put("para_participantname", participantName);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		return sql.toString();
	}
	
	private String findRawRealTimeEntryListForEvent(Map<String,Object> params,
			String eventName, Date startTime) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, entry.value value, entry.actual actual 																\n");
		if(DateUtils.isSameDay(startTime, new Date())||new Date().before(startTime)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																									\n");
		sql.append("	 	participant p ,event_participant ep, event e																		\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.event_uuid = e.uuid																							\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND entry.actual=1																									\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		
		sql.append("	 	ORDER BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		return sql.toString();
	}

	private String findRawRealTimeEntryListForHistoricalEvent(Map<String,Object> params,
			String eventName, Date startTime) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, entry.value value, entry.actual actual 																\n");
		if(DateUtils.isSameDay(startTime, new Date())||new Date().before(startTime)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																									\n");
		sql.append("	 	participant p ,history_event_participant ep, history_event e														\n");
		sql.append("	WHERE e.eventName= ${para_eventName}																					\n");
		sql.append("	 	AND ep.history_event_uuid = e.uuid																					\n");
		sql.append("	 	AND p.uuid = ep.participant_uuid																					\n");
		sql.append("		AND YEAR(entry.time)= YEAR(e.startTime)																				\n");
		sql.append("		AND MONTH(entry.time)= MONTH(e.startTime)  																			\n");
		sql.append("		AND DAY(entry.time)= DAY(e.startTime) 																				\n");
		sql.append("	 	AND source.ownerid = p.participantName  																			\n");
		sql.append("	 	AND entry.actual=1																									\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		
		sql.append("	 	ORDER BY entry.time																									\n");
		
		params.put("para_eventName", eventName);
		return sql.toString();
	}
	
	private DateRange initDataRange(Date date) {
		DateRange dr = new DateRange();
		
		Date start = DateUtil.stripTime(date);
		Date end = DateUtil.endOfDay(date);
		dr.setStartTime(start);
		dr.setEndTime(end);

		return dr;
	}
	
	//duplicated with UsageDataServiceBean.java
    private UsageSummary getUsageSummaryFromList(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	//curTime >= startTime&& curTime <= endTime
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(start), UsageUtil.getCurrentTime(end), DateEntrySelectPredicate.BETWEEN_START_END, PDataEntry.class,"time");
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	double hours = (end.getTime() - start.getTime()) / 3600000.0;
    	summary.setTotal(summary.getAverage() * hours);
    	
    	return summary;
    }
    
    private UsageSummary getUsageSummaryFromList4Event(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	//curTime >= startTime&& curTime <= endTime
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(start), UsageUtil.getCurrentTime(end), DateEntrySelectPredicate.MT_START_LET_END, PDataEntry.class,"time");
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	double hours = (end.getTime() - start.getTime()) / 3600000.0;
    	summary.setTotal(summary.getAverage() * hours);
    	
    	return summary;
    }
	    
	@Override
	public com.akuacom.pss2.event.Event getEventByDateAndParticipant(
			String partName, Date date) {
		com.akuacom.pss2.event.Event eve = null;
		DateRange dr = UsageUtil.generateDateRange(date);
		List<com.akuacom.pss2.event.Event> events = null;
		List<String> excludePrograms = new ArrayList<String>();
		String excludes = sysManager.getPss2Properties().getExcludedProgramsForEventLine();
		if(excludes!=null){
			excludePrograms = Arrays.asList(excludes.split(","));
		}
		
		events = eventManager.findByParticipantProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()), Arrays.asList(partName),excludePrograms);
		
		if(events!=null&&events.size()>0){
			eve = events.iterator().next();
		}
		
		if(eve!=null) return eve;
			
		//if active or schedule event not exists ,get historical data	
		List<Object> participants = new ArrayList<Object>();
		List<Object> programs = new ArrayList<Object>();
		programs.addAll(excludePrograms);
		participants.add(partName);
		List<HistoryEvent> hisList = eventManager.findHisEventByParticipantProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()),participants,programs);
			
		if(hisList!=null&&hisList.size()>0){
			HistoryEvent he = hisList.iterator().next();
			eve = new com.akuacom.pss2.event.Event();
            eve.setEventName(he.getEventName());
            eve.setStartTime(he.getStartTime());
            eve.setEndTime(he.getEndTime());
            
            Set<EventParticipant> eps = new HashSet<EventParticipant>();
            Set<HistoryEventParticipant> heps = he.getEventParticipants();
            for(HistoryEventParticipant ep : heps){
            	if(40!=ep.getParticipation()){//not opt-out before event starts
            		Participant p = new Participant();
            		p.setParticipantName(ep.getParticipantName());
            		EventParticipant evePar = new EventParticipant();
            		evePar.setParticipant(p);
            		evePar.setOptOutTime(null);
            		
            		eps.add(evePar);
            	}
            }
            
            eve.setEventParticipants(eps);
		}
		
		return eve;
	}
		

	@Override
	public DateRange getPickableDateRange(String participantName,
			boolean individual) {
		 DateRange range = new DateRange();
		 PDataSource pdatasource = getDataSourceByNameAndOwner("meter1",
                    participantName);
		 PDataSet usage = this.getDataSetByName("Usage");
		 //if is individual participant, return days have usage data
		 if(individual){
			 List<Date> dates = null;
			 try {
				dates = getDataDays(Arrays.asList(usage.getUUID()), pdatasource.getUUID(),
				            null);
			} catch (EntityNotFoundException e) {
				log.error(e.getMessage());
				//e.printStackTrace();
			} catch(Exception e){
				log.error(e.getMessage());
			}
			
			if(dates==null||dates.isEmpty()){
				range.setStartTime(new Date());
				range.setEndTime(new Date());
				
				return range;
			}
			
			range.setStartTime(dates.get(0));
			range.setEndTime(dates.get(dates.size()-1));
			
			return range;
			
		 }
		 
		//if is aggegrator rentun days have both usage data and baseline data
		 List<PDataSource> expandedDataSource = this.getExpandedSources(pdatasource);
		 List<String> uuids = new ArrayList<String>();
		 uuids.add(pdatasource.getUUID());
		 if(expandedDataSource!=null&&!expandedDataSource.isEmpty()){
			 for(PDataSource source: expandedDataSource){
				 uuids.add(source.getUUID());
			 }
		 }
		
		 List<DataSourceUsage> dus = this.duEao.findBysourceId(uuids);
		 if(dus==null||dus.isEmpty()){
			range.setStartTime(new Date());
			range.setEndTime(new Date());
			
			return range;
		 }
		
		 range.setStartTime(dus.get(0).getDate());
		 range.setEndTime(dus.get(dus.size()-1).getDate());
			
		 return range;
	}


	private String findRealTimeEntryListForIndiviParticipant(Map<String,Object> params,
			String participantName, Date date, String dataSetuuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, ROUND(entry.value,2) value, entry.actual actual 																\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																			\n");
    	sql.append("    (SELECT * FROM datasource_usage WHERE DATE =${para_date} and maxgap <${para_maxGap}) du									\n");
		sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
		sql.append("	 	AND entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND source.ownerid = ${para_participantname}   																		\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	ORDER BY entry.time																									\n");
		
		params.put("para_datasetuuid", dataSetuuid);
		DateRange dr = initDataRange(date);
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_participantname", participantName);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		return sql.toString();
		
	}
	
	private String findContributedParticipantsSql(Map<String,Object> params,
			String participantName, Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																											\n");
		sql.append("	participant.participantName FROM																					\n");
		sql.append("	datasource source,  																								\n");
    	sql.append(" 	(SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du,		\n");
		sql.append("	(																													\n");
		sql.append("		SELECT DISTINCT p.participantName FROM participant p INNER JOIN 															\n");
		sql.append("	 	(	SELECT pp3.participant_uuid FROM program_participant pp3,													\n");
		sql.append("	 		(	SELECT pp.uuid FROM program_participant pp WHERE pp.participant_uuid = 									\n");
		sql.append("	 			(SELECT UUID FROM participant WHERE participantName = ${para_participantname})							\n");
		sql.append("	 		) AS pp2																									\n");
		sql.append("	 		WHERE LOCATE(pp2.uuid, pp3.ancestry) > 0																	\n");
		sql.append("		) AS pp4 																										\n");
		sql.append("	 	ON p.uuid = pp4.participant_uuid																				\n");
		sql.append("	 	UNION ALL 																										\n");
		sql.append("	 	SELECT ${para_participantname} 																					\n");
		sql.append("	) AS participant  																									\n");
		sql.append("	WHERE source.ownerid = participant.participantName  																\n");
		sql.append("	AND du.datasource_uuid = source.uuid																				\n");
		
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_participantname", participantName);
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		return sql.toString();
	}
	
	private String findAllParticipantsSql(Map<String,Object> params,
			String participantName, Date date) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT DISTINCT	participant.participantName FROM 																				\n");
		sql.append("	 	(																													\n");
		sql.append("	 		SELECT p.participantName FROM participant p INNER JOIN 															\n");
		sql.append("	 		(	SELECT pp3.participant_uuid FROM program_participant pp3,													\n");
		sql.append("	 			(	SELECT pp.uuid FROM program_participant pp WHERE pp.participant_uuid = 									\n");
		sql.append("	 				(SELECT UUID FROM participant WHERE participantName = ${para_participantname})							\n");
		sql.append("	 			) AS pp2																									\n");
		sql.append("	 			WHERE LOCATE(pp2.uuid, pp3.ancestry) > 0																	\n");
		sql.append("			) AS pp4 																										\n");
		sql.append("	 		ON p.uuid = pp4.participant_uuid																				\n");
		sql.append("	 		UNION ALL 																										\n");
		sql.append("	 		SELECT ${para_participantname} 																					\n");
		sql.append("		) AS participant  																									\n");
		
		params.put("para_participantname", participantName);
		return sql.toString();
	}

	@Override
	public List<String> findContributedParticipantNames(String participantName, Date date) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		String sql = findContributedParticipantsSql(params,participantName, date);
		List<String>results = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			results = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new SimpleListConverter<String>(
									String.class));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (results==null?new ArrayList<String>():results);
	}

	@Override
	public List<String> findAllParticipantNames(String participantName, Date date) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		String sql = findAllParticipantsSql(params,participantName, date);
		List<String>results = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			results = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new SimpleListConverter<String>(
									String.class));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (results==null?new ArrayList<String>():results);
	}
	
	
	/*
	 * Default implementation for usage data
	 * @param params
	 * @param participantName
	 * @param date
	 * @param dataSetuuid
	 * @param isIndividual
	 * @return
	 */
	private String findEntryListForParticipant(Map<String,Object> params,
			String participantName, Date date, String dataSetuuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, entry.value value, entry.actual actual 																\n");
		
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source  																									\n");
		sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
		sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND source.ownerid = ${para_participantname}   																		\n");
		sql.append("	 	GROUP BY entry.time																									\n");
		
		DateRange dr = initDataRange(date);
		params.put("para_datasetuuid", dataSetuuid);
		params.put("para_startTime", dr.getStartTime());
		params.put("para_endTime", dr.getEndTime());
		params.put("para_participantname", participantName);
		return sql.toString();
	}
	
	public List<PDataEntry> findDefaultEntryListForParticipant(String participantName, String dataSetuuid, Date date){
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = findEntryListForParticipant(params,participantName,
				 date, dataSetuuid);
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL =SQLBuilder.buildNamedParameterSQL(sql,params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (report==null?new ArrayList<PDataEntry>():report);
		
		
	}

	@Override
	public com.akuacom.pss2.event.Event getEventByDateAndAggreagator(
			String partName, Date date) {
		com.akuacom.pss2.event.Event eve = null;
		DateRange dr = UsageUtil.generateDateRange(date);
		List<com.akuacom.pss2.event.Event> events = null;
		List<String> excludePrograms = new ArrayList<String>();
		String excludes = sysManager.getPss2Properties().getExcludedProgramsForEventLine();
		if(excludes!=null){
			excludePrograms = Arrays.asList(excludes.split(","));
		}
		
		List<String> partNames = this.findAllParticipantNames(partName, date);
		//SELECT DISTINCT programName FROM `program_participant` WHERE participant_uuid = (SELECT UUID FROM participant WHERE participantName='A1')
		//AND programName NOT IN ('')
		List<String> programNames = findPrograms(partName, excludePrograms);

		events = eventManager.findByAggregatorProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()), partNames,programNames);
		
		if(events!=null&&events.size()>0){
			eve = events.iterator().next();
			eve.getEventParticipants().size();// init eventparticipants before session close
		}
		
		if(eve!=null) return eve;
			
		//if active or schedule event not exists ,get historical data	
		List<HistoryEvent> hisList = eventManager.findHisEventByAggregatorProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()),partNames,programNames);
			
		if(hisList!=null&&hisList.size()>0){
			HistoryEvent he = hisList.iterator().next();
			eve = new com.akuacom.pss2.event.Event();
            eve.setEventName(he.getEventName());
            eve.setStartTime(he.getStartTime());
            eve.setEndTime(he.getEndTime());
            
            Set<EventParticipant> eps = new HashSet<EventParticipant>();
            Set<HistoryEventParticipant> heps = he.getEventParticipants();
            for(HistoryEventParticipant ep : heps){
            	if(40!=ep.getParticipation()){//not opt-out before event starts
            		Participant p = new Participant();
            		p.setParticipantName(ep.getParticipantName());
            		EventParticipant evePar = new EventParticipant();
            		evePar.setParticipant(p);
            		evePar.setOptOutTime(null);
            		
            		eps.add(evePar);
            	}
            }
            
            eve.setEventParticipants(eps);
		}
		
		return eve;
	}

	@Override
	public List<String> findPrograms(String participantName,
			List<String> exculude) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		String sql = findProgramsSql(participantName,exculude, params);
		List<String>results = null;
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			results = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new SimpleListConverter<String>(
									String.class));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (results==null?new ArrayList<String>():results);
	}
	
	private String findProgramsSql(
			String participantName, List<String> exclude, Map<String,Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT DISTINCT programName FROM program_participant 														\n");
		sql.append("	 	WHERE participant_uuid = (SELECT uuid FROM participant WHERE participantName=${para_participantname})	\n");
		sql.append("	 	AND programName NOT IN ${exclude_programs}															\n");
		
		params.put("para_participantname", participantName);
		params.put("exclude_programs", exclude);
		return sql.toString();
	}

	@Override
	public List<PDataEntry> findForecastUsageDataEntryList(String setUUID,
			List<String> participantNames, Date date) {
		if(participantNames==null||participantNames.isEmpty()) return new ArrayList<PDataEntry>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = findForcastEntryListForParticipant(params,participantNames,
				date,setUUID);
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL =SQLBuilder.buildNamedParameterSQL(sql,params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (report==null?new ArrayList<PDataEntry>():report);
	}

	@Override
	public List<PDataEntry> findRealTimeUsageDataEntryList(String setUUID,
			List<String> participantNames, Date date) {
		if(participantNames==null||participantNames.isEmpty()) return new ArrayList<PDataEntry>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = findRealTimeEntryListForParticipant(params,participantNames,
				date,setUUID);
		List<PDataEntry>report = null;
		try {
			String parameterizedSQL =SQLBuilder.buildNamedParameterSQL(sql,params);
			report = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<PDataEntry>(
							new ColumnAsFeatureFactory<PDataEntry>(
							PDataEntry.class)));
		} catch (Exception e1) {
			log.error(e1);
			throw new EJBException(e1);
		}
		return (report==null?new ArrayList<PDataEntry>():report);
	}

	private String findRealTimeEntryListForParticipant(Map<String,Object> params,
			List<String> participantNames, Date date, String dataSetuuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM dataentry_temp AS entry				 																		\n");
		}else{
			sql.append("	FROM dataentry AS entry				 																				\n");
		}
		sql.append("	, datasource source,  																			\n");
    	sql.append(" (SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du				\n");//TODO:don't needed
		sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
		sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND source.ownerid in ${para_participantNames}  																	\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_datasetuuid", dataSetuuid);
		DateRange dr = initDataRange(date);
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_participantNames", participantNames);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		
		return sql.toString();
	}
	
	private String findForcastEntryListForParticipant(Map<String,Object> params,
			List<String> participantNames, Date date, String dataSetuuid) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																												\n");
		sql.append("	entry.time time, SUM(entry.value) value, MIN(entry.actual) actual 														\n");
		if(DateUtils.isSameDay(date, new Date())||new Date().before(date)){
			sql.append("	FROM baseline_dataentry_temp AS entry				 																\n");
		}else{
			sql.append("	FROM history_baseline_dataentry AS entry				 															\n");
		}
		
		sql.append("	, datasource source,  															\n");
		sql.append("	 	 (SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du									\n");
		
		sql.append("	 	WHERE 	entry.dataset_uuid = ${para_datasetuuid} 																	\n");
		sql.append("	 	And entry.time >=${para_startTime}  AND  entry.time <=${para_endTime} 												\n");
		sql.append("	 	AND entry.actual=1																							\n");
		sql.append("	 	AND entry.dataSource_uuid=source.uuid 																				\n");
		sql.append("	 	AND source.ownerid in ${para_participantNames}   																	\n");
		sql.append("	 	AND du.datasource_uuid = source.uuid																				\n");
		sql.append("	 	GROUP BY entry.time																									\n");
		
		params.put("para_datasetuuid", dataSetuuid);
		DateRange dr = initDataRange(date);
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_maxGap", sysManager.getPss2Properties().getMissingDataThreshold());
		params.put("para_participantNames", participantNames);
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
		
		return sql.toString();
	}

	
	
}
