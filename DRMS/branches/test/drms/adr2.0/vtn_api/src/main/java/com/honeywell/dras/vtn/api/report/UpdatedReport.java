package com.honeywell.dras.vtn.api.report;

import com.honeywell.dras.vtn.api.common.BaseClass;
import com.honeywell.dras.vtn.api.common.Response;

public class UpdatedReport extends BaseClass{
	
	private Response response;
	private CancelReport cancelReport;
	
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
	 * @return the cancelReport
	 */
	public CancelReport getCancelReport() {
		return cancelReport;
	}
	/**
	 * @param cancelReport the cancelReport to set
	 */
	public void setCancelReport(CancelReport cancelReport) {
		this.cancelReport = cancelReport;
	}

}
