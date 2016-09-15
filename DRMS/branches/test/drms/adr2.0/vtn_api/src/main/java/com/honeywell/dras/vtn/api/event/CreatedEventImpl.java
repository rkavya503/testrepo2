package com.honeywell.dras.vtn.api.event;

import java.util.List;

import com.honeywell.dras.vtn.api.CreatedEvent;
import com.honeywell.dras.vtn.api.Response;
import com.honeywell.dras.vtn.api.EventResponse;

/**
 * 
 * @author sunil
 *
 */
public class CreatedEventImpl implements CreatedEvent{
	
	private Response response;
	
	private String venId;
	
    private List<EventResponse> eventResponseList;


	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public List<EventResponse> getEventResponseList() {
		return eventResponseList;
	}

	public void setEventResponseList(List<EventResponse> eventResponseList) {
		this.eventResponseList = eventResponseList;
	}
	
	
	
	

}
