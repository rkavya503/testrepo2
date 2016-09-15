package com.akuacom.pss2.richsite.event.creation.group;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.query.EventEnrollingGroup;

public abstract class EventEnrollingGroupProvider extends AbstractTreeContentProvider<EventEnrollingGroup> 
	implements Serializable {
	private static final long serialVersionUID = -2085541963082201615L;

	/** used by jsf table component **/
	private List<EventEnrollingGroup> contents;
	
	private double totalRegisterShed = -1;
	private double totalAvailableShed = -1;
	
	
	public abstract List<EventEnrollingGroup> getAllGroups();
	
	@Override
	public List<EventEnrollingGroup> getChildren(EventEnrollingGroup parent) {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren(EventEnrollingGroup parent) {
		return false;
	}
	
	@Override
	public int getTotalRowCount() {
		//all show in the table
		return getContents().size();
	}
	
	public int getAllCount(){
		//all available 
		return this.getAllGroups().size();
	}

	@Override
	public List<? extends EventEnrollingGroup> getContents() {
		if(contents==null)
			contents = Collections.emptyList();
		return contents;
	}
	
	@Override
	public void updateModel() {
		clearTreeNodeCache(null);
		contents = getAllGroups();
		this.totalAvailableShed =0;
		this.totalRegisterShed  =0;
		
		for(EventEnrollingGroup group:contents){
			totalAvailableShed+=group.getAvailableShed();
			totalRegisterShed+=group.getRegisterShed();
		}
		sort(contents, getConstraint());
	}
	
	public double getTotalRegisterShed() {
		return totalRegisterShed;
	}

	public void setTotalRegisterShed(double totalRegisterShed) {
		this.totalRegisterShed = totalRegisterShed;
	}

	public double getTotalAvailableShed() {
		return totalAvailableShed;
	}

	public void setTotalAvailableShed(double totalAvailableShed) {
		this.totalAvailableShed = totalAvailableShed;
	}
	
}
