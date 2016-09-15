package com.akuacom.pss2.richsite.event.creation.demo;

import java.io.Serializable;
import java.util.Date;

import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.demo.DemoProgramEJB;
import com.akuacom.pss2.richsite.event.creation.AbstractEventCreation;
import com.akuacom.pss2.richsite.event.creation.CompoundValidator;
import com.akuacom.pss2.richsite.event.creation.EventEnrollment;
import com.akuacom.pss2.richsite.event.creation.EventParticipantSelection;
import com.akuacom.pss2.richsite.event.creation.EventParticipantValidator;
import com.akuacom.pss2.richsite.event.creation.Validator;
import com.akuacom.pss2.richsite.event.creation.Validator.MSG;
import com.akuacom.pss2.richsite.event.creation.Wizard;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.event.DemoEvent;

public class DemoEventCreation extends AbstractEventCreation implements Serializable {

	private static final long serialVersionUID = -7801600190498201386L;
	
	private DemoEvent demoEvent;
	
	private String eventID;
	
	private String nextPageNoValidation;
	
	@Override
	public boolean isCbpProgramEJB() {
		return false;
	}
	public DemoEventCreation(String programName){
		super(programName);
	}
	
	protected String getEventID(){
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(this.getProgramName()+"_");
		return eventID;
	}

	public EventParticipantSelection getParticipantSelection() {
		EventParticipantSelection participantSelection = (EventParticipantSelection) getEventEnrollment();
		participantSelection.recalculateShed();
		return participantSelection;
	}
	
	public void goToConfirm(){
        DemoEvent de = getDemoEvent();
        if (de.isNowUsed()) {
            de.nowNotification();
        }
		this.goToPage("confirmation");
	}
	
	public void goToNextNoValidation(){
		if (this.nextPageNoValidation!=null && this.nextPageNoValidation.equals("confirmation")) {
	        DemoEvent de = getDemoEvent();
	        if (de.isNowUsed()) {
	            de.nowNotification();
	        }
			this.getWizard().goToPage("confirmation");
		} else {
			this.getWizard().nextPage();
		}
	}
	
	public void setNextPage(String nextPage){
		this.nextPageNoValidation=nextPage;
	}
	

	public DemoEvent getDemoEvent() {
		if(this.demoEvent==null){
			demoEvent = new DemoEvent(){
				private static final long serialVersionUID = -8509096512808138104L;
				
				@Override
				protected void reportError(String msg) {
					if(msg!=null)
						report(new MSG(Validator.MSG_ERROR,msg));
				}
				
				@Override
				public String getProgramName() {
					return DemoEventCreation.this.getProgramName();
				}
				
				@Override
				public String getEventID() {
					return DemoEventCreation.this.getEventID();
				}
			};
		}
		return demoEvent;
	}

	
	
	@Override
	protected Wizard createWizard() {
		Wizard wizard = new Wizard("eventSchedule",
				"participantSelection","confirmation");
		return wizard;
	}
	
	@Override
	protected void installValidators() {
		Validator val = new  Validator(){
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractEventCreation model) {
				try {
					DemoEventCreation evtmodel = (DemoEventCreation) model;
					DemoEvent demo = evtmodel.getDemoEvent();
					String msg = demo.validateTiming();
					if(msg!=null){
						return new MSG(Validator.MSG_ERROR,msg);
					}
				
					Event event = evtmodel.toEvent(demo.toUtilityDREvent());
					
					ProgramManager programManager=EJBFactory.getBean(ProgramManager.class);

					ProgramValidator programValidator = ValidatorFactory.getProgramValidator(programManager.getProgramOnly(event.getProgramName()));            
					programValidator.validateEvent(event);
					if(event.getWarnings()!=null)
						return new MSG(Validator.MSG_WARN,"Warnings:"+ErrorUtil.getWarningMessage(event.getWarnings()));
				} catch (ProgramValidationException e) {
					final String s = ErrorUtil.getErrorMessage(e);
					return new MSG(Validator.MSG_ERROR,s);
				}
				return null;
			}
		};
		registerValidator("eventSchedule", val);
		registerValidator("participantSelection", new EventParticipantValidator());
		registerValidator("confirmation", new CompoundValidator(
				val,new EventParticipantValidator()));
	}
	
	@Override
	protected void doCreateEvent(){
		UtilityDREvent drEvent = getDemoEvent().toUtilityDREvent();
		Event event = toEvent(drEvent);
		DemoProgramEJB demoProgram =  EJB3Factory.getLocalBean(DemoProgramEJB.class);
		demoProgram.createEvent(getProgramName(),event,drEvent);
	}
	
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getUniqueEventName(this.getProgramName()+"_");
		}
		return eventName;
	}
	
	protected Event toEvent(UtilityDREvent drEvent){
		DemoEvent evt=getDemoEvent();
		final Event event = new Event();
		event.setEventName(getEventName());
		event.setProgramName(this.getProgramName());
		event.setStartTime(evt.getStart());
		event.setEndTime(evt.getEnd());

		final Date date = new Date();
		event.setIssuedTime(date);
		event.setReceivedTime(date);

		event.setManual(true);

		event.setReceivedTime(new Date());
		EventTiming timing = drEvent.getEventTiming();
		if(timing != null){
			event.setIssuedTime(timing.getNotificationTime()
                    .toGregorianCalendar().getTime());
			event.setStartTime(timing.getStartTime().toGregorianCalendar()
                    .getTime());
			event.setEndTime(timing.getEndTime().toGregorianCalendar()
                    .getTime());
			event.setNearTime(evt.getNearEvent().getDateTime());
		}
		
		populateEventParticipants(event);
		return event;
	}
	
	
	@Override
	public String getEventListPage() {
		return "../"+super.getEventListPage();
	}

	@Override
	public String getProgramPage() {
		return "../"+super.getProgramPage();
	}

	@Override
	public Date getIssueTime() {
		return this.getDemoEvent().getNotification();
	}

	@Override
	public Date getStartTime() {
		return this.getDemoEvent().getStart();
	}

	@Override
	public Date getEndTime() {
		return this.getDemoEvent().getEnd();
	}

	
	@Override
	protected EventEnrollment createEventEnrollment() {
		return  new EventParticipantSelection(this);
	}
	
}
