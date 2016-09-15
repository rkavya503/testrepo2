package com.akuacom.pss2.openadr2.event;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.RequestEvent;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;


public interface EventRequestProcessor {
	@Remote
    public interface R extends EventRequestProcessor {}
    @Local
    public interface L extends EventRequestProcessor {}
    
    public DistributeEvent requestEvent(RequestEvent requestEvent)
			throws VtnDrasServiceException;		

	
	public Response createdEvent(CreatedEvent createdEvent)
			throws VtnDrasServiceException;	

}
