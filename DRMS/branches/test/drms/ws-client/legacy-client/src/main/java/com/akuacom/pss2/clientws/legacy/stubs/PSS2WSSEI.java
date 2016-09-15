/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.stubs.PSS2WSSEI.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.clientws.legacy.stubs;

/**
 * The Interface PSS2WSSEI.
 */
public interface PSS2WSSEI extends java.rmi.Remote {
    
    /**
     * Gets the price.
     * 
     * @param double_1 the double_1
     * 
     * @return the price
     * 
     * @throws RemoteException the remote exception
     */
    public double getPrice(double double_1) throws 
         java.rmi.RemoteException;
    
    /**
     * Gets the price schedule.
     * 
     * @param priceSchedule_1 the price schedule_1
     * 
     * @return the price schedule
     * 
     * @throws RemoteException the remote exception
     */
    public com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule getPriceSchedule(com.akuacom.pss2.clientws.legacy.stubs.PriceSchedule priceSchedule_1) throws
         java.rmi.RemoteException;
    
    /**
     * Checks if is aPE event pending.
     * 
     * @return true, if is aPE event pending
     * 
     * @throws RemoteException the remote exception
     */
    public boolean isAPEEventPending() throws 
         java.rmi.RemoteException;
}
