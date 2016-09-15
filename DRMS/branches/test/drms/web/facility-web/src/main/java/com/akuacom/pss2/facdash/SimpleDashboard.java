package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientManagerBean;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.EventParticipantViewLayout;

public class SimpleDashboard  implements Serializable,EventParticipantViewLayout {
	
	private static final String DEFAULT_INTERVAL = "10000";
	private static final String DEFAULT_LINK1 = "www.akuacom.com";// replace with link 
	private static final String DEFAULT_LINK2 = "www.honeywell.com";// replace with link 
	private static final String DEFAULT_LINK1NAME = "AKUACOM";// replace with link 
	private static final String DEFAULT_LINK2NAME = "HONEYWELL";// replace with link 
	private static final String DEFAULT_ACTIVE_MESSAGE = "Active Event. Time remaining {0} hour(s), {1} min(s)";
	private static final String DEFAULT_NOEVENT_MESSAGE = "No active or pending events";
	private static final String DEFAULT_PENDING_MESSAGE = "Pending - Event starts in {0} hour(s), {1} min(s)";
	private static final long MS_IN_MIN = 1000 * 60;
	private static final long MIN_IN_DAY = 24 * 60;
	private static final long MIN_IN_HR = 60;
	private static final Logger log = Logger.getLogger(SimpleDashboard.class);
//    private String ACTIVE_ICON = "images/active.gif";
//    private String PENDING_ICON = "images/active.gif";
	private boolean canParticipantOptOutOfEvent;
	
	public SimpleDashboard() {
		long startTime = System.currentTimeMillis();
		initBean();
		 long duration = System.currentTimeMillis() - startTime;
		log.info("SimpleDashboard construct took "+duration+ "(ms)");
		buildViewLayout();
	}

	public void initBean() {
		String clientName = null;
		
		long startTime = System.currentTimeMillis();
		
        ClientManager cm = EJB3Factory.getBean(ClientManager.class);
		List<String> clients = cm.getClientNamesByParticipant(FDUtils.getParticipantName());
		Iterator<String> item = clients.iterator();
		if(item.hasNext()){
			clientName = item.next(); // retrieve client info from db
		} 
		long duration = System.currentTimeMillis() - startTime;
		log.info("SimpleDashboard get client took "+duration+ "(ms)");		
		
		long startTime7 = System.currentTimeMillis();
		List<JSFEvent> events  = loadClientFromDB(clientName); // retrieve e info from db
		long duration7 = System.currentTimeMillis() - startTime7;
		log.info("SimpleDashboard load events from db took "+duration7+ "(ms)");
		
		long startTime1 = System.currentTimeMillis();
		initConfiguration();
		long duration1 = System.currentTimeMillis() - startTime1;
		log.info("SimpleDashboard initConfiguration took "+duration1+ "(ms)");	
		long startTime2 = System.currentTimeMillis();
		initTodayEvent(events);
		long duration2 = System.currentTimeMillis() - startTime2;
		log.info("SimpleDashboard initTodayEvent took "+duration2+ "(ms)");	
		
		long startTime3 = System.currentTimeMillis();
		initTomorrowEvent(events);
		long duration3 = System.currentTimeMillis() - startTime3;
		log.info("SimpleDashboard initTomorrowEvent took "+duration3+ "(ms)");	
		
		long startTime4 = System.currentTimeMillis();
		initEventStatus();
		long duration4 = System.currentTimeMillis() - startTime4;
		log.info("SimpleDashboard initEventStatus took "+duration4+ "(ms)");	
		long startTime5 = System.currentTimeMillis();
		initTitleMessages();
		long duration5 = System.currentTimeMillis() - startTime5;
		log.info("SimpleDashboard initTitleMessages took "+duration5+ "(ms)");	
	}


