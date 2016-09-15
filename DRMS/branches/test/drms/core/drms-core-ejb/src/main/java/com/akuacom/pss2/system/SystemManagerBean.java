package com.akuacom.pss2.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.cache.EventStateWrapper;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.ConfirmationLevel;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.contact.ContactsOfflineError;
import com.akuacom.pss2.core.CoreConfig;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.core.ProgramEJBBean;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.data.gridpoint.GridPointTimerManager;
import com.akuacom.pss2.email.ClientOfflineNotificationManager;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.email.PeakChoiceEmailReader;
import com.akuacom.pss2.email.ClientTestEmailManager;
import com.akuacom.pss2.email.UndeliveredEmailManager;
import com.akuacom.pss2.event.ClientConversationState;
import com.akuacom.pss2.event.ClientConversationStateEAO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.ge.ClockTimer;
import com.akuacom.pss2.ha.MasterStatus;
import com.akuacom.pss2.history.BaselineAdjustTimerService;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.history.DumpReportDataTimerService;
import com.akuacom.pss2.history.DumpUsageDataTimer;
import com.akuacom.pss2.nssettings.CleanMessageTimerService;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.price.australia.LocationPriceManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramEAO;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramPerf;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.sceftp.PartticipantUploadTimer;
import com.akuacom.pss2.program.sceftp.SCEDBPDispatchManager;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJB;
import com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013Bean;
import com.akuacom.pss2.program.sdgcpp.SdgCPPProgramEJB;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.report.ClientOfflineReportManager;
import com.akuacom.pss2.rtp.ftp.RTPTemperatureUpdateTimer;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEAO;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.timer.TimerManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.timer.TimerManagers;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.TimingUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class SystemManagerBean extends TimerManagerBean implements SystemManager.R, SystemManager.L {
    // TODO these should probably be properties, but maybe not since the
    // performance of the system is heavily dependent on these
    // high speed timer
    // TODO change to a scheduler service
    public static final int TICK5_TIMER_INITIAL_WAIT_MS = 5 * TimingUtil.SECOND_MS; // 5
                                                                                    // secs
    public static final int TICK5_TIMER_REFRESH_INTERVAL_MS = 30 * TimingUtil.SECOND_MS; // 30
                                                                                         // secs
    public static final String TICK5_TIMER = "Tick5";

    // low speed timer
    // 1 secs - get all the housekeeping down before starting event processing
    public static final int SLOW_TIMER_INITIAL_WAIT_MS = TimingUtil.SECOND_MS;
    // 1.5 min
    public static final int SLOW_TIMER_REFRESH_INTERVAL_MS = 90 * TimingUtil.SECOND_MS;
    public static final String SLOW_TIMER = "Slow Timer";
    // 1 secs - get all the housekeeping down before starting event processing
    public static final int PEAKCHOICE_TIMER_INITIAL_WAIT_MS = TimingUtil.SECOND_MS;
    // 5 min
    public static final int PEAKCHOICE_TIMER_REFRESH_INTERVAL_MS = 5 * TimingUtil.MINUTE_MS;
    public static final String PEAKCHOICE_TIMER = "Peak Choice Timer";
    
    public static final String TEST_PRORGRAM_CLASSNAME = "com.akuacom.pss2.program.testProgram.TestProgramEJB";


    @EJB
    CorePropertyEAO.L corePropEAO;
    @EJB
    PeakChoiceEmailReader.L reader;
    @EJB
    ProgramManager.L programManager;
    @EJB
    ContactManager.L contactManagaer;
    // Li Fei added for clean message function.
    @EJB
    CleanMessageTimerService.L cts;
    
    @EJB
    private ProgramEAO.L programEAO;

    @EJB
    Notifier.L notifier;
    
    @EJB
    Pss2SQLExecutor.L  sqlExecutor;
    
    @EJB
    private SignalEAO.L signalEao;
    
    @Resource
    private TimerService timerService;
    
    //just for debug
    private static boolean timerEnabled=true;
    

    private static final Logger log = Logger.getLogger(SystemManagerBean.class);

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
            // DRMS-1654
            log.error("Can't find program ejb bean for: " + classNameBase, e);
            throw new EJBException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.system.SystemManager#lookupProgramBean(java.lang.String)
     */
    public ProgramEJB lookupProgramBean(String programName) throws EJBException {
        ProgramPerf program = programManager.getProgramPerf(programName);
        if (program == null) {
            throw new ValidationException("no program named " + programName);
        }
        String classNameBase = program.getClassName();
        return lookupProgramBeanFromClassName(classNameBase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.system.SystemManager#getProperty(java.lang.String)
     */
    public CoreProperty getPropertyByName(String propName)
            throws EntityNotFoundException {
        return corePropEAO.getByPropertyName(propName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.system.SystemManager#setProperty(com.akuacom.pss2.core
     * .entities.CoreProperty)
     */
    public CoreProperty setProperty(CoreProperty prop) {
        try {
            CoreProperty cp = corePropEAO.getByPropertyName(prop.getPropertyName());
            cp.setBooleanValue(prop.isBooleanValue());
            cp.setStringValue(prop.getStringValue());
            cp.setTextValue(prop.getTextValue());
            cp.setDoubleValue(prop.getDoubleValue());
            return cp;
        } catch (EntityNotFoundException e) {
            return corePropEAO.merge(prop);
        } finally {
            String propertyName = PropertyName.CLIENT_CONFIMATION_LEVEL.getPropertyName();
            if (propertyName.equals(prop.getPropertyName())) {
                ConfirmationLevel level = ConfirmationLevel.valueOf(prop.getStringValue());
                EventStateCacheHelper.getInstance().setConfirmationLevel(level);
            }
        }
            
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.system.SystemManager#getAllProperties()
     */
    public List<CoreProperty> getAllProperties() {
        return corePropEAO.getAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.system.SystemManager#getPss2Properties()
     */
    public PSS2Properties getPss2Properties() {
        List<CoreProperty> properties = getAllProperties();
        return new PSS2Properties(properties);
    }

    public PSS2Features getPss2Features() {
        List<CoreProperty> properties = getAllProperties();
        return new PSS2Features(properties);
    }

    /**
     * Creates the timers if node is the master node in the cluster. If other beans are managing their own timers, they will not be singletons TODO make an interceptor to control this
     */
    public void createTimers() {
    	//For protection, creates timers if you are the master node in the cluster. TODO make an interceptor to control this
    	if(MasterStatus.getInstance().getStatus()){
    		log.info("Creating timers with Master Status" + MasterStatus.getInstance().getStatus());

            if (!isTimerAvailable(SLOW_TIMER)) {
                timerService.createTimer(SLOW_TIMER_INITIAL_WAIT_MS,
                        SLOW_TIMER_REFRESH_INTERVAL_MS, SLOW_TIMER);
            }
            if (!isTimerAvailable(TICK5_TIMER)) {
                 timerService.createTimer(TICK5_TIMER_INITIAL_WAIT_MS,
                         TICK5_TIMER_REFRESH_INTERVAL_MS, TICK5_TIMER);
            }
            if (!isTimerAvailable(PEAKCHOICE_TIMER)) {
                timerService.createTimer(PEAKCHOICE_TIMER_INITIAL_WAIT_MS,
                        PEAKCHOICE_TIMER_REFRESH_INTERVAL_MS, PEAKCHOICE_TIMER);
            }

            //Invoke create on other timer managers registered
            for(String timerManagerClassName:  TimerManagers.getInstance().getTimerManagers()){
                Class<?> aClass;
                try {
                    aClass = Class.forName(timerManagerClassName);
                } catch (ClassNotFoundException e) {

                        log.error("Can't find timer manager bean for: " + timerManagerClassName, e);
                        throw new EJBException(e.getMessage(), e);

                }
                    TimerManager timerManager = (TimerManager) EJBFactory.getBean(aClass);
                    timerManager.createTimers();
            }
            //All programs should register them selves as timerManagers but currently don't
            for (String programName : programManager.getPrograms()) {
                log.debug("programName " + programName);
                try {
                    ProgramEJB programEJB = lookupProgramBean(programName);
                    programEJB.createTimer(programName);
                } catch (Exception e) {
                    log.error(e);
                }
            }
    	}
    	else {
    		log.info("Skipped timers due to Master Status " + MasterStatus.getInstance().getStatus());
    	}
       
    }

    private boolean isTimerAvailable(String timerName) {
        if (timerName == null) return false;

        Collection col = timerService.getTimers();
        for (Object o : col) {
            Timer timer = (Timer) o;
            if (timerName.equals(timer.getInfo())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Cancels timers for this bean, and other registered timer managers.
     */
    public void cancelTimers() {
        log.info("Cancelling timers of SystemManager");

        Collection timersList = timerService.getTimers();
        //cancel timers that SystemManager manages
		super.cancelTimers(timersList);
		//cancel other timers
		 //Get infos from other managers and their timers
        for(String timerManagerClassName:  TimerManagers.getInstance().getTimerManagers()){
        	Class<?> aClass;
			try {
				aClass = Class.forName(timerManagerClassName);
			} catch (ClassNotFoundException e) {
			
		            log.error("Can't find timer manager bean for: " + timerManagerClassName, e);
		            throw new EJBException(e.getMessage(), e);
		        
			}
        		TimerManager timerManager = (TimerManager) EJBFactory.getBean(aClass);
        		timerManager.cancelTimers();
        }
    }
    
    /**
     * Retrieve the timer list for management. Does not provide info on any timers created by other beans/services.
     */
    @Override
    public String getTimersInfo() {
        log.debug("Getting timers");

		Collection timersList = timerService.getTimers();
        StringBuffer sb = new StringBuffer();
        //Timers that this bean manages
        for (Object timer : timersList) {
			if (timer instanceof Timer) {
		
					sb.append(((Timer) timer).getClass() + " Timer [" + ((Timer) timer).getInfo().toString() + "] next run [" + ((Timer) timer).getNextTimeout() + "]\n");				
				
			}
		}
        
        //Get infos from other managers and their timers
        for(String timerManagerClassName:  TimerManagers.getInstance().getTimerManagers()){
        	Class<?> aClass;
			try {
				aClass = Class.forName(timerManagerClassName);
			} catch (ClassNotFoundException e) {
			
		            log.error("Can't find timer manager bean for: " + timerManagerClassName, e);
		            throw new EJBException(e.getMessage(), e);
		        
			}
        		TimerManager timerManager = (TimerManager) EJBFactory.getBean(aClass);
        		sb.append(timerManager.getTimersInfo());
        }
        
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.akuacom.pss2.system.SystemManager#initialize(boolean)
     */
    public void initialize() {
        try {
        	 EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        	if(timerEnabled){
        		
        		TimerManagers.getInstance().getTimerManagers().add(ClientOfflineReportManager.class.getName());
        		TimerManagers.getInstance().getTimerManagers().add(ClientOfflineNotificationManager.class.getName());
        		
        		TimerManagers.getInstance().getTimerManagers().add(ClientTestEmailManager.class.getName());
        		
	        	TimerManagers.getInstance().getTimerManagers().add(AsynchCaller.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(CleanMessageTimerService.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(ClientManager.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(SCERTPProgramEJB.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(SCERTPProgramEJB2013Bean.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(DumpUsageDataTimer.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(DumpReportDataTimerService.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(SCEDBPDispatchManager.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(PartticipantUploadTimer.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(SdgCPPProgramEJB.class.getName());
	//        	TimerManagers.getInstance().getTimerManagers().add(InterpolateMissingDataTimerService.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(GridPointTimerManager.class.getName());
	        	TimerManagers.getInstance().getTimerManagers().add(RTPTemperatureUpdateTimer.class.getName());
	        	
	        	TimerManagers.getInstance().getTimerManagers().add(BaselineAdjustTimerService.class.getName());
	        	
	        	TimerManagers.getInstance().getTimerManagers().add(UndeliveredEmailManager.class.getName());
	        	
	        	TimerManagers.getInstance().getTimerManagers().add(ProgramParticipantManager.class.getName());
	        	
	        	if(getPss2Features().isFeatureAustraliaPriceEnabled())
	        			TimerManagers.getInstance().getTimerManagers().add(LocationPriceManager.class.getName());
	        	
	        	
	        	TimerManagers.getInstance().getTimerManagers().add(ClockTimer.class.getName());
	        	
	        	
        	}
            loadSignals();
            loadPrograms();
            
            /*
             * When the system starts up, always suppress all outgoing emails
             * and require the operator to enable them.  Every time the system starts.
             */
            CoreProperty emailSquelcherProp = new CoreProperty(PSS2Properties.PropertyName.SUPPRESS_ALL_EMAILS.toString(),Boolean.TRUE);
            setProperty(emailSquelcherProp);
            
           	cache.set("utilityName", getPss2Properties().getUtilityName());
           	cache.set("utilityDisplayName", getPss2Properties().getUtilityDisplayName());
           	
           	cache.setSignalDef("pending",signalEao.getSignal("pending"));
           	cache.setSignalDef("mode",signalEao.getSignal("mode"));
           	
           	cache.setEscacheforoperatorcontacts("OperatorContacts",contactManagaer.getOperatorContacts());
           	cache.setSupressAllEmail(getPss2Properties().isSuppressAllEmails());
           	
           	String result;
            try {
                CoreProperty emailFromAddress = corePropEAO.getByPropertyName("emailFromAddress");
                String stringValue = emailFromAddress.getStringValue();
                InternetAddress.parse(stringValue);
                result = stringValue;
            } catch (EntityNotFoundException e) {
                result = "noreply@openadr.com";
            } catch (AddressException e) {
                result = "noreply@openadr.com";
            }
           	
           	cache.setFromEmailAddress(result);

            // initialize each program
            log.debug("programManager " + programManager);
            Class<?> c = programManager.getClass();
            log.debug("class " + c);
            log.debug("declaring class " + c.getDeclaringClass());
            log.debug("generic ifaces " + Dbg.oSA(c.getGenericInterfaces()));
            log.debug("generic superclass " + c.getGenericSuperclass());
            log.debug("classes " + Dbg.oSA(c.getClasses()));
            log.debug("interfaces " + Dbg.oSA(c.getInterfaces()));
            List<String> programNames = programManager.getPrograms();
            Dbg.debug(programNames);
            for (String programName : programManager.getPrograms()) {
                log.debug("programName " + programName);
                try {
                    ProgramEJB programEJB = lookupProgramBean(programName);
                    programEJB.initialize(programName);
                } catch (Exception e) {
                	log.error(e);
                }
            }

            //creates timers for this manager, and other managers who are registered with us
            createTimers();



        } catch (Exception e) {
            String message = "error initializing ProgramManager";
            // DRMS-1654
            log.error(message, e);
            // TODO: this should exit the program
            throw new EJBException(message, e);
        }
    }

    /**
     * Load signals.
     */
    private void loadSignals() {
        log.debug("loading signals");

        try {
            // load current list from file
            final List<SignalDef> fileList = new ArrayList<SignalDef>();
            Properties config = CoreConfig.loadSignalProperties();
            int number = new Integer(config.getProperty("number"));
            for (int i = 0; i < number; i++) {
                final String type = config.getProperty("s" + i + ".type");
                if (EventInfoInstance.SignalType.LOAD_LEVEL.name().equals(type)) {
                    final SignalDef signal = new SignalDef();
                    signal.setSignalName(config.getProperty("s" + i + ".name"));

                    Boolean levelSignal = false;
                    Boolean inputSignal = false;
                    try {
                        levelSignal = Boolean.parseBoolean(config.getProperty("s" + i + ".levelSignal"));
                        inputSignal = Boolean.parseBoolean(config.getProperty("s" + i + ".inputSignal"));
                    } catch (Exception ex) {
                        // no-op already defaulted
                    }

                    signal.setLevelSignal(levelSignal);
                    signal.setInputSignal(inputSignal);

                    signal.setType(type);
                    String s = config.getProperty("s" + i + ".levels");
                    String defaultLevel = config.getProperty("s" + i + ".defaultLevel");
                    Set<SignalLevelDef> signalLevelDefs = new HashSet<SignalLevelDef>();
                    for (String level : s.split(",")) {
                        SignalLevelDef signalLevelDef = new SignalLevelDef();
                        signalLevelDef.setStringValue(level);
                        signalLevelDef.setSignal(signal);
                        if (level.equals(defaultLevel)) {
                            signalLevelDef.setDefaultValue(true);
                        } else {
                            signalLevelDef.setDefaultValue(false);

                        }
                        signalLevelDefs.add(signalLevelDef);
                    }
                    signal.setSignalLevels(signalLevelDefs);
                    fileList.add(signal);
                } else if ("number".equals(type)) {
                    final SignalDef signal = new SignalDef();
                    signal.setSignalName(config.getProperty("s" + i + ".name"));
                    signal.setType(type);
                    signal.setType(config.getProperty("s" + i + ".numberType"));
                    fileList.add(signal);
                }
            }

            // compare with db, if signal doesn't exist, add to db.
            // NOTE: add only for now, no auto update for existing, no deletion
            // for orphans
            SignalManager signalManager = EJBFactory.getBean(SignalManager.class);
            final List<SignalDef> dbList = signalManager.getSignals();
            for (SignalDef signal : fileList) {
                boolean found = false;
                for (SignalDef s : dbList) {
                    if (signal.getSignalName().equals(s.getSignalName())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    signalManager.addSignal(signal);
                }
            }

        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /**
     * creates the program in the program dao
     * 
     * TODO: this method should probably be called initAndVerifyPrograms. it
     * should only be called once at start up. it should: - load the old program
     * database from persistance - compare the program definitions in the
     * persisted store to what is in the config paramters and: - blow away and
     * programs no longer supported - create any new programs - modify the
     * signals of any existing programs if they differ from the new definitions
     */
    private void loadPrograms() {
        try {
            ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
            List<Program> programs = programManager.getAllPrograms();
            // load program definitions from file if nothing in the db
            if (programs == null || programs.isEmpty()) {
                final Properties config = CoreConfig.loadProgramProperties();
                int number = new Integer(config.getProperty("number"));
                for (int i = 0; i < number; i++) {
                    final String className = config.getProperty("p" + i + ".class");
                    final ProgramEJB ejb = this.lookupProgramBeanFromClassName(className);
                    // delegate the job to ProgramEJB#loadProgram
                    final Program program = ejb.loadProgram(config, i);
                    program.setClassName(className);
                    program.setState(Program.PROGRAM_ACTIVE);
                    // update the db
                    programEAO.createProgram(program);
                }
                log.debug("loaded programs from file");
            } else {
                log.debug("loaded programs from db");
                
            	boolean exist=false;
            	for (Program program:programs){
            		if (program instanceof TestProgram){
            			exist=true;
            			break;
            		}
            	}
            	if(!exist) {
            		int priority=programManager.getNextPriority();
            		loadTestProgram(priority);
            	}
            	
				boolean programExist=false;
				for (Program program:programs){
					if (program.getLongProgramName()!=null || program.getProgramClass()!=null){
						exist=true;
						break;
					}
				}
				if (!programExist) {
					final Properties config = CoreConfig.loadProgramProperties();
					int number = new Integer(config.getProperty("number"));
					for (Program prog:programs){
						for (int i = 0; i < number; i++) {
							final String name=config.getProperty("p" + i + ".name");
							if (prog.getProgramName().equals(name)){
								String programClass=config.getProperty("p" + i + ".programClass");
								String longProgramName=config.getProperty("p" + i + ".longProgramName");
								if (programClass !=null)
									prog.setProgramClass(programClass);
								if (longProgramName !=null)
									prog.setLongProgramName(longProgramName);
								
								programEAO.update(prog);
							}
						}
					}
				}

            }
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
    
    private void loadTestProgram(int priority){
		try {
			final Properties config = CoreConfig.loadProgramProperties();
			int number = new Integer(config.getProperty("number"));
			boolean loaded=false;
			
			for (int i = 0; i < number; i++) {
				final String className = config.getProperty("p" + i + ".class");
				if (!className.equals(TEST_PRORGRAM_CLASSNAME))
					continue;
				
				final ProgramEJB ejb = lookupProgramBeanFromClassName(className);
				final Program program = ejb.loadProgram(config, i);
				program.setPriority(priority);
				program.setClassName(className);
				program.setState(Program.PROGRAM_ACTIVE);
				programEAO.createProgram(program);
				
				loaded=true;
				break;
			}
			
			if (loaded)
				log.debug("loaded test program from file");
			else
				log.debug("no test program defined in the configuration file");
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException(e);
		}
    }

    /**
     * Process programs - call each program's tick method.
     */
    public void processPrograms() {
        // call each program's tick method
        EventManager eventManager = EJBFactory.getBean(EventManager.class);
        Collection<Event> eList = eventManager.findAllPerf();
        ProgramEJB programEJB=null;
        for (Event event : eList) {
            programEJB = lookupProgramBean(event.getProgramName());
            try {
                programEJB.tick5Seconds(event);
            } catch (Exception ex) {
                // All exceptions should be handled and logged
                // in programEJB.tick5Seconds and its subclasses
                // so this block should never be called.
                // Also, those methods should require a new transaction
                // However, we need to ensure that all tick5Seconds methods
                // are executed so putting this catch as a failsafe
                // DRMS-1654
                log.debug("A programEJB.tick5Seconds failed but processing of others should have continued");
            }
        }
    }

    private void processTick5Seconds() {
        try {
        	if(timerEnabled){
        		processPrograms();
        	}
        } catch (Exception ex) {
            // DRMS-1654
            log.warn("Error in tick: " + ex.getMessage(), ex);
        }
    }

    private void processSlowTimer() {
        try {
        	if(timerEnabled){
	            maintainClientConversationStateLog();
	            processComms();
        	}
        } catch (Exception ex) {
            log.warn("Error in slow timer: " + ex.getMessage(), ex);
        }
    }

    public void maintainClientConversationStateLog() {
        try {
            PSS2Properties props = getPss2Properties();
            EventManager eventManager = EJB3Factory.getLocalBean(EventManager.class);
            ClientConversationStateEAO clientConversationStateEAO = EJB3Factory.getLocalBean(ClientConversationStateEAO.class);

            // first the push clients - find all entries that haven't been 
            // confirmed and repush
            List<ClientConversationState> pushConversations = clientConversationStateEAO.findByPushAndTimedOut();
            for (ClientConversationState es : pushConversations) {
                ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
                
                Participant part = participantManager.getParticipant(es.getDrasClientId(), true);
                if (part != null) {                
                    participantManager.setParticipantStatus(part, ClientStatus.ERROR);
                }      
                // TODO should be moved to reports 
                FireLogEntry logEntry = new FireLogEntry();
                logEntry.setCategory(LogUtils.CATAGORY_COMMS);
                if (es.getDrasClientId() != null)
                    logEntry.setUserName(es.getDrasClientId());
                String message = "Push EventState never confirmed: DRAS Client ID: "
                        + es.getDrasClientId()
                        + ", program: "
                        + es.getProgramName()
                        + ", eventStateId: "
                        + es.getConversationStateId();
                logEntry.setDescription(message);
                log.debug(logEntry);
                
                ClientManager clientManager = EJBFactory.getBean(ClientManager.class);
                Participant client = clientManager.getClientOnly(es.getDrasClientId());
                if (client != null) {
                    clientManager.pushClientEventState(client);              
                }
            }
            
            // then everything
            if (props.getConfirmationLevel() == ConfirmationLevel.FULL) {
            	//TODO
            	//this only process max 120 timeout conversation states, see findByTimeOut Query 
            	//and table client_conversation_state will grow very quickly
            	List<ClientConversationState> ongoingConversations = clientConversationStateEAO.findByTimedOut();
            	for (ClientConversationState es : ongoingConversations) {
                    if (props.getConfirmationLevel() == ConfirmationLevel.FULL) {
                        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
                        
                        Participant part = participantManager.getParticipant(es.getDrasClientId(), true);
                        if (part != null) {
                        	// participant could be deleted before timeout when testing.
                        	participantManager.setParticipantStatus(part, ClientStatus.ERROR);
                        }
                        FireLogEntry logEntry = new FireLogEntry();
                        logEntry.setCategory(LogUtils.CATAGORY_COMMS);
                        if (es.getDrasClientId() != null) {
                            logEntry.setUserName(es.getDrasClientId());
                        }
                        String message = "EventState never confirmed: DRAS Client ID: "
                                + es.getDrasClientId()
                                + ", program: "
                                + es.getProgramName()
                                + ", eventStateId: "
                                + es.getConversationStateId();
                        logEntry.setDescription(message);
                        log.info(logEntry);
                    }
                    try { 
                    	//TODO 
                    	//this call well be in a new transaction, and may take some time 
                    	//and will lead to transaction time out 
                    	eventManager.removeClientConversationState(es.getConversationStateId()); 
                    } catch (EJBException ejbx) {
                         // Because of concurrent processes, it's possible
                         // the event state could already be deleted.
                         // So we won't panic if the delete fails.
                    	//BUT 
                         log.warn("EJB exception deleting ClientConversationState: " + ejbx.getMessage());
                    }
            	}
            }
            else{
            //for non confirmation, just remove all time out records immediately in batch
            	String sqltemplate = "delete from client_conversation_state where commTime < ${time}";
            	Map<String,Object> params = new HashMap<String,Object>();
            	params.put("time",new Date(System.currentTimeMillis()-60*60*1000));
            	String sql  = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
            	sqlExecutor.execute(sql,params);
            }
        } catch (Exception ex) {
            String message = "error getting event state entries";
            log.warn(LogUtils.createLogEntry(null, null, message, null));
            log.debug(LogUtils.createExceptionLogEntry(null, getClass()
                    .getName(), ex));
            throw new EJBException(message, ex);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void processTimeOutCommsStatus(Participant part,ParticipantManager participantManager,
    		Map<String, ContactsOfflineError> errorMap,PSS2Properties params, List<Contact> contacts){
    	long nowMS = new Date().getTime();
    	EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
    	//boolean suppressEmails = params.isSuppressAllEmails().booleanValue();
    	boolean suppressEmails = cache.getSupressAllEmail();
        String participantName = part.getParticipantName();
        Date lastContact = part.getCommTime();
         if (lastContact == null) {
             if (part.getStatus() != ClientStatus.OFFLINE.ordinal()) {
                 participantManager.setParticipantStatus(part, ClientStatus.OFFLINE);
                 
                 // log client offline time to history table
                 if (part.isClient())
                 	reportClientStatus(part.getUUID(), new Date(nowMS), participantName);
             }
             return;
         }
         long timeSinceLastContactM = (nowMS - lastContact.getTime())
                 / TimingUtil.MINUTE_MS;
         Integer b = part.getStatus();
         ClientStatus lastStatus = ClientStatus.values()[b];

         if (timeSinceLastContactM > params.getOfflineWarningThresholdM()) {
             // TODO tune this condition later
             if (lastStatus != ClientStatus.OFFLINE) {
                 part = participantManager.setParticipantStatus(part, ClientStatus.OFFLINE);

                 // log client offline time to history table
                 if (part.isClient()) {
                	log.info("offline warning threshold crossed and last status was" +lastStatus+"for participant"+participantName);
                 	reportClientStatus(part.getUUID(), lastContact, participantName);
                 }
             }
             if (!part.isOfflineWarning()) {
                 part.setOfflineWarning(true);
                 participantManager.updateParticipant(part);
             }

             String subject = "DRAS client " + participantName + " offline";
             String content = "The last contact with your "
                     + params.getUtilityDisplayName() + " DRAS client "
                     + participantName + " was at " + lastContact
                     + ". Please check your network and power connections.";

             for (ParticipantContact pContact : part.getContacts()) {
                 // bug 761
                 if (pContact == null)
                     continue;

                 if (timeSinceLastContactM >= pContact
                         .getOfflineErrorThresholdMinutesForSeason(params.isOnSeason())
                         && !pContact.isOfflineError()) {
                     pContact.setOfflineError(true);
                     participantManager.updateParticipant(part);
                     
                     if (!suppressEmails && pContact.isCommNotification() && part.getType() != Participant.TYPE_MANUAL) {
                     	notifier.sendNotification(pContact,
                                 part.getParticipantName(), subject,
                                 content,
                                 NotificationMethod.getInstance(), null,
                                 Environment.isAkuacomEmailOnly(),
                                 false, true, "");
                     }
                 }
             }
             
             if (!suppressEmails && part.getType() != Participant.TYPE_MANUAL) {
                    ProgramEJBBean.sendDRASOperatorCommNotification(contacts, subject,
                            content, NotificationMethod.getInstance(), null,
                            "", part, timeSinceLastContactM, params,notifier, errorMap);
             }
         }
    }
    
    private void processComms() {
    	//it will take some time to update status client by client and therefore lock on some client will not be released timely
    	//a separated API which will create a isolated transaction to update client status
        ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);
        PSS2Properties params = getPss2Properties();
        List<Participant> parts = participantManager.findParticipantsForUpdateStatusLoop();
        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        if (parts != null && !parts.isEmpty()) {
        	Map<String, ContactsOfflineError> errorMap = ProgramEJBBean.getErrorMap();
        	//boolean suppressEmails = params.isSuppressAllEmails().booleanValue(); 
        	boolean suppressEmails = cache.getSupressAllEmail();
            List<Contact> contacts = null;
            if (!suppressEmails) {
                //ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);
               // contacts = cm.getOperatorContacts();
            	contacts =  cache.getEscacheforoperatorcontacts();
                if(contacts.isEmpty()){
                	ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);
                	contacts = cm.getOperatorContacts();
                	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
                }
            }
            for (Participant part : parts) {
            	//log.debug("Participants are available");
            	//process of each client will be in a isolated transaction
            	processTimeOutCommsStatus(part,participantManager,errorMap,params,contacts);
            }
        }
    }

    private void reportClientStatus(String client, Date time, String clientName){
        CustomerReportManager customerReportManager = EJBFactory.getBean(CustomerReportManager.class);
        customerReportManager.reportOfflineStatus(client, time, clientName);
    }
    
    /**
     * timeout event should update participant's online status. no need to
     * maintain the comm log (should go to client communication event)
     * 
     * @param timer
     *            timer object
     */
    @Timeout
    public void timeoutHandler(Timer timer) {
        if (TICK5_TIMER.equals(timer.getInfo())) {
            processTick5Seconds();
        } else if (SLOW_TIMER.equals(timer.getInfo())) {
            processSlowTimer();
        } else if (PEAKCHOICE_TIMER.equals(timer.getInfo())) {
            final Boolean b = getPss2Features().isFeaturePeakChoiceProgramEnabled();
            if (b != null && b) {
                processPeakChoiceEmail();
            }
        }
    }

    private void processPeakChoiceEmail() {
        try {
            reader.poll();
        } catch (Exception e) {
            // DRMS-1654
            log.debug("error here", e);
        }
    }

    @Override
    public CoreProperty getProperty(PropertyName propertyName)
            throws EntityNotFoundException {
        return corePropEAO.getByPropertyName(propertyName.getPropertyName());
    }

    @Override
    public CoreProperty updatePropertyValue(String uuid,
            String propertyValueAsString) throws EntityNotFoundException {
        CoreProperty coreProperty = corePropEAO.getById(uuid);
        coreProperty.setValueAsString(propertyValueAsString);
        corePropEAO.update(coreProperty);
        return coreProperty;
    }
}
