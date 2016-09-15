package com.akuacom.pss2.richsite.event.creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.query.EventEnrollingItem;
import com.akuacom.pss2.query.EvtParticipantCandidate;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.richsite.util.ParticipantShedCalculateUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

public class EventParticipantSelection  implements EventEnrollment, Serializable {

	private static final long serialVersionUID = -5512913099128365885L;
	
	private static final Logger log = Logger.getLogger(EventParticipantSelection.class.getName());
	
	/** all program participant **/
	private List<EvtParticipantCandidate> allCandidates;
	
	/** selected participant to participant this event **/
	private List<EvtParticipantCandidate> selectedCandidates;
	
	private List<EvtParticipantCandidate> rejectedCandidates;
	
	private List<EvtParticipantCandidate> ineligibleCandidates;
	
	private double totalSelectedAvailabeShed = 0;
	private double totalSelectedRegisterShed = 0;
	
	private double totalRejectedAvailableShed =0;
	private double totalRejectedRegisterShed = 0;
	
	private double maxRegisterShed;
	
	transient private NativeQueryManager nativeQuery;
	
	/** used by JSF to render the table **/
	private ParticipantCandidateProvider selectedProvider;
	/** used by JSF to render the table **/
	private ParticipantCandidateProvider rejectedProvider;
	
	private ParticipantCandidateProvider ineligibleProvider;
	
	//inverse reference
	private EvtCreation creationModel;
	
	public EventParticipantSelection(EvtCreation model){
		this.creationModel = model;
		retrieveShedEnabled();
	}
	
	public String getProgramName(){
		return creationModel.getProgramName();
	}
	
	public List<EvtParticipantCandidate> getSelectedParticipant(){
		return getSelectedProvider().getAllCandidates();
	}
	
	public List<EvtParticipantCandidate> getAllCandidates() {
		if(this.allCandidates==null){
			//initialization
			try{
				if (creationModel instanceof EventCreationModel && ((EventCreationModel)creationModel).getUploadFile().isAvailable()) {
					allCandidates = this.getNativeQueryManager().getUploadEvtParticipantCandidate(getProgramName(), 
							((EventCreationModel)creationModel).getUploadFile().getParser().getValidAccounts());
				} else {
						allCandidates = this.getNativeQueryManager().getEvtParticipantCandidate(getProgramName());
						Event event = creationModel.getEvent();
						for(EvtParticipantCandidate candidate:allCandidates){
							if(!shedEnabled){
								candidate.setRegisterShed(0);
							}else{
								double registedShed =ParticipantShedCalculateUtil.calculateEstimatedShed(candidate, event);
//								double registedShed = ParticipantShedCalculateUtil.calculateEstimatedShed(candidate.getParticipantName(), event);
								candidate.setRegisterShed(registedShed);
							}
							
						}
						//by default all program participant is selected 
				}
			}catch(Exception e){
				log.error("Failed to retrieve program participants ",e);
				allCandidates=Collections.emptyList();
			}
			filterParticipants(allCandidates);
		}
		return allCandidates;
	}
	

	@Override
	public List<? extends EventEnrollingItem> getEnrollmentItems() {
		getAllCandidates();
		return this.getSelectedCandidate();
	}
	
	public void setAllCandidates(List<EvtParticipantCandidate> allCandidates) {
		this.allCandidates = allCandidates;
	}

	protected void filterParticipants(List<EvtParticipantCandidate> candidates){
		totalSelectedAvailabeShed =0;totalSelectedRegisterShed=0;maxRegisterShed= 0;
		totalRejectedAvailableShed = 0; totalRejectedRegisterShed = 0;
		List<EvtParticipantCandidate> selected = getSelectedCandidate(); 
		List<EvtParticipantCandidate> ineligible = getIneligibleCandidate();
		selected.clear();
		ineligible.clear();
		getRejectedCandidate().clear();
        for(EvtParticipantCandidate candidate:candidates){
        	if(candidate.getRegisterShed()>maxRegisterShed) maxRegisterShed = candidate.getRegisterShed();
        	if(candidate.isLegiable()){
        		selected.add(candidate);
        		totalSelectedAvailabeShed  += candidate.getAvailableShed();
        		totalSelectedRegisterShed += candidate.getRegisterShed();
        	}else{
        		ineligible.add(candidate);
        		//rejected.add(candidate);
        		//totalRejectedAvailableShed += candidate.getAvailableShed();
        		//totalRejectedRegisterShed  += candidate.getRegisterShed();
        	}
        }
	}
	
	public List<EvtParticipantCandidate> getSelectedCandidate() {
		if( selectedCandidates==null)
			 selectedCandidates= new ArrayList<EvtParticipantCandidate>();
		return selectedCandidates;
	}
	
	public List<EvtParticipantCandidate> getRejectedCandidate() {
		if(rejectedCandidates==null)
			rejectedCandidates =new ArrayList<EvtParticipantCandidate>();
		return rejectedCandidates;
	}
	
	public List<EvtParticipantCandidate> getIneligibleCandidate(){
		if(ineligibleCandidates==null)
			ineligibleCandidates = new ArrayList<EvtParticipantCandidate>();
		return ineligibleCandidates;
	}
	
	public void addParticipantToSelectedList(List<EvtParticipantCandidate> candidate){
		getSelectedCandidate().addAll(candidate);
		for(EvtParticipantCandidate p:candidate){
			totalSelectedAvailabeShed     +=  p.getAvailableShed();
    		totalSelectedRegisterShed     +=  p.getRegisterShed();
    		totalRejectedAvailableShed    -=  p.getAvailableShed();
    		totalRejectedRegisterShed     -=  p.getRegisterShed();
		}
		getRejectedCandidate().removeAll(candidate);
	}
	