	private List<JSFEvent> loadClientFromDB(String clientName) {
		List<JSFEvent> events = new ArrayList<JSFEvent>();
		
		ClientManager.L clientManager = EJB3Factory.getLocalBean(ClientManagerBean.class);
		Participant loggedClient = clientManager.getClientWithEvents(clientName);
		
		if (loggedClient == null) {
			return events;
		}
		
		JSFParticipant jsfLoggedParticipant = new JSFParticipant(loggedClient.getParent());
		FDUtils.setJSFParticipant(jsfLoggedParticipant);
		this.commStatus = loggedClient.getClientStatus().toString();
		this.latitude = loggedClient.getLatitude();
		this.longitude = loggedClient.getLongitude();
		this.name = loggedClient.getParticipantName();
		this.commtime =loggedClient.getCommTime();
		
		if("ONLINE".equals(commStatus)){
			this.deviceOnLine = true;
		}
		
		for (EventParticipant eventC : loggedClient.getEventParticipants()) {
			if(eventC.getEventOptOut()!=0){
				continue;// don't show an event which is in the 'opt out' status.
			}
			Event event = eventC.getEvent();
			if (event != null) {
				JSFEvent jsfEvent = convertToJSFEvent(event);
				events.add(jsfEvent);
			}
		}
        Comparator<JSFEvent> comparator=new Comparator<JSFEvent>(){

			@Override
			public int compare(JSFEvent o1, JSFEvent o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
        	
        };
        //Order by event start time
        Collections.sort(events, comparator);
        
		
		return events;
	}

	private JSFEvent convertToJSFEvent(final Event event) {
		JSFEvent jsfEvent = new JSFEvent();
		jsfEvent.setName(event.getEventName());
		jsfEvent.setProgramName(event.getProgramName());
		jsfEvent.setNotification(event.getIssuedTime());
		jsfEvent.setStart(event.getStartTime());
		jsfEvent.setEnd(event.getEndTime());
		jsfEvent.setStatus(event.getEventStatus().toString());
		
		return jsfEvent;
	}
	
	 public void updateOptControl(){

         ProgramParticipantManager programParticipantManager = EJBFactory.getBean(
                ProgramParticipantManager.class);
         
         ParticipantManager pm = EJB3Factory.getBean(ParticipantManager.class);
         
         for(ProgramParticipant pp:
         	pm.getParticipant(FDUtils.getParticipantName()).
             getProgramParticipants())
            {

        	 int status = this.participantEvent ? 1 : 0;
             pp.setOptStatus(status);
             programParticipantManager.updateProgramParticipant(pp.getProgramName(), pp.getParticipantName(), false, pp);
             
             if( pp.getOptStatus()==1) this.participantEvent = false;
             else this.participantEvent = true;

            }
        

   }
	
	private static boolean isEmptyString(String in){
		if(in==null||in.trim().length()==0){
			return true;
		}
		return false;
	}
	
	
	private void initConfiguration(){
		 SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		 PSS2Features features = systemManager.getPss2Features();
		 
		 interval = String.valueOf(Double.valueOf(systemManager.getPss2Properties().getSimpleDashboardRefreshinterval()).intValue());
		  if(isEmptyString(interval)) interval = DEFAULT_INTERVAL;
		 
		  ParticipantManager partManager=EJB3Factory.getBean(ParticipantManager.class);
		  Participant part=partManager.getParticipantOnly(FDUtils.getParticipantName());//FIXME:slow
		  boolean partEventOptout=part.getPartEventOptoutEnabled();
		  if (part.getDefaultEventOptoutEnabled())
			  partEventOptout=features.isEventOptoutEnabled();
		  
		  optout = features.isSimpleDashBoardOptoutEnabled();
		  eventOptoutEnabled= partEventOptout;
		  
		  String usage = systemManager.getPss2Properties().getStringValue(PropertyName.SIMPLE_DASHBOARD_USAGE);
		  if(features.isSimpleDashBoardWeatherEnabled()){
			  if("YY".equals(usage)){
				  eventListClass = "eventlist";
				  dailyEventlistClass = "dailyEventlist";
				  dailyEventlistulClass = "dailyEventlistul";
				  dailyEventlistulliClass = "dailyEventlistulli";
				  showUsageGraph=true;
				  showSaving=true;
			  }else if("YN".equals(usage)){
				  eventListClass = "eventlist";
				  dailyEventlistClass = "dailyEventlist";
				  dailyEventlistulClass = "dailyEventlistul";//TODO:
				  dailyEventlistulliClass = "dailyEventlistulli";
				  showUsageGraph=true;
				  showSaving=false;
			  }else if("NN".equals(usage)){
				  eventListClass = "eventlist_w";
				  dailyEventlistClass = "dailyEventlist_w";
				  dailyEventlistulClass = "dailyEventlistul_w";
				  dailyEventlistulliClass = "dailyEventlistulli_w";
				  showUsageGraph=false;
				  showSaving=false;
			  }else{
				  eventListClass = "eventlist";
				  dailyEventlistClass = "dailyEventlist";
				  dailyEventlistulClass = "dailyEventlistul";
				  dailyEventlistulliClass = "dailyEventlistulli";
				  showUsageGraph=true;
				  showSaving=true;
			  }
		  }else{
			  if("YY".equals(usage)){
				  eventListClass = "eventlist_noweather"; // with out weather
				  dailyEventlistClass = "dailyEventlist_noweather";
				  dailyEventlistulClass = "dailyEventlistul_noweather";
				  dailyEventlistulliClass = "dailyEventlistulli_noweather";
				  showUsageGraph=true;
				  showSaving=true;
			  }else if("YN".equals(usage)){
				  eventListClass = "eventlist_noweather"; // with out weather
				  dailyEventlistClass = "dailyEventlist_noweather";
				  dailyEventlistulClass = "dailyEventlistul_noweather";
				  dailyEventlistulliClass = "dailyEventlistulli_noweather";
				  showUsageGraph=true;
				  showSaving=false;
			  }else if("NN".equals(usage)){
				  eventListClass = "eventlist_noweather_w"; // with out weather
				  dailyEventlistClass = "dailyEventlist_noweather_w";
				  dailyEventlistulClass = "dailyEventlistul_noweather_w";
				  dailyEventlistulliClass = "dailyEventlistulli_noweather_w";
				  showUsageGraph=false;
				  showSaving=false;
			  }else{
				  eventListClass = "eventlist_noweather"; // with out weather
				  dailyEventlistClass = "dailyEventlist_noweather";
				  dailyEventlistulClass = "dailyEventlistul_noweather";
				  dailyEventlistulliClass = "dailyEventlistulli_noweather";
				  showUsageGraph=true;
				  showSaving=true;
			  }
		  }
		 
		  //DRMS-6394
		  initShowSaving();
		  rssFeedEnable = features.isSimpleDashBoardRssFeedEnabled();
		 
		  akualogo = features.isSimpleDashBoardAkualogoEnabled();
		 
		  link1 = systemManager.getPss2Properties()
         .getStringValue(PropertyName.SIMPLE_DASHBOARD_LINK1);
		  if(isEmptyString(link1)) link1 = DEFAULT_LINK1;
		  
		  link2 = systemManager.getPss2Properties()
         .getStringValue(PropertyName.SIMPLE_DASHBOARD_LINK2);
		  if(isEmptyString(link2)) link2 = DEFAULT_LINK2;
		  
		  linkEnable = features.isSimpleDashBoardLinkEnabled();
		  link1Name = systemManager.getPss2Properties()
	         .getStringValue(PropertyName.SIMPLE_DASHBOARD_LINK1NAME);
		  if(isEmptyString(link1Name)) link1Name = DEFAULT_LINK1NAME;
			  
		  link2Name = systemManager.getPss2Properties()
         .getStringValue(PropertyName.SIMPLE_DASHBOARD_LINK2NAME);
		  if(isEmptyString(link2Name)) link2Name = DEFAULT_LINK2NAME;
		  active_message = systemManager.getPss2Properties()
	         .getStringValue(PropertyName.SIMPLE_ACTIVE_MESSAGE);
			  if(isEmptyString(active_message)) active_message = DEFAULT_ACTIVE_MESSAGE;
		  noevent_message = systemManager.getPss2Properties()
	         .getStringValue(PropertyName.SIMPLE_NOEVENT_MESSAGE);
			  if(isEmptyString(noevent_message)) noevent_message = DEFAULT_NOEVENT_MESSAGE;		
		  pending_message = systemManager.getPss2Properties()
	         .getStringValue(PropertyName.SIMPLE_PENDING_MESSAGE);
			  if(isEmptyString(pending_message)) pending_message = DEFAULT_PENDING_MESSAGE;	
		
	      String utilName = systemManager.getPss2Properties().getUtilityName();
	      
		  if ( (utilName.contains("pge")) || (utilName.equalsIgnoreCase("pge"))){
			this.utility = "pge";
		  }else if((utilName.contains("sce")) ||(utilName.equalsIgnoreCase("sce"))) {
			this.utility = "sce";
		  }else if((utilName.contains("nrcan")) | (utilName.equalsIgnoreCase("nrcan"))) {
			this.utility = "nrcan";
		  }else if((utilName.contains("sdge")) ||(utilName.equalsIgnoreCase("sdge"))) {
			this.utility = "sdge";
		  }else if((utilName.contains("clp")) ||(utilName.equalsIgnoreCase("clp"))) {
			this.utility = "clp";
		  }else if((utilName.contains("hyw")) ||(utilName.equalsIgnoreCase("hyw"))) {
			this.utility = "hyw";
		  }else if((utilName.contains("akua")) ||(utilName.equalsIgnoreCase("akua"))) {
			this.utility = "akua";
		  }else if((utilName.contains("cot")) ||(utilName.equalsIgnoreCase("cot"))) {
			this.utility = "cot";
		  }else if((utilName.contains("duke")) ||(utilName.equalsIgnoreCase("duke"))) {
			this.utility = "duke";
		  }else if((utilName.contains("smud")) ||(utilName.equalsIgnoreCase("smud"))) {
			this.utility = "smud";
		  }
		
	}
	private void initShowSaving(){
		if(this.savingEnergy==0){
			this.setShowSaving(false);
		}else{
			this.setShowSaving(true);
		}
	}
	private void initTitleMessages() {
		EventStatus eventStatus = getEventStatus();
		
		if (eventStatus.equals(EventStatus.noEvent)) {
			titleMessage = this.noevent_message;
			hdrLftCss = "noEventHdr_lft";
			hdrContCss="noEventHdr_cont";
			hdrRightCss="noEventHdr_right";
		}

		if (eventStatus.equals(EventStatus.pending)) {
			Date nextEventStart = todayEvents.get(0).getStart();
			for (JSFEvent event : todayEvents) {
				nextEventStart = getLtDate(event.getStart(), nextEventStart);
			}
			//Map<String, Long> map = remainingTime(nextEventStart);			
			String messageTemplate = this.pending_message;
			//titleMessage = MessageFormat.format(messageTemplate,map.get("diffHours"),map.get("diffMinutes"));
			titleMessage = getTitleMsg(nextEventStart,messageTemplate);
			hdrLftCss = "hdr_lft";
			hdrContCss="hdr_cont";
			hdrRightCss="hdr_right";
		}

		if (eventStatus.equals(EventStatus.active)) {
			Date nextEventEnd = todayEvents.get(0).getEnd();
			for (JSFEvent event : todayEvents) {
				nextEventEnd = getLtDate(event.getEnd(), nextEventEnd);
			}
			
			//Map<String, Long> map = remainingTime(nextEventEnd);
			String messageTemplate = this.active_message;
			//titleMessage = MessageFormat.format(messageTemplate,map.get("diffHours"),map.get("diffMinutes"));
			titleMessage = getTitleMsg(nextEventEnd,messageTemplate);
			hdrLftCss = "activeHdr_lft";
			hdrContCss="activeHdr_cont";
			hdrRightCss="activeHdr_right";
		}
	}
	private String getTitleMsg(Date nextEventStartOrEnd , String messageTemplate){
		if(null == nextEventStartOrEnd){
			return messageTemplate;
		}
		Map<String, Long> map = remainingTime(nextEventStartOrEnd);
		String titleMsg = MessageFormat.format(messageTemplate,map.get("diffHours"),map.get("diffMinutes"));
		return titleMsg;
	}
	private void initEventStatus() {
		if (isEmptyList(todayEvents)) {
			eventStatus = EventStatus.noEvent;
			activeEventName = null;
			return;
		}

		for (JSFEvent event : todayEvents) {
			if (event.getStart().before(new Date())) {
				eventStatus = EventStatus.active;
				active = true;
				activeEventName = event.getName();
				return;
			}
		}
		activeEventName = null;
		eventStatus = EventStatus.pending;
		pending = true;
	}

	private void initTomorrowEvent(List<JSFEvent> eventIn) {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DAY_OF_YEAR, 1);
		
		List<JSFEvent> todayEventTemp = getEventsbyDate(eventIn,cal.getTime());

		tomorrowEvents = todayEventTemp;
		if(tomorrowEvents.size()>0){
    		this.setHasTomorrowList(true);
    	}
	}

