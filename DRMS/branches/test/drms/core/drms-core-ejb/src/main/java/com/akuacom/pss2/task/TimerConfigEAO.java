/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.task;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.contact.Contact;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface TimerConfigEAO extends BaseEAO<TimerConfig>{
    @Remote
    public interface R extends TimerConfigEAO {}

    @Local
    public interface L extends TimerConfigEAO {}
    
    TimerConfig getTimerConfig(String timerConfigName);
    
}