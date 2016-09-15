package com.akuacom.pss2.drw.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.jsf.DRFrameBackingBean;
import com.akuacom.pss2.drw.jsf.HistoryUIBackingBean;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.RTPEventDataModel;
import com.akuacom.pss2.drw.service.EventManager;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.validator.PREventDataModelValidator;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;

public class EventManagerImpl implements EventManager {
	/** logger **/
	private static final Logger log = Logger.getLogger(EventManagerImpl.class);
	/** class instance **/
	private static EventManagerImpl instance = new EventManagerImpl();	
	/**
	 * Constructor
	 */
	private EventManagerImpl(){
		super();
		initialize();
	}
	/**
	 * Singleton function for get class instance
	 * @return
	 */
	public static EventManagerImpl getInstance(){
		return instance;
	}	
	/**
	 * initialize business logic
	 */
	public void initialize(){
		//startDRTimer();
		List<Program> programList = retrieveAllPrograms();
		EventCache.getInstance().setProgramsCache(programList);
		EventCache.getInstance().getProductAPIList().clear();
		EventCache.getInstance().getProductBIPList().clear();
		EventCache.getInstance().getProductCBPList().clear();
		EventCache.getInstance().getProductDBPList().clear();
		EventCache.getInstance().getProductDRCList().clear();
		EventCache.getInstance().getProductSAIList().clear();
		EventCache.getInstance().getProductSDPList().clear();
		EventCache.getInstance().getProductRTPList().clear();
		for(Program program: programList){
			String programClass = program.getProgramClass();
			if(programClass!=null){
				if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_CBP)){
					EventCache.getInstance().getProductCBPList().add(program.getName());
				}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_SAI)){
					EventCache.getInstance().getProductSAIList().add(program.getName());
				}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_DBP)){
					EventCache.getInstance().getProductDBPList().add(program.getName());
				}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_DRC)){
					EventCache.getInstance().getProductDRCList().add(program.getName());
				}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_RTP)){
					EventCache.getInstance().getProductRTPList().add(program.getName());
				}
				//API
				else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_API)){
					EventCache.getInstance().getProductAPIList().add(program.getName());
				}
				//BIP
				else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_BIP)){
					EventCache.getInstance().getProductBIPList().add(program.getName());
				}
				//SDP
				else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_SDP)){
					EventCache.getInstance().getProductSDPList().add(program.getName());
				}
			}
		}
		
		retrieveCountyAndCityList();
	}
	public static void retrieveCountyAndCityList(){
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
	
	
	/**
	 * Function for start DR timer to retrieve data ,
	 * timer interval with 30 seconds
	 */
//	private void startDRTimer(){
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new DRTimerTask(),0,60000*5);
//	}	

	/**
	 * Function for initialize DR frame relative objects
	 */
	public void initializeDRFrame(DRFrameBackingBean prEventBackingBean) {
		String dateFormat = DRWConstants.DATE_FORMATE;
		try{
			if(DRWUtil.getSystemManager()!=null){
				dateFormat = DRWUtil.getSystemManager().getPss2Features().getDateFormat();
			}
		}catch(Exception e){
			log.error("DR webstie event manager get system date format error: "+e.getMessage());
		}
		prEventBackingBean.setDateFomat(dateFormat);
	}

	/**
	 * Function for retrieve all programs object from system.
	 * @return	programs
	 */
	public List<Program> retrieveAllPrograms(){
		List<Program> programList = new ArrayList<Program>();
		try{
			StateMachineManagerImpl.getInstance().setGetProgramsSuccessFlag(false);
			if(DRWUtil.getCFEventManager()!=null){
				programList = DRWUtil.getCFEventManager().getAllProgram();
				StateMachineManagerImpl.getInstance().setGetProgramsSuccessFlag(true);
			}
		}catch(Exception e){
			log.error("DR webstie event manager get all programs error: "+e.getMessage());
		}
		return programList;
	}	
	/**
	 * Function for retrieve event value object list from system
	 * @param program:program name(API\BIP\SDP\SAI\CBP\DBP\DRC)
	 * @param isCommercial:	true->commercial;false->residential
	 * @param isActive:	true->active events;false->scheduled events
	 * @return	events
	 */
	public List<BaseEventDataModel> retrieveEvents(String program,boolean isCommercial,boolean isActive){
		List<BaseEventDataModel> eventList = new ArrayList<BaseEventDataModel>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				List<String> programs= new ArrayList<String>();
				List<EventValue> eventValueList = new ArrayList<EventValue>();
				programs.add(program);
				if(isCommercial){
					if(isActive){
						eventValueList = DRWUtil.getCFEventManager().getActiveEvent(programs,true);
					}else{
						eventValueList = DRWUtil.getCFEventManager().getScheduledEvent(programs,true);
					}
				}else{
					if(isActive){
						eventValueList = DRWUtil.getCFEventManager().getActiveEvent(programs,false);
					}else{
						eventValueList = DRWUtil.getCFEventManager().getScheduledEvent(programs,false);
					}
				}
				for(EventValue eventValue:eventValueList){
					eventList.add(new BaseEventDataModel(eventValue));
				}
			}
		}catch(Exception e){
			//log.error("DR webstie event manager get event status error: "+e.getMessage());
		}
		return eventList;
	}
	
	/**
	 * Function for retrieve RTP forecast from system
	 * @return	RTP forecast list
	 */
	public List<RTPEventDataModel> retrieveForecast(){
		List<RTPEventDataModel> eventList = new ArrayList<RTPEventDataModel>();
		try{
			if(DRWUtil.getCFEventManager()!=null){
				List<WeatherValue> results=DRWUtil.getCFEventManager().getForcast(new Date());
				for(WeatherValue eventValue:results){
					eventList.add(new RTPEventDataModel(eventValue));
				}
			}
		}catch(Exception e){
			//log.error("DR webstie event manager get RTP forcast error: "+e.getMessage());
		}
		return eventList;
	}
	
	public void initializeHistoryData(){
		
	}
	
	public List<ValidationException> searchHistoryEvent(HistoryUIBackingBean historyUIBackingBean){
		
			List<ValidationException> validationErrors = new ArrayList<ValidationException>();
			try{
				PREventDataModelValidator.startDateValidation(historyUIBackingBean.getStartDate());
			}catch(ValidationException e){
				validationErrors.add(e);
			}
			try{
				PREventDataModelValidator.endDateValidation(historyUIBackingBean.getEndDate(), historyUIBackingBean.getStartDate());
			}catch(ValidationException e){
				validationErrors.add(e);
			}try{
				PREventDataModelValidator.dateCompareValidation(historyUIBackingBean.getEndDate(), historyUIBackingBean.getStartDate());
			}catch(ValidationException e){
				validationErrors.add(e);
			}
			try{
				PREventDataModelValidator.searchInputValidation(historyUIBackingBean);
			}catch(ValidationException e){
				validationErrors.add(e);
			}
			try{
				PREventDataModelValidator.searchZipCodeValidation(historyUIBackingBean);
			}catch(ValidationException e){
				validationErrors.add(e);
			}
			
			if(validationErrors.size()>0){
				return validationErrors;
			}
			
			String programClass = historyUIBackingBean.getCurrentSelectProgram();
			String productName = historyUIBackingBean.getCurrentSelectProduct();
			String zipCode = historyUIBackingBean.getZipCode();
			List<String> zipCodeList = DRWUtil.listByComma(zipCode);
//			List<PREvent> result =null;
			List<EventValue> eventValueList = null;
			List<WeatherValue> resultRTP = null;
			
			boolean isAPI=false;
			
			if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_CBP)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_CBP);
				historyUIBackingBean.setBlockColumnDisplayFlag(true);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SAI)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SAI);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DBP)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DBP);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DRC)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DRC);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API)){
				isAPI=true;
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API);
				historyUIBackingBean.setBlockColumnDisplayFlag(true);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP);
				historyUIBackingBean.setBlockColumnDisplayFlag(true);
			}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_CLASS_NAME_SDP_C)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.PROGRAM_CLASS_NAME_SDP_C);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.PROGRAM_CLASS_NAME_SDP_R)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.PROGRAM_CLASS_NAME_SDP_R);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SDP)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SDP);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}else if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SPD)){
				historyUIBackingBean.setHistoryEventSearchTitle(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SPD);
				historyUIBackingBean.setBlockColumnDisplayFlag(false);
			}

			if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP)){
				try{
					resultRTP = DRWUtil.getCFEventManager().getHistoryTems(DRWConstants.RTP_PROGRAM_NAME,historyUIBackingBean.getStartDate(), historyUIBackingBean.getEndDate());
				}catch(Exception e){
					log.error("DR webstie event manager get RTP history records error: "+e.getMessage());
				}
				if(resultRTP!=null){
					historyUIBackingBean.setHistoryRTPEventsFlag(true);
					List<RTPEventDataModel> weathersWrapper = new ArrayList<RTPEventDataModel>();
					for(WeatherValue weather:resultRTP){
						RTPEventDataModel wrapper = new RTPEventDataModel(weather);
						weathersWrapper.add(wrapper);
					}
					historyUIBackingBean.getHistoryRTPEvents().setRtpList(weathersWrapper);
					
					historyUIBackingBean.setHistoryResultsVisibleFlag(true);
					
					historyUIBackingBean.setTotalResults(resultRTP.size());
					if(resultRTP.size()>0){
						historyUIBackingBean.setResultIndexFrom(1);
						if(resultRTP.size()<=50){
							historyUIBackingBean.setResultIndexTo(resultRTP.size());
						}else{
							historyUIBackingBean.setResultIndexTo(50);
						}
					}
					
				}else{
					historyUIBackingBean.setHistoryResultsVisibleFlag(false);
				}
			}else{
				if(productName.equalsIgnoreCase("All")){
					try{
						eventValueList = DRWUtil.getCFEventManager().getHistoryEvent(DRWConstants.convertClassNameToProgram(programClass),null,historyUIBackingBean.getStartDate(),historyUIBackingBean.getEndDate(),zipCodeList);
					}catch(Exception e){
						log.error("DR webstie event manager get events history record error: "+e.getMessage());
					}
				}else{
					try{
						eventValueList = DRWUtil.getCFEventManager().getHistoryEvent(DRWConstants.convertClassNameToProgram(programClass),productName,historyUIBackingBean.getStartDate(),historyUIBackingBean.getEndDate(),zipCodeList);
					}catch(Exception e){
						log.error("DR webstie event manager get events history record error: "+e.getMessage());
					}
				}
				if(eventValueList!=null){
					DRWUtil.sortEventList(eventValueList);
					historyUIBackingBean.setHistoryRTPEventsFlag(false);
					List<BaseEventDataModel> eventList =new ArrayList<BaseEventDataModel>();
					
					for(EventValue event:eventValueList){
						//DRMS-7508
						if(programClass.equalsIgnoreCase(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP)){
							event.setProduct("BIP");
						}
						BaseEventDataModel baseEventDataModel = new BaseEventDataModel(event);
						//DRMS-7470
						
						baseEventDataModel.setAPIHistoryFlag(isAPI);
						
						eventList.add(baseEventDataModel);
					}
					historyUIBackingBean.getHistoryBaseEvents().setEvents(eventList);
					historyUIBackingBean.setHistoryResultsVisibleFlag(true);
					
					historyUIBackingBean.setTotalResults(eventList.size());
					if(eventList.size()>0){
						historyUIBackingBean.setResultIndexFrom(1);
						if(eventList.size()<=50){
							historyUIBackingBean.setResultIndexTo(eventList.size());
						}else{
							historyUIBackingBean.setResultIndexTo(50);
						}
					}
					
				}else{
					historyUIBackingBean.setHistoryResultsVisibleFlag(false);
				}
				
				
				
			}
			return validationErrors;
		
	}
	
	public void exportCSV_EventHistory_Normal(HistoryUIBackingBean bean) throws IOException{
		List<BaseEventDataModel> historys = bean.getHistoryBaseEvents().getEvents();
		List<String> header = new ArrayList<String>();
		header.add("Product");
		header.add("Start Date");
		header.add("End Date");
		header.add("Start Time");
		header.add("End Time");
		if(bean.isBlockColumnDisplayFlag()){
			header.add("Block");
		}
		List<List<String>> contents = new ArrayList<List<String>>();
		
		for(BaseEventDataModel history:historys){
			List<String> content = new ArrayList<String>();
			content.add(history.getProduct());
			content.add("\""+history.getStartDateString()+"\"");
			content.add("\""+history.getEndDateString()+"\"");
			content.add(history.getEventStartTimeString());
			content.add(history.getEventEndTimeString());
			if(bean.isBlockColumnDisplayFlag()){
				if(history.getEvent()!=null){
					content.add("\""+history.getBlockStringWithoutCombine()+"\"");
				}
			}
			contents.add(content);
		}
		
		exportCSV(header,contents);
	}
	public void exportCSV_EventHistory_RTP(HistoryUIBackingBean bean) throws IOException{
		List<RTPEventDataModel> historys = bean.getHistoryRTPEvents().getRtpList();
		List<String> header = new ArrayList<String>();
		header.add("Date");
//		header.add("Temperature");
		header.add("Pricing Category");
		
		List<List<String>> contents = new ArrayList<List<String>>();
		for(RTPEventDataModel weather:historys){
			List<String> content = new ArrayList<String>();
			content.add("\""+weather.getDateString()+"\"");
//			content.add(weather.getTemperatureString());
//			content.add(weather.getWeather().getPricingCategory());
			content.add(weather.getPricingCategoryString());
			contents.add(content);
		}
		exportCSV(header,contents);
	}
	public void exportCSV(List<String> header,List<List<String>> contents) throws IOException{
		String filename = "EventsHistory.csv";
		
		//Setup the output
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        String fileContent = buildCSVContent(header,contents);       
        out.print(fileContent);
        out.close();
        fc.responseComplete();		
	}
	
	private String buildCSVContent(List<String> header,List<List<String>> contents){
		StringBuffer result=new StringBuffer();
		
		for(String headerCell:header){
			result.append(headerCell).append(",");
		}
		result.replace(result.length()-1, result.length(),"\n");
		
		for(List<String> contentRow:contents){
			for(String contentCell:contentRow){
				result.append(contentCell).append(",");
			}
			result.replace(result.length()-1, result.length(),"\n");
		}
		
		result.replace(result.length()-1, result.length(),"\n");
		
		return result.toString();
	}

}
