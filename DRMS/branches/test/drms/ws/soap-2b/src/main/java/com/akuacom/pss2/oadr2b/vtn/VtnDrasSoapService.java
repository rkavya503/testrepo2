package com.akuacom.pss2.oadr2b.vtn;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.jws.WebService;

import org.jboss.logging.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.openadr2.event.EventRequestProcessor;
import com.akuacom.pss2.openadr2.opt.OptSoapServiceManager;
import com.akuacom.pss2.openadr2.party.PartyRequestProcessor;
import com.akuacom.pss2.openadr2.poll.PollRequestProcessor;
import com.akuacom.pss2.openadr2.poll.eao.OadrPollStateManager;
import com.akuacom.pss2.openadr2.report.DataSetDefEnums.DataSetOwnerType;
import com.akuacom.pss2.openadr2.report.PrecisionParameters;
import com.akuacom.pss2.openadr2.report.ReportHelper;
import com.akuacom.pss2.openadr2.report.ReportRequest;
import com.akuacom.pss2.openadr2.report.ReportRequestManager;
import com.akuacom.pss2.openadr2.report.TelemetryDataBusData;
import com.akuacom.pss2.openadr2.report.TelemetryProcessor;
import com.akuacom.pss2.openadr2.report.VenReportRequestHistory;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.honeywell.dras.vtn.api.common.FingerPrint;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.event.RequestEvent;
import com.honeywell.dras.vtn.api.opt.CancelOpt;
import com.honeywell.dras.vtn.api.opt.CanceledOpt;
import com.honeywell.dras.vtn.api.opt.CreateOpt;
import com.honeywell.dras.vtn.api.opt.CreatedOpt;
import com.honeywell.dras.vtn.api.poll.Poll;
import com.honeywell.dras.vtn.api.poll.PollResponse;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatedPartyRegistration;
import com.honeywell.dras.vtn.api.registration.QueryRegistration;
import com.honeywell.dras.vtn.api.registration.Result;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CanceledReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.CreatedReport;
import com.honeywell.dras.vtn.api.report.Payload;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.Report;
import com.honeywell.dras.vtn.api.report.ReportInterval;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdatedReport;
import com.honeywell.dras.vtn.dras.service.VtnDrasService;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;


@WebService(serviceName = "VtnDrasService", endpointInterface = "com.honeywell.dras.vtn.dras.service.VtnDrasService", targetNamespace = "http://dras.honeywell.com/services/api/ports/VtnDrasService")
public class VtnDrasSoapService implements VtnDrasService{

	@EJB
	private PartyRequestProcessor.L partyRequestProcessor;
	@EJB
	private EventRequestProcessor.L eventRequestProcessor;
	@EJB
	private PollRequestProcessor.L pollRequestProcessor;
	@EJB
	private OptSoapServiceManager.L optRequestProcessorManager;
	@EJB
	private OadrPollStateManager.L  pollStateManager;	
	@EJB
	private ReportRequestManager.L reportRequestManager;	
	@EJB
	private TelemetryProcessor.L telemetryProcessor;
	@EJB
	private CorePropertyEAO.L corePropertyEAO;
	
	private Logger log = Logger.getLogger(VtnDrasSoapService.class);
	
