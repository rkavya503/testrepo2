package com.akuacom.pss2.web.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.utils.lang.DateUtil;

public abstract class DemoEvent implements EventConstants, Serializable{

	private static final long serialVersionUID = 941512218126291572L;
	
	public static final int DEFAULT_NOTICE = 2;
	public static final int DEFAULT_DURATION = 4;

	public static final int NOTIFICATION_OFFSET = 1;
	public static final int NEAR_OFFSET_DEFAULT = 1;
	
	public static final int MIN_NOTICE = 2;
	
	protected static final String DEMO_EVENT_PAGE = "demoEvent";

	//protected String[] enabledSignalTypes;

	public enum TimeChange {
		None, Init, NotificationTime, NotificationDate, Notice, startDate, StartTime,
		Duration, EndTime, StartTimeOffSet, EventOffSet, EndTimeOffSet, Now
	}

	/**
	 * Notification time of an Event
	 */
	protected DateStructure notificationTime;

	/**
	 * Notice period of an event, in minutes
	 */
	protected int notice = DEFAULT_NOTICE;

	/**
	 * Start time of an event
	 */
	protected DateStructure startTime;

	/**
	 * duration of an event, in minutes
	 */
	protected int duration = DEFAULT_DURATION;

	protected List<JSFDemoEventInfo> eventInstances = new ArrayList<JSFDemoEventInfo>();

	protected JSFDemoEventInfo notificationEvent;

	protected JSFDemoEventInfo nearEvent;

	protected JSFDemoEventInfo startEvent;

	protected JSFDemoEventInfo endEvent;
	
	/**
	 * end time of an event
	 */
	protected DateStructure endTime;
	
	protected List<SignalDef> enabledSignals;
	
	protected List<String> enabledSignalTypes;

    protected boolean enableNowNotification;

	/** indicates which time has been reset by end user **/
	protected TimeChange timeChanged = TimeChange.Init;

    private boolean nowUsed = false;

	public DemoEvent(boolean phaseListener) {
		
		creationType.clear();
		creationType.add(CREATION_TYPE_TIMING);
		creationType.add(CREATION_TYPE_DURATION);
		
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		PSS2Features features = systemManager.getPss2Features();
		
		ProgramManager programManager = EJB3Factory.getBean(ProgramManager.class);
		Program program = programManager.findProgramPerfByProgramName(this.getProgramName());
		
		boolean demoEventDefaultTimeEnabled = features.isDemoEventDefaultTimeEnabled();

		int demoEventDefaultDurationTime = program.getDefaultDuration()==null?Program.DEFAULT_DURATION:program.getDefaultDuration().intValue();
		
		setDuration(demoEventDefaultDurationTime);
        notificationTime = new DateStructure();
        Date date = getCurrentClientTime();
        startTime = new DateStructure(DateUtil.offSetBy(date,
                NOTIFICATION_OFFSET + DEFAULT_NOTICE));

        endTime = new DateStructure();
        endTime.setTime(DateUtil.offSetBy(getStart(), duration));
        
		enabledSignalTypes = getEnabledSignalTypes();

		updateModel();
		if (phaseListener) {
			addPhaseListener();
		}
		if(demoEventDefaultTimeEnabled){
			setNowUsed(true);
			timeChanged = TimeChange.Now;
			this.updateModel();
		}
	}

	public DemoEvent() {
		this(false);
	}
	
	public List<String> getEnabledSignalTypes() {
		if(this.enabledSignalTypes == null){
			ProgramManager programManager = EJB3Factory
				.getLocalBean(ProgramManager.class);
			enabledSignals = programManager.findSignals(this
					.getProgramName());
			enabledSignalTypes = new ArrayList<String>(enabledSignals.size());
			for (Iterator<SignalDef> it= enabledSignals.iterator();it.hasNext();) {
				SignalDef def = it.next();
				String str = def.getSignalName();
				// Pending is not an editable signal type
				if (!str.equalsIgnoreCase(SIGNAL_PENDDING)) {
					enabledSignalTypes.add(str);
				}else{
					it.remove();
				}
			}
		}
		return enabledSignalTypes;
	}
	
