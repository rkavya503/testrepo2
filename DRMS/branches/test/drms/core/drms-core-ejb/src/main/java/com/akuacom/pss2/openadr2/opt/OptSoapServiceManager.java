package com.akuacom.pss2.openadr2.opt;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.opt.CancelOpt;
import com.honeywell.dras.vtn.api.opt.CanceledOpt;
import com.honeywell.dras.vtn.api.opt.CreateOpt;
import com.honeywell.dras.vtn.api.opt.CreatedOpt;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

public interface OptSoapServiceManager {
	

	@Remote
	public interface R extends OptSoapServiceManager {}
	@Local
	public interface L extends OptSoapServiceManager {}
	
	public CreatedOpt createOpt(CreateOpt createOpt) throws VtnDrasServiceException ;
	public CanceledOpt cancelOpt(CancelOpt cancelOpt) throws VtnDrasServiceException ;
	public boolean canCreateEvent(String resourceId,String programName,Date startTime , Long duration);

}
