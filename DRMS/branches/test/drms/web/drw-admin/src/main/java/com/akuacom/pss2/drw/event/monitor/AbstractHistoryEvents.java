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
import com.akuacom.pss2.drw.admin.FDUtils;
import com.akuacom.pss2.drw.admin.ServiceLocator;
import com.akuacom.pss2.drw.admin.constants.DRWConstants;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.event.creation.CommonValidator;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;
import com.akuacom.pss2.drw.event.creation.EventLocationSelection;
import com.akuacom.pss2.drw.event.creation.EvtLocationCandidateProvider;
import com.akuacom.pss2.drw.event.creation.Wizard;
import com.akuacom.pss2.drw.event.creation.WizardComponent;
import com.akuacom.utils.lang.DateUtil;

public class AbstractHistoryEvents extends WizardComponent implements Serializable{
	
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
	public String getProgramPage() {
		return DRWConstants.BASE_PROGRAM_PAGE;
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
	
	//****JSF Actions
	public AbstractHistoryEvents(){
		super();
	}
	
	private EventLocationSelection locationSelection;
	
	public EventLocationSelection getLocationSelection() {
		if(locationSelection==null){
			locationSelection = new EventLocationSelection(){

				private static final long serialVersionUID = 5704943180581837040L;

				@Override
				public String[] getDispatchByLabels() {
					return AbstractHistoryEvents.this.getDispatchByLabels();
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
	
	public TreeContentProvider<EventDetail> getSelectedParticipants(){
		return new LazyListContentProvider<EventDetail>(){
			private static final long serialVersionUID = -1859578720476173504L;

			@Override
			public List<EventDetail> doGetContents() {
				return getSelectedCandidates();
			}
		};
	}
	
	private EvtLocationCandidateProvider locationProvider;
	
	public EvtLocationCandidateProvider getLocationProvider(){
		if(locationProvider==null){
			locationProvider = new EvtLocationCandidateProvider(){
				private static final long serialVersionUID = -7061374719402582381L;
				
				protected void init(){
					AbstractHistoryEvents.this.getAllCandidates();
				}
				
				@Override
				public List<EventDetail> getAllCandidates() {
					init();
					return AbstractHistoryEvents.this.getAllCandidates();
				}

				@Override
				public String[] getDispatchByLabels() {
					// TODO Auto-generated method stub
					return AbstractHistoryEvents.this.getDispatchByLabels();
				}

			};
		}
		return locationProvider;
	}
	
	public List<EventDetail> doGetContents() {
		List<EventDetail> lists = getEvtManager().getHistoryEvents(programName, "start".equals(getFilterBy()), getStartTime(), getEndTime(), getRates(), locationNo, locationName,getDispatchTypes());
		//List<EventDetail> lists = getEvtManager().getHistoryEvents(programName, "start".equals(getFilterBy()), getStartTime(), getEndTime(), getRates(), locationNo, locationName);
				//getEvtManager().getHistoryEventByStartLimited(getProgramName(), getEndTime(), LIMIT_SIZE);
		return lists;
	}
	public List<EventDetail> getAllCandidates() {
		if(this.allCandidates==null){
			//initialization
			try{
				allCandidates =  null;//this.getNativeQueryManager().getLocationCandidate(getProgramName());
				//SLap ABank Substation
//				List<EventDetail> list = doGetContents();
				
				allCandidates = new ArrayList<EventDetail>();
				//by default all program participant is selected 
			}catch(Exception e){
				allCandidates=Collections.emptyList();
			}
//			filterParticipants(allCandidates);
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
	private Date endTime;
	private Date startTime;

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		if(endTime == null){
			endTime = new Date();
		}
		return endTime;
	}
	
	public Date getStartTime() {
		if(startTime == null){
			startTime = new Date();
		}
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	
	//***JSF Actions
	public void applyFilter(){
		if(startTime == null){
			//actual end or estimated end time is before start time
			report(new MSG(CommonValidator.MSG_ERROR, "From date is required."));
			return;
		}
		if(endTime == null){
			//actual end or estimated end time is before start time
			report(new MSG(CommonValidator.MSG_ERROR, "To date is required."));
			return;
		}
		
		getAllCandidates().clear();
		getAllCandidates().addAll(doGetContents());
	}
	
	
	public void doDelete() {
		List<EventDetail> list = getLocationProvider().getSelectedObjects();
		this.getSelectedCandidates().clear();
		this.getSelectedCandidates().addAll(list);
		List<String> lists = new ArrayList<String>();
		for(EventDetail el : getSelectedCandidates()){
			lists.add(el.getUUID());
		}
		if(!lists.isEmpty()){
			getEvtManager().removeEvent(lists);
		}
		//after delete , init this page
		getLocationProvider().setSelection(null);
		applyFilter();
	}
// add list	
//	public boolean isDeleteDisable() {
//		return (getLocationProvider().getSelectedObjects()==null||getLocationProvider().getSelectedObjects().isEmpty());
//	}

	@Override
	protected void installValidators() {
//		
	}

	public String[] getDispatchByLabels() {
		// don't need implement
		return null;
	}

	private String filterBy;
	public String getFilterBy() {
		if(filterBy==null||"".equals(filterBy)) filterBy = "start";
		return filterBy;
	}

	public void setFilterBy(String filterBy) {
		this.filterBy = filterBy;
	}
	
	private String dispatchType;
	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}
	
	private String locationName;
	private String locationNo;
	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationNo() {
		return locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	
	private String rates;
	public String getRates() {
		return rates;
	}

	public void setRates(String rates) {
		this.rates = rates;
	}
	
	public void exportExcel() throws Exception {
		String name = "history_events";
		String body = getExportContent();
		String filename = name + ".csv";

		FDUtils.export(filename, body);
	}
	
	protected String getExportContent() throws Exception{
		List<EventDetail> contents =  doGetContents();
 		StringBuffer exportContent = new StringBuffer();
 		
 		// append title for SDP event
 		exportContent.append("Issue Date & Time,Start Date&Time,End Date&Time,Dispatch Type,Dispatch Location#,Dispatch Location,Rate,Comments"+"\n\t");
 		//append the first row 
 		for(EventDetail event : contents){
			exportContent.append(" "+DateUtil.format(event.getEvent().getIssuedTime(),"yyyy-MM-dd HH:mm:ss")+ ","
					+ " "+DateUtil.format(event.getEvent().getStartTime(),"yyyy-MM-dd HH:mm:ss") + ","
					+ " "+DateUtil.format(event.getActualEndTime(),"yyyy-MM-dd HH:mm:ss") + ","
//					+ (event.getLocation()!=null?event.getLocation().getType():"SLAP") + ","
					+ (event.getLocation()!=null?event.getLocation().getType():event.getAllLocationType()) + ","
					+ " "+ (event.getLocation()!=null?event.getLocation().getNumber():"-") + ","
					+ (event.getLocation()!=null?event.getLocation().getName():"ALL") + ","
					+ event.getEvent().getProduct() + ","
					+ FDUtils.filterSpecialCharacters(event.getEvent().getComment()));
			exportContent.append("\n\t");
 		}
	 
		exportContent.append("\n\t");
	  
		return exportContent.toString();
	}
	

	private String dispatchTypeFilter;
	public static String[] API_ALL_DISPATCH_TYPE = new String[]{
		"ABank",
		"District",
		"SLAP",
		"Substation"
	};
	/**
	 * @return the dispatchTypeFilter
	 */
	public String getDispatchTypeFilter() {
		return dispatchTypeFilter;
	}
	/**
	 * @param dispatchTypeFilter the dispatchTypeFilter to set
	 */
	public void setDispatchTypeFilter(String dispatchTypeFilter) {
		this.dispatchTypeFilter = dispatchTypeFilter;
	}
	public List<String> getDispatchTypes() {
		List<String> products = new ArrayList<String>();
		if(dispatchTypeFilter==null||dispatchTypeFilter.equalsIgnoreCase("")||dispatchTypeFilter.equalsIgnoreCase("ALL")){
//			products.add("ABank");
//			products.add("District");
//			products.add("SLAP");
//			products.add("Substation");
		}else{
			products.add(dispatchTypeFilter);
		}
		
		return products;
	}
}
