/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.UUIDFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.util.UUID;

/**
 * A factory for creating UUID objects.
 */
public class UUIDFactory {
    
    /**
     * Gets the uUID.
     * 
     * @return the uUID
     */
    public static String getUUID() {
        //return UUID.randomUUID().toString().replaceAll("-", "");
        //Can't use random string UUID. The performance can be downgraded a lot for
        //any where condition involving randomized uuid.
        return getUUID(new Object());
    }

    /**
     * Gets the uUID.
     * 
     * @param obj the obj
     * 
     * @return the uUID
     */
    public static String getUUID(Object obj) {
        return ACUUIDGenerator.generateUUID(obj);
    }
}
