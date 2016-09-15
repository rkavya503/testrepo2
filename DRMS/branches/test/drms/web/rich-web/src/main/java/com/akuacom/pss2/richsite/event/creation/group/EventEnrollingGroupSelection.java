package com.akuacom.pss2.richsite.event.creation.group;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.query.EventEnrollingGroup;
import com.akuacom.pss2.query.EventEnrollingItem;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.SCELocationUtils;
import com.akuacom.pss2.richsite.event.creation.EventEnrollment;
import com.akuacom.pss2.richsite.event.creation.EvtCreation;

public class EventEnrollingGroupSelection  implements EventEnrollment, Serializable {

	private static final long serialVersionUID = -5512913099128365885L;
	
	private static final Logger log = Logger.getLogger(EventEnrollingGroupSelection.class.getName());
	
	/** all groups **/
	private List<EventEnrollingGroup> allCandidates;
	
	/** selected */
	private List<EventEnrollingGroup> selectedCandidates;
	
	private List<EventEnrollingGroup> rejectedCandidates;
	
	private double totalSelectedAvailabeShed = 0;
	private double totalSelectedRegisterShed = 0;
	
	private double totalRejectedAvailableShed =0;
	private double totalRejectedRegisterShed = 0;
	
	
	transient private NativeQueryManager nativeQuery;
	
	/** used by JSF to render the table **/
	private EventEnrollingGroupProvider selectedProvider;
	/** used by JSF to render the table **/
	private EventEnrollingGroupProvider rejectedProvider;
	
	
	//inverse reference
	private EvtCreation creationModel;
	
	private boolean isLocationPage = false;
	
	public EventEnrollingGroupSelection(EvtCreation model){
		this.creationModel = model;
	}
	
	public String getProgramName(){
		return creationModel.getProgramName();
	}
	
	public String getProgramClass(){
		return creationModel.getProgram().getProgramClass();
	}
	
	public List<EventEnrollingGroup> getAllCandidates() {
		if(this.allCandidates==null){
			//initialization
			try{
				doLoadAllGroups();
				for(EventEnrollingGroup group:allCandidates){
					totalSelectedAvailabeShed+=group.getAvailableShed();
					totalSelectedRegisterShed+=group.getRegisterShed();
				}
				if(!isLocationPage){
					selectedCandidates = allCandidates;
				}
			}catch(Exception e){
				log.error("Error to load event locations",e);
			}
		}
		return allCandidates;
	}
	
	public List<String> getLocations(){
		List<String> locationNumbers = new ArrayList<String>();
		for(EventEnrollingGroup group: getSelectedCandidate()){
			locationNumbers.add(group.getGroupId());
		}
		return locationNumbers;
	}
	
	protected  void doLoadAllGroups() throws SQLException{
		String programClass = this.getProgramClass();	
		allCandidates = getNativeQueryManager().getEventParticipantsByLocation(this.getProgramName(), 
				SCELocationUtils.getLocationType(programClass));
	}
	
	public List<EventEnrollingGroup> getSelectedCandidate() {
		if( selectedCandidates==null)
			 selectedCandidates= new ArrayList<EventEnrollingGroup>();
		return selectedCandidates;
	}
	
	public List<EventEnrollingGroup> getRejectedCandidate() {
		if(rejectedCandidates==null)
			rejectedCandidates =new ArrayList<EventEnrollingGroup>();
		return rejectedCandidates;
	}
	
	public void addToSelectedList(List<EventEnrollingGroup> candidate){
		getSelectedCandidate().addAll(candidate);
		for(EventEnrollingGroup p:candidate){
			totalSelectedAvailabeShed     +=  p.getAvailableShed();
    		totalSelectedRegisterShed     +=  p.getRegisterShed();
    		totalRejectedAvailableShed    -=  p.getAvailableShed();
    		totalRejectedRegisterShed     -=  p.getRegisterShed();
		}
		getRejectedCandidate().removeAll(candidate);
	}
	
