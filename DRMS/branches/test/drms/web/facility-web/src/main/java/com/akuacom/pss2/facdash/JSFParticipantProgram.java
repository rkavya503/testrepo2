/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFParticipantProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.event.Event;


/**
 * The Class JSFParticipantProgram.
 */
public class JSFParticipantProgram implements Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private ProgramParticipant programParticipant;
	List<JSFParticipantProgram> relativedClientParticipant = new ArrayList<JSFParticipantProgram>();
	/** The bid. */
	private boolean bid;
	
	/** The clients string. */
	private String clientsString;
	
	/** The events string. */
	private String eventsString;

    private String optStatus;
    
    private boolean demandLimitingSettingsActive;
    
    private boolean dayOfAdjustment; 
    
    private boolean enableCalOptOut=true;
    
    private Date optOutUntil=new Date();
    
    private boolean optedOutFlag=false;
    
    
    
   

	/**
	 * @return the optedOutFlag
	 */
	public boolean isOptedOutFlag() {
		return optedOutFlag;
	}

	/**
	 * @param optedOutFlag the optedOutFlag to set
	 */
	public void setOptedOutFlag(boolean optedOutFlag) {
		this.optedOutFlag = optedOutFlag;
	}

	/**
	 * @return the optOutUntil
	 */
	public Date getOptOutUntil() {
		return optOutUntil;
	}

	/**
	 * @param optOutUntil the optOutUntil to set
	 */
	public void setOptOutUntil(Date optOutUntil) {
		this.optOutUntil = optOutUntil;
	}

	/**
	 * @return the enableCalOptOut
	 */
	public boolean isEnableCalOptOut() {
		return enableCalOptOut;
	}

	/**
	 * @param enableCalOptOut the enableCalOptOut to set
	 */
	public void setEnableCalOptOut(boolean enableCalOptOut) {
		this.enableCalOptOut = enableCalOptOut;
	}
    

	/**
	 * Instantiates a new jSF participant program.
	 */
	public JSFParticipantProgram()
	{
	}
	
	/**
	 * Instantiates a new jSF participant program.
	 * 
	 * @param programParticipant the programParticipant
	 */
	public JSFParticipantProgram(ProgramParticipant programParticipant)
	{
		this.programParticipant = programParticipant;		
		
        ClientManager cm = EJBFactory.getBean(ClientManager.class);
        //ParticipantManager participantManager = EJBFactory.getBean(ParticipantManager.class);

        StringBuilder clientsSB = new StringBuilder();

        for(Participant client: cm.getClients(FDUtils.getParticipantName()))
        {
        	for(ProgramParticipant programClient: client.getProgramParticipants())
        	{
	        	if(programParticipant.getProgramName().equals(programClient.getProgramName())
	        			&& programClient.getState().intValue() == ProgramParticipant.PROGRAM_PART_ACTIVE)
	        	{
					clientsSB.append(client.getParticipantName());
					clientsSB.append(",");
	        	}
        	}
        }
        if(clientsSB.length() == 0)
        {
        	clientsString = "";
        }
        else
        {
        	clientsString =  clientsSB.substring(0, clientsSB.length()-1);
        }
        
		StringBuilder eventsSB = new StringBuilder();
        for(EventParticipant eventP: programParticipant.getParticipant().getEventParticipants()) {
            int optOut = eventP.getEventOptOut();
            if( optOut > 0 ) {
                continue;
            }

            Event event = eventP.getEvent();
            if (event.getProgramName().equals(programParticipant.getProgramName())
                    && (event.getEventStatus() != com.akuacom.pss2.util.EventStatus.RECEIVED ||
                    event.getProgramName().equals(TestProgram.PROGRAM_NAME)))

            {
                eventsSB.append(eventP.getEvent().getEventName());

                eventsSB.append(",");
            }
        }
        if(eventsSB.length() == 0)
        {
        	eventsString = "";
        }
        else
        {
        	eventsString =  eventsSB.substring(0, eventsSB.length()-1);
        }
        
		// determine if the bid action should be available
		bid = false;
 		Program program = programParticipant.getProgram();
		if(program instanceof DBPProgram)
		{
			DBPProgram dbpProgram = (DBPProgram)program;
			if(dbpProgram.getBidConfig().isDrasBidding())
			{
				bid = true;
			}
		}
		
		if (program instanceof DemandLimitingProgram) {
			this.setDemandLimitingSettingsActive(true);
		}

	}
	
	/**
	 * Cancel bids action.
	 * 
	 * @return the string
	 */
	public String cancelBidsAction()
	{
		return "cancelBids";
	}

	/**
	 * Edits the contstraints.
	 */
	public void editContstraints()
	{
        ProgramParticipantManager programParticipantManager = getProgramParticipantManager();
        ProgramParticipant pgrmPart =
        	programParticipantManager.getClientProgramParticipants(
        	programParticipant.getProgramName(), FDUtils.getParticipantName(), false);
        if(pgrmPart.getConstraint() != null)
        {
        	FDUtils.setJSFConstraints(new JSFConstraints(pgrmPart.getConstraint()));
        }
        else
        {
        	FDUtils.setJSFConstraints(new JSFConstraints());
        }
	}

	private ProgramParticipantManager getProgramParticipantManager() {
		ProgramParticipantManager programParticipantManager = EJBFactory.getBean(ProgramParticipantManager.class);
		return programParticipantManager;
	}
	
   /**
    * Update constraints action.
    * 
    * @return the string
    */
   public String updateConstraintsAction()
	{
		FDUtils.getJSFConstraints().parse();
		ProgramParticipantManager programParticipantManager = getProgramParticipantManager();
        ProgramParticipant prgmPart =
        	programParticipantManager.getClientProgramParticipants(
        	programParticipant.getProgramName(), FDUtils.getParticipantName(), false);
		prgmPart.setConstraint(FDUtils.getJSFConstraints().getConstraint());
		programParticipantManager.updateProgramParticipant(
			programParticipant.getProgramName(), FDUtils.getParticipantName(),false, prgmPart);

		return "updateConstraints";
	}
	
	/**
	 * Cancel constraints action.
	 * 
	 * @return the string
	 */
	public String cancelConstraintsAction()
	{
		return "cancelConstraints";
	}

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getProgramName()
	{
		return programParticipant.getProgramName();
	}
    public String getConsolidationProgramName()
	{
		if(CBPUtil.isEnableCBPConsolidation()){
			String key = programParticipant.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
				return "CBP";
            }else{
            	return key;
            }
		}else{
			return programParticipant.getProgramName();
		}
	}
	/**
	 * Checks if is bid.
	 * 
	 * @return true, if is bid
	 */
	public boolean isBid()
	{
		return bid;
	}

	/**
	 * Sets the bid.
	 * 
	 * @param bid the new bid
	 */
	public void setBid(boolean bid)
	{
		this.bid = bid;
	}

	/**
	 * Gets the clients string.
	 * 
	 * @return the clients string
	 */
	public String getClientsString()
	{
		return clientsString;
	}

	/**
	 * Sets the clients string.
	 * 
	 * @param clientsString the new clients string
	 */
	public void setClientsString(String clientsString)
	{
		this.clientsString = clientsString;
	}

	/**
	 * Gets the events string.
	 * 
	 * @return the events string
	 */
	public String getEventsString()
	{
		return eventsString;
	}

	/**
	 * Sets the events string.
	 * 
	 * @param eventsString the new events string
	 */
	public void setEventsString(String eventsString)
	{
		this.eventsString = eventsString;
	}

    public String getOptStatus() {
        return optStatus;
    }

    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }

	public void setDemandLimitingSettingsActive(boolean demandLimitingSettingsActive) {
		this.demandLimitingSettingsActive = demandLimitingSettingsActive;
	}

	public boolean isDemandLimitingSettingsActive() {
		return demandLimitingSettingsActive;
	}
	
	public boolean isCustomRulesEnabled() {
		if (isDemandLimitingSettingsActive()) {
			return false;
		} else {
			if (FDUtils.getHeader1() != null)
				return FDUtils.getHeader1().isCustomRulesEnabled();
			else
				return false;
		}
	}
	
	public boolean isDemandLimitingMockMeterActive() {
		return (isDemandLimitingSettingsActive()) && (FDUtils.getHeader1() != null) && FDUtils.getHeader1().isDemandLimitingMockMeterEnabled();
	}

	public String getParticipantName() {
		return FDUtils.getParticipantName();
	}

	/**
	 * @return the relativedClientParticipant
	 */
	public List<JSFParticipantProgram> getRelativedClientParticipant() {
		return relativedClientParticipant;
	}

	/**
	 * @param relativedClientParticipant the relativedClientParticipant to set
	 */
	public void setRelativedClientParticipant(
			List<JSFParticipantProgram> relativedClientParticipant) {
		this.relativedClientParticipant = relativedClientParticipant;
	}
	
	public boolean isDayOfAdjustment() {
		return dayOfAdjustment;
	}

	public void setDayOfAdjustment(boolean dayOfAdjustment) {
		this.dayOfAdjustment = dayOfAdjustment;
	}

}
