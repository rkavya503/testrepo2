package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.event.LocationCategory;

//TODO: when to clearTreeNodeCache
public abstract class LocationCategoryProvider extends
		AbstractTreeContentProvider<Location> implements Serializable {

	private static final long serialVersionUID = 6309925199452115446L;

	/** allow user to apply a filter on the table **/
	private LocationFilter filter;

	/** used by jsf table component **/
	private List<LocationCategory> contents;

	public LocationCategoryProvider() {

	}

	public abstract List<Location> getAllCandidates();

	@Override
	public List<Location> getChildren(Location parent) {
//		return Collections.emptyList();
		if (!hasChildren(parent)) {
			return Collections.emptyList();
		}

		 return ((LocationCategory)parent).getContents();
	}

	@Override
	public boolean hasChildren(Location parent) {
//		return false;
		// return true;
		if (!(parent instanceof LocationCategory)) {
			return false;
		}
		List<Location> children = ((LocationCategory) parent)
				.getContents();
		return (children != null && !children.isEmpty());
	}

	@Override
	public int getTotalRowCount() {
		// all show in the table
		return getContents().size();
	}

	public int getAllCount() {
		// all available
		return this.getAllCandidates().size();
	}

	@Override
	public List<LocationCategory> getContents() {
		if (contents == null) {
			contents = Collections.emptyList();
		}
		return contents;
	}

	@Override
	public void updateModel() {
		List<LocationCategory> result = new ArrayList<LocationCategory>();
		
		List<Location> slapContents = new ArrayList<Location>();
		List<Location> abankContents = new ArrayList<Location>();
		List<Location> subsContents = new ArrayList<Location>();
		// do filtering
		for (Location p : getAllCandidates()) {
			if (filter == null
					|| filter.equals(LocationFilter.getEmptyFilter(LocationCategoryProvider.this.getDispatchByLabels()))
					|| isMatch(p, filter)) {
				if (p.getType().equalsIgnoreCase("SLAP")) {
					slapContents.add(p);
				} else if (p.getType().equalsIgnoreCase("Abank")) {
					abankContents.add(p);
				} else if (p.getType().equalsIgnoreCase("Substation")) {
					subsContents.add(p);
				}
			}
		}//end of loop
		
		if(!slapContents.isEmpty()){
			LocationCategory cate = new LocationCategory("SLAP",slapContents);
			result.add(cate);
		}
		if(!abankContents.isEmpty()){
			LocationCategory cate = new LocationCategory("Abank",abankContents);
			result.add(cate);
		}
		if(!subsContents.isEmpty()){
			LocationCategory cate = new LocationCategory("Substation",subsContents);
			result.add(cate);
		}
		this.contents = result;
		
		sort(contents, getConstraint());

	}

	@Override
	protected int doCompare(Location row1, Location row2,
			String property) {
		return super.doCompare(row1, row2, property);
	}

	public boolean isEmptyFilter() {
		if (this.filter == null)
			return true;
		if (this.filter.equals(LocationFilter.getEmptyFilter(LocationCategoryProvider.this.getDispatchByLabels())))
			return true;
		return false;
	}

	protected boolean isMatch(Location p, LocationFilter filter) {
		if (filter == null)
			return true;
		// first of all filter by the dispatch type
		int dispatch = filter.getDispatchBy();
		if (dispatch != 0) {// not all
			String dispatchType = filter.getDispatchByLabels()[dispatch];
			if (!dispatchType.equalsIgnoreCase(p.getType())) {
				return false;
			}
		}
		String filterBy = filter.getParticipantName();
		if (filterBy != null) {
			filterBy = filterBy.toUpperCase();
			if (p.getName() == null)
				return false;
			if (p.getName().toUpperCase().startsWith(filterBy))
				return true;
			return false;
		}

		filterBy = filter.getAccountNumber();
		if (filterBy != null) {
			filterBy = filterBy.toUpperCase();
			if (p.getNumber() == null)
				return false;
			if (p.getNumber().toUpperCase().startsWith(filterBy))
				return true;
			return false;
		}
		return true;

	}

	public void clearFilter() {
		this.filter = LocationFilter.getEmptyFilter(LocationCategoryProvider.this.getDispatchByLabels());
	}


	public LocationFilter getFilter() {
		if (filter == null) {
			filter = new LocationFilter(){

				@Override
				public String[] getDispatchByLabels() {
					return LocationCategoryProvider.this.getDispatchByLabels();
				}};
		}
		return filter;
	}
	
	public void applyFilter() {

	}
	
	@Override
	public boolean isEagerLoad(Location  node) {
		if (node instanceof LocationCategory) {
			return true;
		}
		return false;
	}
	
	public abstract String[] getDispatchByLabels();

}
