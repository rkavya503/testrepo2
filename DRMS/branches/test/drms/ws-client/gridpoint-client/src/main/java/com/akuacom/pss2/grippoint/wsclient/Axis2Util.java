package com.akuacom.pss2.grippoint.wsclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.CommonsTransportHeaders;
import org.apache.axis2.transport.http.HTTPConstants;

public class Axis2Util {
	private static String HEADER_COOKIE="Set-Cookie";
	private static String TRANSPORT_HEADERS = "TRANSPORT_HEADERS";
	
	
	private static List<ServiceClient> clientCachePool = new ArrayList<ServiceClient>();
	
	private static void cleanClientCachePool() throws Exception{
		for(ServiceClient client : clientCachePool ){
			if(client!=null){
				client.cleanupTransport();
				client.cleanup();
			}
		}
		clientCachePool.clear();
	}
	
	/**
	 * Function for create SOAPEnvelope instance with the OMElement object
	 * @param  bodySOAPElement the OMElement object
	 * @return SOAPEnvelope instance
	 */
	public static SOAPEnvelope createSOAPEnvelope(OMElement bodySOAPElement) {
		//Get SOAPFactory instance
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		//Create SOAPEnvelope instance
        SOAPEnvelope envelope = fac.getDefaultEnvelope();
        envelope.getBody().addChild(bodySOAPElement);
        return envelope;
	}
	
	/**
	 * Function for create OperationClient instance 
	 * @param webserviceURL
	 * @param soapActionURI
	 * @param envelope
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	public static OperationClient createOperationClient(String webserviceURL,String soapActionURI,SOAPEnvelope envelope,String cookie)throws Exception{
		
		cleanClientCachePool();
		OperationClient operationClient;
		//Prepare-Create EndpointReference instance from web service URL
		EndpointReference endpointReference = new EndpointReference(webserviceURL);
		//1-Create ServiceClient instance    
        ServiceClient sender = new ServiceClient();  
        clientCachePool.add(sender);
        //2-Create OperationClient instance
        operationClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP);
        //3-Creating message context 
		MessageContext outMsgCtx = new MessageContext();
		//4-Create Options instance    
        Options options = outMsgCtx.getOptions();
        options.setAction(soapActionURI);
        options.setTo(endpointReference);  
        options.setManageSession(true);
        //Set the time out to 1 minute to solve the java.net.sockettimeoutexception read timed out problem
        options.setTimeOutInMilliSeconds(600000L);
        //5-Set cookie property- choose able
        if(cookie!=null&&(!cookie.equalsIgnoreCase(""))){
        	options.setProperty(HTTPConstants.COOKIE_STRING, cookie);
        }
        //6-Create authentication request SOAP envelope and set to out MessageContext instance
		outMsgCtx.setEnvelope(envelope);
		//7-Add a message context to operationClient
		operationClient.addMessageContext(outMsgCtx);
        
        return operationClient;
	}
	/**
	 * Function for execute the OperationClient instance and retrieve the response SOAPEnvelop instance
	 * @param operationClient
	 * @return
	 * @throws Exception
	 */
	public static SOAPEnvelope getResponseSOAPEnvelope(OperationClient operationClient) throws Exception{
		SOAPEnvelope response = null;
		MessageContext inMsgtCtx = getResponseMessageContext(operationClient);
		if (inMsgtCtx != null) {
			response = inMsgtCtx.getEnvelope();
		}
		return response;
	}	
	/**
	 * Function for execute the OperationClient instance and retrieve the response(in) MessageContext instance
	 * @param operationClient
	 * @return
	 * @throws Exception
	 */
	public static MessageContext getResponseMessageContext(OperationClient operationClient) throws Exception{
		//Execute SOAP request
		// Clear SSL trustStore info before access a SSL web service
		String trustStore = System.getProperty("javax.net.ssl.trustStore");
		String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
		System.clearProperty("javax.net.ssl.trustStore");               
		System.clearProperty("javax.net.ssl.trustStorePassword");
		try{
			operationClient.execute(true);
		}finally{
			if(trustStore!=null){
				System.setProperty("javax.net.ssl.trustStore", trustStore);	
			}
			if(trustStorePassword!=null){
				System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);	
			}
			trustStore = null;
			trustStorePassword = null;
		}
		
		//Access response message context and response SOAPEnvelope
		return  operationClient.getMessageContext("In");
	}
	/**
	 * Function for retrieve cookie from MessageContext instance
	 * @param messageContext
	 * @return
	 */
	public static String getCookieFromMessageContext(MessageContext messageContext){
		String cookie = null;
		if (messageContext != null) {
			CommonsTransportHeaders header =(CommonsTransportHeaders)messageContext.getProperty(TRANSPORT_HEADERS);
			Object cookieObj = header.get(HEADER_COOKIE);
			if(cookieObj!=null){
				cookie  =header.get(HEADER_COOKIE).toString();				
			}
		}
		return cookie;
	}
}
