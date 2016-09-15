/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ContactServicer.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.nssettings;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;

/**
 * This class serves all the functions related to the notification system global
 * settings management.
 * 
 * @author Li Fei
 * 
 *         Initial date 2010.07.01
 */


public interface NSSettingsEAO extends BaseEAO<NSSettings> {
    @Remote
    public interface R extends NSSettingsEAO {}
    @Local
    public interface L extends NSSettingsEAO {}

    /**
     * Save notification system global settings
     * 
     * @param nssettings
     */
    void saveNSSettings(NSSettings nssettings);

    /**
     * Get notification system global settings
     * 
     * @return NSSettingsEntity Initial date Jul 27, 2010
     */
    NSSettings getNSSettings();

}