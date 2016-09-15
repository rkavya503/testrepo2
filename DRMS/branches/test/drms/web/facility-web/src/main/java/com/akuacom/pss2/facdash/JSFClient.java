/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.JSFClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.openadr.dras.eventstate.ListOfEventStates;

import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientManualSignal;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.PDataEntryEAO;
import com.akuacom.pss2.data.usage.CurrentUsageDataEntryEAO;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.JSFClientLayout;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.Openadr2Util;
import com.akuacom.pss2.utilopws.stubs.ClientUtil;

/**
 * The Class JSFClient.
 */
public class JSFClient implements Serializable, JSFClientLayout {
    static final String CONTROL_STATE_AUTO = "auto";
    static final String CONTROL_STATE_MANUAL = "manual";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 0L;

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(JSFClient.class
            .getName());

    private static final long MS_IN_MIN = 1000 * 60;
    private static final long MIN_IN_DAY = 24 * 60;
    private static final long MIN_IN_HR = 60;

    /** The edit. */
    private boolean edit;

    /** The name. */
    private String name;

    /** The password. */
    private String password;

    /** The confirm password. */
    private String confirmPassword;

    /** The account number. */
    private String accountNumber;
    private String uuid;

    /** The client type. */
    private String clientType;

    /** The address. */
    private String address;

    /** The grid location. */
    private String gridLocation;

    /** The latitude. */
    private double latitude;

    /** The longitude. */
    private double longitude;

    /** The programs string. */
    private String programsString;

    /** The events string. */
    private String eventsString;

    /** The event status. */
    private String eventStatus;

    /** The mode. */
    private String mode;

    /** The last contact. */
    private Date lastContact;
    
    /** The comm status. */
    private String commStatus;
    
    private Date lastUsageContact;
    
    private String usageCommStatus = "";
    
    private boolean clientAllowedToOptOut;
    
    private boolean clientCanFailConfirmation;
    
    private boolean dataUsageEnabled = false;
    
    private boolean retained;

    /** The contacts. */
    private List<JSFContact> contacts = new ArrayList<JSFContact>();

    /** The programs. */
    private List<JSFClientProgram> programs = new ArrayList<JSFClientProgram>();

    /** The events. */
    private List<JSFEvent> events = new ArrayList<JSFEvent>();
    
    /** The EndPoints. */
    private List<JSFEndPoint> endPoints = new ArrayList<JSFEndPoint>();

    /** The event states xml. */
    private String eventStatesXML;

    /** The control state. */
    private String controlState = CONTROL_STATE_AUTO;

    /** The control pending. */
    private String controlPending = "off";

    /** The control mode. */
    private String controlMode = "normal";

    private Date controlExpiresDate = new Date();

    /** The current pw. */
    private String currentPW;

    /** The new pw. */
    private String newPW;

    /** The confirm pw. */
    private String confirmPW;

    /** The check PW. */
    private String checkPW;

    /** The delete. */
    private boolean delete;

    /** The contact name. */
    private String contactKey;

    /** The program name. */
    private String programName;

    private String eventName;

    /** The selected tab. */
    private String selectedTab;
    
    private String tempName;

    private String tempEmail;
    
    private List<Participant> descendantClients;
	
	public List<Participant> getDescendantClients() {
		return descendantClients;
	}

	public void setDescendantClients(List<Participant> descendantClients) {
		this.descendantClients = descendantClients;
	}

    /** can the client be edited to participate for each program */
    private List<ProgramParticipant> clientConfig = new ArrayList<ProgramParticipant>();
    
    private boolean userAuthorized = false;

    /** The clients. */
    private List<JSFClient> clients = new ArrayList<JSFClient>();

    private boolean controlEdit;

    private boolean push;

    private String ip;

    private boolean optionPasswordPanel;
    private boolean optionPushPanel;
    private boolean optStatus = true;
    
    /** demand limiting */
    private boolean clientDemandLimitingEnabled = false;

    //DRMS-6121
    private String deviceType;
    
    private boolean batchUpdateEnabled;
	private ArrayList nonConsolidationPrograms;
	
	
	private EndpointMapping endpointMapping;

    public boolean isClientDemandLimitingEnabled() {
		return clientDemandLimitingEnabled;
	}

	public void setClientDemandLimitingEnabled(boolean clientDemandLimitingEnabled) {
		this.clientDemandLimitingEnabled = clientDemandLimitingEnabled;
	}

	/**
     * Instantiates a new jSF client.
     */
    @SuppressWarnings("deprecation")
    public JSFClient() {
        controlExpiresDate.setHours(23);
        controlExpiresDate.setMinutes(59);
        buildViewLayout();
    }

    // this is used for edit
    /**
     * Load.
     * 
     * @param clientName
     *            the name
     */
    public void load(String clientName) {
  		ClientManager cManager = EJBFactory.getBean(ClientManager.class);
		Participant client = cManager.getClientLJFByName(clientName);
        this.load(client);
        loadEndpointMapping();
    }

    public String remainingTime(Date expire) {

        Date useDate = expire;
        Date today = new Date();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(useDate);
        calendar2.setTime(today);

        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();

        long diffMS = milliseconds1 - milliseconds2;

        long diffMinutes = diffMS / MS_IN_MIN + 1;
        long diffDays = diffMinutes / MIN_IN_DAY;
        diffMinutes -= diffDays * MIN_IN_DAY;
        long diffHours = diffMinutes / MIN_IN_HR;
        diffMinutes -= diffHours * MIN_IN_HR;

        return diffDays + " Days, " + diffHours + " Hours, " + diffMinutes
                + " Minutes";
    }

    
    public void load(Participant client) {
    	load(client, null,true);
    }
    private EndpointManager endpointMappingManager;
    private EndpointManager getEndpointMappingManager(){
    	if(endpointMappingManager==null){
    		endpointMappingManager = EJBFactory.getBean(EndpointManager.class);	
    	}
    	return endpointMappingManager;
    }
    private void loadEndpointMapping(){    	
    	EndpointMapping instance = getEndpointMappingManager().findByParticipantName(this.getName());
		if(instance==null){
			instance = new EndpointMapping();
			instance.setParticipantName(this.getName());
		}
		this.setEndpointMapping(instance);
    } 
    
