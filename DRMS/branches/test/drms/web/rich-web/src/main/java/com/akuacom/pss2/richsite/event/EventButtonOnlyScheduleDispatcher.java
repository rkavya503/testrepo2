package com.akuacom.pss2.richsite.event;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.util.EventUtil;

public class EventButtonOnlyScheduleDispatcher extends AbstractEventScheduleDispatcher implements Serializable {
	private static final long serialVersionUID = -3331438914978201582L;
	
	/** The log */
	private static final Logger log = Logger.getLogger(EventButtonOnlyScheduleDispatcher.class.getName());
	
	private static final String VALIDATION_NOT_PASS="validateNotPass_ButtonOnlySchedulePage";
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
		try {
			UtilityDREvent utilityDREvent = new UtilityDREvent();
			utilityDREvent.setProgramName(eventDataModel.getProgramName());
			utilityDREvent.setEventIdentifier(EventUtil.getEventName());
			eventDataModel.confirmEventParticipantList();			
//			participantsEmptyValidation(eventDataModel, manager);			
			UtilityDREvent.EventInformation eventInformation = manager.transferEventParticipantsToEventInfoInstance(eventDataModel);
			utilityDREvent.setEventInformation(eventInformation);
			manager.getEventManager().createEvent(utilityDREvent, true);
			eventDataModel.setRenewFlag(true);
			manager.setFlag_GoToParent(true);
			return manager.goToEventDisplayListPage();
		} catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			manager.addMsgError(s);
			log.error("pss2.event.create.creationError: " + s);
			eventDataModel.setRenewFlag(false);
			return VALIDATION_NOT_PASS;
		}
	}
}