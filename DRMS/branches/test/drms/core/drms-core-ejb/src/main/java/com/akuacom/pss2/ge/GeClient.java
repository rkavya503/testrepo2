package com.akuacom.pss2.ge;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.StringUtils;

import com.akuacom.pss2.ge.GeTimerInterfaceManagerBean.StatusModes;
import com.akuacom.utils.DateUtil;

public class GeClient {
	
	private static final String DEF_URL = "http://localhost/SOAPInBound/Realtime.asmx";
	private static final String DEF_NAMESPACE = "Realtime";
	private static final String DEF_METHOD = "UpdateScalarValues";
	
	/**
	 * Those attributes should be retrieved from the database using initialize function
	 */
	private String serviceURL;
	private String serviceMethod;
	private String serviceNameSpace;
	
	public GeClient(String url, String namespace, String method){
		serviceNameSpace = StringUtils.isBlank(namespace)?DEF_NAMESPACE:namespace;//"http://www.admmicro.net/";
		serviceURL = StringUtils.isBlank(url)?DEF_URL:url;//"https://admtools-admview.admmicro.net/ADMTools/auth/wslogin.asmx";
		serviceMethod = StringUtils.isBlank(method)?DEF_METHOD:method;//"login";
	}
	
	/**
	 * Function for create the GridPoint Webservice service request OMElement
	 * @return OMElement instance
	 */
	public OMElement buildMessageEnvlop(GeVo vo){
		//Get SOAPFactory instance
		SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
		//Create OMNamespace instance
		OMNamespace omNs = fac.createOMNamespace(getServiceNameSpace(), "");      
		
		OMElement rootElement  = fac.createOMElement("UpdateScalarValuesStc", omNs);
        OMElement averageMWHElement = buildUpdateUnit(fac, omNs, "AverageMWH",vo.getAccountNo(),DateUtil.format(vo.getTimeStamp(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),vo.getLoad());
        OMElement programOptStatusElement = buildUpdateUnit(fac, omNs, "ProgramOptStatus",vo.getAccountNo(),DateUtil.format(vo.getTimeStamp(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),vo.getProgOptStatus());
        OMElement eventOptStatusElement = buildUpdateUnit(fac, omNs, "EventOptStatus",vo.getAccountNo(),DateUtil.format(vo.getTimeStamp(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),vo.getEvtOptStatus());
        OMElement lastPollElement = buildUpdateUnit(fac, omNs, "LastPoll",vo.getAccountNo(),DateUtil.format(vo.getTimeStamp(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),buildLastPollElement(vo));
        OMElement callOffElement = buildUpdateUnit(fac, omNs, "CallOff",vo.getAccountNo(),DateUtil.format(vo.getTimeStamp(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"),vo.getCallOff());
        
        rootElement.addChild(averageMWHElement);
        rootElement.addChild(programOptStatusElement);
        rootElement.addChild(eventOptStatusElement);
        rootElement.addChild(lastPollElement);
        rootElement.addChild(callOffElement);
        
        OMElement request = fac.createOMElement(getServiceMethod(), omNs);  
        request.addChild(rootElement);  

        return request;
        

	}
	
	private String buildLastPollElement(GeVo vo){
		String result = DateUtil.format(vo.getLastPoll(),"yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
		if("".equalsIgnoreCase(result)){
			result=StatusModes.NA.toString();
		}
		return result;
	}
	
	private OMElement buildUpdateUnit(SOAPFactory fac, OMNamespace omNs, String attName, String accountNo, String time, String value) {
		OMElement updateElement  = fac.createOMElement("Update", omNs);
        OMElement addressElement  = fac.createOMElement("Address", omNs);
        OMElement attElement  = fac.createOMElement("AttributeName", omNs);
        OMElement aliasElement  = fac.createOMElement("ComponentAlias", omNs);
        
        OMElement scaElement  = fac.createOMElement("Scalar", omNs);
        OMElement quaElement  = fac.createOMElement("Quality", omNs);
        OMElement timeElement  = fac.createOMElement("Timestamp", omNs);
        OMElement valueElement  = fac.createOMElement("Value", omNs);
        
        attElement.addChild(fac.createOMText(attElement, attName));
        aliasElement.addChild(fac.createOMText(aliasElement, accountNo));
        
        quaElement.addChild(fac.createOMText(quaElement, "0"));
        timeElement.addChild(fac.createOMText(timeElement, time));
        valueElement.addChild(fac.createOMText(valueElement, value));
        
        addressElement.addChild(attElement);
        addressElement.addChild(aliasElement);
        
        scaElement.addChild(quaElement);
        scaElement.addChild(timeElement);
        scaElement.addChild(valueElement);
        
        updateElement.addChild(addressElement);
        updateElement.addChild(scaElement);
		return updateElement;
	}
	
	/**
	 * Function invoke the GridPoint Webservice authentication service and get the response SOAPEnvelope,
	 * also retrieve the cookie with the request
	 * @return SOAPEnvelope instance
	 * @throws Exception
	 */
	public MessageContext updateScalarValues(GeVo vo) throws Exception{

		OperationClient operationClient=Axis2Util.createOperationClient(getServiceURL(), getSoapActionURI(), Axis2Util.createSOAPEnvelope(buildMessageEnvlop(vo)), null);
		return Axis2Util.getResponseMessageContext(operationClient);
	}
	
	//---------------------------------------------------------------Setter and Getter--------------------------------------------------
	
	public String getSoapActionURI() {
	return getServiceNameSpace()+getServiceMethod();
	}
	public static void main(String args[]) throws Exception{
		GeClient cli = new GeClient(null,null,null);
		GeVo vo = new GeVo();
		cli.updateScalarValues(vo);
		
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}

	public String getServiceNameSpace() {
		return serviceNameSpace;
	}

	public void setServiceNameSpace(String serviceNameSpace) {
		this.serviceNameSpace = serviceNameSpace;
	}

}
