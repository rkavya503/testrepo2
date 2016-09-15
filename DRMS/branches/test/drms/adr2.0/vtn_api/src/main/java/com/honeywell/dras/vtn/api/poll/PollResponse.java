package com.honeywell.dras.vtn.api.poll;

import javax.xml.bind.annotation.XmlRootElement;

import com.honeywell.dras.vtn.api.common.BaseClass;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.ReRegistration;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.UpdateReport;

@XmlRootElement
public class PollResponse extends BaseClass{
	
	private CancelPartyRegistration cancelPartyRegistration;
	private ReRegistration reRegistration;
	private DistributeEvent distributeEvent;
	private RegisterReport registerReport;
	private CreateReport createReport;
	private UpdateReport updateReport;
	private CancelReport cancelReport;
	private Response response;
	
	
	/**
	 * @return the cancelPartyRegistration
	 */
	public CancelPartyRegistration getCancelPartyRegistration() {
		return cancelPartyRegistration;
	}
	/**
	 * @param cancelPartyRegistration the cancelPartyRegistration to set
	 */
	public void setCancelPartyRegistration(CancelPartyRegistration cancelPartyRegistration) {
		this.cancelPartyRegistration = cancelPartyRegistration;
	}
	/**
	 * @return the reRegistration
	 */
	public ReRegistration getReRegistration() {
		return reRegistration;
	}
	/**
	 * @param reRegistration the reRegistration to set
	 */
	public void setReRegistration(ReRegistration reRegistration) {
		this.reRegistration = reRegistration;
	}
	/**
	 * @return the distributeEvent
	 */
	public DistributeEvent getDistributeEvent() {
		return distributeEvent;
	}
	/**
	 * @param distributeEvent the distributeEvent to set
	 */
	public void setDistributeEvent(DistributeEvent distributeEvent) {
		this.distributeEvent = distributeEvent;
	}
	/**
	 * @return the registerReport
	 */
	public RegisterReport getRegisterReport() {
		return registerReport;
	}
	/**
	 * @param registerReport the registerReport to set
	 */
	public void setRegisterReport(RegisterReport registerReport) {
		this.registerReport = registerReport;
	}
	/**
	 * @return the createReport
	 */
	public CreateReport getCreateReport() {
		return createReport;
	}
	/**
	 * @param createReport the createReport to set
	 */
	public void setCreateReport(CreateReport createReport) {
		this.createReport = createReport;
	}
	/**
	 * @return the updateReport
	 */
	public UpdateReport getUpdateReport() {
		return updateReport;
	}
	/**
	 * @param updateReport the updateReport to set
	 */
	public void setUpdateReport(UpdateReport updateReport) {
		this.updateReport = updateReport;
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
		this.setRequestId(response.getRequestId()) ;
		this.setSchemaVersion(response.getSchemaVersion());
		this.setFingerprint(response.getFingerprint());
		this.setVenId(response.getVenId()) ;
		this.setCertCommonName(response.getCertCommonName());
	}
	
	

}
