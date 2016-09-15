/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ResourceBundleUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.*;

import com.akuacom.common.resource.ManagedResourceBundle;

/**
 * Utility class to retrieve Java resource bundles for localization
 * 
 * TODO: This needs to be deleted and the pss2 package should be too, but this
 * legacy dependency has to go with Exception handlers in the utils jar for now.
 * 
 * @deprecated use {@link ManagedResourceBundle} instead. User: lin Date: Aug 6,
 *             2008 Time: 12:54:20 PM.
 */
@Deprecated
public class ResourceBundleUtil {
	// TODO: change to enum? it would eliminate the string value.

	public static final String ERROR_INTERNAL_ERROR = "ERROR_INTERNAL_ERROR";
	public static final String ERROR_EVENT_CREATE_CREATION_ERROR = "ERROR_EVENT_CREATE_CREATION_ERROR";
	public static final String ERROR_EVENT_CREATE_EVENT_DATE_EMPTY = "ERROR_EVENT_CREATE_EVENT_DATE_EMPTY";
	public static final String ERROR_EVENT_CREATE_DATAFILE_EMPTY = "ERROR_EVENT_CREATE_DATAFILE_EMPTY";
	public static final String ERROR_EVENT_CREATE_EVENT_DATE_PARSER = "ERROR_EVENT_CREATE_EVENT_DATE_PARSER";
	public static final String ERROR_EVENT_CREATE_RESPOND_BY_DATE_EMPTY = "ERROR_EVENT_CREATE_RESPOND_BY_DATE_EMPTY";
	public static final String ERROR_EVENT_CREATE_RESPOND_BY_DATE_PARSER = "ERROR_EVENT_CREATE_RESPOND_BY_DATE_PARSER";
	public static final String ERROR_EVENT_CREATE_BID_FILE_BAD_NUM_BID_BLOCKS = "ERROR_EVENT_CREATE_BID_FILE_BAD_NUM_BID_BLOCKS";
	public static final String ERROR_EVENT_CREATE_BID_FILE_BID_BLOCK_NOT_SUBSET = "ERROR_EVENT_CREATE_BID_FILE_BID_BLOCK_NOT_SUBSET";
	public static final String ERROR_EVENT_CREATE_BID_FILE_BID_BLOCKS_NOT_CONTIGUOUS = "ERROR_EVENT_CREATE_BID_FILE_BID_BLOCKS_NOT_CONTIGUOUS";
	public static final String ERROR_EVENT_CREATE_BID_FILE_BID_BLOCKS_DONT_FIT = "ERROR_EVENT_CREATE_BID_FILE_BID_BLOCKS_DONT_FIT";
	public static final String ERROR_EVENT_CREATE_BID_FILE_WRONG_NUMBER_OF_BID_BLOCKS = "ERROR_EVENT_CREATE_BID_FILE_WRONG_NUMBER_OF_BID_BLOCKS";
	public static final String ERROR_EVENT_CREATE_BID_FILE_CORRUPTED = "ERROR_EVENT_CREATE_BID_FILE_CORRUPTED";
	public static final String ERROR_EVENT_CREATE_BID_FILE_FILE_NOT_FOUDN = "ERROR_EVENT_CREATE_BID_FILE_FILE_NOT_FOUDN";
	public static final String ERROR_EVENT_CREATE_BID_FILE_ERROR_READING_FILE = "ERROR_EVENT_CREATE_BID_FILE_ERROR_READING_FILE";
	public static final String ERROR_EVENT_CREATE_BID_FILE_EMPTY = "ERROR_EVENT_CREATE_BID_FILE_EMPTY";
	public static final String ERROR_EVENT_PROGRAM_NOT_CONFIGURED = "ERROR_EVENT_PROGRAM_NOT_CONFIGURED";

	/** The Constant token. */
	private static final String token = "${?}";

	/** The _resource name. */
	public static String resourceName = "com.akuacom.pss2.core.ErrorMessages";

	/** The _resouce bundle util. */
	protected static ResourceBundleUtil resouceBundleUtil = null;

	/** The _resource bundle map. */
	protected HashMap resourceBundleMap = new HashMap();

	/**
	 * Gets the default locale.
	 * 
	 * @return the default locale
	 */
	public static Locale getDefaultLocale() {
		String language = "en";
		String country = "US";
		return new Locale(language, country);
	}

	/**
	 * Gets the resource bundle for a locale.
	 * 
	 * @param userLocale
	 *            the user locale
	 * 
	 * @return the resource bundle
	 */
	public static ResourceBundle getResourceBundle(Locale userLocale) {
		Locale locale;
		if (userLocale == null) {
			locale = getDefaultLocale();
		} else {
			locale = userLocale;
		}

		if (resouceBundleUtil == null) {
			resouceBundleUtil = new ResourceBundleUtil();
		}
		if (!resouceBundleUtil.resourceBundleMap.containsKey(locale)) {
			ResourceBundle rb = ResourceBundle.getBundle(resourceName, locale);
			resouceBundleUtil.resourceBundleMap.put(locale, rb);
		}
		return (ResourceBundle) resouceBundleUtil.resourceBundleMap.get(locale);
	}

	/**
	 * Gets the localized string with tokens replaced by the variables.
	 * 
	 * @param key
	 *            the key
	 * @param userLocale
	 *            the user locale
	 * @param variables
	 *            the variables
	 * 
	 * @return the localized string
	 */
	public static String getLocalizedString(String key, Locale userLocale,
			ArrayList variables) {
		String rawString = getLocalizedString(key, userLocale);

		return getLocalizedStringFromRawString(rawString, variables);

		// StringBuffer retString = new StringBuffer();
		// StringTokenizer token = new StringTokenizer(rawString, "${?}");
		// if(rawString.indexOf("${?}") < 0)
		// retString = retString.append(rawString);
		// else
		// {
		// int i = 0;
		// while(token.hasMoreTokens())
		// {
		// retString.append(token.nextToken());
		// if(i < variables.size())
		// {
		// retString.append(variables.get(i));
		// }
		// i++;
		// }
		// }
		// return retString.toString();
	}

	/**
	 * Gets the localized string.
	 * 
	 * @param key
	 *            the key
	 * @param userLocale
	 *            the user locale
	 * 
	 * @return the localized string
	 */
	public static String getLocalizedString(String key, Locale userLocale) {
		return getResourceBundle(userLocale).getString(key);
	}

	/**
	 * Gets the localized string for the default locale.
	 * 
	 * @param key
	 *            the key
	 * 
	 * @return the localized string
	 */
	public static String getLocalizedString(String key) {
		try {
			return getLocalizedString(key, getDefaultLocale());
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * Gets the localized string for the default locale with variable
	 * substitution.
	 * 
	 * @param key
	 *            the key
	 * @param variables
	 *            the variables
	 * 
	 * @return the localized string
	 */
	public static String getLocalizedString(String key, List<String> variables) {
		String rawString = getLocalizedString(key);

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
					retString.append("${?}");
				}
				i++;
			}
			retString.append(rawString.substring(begin));
		}
		return retString.toString();
	}

	/**
	 * Gets the string with tokens replace with variables - no localization.
	 * 
	 * @param rawString
	 *            the raw string
	 * @param variables
	 *            the variables
	 * 
	 * @return the localized string from raw string
	 */
	public static String getLocalizedStringFromRawString(String rawString,
			List<String> variables) {

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
				}
				i++;
			}
			retString.append(rawString.substring(begin));
		}
		return retString.toString();
	}
}
