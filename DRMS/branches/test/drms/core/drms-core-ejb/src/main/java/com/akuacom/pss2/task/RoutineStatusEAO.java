/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.task;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface RoutineStatusEAO extends RoutineStatusGenEAO{
    @Remote
    public interface R extends RoutineStatusEAO {}

    @Local
    public interface L extends RoutineStatusEAO {}
    
    RoutineStatus getRoutineStatus(String routine,Date date);
    
    RoutineStatus insertOrUpdate(RoutineStatus status);
    
}