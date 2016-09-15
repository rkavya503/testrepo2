package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.event.signal.EventSignal;
import com.akuacom.pss2.event.signal.EventSignalEntry;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.system.SystemManager;

public class ClientEventSignals implements Serializable {
	
	List<SignalEntry> clientSignalEntries = null;
	String eventName;
	String participantName;
	String programName;
	String dateFormat;
	
	public ClientEventSignals() {
		loadSignalEntries();
		
	}
	
	private void loadSignalEntries() {
		clientSignalEntries = new ArrayList<SignalEntry>();
		
		Map<String, String> requestMap = 
			FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		this.setParticipantName(requestMap.get("client"));
		this.setEventName(requestMap.get("eventName"));
	
		if (eventName == null || eventName.length() < 1 || participantName == null || participantName.length() < 1) {
			JSFClient client = FDUtils.getJSFClient();
			this.setParticipantName(client.getName());
			this.setEventName(client.getEventName());
		}
		
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		EventParticipant eventParticipant = eventManager.getEventParticipant(eventName, participantName, true);

		if (eventParticipant != null) {
			this.setProgramName(eventParticipant.getEvent().getProgramName());
			
			Set<Signal> signals = new HashSet<Signal>();
        	// Get the client signals
			for (Signal signal : eventParticipant.getSignals()) {
            	signals.add(signal);
        	}
        	// Add the event-level signals
        	Set<EventSignal> eventSignals = (eventParticipant.getEvent().getEventSignals());
        	for(Signal signal : eventSignals) {
            	signals.add(signal);
        	}
        	Date now = new Date();
			for (Signal signal : signals) {
				for (SignalEntry signalEntry : signal.getSignalEntries()) {
                	final SignalEntry vo;
                	if (signalEntry instanceof EventSignalEntry) { 
                    	vo = new EventSignalEntry();
                    	((EventSignalEntry)vo).setUUID(signalEntry.getUUID());
                	}
                	else { 
                    	vo = new EventParticipantSignalEntry();
                    	((EventParticipantSignalEntry)vo).setUUID(signalEntry.getUUID());
                	}
                	vo.setTime(signalEntry.getTime());
                	vo.setParentSignal(signalEntry.getParentSignal());
                	vo.setLevelValue(signalEntry.getLevelValue());
                	vo.setNumberValue(signalEntry.getNumberValue());
                	vo.setExpired(signalEntry.getTime().before(now)); 
                	clientSignalEntries.add(vo);
				}
			}
        	Collections.sort(clientSignalEntries);
		}
	}

	public List<SignalEntry> getClientSignalEntries() {
		if (clientSignalEntries == null) {
			loadSignalEntries();
		}
		return clientSignalEntries;
	}

	public void setClientSignalEntries(List<SignalEntry> clientSignalEntries) {
		this.clientSignalEntries = clientSignalEntries;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	/**
     * Edits the event rules action.
     * 
     * @return the string
     */
    public String editClientSignalsAction() {
        return "editClientSignals";
    }
}
