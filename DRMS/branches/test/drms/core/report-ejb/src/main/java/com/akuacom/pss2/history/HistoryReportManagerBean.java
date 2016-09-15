package com.akuacom.pss2.history;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.akuacom.jdbc.BeanFactory;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MasterDetailFactory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.jdbc.SQLWord;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.history.vo.ClientOfflineInstanceData;
import com.akuacom.pss2.history.vo.ClientOfflineStatisticsData;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.ReportEventParticipant;
import com.akuacom.pss2.history.vo.ReportEventParticipation;
import com.akuacom.pss2.history.vo.ReportEventPerformance;
import com.akuacom.pss2.history.vo.ReportSignalDetail;
import com.akuacom.pss2.history.vo.ReportSignalMaster;
import com.akuacom.pss2.history.vo.ReportSummaryVo;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;

@Stateless //(name="HistoryReportManager")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
//no need to initialize a transaction since all are query operation
public class HistoryReportManagerBean implements HistoryReportManager.L,HistoryReportManager.R{

	private static final Logger log = Logger
		.getLogger(HistoryReportManagerBean.class);
	
	@EJB
	ReportSQLExecutor.L sqlExecutor;
	@EJB
	private SystemManager.L systemManager;
	public SystemManager getSystemManager() {
//		if(systemManager==null)
//			systemManager =ServiceLocator.findHandler(SystemManager.class,
//					"pss2/SystemManagerBean/remote");
		return systemManager;
	}
	
	private static String CLIENT_STAUS_SQL;
	private static String EVENT_CLIENT_STATUS_SQL;
	
	private static String EVENTS_SQL;
	private static String EVENTS_COUNT_SQL;
	
	private static String CLIENT_OFFLINE_INSTANCE_SQL;
	private static String EVENT_OFFLINE_INSTANCE_SQL;
	private static String CLIENT_STATUS_COUNT_SQL;
	
	private static String EVENT_PARTICIPATION_COUNT_SQL;
	
	private static String EVENT_PARTICIPATION_SQL;
	
	private static String EVENT_PARTICIPATION_EVENT_SQL;
	
	private static String CST_PERFORMANCE_SUMMARY_SQL;
	
	private static String CST_PERFORMANCE_SUB_SUMMARY_SQL;
	
	private static String CST_EVENT_PEERFORMANCE_ALL_SQL;
	
	private static String OPT_PERFORMANCE_SUMMARY_SQL;
	
	private static String OPT_PERFORMANCE_SUB_SUMMARY_SQL;
	
	private static String OPT_EVENT_PERFORMANCE_ALL_SQL;
	
	private static String CLIENT_UUID_SQL;
	
	static {
		try {
			CLIENT_STAUS_SQL = getSQLFromFile("clientStatus.sql");
			CLIENT_STATUS_COUNT_SQL=getSQLFromFile("clientStatusCount.sql");
			EVENT_CLIENT_STATUS_SQL = getSQLFromFile("eventClientStatus.sql");
			
			CLIENT_OFFLINE_INSTANCE_SQL = getSQLFromFile("clientOfflineInstance.sql");
			EVENT_OFFLINE_INSTANCE_SQL = getSQLFromFile("eventOfflineInstance.sql");
			
			EVENTS_SQL = getSQLFromFile("events.sql");
			EVENTS_COUNT_SQL = getSQLFromFile("eventsCount.sql");
			
			EVENT_PARTICIPATION_COUNT_SQL =getSQLFromFile("eventparticipationCount.sql");
			EVENT_PARTICIPATION_SQL=getSQLFromFile("eventparticipation.sql");
			EVENT_PARTICIPATION_EVENT_SQL=getSQLFromFile("eventparticipation_event.sql");
			
			//for operator report only
			OPT_PERFORMANCE_SUMMARY_SQL=getSQLFromFile("operatorEventPerformance.sql");
			OPT_PERFORMANCE_SUB_SUMMARY_SQL= getSQLFromFile("operatorSubEventPerformance.sql");
			OPT_EVENT_PERFORMANCE_ALL_SQL= getSQLFromFile("operatorEventPerformanceExportAll.sql");
			
			//for customer report only
			CST_PERFORMANCE_SUMMARY_SQL=getSQLFromFile("customerEventPerformance.sql");
			CST_PERFORMANCE_SUB_SUMMARY_SQL=getSQLFromFile("customerSubEventPerformance.sql");
			CST_EVENT_PEERFORMANCE_ALL_SQL = getSQLFromFile("customerEventPerformanceExportAll.sql");
			
			CLIENT_UUID_SQL = getSQLFromFile("clientUuid.sql");
			
		} catch (Exception e) {
			log.error("Error happened while trying to get SQL from source file." + e.getStackTrace());
		}		
	}
	
	private static String getSQLFromFile(String sqlFileName) throws Exception{
		String sql = "";
		InputStream is = null;
		try{  
			is = HistoryReportManagerBean.class.getResourceAsStream("/com/akuacom/pss2/history/" + sqlFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally{
			if(is != null){
				is.close();
			}
		}
		return sql;
	}
	
	@Override
	public List<ParticipantVO> findParticipants(String participantName,SearchConstraint sc) throws Exception {
		String paramParticipantName = participantName +"%";
		String sqltemplate =" select UUID,participantName from participant where client= false and" +
				" participantName like ${participantName} [${orderBy}] [${range}]";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName", paramParticipantName);
		SQLWord word = SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		
		List<ParticipantVO> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ParticipantVO>(new ColumnAsFeatureFactory<ParticipantVO>(ParticipantVO.class)));
		return results;
	}
	
	public List<ParticipantVO> getParticipants(List<String> participantNames) throws Exception{
		String sqltemplate =" select UUID,participantName from participant where client= false and" +
				" participantName in ${participantName}";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName", participantNames);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		List<ParticipantVO> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ParticipantVO>(new ColumnAsFeatureFactory<ParticipantVO>(ParticipantVO.class)));
		return results;
	}
	@Override
	public int getParticipantCount(String participantName) throws SQLException {
		String param = "'%"+participantName +"%'";
		String sql =" select count(*) as count from participant where client= false and  participantName like ?";
		
		int count = sqlExecutor.doNativeQuery(sql,new Object[]{param},
				new CellConverter<Integer>(Integer.class));
		return count;
	}

	@Override
	public List<Program> findAllPrograms() throws SQLException{
		String sql =" select name as programName from program where name <> ${programName} ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", TestProgram.PROGRAM_NAME);
		
		String parameterizedSQL = null;
		try {
			parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
		} catch (SQLBuilderException e) {
			e.printStackTrace();
		}
		
		List<Program> results = sqlExecutor.doNativeQuery(parameterizedSQL,params,
				new ListConverter<Program>(new ColumnAsFeatureFactory<Program>(Program.class)));
		return results;
	}
	
