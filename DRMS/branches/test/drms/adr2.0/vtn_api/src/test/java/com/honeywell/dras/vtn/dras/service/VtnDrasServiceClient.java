package com.honeywell.dras.vtn.dras.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatedPartyRegistration;

public class VtnDrasServiceClient {

	
	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws VtnDrasServiceException 
	 * @throws BBCSServiceException 
	 * @throws GroupServiceException
	 */
	public static void main(String[] args) throws MalformedURLException, VtnDrasServiceException{
		URL url = new URL("http://localhost:9889/VtnDrasService?wsdl");
		QName qname = new QName(
				"http://dras.honeywell.com/services/api/ports/VtnDrasService",
				"VtnDrasService");
		
		Service service = Service.create(url, qname);
		// Extract the endpoint interface, the service "port".
		VtnDrasService s = service.getPort(VtnDrasService.class);
		
		CreatePartyRegistration createPartyRegistration = new CreatePartyRegistration();
		CreatedPartyRegistration r = s.createPartyRegistration(createPartyRegistration );
		System.out.println("Call Successful");

	}

	
}
