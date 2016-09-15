package com.akuacom.pss2.drw.event.creation;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.DRLocationManager;
import com.akuacom.pss2.drw.admin.FDUtils;
import com.akuacom.pss2.drw.admin.ServiceLocator;
import com.akuacom.pss2.drw.admin.constants.DRWConstants;
import com.akuacom.pss2.drw.event.UiEventDetail;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;
import com.akuacom.pss2.util.EventUtil;
import com.akuacom.utils.lang.DateUtil;

public abstract class AbstractEventCreation implements EvtCreation,Serializable {
	private static final long serialVersionUID = -7608034909996881515L;

	private static final Logger log = Logger.getLogger(AbstractEventCreation.class.getName());
	
	private DREventManager eventManager;
	
	public DREventManager getEvtManager(){
		if(eventManager==null) {
			eventManager = ServiceLocator.findHandler(DREventManager.class,
					"dr-pro/DREventManager/remote");
		}
		
		return eventManager;
	}
	
	private DRLocationManager locationManager;
	
	public DRLocationManager getLocationManager(){
		if(locationManager==null) {
			locationManager = ServiceLocator.findHandler(DRLocationManager.class,
					"dr-pro/DRLocationManager/remote");
		}
		
		return locationManager;
	}
	private EventTiming eventTime;
	public EventTiming getEventTiming() {
		if(eventTime==null){
			eventTime = new EventTiming();
			//initialize with default value in program constraint
		}
		return eventTime;
	}
	
	private Wizard wizard;
	
	private String programName;
	
	protected String eventName;
	
	private Map<String,CommonValidator> validators;
	
	/** sub class to override to provide a different wizard for event creation **/
	abstract protected  Wizard createWizard();
	
	abstract protected void installValidators();
		
	abstract protected void doCreateEvent();
		
	public abstract Date getStartTime();
	
	public abstract Date getEndTime();
	
	public abstract Date getIssuedTime();
	
	private LocationSelection locationSelection;
	
	public abstract String[] getDispatchByLabels();
	public LocationSelection getLocationSelection() {
		if(locationSelection==null){
			locationSelection = new LocationSelection(this){
				private static final long serialVersionUID = 6420953807555072196L;

				@Override
				public String[] getDispatchByLabels() {
					// TODO Auto-generated method stub
					return AbstractEventCreation.this.getDispatchByLabels();
				}};
		}
		return locationSelection;
	}

	public void setLocationSelection(LocationSelection locationSelection) {
		this.locationSelection = locationSelection;
	}
	
	public String getProgramName() {
		return programName;
	}
	
	public String getEventName(){
		if(eventName==null){
			eventName=EventUtil.getEventName();
		}
		return eventName;
	}
	
	public AbstractEventCreation(String programName){
		this.programName = programName;
		getEventTiming().setStartDate(new Date());
		installValidators();
	}
	
	public String getActivePage() {
		return getWizard().getActivePage();
	}

	public void setActivePage(String activePage) {
		this.getWizard().setActivePage(activePage);
	}
	
	public Wizard getWizard(){
		if(this.wizard==null)
			wizard = createWizard();
		return wizard;
	}
	
	public Map<String,CommonValidator> getValidators(){
		if(validators==null){
			validators = new HashMap<String,CommonValidator>();
		}
		return validators;
	}
	
	public void nextPage() {
		CommonValidator val = getValidators().get(getActivePage());
		if(val!=null){
			MSG msg=val.validate(this);
			report(msg);
			if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
				return;
		}
		getWizard().nextPage();
	}
	
	
	public void goToPage(String page, boolean needValidate){
		if(needValidate){
			CommonValidator val = getValidators().get(getActivePage());
			if(val!=null){
				MSG msg=val.validate(this);
				report(msg);
				if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
					return;
			}
		}
		
		getWizard().goToPage(page);
	}
	
	protected void registerValidator(String page,CommonValidator validator){
		getValidators().put(page, validator);
	}
	
	protected void report(MSG msg){
		if(msg!=null){
			switch(msg.type){
			case CommonValidator.MSG_ERROR:
				FDUtils.addMsgError(msg.body);
				break;
			case CommonValidator.MSG_INFO:
				FDUtils.addMsgInfo(msg.body);
				break;
			case CommonValidator.MSG_WARN:
				FDUtils.addMsgWarn(msg.body);
				break;
			}
		}
	}
	
	public void backPage() {
		getWizard().backPage();
	}
	
	/**
	 * 
	 * @return navigation page url
	 */
	public String createEvent(){
		try{
			CommonValidator val = getValidators().get(getActivePage());
			if(val!=null){
				MSG msg=val.validate(this);
				report(msg);
				if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
					return "failure";
			}
			doCreateEvent();
			return goToEventDisplayListPage();
		}catch(Exception e){
			final String s = ErrorUtil.getErrorMessage(e);
			FDUtils.addMsgError(s);
			log.error("pss2.event.create.creationError: " + s);
			return "success";
		}
	}
	
	/** for jsf table components **/
	public abstract TreeContentProvider<UiEventDetail> getSelectedLocations();
	
	public String goToEventDisplayListPage(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			ec.redirect(getProgramPage());
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();			
		return "parent";
	}
	
	public String cancel(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			ec.redirect(getProgramPage());
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();
		return "success";
	}
	

	@Override
	public String getProgramPage() {
		return DRWConstants.BASE_PROGRAM_PAGE;
	}
	
	public void goToConfirm(){
		this.goToPage("confirmation", true);
	}
	
	public void goToEdit(){
		this.goToPage("drEventSchedule", false);
	}
	
	public String validateTiming() {
		 
		if(getEventTiming().getStartTime()==null) return "Start Date & Time is required.";
//		if(getEventTiming().getIssuedTime()==null) return "Issue Date & Time is required.";
		if(getEventTiming().getEndTime()==null) return null;
		//both start time and end time are not null
		if(getEventTiming().getStartTime().after(getEventTiming().getEndTime())){
			return "Start Time must be before End Time.";
		}
		return null;
	}
	
	public void updateEndDate(){
		if(getEventTiming().getEndDate()==null && getEventTiming().getStartDate()!=null)
			getEventTiming().setEndDate(getEventTiming().getStartDate());
		
	}
}
