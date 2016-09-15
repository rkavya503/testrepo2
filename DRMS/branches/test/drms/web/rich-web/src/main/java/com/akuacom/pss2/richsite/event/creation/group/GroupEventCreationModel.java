package com.akuacom.pss2.richsite.event.creation.group;

import java.util.Date;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.query.SCELocationType;
import com.akuacom.pss2.query.SCELocationUtils;
import com.akuacom.pss2.richsite.event.creation.AbstractEventCreation;
import com.akuacom.pss2.richsite.event.creation.CompoundValidator;
import com.akuacom.pss2.richsite.event.creation.EventEnrollment;
import com.akuacom.pss2.richsite.event.creation.EventParticipantValidator;
import com.akuacom.pss2.richsite.event.creation.EventTiming;
import com.akuacom.pss2.richsite.event.creation.EventTimingValidator;
import com.akuacom.pss2.richsite.event.creation.Wizard;

public class GroupEventCreationModel extends AbstractEventCreation {
	
	private static final long serialVersionUID = 1L;
	
	private EventTiming eventTime;
	
	private SCELocationType locationType;
	
	public GroupEventCreationModel(String program) {
		super(program);
		this.locationType = SCELocationUtils.getLocationType(getProgram().getProgramClass());
		this.setSpanDays(true);
	}
	
	public SCELocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(SCELocationType locationType) {
		this.locationType = locationType;
	}
	
	@Override
	public Event converToEvent(boolean withParticipants) {
		Event event=super.converToEvent(withParticipants);
		//locations
		event.setLocations( getEnrollmentSelection().getLocations());
		return event;
	}
	
	/** sub class to override to provide a different wizard for event creation **/
	protected  Wizard createWizard(){
		Wizard wizard = new Wizard("eventTiming",
				"locationSelection",
				"confirmation");
		return wizard;
	}
	
	public void goToConfirm(){
		this.goToPage("confirmation");
	}
	
	/** subclass to override to customize validators for each page **/
	protected void installValidators(){
		registerValidator("eventTiming", new EventTimingValidator());
		registerValidator("locationSelection", new LocationValidator());
		registerValidator("confirmation", new CompoundValidator(
					new EventTimingValidator(),new EventParticipantValidator()));
	}

	public EventTiming getEventTiming() {
		if(eventTime==null){
			eventTime = new EventTiming(this.isSpanDays());
			//initialize with default value in program constraint
		}
		return eventTime;
	}
	
	
	@Override
	public boolean isCbpProgramEJB() {
		return false;
	}
	
	public EventEnrollingGroupSelection getEnrollmentSelection(){
		return (EventEnrollingGroupSelection) getEventEnrollment();
	}
	
	@Override
	protected EventEnrollment createEventEnrollment() {
		return new EventEnrollingGroupSelection(this);
	}
	
	@Override
	public Date getIssueTime() {
		if(this.getEventTiming().getIssuedTime()!=null)
			return this.getEventTiming().getIssuedTime();
		else
			return new Date();
	}
	
	@Override
	public Date getStartTime() {
		return this.getEventTiming().getStartTime();
	}
	
	@Override
	public Date getEndTime() {
		return this.getEventTiming().getEndTime();
	}
	
	@Override
	public String getEventListPage() {
		return "../"+super.getEventListPage();
	}

	@Override
	public String getProgramPage() {
		return "../"+super.getProgramPage();
	}
}
