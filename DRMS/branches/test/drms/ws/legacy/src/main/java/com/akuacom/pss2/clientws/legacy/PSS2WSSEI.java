/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.PSS2WSSEI.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.legacy;

import java.rmi.RemoteException;

import com.akuacom.pss2.util.PriceSchedule;

/**
 * The Interface PSS2WSSEI.
 */
public interface PSS2WSSEI extends java.rmi.Remote
{
    
    /**
     * Gets the price schedule.
     * 
     * @param lastPriceSchedule the last price schedule
     * 
     * @return the price schedule
     * 
     * @throws RemoteException the remote exception
     */
    public PriceSchedule getPriceSchedule(PriceSchedule lastPriceSchedule) 
      throws RemoteException;

    /**
     * Gets the price.
     * 
     * @param lastPrice the last price
     * 
     * @return the price
     * 
     * @throws RemoteException the remote exception
     */
    public double getPrice(double lastPrice) 
      throws RemoteException;

    /**
     * Checks if is aPE event pending.
     * 
     * @return true, if is aPE event pending
     * 
     * @throws RemoteException the remote exception
     */
    public boolean isAPEEventPending() 
      throws RemoteException;
}
