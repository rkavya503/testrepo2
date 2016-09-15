/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.program.sdgcpp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.apache.log4j.Logger;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.kwickview.KWickviewCurtailmentClient;
import com.akuacom.pss2.kwickview.KWickviewEvent;
import com.akuacom.pss2.kwickview.KWickviewResponse;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.cpp.CPPProgramEJBBean;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features.FeatureName;
import com.akuacom.pss2.timer.TimerManager;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;

/**
 *
 * @author spierson
 */
@Stateless
public class SdgCPPProgramEJBBean extends CPPProgramEJBBean
        implements SdgCPPProgramEJB, TimerManager, SdgCPPProgramEJB.R, SdgCPPProgramEJB.L {

    private static final Logger log =
            Logger.getLogger(SdgCPPProgramEJBBean.class);
    public static final String SDG_KWICKVIEW_TIMER = "SDGE_KWICKVIEW";
    public static final int SDG_KWICKVIEW_TIMER_INITIAL_WAIT_MS = 8000; // 8 secs
    public static final int SDG_KWICKVIEW_TIMER_REFRESH_INTERVAL_MS = 5 * 60 * 1000; // 5 min

    public class SdgCPPTimerToken implements Serializable {

        private static final long serialVersionUID = 1L;
        private String programName;

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String name) {
            programName = name;
        }

        public SdgCPPTimerToken() {
        }

        public SdgCPPTimerToken(String programName) {
            this.programName = programName;
        }

        public boolean equals(Serializable other) {
            if (other != null && other instanceof SdgCPPTimerToken) {
                SdgCPPTimerToken sother = (SdgCPPTimerToken) other;
                return programName.equals(sother.getProgramName());
            }
            return false;
        }
    }
    @EJB
    protected SystemManager.L systemManager;
	@EJB
	ParticipantManager.L partManager;

    /**
     * Override of super to handle timeouts.
     */
    @EpicMethod
    @Override
    public void processTimeout(Timer timer) {

        // Sometimes timers get hooked up to programs that aren't turned on       
        if (!thisProgramIsUsed()) {
            timer.cancel();
            return;
        }

        if (timer.getInfo() instanceof SdgCPPTimerToken) {
            SdgCPPTimerToken timerInfo = (SdgCPPTimerToken) timer.getInfo();
            String programName = timerInfo.getProgramName();
            log.debug("SdgCPPProgramEJBBean KWickview polling timer");

            try {
                boolean doKwickview = systemManager.getPropertyByName(FeatureName.KWICKVIEW_ENABLED.toString()).isBooleanValue();
                if (doKwickview) {
                    String user = systemManager.getPropertyByName(FeatureName.KWICKVIEW_USERNAME.toString()).getStringValue();
                    String pwd = systemManager.getPropertyByName(FeatureName.KWICKVIEW_PASSWORD.toString()).getStringValue();
                    String url = systemManager.getPropertyByName(FeatureName.KWICKVIEW_URL.toString()).getStringValue();
                    String programID = systemManager.getPropertyByName(FeatureName.KWICKVIEW_PROGRAM_MAP.toString()).getStringValue();
                    String pollingHours = systemManager.getPropertyByName(FeatureName.KWICKVIEW_POLLING_HOURS.toString()).getStringValue();

                    boolean thisProgramDoesKwickview = false;
                    String KWProgramID = null;
                    String [] progs = programID.split("[ ,;]");
                    for (String prog : progs) {
                        String[] pieces = prog.split(":");
                        if (pieces.length != 2) {
                            log.error("KWickview program property should be in format [DRAS PROGRAM NAME1]:[SDGE PROGRAM ID1],[DRAS PROGRAM NAME2]:[SDGE PROGRAM ID2]...");
                        }
                        else if (programName.equals(pieces[0])) {
                            thisProgramDoesKwickview = true;
                            KWProgramID = pieces[1];
                        }
                    }

                    if (thisProgramDoesKwickview) {
                        KWickviewCurtailmentClient kwick = new KWickviewCurtailmentClient(user, pwd, url);
                        Calendar now = Calendar.getInstance();
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        String[] h = pollingHours.split("-");
                        int startHour = 10;
                        int endHour = 15;
                        try {
                            startHour = Integer.valueOf(h[0]);
                            endHour = Integer.valueOf(h[1]);
                        } catch(Exception ex) {
                            log.error("KWickview polling hours property should be in format nn-nn, as in 10-15 for 10 am to 3 pm");                           
                        }
                        
                        List<DateRange> eventDates = new ArrayList<DateRange>();
                        if (hour >= startHour && hour < endHour) {
                            KWickviewResponse kwresp = kwick.checkForEvents(KWProgramID);

                            // If kWickview has requested that some events get made,
                            // go make them now

                            for (KWickviewEvent kwEvent : kwresp.getEvents()) {
                                
                                Date start = kwEvent.getEventStart();
                                Date end = kwEvent.getEventEnd();
                                DateRange dr = new DateRange();
                                dr.setStartTime(start);
                                dr.setEndTime(end);
                                // Prevent date-range-duplicate events
                                if (eventDates.contains(dr) == false) {
                                    eventDates.add(dr);  
                                    String newEventName = programName + "-kWickview-" + kwEvent.getEventID();
                                    Event alreadyEvent = null;
                                    try {
                                        // prevent name-duplicate events
                                        alreadyEvent = super.getEvent(programName, newEventName);
                                    } catch (Exception kerblam) {  /* well I guess I shouldn't have asked */ }
                                    
                                    if (alreadyEvent == null) {
                                        Event newEvent = new Event();
                                        newEvent.setEventName(newEventName);
                                        newEvent.setProgramName(programName);
                                        newEvent.setStartTime(kwEvent.getEventStart());
                                        newEvent.setEndTime(kwEvent.getEventEnd());
                                        newEvent.setReceivedTime(now.getTime());
                                        newEvent.setIssuedTime(now.getTime());
                                        
                        				List<Participant> parts = partManager.findParticipantsByProgramName(programName);
                        				if (parts != null && parts.size() > 0) {
                        					Set<EventParticipant> eps = new HashSet<EventParticipant>();
                        	
                        					for (Participant part : parts) {
                        						EventParticipant ep = new EventParticipant();
                        						ep.setEvent(newEvent);
                        						ep.setParticipant(part);
                        						part.getEventParticipants().add(ep);
                        						eps.add(ep);
                        					}
                        					newEvent.setEventParticipants(eps);
                        				}
                                        
                                        super.createEvent(programName, newEvent, null);
                                        log.info(LogUtils.createLogEntry(
                                                programName, LogUtils.CATAGORY_EVENT,
                                                "kWickview automated event created", kwEvent.toString()));
                                    }
                                }
                            }
                            // Acknowledge the kWickview instruction
                            kwick.confirmEvents(kwresp, true);
                        }
                    }
                }
            } catch (Exception ex) {
            	sendExceptionNotifications(ex, programName);
                log.error(LogUtils.createExceptionLogEntry("SdgCPPProgramEJBBean", "KWickview Polling", ex));
            }
        }
    }

	public void sendExceptionNotifications(String title, String message, String programName){
		sendDRASOperatorEventNotification(title, message, 
				NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);
	}
	
	public void sendExceptionNotifications(Exception e, String programName){
		StringBuilder subject=new StringBuilder();
        String title = "SDG&E program CPP_D kWickview automated event creation failed";
		subject.append(title);
		
		StringBuilder content=new StringBuilder();
		if (e instanceof ProgramValidationException) {
			content.append("Program validation error(s):\n");
			for (ProgramValidationMessage msg:((ProgramValidationException)e).getErrors()) {
				content.append(msg.getParameterName());
				content.append(": ");
				content.append(msg.getDescription());
				content.append("\n");
			}
		} else {
			content.append("Error(s):\n");
			content.append(MessageUtil.getErrorMessage(e));
		}

		sendDRASOperatorEventNotification(subject.toString(), content.toString(), 
				NotificationMethod.getInstance(), new NotificationParametersVO(), programName, notifier);
	}

    public static synchronized void sendDRASOperatorEventNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName, Notifier notifier) {
        // bug 761
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

        //List<Contact> contacts = cm.getOperatorContacts();
        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
		List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
        if(contacts.isEmpty()){
        	contacts = cm.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
        }
        List<Contact> sendList = new ArrayList<Contact>();

        for (Contact c : contacts) {
        	if (c.eventNotificationOn()) {
                sendList.add(c);
            }
        }

        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }
	
    @Override
    public void createTimers() {
        // Timer gets created in createTimer, so it will have a program name
    }

    @Override
    public void createTimer(String programName) {
        //super.createTimer(programName);

        javax.ejb.TimerService timerService = context.getTimerService();

        SdgCPPTimerToken timerToken = new SdgCPPTimerToken(programName);

        // check the list of timers to see if there is a weather polling timer
        // there should only be one, shared by all programs serviced by this ejb
        Collection<Timer> timers = timerService.getTimers();
        boolean found = false;
        for (Timer timer : timers) {
            if (timerToken.equals(timer.getInfo())) {
                found = true;
                break;
            }
        }

        if (!found) {
            timerService.createTimer(SDG_KWICKVIEW_TIMER_INITIAL_WAIT_MS,
                    SDG_KWICKVIEW_TIMER_REFRESH_INTERVAL_MS, timerToken);
        }

    }

    @Override
    public void cancelTimers() {
        javax.ejb.TimerService timerService = context.getTimerService();
        Collection timersList = timerService.getTimers();
        super.cancelTimers(timersList);
    }

    @Override
    public String getTimersInfo() {
        Collection timersList = context.getTimerService().getTimers();
        return super.getTimersInfo(timersList);
    }
}