	public List<SignalDef> getEnabledSignals(){
		if(enabledSignals==null){
			getEnabledSignalTypes();
		}
		return this.enabledSignals;
	}
	
	
	protected void addPhaseListener() {

	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlCal;
		try {
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			return xmlCal;
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}

	private void updateEventDate() {
		for (int i = 0; i < this.eventInstances.size(); i++) {
			JSFDemoEventInfo info = eventInstances.get(i);

			final Calendar current = Calendar.getInstance();
			current.setTime(info.getDateTime());
			int hour = current.get(Calendar.HOUR_OF_DAY);
			int min = current.get(Calendar.MINUTE);

			current.setTime(getStart());
			current.set(Calendar.HOUR_OF_DAY, hour);
			current.set(Calendar.MINUTE, min);
			current.set(Calendar.SECOND, 0);
			current.set(Calendar.MILLISECOND, 0);

			info.setDateTime(current.getTime());
		}
	}

	public void updateModel() {
		int oldNotice=0;
		clearMessage();
		int nearOffset = isNowUsed() ? 0 : NEAR_OFFSET_DEFAULT;
        switch (timeChanged) {
		case Init:
		case StartTime:
		case startDate:
			
//			if (isNowUsed()) {
//				notificationTime.setTime(new Date());
//				if(notificationEvent!=null){
//					notificationEvent.setDateTime(getNotification());
//				}
//			}
			this.endTime.setDateOnly(this.getStartDateOnly());//make sure end time and start time blong to the same day
			notice = DateUtil.minuteOffset(getNotification(), getStart());
			duration = DateUtil.minuteOffset(getStart(), getEnd());
			// nearEvent.setOffsetFromNotification(NEAR_OFFSET_DEFAULT);
			updateEventInstances();
			//default configurations
			//idx    phase             time   offset    mode      price     bid
			//---    -----------        -----  ------    -----     ---
			// 0     notification(FAR)  14:23   0
			// 1     near               14:24   1
			// 2     start              14:28   5        moderate  0.0       0.0  
			// 3     event1             14:33   10       high      0.0       0.0  
			// 4     end                14:38   15
			if (eventInstances == null || eventInstances.isEmpty()) {
				eventInstances = new ArrayList<JSFDemoEventInfo>();
				//Notification event is the first event instance (index=0)
				notificationEvent = new JSFDemoEventInfo(getNotification(), 0,
						 "Far(Notification)",enabledSignalTypes,true,enabledSignals);
				eventInstances.add(notificationEvent);

				//start event is the second event instance
				nearEvent = new JSFDemoEventInfo(DateUtil.offSetBy(
						getNotification(), nearOffset),
                        nearOffset, "Near",enabledSignalTypes,true,enabledSignals);
				
				nearEvent.setOffsetEditable(true);
				eventInstances.add(nearEvent);
				
				//start event is the third event instance (index = 2)
				startEvent = new JSFDemoEventInfo(getStart(), notice,
						"Active(Start)", enabledSignalTypes, MODEL_LEVELS.MODERATE,enabledSignals);
				eventInstances.add(startEvent);
				startEvent.setAddable(true);
				
				JSFDemoEventInfo evt1 = new JSFDemoEventInfo(getStart(), notice+(DEFAULT_DURATION)/2,
						"Active", enabledSignalTypes, MODEL_LEVELS.HIGH,enabledSignals);
				evt1.setAddable(true);
				evt1.setRemovable(true);
				evt1.setReadOnly(false);
				evt1.setOffsetEditable(true);
				eventInstances.add(evt1);
				
				endEvent = new JSFDemoEventInfo(getEnd(), notice + duration,
						 "None(end)",enabledSignalTypes,true,enabledSignals);
				eventInstances.add(endEvent);
				
				if (isNowUsed()) {
					nowSelectedAction();
				}
				
			} else {
				if (isNowUsed()) {
					nowSelectedAction();
				}else{
				//timing
				oldNotice = startEvent.getOffsetFromNotification();
				for (JSFDemoEventInfo eventInstance : eventInstances) {
	                if ("Active".equals(eventInstance.getEventStatus())) {
	                    int i = eventInstance.getOffsetFromNotification();
	                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
	                }
	            }
				startEvent.setOffsetFromNotification(notice);
				endEvent.setOffsetFromNotification(notice + duration);
				// nearEvent.setOffsetFromNotification(NEAR_OFFSET_DEFAULT);
				updateEventInstances();
				}
			}
			break;
		case NotificationTime:
		case NotificationDate:
			notificationEvent.setDateTime(getNotification());
			if(renderedDurationComponent){
				//duration
				oldNotice = startEvent.getOffsetFromNotification();
				startEvent.setOffsetFromNotification(notice);
	            startTime.setTime(DateUtil.offSetBy(getNotification(),notice));
				endEvent.setOffsetFromNotification(notice + duration);
				endTime.setTime(DateUtil.offSetBy(getNotification(),notice+duration));
				
	            for (JSFDemoEventInfo eventInstance : eventInstances) {
	                if ("Active".equals(eventInstance.getEventStatus())) {
	                    int i = eventInstance.getOffsetFromNotification();
	                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
	                }
	            }
				updateEventInstances();
			}else{
				//timing
				oldNotice = startEvent.getOffsetFromNotification();
				notice = DateUtil.minuteOffset(getNotification(), getStart());
				duration = DateUtil.minuteOffset(getStart(), getEnd());
				for (JSFDemoEventInfo eventInstance : eventInstances) {
	                if ("Active".equals(eventInstance.getEventStatus())) {
	                    int i = eventInstance.getOffsetFromNotification();
	                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
	                }
	            }
				startEvent.setOffsetFromNotification(notice);
				endEvent.setOffsetFromNotification(notice + duration);
				// nearEvent.setOffsetFromNotification(NEAR_OFFSET_DEFAULT);
				updateEventInstances();
			}
			break;
		case Notice:
			if (isNowUsed()) {
				nowSelectedAction();
			}else{
				oldNotice = startEvent.getOffsetFromNotification();
				startEvent.setOffsetFromNotification(notice);
	            startTime.setTime(DateUtil.offSetBy(getNotification(),notice));
				endEvent.setOffsetFromNotification(notice + duration);
				endTime.setTime(DateUtil.offSetBy(getNotification(),notice+duration));
	            for (JSFDemoEventInfo eventInstance : eventInstances) {
	                if ("Active".equals(eventInstance.getEventStatus())) {
	                    int i = eventInstance.getOffsetFromNotification();
	                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
	                }
	            }
				updateEventInstances();
			}
			break;	
		case Now:
			nowSelectedAction();
            
			break;
		case Duration:
			if (isNowUsed()) {
				nowSelectedAction();
			}else{
				// change endTime when duration changes
				endTime.setTime(DateUtil.offSetBy(getStart(), duration));
				endEvent.setOffsetFromNotification(notice + duration);
				updateEventInstances();
			}
			
			break;	
		case EndTime:
			if (isNowUsed()) {
				nowSelectedAction();
			}else{
				duration = DateUtil.minuteOffset(getStart(), getEnd());
				endEvent.setOffsetFromNotification(notice + duration);
				updateEventInstances();	
			}
			break;
		}
		// update timing and validation
		timeChanged = TimeChange.None;
	}

	
	private void nowSelectedAction(){
		// change notificationTime when notice time changes
        notificationTime.setTime(new Date());
        notificationEvent.setDateTime(getNotification());
        if(renderedDurationComponent){
        	//duration
        	int oldNotice = startEvent.getOffsetFromNotification();
			startEvent.setOffsetFromNotification(notice);
            startTime.setTime(DateUtil.offSetBy(getNotification(),notice));
			endEvent.setOffsetFromNotification(notice + duration);
			endTime.setTime(DateUtil.offSetBy(getNotification(),notice+duration));
			
            for (JSFDemoEventInfo eventInstance : eventInstances) {
                if ("Active".equals(eventInstance.getEventStatus())) {
                    int i = eventInstance.getOffsetFromNotification();
                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
                }
            }
			updateEventInstances();
        }else{
        	//timing
			int oldNotice = startEvent.getOffsetFromNotification();
			notice = DateUtil.minuteOffset(getNotification(), getStart());
			duration = DateUtil.minuteOffset(getStart(), getEnd());
			for (JSFDemoEventInfo eventInstance : eventInstances) {
                if ("Active".equals(eventInstance.getEventStatus())) {
                    int i = eventInstance.getOffsetFromNotification();
                    eventInstance.setOffsetFromNotification(i - oldNotice + notice);
                }
            }
			startEvent.setOffsetFromNotification(notice);
			endEvent.setOffsetFromNotification(notice + duration);
			// nearEvent.setOffsetFromNotification(NEAR_OFFSET_DEFAULT);
			updateEventInstances();
        }
	}
	
	protected abstract void reportError(String msg);

	public abstract String getProgramName();

	public abstract String getEventID();

	protected void updateEventInstances() {
		for (Iterator<JSFDemoEventInfo> it = eventInstances.iterator(); it
				.hasNext();) {
			JSFDemoEventInfo evt = it.next();
			evt.setDateTime(DateUtil.offSetBy(getNotification(),
					evt.getOffsetFromNotification()));
		}
	}
	
	public void startTimeChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.StartTime;
		}
	}

	public void startDateChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.startDate;
		}
	}

	public void noticeTimeChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.Notice;
		}
	}

	public void notificationDateChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.NotificationDate;
		}
	}

	public void notificationTimeChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.NotificationTime;
		}
	}

	public void durationTimeChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.Duration;
		}
	}

	public void endTimeChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.EndTime;
		}
	}

	public void startOffSetChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.StartTimeOffSet;
		}
	}

	public void endOffSetChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.EndTimeOffSet;
		}
	}

	public void eventOffSetChange(ValueChangeEvent event) {
		if (!event.getOldValue().equals(event.getNewValue())) {
			timeChanged = TimeChange.EventOffSet;
		}
	}

	public List<JSFDemoEventInfo> getEvents() {
		return eventInstances;
	}


    public void nowNotification() {
        if (isNowUsed()) {
            timeChanged = TimeChange.Now;
        } else {
            timeChanged = TimeChange.Init;
        }
        this.updateModel();
    }

	public void addSingalEntry(ActionEvent evt) {
		String id = evt.getComponent().getAttributes().get("evtid").toString();
		int idx = -1;
		if (id != null) {
			for (int i = 0; i < eventInstances.size(); i++) {
				if (eventInstances.get(i).getId().equals(id)) {
					idx = i;
					break;
				}
			}
		}
		if (idx >= 0) {
			JSFDemoEventInfo thisEvent = eventInstances.get(idx);
			JSFDemoEventInfo evtinfo = JSFDemoEventInfo.nextEvent(thisEvent,
					getNextInterval(thisEvent),enabledSignals);
			evtinfo.setAddable(true);
			evtinfo.setRemovable(true);
			eventInstances.add(idx + 1, evtinfo);
		}
		reportError(validateTiming());
	}

	protected int getNextInterval(JSFDemoEventInfo event) {
		int offset = event.getOffsetFromNotification();
		int idx = eventInstances.indexOf(event);
		int nextOffset = 0;
		if (idx + 1 < eventInstances.size()) {
			nextOffset = eventInstances.get(idx + 1)
					.getOffsetFromNotification();
		}
		if (nextOffset >= offset + 2) {
			return (nextOffset - offset) / 2;
		}
		return 1;
	}

	public void removeSingalEntry(ActionEvent evt) {
		String id = evt.getComponent().getAttributes().get("evtid").toString();
		int idx = -1;
		if (id != null) {
			for (int i = 0; i < eventInstances.size(); i++) {
				if (eventInstances.get(i).getId().equals(id)) {
					idx = i;
					break;
				}
			}
		}
		if (idx >= 0)
			eventInstances.remove(idx);

		if (idx > 2) {
			eventInstances.get(idx - 1).setAddable(true);
		}
		reportError(validateTiming());
	}

	public String validateTiming() {
		//if ((getNotification().getTime() - (new Date()).getTime()) / 1000 < 60) {
		//	return "Notification time must be at least 1 minute later than current time";
		//}
		DateStructure now = new DateStructure(new Date());
		if(getNotification().before(now.getTime()))
			return "Notification time in the past";
		
		int previousOffset = 0;
		int offset = 0;
		// from near notification to far notification
		for (int i = 1; i < this.eventInstances.size(); i++) {
			JSFDemoEventInfo info = eventInstances.get(i);
			previousOffset = offset;
			offset = info.getOffsetFromNotification();
            if (isNowUsed() && i < 3) {
                if (offset != previousOffset) {
//                    return "Notification, issue, and start time should all be now when Now button is checked.";
                }
            } else {
                if (offset <= previousOffset) {
                	String result = "Time Offset values should be in ascendant order.";
                	if(info.getEventStatus().contains("Start")){
                		result+="\nStart time is not correct.";
                	}
                	if(info.getEventStatus().contains("end")){
                		result+="\nEnd time is not correct.";
                	}
                    return result;
                }
            }
		}
		
		if(!DateUtils.isSameDay(this.getStart(), this.getEnd())){
			return "Start time and End time should in the same day";
		}
		
		return null;
	}
	
	public UtilityDREvent toUtilityDREvent() {

		UtilityDREvent drevent = new UtilityDREvent();
		// event timing
		drevent.setProgramName(getProgramName());
		drevent.setEventIdentifier(getEventID());
		drevent.setSchemaVersion(XML_SCHEMA_VERSION);

		EventTiming et = new EventTiming();
		et.setStartTime(toXMLGregorianCalendar(getStart()));
		et.setEndTime(toXMLGregorianCalendar(getEnd()));
		et.setNotificationTime(toXMLGregorianCalendar(getNotification()));
		drevent.setEventTiming(et);

		// Event information
		EventInformation evtInfo = new EventInformation();
		drevent.setEventInformation(evtInfo);

		for (String signalType : enabledSignalTypes) {
			EventInfoInstance evtInstance = new EventInfoInstance();

			String openADRSignalType =getSingalNameInOpenAdr(signalType);
			evtInstance.setEventInfoTypeName(openADRSignalType);

			evtInstance.setSchemaVersion(XML_SCHEMA_VERSION);
			evtInstance.setValues(toEventInfoInstanceValues(signalType));
			evtInfo.getEventInfoInstance().add(evtInstance);
		}
		return drevent;
	}

	public static String getSingalNameInOpenAdr(String signalName){
		if(SIGNAL_MODE.equalsIgnoreCase(signalName))
			return SIGNAL_MODE_OPENADR;
		return signalName;
	}
	
	public EventInfoInstance.Values toEventInfoInstanceValues(String signalType) {
		EventInfoInstance.Values values = new EventInfoInstance.Values();
		List<JSFDemoEventInfo> events = this.getEvents();

		// first is notification
		// second id near
		// last is end
		for (int i = 2; i < events.size() - 1; i++) {
			JSFDemoEventInfo info = events.get(i);
			values.getValue().add(info.toEventInfoValue(signalType, notice));
		}
		return values;
	}
	
	// ------------------- setters and getters ------------------//
	public Date getCurrentClientTime() {
		// should return current time on client side
		Date date = new Date();
		final Calendar current = Calendar.getInstance();
		current.setTime(date);
		current.set(Calendar.SECOND, 0);
		current.set(Calendar.MILLISECOND, 0);
		return current.getTime();
	}

	public void setStart(Date date) {
		startTime.setDateOnly(date);
		updateEventDate();
	}

	public Date getStart() {
		return startTime.getTime();
	}

	public void setStartTimeStr(String time) {
		startTime.setTimeStr(time);
	}

	public String getStartTimeStr() {
		return startTime.getTimeStr();
	}

	public void setNotification(Date date) {
		notificationTime.setDateOnly(date);
		updateEventDate();
	}

	public Date getNotification() {
		return notificationTime.getTime();
	}

	public void setNotificationTimeStr(String timeStr) {
		notificationTime.setTimeStr(timeStr);
	}

	public String getNotificationTimeStr() {
		return notificationTime.getTimeStr();
	}

	public void setEndTimeStr(String timeStr) {
		endTime.setTimeStr(timeStr);
	}

	public String getEndTimeStr() {
		return endTime.getTimeStr();
	}

	public Date getEnd() {
		return endTime.getTime();
	}

	public int getNotice() {
		return notice;
	}

	public void setNotice(int notice) {
		this.notice = notice;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public DateStructure getNotificationTime() {
		return notificationTime;
	}

	public DateStructure getStartTime() {
		return startTime;
	}

	public List<JSFDemoEventInfo> getEventInstances() {
		return eventInstances;
	}

	public JSFDemoEventInfo getNotificationEvent() {
		return notificationEvent;
	}

	public JSFDemoEventInfo getNearEvent() {
		return nearEvent;
	}

	public JSFDemoEventInfo getStartEvent() {
		return startEvent;
	}

	public JSFDemoEventInfo getEndEvent() {
		return endEvent;
	}

	public DateStructure getEndTime() {
		return endTime;
	}

	public Date getStartDateOnly() {
		return this.startTime.getDateOnly();
	}

	public void setStartDateOnly(Date startDateOnly) {
		this.startTime.setDateOnly(startDateOnly);
	}

	public Date getNotificationDateOnly() {
		return notificationTime.getDateOnly();
	}

	public void setNotificationDateOnly(Date notificationDateOnly) {
		this.notificationTime.setDateOnly(notificationDateOnly);
	}

	//-----------------------------------------------------------------------------
	
	private SignalDef testUseSignalDefNameGetSignalDef(String signalDefName){
		for(SignalDef signalDef :enabledSignals){
			if(signalDef.getSignalName().equalsIgnoreCase(signalDefName)){
				return signalDef;
			}
		}
		return null;
	}
	
	private void testGetSignalLevelDef(){
		
	}
	private void testGetSignalDefType(SignalDef signalDef){
		if(signalDef!=null){
			//LOAD_LEVEL
			//PRICE_RELATIVE
			//LOAD_AMOUNT
			//PRICE_ABSOLUTE
			signalDef.getType();
		}
	}

    public boolean isEnableNowNotification() {
        return enableNowNotification;
    }

    public void setEnableNowNotification(boolean enableNowNotification) {
        this.enableNowNotification = enableNowNotification;
    }

    public boolean isNowUsed() {
        return nowUsed;
    }

    public void setNowUsed(boolean nowUsed) {
        this.nowUsed = nowUsed;
    }
    private void clearMessage(){
    	Iterator messageIterator = FacesContext.getCurrentInstance().getMessages();
    	while (messageIterator.hasNext()) {
    		messageIterator.remove();
    	}
	}
    
    //--------------------------------------------------------------------------------------------------------------------------
    
    public static final String CREATION_TYPE_TIMING="TIME";
	public static final String CREATION_TYPE_DURATION="DURATION";
    private List<String> creationType = new ArrayList<String>();
    private String selectedCreationType = CREATION_TYPE_TIMING;
    private boolean renderedTimingComponent = true;
    private boolean renderedDurationComponent = false;
	/**
	 * @return the creationType
	 */
	public List<String> getCreationType() {
		return creationType;
	}

	/**
	 * @param creationType the creationType to set
	 */
	public void setCreationType(List<String> creationType) {
		this.creationType = creationType;
	}
	
	
	/**
	 * @return the selectedCreationType
	 */
	public String getSelectedCreationType() {
		return selectedCreationType;
	}

	/**
	 * @param selectedCreationType the selectedCreationType to set
	 */
	public void setSelectedCreationType(String selectedCreationType) {
		this.selectedCreationType = selectedCreationType;
	}

	/**
	 * @return the renderedTimingComponent
	 */
	public boolean isRenderedTimingComponent() {
		return renderedTimingComponent;
	}

	/**
	 * @param renderedTimingComponent the renderedTimingComponent to set
	 */
	public void setRenderedTimingComponent(boolean renderedTimingComponent) {
		this.renderedTimingComponent = renderedTimingComponent;
	}

	/**
	 * @return the renderedDurationComponent
	 */
	public boolean isRenderedDurationComponent() {
		return renderedDurationComponent;
	}

	/**
	 * @param renderedDurationComponent the renderedDurationComponent to set
	 */
	public void setRenderedDurationComponent(boolean renderedDurationComponent) {
		this.renderedDurationComponent = renderedDurationComponent;
	}

	/**
	 * Type listener.
	 * 
	 * @param e the e
	 */
	public void typeListener(ActionEvent e){
		if(selectedCreationType.equalsIgnoreCase(CREATION_TYPE_TIMING)){
			renderedTimingComponent = true;
			renderedDurationComponent = false;
		}else{
			renderedTimingComponent = false;
			renderedDurationComponent = true;
		}
	}
	
	public void signalChanged(ActionEvent e) {
		
	}
	
	/**
	 * Gets the available types.
	 * 
	 * @return the available types
	 */
	public List<SelectItem> getAvailableTypes(){
		List<SelectItem> types = new ArrayList<SelectItem>();
		for(String type: creationType){
			types.add(new SelectItem(type));
		}
		return types;
	}
}