    /**
     * Function used for JSF page presentate
     * @return
     */
    public boolean isEndpointMappingExist(){
    	if(this.getEndpointMapping()!=null&&(!this.getEndpointMapping().getUUID().equalsIgnoreCase(""))){
    		return true;
    	}else{
    		return false;
    	}
    }
    public void generateVenID(){
    	String venID = Openadr2Util.generateVenID();
    	if(this.getEndpointMapping()!=null&&(!this.getEndpointMapping().getParticipantName().equalsIgnoreCase(""))){
    		this.getEndpointMapping().setVenId(venID);
    		getEndpointMappingManager().createEndpointParticipantLink(endpointMapping);
    	}
    }
    public void saveEndpointMapping(){
    	if(this.getEndpointMapping()!=null){
    		if((!this.getEndpointMapping().getVenId().equalsIgnoreCase(""))&&(!this.getEndpointMapping().getParticipantName().equalsIgnoreCase(""))){
    			getEndpointMappingManager().createEndpointParticipantLink(endpointMapping);
    		}
    	}
    }
    
    
    public void load(Participant client, Participant parent,boolean loadContacts) {
        ParticipantManager participantManager = EJBFactory
            .getBean(ParticipantManager.class);
        uuid = client.getUUID();
        name = client.getParticipantName();
        accountNumber = client.getAccountNumber();
        address = client.getAddress();
        gridLocation = client.getGridLocation();
        latitude = client.getLatitude();
        longitude = client.getLongitude();
        clientType = client.getTypeString();
        clientAllowedToOptOut = client.getClientAllowedToOptOut().booleanValue();
        clientCanFailConfirmation = client.getClientCanFailConfirmation().booleanValue();
        //DRMS-6121
        deviceType = client.getDeviceType();
        
        if (client.getPush() != null)
            push = client.getPush() == 0 ? false : true;
        if (client.getIp() != null)
            ip = client.getIp();

        // disapling password panel
        setOptionPasswordPanel(true);
        Participant participantInfoForParent = null;
        Participant participantInfoForClient = null;
		if(client.getParent()!=null){
			participantInfoForParent = participantManager.getParticipant(client.getParent());
			participantInfoForClient = participantManager.getParticipant(client.getParent(),true);
			com.akuacom.pss2.program.ProgramManager programManager = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);
			descendantClients = programManager.getDescendantClients(client.getParent(), null);
		 }
		
		SystemManager sysManager = EJBFactory.getBean(SystemManager.class);
		PSS2Features features=sysManager.getPss2Features();

		if (client.isClient()) {
			this.setRetained(client.getRetained());
			Participant parentParticipant = null;
			if(parent != null){
				parentParticipant = parent;
			}else{
				parentParticipant = participantInfoForParent;
			}
			
			if (parentParticipant != null) {
				this.setClientDemandLimitingEnabled(parentParticipant.getDemandLimiting());
				setDataUsageEnabled(parentParticipant.getDataEnabler().booleanValue());
				if (this.isDataUsageEnabled() && parent==null) {
					Date lastUsageCommTime = null;
			        long timeOutTrigger = 15 * 60 * 1000; // 15 minutes
			        CurrentUsageDataEntryEAO det = EJB3Factory.getBean(CurrentUsageDataEntryEAO.class);
		        	lastUsageCommTime = det.getLastActualTimeByDatasourceOwner(FDUtils.getParticipantName());
		        	if (lastUsageCommTime==null) {
			        	PDataEntryEAO de = EJB3Factory.getBean(PDataEntryEAO.class);
			        	lastUsageCommTime = de.getLastActualTimeByDatasourceOwner(FDUtils.getParticipantName());
		        	}
			        
			       
			        Double clientTimeout=null;
			        Double clientTimeoutIncrement=1.0;
			        
		        	
		        	clientTimeout=features.getClientTimeout();
		        	if (features.getClientTimeoutIncrement() !=null)
		        		clientTimeoutIncrement=clientTimeoutIncrement+features.getClientTimeoutIncrement();
		        	if (clientTimeout!=null)
		        		timeOutTrigger = new Double(clientTimeout.doubleValue() * clientTimeoutIncrement.doubleValue() * 60.0 * 1000.0).longValue();
		        	
			        setLastUsageContact(lastUsageCommTime);
			        
			        long now = System.currentTimeMillis();
			        boolean offline = lastUsageCommTime == null || now - lastUsageCommTime.getTime() > timeOutTrigger;
	        		if (offline) {
	        			setUsageCommStatus("OFFLINE");
	        		} else {
	        			setUsageCommStatus("ONLINE");
	        		}
				}
			}
			
		} else {
			this.setClientDemandLimitingEnabled(client.getDemandLimiting());
		}

        contacts = new ArrayList<JSFContact>();
        if(loadContacts){
        	//List<ParticipantContact> pc = participantManager.getParticipantContactsForClient(client.getParticipantName());        		
        	for (ParticipantContact contact : client.getContacts()) {
                if (contact != null) {
                	JSFContact jsfContact = new JSFContact(contact);
                	jsfContact.setParticipantDemandLimitingEnabled(this.isClientDemandLimitingEnabled());
                    contacts.add(jsfContact);
                }
            }
        }
        	
        

        programs = new ArrayList<JSFClientProgram>();
        StringBuilder programsSB = new StringBuilder();
        List<String> programsLabel = new ArrayList<String>();
        boolean isForcastEnabled=features.isFeatureForcastEnabled();
        
