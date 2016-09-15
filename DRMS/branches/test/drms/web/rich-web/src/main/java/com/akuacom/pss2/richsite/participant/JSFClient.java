package com.akuacom.pss2.richsite.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.ClientSummary;
import com.akuacom.pss2.query.ParticipantSummary;
import com.akuacom.pss2.query.ClientSummary.ProgramParticipation;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.CBPUtil;

import org.apache.commons.lang.StringUtils;

public class JSFClient implements Serializable {
	
	private static final long serialVersionUID = 5806192788227890899L;
	private static final String MANUAL_POSTFIX = "(MAN)";
	
	private static final String STATUS_ONLINE_COLOR = "GREEN";
	private static final String STATUS_OFF_COLOR = "RED";
	private static final String STATUS_MANUAL_COLOR = "BLACK";
	
	private String name;
	private List<ProgramParticipation> programsParticipation=Collections.emptyList();
	private String currentEventStatus;
	private String lastEventStatus;
	private String currentMode = "NORMAL";
	private String lastMode;
	
	private String type;
	private Date lastContact;
	private String status;
	private String participantName;
	private String UUID;
	private boolean deleted;
	private boolean disabled;
	private boolean manualControl;
	private boolean selected;
    private String userType;
    private String comm_status_color;
    private String event_status_color;
    private String deviceType;
	private boolean leadAccount;
	private String participantType;
	private String ABank;
	private String leadAccountString;
    private List<String> emailAddress =Collections.emptyList();
	
    
	public String getEventStatus() {
		if(this.getType().equalsIgnoreCase("MANUAL")){
			// show current (server) status for manual clients only.
			return StringUtils.replace(getCurrentEventStatus(), "UNKNOWN", "?");
		}
		String eventStatus=getLastEventStatus();
		if (isManualControl()) {
			eventStatus=eventStatus+MANUAL_POSTFIX;
		} else if (!getLastEventStatus().equals(getCurrentEventStatus())) {
			eventStatus=getLastEventStatus() + " [ "+getCurrentEventStatus()+" ]";
		}
		return eventStatus;
	}
	
	public String getMode() {
		if(this.getType().equalsIgnoreCase("MANUAL")){
			//show current (server) mode for manual clients only.
			return StringUtils.replace(getCurrentMode(), "UNKNOWN", "?");
		}
		String mode=getLastMode();
		if (isManualControl()) {
			mode+=MANUAL_POSTFIX;
		} else if (!getLastMode().equals(getCurrentMode())) {
			mode=getLastMode() + " [ "+getCurrentMode()+" ]";
		}
		mode = mode.replaceAll("UNKNOWN", "?");
		return mode;
	}
	
