/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.model.PSS2Properties.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.system.property;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.akuacom.annotations.NoTrace;
import com.akuacom.pss2.contact.ConfirmationLevel;
 
/**
 * The Class PSS2Properties.
 */
@NoTrace
public class PSS2Properties implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1886672989962845225L;

    /** The storage of all properties available in this class. */
    private Map<PropertyName, CoreProperty> properties = new HashMap<PropertyName, CoreProperty>();

    /**
     * Instantiates a new pS s2 properties.
     */
    public PSS2Properties() {
    }

    /**
     * Passes all properties from that to this encouraging non-mutable behavior.
     * 
     * @see #PSS2Properties(Collection) which does the transfer
     * 
     * @param that
     *            template
     */
    public PSS2Properties(PSS2Properties that) {
        this(that.getAllCoreProperties());
    }

    public PSS2Properties(Collection<CoreProperty> properties) {
        for (CoreProperty coreProperty : properties) {
            setCoreProperty(coreProperty);
        }

    }
    
    public PSS2Properties(CoreProperty property) {
    	if(property != null){
    		setCoreProperty(property);	
    	}
    	
    }
    
    

    @Override
    public boolean equals(Object obj) {

        boolean result = false;
        if (obj != null && obj instanceof PSS2Properties) {
            PSS2Properties that = (PSS2Properties) obj;
            result = properties.equals(that.properties);
        }
        return result;
    }

    /**
     * Similar to equals method, however, this verifies the values in the
     * properties are the same per the rules of
     * {@link CoreProperty#equalsValue(CoreProperty)}.
     * 
     * @param that
     *            the other
     * @return true/false
     */
    public boolean equalValues(PSS2Properties that) {

        boolean isEqual = properties.size() == that.properties.size();
        // if the same size, maybe the same properties...let's check each until
        // failure.
        for (Iterator<CoreProperty> iterator = properties.values()
                .iterator(); iterator.hasNext() && isEqual;) {

            CoreProperty thisProperty = iterator.next();

            CoreProperty thatProperty = that.getCoreProperty(thisProperty
                    .getPropertyName());
            if (thatProperty == null) {
                // property not found so these are not equal
                isEqual = false;
            } else {
                isEqual &= thisProperty.equalsValue(thatProperty);
            }
        }
        return isEqual;
    }

    /**
     * Gets the contact e mail.
     * 
     * @return the contact e mail
     */
    public String getContactEMail() {
        return getStringValue(PropertyName.CONTACT_EMAIL);
    }

    /**
     * Gets the contact phone.
     * 
     * @return the contact phone
     */
    public String getContactPhone() {
        return getStringValue(PropertyName.CONTACT_PHONE);
    }

    /**
     * Gets the contact url display name.
     * 
     * @return the contact url display name
     */
    public String getContactURLDisplayName() {
        return getStringValue(PropertyName.CONTACT_URL_DISPLAY_NAME);
    }

    /**
     * Gets the contact url link.
     * 
     * @return the contact url link
     */
    public String getContactURLLink() {
        return getStringValue(PropertyName.CONTACT_URL_LINK);
    }

    public ConfirmationLevel getConfirmationLevel() {
        ConfirmationLevel cl = (ConfirmationLevel) getEnumValue(
                PropertyName.CLIENT_CONFIMATION_LEVEL, ConfirmationLevel.class);
        if (cl == null) {
            cl = ConfirmationLevel.MEDIUM;
        }

        return cl;
    }

    public CoreProperty getCoreProperty(PropertyName utilityName) {
        return properties.get(utilityName);
    }

    private CoreProperty getCoreProperty(String propertyName) {

        return getCoreProperty(PropertyName.valueFromPropertyName(propertyName));
    }

    /**
     * Gets the firelog.
     * 
     * @return the firelog
     */
    public String getFirelog() {
        return getStringValue(PropertyName.FIRELOG);
    }

    /**
     * Gets the log categories.
     * 
     * @return the log categories
     */
    public String[] getLogCategories() {
        return getStringArray(PropertyName.LOG_CATEGORIES);
    }

    /**
     * Gets the logo urls.
     * 
     * @return the logo urls
     */
    public String[] getLogoUrls() {
        return getStringArray(PropertyName.LOGO_URLS);
    }

    /**
     * Gets the offline warning threshold m.
     * 
     * @return the offline warning threshold m
     */
    public long getOfflineWarningThresholdM() {
        return getDoubleValue(PropertyName.OFFLINE_WARNING_THRESHOLD_M)
                .longValue();
    }
    
    public String getResponseHost() {
    	return getStringValue(PropertyName.RESPONSE_HOST);
    }

    /**
     * Gets the string from the propertyName or returns the default value if the
     * property is not set.
     * 
     * @param propertyName
     *            an enumerated value identifying the desired property.
     * @param defaultValue
     *            whatever string is to be the default if the property is not
     *            set.
     * @return the String value of the property or the default value if the
     *         property is not set.
     */
    public String getStringValue(PropertyName propertyName, String defaultValue) {
        String result = getStringValue(propertyName);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * helper method to get the string out of a core property.
     * 
     * @param propertyName
     *            property name
     * @return the value or null if the property is not found
     */
    public String getStringValue(PropertyName propertyName) {

        CoreProperty property = getCoreProperty(propertyName);
        String result;
        if (property == null) {
            result = null;
        } else {
            result = property.getStringValue();
        }
        return result;
    }

    public String getTextValue(PropertyName propertyName, String defaultValue) {

        CoreProperty property = getCoreProperty(propertyName);
        String result;
        if (property == null) {
            result = defaultValue;
        } else {
            result = property.getTextValue();
        }
        return result;
    }
    
	private Boolean getBooleanValue(PropertyName propertyName) {
		CoreProperty property = getCoreProperty(propertyName);
		Boolean result;
		if (property == null) {
			result = null;
		} else {
			result = property.isBooleanValue();
		}
		return result;
	}    
    
	private Boolean getBooleanValue(PropertyName propertyName, boolean defaultValue) {
		CoreProperty property = getCoreProperty(propertyName);
		Boolean result;
		if (property == null) {
			result = new Boolean(defaultValue);
		} else {
			result = property.isBooleanValue();
		}
		return result;
	}    

    private String[] getStringArray(PropertyName propertyName) {
        CoreProperty property = getCoreProperty(propertyName);
        String[] result;
        if (property == null) {
            result = null;
        } else {
            result = property.getStringArray();
        }
        return result;
    }

    private Double getDoubleValue(PropertyName propertyName) {

        CoreProperty property = getCoreProperty(propertyName);
        Double result;
        if (property == null) {
            result = null;
        } else {
            result = property.getDoubleValue();
        }
        return result;

    }
    
    private Integer getIntegerValue(PropertyName propertyName) {

        CoreProperty property = getCoreProperty(propertyName);
        Integer result;
        if (property == null) {
            result = null;
        } else {
            result = Integer.valueOf(property.getStringValue());
        }
        return result;

    }
    
    
    private Enum getEnumValue(PropertyName propertyName, Class enumType) {
        String tmp = getStringValue(propertyName);
        if (tmp == null) {
            return null;
        } else {
            Enum resEnum = null;

            try {
                resEnum = Enum.valueOf(enumType, tmp);
            } catch (IllegalArgumentException iaex) {
                // bad propertyName. return null
            }

            return resEnum;
        }
    }

    public Double getOnSeasonStartMonth() {
        return getDoubleValue(PropertyName.ON_SEASON_START_MONTH);
    }

    public Double getOnSeasonStartDay() {
        return getDoubleValue(PropertyName.ON_SEASON_START_DAY);
    }

    public Double getOffSeasonStartMonth() {
        return getDoubleValue(PropertyName.OFF_SEASON_START_MONTH);
    }

    public Double getOffSeasonStartDay() {
        return getDoubleValue(PropertyName.OFF_SEASON_START_DAY);
    }
    
    public String getTelemetryDisclaimer() {
        return getStringValue(PropertyName.TELEMETRY_DISCLAIMER);
    }

    public void setTelemetryDisclaimer(String telemetryDisclaimer) {
        setStringValue(PropertyName.TELEMETRY_DISCLAIMER, telemetryDisclaimer);
    }

    /**
     * Gets the utility display name.
     * 
     * @return the utility display name
     */
    public String getUtilityDisplayName() {
        return getStringValue(PropertyName.UTILITY_DISPLAY_NAME);
    }

    /**
     * Gets the utility name.
     * 
     * @return the utility name
     */
    public String getUtilityName() {
        return getStringValue(PropertyName.UTILITY_NAME);
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return getStringValue(PropertyName.VERSION);
    }    

    public String getCopyright() {
        return getStringValue(PropertyName.COPYRIGHT);
    }
    public String getLogo() {
        return getStringValue(PropertyName.LOGO);
    }
    
    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    /**
     * Sets the contact e mail.
     * 
     * @param contactEMail
     *            the new contact e mail
     */
    public void setContactEMail(String contactEMail) {
        setStringValue(PropertyName.CONTACT_EMAIL, contactEMail);
    }

    /**
     * Sets the contact phone.
     * 
     * @param contactPhone
     *            the new contact phone
     */
    public void setContactPhone(String contactPhone) {
        setStringValue(PropertyName.CONTACT_PHONE, contactPhone);
    }

    /**
     * Sets the contact url display name.
     * 
     * @param contactURLDisplayName
     *            the new contact url display name
     */
    public void setContactURLDisplayName(String contactURLDisplayName) {
        setStringValue(PropertyName.CONTACT_URL_DISPLAY_NAME,
                contactURLDisplayName);
    }

    /**
     * Sets the contact url link.
     * 
     * @param contactURLLink
     *            the new contact url link
     */
    public void setContactURLLink(String contactURLLink) {
        setStringValue(PropertyName.CONTACT_URL_LINK, contactURLLink);
    }

    public void setCoreProperty(CoreProperty coreProperty) {
        properties.put(PropertyName.valueFromPropertyName(coreProperty
                .getPropertyName()), coreProperty);
    }

    /**
     * Sets the firelog.
     * 
     * @param firelog
     *            the new firelog
     */
    public void setFirelog(String firelog) {
        setStringValue(PropertyName.FIRELOG, firelog);
    }

    /**
     * Sets the log categories.
     * 
     * @param logCategories
     *            the new log categories
     */
    public void setLogCategories(String[] logCategories) {
        setStringArray(PropertyName.LOG_CATEGORIES, logCategories);
    }

    /**
     * Sets the logo urls.
     * 
     * @param logoUrls
     *            the new logo urls
     */
    public void setLogoUrls(String[] logoUrls) {
        setStringArray(PropertyName.LOGO_URLS, logoUrls);
    }

    /**
     * Sets the offline warning threshold m.
     * 
     * @param offlineWarningThresholdM
     *            the new offline warning threshold m
     */
    public void setOfflineWarningThresholdM(long offlineWarningThresholdM) {
        setDoubleValue(PropertyName.OFFLINE_WARNING_THRESHOLD_M,
                (double) offlineWarningThresholdM);
    }

    /**
     * Sets the utility display name.
     * 
     * @param utilityDisplayName
     *            the new utility display name
     */
    public void setUtilityDisplayName(String utilityDisplayName) {
        setStringValue(PropertyName.UTILITY_DISPLAY_NAME, utilityDisplayName);
    }

    /**
     * Sets the utility name.
     * 
     * @param utilityName
     *            the new utility name
     */
    public void setUtilityName(String utilityName) {
        setStringValue(PropertyName.UTILITY_NAME, utilityName);

    }

    /**
     * Sets the version.
     * 
     * @param version
     *            the new version
     */
    public void setVersion(String version) {
        setStringValue(PropertyName.VERSION, version);
    }
    
    public void setCopyright(String copyright) {
        setStringValue(PropertyName.COPYRIGHT, copyright);
    }
    public void setLogo(String logo) {
        setStringValue(PropertyName.LOGO, logo);
    }

    public String getItronWSHostname() {
        return getStringValue(PropertyName.ITRON_WS_HOSTNAME);
    }
    
    // ======================== Internal Worker Methods =============================
    private void setStringValue(PropertyName propertyName, String propertyValue) {
        CoreProperty property = new CoreProperty(
                propertyName.getPropertyName(), propertyValue);
        setCoreProperty(property);
    }

    private void setStringArray(PropertyName propertyName, String[] array) {
        // just put the property in there regardless if it exists or not.
        CoreProperty property = new CoreProperty(
                propertyName.getPropertyName(), array);
        setCoreProperty(property);

    }

    private void setDoubleValue(PropertyName propertyName, Double propertyValue) {
        // just put the property in there regardless if it exists or not.
        CoreProperty property = new CoreProperty(
                propertyName.getPropertyName(), propertyValue);
        setCoreProperty(property);
    }

    public Set<CoreProperty> getAllCoreProperties() {

        Set<CoreProperty> values = new HashSet<CoreProperty>(
                properties.values());

        return Collections.unmodifiableSet(values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return properties.values().toString();
    }

    public String getLoginMessage() {
        return getStringValue(PropertyName.LOGIN_MESSAGE);
    }

    public void setLoginMessage(String loginMessage) {
        setStringValue(PropertyName.LOGIN_MESSAGE, loginMessage);
    }

    public static enum PropertyName {
        CONTACT_URL_DISPLAY_NAME("contactURLDisplayName"), 
        CONTACT_URL_LINK("contactURLLink"), 
        CONTACT_PHONE("contactPhone"), 
        CONTACT_EMAIL("contactEMail"), 
        LOGO_URLS("logoUrls"), 
        SERVER_HOST("serverHost"), 
        MAP_KEY("mapKey"), 
        FIRELOG("firelog"), 
        OFFLINE_WARNING_THRESHOLD_M("offlineWarningThresholdM"), 
        UTILITY_NAME("utilityName"), 
        UTILITY_DISPLAY_NAME("utilityDisplayName"), 
        VERSION("version"), 
        LOG_CATEGORIES("logCategories"), 
        ABOUT_INFORMATION("aboutInformation"), 
        LOGIN_MESSAGE("loginMessage"), 
        ON_SEASON_START_MONTH("onSeasonStartMonth"), 
        ON_SEASON_START_DAY("onSeasonStartDay"), 
        OFF_SEASON_START_MONTH("offSeasonStartMonth"), 
        OFF_SEASON_START_DAY("offSeasonStartDay"), 
        CLIENT_CONFIMATION_LEVEL("clientConfirmationLevel"), 
        ITRON_WS_HOSTNAME("itronWSHostName"), 
        EMAIL_CONTENT_TYPE("emailContentType"), 
        RULE_CPP_PRICE_DEFAULT_PRICE("rule.cppPrice.default.price"), 
        RULE_CPP_DEFAULT_MODE("rule.cpp.default.mode"), 
        BASELINE_MODEL("baseline.model"),
        RESPONSE_HOST("responseHost"),
        BASELINE_MISSINGDATA_THRESHOLD("baseline.missingDataThreshold"),
        SIMPLE_DASHBOARD_REFRESHINTERVAL("simple.dashboard.refreshinterval"),
        SIMPLE_DASHBOARD_USAGE("simple.dashboard.usage"),
        SIMPLE_DASHBOARD_LINK1("simple.dashboard.link1"),
        SIMPLE_DASHBOARD_LINK2("simple.dashboard.link2"),
        SIMPLE_DASHBOARD_LINK1NAME("simple.dashboard.link1name"),
        SIMPLE_DASHBOARD_LINK2NAME("simple.dashboard.link2name"),
        SIMPLE_ACTIVE_MESSAGE("simple.dashboard.active.message"),
        SIMPLE_NOEVENT_MESSAGE("simple.dashboard.noevent.message"),
        DISABLE_USAGE_DATA_INTERPOLATION("disableUsageDataInterpolation"),
        HOLIDAYS("holidays"),
        EXCLUDED_PROGRAMS_FOR_BASELINE("excludedProgramsForBaseline"),
        EXCLUDED_PROGRAMS_FOR_EVENTLINE("excludedProgramsForEventLine"),
        SIMPLE_PENDING_MESSAGE("simple.dashboard.pending.message"),
        INTERPOLATE_SHIFT_SIZE("interpolate.shiftsize"),
        INTERPOLATE_WINDOW_SIZE("interpolate.windowsize"),
        INTERPOLATE_RUN_INTERVAL("interpolate.runinterval"),
        BAELINE_CALCULATE_TIMEPOINT("baseline.calculate.timepoint"),
        UNDELIVERED_SEND_TIMEPOINT("undelivered.send.timepoint"),
        SUPPRESS_ALL_EMAILS("SuppressAllEmails"),
        ENTIREDAYSHEDLINE_NAME("entireDayShedLine.name"),
        EVENTPERIODSHEDLINE_NAME("eventPeriodShedLine.name"),
        ENTIREDAYSHEDLINE_COLOR("entireDayShedLine.color"),
        EVENTPERIODSHEDLINE_COLOR("eventPeriodShedLine.color"),
        TELEMETRY_DISCLAIMER("telemetryDisclimar"),
        CLIENT_DEBUG_ENABLE("client.debug.enable"),
        GMAP_DRAWINGHOLE_ENABLE("gmap.drawinghole.enable"),
        GMAP_LOAD_URL("gmap.load.url"),
        DR_CACHE_INTERVAL("dr.cache.interval"),
        MAX_USAGE_DATA("feature.maxUsageData"),
        COPYRIGHT("copyright"),
        WEATHER_REFRESHINTERVAL("weather.refreshinterval"),
        WEATHER_UK_APIKEY("weather.uk.apikey"),
        WEATHER_UK_URL_FORECAST("weather.uk.url.forecast"),
        WEATHER_UK_URL_OBSERVATION("weather.uk.url.observation"),
        CLIENT_OFFLINE_NOTIFICATION_SUMMER_THRESHOLD("feature.clientOfflineNoficicationSummerThreshold"),
        CLIENT_OFFLINE_NOTIFICATION_UN_SUMMER_THRESHOLD("feature.clientOfflineNoficicationUnSummerThreshold"),
        FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_INTERVAL("feature.clientTestEmailConsolidationInterval"),
        HELP_DESK_EMAIL_ADDRESS("helpDeskEmailAddress"),
        PROGRAM_NAME_FOR_GE("SSEPowerOnEvtCreationProgramName"),
        FEATURE_GEINTERFACEENABLED("feature.geInterfaceEnabled"),
        LOGO("logo"),
        NEWS_URL("feature.news.url"),
        APPSTORE_URL("appstore.url"),
        PLAYSTORE_URL("playstore.url"),
        MOBILEDIV_HIDE("mobileDR"),
        OPENADR_GRANULARITY("openadr.granularity"),
        OPENADR_REPORTBACK_DURATION("openadr.reportbackduration"),
        OPENADR_STATUS_GRANULARITY_REPORTBACKDURATION("openadr.statusgranularityreportbackduration"),
        OPENADR_DISABLE_REPORT_REGISTRATION("openadr.disablereportregistration");
        
        private final String propertyName;

        /**
         * Constructor required to support the existng propertyNames as they
         * have existed for backwards compatibility and existing naming
         * standard.
         * 
         * @param propertyName
         *            property name
         */
        private PropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public String toString() {
            return this.propertyName;
        }

        public static PropertyName valueFromPropertyName(
                String propertyNameValue) {
            // This implementation seems overkill, but makes no assumption about
            // the names supporting the existing property names.
            PropertyName[] values = PropertyName.values();
            PropertyName foundValue = null;
            for (int i = 0; i < values.length && foundValue == null; i++) {
                PropertyName propertyName = values[i];
                if (propertyName.propertyName
                        .equalsIgnoreCase(propertyNameValue)) {
                    foundValue = propertyName;
                }

            }
            return foundValue;
        }

        public String getPropertyName() {
            return propertyName;
        }

    }

    /**
     * Utility method for working with on/off season properties
     * 
     * @return
     */
    public boolean isOnSeason() {
    	Calendar now = new GregorianCalendar();
        Calendar onSeasonCal = new GregorianCalendar();
        onSeasonCal.set(Calendar.MONTH,
                getOnSeasonStartMonth().intValue());
        onSeasonCal.set(Calendar.DAY_OF_MONTH, getOnSeasonStartDay()
                .intValue());

        Calendar offSeasonCal = new GregorianCalendar();
        offSeasonCal.set(Calendar.MONTH, getOffSeasonStartMonth()
                .intValue());
        offSeasonCal.set(Calendar.DAY_OF_MONTH, getOffSeasonStartDay()
                .intValue());

        boolean onSeason;

        if (offSeasonCal.after(onSeasonCal)) {
            onSeason = onSeasonCal.before(now) && offSeasonCal.after(now);
        } else {
            onSeason = !(offSeasonCal.before(now) && onSeasonCal.after(now));
        }

        return onSeason;
    }

    public String getEmailContentType() {
        return getStringValue(PropertyName.EMAIL_CONTENT_TYPE);
    }
    public String getProgramName4GE() {
        return getStringValue(PropertyName.PROGRAM_NAME_FOR_GE);
    }
    public String getRuleCPPDefaultMode() {
        return getStringValue(PropertyName.RULE_CPP_DEFAULT_MODE);
    }

    public Double getRuleCPPPriceDefaultPrice() {
        return getDoubleValue(PropertyName.RULE_CPP_PRICE_DEFAULT_PRICE);
    }

    public String getBaselineModel() {
        if (getStringValue(PropertyName.BASELINE_MODEL) == null) {
            return "ThreeTen";
        }
        return getStringValue(PropertyName.BASELINE_MODEL);
    }

    public Double getMissingDataThreshold() {
        if (getDoubleValue(PropertyName.BASELINE_MISSINGDATA_THRESHOLD) == null) {
            return 0.3;
        }
        return getDoubleValue(PropertyName.BASELINE_MISSINGDATA_THRESHOLD);
    }
    
    public double getSimpleDashboardRefreshinterval() {
        if (getDoubleValue(PropertyName.SIMPLE_DASHBOARD_REFRESHINTERVAL) == null) {
            return 0.0;
        }
        return getDoubleValue(PropertyName.SIMPLE_DASHBOARD_REFRESHINTERVAL);
    }
    
    public String getHolidays() {
    	 return getStringValue(PropertyName.HOLIDAYS);
    }
    
    public String getExcludedProgramsForBaseline() {
    	return getStringValue(PropertyName.EXCLUDED_PROGRAMS_FOR_BASELINE);
    }
    
    public String getExcludedProgramsForEventLine() {
    	return getStringValue(PropertyName.EXCLUDED_PROGRAMS_FOR_EVENTLINE);
    }
    
    public Boolean isSuppressAllEmails() {
		return this.getBooleanValue(PropertyName.SUPPRESS_ALL_EMAILS, true);
	}   
    
    // minutes
    public long getInterpolateShiftsize() {
        if (getDoubleValue(PropertyName.INTERPOLATE_SHIFT_SIZE) == null) {
            return 60;
        }
        return getDoubleValue(PropertyName.INTERPOLATE_SHIFT_SIZE).longValue();
    }
    //minutes
    public long getInterpolateWindowsize() {
    	 if (getDoubleValue(PropertyName.INTERPOLATE_WINDOW_SIZE) == null) {
             return 60*24;
         }
         return getDoubleValue(PropertyName.INTERPOLATE_WINDOW_SIZE).longValue();
    }
    //minutes
    public long getInterpolateInterval() {
    	 if (getDoubleValue(PropertyName.INTERPOLATE_RUN_INTERVAL) == null) {
             return 60 * 60 * 1000;//1 h
         }
         return getDoubleValue(PropertyName.INTERPOLATE_RUN_INTERVAL).longValue()*60*1000;
    }
    
    public String getBaselineCalculateTime() {
    	if(getStringValue(PropertyName.BAELINE_CALCULATE_TIMEPOINT)==null){
    		return "00:00:00";
    	}
    	
    	return getStringValue(PropertyName.BAELINE_CALCULATE_TIMEPOINT);
    }
    
    public String getUndeliveredEmailSendTime() {
    	if(getStringValue(PropertyName.UNDELIVERED_SEND_TIMEPOINT)==null){
    		return "07:00:00";
    	}
    	
    	return getStringValue(PropertyName.UNDELIVERED_SEND_TIMEPOINT);
    }
    
    public String getEntireDayShedLineName() {
    	if(getStringValue(PropertyName.ENTIREDAYSHEDLINE_NAME)==null){
    		return "BASELINE-USAGELINE";
    	}
    	
    	return getStringValue(PropertyName.ENTIREDAYSHEDLINE_NAME);
    }
    
    public String getEventPeriodShedLineName() {
    	if(getStringValue(PropertyName.EVENTPERIODSHEDLINE_NAME)==null){
    		return "SHED";
    	}
    	
    	return getStringValue(PropertyName.EVENTPERIODSHEDLINE_NAME);
    }
    
    public String getEntireDayShedLineColor() {
    	if(getStringValue(PropertyName.ENTIREDAYSHEDLINE_COLOR)==null){
    		return "0xFFFF00";
    	}
    	
    	return getStringValue(PropertyName.ENTIREDAYSHEDLINE_COLOR);
    }
    
    public String getEventPeriodShedLineColor() {
    	if(getStringValue(PropertyName.EVENTPERIODSHEDLINE_COLOR)==null){
    		return "0x009900";
    	}
    	
    	return getStringValue(PropertyName.EVENTPERIODSHEDLINE_COLOR);
    }
    public Double getMaxUsageData() {
	 	 return getDoubleValue(PropertyName.MAX_USAGE_DATA);
	}
    
    public String getClientDebugEnable() {
    	if(getStringValue(PropertyName.CLIENT_DEBUG_ENABLE)==null){
    		return "0";
    	}
    	
    	return getStringValue(PropertyName.CLIENT_DEBUG_ENABLE);
    }
    
    public String getGmapURL() {
    	if(getStringValue(PropertyName.GMAP_LOAD_URL)==null){
    		return "http://maps.google.com/maps/api/js";
    	}
    	
    	return getStringValue(PropertyName.GMAP_LOAD_URL);
    }
    
    public String getGmapDrawingholeEnable() {
    	if(getStringValue(PropertyName.GMAP_DRAWINGHOLE_ENABLE)==null){
    		return "0";
    	}
    	
    	return getStringValue(PropertyName.GMAP_DRAWINGHOLE_ENABLE);
    }
    
    public String getDrCacheInterval() {
    	if(getStringValue(PropertyName.DR_CACHE_INTERVAL)==null){
    		return "10000"; // 10 seconds as default
    	}
    	return getStringValue(PropertyName.DR_CACHE_INTERVAL);
    }
    
    public double getWeatherRefreshinterval() {
        if (getDoubleValue(PropertyName.WEATHER_REFRESHINTERVAL) == null) {
            return 10*60*1000*1.0;// default 10 minutes
        }
        return getDoubleValue(PropertyName.WEATHER_REFRESHINTERVAL)*60*1000;
    }

    public String getWeatherUkApiKey() {
   	 return getStringValue(PropertyName.WEATHER_UK_APIKEY);
    }
    public String getWeatherUkForecastURL() {
      	 return getStringValue(PropertyName.WEATHER_UK_URL_FORECAST);
    }
    public String getWeatherUkObservationURL() {
      	 return getStringValue(PropertyName.WEATHER_UK_URL_OBSERVATION);
    }
    public double getClientTestEmailConsolidationInterval() {
    	if(getDoubleValue(PropertyName.FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_INTERVAL)==null){
    		return 1;
    	}
    	return getDoubleValue(PropertyName.FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_INTERVAL);
  }
    public double getClientOfflineNotificationSummerThreshold() {
    	if(getDoubleValue(PropertyName.CLIENT_OFFLINE_NOTIFICATION_SUMMER_THRESHOLD)==null){
    		return 24;
    	}
    	return getDoubleValue(PropertyName.CLIENT_OFFLINE_NOTIFICATION_SUMMER_THRESHOLD);
  }
    public double getClientOfflineNotificationUnSummerThreshold() {
    	if(getDoubleValue(PropertyName.CLIENT_OFFLINE_NOTIFICATION_UN_SUMMER_THRESHOLD)==null){
    		return 168;
    	}
    	return getDoubleValue(PropertyName.CLIENT_OFFLINE_NOTIFICATION_UN_SUMMER_THRESHOLD);
  }
    public String getHelpDeskEmailAddress() {
    	if(getStringValue(PropertyName.HELP_DESK_EMAIL_ADDRESS)==null){
    		return "AUTODR@sce.com"; 
    	}
    	return getStringValue(PropertyName.HELP_DESK_EMAIL_ADDRESS);
    }
    
    public Boolean isGeInterfaceEnabled() {
		return this.getBooleanValue(PropertyName.FEATURE_GEINTERFACEENABLED, false);
	}  
    
	 public String getAppStoreURL() {
	   	return getStringValue(PropertyName.APPSTORE_URL);
	    }
	 
	 public String getGooglePlayURL() {
		   	return getStringValue(PropertyName.PLAYSTORE_URL);
		    }
	 
	 public Boolean getMobileBadgeHide() {
		   	String hideDiv = getStringValue(PropertyName.MOBILEDIV_HIDE);
		  	if(hideDiv.equalsIgnoreCase("true"))
		   		return true;
		   	else
		   		return false;
		 
	 }
	 
	 public Double getOpenADRGranularity() {		 
		 return getDoubleValue(PropertyName.OPENADR_GRANULARITY);
	 }
	 
	 public Double getOpenADRReportBackDuration(){
		 return getDoubleValue(PropertyName.OPENADR_REPORTBACK_DURATION);
	 }
	 
	 public Double getOpenADRStatusGranularityReportbackduration() {		 
		 return getDoubleValue(PropertyName. OPENADR_STATUS_GRANULARITY_REPORTBACKDURATION);
	 }
	 
	 public Boolean getOpenADRDisableReportRegistration() {
		   	String sendCreateReport = getStringValue(PropertyName.OPENADR_DISABLE_REPORT_REGISTRATION);
		  	if(sendCreateReport.equalsIgnoreCase("true"))
		   		return true;
		   	else
		   		return false;
		 
	 }
}