	@Override
	public CreatedPartyRegistration queryRegistration(
			QueryRegistration queryRegistration) throws VtnDrasServiceException {
		return partyRequestProcessor.queryRegistration(queryRegistration);
	}
	@Override
	public CreatedPartyRegistration createPartyRegistration(
			CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException {
				if(partyRequestProcessor == null)
			log.debug("DEUBG KM ------- > partyRequestProcessor is null ");
		
		return partyRequestProcessor.createPartyRegistration(createPartyRegistration);
	}
	@Override
	public CanceledPartyRegistration cancelPartyRegistration(
			CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException {
				return partyRequestProcessor.cancelPartyRegistration(cancelPartyRegistration);
	}
	@Override
	public Response canceledPartyRegistration(
			CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException {
		return partyRequestProcessor.canceledPartyRegistration(canceledPartyRegistration);
	}
	@Override
	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException {
		return partyRequestProcessor.registerFingerPrint(fingerPrint);
	}
	@Override
	public CreatedOpt createOpt(CreateOpt createOpt)
			throws VtnDrasServiceException {
		return optRequestProcessorManager.createOpt(createOpt);
	}
	@Override
	public CanceledOpt cancelOpt(CancelOpt cancelOpt)
			throws VtnDrasServiceException {
		return optRequestProcessorManager.cancelOpt(cancelOpt);
	}
	
	@Override
	public RegisteredReport registerReport(RegisterReport registerReport)
			throws VtnDrasServiceException {
		
		if(!partyRequestProcessor.isVenValid(registerReport.getVenId(), registerReport.getCertCommonName())){
			return ReportHelper.createInvalidVenRegisteredReportResponse(registerReport);
		}
		RegisteredReport registeredReport = new RegisteredReport();		
		
		try{
			ReportRequest reportRequest = new ReportRequest();
			reportRequest.setCreationTime(new Date());
			reportRequest.setCreator(null);
			reportRequest.setRequestId(registerReport.getRequestId());
			reportRequest.setVenId(registerReport.getVenId());
			
			//Check whether the VenID already exists. Incase the report information exists
			//the new metadata should supplant the earlier the metadata as per the OpenADR spec.
			try{
				ReportRequest findReportRequestByVenId = reportRequestManager.findReportRequestByVenId(registerReport.getVenId());
				if(findReportRequestByVenId != null){
					//A record already exists. So delete the existing info and recreate with new metadata.
					reportRequestManager.deleteReportRequest(findReportRequestByVenId);
				}
			}catch(Exception e){
				log.error("Exception in deleting information about the existing VEN report registration !!! "+e);
			}
			List<Report> reportList = registerReport.getReportList();
			
			Set<com.akuacom.pss2.openadr2.report.Report> reports = null;
			
			if(reportList!=null){	
				//extract report Payload object				
				HashMap<String, Long> props = getRegisterReportProperty();		
				reports = ReportHelper.extractRegisterReportPayload(reportList, props);
			}
			
			reportRequest.setReport(reports);
		
			try {
				reportRequestManager.registerReport(reportRequest);
			} catch (DuplicateKeyException e) {
				log.error("Duplicate Key Exception in registering report." +e);
				registeredReport.setRequestId(registerReport.getRequestId());
				registeredReport.setResponse(ReportHelper.initializeErrorResponse(registerReport.getRequestId()));
				return registeredReport;
			}
		
		}catch(Exception e){
			log.error("Exception in registering report." +e);
			registeredReport.setRequestId(registerReport.getRequestId());
			registeredReport.setResponse(ReportHelper.initializeErrorResponse(registerReport.getRequestId()));
			return registeredReport;
		}
		
		try{
			CoreProperty sendCreateReport = corePropertyEAO.getByPropertyName("openadr.disablereportregistration");
			
			if(sendCreateReport.getStringValue().equalsIgnoreCase("true")){
				pollStateManager.setSendCreateReport(registerReport.getVenId(),true);
			}
			else{
				pollStateManager.setSendCreateReport(registerReport.getVenId(),false);
			}
			
		}catch(EntityNotFoundException e){
			HandleRegisterReportException(pollStateManager,registerReport.getVenId());
		}
		catch(Exception e){
		
			log.error("Exception in setting RegisterReport flag in PollState !!! "+e);
		}		
		
		return ReportHelper.createRegisteredReportResponse(registerReport);			
	}
	
	public void HandleRegisterReportException(OadrPollStateManager pollStateManager, String venId){
		try{
			pollStateManager.setSendCreateReport(venId,true);
		}
		catch(Exception e){
			log.error("Exception in setting RegisterReport flag in PollState !!! "+e);
		}
	}
	
	private HashMap<String, Long> getRegisterReportProperty() {
		HashMap<String, Long> props = new HashMap<String, Long>();
		try {
			Long defaultGranularity = (long) (900 * 1000);
			Long defaultReportBackDuration = (long) (900 * 1000);
			
			Long defaultStatusGranularityRportbackduration = (long) (60*1000);
			
			props.put("openadr.granularity", defaultGranularity);
			props.put("openadr.reportbackduration", defaultReportBackDuration);
			
			props.put("openadr.statusgranularityreportbackduration", defaultStatusGranularityRportbackduration);
			
			CoreProperty granularity = corePropertyEAO.getByPropertyName("openadr.granularity");
			CoreProperty report_back_duration = corePropertyEAO.getByPropertyName("openadr.reportbackduration");
			
			CoreProperty statusGranularityReport_back_duration = corePropertyEAO.getByPropertyName("openadr.statusgranularityreportbackduration");
			
			if(granularity != null && granularity.getDoubleValue() != null) {
				if(granularity.getDoubleValue() > 0) {
					props.put("openadr.granularity", granularity.getDoubleValue().longValue() * 1000);
				}
			}			
			if(report_back_duration != null && report_back_duration.getDoubleValue() != null){
				if(report_back_duration.getDoubleValue() > 0) {
					props.put("openadr.reportbackduration", report_back_duration.getDoubleValue().longValue() * 1000);
				}
			}	
			if(statusGranularityReport_back_duration != null && statusGranularityReport_back_duration.getDoubleValue() != null){
				if(statusGranularityReport_back_duration.getDoubleValue() > 0) {
					props.put("openadr.statusgranularityreportbackduration", statusGranularityReport_back_duration.getDoubleValue().longValue() * 1000);
				}
			}	
		} catch (EntityNotFoundException e1) {
			log.error("Exception in getting granularity and reportbackduration !!! "+e1);
			return props;
		}
		return props;
	}
	
	@Override
	public Response registeredReport(RegisteredReport registeredReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(registeredReport.getVenId(), registeredReport.getCertCommonName())){
			return ReportHelper.initializeInvalidVenResponse(registeredReport.getRequestId());	
		}		
		return ReportHelper.initializeResponse(registeredReport.getRequestId(),registeredReport.getVenId(),registeredReport.getSchemaVersion());
	}
	
	@Override
	public CreatedReport createReport(CreateReport createReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(createReport.getVenId(), createReport.getCertCommonName())){
			ReportHelper.createInvalidVenCreatedReportResponse(createReport);
		}
		return ReportHelper.createCreatedReportResponse(createReport);
	}
	
	@Override
	public Response createdReport(CreatedReport createdReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(createdReport.getVenId(), createdReport.getCertCommonName())){
			return (ReportHelper.initializeInvalidVenResponse(createdReport.getRequestId()));
		}
		boolean createdStatus = false;
		try{
			if(createdReport!=null && createdReport.getPendingReports()!=null){
				createdStatus= reportRequestManager.createdReport(createdReport.getVenId(), createdReport.getPendingReports().getReportRequestIdList());
			}
		}catch(Exception e){
			log.error("Exception in cancelReport !! "+e);
		}
		if(false == createdStatus){
			return ReportHelper.initializeErrorResponse(createdReport.getRequestId());
		}
		return ReportHelper.initializeResponse(createdReport.getRequestId(),createdReport.getVenId(),createdReport.getSchemaVersion());		
	}
	
