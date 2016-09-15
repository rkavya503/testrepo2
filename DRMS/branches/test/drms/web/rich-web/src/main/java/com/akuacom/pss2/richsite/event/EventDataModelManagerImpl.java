package com.akuacom.pss2.richsite.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.UserEAO;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.richsite.event.dbp.DBPNoBidEventDataModel;
import org.ajax4jsf.model.SerializableDataModel;
/**
 * 
 * Filename:    EventDataModelManager.java 
 * Description: EventDataModelManager is the control manager for the backing bean for OpenADR Event model in JSF framework which 
 * 				named EventDataModel
 * Copyright:   Copyright (c)2010
 * Company:     
 * @author:     Yang Liu
 * @version:    
 * Create at:   Dec 14, 2010 2:00:51 PM 
 * 
 */
public class EventDataModelManagerImpl extends SerializableDataModel  implements EventDataModelManager{
	
	private static final long serialVersionUID = -3331438914978201582L;
	
	/** The log */
	private static final Logger log = Logger.getLogger(EventDataModelManager.class.getName());
	
	public static final String EventListPageLocation="../../../pss2.website/uoEvent.do";
	public static final String ProgramPageLocation="../../../pss2.website/uoProgram.do";
	
	/** The EventDataModel instance which in initialized at constructor */
	private EventDataModel eventDataModel;
	
	/** The EventCPPScheduleDispatcher instance */
	private EventScheduleDispatcher eventCPPScheduleDispatcher = new EventCPPScheduleDispatcher();
	
	/** The EventButtonOnlyScheduleDispatcher instance */
	private EventScheduleDispatcher eventButtonOnlyScheduleDispatcher = new EventButtonOnlyScheduleDispatcher();
	
	/** The EventDBPNoBidScheduleDispatcher instance */
	private EventScheduleDispatcher eventDBPNoBidScheduleDispatcher = new EventDBPNoBidScheduleDispatcher();
	
	/** The EventDBPScheduleDispatcher instance */
	private EventScheduleDispatcher eventDBPScheduleDispatcher = new EventDBPScheduleDispatcher();
	
	/** The EventDemoScheduleDispatcher instance */
	private EventScheduleDispatcher eventDemoScheduleDispatcher = new EventDemoScheduleDispatcher();
	
	/** The EventIssueScheduleDispatcher instance */
	private EventScheduleDispatcher eventIssueScheduleDispatcher = new EventIssueScheduleDispatcher();
	
	/** The EventTestScheduleDispatcher instance */
	private EventScheduleDispatcher eventTestScheduleDispatcher = new EventTestScheduleDispatcher();

	private EventScheduleDispatcher eventFastDRScheduleDispatcher = new EventFastDRScheduleDispatcher();

	private EventScheduleDispatcher eventFastDRNotificationDispatcher = new EventFastDRNotificationDispatcher();

	/** The UserEAO instance */
	private UserEAO userEAO;
	
	/** The ClientEAO instance */
	private ClientEAO clientEAO;
	
	/** The EventManager instance */
	private EventManager eventManager;
	
	/** The ProgramManager instance */
	private ProgramManager programManager;	
	
	/** The ProgramParticipantManager instance */
	private ProgramParticipantManager programParticipantManager;
	
	/**
	 * Constructor
	 * @param eventDataModel
	 */
	public EventDataModelManagerImpl(EventDataModel eventDataModel){
		super();
		this.eventDataModel=eventDataModel;
		if(userEAO==null){
			userEAO = (UserEAO)EJB3Factory.getBean(UserEAO.class);
		}
		if(clientEAO==null){
			clientEAO = (ClientEAO)EJB3Factory.getBean(ClientEAO.class);
		}
		if(eventManager==null){
			eventManager=EJBFactory.getBean(EventManager.class);
		}
		if(programManager==null){
			programManager=EJBFactory.getBean(ProgramManager.class);
		}
		if(programParticipantManager==null){
			programParticipantManager=EJBFactory.getBean(ProgramParticipantManager.class);
		}	
	}
		
	//------------------------------------------------------------------Getter and Setter--------------------------------------------------------------
	public ProgramManager getProgramManager() {
		return programManager;
	}

	public void setProgramManager(
			ProgramManager programManager) {
		this.programManager = programManager;
	}
	public ProgramParticipantManager getProgramParticipantManager() {
		return programParticipantManager;
	}

	public void setProgramParticipantManager(
			ProgramParticipantManager programParticipantManager) {
		this.programParticipantManager = programParticipantManager;
	}	
	public EventDataModel getEventDataModel() {
		return eventDataModel;
	}


