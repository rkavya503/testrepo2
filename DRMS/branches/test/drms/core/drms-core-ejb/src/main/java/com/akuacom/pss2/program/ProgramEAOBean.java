/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.rtp.RTPPrice;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class ProgramEAOBean extends com.akuacom.ejb.BaseEAOBean<Program>
		implements ProgramEAO.R, ProgramEAO.L {

	public ProgramEAOBean() {
		super(Program.class);
	}

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ProgramEAOBean.class);
	
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.program.services.ProgramServicer#createProgram(com.akuacom
	 * .pss2.program.Program)
	 */
	public Program createProgram(Program program) throws AppServiceException {
		try {
			program = create(program);
		} catch (Exception e) {
			throw new AppServiceException("ERROR_PROGRAM_CREATE", e);
		}
		return program;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.program.services.ProgramServicer#updateProgram(com.akuacom
	 * .pss2.program.Program)
	 */
	public Program updateProgram(Program program) throws AppServiceException {
		try {
			program = update(program);
		} catch (Exception e) {
			throw new AppServiceException("ERROR_PROGRAM_UPDATE", e);
		}
		return program;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.akuacom.program.services.ProgramServicer#deleteProgramByProgramName
	 * (java.lang.String)
	 */
	@SuppressWarnings( { "unchecked" })
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteProgramByProgramName(String programName)
			throws AppServiceException {
		try {

			Query query = em.createNamedQuery("Program.findByName")
					.setParameter("name", programName.trim());
			List<Program> programs = query.getResultList();

			if (programs == null || programs.size() <= 0) {
				throw new AppServiceException("Program " + programName
						+ " doesn't exist!");
			}
			Program program = programs.get(0);
			final int low = getLowestPriority();
			if (low >= 0) {
				program.setPriority(-1);
			} else {
				program.setPriority(low - 1);
			}
			updateProgram(program);
		} catch (Exception e) {
			throw new AppServiceException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public List<Program> findProgramsByProgramClass(List<String> programClasses, boolean state)
			throws AppServiceException {
		List<Program> programs = new ArrayList<Program>();

		try {
			if (programClasses != null && programClasses.size() != 0) {
				Query query = em.createNamedQuery("Program.findByProgramClass")
						.setParameter("programClasses", programClasses);
				programs = (List<Program>) query.getResultList();
			}
		} catch (Exception e) {
			String message="could not find programs by program classes: "+programClasses.toString();
			throw new AppServiceException(message, e);
		}
		return programs;
	}
	
	public Program getProgramOnly(String programName){
		try {
			Query query = em.createNamedQuery("Program.findByName").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Deprecated
	public Program getProgram(String programName) {
		if (programName == null) {
			String message = "Program name is null";
			log.warn(LogUtils.createLogEntry("", "", message, null));
			throw new EJBException(message);
		}

		Program result = null;

		Query query = em.createNamedQuery(
				"Program.findByNameLoadBidConfig").setParameter("name",
				programName);
		try {
			result = (Program) query.getSingleResult();
			if (result != null) {
//				result.getOperatorContacts().size();
				Set<ProgramParticipant> sPP = result.getProgramParticipants();
				if (sPP != null && (sPP.size() > 0)) {
                	for (ProgramParticipant pp : sPP) {
                    	if(pp.getBidEntries() != null){pp.getBidEntries().size();}
                    	if(pp.getCustomRules() != null){pp.getCustomRules().size();}
                    	if(pp.getProgramParticipantRules() != null){pp.getProgramParticipantRules().size();}
                    	if(pp.getParticipant() != null){
                        	if(pp.getParticipant().getEventParticipants() != null){pp.getParticipant().getEventParticipants().size();}
                        	if(pp.getParticipant().getContacts() != null){pp.getParticipant().getContacts().size();}
                    	}
                	}
				}
			}
			return result;
		} catch (NoResultException e) {
			log.warn(e);
			return null;
		}
	}

//TODO: remove due to program contact is no longer used. DRMS-7792	
//	public Program getProgramPerfWithContact(String programName) {
//		if (programName == null) {
//			String message = "Program name is null";
//			log.warn(LogUtils.createLogEntry("", "", message, null));
//			throw new EJBException(message);
//		}
//
//		Program result = null;
//
//		Query query = em.createNamedQuery("Program.findProgramWithContacts")
//				.setParameter("name", programName);
//		try {
//			result = (Program) query.getSingleResult();
////			if (result != null) {
////				result.getOperatorContacts().size();
////			}
//
//		} catch (NoResultException e) {
//			log.warn(LogUtils.createExceptionLogEntry("", getClass()
//					.getName(), e));
//		}	
//		return result;
//	}

	
	public ProgramPerf getProgramPerf(String programName) {
		if (programName == null) {
			String message = "Program name is null";
			log.warn(LogUtils.createLogEntry("", "", message, null));
			throw new EJBException(message);
		}

		ProgramPerf result = null;

		Query query = em.createNamedQuery("ProgramPerf.findByName")
				.setParameter("name", programName);
		try {
			result = (ProgramPerf) query.getSingleResult();
		} catch (NoResultException e) {
			log.warn(LogUtils.createExceptionLogEntry("", getClass()
					.getName(), e));
		}	
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.akuacom.program.services.ProgramServicer#findPrograms(int)
	 */
	public List<Program> getProgramsHydratePrgmParts() throws AppServiceException {
		try {
			Query q = em.createNamedQuery("Program.findAllProgramsWithParticipants");
			List<Program> pList = q.getResultList();
			/*if (pList != null && pList.size() > 0) {
				for (Program p : pList) {
					Set<ProgramParticipant> sPP = p.getProgramParticipants();
					if (sPP != null) {
						sPP.size();
					}
				}
			}*/
			return pList;
		} catch (Exception ex) {
			throw new AppServiceException("", ex);
		}
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see com.akuacom.program.services.ProgramServicer#findPrograms(int)
	 */
	public List<Program> getProgramsLazy() throws AppServiceException {
		try {
			Query q = em.createNamedQuery("Program.findAll");

			List<Program> pList = q.getResultList();
			
			return pList;

		} catch (Exception ex) {
			throw new AppServiceException("", ex);
		}
	}

    public List<Program> getProgramsHydratePrgmPartsByPriority() throws AppServiceException {
		try {
			Query q = em.createNamedQuery("Program.findByPriority");

			List<Program> pList = q.getResultList();
/*			
			
			if (pList != null && pList.size() > 0) {
				for (Program p : pList) {
					Set<ProgramParticipant> sPP = p.getProgramParticipants();
					if (sPP != null) {
						sPP.size();
					}
				}

			}

*/			
			return pList;

		} catch (Exception ex) {
			throw new AppServiceException("", ex);
		}
	}


    public HashMap<String, Integer> getProgramPriority() {
        try {
            Query query = em
                    .createNamedQuery("Program.getProgramNameAndPriority");
            List<Object[]> list = query.getResultList();

            HashMap<String, Integer> res = new HashMap<String, Integer>();
            if (list != null) {
                for (Object[] obj : list) {
                    res.put((String) obj[0], (Integer) obj[1]);
                }
            }
            return res;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }


    
    public String getUiScheduleEventString(String programName) {
        try {
            Query query = em
                    .createNamedQuery("Program.getUiScheduleEventString");
            query.setParameter("name", programName);
            return (String) query.getSingleResult();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.program.services.ProgramServicer#findActiveProgramNames()
	 */
	public List<String> findProgramNames() throws AppServiceException {
		try {
			return findPrioritySortedProgramNames();
		} catch (Exception ex) {
			throw new AppServiceException("", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.program.services.ProgramServicer#findPrioritySortedProgramNames
	 * ()
	 */
	@SuppressWarnings( { "unchecked" })
	public List<String> findPrioritySortedProgramNames() {
		try {
			Query query = em
					.createNamedQuery("Program.findProgramNameOrderedByPriority");
			return query.getResultList();
		} catch (Exception ex) {
			throw new EJBException("", ex);
		}
	}

	public void setRealTimePrice(RTPPrice price) {
		em.merge(price);
	}

	public RTPPrice getLastRealTimePrice(String programName) {
		RTPPrice res = null;
		Query query = em.createNamedQuery("RTPPrice.findLatest");
		query.setParameter("program", programName);
		try {
			List resList = query.getResultList();
			if (resList != null && resList.size() > 0)
				res = (RTPPrice) resList.get(0);
		} catch (NoResultException e) {
			// ignore
		}

		return res;
	}

	@SuppressWarnings( { "unchecked" })
	public List<RTPPrice> getCurrentRealTimePrices(String programName) {
		List<RTPPrice> res = null;
		Query query = em.createNamedQuery("RTPPrice.findCurrent");
		query.setParameter("now", new Date());
		query.setParameter("program", programName);
		try {
			res = query.getResultList();
			if (res == null)
				res = new ArrayList<RTPPrice>();
		} catch (NoResultException e) {
			// ignore
		}

		return res;
	}

	public Program markPriorityForAddLast(Program program) {
		final Query query = em.createNamedQuery("Program.Priority.Max");
		final Integer max = (Integer) query.getSingleResult();
		program.setPriority(max + 1);
		return program;
	}

	public int getLowestPriority() {
		Query query = em.createNamedQuery("Program.Priority.Min");
		return (Integer) query.getSingleResult();
	}

	@SuppressWarnings( { "unchecked" })
	public Program markPriorityForAddAfter(int before, Program program) {
		try {
			List<Program> programList;

			Query query = em
					.createNamedQuery("Program.findByPrioritySorted");
			query.setParameter("priority", before);
			programList = query.getResultList();
			for (Program p : programList) {
				p.setPriority(p.getPriority() + 1);
				this.markPriority(p);
			}

			program.setPriority(before + 1);
			return program;
		} catch (Exception ex) {
			throw new EJBException("", ex);
		}
	}

	@SuppressWarnings( { "unchecked" })
	public List<Program> getProgramsForAddAfter(int before) {
		Query query = em
				.createNamedQuery("Program.findByPrioritySorted");
		query.setParameter("priority", before);
		return query.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void markPriority(Program p) throws Exception {
		update(p);
	}

	public Program refreshProgram(String programName) {
		Query q = em.createNamedQuery("Program.findByName")
				.setParameter("name", programName);
		Program prog = (Program) q.getSingleResult();

		return prog;
	}

	public int getMaxPriority() {
		final Query query = em.createNamedQuery("Program.Priority.Max");

		return ((Integer) query.getSingleResult());
	}
	
	/**
	 * TODO: return value should be change to String if there is no duplicated long program name
	 */
	@Override
	public List<Program> findProgramsByLongProgramName(String longProgramName) throws AppServiceException{
		List<Program> programs = new ArrayList<Program>();
		try {
			if (longProgramName != null && longProgramName.trim().length() != 0) {
				Query query = em.createNamedQuery("Program.findByLongProgramName")
						.setParameter("longProgramName", longProgramName);
				programs = (List<Program>) query.getResultList();
			}
		} catch (Exception e) {
			String message="could not find programs by long program name: " + longProgramName;
			throw new AppServiceException(message, e);
		}
		return programs;
	}

	@Override
	public Boolean isDrasBiddingByProgramName(String programName) {
		Program p=getProgramWithBidConfig(programName);
		if(p.getBidConfig()!=null){
			return p.getBidConfig().isDrasBidding();
		}
		return false;
		//Query query = em.createNamedQuery("Program.drasBiddingByProgramName").setParameter("name", programName);;
		//return ((Boolean) query.getSingleResult());
	}
	
	@Override
	public String getProgramClassName(String programName){
		Query query = em.createNamedQuery("Program.getProgramClassName").setParameter("name", programName);;
		return ((String) query.getSingleResult());
	}

	@Override
	public String getProgramClassByName(String programName) {
		Query query = em.createNamedQuery("Program.getProgramClassByName").setParameter("name", programName);;
		return ((String) query.getSingleResult());
	}

	@Override
	public Program getProgramWithLoadBid(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findByNameLoadBidConfig").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}

	@Override
	public Program getProgramWithParticipants(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithParticipants").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public Program getProgramWithSignals(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithSignals").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public Program getProgramWithBidConfig(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithBidConfig").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}

	@Override
	public Program getProgramWithRules(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithRules").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public Program getProgramWithAllConfig(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithAllConfig").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public Program getProgramWithParticipantsAndPRules(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithProgramParticipantsAndPRules").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public Program getProgramWithParticipantsAndPRules(String programName,List<String> partList) {
		try {
			Query query = em.createNamedQuery("Program.findOnlyProgramWithProgramParticipantsAndPRules");
			query.setParameter("name",programName);
			query.setParameter("participantName",partList);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@Override
	public List<Object> doQuery(String hql) {
		Query query = em.createQuery(hql);
		return query.getResultList();
	}

	@Override
	public List<Program> getAllPrograms() {
		try {
			Query query = em.createNamedQuery("Program.findAll");
			List<Program> result = (List<Program> ) query.getResultList();
			return result;
		}catch (Exception e){
			log.warn(e);
			return null;
		} 
	}

	@Override
	public Program getProgramWithBidAndProgramParticipantsAndPRules(
			String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithBidAndProgramParticipantsAndPRules").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}

	@Override
	public Program findProgramAndRulesdByProgramName(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramAndRulesdByProgramName").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			result.getRules().size();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}

	@Override
	public Program findProgramWithSignalsPerf(String program) {
		try {
			Query query = em.createNamedQuery("Program.findProgramWithSignalsPerf").setParameter("name",program);
			Program result = (Program) query.getSingleResult();
			result.getSignals().size();//init signals
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+program;
			log.warn(message,e);
			return null;
		}
	}

	@Override
	public Program findProgramPerfByProgramName(String programName) {
		try {
			Query query = em.createNamedQuery("Program.findProgramPerfByProgramName").setParameter("name",programName);
			Program result = (Program) query.getSingleResult();
			return result;
		}catch (NoResultException e){
			String message="could not find programs by program name: "+programName;
			log.warn(message,e);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Program getBySecondaryUtilityName(String secondaryUtilityName) {
		if (secondaryUtilityName==null || secondaryUtilityName.isEmpty())
			return null;
		
		Query query = em.createNamedQuery("Program.getBySecondaryUtilityName");
		query.setParameter("secondaryUtilityName",secondaryUtilityName);
		List<Program> result = query.getResultList();
		if (result!=null && result.size()>0)
			return result.get(0);
		else
			return null;
	}

	@Override
	public List<Program> getProgramsWithParticipants(List<String> programNames) {
		try {
			Query query = em.createNamedQuery("Program.findProgramsWithParticipants").setParameter("programNames",programNames);
			List<Program> result = query.getResultList();
			return result;
		}catch (NoResultException e){
			StringBuffer sb = new StringBuffer();
			for(String name :programNames){
				sb.append(name +",");
			}
			String message="could not find programs by program names: "+sb.toString();
			log.warn(message,e);
			return new ArrayList<Program>();
		}
	}

}