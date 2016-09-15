package com.akuacom.pss2.drw.admin.mock;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.drw.admin.ProgramBackingBean;
import com.akuacom.pss2.drw.admin.constants.DRWConstants;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

public class DRWMockDataManager {
	static PSS2Features features;
	
	/**
	 * Function for mock initialize the programs data
	 * @return List<ProgramBackingBean>
	 */
	public static List<ProgramBackingBean> mockPrograms(){
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
		features = systemManager.getPss2Features();
		
		ProgramBackingBean bean1 = new ProgramBackingBean();
		bean1.setProgramName("SDP");
		bean1.setProgramClass("Summer Discount Plan");
		bean1.setIndex(1);
		bean1.setEventURL(DRWConstants.SDP_EVENT_URL);
		ProgramBackingBean bean2 = new ProgramBackingBean();
		bean2.setProgramName("API");
		bean2.setProgramClass("Agriculture & Pumping Interruptible");
		bean2.setIndex(2);
		bean2.setEventURL(DRWConstants.API_EVENT_URL);
		ProgramBackingBean bean3 = new ProgramBackingBean();
		bean3.setProgramName("BIP");
		bean3.setProgramClass("Base Interruptible Program");
		bean3.setIndex(3);
//		bean3.setEventURL(DRWConstants.BIP_EVENT_URL);
		bean3.setEventURL(DRWConstants.BIP2013_EVENT_URL);
		List<ProgramBackingBean> programList = new ArrayList<ProgramBackingBean>();
		//public static final String SDP_EVENT_URL="/drw.admin/jsp/sdp/eventSDP.jsf";
		// http://localhost:8080/drw.admin/jsp/sdp/historyEvt/historyEvents.jsf
				//http://localhost:8080/drw.admin/jsp/sdp/activeEvt/activeEvents.jsf
		bean1.setHistoryEventURL("/drw.admin/jsp/sdp/historyEvt/historyEvents.jsf?programName=SDP");
		bean1.setActiveEventURL("/drw.admin/jsp/sdp/activeEvt/activeEvents.jsf?programName=SDP");
		bean3.setBipProgram(Boolean.FALSE);
		
		bean2.setHistoryEventURL("/drw.admin/jsp/api/historyEvt/historyEvents.jsf?programName=API");
		bean2.setActiveEventURL("/drw.admin/jsp/api/activeEvt/activeEvents.jsf?programName=API");
		bean2.setBipProgram(Boolean.FALSE);
		
		bean3.setHistoryEventURL("/drw.admin/jsp/bip/historyEvt/historyEvents.jsf?programName=BIP");
		bean3.setActiveEventURL("/drw.admin/jsp/bip/activeEvt/activeEvents.jsf?programName=BIP");
		
		bean3.setHistoryEventURL("/drw.admin/jsp/bip2013/historyEvt/historyEvents.jsf?programName=BIP");
		bean3.setActiveEventURL("/drw.admin/jsp/bip2013/activeEvt/activeEvents.jsf?programName=BIP");
		bean3.setBipProgram(Boolean.TRUE);
		
//		if(features.isBIP2013Enabled()){
//			//disable the original bip event creation and active events monitor page, 
//			bean3.setActiveEventURLStyle("inactive");
//			bean3.setEventURLStyle("inactive");
//			bean3.setActiveEventURL("javascript:void(0);");
//			bean3.setEventURL("javascript:void(0);");
//		}else{
//			bean4.setActiveEventURLStyle("inactive");
//			bean4.setEventURLStyle("inactive");
//			bean4.setHistoryEventURLStyle("inactive");
//			bean4.setActiveEventURL("javascript:void(0);");
//			bean4.setEventURL("javascript:void(0);");
//			bean4.setHistoryEventURL("javascript:void(0);");
//		}
		
		
		programList.add(bean1);
		programList.add(bean2);
		programList.add(bean3);
//		programList.add(bean4);
		
		return programList;
	}
}
