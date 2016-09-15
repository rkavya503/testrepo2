package com.akuacom.pss2.richsite.event;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;

public class EventListDataModel implements Serializable, EventViewLayout {

	private static final long serialVersionUID = 1205685529028647578L;
	private List<ListEvent> events;
	private String selectedPrograms;
	private int selectedCount;
	
	private int endHour;
	private int endMin;
	private int endSec;
	private Date endDate;
	private boolean endImmeidate;
	private String eventName;
	private boolean canDeleteEvent;
	private boolean canOptOutOfEvent;
	private String demoEventNameToDelete;
	private boolean canDeleteDemoEvent;
	
	public boolean getCanDeleteDemoEvent() {
		return canDeleteDemoEvent;
	}

	public void setCanDeleteDemoEvent(boolean canDeleteDemoEvent) {
		this.canDeleteDemoEvent = canDeleteDemoEvent;
	}

	public String getDemoEventNameToDelete() {
		return demoEventNameToDelete;
	}

	public void setDemoEventNameToDelete(String demoEventNameToDelete) {
		this.demoEventNameToDelete = demoEventNameToDelete;
	}

	public EventListDataModel() {
		loadEvents();
		buildViewLayout();
	}

	public List<ListEvent> getEvents() {
		if (events == null || events.isEmpty()) {
			loadEvents();
		}
		return events;
	}
	
	private void loadEvents() {
		// Create the event lists here, cppList and dbpList
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		events = new ArrayList<ListEvent>();

		ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
		
		
        // Note: since these actions are not atomic, the events can be deleted
        // right after been retrieved. So, a null check is necessary.
        for(Event event: eventManager.findAllPerf()) {
        	if (!event.getProgramName().equals(TestProgram.PROGRAM_NAME)) {
                event.setState(eventManager.getEventStatusString(event));
                ListEvent listEvent = new ListEvent(event);
                
                String programClass=programManager.getProgramOnly(event.getProgramName()).getProgramClass();
                if(null != programClass && !programClass.isEmpty() ){
	                if("BIP".equals(programClass)){
	                	listEvent.setManualTerminate(true);
	                }
	                if(programClass.equalsIgnoreCase("DEMO")){
	                	listEvent.setDemoEvent(true);
	                }
                }
                events.add(listEvent);
        	}
		}
        Collections.sort(events);
	}

	public void setEvents(List<ListEvent> events) {
		this.events = events;
	}

	public String getSelectedPrograms() {
		selectedCount = 0;
		StringBuilder sb = new StringBuilder();
		
		Iterator<ListEvent> i = events.iterator();
		while (i.hasNext()) {
			ListEvent event = i.next();
			if (event.isDeleted()) {
				if (selectedCount != 0) {
					sb.append(", ");
				}
				sb.append(event.getEventName());
				selectedCount++;
			}
		}
		
		if (selectedCount == 0) {
			selectedPrograms = "No events selected";
		} else {
			selectedPrograms = sb.toString();
		}
		
		return selectedPrograms;
	}
	
	public boolean isNoneSelected() {
		return selectedCount < 1;
	}

	public void setSelectedPrograms(String selectedPrograms) {
		this.selectedPrograms = selectedPrograms;
	}

