package com.akuacom.pss2.openadr2.event.validator;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.RequestEvent;


public interface EventRequestValidator {
	@Remote
    public interface R extends EventRequestValidator {}
    @Local
    public interface L extends EventRequestValidator {}
    
    public boolean isRequestEventValid(RequestEvent requestEvent);
    public boolean isCreatedEventValid(CreatedEvent createdEvent);
    public Response getErrorResponse();

}
