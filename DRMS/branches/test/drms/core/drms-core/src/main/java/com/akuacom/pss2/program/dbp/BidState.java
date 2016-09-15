/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.BidState.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

/**
 * The Enum BidState.
 */
public enum BidState {
    
    /** The IDLE. */
    IDLE,
    
    /** The MISSE d_ bidding. */
    MISSED_BIDDING,
    
    /** The ACCEPTING. */
    ACCEPTING,
    
    /** The MISSE d_ respon d_ by. */
    MISSED_RESPOND_BY,
    
    /** The SENDIN g_ t o_ utility. */
    SENDING_TO_UTILITY,

    /** The SENDIN g_ t o_ utility. */
    SENDING_TO_UTILITY_FAILED,

    /** The WAITIN g_ fo r_ acceptance. */
    WAITING_FOR_ACCEPTANCE,
    
    /** The PROCESSIN g_ complete. */
    PROCESSING_COMPLETE,
}