	public void removeParticipantFromSelectedList(List<EvtParticipantCandidate> candidate){
		getSelectedCandidate().removeAll(candidate);
		for(EvtParticipantCandidate p:candidate){
			totalSelectedAvailabeShed   -=  p.getAvailableShed();
    		totalSelectedRegisterShed   -=  p.getRegisterShed();
    		totalRejectedAvailableShed  +=  p.getAvailableShed();
    		totalRejectedRegisterShed   +=  p.getRegisterShed();
		}
		getRejectedCandidate().addAll(candidate);
	}

	/** jsf action  **/
	public void addParticipants(){
		List<EvtParticipantCandidate> candidate = getRejectedProvider().getSelectedObjects();
		addParticipantToSelectedList(candidate);
		getSelectedProvider().setSelection(null);
		getRejectedProvider().setSelection(null);
	}
	
	/** jsf action **/
	public void removeParticipants(){
		List<EvtParticipantCandidate> candidate = getSelectedProvider().getSelectedObjects();
		removeParticipantFromSelectedList(candidate);
		getSelectedProvider().setSelection(null);
		getRejectedProvider().setSelection(null);
	}
	
	
	
	public ParticipantCandidateProvider getSelectedProvider(){
		if(selectedProvider==null)
			selectedProvider = new ParticipantCandidateProvider(){
				private static final long serialVersionUID = -8540644897027109606L;

				protected void init(){
					EventParticipantSelection.this.getAllCandidates();
				}
				
				@Override
				public List<EvtParticipantCandidate> getAllCandidates() {
					init();
					return getSelectedCandidate();
				}

				@Override
				public double getInitAvailableShed() {
					init();
					return totalSelectedAvailabeShed;
				}

				@Override
				public double getInitTotalRegisterShed() {
					init();
					return totalSelectedRegisterShed;
				}

				@Override
				public double getMaxShed() {
					init();
					return maxRegisterShed;
				}

				@Override
				public double getMinShed() {
					init();
					return 0;
				}
		};
		
		return selectedProvider;
	}
	
	
	public ParticipantCandidateProvider getRejectedProvider(){
		if(rejectedProvider==null){
			rejectedProvider = new ParticipantCandidateProvider(){
				private static final long serialVersionUID = -7061374719402582381L;
				
				protected void init(){
					EventParticipantSelection.this.getAllCandidates();
				}
				
				@Override
				public List<EvtParticipantCandidate> getAllCandidates() {
					init();
					return getRejectedCandidate();
				}
				@Override
				public double getInitAvailableShed() {
					init();
					return totalRejectedAvailableShed;
				}
				@Override
				public double getInitTotalRegisterShed() {
					init();
					return totalRejectedRegisterShed;
				}
				@Override
				public double getMaxShed() {
					init();
					return maxRegisterShed;
				}
				@Override
				public double getMinShed() {
					init();
					return 0;
				}
			};
		}
		return rejectedProvider;
	}
	
	
	public ParticipantCandidateProvider getIneligibleProvider(){
		if(ineligibleProvider==null){
			ineligibleProvider = new ParticipantCandidateProvider(){
				private static final long serialVersionUID = -7061374719402582381L;
				
				protected void init(){
					EventParticipantSelection.this.getAllCandidates();
				}
				
				@Override
				public List<EvtParticipantCandidate> getAllCandidates() {
					init();
					return getIneligibleCandidate();
				}
				@Override
				public double getInitAvailableShed() {
					init();
					return -1;
				}
				@Override
				public double getInitTotalRegisterShed() {
					init();
					return -1;
				}
				@Override
				public double getMaxShed() {
					init();
					return -1;
				}
				@Override
				public double getMinShed() {
					init();
					return -1;
				}
			};
		}
		return ineligibleProvider;
	}
	
	
	public NativeQueryManager getNativeQueryManager() {
		if(nativeQuery==null){
			nativeQuery=EJBFactory.getBean(NativeQueryManager.class);
		}
		return nativeQuery;
	}
	
	private boolean shedEnabled=true;
	private void retrieveShedEnabled(){
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        PSS2Features features = systemManager.getPss2Features();
        shedEnabled = features.isFeatureShedInfoEnabled();
	}
	/**
	 * Function for support event time changing event
	 */
	public void recalculateShed(){
		if(this.allCandidates!=null){
			Event event = creationModel.getEvent();
			for(EvtParticipantCandidate candidate:allCandidates){
				if(!shedEnabled){
					candidate.setRegisterShed(0);
				}else{
					double registedShed =ParticipantShedCalculateUtil.calculateEstimatedShed(candidate, event);
//					double registedShed = ParticipantShedCalculateUtil.calculateEstimatedShed(candidate.getParticipantName(), event);
					candidate.setRegisterShed(registedShed);
				}
				
			}
			totalSelectedAvailabeShed =0;
			totalSelectedRegisterShed=0;
			totalRejectedAvailableShed = 0; 
			totalRejectedRegisterShed = 0;			
			
			for(EvtParticipantCandidate p:getSelectedCandidate()){
				totalSelectedAvailabeShed     +=  p.getAvailableShed();
	    		totalSelectedRegisterShed     +=  p.getRegisterShed();
			}
			for(EvtParticipantCandidate p:getRejectedCandidate()){
				totalRejectedAvailableShed    +=  p.getAvailableShed();
				totalRejectedRegisterShed     +=  p.getRegisterShed();
			}

			
			
//			filterParticipants(allCandidates);
		}
	}


}
