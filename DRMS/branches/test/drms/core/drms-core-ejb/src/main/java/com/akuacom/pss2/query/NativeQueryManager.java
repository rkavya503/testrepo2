package com.akuacom.pss2.query;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.pss2.report.entities.RTPShedStrategyReportSummary;

public interface NativeQueryManager {

	@Remote
	public interface R extends NativeQueryManager {
	};

	@Local
	public interface L extends NativeQueryManager {
	};

	List<ParticipantSummary> getParticipantSummary(
			ParticipantSearchCriteria psc, SearchConstraint sc)
			throws SQLException;

	int getParticipantSummaryCount(ParticipantSearchCriteria psc)
			throws SQLException;

	List<ClientSummary> getClientSummary(ClientSearchCriteria csc,
			SearchConstraint sc) throws SQLException;

	int getClientSummaryCount(ClientSearchCriteria csc) throws SQLException;

	List<PartContact> getClientContacts(ClientSearchCriteria csc,
			SearchConstraint sc) throws SQLException;

	List<ProgramSummary> getProgramSummary(SearchConstraint sc) throws SQLException;

	List<EvtParticipantCandidate> getEvtParticipantCandidate(String programName)
			throws SQLException;

	List<EventEnrollingGroup> getEventParticipantsByLocation(String programName,SCELocationType locType)
			throws SQLException;
	
	List<EvtPPWithConstraint> getEvtPPCandidateWithConstraint(String programName)
			throws SQLException;

	int validateAccount(String programName, String account) throws SQLException;

	List<EvtParticipantCandidate> getUploadEvtParticipantCandidate(
			String programName, List<String> accounts) throws SQLException;

	public List<EventClientSummary> getEventClientSummary(String eventName)
			throws SQLException;

	public List<EventParticipantSummary> getEventParticipantSummary(String eventName)
				throws SQLException;
	
	public  List<EventBidSummary> getEventBidSummary(String eventName) throws SQLException;

	List<EventParticipantSummary> getEnrollParticipantSummary(
			String programName, String eventName) throws SQLException;

	List<RTPShedStrategyReportSummary> getRTPShedStrategyReport(String programName)
			throws SQLException;
	
	List<Location> getEventLocations(String locType) throws SQLException;

	
	List<OptedOutClientList> getOptOutClients(String programName)
			throws SQLException;
			
}
