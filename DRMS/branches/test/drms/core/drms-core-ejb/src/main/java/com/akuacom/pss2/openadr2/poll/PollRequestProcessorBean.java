package com.akuacom.pss2.openadr2.poll;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.Constants;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.event.EventRequestProcessor;
import com.akuacom.pss2.openadr2.party.PartyRequestProcessor;
import com.akuacom.pss2.openadr2.poll.eao.OadrPollStateManager;
import com.akuacom.pss2.openadr2.report.ReportRequestManager;
import com.akuacom.pss2.util.MemorySequence;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.event.RequestEvent;
import com.honeywell.dras.vtn.api.poll.Poll;
import com.honeywell.dras.vtn.api.poll.PollResponse;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.ReRegistration;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

@Stateless
public class PollRequestProcessorBean implements PollRequestProcessor.L ,PollRequestProcessor.R {
	
	
	private final String res_code_unknownVen = "463";
	private final String res_code_sucsess = "200";
	
	@EJB
	private OadrPollStateManager.L pollStateManager;
	
	@EJB
	EndpointManager.L endpointManager;
	
	@EJB
	private PartyRequestProcessor.L partyReqProcessor;
	
	@EJB
	private ReportRequestManager.L reportReqManager;
	
	
	@EJB
	private EventRequestProcessor.L eventRequestProcessor;

	
	private Logger log = Logger.getLogger(PollRequestProcessorBean.class);
	
	public PollResponse poll(Poll poll) throws VtnDrasServiceException{
		PollResponse pollResponse = null;
		try {
			log.info("Received poll from ven " + poll.getVenId());
			if(!isVenValid(poll.getVenId(), poll.getCertCommonName())){
				return generatePollResponseForInvalidVen(poll);
			}
			pollResponse = generatePollResponse(poll);
		} catch (Exception e) {
			log.error("Error in processing poll "+e.getMessage());
			throw new VtnDrasServiceException("Error in processing poll");
		}
		return pollResponse;
	}
	
