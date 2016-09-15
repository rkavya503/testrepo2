package com.akuacom.pss2.query;

import java.util.ArrayList;
import java.util.List;

public class EventEnrollingGroup implements EventEnrollingItem {
	
	private static final long serialVersionUID = -4557495241866460999L;
	
	private String groupName;
	private String groupId;
	private String type;
	
	private double registerShed;
	private double availableShed;
	
	private List<EvtParticipantCandidate> children= new ArrayList<EvtParticipantCandidate>();
	private List<EvtParticipantCandidate> legibleParticipants = null;
	
	public List<EvtParticipantCandidate> getEventParticipants() {
		return children;
	}
	
	public void setEventParticipants(List<EvtParticipantCandidate> evtParts){
		this.children=evtParts;
	}
	
	public List<EvtParticipantCandidate> getLegibleEventParticipants(){
		filter();
		return this.legibleParticipants;
	}
	
	public int getParticipantCount(){
		return this.getEventParticipants().size();
	}
	
    protected synchronized void filter(){
    	if(legibleParticipants==null) {
    		legibleParticipants = new ArrayList<EvtParticipantCandidate>();
    		registerShed =0;availableShed=0;
			for(EvtParticipantCandidate candidate:children){
	        	if(candidate.isLegiable()){
	        		legibleParticipants.add(candidate);
	        		availableShed  += candidate.getAvailableShed();
	        	}
	        	registerShed   += candidate.getRegisterShed();
			}
	    }
    }
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public double getRegisterShed() {
		filter();
		return this.registerShed;
	}
	
	@Override
	public double getAvailableShed() {
		filter();
		return this.availableShed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		EventEnrollingGroup other = (EventEnrollingGroup) obj;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}
	
	
}
