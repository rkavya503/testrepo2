package com.honeywell.dras.ssm.api.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnrollCustomerResponse {
	
	private Boolean error;
	private String errorMessage;
	
	public EnrollCustomerResponse(){
		this.error = true;
		this.errorMessage = "";
	}
	
	public Boolean getError() {
		return error;
	}
	public void setError(Boolean error) {
		this.error = error;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