	@Override
	public List<Program> findPrograms(List<String> participantIds) throws Exception {
		String sqltemplate =" select programName as programName from program_participant where participant_uuid in ${part}";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("part",participantIds);
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		List<Program> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<Program>(new ColumnAsFeatureFactory<Program>(Program.class)));
		return results;
	}

	@Override
	public List<ReportEvent> findEvents(String programName, DateRange range,
			SearchConstraint sc) throws Exception {
		String sqltemplate =" select UUID,eventName,programName,startTime,endTime from history_event " +
							" where startTime >=${startTime} and endTime<=${endTime}" +
							" [and programName = ${programName}] [${orderBy}] [${range}]";
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(programName != null)
			params.put("programName", programName);
		params.put("startTime", range.getStartTime());
		params.put("endTime", range.getEndTime());
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		List<ReportEvent> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ReportEvent>(new ColumnAsFeatureFactory<ReportEvent>(ReportEvent.class)));
		return results;
	}

	@Override
	public int getEventCount(String programName, DateRange range,
			SearchConstraint sc) throws Exception {
		String sqltemplate =" select count(*) from history_event " +
			" where startTime >=${startTime} and endTime<=${endTime} [and programName = ${programName}]";
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(programName != null)
			params.put("programName", programName);
		params.put("startTime", range.getStartTime());
		params.put("endTime", range.getEndTime());
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		int count = sqlExecutor.doNativeQuery(sql,params,
				new CellConverter<Integer>(Integer.class));
		return count;
	}

	
	@Override
	public List<ParticipantVO> getParticipantsForEvent(ReportEvent event) throws Exception{
		String sqltemplate = "select participant_uuid, participantName FROM " +
				"history_event_participant WHERE eventname LIKE ${eventname} AND CLIENT='0' AND participation<>'40' ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventname", event.getEventName());
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		List<ParticipantVO> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ParticipantVO>(new ColumnAsFeatureFactory<ParticipantVO>(ParticipantVO.class)));
		
		return results;
	}
	
	public HistoryEvent getEventByNameAsHistoryEvent(String eventName) throws Exception {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT * FROM history_event    \n");
		sqltemplate.append(" WHERE eventName =${eventName}           					 \n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
		
		List<HistoryEvent> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<HistoryEvent>(new ColumnAsFeatureFactory<HistoryEvent>(HistoryEvent.class)));
		if(results!=null&&!results.isEmpty())
			return results.get(0);
		
		return null;
	}
	
	public List<HistoryEventParticipant> getParticipantsForEventAsHistoryEventParticipant(String eventName) throws Exception {
		String sqltemplate = "select * FROM " +
				"history_event_participant WHERE eventname LIKE ${eventname}";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventname", eventName);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		List<HistoryEventParticipant> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<HistoryEventParticipant>(new ColumnAsFeatureFactory<HistoryEventParticipant>(HistoryEventParticipant.class)));
		
		return results;
	}
	
	@Override
	public List<ReportEvent> getRelatedEventsForparticipant(List<String> participantNames, Date date) throws Exception{
		DateRange dr = initDataRange(date);
		StringBuffer sqltemplate = new StringBuffer(); 

		sqltemplate.append("   SELECT he.eventname AS eventname,he.startTime AS startTime,   	 		   \n");
		sqltemplate.append("   he.endTime AS endTime,participantName,participation   	 				   \n");
		sqltemplate.append("   FROM history_event he,history_event_participant hep  	 				   \n");
		sqltemplate.append("   WHERE he.UUID = hep.history_event_uuid  	 					         	   \n");
		sqltemplate.append("   AND participantName IN ${participantNames} and participation <>'40' 	 	   \n");
		sqltemplate.append("   AND he.startTime >= ${param_start_time}  					         	   \n");
		sqltemplate.append("   AND he.startTime < ${param_end_time}  					         		   \n");
		sqltemplate.append("   AND he.programName not in ${programNames}	 					           \n");
		sqltemplate.append("   order by he.startTime,he.eventname,participantName 	 					   \n");

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantNames", participantNames);
		params.put("param_start_time", dr.getStartTime());
		params.put("param_end_time", dr.getEndTime());
		List<String> excludePrograms = new ArrayList<String>();
		String excludes = getSystemManager().getPss2Properties().getExcludedProgramsForEventLine();
		if(excludes!=null){
			excludePrograms = Arrays.asList(excludes.split(","));
		}
		
		params.put("programNames", excludePrograms);

		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
		
		MasterDetailFactory<ReportEvent,ReportEventParticipant> factory = new MasterDetailFactory<ReportEvent,ReportEventParticipant>(
                        new ColumnAsFeatureFactory<ReportEvent>(ReportEvent.class,"eventName"),
                        new ColumnAsFeatureFactory<ReportEventParticipant>(ReportEventParticipant.class,"participantName"),true){
                            private static final long serialVersionUID = 1L;
		                    public void buildUp(ReportEvent master, ReportEventParticipant detail){
                                if(detail!=null){						
                                    master.getParticipants().add(detail);
                                }
                            }
                        };
		  
        List<ReportEvent> results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<ReportEvent>(factory));

		return results;
		
	}
	
	@Override
	public List<ReportSummaryVo> getReportSummary(List<String> participants, 
			Date date, List<ReportEvent> events) throws Exception{
		//assume only one event per day
		if (participants == null || participants.isEmpty()) {
			return new ArrayList<ReportSummaryVo>();
		}
		List<PDataEntry> usage = null;
		usage = this.findRealTimeUsageDataEntryList(participants, date);
		Date firstUsageTime = null;
		Date lastUsageTime = null;
		if (usage != null && usage.size() > 0) {
			firstUsageTime = usage.get(0).getTime();
			lastUsageTime = usage.get(usage.size()-1).getTime();
		}
		if (firstUsageTime == null){
			firstUsageTime = DateUtil.getStartOfDay(date);
		}
		if (lastUsageTime == null) {
			lastUsageTime = DateUtil.getStartOfDay(date);       
		}
		
		List<PDataEntry> base = this.findForecastUsageDataEntryList(participants, date);
		ReportSummaryVo eveSum = new ReportSummaryVo();
        eveSum.setCatalog("During Event");
        ReportSummaryVo daySum = new ReportSummaryVo();
        daySum.setCatalog("Entire Day");
        
        UsageSummary actual = this.getUsageSummaryFromList(usage, firstUsageTime, lastUsageTime);             
        if(actual!=null){
            daySum.setActualAvg(convertNumber(actual.getAverage()));
            daySum.setActualTotal(calculateTotal(firstUsageTime, lastUsageTime, convertNumber(actual.getAverage())));
        }
        UsageSummary baseSummary = this.getUsageSummaryFromList(base, firstUsageTime, lastUsageTime);
        if(base!=null){
            daySum.setBaseAvg(convertNumber(baseSummary.getAverage()));
            daySum.setBaseTotal(calculateTotal(firstUsageTime, lastUsageTime, convertNumber(baseSummary.getAverage())));
        }
        if(actual!=null&&baseSummary!=null&&baseSummary.getAverage()>0&&actual.getAverage()>0){
            daySum.setShedAvg(convertNumber(daySum.getBaseAvg()-daySum.getActualAvg()));
            daySum.setShedTotal(daySum.getBaseTotal()-daySum.getActualTotal());
        }
        List<ReportSummaryVo> report =new ArrayList<ReportSummaryVo>();
        ReportEvent event = null;
        if(events!=null&!events.isEmpty()) event = events.get(0);
        if(event==null){
            report.add(daySum);
            report.add(eveSum);
            return report;
        }
        
        /**
         * handle opt-out
         */
        List<String> eventContributed = new ArrayList<String>();
        List<ReportEventParticipant> eps =  event.getParticipants();
        if(eps!=null){
        	for(ReportEventParticipant ep : eps){
    		    eventContributed.add(ep.getParticipantName());
    		}
        }
		List<String> aggContributed = new ArrayList<String>(participants);
		aggContributed.retainAll(eventContributed);//exclude those participants who doesn't attend this activity
		
		usage = this.findRealTimeUsageDataEntryList(aggContributed, date);
		base = this.findForecastUsageDataEntryList(aggContributed, date);
        
        UsageSummary baseEvent = null;
        UsageSummary shedEvent = null;
        UsageSummary actualEvent = null;
                  
        Date eventStart =  null;
        Date eventEnd =   null;
        eventStart =  event.getStartTime();
        eventEnd =   event.getEndTime();
        Date now = new Date();
	    if(!now.after(eventEnd)){
	     // when an event is in active status, calculate the usage report from event start time to current time
            eventEnd = now;
	    }
	    baseEvent = getUsageSummaryFromList4Event(base,eventStart,eventEnd);
	    actualEvent = getUsageSummaryFromList4Event(usage,eventStart,eventEnd);
	    shedEvent = getShedSummaryForEvent(baseEvent,actualEvent);
	    if(actualEvent!=null){
            eveSum.setActualAvg(convertNumber(actualEvent.getAverage()));
            eveSum.setActualTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(actualEvent.getAverage())));
	    }
	          
	    if(baseEvent!=null){
            eveSum.setBaseAvg(convertNumber(baseEvent.getAverage()));
            eveSum.setBaseTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(baseEvent.getAverage())));
	    }
	  
	    if(shedEvent!=null){
            eveSum.setShedAvg(convertNumber(shedEvent.getAverage()));
            eveSum.setShedTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(shedEvent.getAverage())));
	    }
	    
	    report.add(daySum);
	    report.add(eveSum);
		
		return report;
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
	
	private void constractConditionForTime(DateRange dr, StringBuffer sql, Map<String,Object> params) {
		sql.append("  entry.time >=${para_startTime}  AND  entry.time <${para_endTime} 			\n");
		params.put("para_startTime", dr.getStartTime());		
		params.put("para_endTime", dr.getEndTime());
	}
	
	private void constractConditionByPraticipants(
		List<String> participantNames, StringBuffer sql, Map<String,Object> params, Date date) {

		sql.append("  	AND entry.datasource_uuid IN 																						\n");
		sql.append("	(SELECT ds.UUID FROM datasource ds,datasource_usage du WHERE ds.ownerID in ${para_participantNames}					\n");
		sql.append("    AND du.DATE =${para_date} AND du.baseline_state=1 AND du.maxgap <${para_maxGap}										\n");
		sql.append("	AND du.datasource_uuid = ds.uuid																				\n");
		sql.append(" 	)																													\n");
		params.put("para_participantNames", participantNames);	
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_maxGap",getSystemManager().getPss2Properties().getMissingDataThreshold());
	}
	
	private void baselineConstractConditionByPraticipants(
		List<String> participantNames, StringBuffer sql, Map<String,Object> params, Date date) {

		sql.append("  	AND entry.datasource_uuid IN 																						\n");
		sql.append("	(SELECT ds.UUID FROM datasource ds,datasource_usage du WHERE ds.ownerID in ${para_participantNames}					\n");
		sql.append("    AND du.DATE =${para_date} AND du.baseline_state=1 																	\n");
		if(participantNames!=null&&participantNames.size()>1){
			sql.append("    AND du.maxgap <${para_maxGap}																					\n");
		}
		sql.append("	AND du.datasource_uuid = ds.uuid																					\n");
		sql.append(" 	)																													\n");
		params.put("para_participantNames", participantNames);	
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		if(participantNames!=null&&participantNames.size()>1){
			params.put("para_maxGap",getSystemManager().getPss2Properties().getMissingDataThreshold());
		}
	}
	
	private DateRange initDataRange(Date date) {
		DateRange dr = new DateRange();

		Date start = DateUtil.stripTime(date);
		Date end = DateUtil.stripTime(com.akuacom.utils.lang.DateUtil.getNextDay(date));
		dr.setStartTime(start);
		dr.setEndTime(end);

		return dr;
	}
	
	private String getSqlForFindForcastUsageData(Map<String,Object> params,
			List<String> participantNames, DateRange dr) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT  																					\n");
		sql.append("	entry.time time, SUM(ROUND(entry.value,2)) value, max(entry.actual) actual							\n");
		if(DateUtils.isSameDay(dr.getStartTime(), new Date())){
			sql.append("	FROM baseline_dataentry_temp AS entry				 									\n");
		}else{
			sql.append("	FROM history_baseline_dataentry AS entry			 									\n");
		}
		sql.append("	, dataset 																					\n");
		sql.append(" 	WHERE entry.dataset_uuid=dataset.uuid and dataset.name='Baseline' AND						\n");
		sql.append(" 	entry.actual=1  AND																			\n");
		constractConditionForTime(dr, sql, params);
		baselineConstractConditionByPraticipants(participantNames, sql, params,dr.getStartTime());
		sql.append("	GROUP BY 	entry.time																		\n");
		return sql.toString();
	}
	
	private String getSqlForFindRealTimeUsageData(Map<String,Object> params,
			List<String> participantNames, DateRange dr) {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT   																					\n");
		sql.append("	entry.time time, SUM(ROUND(entry.value,2)) value, max(entry.actual) actual   						\n");
		if(DateUtils.isSameDay(new Date(), dr.getStartTime())){
			sql.append("	FROM dataentry_temp AS entry   															\n");
		}else{
			sql.append("	FROM dataentry AS entry   																\n");
		}
		sql.append("	, dataset 																					\n");
		sql.append(" 	WHERE entry.dataset_uuid=dataset.uuid and dataset.name='Usage' AND							\n");
		constractConditionForTime(dr, sql, params);
		constractConditionByPraticipants(participantNames, sql, params,dr.getStartTime());
		sql.append("	GROUP BY 	entry.time																		\n");
		return sql.toString();
	}
	
	@Override
	public List<PDataEntry> findForecastUsageDataEntryList(
			List<String> participantNames, Date date) {
		List<PDataEntry> quertResult = null;
		if (participantNames == null || participantNames.isEmpty()) {
			return Collections.emptyList();
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		String sql = getSqlForFindForcastUsageData(params,participantNames, initDataRange(date));
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql,params);
			
			quertResult = sqlExecutor.doNativeQuery(parameterizedSQL,params,
			new ListConverter<PDataEntry>(
			new ColumnAsFeatureFactory<PDataEntry>(
					PDataEntry.class)));
		} catch (Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}
		return quertResult;
	}
	
	@Override
	public List<PDataEntry> findRealTimeUsageDataEntryList(
			List<String> participantNames, Date date) {
		List<PDataEntry> quertResult = null;
		if (participantNames == null || participantNames.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = getSqlForFindRealTimeUsageData(params,participantNames, initDataRange(date));
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql,params);
			quertResult = sqlExecutor.doNativeQuery(parameterizedSQL,params,
			new ListConverter<PDataEntry>(
			new ColumnAsFeatureFactory<PDataEntry>(
			PDataEntry.class)));
			
		} catch (Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}

		return quertResult;
	}

	@Override
	public ReportEvent getEventByName(String eventName) throws Exception {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT eventName, startTime , endTime FROM history_event    \n");
		sqltemplate.append(" WHERE eventName =${eventName}           					 \n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
		
		List<ReportEvent> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ReportEvent>(new ColumnAsFeatureFactory<ReportEvent>(ReportEvent.class)));
		if(results!=null&&!results.isEmpty())
			return results.get(0);
		
		return null;
	}

	@Override
	public List<ClientOfflineStatisticsData> findClientOfflineStatistics(
			List<String> participantNames, String program, DateRange range,SearchConstraint sc)
			throws Exception {
		
		List<ClientOfflineStatisticsData> results = null;		
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName",participantNames);
		params.put("startTime", range.getStartTime());
		params.put("endTime", range.getEndTime());
		if(program!=null)
			params.put("programName", program);
		
		String sql = SQLBuilder.buildNamedParameterSQL(CLIENT_UUID_SQL, params);
		
		List<String> clientIds = null;
		clientIds = sqlExecutor.doNativeQuery(sql,params,
				new SimpleListConverter<String>(
								String.class));
		if (clientIds.size() > 0 ){
			params.put("clientIds",clientIds);
			
			SQLWord word =  SQLBuilder.getOrderBy(sc);
			if(word!=null) 	params.put("orderBy", word);
			
			word = SQLBuilder.getMySqlLimit(sc);
			if(word!=null)	params.put("range", word);
			
			sql = SQLBuilder.buildNamedParameterSQL(CLIENT_STAUS_SQL, params);
			results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ClientOfflineStatisticsData>(
				new ColumnAsFeatureFactory<ClientOfflineStatisticsData>(ClientOfflineStatisticsData.class)));
		}
		
		return results;
	}

	@Override
	public int getClientOfflineStatisticsCount(List<String> participantNames,
			String program, DateRange range) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName",participantNames);
		
		if(program!=null)
			params.put("programName", program);
		
		String sql = SQLBuilder.buildNamedParameterSQL(CLIENT_STATUS_COUNT_SQL, params);
		
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}

	@Override
	public List<ClientOfflineStatisticsData> findEventClientOfflineStatistics(
			ReportEvent event,SearchConstraint sc) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", event.getEventName());
		params.put("startTime", event.getStartTime());
		params.put("endTime", event.getEndTime());
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENT_CLIENT_STATUS_SQL, params);
		
		List<ClientOfflineStatisticsData> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ClientOfflineStatisticsData>(
			new ColumnAsFeatureFactory<ClientOfflineStatisticsData>(ClientOfflineStatisticsData.class)));
		
		return results;
	}

	@Override
	public int getEventClientOfflineStatisticsCount(ReportEvent event)
			throws Exception {
		String sqlTempalte =""+
			 "SELECT count(participant_uuid) as cnt FROM "+
	         "history_event_participant WHERE eventName =${eventName} AND client=TRUE ";
	    
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", event.getEventName());
		params.put("startTime", event.getStartTime());
		params.put("endTime", event.getEndTime());
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}

	@Override
	public List<ClientOfflineInstanceData> findClientOfflineInstance(
			List<String> participantNames, String program, DateRange range,SearchConstraint sc)
			throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName",participantNames);
		params.put("startTime", range.getStartTime());
		params.put("endTime", range.getEndTime());
		if(program!=null)
			params.put("programName", program);
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(CLIENT_OFFLINE_INSTANCE_SQL, params);
		
		List<ClientOfflineInstanceData> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ClientOfflineInstanceData>(
			new ColumnAsFeatureFactory<ClientOfflineInstanceData>(ClientOfflineInstanceData.class)));
		
		return results;
	}

	@Override
	public int getClientOfflineInstanceCount(List<String> participantNames,
			String program, DateRange range) throws Exception {
		
		String sqlTempalte ="select count(*) from \n ("+CLIENT_OFFLINE_INSTANCE_SQL+"\n) ins";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName",participantNames);
		params.put("startTime", range.getStartTime());
		params.put("endTime", range.getEndTime());
		if(program!=null)
			params.put("programName", program);
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}

	@Override
	public List<ClientOfflineInstanceData> findEventClientOfflineInstance(
			ReportEvent event,SearchConstraint sc) throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", event.getEventName());
		params.put("startTime", event.getStartTime());
		params.put("endTime", event.getEndTime());
		params.put("programName", event.getProgramName());
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENT_OFFLINE_INSTANCE_SQL, params);
		
		List<ClientOfflineInstanceData> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ClientOfflineInstanceData>(
			new ColumnAsFeatureFactory<ClientOfflineInstanceData>(ClientOfflineInstanceData.class)));
		
		return results;
	}

	@Override
	public int getEventClientOfflineInstanceCount(ReportEvent event)
			throws Exception {
		
		String sqlTempalte ="select count(*) from \n("+EVENT_OFFLINE_INSTANCE_SQL+"\n) ins";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", event.getEventName());
		params.put("startTime", event.getStartTime());
		params.put("endTime", event.getEndTime());
		params.put("programName", event.getProgramName());
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}
	
	@Override
	public int getEventParticipationCount(List<String> participantNames,
			List<String> programs, Date start, Date end) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName",participantNames);
		params.put("startTime",      start);
		params.put("endTime",        end);
		if(programs!=null && !programs.isEmpty())
			params.put("programName", programs);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENT_PARTICIPATION_COUNT_SQL, params);
		
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}

	@Override
	public List<ReportEventParticipation> getEventParticipation(List<String> participantName,
			List<String> programs, Date start, Date end, SearchConstraint sc) throws Exception{
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("participantName", participantName);
		if(programs!=null && !programs.isEmpty()){
			params.put("programName",    programs);
		}else
			return Collections.emptyList();
		
		params.put("startTime",      start);
		params.put("endTime",        end);
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENT_PARTICIPATION_SQL, params);
		
		List<ReportEventParticipation> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventParticipation>(
			new ColumnAsFeatureFactory<ReportEventParticipation>(ReportEventParticipation.class)));
		
		return results;
	}
	
	public List<ReportEventParticipation> getEventParticipation(String eventName,
			List<String> programs, Date start, Date end, SearchConstraint sc) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENT_PARTICIPATION_EVENT_SQL, params);
		
		List<ReportEventParticipation> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventParticipation>(
			new ColumnAsFeatureFactory<ReportEventParticipation>(ReportEventParticipation.class)));
		return results;
	}
	
	/**
	 * Get event participation count 
	 * @param participantUUIDList
	 * @param programs
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int getEventParticipationCount(String eventName,List<String> programs, 
						Date start, Date end) throws Exception{
		String sqltemplate="SELECT  COUNT(hep.participantName)" 
			+ " FROM history_event he,  history_event_participant hep,participant p "
			+ " WHERE p.client = 1 "
		    + "    AND hep.eventName=${eventName}"
		    + "     AND hep.history_event_uuid = he.uuid "
		    + "     AND hep.participant_uuid = p.uuid ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}
	
	/**
	 * Get report event
	 * @param eventName
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ReportEvent> getReportEvent(String eventName,SearchConstraint sc) throws Exception{
		String paramEventName = "%"+eventName +"%";

		String sqltemplate =" select programName,eventName,startTime,endTime,scheduledStartTime," +
				"scheduledEndTime,cancelled from history_event where eventName  like ${eventName} [${orderBy}] [${range}]";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", paramEventName);
		if(sc!=null){
			SQLWord word = SQLBuilder.getOrderBy(sc);
			if(word!=null) 	params.put("orderBy", word);
			
			word = SQLBuilder.getMySqlLimit(sc);
			if(word!=null)	params.put("range", word);
		}
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		List<ReportEvent> results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ReportEvent>(new ColumnAsFeatureFactory<ReportEvent>(ReportEvent.class)));
		return results;
	}
	
	
	@Override
	public List<ReportEvent> getReportEvent(List<String> participantNames,
			List<String> programNames, Date start, Date end,SearchConstraint sc) throws Exception{	
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", start);
		params.put("endTime", end);
		if(participantNames!=null && !participantNames.isEmpty())
			params.put("participantNames", participantNames);
		
		if(programNames!=null && !programNames.isEmpty())
			params.put("programNames", programNames);
		else
			return Collections.emptyList();
		
		SQLWord word =  SQLBuilder.getOrderBy(sc);
		if(word!=null) 	params.put("orderBy", word);
		
		word = SQLBuilder.getMySqlLimit(sc);
		if(word!=null)	params.put("range", word);
		
		String sql = SQLBuilder.buildNamedParameterSQL(EVENTS_SQL, params);
		
		List<ReportEvent> results = 
			sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ReportEvent>(new ColumnAsFeatureFactory<ReportEvent>(ReportEvent.class))
				);
		return results;
	}
	
	@Override
	public int getReportEventCount(List<String> participantNames,List<String> programNames,
				Date start, Date end) throws Exception{
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", start);
		params.put("endTime", end);
		if(participantNames!=null && !participantNames.isEmpty())
			params.put("participantNames", participantNames);
		else return 0;
		
		if(programNames!=null && !programNames.isEmpty())
			params.put("programNames", programNames);
		String sql = SQLBuilder.buildNamedParameterSQL(EVENTS_COUNT_SQL, params);
		
		int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
		return count;
	}	
	
	/**
	 * Get report event count
	 * @param eventName
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getReportEventCount(String eventName) throws Exception {
		String param = "%"+eventName +"%";
		String sql =" select count(*) as count from history_event where eventName like ${eventName}";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", param);
		
		String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
		
		int count = sqlExecutor.doNativeQuery(parameterizedSQL,params,new CellConverter<Integer>(Integer.class));
		return count;	
	}	
	
	@Override
	public List<ReportEventPerformance> getChildrenPerformance(String parent, String eventName, 
			String programName, SearchConstraint sc) throws Exception{
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);
		params.put("parent", parent);
		String sql = SQLBuilder.buildNamedParameterSQL(OPT_PERFORMANCE_SUB_SUMMARY_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;
	}
	
	public List<ReportEventPerformance> getAllEventPerformance(String eventName,
			String program, SearchConstraint sc) throws Exception{	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",program);
		params.put("eventName", eventName);
		String sql = SQLBuilder.buildNamedParameterSQL(OPT_EVENT_PERFORMANCE_ALL_SQL, params);
		
		List<ReportEventPerformance> results = 
			sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ReportEventPerformance>(
						new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)
				)
			);
		return results;
	}	
	
	@Override
	public List<ReportEventPerformance> getEventParticipantPerformance(String eventName,
			String program, SearchConstraint sc) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",program);
		params.put("eventName", eventName);
		String sql = SQLBuilder.buildNamedParameterSQL(OPT_PERFORMANCE_SUMMARY_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;
	}
	
	public List<ReportEventPerformance> getEventClientPerformance(String eventName,
			List<String> participantUUIDList, List<String> programs, SearchConstraint sc) throws Exception{
		return null;
	}
	
	@Override
	public List<ReportEventPerformance> getCustomerEventPerformance
				(String programName,String eventName,SearchConstraint sc) 	throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);

		String sql = SQLBuilder.buildNamedParameterSQL(CST_PERFORMANCE_SUMMARY_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;
	}
	@Override
	public List<ReportEventPerformance> getCustomerEventPerformance
				(String programName,String eventName,String participantName, SearchConstraint sc) 	throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);
		params.put("participantName", participantName);

		String sql = SQLBuilder.buildNamedParameterSQL(CST_PERFORMANCE_SUMMARY_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;
	}
	
	@Override
	public List<ReportEventPerformance> getCustomerChildrenEventPerformance
			(String parentName,String programName,String eventName,SearchConstraint sc) 
			throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);
		params.put("parent", parentName);

		String sql = SQLBuilder.buildNamedParameterSQL(CST_PERFORMANCE_SUB_SUMMARY_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;		
				
	}
			
	@Override
	public List<ReportEventPerformance> getCustomerAllEventPerformance(String programName,String eventName) 
		throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);

		String sql = SQLBuilder.buildNamedParameterSQL(CST_EVENT_PEERFORMANCE_ALL_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,params,
			new ListConverter<ReportEventPerformance>(
			new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
		return results;
	}
	@Override
	public List<ReportEventPerformance> getCustomerAllEventPerformance(String programName,String eventName,String participantName) 
		throws Exception {
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName",programName);
		params.put("eventName", eventName);
		params.put("participantName", participantName);

		String sql = SQLBuilder.buildSQL(CST_EVENT_PEERFORMANCE_ALL_SQL, params);
		
		List<ReportEventPerformance> results = sqlExecutor.doNativeQuery(sql,
				new ListConverter<ReportEventPerformance>(
				new ColumnAsFeatureFactory<ReportEventPerformance>(ReportEventPerformance.class)));
			return results;
	}
	@Override
	public List<ReportSignalMaster> getEventSignal(String eventName, String clientName, SearchConstraint sc)throws Exception{
		List<ReportSignalMaster> report =new ArrayList<ReportSignalMaster>();
		
		String sql = generateSignalDetailSQL(eventName, clientName, sc);

		MasterDetailFactory<ReportSignalMaster, ReportSignalDetail> factory = 
				new MasterDetailFactory<ReportSignalMaster, ReportSignalDetail>(
					new ColumnAsFeatureFactory<ReportSignalMaster>(
							ReportSignalMaster.class, "signalTime"),
					new ColumnAsFeatureFactory<ReportSignalDetail>(
							ReportSignalDetail.class)) {

				private static final long serialVersionUID = 6951017266263011396L;
				public void buildUp(ReportSignalMaster master,
						ReportSignalDetail detail) {
					master.getItemMap().put(detail.getSignalName(), detail);
				}
			};

		report = (List<ReportSignalMaster>) sqlExecutor.doNativeQuery(sql,
				new ListConverter<ReportSignalMaster>(factory));
	
		return report;
	}

	@Override
	public List<String> getSignalNames(String eventName, String clientName){
		StringBuffer historySignalSql=new StringBuffer();
		historySignalSql.append("select distinct signalName from history_event_participant_signal ");
		historySignalSql.append(" where 1=1 [and eventName=${eventName}] [and participantName=${clientName}]");
		
		StringBuffer sqltemplate=new StringBuffer();
		sqltemplate.append(historySignalSql.toString());
		sqltemplate.append(" union ");
		sqltemplate.append("select s.signalName from history_event e, program p, program_signal ps, signal_def s ");
		sqltemplate.append(" where p.name=e.programName [and e.eventName= ${eventName}] and p.uuid=ps.program_uuid ");
		sqltemplate.append(" [and p.name=${programName}] and ps.signal_def_uuid= s.uuid ");
		sqltemplate.append(" and not exists (");
		sqltemplate.append(historySignalSql.toString());
		sqltemplate.append(")");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventName", eventName);
		params.put("clientName", clientName);
		
		List<String> report=new ArrayList<String>();
		try {
			String sql=SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			report = (List<String>) sqlExecutor.doNativeQuery(sql,params, new SimpleListConverter<String>(String.class));
		}catch(Exception e) {
			throw new EJBException(e);
		}
		return report;
	}
	
	private String generateSignalDetailSQL(String eventName, String clientName, SearchConstraint sc) throws Exception{
		StringBuilder builder = new StringBuilder();
		builder.append(" select signalName, signalTime, signalValue ");
		builder.append(" from history_event_participant_signal s");
		builder.append(" WHERE s.eventName= ${eventName}");
		builder.append(" AND s.participantName = ${clientName}");
		
		Map<String,Object> params = new HashMap<String,Object>();		
		params.put("eventName", eventName);		
		params.put("clientName", clientName);

		if(sc!=null){
			builder.append(" [${orderBy}] [${limit}] ");
			String sqlTemplate = builder.toString();		
			SQLWord word = SQLBuilder.getOrderBy(sc);
			if(word!=null) 	params.put("orderBy", word);		
			word = SQLBuilder.getMySqlLimit(sc);
			if(word!=null)	params.put("limit", word);
			String sql = SQLBuilder.buildSQL(sqlTemplate, params);
			return sql;
		}else{
			return SQLBuilder.buildSQL(builder.toString(), params);
		}	
	}
	
	@Override
	public PDataSet getDataSetByName(String name) {
		PDataSet set = new PDataSet();
		
		StringBuffer sqltemplate=new StringBuffer();
		sqltemplate.append("SELECT name,unit,sync,period FROM dataset 				\n");
		sqltemplate.append("where  name=${datasetName}								\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("datasetName", name);
		
		try {
			String sql=SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			List<PDataSet> results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<PDataSet>(new ColumnAsFeatureFactory<PDataSet>(PDataSet.class)));
			if(results!=null&&results.size()>0){
				set = results.iterator().next();
			}
			//set =  sqlExecutor.doNativeQuery(sql, new SinglePojoConverter<PDataSet>(new ColumnAsFeatureFactory<PDataSet>(PDataSet.class)));
		}catch(Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}
		return set;
	}
	
	@Override
	public List<String> findAggParticipantNames(String participantName) {
		List<String> quertResult = null;

		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT  participantName FROM participant WHERE CLIENT ='0'									\n");
		sql.append("	AND UUID IN(																				\n");
		sql.append("	SELECT pp3.participant_uuid AS UUID FROM program_participant pp3,   						\n");
		sql.append(" 	( 																							\n");
		sql.append("	SELECT pp.uuid ,pp.participant_uuid FROM program_participant pp 							\n");
		sql.append("	WHERE pp.participant_uuid = 																\n");
		sql.append("	(SELECT UUID FROM participant WHERE participantName = ${para_part_name})					\n");
		sql.append("	) AS pp2																					\n");
		sql.append("	WHERE (LOCATE(pp2.uuid, pp3.ancestry) > 0 OR pp3.participant_uuid=pp2.participant_uuid)		\n");
		sql.append("	AND pp3.state = 1	)																		\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("para_part_name", participantName);
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql.toString(), params);
			quertResult = sqlExecutor.doNativeQuery(parameterizedSQL,params,
			new ListConverter<String>(
			new BeanFactory<String>(){
				private static final long serialVersionUID = 1301228176411308595L;
				@Override
				public String createInstance(ResultSet rs) throws SQLException {
					String value = null;
					ResultSetMetaData metadata =rs.getMetaData();
					for(int i =1  ; i<= metadata.getColumnCount();i++){
						value =  (String) rs.getObject(i);
					}
					return value;
				}
				@Override
				public String[] getKeyColumns() {
					return new String[]{};
				}
				
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return quertResult;
	}
	
	@Override
	public List<Program> getProgramsForParticipant(String participantName) {
		try{
			String sql =" SELECT DISTINCT pg.name AS programName FROM program pg,program_participant pp, participant pc "
				+" WHERE pg.uuid=pp.program_uuid AND pp.participant_uuid = pc.uuid " 
				+" AND pc.participantName =${para_part_name}";
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("para_part_name", participantName);
			
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			
			List<Program> results = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<Program>(new ColumnAsFeatureFactory<Program>(Program.class)));
			return results;
		}catch(Exception e){
			return Collections.emptyList();
		}
	}
	private UsageSummary getUsageSummaryFromList(List<PDataEntry> usageList, Date start, Date end){
        UsageSummary summary = new UsageSummary();
            if(usageList==null||usageList.isEmpty()) return summary;
            
            DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(start.getTime(), end.getTime());
            List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
            
            if(usageDes==null||usageDes.isEmpty()) return summary;
            
            double sum = 0;
            for(PDataEntry entry : usageDes){
                    sum += entry.getValue();
            }
            summary.setAverage(sum/usageDes.size());
            
            return summary;
    }
    //duplicate with usageDataServicebean
    class DateEntrySelectPredicate implements Predicate {
            
            private long startTime;
            private long endTime;
            public DateEntrySelectPredicate(long startTime, long endTime){
                    this.startTime = startTime;
                    this.endTime = endTime;
            }

            @Override
            public boolean evaluate(Object object) {
                    PDataEntry entry = (PDataEntry) object;
                    long curTime = entry.getTime().getTime();
                    if (curTime >= startTime&& curTime <= endTime) {
                            return true;
                    }
                    return false;
                    
            }
    }
    
    private UsageSummary getUsageSummaryFromList4Event(List<PDataEntry> usageList, Date start, Date end){
        UsageSummary summary = new UsageSummary();
            if(usageList==null||usageList.isEmpty()) return summary;
            
            DateEntrySelectPredicate4Event predicate = new DateEntrySelectPredicate4Event(start.getTime(), end.getTime());
            List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
            
            if(usageDes==null||usageDes.isEmpty()) return summary;
            
            double sum = 0;
            for(PDataEntry entry : usageDes){
                    sum += entry.getValue();
            }
            summary.setAverage(sum/usageDes.size());
            
            return summary;
    }
    //duplicate with usageDataServicebean
    class DateEntrySelectPredicate4Event implements Predicate {
            
            private long startTime;
            private long endTime;
            public DateEntrySelectPredicate4Event(long startTime, long endTime){
                    this.startTime = startTime;
                    this.endTime = endTime;
            }

            @Override
            public boolean evaluate(Object object) {
                    PDataEntry entry = (PDataEntry) object;
                    long curTime = entry.getTime().getTime();
                    if (curTime > startTime&& curTime <= endTime) {
                            return true;
                    }
                    return false;
                    
            }
    }
    
    // Calculates the total for an hourly average across a timespan. 
    private static double calculateTotal(Date start, Date end, double avg) {
            if(Double.isNaN(avg)){
                    avg = 0;
            }
        double total = 0;
        double hours = 0.0;
 	    if((start!=null)&&(end!=null)&&DateUtils.isSameDay(start, new Date())){
 		    hours = (end.getTime()-start.getTime())/3600000.0;
 	    }else{
 		    hours = 24;
 	    }
        total = avg*hours;

        return total;
    }
    
    private static double calculateEventDurationTotal(Date start, Date end, Date current, double avg) {
        if(Double.isNaN(avg)){
                avg = 0;
        }
        if(!current.after(start)){
                //event have not started yet
                return 0;
        }
        //if event still active, event duration = current time - start time
        end = end.after(current)?current:end;
        double total = 0;
        double hours = (end.getTime()-start.getTime())/3600000.0;

        total = avg*hours;

        return total;
    }
    
    private UsageSummary getShedSummaryForEvent(UsageSummary baseEvent, UsageSummary actualEvent){
        UsageSummary summary = new UsageSummary();
        if(baseEvent==null||actualEvent==null||baseEvent.getAverage()==0||actualEvent.getAverage()==0) return summary;
                
        summary.setAverage(convertNumber(baseEvent.getAverage())-convertNumber(actualEvent.getAverage()));
                
        return summary;
    }

	@Override
	public List<String> findContributedParticipantNames(Date date,
			List<String> participantNames) {
		List<String> quertResult = null;

		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT	 																											\n");
		sql.append("	source.ownerid FROM																					\n");
		sql.append("	datasource source,  																								\n");
    	sql.append(" 	(SELECT * FROM datasource_usage WHERE DATE =${para_date} AND baseline_state=1 and maxgap <${para_maxGap}) du		\n");
		sql.append("	WHERE source.ownerid in ${para_participantnames}  																\n");
		sql.append("	AND du.datasource_uuid = source.uuid																				\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("para_date", DateUtil.format(date, "yyyy-MM-dd"));
		params.put("para_participantnames",  participantNames);
		params.put("para_maxGap", getSystemManager().getPss2Properties().getMissingDataThreshold());
		try {
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql.toString(), params);
			quertResult = sqlExecutor.doNativeQuery(parameterizedSQL,params,
			new ListConverter<String>(
			new BeanFactory<String>(){
				private static final long serialVersionUID = 1301228176411308595L;
				@Override
				public String createInstance(ResultSet rs) throws SQLException {
					String value = null;
					ResultSetMetaData metadata =rs.getMetaData();
					for(int i =1  ; i<= metadata.getColumnCount();i++){
						value =  (String) rs.getObject(i);
					}
					return value;
				}
				@Override
				public String[] getKeyColumns() {
					return new String[]{};
				}
				
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return quertResult;
	}
	 	 	
}
