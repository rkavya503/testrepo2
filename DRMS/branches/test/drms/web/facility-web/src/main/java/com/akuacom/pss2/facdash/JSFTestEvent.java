package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.program.testProgram.TestProgramEJB;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.pss2.web.event.DemoEvent;
import com.akuacom.pss2.web.event.JSFDemoEventInfo;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.system.property.CoreProperty;

public class JSFTestEvent extends DemoEvent  implements Serializable {
		
	private static final long serialVersionUID = 275428249445893364L;
	
	private static  Logger log = Logger.getLogger("JSFTestEvent");

    private String disclimar;

	/** all client participants **/
	private List<EvtParticipant> participants;
	
	/** the id of event, format EVENTNAME_YYYYMMDD-HHMMSS DEMO_20101221-160219**/ 
	private String eventID;
	
	/** indicates which time has been reset by end user **/
	public JSFTestEvent(boolean phaseListener){
		super(phaseListener);
	}
	
	public JSFTestEvent(){
		this(true);
	}
	
	@Override
	protected void reportError(String msg){
		if(msg!=null)
			FDUtils.addMsgError(msg);
	}
	
	public String getParticipantName(){
    	return FDUtils.getParticipantName();
    }
    
    public List<EvtParticipant> getParticipantClients(){
        ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
    	if(participants==null){
    		participants = new ArrayList<EvtParticipant>();
        	for(Participant part: 
                clientManager.getClients(FDUtils.getParticipantName())){
        		EvtParticipant evtpart= new EvtParticipant(part);
        		evtpart.setSelected(true);
        		participants.add(evtpart);
        	}
    	}
    	return participants;
	}
    
	public void selectAllAction(ActionEvent evt){
		for(EvtParticipant client:this.getParticipantClients()){
			client.setSelected(true);
		}
	}
	
	public void deselectAllAction(ActionEvent evt){
		for(EvtParticipant client:this.getParticipantClients()){
			client.setSelected(false);
		}
	}
	
	public String issueEventAction() {
        if (isNowUsed()) {
            Date time = new Date();
            getNotificationTime().setTime(time);
//            getStartTime().setTime(time);
            notice = 0;
            timeChanged = TimeChange.StartTime;
            updateModel();
        }

        String msg = validateTiming();
        if (msg != null) {
            FDUtils.addMsgError(msg);
            return "IssueFailed";
        }
        try {
            TestProgramEJB testProgram = EJB3Factory.getLocalBean(TestProgramEJB.class);
            UtilityDREvent drEvent = toUtilityDREvent();
            Event event = toEvent(drEvent);
            if (event.getEventParticipants() == null || event.getEventParticipants().size() <= 1) {
                FDUtils.addMsgError("At least one client must be selected");
                return "IssueFailed";
            }
            testProgram.createEvent(TestProgram.PROGRAM_NAME, event, drEvent);

            return FDUtils.getHeader1().eventsAction();
        } catch (EJBException e) {
            log.error("Failed to create Test Event", e);
            FDUtils.addMsgError("Failed to create Event: " + MessageUtil.getErrorMessage(e));
            return "IssueFailed";
        }
    }
	
	public String getEventID(){
		if(eventID==null)
			eventID = EventUtil.getUniqueEventName(TestProgram.PROGRAM_NAME+"_");
		return eventID;
	}
	
	public UtilityDREvent toUtilityDREvent(){
		
		UtilityDREvent drevent = new UtilityDREvent();
		//event timing
		drevent.setProgramName(TestProgram.PROGRAM_NAME);
		drevent.setEventIdentifier(getEventID());
		drevent.setSchemaVersion(XML_SCHEMA_VERSION);
		
		EventTiming et = new EventTiming();
		et.setStartTime(toXMLGregorianCalendar(getStart()));
		et.setEndTime(toXMLGregorianCalendar(getEnd()));
		et.setNotificationTime(toXMLGregorianCalendar(getNotification()));
		drevent.setEventTiming(et);
		
		//Event information
		EventInformation  evtInfo = new EventInformation();
		drevent.setEventInformation(evtInfo);
		
		for(String signalType:enabledSignalTypes){
			EventInfoInstance evtInstance = new  EventInfoInstance();
			
			String openADRSignalType=DemoEvent.getSingalNameInOpenAdr(signalType);
			evtInstance.setEventInfoTypeName(openADRSignalType);
			
			evtInstance.setSchemaVersion(XML_SCHEMA_VERSION);
			evtInstance.setValues(toEventInfoInstanceValues(signalType));
			evtInfo.getEventInfoInstance().add(evtInstance);			
		}
		
		
		return drevent;
	}
	