	private PollResponse generatePollResponseForInvalidVen(Poll poll){
		PollResponse pollResponse = createDefaultPollResponse(poll) ;
		pollResponse.getResponse().setResponseCode(res_code_unknownVen);
		pollResponse.getResponse().setResponseDescription("Invalid Ven Received");
		pollResponse.getResponse().setRequestId(Constants.REQUEST_ID_PREFIX + "ERROR_INVALID_VEN");
		pollResponse.getResponse().setSchemaVersion(poll.getSchemaVersion());
		pollResponse.getResponse().setVenId(poll.getVenId());
		pollResponse.setRequestId(pollResponse.getResponse().getRequestId());
		pollResponse.setRequestId(poll.getRequestId());
		pollResponse.setSchemaVersion(poll.getSchemaVersion());
		pollResponse.setVenId(poll.getVenId());
		pollResponse.setCertCommonName(poll.getCertCommonName());
		return pollResponse;
	}
	private PollResponse generatePollResponse(Poll poll) throws VtnDrasServiceException{
		
		PollResponse pollResponse = null;
		if(checkPendingPollResponse(poll.getVenId())){
			pollResponse = getPendingPollResponse(poll);
			if(null == pollResponse){
				pollResponse = generatePollResponseForDistributeEvent(poll);
			}
			return pollResponse;
		}
		return generatePollResponseForReoccurringPoll(poll);
	}
	private PollResponse generatePollResponseForReoccurringPoll(Poll poll) throws VtnDrasServiceException{
		PollResponse pollResponse = null;
		if(pollStateManager.isSendDistributeEvent(poll.getVenId())){
			pollResponse = generatePollResponseForDistributeEvent(poll);
		}
		else if(pollStateManager.isSendResponse(poll.getVenId())){
			pollResponse = generatePollResponseForFinalResponse(poll);
		}
		return pollResponse;
	}
	private PollResponse getPendingPollResponse(Poll poll){
		PollResponse pollResponse = null;
		pollResponse = getPendingPollResponseForVen(poll);
		if(null == pollResponse){
			pollResponse = getPendingPollResponseForReport(poll);
		}
		return pollResponse;
	}
	private PollResponse getPendingPollResponseForVen(Poll poll){
		String venId = poll.getVenId();
		PollResponse pollResponse = null;
		if(pollStateManager.isSendCancelVenRegistration(venId)){
			pollResponse = generatePollResponseForCancelVenRegistration(poll);
		}
		else if(pollStateManager.isSendReregisterVen(venId)){
			pollResponse = generatePollResponseForVenReregistration(poll);
		}
		return pollResponse;
	}
	private PollResponse getPendingPollResponseForReport(Poll poll){
		String venId = poll.getVenId();
		PollResponse pollResponse = null;
		if(pollStateManager.isSendRegisterReport(venId)){
			pollResponse = generatePollResponseForReportRegistration(poll);
		}
		else if(pollStateManager.isSendCreateReport(venId)){
			pollResponse = generatePollResponseForReportCreation(poll);
		}
		else if(pollStateManager.isSendCancelReport(venId)){
			pollResponse = generatePollResponseForCancelReport(poll);
		}
		return pollResponse;
	}
	private PollResponse generatePollResponseForVenReregistration(Poll poll){
		PollResponse pollRes = new PollResponse();
		pollRes.setReRegistration(createReregistrationObj(poll));
		
		updatePollStateForReregisterResponseSent(poll.getVenId());
		
		return pollRes;
	}
	private ReRegistration createReregistrationObj(Poll poll){
		ReRegistration reRegister = new ReRegistration();
		reRegister.setCertCommonName(poll.getCertCommonName());
		reRegister.setFingerprint(poll.getFingerprint());
		reRegister.setRequestId(poll.getRequestId());
		reRegister.setSchemaVersion(poll.getSchemaVersion());
		reRegister.setVenId(poll.getVenId());
		return reRegister;
	}
	private PollResponse generatePollResponseForCancelVenRegistration(Poll poll){
		PollResponse pollRes = new PollResponse();
		pollRes.setCancelPartyRegistration(createCancelVenRegistrationObj(poll));
		pollRes.setRequestId(poll.getRequestId());
		updatePollStateForCancelVenRegistrationResponseSent(poll.getVenId());
		return pollRes;
	}
	private CancelPartyRegistration createCancelVenRegistrationObj(Poll poll){
		CancelPartyRegistration cancelPartyRegistration = new CancelPartyRegistration();
		cancelPartyRegistration.setCertCommonName(poll.getCertCommonName());
		cancelPartyRegistration.setFingerprint(poll.getFingerprint());
		cancelPartyRegistration.setRequestId(generateRequestId(poll.getRequestId()));
		cancelPartyRegistration.setSchemaVersion(poll.getSchemaVersion());
		cancelPartyRegistration.setRegistrationId(getRegistrationId(poll.getVenId()));
		cancelPartyRegistration.setVenId(poll.getVenId());
		return cancelPartyRegistration;
	}
	private PollResponse generatePollResponseForReportRegistration(Poll poll){
		PollResponse pollRes = new PollResponse();
		pollRes.setRegisterReport(createRegisterReportObj(poll));
		updatePollStateForRegisterReportResponseSent(poll.getVenId());
		return pollRes;
	}
	private RegisterReport createRegisterReportObj(Poll poll){
		RegisterReport registerReport = new RegisterReport();
		registerReport.setCertCommonName(poll.getCertCommonName());
		registerReport.setFingerprint(poll.getFingerprint());
		registerReport.setRequestId(generateRequestId(poll.getRequestId()));
		registerReport.setSchemaVersion(poll.getSchemaVersion());
		registerReport.setVenId(poll.getVenId());
		return reportReqManager.getRegisterReportForVenId(poll.getVenId(),poll.getSchemaVersion());
		//return registerReport;
	}
	private PollResponse generatePollResponseForReportCreation(Poll poll){
		PollResponse pollRes = new PollResponse();
		pollRes.setCreateReport(createCreateReportObj(poll));
		setCreateReportResponseSent(poll.getVenId());
		return pollRes;
	}
	private CreateReport createCreateReportObj(Poll poll){
		CreateReport createReport = new CreateReport();
		createReport.setCertCommonName(poll.getCertCommonName());
		createReport.setFingerprint(poll.getFingerprint());
		createReport.setRequestId(poll.getRequestId());
		createReport.setSchemaVersion(poll.getSchemaVersion());
		createReport.setVenId(poll.getVenId());
		return reportReqManager.getCreateReportForVenId(poll.getVenId(),poll.getSchemaVersion());
		//return createReport;
	}
	
