package com.akuacom.pss2.richsite.event;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.demo.DemoProgramEJB;
import com.akuacom.pss2.richsite.event.demo.JSFDemoEvent;
import java.io.Serializable;

public class EventDemoScheduleDispatcher extends AbstractEventScheduleDispatcher
        implements Serializable {
	private static final long serialVersionUID = -3331438914778201582L;

	/** The log */
	private static final Logger log = Logger.getLogger(EventDemoScheduleDispatcher.class.getName());

	/**
	 * Function for get JSF presentation layer request and handler
	 * 
	 * Validation Logic:
	 * 		Event participant list can not be empty
	 * 		Participant which contain empty client can not be involved
	 * 		If the validation pass, create the event and return to the event display page
	 * 		If the validation not pass, return the original page and display the validation message		
	 * 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager) {
		String programName ="";
		try {
			eventDataModel.confirmEventParticipantList();
			participantsEmptyValidation(eventDataModel, manager);			
			JSFDemoEvent demoEvent = (JSFDemoEvent) eventDataModel;			
			String msg = demoEvent.validateTiming();
			if (msg != null) {
				log.error("pss2.event.create.creationError: " + msg);
				throw new ValidationException(msg);
			}
			UtilityDREvent drEvent = demoEvent.toUtilityDREvent();
			Event event = demoEvent.toEvent(drEvent);
			programName = event.getProgramName();
			DemoProgramEJB demoProgram =  EJB3Factory.getLocalBean(DemoProgramEJB.class);
//			demoProgram.createEvent(JSFTestDemoEvent.PROGRAM_NAME,event,drEvent);	
			demoProgram.createEvent(eventDataModel.getProgramName(),event,drEvent);	
			
			addEventLog(true, programName, getWelcomeName(), null);
			eventDataModel.setRenewFlag(true);
			manager.setFlag_GoToParent(true);
			return manager.goToEventDisplayListPage();
		} catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			manager.addMsgError(s);
//			log.error("pss2.event.create.creationError: " + s);
			addEventLog(false, programName, getWelcomeName(), e);
			eventDataModel.setRenewFlag(false);
			return "validateNotPass_DemoSchedulePage";
		}
	}
}