/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.CoreConfig.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This class manages configuration files since EJB beans should not using IO
 * api directly.
 * 
 * Note: this class may throw all kinds of exception because all things in
 * this class have to be loaded right, otherwise, the whole app should fail.
 * 
 * @author Dichen Mao
 */
public class CoreConfig {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(CoreConfig.class);

    /** The Constant jbossdir. */
    public static final String jbossdir = System.getProperty("jboss.server.home.dir");
    
    /** The Constant confdir. */
    public static final String confdir = jbossdir + File.separator + "conf" + File.separator;

    // default configuration file name
    /** The Constant SIGNALS_CONFIG_FILE_NAME. */
    public static final String SIGNALS_CONFIG_FILE_NAME = confdir + "pss2.signals.conf";
    
    /** The Constant PROGRAMS_CONFIG_FILE_NAME. */
    public static final String PROGRAMS_CONFIG_FILE_NAME = confdir + "pss2.programs.conf";
    
    /** The Constant ACCESS_CONFIG_FILE_NAME. */
    public static final String ACCESS_CONFIG_FILE_NAME = confdir + "pss2.access.conf";

    /**
     * Load signal properties.
     * 
     * @return the properties
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Properties loadSignalProperties() throws IOException {
        return loadProperties(SIGNALS_CONFIG_FILE_NAME);
    }

    /**
     * Load program properties.
     * 
     * @return the properties
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Properties loadProgramProperties() throws IOException {
        return loadProperties(PROGRAMS_CONFIG_FILE_NAME);
    }

    /**
     * Load access properties.
     * 
     * @return the properties
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Properties loadAccessProperties() throws IOException {
        return loadProperties(ACCESS_CONFIG_FILE_NAME);
    }

    /**
     * Load properties.
     * 
     * @param fileName the file name
     * 
     * @return the properties
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static Properties loadProperties(String fileName) throws IOException {
        Properties config = new Properties();
        File configFile = new File(fileName);
        if (!configFile.isFile()) {
            String message = "can't find configuration file: " + fileName;
            log.fatal(message);
            throw new RuntimeException(message);
        } else {
            FileReader reader = new FileReader(configFile);
            config.load(reader);
            reader.close();
        }

        return config;
    }
}
