package com.honeywell.dras.vtn.api.event;

import java.util.List;

import com.honeywell.dras.vtn.api.DistributeEvent;
import com.honeywell.dras.vtn.api.Oadr2Event;
import com.honeywell.dras.vtn.api.Response;

public class DistributeEventImpl implements DistributeEvent{
	
	
	private String requestID;
	
	private String vtnID;
	
	private List<Oadr2Event> eventList;
	
	private Response response;
	



	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getVtnID() {
		return vtnID;
	}

	public void setVtnID(String vtnID) {
		this.vtnID = vtnID;
	}

	public List<Oadr2Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Oadr2Event> eventList) {
		this.eventList = eventList;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	
	
	

}