	private List<JSFEvent> getEventsbyDate(List<JSFEvent> events,Date today) {
	    List<JSFEvent> todayEventTemp = new ArrayList<JSFEvent>();
	    
	    for(JSFEvent event : events){
	    	if(DateUtils.isSameDay(today, event.getStart())){
	    		todayEventTemp.add(event);
	    	}
	    }
		return todayEventTemp;
	}

	private void initTodayEvent(List<JSFEvent> eventIn) {
	    List<JSFEvent> todayEventTemp = getEventsbyDate(eventIn,new Date());

    	todayEvents = todayEventTemp;
    	if(todayEvents.size()>0){
    		this.setHasTodayList(true);
    	}
        
	}

	private static Date getLtDate(Date date1, Date date2) {
		if(null == date1 || null == date2){
			return null;
		}

		if (date1.before(date2)) {
			return date1;
		}
		return date2;

	}
	
	public static Map<String, Long> remainingTime(Date eventEndTime) {

		Date useDate = eventEndTime;
		Date today = new Date();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(useDate);
		calendar2.setTime(today);

		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();

		long diffMS = (milliseconds1 - milliseconds2);

		long diffMinutes = diffMS / MS_IN_MIN + 1;
		long diffDays = diffMinutes / MIN_IN_DAY;
		diffMinutes -= diffDays * MIN_IN_DAY;
		long diffHours = diffMinutes / MIN_IN_HR;
		diffMinutes -= diffHours * MIN_IN_HR;

		Map<String, Long> map = new HashMap<String, Long>();
		map.put("diffDays", diffDays);
		map.put("diffHours", diffHours);
		map.put("diffMinutes", diffMinutes);
		return map;

	}

