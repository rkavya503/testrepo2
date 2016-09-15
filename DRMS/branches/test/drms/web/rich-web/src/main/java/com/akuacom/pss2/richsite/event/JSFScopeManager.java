package com.akuacom.pss2.richsite.event;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * 
 * Filename:    JSFScopeManager.java 
 * Description: This class is the manager for control JSF framework scope object.
 * In some senario that object need transfer between Request and Session.
 * For example: A object which scope is request but need pass object between presentation pages, 
 * the behavior of this object act as a session. Also a object which scope is session some times need renew itself, when
 * the behavior of this object act as a request. This class is a utility manager class for handler this. 
 * Copyright:   Copyright (c)2010
 * Company:     
 * @author:     Yang Liu
 * @version:    
 * Create at:   Jan 6, 2011 11:46:27 AM 
 * 
 * Modification History: 
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------ 
 * Jan 6, 2011   Yang Liu   1.0         1.0 Version
 */
public class JSFScopeManager {
	//Singleton instance
	private static JSFScopeManager instance;
	
	/**
	 * Constructor
	 * private constructor to let the manager use Singleton
	 */
	private JSFScopeManager(){}
	/**
	 * Get manager instance
	 * @return
	 */
	public synchronized static JSFScopeManager getInstance(){
		if(instance==null){
			instance = new JSFScopeManager();
		}
		return instance;
	}
    /**
     * Return the FacesContext instance for the request that is
     * being processed by the current thread, if any.
     */
	public FacesContext getFacesContextInstance(){
		return FacesContext.getCurrentInstance();
	}
    /**
     * Return the ExternalContext instance for this
     * FacesContext instance.
     *
     * @throws IllegalStateException if this method is called after
     *  this instance has been released
     */	
	public ExternalContext getExternalContext(){
		return getFacesContextInstance().getExternalContext();
	}
	/**
	 * Return a mutable Map representing the session
     * scope attributes for the current JSF application.  
	 */
	public Map<String,Object> getSessionMap(){
		return getExternalContext().getSessionMap();
	}
	/**
	 * Return whether the session scope contains the key object
	 * @param key
	 * @return
	 */
	public boolean isSessionContains(String key){
		return getSessionMap().containsKey(key);
	}
	
	/**
	 * Return the result for delete key from the session.
	 * If the key contains, remove the object and return true value.
	 * If the key not contains, return the false value.
	 * @param key
	 * @return
	 */
	public boolean cleanSession(String key){
		boolean flag = false;
		if(isSessionContains(key)){
			getSessionMap().remove(key);
			flag = true;
		}
		return flag;
	}
	/**
	 * Add or Update the relative key-value into JSF session scope.
	 * If the key contains in the session, when the old value is replaced by the specified value.
	 * If the key not contains in the session, when add a new key-value into the session. 
	 * @param key
	 * @param value
	 */
	public void addOrUpdateSession(String key,Object value){
		getSessionMap().put(key, value);
	}
	

}