	public String getProgramNames(){
		String programs ="";
		for(ProgramParticipation pp:programsParticipation){
			programs+=" "+pp.getProgramName();
		}
		if(!programs.equals("")){
			programs.substring(1, programs.length());
		}
		return programs;
	}
	public String getProgramNamesWithConsolidation(){
		List<String> programList = new ArrayList<String>();
		for(ProgramParticipation pp:programsParticipation){
			String key = pp.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	if(!containsCBP(programList)){
            		key="CBP";
            		programList.add(key);
            	}
            }else{
            	programList.add(key);
            }
		}
		Collections.sort(programList);
		String result ="";
		for(String p: programList){
			result=result.concat(" "+p);
		}
		if(result.equals(""))
			return "";
		else
			return result.substring(1);
	}
	private boolean containsCBP(List<String> pp){
		boolean result = false;
		for(String ppInstance:pp){
			if(ppInstance.equalsIgnoreCase("CBP")){
				result = true;
				return result;
			}
		}
		return result;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProgramParticipation> getProgramsParticipation() {
		if(isEnableCBPConsolidation()){
			List<ClientSummary.ProgramParticipation> pp = new ArrayList<ClientSummary.ProgramParticipation>();
			for(ClientSummary.ProgramParticipation instance:programsParticipation){
				String key = instance.getProgramName();
				if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
	            	if(!contains(pp)){
	            		instance.setProgramName("CBP");
	            		pp.add(instance);
	            	}
	            }else{
	            	pp.add(instance);
	            }
			}
			programsParticipation = pp;
		}
		return programsParticipation;
	}
	private boolean contains(List<ClientSummary.ProgramParticipation> pp){
		boolean result = false;
		for(ClientSummary.ProgramParticipation ppInstance:pp){
			if(ppInstance.getProgramName().equalsIgnoreCase("CBP")){
				result = true;
				return result;
			}
		}
		return result;
	}
	public void setProgramsParticipation(List<ProgramParticipation> programsParticipation) {
		this.programsParticipation = programsParticipation;
	}
	public String getCurrentEventStatus() {
		return currentEventStatus;
	}
	public void setCurrentEventStatus(String currentEventStatus) {
		this.currentEventStatus = currentEventStatus;
	}
	public String getLastEventStatus() {
		return lastEventStatus;
	}
	public void setLastEventStatus(String lastEventStatus) {
		this.lastEventStatus = lastEventStatus;
	}
	
	public String getCurrentMode() {
		return currentMode;
	}
	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}
	public String getLastMode() {
		return lastMode;
	}
	public void setLastMode(String lastMode) {
		this.lastMode = lastMode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getLastContact() {
		return lastContact;
	}
	public void setLastContact(Date lastContact) {
		this.lastContact = lastContact;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isManualControl() {
		return manualControl;
	}
	public void setManualControl(boolean manualControl) {
		this.manualControl = manualControl;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	


	public List<String> getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(List<String> emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the leadAccount
	 */
	public boolean isLeadAccount() {
		return leadAccount;
	}

	/**
	 * @param leadAccount the leadAccount to set
	 */
	public void setLeadAccount(boolean leadAccount) {
		this.leadAccount = leadAccount;
	}

	/**
	 * @return the participantType
	 */
	public String getParticipantType() {
		return participantType;
	}

	/**
	 * @param participantType the participantType to set
	 */
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	/**
	 * @return the aBank
	 */
	public String getABank() {
		return ABank;
	}

	/**
	 * @param aBank the aBank to set
	 */
	public void setABank(String aBank) {
		ABank = aBank;
	}

	/**
	 * @return the leadAccountString
	 */
	public String getLeadAccountString() {
		if(leadAccount){
			leadAccountString="Y";
		}else{
			leadAccountString="N";
		}
		return leadAccountString;
	}

	/**
	 * @param leadAccountString the leadAccountString to set
	 */
	public void setLeadAccountString(String leadAccountString) {
		this.leadAccountString = leadAccountString;
	}

	/**
	 * @return the comm_status_color
	 */
	public String getComm_status_color() {
		if (this.getStatus().equalsIgnoreCase("ONLINE")){
        	
			comm_status_color = STATUS_ONLINE_COLOR;	
        }   
        else if(this.getType().equalsIgnoreCase("MANUAL"))
        	comm_status_color = STATUS_MANUAL_COLOR;
        else
        	comm_status_color = STATUS_OFF_COLOR;
      return comm_status_color;
	}

	/**
	 * @param comm_status_color the comm_status_color to set
	 */
	public void setComm_status_color(String comm_status_color) {
		this.comm_status_color = comm_status_color;
	}

	/**
	 * @return the event_status_color
	 */
	public String getEvent_status_color() {
		if (this.getStatus().equalsIgnoreCase("ONLINE")){
        	//DRMS-7895
        	if(("OPT OUT").equalsIgnoreCase(getCurrentEventStatus())){
        		event_status_color = STATUS_OFF_COLOR;
        	}else{
        		event_status_color = STATUS_ONLINE_COLOR;	
        	}
        }   
        else if(this.getType().equalsIgnoreCase("MANUAL"))
        	event_status_color = STATUS_MANUAL_COLOR;
        else
        	event_status_color = STATUS_OFF_COLOR;
      return event_status_color;
	}

	/**
	 * @param event_status_color the event_status_color to set
	 */
	public void setEvent_status_color(String event_status_color) {
		this.event_status_color = event_status_color;
	}
	private SystemManager systemManager;
    protected SystemManager getSystemManager(){
		if(this.systemManager==null){
			systemManager = EJBFactory.getBean(SystemManager.class);
		}
		return systemManager;
	}
    private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		return CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
	}
}
