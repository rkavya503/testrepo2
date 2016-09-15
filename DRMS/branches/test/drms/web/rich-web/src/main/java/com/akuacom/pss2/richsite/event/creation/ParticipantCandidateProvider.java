package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.pss2.query.EvtParticipantCandidate;

public abstract  class ParticipantCandidateProvider
				 extends AbstractTreeContentProvider<EvtParticipantCandidate> 
				 implements Serializable {
	
	private static final long serialVersionUID = 6309925199452115446L;
	
	/** allow user to apply a filter on the table**/
	private ParticipantFilter filter;
	
	
	/** used by jsf table component **/
	private List<EvtParticipantCandidate> contents;
	
	private double totalRegisterShed = -1;
	private double totalAvailableShed = -1;
	
	public ParticipantCandidateProvider(){

	}
	
	public abstract List<EvtParticipantCandidate> getAllCandidates();
	
	public abstract double getInitAvailableShed();
	
	public abstract double getInitTotalRegisterShed();
	
	public abstract double getMaxShed();
	
	public abstract double getMinShed();
	
	@Override
	public List<EvtParticipantCandidate> getChildren(
			EvtParticipantCandidate parent) {
		return Collections.emptyList();
	}
	
	@Override
	public boolean hasChildren(EvtParticipantCandidate parent) {
		return false;
	}

	@Override
	public int getTotalRowCount() {
		//all show in the table
		return getContents().size();
	}
	
	public int getAllCount(){
		//all available 
		return this.getAllCandidates().size();
	}
	
	@Override
	public List<EvtParticipantCandidate> getContents() {
		if(contents==null)
			contents = Collections.emptyList();
		return contents;
	}
	
	
	@Override
	public void updateModel() {
		//TODO
		clearTreeNodeCache(null);
		//Do sorting
		if(filter!=null && !filter.equals(ParticipantFilter.getEmptyFilter())){
			//do filtering 
			filter.setMaxShed(this.getMaxShed());
			filter.setMinShed(this.getMinShed());
			totalAvailableShed = 0; totalRegisterShed = 0;
			List<EvtParticipantCandidate> results = new ArrayList<EvtParticipantCandidate>();
			for(EvtParticipantCandidate p:getAllCandidates()){
				if(isMatch(p,filter)){
					results.add(p);
					this.totalAvailableShed += p.getAvailableShed();
					this.totalRegisterShed  += p.getRegisterShed();
				}
			}
			contents = results;
		}else{
			contents = getAllCandidates();
			if(filter!=null){
				filter.setMaxShed(getMaxShed());
				filter.setMinShed(getMinShed());
			}
			this.totalAvailableShed = this.getInitAvailableShed();
			this.totalRegisterShed  = this.getInitTotalRegisterShed();
		}
		
		sort(contents, getConstraint());
	}
	
	@Override
	protected int doCompare(EvtParticipantCandidate row1,
			EvtParticipantCandidate row2, String property) {
		if(property.equals("comments")){
			return getComments(row1).compareTo(getComments(row2));
		}
		else return super.doCompare(row1, row2, property);
	}
	
	public boolean isEmptyFilter(){
		if(this.filter==null) return true;
		if(this.filter.equals(ParticipantFilter.getEmptyFilter())) return true;
		return false;
	}
	
	protected boolean isMatch(EvtParticipantCandidate p,ParticipantFilter filter){
		if(filter==null) return true;
		String filterBy = filter.getParticipantName();
		if(filterBy!=null){
			filterBy = filterBy.toUpperCase();
			if(p.getParticipantName()==null) return false;
			if(p.getParticipantName().toUpperCase().startsWith(filterBy))
				return true;
			return false;
		}
		
		filterBy = filter.getAccountNumber();
		if(filterBy!=null){
			filterBy = filterBy.toUpperCase();
			if(p.getAccount()==null) return false;
			if(p.getAccount().toUpperCase().startsWith(filterBy))
				return true;
			return false;
		}
		
		double min = filter.getSearchMinShed();
		double max = filter.getSearchMaxShed();
		if(max>=min){
			if(p.getRegisterShed()>=min && p.getRegisterShed()<=max)
				return true;
			return false;
		}else
			return false;
	}
	
	public void clearFilter(){
		setSelection(null);
		this.filter = ParticipantFilter.getEmptyFilter();
	}
	
	public void applyFilter(){
		setSelection(null);
	}
	
	public ParticipantFilter getFilter() {
		if(filter==null){
			filter = new ParticipantFilter();
		}
		return filter;
	}
	
	protected String REJECTED_REASONS[] = new String[]{
		"program opt out", //0
		"participant opt out",//1
		"program opt out by aggregation",//2
		"participant opt out by aggregation",//3
		"no clients",//4
		"no clients in program"//5
	};
	
	public String getComments(EvtParticipantCandidate p){
		if(p.isProgramOptOut()) return REJECTED_REASONS[0];
		if(p.isParticipantOptOut()) return REJECTED_REASONS[1];
		//if(p.isAncestorProgramOptOut()) return REJECTED_REASONS[2];
		//if(p.isAncestorOptOut()) return REJECTED_REASONS[3];
		if(p.getClientCount()==0 || p.getClients().size()==0) 
				return REJECTED_REASONS[5];
		return "";
	}
	
	public String getRejectedReason(){
		EvtParticipantCandidate p = this.getCurrent();
		return getComments(p);
	}

	public double getTotalRegisterShed() {
		return totalRegisterShed;
	}

	public double getTotalAvailableShed() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(2);
		if(Double.isNaN(totalAvailableShed)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(totalAvailableShed));
	}
	
}


			