package com.akuacom.pss2.openadr2;

import javax.ejb.Local;
import javax.ejb.Remote;

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

public interface VtnDrasManager  {
	@Remote
    public interface R extends VtnDrasManager {}
    @Local
    public interface L extends VtnDrasManager {}
    
   	
	public CreatedPartyRegistration queryRegistration(QueryRegistration queryRegistration)
			throws VtnDrasServiceException;

	
	public CreatedPartyRegistration createPartyRegistration(CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException;
	

	public CanceledPartyRegistration cancelPartyRegistration(CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException;
	

	
	public Response canceledPartyRegistration(CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException;


	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException;

	
	public CreatedOpt createOpt(CreateOpt createOpt)
			throws VtnDrasServiceException;
	
	
	public CanceledOpt cancelOpt(CancelOpt cancelOpt)
			throws VtnDrasServiceException;
	
	
	public RegisteredReport registerReport(RegisterReport registerReport)
			throws VtnDrasServiceException;
	

	public Response registeredReport(RegisteredReport registeredReport)
			throws VtnDrasServiceException;	
	

	
	public CreatedReport createReport(CreateReport createReport)
			throws VtnDrasServiceException;	
	
	
	public Response createdReport(CreatedReport createdReport)
			throws VtnDrasServiceException;	
	
	
	public UpdatedReport updateReport(UpdateReport updateReport)
			throws VtnDrasServiceException;	
	

	public Response updatedReport(UpdatedReport updatedReport)
			throws VtnDrasServiceException;	
	
	
	public CanceledReport cancelReport(CancelReport cancelReport)
			throws VtnDrasServiceException;	
	
	
	public Response canceledReport(CanceledReport canceledReport)
			throws VtnDrasServiceException;	
	
	
	public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException;		

	
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException;	
	
	
	public PollResponse poll(Poll poll)
			throws VtnDrasServiceException;	
}
