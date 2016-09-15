package com.honeywell.dras.vtn.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;
import com.honeywell.dras.vtn.api.common.Response;

@XmlRootElement
public class DistributeEvent extends BaseClass{
	
	private Response response;
	private List<Event> eventList;
	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
	/**
	 * @return the eventList
	 */
	public List<Event> getEventList() {
		if (eventList == null) {
			eventList = new ArrayList<Event>();
		}
		return eventList;
	}
	/**
	 * @param eventList the eventList to set
	 */
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

}
