/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ParticipantBidState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant.bid;

/**
 * This class represents each event participant's bidding state.
 * Don't mix with BidState which is for event level bidding state.
 * 
 * @see com.akuacom.pss2.program.dbp.BidState
 * @author Dichen Mao
 */
public enum ParticipantBidState {
    
    /** The Pending. */
    Pending,
    
    /** The Acknowledged. */
    Acknowledged,
    
    /** The Rejected. */
    Rejected,
    
    /** The Accepted. */
    Accepted
}