	@Override
	public UpdatedReport updateReport(UpdateReport updateReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(updateReport.getVenId(), updateReport.getCertCommonName())){
			return ReportHelper.createInvalidVenUpdatedReportResponse(updateReport);
		}
		
		UpdatedReport updatedReport = new UpdatedReport();
		List<Report> reportList = updateReport.getReportList();
		
//		List<com.akuacom.pss2.openadr2.report.Report> reports = new ArrayList<com.akuacom.pss2.openadr2.report.Report>();
		try{
			if(reportList!=null){
				for(Report report:reportList){
//					com.akuacom.pss2.openadr2.report.Report configureReport = new com.akuacom.pss2.openadr2.report.Report();
					
					//Report Parameters
//					configureReport = ReportHelper.configureBasicReportParameters(configureReport, report);
					if(null != updateReport.getSchemaVersion()){
						if(!updateReport.getSchemaVersion().equalsIgnoreCase("OBIX")){
							VenReportRequestHistory venReportRequestHistory = null;
							venReportRequestHistory = reportRequestManager.findVenReportRequestHistoryByVenIdandReportRequestId(updateReport.getVenId(), report.getReportRequestId());
							if(null == venReportRequestHistory){
								log.error("Invalid venId or reportRequestId. Sending back error code 452 ");
								return ReportHelper.createInvalidReportRequestIdUpdatedReportResponse(updateReport);
							}
						}
					}
					
					long startTime = 0;
					
					if(null != report.getStart())
						startTime = report.getStart().getTime();
					
					//Interval parameters
					List<ReportInterval> intervalList = report.getIntervalList();
					for(ReportInterval interval : intervalList){
						//Time calculation for telemetry
						long totalTime = 0;
						
						//Overriding the report Start time with the Interval start time if available
						if(null != interval.getStart())
							startTime = interval.getStart().getTime();
						if(null != interval.getDuration()){
							totalTime = startTime + interval.getDuration();
						}else{
							totalTime = startTime;
						}
						Date entryTime = new Date(totalTime);
						
						Set<String> ridList=  new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
						for(Payload payload :interval.getPayloadList()){
							ridList.add(payload.getrId());
						}
						if(null != interval.getPayloadList()){
							for(Payload payload :interval.getPayloadList()){
								TelemetryDataBusData data = new TelemetryDataBusData();
								data.setOwnerType(DataSetOwnerType.RESOURCE);
								//TODO
								//Values to be filled into the telemetry data entry table
								PrecisionParameters precisionParameters = new PrecisionParameters();
								precisionParameters.setAccuracy(payload.getAccuracy());
								precisionParameters.setConfidence(payload.getConfidence());
								precisionParameters.setDataQuality(payload.getDataQuality());
								data.setPrecisionParameters(precisionParameters);
								
								String resourceId = null;
								
								if (updateReport.getSchemaVersion().equalsIgnoreCase("OBIX")) {
									resourceId = payload.getrId();
								} else {
									try{
										resourceId = reportRequestManager.findResourceIdfromReportDataSourceByrId(payload.getrId(),report.getReportSpecifierId(),updateReport.getVenId());
										log.info("ResourceId is : "+resourceId);
									}catch(Exception e){
										log.error("Exception in finding resourceId from reportDataSource by rId !!! "+e);
									}
									if(null == resourceId){
										log.error("Unable to find the resourceId for the rId !!!!!!!!!!!!!!!!!!!");
										continue;
									}
								}
								
								
								data.setOwnerEntityID(updateReport.getVenId());
								data.setEntryTime(entryTime);
								final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
						                .getBean(com.akuacom.pss2.system.SystemManager.class);
								String baselineUsageEquivalentValue = systemManager.getPss2Features().getBaselineUsageEquivalentValue();
								if(!ridList.contains(baselineUsageEquivalentValue)){
									telemetryProcessor.resolvePayload(payload, data);
								}else{
										telemetryProcessor.resolveMultipleDataPayload(payload, data);
								}
							}
						}
					}
					//reports.add(configureReport);
				}
			}
		}catch(Exception e){
			log.error("Exception in updateReport !! "+e);
			updatedReport.setRequestId(updateReport.getRequestId());
			updatedReport.setVenId(updateReport.getVenId());
			updatedReport.setResponse(ReportHelper.initializeErrorResponse(updateReport.getRequestId()) );
			return updatedReport;
		}		
		//No errors
		
