
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantShedEntry;

public class JSFParticipantShed implements Serializable {

	private static final long serialVersionUID = 1966469164564602658L;
	private String participantName="";
	private String participantShedType="SIMPLE";
	 /** The entries. */
	private List<ParticipantShedEntryBean> entries = new ArrayList<ParticipantShedEntryBean>();
	
	public JSFParticipantShed(){
		initialize();
	}
	private void initialize(){
		participantName = FDUtils.getParticipantName();
		if(participantName!=null&&(!participantName.equalsIgnoreCase(""))){
			ParticipantManager pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
			Participant participant = pm.getParticipantAndShedsOnly(participantName);
			participantShedType = participant.getShedType();
			if(participantShedType.equalsIgnoreCase("SIMPLE")){
				 ParticipantShedEntryBean bean = new ParticipantShedEntryBean();
			     bean.setHourIndex(0);
			     bean.setType("SIMPLE");
			     bean.setValue(String.valueOf(participant.getShedPerHourKW()));
			     entries.clear();
			     entries.add(bean);
			}else if(participantShedType.equalsIgnoreCase("ADVANCED")){
				//advanced type
				if(participant.getShedEntries()!=null){
					List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
					if(shedEntries.size()>0){
						List<ParticipantShedEntryBean> advancedEntries = new ArrayList<ParticipantShedEntryBean>();
						for(ParticipantShedEntry entry:shedEntries){
							ParticipantShedEntryBean bean = ParticipantShedEntryBean.transfer(entry);
							advancedEntries.add(bean);
						}
						entries=advancedEntries;
					}
				}	
			}
		}
		
		
	}
	/**
	 * @return the participantName
	 */
	public String getParticipantName() {
		return participantName;
	}
	/**
	 * @param participantName the participantName to set
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return the participantShedType
	 */
	public String getParticipantShedType() {
		return participantShedType;
	}
	/**
	 * @param participantShedType the participantShedType to set
	 */
	public void setParticipantShedType(String participantShedType) {
		this.participantShedType = participantShedType;
	}
	/**
	 * @return the entries
	 */
	public List<ParticipantShedEntryBean> getEntries() {
		return entries;
	}
	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<ParticipantShedEntryBean> entries) {
		this.entries = entries;
	}
	
	
	
	
	
}
