package com.akuacom.pss2.grippoint.wsclient;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.StringUtils;

public class AuthClient {
	
	private static final String DEF_URL = "https://admtools-admview.admmicro.net/ADMTools/auth/wslogin.asmx";
	private static final String DEF_NAMESPACE = "http://www.admmicro.net/";
	private static final String DEF_METHOD = "login";
	
	/**
	 * Those attributes should be retrieved from the database using initialize function
	 */
	private String authenticationURL;
	private String authenticationMethod;
	private String authenticationNameSpace;
	
	public AuthClient(String namespace, String url, String method){
		authenticationNameSpace = StringUtils.isBlank(namespace)?DEF_NAMESPACE:namespace;//"http://www.admmicro.net/";
		authenticationURL = StringUtils.isBlank(url)?DEF_URL:url;//"https://admtools-admview.admmicro.net/ADMTools/auth/wslogin.asmx";
		authenticationMethod = StringUtils.isBlank(method)?DEF_METHOD:method;//"login";
	}
	
	/**
	 * Function for create the GridPoint Webservice authentication request OMElement
	 * @return OMElement instance
	 */
	public OMElement createAuthenticationRequest(String ussername, String psw){
		//Get SOAPFactory instance
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		//Create OMNamespace instance
		OMNamespace omNs =   fac.createOMNamespace(getAuthenticationNameSpace(), "");      
        //Create user name node OMElement
		OMElement usernameElement  = fac.createOMElement("username", omNs);  
        usernameElement.addChild(fac.createOMText(usernameElement, ussername));  
        //Create credential node OMElement
        OMElement passwordElement  = fac.createOMElement("pw", omNs);  
        passwordElement.addChild(fac.createOMText(passwordElement, psw));
        //Create request body OMElement
        OMElement request = fac.createOMElement(getAuthenticationMethod(), omNs);  
        request.addChild(usernameElement);  
        request.addChild(passwordElement); 
        //System.out.println("request is:"+request);
        return request;
	}
	
	/**
	 * Function invoke the GridPoint Webservice authentication service and get the response SOAPEnvelope,
	 * also retrieve the cookie with the request
	 * @return SOAPEnvelope instance
	 * @throws Exception
	 */
	public MessageContext authentication(String username, String psw) throws Exception{

		OperationClient operationClient=Axis2Util.createOperationClient(authenticationURL, getAuthenticationSoapActionURI(), Axis2Util.createSOAPEnvelope(createAuthenticationRequest(username, psw)), null);
		return Axis2Util.getResponseMessageContext(operationClient);
	}
	
	//---------------------------------------------------------------Setter and Getter--------------------------------------------------
	public String getAuthenticationURL() {
		return authenticationURL;
	}
	public void setAuthenticationURL(String authenticationURL) {
		this.authenticationURL = authenticationURL;
	}
	public String getAuthenticationNameSpace() {
		return authenticationNameSpace;
	}
	public void setAuthenticationNameSpace(String authenticationNameSpace) {
		this.authenticationNameSpace = authenticationNameSpace;
	}
	public String getAuthenticationMethod() {
		return authenticationMethod;
	}
	public void setAuthenticationMethod(String authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}
	public String getAuthenticationSoapActionURI() {
		return authenticationNameSpace+authenticationMethod;//"http://www.admmicro.net/login";
	}

}
