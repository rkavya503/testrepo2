package com.akuacom.pss2.openadr2.event.eao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.openadr2.event.EventRequest;


@Stateless
public class EventRequestManagerBean implements EventRequestManager.L,EventRequestManager.R {

	
	@EJB
	private EventRequestEAO.L eventRequestEAO;
	
	@Override
	public void createEventRequest(EventRequest eventRequest) {
		eventRequestEAO.create(eventRequest);
	}

	@Override
	public void deleteEventRequestByRequestId(String requestId) throws EntityNotFoundException {
		List<EventRequest> eventRequestList = this.getEventRequestByRequestId(requestId);
		for(EventRequest eventRequest : eventRequestList){
			eventRequestEAO.delete(eventRequest);
		}
	}

	@Override
	public List<EventRequest> getEventRequestByRequestIdAndVenId(
			String requestId, String venId) {
		List<EventRequest> eventRequestList = new ArrayList<EventRequest>();
		boolean isParametersIncorrect = ( null == requestId || requestId.isEmpty()) &&
										(null == venId || venId.isEmpty());
		if(isParametersIncorrect){
			return eventRequestList;
		}
		eventRequestList = eventRequestEAO.findByRequestAndVenID(requestId, venId);
		return eventRequestList;
	}

	@Override
	public List<EventRequest> getEventRequestByRequestIdAndVenIdAndEventId(
			String requestId, String venId, String eventId) {
		List<EventRequest> eventRequestList = new ArrayList<EventRequest>();
		boolean isParametersIncorrect = ( null == requestId || requestId.isEmpty()) &&
										(null == venId || venId.isEmpty() ) && 
										(null == eventId || eventId.isEmpty());
		if(isParametersIncorrect){
			return eventRequestList;
		}
		eventRequestList = eventRequestEAO.findByRequestAndEventAndVenID(requestId, eventId, venId);
		return eventRequestList;
	}
	@Override
	public List<EventRequest> getEventRequestByRequestId(String requestId) {
		 List<EventRequest> eventRequestList = new ArrayList<EventRequest>();
		 if(null == requestId || requestId.isEmpty()){
			 return eventRequestList;
		 }
		 eventRequestList = eventRequestEAO.findByRequestId(requestId);
		 return eventRequestList;
	}
	@Override
	public EventRequest getEventRequestByRequestIdAndEventId(
			String requestId, String eventId) {
		boolean isParametersIncorrect = ( null == requestId || requestId.isEmpty()) &&
										(null == eventId || eventId.isEmpty());
		if(isParametersIncorrect){
			return null;
		}
		return eventRequestEAO.findByRequestIdAndEventId(requestId, eventId);
	}
	

}