	public void removeFromSelectedList(List<EventEnrollingGroup> candidate){
		getSelectedCandidate().removeAll(candidate);
		for(EventEnrollingGroup p:candidate){
			totalSelectedAvailabeShed   -=  p.getAvailableShed();
    		totalSelectedRegisterShed   -=  p.getRegisterShed();
    		totalRejectedAvailableShed  +=  p.getAvailableShed();
    		totalRejectedRegisterShed   +=  p.getRegisterShed();
		}
		getRejectedCandidate().addAll(candidate);
	}
	
	public void removeFromList(List<EventEnrollingGroup> candidate){
		
		for(EventEnrollingGroup p:candidate){
			totalSelectedAvailabeShed   -=  p.getAvailableShed();
    		totalSelectedRegisterShed   -=  p.getRegisterShed();
    		totalRejectedAvailableShed  +=  p.getAvailableShed();
    		totalRejectedRegisterShed   +=  p.getRegisterShed();
		}
		getRejectedCandidate().addAll(candidate);
		getSelectedCandidate().removeAll(candidate);
	}
	
	
	/** jsf action **/
	public void removeEnrollment(){
		List<EventEnrollingGroup> candidate = getSelectedProvider().getSelectedObjects();
		removeFromSelectedList(candidate);
		getSelectedProvider().setSelection(null);
		getRejectedProvider().setSelection(null);
	}
	public void removeEnrollmentAll(){
		
		if(this.getRejectedProvider().getAllCount() ==0 && this.getSelectedCandidate().size() ==0){
			List<EventEnrollingGroup> candidate = this.getAllCandidates();
			removeFromList(candidate);
			getSelectedProvider().setSelection(null);
			getRejectedProvider().setSelection(null);
		}
	}
	
	public void addAll(){
		
		selectedCandidates=null;
		rejectedCandidates=null;
		totalSelectedAvailabeShed     =  0;
		totalSelectedRegisterShed     =  0;
		totalRejectedAvailableShed    =  0;
		totalRejectedRegisterShed     =  0;
		getSelectedCandidate().addAll(EventEnrollingGroupSelection.this.getAllCandidates());
		for(EventEnrollingGroup p:EventEnrollingGroupSelection.this.getAllCandidates()){
			totalSelectedAvailabeShed     +=  p.getAvailableShed();
    		totalSelectedRegisterShed     +=  p.getRegisterShed();
		}
		
	}
	
	public void addEnrollment(){
		List<EventEnrollingGroup> candidate = getRejectedProvider().getSelectedObjects();
		addToSelectedList(candidate);
		getSelectedProvider().setSelection(null);
		getRejectedProvider().setSelection(null);
	}
	
	public EventEnrollingGroupProvider getSelectedProvider(){
		
		if(selectedProvider==null)
			selectedProvider = new EventEnrollingGroupProvider(){
				private static final long serialVersionUID = -8540644897027109606L;

				protected void init(){
					EventEnrollingGroupSelection.this.getAllCandidates();
				}
				
				@Override
				public List<EventEnrollingGroup> getAllGroups() {
					//init();
					return EventEnrollingGroupSelection.this.getSelectedCandidate();
				}
				
		};
		return selectedProvider;
	}
	
	public EventEnrollingGroupProvider getRejectedProvider(){
		if(rejectedProvider==null)
			rejectedProvider = new EventEnrollingGroupProvider(){
				private static final long serialVersionUID = -8540644897027109606L;

				protected void init(){
					isLocationPage = true;
				}
				
				@Override
				public List<EventEnrollingGroup> getAllGroups() {
					init();
					return EventEnrollingGroupSelection.this.getRejectedCandidate();
				}
				
		};
		return rejectedProvider;
	}
	
	public NativeQueryManager getNativeQueryManager() {
		if(nativeQuery==null){
			nativeQuery=EJBFactory.getBean(NativeQueryManager.class);
		}
		return nativeQuery;
	}

	@Override
	public List<? extends EventEnrollingItem> getEnrollmentItems() {
		getAllCandidates();
		return this.getSelectedCandidate();
	}
	
}
