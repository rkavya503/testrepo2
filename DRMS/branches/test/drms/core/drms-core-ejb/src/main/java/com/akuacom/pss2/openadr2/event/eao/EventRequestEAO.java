package com.akuacom.pss2.openadr2.event.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.event.EventRequest;

public interface EventRequestEAO  {
	@Remote
    public interface R extends EventRequestEAO {}
    @Local
    public interface L extends EventRequestEAO {}
    
    public void create(EventRequest er);
   	public void delete(EventRequest er) throws EntityNotFoundException;
   	public EventRequest findByUUID(String uuid) throws EntityNotFoundException;
   	public List<EventRequest> findByEventID(String eventId);
   	public List<EventRequest> findByRequestAndVenID(String requestId, String venId);
   	public List<EventRequest> findByRequestAndEventAndVenID(String requestId, String eventId, String venId);
   	public List<EventRequest> findByRequestId(String requestId);
   	public EventRequest findByRequestIdAndEventId(String requestId, String eventId);

}
