/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.ClientStatus.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.client;

/**
 * The Enum ClientStatus.
 */
public enum ClientStatus {

    /** The ONLINE. */
    ONLINE,

    /** The ERROR. */
    ERROR,

    /** The OFFLINE. */
    OFFLINE;
    
    public static ClientStatus valueOf(int i) {
        return values()[i];
    }
}
