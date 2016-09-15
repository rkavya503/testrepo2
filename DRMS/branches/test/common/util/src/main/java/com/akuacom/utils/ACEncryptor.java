/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ACEncryptor.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: lin
 * Date: Aug 26, 2008
 * Time: 11:52:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ACEncryptor
{
    
    /**
     * Gets the hash bytes.
     * 
     * @param input the input
     * 
     * @return the hash bytes
     * 
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static byte[] getHashBytes(String input)
        throws NoSuchAlgorithmException
    {
        byte[] output;
        MessageDigest msgDg = MessageDigest.getInstance("MD5");
        msgDg.update(input.getBytes());
        output = msgDg.digest();
        return output;
    }

    /**
     * Gets the hash bytes.
     * 
     * @param input the input
     * 
     * @return the hash bytes
     * 
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static byte[] getHashBytes(byte input[])
        throws NoSuchAlgorithmException
    {
        byte[] output;
        MessageDigest msgDg = MessageDigest.getInstance("MD5");
        msgDg.update(input);
        output = msgDg.digest();
        return output;
    }

    /**
     * Gets the hash string.
     * 
     * @param input the input
     * 
     * @return the hash string
     */
    public static String getHashString(byte input[])
    {
        StringBuffer buffer = new StringBuffer(input.length * 2);
        for(int i=0; i<input.length;i++)
        {
            if(((int) input[i] & 0xff ) < 0x10)
            {
                buffer.append("0");
            }
            buffer.append(Long.toString((int) input[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    /**
     * Encrypt.
     * 
     * @param input the input
     * 
     * @return the string
     * 
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String encrypt(String input)
                            throws NoSuchAlgorithmException
    {
        return getHashString(getHashBytes(input));
    }

    /**
     * Encrypt.
     * 
     * @param input the input
     * 
     * @return the string
     * 
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String encrypt(byte input[])
                            throws NoSuchAlgorithmException
    {
        return getHashString(getHashBytes(input));
    }
}