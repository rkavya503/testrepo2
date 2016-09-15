package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public abstract class LocationFilter implements Serializable {

	private static final long serialVersionUID = -3459442424223902481L;
	
	public static String[] FILTER_BY_LABEL = new String[]{
		"Name",
		"Number"
	};
	public abstract String[] getDispatchByLabels();

	
	private int filterBy = 0;
	private String searchByValue ="";
//	public static String[] DISPATCH_LABEL = new String[]{
//		"All",
//		"SLap",
//		"ABank",
//		"Substation"
//	};
	private int dispatchBy = 0;
	public int getDispatchBy() {
		return dispatchBy;
	}

	public void setDispatchBy(int dispatchBy) {
		this.dispatchBy = dispatchBy;
	}
	
	public List<SelectItem> getDispatchByList() {
		List<SelectItem> items =new ArrayList<SelectItem>(getDispatchByLabels().length);
	    for (int i = 0; i < getDispatchByLabels().length; i++) {
	    	   items.add(new SelectItem(i,getDispatchByLabels()[i]));
	    }
	    return items;
	}
	//TODO: verify this 
	public static LocationFilter getEmptyFilter(final String[] dispatchLabels){
		LocationFilter filter = new LocationFilter(){

			@Override
			public String[] getDispatchByLabels() {
				// TODO Auto-generated method stub
				return dispatchLabels;
			}};
		return filter;
	}
			
	public LocationFilter(){
		
	}

	public String getParticipantName() {
		if(this.isByParticipantName()){
			return this.getSearchByValue();
		}
		return null;
	}
	
	public String getAccountNumber() {
		if(this.isByAccount()){
			return this.getSearchByValue();
		}
		return null;
	}
	

	public boolean isByParticipantName(){
		return getFilterBy()==0;
	}
	
	public boolean isByAccount(){
		return getFilterBy()==1;
	}
	
	
	public int getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(int filterBy) {
		int old =this.filterBy;
		this.filterBy = filterBy;
		if(old!=filterBy){
			this.searchByValue="";
		}
	}
	
	public String getSearchByValue() {
		 if(searchByValue==null)
			 searchByValue="";
		return searchByValue;
	}

	public void setSearchByValue(String searchValue) {
		this.searchByValue = searchValue;
	}
	
	public String getSearchByLabel() {
		return FILTER_BY_LABEL[filterBy];
	}

	public List<SelectItem> getFilterByList() {
		List<SelectItem> items =new ArrayList<SelectItem>(FILTER_BY_LABEL.length);
	    for (int i = 0; i < FILTER_BY_LABEL.length; i++) {
	    	   items.add(new SelectItem(i,FILTER_BY_LABEL[i]));
	    }
	    return items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + filterBy;
		result = prime * result
				+ ((searchByValue == null) ? 0 : searchByValue.hashCode());
		result = prime * result + dispatchBy;
		//TODO: need add dispatch type
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationFilter other = (LocationFilter) obj;
		if (filterBy != other.filterBy)
			return false;
		if (dispatchBy != other.dispatchBy)
			return false;
		if (searchByValue == null) {
			if (other.searchByValue != null)
				return false;
		} else if (!searchByValue.equals(other.searchByValue))
			return false;
		//TODO: need add dispatch type
		return true;
	}
	
	
}
