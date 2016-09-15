package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.richfaces.component.UIColumn;

import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.DRLocationManager;
import com.akuacom.pss2.drw.admin.ServiceLocator;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.event.LocationCategory;

public abstract class LocationSelection implements Serializable {
	private static final Logger log = Logger.getLogger(LocationSelection.class);
	private boolean clearSelection;
	
	public boolean isClearSelection() {
		return clearSelection;
	}

	public void setClearSelection(boolean clearSelection) {
		this.clearSelection = clearSelection;
	}


	private boolean slapAll;
	
	public boolean isSlapAll() {
		return slapAll;
	}

	public void setSlapAll(boolean slapAll) {
		this.slapAll = slapAll;
	}
	
	
	public void locationValueChangeListener(){
		setClearSelection(true);
		eventPerformanceActionFlag = true;
		this.getRejectedProvider().applyFilter();
	}

	public void slapValueChanged(){
		eventPerformanceActionFlag = true;
		slapAll = true;
		//clear selected, add all item
		List<Location> locations = getSelectedCandidate();
		Location allSlap = new Location();
		allSlap.setName("All"); 
		allSlap.setType("SLAP");
		allSlap.setNumber("");
		allSlap.setID(-1);
		
		locations.clear();
		locations.add(allSlap);
		//...
	}
	
	public void addAll(){
		if(getSeletedLocationSize()>0) {
			return;// do nothing when some locations were selected.
		}
		slapValueChanged(); // add all location
	}
	
	public int getSeletedLocationSize() {
		return getSelectedCandidate().size();
	}
	
	
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
	
	/** jsf action  **/
	public void applyFilter(){
		eventPerformanceActionFlag = true;
	}
	public void clearFilter(){
		eventPerformanceActionFlag = true;
		this.getSelectedProvider().clearFilter();
	}
	/** event performance action flag*/
	private boolean eventPerformanceActionFlag = false; // don't clear when tree expand
	/** jsf action  **/
	public void addParticipants(){
		eventPerformanceActionFlag = true;
		setClearSelection(true);
		List<Location> candidate = getRejectedProvider().getSelectedObjects();
		
		addParticipantToSelectedList(filterCategory(candidate));
		
		//clear filter after enroll operation
		clearRejectedFilter();
		
	}
	
	private void clearRejectedFilter() {
		int dispatchBy = this.getRejectedProvider().getFilter().getDispatchBy();
		//clear filter after [Enroll Selected]
		this.getRejectedProvider().clearFilter();
		//restore dispatch location type
		this.getRejectedProvider().getFilter().setDispatchBy(dispatchBy);
	}
	
	public void addParticipantToSelectedList(List<Location> candidate){
		List<Location> selected = getSelectedCandidate();
		List<Location> temp = new ArrayList<Location>();
		for(Location candi : candidate){
			if(!selected.contains(candi)){
				temp.add(candi);
			}
		}
		getSelectedCandidate().addAll(temp);
//		getRejectedCandidate().removeAll(candidate);
	}
	/** jsf action  **/
	public void removeParticipants(){
		eventPerformanceActionFlag = true;
		List<Location> candidate = getSelectedProvider().getSelectedObjects();
		removeParticipantFromSelectedList(filterCategory(candidate));
		getSelectedProvider().setSelection(null);
	}

	private List<Location> filterCategory(
			List<Location> candidate) {
		List<Location> filterResult = new ArrayList<Location>();
		for(Location candi : candidate){
			if(!(candi instanceof LocationCategory)){
				//don't need to add or remove category
				filterResult.add(candi);
				if(candi.getID()==-1){
					slapAll = false;// reset slapAll value when all entry is going to be removed
				}
			}
		}
		return filterResult;
	}
	
