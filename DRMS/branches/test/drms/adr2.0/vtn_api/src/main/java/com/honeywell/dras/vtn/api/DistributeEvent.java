package com.honeywell.dras.vtn.api;

import java.util.List;



/**
 * DistributeEvent
 * @author sunil
 *
 */
public interface DistributeEvent {

	public String getRequestID();
	
	public String getVtnID();
	
	public List<Oadr2Event> getEventList();
	
	public Response getResponse();
}
