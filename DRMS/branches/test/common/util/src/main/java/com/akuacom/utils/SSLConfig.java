/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.SSLConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

/**
 * The Class SSLConfig.
 */
public class SSLConfig
{
	
	/** The logger. */
	private final Logger logger;
	
	/**
	 * Instantiates a new sSL config.
	 */
	public SSLConfig()
	{
		logger = null;
	}
	
	/**
	 * Instantiates a new sSL config.
	 * 
	 * @param theLogger the the logger
	 */
	public SSLConfig(Logger theLogger)
	{
		logger = theLogger;
	}
	
	/**
	 * Output.
	 * 
	 * @param output the output
	 */
	private void output(String output)
	{
		if(logger != null)
		{
			logger.warn(output);
		}
		else
		{
			System.out.println(output);
		}
	}
	
	/**
	 * Configure.
	 * 
	 * @param store the store
	 * @param password the password
	 */
	public void configure(String store, String password)
	{
        System.setProperty("javax.net.ssl.trustStore", store);
        System.setProperty("javax.net.ssl.trustStorePassword", password);
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                output("Warning: URL Host: " + urlHostName +
                  " vs. " + session.getPeerHost());
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
}