	public void removeParticipantFromSelectedList(List<Location> candidate){
		getSelectedCandidate().removeAll(candidate);
		getSelectedProvider().setSelection(null);
//		getRejectedCandidate().addAll(candidate);
	}
	public List<Location> getSelectedParticipant(){
		return getSelectedProvider().getAllCandidates();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//inverse reference
		private EvtCreation creationModel;
		
		public LocationSelection(EvtCreation model){
			this.creationModel = model;
		}
		
		public String getProgramName(){
			return creationModel.getProgramName();
		}
		
		/** selected participant to participant this event **/
		private List<Location> selectedCandidates;
//		private List<Location> rejectedCandidates;
		public List<Location> getSelectedCandidate() {
			if( selectedCandidates==null)
				 selectedCandidates= new ArrayList<Location>();
			return selectedCandidates;
		}
		
//		public List<Location> getRejectedCandidate() {
//			if(rejectedCandidates==null)
//				rejectedCandidates =new ArrayList<Location>();
//			return rejectedCandidates;
//		}
		
		/** used by JSF to render the table **/
		private LocationCategoryProvider selectedProvider;
		/** used by JSF to render the table **/
		private LocationCandidateProvider rejectedProvider;
		
		public LocationCategoryProvider getSelectedProvider(){
			if(selectedProvider==null)
				selectedProvider = new LocationCategoryProvider(){
					private static final long serialVersionUID = -8540644897027109606L;
					
					@Override
					public List<Location> getAllCandidates() {
						return getSelectedCandidate();
					}
					
					@Override
					public void updateModel() {
						if(eventPerformanceActionFlag){
							clearTreeNodeCache(null);
							eventPerformanceActionFlag = false;
						}
						
						super.updateModel();

					}

					@Override
					public String[] getDispatchByLabels() {
						return LocationSelection.this.getDispatchByLabels();
					}

					@Override
					public String getCellStyleClass(Location row,
							UIColumn column) {
						if(row instanceof LocationCategory  || row.getID()<0){
							if("account".equals(column.getId())){
								return "account";
							}else if("name".equals(column.getId())){
								return "name";
							}
						}
						return null;
					}
					
					@Override
					public String getRowStyleClass(Location row) {
						if(row instanceof LocationCategory){
							return "row-category";
						}
						if(row.getID()<0){
							return "all-event";
						}
						return null;
					}
					
					@Override
					public int getColSpan(Location row, UIColumn column) {
						/*if(row instanceof LocationCategory  || row.getID()<0){
							if("account".equals(column.getId())){
								return 0;
							}else if("name".equals(column.getId())){
								return 2;
							}
							return 1;
						}*/
						return super.getColSpan(row, column);
					}
					
			};
			return selectedProvider;
		}
		
		public LocationCandidateProvider getRejectedProvider(){
			if(rejectedProvider==null){
				rejectedProvider = new LocationCandidateProvider(){
					private static final long serialVersionUID = -7061374719402582381L;
					
					@Override
					public String[] getDispatchByLabels() {
						return LocationSelection.this.getDispatchByLabels();
					}
					

					@Override
					public void updateModel() {
						clearTreeNodeCache(null);
						// init filter
						// clear content
						this.getContents().clear();
						// clear select in 3 cases:
						if(isClearSelection()||LocationSelection.this.isClearSelection()){
							this.setSelection(null);
							setClearSelection(false);
							LocationSelection.this.setClearSelection(false);
						}
						
//						if(!this.isEventActionFlag()) {
//							this.getFilter().setDispatchBy(0);
//							this.getFilter().setFilterBy(0);
//							this.getFilter().setSearchByValue("");
//							return;//do nothing 
//						}
						
						// handle applyfilter or clearFilter action
						if(this.isEventActionFlag()){
							this.setEventActionFlag(false);
						}
						
						//retrieve from db and filter the selected ones
						if(this.getFilter()!=null){
							List<String> types = null;
							String number = null;
							String name = null;
							
							// ******************************Mocked filter handler*************************************
							int dispatch = this.getFilter().getDispatchBy();
							String dispatchType = this.getFilter().getDispatchByLabels()[dispatch];
							types = Arrays.asList(dispatchType);
				
							String filterBy = this.getFilter().getParticipantName();
							if (filterBy != null) {
								name = filterBy;
							}

							filterBy = this.getFilter().getAccountNumber();
							if (filterBy != null) {
								number = filterBy;
							}
							//******************************Mocked filter handler*************************************
							//do filtering 
						
							this.getContents().addAll(LocationSelection.this.getAllCandidates(types, number, name));
						}else{
							this.getContents().addAll(LocationSelection.this.getAllCandidates(Arrays.asList(getDispatchByLabels()), null, null));
						}
						sort(this.getContents(), getConstraint());
						// init filter
//						this.getFilter().setDispatchBy(0);
//						this.getFilter().setFilterBy(0);
//						this.getFilter().setSearchByValue("");
					}

				};
			}
			return rejectedProvider;
		}
		
				private List<Location> getAllCandidates(List<String> type, String number, String name) {
					Date start = new Date();
					log.debug("getAllCandidates() start at: "+start);
					List<Location> allCandidates =  getLocationManager().getLocation(type, number, name);
					allCandidates = filterParticipants(allCandidates);
					Date end = new Date();
					log.debug("getAllCandidates() end at: "+end +"cost :" +(end.getTime()-start.getTime()));
					return allCandidates;
				}
		
				
				private List<Location> filterParticipants(List<Location> candidates){
					List<Location> selected = getSelectedCandidate(); 
					List<Location> rejected = new ArrayList<Location>(); 
			        for(Location candidate:candidates){
		        		if(slapAll){
		        			continue;
		        		}
		        		
			        	if(selected==null||!selected.contains(candidate)){
			        		rejected.add(candidate);
			        	}
		        		
			        }
			        return rejected;
				}
		
		public abstract String[] getDispatchByLabels();

}