	public Event toEvent(UtilityDREvent drEvent){
		final Event event = new Event();
		event.setEventName(EventUtil.getUniqueEventName(TestProgram.PROGRAM_NAME+"_"));
		event.setProgramName(TestProgram.PROGRAM_NAME);
		event.setStartTime(getStart());
		event.setEndTime(getEnd());
		
		final Date date = new Date();
		event.setIssuedTime(date);
		event.setReceivedTime(date);
		
		
		event.setManual(true);
		
		EventTiming timing = drEvent.getEventTiming();
		if(timing != null){
			event.setIssuedTime(timing.getNotificationTime()
                    .toGregorianCalendar().getTime());
			event.setStartTime(timing.getStartTime().toGregorianCalendar()
                    .getTime());
			event.setEndTime(timing.getEndTime().toGregorianCalendar()
                    .getTime());
			event.setNearTime(nearEvent.getDateTime());
			
			if (event.getIssuedTime().getTime()<event.getReceivedTime().getTime())
				event.setReceivedTime(event.getIssuedTime());
		}
		
		//add participant itself and selected clients as event participants
		populateEventParticipants(event);
		
		return event;
	}
	
	/**
	 * add participants including clients to event
	 * @param event
	 */
	public void populateEventParticipants(Event event){
		//Add participant itself to event participant
        ParticipantManager pariticpantManager = EJBFactory.getBean(ParticipantManager.class);
		Participant participant=pariticpantManager.getParticipant(FDUtils.getParticipantName());
		participant.setEventParticipants(new HashSet<EventParticipant>());
		
		//create event participant
		EventParticipant pevp = new EventParticipant();
		//set value at both ends of bi-literal relationship
		//EventParticipant - Event 
		pevp.setEvent(event);
		event.getEventParticipants().add(pevp);
		
		//set value at both ends of bi-literal relationship
		//EventParticipant - Participant
		pevp.setParticipant(participant);
		participant.getEventParticipants().add(pevp);
		
		//add selected clients as event participants
		for(EvtParticipant evp:getParticipantClients()){
			if(!evp.isSelected()) continue;
			
			Participant client = evp.getParticipant();
			
			EventParticipant cep = new EventParticipant();
			//set value at both ends of bi-literal relationship
			//EventParticipant - Event
			cep.setEvent(event);
			event.getEventParticipants().add(cep);
			
			//set value at both ends of bi-literal relationship
			//EventParticipant - Participant
			cep.setParticipant(client);
			client.getEventParticipants().add(cep);
		}
	}
	
	public  EventInfoInstance.Values toEventInfoInstanceValues(String signalType){
		EventInfoInstance.Values values= new EventInfoInstance.Values();
		List<JSFDemoEventInfo> events = this.getEvents();
		
		//first is notification
		//second id near 
		//last is end 
		for(int i = 2;i<events.size()-1;i++){
			JSFDemoEventInfo info =events.get(i);
			values.getValue().add(info.toEventInfoValue(signalType, notice));
		}
		return values;
	}

    public String getDisclimar() {
          SystemManager systemManager = EJBFactory.getBean(SystemManagerBean.class);
           for(CoreProperty corp : systemManager.getAllProperties()){
        	   if (corp.getPropertyName().equalsIgnoreCase("testEventDisclimar"))
                   disclimar =corp.getStringValue();
           }
        return disclimar;
    }


    public void setDisclimar(String disclimar) {
        this.disclimar = disclimar;
    }
	
	
	@Override
	public String getProgramName() {
		return TestProgram.PROGRAM_NAME;
	}

	public static class EvtParticipant implements Serializable {
		
		/** selected by user**/
		private boolean selected;
		
		/** participant **/
		private Participant participant;
		
		public EvtParticipant(Participant part){
			this.participant =part;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public ClientStatus getClientStatus() {
			return participant.getClientStatus();
		}

		public String getParticipantName() {
			return participant.getParticipantName();
		}

		public Participant getParticipant() {
			return participant;
		}


	}
	
	
}