	private static boolean isEmptyList(@SuppressWarnings("rawtypes") List in) {
		if (in == null || in.size() == 0) {
			return true;
		}
		return false;
	}
	
	private String hdrLftCss = "hdr_lft";
	private String hdrContCss="hdr_cont";
	private String hdrRightCss="hdr_right";
	private boolean hasTodayList;
	private boolean hasTomorrowList;
	private String interval;
	private boolean optout;
	private boolean eventOptoutEnabled;
	private boolean showUsageGraph;
	private boolean showSaving;
	private boolean deviceOnLine;
	private boolean rssFeedEnable;
	private boolean akualogo;
	private String link1;
	private String link2;
	private boolean participantEvent;
	private String titleCss;
	private String titleMessage;
	private List<JSFEvent> todayEvents;
	private List<JSFEvent> tomorrowEvents;
	private double savingEnergy;
	public EventStatus eventStatus;
	private boolean active;
	private boolean pending;
	private String commStatus;
	private double latitude;
	private double longitude;
	private String name;
	private Date commtime;
	private String active_message;
	private String noevent_message;
	private String pending_message;
	private String eventListClass;
	private String dailyEventlistClass;
	private String dailyEventlistulClass;
	private String dailyEventlistulliClass;
	private String utility;
	private String activeEventName;
	//TODO:
	private boolean linkEnable;
	private String link1Name;
	private String link2Name;
//    private boolean booleanTodayEvents;
//    private boolean booleanTomorrowEvents;
    