		return ReportHelper.createUpdatedReportResponse(updateReport);
	}
	
	@Override
	public Response updatedReport(UpdatedReport updatedReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(updatedReport.getVenId(), updatedReport.getCertCommonName())){
			return ReportHelper.initializeInvalidVenResponse(updatedReport.getRequestId());
		}
		return ReportHelper.initializeResponse(updatedReport.getRequestId(), updatedReport.getVenId(),updatedReport.getSchemaVersion());
	}
	
	@Override
	public CanceledReport cancelReport(CancelReport cancelReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(cancelReport.getVenId(), cancelReport.getCertCommonName())){
			return ReportHelper.createInvalidVenCanceledReportResponse(cancelReport);
		}
		
		CanceledReport canceledReport = new CanceledReport();
		
		boolean cancelStatus = false;
		try{
			cancelStatus= reportRequestManager.UpdateCancelledStatusForVenReportRequestHistoryByVenIdandRequestId(cancelReport.getVenId(), cancelReport.getRequestId());
		}catch(Exception e){
			log.error("Exception in cancelReport !! "+e);
		}
		if(false == cancelStatus){
			canceledReport.setResponse(ReportHelper.initializeErrorResponse(cancelReport.getRequestId()));
			return canceledReport;
		}
		return ReportHelper.createCanceledReportResponse(cancelReport);		
	}
	
	@Override
	public Response canceledReport(CanceledReport canceledReport)
			throws VtnDrasServiceException {
		if(!partyRequestProcessor.isVenValid(canceledReport.getVenId(), canceledReport.getCertCommonName())){
			return ReportHelper.initializeInvalidVenResponse(canceledReport.getRequestId());
		}
		
		//TODO The cancelled flag status set for report and reportDescription when we send the cancelReport message to the ven
		//So we need not do anything here.
		
		return ReportHelper.initializeResponse(canceledReport.getRequestId(),canceledReport.getVenId(),canceledReport.getSchemaVersion());
	}
	@Override
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException {
		return eventRequestProcessor.requestEvent(requestEvent);
	}
	@Override
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException {
		return eventRequestProcessor.createdEvent(createdEvent);
	}
	@Override
	public PollResponse poll(Poll poll) throws VtnDrasServiceException {
		if(pollRequestProcessor == null)
			log.debug(" POLL Request Processor is null ");
		return pollRequestProcessor.poll(poll);
	}
}
