package com.honeywell.dras.ssm.service;

import javax.jws.WebService;

import com.honeywell.dras.ssm.SsmSoapServiceHelper;
import com.honeywell.dras.ssm.api.SsmDrasService;
import com.honeywell.dras.ssm.api.request.EnrollAggregatorRequest;
import com.honeywell.dras.ssm.api.request.EnrollCustomerRequest;
import com.honeywell.dras.ssm.api.request.GetProgramsRequest;
import com.honeywell.dras.ssm.api.request.ParticipantRequest;
import com.honeywell.dras.ssm.api.response.EnrollAggregatorResponse;
import com.honeywell.dras.ssm.api.response.EnrollCustomerResponse;
import com.honeywell.dras.ssm.api.response.GetProgramResponse;
import com.honeywell.dras.ssm.api.response.ParticipantResponse;

@WebService(serviceName="SsmDrasService" ,endpointInterface="com.honeywell.dras.ssm.api.SsmDrasService",  targetNamespace="http://dras.honeywell.com/services/api/ports/ssmDrasService")
public class SsmDrasSoapService implements SsmDrasService{
	
	
	private SsmSoapServiceHelper ssmSoapServiceHelper;

	@Override
	public GetProgramResponse getPrograms(GetProgramsRequest request) {
		GetProgramResponse response = getServiceHelperObject().getProgramList();
		return response;
	}

	@Override
	public EnrollCustomerResponse enrollCustomer(EnrollCustomerRequest request) {
		EnrollCustomerResponse enrollCustomerInDras = getServiceHelperObject().enrollCustomerInDras(request);//ssmManager.enrollCustomerInDras(request);
		return enrollCustomerInDras;
	}

	@Override
	public EnrollAggregatorResponse enrollAggregator(
			EnrollAggregatorRequest request) {
		EnrollAggregatorResponse enrollAggregatorInDras = getServiceHelperObject().enrollAggregatorInDras(request);
		return enrollAggregatorInDras;
	}
	
	@Override
	public ParticipantResponse checkAccountNumberExist(ParticipantRequest request) {
		ParticipantResponse response = getServiceHelperObject().checkAccountNumberExist(request);
		return response;
	}
	
	@Override
	public ParticipantResponse checkParticipantExist(ParticipantRequest request) {
		ParticipantResponse response = getServiceHelperObject().checkParticipantExist(request);
		return response;
	}
	
	@Override
	public ParticipantResponse getAllAccountNos(ParticipantRequest request) {
		ParticipantResponse response = getServiceHelperObject().getAllAccountNos(request);
		return response;
	}
	
	@Override
	public ParticipantResponse getAllPartNames(ParticipantRequest request) {
		ParticipantResponse response = getServiceHelperObject().getAllPartNames(request);
		return response;
	}
	
	@Override
	public ParticipantResponse getAllAccNoAndPartName(ParticipantRequest request) {
		ParticipantResponse response = getServiceHelperObject().getAllAccNoAndPartName(request);
		return response;
	}
	
	@Override
	public void rollbackParticipants(ParticipantRequest request) {
		getServiceHelperObject().rollbackParticipants(request);;
	}
	
	
	private SsmSoapServiceHelper getServiceHelperObject(){
		if(null == ssmSoapServiceHelper){
			ssmSoapServiceHelper = new SsmSoapServiceHelper();
		}
		return ssmSoapServiceHelper;
	}

}
