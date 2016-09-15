package com.akuacom.pss2.openadr2.report;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.akuacom.ejb.DuplicateKeyException;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.RegisterReport;

public interface ReportRequestManager  {
	@Remote
    public interface R extends ReportRequestManager {}
    @Local
    public interface L extends ReportRequestManager {}
    
	public void registerReport(ReportRequest registerReport) throws DuplicateKeyException;
	
	public boolean cancelReport(String venId, List<String> reportRequestIds);

	public boolean createdReport(String venId, List<String> reportRequestId);
	
	public ReportRequest findReportRequestByVenId(String venId);
	
	public void deleteReportRequest(ReportRequest reportRequest);
	
	
	//Methods for Poll Service
	public RegisterReport getRegisterReportForVenId(String venId, String schemaVersion);
	public CreateReport getCreateReportForVenId(String venId,String schemaVersion);
	public CancelReport getCancelReportForVenId(String venId,String schemaVersion);
	
	
	public ReportRequest findCreatedReportsNotCancelledByVenId(String venId);

	public ReportRequest findByVenIdandReportRequestId(String id,
			String reportRequestId);
	public void updateReportRequest(ReportRequest reportRequest );

	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestId(
			String venId, String reportRequestId);

	public	Boolean UpdateCancelledStatusForVenReportRequestHistoryByVenIdandRequestId(
			String venId, String requestId);
	
	public String findResourceIdfromReportDataSourceByrId(String rId,String reportSpecifierId, String venId) throws NoResultException, NonUniqueResultException;
}