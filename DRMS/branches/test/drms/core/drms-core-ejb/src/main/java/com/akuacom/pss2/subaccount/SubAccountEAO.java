/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.subaccount;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */

public interface SubAccountEAO extends BaseEAO<SubAccount> {
    @Remote
    public interface R extends SubAccountEAO {}

    @Local
    public interface L extends SubAccountEAO {}

    List<SubAccount> getSubAccounts(String participantName, boolean isClient);

    void removeSubAccounts(String participantName, boolean isClient);

    SubAccount getSubAccount(String subAccountName, String participantName,
            boolean isClient);

}