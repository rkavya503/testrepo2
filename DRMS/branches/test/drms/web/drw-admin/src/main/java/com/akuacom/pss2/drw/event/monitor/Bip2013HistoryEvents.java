package com.akuacom.pss2.drw.event.monitor;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.admin.FDUtils;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.utils.lang.DateUtil;

public class Bip2013HistoryEvents extends AbstractHistoryEvents {
	
	private static final long serialVersionUID = 3110650994513523771L;

	@Override
	protected String getExportContent() throws Exception{
		List<EventDetail> contents =  doGetContents();
 		StringBuffer exportContent = new StringBuffer();
 		
 		// append title for SDP event
 		exportContent.append("Issue Date & Time,Start Date&Time, End Date&Time,Dispatch Type,Dispatch Location#,Dispatch Location,Comments"+"\n\t");
 		//append the first row 
 		for(EventDetail event : contents){
			exportContent.append(" "+DateUtil.format(event.getEvent().getIssuedTime(),"yyyy-MM-dd HH:mm:ss") + ","
					+" "+ DateUtil.format(event.getEvent().getStartTime(),"yyyy-MM-dd HH:mm:ss") +","
					+" "+ DateUtil.format(event.getActualEndTime(),"yyyy-MM-dd HH:mm:ss") +","
					+ (event.getLocation()!=null?event.getLocation().getType():event.getAllLocationType()) + ","
					+ " "+(event.getLocation()!=null?event.getLocation().getNumber():"-") + ","
					+ (event.getLocation()!=null?event.getLocation().getName():"ALL") + ","
					+ FDUtils.filterSpecialCharacters(event.getEvent().getComment()));
			exportContent.append("\n\t");
 		}
	 
		exportContent.append("\n\t");
	  
		return exportContent.toString();
	}
	
	public String getRates() {
		return "BIP2013"; // product type
	}
}
