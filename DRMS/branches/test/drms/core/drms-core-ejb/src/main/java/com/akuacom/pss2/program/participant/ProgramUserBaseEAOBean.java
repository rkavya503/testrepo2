/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class ProgramUserBaseEAOBean extends
        com.akuacom.ejb.BaseEAOBean<ProgramParticipant> implements
        ProgramUserBaseEAO.R, ProgramUserBaseEAO.L {
    /** The Constant log. */
    private static final Logger log = Logger
            .getLogger(ProgramUserBaseEAOBean.class);

    @EJB
    ParticipantEAO.L participantEAO;

    public ProgramUserBaseEAOBean() {
        super(ProgramParticipant.class);
    }

    
    public void createProgramParticipants(String programName,
            List<Participant> participant, boolean isClient){
        if (programName == null || participant == null) {
            String message = "programName or participantName can not be null";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            throw new EJBException(message);
        }

        try {

            Query query = em
                    .createNamedQuery("Program.findByName")
                    .setParameter("name", programName);
            Program pe = (Program) query.getSingleResult();

            for(Participant p : participant){
            	String participantName = p.getParticipantName();
            	
            	if (isProgramParticipantInDB(
                        programName, participantName, isClient)) {
                    String message = "participant " + participantName
                            + " already exists for program " + programName;
                    log.warn(LogUtils.createLogEntry(programName, participantName,
                            message, null));
                    throw new EJBException(message);
                }
            	
                // need program and participant actively managed by em

                Participant part = participantEAO.findByNameAndClient(participantName, isClient);

                if (pe == null || part == null) {
                    String message = "program or participant doesn't exist";
                    log.warn(LogUtils.createLogEntry(programName, participantName,
                            message, null));
                    throw new EJBException(message);
                }
            	
                ProgramParticipant pp = new ProgramParticipant();
                pp.setName("");
                pp.setValue("");
                pp.setProgram(pe);
                pp.setParticipant(part);
                pp.setProgramName(programName);
                pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
                
                if ((pe != null) && (pe instanceof DemandLimitingProgram)) {
            		if (pp.getProgramParticipantRules() == null) {
            			pp.setProgramParticipantRules(new HashSet<ProgramParticipantRule>());
            		}

                	for (ProgramRule demandLimitingProgramRule: pe.getRules()) {
                    	ProgramParticipantRule demandLimitingProgramParticipantRule = new ProgramParticipantRule();

                    	demandLimitingProgramParticipantRule.setSortOrder(demandLimitingProgramRule.getSortOrder());
                    	demandLimitingProgramParticipantRule.setMode(demandLimitingProgramRule.getMode());
                    	demandLimitingProgramParticipantRule.setThreshold(demandLimitingProgramRule.getThreshold());
                    	demandLimitingProgramParticipantRule.setStart(demandLimitingProgramRule.getStart());
                    	demandLimitingProgramParticipantRule.setEnd(demandLimitingProgramRule.getEnd());
                    	demandLimitingProgramParticipantRule.setVariable(demandLimitingProgramRule.getVariable());
                    	demandLimitingProgramParticipantRule.setOperator(demandLimitingProgramRule.getOperator());
                    	demandLimitingProgramParticipantRule.setValue(demandLimitingProgramRule.getValue());
                    	demandLimitingProgramParticipantRule.setSource(demandLimitingProgramRule.getSource());
                    	demandLimitingProgramParticipantRule.setSignalAction(demandLimitingProgramRule.getSignalAction());
                    	demandLimitingProgramParticipantRule.setNotifyAction(demandLimitingProgramRule.getNotifyAction());			
                    	
                    	pp.getProgramParticipantRules().add(demandLimitingProgramParticipantRule);
                	}
                }

                if (part.getProgramParticipants() == null) {
                    part.setProgramParticipants(new HashSet<ProgramParticipant>());
                }
                part.getProgramParticipants().add(pp);
                em.merge(part);
            	
            }
            
        	



            // refresh program since we did not update their program participant
            // list
            em.refresh(pe);
        } catch (Exception e) {
            String message = "error adding particpants " 
                    + "into program: " + programName;

            log.warn(LogUtils.createLogEntry(programName, "participantName",
                    message, null));
            log.debug(LogUtils.createExceptionLogEntry(programName, this
                    .getClass().getName(), e));
            throw new EJBException(message, e);
        }    	
    }

    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#createProgramParticipant
     * (com.akuacom.program.beans.ProgramParticipantImpl)
     */
    public ProgramParticipant createProgramParticipant(String programName,
            String participantName, boolean isClient) {
        if (programName == null || participantName == null) {
            String message = "programName or participantName can not be null";
            log.warn(LogUtils.createLogEntry("", "", message, null));
            throw new EJBException(message);
        }

        try {
            if (isProgramParticipantInDB(
                    programName, participantName, isClient)) {
                String message = "participant " + participantName
                        + " already exists for program " + programName;
                log.warn(LogUtils.createLogEntry(programName, participantName,
                        message, null));
                throw new EJBException(message);
            }

            // need program and participant actively managed by em
            final Query query = em
                    .createNamedQuery("Program.findByName")
                    .setParameter("name", programName);
            Program pe = (Program) query.getSingleResult();


            	
            
            
            Participant part = participantEAO.findByNameAndClient(participantName, isClient);

            if (pe == null || part == null) {
                String message = "program or participant doesn't exist";
                log.warn(LogUtils.createLogEntry(programName, participantName,
                        message, null));
                throw new EJBException(message);
            }

            ProgramParticipant pp = new ProgramParticipant();
            pp.setName("");
            pp.setValue("");
            pp.setProgram(pe);
            pp.setParticipant(part);
            pp.setProgramName(programName);
            pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
            
            if ((pe != null) && (pe instanceof DemandLimitingProgram)) {
        		if (pp.getProgramParticipantRules() == null) {
        			pp.setProgramParticipantRules(new HashSet<ProgramParticipantRule>());
        		}

            	for (ProgramRule demandLimitingProgramRule: pe.getRules()) {
                	ProgramParticipantRule demandLimitingProgramParticipantRule = new ProgramParticipantRule();

                	demandLimitingProgramParticipantRule.setSortOrder(demandLimitingProgramRule.getSortOrder());
                	demandLimitingProgramParticipantRule.setMode(demandLimitingProgramRule.getMode());
                	demandLimitingProgramParticipantRule.setThreshold(demandLimitingProgramRule.getThreshold());
                	demandLimitingProgramParticipantRule.setStart(demandLimitingProgramRule.getStart());
                	demandLimitingProgramParticipantRule.setEnd(demandLimitingProgramRule.getEnd());
                	demandLimitingProgramParticipantRule.setVariable(demandLimitingProgramRule.getVariable());
                	demandLimitingProgramParticipantRule.setOperator(demandLimitingProgramRule.getOperator());
                	demandLimitingProgramParticipantRule.setValue(demandLimitingProgramRule.getValue());
                	demandLimitingProgramParticipantRule.setSource(demandLimitingProgramRule.getSource());
                	demandLimitingProgramParticipantRule.setSignalAction(demandLimitingProgramRule.getSignalAction());
                	demandLimitingProgramParticipantRule.setNotifyAction(demandLimitingProgramRule.getNotifyAction());			
                	
                	pp.getProgramParticipantRules().add(demandLimitingProgramParticipantRule);
            	}
            }

            if (part.getProgramParticipants() == null) {
                part.setProgramParticipants(new HashSet<ProgramParticipant>());
            }
            part.getProgramParticipants().add(pp);
            em.merge(part);

            // refresh program since we did not update their program participant
            // list
            em.refresh(pe);

            return findProgramParticipantsByProgramNameAndPartName(
                    pp.getProgramName(), pp.getParticipantName());
        } catch (Exception e) {
            String message = "error add particpant " + participantName
                    + "into program: " + programName;
            // 09.20.2010 Linda: DRMS-1665
            log.warn(LogUtils.createLogEntry(programName, participantName,
                    message, null));
            log.debug(LogUtils.createExceptionLogEntry(programName, this
                    .getClass().getName(), e));
            // log.warn(message, e);
            throw new EJBException(message, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#updateProgramParticipant
     * (com.akuacom.program.beans.ProgramParticipant)
     */
    public ProgramParticipant updateProgramParticipant(ProgramParticipant value) {
        if (value == null) {
            String message = "participant doesn't exist in program";
            // 09.20.2010 Linda: DRMS-1665
            log.warn(LogUtils.createLogEntry("",
                    LogUtils.CATAGORY_CONFIG_CHANGE, message, null));
            // log.warn(message);
            throw new EJBException(message);
        }

        try {
            value = em.merge(value);

        } catch (Exception e) {
            String message = "error add particpant "
                    + value.getParticipantName() + "to program: "
                    + value.getProgramName();
            // 09.20.2010 Linda: DRMS-1665
            log.warn(LogUtils.createLogEntry("",
                    LogUtils.CATAGORY_CONFIG_CHANGE, message, null));
            log.debug(LogUtils.createExceptionLogEntry(value.getProgramName(),
                    getClass().getName(), e));
            // log.warn(message, e);
            throw new EJBException(message, e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.program.services.ProgramServicer#deleteProgramParticipant
     * (java.lang.String)
     */
    public void deleteProgramParticipant(ProgramParticipant pp) {
        if (pp == null || pp.getUUID() == null) {
            String message = "participant doesn't exist in program";
            // 09.20.2010 Linda: DRMS-1665
            log.warn(LogUtils.createLogEntry("",
                    LogUtils.CATAGORY_CONFIG_CHANGE, message, null));
            // log.warn(message);
            throw new EJBException(message);
        }

        try {
            ProgramParticipant pp2 = em.find(ProgramParticipant.class,
                    pp.getUUID());
            if (pp2 == null) {
                String message = "participant doesn't exist in program. Find failed.";
                // 09.20.2010 Linda: DRMS-1665
                log.warn(LogUtils.createLogEntry("",
                        LogUtils.CATAGORY_CONFIG_CHANGE, message, null));
                // log.warn(message);
                throw new EJBException(message);
            }

            em.remove(pp2);
        } catch (Exception e) {
            String message = "error remove particpant "
                    + pp.getParticipantName() + "from program: "
                    + pp.getProgramName();
            // 09.20.2010 Linda: DRMS-1665
            log.warn(LogUtils.createLogEntry("",
                    LogUtils.CATAGORY_CONFIG_CHANGE, message, null));
            log.debug(LogUtils.createExceptionLogEntry(pp.getProgramName(),
                    getClass().getName(), e));
            // log.warn(message, e);
            throw new EJBException(message, e);
        }
    }

    public void createProgramParticipant(ProgramParticipant pp) {
        if (pp == null) {
            String message = "participant doesn't exist in program";
            throw new EJBException(message);
        }

        try {
            if (pp.getState() == ProgramParticipant.PROGRAM_PART_DELETED) {
                pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
                em.merge(pp);
            } else if (pp.getState() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
                String message = "participant " + pp.getParticipantName()
                        + " already exists for program " + pp.getProgramName();
                throw new EJBException(message);
            }

        } catch (Exception e) {
            String message = "error add particpant " + pp.getParticipantName()
                    + "to program: " + pp.getProgramName();
            throw new EJBException(message, e);
        }
    }

    
    public List<ProgramParticipant> findByProgramAndParentParticipantForClient(java.lang.String programName, String parentParticipantName, boolean client){
		Query q = em.createNamedQuery( "ProgramParticipant.findByProgramAndParentParticipantForClient" );
		q.setParameter("programName", programName);
		q.setParameter("parentParticipantName", parentParticipantName);
		q.setParameter("client", client);
		return q.getResultList();    	
    }
    
    public List<ProgramParticipant> findProgramParticipantsByProgramName(
            String progName) throws AppServiceException {
        try {
            Query namedQuery = em.createNamedQuery(
                    "ProgramParticipant.findAggregatorTestParticipantByProgram").setParameter(
                    "programName", progName);
            List<ProgramParticipant> list = namedQuery.getResultList();
            for(ProgramParticipant pp : list){
            	pp.getParticipant().getProgramParticipants().size();
            }

            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }
    
    public List<ProgramParticipant> findAllProgramParticipantsByProgramName(
            String progName) throws AppServiceException {
        try {
            Query namedQuery = em.createNamedQuery("ProgramParticipant.findProgramParticipantAllByProgram")
            	.setParameter("programName", progName);
            List<ProgramParticipant> list = namedQuery.getResultList();
            for(ProgramParticipant pp : list){
            	pp.getParticipant().getProgramParticipants().size();
            }

            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    public List<ProgramParticipant> findProgramParticipantsByProgramNames(
            String progName1, String progName2) throws AppServiceException {
        try {
            String query = "select pp from ProgramParticipant pp where pp.programName = ?1 and pp.participant.participantName in (select pp2.participant.participantName from ProgramParticipant pp2 where pp2.programName = ?2 and pp2.participant.client = 0 ) ";

            List<Object> names = new ArrayList<Object>();
            names.add(progName1);
            names.add(progName2);
            List<ProgramParticipant> list = super.getByFilters(query, names);

            return list;
        } catch (Exception ex) {
            throw new AppServiceException("", ex);
        }
    }

    
    
    public Boolean isProgramParticipantInDB(String progName, String partName, boolean isClient){
        Query query = em
                .createNamedQuery(
                        "ProgramParticipant.countByProgramAndParticipant")
                .setParameter("programName", progName)
                .setParameter("participantName", partName)
                .setParameter("client", isClient);
        
        Long result = (Long) query.getSingleResult();
        // if there is result and there is matching record, return false.
        return result != null && result != 0;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.program.services.ProgramServicer#
     * findProgramParticipantsByProgramNameAndPartName(java.lang.String,
     * java.lang.String)
     */
    public ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findByProgramAndParticipant")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("client", false);
            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
/*            log.debug("ERROR_PROGRAM_PARTICIPANT_GET: " + progName + "/"
                    + partName + " | " + e.getMessage());
*/            resPP = null;
        }

        return resPP;

    }

    public ProgramParticipant findProgramParticipantsByProgramNameAndPartName(
            String progName, String partName, int state)
            throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findAllByProgramAndParticipant")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("state", state).setParameter("client", false);

            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
            // 09.20.2010 Linda: DRMS-1665
            log.debug(LogUtils.createExceptionLogEntry(progName, partName, e));
            // log.debug("ERROR_PROGRAM_PARTICIPANT_GET: " + progName + "/" +
            // partName + " | " + e.getMessage());
            resPP = null;
        }

        return resPP;

    }

    public ProgramParticipant findClientProgramParticipantsByProgramNameAndPartName(
            String progName, String partName) throws AppServiceException {
        ProgramParticipant resPP;
        try {
            Query query = em
                    .createNamedQuery(
                            "ProgramParticipant.findByProgramAndParticipantForClient")
                    .setParameter("programName", progName)
                    .setParameter("participantName", partName)
                    .setParameter("client", true);
            resPP = (ProgramParticipant) query.getSingleResult();
        } catch (Exception e) {
            // 09.20.2010 Linda: DRMS-1665
            log.debug(LogUtils.createExceptionLogEntry(progName, partName, e));
            // log.debug("ERROR_PROGRAM_PARTICIPANT_GET_CLIENT: " + progName +
            // "/" + partName + " | " + e.getMessage());
            resPP = null;
        }

        return resPP;

    }

    public void removeClientParticipantFromProgram(String programName,
            String participantName) {
        try {
            if (programName == null) {
                String message = "program name is null";
                throw new EJBException(message);
            }
            if (participantName == null) {
                String message = "participant name is null";
                throw new EJBException(message);
            }

            ProgramParticipant pp = this
                    .findClientProgramParticipantsByProgramNameAndPartName(
                            programName, participantName);
            if (pp != null && pp.getUUID() != null)
                deleteProgramParticipant(pp);
        } catch (Exception ex) {
            String message = "error removing pariticpant " + participantName
                    + " from program " + programName;
            throw new EJBException(message, ex);
        }
    }

    public void removeParticipantFromProgram(String programName,
            String participantName) {
        try {
            if (programName == null) {
                String message = "program name is null";
                throw new EJBException(message);
            }
            if (participantName == null) {
                String message = "participant name is null";
                throw new EJBException(message);
            }

            ProgramParticipant pp = this
                    .findProgramParticipantsByProgramNameAndPartName(
                            programName, participantName);
            if (pp != null && pp.getUUID() != null)
                deleteProgramParticipant(pp);
        } catch (Exception ex) {
            String message = "error removing pariticpant " + participantName
                    + " from program " + programName;
            throw new EJBException(message, ex);
        }

    }

    protected Participant findPart(String participantName) {
        return participantEAO.findByNameAndClient(participantName, false);
    }

    public List<ProgramParticipant> findProgramParticipantsByParticipant(
            Participant p) {
        Query q = em.createNamedQuery("ProgramParticipant.findByParticipant");
        q.setParameter("participantName", p.getParticipantName());
        q.setParameter("client", p.isClient());
        return q.getResultList();
    }


	@Override
	public ProgramParticipant findRtpStrategyByProgAndPartiForClient(
			String programName, String participantName, boolean client) {
		Query q = em.createNamedQuery( "ProgramParticipant.findRtpStrategyByProgAndPartForClient" );
		q.setParameter("programName", programName);
		q.setParameter("participantName", participantName);
		q.setParameter("client", client);
		
		return (ProgramParticipant) q.getSingleResult();
		
	}


	@Override
	public List<ProgramParticipant> findProgramClientByProgramAndParticipant(
			String progName, List<String> participantNames){
		List<ProgramParticipant> resPP;
		if(participantNames==null||participantNames.isEmpty()) return new ArrayList<ProgramParticipant>();
	        try {
	            Query query = em
	                    .createNamedQuery(
	                            "ProgramParticipant.findProgramClientByProgramAndParticipant")
	                    .setParameter("programName", progName)
	                    .setParameter("participantNames", participantNames);
	            resPP = query.getResultList();
	        } catch (Exception e) {
	            log.debug(LogUtils.createExceptionLogEntry(progName, "", e));
	            resPP = null;
	        }

	        return resPP;
	}


	@Override
	public List<ProgramParticipant> findOptedOutProgramParticipants() {
		 Query namedQuery = em.createNamedQuery("ProgramParticipant.findOptedOutProgram");
         List<ProgramParticipant> list = namedQuery.getResultList();        
         return list;
	}
    
}