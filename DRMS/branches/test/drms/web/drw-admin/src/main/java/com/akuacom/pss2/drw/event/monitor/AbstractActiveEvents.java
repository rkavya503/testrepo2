package com.akuacom.pss2.drw.event.monitor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.akuacom.jsf.model.LazyListContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.admin.ServiceLocator;
import com.akuacom.pss2.drw.admin.constants.DRWConstants;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.event.EventDetailAdapter;
import com.akuacom.pss2.drw.event.UiEventDetail;
import com.akuacom.pss2.drw.event.creation.CommonValidator;
import com.akuacom.pss2.drw.event.creation.EventLocationSelection;
import com.akuacom.pss2.drw.event.creation.EventTiming;
import com.akuacom.pss2.drw.event.creation.EvtLocationCandidateProvider;
import com.akuacom.pss2.drw.event.creation.Wizard;
import com.akuacom.pss2.drw.event.creation.WizardComponent;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;
//activeEvents
public class AbstractActiveEvents extends WizardComponent implements Serializable{
	
	private static final Integer LIMIT_SIZE = 1000;
	private String programName;
	public String getProgramName(){
		if(this.programName==null){
			FacesContext context = FacesContext.getCurrentInstance();
			if(context!=null){
				ExternalContext ec = context.getExternalContext();
				if(ec!=null){
					HttpServletRequest request = (HttpServletRequest)ec.getRequest();
	                if(request!=null){
	                    programName =request.getParameter("programName");
	                    if (programName == null )
	                    	programName="SDP";
	               }
				}
			}
		}
		return programName;
	}
	
	private DREventManager eventManager;
	
	public DREventManager getEvtManager(){
		if(eventManager==null) {
			eventManager = ServiceLocator.findHandler(DREventManager.class,
					"dr-pro/DREventManager/remote");
		}
		
		return eventManager;
	}
	
	private static final long serialVersionUID = -2332123409402782870L;
	
	//***JSF Actions
	
	public String getProgramPage() {
		return DRWConstants.BASE_PROGRAM_PAGE;
	}
	
	public AbstractActiveEvents(){
		super();
		resetStartDate();
		
	}
	
	private EventLocationSelection locationSelection;
	
	public EventLocationSelection getLocationSelection() {
		if(locationSelection==null){
			locationSelection = new EventLocationSelection(){
				private static final long serialVersionUID = 5526800398659478149L;

				@Override
				public String[] getDispatchByLabels() {
					return null;//BIP program don't need dispatch locations
				}
				
			};
		}
		return locationSelection;
	}

	public void setLocationSelection(EventLocationSelection locationSelection) {
		this.locationSelection = locationSelection;
	}

	@Override
	protected Wizard createWizard() {
		Wizard wizard = new Wizard("eventList",
				"eventEdit","eventEnd");
		return wizard;
	}
	//TODO: Adapter
	public TreeContentProvider<UiEventDetail> getSelectedParticipants(){
		return new LazyListContentProvider<UiEventDetail>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<UiEventDetail> doGetContents() {
				List<EventDetail> details = getSelectedCandidates();
				if(details==null||details.isEmpty()){
					return Collections.emptyList();
				}
				
				List<UiEventDetail> result = new ArrayList<UiEventDetail>();
				for(int i=0; i<details.size(); i++){
					EventDetailAdapter ui = new EventDetailAdapter();
					ui.setEvtDetail(details.get(i));
					ui.setRowIndex(i);
					result.add(ui);
				}
				return result;
			}
		};
	}
	
	private EvtLocationCandidateProvider locationProvider;
	/**
	 * Location provider for ui display
	 * @return
	 */
	public EvtLocationCandidateProvider getLocationProvider(){
		if(locationProvider==null){
			locationProvider = new EvtLocationCandidateProvider(){
				private static final long serialVersionUID = -7061374719402582381L;
				
				protected void init(){
					AbstractActiveEvents.this.getAllCandidates();
				}
				
				@Override
				public List<EventDetail> getAllCandidates() {
					init();
					return AbstractActiveEvents.this.getAllCandidates();
				}

				@Override
				public String[] getDispatchByLabels() {
					return null;
				}

			};
		}
		return locationProvider;
	}
	/**
	 * Retrieve from database
	 * @return
	 */
	public List<EventDetail> doGetContents() {
		return getEvtManager().getActiveEventLimited(this.getProgramName(), LIMIT_SIZE);
	
	}
	public List<EventDetail> getAllCandidates() {
		if(this.allCandidates==null){
			//initialization
			try{
				allCandidates =  null;//this.getNativeQueryManager().getLocationCandidate(getProgramName());
				List<EventDetail> list = doGetContents();
				
				allCandidates = list;
			}catch(Exception e){
				allCandidates=Collections.emptyList();
			}
		}
		return allCandidates;
	}
	private List<EventDetail> allCandidates;
	/** selected participant to participant this event **/
	private List<EventDetail> selectedCandidates;
	private List<EventDetail> rejectedCandidates;
	
