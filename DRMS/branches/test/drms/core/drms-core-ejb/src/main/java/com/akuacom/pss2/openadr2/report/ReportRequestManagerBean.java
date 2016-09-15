package com.akuacom.pss2.openadr2.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.honeywell.dras.vtn.api.common.WsCalanderInterval;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.ItemBase;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.ReportSpecifier;
import com.honeywell.dras.vtn.api.report.SpecifierPayload;


@Stateless
public class ReportRequestManagerBean implements ReportRequestManager.R, ReportRequestManager.L {
	
	@EJB
	ReportRequestEAO.L reportRequestEAO;
	
	private Logger log = Logger.getLogger(ReportRequestManagerBean.class);
	
	public static final String REQUEST_ID_PREFIX = "AKUACOM.7.2.REQ:";
	
	public static final String REPORT_REQUEST_ID_PREFIX = "AKUACOM.7.2.REQ:RReq:";
	
	public static final String REPORT_REGISTER_REQ_ID = "RReg:";
	public static final String REPORT_CREATE_REQ_ID = "RCre:";
	public static final String REPORT_CANCEL_REQ_ID = "RCan:";
	
	@Override
	public void registerReport(ReportRequest registerReport)
			throws DuplicateKeyException {
		reportRequestEAO.create(registerReport);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean cancelReport(String venId, List<String> reportRequestIds) {
		boolean status = true;
		try {
			for(String reportRequestId: reportRequestIds){
				ReportRequest reportRequest =null;
				try{
					reportRequest = reportRequestEAO.findByVenIdandReportRequestId(venId, reportRequestId);
				} catch (NoResultException e) {
					log.error("No report found for the given venid and reportrequestId");
				}catch(Exception e){
					log.error("Exception in !!!!"+e);
				}
				if(null == reportRequest){
					return false;
				}
				
				//Only one report will be returned
				for(Report report: reportRequest.getReport()){
					report.setCancelled(true);
				}
				reportRequestEAO.updateReportRequest(reportRequest);
			}
		} catch (Exception e) {
			log.error("Exception in cancelReport !! "+e);
			status = false;
		}
		return status;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean createdReport(String venId, List<String> reportRequestIds) {
		boolean status = true;
		try {
			for(String reportRequestId: reportRequestIds){
				VenReportRequestHistory venReportRequestHistory = null;
				try{
					 venReportRequestHistory = reportRequestEAO.findVenReportRequestHistoryByVenIdandReportRequestId(venId, reportRequestId);
				} catch (NoResultException e) {
					log.error("No reportRequests found for the given venid and reportrequestIds");
				}catch(Exception e){
					log.error("Exception in !!!!"+e);
				}
				if(null == venReportRequestHistory){
					return false;
				}
				
				venReportRequestHistory.setCreated(true);
				reportRequestEAO.updateVenReportRequestHistory(venReportRequestHistory);
				
			}
		} catch (Exception e) {
			log.error("Exception in createdReport !! "+e);
			status = false;
		}
		return status;
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ReportRequest findReportRequestByVenId(String venId){
		ReportRequest reportRequest =null;
		try {
			reportRequest = reportRequestEAO.findByVenId(venId);
			if(null !=reportRequest){
				for(Report report: reportRequest.getReport()){
					if(null != report){
						/*for(ReportDescription reportDescription: report.getReportDescription()){
							log.info("Size of reportDescription is :" + report.getReportDescription().size());
						}*/
					}
				}
			}
		}catch(EntityNotFoundException e){
			log.error("EntityNotFoundException in findReportRequestByVenId !! ");
		}catch (Exception e) {
			log.error("Exception in findReportRequestByVenId !! "+e);
		}
		return reportRequest;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteReportRequest(ReportRequest reportRequest){
		try {
			reportRequestEAO.deleteReportRequest(reportRequest);
		} catch (Exception e) {
			log.error("Exception in deleteReportRequest !! "+e);
		}
	}
	

//////////////////////////////////////////////////////////////////////////////////
// REPORT functions for Oadr Poll Service
//////////////////////////////////////////////////////////////////////////////////

	/**
	* getRegisterReportForVenId returns a Not-Null object
	* if the registration is not done.i.e, to ask for registration from VEN.
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public RegisterReport getRegisterReportForVenId(String venId,String schemaVersion){
		RegisterReport registerReport = new RegisterReport();
		registerReport.setRequestId(REQUEST_ID_PREFIX +REPORT_REGISTER_REQ_ID+ System.currentTimeMillis());
		registerReport.setVenId(venId);
		registerReport.setSchemaVersion(schemaVersion);
		
		ReportRequest confReportRequest = null;
		try {
			confReportRequest = reportRequestEAO.findByVenId(venId);
			if(confReportRequest!=null){
				//There is already an entry in the reportRequest table so return a null object.
				return null;
			}
		} catch (EntityNotFoundException e) {
			log.error("Requesting VEN for registering Reports !!! ");
			return registerReport;
		}catch (Exception e) {
			log.error("Exception in creating registerReport Request!!! "+e);
			return registerReport;
		}
		return null;
	}


	/**
	*Returns a Not-Null object when a valid record exists in the database
	*/
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public CreateReport getCreateReportForVenId(String venId,String schemaVersion){
		
		CreateReport createReport = new CreateReport();
		createReport.setRequestId(REQUEST_ID_PREFIX + REPORT_CREATE_REQ_ID+System.currentTimeMillis());
		createReport.setVenId(venId);
		createReport.setSchemaVersion(schemaVersion);
		List<com.honeywell.dras.vtn.api.report.ReportRequest> reportRequestList = new ArrayList<com.honeywell.dras.vtn.api.report.ReportRequest>();
				
		ReportRequest confReportRequest = null;		
		try {
			confReportRequest = reportRequestEAO.findCreateReportMessagesByVenId(venId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException in creating createReport Request!!! ");
			return null;
		} catch (NoResultException e) {
			log.error("NoResultException in creating createReport Request!!! ");
			return null;
		}catch (Exception e) {
			log.error("Exception in creating createReport Request!!! "+e);
			return null;
		}
		
		
		if(null != confReportRequest){
			for(Report confReport:confReportRequest.getReport()){
				//Extra condition to check if the hibernate query is sending extra report collection members
				if(!confReport.isToCreate())
					continue;
				
				log.info("ConfReport value : "+ confReport.getReportName());
				
				//Since the reportSpecifier cant add more than 1 specifier inside a reportRequest,
				//we need to create separate reportRequests for the multiple confReports
				com.honeywell.dras.vtn.api.report.ReportRequest reportRequest = new com.honeywell.dras.vtn.api.report.ReportRequest();
				
				ReportSpecifier reportSpecifier = new ReportSpecifier();
				
				//Report request is newly created for each request towards VEN.
				reportRequest.setReportRequestId(REPORT_REQUEST_ID_PREFIX + System.nanoTime());
				
				//Table for storing request transactional information
				try{
					VenReportRequestHistory oVenReportRequestHistory = new VenReportRequestHistory();
					oVenReportRequestHistory.setVenId(venId);
					oVenReportRequestHistory.setReportRequestId(reportRequest.getReportRequestId());
					oVenReportRequestHistory.setRequestId(createReport.getRequestId());
					reportRequestEAO.create(oVenReportRequestHistory);
				}catch(Exception e){
					log.error("Exception in storing createReport request transactional information !!!");
				}
				
				
				//Set the UI toCreate to false - to indicate VTN has send the request to VEN
				confReport.setToCreate(false);
				confReport.setCreated(true);
				confReport.setReportRequestId(reportRequest.getReportRequestId());
				
				List<SpecifierPayload> specifierPayloadList = new ArrayList<SpecifierPayload>();
				for(ReportDescription confReportDescription:confReport.getReportDescription()){
					//Extra condition to check if the hibernate query is sending extra report collection members
					if(!confReportDescription.isToCreate())
						continue;
					
					log.info("ConfReportDescription value : "+ confReportDescription.getrId());
					
					reportSpecifier.setGranularity(confReportDescription.getUiGranularity());
					reportSpecifier.setReportBackDuration(confReportDescription.getUiReportBackDuration());
				
					//TODO WSCalendar Interval - Only duration set.
					WsCalanderInterval reportInterval = new WsCalanderInterval();
					reportInterval.setDuration(confReportDescription.getUiDuration());
					if(null != confReportDescription.getUiDtStart()){
						reportInterval.setStart(confReportDescription.getUiDtStart());
					}else{
						reportInterval.setStart(new Date());
					}
					//reportInterval.setNotification(confReportDescription.getSamplingMaxPeriod());
					//reportInterval.setRampUp(confReportDescription.getSamplingMaxPeriod());
					//reportInterval.setRecovery(confReportDescription.getSamplingMaxPeriod());
					//reportInterval.setToleranceStartAfter(confReportDescription.getSamplingMaxPeriod());
					reportSpecifier.setReportInterval(reportInterval );
					
					reportSpecifier.setReportSpecifierId(confReport.getReportSpecifierId());
					
					SpecifierPayload specifierPayload = new SpecifierPayload();
					
					ItemBase itemBase = new ItemBase();
					if(confReportDescription!=null && confReportDescription.getItemBase()!=null){
						itemBase.setItemDescription(confReportDescription.getItemBase().getItemDescription());
						itemBase.setItemUnits(confReportDescription.getItemBase().getItemUnits());
						itemBase.setSiScaleCode(confReportDescription.getItemBase().getSiScaleCode());
					}
					
					//Set the UI toCreate to false - to indicate VTN has send the request to VEN
					confReportDescription.setToCreate(false);
					confReportDescription.setCreated(true);
					
					specifierPayload.setItemBase(itemBase);
					
					specifierPayload.setReadingType(confReportDescription.getReadingType());
					specifierPayload.setrId(confReportDescription.getrId());
					
					specifierPayloadList.add(specifierPayload);
				}
				reportSpecifier.setSpecifierPayloadList(specifierPayloadList);
				reportRequest.setReportSpecifier(reportSpecifier);
				reportRequestList.add(reportRequest);
			}
		
			createReport.setReportRequestList(reportRequestList );
			
			//Set the toCreate to false before sending message to VEN
			try{
				reportRequestEAO.updateReportRequest(confReportRequest);
			}catch(Exception e){
				log.error("Exception in updating the toCreate flag to false!!! "+e);
			}
			return createReport;
		}else{ 
			//No message for the venID so return null
			return null;
		}
	}

	/**
	*Returns a Not-Null object when a valid record exists in the database
	*/	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public CancelReport getCancelReportForVenId(String venId,String schemaVersion){
		CancelReport cancelReport = new CancelReport(); 
		cancelReport.setRequestId(REQUEST_ID_PREFIX + REPORT_CANCEL_REQ_ID+System.currentTimeMillis());
		cancelReport.setVenId(venId);
		cancelReport.setSchemaVersion(schemaVersion);
		
		ReportRequest confReportRequest = null;
		
		try {
			confReportRequest = reportRequestEAO.findCancelReportMessagesByVenId(venId);
		} catch (EntityNotFoundException e) {
			log.error("Exception in creating cancelReport Request!!! ");
			return null;
		}catch (NoResultException e) {
			log.error("NoResultException in creating cancelReport Request!!! ");
			return null;
		}catch (Exception e) {
			log.error("Exception in creating cancelReport Request!!! "+e);
			return null;
		}
		
		for(Report confReport:confReportRequest.getReport()){
			
			if(!confReport.isToCancel())
				continue;
			//Set the UI toCancel to false - to indicate VTN has send the request to VEN
			confReport.setToCancel(false);
			confReport.setCancelled(true);
			
			//Store the cancel Report request id in the initial CreateReport transaction
			//So when they return only the cancel request Id we can mark the original the createReport record as cancelled
			try{
				VenReportRequestHistory venReportRequestHistory = null;
				try{
					 venReportRequestHistory = reportRequestEAO.findVenReportRequestHistoryByVenIdandReportRequestId(venId, confReport.getReportRequestId());
				} catch (NoResultException e) {
					log.error("No reportRequests found for the given venid and reportrequestIds");
				}catch(Exception e){
					log.error("Exception in !!!!"+e);
				}
				
				venReportRequestHistory.setCancelRequestId(cancelReport.getRequestId());
				reportRequestEAO.updateVenReportRequestHistory(venReportRequestHistory);
			}catch(Exception e){
				log.error("Exception in storing createReport request transactional information !!!");
			}
			
			/*if(null != confReport.getReportDescription()){
				for(ReportDescription confReportDescription: confReport.getReportDescription()){
					if(!confReportDescription.isToCancel())
						continue;
					//Set the UI toCancel to false - to indicate VTN has send the request to VEN
					confReportDescription.setToCancel(false);
					confReportDescription.setCancelled(true);
				}
			}*/
			
			//TODO
			//Artificially creating a list for mapping to a list 
			//though it is not a list on the database side
			List<String> reportRequestIds = new ArrayList<String>();
			reportRequestIds.add(confReport.getReportRequestId());
			cancelReport.setReportRequestId(reportRequestIds);
			
		}		
		//Set the toCancel to false before sending message to VEN
		try{
			reportRequestEAO.updateReportRequest(confReportRequest);
		}catch(Exception e){
			log.error("Exception in updating the toCancel flag to false!!! "+e);
		}
		
		
		return cancelReport;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ReportRequest findCreatedReportsNotCancelledByVenId(String venId) {
		try {
			return reportRequestEAO.findCreatedReportsNotCancelledByVenId(venId);
		} catch (Exception e) {
			log.error("Exception in findCreatedReportsNotCancelledByVenId !! "+e);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ReportRequest findByVenIdandReportRequestId(String venId,
			String reportRequestId) {
		try {
			return reportRequestEAO.findByVenIdandReportRequestId(venId, reportRequestId);
		} catch (NoResultException e) {
			log.error("No report found for the given venid and reportrequestId");
		}catch(Exception e){
			log.error("Exception in !!!!"+e);
		}
		return null;
	}

	@Override
	public void updateReportRequest(ReportRequest reportRequest) {
		reportRequestEAO.updateReportRequest(reportRequest);
	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public VenReportRequestHistory findVenReportRequestHistoryByVenIdandReportRequestId(String venId,
			String reportRequestId) {
		try{
			 return reportRequestEAO.findVenReportRequestHistoryByVenIdandReportRequestId(venId, reportRequestId);
		} catch (NoResultException e) {
			log.error("No reportRequests found for the given venid and reportrequestId");
		}catch(Exception e){
			log.error("Exception in !!!!"+e);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Boolean UpdateCancelledStatusForVenReportRequestHistoryByVenIdandRequestId(String venId,
			String requestId) {
		boolean status = false;
		try{
			List<VenReportRequestHistory> venHistories = null;
			try{
				 venHistories = reportRequestEAO.findVenReportRequestHistoryByVenIdandRequestId(venId, requestId);
			} catch (NoResultException e) {
				log.error("No reportRequests found for the given venid and reportrequestIds");
			}catch(Exception e){
				log.error("Exception in !!!!"+e);
			}
			
			if(null != venHistories){
				for(VenReportRequestHistory venReportRequestHistory : venHistories){
					venReportRequestHistory.setCancelled(true);
					reportRequestEAO.updateVenReportRequestHistory(venReportRequestHistory);
				}
				//No exception and we are able to Update status for all the reportRequestIds
				status = true;
			}
			
			return status;
		}catch(Exception e){
			log.error("Exception in storing createReport request transactional information !!!");
		}
		return status;
	}

	public String findResourceIdfromReportDataSourceByrId(String rId,String reportSpecifierId,String venId) throws NoResultException, NonUniqueResultException{
		String resourceId = null;
		try{
			resourceId = reportRequestEAO.findResourceIdfromReportDataSourceByrId(rId,reportSpecifierId,venId);
		}catch(Exception e){
			log.error("Exception in finding resourceId from reportDataSource by rId !!! "+e);
		}
		return resourceId;
	}
	
}