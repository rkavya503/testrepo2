package com.honeywell.dras.vtn.push.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.honeywell.dras.vtn.api.common.PushProfile;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.event.DistributeEventRequest;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistrationRequest;
import com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration;
import com.honeywell.dras.vtn.api.registration.ReRegistration;
import com.honeywell.dras.vtn.api.registration.RequestReRegistrationRequest;
import com.honeywell.dras.vtn.api.registration.Result;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CancelReportRequest;
import com.honeywell.dras.vtn.api.report.CanceledReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.CreateReportRequest;
import com.honeywell.dras.vtn.api.report.CreatedReport;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisterReportRequest;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdateReportRequest;
import com.honeywell.dras.vtn.api.report.UpdatedReport;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

@WebService(name = "VtnPushService", targetNamespace = "http://dras.honeywell.com/services/api/ports/VtnPushService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
//@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
public interface VtnPushService {
	
	@WebMethod(operationName = "cancelPartyRegistration")
	public CanceledPartyRegistration cancelPartyRegistration(CancelPartyRegistrationRequest cancelPartyRegistrationRequest)
			throws VtnPushServiceException;
	
	
	@WebMethod(operationName = "requestReRegistration")
	public Response requestReRegistration(RequestReRegistrationRequest requestReRegistrationRequest)
			throws VtnPushServiceException;
	
	
	@WebMethod(operationName = "registerReport")
	public RegisteredReport registerReport(RegisterReportRequest registerReportRequest)
			throws VtnDrasServiceException;
	
	@WebMethod(operationName = "createReport")
	public CreatedReport createReport(CreateReportRequest createReportRequest)
			throws VtnDrasServiceException;	
	
	@WebMethod(operationName = "updateReport")
	public UpdatedReport updateReport(UpdateReportRequest updateReportRequest)
			throws VtnDrasServiceException;	
	
	
	@WebMethod(operationName = "cancelReport")
	public CanceledReport cancelReport(CancelReportRequest cancelReportRequest)
			throws VtnDrasServiceException;	
	
	

	@WebMethod(operationName = "distributeEvent")
	public Result distributeEvent(DistributeEventRequest distributeEventRequest)
			throws VtnDrasServiceException;		

	
	
}
