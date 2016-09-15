package com.akuacom.pss2.richsite.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantShedEntry;

public class ParticipantShedBean {
	private Participant participant;
	private List<Participant> clients = new ArrayList<Participant>();
	private List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>();
	private boolean loadInDBFlag=false;
	private double registedShed=0;
	private double availableShed=0;
	private Date startTime;
	private Date endTime;
	
	public ParticipantShedBean(Participant p,List<Participant> clients,Date startTime,Date endTime,boolean loadInDBFlag){
		this.participant=p;
		this.clients=clients;
		this.startTime=startTime;
		this.endTime=endTime;
		this.loadInDBFlag=loadInDBFlag;
		if(loadInDBFlag){
			
		}else{
			//do something;
			participant = ParticipantShedCalculateUtil.getPm().getParticipantAndShedsOnly(participant.getParticipantName());
			if(participant.getShedType().equalsIgnoreCase("SIMPLE")){
//				ParticipantShedEntry entry = new ParticipantShedEntry();
//				entry.setValue(participant.getShedPerHourKW());
//				shedEntries.clear();
//				shedEntries.add(entry);
			}else if(participant.getShedType().equalsIgnoreCase("ADVANCED")){
				shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
			}
			
			loadInDBFlag = true;
		}
		
		calculateRegistedShed();
		calculateAvailableShed();
	}
	
	public double calculateRegistedShed(){
		registedShed=0;
		if(participant==null){
			//OR THROW EXCEPTION
			return registedShed;
		}
		if(participant!=null){
			if(participant.getShedType()!=null){
				if(participant.getShedType().equalsIgnoreCase("SIMPLE")){
					registedShed = participant.getShedPerHourKW();
				}else if(participant.getShedType().equalsIgnoreCase("ADVANCED")){
					//------------------------ADVANCED SHED COMPUTING BEGIN-----------------------------
					Calendar calS=Calendar.getInstance();
					calS.setTime(startTime);
					Calendar calE=Calendar.getInstance();
					calE.setTime(endTime);
					double totalHours=0;
					double totalSheds=0;
					double hourStart = calS.get(Calendar.HOUR_OF_DAY);
					double hourEnd = calE.get(Calendar.HOUR_OF_DAY);
					double minStart = calS.get(Calendar.MINUTE);
					double minEnd = calE.get(Calendar.MINUTE);
					for(ParticipantShedEntry entry:shedEntries){
						int hourIndex = entry.getHourIndex();
						double value = entry.getValue();
						if(hourIndex>=hourStart&&hourIndex<=hourEnd){
							if(hourIndex==hourStart&&hourIndex==hourEnd){
								registedShed=value;
								return registedShed;
							}else if(hourIndex==hourStart){
								totalSheds+=(60-minStart)/60*value;
								totalHours+=(60-minStart)/60;
							}else if(hourIndex==hourEnd){
								totalSheds+=(minEnd/60)*value;
								totalHours+=minEnd/60;
							}else{
								totalSheds+=value;
								totalHours++;
							}
						}
					}
					if(totalHours!=0){
						registedShed = totalSheds/totalHours;	
					}
					java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
					registedShed = Double.parseDouble(df.format(registedShed));
					
					//------------------------ADVANCED SHED COMPUTING END-----------------------------
				}
			}else{
				//SIMPLE mode 
				registedShed = participant.getShedPerHourKW();
			}
		}
		return registedShed;
	}
	public double calculateAvailableShed(){
		int c = 0;
		for (Participant client : clients) {
			if (client.getStatus() == 0){
				c++;
			}
		}
		if (clients.size() != 0 && c !=  0) {
			availableShed = ParticipantShedCalculateUtil.calcAvailableSheds(registedShed,c,clients.size());
			return availableShed;
		}
		availableShed=0;
		return availableShed;
	}
	

	/**
	 * @return the participant
	 */
	public Participant getParticipant() {
		return participant;
	}

	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	/**
	 * @return the clients
	 */
	public List<Participant> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<Participant> clients) {
		this.clients = clients;
	}

	/**
	 * @return the shedEntries
	 */
	public List<ParticipantShedEntry> getShedEntries() {
		return shedEntries;
	}

	/**
	 * @param shedEntries the shedEntries to set
	 */
	public void setShedEntries(List<ParticipantShedEntry> shedEntries) {
		this.shedEntries = shedEntries;
	}

	/**
	 * @return the loadInDBFlag
	 */
	public boolean isLoadInDBFlag() {
		return loadInDBFlag;
	}

	/**
	 * @param loadInDBFlag the loadInDBFlag to set
	 */
	public void setLoadInDBFlag(boolean loadInDBFlag) {
		this.loadInDBFlag = loadInDBFlag;
	}

	/**
	 * @return the registedShed
	 */
	public double getRegistedShed() {
		return registedShed;
	}

	/**
	 * @param registedShed the registedShed to set
	 */
	public void setRegistedShed(double registedShed) {
		this.registedShed = registedShed;
	}

	/**
	 * @return the availableShed
	 */
	public double getAvailableShed() {
		return availableShed;
	}

	/**
	 * @param availableShed the availableShed to set
	 */
	public void setAvailableShed(double availableShed) {
		this.availableShed = availableShed;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	
	
	
}
