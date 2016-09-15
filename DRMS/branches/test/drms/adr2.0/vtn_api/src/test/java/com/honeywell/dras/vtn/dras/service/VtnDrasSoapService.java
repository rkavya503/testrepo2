package com.honeywell.dras.vtn.dras.service;

import javax.jws.WebService;

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

@WebService(serviceName = "VtnDrasService", endpointInterface = "com.honeywell.dras.vtn.dras.service.VtnDrasService", targetNamespace = "http://dras.honeywell.com/services/api/ports/VtnDrasService")
public class VtnDrasSoapService implements VtnDrasService{

	@Override
	public CreatedPartyRegistration queryRegistration(
			QueryRegistration queryRegistration) throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreatedPartyRegistration createPartyRegistration(
			CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CanceledPartyRegistration cancelPartyRegistration(
			CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response canceledPartyRegistration(
			CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreatedOpt createOpt(CreateOpt createOpt)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CanceledOpt cancelOpt(CancelOpt cancelOpt)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegisteredReport registerReport(RegisterReport registerReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response registeredReport(RegisteredReport registeredReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreatedReport createReport(CreateReport createReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createdReport(CreatedReport createdReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdatedReport updateReport(UpdateReport updateReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updatedReport(UpdatedReport updatedReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CanceledReport cancelReport(CancelReport cancelReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response canceledReport(CanceledReport canceledReport)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PollResponse poll(Poll poll) throws VtnDrasServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
