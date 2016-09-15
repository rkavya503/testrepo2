package com.akuacom.pss2.richsite.event;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.UserEAO;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
/**
 * 
 * Filename:    EventDataModelManager.java 
 * Description:  
 * Copyright:   Copyright (c)2010
 * Company:     
 * @author:     Yang Liu
 * @version:    
 * Create at:   Dec 14, 2010 2:35:53 PM 
 * 
 */
public interface EventDataModelManager {
	
	public void addMsgError(String message);
	/**
	 * Function for get ProgramManager instance
	 * @return
	 */
	public ProgramManager getProgramManager();
	/**
	 * Function for get ProgramParticipantManager instance
	 * @return
	 */
	public ProgramParticipantManager getProgramParticipantManager();
	/**
	 * Function for get EventManager instance
	 * @return
	 */
	public EventManager getEventManager();
	/**
	 * Function for get ClientEAO instance
	 * @return
	 */
	public ClientEAO getClientEAO();
	/**
	 * Function for get UserEAO instance 
	 * @return
	 */
	public UserEAO getUserEAO();
	/**
	 * Function for set goToParent flag, this function main to fix the bug for invoke from Struts framework to JSF framework 
	 * @param flag_GoToParent
	 */
	public void setFlag_GoToParent(boolean flag_GoToParent);
	/**
	 * Function for set confirmCancel flag, this function main to fix the bug for invoke from Struts framework to JSF framework 
	 * @param flag_ConfirmCancel
	 */
	public void setFlag_ConfirmCancel(boolean flag_ConfirmCancel);
	/**
	 * Function for add EventDataModel into JSF session cache
	 * @param model
	 * @return
	 */
	public boolean addSessionCache(EventDataModel model);
	/**
	 * Function for transfer EventDataModel to Event
	 * @param model
	 * @return
	 */
	public Event transferEventDataModelToEvent(EventDataModel model);
	public EventDataModel transferEventToEventDataModel(Event event,EventDataModel model);
	/**
	 * Function for transfer EventDataModel to DBPEvent
	 * @param model
	 * @return
	 */	
	public Event transferEventDataModelToDBPEvent(EventDataModel model);	
	/**
	 * Function for transfer EventParticipant object to EventInfoInstance object
	 * @param model
	 * @return
	 */
	public UtilityDREvent.EventInformation transferEventParticipantsToEventInfoInstance(EventDataModel model);
	/**
	 * Function for JSF presentation layer request for confirm event
	 * @param programName
	 * @return
	 */
	public String confirmDispatchLogic(String programName);
	/**
	 * Function for JSF presentation layer request for submit event to database
	 * @param programName
	 * @return
	 */
	public String submitToDBDispatchLogic(String programName);
	/**
	 * Function for invoke JSF presentation layer forward to cancel page
	 * @return
	 */
	public String confirmCancel();
	/**
	 * Function for invoke JSF presentation layer forward to event list display page 
	 * @return
	 */
	public String goToEventDisplayListPage();
}
