package com.akuacom.pss2.openadr2.event.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.event.EventRequest;


public interface EventRequestManager {
	@Remote
    public interface R extends EventRequestManager {}
    @Local
    public interface L extends EventRequestManager {}
    
	public void createEventRequest(EventRequest eventRequest) ;
	public void deleteEventRequestByRequestId(String requestId) throws EntityNotFoundException;
	public List<EventRequest> getEventRequestByRequestIdAndVenId(String requestId, String venId);
	public List<EventRequest> getEventRequestByRequestIdAndVenIdAndEventId(String requestId, String venId,String eventId);
	public List<EventRequest> getEventRequestByRequestId(String requestId);
	public EventRequest getEventRequestByRequestIdAndEventId(String requestId, String eventId);
}
