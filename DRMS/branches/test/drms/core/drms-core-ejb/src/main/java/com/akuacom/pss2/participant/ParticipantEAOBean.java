/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccessBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.query.ParticipantClientListFor20Ob;
import com.akuacom.pss2.report.entities.EventParticipation;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;

/**
 * Stateless session bean that acts as a EAO layer for Participant access.
 */
@SuppressWarnings({ "unchecked" })
@Stateless
public class ParticipantEAOBean extends UserEAOBean implements
        ParticipantEAO.R, ParticipantEAO.L {

    /** The Constant log. */
    private static Logger log = Logger.getLogger(ParticipantEAOBean.class);
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#createParticipant(com.akuacom.pss2.core
     * .model.Participant, char[])
     */
    public Participant createParticipant(Participant p) {
        if (p == null) {
            String message = "participant is null";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            throw new EJBException(message);
        }

        if (p.getAccountNumber() == null || p.getAccountNumber().isEmpty()) {
            String message = "ERROR_ACCOUNT_NUMBER_EMPTY";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            throw new ValidationException(message);
        }

        String name = p.getParticipantName();
        try {
            Participant o = null;
            try {
                o = findByNameAndClient(name, p.isClient());
            }catch(NoResultException e) {
                
            }
            if (o != null) {
                String message = "ERROR_CREATE_PARTICIPANT_ALREADY_EXIST";

                List<String> parameters = new ArrayList<String>();
//              parameters.add(p.getParticipantName());
                String warning = "Participant name: " +o.getParticipantName() + ";";
                warning=warning+"Account number: "+o.getAccountNumber();
                parameters.add(warning);
                ValidationException validationException = new ValidationException(
                        message, parameters);
                log.warn(LogUtils.createLogEntryUser("", name, "", message,
                        null));
                log.debug(LogUtils.createExceptionLogEntry("", getClass()
                        .getName(), validationException));
                throw validationException;
                // throw new EJBException(validationException);
            }

            Query query = em.createNamedQuery("Participant.findByAccount")
                    .setParameter("account", p.getAccountNumber());
            Participant o1 = null;
            try {
                o1 = (Participant) query.getSingleResult();
            } catch (NoResultException e) {
                // ignore
            }
            if (o1 != null) {
                String message = "ERROR_CREATE_PARTICIPANT_ALREADY_EXIST";
                List<String> parameters = new ArrayList<String>();
//              parameters.add(p.getParticipantName());
                String warning = "Participant name: " +o1.getParticipantName() + ";";
                warning=warning+"Account number: "+o1.getAccountNumber();
                parameters.add(warning);
                ValidationException validationException = new ValidationException(
                        message, parameters);
                log.warn(LogUtils.createLogEntry("", "", message, null));
                log.debug(LogUtils.createExceptionLogEntry("", getClass()
                        .getName(), validationException));
                throw validationException;
            }
            
            //Seconday account number check
            if(!StringUtils.isEmpty(p.getSecondaryAccountNumber())){
            	List<Participant> exist= findBySecondaryAccount(p.getSecondaryAccountNumber());
                if (exist!=null && exist.size()>0) {
            		List<String> duplicatedNames=new ArrayList<String>();
            		for (Participant part:exist) {
            			if (!part.getParticipantName().equalsIgnoreCase(p.getParticipantName())) {
            				duplicatedNames.add(part.getParticipantName());
            			}
            		}
            		
            		if (duplicatedNames.size()>0) {
        	            
            			String message = "ERROR_CREATE_PARTICIPANT_SECONDACCOUNTNUMBER_ALREADY_EXIST";
                        ValidationException validationException = new ValidationException(
                                message, duplicatedNames.toString());
                        log.warn(LogUtils.createLogEntry("", "", message, null));
                        log.debug(LogUtils.createExceptionLogEntry("", getClass()
                                .getName(), validationException));
                        throw validationException;
//            			
//        	            throw new ValidationException(
//        						"ERROR_CREATE_PARTICIPANT_SECONDACCOUNTNUMBER_ALREADY_EXIST", duplicatedNames.toString());
            		}
                }
            }
            //Application Id Check
            if(p.getApplicationId() != null && !StringUtils.isEmpty(p.getApplicationId())){
            	List<Participant> isAppIdExist = findByApplicationId(p.getApplicationId());
            	if(isAppIdExist != null && !isAppIdExist.isEmpty()){
            		List<String> duplicateAppIds=new ArrayList<String>();
            		for (Participant par:isAppIdExist) {
            			if (!par.getParticipantName().equalsIgnoreCase(p.getParticipantName())) {
            				duplicateAppIds.add(par.getParticipantName());
            			}
            		}
            		if(!duplicateAppIds.isEmpty()) {
	            		String message = "ERROR_CREATE_PARTICIPANT_APPLICATIONID_ALREADY_EXIST";
	                    ValidationException validationException = new ValidationException(message, duplicateAppIds.toString());
	                    log.warn(LogUtils.createLogEntry("", "", message, null));
	                    log.debug(LogUtils.createExceptionLogEntry("", getClass()
	                            .getName(), validationException));
	                    throw validationException;
            		}
            	}
            }

            return create(p);

        } catch (ValidationException e) {
            throw e;
        } catch (Exception ex) {
            String message = "error creating particpant " + name;
            log.warn(LogUtils.createLogEntry("", "", message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getParticipant(java.lang.String)
     */
    public Participant getParticipant(String participantName) {
        Participant res = null;
        try {
            res = findByNameAndClient(participantName, false);
            if (res != null)
            	res.getProgramParticipants().size();
        }catch(NoResultException e) {}
        return res;
    }

    /*
     * (non-Javadoc)
     */
    public Participant getParticipantLJF(String participantName) {
        try {
        	Participant p = findByNameAndClientLJF(participantName, false);
            return p;
        }catch(NoResultException e){
            return null;
        }
    }
    
    public Participant getParticipantForProgramNameLJF(String participantName,String programName) {
        try {
        	//Participant p = findByNameClientAndProgramLJF(participantName,programName,false);
        	Query q = em.createNamedQuery( "Participant.findByNameClientAndProgramLJF.Single");
        	q.setParameter("name", participantName);
    		q.setParameter("client", false);
    		q.setParameter("programName", programName);
    		List<Participant> val = q.getResultList();
    		if(val.isEmpty()) {
    			return null;
    		} else if (val.size() == 1) {
    			return val.get(0);
    		} else {
    			throw new NonUniqueResultException(q.toString());
    		}
        }catch(NoResultException e){
            return null;
        }
    }

    public Participant getParticipantByAccount(String accountNumber) {
        return super.getByAccount(accountNumber, false);
    }
    
   public  Participant getParticipantByClient(String participantName) {
	   
	   return super.getParticipantByClient(participantName);
   }
	   
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#getParticipants()
     */
    public List<String> getParticipants() {
        return getParticipants(Participant.SORT_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @deprecated remove after uplayer changed to use clientEAO
     * 
     * @see com.akuacom.pss2.core.DataAccess#getParticipants(int)
     */
    public List<String> getParticipants(int sort) {
        List<Participant> list = super.getByUserType(false);
        List<String> results = new ArrayList<String>();
        for (Participant p : list) {
            results.add(p.getParticipantName());
        }
        return results;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ProgramServicer#getAllParticipants()
     */
    public List<Participant> getAllParticipants() {
        return super.getByUserType(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.core.DataAccess#removeParticipant(java.lang.String)
     */
    public void removeParticipant(String participantName) {
        super.remove(participantName, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @deprecated remove after uplayer changed to use clientEAO
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#getParticipantStatus(java.lang.String)
     */
    public ClientStatus getParticipantStatus(String participantName) {
        try {
            Participant p = findByNameAndClient(participantName, false);
            Integer b = p.getStatus();
            return ClientStatus.values()[b];
        } catch (Exception ex) {
            String message = "error getting status for participant "
                    + participantName;
            log.warn(LogUtils.createLogEntryUser("", participantName, "",
                    message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @deprecated remove after uplayer changed to use clientEAO
     * 
     * @see
     * com.akuacom.pss2.core.DataAccess#setParticipantStatus(java.lang.String,
     * com.akuacom.pss2.core.model.ClientStatus)
     */
    public Participant setParticipantStatus(Participant p,  ClientStatus status) {
        try {
        	//Participant p = findByNameAndClient(participantName, isClient);
            byte b = (byte) status.ordinal();
            /*if (b != p.getStatus()) {
                EventParticipation ep = getEventParticipantion(p,
                        getReason(status), true);
            }*/
            p.setStatus((int) b);
            return em.merge(p);
        } catch (Exception ex) {
            String message = "error setting status for participant "
                    + p.getParticipantName();
            log.warn(LogUtils.createLogEntryUser("", p.getParticipantName(), "",
                    message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }
    }

    public List<Participant> findParticipantsByAccounts(List<String> accountIDs) {
        return super.findParticipantsByAccounts(accountIDs);
    }

    public List<Participant> findParticipantsByProgramName(String progName)
            throws AppServiceException {
        return super.findParticipantsByProgramName(progName, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @deprecated remove after uplayer changed to use clientEAO
     * 
     * @seecom.akuacom.program.services.ProgramServicer#
     * findParticipantsForUpdateStatusLoop()
     */
    public List<Participant> findParticipantsForUpdateStatusLoop() {
        List<Participant> parts;
        try {
            Query query = em
                    .createNamedQuery("Participant.findClientBefore");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -3);
            query.setParameter("time", cal.getTime());
            parts = query.getResultList();
        } catch (Exception ex) {
            String message = "error getting comm log list";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }

        return parts;
    }

    public List<ParticipantPerf> getAllParticipantPerfs() {
        Query q = em.createNamedQuery("ParticipantPerf.findAll");
        return q.getResultList();
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

    public List<EventParticipant> findEventParticipants(String participantName) {
        return super.findEventParticipants(participantName, false);
    }

    public List<EventParticipant> findEventParticipants(List<String> names) {
        return super.findEventParticipants(names, false);
    }

 

    public void removeParticipantFromAllEvents(String participantName) {
        try {
            if (participantName == null) {
                String message = "participant name is null";
                log.warn(LogUtils.createLogEntry("", "", message, null));
                throw new EJBException(message);
            }

            Query query = em
                    .createNamedQuery("EventParticipant.findByParticipant")
                    .setParameter("participantName", participantName)
                    .setParameter("client", false);
            List<EventParticipant> resultList = query.getResultList();
            for (Object o : resultList) {
                EventParticipant dao = (EventParticipant) o;
                em.remove(dao);
            }
        } catch (Exception ex) {
            String message = "error removing pariticpant " + participantName;
            log.warn(LogUtils.createLogEntry("", "", message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }
    }
	
	public List<Participant> getParticipants(List<String> partNames,
			List<String> accounts, List<String> programNames,
			Integer startPage, Integer pageSize, String orderBy, String order) {

		Query query = null;

		String queryString = "select distinct p from Participant p LEFT JOIN fetch  p.programParticipants pp where p.client=0";
		if (partNames != null) {
			for (String partName : partNames) {
				queryString = queryString + " and p.participantName like '" + partName + "%' ";
			}
		}

		if (accounts != null) {
			for (String accountNumber : accounts) {
				queryString = queryString + " and p.accountNumber like '" + accountNumber + "%' ";
			}
		}

		if (programNames != null) {
			for (String programName : programNames) {
				queryString = queryString + " and exists (select pp from p.programParticipants pp where pp.programName like '" + programName + "%') ";
			}
		}
		
		if (queryString.contains("_"))
			queryString=queryString.replace("_", "\\_");

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


		query = em.createQuery(queryString);

		query.setFirstResult(startPage);
		query.setMaxResults(pageSize);

		List<Participant> participantList = query.getResultList();
		for (Participant p : participantList) {
			if (p == null) {
				continue;
			}
			if (p.getProgramParticipants() != null) {
				p.getProgramParticipants().size();
			}
			if (p.getEventParticipants() != null) {
				p.getEventParticipants().size();
			}
		}

		return participantList;
	}
	
	public List<Participant> getParticipants(Integer start, Integer pageSize,
			String filterContent, String filterColumn, String orderBy, String order) {

		Query query = null;

		String queryString = "SELECT DISTINCT p FROM Participant p " +
										"LEFT JOIN fetch p.programParticipants pp " +
										"WHERE p.client=0 ";
		
		if(filterContent != null && filterColumn!= null){
			if(!filterContent.equals("") && !filterColumn.equals("")){
				queryString = queryString + "AND " + filterColumn + " like :filterContent";				
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

		query = em.createQuery(queryString);
		
		if (filterContent != null && filterColumn != null) {
			if (!filterContent.equals("") && !filterColumn.equals("")) {
				if (filterContent.contains("_")) 
					filterContent = filterContent.replace("_", "\\_");
				
				query.setParameter("filterContent", filterContent );
			}
		}
		
		query.setFirstResult(start);
		query.setMaxResults(pageSize);

		List<Participant> participantList = query.getResultList();
		for (Participant p : participantList) {
			if (p == null) {
				continue;
			}
			if (p.getProgramParticipants() != null) {
				p.getProgramParticipants().size();
			}
			if (p.getEventParticipants() != null) {
				p.getEventParticipants().size();
			}
		}

		return participantList;
	}
	
	Map<String,String> countQueries =  new HashMap<String,String>();
    @PostConstruct
    private void init() {
        countQueries.put("p.participantName", "Participant.countByName");
        countQueries.put("p.accountNumber", "Participant.countByAccountNo");
        countQueries.put("pp.program.programName", "Participant.countByProgramName");
    }

	@Override
	public Integer getParticipantRowCount(List<String> partNames,
			List<String> accounts, List<String> programNames) {
		Query query = null;

		String queryString = "select distinct p from Participant p left join fetch p.programParticipants pp where p.client=0";
		if (partNames != null) {
			for (String partName : partNames) {
				queryString = queryString + " and p.participantName like '" + partName + "%' ";
			}
		}

		if (accounts != null) {
			for (String accountNumber : accounts) {
				queryString = queryString + " and p.accountNumber like '" + accountNumber + "%' ";
			}
		}

		if (programNames != null) {
			for (String programName : programNames) {
				queryString = queryString + " and pp.program.programName like '" + programName + "%' ";
			}
		}

		query = em.createQuery(queryString);

		List<Participant> participantList = query.getResultList();

		if(participantList != null){
			return participantList.size();
		}
		else{
			return 0;			
		}
	}

	@Override
	public Integer getParticipantRowCount(String filterContent,
			String filterColumn) {
		Query query = em.createNamedQuery(countQueries.get(filterColumn));
		if(filterContent != null && filterColumn!= null){
			if(!filterContent.equals("") && !filterColumn.equals("")){
				query.setParameter("name", filterContent );
			} else {
			    query.setParameter("name", "%" );
			}
		}
		return ((Long) query.getSingleResult()).intValue();
	}
	
	
	@Override
	public Integer getParticipantRowCountByProgramAndClient(String programName, boolean isClient){
        Query query = em
                .createNamedQuery("Participant.countByProgramNameAndClient");
        query.setParameter("name", programName);
        query.setParameter("client", isClient);
        return ((Long) query.getSingleResult()).intValue();
		
	}

    @Override
	public Integer getParticipantRowCountByProgramAndClientAndState(String programName, boolean isClient){
        Query query = em
                .createNamedQuery("Participant.countByProgramNameAndClientAndState");
        query.setParameter("name", programName);
        query.setParameter("client", isClient);

        return ((Long) query.getSingleResult()).intValue();

	}

	@Override
	public Boolean isParticipantOnline(String participantName) {
		Boolean online = false;
        try {
            Query query = em
                    .createNamedQuery("Participant.findOfflineClientsForParticipant");
            query.setParameter("name", participantName);
            if (query.getResultList() != null) {
            	online = (query.getResultList().size() == 0);
            }
        } catch (Exception ex) {
            String message = "error getting participant online/offline status";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            log.debug(LogUtils.createExceptionLogEntry("",
                    getClass().getName(), ex));
            throw new EJBException(message, ex);
        }
        return online;
	
	}

	@Override
	public List<Participant> findInAccounts(Collection<java.lang.String> accounts) {
		Query q = em.createNamedQuery( "Participant.findInAccounts" );
		q.setParameter("accounts", accounts);
		q.setParameter("time", DateUtil.getStartOfDay(new Date()));
		return q.getResultList();
	}

	@Override
	public List<Participant> findNotInAccounts(List<String> accounts) {
		Query q = em.createNamedQuery( "Participant.findNotInAccounts" );
		q.setParameter("accounts", accounts);
		q.setParameter("time", DateUtil.getStartOfDay(new Date()));
		return q.getResultList();
	}

	@Override
	public List<String> getInactiveAccount(List<String> accounts) {
		Query q = em.createNamedQuery( "Participant.getInactiveAccounts" );
		q.setParameter("accounts", accounts);
		q.setParameter("time", DateUtil.getStartOfDay(new Date()));
		return q.getResultList();
	}

	@Override
    public List<String> findUtilityProgramNamesByParticipant(String participantName, boolean isClient) {
        List<ProgramParticipant> ppList=new ArrayList<ProgramParticipant>();
        try {
            Query query = em
                    .createNamedQuery("ProgramParticipant.findByParticipant");
            query.setParameter("participantName", participantName).setParameter(
                    "client", isClient);
            ppList = query.getResultList();
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_GET: " + participantName
                    + " | " + e.getMessage(), e);
        }

        try {
            List<String> newList = new ArrayList<String>();
            for (ProgramParticipant pp : ppList) {
                newList.add(pp.getProgram().getUtilityProgramName());
            }
            
            return newList;
        } catch (Exception ex) {
            throw new EJBException("", ex);
        }
    }

	@Override
	public List<Participant> findParticipantsBySecondaryAccounts(
			List<String> accountIDs) {
		 return super.findParticipantsBySecondaryAccounts(accountIDs);
	}
	@Override
	public List<Participant> findParticipantsWithEventParticipantsByAccounts(List<String> accounts){
		Query q = em.createNamedQuery( "Participant.findParticipantsWithEventParticipantsByAccounts" );
		q.setParameter("accounts", accounts);
		return q.getResultList();
	}

	@Override
	public Participant getParticipantByApplicationId(String applicationId) {
		Query q = em.createNamedQuery("Participant.findByApplicationId");
		q.setParameter("applicationId", applicationId);
		return (Participant) q.getSingleResult();
	}
	
	@Override
    public Map<String,ParticipantClientListFor20Ob> getParticipantCLientListFor20Ob(ArrayList<String> clientNames)
    {
		   Map<String,ParticipantClientListFor20Ob> resultMap = new HashMap <String,ParticipantClientListFor20Ob>();
           Query query = null;
           StringBuilder clients = new StringBuilder();
                    for(String str:clientNames){
                           clients.append("'"+str+"',");                  
             }
       if(clients.length()>0){
           clients.delete(clients.length()-1, clients.length());
           StringBuilder sqltemplate = new StringBuilder();
           sqltemplate.append("select parentName,parentAccount,parentApplicationID,clientName from  all_participant_client");
           sqltemplate.append(" where clientName in ("+clients+")");
           
           String queryString = sqltemplate.toString();
           query = em.createNativeQuery(queryString);
           List<Object[]> rows = query.getResultList();
           List<ParticipantClientListFor20Ob> result = new ArrayList<ParticipantClientListFor20Ob>(rows.size());
           for (Object[] row : rows) {
               resultMap.put((String) row[3], new ParticipantClientListFor20Ob((String) row[0],(String) row[1],(String) row[2],(String) row[3]));
           }
           
       }
       return resultMap;
    }
}