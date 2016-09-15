package com.akuacom.drms.system;

import static org.junit.Assert.*;

import java.net.URL;
import java.net.HttpURLConnection;

import obix.net.HttpConnection;

import org.junit.Ignore;
import org.junit.Test;

public class WebappTest {

	private static final String NOSSL_REST = "nossl/rest";


	public void testPage(String context,String path,int responseCode) throws Exception {
		StringBuffer urlValue = new StringBuffer("http://localhost:8080/");
		urlValue.append(context);
		if(path != null){
			urlValue.append("/") ;
			urlValue.append(path);
					
		}
		URL url = new URL(urlValue.toString());
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		assertEquals(urlValue.toString(),responseCode, connection.getResponseCode());
	}

	public void testWebService(String webServiceName) throws Exception{
		testWebService(webServiceName,true);
	}
	public void testWebService(String webServiceName,boolean requiresAuth) throws Exception{
		int responseCode;
		if(requiresAuth){
			responseCode = HttpURLConnection.HTTP_MOVED_TEMP;
		}else{
			responseCode = HttpURLConnection.HTTP_OK;
		}
		testPage(webServiceName,webServiceName,responseCode);
	}
	public void testPage(String context) throws Exception{
		this.testPage(context,null,HttpURLConnection.HTTP_OK);
	}
	@Test
	public void testFacdash() throws Exception {
		testPage("facdash");
	}

	@Test
	@Ignore
	public void testAkuaUtilOpWs() throws Exception {
		testWebService("AkuaUtilityOperatorWS");
	}

	@Test
	@Ignore
	public void testParticipantOperatorWS() throws Exception {
		testWebService("ParticipantOperatorWS");
	}

	@Test
	@Ignore("ignoring until 401 is resolved")
	public void testUtilityOperatorWS() throws Exception {
		testWebService("UtilityOperatorWS");
	}

	@Test
	@Ignore("update with an endpoint that exists")
	public void testSdgeCpp()  throws Exception{
		testWebService("pss2.sdgecpp");
	}

	@Test
	@Ignore("update with a valid endpoint")
	public void testDRASClientWS() throws Exception {
		testWebService("DRASClientWS");
	}

	@Test
	@Ignore
	public void testLegacy() throws Exception {
		testWebService("PSS2WS");
	}

	@Test
	@Ignore
	public void testRestClientWS() throws Exception {
		testPage("RestClientWS",NOSSL_REST,HttpURLConnection.HTTP_UNAUTHORIZED);
	}

	public void testObixServer() throws Exception {
		testWebService("obixserver");
	}
	@Test
	@Ignore
	public void testSoapClientWs() throws Exception {
		testWebService("SOAPClientWS");
	}
	
	@Test
	@Ignore("update with an existing endpoint")
	public void testItronPgeDbp()  throws Exception{
		testPage("pss2.program.autodbp");
	}

	@Test
	@Ignore("update with an existing endpoint")
	public void testRESTAPIUtilOpWS()  throws Exception{
		String context = "RESTAPIUtilOpWS";
		testPage(context,NOSSL_REST,HttpURLConnection.HTTP_UNAUTHORIZED);
	}
	
	@Test
	public void testPss2Website()  throws Exception{
		testPage("pss2.website");
	}

}