        com.akuacom.pss2.program.participant.ProgramParticipantEAO programParticipantEAO = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.participant.ProgramParticipantEAO.class);
        List<ProgramParticipant> prgParticipant =  programParticipantEAO.findProgramClientByParticipantName(client.getParticipantName());
        for (ProgramParticipant programClient : prgParticipant) {
            JSFClientProgram jsfProgram = new JSFClientProgram(programClient,isForcastEnabled,features);

        

              if (FDUtils.getJSFProgram() != null && jsfProgram.getProgramName().equalsIgnoreCase(FDUtils.getJSFProgram().getName())  
            		  && !jsfProgram.getProgramName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)){

                    if (programClient.getState().intValue() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
                            jsfProgram.setParticipating(true);
                            String consolidationProgramName = jsfProgram.getConsolidationProgramName();
                            //programsSB.append(jsfProgram.getProgramName());
                            programsSB.append(consolidationProgramName);
                            programsSB.append(",");
                            
                            if(!programsLabel.contains(consolidationProgramName)){
                            	programsLabel.add(consolidationProgramName);
                            }
                    }

                    // find the participant's corresponding program participant and set
                    // the activated flag based upon that
                    
                    
                    
        			Participant participant = null;
        			if(parent != null){
        				participant = parent;
        			}else{
        				if(client.getParent().equalsIgnoreCase(FDUtils.getParticipantName())){
        					participant =participantInfoForParent;
        				}else{
        					participant = participantInfoForClient;
        				}
        			}
                    
                    
                     for (ProgramParticipant programParticipant : participant
                            .getProgramParticipants()) {
                        if (programParticipant.getProgramName().equals(
                                programClient.getProgramName())) {
                            jsfProgram.setActivated(programParticipant
                                    .getClientConfig() == 0 ? false : true);
                        }
                    }
                    programs.add(jsfProgram);
               
                 }else if (!jsfProgram.getProgramName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)){

                      if (programClient.getState().intValue() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
                         if (!jsfProgram.getProgramName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)){
                            jsfProgram.setParticipating(true);
                            String consolidationProgramName = jsfProgram.getConsolidationProgramName();
                            programsSB.append(consolidationProgramName);
                            //programsSB.append(jsfProgram.getProgramName());
                            programsSB.append(",");
                            if(!programsLabel.contains(consolidationProgramName)){
                            	programsLabel.add(consolidationProgramName);
                            }
                         }
                        }

                        // find the participant's corresponding program participant and set
                        // the activated flag based upon that
                        
                        
                        
            			Participant participant = null;
            			if(parent != null){
            				participant = parent;
            			}else{
            				if(client.getParent().equalsIgnoreCase(FDUtils.getParticipantName())){
            					participant =participantInfoForParent;
            				}else{
            					participant = participantInfoForClient;
            				}
            			}
                        
                        
                         for (ProgramParticipant programParticipant : participant
                                .getProgramParticipants()) {
                            if (programParticipant.getProgramName().equals(
                                    programClient.getProgramName())) {
                                jsfProgram.setActivated(programParticipant
                                        .getClientConfig() == 0 ? false : true);
                            }
                        }
                        programs.add(jsfProgram);

                 }
        }
        nonConsolidationPrograms = new ArrayList(programs);
        programs = CBPUtil.transferList(programs);
        
