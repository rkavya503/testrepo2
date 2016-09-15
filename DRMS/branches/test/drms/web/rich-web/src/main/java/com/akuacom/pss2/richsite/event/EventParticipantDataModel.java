package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.richsite.util.ParticipantShedBean;
import com.akuacom.pss2.richsite.util.ParticipantShedCalculateUtil;
import com.akuacom.pss2.util.EventState;

public class EventParticipantDataModel implements Serializable {

	private static final long serialVersionUID = -5278993232074157519L;
	
	private static final String MANUAL_POSTFIX = "(MAN)";
	
	public EventParticipantDataModel(){
		super();
		select = true;
		delete =false;
	}
	public EventParticipantDataModel(Participant participant, Boolean hasClients){
		super();
		this.participant=participant;
		if(hasClients){
			select = true;
			delete =false;
		}else{
			select = false;
			delete =true;
		}
		this.nonselectable = !hasClients;
	}
	private Participant participant;
	private Event event;
	private List<Participant> clientJoinParticipants;
	private EventParticipant eventParticipant;
	private boolean select;
	private boolean delete;
	private Boolean nonselectable;
    private double participantAvilableShed;
    private ClientManager cm = null;
	private double[] bids;
	private String bidStatus;
	private boolean bidSelected;
	private double participantRegistedShed;
	private boolean shedEnabled=true;
	private ParticipantShedBean shedBean;
	public Boolean getNonselectable() {
		return nonselectable;
	}
	public void setNonselectable(Boolean nonselectable) {
		this.nonselectable = nonselectable;
	}
	
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public EventParticipant getEventParticipant() {
		return eventParticipant;
	}
	public void setEventParticipant(EventParticipant eventParticipant) {
		this.eventParticipant = eventParticipant;
	}
	public boolean isSelect() {
		return select;
	}
	/**
	 * Setter function for select attribute
	 * 
	 * Business Logic: when the nonselectable attribute is true, the object can not be select.
	 * @param select
	 */
	public void setSelect(boolean select) {
		if(!this.getNonselectable()){
			this.select = select;
		}		
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public List<Participant> getClientJoinParticipants() {
		if(clientJoinParticipants==null){
			clientJoinParticipants = new ArrayList<Participant>();
		}
		return clientJoinParticipants;
	}
	public void setClientJoinParticipants(List<Participant> clientJoinParticipants) {
		this.clientJoinParticipants = clientJoinParticipants;
	}

    public double getParticipantAvilableShed() {
    	if(!shedEnabled){
    		participantAvilableShed=0;
		}else{
			if(shedBean!=null){
				participantAvilableShed = shedBean.getAvailableShed();
			}else{	
			}
		}
        return participantAvilableShed;
    }

    public void setParticipantAvilableShed(double participantAvilableShed) {
        this.participantAvilableShed = participantAvilableShed;
    }


   @Override
   public boolean equals(Object obj) {
    
       final EventParticipantDataModel other = (EventParticipantDataModel) obj;
       if (this.participant.getUUID().equalsIgnoreCase(other.getParticipant().getUUID()) ){
           return true;
       }
       else return false;
     
       //return true;
   }

   public String getOnlineStatus() {
	   if (this.getParticipant().getStatus() != null) {
		   return this.getParticipant().getClientStatus().toString();
	   } else {
		   return ClientStatus.OFFLINE.toString();
	   }
   }
   
   public Date getLastCommTime() {
		   return this.getParticipant().getCommTime();
	}

   public String getClientType() {
	   if (this.getParticipant().getType() == 2) {
		   return "MANUAL";
	   } else {
		   return "AUTO";
	   }
   }
   
   private ClientManager getClientManager() {
	   if (cm == null) {
		   cm = (ClientManager) EJBFactory.getBean(ClientManager.class);
	   }
	   
	   return cm;
   }
   
   public String getClientStatus() {
	   if(!this.getParticipant().isClient())
	   {
		   return "";
	   }
	   List<EventState> clientEventStates = 
		   getClientManager().getClientEventStates(this.participant.getParticipantName(), false);
       //EventState.loadEventStatus(clientEventStates.get(0).getEventStatus());
	   
	   String res = clientEventStates.get(0).getOperationModeValue().toString();
	   if (clientEventStates.get(0).isManualControl()) {
			return res + " (MAN)";

		} else {
			return res;
		}
   }
   
   public String getManualSignalState() {
		if (this.participant.isManualControl()) {
			return this.participant.getOperationMode().toString() + " " + MANUAL_POSTFIX;

		} else {
			return this.participant.getOperationMode().toString();
		}

	}
	public double[] getBids()
	{
		return this.bids;
	}
	
	public void setBids(double[] bids)
	{
		this.bids = bids;
	}
	
	public String getBidStatus()
	{
		return bidStatus;
	}
	
	public void setBidStatus(String bidStatus)
	{
		this.bidStatus = bidStatus;
	}
	public boolean isBidSelected()
	{
		return bidSelected;
	}
	public void setBidSelected(boolean bidSelected)
	{
		this.bidSelected = bidSelected;
	}
	/**
	 * @return the participantRegistedShed
	 */
	public double getParticipantRegistedShed() {
		if(!shedEnabled){
			participantRegistedShed=0;
		}else{
			if(shedBean!=null){
				participantRegistedShed = shedBean.getRegistedShed();
			}else{
				participantRegistedShed = ParticipantShedCalculateUtil.calculateEstimatedShed(participant, event);	
			}
		}
		
		return participantRegistedShed;
	}
	/**
	 * @param participantRegistedShed the participantRegistedShed to set
	 */
	public void setParticipantRegistedShed(double participantRegistedShed) {
		this.participantRegistedShed = participantRegistedShed;
	}
	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}
	/**
	 * @param event the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
	}
	/**
	 * @return the shedEnabled
	 */
	public boolean isShedEnabled() {
		return shedEnabled;
	}
	/**
	 * @param shedEnabled the shedEnabled to set
	 */
	public void setShedEnabled(boolean shedEnabled) {
		this.shedEnabled = shedEnabled;
	}
	/**
	 * @return the shedBean
	 */
	public ParticipantShedBean getShedBean() {
		return shedBean;
	}
	/**
	 * @param shedBean the shedBean to set
	 */
	public void setShedBean(ParticipantShedBean shedBean) {
		this.shedBean = shedBean;
	}


}
