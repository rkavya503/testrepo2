package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.BIPEventUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.Location;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.richsite.FDUtils;

public class EvetLocationModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Logger log =
        Logger.getLogger(EvetLocationModel.class.getName());
	
	/** used by JSF to render the table **/
	private AbstractTreeContentProvider<Location> locationProvider;
	private List<Location> eventLocations;
	
	private int endHour;
	private int endMin;
	private int endSec;
	private Date endDate;
	private boolean endImmeidate;
	
	private  Event event;
	private String eventName;
	
	private NativeQueryManager nativeQuery;
	
	public EvetLocationModel(String eventName) {
		this.eventName=eventName;
	}

	public AbstractTreeContentProvider<Location> getLocationProvider(){
		if(this.locationProvider==null){
			locationProvider = new AbstractTreeContentProvider<Location>(){
				private static final long serialVersionUID = 1L;

				@Override
				public List<Location> getChildren(Location parent) {
					return Collections.emptyList();
				}
				@Override
				public boolean hasChildren(Location parent) {
					return false;
				}
				@Override
				public int getTotalRowCount() {
					return getEventLocations().size();
				}
				@Override
				public List<? extends Location> getContents() {
					return getEventLocations();
				}
				@Override
				public void updateModel() {
					this.clearTreeNodeCache(null);
					this.clearSelection();
					sort(getContents(), getConstraint());
				}
				@Override
				public String getRowStyleClass(Location row) {
					if(row.getEndTime()!=null && row.getEndTime().before(new Date())){
						return "grey";
					}
					return "";
				}
				@Override
				public boolean isSelectable(Location current) {
					if(current.getEndTime()!=null && current.getEndTime().before(new Date())){
						return false;
					}
					return true;
				}
			};
		}
		return locationProvider;
	}
	
	public synchronized List<Location> getEventLocations(){
		if( eventLocations ==null && getEvent()!=null){
			String locType=null;
			ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
			String programClass=programManager.getProgramOnly(getEvent().getProgramName()).getProgramClass();
            if("BIP".equals(programClass)){
            	locType = "ABank";
            }else{
            	//TODO
            }
			
			try {
				List<Location> locations = getQueryManager().getEventLocations(locType);
				List<String> locNumbers = BIPEventUtil.getAllEventLocations(getEvent());
				Map<String, Date>  locTime = BIPEventUtil.getAllEventLocationAndEndTime(getEvent());
				for(Iterator<Location> it = locations.iterator();it.hasNext();){
					Location loc = it.next();
					if(locNumbers.indexOf(loc.getLocationNumber().trim())<0){
						it.remove();
					}else{
						loc.setEndTime(locTime.get(loc.getLocationNumber()));
					}
				}
				eventLocations = locations;
			} catch (SQLException e) {
				eventLocations = Collections.emptyList();
				log.error(e.getMessage(),e);
			}
		}
		return eventLocations;
	}
	
	public void endEvent(){
		if(!validate()){
			return;
		}
		List<Location> locations=getLocationProvider().getSelectedObjects();
		List<String> idList = new ArrayList<String>();
		for(Location loc:locations){
			idList.add(loc.getLocationNumber());
		}
		EventManager eventManager = EJBFactory.getBean(EventManager.class);
		eventManager.endEvent(getEvent().getProgramName(),idList, getEvent().getStartTime(),getEndTimeForEvent());
		//reload event
		this.event=null;
		this.eventLocations=null;
		this.locationProvider=null;
		
	}
	
	
	protected boolean validate(){
		Date startTime = getEvent().getStartTime();
		Date endTime = getEndTimeForEvent();
		if(startTime!=null && endTime.before(startTime)){
			FDUtils.addMsgError("end time must be later than start time ");
			return false;
		}
		return true;
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
	
	protected Date getEndTimeForEvent(){
		if(this.endImmeidate) return new Date();
		return construct(endDate, endHour, endMin, endSec);
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

	public Event getEvent() {
		if(event==null && eventName!=null){
			EventManager eventManager = EJBFactory.getBean(EventManager.class);
		    event = (Event)eventManager.getEventWithParticipants(eventName);
		}
		return event;
	}


	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public NativeQueryManager getQueryManager() {
		if (nativeQuery == null)
			nativeQuery = EJB3Factory.getBean(NativeQueryManager.class);
		return nativeQuery;
	}
	
}
