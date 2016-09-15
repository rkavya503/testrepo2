/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.ProgramManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.history.ClientParticipationStatus;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.matrix.ProgramMatrix;
import com.akuacom.pss2.program.matrix.ProgramMatrixGenEAO;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;
import com.akuacom.pss2.program.participant.Constraint;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.TreeNodeVo;
import com.akuacom.pss2.program.rtp.RTPPrice;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.RTPConfigEAO;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJB;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.pss2.season.SeasonConfigEAO;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;

/**
 * Stateless session bean providing a DRAS Entity BO facade.
 */
@Stateless
public class ProgramManagerBean implements ProgramManager.R, ProgramManager.L {
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ProgramManagerBean.class);

	/** The program servicer. */
	@EJB
	private ProgramEAO.L programEAO;

	@EJB
	private SeasonConfigEAO.L seasonEAO;
	
	@EJB
	private ProgramParticipantEAO.L ppEAO;

    @EJB
    private ProgramMatrixGenEAO.L programMatrixEAO;
    
    
	@EJB
	private ParticipantEAO.L participantEAO;
	
	@EJB
	private ContactEAO.L contactEAO;
    
	public String getProgramClassByName(String programName){
		return programEAO.getProgramClassByName(programName);
	}
	
	public String getProgramClassName(String programName){
		return programEAO.getProgramClassName(programName);
	}

	public Boolean isDrasBiddingByProgramName(String programName){
		return programEAO.isDrasBiddingByProgramName(programName);
	}
	
	
	public Integer getParticipantRowCountByProgramAndClient(String programName, boolean isClient){
		return participantEAO.getParticipantRowCountByProgramAndClient(programName, isClient);
	}


    public Integer getParticipantRowCountByProgramAndClientAndState(String programName, boolean isClient){
		return participantEAO.getParticipantRowCountByProgramAndClientAndState(programName, isClient);
	}

    
    
	public ProgramEJB lookupProgramBean(Program program) throws EJBException {
		if (program == null) {
            throw new ValidationException("no program found");
		}
		String classNameBase = program.getClassName();
		return lookupProgramBeanFromClassName(classNameBase);
	}
    
    

    //@EJB
    //private SCERTPProgramManager.L rptProgramManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.system.SystemManager#lookupProgramBean(java.lang.String)
	 */
	public ProgramEJB lookupProgramBean(String programName) throws EJBException {
		Program program = getProgramOnly(programName);
		if (program == null) {
            throw new ValidationException("no program named " + programName);
		}
		String classNameBase = program.getClassName();
		return lookupProgramBeanFromClassName(classNameBase);
	}

	/**
	 * Lookup program bean from class name.
	 * 
	 * @param classNameBase
	 *            the class name base
	 * 
	 * @return the program ejb
	 * 
	 * @throws EJBException
	 *             the EJB exception
	 */
	public ProgramEJB lookupProgramBeanFromClassName(String classNameBase)
			throws EJBException {
		try {
			final Class<?> aClass = Class.forName(classNameBase);
			return (ProgramEJB) EJBFactory.getBean(aClass);
		} catch (ClassNotFoundException e) {
			log.debug(LogUtils.createLogEntry("", getClass().getName(),
					"Can't find program ejb bean for: " + classNameBase, null));
			log.debug(LogUtils.createExceptionLogEntry("", getClass()
					.getName(), e));
			// log.fatal("Can't find program ejb bean for: " + classNameBase,
			// e);
			throw new EJBException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#createProgram(com.akuacom.pss2
	 * .core.model.Program)
	 */
	public Program createProgram(Program program) throws AppServiceException {
		return programEAO.createProgram(program);
	}

    public HashMap<String, Integer> getProgramPriority()
    {
        return programEAO.getProgramPriority();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#getProgramFromUtilityProgramName
	 * (java.lang.String)
	 */
	public Program getProgramFromUtilityProgramName(String utilityProgramName) {
		try {
			for (String programName : getPrograms()) {
				Program program = getProgramOnly(programName);
				if (utilityProgramName.equals(program.getUtilityProgramName())) {
					return program;
				}
			}
			throw new EJBException(
					"can't find program with utilityProgramName "
							+ utilityProgramName);
		} catch (Exception e) {
			String message = "error looking up utilityProgramName "
					+ utilityProgramName;
			log.debug(LogUtils.createExceptionLogEntry("", "", e));
			// log.fatal(message, e);
			throw new EJBException(message, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#updateProgram(com.akuacom.pss2
	 * .core.model.Program)
	 */
	public void updateProgram(Program program) {
		try {
			programEAO.updateProgram(program);
		} catch (AppServiceException e) {
			throw new EJBException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#removeProgram(java.lang.String)
	 */
	public void removeProgram(String programName) {
		try {
            Program program = getProgramWithParticipants(programName);
			if (program.getProgramParticipants().size() > 0) {
				throw new EJBException(
						"Program "
								+ programName
								+ " can't be deleted since it still has participants in it.");
			}
			removeProgramMatrix(program.getUUID());
			deleteRTPConfigs(program);
			programEAO.delete(program);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
	
	private void deleteRTPConfigs(Program program) {
		if (program.getClassName().contains("RTPProgram")) {
	        RTPConfigEAO rtpConfigEAO = EJBFactory.getBean(RTPConfigEAO.class);
	        rtpConfigEAO.deleteRTPConfigs(program.getProgramName());
		}
    }

	/**
	 * Removed Program Matrix for a particular program
	 * @param uuid Program UUID
	 */
	private void removeProgramMatrix(String uuid) throws  EntityNotFoundException{
		if(uuid != null){
			List<ProgramMatrix> pmList = programMatrixEAO.findMatrixForProgram(uuid);
			for(ProgramMatrix pm : pmList){
				programMatrixEAO.delete(pm);
			}
		}
		
		
		
	}
	
	
	public List<EventInfo> getEventsForProgram(Program program){
		ProgramEJB programEJB = lookupProgramBean(program);
		return programEJB.getEvents(program.getProgramName());

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#getEventsForProgram(java.lang
	 * .String)
	 */
	public List<EventInfo> getEventsForProgram(String programName) {
		ProgramEJB programEJB = lookupProgramBean(programName);
		return programEJB.getEvents(programName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.ProgramManager#saveProgramMatrixTrig(java.util
	 * .List)
	 */
	public void saveProgramMatrixTrig(List<ProgramMatrix> matrix) {
		if (matrix == null) {
			return;
		}

		ProgramMatrixTrig pmt = getProgramMatrixTrig();

		try {
			List<ProgramMatrix> existmatrix = pmt.getProgramMatrix();
			for (ProgramMatrix pm : existmatrix) {
				boolean removed = true;

				for (ProgramMatrix progM : matrix) {
					if ((pm.getProgram1UUID().equals(progM.getProgram1UUID()) && pm
							.getProgram2UUID().equals(progM.getProgram2UUID()))
							|| (pm.getProgram1UUID().equals(
									progM.getProgram2UUID()) && pm
									.getProgram2UUID().equals(
											progM.getProgram1UUID()))) {
						removed = false;
						break;
					}
				}

				if (removed) {
					String progName1 = (String) pmt.getPrograms().get(
							pm.getProgram1UUID());
					String progName2 = (String) pmt.getPrograms().get(
							pm.getProgram2UUID());
					List<ProgramParticipant> pps1 = ppEAO
							.findProgramParticipantsByProgramNames(progName1,
									progName2);
					if (pps1 != null && pps1.size() > 0) {
						throw new ValidationException(
								"ERROR_PROGRAM_MATRIX_REMOVE", progName1
										+ " and " + progName2);
					}
				}

                programMatrixEAO.delete(pm);
			}
			for (ProgramMatrix progM : matrix) {
                programMatrixEAO.create(progM);
            }
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException("ERROR_PROGRAMMATRIX_CREATE: "
					+ e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.program.ProgramManager#getProgramMatrixTrig()
	 */
	@SuppressWarnings({"unchecked"})
    public ProgramMatrixTrig getProgramMatrixTrig() {

		try {
            ProgramMatrixTrig triangle = new ProgramMatrixTrig();

            List<ProgramMatrix> matrix = programMatrixEAO.findAll();
            triangle.setProgramMatrix(matrix);

            HashMap<String,String> idNames = new HashMap<String,String>();
			List<Program> programs = programEAO.getAll();
			for (Program program : programs) {
				if(program.getState()<1){//deleted program
					continue;
				}
				String key = program.getUUID();
				String name = program.getProgramName();
				idNames.put(key, name);
			}
			triangle.setPrograms(idNames);
			return triangle;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(LogUtils.createExceptionLogEntry("", getClass()
					.getName(), e));
		}

		return null;
	}

	
	public String getUiScheduleEventString(String programName){
		return programEAO.getUiScheduleEventString(programName);
	}
	
	public List<String> getProgramRuleVariables(String programName) {
		ProgramEJB programEJB = lookupProgramBean(programName);
		return programEJB.getProgramRuleVariables(programName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.program.services.ProgramServicer#saveSeasonConfig(java.util
	 * .Set, java.lang.String)
	 */
	public void saveSeasonConfig(Set<SeasonConfig> value, String programName) {
		try {
			ProgramPerf program = getProgramPerf(programName);

			List<SeasonConfig> existing = seasonEAO.findSeasonConfigs(program
					.getUUID());
			for (SeasonConfig sConfig : existing) {
				boolean deleted = true;
				for (SeasonConfig season : value) {
					if (sConfig.getUUID().equals(season.getUUID())) {
						deleted = false;
						break;
					}
				}
				if (deleted) {
					seasonEAO.delete(sConfig.getUUID());
				}
			}
			for (SeasonConfig season : value) {

				if (season.getUUID() != null && !season.getUUID().isEmpty()) {
					SeasonConfig sc = seasonEAO.get(season.getUUID());
					sc.setStartDate(season.getStartDate());
					sc.setEndDate(season.getEndDate());
					sc.setName(season.getName());
					seasonEAO.updateSeasonConfig(sc);
				} else {
					if (programName != null && !programName.isEmpty()) {
						Program p = getProgramOnly(programName);
						if (p != null) {
							season.setProgramVersionUuid(p.getUUID());
						}
					}
					seasonEAO.createSeasonConfig(season);
				}
			}

		} catch (Exception e) {
			throw new EJBException("ERROR_SEASONCONFIG_UPDATE", e);
		}
	}

	public List<SeasonConfig> findSeasonConfigs(String programName) {
		ProgramManager programManager = EJBFactory
				.getBean(ProgramManager.class);
		ProgramPerf program = programManager.getProgramPerf(programName);

		return seasonEAO.findSeasonConfigs(program.getUUID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.system.SystemManager#getSeason(java.lang.String,
	 * java.util.Date)
	 */
	public String getSeason(String programName, Date date) {
		try {
			ProgramEJB programEJB = lookupProgramBean(programName);
			if (programEJB instanceof SCERTPProgramEJB) {
				SCERTPProgramEJB srejb = (SCERTPProgramEJB) programEJB;
				return srejb.getSeason(programName, date);
			} else {
				throw new EJBException(
						"method getRate is not supported for the program: "
								+ programName);
			}
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
	
	public List<SignalDef> findSignals(String programName) {
		List<SignalDef> signal=new ArrayList<SignalDef>();
		Program program = programEAO.getProgramWithSignals(programName);
		Set<ProgramSignal> proSignals = program.getSignals();
		for(ProgramSignal ps:proSignals){
			signal.add(ps.getSignalDef());
		}
		return signal;
	}

	public void setProgram(Program program) {
		programEAO.merge(program);
	}

	public void setRealTimePrice(RTPPrice price) {
		programEAO.setRealTimePrice(price);
	}

	public RTPPrice getLastRealTimePrice(String programName) {
		return programEAO.getLastRealTimePrice(programName);
	}

	public List<RTPPrice> getCurrentRealTimePrices(String programName) {
		return programEAO.getCurrentRealTimePrices(programName);
	}

	public Program copyProgram(String uuid, String newProgramName) {
		try {
			Program existing = programEAO.getById(uuid);
			Program newOne = existing.copy(existing, newProgramName);
			log.info("newOne " + newOne.getUUID());
			int before = existing.getPriority();
			// newOne = programEAO.markPriorityForAddAfter(before, newOne);

			List<Program> programList = programEAO
					.getProgramsForAddAfter(before);
			for (Program p : programList) {
				p.setPriority(p.getPriority() + 1);
				programEAO.markPriority(p);
			}

			newOne.setPriority(before + 1);

            log.info("newOne now " + newOne.getUUID());
			newOne = programEAO.create(newOne);
			log.debug("existing " + existing + " id " + existing.getUUID());
			log.debug("newOne " + newOne + " id " + newOne.getUUID());

            String existingProgramName = existing.getProgramName();
            List<SeasonConfig> scs = findSeasonConfigs(existingProgramName);
            if (scs != null) {
                Set<SeasonConfig> scSet = new HashSet<SeasonConfig>();
                for (SeasonConfig sc : scs) {
                    SeasonConfig n = new SeasonConfig();
                    n.setProgramVersionUuid(newOne.getUUID());
                    n.setEndDate(sc.getEndDate());
                    n.setName(sc.getName());
                    n.setStartDate(sc.getStartDate());
                    scSet.add(n);
                }
                saveSeasonConfig(scSet, newOne.getProgramName());
            }


            SCERTPProgramManager rptProgramManager = EJBFactory.getBean(SCERTPProgramManager.class);
            List<RTPConfig> rtpConfigs = rptProgramManager.findRTPConfigs(existingProgramName);
            if (rtpConfigs != null) {
                List<RTPConfig> rtpConfigList = new ArrayList<RTPConfig>();
                for (RTPConfig config : rtpConfigs) {
                    RTPConfig n = new RTPConfig();
                    n.setProgramVersionUuid(newOne.getUUID());
                    n.setEndTemperature(config.getEndTemperature());
                    n.setEndTime(config.getEndTime());
                    n.setName(config.getName());
                    n.setRate(config.getRate());
                    n.setSeasonName(config.getSeasonName());
                    n.setStartTemperature(config.getStartTemperature());
                    n.setStartTime(config.getStartTime());
                    n.setUnit(config.getUnit());
                    rtpConfigList.add(n);
                }
                rptProgramManager.saveRTPConfig(rtpConfigList, newOne.getProgramName());
            }


			copyProgramMatrix(existing.getUUID(), newOne.getUUID());
			
            try {
                ProgramEJB programEJB = lookupProgramBean(newOne.getProgramName());
                programEJB.createTimer(newOne.getProgramName());
            } catch (Exception e) {
                log.error(e);
            }

			return newOne;
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void copyProgramMatrix(String uuid, String newProgramUUID) {
		List<ProgramMatrix> pms = getProgramMatrixTrig()
				.getProgramMatrix();
		List<ProgramMatrix> newlist = new ArrayList<ProgramMatrix>();
		for (ProgramMatrix pm : pms) {
			if (pm.getProgram1UUID().equals(uuid)) {
				ProgramMatrix pme = new ProgramMatrix();

				pme.setProgram1UUID(newProgramUUID);
				pme.setProgram2UUID(pm.getProgram2UUID());
				pme.setCoexist(true);
				newlist.add(pme);
			} else if (pm.getProgram2UUID().equals(uuid)) {
				ProgramMatrix pme = new ProgramMatrix();

				pme.setProgram1UUID(pm.getProgram1UUID());
				pme.setProgram2UUID(newProgramUUID);
				pme.setCoexist(true);
				newlist.add(pme);
			}
		}

		for (ProgramMatrix matrix : newlist) {
            try {
                programMatrixEAO.create(matrix);
            } catch (DuplicateKeyException e) {
                throw new EJBException(e);
            }
        }
	}

	
	public ProgramPerf getProgramPerf(String programName) {
		return programEAO.getProgramPerf(programName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.core.this#getPrograms()
	 */
	public List<String> getPrograms() {
		try {

			return programEAO.findProgramNames();

		} catch (Exception ex) {
			String message = "error getting programs";
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akuacom.pss2.core.this#getProgram(java.lang.String)
	 */
	@Deprecated //too expensive
	public Program getProgram(String programName) {
		if (programName == null) {
			String message = "program name is null";
			throw new EJBException(message);
		}

		try {
			return programEAO.getProgram(programName);
		} catch (Exception ex) {
			String message = "error getting program " + programName;
			throw new EJBException(message, ex);
		}
	}

	public Program getProgramOnly(String programName){
		try {
			return programEAO.getProgramOnly(programName);
		} catch (Exception ex) {
			String message = "error getting program " + programName;
			throw new EJBException(message, ex);
		}
	}
	
	public List<Program> getProgramsAsPrograms() {
        try {
            return programEAO.getProgramsHydratePrgmParts();
        } catch (AppServiceException ex) {
			String message = "error getting all programs";
			throw new EJBException(message, ex);
        }
	}
	public List<Program> getProgramsAsProgramsLazy() {
        try {
            return programEAO.getProgramsLazy();
        } catch (AppServiceException ex) {
			String message = "error getting all programs";
			throw new EJBException(message, ex);
        }
	}
    public List<Program> getProgramsAsProgramsByPriorities() {
        try {
            return programEAO.getProgramsHydratePrgmPartsByPriority();
        } catch (AppServiceException ex) {
			String message = "error getting all programs";
			throw new EJBException(message, ex);
        }
	}

	public Program refreshProgram(String programName) {
		return programEAO.refreshProgram(programName);
	}
	
    public int getNextPriority(){
	    final Integer max = programEAO.getMaxPriority();
	     
	    return (max+1);
    }

	@Override
	public List<Program> findProgramsByProgramClass(List<String> programClasses) throws AppServiceException {
		List<Program> programs=new ArrayList<Program>();
		programs=programEAO.findProgramsByProgramClass(programClasses, true);
		
		return programs;
	}

	@Override
	public List<Program> findProgramsByLongProgramName(String longProgramName) throws AppServiceException {
		List<Program> programs=new ArrayList<Program>();
		programs=programEAO.findProgramsByLongProgramName(longProgramName);
			
		return programs;
	}
	
	/**
     * confirm the event participants are ok
     * <p>
     * Call setProgram before calling this method
     * <p>
     * empty list == no errors.
     * 
     * @param event
     *            The event to validate
     * 
     * @throws ProgramValidationException
     *             Thrown if the event breaks a rule. Contains a list of
     *             violations wrapped in ProgramValidationMessages.
     */
	@Override
    public Set<EventParticipant> filterEventParticipants(String programName) {
    	Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();
    	if (programName!=null && programName.length()>0){
    		//need program participants
	    	Program program = this.getProgramWithParticipantsAndPRules(programName);
	    	eventParticipants = this.filterEventParticipants(program);
    	}
    	return eventParticipants;
    	
    }
	
	/**
     * confirm the event participants are ok
     * <p>
     * Call setProgram before calling this method
     * <p>
     * empty list == no errors.
     * 
     * @param event
     *            The event to validate
     * 
     * @throws ProgramValidationException
     *             Thrown if the event breaks a rule. Contains a list of
     *             violations wrapped in ProgramValidationMessages.
     */
	@Override
    public Set<EventParticipant> filterEventParticipants(Program program) {
    	Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();
    	if (program.getProgramName()!=null && program.getProgramName().length()>0){
	    	ProgramParticipantManager programParticipantManager =EJBFactory.getBean(ProgramParticipantManager.class);
	    	List<Participant> programParticipant = programParticipantManager.getParticipantsForProgramAsObject(program.getProgramName());
	    	for(Participant participant:programParticipant){
	    		EventParticipant eventParticipant = new EventParticipant();
				eventParticipant.setParticipant(participant);
				eventParticipants.add(eventParticipant);
	    	}
	    	
	    	eventParticipants = this.filterEventParticipants(eventParticipants, program, null);
    	}
    	return eventParticipants;
    	
    }
    
    /**
     * confirm the event participants are ok
     * <p>
     * Call setProgram before calling this method
     * <p>
     * empty list == no errors.
     * 
     * @param event
     *            The event to validate
     * 
     * @throws ProgramValidationException
     *             Thrown if the event breaks a rule. Contains a list of
     *             violations wrapped in ProgramValidationMessages.
     */
    @SuppressWarnings("deprecation")
    @Override
    public Set<EventParticipant> filterEventParticipants(
    	Set<EventParticipant> eventParticipants, Program programWithParticipants,
    	EventTiming eventTiming, boolean eventCreation) {

        ProgramParticipantAggregationManager prgmPartAggManager = EJBFactory
                .getBean(ProgramParticipantAggregationManager.class);

        boolean constraintActived = false;
        StringBuilder sb = new StringBuilder();

        // create a set of participants to delete
        Map<String, ProgramParticipant> ppMap = 
        	new HashMap<String, ProgramParticipant>();

        for (ProgramParticipant pp : programWithParticipants.getProgramParticipants()) {
            ppMap.put(pp.getParticipantName(), pp);
        }

        Map<String, ClientParticipationStatus> deleteSet = new HashMap<String, ClientParticipationStatus>();
        for(EventParticipant eventParticipant: eventParticipants) {
            Participant participant = eventParticipant.getParticipant();
            if (participant.isClient()) {
                continue;
            } 
            
            ProgramParticipant programParticipant = 
            	ppMap.get(participant.getParticipantName());
            
            if(programParticipant == null)
            {
            	// had to merge this from 6.6 and i'm not sure if i'm setting the 
            	// ClientParticipationStatus correctly here
            	deleteSet.put(participant.getParticipantName(), ClientParticipationStatus.PROGRAM_PARTICIPANT_OPT_OUT);
            	continue;
            }

            if (programParticipant.getParticipant().getOptOut()) {
                constraintActived = true;
                sb.append("participant opt out: ");
                sb.append(participant.getParticipantName());
                sb.append("\n");
            	addParticipantAndDecendentsToDeleteSet(eventParticipant, 
                	deleteSet, prgmPartAggManager, ppMap, sb, ClientParticipationStatus.PARTICIPANT_OPT_OUT);
                continue;
            }
            
            if (programParticipant.getOptStatus() != null && 
            		programParticipant.getOptStatus().intValue() != 0 ) {
                constraintActived = true;
                sb.append("program participant opt out: ");
                sb.append(participant.getParticipantName());
                sb.append("\n");
            	addParticipantAndDecendentsToDeleteSet(eventParticipant, 
                	deleteSet, prgmPartAggManager, ppMap, sb, ClientParticipationStatus.PROGRAM_PARTICIPANT_OPT_OUT);
                continue;
            }
            
            if(eventTiming == null){
            	continue;
            }
            
            Constraint constraint = programParticipant.getConstraint();
            if (constraint == null) {
                continue;
            }  
            
            if (constraint.getInvalidDates() != null
                    && constraint.getInvalidDates().contains(
                            dateOnly(eventTiming.getStartTime()))) {
                constraintActived = true;
                sb.append("opt out date violation: ");
                sb.append(participant.getParticipantName());
                sb.append(": rejected\n");
                addParticipantAndDecendentsToDeleteSet(eventParticipant, 
                	deleteSet, prgmPartAggManager, ppMap, sb, 
                	ClientParticipationStatus.PROGRAM_PARTICIPANT_CONSTRAINTS_INVALID_DATE_VIOLATION);
                continue;
            }
            // validate active window
            if (eventDate(eventTiming, constraint.getMaxActive()) < eventTiming
                    .getStartTime().getTime()
                    || eventDate(eventTiming, constraint.getMinActive()) > eventTiming
                            .getEndTime().getTime()) {
                constraintActived = true;
                sb.append("active window violation ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMinActive()));
                sb.append(" to ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMaxActive()));
                sb.append(": ");
                sb.append(participant.getParticipantName());
                switch (constraint.getActiveAction()) {
                case REJECT:
                    sb.append(": rejected\n");
	                addParticipantAndDecendentsToDeleteSet(eventParticipant, 
	                	deleteSet, prgmPartAggManager, ppMap, sb,
	                	ClientParticipationStatus.PROGRAM_PARTICIPANT_CONSTRAINTS_ACTIVE_WINDOW_VIOLATION);
                    continue;
                default:
                    sb.append(": accepted\n");
                    break;
                }
            }
            // validate notification window
            long notifyLeadMS = eventTiming.getStartTime().getTime()
                    - eventTiming.getIssuedTime().getTime();
            long minNotifyLeadMS = ((constraint.getMinNotify().getHours() * 60) + constraint
                    .getMinNotify().getMinutes()) * 60 * 1000;
            long maxNotifyLeadMS = ((constraint.getMaxNotify().getHours() * 60) + constraint
                    .getMaxNotify().getMinutes()) * 60 * 1000;
            if (notifyLeadMS < minNotifyLeadMS
                    || notifyLeadMS > maxNotifyLeadMS) {
                constraintActived = true;
                sb.append("notify window violation: ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMinNotify()));
                sb.append(" to ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMaxNotify()));
                sb.append(": ");
                sb.append(participant.getParticipantName());
                switch (constraint.getNotifyAction()) {
                case REJECT:
                    sb.append(": rejected\n");
	                addParticipantAndDecendentsToDeleteSet(eventParticipant, 
	                	deleteSet, prgmPartAggManager, ppMap, sb,
	                	ClientParticipationStatus.PROGRAM_PARTICIPANT_CONSTRAINTS_NOTIFY_WINDOW_VIOLATION);
                    continue;
                default:
                    sb.append(": accepted\n");
                    break;
                }
            }
            // validate event duration window
            long durationMS = eventTiming.getEndTime().getTime()
                    - eventTiming.getStartTime().getTime();
            long minDurationMS = ((constraint.getMinDuration().getHours() * 60) + constraint
                    .getMinDuration().getMinutes()) * 60 * 1000;
            long maxDurationMS = ((constraint.getMaxDuration().getHours() * 60) + constraint
                    .getMaxDuration().getMinutes()) * 60 * 1000;
            if (durationMS < minDurationMS || durationMS > maxDurationMS) {
                constraintActived = true;
                sb.append("duration window violation: ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMinDuration()));
                sb.append(" to ");
                sb.append(DateUtil.dateFormatHHmm().format(constraint.getMaxDuration()));
                sb.append(": ");
                sb.append(participant.getParticipantName());
                switch (constraint.getDurationAction()) {
                case REJECT:
                    sb.append(": rejected\n");
	                addParticipantAndDecendentsToDeleteSet(eventParticipant, 
	                	deleteSet, prgmPartAggManager, ppMap, sb, 
	                	ClientParticipationStatus.PROGRAM_PARTICIPANT_CONSTRAINTS_DURATION_WINDOW_VIOLATION);
                    continue;
                default:
                    sb.append(": accepted\n");
                    break;
                }
            }
        }
        
        // now actually delete them
        // using an interator here so we can do remove
        Iterator<EventParticipant> deleteEventParticipants = eventParticipants.iterator();
        while (deleteEventParticipants.hasNext()) {
            EventParticipant eventParticipant = deleteEventParticipants.next();
            if(deleteSet.keySet().contains(
            	eventParticipant.getParticipant().getParticipantName()))
            {
            	deleteEventParticipants.remove();
            }
        }

        if (constraintActived) {
            log.info(LogUtils.createLogEntry(programWithParticipants.getProgramName(),
                    LogUtils.CATAGORY_EVENT, "participant constraints",
                    sb.toString()));
        }
        
        // report non participated participants
        if (eventCreation && constraintActived) {
        	if (eventParticipants.size()>0) {
        		String eventName=eventParticipants.iterator().next().getEvent().getEventName();
        		
                CustomerReportManager customerReportManager = EJBFactory.getBean(CustomerReportManager.class);
                customerReportManager.reportClientNonParticipation(eventName, deleteSet, programWithParticipants);
        	}
        }
        
        return eventParticipants;
    }
    
    /**
     * Convert a hh:mm date into a full date using the event start time
     * 
     * @param event
     *            the event
     * @param date
     *            the hh:mm date
     * @return the date in ms
     */
    private long eventDate(EventTiming event, Date date) {
        GregorianCalendar newCal = new GregorianCalendar();
        newCal.setTime(event.getStartTime());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        newCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        newCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        newCal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        newCal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));

        return newCal.getTimeInMillis();
    }

    private void addParticipantAndDecendentsToDeleteSet(
    	EventParticipant eventParticipant, Map<String, ClientParticipationStatus> deleteSet,
    	ProgramParticipantAggregationManager prgmPartAggManager,
    	Map<String, ProgramParticipant> ppMap, StringBuilder sb,
    	ClientParticipationStatus status)
    {
    	String participantName = eventParticipant.getParticipant().getParticipantName();
    	deleteSet.put(participantName, status);
    	

    	ProgramParticipant programParticipant = ppMap.get(participantName);
    	for(ProgramParticipant decendent: 
    		prgmPartAggManager.getFlatDescendants(programParticipant))
    	{
            sb.append("participant removed because of parent contraint: ");
            sb.append(eventParticipant.getParticipant().getParticipantName());
            sb.append("\n");
        	deleteSet.put(decendent.getParticipantName(), status);
    	}
    }

    /**
     * Strip the hh:ss.sss off a time and return it.
     * 
     * @param date
     *            the date
     * @param date
     *            the hh:mm date
     * @return the date in ms
     */
    private Date dateOnly(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

	@Override
	public Set<EventParticipant> filterEventParticipants(
			Set<EventParticipant> eventParticipants, Program program,
			EventTiming eventTiming) {
		return this.filterEventParticipants(eventParticipants, program, eventTiming, false);
	}
	
	@Override
	public List<Contact> getOperatorContacts(String programName) {
		return contactEAO.getOperatorContacts(programName);
	}
	
//	public Program getProgramWithContacts(String  programName){
//		return programEAO.getProgramPerfWithContact(programName);
//	}
	
	public Program getProgramWithLoadBid(String  programName){
		return programEAO.getProgramWithBidConfig(programName);
	}
	
	public Program getProgramWithParticipants(String programName){
		return programEAO.getProgramWithParticipants(programName);
	}
	
	public Program getProgramWithSignals(String programName){
		return programEAO.getProgramWithSignals(programName);
	}

	@Override
	public Program getProgramWithRules(String programName) {
		return programEAO.getProgramWithRules(programName);
	}
	
	@Override
	public Program getProgramWithAllConfig(String programName) {
		return programEAO.getProgramWithAllConfig(programName);	
	}

	public Program getProgramWithParticipantsAndPRules(String programName) {
		return programEAO.getProgramWithParticipantsAndPRules(programName);
	}

	public List<Program> getAllPrograms() {
		return programEAO.getAllPrograms();
	}

	@Override
	public Program getProgramWithBidAndProgramParticipantsAndPRules(
			String programName) {
		return programEAO.getProgramWithBidAndProgramParticipantsAndPRules(programName);
	}
	
	@Override
	public Program findProgramAndRulesdByProgramName(String programName){
		return programEAO.findProgramAndRulesdByProgramName(programName);	
	}

	@Override
	public List<SeasonConfig> findSeasonConfigsByProgramId(String programUUID) {
		return seasonEAO.findSeasonConfigs(programUUID);
	}

	@Override
	public List<SignalDef> findSignalsPerf(String programName) {
		List<SignalDef> signal=new ArrayList<SignalDef>();
		Program program = programEAO.findProgramWithSignalsPerf(programName);
		Set<ProgramSignal> proSignals = program.getSignals();
		for(ProgramSignal ps:proSignals){
			signal.add(ps.getSignalDef());
		}
		return signal;
	}

	@Override
	public Program findProgramPerfByProgramName(String programName) {
		return programEAO.findProgramPerfByProgramName(programName);	
	}

	@Override
	public List<TreeNodeVo> getChildren(String participantName,
			String programName) {
		return ppEAO.getChildren(participantName, programName);
	}

	@Override
	public List<Participant> getDescendantClients(String participantName,
			String programName) {
		return ppEAO.getDescendantClients(participantName, programName);
	}

	@Override
	public List<Participant> getSiblingClients(String participantName,
			String programName) {
		return ppEAO.getSiblingClients(participantName, programName);
	}
	
	@Override
	public Program getBySecondaryUtilityName(String secondaryUtilityName) {
		return programEAO.getBySecondaryUtilityName(secondaryUtilityName);	
	}
	
	 public List<ProgramParticipant> findProgramClientByProgramAndParticipant(
	            String progName, List<String> partNames) {
	            List<ProgramParticipant> list = ppEAO.findProgramClientByProgramAndParticipant(progName,partNames);
	            return list;
	    }

	@Override
	public List<Program> getProgramsWithParticipants(List<String> programNames) {
		// TODO Auto-generated method stub
		if(programNames==null||programNames.isEmpty()){
			return new ArrayList<Program>();
		}
		return programEAO.getProgramsWithParticipants(programNames);
	}

	@Override
	public Program getProgramWithParticipantsAndPRules(String programName,List<String> partList) {
		return programEAO.getProgramWithParticipantsAndPRules(programName,partList);
	}
}