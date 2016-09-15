/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;

/**
 * the class ParticipantProgram
 *
 */
public class SCEParticipantProgram {
	//just for sce programs
	Participant participant;
	SCEParticipantEntry entry;
	
	Set<String> programInDras=new HashSet<String>();
	Set<String> programInFile=new HashSet<String>();
	Set<String> clientPrograms=new HashSet<String>();
	
	boolean changed=false;
	boolean clientDiscrepancy=false;
	boolean isBIP=false;
	boolean isAPI=false;
	boolean isSDP=false; 

	public SCEParticipantProgram(Participant part, SCEParticipantEntry entry){
		this.participant=part;
		this.entry=entry;
		
		init();
	}
	
	public void init(){
		updateParticipant();
		checkProgramVariation();
	}
	
	private void updateParticipant(){
		this.participant.setCustomerName(entry.getCustomerName());
		this.participant.setServiceStreetAddress(entry.getServiceStreetAddress());
		this.participant.setServiceCityName(entry.getServiceCityName());
		this.participant.setZip(entry.getZip());
		this.participant.setABank(entry.getaBank());
		this.participant.setSlap(entry.getSlap());
		this.participant.setPNode(entry.getPnode());
		this.participant.setServicePlan(entry.getServicePlan());
		this.participant.setRateEffectiveDate(entry.getRateEffectiveDate());
		this.participant.setDirectAccessParticipant(entry.isDirectAccessParticipant());
		this.participant.setBcdRepName(entry.getBcdRepName());
		this.participant.setAutoDrProfileStartDate(entry.getAutoDrProfileStartDate());
		this.participant.setProgramOption(entry.getProgramOption());
		this.participant.setSubstation(entry.getSubstation());
		this.participant.setBlockNumber(entry.getBlockNumber());
	}
	
	private void checkProgramVariation(){
		ParticipantEAO participantEAO=EJBFactory.getBean(ParticipantEAO.class);

		List<String> programInDras =participantEAO.findUtilityProgramNamesByParticipant(participant.getParticipantName(), false);
		programInFile.addAll(entry.getUtilityPrograms());
		this.programInDras.addAll(programInDras);
		
		if (!this.programInDras.equals(this.programInFile)) {
			this.changed=true;
		} else if(this.participant.isClient()) {
			ProgramParticipantEAO ppEAO=EJBFactory.getBean(ProgramParticipantEAO.class);
			this.clientPrograms.addAll(ppEAO.findClientProgram(this.participant.getParticipantName()));
			if (!this.programInDras.equals(this.clientPrograms))
				this.clientDiscrepancy=true;
		}
		for(String program:programInDras){
			if(program.contains("BIP")||"Base Interruptible Program".equalsIgnoreCase(program)){
				this.setBIP(true);
			}
			if(program.contains("API")){
				this.setAPI(true);
			}
			if(program.contains("SDP")){
				this.setSDP(true);
			}
		}
	}
	
	public Participant getParticipant() {
		return participant;
	}
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public Set<String> getProgramInDras() {
		return programInDras;
	}
	public void setProgramInDras(Set<String> programInDras) {
		this.programInDras = programInDras;
	}
	public Set<String> getProgramInFile() {
		return programInFile;
	}
	public void setProgramInFile(Set<String> programInFile) {
		this.programInFile = programInFile;
	}
	
	public Set<String> getClientPrograms() {
		return clientPrograms;
	}

	public void setClientPrograms(Set<String> clientPrograms) {
		this.clientPrograms = clientPrograms;
	}

	public boolean isClientDiscrepancy() {
		return clientDiscrepancy;
	}

	public void setClientDiscrepancy(boolean clientDiscrepancy) {
		this.clientDiscrepancy = clientDiscrepancy;
	}

	public boolean isBIP() {
		return isBIP;
	}

	public void setBIP(boolean isBIP) {
		this.isBIP = isBIP;
	}

	public boolean isAPI() {
		return isAPI;
	}

	public void setAPI(boolean isAPI) {
		this.isAPI = isAPI;
	}

	public boolean isSDP() {
		return isSDP;
	}

	public void setSDP(boolean isSDP) {
		this.isSDP = isSDP;
	}

}
