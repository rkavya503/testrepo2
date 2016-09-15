package com.akuacom.pss2.grippoint.wsclient;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.CommonsTransportHeaders;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class AuthResponseBean {
	private static final String TRANSPORT_HEADERS = "TRANSPORT_HEADERS";
	private static final String SET_COOKIE = "Set-Cookie";
	private String loginResult="";
	private String cookie;

	public AuthResponseBean(MessageContext inMsgtCtx) throws Exception{
		
		NodeList nodes = XPathQueryUtil.parse2Nodes("//loginResponse", inMsgtCtx.getEnvelope().toString());
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			loginResult= XPathQueryUtil.getAttribute(node, "loginResult");
		}
		
		org.apache.axis2.transport.http.CommonsTransportHeaders header = (CommonsTransportHeaders) inMsgtCtx.getProperty(TRANSPORT_HEADERS);//inMsgtCtx.getProperties().get("TRANSPORT_HEADERS");
		cookie = (String) header.get(SET_COOKIE);
	}

	public String getLoginResult() {
		return loginResult;
	}

	public String getCookie() {
		return cookie;
	}
	
	public boolean hasCookies() {
		return (cookie!=null&&cookie.trim().length()>0);
	}

}
