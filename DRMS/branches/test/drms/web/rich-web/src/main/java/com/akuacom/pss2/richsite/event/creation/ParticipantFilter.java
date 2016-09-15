package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ParticipantFilter implements Serializable {

	private static final long serialVersionUID = -3459442424223902481L;
	
	public static String[] FILTER_BY_LABEL = new String[]{
		"Participant Name",
		"Account#",
		"Register Shed"
	};
	
	private int filterBy = 0;
	private double minShed = 0;
	private double maxShed = 0;
	private String searchByValue ="";
	
	private double searchMinShed = 0;
	private double searchMaxShed = 0;
	
	
	public static ParticipantFilter getEmptyFilter(){
		ParticipantFilter filter = new ParticipantFilter();
		return filter;
	}
			
	public ParticipantFilter(){
		
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
	
	public double getMinShed() {
		return minShed;
	}

	public void setMinShed(double minshed) {
		this.minShed = minshed;
	}

	public double getMaxShed() {
		return maxShed;
	}

	public void setMaxShed(double maxShed) {
		this.maxShed = maxShed;
	}

	public double getSearchMinShed() {
		if(this.isByShed()){
			return searchMinShed;
		}
		return 0;
	}

	public void setSearchMinShed(double searchMinShed) {
		this.searchMinShed = searchMinShed;
	}
	
	public double getSearchMaxShed() {
		if(this.isByShed()){
			return searchMaxShed;
		}
		return 0;
	}
	
	public int getStep(){
		return (int)((getMaxShed()-getMinShed())/5);
	}
	
	public void setSearchMaxShed(double searchMaxShed) {
		this.searchMaxShed = searchMaxShed;
	}

	public boolean isByParticipantName(){
		return getFilterBy()==0;
	}
	
	public boolean isByAccount(){
		return getFilterBy()==1;
	}
	
	public boolean isByShed(){
		return this.getFilterBy()==2;
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
		long temp;
		temp = Double.doubleToLongBits(getSearchMaxShed());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(getSearchMinShed());
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ParticipantFilter other = (ParticipantFilter) obj;
		if (filterBy != other.filterBy)
			return false;
		if (searchByValue == null) {
			if (other.searchByValue != null)
				return false;
		} else if (!searchByValue.equals(other.searchByValue))
			return false;
		if (Double.doubleToLongBits(getSearchMaxShed()) != Double
				.doubleToLongBits(other.getSearchMaxShed()))
			return false;
		if (Double.doubleToLongBits(getSearchMinShed()) != Double
				.doubleToLongBits(other.getSearchMinShed()))
			return false;
		return true;
	}
	
	
}
