/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.Configuration.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.util.Properties;
import java.io.File;

/**
 * Configure most of application specific parameters and make them available to
 * other classes that uses it.
 * 
 * @author Dichen Mao
 * @since Sept 28, 2007
 */
public class Configuration {
    
    /** The properties. */
    private Properties properties;

    /**
     * Instantiates a new configuration.
     */
    public Configuration() {
        properties = new Properties();
        properties.putAll(System.getProperties());
    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     * 
     * @param properties the new properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets the home path.
     * 
     * @return the home path
     */
    public String getHomePath() {
        return (String)properties.get("jboss.server.home.dir");
    }

    /**
     * Gets the conf path.
     * 
     * @return the conf path
     */
    public String getConfPath() {
        return getHomePath() + File.separator + "conf";
    }

    /**
     * Gets the log path.
     * 
     * @return the log path
     */
    public String getLogPath() {
        return getHomePath() + File.separator + "log";
    }
}
