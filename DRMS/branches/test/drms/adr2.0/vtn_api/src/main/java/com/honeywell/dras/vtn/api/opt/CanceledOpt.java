package com.honeywell.dras.vtn.api.opt;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;
import com.honeywell.dras.vtn.api.common.Response;

@XmlRootElement
public class CanceledOpt extends BaseClass{

	private Response response;
	private String optId;
	
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}
	
}
