/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ErrorResourceUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.core.ResourceBundleUtil;

import java.util.*;

/**
 * Provides access to the error messages resource bundle.
 * User: lin
 * Date: Aug 6, 2008
 * Time: 2:06:48 PM
 */
public class EmailResourceUtil extends ResourceBundleUtil
{

    /** The _resource name. */
    private String resourceName = "com.akuacom.pss2.email.EmailMessages";

    protected static EmailResourceUtil resouceBundleUtil = null;

    protected HashMap resourceBundleMap = new HashMap();

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
            resouceBundleUtil = new EmailResourceUtil();
        }
        if(!resouceBundleUtil.resourceBundleMap.containsKey(locale))
        {
            ResourceBundle rb = ResourceBundle.getBundle(resouceBundleUtil.resourceName, locale);
            resouceBundleUtil.resourceBundleMap.put(locale, rb);
        }
        return (ResourceBundle) resouceBundleUtil.resourceBundleMap.get(locale);
    }

    public static String getLocalizedString(String key, List<String> variables) {
		String rawString = getLocalizedString(key, getDefaultLocale());
        String token = "${?}";
		StringBuffer retString = new StringBuffer();
		if (rawString.indexOf(token) < 0)
			retString = retString.append(rawString);
		else {
			int i = 0;
			int begin = 0;
			int end = 0;
			while ((end = rawString.indexOf(token, begin)) >= 0) {
				retString.append(rawString.substring(begin, end));
				begin = end + token.length();
				if (i < variables.size()) {
					retString.append(variables.get(i));
				} else {
					retString.append(token);
				}
				i++;
			}
			retString.append(rawString.substring(begin));
		}
		return retString.toString();
	}

    public static String getLocalizedString(String key, Locale userLocale) {
        try
        {
            return getResourceBundle(userLocale).getString(key);
        }
        catch (Exception e) {
			return "";
		}
    }
}