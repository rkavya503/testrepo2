
package com.akuacom.pss2.drw.cache;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.CacheCategory;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.StatusCache;
import com.akuacom.pss2.drw.service.impl.EventListViewManagerImpl;
import com.akuacom.pss2.drw.util.DRWUtil;


public class DRTimerTask extends java.util.TimerTask{ 
	
	private static final Logger log = Logger.getLogger(DRTimerTask.class);
	
	public DRTimerTask(ServletContext ctx){
		this.ctx = ctx;
	}
	
	private int rtptime = 0;
	private int scheduletime = 0;
	
	@SuppressWarnings("unused")
	private ServletContext ctx;
	@Override
	public void run() {
		try{
			updateStatus();
		}catch(Exception e){
			log.error("DR Website retrieve data timer catch error: " +e.getMessage());
		}
	}
	private void updateScheduleStatus(){
		//1, retrieve schedule events from database 1 minutes by timer
		//2, check events which issue time from < now to >now
		//3, update cache status for specified scheduled events cache
//		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
//		boolean needUpdate = manager.scheduledEventsNeedUpdate();
//		if(needUpdate){
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
			
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_API_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_BIP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_CBP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_SDPRESI_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_SDPCOME_EVENT, current);
			
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_RESIDENTIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SPD_RESIDENTIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_COMMERCIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.DBP_COMMERCIAL_SCHEDULED, current);
//		}
	}
	
	private void updateActiveStatus(){
		//1, retrieve schedule events from database 1 minutes by timer
		//2, check events which issue time from < now to >now
		//3, update cache status for specified scheduled events cache
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		boolean needUpdate = manager.activeEventsNeedUpdate();
		if(needUpdate){
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_API_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_BIP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPRESI_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPCOME_EVENT, current);
			
		}
	}
	
	
	
	private void updateStatus(){
		// handle  events;
		
		//Residential
		updateResidentialSAIEventStatus();
		updateResidentialSDPEventStatus();
		updateResidentialSPDEventStatus();
		
		//Commercial
		updateCommercialAPIEventStatus();
		updateCommercialSDPEventStatus();
		updateCommercialSAIEventStatus();
		updateCommercialBIPEventStatus();
		updateCommercialCBPEventStatus();
		updateCommercialDBPEventStatus();
		updateCommercialDRCEventStatus();
		
		//RTP
		rtptime += 10000;
		if(rtptime==10*60*1000){
			StatusCache.getInstance().setCacheStatus(CacheCategory.RTP_FORCAST, new Date());
			rtptime= 0;
		}
		//shedule
		scheduletime += 10000;
		if(scheduletime>=30000){
//			updateScheduleStatus();
//			updateActiveStatus();
			scheduletime= 0;
		}
		updateCountyAndCityList();
		StatusCache.getInstance().setCacheStatus(CacheCategory.LOCATION, new Date());
		
	}
	public static void updateCountyAndCityList(){
		List<String> countys = EventListViewManagerImpl.getInstance().retrieveCountyList();
		if(countys!=null){
			DRWUtil.sortList(countys);
			EventCache.getInstance().setCountyList(countys);
			EventCache.getInstance().getCityMap().clear();
			for(String county:countys){
				List<String> cityList = EventListViewManagerImpl.getInstance().retrieveCityList(county);
				DRWUtil.sortList(cityList);
				EventCache.getInstance().getCityMap().put(county, cityList);
			}
		}
		List<String> cities = EventListViewManagerImpl.getInstance().retrieveCityList();
		if(cities!=null){
			EventCache.getInstance().setCityList(cities);	
		}
	}
	
	//API
	private void updateCommercialAPIEventStatus(){
		List<BaseEventDataModel> scheduleAPICommEvents = EventCache.getInstance().getScheApiEvents().getValueList();
		List<BaseEventDataModel> activeAPICommEvents = EventCache.getInstance().getActApiEvents().getValueList();
		
		boolean scheduleAPICommEventsFlag = false;
		boolean activeAPICommEventsFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleAPICommEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
				if(startTime.before(current)){
					
					
					scheduleAPICommEventsFlag = true;
				}
			}
			
		}
		// active - > history 
		for(BaseEventDataModel event :activeAPICommEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				//status cache update
				
				
				activeAPICommEventsFlag = true;
			}
			}
		}
		if(scheduleAPICommEventsFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_API_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_API_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeAPICommEventsFlag){

			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_API_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//BIP Commercial
	private void updateCommercialBIPEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheBipEvents().getValueList();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActBipEvents().getValueList();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_BIP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_BIP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_BIP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//SDP Residential
	private void updateResidentialSDPEventStatus(){
		List<BaseEventDataModel> scheduleSDPResiEvents = EventCache.getInstance().getScheSdprEvents().getValueList();
		List<BaseEventDataModel> activeSDPResiEvents = EventCache.getInstance().getActSdprEvents().getValueList();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleSDPResiEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeSDPResiEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPRESI_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_SDPRESI_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPRESI_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}

	private void updateResidentialSPDEventStatus(){
		List<BaseEventDataModel> scheduleSPDResiEvents = EventCache.getInstance().getScheduleSPDResiEvents();
		List<BaseEventDataModel> activeSPDResiEvents = EventCache.getInstance().getActiveSPDResiEvents();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleSPDResiEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeSPDResiEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SPD_RESIDENTIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SPD_RESIDENTIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SPD_RESIDENTIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//SDP Commercial
	private void updateCommercialSDPEventStatus(){
		List<BaseEventDataModel> scheduleSDPCommEvents = EventCache.getInstance().getScheSdpcEvents().getValueList();
		List<BaseEventDataModel> activeSDPCommEvents = EventCache.getInstance().getActSdpcEvents().getValueList();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleSDPCommEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeSDPCommEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPCOME_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_SDPCOME_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_SDPCOME_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	
	//SAI Residential
	private void updateResidentialSAIEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheduleSAIResiEvents();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActiveSAIResiEvents();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_RESIDENTIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_RESIDENTIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_RESIDENTIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//SDP Commercial
	private void updateCommercialSAIEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheduleSAICommEvents();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActiveSAICommEvents();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_COMMERCIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.SAI_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	
	

	//CBP Commercial
	private void updateCommercialCBPEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheCbpEvents().getValueList();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActCbpEvents().getValueList();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_CBP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.SCHE_CBP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.ACT_CBP_EVENT, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//DBP Commercial
	private void updateCommercialDBPEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheduleDBPCommEvents();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActiveDBPCommEvents();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.DBP_COMMERCIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.DBP_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.DBP_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}
	//DRC Commercial
	private void updateCommercialDRCEventStatus(){
		List<BaseEventDataModel> scheduleEvents = EventCache.getInstance().getScheduleDRCCommEvents();
		List<BaseEventDataModel> activeEvents = EventCache.getInstance().getActiveDRCCommEvents();
		boolean scheduleToActiveFlag = false;
		boolean activeToHistoryFlag = false;
		// schedule - > active 
		for(BaseEventDataModel event :scheduleEvents ){
			Date startTime = event.getEvent().getStartTime();
			Date current = new Date();
			if(startTime!=null&&current!=null){
			if(startTime.before(current)){
				
				
				scheduleToActiveFlag = true;
			}
			}
		}
		// active - > history 
		for(BaseEventDataModel event :activeEvents ){
			Date endTime = event.getEvent().getEndTime();
			Date current = new Date();
			if(endTime!=null&&current!=null){
			if(endTime.before(current)){
				
				
				activeToHistoryFlag = true;
			}
			}
		}
		if(scheduleToActiveFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.DRC_COMMERCIAL_SCHEDULED, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.DRC_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_SCHEDULED, current);
		}
		else if(activeToHistoryFlag){

			//status cache update
			Date current = new Date();
			StatusCache.getInstance().setCacheStatus(CacheCategory.DRC_COMMERCIAL_ACTIVE, current);
			StatusCache.getInstance().setCacheStatus(CacheCategory.KIOSK_ACTIVE, current);
		}
	}

	

}