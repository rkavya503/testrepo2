package com.akuacom.pss2.richsite.event;
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
public interface EventScheduleDispatcher {
	/**
	 * Function for get JSF presentation layer request and handler 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager);
	/**
	 * Function for save event to Database
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String submitToDB(EventDataModel eventDataModel,EventDataModelManager manager);
}
