/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ConfigProperties.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The Class ConfigProperties.
 */
public class ConfigProperties
{
    
    /**
     * Instantiates a new config properties.
     */
    public ConfigProperties()
    {
    }

    // configure the program properties
     /**
     * Configure properties.
     * 
     * @param args the args
     * @param configFileName the config file name
     * @param defaultProps the default props
     * 
     * @return the properties
     */
    public static Properties configureProperties(String[] args, String configFileName,
       String[][] defaultProps)
     {
         // if there are any command line arguments, assume the first one is the
         // configuration file name and ignore all the others
    	 if(args != null)
    	 {
	         if(args.length >= 1)
	         {
	             configFileName = args[0];
	         }
	         if(args.length > 1)
	         {
	             System.out.println("ignoring extra command line arguments");            
	         }
    	 }
    	 
         // setup the default configuration
         Properties defaultConfig = new Properties();
         for(String[] prop: defaultProps)
         {
             defaultConfig.put(prop[0], prop[1]);
         }

         // create the poperties object using the deafults and then read in any
         // custimazations from the configu file
         Properties config = new Properties(defaultConfig);
         
         // read the config file
         File configFile = new File(configFileName);
         System.out.println("configuration file = " + configFile.getAbsolutePath());
      
         {
             try 
             {
                 InputStream in = ClassLoader.getSystemResourceAsStream(configFileName);
                 config.load(in);
             }
             catch(IOException e)
             {
            	 System.out.println("configuration file not found - using defaults");
                
             }
         }

         // dump properties
         config.list(System.out);
         System.out.println("------------------------");
         
         return config;
    }

    // configure the program properties
     /**
     * Configure properties.
     * 
     * @param args the args
     * @param configFileName the config file name
     * @param defaultProps the default props
     * @param logger the logger
     * 
     * @return the properties
     */
    public static Properties configureProperties(String[] args, String configFileName,
       String[][] defaultProps, Logger logger)
     {
         // if there are any command line arguments, assume the first one is the
         // configuration file name and ignore all the others
    	 if(args != null)
    	 {
	         if(args.length >= 1)
	         {
	             configFileName = args[0];
	         }
	         if(args.length > 1)
	         {
	             logger.debug("ignoring extra command line arguments");            
	         }
    	 }
         
         // setup the default configuration
         Properties defaultConfig = new Properties();
         for(String[] prop: defaultProps)
         {
             defaultConfig.put(prop[0], prop[1]);
         }

//         logger.debug(defaultConfig);

         // create the poperties object using the deafults and then read in any
         // custimazations from the configu file
         Properties config = new Properties(defaultConfig);
         
         // read the config file
         File configFile = new File(configFileName);
//         logger.debug("configuration file = " + configFile.getAbsolutePath());
         if(!configFile.isFile())
         {
             logger.debug("configuration file not found - using defaults");
         }
         else
         {
             try 
             {
                 FileInputStream in = new FileInputStream(configFile);
                 config.load(in);
             }
             catch(IOException e)
             {
                 logger.warn(
                   "error reading configuration file - using defaults");
             }
         }

         // dump properties
//         logger.debug(config);
//         logger.debug("------------------------");
         
         return config;
    }
}
