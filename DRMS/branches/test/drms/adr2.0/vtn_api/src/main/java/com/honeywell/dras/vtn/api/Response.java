package com.honeywell.dras.vtn.api;

/**
 * Response
 * @author sunil
 *
 */
public interface Response {

	public String getResponseCode();
	
	public String getRequestId();
	
	public String getResponseDescription();
}
