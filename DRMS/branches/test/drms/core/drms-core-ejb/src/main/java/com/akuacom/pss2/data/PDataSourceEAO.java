/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;

/**
 */

public interface PDataSourceEAO extends BaseEAO<PDataSource> {
    @Remote
    public interface R extends PDataSourceEAO {}
    @Local
    public interface L extends PDataSourceEAO {}

    PDataSource getDataSourceByNameAndOwner(String name, String owner);
    
    List<PDataSource> getDataSourceByOwner(List<String> participantNames);

    Map<String, String> getDataSourceIdMapByParticipantNames(List<String> participantNames);
    
    List<String> getDataSourceOwnerById(List<String> ids);
    
    Map<String, String> getDataSourcesByPartNames(List<String> partNames);

    void createMissing();
    
    List<String> getDataSourceIdByNameAndOwners(String name, List<String> owners);

	List<PDataSource> getDataSourceBySiteID();
}