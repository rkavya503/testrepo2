package com.akuacom.pss2.richsite.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.ParticipantSummary;
import com.akuacom.pss2.query.ParticipantSummary.ProgramParticipation;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.UserType;

public class JSFParticipant  implements  Serializable  {

	private static final long serialVersionUID = -3945605658735411611L;
	
	public JSFParticipant() {
		
	}
	private Date autoDrProfileStartDate;
	private String bcdRepName;
	private String requestId = "1";
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getDefaultFacdashPage() {
		if (UserType.SIMPLE.toString().equals(this.getUserType())) {
			return "/facdash/jsp/simpleDashboard.jsf";
		} else {
			return "/facdash/jsp/clients.jsf";
		}
	}
	
	public List<ProgramParticipation> getProgramsParticipation() {
		if(isEnableCBPConsolidation()){
			List<ParticipantSummary.ProgramParticipation> pp = new ArrayList<ParticipantSummary.ProgramParticipation>();
			for(ParticipantSummary.ProgramParticipation instance:programsParticipation){
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
	private boolean contains(List<ParticipantSummary.ProgramParticipation> pp){
		boolean result = false;
		for(ParticipantSummary.ProgramParticipation ppInstance:pp){
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

	/**
	 * Instantiates a new jSF participant.
	 * 
	 * @param name
	 *            the name
	 */
	public JSFParticipant(String name) {
		this.name = name;
	}

	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

    public int getAggPartCount() {
        return aggPartCount;
    }

    public void setAggPartCount(int aggPartCount) {
        this.aggPartCount = aggPartCount;
    }

    public boolean isInstaller() {
        return installer;
    }

    public void setInstaller(boolean installer) {
        this.installer = installer;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    
	public void setHasNotes(boolean hasNotes) {
		this.hasNotes = hasNotes;
	}

	public boolean isHasNotes() {
		return hasNotes;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Date getAutoDrProfileStartDate() {
		return autoDrProfileStartDate;
	}

	public void setAutoDrProfileStartDate(Date autoDrProfileStartDate) {
		this.autoDrProfileStartDate = autoDrProfileStartDate;
	}

	public String getBcdRepName() {
		return bcdRepName;
	}

	public void setBcdRepName(String bcdRepName) {
		this.bcdRepName = bcdRepName;
	}
	private String accountNumber;
	private String name;
	private String type;
	private List<ParticipantSummary.ProgramParticipation> programsParticipation;
	
	private String UUID;
	private boolean deleted;
	private boolean disabled;
	private boolean selected;
    private int aggPartCount;
    private boolean installer;
    private String userType;
    private boolean hasNotes;
    private String notes;
	
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