//	EvtLocationCandidateProvider
	
	public List<EventDetail> getSelectedCandidates() {
		if( selectedCandidates==null)
			 selectedCandidates= new ArrayList<EventDetail>();
		return selectedCandidates;
	}
	public void setSelectedCandidates(List<EventDetail> selectedCandidates) {
		this.selectedCandidates = selectedCandidates;
	}
	public List<EventDetail> getRejectedCandidates() {
		if(rejectedCandidates==null)
			rejectedCandidates =new ArrayList<EventDetail>();
		return rejectedCandidates;
	}
	public void setRejectedCandidates(List<EventDetail> rejectedCandidates) {
		this.rejectedCandidates = rejectedCandidates;
	}
	
	private EventTiming eventTime;
	public EventTiming getEventTiming() {
		if(eventTime==null){
			eventTime = new EventTiming();
			//initialize with default value in program constraint
		}
		return eventTime;
	}	
	public String validateCollections() {
		List<EventDetail> list = getLocationProvider().getSelectedObjects();
		String msg = null;
		if(list==null || list.isEmpty()){
			msg = "Cannot Modify Event with empty location list";
		}
		return msg;
	}
	
	public String validateDate() {
		
		if(getEventTiming().getStartTime()==null) return "Date is required.";
		return null;
	}

	public String redirectTo(String url){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec=context.getExternalContext();
		try{
			ec.redirect(url);
		}catch(IOException e){
			return "failure";
		}
		context.responseComplete();
		return "success";
	}
	
	@Override
	protected void installValidators() {
		
		CommonValidator<AbstractActiveEvents> val = new  CommonValidator<AbstractActiveEvents>(){
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractActiveEvents model) {
				String msg = validateCollections();
				if(msg!=null){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				return null;
			}

		};
		
		CommonValidator<AbstractActiveEvents> dateVal = new  CommonValidator<AbstractActiveEvents>(){
			private static final long serialVersionUID = -4749519418706486685L;
			
			@Override
			public MSG validate(AbstractActiveEvents model) {
				String msg = model.validateDate();
				if(msg!=null){
					return new MSG(CommonValidator.MSG_ERROR,msg);
				}
				return null;
			}

		};
		
		registerValidator("eventList", val);
		registerValidator("eventEdit", dateVal);
		registerValidator("eventEnd", dateVal);
	}
	
	
	//***********************
	
	//***JSF Actions
		public void goToEnd() {
			List<EventDetail> list = getLocationProvider().getSelectedObjects();
			this.getSelectedCandidates().clear();
			this.getSelectedCandidates().addAll(list);
			resetStartDate();
			this.goToPage("eventEnd");
		}
		
		public void goToEdit() {
			List<EventDetail> list = getLocationProvider().getSelectedObjects();
			this.getSelectedCandidates().clear();
			this.getSelectedCandidates().addAll(list);
			resetStartDate();
//			this.nextPage();
			this.goToPage("eventEdit");
		}
		
		public void doDelete() {
			List<EventDetail> list = getLocationProvider().getSelectedObjects();
			this.getSelectedCandidates().clear();
			this.getSelectedCandidates().addAll(list);
			List<String> uuids = new ArrayList<String>();
			for(EventDetail el : getSelectedCandidates()){
				uuids.add(el.getUUID());
			}
			if(!uuids.isEmpty()){
				getEvtManager().removeEvent(uuids);
			}
			
			redirectTo(getRedirectUrl());
		}
		
		public String doExit() {
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
		
		public void doEdit() {
//			// validation
//			CommonValidator val = getValidators().get(getActivePage());
//			if(val!=null){
//				MSG msg=val.validate(this);
//				report(msg);
//				if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
//					return;
//			}
			
			// TO-BE date: getEventTiming().getStartTime()
			List<EventDetail> list = getLocationProvider().getSelectedObjects();
			Date latestStartTime = null;
			for(EventDetail detail : list) {
				if(latestStartTime==null 
						|| latestStartTime.before(detail.getEvent().getStartTime())){
					latestStartTime = detail.getEvent().getStartTime();
				}
			}
			if(getEventTiming().getStartTime()!=null&&getEventTiming().getStartTime().before(latestStartTime)){
				//actual end or estimated end time is before start time
				report(new MSG(CommonValidator.MSG_ERROR, "The end date / time should not be before the start date / time :" +latestStartTime+" ."));
				return;
			}
			this.getSelectedCandidates().clear();
			this.getSelectedCandidates().addAll(list);
			List<String> uuids = new ArrayList<String>();
			for(EventDetail el : getSelectedCandidates()){
				uuids.add(el.getUUID());
			}
		//	List<String> eventDetails, Date endTime, boolean actual
			getEvtManager().updateEndTime(uuids, getEventTiming().getStartTime(), false);
			
			redirectTo(getRedirectUrl());
		}
		
		public void doEnd() {

			// TO-BE date: getEventTiming().getStartTime()
			List<EventDetail> list = getLocationProvider().getSelectedObjects();
			Date latestStartTime = null;
			for(EventDetail detail : list) {
				if(latestStartTime==null 
						|| latestStartTime.before(detail.getEvent().getStartTime())){
					latestStartTime = detail.getEvent().getStartTime();
				}
			}
			if(getEventTiming().getStartTime()!=null&&getEventTiming().getStartTime().before(latestStartTime)){
				//actual end or estimated end time is before start time
				report(new MSG(CommonValidator.MSG_ERROR, "The end date / time should not be before the start date / time :" +latestStartTime+" ."));
				return;
			}
			this.getSelectedCandidates().clear();
			this.getSelectedCandidates().addAll(list);
			List<String> uuids = new ArrayList<String>();
			for(EventDetail el : getSelectedCandidates()){
				uuids.add(el.getUUID());
			}
		//	List<String> eventDetails, Date endTime, boolean actual
			getEvtManager().updateEndTime(uuids, getEventTiming().getStartTime(), true);
			
			redirectTo(getRedirectUrl());
		}
		
		public void doCancel() {
			getWizard().backPage();
		}
		
		private String getRedirectUrl() {
			String programName = this.getProgramName();
			
			if("SDP".equalsIgnoreCase(programName)){
				return DRWConstants.SDP_ACTIVE_PAGE;
			}
			
			if("BIP".equalsIgnoreCase(programName)){
				return DRWConstants.BIP2013_ACTIVE_PAGE;
			}
			
			if("API".equalsIgnoreCase(programName)){
				return DRWConstants.API_ACTIVE_PAGE;
			}
			return null;
		}
		
//		public String getSeletedItems() {
//			List<EventDetail> list = getLocationProvider().getSelectedObjects();
//			StringBuffer sb = new StringBuffer();
//			for(EventDetail item : list) {
//				sb.append("[");
//				sb.append(item.getLocation().getName());
//				sb.append(" start date is: ");
//				sb.append(item.getEvent().getStartTime());
//				sb.append("], ");
//			}
////	sb.substring(0, sb.length()-2);
//			return sb.toString();
//		}
		
		private void resetStartDate() {
			Date cur = new Date();
			getEventTiming().setStartDate(cur);
			getEventTiming().setStartHour(cur.getHours());
			getEventTiming().setStartMin(cur.getMinutes());
			getEventTiming().setStartSec(cur.getSeconds());
		}

}
