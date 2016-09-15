package com.akuacom.drms.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;
import org.junit.Test;

import com.akuacom.pss2.grippoint.wsclient.AuthClient;
import com.akuacom.pss2.grippoint.wsclient.AuthResponseBean;
import com.akuacom.pss2.grippoint.wsclient.RetrieveDataClient;
import com.akuacom.pss2.grippoint.wsclient.RetrieveResponseBean;

/**
 * Unit test for simple App.
 */
public class GridpointWSClientTest
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	@Test
    public void AuthTest()
    {
		String url = "https://admtools-admview.admmicro.net/ADMTools/auth/wslogin.asmx";
		String method = "login";
		String username = "Honeywell";
		String psw = "hwell$123";
		String retrieveDataURL = "https://admtools-admview.admmicro.net/ADMTools/admview/admdata.asmx";
		String nameSpace = "http://www.admmicro.net/";
		String retrieveDataMethod = "getlpdata";
		String siteId = "TGT2488Int15";
		String startDate = "2011-11-17T00:00:00";
		String endDate = "2011-11-17T03:00:00";
		String dst = "true";
    	AuthClient authenticationInvoker = new AuthClient(nameSpace, url, method);
		
		try {
			// 1. authentication process 
			MessageContext authenticationResult = authenticationInvoker.authentication(username, psw);
			AuthResponseBean bean = new AuthResponseBean(authenticationResult);
			assertEquals("ok",bean.getLoginResult());
			assertTrue(bean.hasCookies());
			
			if("ok".equals(bean.getLoginResult())&&bean.hasCookies()){
				// 2. retrieve data with the authentication cookie has been obtained.
				RetrieveDataClient retrieveDataInvoker = new RetrieveDataClient(nameSpace, retrieveDataURL, retrieveDataMethod);
				SOAPEnvelope resEnvelope = retrieveDataInvoker.retrieveData(bean.getCookie(), siteId, startDate, endDate, dst);
				System.out.println(resEnvelope);
				RetrieveResponseBean resBean = new RetrieveResponseBean(resEnvelope);
				if(resBean.isSuccessful()){
					Map<Date, Double> map = resBean.getMap();
					System.out.println(map);
				}else{
					System.out.println(resBean.getMeesage());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
