/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.bidding.BiddingProgramManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.bidding;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dbp.Bid;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPBidProgramEJB;
import com.akuacom.pss2.program.dbp.DBPBidValidator;
import com.akuacom.pss2.program.dbp.DBPDataAccess;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB;
import com.akuacom.pss2.program.dbp.DBPNoBidValidator;
import com.akuacom.pss2.program.dbp.DBPProgram;

/**
 * The Class BiddingProgramManagerBean.
 */
@Stateless
public class BiddingProgramManagerBean implements BiddingProgramManager.R,
        BiddingProgramManager.L {

    /** The dbp program ejb. */
    @EJB
    protected DBPBidProgramEJB.L dbpProgramEJB;

    /** The dbp program ejb. */
    @EJB
    protected DBPNoBidProgramEJB.L dbpNoBidProgramEJB;

    /** The dbp data access. */
    @EJB
    protected DBPDataAccess.L dbpDataAccess;

    @EJB
    protected ClientManager.L clientManager;

    @EJB
    protected EventEAO.L eventEAO;

    @EJB
    protected ParticipantManager.L participantManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#getCurrentBid(
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public List<BidEntry> getCurrentBid(String programName, String eventName,
            String participantName, boolean isClient) {

        try {
            final Event event = eventEAO.getByEventNameWithParticipants(eventName);
            final Participant participant = participantManager.getParticipant(
                    participantName, isClient);
            return dbpDataAccess.getCurrentBid(programName, event, participant,
                    isClient);
        } catch (EntityNotFoundException e) {
            throw new EJBException(e);
        }
    }

	public List<BidEntry> getCurrentBid(String programName, Event event,
		Participant participant, boolean isClient) {
            return dbpDataAccess.getCurrentBid(programName, event, participant, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#getDefaultBid(
     * java.lang.String, java.lang.String)
     */
    public List<BidEntry> getDefaultBid(String programName,
            String participantName, boolean isClient) {
        return dbpDataAccess.getDefaultBid(programName, participantName,
                isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#getLevelMap(java
     * .lang.String, java.lang.String)
     */
    public Map<String, List<String>> getLevelMap(String programName,
            String participantName, boolean isClient) {
        return dbpDataAccess
                .getLevelMap(programName, participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#isBidAccepted(
     * java.lang.String, java.lang.String, java.lang.String)
     */
	public boolean isBidAccepted(String programName, Event event,
		String participantName, boolean isClient)
	{
		return dbpDataAccess.isBidAccepted(programName, event,
                participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#isBidAcknowledged
     * (java.lang.String, java.lang.String, java.lang.String)
     */
	public boolean isBidAcknowledged(String programName, Event event,
		String participantName, boolean isClient)
	{
		return dbpDataAccess.isBidAcknowledged(programName, event,
                participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#isBidDeclined(
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean isBidDeclined(String programName, String eventName,
            String participantName, boolean isClient) {
        return dbpDataAccess.isBidDeclined(programName, eventName,
                participantName, isClient);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setBidAccepted
     * (java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public void setBidAccepted(String programName, String eventName,
            String participantName, boolean isClient, boolean accepted) {
        dbpProgramEJB.setBidAccepted(programName, eventName, participantName,
                isClient, accepted);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setBidDeclined
     * (java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public void setBidDeclined(String programName, String eventName,
            String participantName, boolean isClient, boolean declined) {
        dbpProgramEJB.setBidAccepted(programName, eventName, participantName,
                isClient, !declined);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setCurrentBid(
     * java.lang.String, java.lang.String, java.lang.String, java.util.List)
     */
    public void setCurrentBid(String programName, String eventName,
        String participantName, boolean isClient, List<BidEntry> bids, boolean processRules)
        throws ProgramValidationException
    {
        ProgramManager programManager = EJBFactory.getBean(ProgramManager.class);
        DBPProgram programWithBidload = (DBPProgram)programManager.getProgramWithLoadBid(programName);
        DBPNoBidValidator validator =
            (DBPNoBidValidator)ValidatorFactory.getProgramValidator(programWithBidload);
        validator.validateCurrentBids(programWithBidload, bids);
        EventManager eventManager = EJBFactory.getBean(EventManager.class);
        Event event = eventManager.getEventAll(eventName);

        EventParticipant eventParticipant = null;
        for(EventParticipant ep: event.getEventParticipants())
        {
            if(ep.getParticipant().getParticipantName().equals(participantName)
                    && ep.getParticipant().isClient() == isClient)
            {
                eventParticipant = ep;
            }
        }

        dbpDataAccess.setCurrentBid(programName, event, eventParticipant, bids);

        if(processRules)
        {
        	DBPProgram programWithPPandPRules =(DBPProgram) programManager.getProgramWithParticipantsAndPRules(programName);
            dbpNoBidProgramEJB.processParticipantRulesAndSignals(programWithPPandPRules, event,
                null, eventParticipant, null, null, new Date(), null, null);
        }

        try
        {
            eventEAO.update(event);
        }
        catch (EntityNotFoundException e)
        {
            throw new EJBException(e);
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setDefaultBid(
     * java.lang.String, java.lang.String, java.util.List)
     */
    public void setDefaultBid(String programName, String participantName,
            boolean isClient, List<BidEntry> bids)
            throws ProgramValidationException {
        ProgramManager programManager = EJBFactory
                .getBean(ProgramManager.class);
        DBPProgram program = (DBPProgram) programManager
                .getProgramWithLoadBid(programName);
        final ProgramValidator programValidator = ValidatorFactory
                .getProgramValidator(program);
        if (programValidator instanceof DBPBidValidator) {
            DBPBidValidator validator = (DBPBidValidator) programValidator;
            validator.validateDefaultBids(program, bids);
        }
        dbpDataAccess.setDefaultBid(programName, participantName, isClient,
                bids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setLevelMap(java
     * .lang.String, java.lang.String, java.util.Map)
     */
    public void setLevelMap(String programName, String participantName,
            boolean isClient, Map<String, List<String>> ruleMap) {
        dbpDataAccess.setLevelMap(programName, participantName, isClient,
                ruleMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#nextState(java
     * .lang.String, com.akuacom.pss2.core.model.DBPEvent)
     */
    public void nextState(String programName, DBPEvent event) {
        dbpProgramEJB.nextState(programName, event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#getBid(java.lang
     * .String, java.lang.String, java.lang.String)
     */
    public List<BidEntry> getBid(String programName, String eventName,
            String participantName, boolean isClient) {
        // TODO lin: getCurrentBid should return default bids. need to reproduce
        // this case.
        List<BidEntry> ret;

        try {
            ret = this.getCurrentBid(programName, eventName, participantName,
                    isClient);
            if (ret == null) {
                ret = this
                        .getDefaultBid(programName, participantName, isClient);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.akuacom.pss2.program.bidding.BiddingProgramManager#setBidStatus(java
     * .util.List, java.util.List)
     */
    public void setBidStatus(List<Bid> eventParticipantAcceptedList,
            List<Bid> eventParticipantRejectedList) {
        dbpProgramEJB.setBidStatus(eventParticipantAcceptedList,
                eventParticipantRejectedList);
    }

    @Override
    public UtilityDREvent parseBidFile(String filename, String fileString) throws ProgramValidationException {
        return dbpNoBidProgramEJB.parseBidFile(filename, fileString);
    }
}
