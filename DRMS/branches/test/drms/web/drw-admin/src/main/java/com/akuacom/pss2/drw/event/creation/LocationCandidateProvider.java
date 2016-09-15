package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.drw.core.Location;

//TODO: when to clearTreeNodeCache
public abstract class LocationCandidateProvider extends
		AbstractTreeContentProvider<Location> implements Serializable {

	private static final long serialVersionUID = 6309925199452115446L;

	/** allow user to apply a filter on the table **/
	private LocationFilter filter;

	/** used by jsf table component **/
	private List<Location> contents;

	public LocationCandidateProvider() {

	}

//	public abstract List<Location> getAllCandidates();

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
		// all show in the table
		return getContents().size();
	}

	public int getAllCount() {
		// all available
		return this.getContents().size();
	}

	@Override
	public List<Location> getContents() {
		if (contents == null) {
			contents = new ArrayList<Location>();
		}
		return contents;
	}

	@Override
	public abstract void updateModel();// {

	@Override
	protected int doCompare(Location row1, Location row2,
			String property) {
		return super.doCompare(row1, row2, property);
	}

	public boolean isEmptyFilter() {
		if (this.filter == null)
			return true;
		if (this.filter.equals(LocationFilter.getEmptyFilter(LocationCandidateProvider.this.getDispatchByLabels())))
			return true;
		return false;
	}

//	protected boolean isMatch(Location p, LocationFilter filter) {
//		if (filter == null)
//			return true;
//		// first of all filter by the dispatch type
//		int dispatch = filter.getDispatchBy();
//		if (dispatch != 0) {// not all
//			String dispatchType = filter.getDispatchByLabels()[dispatch];
//			if (!dispatchType.equalsIgnoreCase(p.getType())) {
//				return false;
//			}
//		}
//		String filterBy = filter.getParticipantName();
//		if (filterBy != null) {
//			filterBy = filterBy.toUpperCase();
//			if (p.getParticipantName() == null)
//				return false;
//			if (p.getParticipantName().toUpperCase().startsWith(filterBy))
//				return true;
//			return false;
//		}
//
//		filterBy = filter.getAccountNumber();
//		if (filterBy != null) {
//			filterBy = filterBy.toUpperCase();
//			if (p.getAccount() == null)
//				return false;
//			if (p.getAccount().toUpperCase().startsWith(filterBy))
//				return true;
//			return false;
//		}
//		return true;
//
//	}

//	public void clearFilter() {
//		this.filter = LocationFilter.getEmptyFilter(LocationCandidateProvider.this.getDispatchByLabels());
//	}
//
//	public void applyFilter() {
//
//	}
	
	private boolean eventActionFlag = false; 
	public boolean isEventActionFlag() {
		return eventActionFlag;
	}

	public void setEventActionFlag(boolean eventActionFlag) {
		this.eventActionFlag = eventActionFlag;
	}

	private boolean clearSelection;
	
	public boolean isClearSelection() {
		return clearSelection;
	}

	public void setClearSelection(boolean clearSelection) {
		this.clearSelection = clearSelection;
	}
	public void clearFilter() {
		clearSelection = true;
		eventActionFlag = true;
		this.filter.setSearchByValue(null);
		//2 options, 1. Only clear the input text 2. clear all conditions, return a empty filter
		//this.filter.setSearchByValue(null);
		//this.filter = LocationFilter.getEmptyFilter(LocationCandidateProvider.this.getDispatchByLabels());
	}

	public void applyFilter() {
		clearSelection = true;
		eventActionFlag = true;		
	}

	public LocationFilter getFilter() {
		if (filter == null) {
			filter = new LocationFilter(){

				@Override
				public String[] getDispatchByLabels() {
					// TODO Auto-generated method stub
					return LocationCandidateProvider.this.getDispatchByLabels();
				}
				
			};
		}
		return filter;
	}
	
	public abstract String[] getDispatchByLabels();
	

}