    public boolean isBooleanTomorrowEvents() {
        return this.getTomorrowEvents().isEmpty();
    }

//    public void setBooleanTomorrowEvents(boolean booleanTomorrowEvents) {
//        this.booleanTomorrowEvents = booleanTomorrowEvents;
//    }

    public boolean isBooleanTodayEvents() {
        return this.getTodayEvents().isEmpty();
    }

//    public void setBooleanTodayEvents(boolean booleanTodayEvents) {
//        this.booleanTodayEvents = booleanTodayEvents;
//    }

	
	public String getActiveEventName() {
		return activeEventName;
	}

	public void setActiveEventName(String activeEventName) {
		this.activeEventName = activeEventName;
	}

	public String getUtility() {
		return utility;
	}

	public void setUtility(String utility) {
		this.utility = utility;
	}

	public String getDailyEventlistClass() {
		return dailyEventlistClass;
	}

	public void setDailyEventlistClass(String dailyEventlistClass) {
		this.dailyEventlistClass = dailyEventlistClass;
	}

	public String getDailyEventlistulClass() {
		return dailyEventlistulClass;
	}

	public void setDailyEventlistulClass(String dailyEventlistulClass) {
		this.dailyEventlistulClass = dailyEventlistulClass;
	}

	public String getDailyEventlistulliClass() {
		return dailyEventlistulliClass;
	}

