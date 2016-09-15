package com.akuacom.pss2.history;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.WebContext;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.history.vo.EventVo;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.pss2.history.vo.UsageDataTransferVo;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;

@Stateless
@WebService(endpointInterface = "com.akuacom.pss2.history.OperatorReportUsageDataServicer", serviceName = "OperatorReportUsageDataServicer")
@WebContext(authMethod="BASIC",  contextRoot = "/opt.report/ws" )
@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC)
public class OperatorReportUsageDataServicerBean implements
OperatorReportUsageDataServicer.R, OperatorReportUsageDataServicer.L {
	
	@EJB
	HistoryReportManager.L historyReportManager;
	
	private ThreadLocal<WebServiceContext> threadSafeContext = new ThreadLocal<WebServiceContext>();
    @Resource
    public void setContext(WebServiceContext context){
    	threadSafeContext.set(context);
    }

    private static final Logger log = Logger
            .getLogger(OperatorReportUsageDataServicer.class);

    
    @javax.jws.WebMethod()
    public UsageDataTransferVo getReportDataEntries(List<String> partNames, String strDate, String eventName) {
    	UsageDataTransferVo vo = new UsageDataTransferVo();
    	
       String loginUser = threadSafeContext.get().getUserPrincipal().getName();
   	   boolean accessAble = (partNames==null||partNames.isEmpty())?true:false;
   	   String targetname = null;
   	   for(String partName : partNames){
   		   accessAble = accessCheck(partName, loginUser);
   		   if(!accessAble){
   			targetname = partName;
   			   break;
   		   }
   	   }
   	   	
   	   if(!accessAble) {
   		   log.error("Access denied error: "+loginUser +" tried to access "+targetname+"'s usage data at "+new Date());
      		
   		   throw new EJBException("Access denied error: "+loginUser +" tried to access "+targetname+"'s usage data at "+new Date());
   	   }
    	
    	
    	if(eventName!=null&&!eventName.trim().equals("")){
    		//search by event
    		ReportEvent event = new ReportEvent();
    		event.setEventName(eventName);
    		List<ParticipantVO> participants = null;
    		try {
    			participants = historyReportManager.getParticipantsForEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(participants==null||participants.isEmpty()) return vo;
			
			partNames = new ArrayList<String>();
			for(ParticipantVO pvo : participants){
				partNames.add(pvo.getParticipantName());
			}
			
    	}else if(partNames!=null&&!partNames.isEmpty()){
    		//search by participant,obtain all related participants of the aggegator
    		List<String> allPartNames = new ArrayList<String>();
    		for(String name : partNames){
    			allPartNames.addAll(historyReportManager.findAggParticipantNames(name));
    		}
    		partNames = allPartNames;
    		
    	}else{
    		// currently only support search by event or participant 2 methods
    		return vo;
    	}
    	
    	Date date = DateUtil.parseStringToDate(strDate, new SimpleDateFormat("yyyy-MM-dd"));
    	
		List<PDataEntry>  forcastList = historyReportManager.findForecastUsageDataEntryList(partNames, date);
		List<PDataEntry>  usageList = historyReportManager.findRealTimeUsageDataEntryList(partNames, date);
		
    	formatNumber(forcastList);
    	formatNumber(usageList);
    	
    	vo.setBaselineList(forcastList);
    	vo.setUsagelineList(usageList);
    	
    	if(eventName!=null&&!eventName.trim().equals("")){
    		ReportEvent event = null;
			try {
				event = historyReportManager.getEventByName(eventName.trim());
			} catch (Exception e) {
				log.debug(LogUtils.createExceptionLogEntry(null, null, e));
			}
			
			if(event==null) return vo;
    		
    		List<EventVo> eventPeriodList = new ArrayList<EventVo>();
    		EventVo evo = new EventVo();
			evo.setEventName(event.getEventName());
			evo.setStartTime(event.getStartTime());
			evo.setEndTime(event.getEndTime());
			eventPeriodList.add(evo);
			vo.setEventPeriodList(eventPeriodList);
			
			return vo;
    	}
    	
    	List<ReportEvent> events = null;
		try {
			events = historyReportManager.getRelatedEventsForparticipant(partNames, date);
		} catch (Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}
    	
    	if(events!=null&&(!events.isEmpty())){
    		List<EventVo> eventPeriodList = new ArrayList<EventVo>();
    		ReportEvent e = events.iterator().next();//currently only support one event per day.
			EventVo evo = new EventVo();
			evo.setEventName(e.getEventName());
			evo.setStartTime(e.getStartTime());
			evo.setEndTime(e.getEndTime());
			eventPeriodList.add(evo);
			
    		vo.setEventPeriodList(eventPeriodList);
    	}
        return vo;
    }
    

	private void formatNumber(List<PDataEntry> forcastList) {
		if(forcastList==null||forcastList.isEmpty()) return;
		int maxiFractionDigits = 3;
        NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(maxiFractionDigits);
		for(PDataEntry entry : forcastList){
			entry.setValue(Double.valueOf(nf.format(entry.getValue())));
		}
	}
	
    private boolean accessCheck(String partName,
			String loginUser) {
		if (threadSafeContext.get().isUserInRole("Admin")||
				threadSafeContext.get().isUserInRole("Operator") ||
				threadSafeContext.get().isUserInRole("Readonly")|| 
				threadSafeContext.get().isUserInRole("Dispatcher")) {
    		 return true;
         }
    	if(partName.equals(loginUser)) {
    		return true;
    	}else{
    	List<String> children = historyReportManager.findAggParticipantNames(loginUser);
    		if(children.contains(partName)){
    			return true;
    		}
    	}
		return false;
	}

}