/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.EntityNotFoundException;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface PDataSetEAO extends BaseEAO<PDataSet> {
    @Remote
    public interface R extends PDataSetEAO {}
    @Local
    public interface L extends PDataSetEAO {}

    PDataSet getDataSetByName(String name);

    List<String> getDataSetBySource(String sourceUUID)
            throws EntityNotFoundException;

}