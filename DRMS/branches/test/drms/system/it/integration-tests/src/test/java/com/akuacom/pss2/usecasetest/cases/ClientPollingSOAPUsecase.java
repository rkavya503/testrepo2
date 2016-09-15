package com.akuacom.pss2.usecasetest.cases;

import java.io.StringWriter;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.openadr.dras.drasclientsoap.DRASClientSOAP;
import org.openadr.dras.drasclientsoap.DRASClientSOAP_Service;
import org.openadr.dras.eventstate.EventState;
import org.openadr.dras.eventstate.EventStateConfirmation;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.eventstate.ObjectFactory;

import com.akuacom.pss2.usecasetest.tests.ClientPollingState;

public class ClientPollingSOAPUsecase extends AbstractUseCase {
	
	private String clientName;
	private String password;
	
	public ClientPollingSOAPUsecase(String clientName, String password) {
		this.clientName = clientName;
		this.password = password;
	}

	@Override
	public Object runCase() throws Exception {
		ClientPollingState res = new ClientPollingState();
		String endPoint = "http://localhost:8080/SOAPClientWS/nossl/soap";
		
		try 
        {
         	Authenticator.setDefault(new SimpleAuthenticator());

         	DRASClientSOAP service = 
        		new DRASClientSOAP_Service(new URL(endPoint + "?wsdl"),
        		new QName("http://www.openadr.org/DRAS/DRASClientSOAP/", 
        		"SOAPWSService")).getDRASClientSOAPPort();
        	
         	// force the endpoint since what comes back from the server isn't correct
			final BindingProvider bp = (BindingProvider) service;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, 
			  endPoint);

			// get the EventState
        	Holder<String> holderRV = new Holder<String>();
        	Holder<ListOfEventStates> eventStates = new Holder<ListOfEventStates>();
			service.getEventStates("", holderRV, eventStates);
			//System.out.println("\ngetEventStates returned1: " + holderRV.value);
			
			JAXBContext jc = JAXBContext.newInstance("org.openadr.dras.eventstate");
	    	JAXBElement<org.openadr.dras.eventstate.ListOfEventStates> 
	    		wsEventStatesElement = (new ObjectFactory()).
	    		createListOfEventState(eventStates.value);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			java.io.StringWriter sw = new StringWriter();
			marshaller.marshal(wsEventStatesElement, sw);
			
			//List<EventState> esList = wsEventStatesElement.getValue().getEventStates();
			

			EventStateConfirmation confirm = new EventStateConfirmation();
			String rv = service.setEventStateConfirmation(confirm);
			res.setConfirmationStatus(rv);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
		
		return res;
	}
	
	/**
     * The Class SimpleAuthenticator.
     */
    class SimpleAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(clientName, password.toCharArray());
		}
	} 
}
