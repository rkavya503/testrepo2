package com.honeywell.dras.vtn.api;

import java.util.List;

/**
 * CreatedEvent
 * @author sunil
 *
 */
public interface CreatedEvent {

	public String getVenId();
	
	public Response getResponse();
	
	public List<EventResponse> getEventResponseList();
}
