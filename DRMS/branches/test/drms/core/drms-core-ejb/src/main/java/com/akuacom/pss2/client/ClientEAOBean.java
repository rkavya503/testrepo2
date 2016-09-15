/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccessBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantESPerf;
import com.akuacom.pss2.participant.UserEAOBean;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.report.entities.EventParticipation;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;



/**
 * Stateless session bean that acts as a EAO layer for Participant access.
 */
@SuppressWarnings({ "unchecked" })
@Stateless
public class ClientEAOBean extends UserEAOBean implements ClientEAO.R,
        ClientEAO.L {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ClientEAOBean.class);

    @EJB
    SignalManager.L signalManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#createClient(com.akuacom.pss2.core.model
     * .Participant, char[])
     */
    public Participant createClient(Participant client) {
        if (client == null) {
            String message = "participant is null";
            throw new EJBException(message);
        }
        if (!client.isClient()) {
            String message = "participant is not a client";
            throw new EJBException(message);
        }
        if (client.getParent() == null) {
            String message = "client has no parent";
            throw new EJBException(message);
        }

        Participant parent = findByNameAndClient(client.getParent(), false);
        if (parent == null) {
            String message = "ERROR_PARTICIPANT_DOES_NOT_EXIST";
            List<String> parameters = new ArrayList<String>();
            parameters.add(client.getParticipantName());
            ValidationException validationException = new ValidationException(
                    message, parameters);
            throw validationException;
        }

        client.setAccountNumber(client.getParticipantName());
        try {
            Participant o = findByNameAndClient(client.getParticipantName(), true);
            if (o != null) {
                String message = "ERROR_CREATE_CLIENT_ALREADY_EXIST";

                List<String> parameters = new ArrayList<String>();
                parameters.add(client.getParticipantName());
                ValidationException validationException = new ValidationException(
                        message, parameters);
                throw validationException;
            }

            final Query query = em
                    .createNamedQuery("Participant.findByAccount")
                    .setParameter("account", client.getAccountNumber());
            Participant o1 = null;
            try {
                o1 = (Participant) query.getSingleResult();
            } catch (NoResultException e) {
                // ignore
            }
            if (o1 != null) {
                String message = "ERROR_CREATE_CLIENT_ALREADY_EXIST";
                List<String> parameters = new ArrayList<String>();
                parameters.add(client.getParticipantName());
                ValidationException validationException = new ValidationException(
                        message, parameters);
                throw validationException;
            }

            return create(client);

        } catch (ValidationException e) {
            throw e;
        } catch (Exception ex) {
            String message = "error creating client " + client.getParticipantName();
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getParticipant(java.lang.String)
     */
    public Participant getClient(String name) {
        return findByNameAndClient(name, true);
    }

    
	public ParticipantESPerf getClientESPerf(String name) {
		Query query = em.createNamedQuery("ParticipantESPerf.findByName");
		query.setParameter("name", name).setParameter("client", true);
		return (ParticipantESPerf)query.getSingleResult();
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#getParticipantByAccount(java.lang.String
     * )
     */
    public Participant getClientByAccount(String accountNumber) {
        return getByAccount(accountNumber, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getClients()
     */
    public List<String> getClients() {
        return getClients(Participant.SORT_NAME);
    }

    public SearchHandler searchClientsByParticipant(
            SearchHandler searchHandler, String participantName,
            boolean isClient) {
        try {
            String query = "SELECT p FROM Participant p where 0=0 and p.client = true and p.parent = ?1 order by p.participantName";
            String queryCount = "SELECT count(p) FROM Participant p where 0=0 and p.client = true and p.parent = ?1 order by p.participantName";
            List values = new ArrayList();
            values.add(participantName);

            if (searchHandler == null) {
                searchHandler = new SearchHandler();
            }
            searchHandler = BaseEAOBean.search(searchHandler, query,
                    queryCount, values, em);

            return searchHandler;
        } catch (EntityNotFoundException ex) {
            return searchHandler;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getClients(int)
     */
    public List<String> getClients(int sort) {
        List<Participant> list = getAllClients();
        final List<String> results = new ArrayList<String>();
        for (Object aList : list) {
            Participant p = (Participant) aList;
            results.add(p.getParticipantName());
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getClients(int)
     */
    public List<Participant> getAllClients() {
        return getByUserType(true);
    }

    public List<String> getClientNamesByParticipant(String partName) {
        Query namedQuery = em.createNamedQuery(
                "Participant.findClientNamesByParticipant").setParameter(
                "parentName", partName);
        return (List<String>) namedQuery.getResultList();
    }

    public List<Participant> getClientsByParticipant(String partName) {
        Query namedQuery = em.createNamedQuery(
                "Participant.findClientsByParticipant").setParameter(
                "parentName", partName);
		List<Participant> res = (List<Participant>) namedQuery.getResultList();
		
		for (Participant p : res) {
			p.getProgramParticipants().size();
			p.getEventParticipants().size();
		}
		
		return res;

        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#removeParticipant(java.lang.String)
     */
    public void removeClient(String name) {
        remove(name, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#getParticipantLastPrice(java.lang.String
     * )
     */
    public double getClientLastPrice(String name) {

        try {
            final Participant p = findByNameAndClient(name, true);
            return p.getLastPrice();
        } catch (Exception e) {
            String message = "error getting last price for participant " + name;
            throw new EJBException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#setParticipantLastPrice(java.lang.String
     * , double)
     */
    public void setClientLastPrice(String name, double lastPrice) {
        try {
            final Participant p = findByNameAndClient(name, true);
            p.setLastPrice(lastPrice);
            em.merge(p);
        } catch (Exception ex) {
            String message = "error setting last price for participant " + name;
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#getParticipantStatus(java.lang.String)
     */
    public ClientStatus getClientStatus(String name) {
        try {
            return ClientStatus.valueOf(findByNameAndClient(name, true).getStatus());
        } catch (Exception ex) {
            String message = "error getting status for participant " + name;
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#setParticipantStatus(java.lang.String,
     * com.akuacom.pss2.core.model.ClientStatus)
     */
    public void setClientStatus(String name, ClientStatus status) {
        try {
            final Participant p = findByNameAndClient(name, true);
            final byte b = (byte) status.ordinal();
            if (b != p.getStatus()) {
                // 07.19.2010 JerryM DRMS-1106: get event data for client
                // final EventParticipation ep = getEventParticipantion(p,
                // getReason(status));
                final EventParticipation ep = getEventParticipantion(p,
                        getReason(status), true);
            }
            p.setStatus((int) b);
            em.merge(p);
        } catch (Exception ex) {
            String message = "error setting status for participant " + name;
            throw new EJBException(message, ex);
        }
    }

    public List<Participant> findClientsByAccounts(List<String> accountIDs) {
        return findParticipantsByAccounts(accountIDs);
    }

    public List<Participant> findClientsByProgramName(String progName)
            throws AppServiceException {
        return findParticipantsByProgramName(progName, true);
    }

    public Participant getClientByName(String name) {
        return findByNameAndClientLJF(name, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.akuacom.program.services.ProgramServicer#
     * findParticipantsForUpdateStatusLoop()
     */
    public List<Participant> findClientsForUpdateStatusLoop() {
        List<Participant> parts;
        try {
            Query query = em.createNamedQuery("Participant.findClientBefore");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -3);
            query.setParameter("time", cal.getTime());
            parts = query.getResultList();
        } catch (Exception ex) {
            String message = "error getting comm log list";
            throw new EJBException(message, ex);
        }

        return parts;
    }

     public List<EventParticipant> findEventParticipants(String name) {
        return findEventParticipants(name, true);
    }

    public List<EventParticipant> findEventParticipants(List<String> names) {
        return findEventParticipants(names, true);
    }

    public List<ProgramParticipant> findAllProgramParticipantsForClient(
            String clientName) {
        return findAllProgramParticipants(clientName, true);
    }

    

    public void removeClientFromAllEvents(String name) {
        try {
            if (name == null) {
                String message = "participant name is null";
                throw new EJBException(message);
            }

            for (EventParticipant ep : (List<EventParticipant>)em
                    .createNamedQuery("EventParticipant.findByParticipant")
                    .setParameter("participantName", name)
                    .setParameter("client", true)) {
                em.remove(ep);
            }
        } catch (Exception ex) {
            String message = "error removing pariticpant " + name;
            throw new EJBException(message, ex);
        }
    }

    /*
     * private Participant findPart(String participantName) { return
     * clientEAO.findByName(participantName, false); }
     */

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getClientManualSignalState(java
     * .lang.String, java.lang.String, java.util.List)
     */
    public String getClientManualSignalValueAsString(Participant participant,
            String signalName) {
        Set<ClientManualSignal> signalStates = participant.getManualSignals();
        if (signalStates != null) {
            for (ClientManualSignal signalState : signalStates) {
                if (signalState != null) {
                    if (signalState.getName().equals(signalName)) {
                        return signalState.getValue();
                    }
                }
            }
        }

        SignalDef signalDef = signalManager.getSignal(signalName);
        if (signalDef.isLevelSignal()) {
            return signalDef.getLevelDefault();
        } else {
            return Double.toString(signalDef.getNumberDefault());
        }
    }

    public long countParticipants() {
        return (Long) em.createNamedQuery("Participant.countAll")
                .getSingleResult();
    }

    
	public List<Participant> getParticipants(List<String> clientNames,
			List<String> participantNames, List<String> programNames,
			List<String> clientTypes, List<String> clientStatus, Integer startPage, Integer pageSize, 
			String orderBy, String order) {
		
		Query query = null;

		String queryString = "SELECT DISTINCT p FROM Participant p LEFT JOIN p.programParticipants pp WHERE p.client=1";
		if (clientNames != null) {
			for (String clientName : clientNames) {
				queryString = queryString + " AND p.participantName like '" + clientName + "%' ";
			}
		}
		
		if (participantNames != null) {
			for (String partName : participantNames) {
				queryString = queryString + " AND p.parent like '" + partName + "%' ";
			}
		}

		if (programNames != null) {
			for (String programName : programNames) {
				//queryString = queryString + " and pp.program.programName like '" + programName + "%' ";
				queryString = queryString + " AND EXISTS (SELECT pp FROM p.programParticipants pp WHERE pp.program.programName LIKE '" + programName + "%' AND pp.state = 1) ";
			}
		}

		if (clientTypes != null) {
			for (String clientType : clientTypes) {
				Byte type = 0;
	            if (clientType.equals("2")){
	            	type = 2;
	            }
	            else if (clientType.equals("-1")){
	            	type = -1;
	            }
				queryString = queryString + " AND p.type = " + type;
			}
		}
		
		if (clientStatus != null) {
			for (String cStatus : clientStatus) {
		      Integer status = 0;
		      if (cStatus.equals("2")){
		          status = 2;
		      }
		      else if (cStatus.equals("-1")){
		          status = -1;
		      }
				queryString = queryString + " and p.status = "	+ status;
			}
		}

		if(orderBy != null && !orderBy.equals("")){
			queryString = queryString + " ORDER BY " + orderBy;
		}
		if(order != null && !order.equals("")){
			queryString = queryString + " " + order;
		}

		if(queryString.contains("_")){
			queryString = queryString.replace("_", "\\_");
		}

		query = em.createQuery(queryString);
		query.setFirstResult(startPage);
		query.setMaxResults(pageSize);
		final List<Participant> participantList = query.getResultList();
		
		for (Participant p : participantList) {
			if (p == null) {
				continue;
			}
			if(p.isManualControl() && p.getManualSignals()!=null) p.getManualSignals().size();
			if (p.getProgramParticipants() != null) {
				p.getProgramParticipants().size();
			}
			if (p.getEventParticipants() != null) {
				p.getEventParticipants().size();
				for(EventParticipant pp:p.getEventParticipants()){
					if(pp.getEvent()!=null && pp.getEvent().getEventSignals()!=null) pp.getEvent().getEventSignals().size();
					if(pp.getSignalEntries()!=null) pp.getSignalEntries().size();
				}
			}
		}
		return participantList;		
		
	}
	
	@Override
	public List<Participant> getParticipants(String filterContent,
			String filterColumn, Integer startPage, Integer pageSize,
			String orderBy, String order) {

		Query query = null;

		String queryString = "SELECT DISTINCT p FROM Participant p " +
                "LEFT JOIN FETCH p.contacts " +
                "JOIN p.programParticipants pp " +
                "WHERE p.client=true ";
	
		if(filterContent != null && filterColumn!= null && !filterContent.equals("") && !filterColumn.equals("")){
			if(filterColumn.equals("p.type") || filterColumn.equals("p.status")){
				queryString = queryString + " AND " + filterColumn + " = :filterContent";
			}
			else if(filterColumn.equals("pp.program.programName")){
				queryString = queryString + " AND EXISTS (SELECT pp FROM p.programParticipants pp WHERE pp.program.programName LIKE :filterContent AND pp.state = 1) ";
			}
			else{
				queryString = queryString + " AND " + filterColumn + " LIKE :filterContent";
			}
		}

		if(orderBy != null && !orderBy.equals("")){
			queryString = queryString + " ORDER BY " + orderBy;
		}
		else{
			queryString = queryString + " ORDER BY p.participantName";
		}
		if(order != null && !order.equals("")){
			queryString = queryString + " " + order;
		}
		else{
			queryString = queryString + " ASC";
		}

		if(queryString.contains("_")){
			queryString = queryString.replace("_", "\\_");
		}

		query = em.createQuery(queryString);
		
		if(filterContent != null && filterColumn!= null && !filterContent.equals("") && !filterColumn.equals("")){
			if(filterColumn.equals("p.type")){
	            Byte type = 0;
	            if (filterContent.equals("2")){
	                type = 2;
	            }
	            else if (filterContent.equals("-1")){
	                type = -1;
	            }
				query.setParameter("filterContent", type);	
			}
			else if(filterColumn.equals("p.status")){
	            Integer status = 0;
	            if (filterContent.equals("2")){
	                status = 2;
	            }
	            else if (filterContent.equals("-1")){
	                status = -1;
	            }
				query.setParameter("filterContent", status);	
			}
			else if(filterColumn.equals("p.eventStatus")){

				if(filterContent.equals(EventState.EVENT_STATUS_FAR)){
					query.setParameter("filterContent", EventStatus.FAR);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_NEAR)){
					query.setParameter("filterContent", EventStatus.NEAR);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_ACTIVE)){
					query.setParameter("filterContent", EventStatus.ACTIVE);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_NONE)){
					query.setParameter("filterContent", EventStatus.NONE);
				}
			}
			else if(filterColumn.equals("p.operationMode")){
				
				if(filterContent.equals(OperationModeValue.NORMAL + "")){
					query.setParameter("filterContent", OperationModeValue.NORMAL);
				}
				else if(filterContent.equals(OperationModeValue.MODERATE + "")){
					query.setParameter("filterContent", OperationModeValue.MODERATE);
				}
				else if(filterContent.equals(OperationModeValue.HIGH + "")){
					query.setParameter("filterContent", OperationModeValue.HIGH);
				}
				else if(filterContent.equals(OperationModeValue.SPECIAL + "")){
					query.setParameter("filterContent", OperationModeValue.SPECIAL);
				}
				else if(filterContent.equals(OperationModeValue.UNKNOWN + "")){
					query.setParameter("filterContent", OperationModeValue.UNKNOWN);
				}

			}
			else if(filterColumn.equals("pp.program.programName")) {
				query.setParameter("filterContent", filterContent + "%");
			}
			else{
				query.setParameter("filterContent", filterContent + "%");
			}
		}
		
		if(startPage != null){
			query.setFirstResult(startPage);			
		}
		if(pageSize != null){
			query.setMaxResults(pageSize);			
		}
		
		List<Participant> participantList = query.getResultList();
		
		List<String> nonManualControlIds = new ArrayList<String>(participantList.size());
		List<String> manualControlIds = new ArrayList<String>(participantList.size());
		for (Participant p : participantList) {
			if (p == null) {
				continue;
			}
			if(p.isManualControl()){
				manualControlIds.add(p.getUUID());
			}else{
				nonManualControlIds.add(p.getUUID());
			}
			//TODO
			if(p.isManualControl() && p.getManualSignals()!=null) p.getManualSignals().size();
			if (p.getProgramParticipants() != null) {
				p.getProgramParticipants().size();
			}
			if (p.getEventParticipants() != null) {
				p.getEventParticipants().size();
				for(EventParticipant pp:p.getEventParticipants()){
					if(pp.getEvent()!=null && pp.getEvent().getEventSignals()!=null) pp.getEvent().getEventSignals().size();
					if(pp.getSignalEntries()!=null) pp.getSignalEntries().size();
				}
			}
		}
		return participantList;
	}
    
	@Override
	public Integer getParticipantRowCount(String filterContent, String filterColumn) {
		Query query = null;

		String queryString = "SELECT DISTINCT p FROM Participant p " +
										"LEFT JOIN p.programParticipants pp " +
										"WHERE p.client=1 ";
		
		if(filterContent != null && filterColumn!= null && !filterContent.equals("") && !filterColumn.equals("")){
			if(filterColumn.equals("p.type") || filterColumn.equals("p.status") || 
				filterColumn.equals("p.eventStatus") || filterColumn.equals("p.operationMode")){
				
				queryString = queryString + "AND " + filterColumn + " = :filterContent";
			}
			else if(filterColumn.equals("pp.program.programName")){
				queryString = queryString + " AND EXISTS (SELECT pp FROM p.programParticipants pp WHERE pp.program.programName LIKE :filterContent AND pp.state = 1) ";
			}			
			else{
				queryString = queryString + "AND " + filterColumn + " LIKE :filterContent";									
			}
		}
		
		if(queryString.contains("_")){
			queryString = queryString.replace("_", "\\_");
		}

		query = em.createQuery(queryString);
		
		if(filterContent != null && filterColumn!= null && !filterContent.equals("") && !filterColumn.equals("")){
			if(filterColumn.equals("p.type")){
	            Byte type = 0;
	            if (filterContent.equals("2")){
	                type = 2;
	            }
	            else if (filterContent.equals("-1")){
	                type = -1;
	            }
				query.setParameter("filterContent", type);	
			}
			else if(filterColumn.equals("p.status")){
	            Integer status = 0;
	            if (filterContent.equals("2")){
	                status = 2;
	            }
	            else if (filterContent.equals("-1")){
	                status = -1;
	            }
				query.setParameter("filterContent", status);	
			}
			else if(filterColumn.equals("p.eventStatus")){

				if(filterContent.equals(EventState.EVENT_STATUS_FAR)){
					query.setParameter("filterContent", EventStatus.FAR);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_NEAR)){
					query.setParameter("filterContent", EventStatus.NEAR);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_ACTIVE)){
					query.setParameter("filterContent", EventStatus.ACTIVE);
				}
				else if(filterContent.equals(EventState.EVENT_STATUS_NONE)){
					query.setParameter("filterContent", EventStatus.NONE);
				}
			}
			else if(filterColumn.equals("p.operationMode")){
				
				if(filterContent.equals(OperationModeValue.NORMAL + "")){
					query.setParameter("filterContent", OperationModeValue.NORMAL);
				}
				else if(filterContent.equals(OperationModeValue.MODERATE + "")){
					query.setParameter("filterContent", OperationModeValue.MODERATE);
				}
				else if(filterContent.equals(OperationModeValue.HIGH + "")){
					query.setParameter("filterContent", OperationModeValue.HIGH);
				}
				else if(filterContent.equals(OperationModeValue.SPECIAL + "")){
					query.setParameter("filterContent", OperationModeValue.SPECIAL);
				}
				else if(filterContent.equals(OperationModeValue.UNKNOWN + "")){
					query.setParameter("filterContent", OperationModeValue.UNKNOWN);
				}

			}
			else if(filterColumn.equals("pp.program.programName")) {
				query.setParameter("filterContent", filterContent);
			}
			else{
				query.setParameter("filterContent", filterContent + "%");
			}
			
			
		}
		
		List<Participant> participantList = query.getResultList();
		return participantList.size();
	}
	
	@Override
	public Integer getParticipantRowCount(List<String> clientNames,
			List<String> participantNames, List<String> programNames,
			List<String> clientTypes, List<String> clientStatus){

		Query query = null;

		String queryString = "SELECT DISTINCT p FROM Participant p LEFT JOIN p.programParticipants pp WHERE p.client=1";
		if (clientNames != null) {
			for (String clientName : clientNames) {
				queryString = queryString + " AND p.participantName LIKE '" + clientName + "%' ";
			}
		}

		if (participantNames != null) {
			for (String partName : participantNames) {
				queryString = queryString + " AND p.parent LIKE '" + partName + "%' ";
			}
		}

		if (programNames != null) {
			for (String programName : programNames) {
				//queryString = queryString + " AND pp.program.programName LIKE '" + programName + "%' ";
				queryString = queryString + " AND EXISTS (SELECT pp FROM p.programParticipants pp WHERE pp.program.programName LIKE '" + programName + "%' AND pp.state = 1) ";
			}
		}

		if (clientTypes != null) {
			for (String clientType : clientTypes) {
				queryString = queryString + " AND p.type = " + clientType;
			}
		}

		if (clientStatus != null) {
			for (String status : clientStatus) {
				queryString = queryString + " AND p.status = "	+ status;
			}
		}
		if(queryString.contains("_")){
			queryString = queryString.replace("_", "\\_");
		}
		query = em.createQuery(queryString);
		List<Participant> participantList = query.getResultList();
		
		return participantList.size();
	}
	/**
	 * Override to load eventParticipants
	 */
	@Override
	public Participant findByNameAndClient(java.lang.String name, boolean client) {
		Query q = em.createNamedQuery( "Participant.findByNameAndClient.Single" );
		q.setParameter("name", name);
		q.setParameter("client", client);
		List<Participant> val = q.getResultList();
		if(val.isEmpty()) {
			return null;
		} else if (val.size() == 1) {
			val.get(0).getEventParticipants().size();
			return val.get(0);
		} else {
			throw new NonUniqueResultException(q.toString());
		}
	}

	@Override
	public List<Participant> getClientsAllInfoByParent(String parentName) {
		Query q = em.createNamedQuery("Participant.findClientAllInfoByParticipant");
		q.setParameter("parentName", parentName);
		List<Participant> val = q.getResultList();
		return val;
	}
	@Override
	public List<Participant> getClientsAllInfoExceptContactsByParent(String parentName) {
		Query q = em.createNamedQuery("Participant.findClientAllInfoExceptContactsByParticipant");
		q.setParameter("parentName", parentName);
		List<Participant> val = q.getResultList();
		return val;
	}
	
	/*
     * (non-Javadoc)
     */
    public Participant findByClientLJF(String name, boolean client) {
    	Query q = em.createNamedQuery( "Participant.findByClientName.Single" );
		q.setParameter("name", name);
		q.setParameter("client", client);
		List<Participant> val = q.getResultList();
		if(val.isEmpty()) {
			return null;
		} else if (val.size() == 1) {
			return val.get(0);
		} else {
			throw new NonUniqueResultException(q.toString());
		}
    }
	
}