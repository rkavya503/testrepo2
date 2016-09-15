package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;

public class ProgramHelper implements Serializable {
	
	private List<Participant> parentList;
	private List<Participant> childList;
	private Map<String, List<Participant>> parentChildMap;
	private Set<EventParticipant> eventParticipants;
	
	
	public ProgramHelper(Program eventProgramWithParticipants){
		
		parentList = new ArrayList<Participant>();
		childList = new ArrayList<Participant>();
		parentChildMap = new HashMap<String, List<Participant>>();
		eventParticipants = new HashSet<EventParticipant>();


        Set<ProgramParticipant> ppp = eventProgramWithParticipants.getProgramParticipants();
		for(ProgramParticipant pp: ppp){
            Participant p = pp.getParticipant();
           if (pp.getOptStatus() == 0){
    
    		EventParticipant eventParticipant = new EventParticipant();
			eventParticipant.setParticipant(p);
			eventParticipants.add(eventParticipant);

			if(p.isClient()){
				childList.add(p);
				String parent = p.getParent();
				if(parent != null && !"".equals(parent)){
					if(parentChildMap.containsKey(parent)){
						parentChildMap.get(parent).add(p);
					}else{
						List<Participant> newChildList = new ArrayList<Participant>();
						newChildList.add(p);
						parentChildMap.put(parent, newChildList);
					}
				}
				
			}else{
				parentList.add(p);
				String parent = p.getParticipantName();
				if(parent != null && !"".equals(parent)){
					if(!parentChildMap.containsKey(parent)){
						List<Participant> newChildList = new ArrayList<Participant>();
						parentChildMap.put(parent, newChildList);

					}
				}
			 } 
          }
		}	
	}
	public Set<EventParticipant> getEventParticipants(){
		return eventParticipants;
	}
	
	public List<Participant> getParentParticipants(){
		return parentList;
	}
	
	public List<Participant>  getChildren(String participantName){
		return parentChildMap.get(participantName);
	}
	
	

}
