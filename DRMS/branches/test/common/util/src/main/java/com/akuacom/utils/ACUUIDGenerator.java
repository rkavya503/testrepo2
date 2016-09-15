/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ACUUIDGenerator.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 26, 2008
 * Time: 11:21:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ACUUIDGenerator
{
    
    /** The LOCA l_ mac. */
    static String LOCAL_MAC = null;
    
    /**
     * Generate uuid.
     * 
     * @param obj the obj
     * 
     * @return the string
     */
    static public String generateUUID(Object obj)
    {
       return getMac()  + Long.toHexString(System.currentTimeMillis()) + Integer.toHexString(obj.hashCode());
    }

    /**
     * Gets the mac.
     * 
     * @return the mac
     */
    static public String getMac()
    {
        try
        {
           if(LOCAL_MAC == null)
           {
               InetAddress address = InetAddress.getLocalHost();
               NetworkInterface ni = NetworkInterface.getByInetAddress(address);
               byte[] mac = ni.getHardwareAddress();
               LOCAL_MAC = ACEncryptor.getHashString(mac);
           }


           return LOCAL_MAC;
       }
       catch (Exception e)
        {
            long random =(long)(Math.random()*(Long.MAX_VALUE));
            return Long.toHexString(random) ;
        }
    }

}