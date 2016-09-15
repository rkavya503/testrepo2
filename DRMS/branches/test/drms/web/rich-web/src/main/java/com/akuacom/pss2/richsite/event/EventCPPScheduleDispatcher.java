package com.akuacom.pss2.richsite.event;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.util.EventUtil;
import java.io.Serializable;

import com.akuacom.pss2.richsite.FDUtils;

public class EventCPPScheduleDispatcher extends AbstractEventScheduleDispatcher implements Serializable {
	private static final long serialVersionUID = -3331438914928201582L;

	private static final Logger log = Logger.getLogger(EventCPPScheduleDispatcher.class.getName());

	/**
	 * Function for get JSF presentation layer request and handler
	 * 
	 * Validation Logic:
	 * 		Event participant list can not be empty
	 * 		Participant which contain empty client can not be involved
	 * 		If the validation pass, return to the event confirm page
	 * 		If the validation not pass, return the original page and display the validation message		
	 * 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager) {
		try {
			eventDataModel.setEventName(EventUtil.getEventName());
			startTimeValidation(eventDataModel);
			endTimeValidation(eventDataModel);
			final Date date = new Date();
			if(eventDataModel.getIssuedTime() != null)
			{
				issuedTimeValidation(eventDataModel);
			}
			eventDataModel.setReceivedTime(new Date());
			Program program = manager.getProgramManager().getProgramWithParticipantsAndPRules(eventDataModel.getProgramName());
			List<EventParticipantDataModel> eList = eventDataModel.confirmEventParticipantList(program);
			participantsEmptyValidation(eList);
			sameDayValidation(eventDataModel.getStartTime(),eventDataModel.getEndTime());
			eventDataModel.setManual(true);
			eventDataModel.setCommit(false);
			eventDataModel.setRenewFlag(false);
			Event event = manager.transferEventDataModelToEvent(eventDataModel);
			
		    
			String classNameBase = program.getClassName();
			ProgramEJB programEJB =manager.getProgramManager().lookupProgramBeanFromClassName(classNameBase);
			Set<EventParticipant> filteredEventParticipants = new HashSet<EventParticipant>();
			for(Participant participant:eventDataModel.getParticipants()){
				EventParticipant eventParticipant = new EventParticipant();
				eventParticipant.setParticipant(participant);
				eventParticipant.setEvent(event);
				filteredEventParticipants.add(eventParticipant);	
			}
			
			Set<EventParticipant> aggregatedEventParticipants = programEJB.createAggregatedEventParticipants(filteredEventParticipants, program);
			filteredEventParticipants.addAll(aggregatedEventParticipants);
			
			Set<EventParticipant> filteredEventParticipantsAggr = manager.getProgramManager().filterEventParticipants(filteredEventParticipants,program,event);
            Set<EventParticipant> eventClients = programEJB.createEventClients(filteredEventParticipantsAggr, program);
            
            eventDataModel.getParticipants().clear();
			for(EventParticipant eventParticipant:filteredEventParticipantsAggr){   
				 eventDataModel.getParticipants().add(eventParticipant.getParticipant());
            }
			 
			eventDataModel.getClients().clear();
			for(EventParticipant eventParticipant:eventClients){   
				eventDataModel.getClients().add(eventParticipant.getParticipant());
	        }
			ProgramValidator programValidator = ValidatorFactory.getProgramValidator(program);            
			programValidator.validateEvent(event);
			 
			final String s = ErrorUtil.getWarningMessage(event.getWarnings());
			if (s != null && !s.isEmpty()) {
				manager.addMsgError("Warnings: " + s);
			}
			return "confirmView";
		} catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			// manager.addMsgError(s);
			FDUtils.addMsgError(s);
			log.error("pss2.event.create.creationError: " + s);
			eventDataModel.setRenewFlag(false);
			return null;
		}
	}

	/**
	 * Function for save event to Database
	 * 
	 * Validation Logic:
	 * 		If the validation pass, create the event and return to the event display page
	 * 		If the validation not pass, return the original page and display the validation message		
	 * 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	@Override
	public String submitToDB(EventDataModel eventDataModel,EventDataModelManager manager) {
		String programName ="";
		
		try {
			eventDataModel.setRenewFlag(true);
			Event event = manager.transferEventDataModelToEvent(eventDataModel);
			programName = event.getProgramName();
			
			if (eventDataModel.isWarnMsgConfirm()) {
				Program program = manager.getProgramManager().getProgramWithParticipantsAndPRules(programName);
				ProgramValidator programValidator = ValidatorFactory.getProgramValidator(program);
				programValidator.validateEvent(event);
	
				if (event.getWarnings()!=null && event.getWarnings().size()>0) {
					eventDataModel.setWarnMsg("Warnings:"+ErrorUtil.getWarningMessage(event.getWarnings()));
					return "success";
				}
			}
			
			final EventManager eventManager = EJBFactory.getBean(EventManager.class);
			// event.setIssuedTime(new Date());
			eventManager.createEvent(event.getProgramName(), event);
			addEventLog(true, programName, getWelcomeName(), null);
			manager.setFlag_GoToParent(true);
			return manager.goToEventDisplayListPage();
		} catch (Exception e) {
			eventDataModel.setWarnMsgConfirm(false);
			final String s = ErrorUtil.getErrorMessage(e);
			manager.addMsgError(s);
//			log.error("pss2.event.create.creationError: " + s);
			addEventLog(false, programName, getWelcomeName(), e);
			return "success";
		}
	}

}
