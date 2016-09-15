package com.akuacom.pss2.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.openadr.dras.eventstate.ListOfEventStates;

import scala.actors.threadpool.Arrays;

import com.akuacom.accmgr.ws.Role;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.cache.ClientCache;
import com.akuacom.pss2.cache.ConfirmationResult;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.ConfirmationLevel;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.email.ExceptionNotifierInterceptor;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.event.ClientConversationStateEAO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.aggregator.eao.ProgramAggregatorEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.queuedhttp.ClientPush;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.ModeSlot;
import com.akuacom.pss2.util.OperationModeValue;
import com.akuacom.pss2.util.PriceSchedule;
import com.akuacom.pss2.util.PriceScheduleEntry;
import com.akuacom.pss2.utilopws.stubs.ClientUtil;
import com.akuacom.utils.lang.TimingUtil;

@Stateless
public class ClientManagerBean extends TimerManagerBean implements ClientManager.R, ClientManager.L {

    @EJB
    ProgramManager.L programManager;
    @EJB
    ParticipantManager.L participantManager;
    @EJB
    EventManager.L eventManager;
    @EJB
    ParticipantEAO.L participantEAO;
    @EJB
    ClientEAO.L clientEAO;
    @EJB
    SystemManager.L systemManager;
    @EJB
    EndpointManager.L endpointManager;
    
    @EJB
    SCERTPProgramManager.L scertpPProgramManager;
    @EJB
    ContactManager.L contactManager;
//    @EJB
//    ReportManager.L report;
    @EJB 
    Notifier.L notifier;
    
    @EJB
	protected AsynchCaller.L asynchCaller;
    
    @EJB
    protected ClientPush.L httpPusher;
    @EJB
    protected EventParticipantEAO.L eventParticipantEAO;
    @EJB
    protected ProgramAggregatorEAO.L programAggregatorEAO;
    @EJB
    protected ClientConversationStateEAO.L clientConversationtStateEAO;
    @EJB
    protected CustomerReportManager.L customerReportManager;
    @Resource
    protected SessionContext context;
    public static final String CLIENT_MANUAL_CTRL_EXPIRE_TIMER = "CLIENT_MANUAL_CTRL_EXPIRE";
    private static final Logger log = Logger.getLogger(SystemManagerBean.class);

    private static String drasVersion = null;

    private ClientCache cache = ClientCache.getInstance();
    
    private EventStateCacheHelper eventCache = EventStateCacheHelper.getInstance();
    
