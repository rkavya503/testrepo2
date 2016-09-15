package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.drw.core.EventDetail;

//TODO: when to clearTreeNodeCache
public abstract class EvtLocationCandidateProvider extends
		AbstractTreeContentProvider<EventDetail> implements Serializable {

	private static final long serialVersionUID = 6309925199452115446L;

	/** allow user to apply a filter on the table **/
	private LocationFilter filter;

	/** used by jsf table component **/
	private List<EventDetail> contents;

	public EvtLocationCandidateProvider() {

	}

	public abstract List<EventDetail> getAllCandidates();

	@Override
	public List<EventDetail> getChildren(EventDetail parent) {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren(EventDetail parent) {
		return false;
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
	public List<EventDetail> getContents() {
		if (contents == null) {
			contents = new ArrayList<EventDetail>();
		}
		return contents;
	}

	@Override
	public void updateModel() {
		clearTreeNodeCache(null);
		
		contents = getAllCandidates();
		
		sort(contents, getConstraint());

	}

	@Override
	protected int doCompare(EventDetail row1, EventDetail row2,
			String property) {
		return super.doCompare(row1, row2, property);
	}

	public boolean isEmptyFilter() {
		if (this.filter == null)
			return true;
		if (this.filter.equals(LocationFilter.getEmptyFilter(EvtLocationCandidateProvider.this.getDispatchByLabels())))
			return true;
		return false;
	}

	public void clearFilter() {
		this.filter = LocationFilter.getEmptyFilter(EvtLocationCandidateProvider.this.getDispatchByLabels());
	}

	public void applyFilter() {

	}


	public LocationFilter getFilter() {
		if (filter == null) {
			filter = new LocationFilter(){
				@Override
				public String[] getDispatchByLabels() {
					// TODO Auto-generated method stub
					return null;
				}
				
			};
		}
		return filter;
	}
	
	public abstract String[] getDispatchByLabels();

}
