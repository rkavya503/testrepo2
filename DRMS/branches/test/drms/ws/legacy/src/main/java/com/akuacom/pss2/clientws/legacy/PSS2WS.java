/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.PSS2WS.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.clientws.legacy;

import java.rmi.RemoteException;

import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.log4j.Logger;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.PriceSchedule;
import com.akuacom.pss2.util.PriceScheduleEntry;

/*
 * rps jax-rpc web service.
 * 
 * even though this doesn't explicitly extend HttpServlet, under the covers 
 * jax-rpc runs each instance as a servlet which is why we can say it
 * implements ServiceLifecycle.
 */
/**
 * The Class PSS2WS.
 */
public class PSS2WS implements PSS2WSSEI, ServiceLifecycle
{
	
	/** The user. */
	String user = null;

	/** The participant. */
	Participant participant = null;

	/** The log. */
	private static Logger log = Logger.getLogger(PSS2WS.class.getName());

	ClientManager clientManager = EJBFactory.getBean(ClientManager.class);

	//*********************************************************************
	// begin PSS2WSSEI interface
	//
	// process a RPSRequest
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.clientws.legacy.PSS2WSSEI#getPriceSchedule(com.akuacom.pss2.util.PriceSchedule)
	 */
	public PriceSchedule getPriceSchedule(PriceSchedule lastPriceSchedule)
		throws RemoteException
	{
		try
		{
			return clientManager.getPriceSchedule(user, lastPriceSchedule);
		}
		catch (Exception ex)
		{
			String id=user+"_schedule _CLIENT_POLLING_EXCEPTION";
			log.fatal(LogUtils.createSuppressibleLogEntry("",ex,id,60*60));
			// TODO: should probably throw an exception back to the caller here
			PriceSchedule priceSchedule = new PriceSchedule();
			priceSchedule.setCurrentPriceDPKWH(1.0);
			priceSchedule.setEntries(new PriceScheduleEntry[0]); 
			return priceSchedule;
		}
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.clientws.legacy.PSS2WSSEI#getPrice(double)
	 */
	public double getPrice(double lastPrice) throws RemoteException
	{
		try
		{
			return clientManager.getPrice(user, lastPrice);
		}
		catch (Exception ex)
		{
			String id=user+"_price_CLIENT_POLLING_EXCEPTION";
			log.fatal(LogUtils.createSuppressibleLogEntry("",ex,id,60*60));
			return 1.0;
		}
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.clientws.legacy.PSS2WSSEI#isAPEEventPending()
	 */
	public boolean isAPEEventPending() throws RemoteException
	{
		try
		{
			return clientManager.isAPEEventPending(user);
		}
		catch (Exception ex)
		{
			String id=user+"_pending_CLIENT_POLLING_EXCEPTION";
			log.fatal(LogUtils.createSuppressibleLogEntry("",ex,id,60*60));
			return false;
		}
	}

	// end PSS2WSSEI interface
	//**********************************************************************    

	//*********************************************************************
	// begin ServiceLifecycle interface
    /* (non-Javadoc)
	 * @see javax.xml.rpc.server.ServiceLifecycle#init(java.lang.Object)
	 */
	public void init(Object context)
    {
        // set the thread name for debugging
        Thread.currentThread().setName("PSS2WS");
        user = ((ServletEndpointContext)context).getUserPrincipal().getName();
    }

	/* (non-Javadoc)
	 * @see javax.xml.rpc.server.ServiceLifecycle#destroy()
	 */
	public void destroy()
	{
	}
	// end ServiceLifecycle interface
	//**********************************************************************    
}