	public void setDailyEventlistulliClass(String dailyEventlistulliClass) {
		this.dailyEventlistulliClass = dailyEventlistulliClass;
	}

	public String getEventListClass() {
		return eventListClass;
	}

	public void setEventListClass(String eventListClass) {
		this.eventListClass = eventListClass;
	}

	public String getHdrLftCss() {
		return hdrLftCss;
	}

	public void setHdrLftCss(String hdrLftCss) {
		this.hdrLftCss = hdrLftCss;
	}

	public String getHdrContCss() {
		return hdrContCss;
	}

	public void setHdrContCss(String hdrContCss) {
		this.hdrContCss = hdrContCss;
	}

	public String getHdrRightCss() {
		return hdrRightCss;
	}

	public void setHdrRightCss(String hdrRightCss) {
		this.hdrRightCss = hdrRightCss;
	}

	public boolean isHasTodayList() {
		return hasTodayList;
	}

	public void setHasTodayList(boolean hasTodayList) {
		this.hasTodayList = hasTodayList;
	}

	public boolean isHasTomorrowList() {
		return hasTomorrowList;
	}

	public void setHasTomorrowList(boolean hasTomorrowList) {
		this.hasTomorrowList = hasTomorrowList;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public boolean isOptout() {
		return optout;
	}

	public void setOptout(boolean optout) {
		this.optout = optout;
	}

	public boolean isShowUsageGraph() {
		return showUsageGraph;
	}

	public void setShowUsageGraph(boolean showUsageGraph) {
		this.showUsageGraph = showUsageGraph;
	}

	public boolean isShowSaving() {
		return showSaving;
	}

	public void setShowSaving(boolean showSaving) {
		this.showSaving = showSaving;
	}

	public boolean isDeviceOnLine() {
		return deviceOnLine;
	}

	public void setDeviceOnLine(boolean deviceOnLine) {
		this.deviceOnLine = deviceOnLine;
	}

	public boolean isAkualogo() {
		return akualogo;
	}

	public void setAkualogo(boolean akualogo) {
		this.akualogo = akualogo;
	}

	public String getLink1() {
		return link1;
	}

	public void setLink1(String link1) {
		this.link1 = link1;
	}

	public String getLink2() {
		return link2;
	}

	public void setLink2(String link2) {
		this.link2 = link2;
	}

	public boolean isParticipantEvent() {
		return participantEvent;
	}

	public void setParticipantEvent(boolean participantEvent) {
		this.participantEvent = participantEvent;
	}

	public String getTitleCss() {
		return titleCss;
	}

	public void setTitleCss(String titleCss) {
		this.titleCss = titleCss;
	}

	public String getTitleMessage() {
		return titleMessage;
	}

	public void setTitleMessage(String titleMessage) {
		this.titleMessage = titleMessage;
	}

	public List<JSFEvent> getTodayEvents() {
		return todayEvents;
	}

	public void setTodayEvents(List<JSFEvent> todayEvents) {
		this.todayEvents = todayEvents;
	}

	public List<JSFEvent> getTomorrowEvents() {
		return tomorrowEvents;
	}

	public void setTomorrowEvents(List<JSFEvent> tomorrowEvents) {
		this.tomorrowEvents = tomorrowEvents;
	}

	public double getSavingEnergy() {
		return savingEnergy;
	}

	public void setSavingEnergy(double savingEnergy) {
		this.savingEnergy = savingEnergy;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(String commStatus) {
		this.commStatus = commStatus;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommtime() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(commtime==null){
			return "This device has not connected to server yet.";
		}
		return "The last contact time is : "+format.format(commtime);
	}

	public void setCommtime(Date commtime) {
		this.commtime = commtime;
	}

	public boolean isRssFeedEnable() {
		return rssFeedEnable;
	}

	public void setRssFeedEnable(boolean rssFeedEnable) {
		this.rssFeedEnable = rssFeedEnable;
	}

	public boolean isLinkEnable() {
		return linkEnable;
	}

	public void setLinkEnable(boolean linkEnable) {
		this.linkEnable = linkEnable;
	}

	public String getLink1Name() {
		return link1Name;
	}

	public void setLink1Name(String link1Name) {
		this.link1Name = link1Name;
	}

	public String getLink2Name() {
		return link2Name;
	}

	public void setLink2Name(String link2Name) {
		this.link2Name = link2Name;
	}
	

	public void optoutListener(ActionEvent e) {
		eventName = e.getComponent().getAttributes().get("eventName")
				.toString();
	}


	public String optoutAction() {

		EventManager em = EJBFactory.getBean(EventManager.class);
		em.removeParticipantFromEvent(eventName, FDUtils.getParticipantName());
        this.initBean();
        this.initEventStatus();

        
		return null;
	}

        	/** The event name. */
	private String eventName;


	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public boolean isEventOptoutEnabled() {
		return eventOptoutEnabled && getCanParticipantOptOutOfEvent();
	}

	public void setEventOptoutEnabled(boolean eventOptoutEnabled) {
		this.eventOptoutEnabled = eventOptoutEnabled;
	}

	public boolean getCanParticipantOptOutOfEvent() {
		return this.canParticipantOptOutOfEvent;
	}

	public void setCanParticipantOptOutOfEvent(boolean value) {
		this.canParticipantOptOutOfEvent = value;
		
	}
	private void buildViewLayout(){
		try {
			getViewBuilderManager().buildEventParticipantViewLayout(this);
		} catch (NamingException e) {
			// log exception
		}
		
	}
	private ViewBuilderManager getViewBuilderManager() throws NamingException{
		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}


}


