package com.akuacom.pss2.core;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceEmailResourceUtil extends EmailResourceUtil {
	
	private static EmailResourceUtil commonResourceUtil = new EmailResourceUtil();
	
	 /** The _resource name. */
    private String resourceName = "com.akuacom.pss2.email.SceEmailMessages";

    protected static SceEmailResourceUtil resouceBundleUtil = null;

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
            resouceBundleUtil = new SceEmailResourceUtil();
        }
        if(!resouceBundleUtil.resourceBundleMap.containsKey(locale))
        {
            ResourceBundle rb = ResourceBundle.getBundle(resouceBundleUtil.resourceName, locale);
            resouceBundleUtil.resourceBundleMap.put(locale, rb);
        }
        return (ResourceBundle) resouceBundleUtil.resourceBundleMap.get(locale);
    }

    public static String getLocalizedString(String key, List<String> variables) {
		String rawString = null;
		rawString = getLocalizedString(key, getDefaultLocale());
		if(rawString==null){
			rawString = commonResourceUtil.getLocalizedString(key, variables);
		}
		
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
