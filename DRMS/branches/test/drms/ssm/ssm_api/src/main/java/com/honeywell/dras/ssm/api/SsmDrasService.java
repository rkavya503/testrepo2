package com.honeywell.dras.ssm.api;

/**
 * 
 * @author E395886(Ram Pandey)
 *
 */

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.honeywell.dras.ssm.api.request.EnrollAggregatorRequest;
import com.honeywell.dras.ssm.api.request.EnrollCustomerRequest;
import com.honeywell.dras.ssm.api.request.GetProgramsRequest;
import com.honeywell.dras.ssm.api.request.ParticipantRequest;
import com.honeywell.dras.ssm.api.response.EnrollAggregatorResponse;
import com.honeywell.dras.ssm.api.response.EnrollCustomerResponse;
import com.honeywell.dras.ssm.api.response.GetProgramResponse;
import com.honeywell.dras.ssm.api.response.ParticipantResponse;

@WebService(name="SsmDrasService" , targetNamespace="http://dras.honeywell.com/services/api/ports/ssmDrasService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SsmDrasService {
	
	@WebMethod
	public GetProgramResponse getPrograms(GetProgramsRequest request);
	@WebMethod
	public EnrollCustomerResponse enrollCustomer(EnrollCustomerRequest request);
	@WebMethod
	public EnrollAggregatorResponse enrollAggregator(EnrollAggregatorRequest request) ;
	@WebMethod
	public ParticipantResponse checkAccountNumberExist(ParticipantRequest request);
	@WebMethod
	public ParticipantResponse checkParticipantExist(ParticipantRequest request);
	@WebMethod
	public ParticipantResponse getAllAccountNos(ParticipantRequest request);
	@WebMethod
	public ParticipantResponse getAllPartNames(ParticipantRequest request);
	@WebMethod
	public ParticipantResponse getAllAccNoAndPartName(ParticipantRequest request);
	@WebMethod
	public void rollbackParticipants(ParticipantRequest request);
}
