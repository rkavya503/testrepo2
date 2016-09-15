/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.util.EJBFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.util;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;

/**
 * A factory for creating EJB objects.
 */
public class EJBFactory extends EJB3Factory {

    /**
     * Gets the program manager.
     * 
     * @return the program manager
     */
    public static EventManager getEventManager() {
        return getLocalBean(EventManager.class);
    }

    /**
     * Gets the bidding program manager.
     * 
     * @return the bidding program manager
     */
    public static BiddingProgramManager getBiddingProgramManager() {
        return getLocalBean(BiddingProgramManager.class);
    }
}
