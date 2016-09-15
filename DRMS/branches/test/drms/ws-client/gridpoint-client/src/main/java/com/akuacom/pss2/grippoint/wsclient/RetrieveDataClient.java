package com.akuacom.pss2.grippoint.wsclient;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.client.OperationClient;
import org.apache.commons.lang.StringUtils;

public class RetrieveDataClient {
	
	private static final String DEF_URL = "https://admtools-admview.admmicro.net/ADMTools/admview/admdata.asmx";
	private static final String DEF_NAMESPACE = "http://www.admmicro.net/";
	private static final String DEF_METHOD = "getlpdata";
	/**
	 * Those attributes should be retrieved from the database using initialize function
	 */
	private String retrieveDataURL;
	private String retrieveDataNameSpace;
	private String retrieveDataMethod;
	
	public RetrieveDataClient(String namespace, String url, String method){
		retrieveDataNameSpace = StringUtils.isBlank(namespace)?DEF_NAMESPACE:namespace;;//"http://www.admmicro.net/";
		retrieveDataURL = StringUtils.isBlank(url)?DEF_URL:url;;//"https://admtools-admview.admmicro.net/ADMTools/admview/admdata.asmx";
		retrieveDataMethod = StringUtils.isBlank(namespace)?DEF_METHOD:method;;//"getlpdata";
	}
	
	/**
	 * Function for create the GridPoint Webservice retrieveData request OMElement
	 * @return OMElement instance
	 */
	private OMElement createRetrieveDataRequest(String site_id,String startdate,String enddate,String dst){
		//Get SOAPFactory instance
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		//Create OMNamespace instance
		OMNamespace omNs =   fac.createOMNamespace(getRetrieveDataNameSpace(), "");      

		OMElement param1  = fac.createOMElement("site_id", omNs);  
        param1.addChild(fac.createOMText(param1,site_id));  
        OMElement param2  = fac.createOMElement("startdate", omNs);  
        param2.addChild(fac.createOMText(param2, startdate));
        OMElement param3  = fac.createOMElement("enddate", omNs);  
        param3.addChild(fac.createOMText(param3, enddate));
        OMElement param4  = fac.createOMElement("dst", omNs);  
        param4.addChild(fac.createOMText(param4, dst));  
        
        //Create request body OMElement
        OMElement request = fac.createOMElement(getRetrieveDataMethod(), omNs);  
        request.addChild(param1);  
        request.addChild(param2);  
        request.addChild(param3);  
        request.addChild(param4);   
        //System.out.println("request is:"+request);
        return request;
	}
	/**
	 * Function for retrieve data
	 * @param cookie
	 * @return
	 * @throws Exception
	 */
	public SOAPEnvelope retrieveData(String cookie,String site_id,String startdate,String enddate,String dst) throws Exception{
		
		OperationClient operationClient=Axis2Util.createOperationClient(getRetrieveDataURL(), getRetrieveDataSoapActionURI(), 
				Axis2Util.createSOAPEnvelope(createRetrieveDataRequest(site_id, startdate, enddate, dst)), cookie);
		return Axis2Util.getResponseSOAPEnvelope(operationClient);

	}
	
	//---------------------------------------------------------------Setter and Getter--------------------------------------------------
	public String getRetrieveDataURL() {
		return retrieveDataURL;
	}
	public void setRetrieveDataURL(String retrieveDataURL) {
		this.retrieveDataURL = retrieveDataURL;
	}
	public String getRetrieveDataNameSpace() {
		return retrieveDataNameSpace;
	}
	public void setRetrieveDataNameSpace(String retrieveDataNameSpace) {
		this.retrieveDataNameSpace = retrieveDataNameSpace;
	}
	public String getRetrieveDataMethod() {
		return retrieveDataMethod;
	}
	public void setRetrieveDataMethod(String retrieveDataMethod) {
		this.retrieveDataMethod = retrieveDataMethod;
	}
	public String getRetrieveDataSoapActionURI() {
		return retrieveDataNameSpace+retrieveDataMethod;
	}
	
}
