package com.akuacom.pss2.openadr2;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.openadr2.opt.OptSoapServiceManager;
import com.akuacom.pss2.openadr2.party.PartyRequestProcessor;
import com.akuacom.pss2.openadr2.poll.PollRequestProcessor;
import com.akuacom.pss2.openadr2.poll.eao.OadrPollStateManager;
import com.akuacom.pss2.util.MemorySequence;
import com.honeywell.dras.vtn.api.CreatedEvent;
import com.honeywell.dras.vtn.api.DistributeEvent;
import com.honeywell.dras.vtn.api.common.FingerPrint;
import com.honeywell.dras.vtn.api.common.Response;
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
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdatedReport;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;


@Stateless
public class VtnDrasManagerBean implements VtnDrasManager.R, VtnDrasManager.L {
	
	@EJB
	private PartyRequestProcessor.L partyRequestProcessor;
	
	@EJB
	private OptSoapServiceManager.L optSoapServiceManager;
	
	@EJB
	private PollRequestProcessor.L pollRequestProcessor;
	@EJB
	private OadrPollStateManager.L pollStateManager;
	
	@Override
	public CreatedPartyRegistration queryRegistration(QueryRegistration request)
			throws VtnDrasServiceException {		
		return partyRequestProcessor.queryRegistration(request);
	}

	@Override
	public CreatedPartyRegistration createPartyRegistration(CreatePartyRegistration createPartyRegistration) throws VtnDrasServiceException {
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
		return optSoapServiceManager.createOpt(createOpt);
	}

	@Override
	public CanceledOpt cancelOpt(CancelOpt cancelOpt)
			throws VtnDrasServiceException {
		return optSoapServiceManager.cancelOpt(cancelOpt);
	}

	@Override
	public RegisteredReport registerReport(RegisterReport registerReport)
			throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service register report ");
		//String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		Response response = new Response();
		response.setRequestId(registerReport.getRequestId());
		response.setResponseCode("200");
		response.setResponseDescription("success");
		RegisteredReport registeredReport = new RegisteredReport();
		registeredReport.setCertCommonName(registerReport.getCertCommonName());
		registeredReport.setFingerprint(registerReport.getFingerprint());
		registeredReport.setRequestId(registerReport.getRequestId());
		registeredReport.setResponse(response);
		registeredReport.setSchemaVersion(registerReport.getSchemaVersion());
		registeredReport.setVenId(registerReport.getVenId());
		
		pollStateManager.setSendCreateReport(registerReport.getVenId(), true);
		
		return registeredReport;
	}

	@Override
	public Response registeredReport(
			RegisteredReport registeredReport) throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service registered report ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(registeredReport.getVenId(), registeredReport.getCertCommonName())){
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			return response;
		}
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(registeredReport.getVenId());
		response.setSchemaVersion(registeredReport.getSchemaVersion());
		return response;
	}

	@Override
	public CreatedReport createReport(CreateReport createReport)
			throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service createReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(createReport.getVenId(), createReport.getCertCommonName())){
			CreatedReport createdReport = new CreatedReport();
			createdReport.setVenId(createReport.getVenId());
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			createdReport.setResponse(response);
			createdReport.setRequestId(createReport.getRequestId());
			return createdReport;
		}
		CreatedReport createdReport = new CreatedReport();
		createdReport.setVenId(createReport.getVenId());
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(createReport.getVenId());
		response.setSchemaVersion(createReport.getSchemaVersion());
		createdReport.setResponse(response);
		createdReport.setRequestId(createReport.getRequestId());
		return createdReport;
	}

	@Override
	public Response createdReport(
			CreatedReport createdReport) throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service crreatedReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(createdReport.getVenId(), createdReport.getCertCommonName())){
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			return response;
		}
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(createdReport.getVenId());
		response.setSchemaVersion(createdReport.getRequestId());
		return response;
	}

	@Override
	public UpdatedReport updateReport(UpdateReport updateReport)
			throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service updateReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(updateReport.getVenId(), updateReport.getCertCommonName())){
			UpdatedReport updatedReport = new UpdatedReport();
			updatedReport.setVenId(updateReport.getVenId());
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			updatedReport.setResponse(response);
			updatedReport.setRequestId(updateReport.getRequestId());
			updatedReport.setSchemaVersion(updateReport.getSchemaVersion());
			return updatedReport;
		}
		UpdatedReport updatedReport = new UpdatedReport();
		updatedReport.setVenId(updateReport.getVenId());
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(updateReport.getVenId());
		response.setSchemaVersion(updateReport.getSchemaVersion());
		updatedReport.setResponse(response);
		updatedReport.setRequestId(updateReport.getRequestId());
		updatedReport.setSchemaVersion(updateReport.getSchemaVersion());
		return updatedReport;
	}

	@Override
	public Response updatedReport(
			UpdatedReport updatedReport) throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service updatedReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(updatedReport.getVenId(), updatedReport.getCertCommonName())){
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			return response;
		}
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(updatedReport.getVenId());
		response.setSchemaVersion(updatedReport.getSchemaVersion());
		return response;
	}

	@Override
	public CanceledReport cancelReport(CancelReport cancelReport)
			throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service cancelReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(cancelReport.getVenId(), cancelReport.getCertCommonName())){
			CanceledReport canceledReport = new CanceledReport();
			canceledReport.setVenId(cancelReport.getVenId());
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			canceledReport.setResponse(response);
			canceledReport.setRequestId(cancelReport.getRequestId());
			return canceledReport;
		}
		
		CanceledReport canceledReport = new CanceledReport();
		canceledReport.setVenId(cancelReport.getVenId());
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(cancelReport.getVenId());
		response.setSchemaVersion(cancelReport.getSchemaVersion());
		canceledReport.setResponse(response);
		canceledReport.setRequestId(cancelReport.getRequestId());
		return canceledReport;
	}



	@Override
	public Response canceledReport(
			CanceledReport canceledReport) throws VtnDrasServiceException {
		System.out.println("===========> vtn soap service canceledReport ");
		String requestId =  Constants.REQUEST_ID_PREFIX + MemorySequence.getNextSequenceId();
		if(!partyRequestProcessor.isVenValid(canceledReport.getVenId(), canceledReport.getCertCommonName())){
			Response response = new Response();
			response.setRequestId(requestId);
			response.setResponseCode("463");
			response.setResponseDescription("ERROR");
			return response;
		}
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(canceledReport.getVenId());
		response.setSchemaVersion(canceledReport.getSchemaVersion());
		return response;
	}

	@Override
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException {
		return null;
	}

	@Override
	public Response createdEvent(
			CreatedEvent createdEvent) throws VtnDrasServiceException {
		return null;
	}

	@Override
	public PollResponse poll(Poll poll) throws VtnDrasServiceException {
		return pollRequestProcessor.poll(poll);
	}
	
}
