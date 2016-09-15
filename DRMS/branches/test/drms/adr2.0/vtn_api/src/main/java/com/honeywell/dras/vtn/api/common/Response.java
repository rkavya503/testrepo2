package com.honeywell.dras.vtn.api.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response extends BaseClass{
	
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
