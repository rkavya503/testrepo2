/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.EventTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.web.aggregation.AggregationTree;

/**
 * The Class EventTable.
 */
public class EventTable  implements Serializable,EventViewLayout {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JSFClient.class
			.getName());

	/** The event name. */
	private String eventName;
	
	/** The programs. */
	private List<JSFEvent> events;

	private boolean loadFlag;
	
	private boolean eventOptOutEnabled;
	
	private boolean isUserRoleAllowsToOptOutFromEvent;
	
	private boolean dayOfAdjustment;
	
	public EventTable(){
		buildViewLayout();
	}
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	private void addEvent(Event event)
	{
		boolean aggregationFiltered = false;
		
		JSFProgram program = FDUtils.getJSFProgram();
        if (program != null &&  
        	!program.getName().equals(AggregationTree.NO_PROGRAM) &&
        	!CBPUtil.equalsProgramName(event.getProgramName(),FDUtils.getJSFProgram().getName()) &&
            !event.getProgramName().equals(TestProgram.PROGRAM_NAME))
        {
        	aggregationFiltered = true;
        }
        
        if (!aggregationFiltered && event != null && 
        	(event.getEventStatus() != com.akuacom.pss2.util.EventStatus.RECEIVED ||
        	event.getProgramName().equals(TestProgram.PROGRAM_NAME)))
        {
            event.setState(event.getEventStatus().toString());
            JSFEvent jsfEvent = new JSFEvent();
            jsfEvent.load(event);
			if (!events.contains(jsfEvent)) 
			{
				events.add(jsfEvent);
			}
        }		
	}	

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	public List<JSFEvent> getEvents() {
		events = new ArrayList<JSFEvent>();
		ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
		// Note: since these actions are not atomic, the events can be deleted
		// right after been retrieved. So, a null check is necessary.
        //Participant p = participantManager.getParticipant(FDUtils.getParticipantName(), false);
        //For Performance 2012-06-28
		Participant p = participantManager.getParticipantAndEventsOnly(FDUtils.getParticipantName(), false);
		Set<EventParticipant> setEP = p.getEventParticipants();
		
		SystemManager sysManager = EJBFactory.getBean(SystemManager.class);
		PSS2Features features=sysManager.getPss2Features();

		if (p.getDefaultEventOptoutEnabled())
			eventOptOutEnabled=features.isEventOptoutEnabled();
		else
			eventOptOutEnabled=p.getPartEventOptoutEnabled();
		
		// add participant events
		for (EventParticipant eventP : setEP) {
			if(eventP.getEventOptOut()==0)
				addEvent(eventP.getEvent());
		}

		// add client events
		// TODO: how can a client be an event that the participant is not in?
		
        //For Performance 2012-06-28
		//ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
		List<Participant> participants = participantManager.getClientsAndEvents(FDUtils.getParticipantName());
//				for (Participant client : clientManager.getClients(FDUtils.getParticipantName())) {
		for (Participant client : participants) {
			for (EventParticipant eventC : client.getEventParticipants()) {
				if(eventC.getEventOptOut()==0)
					addEvent( eventC.getEvent());
            }
		}
        
		return events;
	}

	/**
	 * Edits the bids action.
	 * 
	 * @return the string
	 */
	public String editBidsAction() {
		for (JSFEvent event : events) {
			if (event.getName().equals(eventName)) {
				FDUtils.setBids(new Bids(event.getProgramName(), eventName,
						true));
				break;
			}
		}
		// TODO: what if it isn't found?
		return "editBids";
	}

	/**
	 * Edits the bids listener.
	 * 
	 * @param e
	 *            the e
	 */
	public void editBidsListener(ActionEvent e) {

		eventName = e.getComponent().getAttributes().get("eventName")
				.toString();
	}

	/**
	 * Adding event opt-out listener.
	 * 
	 * @param e
	 *            the e
	 */
	public void optoutListener(ActionEvent e) {
		eventName = e.getComponent().getAttributes().get("eventName")
				.toString();
	}

	/**
	 * opt-out action.
	 * 
	 * @return the string
	 */
	public String optOutAction() {
		EventManager em = EJBFactory.getBean(EventManager.class);
		em.removeParticipantFromEvent(eventName, FDUtils.getParticipantName());
		events = null;
		getEvents();

		return null;
	}

	public String optInAction() {

		EventManager em = EJBFactory.getBean(EventManager.class);
        em.addParticipantsToEvent(eventName, FDUtils.getParticipantName());

        events = null;
		getEvents();

		return null;
	}

	/**
	 * @param loadFlag the loadFlag to set
	 */
	public void setLoadFlag(boolean loadFlag) {
		this.loadFlag = loadFlag;
	}

	/**
	 * @return the loadFlag
	 */
	public boolean isLoadFlag() {
		return loadFlag;
	}

	public boolean isEventOptOutEnabled() {
		return eventOptOutEnabled;
	}

	public void setEventOptOutEnabled(boolean eventOptOutEnabled) {
		this.eventOptOutEnabled = eventOptOutEnabled;
	}

	public boolean getCanDeleteEvent() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCanDeleteEvent(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public boolean getCanOptOutOfEvent() {
		return this.isUserRoleAllowsToOptOutFromEvent;
	}

	public void setCanOptOutOfEvent(boolean value) {
		this.isUserRoleAllowsToOptOutFromEvent = value;
		
	}
	
	private void buildViewLayout(){
		try {
			getViewBuilderManager().buildEventViewLayout(this);
		} catch (NamingException e) {
			// log exception
		}
		
	}
	private ViewBuilderManager getViewBuilderManager() throws NamingException{
		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}
	public boolean getCanDeleteDemoEvent() {
		// TODO Auto-generated method stub
		return false;
	}
	public void setCanDeleteDemoEvent(boolean value) {
		// TODO Auto-generated method stub
		
	}
	public boolean isDayOfAdjustment() {
		return dayOfAdjustment;
	}
	public void setDayOfAdjustment(boolean dayOfAdjustment) {
		this.dayOfAdjustment = dayOfAdjustment;
	}
	
	public void updateDayOfAdjustmentControl(){

        JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
        EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
        updateDayOfAdjustment(eventParticipantManager,jsfParticipant,this.eventName);
       
  }
	
	private void updateDayOfAdjustment(EventParticipantManager eventParticipantManager,JSFParticipant jsfParticipant,String eventName){
	   	 // update participant 
	       EventParticipant ep = eventParticipantManager.getEventParticipant(eventName, jsfParticipant.getName(), false);

	       int dayOfAdjustment =  this.dayOfAdjustment ? 1:0;
	       ep.setApplyDayOfBaselineAdjustment(dayOfAdjustment);
	       eventParticipantManager.updateEventParticipant(eventName, ep.getParticipant().getParticipantName(), false, ep);
	       
	   }
	
	public void setDayOfAdjustmentControl(){
    	JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
    	EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
    	 EventParticipant ep = eventParticipantManager.getEventParticipant(this.eventName, jsfParticipant.getName(), false);
        if(ep.getApplyDayOfBaselineAdjustment() ==1)
        	this.setDayOfAdjustment(true);
        else 
        	this.setDayOfAdjustment(false);
    }
}
