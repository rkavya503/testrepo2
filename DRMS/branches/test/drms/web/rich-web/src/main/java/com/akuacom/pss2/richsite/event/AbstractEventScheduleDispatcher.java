package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * 
 * Filename:    EventScheduleDispatcher.java 
 * Description:  
 * Copyright:   Copyright (c)2010
 * Company:     
 * @author:     Yang Liu
 * @version:    
 * Create at:   Dec 14, 2010 2:47:45 PM 
 * 
 */
public abstract class AbstractEventScheduleDispatcher implements EventScheduleDispatcher, Serializable{

	private static final long serialVersionUID = 7936365090151014827L;
	private static final Logger log = Logger.getLogger(AbstractEventScheduleDispatcher.class.getName());
	/**
	 * Function for get JSF presentation layer request and handler 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public abstract String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager);
	/**
	 * Function for save event to Database
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String submitToDB(EventDataModel eventDataModel,EventDataModelManager manager){
		return null;
	}
	
	/**
	 * Function for check the participants which contain in event is empty
	 * If the participant is empty, add warn message and return true;
	 * If the participant is not empty, return false.
	 * @return
	 */
	public boolean participantsEmptyCheck(EventDataModel eventDataModel,EventDataModelManager manager){
		if(eventDataModel.getEventParticipants().size()<=0){
			final String warn ="Cannot create an event with no participants";
			manager.addMsgError(warn);
			return true;
		}
		return false;
	}
	/**
	 * Function for check the participants which contain in event is empty using ValidationException
	 * @param eventDataModel
	 * @param manager
	 */
	public void participantsEmptyValidation(EventDataModel eventDataModel,EventDataModelManager manager){
		if(eventDataModel.getAllParticipantsInProgram().size()<=0){
			throw new ValidationException("Cannot create an event with no participants");
		}
	}


	public void participantsEmptyValidation(List<EventParticipantDataModel> eList){
		if(eList == null || eList.size()<=0){
			throw new ValidationException("Cannot create an event with no participants");
		}
	}

	
	/**
	 * Function for check the event start day is same as end day using ValidationException
	 * @param eventDataModel
	 * @param manager
	 */
	public void sameDayValidation(Date startDate,Date endDate){
		if(startDate!=null&&endDate!=null){
			Date day_End_Start = DateUtil.getEndOfDay(startDate);
			Date day_End_End = DateUtil.getEndOfDay(endDate);
			if(day_End_Start.toString().equalsIgnoreCase(day_End_End.toString())){
				
			}else{
				throw new ValidationException("event end day must same as start day");
			}
		}		
	}
	
	public void startTimeValidation(EventDataModel eventDataModel){
		try {
			eventDataModel.buildStartTime();
		} catch (Exception e) {
			throw new ValidationException("start time is not correct");
		}
	}
	
	public void endTimeValidation(EventDataModel eventDataModel){
		try {
			eventDataModel.buildEndTime();
		} catch (Exception e) {
			throw new ValidationException("end time is not correct");
		}
	}
	public void issuedTimeValidation(EventDataModel eventDataModel){
		try {
			eventDataModel.buildIssuedTime();
		} catch (Exception e) {
			throw new ValidationException("issue time is not correct");
		}
	}
	
	public void respondByTimeValidation(EventDataModel eventDataModel){
		try {
			eventDataModel.buildRespondByTime();
		} catch (Exception e) {
			throw new ValidationException("respond by time is not correct");
		}
	}

	
	public static void addEventLog(boolean successFlag,String programName,String userName,Exception ex){
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1(programName);
		logEntry.setCategory(LogUtils.CATAGORY_EVENT);
		logEntry.setLongDescr(null);
		logEntry.setUserName(userName);
		if(successFlag){
			String message = "SUCCESS_CREATING_EVENT_STATE";
			logEntry.setDescription(message);
			log.info(logEntry);	
		}else{
			String message = "ERROR_CREATING_EVENT_STATE";
			logEntry.setDescription(message+":"+ex.getMessage());
			StringBuilder sb = new StringBuilder();
			sb.append(ex.toString());
			sb.append("\n");
			for(StackTraceElement element: ex.getStackTrace())
			{
				sb.append(element.toString());
				sb.append("\n");
			}
			logEntry.setLongDescr(sb.toString());
			log.error(logEntry);	
		}
	}
	
	public static String getWelcomeName() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		return request.getRemoteUser();
	}
}
