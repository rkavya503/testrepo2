package com.honeywell.dras.vtn.api.event;

import java.io.Serializable;

import com.honeywell.dras.vtn.api.Response;

public class ResponseImpl implements Response, Serializable {

	private static final long serialVersionUID = -2749156774808368649L;

	private String responseCode;
	
	private String requestId;
	
	private String responseDescription;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	
}
