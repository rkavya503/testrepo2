package com.akuacom.pss2.grippoint.wsclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.axiom.soap.SOAPEnvelope;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RetrieveResponseBean {
	private boolean isSuccessful;
	private String meesage;
	private Map<Date, Double> map;
	
	public RetrieveResponseBean(SOAPEnvelope resEnvelope) throws Exception{
		map = new TreeMap<Date, Double>();//new HashMap();
		//************************
		handleError(resEnvelope);
		if(!isSuccessful){
			return;
		}
		//****************************
		NodeList nodes = XPathQueryUtil.parse2Nodes("//rv[count(*)>1]", resEnvelope.toString());

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			String strDate=XPathQueryUtil.getAttribute(node, "Datestamp");
			String strValue=XPathQueryUtil.getAttribute(node,"Main_x0020_Load_x007C_KW");
			
			if (strDate==null || strDate=="" || strValue==null || strValue=="")
				continue;
			
			Date date = format.parse(XPathQueryUtil.getAttribute(node, "Datestamp"));
			Double value = Double.valueOf(XPathQueryUtil.getAttribute(node,"Main_x0020_Load_x007C_KW"));
			
			map.put(date, value);
		}
	}

	private void handleError(SOAPEnvelope resEnvelope)
			throws Exception {
		NodeList nodes = XPathQueryUtil.parse2Nodes("//error", resEnvelope.toString());
		isSuccessful = true;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			isSuccessful = false;
			meesage= XPathQueryUtil.getAttribute(node, "errorMessage");
		}
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	public String getMeesage() {
		return meesage;
	}
	public void setMeesage(String meesage) {
		this.meesage = meesage;
	}
	public Map<Date, Double> getMap() {
		return map;
	}
	public void setMap(Map<Date, Double> map) {
		this.map = map;
	}
	
}
