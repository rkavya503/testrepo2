package com.akuacom.pss2.history;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.history.vo.ClientOfflineInstanceData;
import com.akuacom.pss2.history.vo.ClientOfflineStatisticsData;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.ReportEventParticipation;
import com.akuacom.pss2.history.vo.ReportEventPerformance;
import com.akuacom.pss2.history.vo.ReportSignalMaster;
import com.akuacom.pss2.history.vo.ReportSummaryVo;
import com.akuacom.pss2.program.Program;

public interface HistoryReportManager {

	@Remote
	public interface R extends HistoryReportManager {
	}

	@Local
	public interface L extends HistoryReportManager {
	}
	
	/**
	 * Get participant according to participantName name
	 * @param eventID
	 * @param programName
	 * @return
	 * @throws SQLException 
	 */
	List<ParticipantVO> findParticipants(String participantName,SearchConstraint sc) throws Exception;
	
	
	int getParticipantCount(String participantName) throws Exception;
	
	
	List<Program> findAllPrograms() throws Exception;
	
	
	List<Program> findPrograms(List<String> participantIds) throws Exception;
	
	
	List<ReportEvent> findEvents(String programName,DateRange range, SearchConstraint sc) throws Exception;
	
	int getEventCount(String programName,DateRange range, SearchConstraint sc) throws Exception;
	
	
	/**
	 * Get event participation 
	 * @param participantUUIDList
	 * @param programs
	 * @param start
	 * @param end
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	public List<ReportEventParticipation> getEventParticipation(List<String> participantUUIDList, 
			List<String> programs, Date start, Date end, SearchConstraint sc) throws Exception;
	public List<ReportEventParticipation> getEventParticipation(String eventName,
			List<String> programs, Date start, Date end, SearchConstraint sc) throws Exception;
	/**
	 * Get event participation count 
	 * @param participantUUIDList
	 * @param programs
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int getEventParticipationCount(List<String> participantUUIDList,
			List<String> programs, Date start, Date end) throws Exception;
	public int getEventParticipationCount(String eventName,
			List<String> programs, Date start, Date end) throws Exception;
	/**
	 * Get report event
	 * @param eventName
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	public List<ReportEvent> getReportEvent(String eventName,SearchConstraint sc) throws Exception;
	/**
	 * Get report event
	 * @param participantNames
	 * @param programNames
	 * @param start
	 * @param end
	 * @param sc
	 * @return
	 * @throws Exception
	 */
	public List<ReportEvent> getReportEvent(List<String> participantNames,
			List<String> programNames, Date start, Date end,SearchConstraint sc) throws Exception;
	/**
	 * Get report event count
	 * @param eventName
	 * @return
	 * @throws Exception
	 */
	public int getReportEventCount(String eventName) throws Exception ;
	/**
	 * Get report event count
	 * @param participantUUIDList
	 * @param programs
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public int getReportEventCount(List<String> participantNames,
			List<String> programNames, Date start, Date end) throws Exception;
	
	
	public List<ReportEventPerformance> getAllEventPerformance(String eventName,
			String program, SearchConstraint sc) throws Exception;
	/**
	 * Find Participants that participant in a given event
	 * @param event
	 * @return
	 * @throws Exception
	 */
	List<ParticipantVO> getParticipantsForEvent(ReportEvent event)  throws Exception;
		
	/**
	 * Get report summary for a group of participants
	 * @param participants
	 * @param date
	 * @param events
	 * @return
	 * @throws Exception
	 */
	List<ReportSummaryVo> getReportSummary(List<String> participantNames, 
			Date date, List<ReportEvent> events) throws Exception;
		
	/**
	 * Find events for a group given participants
	 * @param participants
	 * @param date
	 * @return
	 * @throws Exception
	 */
	List<ReportEvent> getRelatedEventsForparticipant(List<String> participants, Date date) throws Exception;
	
	/**
	 * Find a event from DB by a given name
	 * @param eventName
	 * @return
	 * @throws Exception
	 */
	ReportEvent getEventByName(String eventName) throws Exception;
		
	/**
	 * Find baseline from DB for a given date and participants
	 * @param participantNames
	 * @param date
	 * @return
	 */
	List<PDataEntry> findForecastUsageDataEntryList(List<String> participantNames,
            Date date);
		
	/**
	 * Find usage line from DB for a given date and participants
	 * @param participantNames
	 * @param date
	 * @return
	 */
	List<PDataEntry> findRealTimeUsageDataEntryList(List<String> participantNames,
            Date date);
	
	List<ClientOfflineStatisticsData> findClientOfflineStatistics(
			List<String> participantNames,String program,DateRange range,SearchConstraint sc) throws Exception;
	
	
	int  getClientOfflineStatisticsCount(
			List<String> participantNames,String program,DateRange range) throws Exception;
	
	List<ClientOfflineStatisticsData> findEventClientOfflineStatistics(
			ReportEvent event,SearchConstraint sc) throws Exception;
	
	int getEventClientOfflineStatisticsCount(
			ReportEvent event) throws Exception;
		
	
	List<ClientOfflineInstanceData> findClientOfflineInstance(
			List<String> participantNames,String program,DateRange range,SearchConstraint sc) throws Exception;
			
	
	int getClientOfflineInstanceCount(List<String> participantNames,
			String program,DateRange range) throws Exception;
	
	
	List<ClientOfflineInstanceData> findEventClientOfflineInstance(
			ReportEvent event,SearchConstraint sc) throws Exception;
	
	int getEventClientOfflineInstanceCount(
			ReportEvent event) throws Exception;
	
	public List<ReportEventPerformance> getEventParticipantPerformance(String eventName,
			String program, SearchConstraint sc) throws Exception;
	
	public List<ReportEventPerformance> getChildrenPerformance(String participantName,
			String eventName, String programName, SearchConstraint sc) throws Exception;
	
	public List<ReportSignalMaster> getEventSignal(String eventName, 
			String clientName, SearchConstraint sc)throws Exception;
	
	public List<String> getSignalNames(String eventName, String clientName);	
			PDataSet getDataSetByName(String name);
			
	public List<String> findAggParticipantNames(String participantName);
	
	public List<String> findContributedParticipantNames(Date date, List<String> participantNames);
	
	public HistoryEvent getEventByNameAsHistoryEvent(String eventName) throws Exception;
	
	public List<HistoryEventParticipant> getParticipantsForEventAsHistoryEventParticipant(String eventName) throws Exception;
	public List<ParticipantVO> getParticipants(List<String> participantNames) throws Exception;
	
	
	public List<ReportEventPerformance> getCustomerEventPerformance(String programName,String eventName,SearchConstraint sc) 
				throws Exception;
	public List<ReportEventPerformance> getCustomerEventPerformance(String programName,String eventName,String participantName, SearchConstraint sc) 
	throws Exception;
	
	public List<ReportEventPerformance> getCustomerChildrenEventPerformance(String parentName,String programName,String eventName,SearchConstraint sc) 
				throws Exception;
	
	public List<ReportEventPerformance> getCustomerAllEventPerformance(String programName,String eventName) 
	throws Exception;
	public List<ReportEventPerformance> getCustomerAllEventPerformance(String programName,String eventName,String participantName) 
	throws Exception;

	List<Program> getProgramsForParticipant(String participantName);

}


