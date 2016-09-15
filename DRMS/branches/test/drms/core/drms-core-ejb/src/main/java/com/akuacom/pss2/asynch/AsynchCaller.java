package com.akuacom.pss2.asynch;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

/**
 * <tt>AsynchCaller</tt>provides an asynchronous call mechanism using jms channel 
 *  Multiple threads are not encouraged to used inside EJB due to various reasons, 
 *  such as transaction problem. 
 *  
 *  the jms based asynchronous call works well with container managed transaction mode. 
 *  if the caller's transaction is rolled back, the jms channel will also roll back messages received
 *  so that the  asynchronous block to be executed will not be fired. 
 *  
 *  
 */
public interface AsynchCaller extends TimerManager {

	@Remote
	public interface R extends AsynchCaller {}
	@Local
	public interface L extends AsynchCaller {}
	
	public void call(AsynchRunable runable);
	
	
}
