/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ConfigPropertiesTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.util.Properties;
import java.util.logging.Level;

import junit.framework.TestCase;

/**
 * The Class ConfigPropertiesTest.
 */
public class ConfigPropertiesTest extends TestCase
{
    // configuration properties
    /** The config. */
    private Properties config = null;
    
    // default configuration file name
    /** The Constant DEFAULT_CONFIG_FILE_NAME. */
    private static final String DEFAULT_CONFIG_FILE_NAME = "ConfigPropertiesTest.conf";
    
    // for each property, there are two macros:
    //  - PROP_* the property name
    //  - DEFAULT_* is the default value if not overridden in the config file
    // test1
    /** The Constant PROP_TEST1. */
    private static final String PROP_TEST1 = "test1";
    
    /** The Constant DEFAULT_TEST1. */
    private static final String DEFAULT_TEST1 = "test1";
    // test2
    /** The Constant PROP_TEST2. */
    private static final String PROP_TEST2 = "test2";
    
    /** The Constant DEFAULT_TEST2. */
    private static final String DEFAULT_TEST2 = "test2";
    // test3
    /** The Constant PROP_TEST3. */
    private static final String PROP_TEST3 = "test3";
    
    /** The Constant DEFAULT_TEST3. */
    private static final String DEFAULT_TEST3 = "test3";
    
    /** The Constant defaultProps. */
    private static final String[][] defaultProps = 
    {
        {PROP_TEST1, DEFAULT_TEST1},
        {PROP_TEST2, DEFAULT_TEST2},
        {PROP_TEST3, DEFAULT_TEST3},
    };

    /**
     * Instantiates a new config properties test.
     */
    public ConfigPropertiesTest()
    {
    }
    
    /**
     * Test.
     * 
     * @throws Exception the exception
     */
    public void test() throws Exception
    {
        // set up the properties
        config = ConfigProperties.configureProperties(new String[0], 
          DEFAULT_CONFIG_FILE_NAME, defaultProps);  

        assertEquals(config.getProperty(PROP_TEST1), DEFAULT_TEST1);
        assertEquals(config.getProperty(PROP_TEST2), "newTest2");
        assertEquals(config.getProperty(PROP_TEST3), DEFAULT_TEST3);
    }		
}