    public SessionContext getSessionContext() {
        return context;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.akuacom.pss2.client.ClientManager#getParticipantLastPrice(java.lang
      * .String)
      */
	public double getParticipantLastPrice(String participantName) {
		try {
			return clientEAO.getClientLastPrice(participantName);
		} catch (Exception ex) {
			String message = "error getting last price for participant "
					+ participantName;
			log.warn(message, ex);
			return -1.0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.client.ClientManager#setParticipantLastPrice(java.lang
	 * .String, double)
	 */
	public void setParticipantLastPrice(String participantName, double lastPrice) {
		try {
			clientEAO.setClientLastPrice(participantName, lastPrice);
		} catch (Exception ex) {
			String message = "error setting last price for participant "
					+ participantName;
			log.warn(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.client.ClientManager#getParticipantLastContact(java.
	 * lang.String)
	 */
	/*
	 * public Date getParticipantLastContact(String participantName) {
	 * Participant participant =
	 * participantManager.getParticipant(participantName); return
	 * participantEAO.getParticipantLastContact(participant); }
	 */
	// PERF_COMMENT: new getPrice
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.client.ClientManager#getPrice(java.lang.String,
	 * double)
	 */
	public double getPrice(String participantName, double lastPrice) {
		double price = 1.0;
		Date now = new Date();
		boolean inEvent = false;

		// TODO: get this down to one participant update including updating
		// comms
		// comment out for tuning.
		// ClientConversationState eventState =
		// getParticipantEventStates(participantName).get(0);

		List<EventState> eventStates;
		Participant part = participantEAO.findByNameAndClient(participantName, true);
		eventStates = getClientEventStates(part.getParticipantName(), false);
		EventState eventState = eventStates.get(0);
		if (!eventState.getEventIdentifier().equals("")) {
			inEvent = true;
		}

		double savedLastPrice = getParticipantLastPrice(participantName);
		boolean success = savedLastPrice < 0.0 || lastPrice == savedLastPrice;

		final OperationModeValue value = eventState.getOperationModeValue();
		if (OperationModeValue.NORMAL == value) {
			price = 1.0;
		} else if (OperationModeValue.MODERATE == value) {
			price = 3.0;
		} else if (OperationModeValue.HIGH == value) {
			price = 5.0;
		} else if (OperationModeValue.SPECIAL == value) {
			price = 7.0;
		}
		setParticipantLastPrice(participantName, price);

        ClientConversationState convState = new ClientConversationState();
        convState.setEventStatus(eventState.getEventStatus());
        convState.setOperationModeValue(eventState.getOperationModeValue());
		updateParticipantCommunications(participantName, now, success, convState);
//		final EventSignal signal = new EventSignal();
//		signal.setProgramName("");
//		signal.setEventName("");
//		signal.setAccountId("");
//		signal.setParticipantName(participantName);
//		signal.setSignalTime(now);
//		signal.setSignalLevel("");
//		signal.setActualLevel(value.toString().toLowerCase());
//		report.reportClientPoll(signal);

		return price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.client.ClientManager#getPriceSchedule(java.lang.String,
	 * com.akuacom.pss2.util.PriceSchedule)
	 */
	@Deprecated
	public PriceSchedule getPriceSchedule(String participantName,
			PriceSchedule lastPriceSchedule) {
		// processPrograms();

		double lastPrice = -1.0;
		if (lastPriceSchedule != null) {
			lastPrice = lastPriceSchedule.getCurrentPriceDPKWH();
		}
		PriceSchedule priceSchedule = new PriceSchedule();
		priceSchedule
				.setCurrentPriceDPKWH(getPrice(participantName, lastPrice));
		priceSchedule.setEntries(new PriceScheduleEntry[0]);
		return priceSchedule;
	}

	// PERF_COMMENT: new isAPEEventPending
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.client.ClientManager#isAPEEventPending(java.lang.String)
	 */
	public boolean isAPEEventPending(String participantName) {

		List<EventState> eventStates;
		Participant part = participantManager.getParticipant(participantName,
				true);
		ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
		eventStates = clientManager.getClientEventStates(part
				.getParticipantName(), false);
		EventState eventState = eventStates.get(0);
		if (eventStates == null || eventStates.size() <= 0) {
			return false;
		}

		Participant participant = clientEAO.getClient(participantName);
		StringBuilder sb = new StringBuilder();
		sb.append(eventState.toString());
		sb.append("\n\nmanual control: ");
		sb.append(participant.isManualControl());

		boolean apePending = false;
		if (eventState.getEventStatus() == EventStatus.ACTIVE
				|| eventState.getEventStatus() == EventStatus.NEAR) {
			apePending = true;
		}
		return apePending;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.client.ClientManager#getClients(java.lang.String)
	 */
	public List<Participant> getClients(String participantName) {
		return clientEAO.getClientsByParticipant(participantName);
	}

	public SearchHandler searchClients(SearchHandler searchHandler,
			String participantName) {
		return clientEAO.searchClientsByParticipant(searchHandler,
				participantName, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.client.ClientManager#getClient(java.lang.String)
	 */
    public Participant getClient(String clientName) {
        return clientEAO.findByNameAndClient(clientName, true);
    }

    public Participant getClientLJF(String clientName) {
        return clientEAO.findByNameAndClientLJF(clientName, true);
    }

    /**
     * @param clientName
     * @return list of names of events that the named client is in, but in 
     * an opted-out state that doesn't appear in normal queries (messy solution)
     */
    private List<String> getClientEventOptOuts(String clientName) {
		final List<String> results = new ArrayList<String>();
		final List<EventParticipant> eps = clientEAO
				.findCurrentEventOptouts(clientName, true);
		for (EventParticipant eventParticipantEAO : eps) {
			results.add(eventParticipantEAO.getEvent().getEventName());
		}
		return results;
    }
        
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.client.ClientManager#removeClient(java.lang.String)
	 */
    @Override
	public void removeClient(String clientName) {
        
        /* Unfortunately, the usual methods to list what events a client
         * might be a participant in omit opted-out participation. 
         * This can leave a hidden DB reference that will blow chunks
         * when the cryptically participating (but not really) client is deleted
         * Se we have to go looking for opted out event participants and 
         * clean them up before deleting the client.
         */
        List<String> optOutEvents = getClientEventOptOuts(clientName);
        if (optOutEvents != null) {
            for (String optOutEvent : optOutEvents) {
                Event evt = eventManager.getEventPerf(optOutEvent);
                Set<EventParticipant> eParts = evt.getEventParticipants();
                List<EventParticipant> getRid = new ArrayList<EventParticipant>();
                for (EventParticipant ep : eParts) {                    
                    if (ep.getParticipant().getParticipantName().equals(clientName)) {
                        getRid.add(ep);
                    }
                }
                for (EventParticipant ep : getRid) {
                    eParts.remove(ep);
                }
            }
        }
        // End of opted-out EventParticipant cleanup
        removeClientEndpointMapping(clientName);
		clientEAO.removeClient(clientName);

		AccMgrWSClient accmgrClient = new AccMgrWSClient();
		accmgrClient.initialize();
		User user = null;
		try {
			user = accmgrClient.getAccmgr().getUserByName("CLIENT", clientName);
		} catch (Exception ex) {
			user = null;
			log.error("Failed to lookup user in account mgr prior to delete", ex);
		}
		if (user != null) {
			accmgrClient.getAccmgr().removeUser(user.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.client.ClientManager#updateClient(com.akuacom.pss2.core
	 * .model.Client)
	 */
	public void updateClient(Participant client) {
		participantManager.updateParticipant(client);
		EventStateCacheHelper.getInstance().delete(client.getParticipantName());
	}
	
	
	
	public void updateParticipantCommunications(String participantName,
			Date timestamp, boolean success, String eventState, String operationModeValue){
		try {
		Participant participant = clientEAO.findParticipantOnlyByName(participantName, true);
		Date lastContact=participant.getCommTime();
		
		processLastContact(participant, timestamp);
		participant.setCommTime(timestamp);
		
		Integer status=participant.getStatus();
		if (success) {
			participant.setStatus(ClientStatus.ONLINE.ordinal());
		} else {
			participant.setStatus(ClientStatus.ERROR.ordinal());
		}
		participant.setEventStatus(EventStatus.valueOf(eventState));
		participant.setOperationMode(OperationModeValue.valueOf(operationModeValue));
		participantManager.updateParticipant(participant);
		
		//log client online status to client status history table
		if (success && status != ClientStatus.ONLINE.ordinal())
			reportClientStatus(participant.getUUID(), true, timestamp, lastContact, participant.getParticipantName());
		
	} catch (Exception ex) {
		String message = "error updating comms for participant "
				+ participantName;
		log.warn(message, ex);
	}
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.akuacom.pss2.participant.ParticipantManager#
	 * updateParticipantCommunications(java.lang.String, java.util.Date,
	 * boolean)
	 */
    @Override
    @Interceptors({ExceptionNotifierInterceptor.class})
	public void updateParticipantCommunications(String participantName,
			Date timestamp, boolean success, ClientConversationState convState) {
		try {
			Participant participant = participantEAO.findParticipantOnlyByName(participantName, true);
			Date lastContact=participant.getCommTime();
			
			processLastContact(participant, timestamp);
			participant.setCommTime(timestamp);
			
			Integer status=participant.getStatus();
			if (success) {
				participant.setStatus(ClientStatus.ONLINE.ordinal());
			} else {
				participant.setStatus(ClientStatus.ERROR.ordinal());
			}
			if(convState != null) {
				participant.setEventStatus(convState.getEventStatus());
				participant.setOperationMode(convState.getOperationModeValue());
            }
			
			participantManager.updateParticipant(participant);
			
			//log client online status to client status history table
			if (success && status != ClientStatus.ONLINE.ordinal())
				reportClientStatus(participant.getUUID(), true, timestamp, lastContact, participant.getParticipantName());
			
		} catch (Exception ex) {
			String message = "error updating comms for participant "
					+ participantName;
			log.warn(message, ex);
		}
	}

	/**
	 * Process last contact.
	 * 
	 * @param participant
	 *            the participant
	 * @param d
	 *            the d
	 */
	private void processLastContact(Participant participant, Date d) {
		try {
			Date lastContact = participant.getCommTime();
			long timeSinceLastContactM;
			if (lastContact != null) {
				timeSinceLastContactM = (d.getTime() - lastContact.getTime())
						/ TimingUtil.MINUTE_MS;
			} else {
				timeSinceLastContactM = 0;
			}
			if (participant.isOfflineWarning()) {
				participant.setOfflineWarning(false);

				PSS2Properties props = systemManager.getPss2Properties();
				String subject = "DRAS client "
						+ participant.getParticipantName() + " back online";
	        	String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
				String content = "Your " + utilityDisplayName
						+ " DRAS client " + participant.getParticipantName()
						+ " came back online at " + d + ". It was "
						+ "offline for " + timeSinceLastContactM + " minutes.";


				if (participant.getType() != Participant.TYPE_MANUAL) {
					ProgramEJBBean.sendDRASOperatorCommNotification(subject,
							content, NotificationMethod.getInstance(), null, "",
							participant, -1, props,notifier, ProgramEJBBean.getErrorMap());
				}

				for (ParticipantContact pContact : participant
						.getContacts()) {
					if (pContact == null) {
						continue;
					}

					// we are doing this because we need to rese the falg before
					// sending the mail message in case that throws an
					// exception.
					// we could simplify this after we put it the catch on the
					// mail
					// send
					boolean participantOfflineErrorFlag = pContact.isOfflineError();
					pContact.setOfflineError(false);


					if (participantOfflineErrorFlag
							&& pContact.isCommNotification() && participant.getType() != Participant.TYPE_MANUAL) {
						// TODO: we should catch exceptions for this
						notifier.sendNotification(pContact, participant
								.getParticipantName(), subject, content,
								NotificationMethod.getInstance(), null,
								Environment.isAkuacomEmailOnly(), false, true,
								"");
					}
				}
			
				participantManager.updateParticipant(participant);
			}
		} catch (EJBException e) {
			throw e;
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}
	
	
	
	private void addClientToParentsPrograms(List<Participant> clients, Program program) {
		Map<String, Program> programMap = new HashMap<String,Program>();
		Map<String, ProgramEJB> programEjbMap = new HashMap<String,ProgramEJB>();
		Map<String, List<ProgramParticipantRule>> defaulRulesMap = new HashMap<String,List<ProgramParticipantRule>>();
		ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
		ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
		PSS2Features features = systemManager.getPss2Features();

		
		programParticipantManager.addParticipantToProgram(program, clients, true, systemManager.lookupProgramBean(program.getProgramName()));
		List<ProgramParticipant> ppList = new ArrayList<ProgramParticipant>();
		
		for(Participant client : clients){
			String clientName = client.getParticipantName();
			String parentName = client.getParent();
			List<String> progs = participantManager.getProgramsForParticipant(parentName, false);
			for (String prog : progs) {
				Program p = programMap.get(prog);
				if(p == null){
					p = programManager.getProgramWithRules(prog);
					programMap.put(p.getProgramName(), p);
				}
				ProgramEJB programEJB = programEjbMap.get(prog);
				if(programEJB == null){
					programEJB = systemManager.lookupProgramBean(prog);
					programEjbMap.put(p.getProgramName(), programEJB);
				}
				
				
				//programParticipantManager.addParticipantToProgram(p, client, true, programEJB);
				
				// update client as program participant
				ProgramParticipant pp = programParticipantManager.getClientProgramParticipants(prog, clientName, true);

				// create default rules if any
				Set<ProgramParticipantRule> currRules = pp.getProgramParticipantRules();
				if (currRules == null) {
					currRules = new HashSet<ProgramParticipantRule>();
				}



				List<ProgramParticipantRule> defaultRules = defaulRulesMap.get(prog);
				if(defaultRules == null){
					defaultRules = programEJB.createDefaultClientRules(p);
					defaulRulesMap.put(prog, defaultRules);
				}
				
				if (defaultRules != null && defaultRules.size() > 0) {
					currRules.addAll(defaultRules);
					pp.setProgramParticipantRules(currRules);
				}

				// Import for to track UI change for client config
				
				if (features.isClientsAutoEnrollInProgramEnabled()) {
					pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
				} else {
					pp.setState(ProgramParticipant.PROGRAM_PART_DELETED);
				}

				// Save
				programParticipantManager.updateProgramParticipant(prog, clientName, true, pp);
				//programParticipantManager.setProgramParticipant(pp, true);
				
				ppList.add(pp);
				
			}
			
		}
		
		
		//programParticipantManager.setProgramParticipants(ppList);
		
		
	}

	private void addClientToParentsPrograms(String clientName, String parentName, Participant client) {
		List<String> progs = participantManager.getProgramsForParticipant(parentName, false);
		for (String prog : progs) {
			addClientToProgram(clientName, prog, client);
		}
	}

	public void addClientToProgram(String clientName, String programName, Participant client) {
		// create client as program participant
		ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
		ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
		programParticipantManager.addParticipantToProgram(programName, clientName, true);
		
		// update client as program participant
		ProgramParticipant pp = programParticipantManager.getClientProgramParticipants(programName, clientName, true);

		// create default rules if any
		Set<ProgramParticipantRule> currRules = pp.getProgramParticipantRules();
		if (currRules == null) {
			currRules = new HashSet<ProgramParticipantRule>();
		}

		ProgramEJB programEJB = systemManager.lookupProgramBean(programName);
		//No need to load all referenced object
		Program prog = programManager.getProgramWithRules(programName);
		
		
		//default shed strategy , currently only for rtp
		if(pp!=null&&(pp.getRtpStrateges()==null||pp.getRtpStrateges().isEmpty())){
			programEJB.generateDefaultStrategy(pp);
		}
		
		//currently return empty rules, why?
		List<ProgramParticipantRule> defaultRules = programEJB.createDefaultClientRules(prog);
		if (defaultRules != null && defaultRules.size() > 0) {
			currRules.addAll(defaultRules);
			pp.setProgramParticipantRules(currRules);
		}

		// Import for to track UI change for client config
		PSS2Features features = systemManager.getPss2Features();
		if (features.isClientsAutoEnrollInProgramEnabled()) {
			pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
		} else {
			pp.setState(ProgramParticipant.PROGRAM_PART_DELETED);
		}

		// Save
		programParticipantManager.updateProgramParticipant(programName, clientName, true, pp);
	}

	public void configureClientManualSignals(String clientName) {
		Participant client = clientEAO.getClientByName(clientName);
		if (client.getManualSignals() == null
				|| client.getManualSignals().size() < 1) {
			SignalManager signalManager = EJBFactory
					.getBean(SignalManager.class);
			List<ClientManualSignal> sigs = signalManager
					.findSignalsWithDefaults(client);
			Set<ClientManualSignal> sigs2 = new HashSet<ClientManualSignal>();
			sigs2.addAll(sigs);
			client.setManualSignals(sigs2);
			participantManager.updateParticipant(client);
		}
	}

    /**
     * Creates a bunch of clients
     * @param clients list of clients
     * @param passwords list of passwords - indexes should match
     */
	public void createClient(List<Participant> clients, List<char[]> passwords, Program program) {
		
		for(int i = 0; i < clients.size(); i++){
			Participant client = clients.get(i);
			char[] password = passwords.get(i);
	        clientEAO.createClient(client);

	        AccMgrWSClient accmgrClient = new AccMgrWSClient();
	        accmgrClient.initialize();
	        User user = new User();
	        user.setId(client.getParticipantName());
	        user.setDomainname("CLIENT");
	        user.setUsername(client.getParticipantName());
	        user.setPassword(new String(password));
	        Role role = new Role();
	        role.setRolename("PSS2WS");
	        user.getRoles().add(role);
	        user.setEmail(client.getParticipantName());
	        user.setStatus("ACTIVE");
	        accmgrClient.getAccmgr().createUser("CLIENT", user);
			configureClientManualSignals(client.getParticipantName());
			reportClientStatus(client.getUUID(), false, null, new Date(), client.getParticipantName());
		}

		addClientToParentsPrograms(clients, program);
		
		
	}
	
	
	
	
	
	/**
	 * convenience method to do all the work in one transaction
	 */
	public void createClient(Participant client, char[] password) {
		
		String venId= client.getAccountNumber();
        clientEAO.createClient(client);
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        User user = new User();
        user.setId(client.getParticipantName());
        user.setDomainname("CLIENT");
        user.setUsername(client.getParticipantName());
        user.setPassword(new String(password));
        Role role = new Role();
        role.setRolename("PSS2WS");
        user.getRoles().add(role);
        user.setEmail(client.getParticipantName());
        user.setStatus("ACTIVE");
        accmgrClient.getAccmgr().createUser("CLIENT", user);

		addClientToParentsPrograms(client.getParticipantName(), client.getParent(), client);
		configureClientManualSignals(client.getParticipantName());
		
		reportClientStatus(client.getUUID(), false, null, new Date(), client.getParticipantName());
		if(venId != null && !venId.isEmpty() && !venId.equalsIgnoreCase("Select")) {
			clinetMappingToEndPoint(client.getParticipantName(), venId);
		}
	}
	
	private void clinetMappingToEndPoint(String particitpantName, String venId) {
		try {
			Endpoint ep = endpointManager.findByVenId(venId);
			if(ep != null) {
				EndpointMapping epm = new EndpointMapping();
				 epm.setEndpoint(ep);
				 Participant participant = participantManager.getParticipant(particitpantName, true);
				 epm.setParticipant(participant);
				 epm.setVenId(ep.getVenId());
				 epm.setModifiedTime(new Date());
				 Calendar c = Calendar.getInstance();
				 c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)+15);
				 epm.setLinkexpiry(c.getTime());
				 epm.setLinkactivateddate(new Date());
				 epm.setCreationTime(new Date());
				 epm.setLinkactivateddate(new Date());
				 epm.setParticipantName(participant.getParticipantName());
				 epm.setLinkactive(true);
				 endpointManager.createEndpointParticipantLink(epm);
			}
		} catch (EntityNotFoundException e) {
			log.error("Error mapping clinet to VEN", e);
		}
	}

	private void reportClientStatus(String client_uuid, boolean online, Date time, Date lastContact, String clientName){
		if(online)
			asynchCaller.call(new EJBAsynchRunable(CustomerReportManager.class, 
				"reportOnlineStatus",
				 new Class[]{String.class,Date.class,Date.class,String.class},
				 new Object[]{client_uuid, time, lastContact, clientName}));
		else
			asynchCaller.call(new EJBAsynchRunable(CustomerReportManager.class, 
					"reportOfflineStatus",
					 new Class[]{String.class,Date.class,String.class},
					 new Object[]{client_uuid, lastContact, clientName}));
		/*
		if (online)
			customerReportManager.reportOnlineStatus(client_uuid, time, lastContact, clientName);
		else
			customerReportManager.reportOfflineStatus(client_uuid, lastContact, clientName);*/
	}
	
	@Override
	public List<org.openadr.dras.eventstate.EventState> getClientDrasEventStates(String participantName){

        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        List<org.openadr.dras.eventstate.EventState> es = cache.get(participantName);
		if(es == null){

            ConfirmationLevel level = cache.getConfirmationLevel();
            boolean update = level == ConfirmationLevel.NONE;

            Map<String,Date> nearTimeMap = new HashMap<String,Date>();
			List<EventState> pss2EventStates = getClientEventStates(participantName, update, nearTimeMap);
			es = new ArrayList<org.openadr.dras.eventstate.EventState>();
			for (com.akuacom.pss2.util.EventState pss2EventState : pss2EventStates) {
				org.openadr.dras.eventstate.EventState ess = parseEventState(participantName, pss2EventState);
                es.add(ess);
            }

            cache.set(participantName, es, nearTimeMap);
		}
		return es;
		
	}
	
	@Override
	public List<org.openadr.dras.eventstate.EventState> getAllDrasEventStateForClient(String participantName){

        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        List<org.openadr.dras.eventstate.EventState> es = cache.getEventState(participantName);
		if(es == null){
            ConfirmationLevel level = cache.getConfirmationLevel();
            boolean update = level == ConfirmationLevel.NONE;

            Map<String,Date> nearTimeMap = new HashMap<String,Date>();
			List<EventState> pss2EventStates = getAllEventStatesForClient(participantName, update, nearTimeMap);
			es = new ArrayList<org.openadr.dras.eventstate.EventState>();
			for (com.akuacom.pss2.util.EventState pss2EventState : pss2EventStates) {
				org.openadr.dras.eventstate.EventState ess = parseEventState(participantName, pss2EventState);
                es.add(ess);
            }

            cache.set(participantName, es, nearTimeMap);
		}
		return es;
		
	}
	
	@Override
	public List<org.openadr.dras.eventstate.EventState> getAllDrasEventStateForParentParticipant(String participantName){

        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        List<org.openadr.dras.eventstate.EventState> es = cache.getEventState(participantName);
		if(es == null){
            ConfirmationLevel level = cache.getConfirmationLevel();
            boolean update = level == ConfirmationLevel.NONE;

            Map<String,Date> nearTimeMap = new HashMap<String,Date>();
			List<EventState> pss2EventStates = getAllEventStatesForParentParticipant(participantName, update, nearTimeMap);
			es = new ArrayList<org.openadr.dras.eventstate.EventState>();
			for (com.akuacom.pss2.util.EventState pss2EventState : pss2EventStates) {
				org.openadr.dras.eventstate.EventState ess = parseEventState(participantName, pss2EventState);
                es.add(ess);
            }

            cache.set(participantName, es, nearTimeMap);
		}
		return es;
		
	}


    @Override
	public List<EventState> getClientEventStates(String name, boolean update) {
    	return getClientEventStates(name, update, null);
    }
 
	
	
	
	
    @Override
	public List<EventState> getClientEventStates(String name, boolean update, Map<String,Date> nearTimeMap) {
        Participant client = clientEAO.getClient(name);
                List<EventState> eventStates = getClientEventStates(client, nearTimeMap);
        if(update && eventStates.get(0) != null)
        {
        	client.setEventStatus(eventStates.get(0).getEventStatus());
        	client.setOperationMode(eventStates.get(0).getOperationModeValue());
        	participantManager.updateParticipant(client);
        }
        return eventStates;
	}
    
    @Override
   	public List<EventState> getAllEventStatesForClient(String name, boolean update, Map<String,Date> nearTimeMap) {
           Participant client = clientEAO.getClient(name);       
           List<EventState> eventStates = getEventStatesByClinet(client, nearTimeMap);
           if(update && eventStates.get(0) != null)
           {
           	client.setEventStatus(eventStates.get(0).getEventStatus());
           	client.setOperationMode(eventStates.get(0).getOperationModeValue());
           	participantManager.updateParticipant(client);
           }
           return eventStates;
   	}
    
    @Override
    public List<EventState> getAllEventStatesForParentParticipant(String name, boolean update, Map<String,Date> nearTimeMap) {
        Participant participant = participantEAO.getParticipant(name);
        List<EventState> eventStates = getEventStatesByParticipant(participant, nearTimeMap);
        if(update && eventStates.get(0) != null)
        {
        	participant.setEventStatus(eventStates.get(0).getEventStatus());
        	participant.setOperationMode(eventStates.get(0).getOperationModeValue());
        	participantManager.updateParticipant(participant);
        }
        return eventStates;
	}

    @Override
	public List<EventState> getClientEventStates(Participant client) {
    	return getClientEventStates(client, null);
    }
    
    public List<EventState> getEventStatesByClinet(Participant client, Map<String,Date> nearTimeMap) {
    	 HashMap<String, Integer> programPriorities = programManager.getProgramPriority();

	        long nowMS = System.currentTimeMillis();
			List<EventState> eventStates = new ArrayList<EventState>();
			List<EventParticipant> findEventParticipantWithSignalsByClientUUID = new ArrayList<EventParticipant>();
			HashMap<String,EventParticipant> mapOfEventUUIdAndEventParticipant = new HashMap<String, EventParticipant>();
			findEventParticipantWithSignalsByClientUUID = 
					eventParticipantEAO.findEventParticipantWithSignalsByClientUUID(client.getUUID());
	        
			for (EventParticipant eventPart :findEventParticipantWithSignalsByClientUUID) {
	            Event event = eventPart.getEvent();
	            if (event != null) {
	                    EventState eventState = new EventState(); 
	                    eventState.setDrasClientID(client.getParticipantName());
	                    eventState.setEventIdentifier(event.getEventName());
	                    
	    	        	String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
	    	        	
	                    if((event.getProgramName().contains("DBP")) && ((utilityDisplayName).equalsIgnoreCase("PGE"))){
	                    	if(eventPart.getSignals().size()>0){
	                    		eventState.setEventModNumber(1);	
	                    	}else {
	                    		eventState.setEventModNumber(0);	
	                    	}
	            		}else{// Calculate the Mid night modification for an event
	            		Date nowDate = new Date();
	            		Date startDate = event.getStartTime();
	            		
	            		Calendar cal = Calendar.getInstance();
	            		cal.setTime(startDate);
	            		// Set time fields to zero  
	            		cal.set(Calendar.HOUR_OF_DAY, 0);  
	            		cal.set(Calendar.MINUTE, 0);  
	            		cal.set(Calendar.SECOND, 0);  
	            		cal.set(Calendar.MILLISECOND, 0);
	            		startDate = cal.getTime();
	            		
	            		cal.setTime(nowDate);
	            		// Set time fields to zero  
	            		cal.set(Calendar.HOUR_OF_DAY, 0);  
	            		cal.set(Calendar.MINUTE, 0);  
	            		cal.set(Calendar.SECOND, 0);  
	            		cal.set(Calendar.MILLISECOND, 0);
	            		nowDate = cal.getTime();
	            		long eventDate = startDate.getTime();
	            		long sysDate = nowDate.getTime();
	            		int diffInDays = (int)( (sysDate-eventDate)/ (1000 * 60 * 60 * 24) );
	            		
	            		eventState.setEventModNumber((diffInDays < 0) ? 0 : diffInDays);
	            		}
	                   // eventState.setEventModNumber(eventPart.getEventModNumber());

	            		eventState.setEventStateID(MemorySequence
	                            .getNextSequenceId());
	                    eventState.setProgramName(event.getProgramName());
	                    
	                    if (event.getLocationInfo()!=null && !event.getLocationInfo().isEmpty()) {
	                    	List<String> locations=Arrays.asList(event.getLocationInfo().split(","));
	                    	eventState.setLocations(locations);
	                    }

	                    long startTimeMS = event.getStartTime().getTime();
	                    
	                    if(nearTimeMap != null){
	                    	EventParticipantSignalEntry es = eventPart.getSignalValueForEventParticipantByName("pending");
	                    	if(es != null){
	                    		Date date = es.getTime();
	                    		if(date != null){
	                    			String eventName = event.getEventName();
	                    			nearTimeMap.put(eventName, date);
	                    			eventState.setNearTime(date);
	                    		}
	                    	}
	                    }
	                    

	                    if (client.isManualControl()) {
	                        String pendingValue = getClientManualSignalValueAsString(client, "pending");
	                        if (pendingValue == null) {
	                            // hardcoded default
	                            pendingValue = "far";
	                        }
	                        if (pendingValue.equals("active")) {
	                            eventState.setEventStatus(EventStatus.ACTIVE);
	                        } else if (pendingValue.equals("near")) {
	                            eventState.setEventStatus(EventStatus.NEAR);
	                        } else if (pendingValue.equals("far")) {
	                            eventState.setEventStatus(EventStatus.FAR);
	                        } else {
	                            eventState.setEventStatus(EventStatus.NONE);
	                        }
	                    } else {
	                        String pendingValue = eventPart
	                                .getSignalValueForEventParticipantAsString("pending");
	                        if (pendingValue == null) {
	                            pendingValue = "off";
	                        }
	                        if (startTimeMS < nowMS) {
	                            eventState
	                                    .setEventStatus(EventStatus.ACTIVE);
	                        } else if (pendingValue.equals("on")) {
	                            eventState
	                                    .setEventStatus(EventStatus.NEAR);
	                        } else {
	                            eventState
	                                    .setEventStatus(EventStatus.FAR);
	                        }
	                    }

	                    String modeSignalValue;
	                    if (client.isManualControl()) {
	                        modeSignalValue = getClientManualSignalValueAsString(client, "mode");
	                        if (modeSignalValue == null) {
	                            // hardcoded default.
	                            modeSignalValue = "normal";
	                        }
	                    } else {
	                        modeSignalValue = eventPart
	                                .getSignalValueForEventParticipantAsString("mode");
	                        if (modeSignalValue == null) {
	                            modeSignalValue = "normal";
	                        }
	                    }
	                    eventState.setOperationModeValue(modeSignalValue);
	                    eventState.setManualControl(client.isManualControl());
	                    eventState
	                            .setCurrentTimeS((nowMS - startTimeMS) / 1000.0);

	                    List<ModeSlot> operationModeSchedule = new ArrayList<ModeSlot>();

	                    List<EventParticipantSignalEntry> signalEntries = eventPart
	                            .getSignalEntries();
	                    for (EventParticipantSignalEntry entry : signalEntries) {
	                        if (!entry.getParentSignal().getSignalDef()
	                                .getSignalName().equals("mode")) {
	                            continue;
	                        }

	                        ModeSlot modeSlot = new ModeSlot();
	                        modeSlot
	                                .setOperationModeValue(((EventParticipantSignalEntry) entry)
	                                        .getLevelValue());
	                        modeSlot
	                                .setTimeSlotS((entry.getTime().getTime() - startTimeMS) / 1000.0);
	                        operationModeSchedule.add(modeSlot);
	                    }
	                    eventState.setOperationModeSchedule(operationModeSchedule);
	                    eventState.setNotificationTime(event.getIssuedTime());
	                    eventState.setStartTime(event.getStartTime());
	                    eventState.setEndTime(event.getEndTime());

						// Get the client's parent's event participant signals
				 		Set<Signal> parentEPSignals = new HashSet<Signal>();
				 		EventParticipant parentEventParticipant = 
				 			eventParticipantEAO.findParentEventParticipant(
				 			event.getEventName(), client.getParent());
						for (Signal signal : parentEventParticipant.getSignals()) {
							parentEPSignals.add(signal);
						}
	                    eventState.setEventInfoInstances(eventPart
	                        .getEventInfoInstances(
	                        eventState.getStartTime().getTime(), parentEPSignals));

	                    eventStates.add(eventState);
	            }
	        }  			

			if (client.isClient()) {
				Collections.sort(eventStates, new EventState.PriorityComparator(
						programPriorities));
			}

    	return eventStates; 	
    }

    
	/**
	 * Returns a list of fully loaded event states based on event information
	 * loaded with this entity.
	 *
	 * It is called "quick" because it is faster than
	 * ClientManager.getClientEventStates because it uses data in memory in this
	 * object graph. Currently, this method should return exactly the same
	 * results as ClientManager. However, ClientManager calls ProgramEJB
	 * newEventState and getClientEventState which may be overridden in the
	 * future. This method does not.
	 *
	 * Also, to avoid manager calls from an entity bean, this method hard codes
	 * signal defaults for pending and mode.
	 *
	 * @return
	 */
    @Override
	public List<EventState> getClientEventStates(Participant client, Map<String,Date> nearTimeMap) {

        HashMap<String, Integer> programPriorities =
            programManager.getProgramPriority();

        long nowMS = System.currentTimeMillis();
		List<EventState> eventStates = new ArrayList<EventState>();
		List<EventParticipant> eventPartList = new ArrayList<EventParticipant>();
		eventPartList = eventParticipantEAO.findEventParticipantWithSignalsByClientUUID(client.getUUID());
		Set<EventParticipant> eventParticipantListForParent = new HashSet<EventParticipant>();
		Map<String,EventParticipant> eventIdNListOfEventParticipantMap = new HashMap<String,EventParticipant>();
		if(client.getParent()!=null){
			eventParticipantListForParent = new HashSet<EventParticipant>(eventParticipantEAO.findEventParticipantForParent(client.getParent()));
		}
		for(EventParticipant evPart:eventParticipantListForParent){
				eventIdNListOfEventParticipantMap.put(evPart.getEvent().getEventName(), evPart);
		}
        for (EventParticipant eventPart : eventPartList){
            Event event = eventPart.getEvent();
            // only add to list of the event is issued
            if (event != null) {
                if (event.isIssued()) {
                    EventState eventState = new EventState(); // programEJB.newEventState();
                    eventState.setDrasClientID(client.getParticipantName());
                    eventState.setEventIdentifier(event.getEventName());
                    eventState.setEventModNumber(eventPart
                            .getEventModNumber());
                    eventState.setEventStateID(MemorySequence
                            .getNextSequenceId());
                    eventState.setProgramName(event.getProgramName());
                    
                    if (event.getLocationInfo()!=null && !event.getLocationInfo().isEmpty()) {
                    	List<String> locations=Arrays.asList(event.getLocationInfo().split(","));
                    	eventState.setLocations(locations);
                    }

                    long startTimeMS = event.getStartTime().getTime();
                    
                    if(nearTimeMap != null){
                    	EventParticipantSignalEntry es = eventPart.getSignalValueForEventParticipantByName("pending");
                    	if(es != null){
                    		Date date = es.getTime();
                    		if(date != null){
                    			String eventName = event.getEventName();
                    			nearTimeMap.put(eventName, date);
                    			eventState.setNearTime(date);
                    		}
                    	}
                    }
                    

                    if (client.isManualControl()) {
                        String pendingValue = getClientManualSignalValueAsString(client, "pending");
                        if (pendingValue == null) {
                            // hardcoded default
                            pendingValue = "far";
                        }
                        if (pendingValue.equals("active")) {
                            eventState.setEventStatus(EventStatus.ACTIVE);
                        } else if (pendingValue.equals("near")) {
                            eventState.setEventStatus(EventStatus.NEAR);
                        } else if (pendingValue.equals("far")) {
                            eventState.setEventStatus(EventStatus.FAR);
                        } else {
                            eventState.setEventStatus(EventStatus.NONE);
                        }
                    } else {
                        String pendingValue = eventPart
                                .getSignalValueForEventParticipantAsString("pending");
                        if (pendingValue == null) {
                            pendingValue = "off";
                        }
                        if (startTimeMS < nowMS) {
                            eventState
                                    .setEventStatus(EventStatus.ACTIVE);
                        } else if (pendingValue.equals("on")) {
                            eventState
                                    .setEventStatus(EventStatus.NEAR);
                        } else {
                            eventState
                                    .setEventStatus(EventStatus.FAR);
                        }
                    }

                    String modeSignalValue;
                    if (client.isManualControl()) {
                        modeSignalValue = getClientManualSignalValueAsString(client, "mode");
                        if (modeSignalValue == null) {
                            // hardcoded default.
                            modeSignalValue = "normal";
                        }
                    } else {
                        modeSignalValue = eventPart
                                .getSignalValueForEventParticipantAsString("mode");
                        if (modeSignalValue == null) {
                            modeSignalValue = "normal";
                        }
                    }
                    eventState.setOperationModeValue(modeSignalValue);
                    eventState.setManualControl(client.isManualControl());
                    eventState
                            .setCurrentTimeS((nowMS - startTimeMS) / 1000.0);

                    List<ModeSlot> operationModeSchedule = new ArrayList<ModeSlot>();
                    
                    List<EventParticipantSignalEntry> signalEntries = new ArrayList<EventParticipantSignalEntry>();

                    signalEntries = eventPart.getSignalEntries();
                    for (EventParticipantSignalEntry entry : signalEntries) {
                        if (!entry.getParentSignal().getSignalDef()
                                .getSignalName().equals("mode")) {
                            continue;
                        }

                        ModeSlot modeSlot = new ModeSlot();
                        modeSlot
                                .setOperationModeValue(((EventParticipantSignalEntry) entry)
                                        .getLevelValue());
                        modeSlot
                                .setTimeSlotS((entry.getTime().getTime() - startTimeMS) / 1000.0);
                        operationModeSchedule.add(modeSlot);
                    }
                    eventState.setOperationModeSchedule(operationModeSchedule);
                    eventState.setNotificationTime(event.getIssuedTime());
                    eventState.setStartTime(event.getStartTime());
                    eventState.setEndTime(event.getEndTime());

					// Get the client's parent's event participant signals
			 		Set<Signal> parentEPSignals = new HashSet<Signal>();
			 		EventParticipant parentEventParticipant = null;
			 		if(eventIdNListOfEventParticipantMap.isEmpty()){
			 			parentEventParticipant =eventParticipantEAO.findParentEventParticipant(event.getEventName(), client.getParent());
			 		}else{
			 			parentEventParticipant = eventIdNListOfEventParticipantMap.get(event.getEventName());
			 		}
			 		Set<EventParticipantSignal> signals = new HashSet<EventParticipantSignal>();
			 		if(parentEventParticipant!=null){
				 		signals = parentEventParticipant.getSignals();
						for (Signal signal : signals) {
							parentEPSignals.add(signal);
						}
			 		}
                    eventState.setEventInfoInstances(eventPart
                        .getEventInfoInstances(
                        eventState.getStartTime().getTime(), parentEPSignals));

                    eventStates.add(eventState);
                }
            }
        }

        String utilityDisplayName = eventCache.getUtilityName("utilityDisplayName");
		if ((eventStates.size() == 0 && nearTimeMap == null) || (eventStates.size() == 0 && (utilityDisplayName).equalsIgnoreCase("PGE"))) {
			// create event state based on manual control or a empty/no event
			// NORMAL one
			EventState eventState = new EventState();
			eventState.setDrasClientID(client.getParticipantName());
			eventState.setEventIdentifier("");
			eventState.setEventModNumber(0);
			eventState.setEventStateID(MemorySequence.getNextSequenceId());
			eventState.setProgramName("");

			String pendingSignalValue = "";
			String modeSignalValue = "";
			if (client.isManualControl()) {
				pendingSignalValue = getClientManualSignalValueAsString(client, "pending");
				modeSignalValue = getClientManualSignalValueAsString(client, "mode");
			} else {
				pendingSignalValue = "none";
				modeSignalValue = "normal";
			}

            if (pendingSignalValue.equals("active")) {
                eventState.setEventStatus(EventStatus.ACTIVE);
            } else if (pendingSignalValue.equals("near")) {
                eventState.setEventStatus(EventStatus.NEAR);
            } else if (pendingSignalValue.equals("far")) {
                eventState.setEventStatus(EventStatus.FAR);
            } else {
                eventState.setEventStatus(EventStatus.NONE);
            }

			eventState.setOperationModeValue(modeSignalValue.toUpperCase());
			eventState.setManualControl(client.isManualControl());
			eventStates.add(eventState);
		}

		if (client.isClient()) {
			Collections.sort(eventStates, new EventState.PriorityComparator(
					programPriorities));
		}

		return eventStates;
	}

	/**
	 * Returns the OperationModeValue from this object's getQuickEventStates.
	 *
	 * See the docs for getQuickEventStates for warnings about using this method
	 */
	public OperationModeValue getOperationModeValue(Participant client) {
		return getClientEventStates(client).get(0).getOperationModeValue();
	}

    /**
	 * Utility method returns the string value for the signal or null if signal
	 * is not found
	 *
	 * @param signalName
	 * @return
	 */
	public String getClientManualSignalValueAsString(Participant client, String signalName) {
		// mode and pending would always be available in client manual signals
		// If not, manual control will not work.

		String res = null;
		if (client.getManualSignals() != null) {
			for (ClientManualSignal signalState : client.getManualSignals()) {
				if (signalState != null) {
					if (signalState.getName().equals(signalName)) {
						res = signalState.getValue();
						break;
					}
				}
			}
		}

		return res;
	}

    @Override
	public List<String> getClientEventNames(String clientName) {
		final List<String> results = new ArrayList<String>();
		final List<EventParticipant> eps = clientEAO
				.findEventParticipants(clientName);
		for (EventParticipant eventParticipantEAO : eps) {
			results.add(eventParticipantEAO.getEvent().getEventName());
		}
		return results;
	}

	public ClientStatus getClientStatus(String name) {
		try {
			return clientEAO.getClientStatus(name);
		} catch (Exception ex) {
			String message = "error getting participant " + name;
			log.warn(message, ex);
			return ClientStatus.OFFLINE;
		}
	}

	public List<Participant> findClientsByAccounts(List<String> accountIDs) {
		try {
			List<Participant> ret;
			ret = clientEAO.findParticipantsByAccounts(accountIDs);
			return ret;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void initialize() {
		Date now = new Date();
		for (Participant client : clientEAO.getAllClients()) {
			if (client.isManualControl()
					&& client.getManualControlExpires() != null) {
				if (client.getManualControlExpires().before(now)) {
					client.setManualControl(false);
					updateClient(client);
				} 
			}
		}
	}

	public void updateExpireManualControlForClientTimer(String clientName,
			Date expireTime, boolean addTimer) throws IllegalArgumentException {

		if (clientName == null) {
			throw new IllegalArgumentException(
					"clientName parameter is not valid");
		}
		if (addTimer && expireTime == null) {
			throw new IllegalArgumentException(
					"expireTime parameter is not valid");
		}

		String key = CLIENT_MANUAL_CTRL_EXPIRE_TIMER + "-" + clientName;
		javax.ejb.TimerService timerService = context.getTimerService();

		log
				.info("ClientManager.updateExpireManualControlForClientTimer: processing for "
						+ key);

		// disable any active times
		Collection<Timer> timers = timerService.getTimers();
		for (Timer timer : timers) {
			if (key.equals(timer.getInfo())) {
				log
						.info("ClientManager.updateExpireManualControlForClientTimer: canceled timer for "
								+ key);
				timer.cancel();
			}
		}

		if (addTimer) {
			log
					.info("ClientManager.updateExpireManualControlForClientTimer: initialized timer for "
							+ key);
			timerService.createTimer(expireTime, key);
		}
	}

	@Timeout
	public void timeoutHandler(Timer timer) {
		log.info("ClientManager.timeoutHandler for " + timer.getInfo());
		String info = (String) timer.getInfo();
		String clientName = info.substring(CLIENT_MANUAL_CTRL_EXPIRE_TIMER
				.length() + 1);
		Participant client = getClient(clientName);
		if (client != null) {
			client.setManualControl(false);
			updateClient(client);
		}
	}
	
	@Override
	public String getTimersInfo() {
		Collection timersList = context.getTimerService().getTimers();
		return super.getTimersInfo(timersList);
	}

	@Override
	public long countParticipants() {
		return clientEAO.countParticipants();
	}
	
    /**
     * Takes a participant name and an internal EventState object
     * and creates an outbound EventState (which is TOTALLY a different thing)
     * suitable for marshaling to XML and sending to a client  
     * 
     * @param participantName The name of a participant
     * @param eventState  The kind of EventState that doesn't turn into XML
     * @return The kind of EventState that does turn into XML.
     */
    @Override   
    public org.openadr.dras.eventstate.EventState parseEventState(
            String participantName, com.akuacom.pss2.util.EventState eventState)
    {
        if (drasVersion == null) {
            com.akuacom.utils.BuildProperties buildProperties = new com.akuacom.utils.BuildProperties();
            final PSS2Properties params = systemManager.getPss2Properties();
            if(!org.apache.commons.lang.StringUtils.isBlank(params.getVersion())){
                drasVersion = params.getVersion();
            } else {
                drasVersion = buildProperties.getVersion();
            }
        }
        
        String DrasName = "Akuacom "+drasVersion;
        return ClientUtil.parseEventState(participantName, DrasName,  eventState);
    }

	@Override
	public void pushClientEventState(Participant theClient) {

        List <ClientConversationState> existingConversations =
                clientConversationtStateEAO.findByDrasClientId(theClient.getParticipantName());
         for (ClientConversationState es : existingConversations) {
             // Because of concurrent access from client confirmations,
             // it is common to try to delete an EventState, only to find
             // that someone else already deleted it.
             // So we allow for this and don't let it hose the larger transaction
             try { eventManager.removeClientConversationState(es.getConversationStateId()); }
             catch (EJBException ejbx) {
                 log.warn("Conversation state delete failed for client:"
                         + theClient.getParticipantName() + " " + ejbx.getMessage());
             } catch (Exception ex) {
                 log.error("Conversation state delete failed for client:"
                         + theClient.getParticipantName(), ex);
             }
        }

        if (theClient.getPush() != 0 ) {
            // It's possible the client became non-push,
            // in which case, stop retrying
            List<EventState> eventStates = getClientEventStates(theClient);

            if (eventStates != null) {
                ListOfEventStates listOfEventStates =
                        new org.openadr.dras.eventstate.ListOfEventStates();
                for (EventState eventState : eventStates) {
                    // check to see if the event state is stale (from expired event)
                    String eventID = eventState.getEventIdentifier();
                    Event validEvent;
                    boolean validState = false;
                    if (eventID == null || eventID.trim().isEmpty()) {
                        // Idle, nothing going on
                        validState = true;
                    } else {
                        // Says there's an event.  See if it's a valid event.
                        validEvent = eventManager.getEventOnly(eventID);
                        if (validEvent != null) { validState = true; }
                    }
                    if (validState) {
                        eventManager.createClientConversationState(eventState, true);
                        listOfEventStates.getEventStates().add(
                                parseEventState(theClient.getParticipantName(), eventState));
                    }
                }
                String eventStatesXML = ClientUtil.eventStatesToXML(listOfEventStates);
                //System.out.println(eventStatesXML);
                try {
                    httpPusher.pushEventState(theClient, eventStatesXML);
                } catch (Exception ex) {
                    // The post failed
                    log.error("Event state push failed for client:"
                            + theClient.getParticipantName(), ex);
                }
            }
        }
    }
	
	@Override
	public void createTimers() {
		createTimers(clientEAO.getAllClients());
	}
	
	public void createTimers(List<Participant> clients) {
		for (Participant client : clients) {
			if (client.isManualControl()
					&& client.getManualControlExpires() != null) {
			
					updateExpireManualControlForClientTimer(client
							.getParticipantName(), client
							.getManualControlExpires(), true);
				
			}
		}
	}

	@Override
	public void cancelTimers() {
		 javax.ejb.TimerService timerService = context.getTimerService();
	        Collection timersList = timerService.getTimers();
	        super.cancelTimers(timersList);
	}


	@Override
	public ConfirmationResult isConfirmationOk(String participantName, long eventStateID){
		ConfirmationResult result = null;
		EventStateCacheHelper eh = EventStateCacheHelper.getInstance();
		ConfirmationLevel confirmationLevel = eh.getConfirmationLevel();
		if(confirmationLevel == null){
			CoreProperty cp = null;
			try {
				cp = systemManager.getProperty(PropertyName.CLIENT_CONFIMATION_LEVEL);
			} catch (EntityNotFoundException e) {
                // ignored
			}
            if (cp != null) {
                confirmationLevel = ConfirmationLevel.valueOf(cp.getStringValue());
            } else {
                confirmationLevel = ConfirmationLevel.NONE;
            }
			eh.setConfirmationLevel(confirmationLevel);
		}
		 
		if(confirmationLevel != ConfirmationLevel.NONE){
			result = eh.isConfirmationOk(participantName, eventStateID);
		}
		
		// hack for clients returning bad event state id
        if (confirmationLevel != ConfirmationLevel.FULL) {
            if (result != null) {   // DRMS-7273: avoid NPE
                result.setConfirmation(true);
            }
        }

		
		return result;
	}

	public List<Participant> getClientsAllInfoByParent(String parentName) {
		return clientEAO.getClientsAllInfoByParent(parentName);
	}
	public List<Participant> getClientsAllInfoExceptContactsByParent(String parentName){
		return clientEAO.getClientsAllInfoExceptContactsByParent(parentName);
	}
	@Override
	public Participant getClientOnly(String name) {
		return clientEAO.findParticipantOnlyByName(name, true);
	}

	@Override
	public Participant getClientWithContacts(String clientName) {
		return clientEAO.findParticipantWithContacts(clientName,true);
		
	}

	@Override
	public Participant getClientWithEvents(String clientName) {
		return clientEAO.findParticipantWithEvents(clientName,true);
	}
	
	@Override
	public Participant getClientWithManualSignals(String clientName) {
		return clientEAO.findParticipantWithManualSignals(clientName,true);
	}

	@Override
	public List<String> getClientNamesByParticipant(String partName) {
		 return clientEAO.getClientNamesByParticipant(partName);
	}

    @Override
    public void createClientConversationState(EventState eventState) {
        ClientConversationState c = buildClientConversationState(eventState);
        putClientConversationState(c);
    }

    private ClientConversationState buildClientConversationState(EventState eventState) {
        ClientConversationState c = new ClientConversationState();
        c.setDrasClientId(eventState.getDrasClientID());
        c.setEventIdentifier(eventState.getEventIdentifier());
        c.setEventModNumber(eventState.getEventModNumber());
        c.setConversationStateId((int) eventState.getEventStateID());
        c.setProgramName(eventState.getProgramName());
        c.setCommTime(new Date());
        c.setEventStatus(eventState.getEventStatus());
        c.setOperationModeValue(eventState.getOperationModeValue());
        return c;
    }

    @Override
    public void putClientConversationState(ClientConversationState state) {
        cache.putClientConversationState(state.getConversationStateId(), state);
    }

    @Override
    public ClientConversationState getClientConversationState(long eventStateId) {
        return cache.getClientConversationState((int) eventStateId);
    }

    @Override
    public void removeClientConversationState(long eventStateId) {
        cache.removeClientConversationState((int)eventStateId);
    }
    private void removeClientEndpointMapping(String clientName){
		EndpointManager endpointManager = EJBFactory.getBean(EndpointManager.class);	
		EndpointMapping endpointMappint = endpointManager.findByParticipantName(clientName);
		if(null == endpointMappint){
			return;
		}
		endpointManager.deleteEndpointParticipantLink(endpointMappint);
	}
    
    
    public List<EventState> getEventStatesByParticipant(Participant client, Map<String,Date> nearTimeMap) {
    		HashMap<String, Integer> programPriorities = programManager.getProgramPriority();

	        long nowMS = System.currentTimeMillis();
			List<EventState> eventStates = new ArrayList<EventState>();
			List<EventParticipant> findEventParticipantWithSignalsByClientUUID = 
					eventParticipantEAO.findEventParticipantWithSignalsByClientUUID(client.getUUID());
	        
			for (EventParticipant eventPart :findEventParticipantWithSignalsByClientUUID) {
	            Event event = eventPart.getEvent();
	            if (event != null) {
	                    EventState eventState = new EventState(); 
	                    eventState.setDrasClientID(client.getParticipantName());
	                    eventState.setEventIdentifier(event.getEventName());
	                    eventState.setEventModNumber(eventPart
	                            .getEventModNumber());
	                    eventState.setEventStateID(MemorySequence
	                            .getNextSequenceId());
	                    eventState.setProgramName(event.getProgramName());
	                    
	                    if (event.getLocationInfo()!=null && !event.getLocationInfo().isEmpty()) {
	                    	List<String> locations=Arrays.asList(event.getLocationInfo().split(","));
	                    	eventState.setLocations(locations);
	                    }

	                    long startTimeMS = event.getStartTime().getTime();
	                    
	                    if(nearTimeMap != null){
	                    	EventParticipantSignalEntry es = eventPart.getSignalValueForEventParticipantByName("pending");
	                    	if(es != null){
	                    		Date date = es.getTime();
	                    		if(date != null){
	                    			String eventName = event.getEventName();
	                    			nearTimeMap.put(eventName, date);
	                    		}
	                    	}
	                    }
	                    

	                    if (client.isManualControl()) {
	                        String pendingValue = getClientManualSignalValueAsString(client, "pending");
	                        if (pendingValue == null) {
	                            // hardcoded default
	                            pendingValue = "far";
	                        }
	                        if (pendingValue.equals("active")) {
	                            eventState.setEventStatus(EventStatus.ACTIVE);
	                        } else if (pendingValue.equals("near")) {
	                            eventState.setEventStatus(EventStatus.NEAR);
	                        } else if (pendingValue.equals("far")) {
	                            eventState.setEventStatus(EventStatus.FAR);
	                        } else {
	                            eventState.setEventStatus(EventStatus.NONE);
	                        }
	                    } else {
	                        String pendingValue = eventPart
	                                .getSignalValueForEventParticipantAsString("pending");
	                        if (pendingValue == null) {
	                            pendingValue = "off";
	                        }
	                        if (startTimeMS < nowMS) {
	                            eventState
	                                    .setEventStatus(EventStatus.ACTIVE);
	                        } else if (pendingValue.equals("on")) {
	                            eventState
	                                    .setEventStatus(EventStatus.NEAR);
	                        } else {
	                            eventState
	                                    .setEventStatus(EventStatus.FAR);
	                        }
	                    }

	                    String modeSignalValue;
	                    if (client.isManualControl()) {
	                        modeSignalValue = getClientManualSignalValueAsString(client, "mode");
	                        if (modeSignalValue == null) {
	                            // hardcoded default.
	                            modeSignalValue = "normal";
	                        }
	                    } else {
	                        modeSignalValue = eventPart
	                                .getSignalValueForEventParticipantAsString("mode");
	                        if (modeSignalValue == null) {
	                            modeSignalValue = "normal";
	                        }
	                    }
	                    eventState.setOperationModeValue(modeSignalValue);
	                    eventState.setManualControl(client.isManualControl());
	                    eventState
	                            .setCurrentTimeS((nowMS - startTimeMS) / 1000.0);

	                    List<ModeSlot> operationModeSchedule = new ArrayList<ModeSlot>();

	                    List<EventParticipantSignalEntry> signalEntries = eventPart
	                            .getSignalEntries();
	                    for (EventParticipantSignalEntry entry : signalEntries) {
	                        if (!entry.getParentSignal().getSignalDef()
	                                .getSignalName().equals("mode")) {
	                            continue;
	                        }

	                        ModeSlot modeSlot = new ModeSlot();
	                        modeSlot
	                                .setOperationModeValue(((EventParticipantSignalEntry) entry)
	                                        .getLevelValue());
	                        modeSlot
	                                .setTimeSlotS((entry.getTime().getTime() - startTimeMS) / 1000.0);
	                        operationModeSchedule.add(modeSlot);
	                    }
	                    eventState.setOperationModeSchedule(operationModeSchedule);
	                    eventState.setNotificationTime(event.getIssuedTime());
	                    eventState.setStartTime(event.getStartTime());
	                    eventState.setEndTime(event.getEndTime());

						// Get the event participant signals
				 		Set<Signal> parentEPSignals = new HashSet<Signal>();	 		
				 		
						for (Signal signal : eventPart.getSignals()) {
							parentEPSignals.add(signal);
						}
	                    eventState.setEventInfoInstances(eventPart
	                        .getEventInfoInstances(
	                        eventState.getStartTime().getTime(), parentEPSignals));

	                    eventStates.add(eventState);
	            }
	        } 

			Collections.sort(eventStates, new EventState.PriorityComparator(
						programPriorities));
			

   	return eventStates; 	
   }
    
    @Override
    public Participant getClientLJFByName(String clientName) {
        return clientEAO.findByClientLJF(clientName, true);
    }

}
