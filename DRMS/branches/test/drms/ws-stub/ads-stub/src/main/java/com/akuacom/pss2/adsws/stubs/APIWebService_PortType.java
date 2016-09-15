/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.stubs.APIWebService_PortType.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.akuacom.pss2.adsws.stubs;

/**
 * The Interface APIWebService_PortType.
 */
public interface APIWebService_PortType extends java.rmi.Remote {
    
    /**
     * Gets the dispatch batches since uid.
     * 
     * @param batchUID the batch uid
     * 
     * @return the dispatch batches since uid
     * 
     * @throws RemoteException the remote exception
     */
    public java.lang.String getDispatchBatchesSinceUID(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Gets the batch status.
     * 
     * @param batchUID the batch uid
     * 
     * @return the batch status
     * 
     * @throws RemoteException the remote exception
     */
    public int getBatchStatus(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Gets the dispatch batch.
     * 
     * @param batchUID the batch uid
     * 
     * @return the dispatch batch
     * 
     * @throws RemoteException the remote exception
     */
    public java.lang.String getDispatchBatch(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Gets the batch header.
     * 
     * @param batchUID the batch uid
     * 
     * @return the batch header
     * 
     * @throws RemoteException the remote exception
     */
    public java.lang.String getBatchHeader(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Checks if is new traj data.
     * 
     * @param batchUID the batch uid
     * 
     * @return true, if is new traj data
     * 
     * @throws RemoteException the remote exception
     */
    public boolean isNewTrajData(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Gets the trajectory data.
     * 
     * @param batchUID the batch uid
     * 
     * @return the trajectory data
     * 
     * @throws RemoteException the remote exception
     */
    public java.lang.String getTrajectoryData(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Validate dispatch batch.
     * 
     * @param batchUID the batch uid
     * 
     * @throws RemoteException the remote exception
     */
    public void validateDispatchBatch(java.lang.String batchUID) throws 
         java.rmi.RemoteException;
    
    /**
     * Submit msslf request.
     * 
     * @param request the request
     * 
     * @return the java.lang. string
     * 
     * @throws RemoteException the remote exception
     */
    public java.lang.String submitMSSLFRequest(java.lang.String request) throws 
         java.rmi.RemoteException;
}
