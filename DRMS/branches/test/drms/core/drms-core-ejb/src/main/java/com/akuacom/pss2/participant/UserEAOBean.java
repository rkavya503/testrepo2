/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccessBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.EventParticipation;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.util.EventState;

/**
 * Stateless session bean that acts as a EAO layer for Participant access.
 */
@SuppressWarnings({ "unchecked" })
@Stateless
public class UserEAOBean extends ParticipantGenEAOBean implements UserEAO.R,
        UserEAO.L {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(UserEAOBean.class);

    /** Participant/Client Pagination page Size */
    private static final int PAGESIZE = 20;

    public UserEAOBean() {
        super(Participant.class);
    }

    /**
     * @get AllPart/ClientSetSize()
     */
    public int getUserDataSetSize(boolean isClient) {
        final Query countQuery = em.createNamedQuery("Participant.findCount")
                .setParameter("client", isClient);
        return ((Long) countQuery.getSingleResult()).intValue();
    }

    public List<Participant> getUserPage(int start, int pageSize,
            String filter, String filterByColumn, String sort, boolean isClient) {

        Query query = null;

        if (filterByColumn.equals("pName")) {
            filterByColumn = "p.participantName";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterNameAndSort");
            query.setParameter("filter", filter + "%");
        } else if (filterByColumn.equals("pAccount")) {
            filterByColumn = "p.accountNumber";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterAccountAndSort");
            query.setParameter("filter", filter + "%");
        } else if (filterByColumn.equals("pProgram")) {
            filterByColumn = "p.programParticipants.programName";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterProgramAndSort");
            query.setParameter("programName", filter + "%");
        } else if (filterByColumn.equals("cName")) {
            filterByColumn = "p.participantName";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterNameAndSort");
            query.setParameter("filter", filter + "%");
        } else if (filterByColumn.equals("cAccount")) {
            filterByColumn = "p.accountNumber";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterAccountAndSort");
            query.setParameter("filter", filter + "%");
        } else if (filterByColumn.equals("cParticipantName")) {
            filterByColumn = "p.parent";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterParentAndSort");
            query.setParameter("filter", filter + "%");
        }

        else if (filterByColumn.equals("cProgram")) {
            filterByColumn = "p.programParticipants.programName";
            query = em
                    .createNamedQuery("Participant.findClientByPageByFilterProgramAndSort");
            query.setParameter("programName", filter + "%");
        }

        else if (filterByColumn.equals("cStatus")) {
            filterByColumn = "p.status";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterStatusAndSort");
            
            int status = 0;
            if (filter.equals("2")){
                status = 2;
            }
            else if (filter.equals("-1")){
                status = -1;
            }

            //query.setParameter("status", Integer.parseInt(filter));
            query.setParameter("status", status);
        }

        else if (filterByColumn.equals("cType")) {
            filterByColumn = "p.type";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterTypeAndSort");
            java.lang.Byte type = 0;
            if (filter.equals("2")){
                type = 2;
            }
            else if (filter.equals("-1")){
                type = -1;
            }
            query.setParameter("filterType", type);
        } else {
            filterByColumn = "p.participantName";
            query = em
                    .createNamedQuery("Participant.findParticipantByPageByFilterNameAndSort");
            query.setParameter("filter", filter + "%");
        }

        // query.setParameter("filterColumn", filterByColumn );
        query.setParameter("client", isClient);

        if (sort != null && (!sort.equalsIgnoreCase("")))
            query.setParameter("sort", "p." + sort);
        else
            query.setParameter("sort", "p.participantName");

        /** JPA pagination mechanism */
        // log.error("Start: " + start + "++++ End"+ (pageSize + start));
        query.setFirstResult(start);
        query.setMaxResults(pageSize);

        List<Participant> participantList = query.getResultList();

        for (Participant p : participantList) {
            p.getEventParticipants().size();
            p.getProgramParticipants().size();
        }

        return participantList;
    }

    @Override
    public boolean checkAccount(String accountNumber) {
        boolean result = false;

        final Query query = em.createNamedQuery("Participant.findByAccount")
                .setParameter("account", accountNumber);
        List list = query.getResultList();
        if (list != null && list.size() != 0) {
            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.core.DataAccess#getParticipantByAccount(java.lang.String
     * )
     */
    public Participant getByAccount(String accountNumber, boolean isClient) {
        if (accountNumber == null) {
            String message = "participant account number is null";
            throw new ValidationException(message);
        }

        final Query query = em.createNamedQuery("Participant.findByAccount")
                .setParameter("account", accountNumber);
        try {
            final Participant dao = (Participant) query.getSingleResult();
            return dao;
        } catch (Exception e) {
            throw new ValidationException(
                    "can't find participant with account number "
                            + accountNumber);
        }
    }
    public Participant getParticipantByClient(String participantName){
    	final Query query = em.createNamedQuery("Participant.getParticipantByClient")
            .setParameter("participantName", participantName);
        final Participant dao = (Participant) query.getSingleResult();
        return dao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getParticipants(int)
     */
    public List<Participant> getByUserType(boolean isClient) {
        // final Query namedQuery = em.createNamedQuery("Participant.findAll");
        final Query namedQuery = em.createNamedQuery(
                "Participant.findAllByClient").setParameter("client", isClient);
        List<Participant> res = new ArrayList<Participant>();
        res = namedQuery.getResultList();

        for (Participant p : res) {
            p.getEventParticipants().size();
            p.getProgramParticipants().size();
        }

        return res;
    }

    public List<Participant> getAll() {
        return em.createNamedQuery("Participant.findAll").getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#updateParticipant(java.lang.String,
     * com.akuacom.pss2.core.model.Participant)
     */
    // TODO 2992
    public void updateParticipant(String participantName,
            Participant participant) {
    	updateParticipant(participant);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#updateParticipant(java.lang.String,
     * com.akuacom.pss2.core.model.Participant)
     */
    // TODO 2992
    public void updateParticipant(Participant participant) {
    	em.merge(participant);
    }

    // TODO 2992
    public void updateParticipant(ParticipantPerf participant) {
        em.merge(participant);
    }
    
    public void remove(String participantName, boolean isClient) {
        if (participantName == null) {
            String message = "participant name is null";
            throw new EJBException(message);
        }

        try {
            Participant p = findByNameAndClient(participantName, isClient);
            if (p == null) {
                String message = "participant " + participantName
                        + " doesn't exist";
                throw new EJBException(message);
            }
            em.remove(p);
        } catch (Exception ex) {
            String message = "error removing participant " + participantName;
            throw new EJBException(message, ex);
        }
    }

    protected EventParticipation getEventParticipantion(Participant p,
            String reason) {
        final EventParticipation ep = new EventParticipation();
        ep.setProgramName("");
        ep.setEventName("");
        ep.setParticipantName(p.getParticipantName(), p.isClient());
        ep.setAccountId(p.getAccountNumber());
        ep.setEntryTime(new Date());
        ep.setType(p.getTypeString());
        ep.setReason(reason);
        return ep;
    }

    protected EventParticipation getEventParticipantion(ParticipantPerf p,
            String reason, boolean blnGetEventData) {
        Participant participant = new Participant();
        participant.setAccountNumber(p.getAccountNumber());
        participant.setParticipantName(p.getParticipantName());
        participant.setClient(p.isClient());
        participant.setType(p.getType());

        return getEventParticipantion(participant, reason, true);
    }

    protected EventParticipation getEventParticipantion(Participant p,
            String reason, boolean blnGetEventData) {
        if (!blnGetEventData) {
            return getEventParticipantion(p, reason);
        } else {
            final EventParticipation ep = new EventParticipation();

            // Set event name
            List<EventParticipant> list = findEventParticipants(
                    p.getParticipantName(), p.isClient());
            if (list.size() == 0) {
                ep.setEventName("");
                ep.setProgramName("");
            } else {
                // Old design is that client is take part in one event
                ep.setProgramName(list.get(0).getEvent().getProgramName());
                ep.setEventName(list.get(0).getEvent().getEventName());
            }

            ep.setParticipantName(p.getParticipantName(), p.isClient());
            ep.setAccountId(p.getAccountNumber());
            ep.setEntryTime(new Date());
            ep.setType(p.getTypeString());
            ep.setReason(reason);
            return ep;
        }
    }

    protected String getReason(ClientStatus status) throws Exception {
        String reason;
        if (status == ClientStatus.ONLINE) {
            reason = "ONLINE";
        } else if (status == ClientStatus.OFFLINE) {
            reason = "OFFLINE";
        } else if (status == ClientStatus.ERROR) {
            reason = "ERROR";
        } else {
            reason = "UNKNOWN";
        }
        return reason;
    }

    public List<Participant> findParticipantsByAccounts(List<String> accountIDs) {
        try {
            Query namedQuery = em
                    .createNamedQuery("Participant.findByAccounts");
            namedQuery.setParameter("accounts", accountIDs);
            return namedQuery.getResultList();
        } catch (Exception ex) {
            throw new EJBException("", ex);
        }

    }

    public List<Participant> findParticipantsByProgramName(String progName,
            boolean isClient) throws AppServiceException {
        try {
            Query namedQuery = em
                    .createNamedQuery("ProgramParticipant.findByProgramAndType")
                    .setParameter("programName", progName)
                    .setParameter("client", isClient);
            List<ProgramParticipant> list = namedQuery.getResultList();
            List<Participant> results = new ArrayList<Participant>();
            for (ProgramParticipant pp : list) {
                results.add(pp.getParticipant());
            }
            return results;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    public List<Participant> findUsersByProgramName(String progName)
            throws AppServiceException {
        try {
            Query namedQuery = em.createNamedQuery(
                    "ProgramParticipant.findByProgram").setParameter(
                    "programName", progName);
            List<ProgramParticipant> list = namedQuery.getResultList();
            List<Participant> results = new ArrayList<Participant>();
            for (ProgramParticipant pp : list) {
                results.add(pp.getParticipant());
            }
            return results;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    // TODO 3118
    private SearchHandler doSearch(SearchHandler searchHandler,
            String querySuffix, List<Object> values)
            throws EntityNotFoundException {
        if (searchHandler == null) {
            searchHandler = new SearchHandler();
        }
        // TODO: when participantEAO extends BaseEAO, we can get rid of static
        String query = "SELECT p" + querySuffix;
        String queryCount = "SELECT count(p)" + querySuffix;
        searchHandler = BaseEAOBean.search(searchHandler, query, queryCount,
                values, em);

        // TODO: get rid of lazy load
        if (searchHandler != null) {
            for (Object part : searchHandler.getResults()) {
                ((Participant) part).getProgramParticipants().size();
            }
        }

        return searchHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#searchParticipants(java.
     * util.List, java.util.List, com.akuacom.ejb.search.SearchHandler)
     */
    // TODO 3118
    public SearchHandler searchParticipants(List<String> programNames,
            List<String> accountIDs, SearchHandler searchHandler)
            throws AppServiceException {
        try {
            StringBuilder query = new StringBuilder(
                    " FROM Participant p where 0=0 ");
            List<Object> values = new ArrayList<Object>();
            
            // 0=0 is here so we can always append " and "
            if (programNames != null && programNames.size() > 0) {
                values.add(new Integer(ProgramParticipant.PROGRAM_PART_ACTIVE));
                query.append(" and p.participantName in ");
                query.append("(select pp.participant.participantName");
                query.append(" from ProgramParticipant pp where pp.state = ?1 and (");
                int i = 0;
                for (String programName : programNames) {
                    if (i > 0) {
                        query.append(" or ");
                    }
                    query.append(" pp.programName = ?");
                    query.append(i + 2);
                    values.add(programName);
                    i++;
                }
                query.append("))");
            }

            if (accountIDs != null && accountIDs.size() > 0) {
                if (accountIDs.size() == 1 && (accountIDs.get(0) == null)) {
                    // 0=1 is here so we can always append " or "
                    query.append(" and 1=0 ");
                } else {
                    query.append(" and p.account in ( ");
                    int i = 0;
                    for (String account : accountIDs) {
                        if (i > 0) {
                            query.append(" , ");
                        }
                        query.append("'");
                        query.append(account);
                        query.append("'");
                        i++;
                    }
                    query.append(")");
                }

            }

            if (searchHandler != null) {
                searchHandler.setSortField("p.participantName");
            }

            return doSearch(searchHandler, query.toString(), values);
        } catch (EntityNotFoundException ex) {
            throw new AppServiceException("", ex);
        }
    }

    /**
     * Parses the signal string - PARTIAL IMPLEMENTATION.
     * 
     * @param eventState
     *            the event state
     * @param signalName
     *            the signal name
     * 
     * @return the string
     */
    public String parseSignalString(EventState eventState, String signalName) {
        StringBuilder rv = new StringBuilder();
        if (signalName.equals("pending")) {
            rv.append(eventState.getEventStatus().toString());
        } else if (signalName.equals("mode")) {
            rv.append(eventState.getOperationModeValue().toString());
        } else {
            // TODO: parse out the smart client signals
            // for(:)
        }
        if (eventState.isManualControl()) {
            rv.append("(man)");
        }
        return rv.toString();
    }

    /**
     * Gets the new signals in program.
     * 
     * @param programName
     *            the program name
     */
    public List<ProgramSignal> getNewSignalsInProgram(String programName) {
        Program program = EJBFactory.getBean(ProgramManager.class).getProgramWithSignals(
                programName);
        if (program == null) {
            return null;
        }
        List<ProgramSignal> signals = new ArrayList<ProgramSignal>();

        for (ProgramSignal signal : program.getSignals()) {
            boolean found = false;
            for (ProgramSignal newSignal : signals) {
                if (newSignal.getSignalDef().getSignalName()
                        .equals(signal.getSignalDef().getSignalName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                signals.add(signal);
            }
        }

        return signals;
    }

    /**
     * Gets the default signal state.
     * 
     * @param signalName
     *            the signal name
     * 
     * @return the default signal state
     */
    public String getDefaultSignalValueAsString(String signalName) {
        // if a program wasn't driving the signal, return the default
        final SignalDef signalDef = EJBFactory.getBean(SignalManager.class)
                .getSignal(signalName);
        if (signalDef.isLevelSignal()) {
            return signalDef.getLevelDefault();
        } else {
            return Double.toString(signalDef.getNumberDefault());
        }
    }

    @Override
    public List<EventParticipant> findCurrentEventOptouts(String participantName, boolean isClient) {
        try {
            Query query = em
                    .createNamedQuery(
                            "EventParticipant.findByParticipantAndTypeAndOptOut")
                    .setParameter("participantName", participantName)
                    .setParameter("client", isClient);
            List<EventParticipant> list = query.getResultList();
            /*for (EventParticipant ep : list) {
                ep.getSignalEntries().size();
            }*/
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
    }
    
    public List<EventParticipant> findEventParticipants(String participantName,
            boolean isClient) {
        try {
            Query query = em
                    .createNamedQuery(
                            "EventParticipant.findByParticipantAndType")
                    .setParameter("participantName", participantName)
                    .setParameter("client", isClient);
            List<EventParticipant> list = query.getResultList();
            /*for (EventParticipant ep : list) {
                ep.getSignalEntries().size();
            }*/
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public List<EventParticipant> findEventParticipantsByPartUUID(String uuid) {
        try {
            Query query = em.createNamedQuery(
                    "EventParticipant.findByParticipantUUID").setParameter(
                    "uuid", uuid);
            List<EventParticipant> list = query.getResultList();
            /*for (EventParticipant ep : list) {
                ep.getSignalEntries().size();
            }*/
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public List<EventParticipant> findEventParticipants(List<String> names,
            boolean isClient) {
        List<EventParticipant> list = new ArrayList<EventParticipant>();
        if (names == null || names.size() == 0) {
            return list;
        }
        try {
            Query query = em
                    .createNamedQuery(
                            "EventParticipant.findByParticipantByPartNames")
                    .setParameter("participantNames", names)
                    .setParameter("client", isClient);
            list = query.getResultList();
           /* for (EventParticipant ep : list) {
                ep.getSignalEntries().size();
            }*/
            return list;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public List<String> findProgramNamesByName(String partName, boolean isClient) {
        List<ProgramParticipant> ppList;
        try {
            Query query = em
                    .createNamedQuery("ProgramParticipant.findByParticipant");
            query.setParameter("participantName", partName).setParameter(
                    "client", isClient);
            ppList = query.getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_GET: " + partName
                    + " | " + e.getMessage(), e);
        }

        try {
            List<String> newList = new ArrayList<String>();
            for (ProgramParticipant pp : ppList) {
                newList.add(pp.getProgramName());
            }

            return newList;
        } catch (Exception ex) {
            throw new EJBException("", ex);
        }
    }

    public List<ProgramParticipant> findAllProgramParticipants(String name,
            boolean isClient) {
        try {
            Query query = em
                    .createNamedQuery("ProgramParticipant.findAllByClient");
            query.setParameter("participantName", name).setParameter("client",
                    isClient);
            return query.getResultList();
        } catch (Exception e) {
            throw new EJBException(
                    "ERROR_PROGRAM_PARTICIPANT_GET_ALL_FOR_CLIENT: " + name
                            + " | " + e.getMessage(), e);
        }
    }

    public List<ProgramParticipant> findAllProgramParticipantsConfig(
            String name, boolean isClient) {
        try {

            Query query = em
                    .createNamedQuery("ProgramParticipant.findByProgramConfig");
            query.setParameter("participantName", name);
            return query.getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_GET_ALL_CONFIG: "
                    + name + " | " + e.getMessage(), e);
        }
    }

    public Participant findByName(String name, boolean isClient) {
        Participant part;
        try {
            Query query = em.createNamedQuery("Participant.findByName.Single");
            query.setParameter("name", name).setParameter("client", isClient);
            part = (Participant) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            String message = "error getting participant";
            throw new EJBException(message, ex);
        }

        return part;
    }

    @Override
    public long countByParticipantNameAndClient(String participantName,
            boolean client) {
        return (Long) em
                .createNamedQuery("Participant.countByParticipantNameAndClient")
                .setParameter("participantName", participantName)
                .setParameter("client", client).getSingleResult();
    }

    public boolean existsQ(String participantName, boolean client) {
        return countByParticipantNameAndClient(participantName, client) > 0;
    }
    
    
    public List<Participant> findParticipantsBySecondaryAccounts(List<String> accountIDs) {
        try {
            Query namedQuery = em
                    .createNamedQuery("Participant.findBySecondaryAccounts");
            namedQuery.setParameter("accounts", accountIDs);
            return namedQuery.getResultList();
        } catch (Exception ex) {
            throw new EJBException("", ex);
        }

    }
}