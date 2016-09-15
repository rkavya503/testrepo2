/**
 * 
 */
package com.akuacom.pss2.history.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * the class ReportSignalMaster
 *
 */
public class ReportSignalMaster implements Serializable {

	private static final long serialVersionUID = -225713520147040802L;
	
	Date signalTime;
	String mode;
	String pending;
	String price;
	String bid;
	String CPPPrice;

	private Map<String,ReportSignalDetail> itemMap= new HashMap<String,ReportSignalDetail>();
	
	public Date getSignalTime() {
		return signalTime;
	}
	public void setSignalTime(Date signalTime) {
		this.signalTime = signalTime;
	}
	
	public String getMode(){
		if (itemMap.containsKey(SignalNameConstants.MODE_SIGNAL_NAME))
			return ((ReportSignalDetail)itemMap.get(SignalNameConstants.MODE_SIGNAL_NAME)).getSignalValue();
		return null;
	}

	public String getPending(){
		if (itemMap.containsKey(SignalNameConstants.PENDING_SIGNAL_NAME))
			return ((ReportSignalDetail)itemMap.get(SignalNameConstants.PENDING_SIGNAL_NAME)).getSignalValue();
		return null;
	}

	public String getPrice(){
		if (itemMap.containsKey(SignalNameConstants.PRICE_SIGNAL_NAME))
			return ((ReportSignalDetail)itemMap.get(SignalNameConstants.PRICE_SIGNAL_NAME)).getSignalValue();
		return null;
	}

	public String getBid(){
		if (itemMap.containsKey(SignalNameConstants.BID_SIGNAL_NAME))
			return ((ReportSignalDetail)itemMap.get(SignalNameConstants.BID_SIGNAL_NAME)).getSignalValue();
		return null;
	}
	
	public String getCPPPrice(){
		if (itemMap.containsKey(SignalNameConstants.CPP_PRICE_SIGNAL_NAME))
			return ((ReportSignalDetail)itemMap.get(SignalNameConstants.CPP_PRICE_SIGNAL_NAME)).getSignalValue();
		return null;
	}
	
	public Map<String, ReportSignalDetail> getItemMap() {
		return itemMap;
	}
}
