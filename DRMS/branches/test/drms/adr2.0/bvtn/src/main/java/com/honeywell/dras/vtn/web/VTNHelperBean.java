package com.honeywell.dras.vtn.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.jboss.logging.Logger;

import com.honeywell.dras.vtn.dras.service.VtnDrasService;
@Stateless
public  class VTNHelperBean implements VTNHelper.L,VTNHelper.R {

	private static String vtnDrasServiceUrl = "com.honeywell.dras.vtn.dras.service.VtnDrasServiceUrl";
	private VtnDrasService vtnDrasService=null;
	
	private Logger log = Logger.getLogger(VTNHelperBean.class);
	
	@Override
	public VtnDrasService getVtnDrasService() {
    	//If the vtnDrasService already exists just return the member variable instance
    	if(vtnDrasService==null){
	    	URL url = null;
			try {
				url = getVtnDrasServiceUrl();
			} catch (Exception e) {
				log.error("Exception in getting VtnService url !!! "+e);
			}
			QName qname = new QName("http://dras.honeywell.com/services/api/ports/VtnDrasService","VtnDrasService");
			Service service = Service.create(url, qname);
			// Extract the endpoint interface, the service "port".
			vtnDrasService = service.getPort(VtnDrasService.class);
    	}
		return vtnDrasService;
    }
    
    
    public static URL getVtnDrasServiceUrl() throws MalformedURLException, IOException{
		return new URL(System.getProperty(vtnDrasServiceUrl));
	}
	
}
