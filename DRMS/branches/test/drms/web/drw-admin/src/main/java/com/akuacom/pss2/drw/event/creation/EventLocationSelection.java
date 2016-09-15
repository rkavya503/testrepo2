package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.event.LocationCategory;

public abstract class EventLocationSelection implements Serializable {
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
		List<Location> candidate = getRejectedProvider().getSelectedObjects();
		
		addParticipantToSelectedList(filterCategory(candidate));
	}
	
	public void addParticipantToSelectedList(List<Location> candidate){
		getSelectedCandidate().addAll(candidate);
//		getRejectedCandidate().removeAll(candidate);
	}
	/** jsf action  **/
	public void removeParticipants(){
		eventPerformanceActionFlag = true;
		List<Location> candidate = getSelectedProvider().getSelectedObjects();
		removeParticipantFromSelectedList(filterCategory(candidate));
	}

	private List<Location> filterCategory(
			List<Location> candidate) {
		List<Location> filterResult = new ArrayList<Location>();
		for(Location candi : candidate){
			if(!(candi instanceof LocationCategory)){
				//don't need to add or remove category
				filterResult.add(candi);
			}
		}
		return filterResult;
	}
	
	public void removeParticipantFromSelectedList(List<Location> candidate){
		getSelectedCandidate().removeAll(candidate);
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
		
		public EventLocationSelection(){
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
						return EventLocationSelection.this.getDispatchByLabels();
					}
					

			};
			
			return selectedProvider;
		}
		
		public LocationCandidateProvider getRejectedProvider(){
			if(rejectedProvider==null){
				rejectedProvider = new LocationCandidateProvider(){
					private static final long serialVersionUID = -7061374719402582381L;
					
//					@Override
//					public List<Location> getAllCandidates() {
//						init();
//						return getRejectedCandidate();
//					}

					@Override
					public String[] getDispatchByLabels() {
						// TODO Auto-generated method stub
						return EventLocationSelection.this.getDispatchByLabels();
					}

					@Override
					public void updateModel() {

						clearTreeNodeCache(null);
						
						if(this.getFilter()!=null && !this.getFilter().equals(LocationFilter.getEmptyFilter(EventLocationSelection.this.getDispatchByLabels()))){
							
							// ******************************Mocked filter handler*************************************
							int dispatch = this.getFilter().getDispatchBy();
							if (dispatch != 0) {// not all
								String dispatchType = this.getFilter().getDispatchByLabels()[dispatch];
							}
							String filterBy = this.getFilter().getParticipantName();
							if (filterBy != null) {
								filterBy = filterBy.toUpperCase();
							}

							filterBy = this.getFilter().getAccountNumber();
							if (filterBy != null) {
								filterBy = filterBy.toUpperCase();
							}
							//******************************Mocked filter handler*************************************
							//do filtering 
							List<Location> results = new ArrayList<Location>();
							for(Location p:EventLocationSelection.this.getAllCandidates()){
//								if(isMatch(p,this.getFilter())){
//									results.add(p);
//								}
							}
							this.getContents().clear();
							this.getContents().addAll(results);
						}else{
							this.getContents().clear();
							this.getContents().addAll(EventLocationSelection.this.getAllCandidates());
						}
					
						
					}

				};
			}
			return rejectedProvider;
		}
		
		//return rejected ones
		private List<Location> getAllCandidates() {
			//initialization
				List<Location> allCandidates =  null;//this.getNativeQueryManager().getLocationCandidate(getProgramName());
				//SLap ABank Substation
//				List<Location> list = initList();
				
//				allCandidates = list;
				//by default all program participant is selected 
			allCandidates = filterParticipants(allCandidates);
			return allCandidates;
		}
		
		private List<Location> filterParticipants(List<Location> candidates){
			List<Location> selected = getSelectedCandidate(); 
			List<Location> rejected = new ArrayList<Location>(); 
	        for(Location candidate:candidates){
	        	if(selected ==null||!selected.contains(candidate)){
	        		rejected.add(candidate);
	        	}
	        }
	        return rejected;
		}

//		private List<Location> initList() {
//			List<Location> list = new ArrayList<Location>();
//			
//			Location l1 = new Location();
//			l1.setAccount("1");
//			l1.setParticipantName("Slap1");
//			l1.setType("Slap");
//			
//			Location l2 = new Location();
//			l2.setAccount("2");
//			l2.setParticipantName("Slap2");
//			l2.setType("Slap");
//			
//			Location a1 = new Location();
//			a1.setAccount("3");
//			a1.setParticipantName("Abank1");
//			a1.setType("Abank");
//			
//			Location a2 = new Location();
//			a2.setAccount("4");
//			a2.setParticipantName("Abank2");
//			a2.setType("Abank");
//			
//			Location s1 = new Location();
//			s1.setAccount("5");
//			s1.setParticipantName("Substation1");
//			s1.setType("Substation");
//			
//			Location s2 = new Location();
//			s2.setAccount("6");
//			s2.setParticipantName("Substation2");
//			s2.setType("Substation");
//			
//			list.add(l1);
//			list.add(l2);
//			list.add(a1);
//			list.add(a2);
//			list.add(s1);
//			list.add(s2);
//			return list;
//		}
		
		public abstract String[] getDispatchByLabels();//EventLocationSelection

}
