/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.kwickview;

import com.schneider_electric.webservices.ArrayOfCurtailmentEvent;
import com.schneider_electric.webservices.CurtailmentEvent;
import com.schneider_electric.webservices.CurtailmentResponse;
import com.schneider_electric.webservices.CurtailmentService;
import com.schneider_electric.webservices.CurtailmentServiceSoap;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author spierson
 */
public class KWickviewCurtailmentClient {
    
    CurtailmentServiceSoap KWickview;
    
    URL wsdlURL;
    QName serviceName = new QName("http://www.schneider-electric.com/WebServices","CurtailmentService");
    String loginName;
    String password;
    String program;
    
    public KWickviewCurtailmentClient(String user, String pwd, String url) {
        
        try {
            wsdlURL = new URL(url); 
            loginName = user;
            password = pwd;
            CurtailmentService service = new CurtailmentService(wsdlURL, serviceName);      
            KWickview = service.getCurtailmentServiceSoap();                
        }
        catch (Exception ex) {
            Logger.getLogger(KWickviewCurtailmentClient.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }
    
    public KWickviewResponse checkForEvents(String programID) throws Exception {
        try {
            CurtailmentResponse resp = KWickview.checkForCurtailmentEvents(loginName, password, programID);
            KWickviewResponse kwresp = new KWickviewResponse(resp);
            return kwresp;
        } catch (SOAPFaultException sfe) {
            String err = "Error contacting KWickview webservice";
            Logger.getLogger(KWickviewCurtailmentClient.class.getName()).log(Level.SEVERE, err, sfe);
            throw new Exception(sfe);
        }
    }
    
    public void confirmEvents(KWickviewResponse kwResp, boolean eventsAccepted) {
        KWickview.curtailmentEventsRecieved(loginName, password, kwResp.getRequestID(), eventsAccepted? 1:0);
    }
    
    
    public static void main(String[] args) {
        //String url = "http://epotest.energyinteractive.com/sdgeepo/webService/CurtailmentWebService/CurtailmentService.asmx?WSDL";
        String url = "http://localhost:8084/KWickviewSimulator/CurtailmentService?wsdl";

        KWickviewCurtailmentClient client = new KWickviewCurtailmentClient("spierson", "pierson", url);
        try {
            KWickviewResponse kwresp = client.checkForEvents("CPPD");
            System.out.println(kwresp);
        } catch (Exception ex) {
            Logger.getLogger(KWickviewCurtailmentClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
}
