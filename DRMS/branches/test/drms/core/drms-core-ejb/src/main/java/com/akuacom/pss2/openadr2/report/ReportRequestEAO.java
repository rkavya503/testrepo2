package com.akuacom.pss2.openadr2.report;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;


public interface ReportRequestEAO{

	@Remote
    public interface R extends ReportRequestEAO {}
    @Local
    public interface L extends ReportRequestEAO {}
   
	public ReportRequest create(ReportRequest entity) 
			throws DuplicateKeyException;
    public Report findById(String reportRequestId);
    public void updateReport(Report report);
    public void updateReportRequest(ReportRequest reportRequest);
    public ReportRequest findByVenIdandReportRequestId(String venId,String reportRequestId) 
    		throws NoResultException;
    public ReportRequest findByVenIdandReportRequestIds(String venId,List<String> reportRequestId)
    		throws EntityNotFoundException;
    public ReportRequest findByVenId(String venId) 
    		throws EntityNotFoundException;
	public void deleteReportRequest(ReportRequest reportRequest);
	public ReportRequest findCreateReportMessagesByVenId(String venId)
			throws EntityNotFoundException;
	public ReportRequest findCancelReportMessagesByVenId(String venId)
			throws EntityNotFoundException;
	public void deleteReport(Report report);
	public ReportRequest findOnlyReportRequestByVenId(String venId);
	public List<Report> findOnlyReportByReportRequest_uuid(String reportRequest_uuid,Boolean isCreated,Boolean isCancelled);
	public List<ReportDescription> findOnlyReportDescriptionByReport_uuid(String report_uuid,Boolean isCreated,Boolean isCancelled);
	public ReportRequest findCreatedReportsNotCancelledByVenId(String venId)
			throws EntityNotFoundException;
	public VenReportRequestHistory create(VenReportRequestHistory entity)
			throws DuplicateKeyException;
	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestIds(
			String venId, List<String> reportRequestId)
			throws EntityNotFoundException;
	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestId(
			String venId, String reportRequestId) throws NoResultException;
	public void updateVenReportRequestHistory(
			VenReportRequestHistory venReportRequestHistory);
	public List<VenReportRequestHistory> findVenReportRequestHistoryByVenIdandRequestId(
			String venId, String requestId) throws NoResultException;
	public String findResourceIdfromReportDataSourceByrId(String rId,String reportSpecifierId,String venId) throws NoResultException, NonUniqueResultException;
	
}
