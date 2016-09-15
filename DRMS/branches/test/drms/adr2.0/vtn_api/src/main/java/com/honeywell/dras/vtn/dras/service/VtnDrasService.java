package com.honeywell.dras.vtn.dras.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

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
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdatedReport;

@WebService(name = "VtnDrasService", targetNamespace = "http://dras.honeywell.com/services/api/ports/VtnDrasService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)

public interface VtnDrasService {

	@WebMethod(operationName = "queryRegistration")
	public CreatedPartyRegistration queryRegistration(QueryRegistration queryRegistration)
			throws VtnDrasServiceException;

	
	@WebMethod(operationName = "createPartyRegistration")
	public CreatedPartyRegistration createPartyRegistration(CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException;
	

	@WebMethod(operationName = "cancelPartyRegistration")
	public CanceledPartyRegistration cancelPartyRegistration(CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException;
	

	
	@WebMethod(operationName = "canceledPartyRegistration")
	public Response canceledPartyRegistration(CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException;


	@WebMethod(operationName = "registerFingerPrint")
	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException;

	
	@WebMethod(operationName = "createOpt")
	public CreatedOpt createOpt(CreateOpt createOpt)
			throws VtnDrasServiceException;
	
	
	@WebMethod(operationName = "cancelOpt")
	public CanceledOpt cancelOpt(CancelOpt cancelOpt)
			throws VtnDrasServiceException;
	
	
	@WebMethod(operationName = "registerReport")
	public RegisteredReport registerReport(RegisterReport registerReport)
			throws VtnDrasServiceException;
	

	@WebMethod(operationName = "registeredReport")
	public Response registeredReport(RegisteredReport registeredReport)
			throws VtnDrasServiceException;	
	

	
	@WebMethod(operationName = "createReport")
	public CreatedReport createReport(CreateReport createReport)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "createdReport")
	public Response createdReport(CreatedReport createdReport)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "updateReport")
	public UpdatedReport updateReport(UpdateReport updateReport)
			throws VtnDrasServiceException;	
	

	@WebMethod(operationName = "updatedReport")
	public Response updatedReport(UpdatedReport updatedReport)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "cancelReport")
	public CanceledReport cancelReport(CancelReport cancelReport)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "canceledReport")
	public Response canceledReport(CanceledReport canceledReport)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "requestEvent")
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException;		

	
	@WebMethod(operationName = "createdEvent")
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "poll")
	public PollResponse poll(Poll poll)
			throws VtnDrasServiceException;	
	
}