	private PollResponse generatePollResponseForCancelReport(Poll poll){
		PollResponse pollRes = new PollResponse();
		pollRes.setCancelReport(createCancelReportObj(poll));
		updataPollStateForCancelReportResponseSent(poll.getVenId());
		return pollRes;
	}
	private CancelReport createCancelReportObj(Poll poll){
		CancelReport cancelReport = new CancelReport();
		cancelReport.setCertCommonName(poll.getCertCommonName());
		cancelReport.setFingerprint(poll.getFingerprint());
		cancelReport.setRequestId(poll.getRequestId());
		cancelReport.setSchemaVersion(poll.getSchemaVersion());
		cancelReport.setVenId(poll.getVenId());
		return reportReqManager.getCancelReportForVenId(poll.getVenId(),poll.getSchemaVersion());
		//return cancelReport;
	}
	private PollResponse generatePollResponseForDistributeEvent(Poll poll) throws VtnDrasServiceException{
		
		PollResponse pollRes = new PollResponse();
		pollRes.setDistributeEvent(createDistributeEventObj(poll));
		updatePollStateForDistributeEventSent(poll.getVenId());
		return pollRes;
	}
	private DistributeEvent createDistributeEventObj(Poll poll) throws VtnDrasServiceException{
		String requestId = generateRequestId(poll.getRequestId());
		DistributeEvent distributeEvent = new DistributeEvent();
		distributeEvent.setCertCommonName(poll.getCertCommonName());
		distributeEvent.setFingerprint(poll.getFingerprint());
		distributeEvent.setRequestId(requestId);
		distributeEvent.setSchemaVersion(poll.getSchemaVersion());
		distributeEvent.setVenId(poll.getVenId());
		
		Response response = new Response();
		response.setResponseCode("200");
		response.setResponseDescription("Success");
		response.setCertCommonName(poll.getCertCommonName());
		response.setFingerprint(poll.getFingerprint());
		response.setRequestId(requestId);
		//response.setResponseCode(poll.getRes)
		response.setSchemaVersion(poll.getSchemaVersion());
		response.setVenId(poll.getVenId());
		
		distributeEvent.setResponse(response);
		
		return getDistributeEvent(poll);
		//return distributeEvent;
	}
	private PollResponse generatePollResponseForFinalResponse(Poll poll){
		PollResponse pollRes = createDefaultPollResponse(poll);
		pollRes.getResponse().setResponseCode(res_code_sucsess);
		
		updatePollStateForFinalResponseSent(poll.getVenId());
		return pollRes;
	}
	private void updatePollStateForReregisterResponseSent(String venId){
		pollStateManager.setSendReregisterVen(venId, false);
		pollStateManager.setReregisterVenSent(venId, true);
		pollStateManager.setSendDistributeEvent(venId, true);
	}
	private void updatePollStateForCancelVenRegistrationResponseSent(String venId){
		pollStateManager.setSendCancelVenRegistration(venId, false);
		pollStateManager.setCancelVenRegistrationSent(venId, true);
		pollStateManager.setSendDistributeEvent(venId, true);
		
		
	}
	private void updataPollStateForCancelReportResponseSent(String venId){
		pollStateManager.setSendCancelReport(venId, false);
		pollStateManager.setCancelReportSent(venId, true);
		pollStateManager.setSendDistributeEvent(venId, true);
	}
	private void updatePollStateForRegisterReportResponseSent(String venId){
		pollStateManager.setSendRegisterReport(venId, false);
		pollStateManager.setReportRegisterSent(venId, true);
		pollStateManager.setSendDistributeEvent(venId, true);
	}
	private void setCreateReportResponseSent(String venId){
		pollStateManager.setSendCreateReport(venId, false);
		pollStateManager.setCreateReportSent(venId,true);
		pollStateManager.setSendDistributeEvent(venId, true);
	}
	private void updatePollStateForDistributeEventSent(String venId){
		pollStateManager.setSendDistributeEvent(venId, false);
		pollStateManager.setDistributeEventSent(venId, true);
		pollStateManager.setSendResponse(venId, true);
		
	}
	private void updatePollStateForFinalResponseSent(String venId){
		pollStateManager.setSendResponse(venId,false);
		pollStateManager.setResponseSent(venId, true);
	}
	
	private boolean checkPendingPollResponse(String venId){
		boolean sendDirstributeEvent = pollStateManager.isSendDistributeEvent(venId);
		boolean sendResponse = pollStateManager.isSendResponse(venId);
		boolean checkForPendingResponse = (! sendDirstributeEvent && !sendResponse);
		return checkForPendingResponse;
	}
		
	private PollResponse createDefaultPollResponse(Poll poll){
		PollResponse pollResponse = new PollResponse();
		Response response = new Response();
		response.setCertCommonName(poll.getCertCommonName());
		response.setFingerprint(poll.getFingerprint());
		response.setRequestId(generateRequestId(poll.getRequestId()));
		response.setResponseCode("");
		response.setResponseDescription("");
		response.setSchemaVersion(poll.getSchemaVersion());
		response.setVenId(poll.getVenId());
		pollResponse.setResponse(response);
		return pollResponse;
	}
	
	private boolean isVenValid(String venId, String certCommonName){
//		System.out.println("DEBUG ------ >    "+venId +" "+" "+"Cert CommonName = "+certCommonName);
//		boolean isVenIdInValid = ((null == venId) ||(venId.equals("")));
//		boolean isCertInValid = ((null == certCommonName) ||(certCommonName.equals("")));
//		if(isVenIdInValid || isCertInValid){
//			return false;
//		}
		return partyReqProcessor.isVenValid(venId, certCommonName);
		
		
	}
	private DistributeEvent getDistributeEvent(Poll poll) throws VtnDrasServiceException{
		RequestEvent requestEvent = new RequestEvent();
		requestEvent.setCertCommonName(poll.getCertCommonName());
		requestEvent.setFingerprint(poll.getFingerprint());
		requestEvent.setReplyLimit(0L);
		requestEvent.setRequestId(poll.getRequestId());
		requestEvent.setSchemaVersion(poll.getSchemaVersion());
		requestEvent.setVenId(poll.getVenId());
		return eventRequestProcessor.requestEvent(requestEvent);
	}
	private String generateRequestId(String requestIdFromPollRequest){
		String requestId = requestIdFromPollRequest;
		if(null == requestId || requestId.equals("")){
			requestId = Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		}
		return requestId;
	}
	
	private String getRegistrationId(String venId){
		String registrationId="" ;
		Endpoint endPoint = null;
		try {
			endPoint = endpointManager.findByVenId(venId);
			if(endPoint != null){
				registrationId = endPoint.getRegistrationId();
			}
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
		}
		
		
		return registrationId;
	}
}