	public void addMsgError(String message) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}

	public void eventDeleteAction() throws IOException {
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		Iterator<ListEvent> i = events.iterator();
		while (i.hasNext()) {
			ListEvent event = i.next();
			if (event.isDeleted()) {
				eventManager.removeEvent(event.getProgramName(), event.getEventName());
			}
		}
		this.getEvents().clear();
	}
	public void deleteDemoEvent(){
		String demoEventName = getDemoEventNameToDelete();
		if(null == demoEventName || demoEventName.isEmpty()){
			return;
		}
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		Iterator<ListEvent> i = events.iterator();
		while (i.hasNext()) {
			ListEvent event = i.next();
			if (event.getEventName().equalsIgnoreCase(demoEventName)) {
				eventManager.removeEvent(event.getProgramName(), event.getEventName());
				break;
			}
		}
		this.getEvents().clear();
		
	}
	
	public void endEvent() {
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		String evtName =this.getEventName();
		if(!validate(evtName)) return;
		Iterator<ListEvent> i = events.iterator();
		while (i.hasNext()) {
			ListEvent event = i.next();
			if (event.getEventName().equals(evtName)) {
				eventManager.endEvent(event.getProgramName(), event.getEventName(),this.getEndTime());
			}
		}
		endDate = null;
		endHour = 00;
		endImmeidate = false;
		endMin = 00;
		endSec = 00;
		eventName = null;		
		events= null;
	}
	
	
	public void reset() {
		endDate = null;
		endHour = 00;
		endImmeidate = false;
		endMin = 00;
		endSec = 00;
		eventName = null;
		events= null;
	}
	
	public String close() {
		endDate = null;
		endHour = 00;
		endImmeidate = false;
		endMin = 00;
		endSec = 00;
		eventName = null;
		events= null;
	    return null;
	   }
	
	private Date getSelectedStartTime(String evtName){
		Iterator<ListEvent> i = events.iterator();
		while (i.hasNext()) {
		ListEvent event = i.next();
			if (event.getEventName().equals(evtName)) {
				return event.getStartTime();
			}
		}
		return null;
	}
	
	protected boolean validate(String eventName){
		if(!this.endImmeidate){
			if(this.getEndDate()==null  ){
				FDUtils.addMsgError("End date is required");
				return false;
			}else {
				Date startTime = this.getSelectedStartTime(eventName);
				if (startTime==null ){
					FDUtils.addMsgError("Please try again");
					return false;						
				}else if(startTime!=null && getEndTime().before(startTime)){
					FDUtils.addMsgError("End time must be later than start time");
					return false;
				}
			}
		}else{
			Date startTime = this.getSelectedStartTime(eventName);
			if (startTime==null ){
				FDUtils.addMsgError("Please try again");
				return false;						
			}else if(startTime!=null && startTime.after(new Date())){
				FDUtils.addMsgError("End time must be later than start time");
				return false;
			}
		}
		return true;
	}	
	protected Date getEndTime(){
		if(this.endImmeidate) return null;
		return construct(endDate, endHour, endMin, endSec);
	}
	
	
	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getEndMin() {
		return endMin;
	}

	public void setEndMin(int endMin) {
		this.endMin = endMin;
	}

	public int getEndSec() {
		return endSec;
	}

	public void setEndSec(int endSec) {
		this.endSec = endSec;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isEndImmeidate() {
		return endImmeidate;
	}

	public void setEndImmeidate(boolean endImmeidate) {
		this.endImmeidate = endImmeidate;
	}
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	// option list for time & hour selection
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
	private static List<SelectItem> secList;
	    
	private static String formatTime(int i){
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}
	
	public List<SelectItem> getHourList() {
		return hourList;
	}

	public List<SelectItem> getMinList() {
		return minList;
	}

	public List<SelectItem> getSecList() {
		return secList;
	}
	
	private static Date construct(Date date,int hour,int min, int sec){
		if(date==null) return null;
		final Calendar calendar =  Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, sec);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	static {
		hourList = new ArrayList<SelectItem>();
        for (int i = 0; i < 24; i++) {
        	hourList.add(new SelectItem(i,formatTime(i)));
        }
		
        minList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	minList.add(new SelectItem(i,formatTime(i)));
        }
        
        secList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	secList.add(new SelectItem(i,formatTime(i)));
        }
	}

	@Override
	public boolean getCanDeleteEvent() {
		// TODO Auto-generated method stub
		return this.canDeleteEvent;
	}

	@Override
	public void setCanDeleteEvent(boolean value) {
		this.canDeleteEvent = value;
	}
	@Override
	public boolean getCanOptOutOfEvent() {
		return this.canOptOutOfEvent;
	}

	@Override
	public void setCanOptOutOfEvent(boolean value) {
		this.canOptOutOfEvent = value;
		
	}
	private void buildViewLayout(){
		try {
			getViewBuilderManager().buildEventViewLayout(this);
		} catch (NamingException e) {
			// log exception
		}
		
	}
	private ViewBuilderManager getViewBuilderManager() throws NamingException{
		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}

	
}
