/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ErrorResourceUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.ArrayList;

/**
 * Provides access to the error messages resource bundle.
 * User: lin
 * Date: Aug 6, 2008
 * Time: 2:06:48 PM
 */
public class ErrorResourceUtil extends ResourceBundleUtil
{
    
    /** The _resource name. */
    private static String _resourceName = "com.akuacom.pss2.core.ErrorMessages";

    /**
     * Gets the resource bundle.
     * 
     * @param userLocale the user locale
     * 
     * @return the resource bundle
     */
    public static ResourceBundle getResourceBundle(Locale userLocale)
    {
        Locale locale;
        if(userLocale == null)
        {
            locale = getDefaultLocale();
        }
        else
        {
            locale = userLocale;
        }

        if(resouceBundleUtil == null)
        {
            resouceBundleUtil = new ResourceBundleUtil();
        }
        if(!resouceBundleUtil.resourceBundleMap.containsKey(locale))
        {
            ResourceBundle rb = ResourceBundle.getBundle(_resourceName, locale);
            resouceBundleUtil.resourceBundleMap.put(locale, rb);
        }
        return (ResourceBundle) resouceBundleUtil.resourceBundleMap.get(locale);
    }

    /**
     * Gets the error message.
     * 
     * @param key the key
     * @param variables the variables
     * 
     * @return the error message
     */
    public static String getErrorMessage(String key, List<String> variables)
    {
        return getLocalizedString(key, variables);
    }

    /**
     * Gets the error message.
     * 
     * @param key the key
     * 
     * @return the error message
     */
    public static String getErrorMessage(String key)
    {
        return getErrorMessage(key, new String());
    }

    /**
     * Gets the error message.
     * 
     * @param key the key
     * @param variable the variable
     * 
     * @return the error message
     */
    public static String getErrorMessage(String key, String variable)
    {
        List<String> parameters = new ArrayList<String>();
        parameters.add(variable);
        return ErrorResourceUtil.getErrorMessage(key, parameters);
    }

    /**
     * Gets the localized string from raw string.
     * 
     * @param rawString the raw string
     * @param variables the variables
     * 
     * @return the localized string from raw string
     */
    public static String getLocalizedStringFromRawString(String rawString, List<String> variables)
    {
        return ResourceBundleUtil.getLocalizedStringFromRawString(rawString, variables);
    }
}
