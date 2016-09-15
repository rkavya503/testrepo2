package com.akuacom.pss2.participant;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.akuacom.accmgr.ws.Role;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.ws.UserArray;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.participant.contact.ParticipantContactEAO;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.subaccount.SubAccount;
import com.akuacom.pss2.subaccount.SubAccountEAO;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.EncryptUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.UserType;
import com.honeywell.dras.ssm.api.request.EnrollAggregatorRequest;
import com.honeywell.dras.ssm.api.request.data.AggregatorEnrollmentData;
import com.honeywell.dras.ssm.api.request.data.CustomerEnrollmentData;
import com.honeywell.dras.ssm.api.response.EnrollAggregatorResponse;

@Stateless
public class ParticipantManagerBean implements ParticipantManager.R,
        ParticipantManager.L {

    @EJB
    ParticipantEAO.L participantEAO;

    @EJB
    ClientEAO.L clientEAO;

    @EJB
    SystemManager.L systemManager;

    @EJB
    SubAccountEAO.L sAccountEAO;

    @EJB
    ProgramParticipantEAO.L ppeao;

    @EJB
    EventEAO.L eventEAO;
    
    @EJB
    ParticipantContactEAO.L participantContactEAO;
    

    private static final Logger log = Logger
            .getLogger(ParticipantManagerBean.class);
    
    private static final String PRIVATEKEY = "ssmprivate.key";
    
    public int getParentParticipantCount(){
    	return participantEAO.getUserDataSetSize(false);
    }


    public int getClientParticipantCount(){
    	return participantEAO.getUserDataSetSize(true);
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.participant.ParticipantManager#getParticipants()
     */
    public List<String> getParticipants() {
        return participantEAO.getParticipants();
    }

    public List<Participant> getAllParticipants() {
        return participantEAO.getAll();
    }

    /**
     * TODO: make sure participant will have 4 contacts or current UI to display
     * correctly.
     * 
     * @param participantName
     *            participant name
     * 
     * @return Participant object or null if not found
     */
    public Participant getParticipant(String participantName) {
        try {
            return participantEAO.getParticipantLJF(participantName);
        } catch(NoResultException nre) {
            log.warn("no result for " + participantName);
            return null;
        }
        catch (Exception e) {
            String message = "error get participant " + participantName;
            throw new EJBException(message + " "+ e);
        }
    }
    
    public Participant getParticipantForProgramName(String participantName,String programName) {
        try {
            return participantEAO.getParticipantForProgramNameLJF(participantName,programName);
        } catch(NoResultException nre) {
            log.warn("no result for " + participantName);
            return null;
        }
        catch (Exception e) {
            String message = "error get participant " + participantName;
            throw new EJBException(message + " "+ e);
        }
    }
    
    public Participant getParticipant(String participantName, boolean isClient) {
        try {
            return participantEAO.findByNameAndClientLJF(participantName, isClient);
        } catch(NoResultException nre) {
            return null;
        } catch (Exception e) {
            String message = "error get participant " + participantName;
            throw new EJBException(message + " "+ e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getParticipantByAccount
     * (java.lang.String)
     */
    public Participant getParticipantByAccount(String accountNumber)  {
        try {
            return participantEAO.getParticipantByAccount(accountNumber);
        } catch (Exception e) {
            String message = "error get participant with account number "
                    + accountNumber;
//            log.warn(LogUtils.createLogEntry("", "", message, null));
            throw new EJBException(message, e);
        }
    }
    
    @Override
    public Participant getParticipantByClient(String participantName) {
		return participantEAO.getParticipantByClient(participantName);
	}
    
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Participant getByAccount(String accountNumber, boolean client){
    	return getParticipantByAccount(accountNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#createParticipantAccount
     * (java.util.List)
     */
    public void createParticipantAccount(List<ParticipantExtInfo> PAlist) {
        String curParAccID = "";
        try {
            for (ParticipantExtInfo pai : PAlist) {
                curParAccID = pai.getAccountNumber();

                List<Program> progs = pai.getPrograms();
                String[] programNames = new String[progs.size()];
                int i = 0;
                for (Program prog : progs) {
                    programNames[i] = prog.getProgramName();
                    i++;
                }
                this.createParticipant(pai, pai.getPassword().toCharArray(),
                        programNames);

            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("Failed to create " + curParAccID, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#setParticipantPassword
     * (java.lang.String, String)
     */
    public void setParticipantPassword(String uuid, String passward) {
        try {
            Participant part = participantEAO.getById(uuid);
            AccMgrWSClient accmgrClient = new AccMgrWSClient();
            accmgrClient.initialize();
            String domainName = null;
            if (part.isClient()) {
                domainName = "CLIENT";
            } else {
                domainName = "PSS2";
            }
            User user = accmgrClient.getAccmgr().getUserByName(domainName,
                    part.getParticipantName());
            accmgrClient.getAccmgr().changePassword(user.getId(), null,
                    passward);
        } catch (Exception e) {
            throw new EJBException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#updateParticipantPassword
     * (java.lang.String, char[], char[])
     */
    public void updateParticipantPassword(String participantName,
            char[] oldPass, char[] password) {
        throw new EJBException(
                "Method updateParticipantPassword not implemented yet.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getSceAccounts(java.lang
     * .String)
     */
    public List<SubAccount> getSubAccounts(String participantName) {
        return sAccountEAO.getSubAccounts(participantName, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#createSubAccount(com.
     * akuacom.pss2.core.model.SCEAccountInfo, java.lang.String)
     */
    public void createSubAccount(SubAccount account, String participantName) {
		SubAccount similarAccount = this.getSubAccount(account.getSubAccountId(), participantName);
		if (similarAccount == null) {
			Participant p = participantEAO.getParticipant(participantName);
			account.setParticipant(p);
			try {
				sAccountEAO.create(account);
			} catch (DuplicateKeyException e) {
				throw new EJBException("Duplicate Key!");
			} catch (Exception e) {
				throw new EJBException("Error creating SubAccount!");
			}
		} else {
			throw new EJBException("Duplicate Subaccount!");
		}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getSubAccount(java.lang
     * .String, java.lang.String)
     */
    public SubAccount getSubAccount(String subAccountName,
            String participantName) {
		SubAccount subAccount = null;
		subAccount = sAccountEAO
			.getSubAccount(subAccountName, participantName, false);

		return subAccount;
    }

    public SubAccount getSubAccount(String uuid) {
        // it's better to save the subAccount uuid in clients and always use
        // uuid to get object if possible.
        try {
            return sAccountEAO.getById(uuid);
        } catch (EntityNotFoundException e) {
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#removeSubAccount(java
     * .lang.String, java.lang.String)
     */
    public void removeSubAccount(String subAccountName, String participantName) {
        // todo change to use SubAccountEAOBean
        // eao.removeSceAccount(subAccountName, participantName);
    }

    public void removeSubAccount(String uuid) {
        // it's better to save the subAccount uuid in clients and always use it
        // to get object if possible.
        try {
            sAccountEAO.delete(uuid);
        } catch (EntityNotFoundException e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#updateSubAccounts(java
     * .util.List, java.lang.String)
     */
    public void updateSubAccounts(List<SubAccount> accounts,
            String participantName) {
        sAccountEAO.removeSubAccounts(participantName, false);
        for (SubAccount account : accounts) {
            this.createSubAccount(account, participantName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#updateParticipantAccount
     * (java.util.List)
     */
    public void updateParticipantAccount(List<ParticipantExtInfo> PAlist) {
        String curParAccID = "";
        // TODO:remove dependency on ProgramParticipant
        ProgramParticipantManager programManager = EJBFactory
                .getBean(ProgramParticipantManager.class);
        try {
            for (ParticipantExtInfo pai : PAlist) {
                curParAccID = pai.getAccountNumber();
                participantEAO.updateParticipant(pai.getParticipantName(), pai);
                this.removeParticipantFromAllPrograms(pai.getUser());
                List<Program> progs = pai.getPrograms();
                for (Program prog : progs) {
                    programManager.addParticipantToProgram(
                            prog.getProgramName(), pai.getUser(), false);
                }
            }
        } catch (Exception e) {
            throw new EJBException("Failed to create " + curParAccID, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#searchParticipants(java
     * .util.List, com.akuacom.ejb.search.SearchHandler)
     */
    public SearchHandler searchParticipants(List<String> programNames,
            SearchHandler searchHandler) {
        try {
            SearchHandler ret;
            ret = participantEAO.searchParticipants(programNames, null, searchHandler);
            return ret;
        } catch (AppServiceException asex) {
            throw new EJBException(asex);
        }
    }

    //
    public SearchHandler searchParticipants(List<String> programNames,
            List<String> accountIDs, SearchHandler searchHandler) {
        try {
            return participantEAO.searchParticipants(programNames, accountIDs,  searchHandler);
        } catch (AppServiceException asex) {
            throw new EJBException(asex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#findParticipantsByAccounts
     * (java.util.List)
     */
    public List<Participant> getParticipantsByAccounts(List<String> accountIDs) {
        try {
            List<Participant> ret;
            ret = participantEAO.findParticipantsByAccounts(accountIDs);
            return ret;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getSignalsForParticipant
     * (java.lang.String)
     */
    public List<ProgramSignal> getSignalsForParticipant(String participantName,
            boolean isClient) {

        List<ProgramSignal> signals = new ArrayList<ProgramSignal>();
        try {
            for (String programName : getProgramsForParticipant(
                    participantName, isClient)) {
                signals = participantEAO.getNewSignalsInProgram(programName);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return signals;
    }

    private void removeAggregation(ProgramParticipant programParticipant)
    {
        ProgramParticipantAggregationManager aggManager = 
        	EJBFactory.getBean(ProgramParticipantAggregationManager.class);

       	if(programParticipant.getChildren() != null && 
       		programParticipant.getChildren().size() != 0)
    	{
    		aggManager.removeChildren(programParticipant, 
    			programParticipant.getChildren());
    	}
    	if(programParticipant.getParent() != null)
    	{
    		aggManager.removeParent(programParticipant);
    	}
    }

    private void removeUser(String userName)
    {
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        
        User user = null;
		try {
			user = accmgrClient.getAccmgr().getUserByName("PSS2", userName);
		} catch (Exception ex) {
			user = null;
			log.error("Failed to lookup user in account mgr prior to delete", ex);
		}
		if (user != null) {
			accmgrClient.getAccmgr().removeUser(user.getId());
		}
        
    }
    // participant
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#removeParticipant(java
     * .lang.String)
     */
    public void removeParticipant(String participantName) {

        Participant participant = getParticipant(participantName);
        // remove all event participants
        // create a list of event names first instead of just removing them b/c
        // EventManager.removeParticipantFromEvent() modifies 
        // Participant.eventParticpants
        EventManager eventManager = EJBFactory.getBean(EventManager.class);
        Set<String> eventNames = new HashSet<String>();
        for(EventParticipant eventParticipant: participant.getEventParticipants())
        {
        	eventNames.add(eventParticipant.getEvent().getEventName());
        }
        for(String eventName: eventNames)
        {
        	eventManager.removeParticipantFromEvent(eventName, participantName, true);
        }
        
        ClientManager clientManager = EJBFactory.getBean(ClientManager.class);

        // delete all children
        List<String> client = this.getClientNamesByParticipant(participantName);
        for (String clientName : client) {
            clientManager.removeClient(clientName);
        }
        
        // delete all aggregation relationships
        for(ProgramParticipant programParticipant: 
        	participant.getProgramParticipants())
        {
        	removeAggregation(programParticipant);
        }
        
        participantEAO.removeParticipant(participantName);

        removeUser(participantName);
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.pss2.participant.ParticipantManager#
     * removeParticipantFromAllPrograms(java.lang.String)
     */
    public void removeParticipantFromAllPrograms(String participantName) {
        // todo: participantUUID only used in openADR
        ParticipantEAOBean partManager = EJBFactory
                .getBean(ParticipantEAOBean.class);
        partManager.removeParticipantFromAllEvents(participantName);
        List<String> partNames = null;
        try {
            partNames = getProgramsForParticipant(participantName, false);
        } catch (Exception ex) {
            partNames = null;
        }
        if (partNames != null) {
            for (String programName : partNames) {
                ppeao.removeParticipantFromProgram(programName, participantName);

            }
        }
    }

    // TODO; this should be moved to the u/i
    /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.pss2.participant.ParticipantManager#
     * getProgramsForParticipantAsString(java.lang.String)
     */
    public String getProgramsForParticipantAsString(String participantName,
            boolean isClient) {
        StringBuilder rv = new StringBuilder();
        List<String> progs = getProgramsForParticipant(participantName,
                isClient);
        int size = progs.size();
        int count = 0;
        for (String program : progs) {
            rv.append(program);
            if (++count < size) {
                rv.append(",");
            }
        }
        return rv.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getProgramsForParticipant
     * (java.lang.String)
     */
    public List<String> getProgramsForParticipant(String participantName,
            boolean isClient) {

        return participantEAO.findProgramNamesByName(participantName, isClient);

    }
    
    public List<String> getProgramGroup(String participantName, boolean isClient) {
    	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @ seecom.akuacom.pss2.participant.ParticipantManager#
     * getProgramsForParticipantAsObject(java.lang.String)
     */
    public List<Program> getProgramsForParticipantAsObject(
            String participantName, boolean isClient) {
        List<Program> programs = new ArrayList<Program>();
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        for (String programName : programManager.getPrograms()) {
            ProgramEJB programEJB = systemManager
                    .lookupProgramBean(programName);
            if (programEJB.isParticipantInProgram(programName, participantName,
                    isClient)) {
                programs.add(programManager.getProgram(programName));
            }
        }
        return programs;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.pss2.participant.ParticipantManager#
     * removeParticipantsByAccountNumber(java.util.List)
     */
    public void removeParticipantsByAccountNumber(List<String> accountNumberList) {
        String curID = "";
        for (String accountNumber : accountNumberList) {
            try {
                Participant part = participantEAO.getParticipantByAccount(accountNumber);
                curID = part.getAccountNumber();
                this.removeParticipant(part.getUser());
            } catch (Exception e) {
                throw new EJBException("Failed on current id: " + curID, e);
            }
        }
    }

    
    
    public List<EventInfo> getEventsForParticipant(Program program, String participantName, boolean isClient){
        List<EventInfo> participantEvents = new ArrayList<EventInfo>();
        EventManager pmb = EJBFactory.getBean(EventManager.class);
        com.akuacom.pss2.program.ProgramManager programManager = EJBFactory
                .getBean(com.akuacom.pss2.program.ProgramManager.class);

        
        

    	List<EventInfo> eventList = programManager
                .getEventsForProgram(program);
   	
        for (EventInfo eventInfo : eventList) {
            Event event = pmb.getEvent(program.getProgramName(),
                    eventInfo.getEventName());
            if (event.isIssued()) {
                for (EventParticipant eventParticipant : event
                        .getParticipants()) {
                    if (eventParticipant.getParticipant()
                            .getParticipantName().equals(participantName)) {
                        participantEvents.add(eventInfo);
                        break;
                    }
                }
            }
        }
            
        
        return participantEvents;
    	
    }
    
    
    
    // TODO: this should be sorted correctly
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getEventsForParticipant
     * (java.lang.String)
     */
    public List<EventInfo> getEventsForParticipant(String participantName,
            boolean isClient) {
        List<EventInfo> participantEvents = new ArrayList<EventInfo>();
        EventManager pmb = EJBFactory.getBean(EventManager.class);
        com.akuacom.pss2.program.ProgramManager programManager = EJBFactory
                .getBean(com.akuacom.pss2.program.ProgramManager.class);

        for (String programName : getProgramsForParticipant(participantName,
                isClient)) {
            for (EventInfo eventInfo : programManager
                    .getEventsForProgram(programName)) {
                Event event = pmb.getEvent(programName,
                        eventInfo.getEventName());
                if (event.isIssued()) {
                    for (EventParticipant eventParticipant : event
                            .getParticipants()) {
                        if (eventParticipant.getParticipant()
                                .getParticipantName().equals(participantName)) {
                            participantEvents.add(eventInfo);
                            break;
                        }
                    }
                }
            }
        }
        return participantEvents;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.pss2.participant.ParticipantManager#
     * getParticipantSignalActiveEvent(java.lang.String, java.lang.String,
     * java.lang.String)
     
    public Event getParticipantSignalActiveEvent(String programName,
            String participantName, boolean isClient, String signalName) {

        final List<Event> events = eventEAO.findByProgramName(programName);
        for (Event event : events) {
            ProgramEJB programEJB = systemManager
                    .lookupProgramBean(programName);
            if (programEJB
                    .isParticipantEventSignalActive(programName,
                            event.getEventName(), participantName, isClient,
                            signalName)) {
                return event;
            }
        }
        return null;
    }*/

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.participant.ParticipantManager#getParticipantStatus(
     * java.lang.String)
     */
    public ClientStatus getParticipantStatus(String participantName,
            boolean isClient) {
        try {
            return participantEAO.getParticipantStatus(participantName);
        } catch (Exception ex) {
            String message = "error getting participant " + participantName;
            log.warn(LogUtils.createLogEntryUser("", participantName, "",
                    message, null));
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), ex));
            return ClientStatus.OFFLINE;
        }
    }

 
    public List<Participant> findParticipantsByProgramName(String progName)
            throws AppServiceException {
        return participantEAO.findUsersByProgramName(progName);
    }

     /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.program.services.ProgramServicer#
     * findParticipantsForUpdateStatusLoop()
     */
    public List<Participant> findParticipantsForUpdateStatusLoop() {
        return participantEAO.findParticipantsForUpdateStatusLoop();
    }

    public Participant setParticipantStatus(Participant part,ClientStatus status) {
        return participantEAO.setParticipantStatus(part, status);
    }

    public List<String> getClientNamesByParticipant(String partName) {
        return clientEAO.getClientNamesByParticipant(partName);
    }

    public List<EventParticipant> getEventParticipantsByPartNames(
            List<String> participantNames, boolean isClient) {
        try {
            return participantEAO.findEventParticipants(participantNames, isClient);
        } catch (Exception ex) {
            String message = "error creating EventParticipants for participant "
                    + participantNames;
            log.warn(LogUtils.createLogEntry("", "", message, null));
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), ex));
        }
        return null;
    }

    public List<Participant> getAllClients(){
        return clientEAO.getByUserType(true);
    }

    
    
    public void createParticipants(List<Participant> pList, List<char[]> passwords,
            Program prog) {
        String programName = prog.getProgramName();
    	ProgramEJB programEJB = systemManager
                .lookupProgramBean(programName);

    	for(int i = 0; i < pList.size(); i++){
    		createPart(pList.get(i), passwords.get(i), prog, programEJB);
    	}
    }
    
    
    public void createPart(Participant p, char[] password,
            Program prog, ProgramEJB programEJB) {
        participantEAO.createParticipant(p);

        createUser(password, p.getParticipantName(), getParticipantRoles());

        ProgramParticipantManager programManager = EJBFactory
                    .getBean(ProgramParticipantManager.class);
        programManager.addParticipantToProgram(prog, 
                p, p.isClient(), programEJB);
    }

    
    
    public void createParticipant(Participant p, char[] password,
            String[] assignedProgramNames) {
        participantEAO.createParticipant(p);
        createUser(password, p.getParticipantName(), getParticipantRoles());

        if (assignedProgramNames != null) {
            final ProgramParticipantManager programManager = EJBFactory
                    .getBean(ProgramParticipantManager.class);
            for (String program : assignedProgramNames) {
                programManager.addParticipantToProgram(program, 
                    p.getParticipantName(), p.isClient());
            }
        }
    }
    
//    public Participant createParticipant(Participant p, char[] password,
//            String[] assignedProgramNames) {
//        Participant create = participantEAO.createParticipant(p);
//
//        createUser(password, p.getParticipantName(), getParticipantRoles());
//
//        if (assignedProgramNames != null) {
//            final ProgramParticipantManager programManager = EJBFactory
//                    .getBean(ProgramParticipantManager.class);
//            for (String program : assignedProgramNames) {
//                programManager.addParticipantToProgram(program, 
//                    p.getParticipantName(), p.isClient());
//            }
//        }
//        return create;
//    }


    private void createUser(Participant p, char[] password){
        createUser(password, p.getParticipantName(), getParticipantRoles());
    }
    
	public List<String> enrollAggregatorInDras(AggregatorEnrollmentData aggregatorEnrollmentData) {
		//createCustomerForAggregator(aggregatorEnrollmentData);
		List<String> participantList = new ArrayList<String>();
		String aggName = aggregatorEnrollmentData.getCustomerName();
	    try {		
		/// -- Suresh 
		
		// [Arun]  participantManager.createParticipant(participant, participantPassword.toCharArray(), null);
		log.info("[START OF PARTICIPANT AND CLIENT CREATION]");
	    EncryptUtil passwordEncoder = new EncryptUtil();
	    InputStream inputStream = null;
	    String clientPwd = null;
	    String partName;
	    String decryptPPwd = null;
	    String decryptCPwd = null;
	    String pwd = null;
	    Participant part = null;
	    String[] programs = null;
	    inputStream = ParticipantManagerBean.class.getClassLoader().getResourceAsStream(PRIVATEKEY);
		ObjectInputStream keyinputStream = null;
		keyinputStream = new ObjectInputStream(inputStream);
		final PrivateKey privateKey = (PrivateKey) keyinputStream.readObject();
		List<CustomerEnrollmentData> enrollmentDataList = aggregatorEnrollmentData.getChilds();
		ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
		Participant client = null;
		for ( CustomerEnrollmentData enrollmentData : enrollmentDataList ) {
			partName = enrollmentData.getParticipantName();
			participantList.add(partName);
			part = getParticipantDetails(enrollmentData);
			pwd = enrollmentData.getPassword();
			clientPwd = enrollmentData.getClientPassword();
			decryptPPwd = passwordEncoder.decrypt(pwd, privateKey).trim();
			decryptCPwd = passwordEncoder.decrypt(clientPwd, privateKey).trim();
			//createUser(p, password.toCharArray());
			programs = enrollmentData.getPrograms();
			createParticipant(part, decryptPPwd.toCharArray(), programs);
			//[Arun] clientManager.createClient(client, clientPassword.toCharArray());
			client = new Participant();
			String fullClientName =
					enrollmentData.getParticipantName() + enrollmentData.getClientName();
				client.setParticipantName(fullClientName);
				client.setAccountNumber(enrollmentData.getAccountNumber());
				client.setParent(enrollmentData.getParticipantName());
				client.setClient(true);
				client.setType((byte) 0);
//				client.setContacts(contacts);
				clientManager.createClient(client, decryptCPwd.toCharArray());
//				clientManager.updateClient(clientDetails);
				createClientContactForSSM(enrollmentData, fullClientName, clientManager);
		}
		log.info("[END OF PARTICIPANT AND CLIENT CREATION]");
//		log.info("[START OF AGGRIGATION]");
	
//		createAggregatorHierarchy(aggregatorEnrollmentData);
//		log.info("[END OF AGGRIGATION]");
		//// [Suresh] createClientContactForSSM(enrollmentData, clientName);
		/// -- Suresh ENDS
		} catch (Exception exc) {
			log.error("Exception on creating aggregation:: Participant List already created: " + participantList);
			removeCustomerCreatedOnAggregator(participantList);
			throw new ValidationException("Approval process was unsuccessful, please try again later.");
		}
	    return participantList;
	}
	
	public void enrollAggregatorClientInDras(AggregatorEnrollmentData aggregatorEnrollmentData) {
		List<CustomerEnrollmentData> enrollmentDataList = aggregatorEnrollmentData.getChilds();
		for ( CustomerEnrollmentData enrollmentData : enrollmentDataList ) {	
			createClientForSSM(enrollmentData);
		}
		
	}
	
	public void enrollAggregatorUserInDras(AggregatorEnrollmentData aggregatorEnrollmentData) {
		List<CustomerEnrollmentData> enrollmentDataList = aggregatorEnrollmentData.getChilds();
		for ( CustomerEnrollmentData enrollmentData : enrollmentDataList ) {	
			Participant p = getParticipantDetails(enrollmentData);
			String password = enrollmentData.getPassword();
			createUser(p, password.toCharArray());
		}
		createAggregatorHierarchy(aggregatorEnrollmentData);
	}
	
    public void createCustomerForAggregator(AggregatorEnrollmentData aggregatorEnrollmentData) {
    	List<CustomerEnrollmentData> enrollmentDataList = aggregatorEnrollmentData.getChilds();
    	List<String> participantList = new ArrayList<String>();
    	try {
        	for ( CustomerEnrollmentData enrollmentData : enrollmentDataList ) {		
        		String[] programs = enrollmentData.getPrograms();

        		Participant p = getParticipantDetails(enrollmentData);
        		participantEAO.createParticipant(p);
        		
        		 if (programs != null) {
		            final ProgramParticipantManager programManager = EJBFactory
		                    .getBean(ProgramParticipantManager.class);
		            for (String program : programs) {
		                programManager.addParticipantToProgram(program, 
		                    p.getParticipantName(), p.isClient());
		            }
		        }
        		participantList.add(enrollmentData.getCustomerName());
        	}
        	
    	} catch (Exception exc) {
    		log.error("Exception on creating aggregation participants, "+ exc.getMessage());
    		removeCustomerCreatedOnAggregator(participantList);
    	}
    }
    
    private Participant getParticipantDetails(CustomerEnrollmentData enrollmentData){
    	String accNum = enrollmentData.getAccountNumber();
		Participant p = new Participant();
		p.setUser(enrollmentData.getParticipantName());
		p.setAccountNumber(accNum);
		p.setClientOfflineNotificationEnable(false);
		p.setOptOutClientOfflineNotification(false);
		p.setClientOfflineNotificationAggEnable(false);
		p.setThresholdsSummer(24);
		p.setThresholdsUnSummer(168);
		PSS2Features features = systemManager.getPss2Features();
		p.setDefaultEventOptoutEnabled(true);
		p.setPartEventOptoutEnabled(features.isEventOptoutEnabled());
		p.setEnrollmentDate(new Date());
		p.setCustomerName(enrollmentData.getCustomerName());
		p.setUserType(UserType.ADVANCED);
		p.setShedType("SIMPLE");
		p.setShedPerHourKW(Double.parseDouble("0.0"));
		p.setRequestId(enrollmentData.getRequestId());
		return p;
    }
    private void createUser(char[] password, String userName,
            List<Role> participantRoles) {
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        User user = new User();
        user.setId(userName);
        user.setDomainname("PSS2");
        user.setUsername(userName);
        user.setPassword(new String(password));
        user.getRoles().addAll(participantRoles);
        user.setEmail(userName);
        user.setStatus("ACTIVE");
        accmgrClient.getAccmgr().createUser("PSS2", user);
    }

    private List<Role> getParticipantRoles() {
        final ArrayList<Role> list = new ArrayList<Role>();
        list.add(generateRole(DrasRole.FacilityManager.toString()));
        list.add(generateRole("website"));
        list.add(generateRole("PSS2WS"));
        return list;
    }

    private Role generateRole(String rolename) {
        Role role = new Role();
        role.setRolename(rolename);
        return role;
    }

	public void updateParticipant(Participant p, String[] programNames) {
		String participantName = p.getParticipantName();
        List<Participant> exist=participantEAO.findByAccount(p.getAccountNumber());
        if (exist!=null && exist.size()>0) {
    		List<String> duplicatedNames=new ArrayList<String>();
    		for (Participant part:exist) {
    			if (!part.getParticipantName().equalsIgnoreCase(participantName)) {
    				duplicatedNames.add(part.getParticipantName());
    			}
    		}
    		
    		if (duplicatedNames.size()>0) {
	            //throw new EJBException(message);
	            throw new ValidationException(
						"ERROR_CREATE_PARTICIPANT_ALREADY_EXIST", duplicatedNames.toString());
    		}
        }
        
        //Seconday account number check
        if(!StringUtils.isEmpty(p.getSecondaryAccountNumber())){
        	exist=participantEAO.findBySecondaryAccount(p.getSecondaryAccountNumber());
            if (exist!=null && exist.size()>0) {
        		List<String> duplicatedNames=new ArrayList<String>();
        		for (Participant part:exist) {
        			if (!part.getParticipantName().equalsIgnoreCase(participantName)) {
        				duplicatedNames.add(part.getParticipantName());
        			}
        		}
        		
        		if (duplicatedNames.size()>0) {
    	            //throw new EJBException(message);
    	            throw new ValidationException(
    						"ERROR_CREATE_PARTICIPANT_SECONDACCOUNTNUMBER_ALREADY_EXIST", duplicatedNames.toString());
        		}
            }
        }
        
        //ApplicationID Check
        
        if(p.getApplicationId() != null && !StringUtils.isEmpty(p.getApplicationId())){
        	List<Participant> appIdExist = participantEAO.findByApplicationId(p.getApplicationId());
        	if(appIdExist != null && !appIdExist.isEmpty()){
        		List<String> duplicateAppIds=new ArrayList<String>();
        		for(Participant par: appIdExist){
        			if(!par.getParticipantName().equalsIgnoreCase(p.getParticipantName())){
        				duplicateAppIds.add(par.getParticipantName());        				       				
        			}
        		}
        		
        		if(!duplicateAppIds.isEmpty()){
        			throw new ValidationException(
    						"ERROR_CREATE_PARTICIPANT_APPLICATIONID_ALREADY_EXIST", duplicateAppIds.toString()); 
        		}
        	}
        }
    	
        try {
            // logic check
            List<EventParticipant> epList = participantEAO.findEventParticipants(
                    participantName, false);
            
            // we need to get the participant again here otherwise when this is 
            // called from the ui we get a lazy load exception
            Participant participant = getParticipant(p.getParticipantName());
            participant.setAccountNumber(p.getAccountNumber());
            participant.setSecondaryAccountNumber(p.getSecondaryAccountNumber());
            participant.setApplicationId(p.getApplicationId());
	        participant.setType(p.getType());
	        participant.setFirstName(p.getFirstName());
	        participant.setLastName(p.getLastName());
	        participant.setFeedback(p.isFeedback());
	        participant.setMeterId(p.getMeterId());
	        participant.setAddress(p.getAddress());
	        participant.setGridLocation(p.getGridLocation());
	        participant.setLatitude(p.getLatitude());
	        participant.setLongitude(p.getLongitude());
	        participant.setActivated(p.getActivated());
	        participant.setDataEnabler(p.getDataEnabler());
	        participant.setShedPerHourKW(p.getShedPerHourKW());
	        participant.setShedType(p.getShedType());
	        participant.setClient(p.isClient());
	        participant.setInstaller(p.getInstaller());
	        participant.setUserType(p.getUserType());
	        participant.setAggregator(p.getAggregator());
	        participant.setNonAutoDR(p.getNonAutoDR());
	        participant.setCustomer(p.getCustomer());
	        participant.setRetained(p.getRetained());
	        
	        
	        participant.setCustomerName(p.getCustomerName());
	        participant.setServiceStreetAddress(p.getServiceStreetAddress());
	        participant.setServiceCityName(p.getServiceCityName());
	        participant.setZip(p.getZip());
	        participant.setABank(p.getABank());
	        participant.setSlap(p.getSlap());
	        participant.setPNode(p.getPNode());
	        participant.setServicePlan(p.getServicePlan());
	        
	        participant.setNotes(p.getNotes());
	        participant.setNotesAuthor(p.getNotesAuthor());
	        participant.setNotesLastUpdate(p.getNotesLastUpdate());
	        
	        participant.setRateEffectiveDate(p.getRateEffectiveDate());
	        participant.setRateEffectiveDate(p.getRateEffectiveDate());
	        
	        participant.setDirectAccessParticipant(p.getDirectAccessParticipant());
	        participant.setTestParticipant(p.getTestParticipant());

            participant.setPremiseNumber(p.getPremiseNumber());
            participant.setStartDate(p.getStartDate());
            participant.setEndDate(p.getEndDate());

            participant.setComment(p.getComment());
            participant.setEnrollmentDate(p.getEnrollmentDate());

            participant.setDefaultEventOptoutEnabled(p.getDefaultEventOptoutEnabled());
            participant.setPartEventOptoutEnabled(p.getPartEventOptoutEnabled());
            
          //participant client offline notification setting
            participant.setClientOfflineNotificationEnable(p.getClientOfflineNotificationEnable());
            participant.setClientOfflineNotificationAggEnable(p.getClientOfflineNotificationAggEnable());
            participant.setOptOutClientOfflineNotification(p.getOptOutClientOfflineNotification());
            participant.setThresholdsSummer(p.getThresholdsSummer());
            participant.setThresholdsUnSummer(p.getThresholdsUnSummer());
            
	        participant.setDemandLimiting(p.getDemandLimiting());
	        if ((p.getDemandLimiting() != null) && (p.getDemandLimiting()))
	        	participant.setDataEnabler(true);
	        //participant.setUisActive(p.getUisActive());        

	        
            Set<ProgramParticipant> programParticipants = 
            	participant.getProgramParticipants();
            ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
            List<Participant> clientList = clientManager.getClients(participantName);
            // remove unselected program participant from participant
            List<String> programs = Arrays.asList(programNames);
            log.debug("Getting size of program participants for participant: " + 
            	participant.getUUID() + "" + programParticipants.size());
            for (Iterator<ProgramParticipant> iterator = programParticipants.iterator(); iterator.hasNext();) {
                ProgramParticipant pp = iterator.next();
                if (!programs.contains(pp.getProgramName())) {
                    for (EventParticipant ep: epList) {
                    	if (ep.getEvent().getProgramName().equals(pp.getProgramName())) {
                    		throw new ValidationException(
                                    "ERROR_PARTICIPANT_UPDATE_IN_ACTIVE_EVENT", participantName);
                        }
                    }
                    
                    // remove aggregation connections
                    removeAggregation(pp);

                    // remove participant from program
                    iterator.remove();
                    // iter through clients
                    for (Participant client : clientList) {
                        final Set<ProgramParticipant> programClients = client.getProgramParticipants();
                        for (Iterator<ProgramParticipant> iterator2 = programClients.iterator(); iterator2.hasNext();) {
                            ProgramParticipant programParticipant = iterator2.next();
                            // remove client from program
                            if (!programs.contains(programParticipant.getProgramName())) {
                                iterator2.remove();
                            }
                        }
                    }
                }
            }

            for (Participant client : clientList) {
                participantEAO.updateParticipant(client.getParticipantName(), client);
            }

            // update (only delete the unselected ones, the new one will be added by later logic)
            participantEAO.updateParticipant(participantName, participant);

            // get new program list
            final Set<String> newProgramNames = new HashSet<String>();
            for (String programName : programs) {
                boolean found = false;
                for (ProgramParticipant pp : programParticipants) {
                    if (programName.equals(pp.getProgramName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    newProgramNames.add(programName);
                }
            }

            final ProgramParticipantManager programManager = EJBFactory.getBean(ProgramParticipantManager.class);
            // add participant/clients to program
			List<String> clients = getClientNamesByParticipant(participantName);
			for (String program : newProgramNames) {
				programManager.addParticipantToProgram(program, participantName, false);
                for (String client : clients) {
                    clientManager.addClientToProgram(client, program, null);
                }
            }
        } catch (EJBException e) {
            EntityExistsException eee = ErrorUtil.getEntityExistsException(e);
            if (eee != null) {
                throw new ValidationException(
						"ERROR_CREATE_PARTICIPANT_ALREADY_EXIST", participantName);
            } else {
                throw e;
            }
        }
    }

    public void updateParticipant(Participant participant) {
        //log.debug("update participant manual control: "
        //        + participant.isManualControl());
        //log.debug("update participantexpires date: "
        //        + participant.getManualControlExpires());
        participantEAO.updateParticipant(participant);
    }

    public void updateParticipant(ParticipantPerf participant) {
        participantEAO.updateParticipant(participant);
    }

    public List<ProgramParticipant> getProgramParticpantsForClient(
            String clientName) {
        return clientEAO.findAllProgramParticipantsForClient(clientName);
    }

    public List<ProgramParticipant> getProgramParticpantsForClientConfig(
            String participantName, boolean isClient) {
        // return userEAO.findAllProgramParticipantsConfig(participantName,
        // false);
        return participantEAO.findAllProgramParticipantsConfig(participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.ProgramDataAccess#getParticipants(java.lang.String)
     */
    public List<String> getParticipantsForProgram(String programName) {
        try {
            List<String> participants = new ArrayList<String>();

            if (programName == null) {
                String message = "program name is null";
                log.error(message);
                throw new EJBException(message);
            }
            List<ProgramParticipant> pps = ppeao
                    .findProgramParticipantsByProgramName(programName);
            if (pps != null) {
                for (ProgramParticipant pp : pps) {
                    participants.add(pp.getParticipantName());
                }
            }

            return participants;
        } catch (Exception ex) {
            String message = "error getting participant for program "
                    + programName;
         // TODO 2992
            log.error(LogUtils.createLogEntry(programName, null, message, null));
            throw new EJBException(message, ex);
        }
    }

	@Override
	public void updateParticipantContact(ParticipantContact participantContact) {
		participantContactEAO.updateParticipant(participantContact);
		
	}
	
	public void createExternalContacts(String programName,
		Map<String,List<ParticipantContact>> externalContacts)
	{
		try { 
			ClientManager clientManager = EJBFactory.getBean(ClientManager.class);

            StringBuilder error = new StringBuilder("");
			for(String accountNumber: externalContacts.keySet()) {
                try {
                    boolean check = participantEAO.checkAccount(accountNumber);
                    if (!check) {
                        error.append("accountNumber ").append(accountNumber).append(" doesn't exist.\n");
                        continue;
                    }
                    Participant participant = getParticipantByAccount(accountNumber);
                    boolean created = false;
                    for (Participant client :
                            clientManager.getClients(participant.getParticipantName())) {
                        // first clear out any existing external contacts
                        Iterator<ParticipantContact> contactsIterator =
                                client.getContacts().iterator();
                        while (contactsIterator.hasNext()) {
                            ParticipantContact contact = (ParticipantContact) contactsIterator.next();
                            if (contact.getExternal()) {
                                contactsIterator.remove();
                            }
                        }

                        // if this is the first client in the given program,
                        // create the new external contacts for that client
                        if (!created) {
                            for (ProgramParticipant programParticipant :
                                    client.getProgramParticipants()) {
                                if (programParticipant.getProgramName().equals(programName)) {
                                    for (ParticipantContact contact :
                                            externalContacts.get(accountNumber)) {
                                        ParticipantContact newContact = new ParticipantContact();
                                        newContact.setType(contact.getType());
                                        newContact.setAddress(contact.getAddress());
                                        newContact.setCommNotification(false);
                                        newContact.setDescription(contact.getDescription());
                                        newContact.setEventNotification(
                                                ContactEventNotificationType.NoNotification);
                                        newContact.setExternal(true);
                                        newContact.setMsgThreshold(10);
                                        newContact.setOffSeasonNotiHours(0.0);
                                        newContact.setOnSeasonNotiHours(0.0);
                                        newContact.setParticipant(client);
                                        client.getContacts().add(newContact);
                                    }
                                }
                            }
                            created=true;
                        }
                        clientEAO.update(client);
                    }
                } catch (EJBException e) {
                    Exception e2 = e.getCausedByException();
                    if (e2 instanceof EntityNotFoundException) {
                        EntityNotFoundException e3 = (EntityNotFoundException) e2;
                        error.append(e3.getMessage()).append("\n");
                    }
				}
            }

            if (error.length() > 0) {
                throw new EJBException("The following ID(s) are not present in DRAS: \n" + error.toString());
            }

		} catch (EntityNotFoundException ex) {
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Returns whether the participant is online.
	 * The participant is considered online if all of its clients are online; otherwise, it is offline.
	 * @param participantName
	 * @return true if the participant is online
	 */
	public Boolean isParticipantOnline(String participantName) {
		return participantEAO.isParticipantOnline(participantName);
	}


	public Participant getParticipantOnly(String name, boolean client) {
		return clientEAO.findParticipantOnlyByName(name, client);
	}
	
	public Participant getParticipantAndEventsOnly(String name, boolean client) {
		return clientEAO.findParticipantWithEvents(name, client);
	}
	public List<Participant> getClientsAndEvents(String parent) {
		return clientEAO.findClientsWithEvents(parent);
	}
	public Participant getParticipantAndProgramParticipantsOnly(String name, boolean client){
		return clientEAO.findParticipantWithProgramsParticipants(name, client);
	}


	@Override
	public Participant getParticipantOnly(String participantName) {
		return participantEAO.getParticipant(participantName);
	}
	@Override
	public Participant getParticipantAndShedsOnly(String name) {
		return clientEAO.findParticipantWithSheds(name);
	}

	@Override
	public void persistParticipantContact(
			ParticipantContact participantContact, String uuid,
			List<String> participant_uuids) {
		participantContactEAO.persistParticipantContact(participantContact,uuid, participant_uuids);
		
	}


	@Override
	public void removeParticipantContact(String address, String desc,
			List<String> participant_uuids) {
		participantContactEAO.removeParticipantContact(address, desc, participant_uuids);
		
	}


	@Override
	public void updateParticipantContact(ParticipantContact participantContact, String orig_type,
			String orig_address, String orig_desc,
			List<String> participant_uuids) {
		participantContactEAO.updateParticipantContact(participantContact, orig_type, orig_address, orig_desc, participant_uuids);
		
	}


	@Override
	public void removeParticipantContact(
			List<ParticipantContact> participantContacts,
			List<String> participant_uuids) {
		for(ParticipantContact pc : participantContacts){
			participantContactEAO.removeParticipantContact(pc.getAddress(), pc.getDescription(), participant_uuids);
		}
		
	}


	@Override
	public List<Participant> getParticipantsBySecondaryAccounts(
			List<String> accountIDs) {
		  try {
	            List<Participant> ret;
	            ret = participantEAO.findParticipantsBySecondaryAccounts(accountIDs);
	            return ret;
	        } catch (Exception e) {
	            throw new EJBException(e);
	        }
	}


	@Override
	public void cascadeRemoveParticipantContact(
			List<ParticipantContact> participantContacts,String rootUuid,
			List<String> participant_uuids) {
		List<ParticipantContact> pcs = participantContactEAO.getParticipantContacts(rootUuid);
		
		List<ParticipantContact> retained = new ArrayList<ParticipantContact>();
		
		for (ParticipantContact c : pcs) {
    		if (c == null || c.getAddress() == null || c.getDescription() == null) {
    			continue;
    		}
    		boolean isRemoved = false;
    		for (ParticipantContact removed : participantContacts) {
    			if (c.getAddress().equalsIgnoreCase(removed.getAddress()) && 
    					c.getDescription().equals(removed.getDescription())) {
    				isRemoved = true;
    				continue;
    			}
    		}
    		if(!isRemoved){
    			retained.add(c);
    		}
    		
    	}
		
		for(ParticipantContact pc : participantContacts){
			participantContactEAO.removeParticipantContact(pc.getAddress(), pc.getDescription(), Arrays.asList(rootUuid));
		}
		
		List<String> part_uuids = new ArrayList<String>();
		if(participant_uuids!=null&&!participant_uuids.isEmpty()){
			for(String p : participant_uuids){
				if(!rootUuid.equalsIgnoreCase(p)){
					part_uuids.add(p);
				}
			}
		}
		participantContactEAO.removeParticipantContact(part_uuids);
		participantContactEAO.persistParticipantContacts(retained, part_uuids);
		
	}
	
	@Override
	public void cascadeUpdateParticipantContact(ParticipantContact participantContact, String orig_type,
			String orig_address, String orig_desc,String rootUuid,
			List<String> participant_uuids) {
		List<ParticipantContact> pcs = participantContactEAO.getParticipantContacts(rootUuid);
		
		participantContactEAO.updateParticipantContact(participantContact, orig_type, orig_address, orig_desc, Arrays.asList(rootUuid));
		
		for (ParticipantContact c : pcs) {
    		if (c == null || c.getAddress() == null || c.getDescription() == null) {
    			continue;
    		}
			if (c.getAddress().equalsIgnoreCase(orig_address) && 
					c.getDescription().equals(orig_desc)) {
				c.setType(participantContact.getType());
				c.setAddress(participantContact.getAddress());
				c.setDescription(participantContact.getDescription());
				c.setCommNotification(participantContact.isCommNotification());
				c.setEventNotification(participantContact.getEventNotification());
				c.setOffSeasonNotiHours(participantContact.getOffSeasonNotiHours());
				c.setOnSeasonNotiHours(participantContact.getOnSeasonNotiHours());
				c.setMsgThreshold(participantContact.getMsgThreshold());
				c.setDefaultMsgThreshold(participantContact.getDefaultMsgThreshold());
				c.setDemandLimitingNotifications(participantContact.getDemandLimitingNotifications());
				c.setRateLimitExceededNotifications(participantContact.getRateLimitExceededNotifications());
				c.setDemandModerateNotifications(participantContact.getDemandModerateNotifications());
				c.setDemandHighNotifications(participantContact.getDemandHighNotifications());
				c.setDemandNormalNotifications(participantContact.getDemandNormalNotifications());
				c.setDemandWarningNotifications(participantContact.getDemandWarningNotifications());
				
			}
    		
    	}
		
		List<String> part_uuids = new ArrayList<String>();
		if(participant_uuids!=null&&!participant_uuids.isEmpty()){
			for(String p : participant_uuids){
				if(!rootUuid.equalsIgnoreCase(p)){
					part_uuids.add(p);
				}
			}
		}
		
		participantContactEAO.removeParticipantContact(part_uuids);
		participantContactEAO.persistParticipantContacts(pcs, part_uuids);
		
	}
	@Override
	public List<Participant> findParticipantsWithEventParticipantsByAccounts(List<String> accounts){
		try {
            List<Participant> ret;
            ret = participantEAO.findParticipantsWithEventParticipantsByAccounts(accounts);
            return ret;
        } catch (Exception e) {
            throw new EJBException(e);
        }
	}


	@Override
	public Participant getParticipantByApplicationId(String applicationId) {
		try {
            return participantEAO.getParticipantByApplicationId(applicationId);
        } catch (Exception e) {
            String message = "error get participant with applicationId "
                    + applicationId;
            throw new EJBException(message, e);
        }
	}
	
	public void updateMeterName(String programName,
			Collection<String> accountNumbers, Map<String, String> meterNames)
		{
			try { 
				ClientManager clientManager = EJBFactory.getBean(ClientManager.class);

	            StringBuilder error = new StringBuilder("");
				for(String accountNumber: accountNumbers) {
	                try {
	                    boolean check = participantEAO.checkAccount(accountNumber);
	                    if (!check) {
	                        error.append("accountNumber ").append(accountNumber).append(" doesn't exist.\n");
	                        continue;
	                    }
	                    Participant participant = getParticipantByAccount(accountNumber);
	                    for (Participant client :
	                            clientManager.getClients(participant.getParticipantName())) {
	                        // if this is the first client in the given program,
	                        // create the new external contacts for that client
	                        client.setMeterName(meterNames.get(accountNumber));
	                        clientEAO.update(client);
	                    }
	                } catch (EJBException e) {
	                    Exception e2 = e.getCausedByException();
	                    if (e2 instanceof EntityNotFoundException) {
	                        EntityNotFoundException e3 = (EntityNotFoundException) e2;
	                        error.append(e3.getMessage()).append("\n");
	                    }
					}
	            }
	            if (error.length() > 0) {
	                throw new EJBException("The following ID(s) are not present in DRAS: \n" + error.toString());
	            }
			} catch (EntityNotFoundException ex) {
				throw new EJBException(ex);
			}
		}
	
	private void createClientForSSM(CustomerEnrollmentData enrollmentData){
		ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
		String participantName = enrollmentData.getParticipantName();
		String clientName = participantName+"."+enrollmentData.getClientName();
		Participant client = new Participant();
		client.setClient(true);
		client.setParent(participantName);
		client.setParticipantName(clientName);
		client.setCustomerName(enrollmentData.getCustomerName());
		client.setType((byte) 0);
		ParticipantEAO participantEAO = EJB3Factory
				.getLocalBean(ParticipantEAO.class);
		boolean found = participantEAO.checkAccount(clientName);
		if (found) {
			String message = "ERROR_CLIENT_NAME_DUPLICATED";

			//log.warn(LogUtils.createLogEntry("", "", message, null));
			throw new ValidationException(message);
		}
		clientManager.createClient(client, enrollmentData.getClientPassword().toCharArray());
//		createClientContactForSSM(enrollmentData, clientName);
	}
	
	private void createClientContactForSSM(CustomerEnrollmentData enrollmentData, String clientName, ClientManager clientManager){
//		ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
		Participant clientDetails = clientManager.getClientWithContacts(clientName);
			if (clientDetails != null && clientDetails.getContacts() != null)
			{
				for (ParticipantContact contact : clientDetails.getContacts())
				{
					if (contact.getAddress().equals(enrollmentData.getCcEmail1())
						&& contact.getType().equals("Email"))
					{
						continue;
					}
				}
			}

			ParticipantContact contact = null;
			Set<ParticipantContact> contacts = new HashSet<ParticipantContact>();
			
			/*contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName1());
			contact.setAddress(enrollmentData.getCcPhone1());;
			contact.setType(Contact.PHONE_NUMBER);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			
			//contacts.add(contact);
			clientDetails.getContacts().add(contact);
			contact.setParticipant(clientDetails);
			getClientManager().updateClient(clientDetails);
*/

//			Participant clientDetails = new Participant();
			contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName1());
			contact.setAddress(enrollmentData.getCcEmail1());;
			contact.setType(Contact.EMAIL_ADDRESS);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			contact.setDefaultMsgThreshold(true);

			contacts.add(contact);
			contact.setParticipant(clientDetails);
//			clientManager.updateClient(clientDetails);
			
			
//			contacts.add(contact);
			
			/*contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName2());
			contact.setAddress(enrollmentData.getCcPhone2());;
			contact.setType(Contact.PHONE_NUMBER);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			//contacts.add(contact);
			
			clientDetails.getContacts().add(contact);
			contact.setParticipant(clientDetails);
			getClientManager().updateClient(clientDetails);

*/			
			if (enrollmentData.getCcName2() != null && !enrollmentData.getCcName2().isEmpty() ) {
				if (enrollmentData.getCcEmail2() != null && !enrollmentData.getCcName2().isEmpty() ) {
					contact = new ParticipantContact();
					contact.setDescription(enrollmentData.getCcName2());
					contact.setAddress(enrollmentData.getCcEmail2());
					contact.setType(Contact.EMAIL_ADDRESS);
					contact.setCommNotification(true);
					contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
					contact.setOffSeasonNotiHours(Double.parseDouble("8"));
					contact.setOnSeasonNotiHours(Double.parseDouble("1"));
					contact.setMsgThreshold(Integer.parseInt("10"));
					contact.setDefaultMsgThreshold(true);
					//contacts.add(contact);
					contacts.add(contact);
					contact.setParticipant(clientDetails);
//					clientManager.updateClient(clientDetails);
				}
			}
			clientDetails.setContacts(contacts);
			clientManager.updateClient(clientDetails);
//			return contacts;
	}
	
	public void removeCustomerCreatedOnAggregator(List<String> participantList) {
		String participantName = null;
		Participant participant = null;
		AccMgrWSClient accmgrClient = new AccMgrWSClient();
		accmgrClient.initialize();
//		List<UserArray> cUserArrayList = Arrays.asList(accmgrClient.getAccmgr().getAllUsersForDomain("CLIENT"));
//		List<UserArray> pUserArrayList = Arrays.asList(accmgrClient.getAccmgr().getAllUsersForDomain("PSS2"));
		List<String> tempParticipantList = new ArrayList<String>(participantList);
		log.info("Exception on creating aggregation :: Rolled back the participant created :: participantList :: " + participantList+" unsuccessful roll back participants : " + tempParticipantList);
		try {
			for(String tempPartName : participantList){
				participantName = tempPartName;
				participant = participantEAO.getParticipantLJF(participantName);
				if ( participant != null && participant.getRequestId() != null ) {
					removeParticipant(tempPartName);
					tempParticipantList.remove(tempPartName);
				} else {
//					for (UserArray tempPUserArray : pUserArrayList) {
//						for (User tempPUser : tempPUserArray.getItem()) {
//							if (tempPUser.getUsername().equalsIgnoreCase(participantName)) {
								removeUserCreatedBySSM("PSS2", participantName, accmgrClient);
//							}
//						}
//					}
//					for (UserArray tempCUserArray : cUserArrayList) {
//						for (User tempCUser : tempCUserArray.getItem()) {
//							if (tempCUser.getUsername().equalsIgnoreCase(participantName+".Client")) {
								removeUserCreatedBySSM("CLIENT", participantName+".Client", accmgrClient);
							}
//						}
//					}
		        	tempParticipantList.remove(tempPartName);
//				}
			}
			log.info("Rolled back the users created :: participantList :: " + participantList+" unsuccessful roll back participants : " + tempParticipantList);
		} catch (Exception exe) {
			log.error("Exception on removeCustomerCreatedOnAggregator"+exe.getMessage());
			for(String tempPartName : participantList){
				participantName = tempPartName;
//				for (UserArray tempPUserArray : pUserArrayList) {
//					for (User tempPUser : tempPUserArray.getItem()) {
//						if (tempPUser.getUsername().equalsIgnoreCase(participantName)) {
							removeUserCreatedBySSM("PSS2", participantName, accmgrClient);
//						}
//					}
//				}
//				for (UserArray tempCUserArray : cUserArrayList) {
//					for (User tempCUser : tempCUserArray.getItem()) {
//						if (tempCUser.getUsername().equalsIgnoreCase(participantName+".Client")) {
							removeUserCreatedBySSM("CLIENT", participantName+".Client", accmgrClient);
//						}
//					}
//				}
        	tempParticipantList.remove(tempPartName);
			}
			log.info("Rolled back the users created :: participantList :: " + participantList+" unsuccessful roll back participants : " + tempParticipantList);
		}
	}
	
	
	public void createAggregatorHierarchy(AggregatorEnrollmentData aggEnrollmentData) {
//		List<String> aggProgramList = aggEnrollmentData.getProgramList();
		
		String aggName = aggEnrollmentData.getCustomerName();
		String programName = null;
		String childPartName = null;
		Participant participant = getParticipant(aggName);
		Participant childProgPart = null;
		List<String> aggPrograms = new ArrayList<String>();
		ProgramParticipant programParticipant = null;
		Set<ProgramParticipant> programParticipantSet = participant.getProgramParticipants();
		Iterator<ProgramParticipant> iter = programParticipantSet.iterator();
		Map<String, ProgramParticipant> parentPPMap = new HashMap<String, ProgramParticipant>();
		
		while (iter.hasNext()) {
			programParticipant = iter.next();
			programName = programParticipant.getProgramName();
			parentPPMap.put(programName, programParticipant);
			aggPrograms.add(programName);
		}
        final ProgramParticipantManager programManager = EJBFactory
                .getBean(ProgramParticipantManager.class);
        final ProgramParticipantAggregationManager aggManager = 
            	EJBFactory.getBean(ProgramParticipantAggregationManager.class);		
//		String aggName = aggEnrollmentData.getCustomerName();
		List<CustomerEnrollmentData> childs = aggEnrollmentData.getChilds();
		for(CustomerEnrollmentData chData : childs){
			childPartName = chData.getParticipantName();
			childProgPart = getParticipant(childPartName);
			List<String> commonPrograms = new ArrayList<String>(aggPrograms);
			commonPrograms.retainAll(chData.getProgramList());
			createAggregatorHierarchyForProgram(commonPrograms, parentPPMap, childProgPart, programManager, aggManager);
		}
	}
	private void createAggregatorHierarchyForProgram(List<String> programs,Map<String, ProgramParticipant> parentPPMap , Participant childProgPart, ProgramParticipantManager programManager, ProgramParticipantAggregationManager aggManager) {
		ProgramParticipant parentPP = null;
		ProgramParticipant childprogPart = null;
		String childPrgName = null;
		ProgramParticipant childPP = null ;
		Map<String, ProgramParticipant> childPPMap = new HashMap<String, ProgramParticipant>();
		Set<ProgramParticipant> programParts = childProgPart.getProgramParticipants();
		Iterator<ProgramParticipant> iter = programParts.iterator();
		while (iter.hasNext()) {
			childprogPart = iter.next();
			childPrgName = childprogPart.getProgramName();
			childPPMap.put(childPrgName, childprogPart);
		}

		for(String programName : programs){
			parentPP = parentPPMap.get(programName);
			childPP = childPPMap.get(programName);
//			childPP = programManager.getProgramParticipant(programName, childName, false);
			if (parentPP == null || childPP == null) {
				continue;
			}
			/*if (aggManager.isDescendant(childPP, parentPP)) {
				// Need to break link before adding to
				aggManager.removeParent(parentPP);
				parentPP = programManager.getProgramParticipant(programName, aggName, false);
				//childPP = programManager.getProgramParticipant(programName, childName, false);
			} */
			aggManager.addChild(parentPP, childPP);
		}
	}
	
	private void removeUserCreatedBySSM(String clientOrPart, String name, AccMgrWSClient accmgrClient) {
		User user = null;
		try {
			user = accmgrClient.getAccmgr().getUserByName(clientOrPart, name);
		} catch (Exception ex) {
			user = null;
//			log.error("Failed to lookup user in account mgr prior to delete : Name : "+ name);
		}
		if (user != null) {
			accmgrClient.getAccmgr().removeUser(user.getId());
		}
	}
	
	public Map<String,ParticipantClientListFor20Ob>getParticipantCLientListFor20Ob(ArrayList<String> clientName) {
		return participantEAO.getParticipantCLientListFor20Ob(clientName);
	}

}