//        if (programsSB.length() == 0) {
//            programsString = "";
//        } else {
//            programsString = programsSB.substring(0, programsSB.length() - 1);
//        }
        programsString = "";
        if(programsLabel.size()>0){
        	 for(String program:programsLabel){
             	programsString=programsString+program+",";
             }
             programsString = programsString.substring(0,programsString.length()-1);
        }

        if (client.isManualControl()) {
            controlState = CONTROL_STATE_MANUAL;
            controlExpiresDate = client.getManualControlExpires();

            Set<ClientManualSignal> signalStates = client.getManualSignals();
            for (ClientManualSignal signalState : signalStates) {
                if (signalState == null)
                    continue;

                if (signalState.getName().equals("mode")) {
                    controlMode = signalState.getValue();
                } else if (signalState.getName().equals("pending")) {
                    controlPending = signalState.getValue();
                }
            }
        } else {
            controlState = CONTROL_STATE_AUTO;
        }

        loadEventStatus(client);

        lastContact = client.getCommTime();
        commStatus = client.getClientStatus().toString();
        
        int c = 0;
        StringBuffer sb = new StringBuffer();
        for (JSFEvent event : events) {
            c++;
            if (c > 1)
                sb.append(" , ");
            sb.append(event.getName());
        }
   
        eventsString = sb.toString();

        for (ClientManualSignal signalState : client.getManualSignals()) {
            if (signalState == null) {
                // ClientManualSignal ms = new ClientManualSignal();
                // ms.setParticipant(client);
            } else {
                if (client.isManualControl()
                        && signalState.getName().equals("mode")) {
                    mode = signalState.getValue();
                }
            }
        }
        
        
        endPoints = new ArrayList<JSFEndPoint>();
        JSFEndPoint jsfEndpoint = new JSFEndPoint(client);
        endPoints.add(jsfEndpoint);        

        clients = new ArrayList<JSFClient>();
        clients.add(this);
        
        
		batchUpdateEnabled = features.isAggBatchUpdateEnabled();
        
		if(batchUpdateEnabled){
			//com.akuacom.pss2.program.ProgramManager programManager = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);
			//descendantClients = programManager.getDescendantClients(client.getParent(), null);
			
			boolean inList = false;
			for(Participant p:descendantClients){
				if(p.getParticipantName().equalsIgnoreCase(client.getParticipantName())){
					inList = true;
					break;
				}
			}
			if(!inList){
				if(descendantClients==null) descendantClients = new ArrayList<Participant>();
				descendantClients.add(client);//add current selected client
			}
						
		}else{
			if(descendantClients==null) descendantClients = new ArrayList<Participant>();
			descendantClients.add(client);//add current selected client
		}
		
		
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isOptionPasswordPanel() {
        return optionPasswordPanel;
    }

    public void setOptionPasswordPanel(boolean optionPasswordPanel) {
        this.optionPasswordPanel = optionPasswordPanel;
    }

    public boolean isOptionPushPanel() {
        return optionPushPanel;
    }

    public void setOptionPushPanel(boolean optionPushPanel) {
        this.optionPushPanel = optionPushPanel;
    }

    public void optionPanelAction() {
        if (optionPushPanel)
            optionPasswordPanel = true;
        else
            optionPasswordPanel = false;
    }

    /**
     * Load event status.
     */
    private void loadEventStatus(Participant client) {
        ClientManager cm = EJBFactory.getBean(ClientManager.class);
        StringBuilder eventsSB = new StringBuilder();
        events = new ArrayList<JSFEvent>();
        List<EventState> eventStates = cm.getClientEventStates(client);
        if (eventStates != null) {
            ListOfEventStates listOfEventStates = new org.openadr.dras.eventstate.ListOfEventStates();
            for (EventState eventState : eventStates) {

               if (FDUtils.getJSFProgram() != null && eventState.getProgramName().equalsIgnoreCase(FDUtils.getJSFProgram().getName()) ){

					if (!eventState.getEventIdentifier().equals("")) {
						JSFEvent event = new JSFEvent();
						event.setName(eventState.getEventIdentifier());
						event.setProgramName(eventState.getProgramName());
						event.setStart(eventState.getStartTime());
						event.setEnd(eventState.getEndTime());
						event.setStatus(eventState.getEventStatus().toString());
						events.add(event);
						eventsSB.append(event.getName());
						eventsSB.append(",");
					}
					listOfEventStates.getEventStates().add(cm.parseEventState(name, eventState));

				} else {
					if (!eventState.getEventIdentifier().equals("")) {
						JSFEvent event = new JSFEvent();
						event.setName(eventState.getEventIdentifier());
						event.setProgramName(eventState.getProgramName());
						event.setStart(eventState.getStartTime());
						event.setEnd(eventState.getEndTime());
						event.setStatus(eventState.getEventStatus().toString());
						events.add(event);
						eventsSB.append(event.getName());
						eventsSB.append(",");
					}
					listOfEventStates.getEventStates().add(
							cm.parseEventState(name, eventState));
				}

            }
            eventStatesXML = ClientUtil.eventStatesToXML(listOfEventStates);
            eventStatus = EventState.loadEventStatus(eventStates.get(0).getEventStatus());

            mode = eventStates.get(0).getOperationModeValue().toString();
            if (controlState.equals(CONTROL_STATE_MANUAL)) {
                mode += EventState.MANUAL_SUFFIX;
                eventStatus += EventState.MANUAL_SUFFIX;
            }
            if (eventsSB.length() == 0) {
                eventsString = "";
            } else {
                eventsString = eventsSB.substring(0, eventsSB.length() - 1);
            }
        } else {
            eventStatus = EventState.EVENT_STATUS_NONE;
            eventsString = "";
            eventStatesXML = "";
        }

        lastContact = client.getCommTime();
        commStatus = ClientStatus.values()[client.getStatus()].toString();

    }

    /**
     * Done client action.
     * 
     * @return the string
     */
    public String doneClientAction() {
        return "done";
    }

    private void updatePrograms() {
        ParticipantManager participantManager = EJBFactory
                .getBean(ParticipantManager.class);

        ProgramParticipantManager programParticipantManager = EJBFactory
                .getBean(ProgramParticipantManager.class);
        List<String> currentList = participantManager
                .getProgramsForParticipant(name, true);
        List<String> addList = new ArrayList<String>();
        List<String> removeList = new ArrayList<String>();
        
        List<JSFClientProgram> clientPrograms = getPrograms();
        clientPrograms = CBPUtil.revertList(clientPrograms, nonConsolidationPrograms);
        for (JSFClientProgram jsfProgram : clientPrograms) {
            if (jsfProgram.isParticipating()
                    && !currentList.contains(jsfProgram.getProgramName())) {
                addList.add(jsfProgram.getProgramName());
            } else if (!jsfProgram.isParticipating()
                    && currentList.contains(jsfProgram.getProgramName())) {
                removeList.add(jsfProgram.getProgramName());
            }
        }
        for (String program : removeList) {
            boolean reverted = false;
            // check for active events
            Set<EventParticipant> epList = participantManager
                    .getParticipant(FDUtils.getParticipantName()).getEventParticipants();
            if (epList != null) {
                for (EventParticipant ep : epList) {
                    if (program.equals(ep.getEvent().getProgramName())) {
                        // revert UI participation back if in active event
                        for (JSFClientProgram jsfProgram : clientPrograms) {
                            if (program.equalsIgnoreCase(jsfProgram
                                    .getProgramName())
                                    && (!jsfProgram.isParticipating()))
                                jsfProgram.setParticipating(true);
                        }
                        FDUtils.addMsgError("Client is in active event "
                                + ep.getEvent().getEventName()
                                + " and can't be removed from program. You must opt out first.");
                        reverted = true;
                        break;
                    }
                }
            }
            if (!reverted) {
                // remove participantion from program
                ProgramParticipant pp = programParticipantManager
                        .getClientProgramParticipants(program, name, true);
                pp.setState(ProgramParticipant.PROGRAM_PART_DELETED);
                programParticipantManager.updateProgramParticipant(program,
                        name, true, pp);
            }
        }
        for (String program : addList) {
            ProgramParticipant pp = programParticipantManager
                    .getClientProgramParticipants(program, name, true);
            pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
            programParticipantManager.updateProgramParticipant(program, name,
                    true, pp);
        }
    }

    /**
     * Update client.
     * this method is too heavy, we need another lighter method, just update contacts only.
     */
    public void updateClient() {
        ParticipantManager participantManager = EJBFactory
                .getBean(ParticipantManager.class);
        Participant client = participantManager
                .getParticipant(name, true);

        client.setPush(isPush() == false ? 0 : 1);
        client.setIp(ip);
        //DRMS-6121
        client.setDeviceType(deviceType);
        client.setAddress(address);
        client.setGridLocation(gridLocation);
        client.setLatitude(latitude);
        client.setLongitude(longitude);
		if ("MANUAL".equalsIgnoreCase(this.clientType))
			client.setType((byte)2);
		else
			client.setType((byte)0);

        // contacts
        // TODO: might want to do a merge here - might be safer
        Set<ParticipantContact> clientContacts = new TreeSet<ParticipantContact>();
        for (JSFContact contact : contacts) {
            ParticipantContact tmpContact = contact.parseContact();
            tmpContact.setParticipant(client);
            clientContacts.add(tmpContact);
        }
        client.setContacts(clientContacts);
        updatePrograms();

        // control
        final boolean manual = controlState.equals(CONTROL_STATE_AUTO) ? false
                : true;

        client.setManualControl(manual);

        if (manual) {

            client.setManualControlExpires(controlExpiresDate);
            Set<ClientManualSignal> signalStates = client.getManualSignals();
            for (ClientManualSignal signalState : signalStates) {
                if (signalState == null)
                    continue;

                if (signalState.getName().equals("mode")) {
                    signalState.setValue(controlMode);
                } else if (signalState.getName().equals("pending")) {
                    signalState.setValue(controlPending);
                }
            }
        }

        participantManager.updateParticipant(client);
    }
    

    /**
     * Edits the program rules action.
     * 
     * @return the string
     */
    public String editCustomRulesAction() {
        for (JSFClientProgram program : getPrograms()) {
            if (program.getProgramName().equals(programName)) {
                FDUtils.setJSFClientProgram(program);
                program.editCustomRules();
                break;
            }
        }

        return "editRules";
    }

    /**
     * Edits the program rules listener.
     * 
     * @param e
     *            the e
     */
    public void editCustomRulesListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    /**
     * Edits the rtp shed strategies action.
     * 
     * @return the string
     */
    public String editRTPShedStrategiesAction() {
    	
   		String programClass="";
   		JSFClientProgram prgm=null;
   		
	    for (JSFClientProgram program : getPrograms()) {
	        if (program.getProgramName().equals(programName)) {
	        	programClass = program.getScertp2013BackingBean().getProgramClassName();
	        	prgm = program;
	            FDUtils.setJSFClientProgram(program);
	            break;
	        }
	    }
   	
        //RTP 2013 is enabled
        if(programClass.equals("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013")){
        	//jump to scertp-shed-strategies.jsp edit page 
            return "editStrategy2013";//getRTPShedStrategies().newStrategyAction();
        	
        }else{
        	prgm.editRTPShedStrategies();
        	  return "editRTPShedStrategies";
        }
       
    }
    
    //******************used for 2013RTP------start
    private RTPShedStrategies rtpShedStrategies; // +setter
	
	public RTPShedStrategies getRTPShedStrategies() {
			FacesContext context = FacesContext.getCurrentInstance();
			RTPShedStrategies instance = (RTPShedStrategies)context.getApplication()
		          .evaluateExpressionGet(context, "#{rtpShedStrategies}", RTPShedStrategies.class);
			rtpShedStrategies = instance;
		return rtpShedStrategies;
	}
	//******************used for 2013RTP------end

    /**
     * Edits the rtp shed strategies listener.
     * 
     * @param e
     *            the e
     */
    public void editRTPShedStrategiesListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    /**
     * Edits the rtp shed strategies action.
     * 
     * @return the string
     */
    public String editCPPShedStrategiesAction() {
        for (JSFClientProgram program : getPrograms()) {
            if (program.getProgramName().equals(programName)) {
                FDUtils.setJSFClientProgram(program);
                break;
            }
        }

        return "editCPPShedStrategies";
    }

    /**
     * Edits the rtp shed strategies listener.
     * 
     * @param e
     *            the e
     */
    public void editCPPShedStrategiesListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    /**
     * Edits the rtp shed strategies action.
     * 
     * @return the string
     */
    public String viewForecastAction() {
    	String programClass="";
    	 for (JSFClientProgram program : getPrograms()) {
             if (program.getProgramName().equals(programName)) {
            	 programClass = program.getScertp2013BackingBean().getProgramClassName();
                 FDUtils.setJSFClientProgram(program);
                 program.viewForecast();
                 break;
             }
         }
    	 
    	if(programClass.equals("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013")){
    		return "viewForecast2013";
    	}else{
    		return "viewForecast";
    	}
    }

    /**
     * Edits the rtp shed strategies listener.
     * 
     * @param e
     *            the e
     */
    public void viewForecastListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    /**
     * Opt out event action.
     * 
     * @return the string
     */
    public String optOutEventAction() {
        return "optOutEvent";
    }

    /**
     * Opt out event listener.
     * 
     * @param e
     *            the e
     */
    public void optOutEventListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    /**
     * Edits the event rules action.
     * 
     * @return the string
     */
    public String editEventRulesAction() {
        for (JSFEvent event : events) {
            if (event.getName().equals(eventName)) {
                FDUtils.setJSFEvent(event);
                event.editRules();
                break;
            }
        }

        return "editEventRules";
    }

    /**
     * Edits the event rules listener.
     * 
     * @param e
     *            the e
     */
    public void editEventRulesListener(ActionEvent e) {
        eventName = e.getComponent().getAttributes().get("eventName")
                .toString();
    }
    
    /**
     * Edits the event rules action.
     * 
     * @return the string
     */
    public String editClientSignalsAction() {
        for (JSFEvent event : events) {
            if (event.getName().equals(eventName)) {
                FDUtils.setJSFEvent(event);
                break;
            }
        }

        return "editClientSignals";
    }

    /**
     * Edits the event rules listener.
     * 
     * @param e
     *            the e
     */
    public void editClientSignalsListener(ActionEvent e) {
        eventName = e.getComponent().getAttributes().get("eventName")
                .toString();
    }

    /**
     * Geocode action.
     * 
     * @return the string
     */
    public String geocodeAction() {
        latitude = 100.0;
        longitude = 100.0;
        return "back";
    }

    /**
     * New contact action.
     * 
     * @return the string
     */
    public String newContactAction() {
        FDUtils.setJSFContact(new JSFContact());
        FDUtils.getJSFContact().setParticipantDemandLimitingEnabled(this.isClientDemandLimitingEnabled());
        return "newContact";
    }

    /**
     * Edits the contact action.
     * 
     * @return the string
     */
    public String editContactAction() {
        for (JSFContact contact : contacts) {
            if (contact.getKey().equals(contactKey)) {
                contact.setEdit(true);
                FDUtils.setJSFContact(contact);
                break;
            }
        }
        return "editContact";
    }

    /**
     * Creates the contact action.
     * 
     * @return the string
     */
    public String createContactAction() {
    	JSFContact contact = FDUtils.getJSFContact();
        contact.setParticipantDemandLimitingEnabled(this.isClientDemandLimitingEnabled());
        return baseCreateContactAction(contact);
    }

    public String createSimpleContactAction() {
    	JSFContact contact = new JSFContact();
    	contact.setParticipantDemandLimitingEnabled(this.isClientDemandLimitingEnabled());
    	contact.setName(getTempName());
    	contact.setAddress(getTempEmail());
    	contact.setCommNotification(true);
    	contact.setEventNotification(ContactEventNotificationType.FullNotification.toString());
    	
    	boolean error = false;
    	
    	if (contact.getName() == null || contact.getName().trim().length() < 1) {
    		FDUtils.addMsgError("navForm", "Contact Name is required");
    		error = true;
    	}
    	
    	if (contact.getAddress() == null || !EmailValidator.getInstance().isValid(contact.getAddress())) {
    		FDUtils.addMsgError("navForm", "A valid email address is required");
    		error = true;
    	}
    	
    	if (error) {
    		return "errorContact";
    	}
        
        return baseCreateContactAction(contact);
    }
    
    private String baseCreateContactAction(JSFContact contact) {
    	for (JSFContact c : contacts) {
    		if (c != null && c.getAddress() != null && c.getName() != null) {
    			if (c.getAddress().equalsIgnoreCase(contact.getAddress()) && 
    					c.getName().equals(contact.getName())) {
    				FDUtils.addMsgError("navForm", "Duplicate contacts are not allowed");
    	    		return "errorContact";
    			}
    		}
    	}
    	
    	try {
    		contacts.add(contact); // backing bean has been refreshed
    		
    		
    		 ParticipantContact tmpContact = contact.parseContact();
             
             List<String> uuids = getDecendantUUIDS();
             ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
         	 participantManager.persistParticipantContact(tmpContact, getUuid(), uuids);
    		//updateClientContacts();// just update contacts only, instead of update the entire client entity
    		//updateClient();
    	} catch (Exception ex) {
    		//FDUtils.addMsgError("navForm", "Error creating contact - " + ex.getMessage());
            FDUtils.addMsgError("You need to add clients to this participant first");
    		return "errorContact";
    	}
        return "createContact";
    }
    
    public String clearTemps() {
    	this.setTempEmail("");
    	this.setTempName("");
    	
    	return "ok";
    }

    /**
     * Update contact action.
     * 
     * @return the string
     */
    public String updateContactAction() {
//		NSSettingsManager NSSettingsManager = EJBFactory.getBean(NSSettingsManager.class);
//		NSSettings settings = NSSettingsManager.getNSSettings();

        JSFContact newJSFContact = FDUtils.getJSFContact();
        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        Participant client = participantManager.getParticipant(name, true);

        Set<ParticipantContact> clientContacts = client.getContacts();
        ParticipantContact tmpContact = newJSFContact.parseContact();

        for (ParticipantContact contact : clientContacts) {
            if (contact.getUUID().equalsIgnoreCase(newJSFContact.getUUID())) {
            	List<String> uuids = getDecendantUUIDS();
            	participantManager.cascadeUpdateParticipantContact(tmpContact, contact.getType(), contact.getAddress(), contact.getDescription(), this.getUuid(),uuids);
                break;
            }
        }
        
        //need to refresh backing bean? don't need


        return "updateContact";
    }

	private List<String> getDecendantUUIDS() {
		List<Participant>  all = this.getDescendantClients();
		List<String> uuids = new ArrayList<String>();
		for(Participant p :all){
			uuids.add(p.getUUID());
		}
		return uuids;
	}

    /**
     * Contact listen.
     * 
     * @param e
     *            the e
     */
    public void contactListen(ActionEvent e) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        contactKey = (String) ctx.getExternalContext().getRequestParameterMap()
                .get("contactKey");
    }

    /**
     * Delete contacts action.
     * 
     * @return the string
     */
    public String deleteContactsAction() {
        Iterator<JSFContact> i = contacts.iterator();
        List<ParticipantContact> deleted = new ArrayList<ParticipantContact>();
        List<JSFContact> retained = new ArrayList<JSFContact>();
        while (i.hasNext()) {
            JSFContact contact = i.next();
            if (contact.isDelete()) {
                ParticipantContact tmpContact = contact.parseContact();
                deleted.add(tmpContact);
            }else{
            	retained.add(contact);
            }
        }
        
        this.setContacts(retained);//refresh backing bean
        List<String> uuids = getDecendantUUIDS();
        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        participantManager.cascadeRemoveParticipantContact(deleted, getUuid(), uuids);
        
//        FDUtils.getJSFClient().updateClient();
        return "deleteContacts";
    }

    /**
     * Delete internal contacts action.
     *
     * @return the string
     */
    public String deleteAllContactsAction() {
        Iterator<JSFContact> i = contacts.iterator();
        while (i.hasNext()) {
            JSFContact contact = i.next();
//            if (contact.isExternal()) {
                i.remove();
//            }
        }       
        FDUtils.getJSFClient().updateClient();
        return "deleteContacts";
    }

    /**
     * Cancel contact action.
     * 
     * @return the string
     */
    public String cancelContactAction() {
        return "cancelContact";
    }

    /**
     * New account action.
     * 
     * @return the string
     */
    public String newAccountAction() {
        FDUtils.setJSFAccount(new JSFAccount());
        return "newAccount";
    }

    /**
     * Update control action.
     * 
     * @return the string
     */
    public String updateControlAction() {
        log.debug("controlState: " + controlState);
        log.debug("controlExpiresDate: " + controlExpiresDate);

        if (controlExpiresDate == null) {
            FDUtils.addMsgError("Invalid date");
            return null;
        }

        ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
        Participant client = clientManager.getClientWithManualSignals(name);
        final boolean manual = controlState.equals(CONTROL_STATE_AUTO) ? false
                : true;
        client.setManualControl(manual);

        if (manual) {
            client.setManualControlExpires(controlExpiresDate);

            if (controlExpiresDate.getTime() > new Date().getTime()) {
                controlState = CONTROL_STATE_MANUAL;
                String controlMessage = "Time remaining until return to Auto-DR: "
                        + " "
                        + remainingTime(getControlExpiresDate());
                FDUtils.addMsgInfo(controlMessage);
            } else {
                FDUtils.addMsgError("You can not set the return to Auto-DR time in the past");
                return null;
            }

            Set<ClientManualSignal> signalStates = client.getManualSignals();
            for (ClientManualSignal signalState : signalStates) {
                if (signalState == null)
                    continue;

                if (signalState.getName().equals("mode")) {
                    signalState.setValue(controlMode);
                } else if (signalState.getName().equals("pending")) {
                    signalState.setValue(controlPending);
                }
            }
            client.setManualSignals(signalStates);

        } else {
            FDUtils.addMsgInfo("Auto-DR enabled");
        }
        clientManager.updateClient(client);
        loadEventStatus(client);

        clientManager.updateExpireManualControlForClientTimer(
                client.getParticipantName(), client.getManualControlExpires(),
                client.isManualControl());

        return null;
    }

    /**
     * Update password action.
     * 
     * @return the string
     */

    public void checkPasswordAction() {
        final AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        final AccMgrWS stub = accmgrClient.getAccmgr();
        User user = stub.getUserByName("CLIENT", name);
        ValidatePassword.validate(user.getPassword(), checkPW);
    }

    public void checkPasswordListener(ValueChangeEvent e){
    	try{
    		checkPasswordAction();	
    	}catch(Exception exception){
    		
    	}
	}
    public void updatePasswordAction() {
        final AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        // final AccMgrWS stub = accmgrClient.getAccmgr();
        // User user = stub.getUserByName("CLIENT", name);

        if (!ValidatePassword.validate(newPW, confirmPW, false)) {
           
        } else {

            try {
                ParticipantManager ParticipantManagerBean = (ParticipantManager) EJBFactory
                        .getBean(ParticipantManager.class);
                Participant p = ParticipantManagerBean.getParticipant(name,
                        true);
                ParticipantManagerBean.setParticipantPassword(p.getUUID(),
                        newPW);
            } catch (Exception e) {
                FDUtils.addMsgError("Error Updating password");
                return;
            }

            FDUtils.addMsgInfo("Password updated");
        }
    }
    
    /**
     * Program participating action.
     * 
     * @return the string
     */
    public String programParticipatingAction() {
        updatePrograms();
        return null;
    }

    /**
     * Edits the bid mapping action.
     * 
     * @return the string
     */
    public String editBidMappingAction() {
        for (JSFClientProgram program : getPrograms()) {
            if (program.getProgramName().equals(programName)) {
                FDUtils.setJSFClientProgram(program);
                break;
            }
        }
        // TODO: what if it isn't found?
        return "editBidMapping";
    }

    /**
     * Edits the bid mapping listener.
     * 
     * @param e
     *            the e
     */
    public void editBidMappingListener(ActionEvent e) {
        programName = e.getComponent().getAttributes().get("programName")
                .toString();
    }

    public void setControlEdit(boolean controlEdit) {
        this.controlEdit = controlEdit;
    }

    /**
     * Type listener.
     */
    public boolean isControlEdit() {
        if (controlState.equals(CONTROL_STATE_AUTO)) {
            setControlEdit(true);
            return true;
        } else {
            setControlEdit(false);
            return false;
        }
    }

    /**
     * Gets the programs.
     * 
     * @return the programs
     */
    public List<JSFClientProgram> getPrograms() {
        return programs;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the confirm password.
     * 
     * @return the confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the confirm password.
     * 
     * @param confirmPassword
     *            the new confirm password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the account number.
     * 
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     * 
     * @param accountNumber
     *            the new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     * 
     * @param address
     *            the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the grid location.
     * 
     * @return the grid location
     */
    public String getGridLocation() {
        return gridLocation;
    }

    /**
     * Sets the grid location.
     * 
     * @param gridLocation
     *            the new grid location
     */
    public void setGridLocation(String gridLocation) {
        this.gridLocation = gridLocation;
    }

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     * 
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     * 
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the programs string.
     * 
     * @return the programs string
     */
    public String getProgramsString() {
        return programsString;
    }

    /**
     * Gets the event status.
     * 
     * @return the event status
     */
    public String getEventStatus() {
        return eventStatus.toUpperCase();
    }

    /**
     * Sets the event status.
     * 
     * @param eventStatus
     *            the new event status
     */
    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Gets the last contact.
     * 
     * @return the last contact
     */
    public Date getLastContact() {
        return lastContact;
    }

    /**
     * Sets the last contact.
     * 
     * @param lastContact
     *            the new last contact
     */
    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    public Date getLastUsageContact() {
		return lastUsageContact;
	}

	public void setLastUsageContact(Date lastUsageContact) {
		this.lastUsageContact = lastUsageContact;
	}

	public String getUsageCommStatus() {
		return usageCommStatus;
	}

	public void setUsageCommStatus(String usageCommStatus) {
		this.usageCommStatus = usageCommStatus;
	}

	public boolean isDataUsageEnabled() {
		return dataUsageEnabled;
	}

	public void setDataUsageEnabled(boolean dataUsageEnabled) {
		this.dataUsageEnabled = dataUsageEnabled;
	}

	/**
     * Gets the comm status.
     * 
     * @return the comm status
     */
    public String getCommStatus() {
        return commStatus;
    }

    /**
     * Sets the comm status.
     * 
     * @param commStatus
     *            the new comm status
     */
    public void setCommStatus(String commStatus) {
        this.commStatus = commStatus;
    }

    /**
     * Gets the contacts.
     * 
     * @return the contacts
     */
    public List<JSFContact> getContacts() {
        return contacts;
    }

    /**
     * Sets the contacts.
     * 
     * @param contacts
     *            the new contacts
     */
    public void setContacts(List<JSFContact> contacts) {
        this.contacts = contacts;
    }

    /**
     * Checks if is edits the.
     * 
     * @return true, if is edits the
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * Sets the edits the.
     * 
     * @param edit
     *            the new edits the
     */
    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    /**
     * Gets the client type.
     * 
     * @return the client type
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * Sets the client type.
     * 
     * @param clientType
     *            the new client type
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * Checks if is delete.
     * 
     * @return true, if is delete
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Sets the delete.
     * 
     * @param delete
     *            the new delete
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * Gets the control state.
     * 
     * @return the control state
     */
    public String getControlState() {
        return controlState;
    }

    /**
     * Sets the control state.
     * 
     * @param controlState
     *            the new control state
     */
    public void setControlState(String controlState) {
        this.controlState = controlState;
    }

    /**
     * Gets the control pending.
     * 
     * @return the control pending
     */
    public String getControlPending() {
    	return controlPending;
    }

    /**
     * Sets the control pending.
     * 
     * @param controlPending
     *            the new control pending
     */
    public void setControlPending(String controlPending) {
        this.controlPending = controlPending;
    }

    /**
     * Gets the control mode.
     * 
     * @return the control mode
     */
    public String getControlMode() {
        return controlMode;
    }

    /**
     * Sets the control mode.
     * 
     * @param controlMode
     *            the new control mode
     */
    public void setControlMode(String controlMode) {
        this.controlMode = controlMode;
    }

    public Date getControlExpiresDate() {
        return controlExpiresDate;
    }

    public void setControlExpiresDate(Date controlExpiresDate) {
        this.controlExpiresDate = controlExpiresDate;
    }

    /**
     * Gets the current pw.
     * 
     * @return the current pw
     */
    public String getCurrentPW() {
        return currentPW;
    }

    /**
     * Sets the current pw.
     * 
     * @param currentPW
     *            the new current pw
     */
    public void setCurrentPW(String currentPW) {
        this.currentPW = currentPW;
    }

    /**
     * Gets the new pw.
     * 
     * @return the new pw
     */
    public String getNewPW() {
        return newPW;
    }

    /**
     * Sets the new pw.
     * 
     * @param newPW
     *            the new new pw
     */
    public void setNewPW(String newPW) {
        this.newPW = newPW;
    }

    /**
     * Gets the confirm pw.
     * 
     * @return the confirm pw
     */
    public String getConfirmPW() {
        return confirmPW;
    }

    /**
     * Sets the confirm pw.
     * 
     * @param confirmPW
     *            the new confirm pw
     */
    public void setConfirmPW(String confirmPW) {
        this.confirmPW = confirmPW;
    }

    public String getCheckPW() {
        return checkPW;
    }

    /**
     * Sets the confirm pw.
     * 
     * @param checkPW
     *            the new confirm pw
     */
    public void setCheckPW(String checkPW) {
        this.checkPW = checkPW;
    }

    /**
     * Gets the mode.
     * 
     * @return the mode
     */
    public String getMode() {
    	if(mode != null){
    		return mode.toUpperCase();
    	}
        return mode;
    }

    /**
     * Sets the mode.
     * 
     * @param mode
     *            the new mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the events.
     * 
     * @return the events
     */
    public List<JSFEvent> getEvents() {
        return events;
    }

    /**
     * Gets the selected tab.
     * 
     * @return the selected tab
     */
    public String getSelectedTab() {
        return selectedTab;
    }

    /**
     * Sets the selected tab.
     * 
     * @param selectedTab
     *            the new selected tab
     */
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    /**
     * Gets the event states xml.
     * 
     * @return the event states xml
     */
    public String getEventStatesXML() {
        return eventStatesXML;
    }

    /**
     * Sets the event states xml.
     * 
     * @param eventStatesXML
     *            the new event states xml
     */
    public void setEventStatesXML(String eventStatesXML) {
        this.eventStatesXML = eventStatesXML;
    }

    /**
     * Gets the events string.
     * 
     * @return the events string
     */
    public String getEventsString() {
        return eventsString;
    }

    /**
     * Sets the events string.
     * 
     * @param eventsString
     *            the new events string
     */
    public void setEventsString(String eventsString) {
        this.eventsString = eventsString;
    }

    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName
     *            the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the clients.
     * 
     * @return the clients
     */
    public List<JSFClient> getClients() {
        return clients;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName
     *            the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getContactKey() {
        return contactKey;
    }

	public void setContactKey(String contactKey) {
		this.contactKey = contactKey;
	}

	public List<ProgramParticipant> getClientConfig() {
		return clientConfig;
	}

	public boolean isOptStatus() {
		return optStatus;
	}

	public void setOptStatus(boolean optStatus) {
		this.optStatus = optStatus;
	}

	public boolean isClientAllowedToOptOut() {
		return clientAllowedToOptOut;
	}

	public void setClientAllowedToOptOut(boolean clientAllowedToOptOut) {
		this.clientAllowedToOptOut = clientAllowedToOptOut;
	}

	public boolean isClientCanFailConfirmation() {
		return clientCanFailConfirmation;
	}

	public void setClientCanFailConfirmation(boolean clientCanFailConfirmation) {
		this.clientCanFailConfirmation = clientCanFailConfirmation;
	}

	private static Map<String, Object> optValues;
	
	static {
		optValues = new LinkedHashMap<String, Object>();
		optValues.put("Opt-in", false); // label, value
		optValues.put("Opt-out", true);

	}

	public Map<String, Object> getOptValues() {
		return optValues;
	}

	public void updateOptControl() {
		JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
		ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
		Participant p = participantManager.getParticipant(jsfParticipant.getName());
		p.setOptOut(isOptStatus());
		participantManager.updateParticipant(p);
	}
	
	public void updateClientIsAllowedToOptOut() {
		ClientManager cManager = EJBFactory.getBean(ClientManager.class);
		Participant client = cManager.getClientOnly(name);
		client.setClientAllowedToOptOut(clientAllowedToOptOut);
		cManager.updateClient(client);
	}
	
	public void updateClientCanFailConfirmation() {
		ClientManager cManager = EJBFactory.getBean(ClientManager.class);
		Participant client = cManager.getClientOnly(name);
		client.setClientCanFailConfirmation(clientCanFailConfirmation);
		cManager.updateClient(client);
	}
	
	public void updateClientDeviceType(){
		ClientManager cManager = EJBFactory.getBean(ClientManager.class);
		Participant client = cManager.getClientOnly(name);
		client.setDeviceType(deviceType);
		cManager.updateClient(client);
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	public String getTempEmail() {
		return tempEmail;
	}

	public void setTempEmail(String tempEmail) {
		this.tempEmail = tempEmail;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	public boolean isBatchUpdateEnabled() {
		return batchUpdateEnabled;
	}

	public void setBatchUpdateEnabled(boolean batchUpdateEnabled) {
		this.batchUpdateEnabled = batchUpdateEnabled;
	}

	/**
	 * @return the endpointMapping
	 */
	public EndpointMapping getEndpointMapping() {
		
		return endpointMapping;
	}

	/**
	 * @param endpointMapping the endpointMapping to set
	 */
	public void setEndpointMapping(EndpointMapping endpointMapping) {
		this.endpointMapping = endpointMapping;
	}	
	
	
	private EndPoints endpoints = new EndPoints();
	private List<SelectItem>  venList;
	private boolean isAdminOrOperator = false;

	public boolean isAdminOrOperator() {
		return isAdminOrOperator;
	}

	public void setAdminOrOperator(boolean isAdminOrOperator) {
		this.isAdminOrOperator = isAdminOrOperator;
	}

	public EndPoints getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(EndPoints endpoints) {
		this.endpoints = endpoints;
	}	
	
	public List<SelectItem> getVenList() {
		return venList;
	}

	public void setVenList(List<SelectItem> venList) {
		this.venList = venList;
	}
	
	public boolean isUserAuthorized() {
		return userAuthorized;
	}

	public void setUserAuthorized(boolean userAuthorized) {
		this.userAuthorized = userAuthorized;
	}
	
	private void buildViewLayout(){

		try {
			
			getViewBuilderManager().buildJSFClientLayout(this);

		} catch (NamingException e) {

			// log exception

		}



	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{

		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");

	}
	
	public String changeVenID(){
		return null;
	}

	public List<JSFEndPoint> getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(List<JSFEndPoint> endPoints) {
		this.endPoints = endPoints;
	}
	
	
	
}