	public void setEventDataModel(EventDataModel eventDataModel) {
		this.eventDataModel = eventDataModel;
	}

	
	public UserEAO getUserEAO() {
		return userEAO;
	}


	public void setUserEAO(UserEAO userEAO) {
		this.userEAO = userEAO;
	}


	public ClientEAO getClientEAO() {
		return clientEAO;
	}


	public void setClientEAO(ClientEAO clientEAO) {
		this.clientEAO = clientEAO;
	}


	public EventManager getEventManager() {
		return eventManager;
	}


	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	//----------------------------------------------------------------Business Logic Method----------------------------------------------------------------
	
	/**
	 * Dispatch confirm request from the JSF Presentation Layer to the suitable Event Dispatcher for handler it.
	 * 
	 * @param programName
	 * @return
	 */
	public String confirmDispatchLogic(String programName){
		log.debug("confirmDispatchLogic method invoked and program name is "+programName);
		if(programName!=null&&programName!=""){
			ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
			String uiScheduleEventString = programManager.getUiScheduleEventString(programName);
	        if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.CPPSchedulePage)){
	        	return confirm_CPPSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DBPNoBidSchedulePage)){
	        	return confirm_DBPNoBidSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DBPSchedulePage)){
	        	return confirm_DBPSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.IssueSchedulePage)){
	        	return confirm_IssueSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DemoSchedulePage)){
	        	return confirm_DemoSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.TestSchedulePage)){
	        	return confirm_TestSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.ButtonOnlySchedulePage)){
	        	return confirm_ButtonOnlySchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.SCERTPSchedulePage)){
	        	return confirm_ButtonOnlySchedulePage();
	        }else{
	        	flag_GoToParent = true;
	        	eventDataModel.setRenewFlag(false);
	    		return goToEventDisplayListPage();
	        }
		}
		flag_GoToParent = true;
		eventDataModel.setRenewFlag(false);
		return goToEventDisplayListPage();
	}
	
	/**
	 * Dispatch submitToDB request from the JSF Presentation Layer to the suitable Event Dispatcher for handler it.
	 * 
	 * @param programName
	 * @return
	 */	
	public String submitToDBDispatchLogic(String programName){
		if(programName!=null&&programName!=""){
			ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
	        String uiScheduleEventString = programManager.getUiScheduleEventString(programName);
	        if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.CPPSchedulePage)){
	        	return submitToDB_CPPSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DBPNoBidSchedulePage)){
	        	return submitToDB_DBPNoBidSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DBPSchedulePage)){
	        	return submitToDB_DBPSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.IssueSchedulePage)){
	        	return submitToDB_IssueSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.DemoSchedulePage)){
	        	return submitToDB_DemoSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.TestSchedulePage)){
	        	return submitToDB_TestSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.ButtonOnlySchedulePage)){
	        	return submitToDB_ButtonOnlySchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.FastDRSchedulePage)){
                return submitToDB_FastDRSchedulePage();
	        }else if(uiScheduleEventString.equalsIgnoreCase(EventDataModelConstants.FastDRNotificationPage)){
                return submitToDB_FastDRNotificationPage();
	        }else{
	        	flag_GoToParent = true;
	        	eventDataModel.setRenewFlag(false);
	    		return goToEventDisplayListPage();
	        }
		}
		flag_GoToParent = true;
		eventDataModel.setRenewFlag(false);
		return goToEventDisplayListPage();
	}
    /**
	 * Function of EventCPPScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_CPPSchedulePage(){
		return eventCPPScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventDBPNoBidScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_DBPNoBidSchedulePage(){
		return eventDBPNoBidScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventDBPScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_DBPSchedulePage(){
		return eventDBPScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventIssueScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_IssueSchedulePage(){
		return eventIssueScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventDemoScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_DemoSchedulePage(){
		return eventDemoScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventTestScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_TestSchedulePage(){
		return eventTestScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}
	
	/**
	 * Function of EventButtonOnlyScheduleDispatcher for invoke confirm request 
	 * @return
	 */
	private String confirm_ButtonOnlySchedulePage(){
		return eventButtonOnlyScheduleDispatcher.dispatchEvent(eventDataModel,this);
	}	

	/**
	 * Function of EventCPPScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_CPPSchedulePage(){
		return eventCPPScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventCPPScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_DBPNoBidSchedulePage(){
		return eventDBPNoBidScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventCPPScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_DBPSchedulePage(){
		return eventDBPScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventIssueScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_IssueSchedulePage(){
		return eventIssueScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventDemoScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_DemoSchedulePage(){
		return eventDemoScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventTestScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_TestSchedulePage(){
		return eventTestScheduleDispatcher.submitToDB(eventDataModel,this);
	}

	/**
	 * Function of EventButtonOnlyScheduleDispatcher for invoke submitToDB request 
	 * @return
	 */
	private String submitToDB_ButtonOnlySchedulePage(){
		return eventButtonOnlyScheduleDispatcher.submitToDB(eventDataModel,this);
	}	
	
	private String submitToDB_FastDRSchedulePage(){
		return eventFastDRScheduleDispatcher.submitToDB(eventDataModel,this);
	}

    private String submitToDB_FastDRNotificationPage() {
        return eventFastDRNotificationDispatcher.submitToDB(eventDataModel, this);
    }


	public boolean flag_GoToParent = false;
	public boolean isFlag_GoToParent() {
		return flag_GoToParent;
	}
	/**
	 * Function for set goToParent flag, this function main to fix the bug for invoke from Struts framework to JSF framework 
	 * @param flag_GoToParent
	 */
	public void setFlag_GoToParent(boolean flag_GoToParent) {
		this.flag_GoToParent = flag_GoToParent;
	}
	/**
	 * Function for invoke JSF presentation layer forward to event list display page 
	 * @return
	 */
	public String goToEventDisplayListPage(){
		if(flag_GoToParent){
			flag_GoToParent = false;
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec=context.getExternalContext();
			try{
				ec.redirect(EventDataModelManagerImpl.EventListPageLocation);
			}catch(IOException e){
				return "failure";
			}
			context.responseComplete();			
		}
		return "parent";
	}	

	public boolean flag_ConfirmCancel = false;
	public boolean isFlag_ConfirmCancel() {
		return flag_ConfirmCancel;
	}
	/**
	 * Function for set confirmCancel flag, this function main to fix the bug for invoke from Struts framework to JSF framework 
	 * @param flag_ConfirmCancel
	 */
	public void setFlag_ConfirmCancel(boolean flag_ConfirmCancel) {
		this.flag_ConfirmCancel = flag_ConfirmCancel;
	}
	/**
	 * Function for invoke JSF presentation layer forward to cancel page
	 * @return
	 */
	public String confirmCancel(){
		flag_ConfirmCancel=true;
		eventDataModel.setRenewFlag(true);
		
		return confirmCancelInvoker();
	}
	/**
	 * Function for invoke JSF presentation layer forward to cancel page with fix Struts to JSF bug
	 * @return
	 */	
	public String confirmCancelInvoker(){
		if(flag_ConfirmCancel){
			flag_ConfirmCancel=false;
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec=context.getExternalContext();
			try{
//				ec.redirect(EventDataModelManagerImpl.EventListPageLocation);
				ec.redirect(EventDataModelManagerImpl.ProgramPageLocation);
			}catch(IOException e){
				return "failure";
			}
			context.responseComplete();
		}
		return "success";
	}	
	
	/**
	 * Function for transfer EventDataModel to Event
	 * @param model
	 * @return
	 */	
	public Event transferEventDataModelToEvent(EventDataModel model){
		Event event = new Event();
		event.setEndTime(model.getEndTime());
		event.setEventName(model.getEventName());
		event.setParticipants(model.getEventParticipants());
		Set<EventParticipant> epSet = new HashSet<EventParticipant>();
		for(int i=0;i<model.getEventParticipants().size();i++){
			epSet.add(model.getEventParticipants().get(i));
		}
		event.setEventParticipants(epSet);
		if(model.getIssuedTime() == null)
		{
			event.setIssuedTime(model.getReceivedTime());
		}
		else
		{
			event.setIssuedTime(model.getIssuedTime());
		}
		event.setEventStatus(model.getEventStatus());
		event.setManual(model.isManual());
		event.setProgramName(model.getProgramName());
		event.setReceivedTime(model.getReceivedTime());
		event.setStartTime(model.getStartTime());
		event.setState(model.getState());
		event.setWarnings(model.getWarnings());
		return event;
	}
	public EventDataModel transferEventToEventDataModel(Event event,EventDataModel model){
		if(model!=null){
			model.setEndTime(event.getEndTime());
			model.setEventName(event.getEventName());
			model.setIssuedTime(event.getIssuedTime());
			model.setEventStatus(event.getEventStatus());
			model.setManual(event.isManual());
			model.setProgramName(event.getProgramName());
			model.setReceivedTime(event.getReceivedTime());
			model.setStartTime(event.getStartTime());
			model.setState(event.getState());
			model.setWarnings(event.getWarnings());	
			
			if((model instanceof DBPNoBidEventDataModel) && (event instanceof DBPEvent)){
				model.setRespondBy(((DBPEvent)event).getRespondBy());
				model.setDrasRespondBy(((DBPEvent)event).getDrasRespondBy());
			}
			
			List<EventParticipant> eventParticipants = event.getParticipants();
			model.getParticipants().clear();
			model.getClients().clear();
			for(int i =0;i<eventParticipants.size();i++){
				final Participant participant = eventParticipants.get(i).getParticipant();
				model.getParticipants().add(participant);				
			}
			List<Participant> clientList = new ArrayList<Participant>();
			if(model.getProgramName()!=""){
				List<Participant> participantAndClientList =getProgramParticipantManager().getParticipantsForProgramAsObject(model.getProgramName());
				for(int i=0;i<participantAndClientList.size();i++){
					if(participantAndClientList.get(i).isClient()){
						clientList.add(participantAndClientList.get(i));
					}
				}
				for(int i=0;i<clientList.size();i++){
					Participant client = clientList.get(i);
					for(int j=0;j<model.getParticipants().size();j++){
						String participantName = model.getParticipants().get(j).getParticipantName();
						if(participantName.equalsIgnoreCase(client.getParent())){
							model.getClients().add(client);
							break;
						}
					}
				}
			}
		}
		return model;
	}	
	/**
	 * Function for transfer EventDataModel to DBPEvent
	 * @param model
	 * @return
	 */	
	public Event transferEventDataModelToDBPEvent(EventDataModel model){
		DBPEvent event = new DBPEvent();
		event.setEndTime(model.getEndTime());
		event.setEventName(model.getEventName());
		event.setParticipants(model.getEventParticipants());
		Set<EventParticipant> epSet = new HashSet<EventParticipant>();
		for(int i=0;i<model.getEventParticipants().size();i++){
			epSet.add(model.getEventParticipants().get(i));
		}
		event.setEventParticipants(epSet);
		event.setIssuedTime(model.getIssuedTime());
		event.setEventStatus(model.getEventStatus());
		event.setManual(model.isManual());
		event.setProgramName(model.getProgramName());
		event.setReceivedTime(model.getReceivedTime());
		event.setStartTime(model.getStartTime());
		event.setState(model.getState());
		event.setWarnings(model.getWarnings());
		
		event.setRespondBy(model.getRespondBy());
		event.setDrasRespondBy(model.getDrasRespondBy());
		
		return event;
	}
	
	/**
	 * Function for transfer EventParticipant object to EventInfoInstance object
	 * Warn that in the UtilityDREvent model , the information can not contain the participant which is clients.
	 * But in the Event model ,the eventparticipant attributes needs contain both participant which is participant and clients.
	 * This is really funny.
	 * @param model
	 * @return
	 */
	public UtilityDREvent.EventInformation transferEventParticipantsToEventInfoInstance(EventDataModel model){
		UtilityDREvent.EventInformation eventInformation = new UtilityDREvent.EventInformation();
		eventInformation.getEventInfoInstance();
		

		if(model.getEventParticipants()!=null){
			for(int i=0;i<model.getEventParticipants().size();i++){
				EventParticipant eventParticipant =model.getEventParticipants().get(i);
				EventInfoInstance eventInfoInstance = new EventInfoInstance();
				if(eventInfoInstance.getParticipants()==null){
					eventInfoInstance.setParticipants(new EventInfoInstance.Participants());
				}
				boolean isClient = eventParticipant.getParticipant().isClient();
				if(isClient){
					//Do nothing
				}else{
					String accountNumber =eventParticipant.getParticipant().getAccountNumber();
					eventInfoInstance.getParticipants().getAccountID().add(accountNumber);
					eventInformation.getEventInfoInstance().add(eventInfoInstance);	
				}
			}
		}
		return eventInformation;
	}
	
	
	/**
	 * Function for add EventDataModel into JSF session cache
	 * @param model
	 * @return
	 */	
	public boolean addSessionCache(EventDataModel model){
		boolean flag =false;
		if(model!=null){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("eventDataModel",model);
			flag = true;
		}
		return flag;
	}

	@Override
	public void addMsgError(String message) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,message, message));
	}

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowKey(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void walk(FacesContext fc, DataVisitor dv, Range range, Object o) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowAvailable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowIndex(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setWrappedData(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
