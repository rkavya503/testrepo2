package com.akuacom.pss2.openadr2.event.validator;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.openadr2.party.PartyRequestProcessor;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.EventResponse;
import com.honeywell.dras.vtn.api.event.RequestEvent;

@Stateless
public class EventRequestValidatorBean implements EventRequestValidator.L, EventRequestValidator.R{
	private Response response = new Response();
	private Logger log = Logger.getLogger(EventRequestValidatorBean.class);
	
	@EJB
	private PartyRequestProcessor.L partyRequestProcessor;
	
	public boolean isRequestEventValid(RequestEvent requestEvent){
		if(null == requestEvent){
			response.setResponseCode("452");
			response.setResponseDescription(" Request event parameter is invalid");
			log.error("Request event object is null");
			return false;
		}
		String venId = requestEvent.getVenId();
		String certCommName = requestEvent.getCertCommonName();
		if(!isVenValid(venId,certCommName)){
			return false;
		}
		return true;
	}
	public boolean isCreatedEventValid(CreatedEvent createdEvent){
		
		if(null == createdEvent){
			response.setResponseCode("452");
			response.setResponseDescription(" Created event parameter is invalid");
			log.error("Created event object is null");
			return false;
		}
		response.setCertCommonName(createdEvent.getCertCommonName());
		response.setFingerprint(createdEvent.getFingerprint());
		response.setRequestId(createdEvent.getRequestId());
		response.setSchemaVersion(createdEvent.getSchemaVersion());
		response.setVenId(createdEvent.getVenId());
		if(!this.isVenValid(createdEvent.getVenId(), createdEvent.getCertCommonName())){
			return false;
		}
		
		if(createdEvent.getResponse().getResponseCode().equalsIgnoreCase("400")){
			response.setResponseCode("200");
			response.setResponseDescription("Success");
			return false;
		}
		
		if(!validateEventResponse(createdEvent.getEventResponseList())){
			return false;
		}
		return true;
	}
	public Response getErrorResponse(){
		return response;
	}
	private boolean isVenValid(String venId , String certCommName){
		boolean isDataValid = true;
		if(null == venId || venId.isEmpty()){
			response.setResponseCode("410");
			response.setResponseDescription("venID is required");
			isDataValid = false;
		}
		/*else if(null == certCommName || certCommName.isEmpty()){
			response.setResponseCode("410");
			response.setResponseDescription("cert is required");
			isDataValid = false;
		}*/
		else if(!this.partyRequestProcessor.isVenValid(venId, certCommName)){
			response.setResponseCode("452");
			response.setResponseDescription("VEN is not valid");
			isDataValid = false;
		}
		return isDataValid;
	}
	private boolean validateEventResponse(List<EventResponse> eventResponseList){
		if(null == eventResponseList || eventResponseList.isEmpty()){
			log.error("Event Response List is required");
			response.setResponseCode("411");
			response.setResponseDescription("Event Response List is required");
			return false;
		}
		for (com.honeywell.dras.vtn.api.event.EventResponse er : eventResponseList) {
			if (er.getRequestID() == null || er.getRequestID().isEmpty()) {
				log.error("Request ID is required for each Event Response");
				response.setResponseCode("412");
				response.setResponseDescription("Request ID is required for each Event Response");
				return false;
			}
		}
		return true;
	}
	
	

}
