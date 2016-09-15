/**
 *
 */
package com.akuacom.pss2.program.participant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorResourceUtil;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.email.MessageEntity;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.email.UndeliveredEmailManagerBean;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.task.TimerConfigEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.LogUtils;
import com.kanaeki.firelog.util.FireLogEntry;

/**
 * @author Aaron Roller
 * @see ProgramParticipant
 */
@Stateless
public class ProgramParticipantManagerBean extends TimerManagerBean implements
        ProgramParticipantManager.R, ProgramParticipantManager.L {

    private static final Logger log = Logger
            .getLogger(ProgramParticipantManagerBean.class);

    /** The participant manager. */
    @EJB
    private ParticipantManager.L participantManager;

    @EJB
    private ClientManager.L clientManager;

    @EJB
    private ProgramManager.L programManager;

    @EJB
    protected Notifier.L notifier;
    
    @EJB
    private ProgramParticipantEAO.L ppEAO;

    @EJB
    private ProgramClientEAO.L pcEAO;
    
    @Resource
	private TimerService timerService;
    
    @Override
    public void cancelTimers() {
		Collection<?> timersList = timerService.getTimers();
		for (Object timer : timersList) {
			if (timer instanceof Timer) {
				Logger.getLogger(this.getClass()).debug(
						"" + ((Timer) timer).getInfo() + " was [Stopped].");
				((Timer) timer).cancel();

			}
		}
	}
	
    @Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		StringBuffer sb = new StringBuffer();
		// Timers that this bean manages
		for (Object timer : timersList) {
			if (timer instanceof Timer) {

				sb.append(((Timer) timer).getClass() + " Timer ["
						+ ((Timer) timer).getInfo().toString() + "] next run ["
						+ ((Timer) timer).getNextTimeout() + "]\n");

			}
		}
		return sb.toString();
	}

    
	 public void scheduleTimer() {
									
			final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
	                .getBean(com.akuacom.pss2.system.SystemManager.class);
	        Integer multipleDataEnable=systemManager.getPss2Features().getOptOutTimeOut();
	        
			Date d= new Date();
			Calendar invokeTime = Calendar.getInstance();
			invokeTime.setTime(d);
			invokeTime.set(Calendar.HOUR_OF_DAY, multipleDataEnable);
			invokeTime.set(Calendar.MINUTE, 01);
			d=invokeTime.getTime();
			
			
			Date date=new Date();
			if(d.getTime()<date.getTime()){
				invokeTime.add(Calendar.DATE, 1);
				d=invokeTime.getTime();
				
			}
			timerService.createTimer(d,
					24*60*60*1000, ProgramParticipantManagerBean.class.getName());
			log.info("Opt-Out timer is scheduled to start @ "+d);
			
		}
	 
    @Override
	 public void createTimers() {
			scheduleTimer();
		}
	 
    	@Override
		@Timeout
		public void timeoutHandler(Timer timer) {
			List<ProgramParticipant> pps = getOptedOutProgramParticipants();
			for(ProgramParticipant pp:pps){
				pp.setOptStatus(0);
				ppEAO.merge(pp);
				Participant participant = pp.getParticipant();
				Set<ParticipantContact> contacts = participant.getContacts();
				sendOptInNotifications(contacts, pp.getProgramName(), participant.getParticipantName() );
				log.info(participant.getParticipantName()+" has opted-in on expiry of program opt-out");
			}
			
		}

    	protected void sendOptInNotifications(Set<ParticipantContact> contacts, String programName, String participantName) {
            
        	final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
	                .getBean(com.akuacom.pss2.system.SystemManager.class);
        	
            PSS2Properties pss2Props = systemManager.getPss2Properties();
           
           String subject = "You have been opted back into " + programName +" Program";
           String content = "The system has automatically opted you back into " + programName + " program. Your Program Optout duration has Expired. Log into Customer portal to change the preferences or Call Customer care for further assistance.";
           

           String emailContentType = pss2Props.getEmailContentType();
           
           String result;
           try {
               CoreProperty emailFromAddress = EJB3Factory.getBean(CorePropertyEAO.class).getByPropertyName("emailFromAddress");
               String stringValue = emailFromAddress.getStringValue();
               InternetAddress.parse(stringValue);
               result = stringValue;
           } catch (EntityNotFoundException e) {
               result = "noreply@openadr.com";
           } catch (AddressException e) {
               result = "noreply@openadr.com";
           }
           
           for (ParticipantContact pContacts: contacts)
           {
        	  /* Contact participantContactAsContact = pContacts.getParticipantContactAsContact();*/
        	   	if (pContacts.getType().equalsIgnoreCase("Email Address")) { 
	        	   notifier.sendMail(pContacts.getAddress(), result, subject, 
	        	    		content, emailContentType,
	        	    		participantName, null, null,  
	        	            MessageEntity.PRIORITY_URGENT, pContacts.getUUID() );
	        	   	}
        	   }
           }
     	    	
    
    private List<ProgramParticipant> getOptedOutProgramParticipants() {
    	
    	List<ProgramParticipant> pps = ppEAO.findOptedOutProgramParticipants();
			return pps;
		}

	public void addProgramParticipant(String programName, List<Participant> participants,
            boolean isClient){
        if (programName != null && participants != null) {
            try {
                if (isClient) {
                    pcEAO.createProgramParticipants(programName,
                    		participants, isClient);
                } else {
                    ppEAO.createProgramParticipants(programName,
                    		participants, isClient);
                }
            } catch (Exception ex) {
                String message = "error adding participants to program " + programName;
                log.error(LogUtils.createLogEntryUser(programName,
                        "participants", LogUtils.CATAGORY_CONFIG_CHANGE,
                        message, null));
                throw new EJBException(message, ex);
            }

        }
    	
    }

    
    
    public void addProgramParticipant(String programName,
            String participantName, boolean isClient) {

        if (programName != null && participantName != null) {
            try {
                if (isClient) {
                    pcEAO.createProgramParticipant(programName,
                            participantName, isClient);
                } else {
                    ppEAO.createProgramParticipant(programName,
                            participantName, isClient);
                }
            } catch (Exception ex) {
                String message = "error adding " + participantName + " to program " + programName;
                // TODO 2992
                log.error(LogUtils.createLogEntryUser(programName,
                        participantName, LogUtils.CATAGORY_CONFIG_CHANGE,
                        message, null));
                throw new EJBException(message, ex);
            }

        }
    }

    /**
     * Sets the program participant.
     *
     * @param pp
     *            the pp
     *
     * @param isClient flag
     * @return the program participant
     */
    public ProgramParticipant setProgramParticipant(ProgramParticipant pp,
            boolean isClient) {

        pp = ppEAO.merge(pp);
        return pp;
    }
    

    
    public void setProgramParticipants(List<ProgramParticipant> ppList){
    	ppEAO.mergeAll(ppList);
    }
    
    public void addParticipantToProgram(Program program,
    		List<Participant> part, boolean isClient, ProgramEJB programEJB) {
    	String programName = program.getProgramName();
    	ProgramMatrixTrig pmt = programManager.getProgramMatrixTrig();

        try {
            ProgramValidator pv = ValidatorFactory.getProgramValidator(program);
            for(Participant p : part){
            	pv.validateProgramParticipant(p, pmt);
            }
                    
            programEJB.addParticipant(programName, part, isClient);
        } catch (Exception e) {
            throw new EJBException("add participant failed: " + programName,e);
        }
    	
    }

    
    
    public void addParticipantToProgram(Program program,
    		Participant part, boolean isClient, ProgramEJB programEJB) {
    	String programName = program.getProgramName();
    	String participantName = part.getParticipantName();

        try {
        	if(program instanceof DBPProgram){
        		program =programManager.getProgramWithLoadBid(programName);
        	}
        	
            ValidatorFactory.getProgramValidator(program)
                    .validateProgramParticipant(part);
            programEJB.addParticipant(programName, participantName, isClient);
            
        } catch (Exception e) {
            throw new EJBException("add participant failed: " + programName,e);
        }
    	
    }
    
    

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#addParticipantToProgram(java.
     * lang.String, java.lang.String)
     */
    public void addParticipantToProgram(String programName,
            String participantName, boolean isClient) {
        ProgramEJB programEJB = programManager.lookupProgramBean(programName);

        try {
            final Program program = programManager.getProgramOnly(programName);
            // todo get participant
            Participant part = participantManager.getParticipant(
                    participantName, isClient);
            ValidatorFactory.getProgramValidator(program)
                    .validateProgramParticipant(part);
            programEJB.addParticipant(programName, participantName, isClient);
        } catch (Exception e) {
            throw new EJBException("add participant failed: " + programName,e);
        }

    }

    /**
     * Finds if participant is in the program
     *
     * @param programName
     *            Program Name
     * @param participantName
     *            Participant Name
     * @param isClient
     *            if it is a client then true
     * @return result
     */
    private boolean isParticipantInProgram(String programName,
            String participantName, boolean isClient) {

        if (programName != null && participantName != null) {
            try {
                ProgramParticipant pp;
                if (isClient) {
                    pp = pcEAO.findProgramParticipantsByProgramNameAndPartName(
                            programName, participantName);
                } else {
                    pp = ppEAO.findProgramParticipantsByProgramNameAndPartName(
                            programName, participantName);
                }
                return pp != null;
            } catch (Exception e) {
                log.error(LogUtils.createLogEntryUser(programName,
                        participantName, LogUtils.CATAGORY_CONFIG_CHANGE,
                        e.getMessage(), null));
                return false;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#addParticipantsToProgram(java
     * .lang.String, java.util.List)
     */
    public void addParticipantsToProgram(String programName,
            List<String> pariticipantNames, boolean isClient) {
        try {
            Program existProg = programManager.getProgramOnly(programName);
            if (existProg == null) {
                throw new ValidationException(
                        ErrorResourceUtil.getErrorMessage(
                                "ERROR_UPDATE_PROGRAM_NOT_EXIST", programName));
            }

            for (String partName : pariticipantNames) {
                if (!isParticipantInProgram(programName, partName, isClient)) {
                    addParticipantToProgram(programName, partName,
                            isClient);
                }
            }
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#getParticipantsForProgram(java
     * .lang.String)
     */
    public List<String> getParticipantsForProgram(String programName) {
        ProgramEJB programEJB = programManager.lookupProgramBean(programName);
        return programEJB.getParticipants(programName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#getParticipantsForProgramAsObject
     * (java.lang.String)
     */
    public List<Participant> getParticipantsForProgramAsObject(
            String programName) {
        List<Participant> parts = new ArrayList<Participant>();
        try {
            List<ProgramParticipant> pps = ppEAO
                    .findProgramParticipantsByProgramName(programName);
            for (ProgramParticipant pp : pps) {
                parts.add(pp.getParticipant());

            }
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return parts;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#getParticipantsForProgramAsObject
     * (java.lang.String)
     */
    public List<ProgramParticipant> getProgramParticipantsForProgramAsObject(String programName) {
    	List<ProgramParticipant> pps = new ArrayList<ProgramParticipant>();
        try {
            pps = ppEAO.findAllProgramParticipantsByProgramName(programName);
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return pps;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#removeParticipantFromProgram(
     * java.lang.String, java.lang.String)
     */
    private void removeParticipantFromProgram(String programName,
            String participantName, boolean isClient) {
        // first remove the clients for the program
        // TODO: this should be moved down into the EAO layer i think
        for (Participant client : clientManager.getClients(participantName)) {
            pcEAO.removeClientParticipantFromProgram(programName,
                    client.getParticipantName());
        }

        ppEAO.removeParticipantFromProgram(programName, participantName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#removeParticipantsToProgram(java
     * .lang.String, java.util.List)
     */
    public void removeParticipantsFromProgram(String programName,
            List<String> pariticipantNames, boolean isClient) {
        try {
            Program existProg = programManager.getProgramOnly(programName);
            if (existProg == null) {
                throw new ValidationException(
                        ErrorResourceUtil.getErrorMessage(
                                "ERROR_UPDATE_PROGRAM_NOT_EXIST", programName));

            }

            for (String partName : pariticipantNames) {
                if (isParticipantInProgram(programName, partName, isClient)) {
                    removeParticipantFromProgram(programName, partName,
                            isClient);
                }
            }
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    
    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#getProgramParticipant(java.lang
     * .String, java.lang.String)
     */
    public List<ProgramParticipant> getProgramParticipantsByParent(String programName,
            String parentPartipantName, boolean isClient){
    	
    	List<ProgramParticipant> ppList = null;
    	
        try {
        	ppList = ppEAO.findByProgramAndParentParticipantForClient(programName,parentPartipantName, isClient);
        
        } catch (javax.persistence.NoResultException e) {
            return ppList;
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_BY_PARENT_GET: "
                    + programName + "/" + parentPartipantName + " | "
                    + e.getMessage(), e);
        }
    	return ppList;
    }
    

    
    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#getProgramParticipant(java.lang
     * .String, java.lang.String)
     */
    public ProgramParticipant getProgramParticipant(String programName,
            String participantName, boolean isClient) {

        ProgramParticipant resPP = null;
        try {
            if (isClient) {
                resPP = pcEAO.findProgramParticipantsByProgramNameAndPartName(
                        programName, participantName);
            } else {
                resPP = ppEAO.findProgramParticipantsByProgramNameAndPartName(
                        programName, participantName);
            }
        } catch (javax.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_GET: "
                    + programName + "/" + participantName + " | "
                    + e.getMessage(), e);
        }
        return resPP;
    }

    public ProgramParticipant getClientProgramParticipants(String programName,
            String participantName, boolean isClient) {
        ProgramParticipant resPP = null;

        try {

            if (resPP == null) {
                if (isClient) {
                    resPP = pcEAO
                            .findClientProgramParticipantsByProgramNameAndPartName(
                                    programName, participantName);
                } else {
                    resPP = ppEAO
                            .findProgramParticipantsByProgramNameAndPartName(
                                    programName, participantName);

                }
            }
        } catch (javax.persistence.NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new EJBException("ERROR_PROGRAM_PARTICIPANT_GET_C: "
                    + programName + "/" + participantName + " | "
                    + e.getMessage(), e);
        }

        return resPP;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#createProgramParticipant(java
     * .lang.String, java.lang.String,
     * com.akuacom.pss2.core.entities.ProgramParticipant)
     */
    public ProgramParticipant createProgramParticipant(String programName,
            String participantName, boolean isClient, ProgramParticipant pp) {
        addParticipantToProgram(programName, participantName, isClient);

        // addParticipantToProgram does a lot of validation and throws an
        // exception if there is a problem
        ProgramParticipant pp2 = getProgramParticipant(programName,
                participantName, isClient);

        pp2.setState(pp.getState());

        // check if any constraint data is set
        if (pp.getConstraint() != null
                && (pp.getConstraint().getMinActive() != null
                        || pp.getConstraint().getMaxActive() != null
                        || pp.getConstraint().getActiveAction() != null
                        || pp.getConstraint().getMinNotify() != null
                        || pp.getConstraint().getMaxNotify() != null
                        || pp.getConstraint().getNotifyAction() != null
                        || pp.getConstraint().getMinDuration() != null
                        || pp.getConstraint().getMaxDuration() != null
                        || pp.getConstraint().getDurationAction() != null
                        || pp.getConstraint().getMaxConsecutiveD() != null || pp
                        .getConstraint().getConsecutiveAction() != null)) {
            pp2.setConstraint(pp.getConstraint());
            pp2.getConstraint().setProgramParticipant(pp2);

            if (pp.getConstraint().getInvalidDates() != null
                    && pp.getConstraint().getInvalidDates().size() > 0) {
                HashSet<InvalidDate> dlist = new HashSet<InvalidDate>();
                for (InvalidDate iDate : pp.getConstraint().getInvalidDates()) {
                    if (iDate != null && iDate.getInvalidDate() != null)
                        iDate.setConstraint(pp.getConstraint());
                    dlist.add(iDate);
                }

                pp2.getConstraint().setInvalidDates(dlist);
            }
        }

        // check if any rule info has been set
        if (pp.getProgramParticipantRules() != null
                && pp.getProgramParticipantRules().size() > 0) {
            HashSet<ProgramParticipantRule> rlist = new HashSet<ProgramParticipantRule>();
            for (ProgramParticipantRule rule : pp.getProgramParticipantRules()) {
                if (rule != null
                        && (rule.getStart() != null || rule.getEnd() != null
                                || rule.getMode() != null
                                || rule.getVariable() != null
                                || rule.getOperator() != null
                                || rule.getValue() != null || rule.getSource() != null)) {
                    rule.setProgramParticipant(pp2);
                    rlist.add(rule);
                }
            }

            pp2.setProgramParticipantRules(rlist);
        }

        return setProgramParticipant(pp2, isClient);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.akuacom.pss2.program.ProgramManager#updateProgramParticipant(java
     * .lang.String, java.lang.String,
     * com.akuacom.pss2.core.entities.ProgramParticipant)
     */
    public ProgramParticipant updateProgramParticipant(String programName,
            String participantName, boolean isClient, ProgramParticipant ppIn) {
        if (programName == null || ppIn.getProgramName() == null) {
            String message = "program name is null";
            throw new EJBException(message);
        }
        if (!programName.equals(ppIn.getProgramName())) {
            String message = "program names do not match";

            throw new EJBException(message);
        }
        if (participantName == null || ppIn.getParticipantName() == null) {
            String message = "participant name is null";
            throw new EJBException(message);
        }
        if (!participantName.equals(ppIn.getParticipantName())) {
            String message = "participant names do not match";
            throw new EJBException(message);
        }
        if (ppIn.getUUID() == null || ppIn.getUUID().length() < 32) {
            String message = "UUID is null";
            throw new EJBException(message);
        }

        try {
                // check if any constraint data is set
                if (ppIn.getConstraint() != null
                        && (ppIn.getConstraint().getMinActive() != null
                                || ppIn.getConstraint().getMaxActive() != null
                                || ppIn.getConstraint().getActiveAction() != null
                                || ppIn.getConstraint().getMinNotify() != null
                                || ppIn.getConstraint().getMaxNotify() != null
                                || ppIn.getConstraint().getNotifyAction() != null
                                || ppIn.getConstraint().getMinDuration() != null
                                || ppIn.getConstraint().getMaxDuration() != null
                                || ppIn.getConstraint().getDurationAction() != null
                                || ppIn.getConstraint().getMaxConsecutiveD() != null || ppIn
                                .getConstraint().getConsecutiveAction() != null)) {
                    ppIn.getConstraint().setProgramParticipant(ppIn);

                    if (ppIn.getConstraint().getInvalidDates() != null
                            && ppIn.getConstraint().getInvalidDates().size() > 0) {
                        for (InvalidDate iDate : ppIn.getConstraint()
                                .getInvalidDates()) {
                            if (iDate != null && iDate.getInvalidDate() != null)
                                iDate.setConstraint(ppIn.getConstraint());
                        }
                    }
                }

                // check if any rule info has been set
                if (ppIn.getProgramParticipantRules() != null
                        && ppIn.getProgramParticipantRules().size() > 0) {
                    for (ProgramParticipantRule rule : ppIn
                            .getProgramParticipantRules()) {
                        if (rule != null
                                && (rule.getStart() != null
                                        || rule.getEnd() != null
                                        || rule.getMode() != null
                                        || rule.getVariable() != null
                                        || rule.getOperator() != null
                                        || rule.getValue() != null || rule
                                        .getSource() != null)) {
                            rule.setProgramParticipant(ppIn);
                        }
                    }
                }

        } catch (Exception ex) {
            String message = "error updating participant " + participantName + " in program " + programName + " with " + ppIn;
            throw new EJBException(message, ex);
        }

        return setProgramParticipant(ppIn, isClient);
    }

    /*
    @Override
    public List<ProgramParticipant> findByParticipant(String participantName,
            boolean isClient) {
        final Participant p = participantManager.getParticipant(
                participantName, isClient);
        return ppEAO.findProgramParticipantsByParticipant(p);
    }*/
    
    public void addRules(String clientName, String programName,
        List<ProgramParticipantRule> rules, Rule.Source source)
    {
		ProgramParticipant prgmClient =
			getClientProgramParticipants(programName, clientName , true);
		if (prgmClient.getProgramParticipantRules() == null)
		{
			prgmClient.setProgramParticipantRules(new HashSet<ProgramParticipantRule>());
		}
		else
		{
            Iterator<ProgramParticipantRule> i =
                prgmClient.getProgramParticipantRules().iterator();
			while(i.hasNext())
            {
                ProgramParticipantRule rule = (ProgramParticipantRule)i.next();
                if(rule.getSource().startsWith(source.getDescription()))
                {
                    i.remove();
                }
            }
		}
		int index = source.getOffset();
		for (ProgramParticipantRule rule : rules)
		{
			rule.setSortOrder(index++);
			prgmClient.getProgramParticipantRules().add(rule);
		}
		updateProgramParticipant(programName,
            clientName , true, prgmClient);
    }

    public List<ProgramParticipantRule> getRules(String clientName,
        String programName)
    {
		ProgramParticipant pgrmPart = getClientProgramParticipants(
			programName, clientName, true);

		if (pgrmPart.getProgramParticipantRules() != null)
		{
			List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();
			for(ProgramParticipantRule rule: pgrmPart.getProgramParticipantRules())
			{
				rules.add(rule);
			}
			Collections.sort(rules, new Rule.SortOrderComparator());
			return rules;
		}
		else
		{
			return new ArrayList<ProgramParticipantRule>();
		}

    }


}
