package com.akuacom.pss2.system.property;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.akuacom.annotations.NoTrace;

/**
 * 
 * Retrieve CoreProperties that toggle whether features are enabled for the user.
 * Use set CoreProperties to persist.
 *
 */
@NoTrace
public class PSS2Features implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** The storage of all properties available in this class. */
	private Map<FeatureName, CoreProperty> properties = new HashMap<FeatureName, CoreProperty>();
	
	public PSS2Features(Collection<CoreProperty> properties) {
		for (CoreProperty coreProperty : properties) {
			if (FeatureName.valueFromPropertyName(coreProperty.getPropertyName()) != null) 
				setCoreProperty(coreProperty);
		}
	}
	
	private void setCoreProperty(CoreProperty coreProperty) {
		this.properties.put(FeatureName.valueFromPropertyName(coreProperty
				.getPropertyName()), coreProperty);
	}
	
	public Boolean isFeatureFeedbackEnabled() {
		return this.getBooleanValue(FeatureName.FEEDBACK);
	}
	
	public Boolean isFeatureLocationEnabled() {
		return this.getBooleanValue(FeatureName.LOCATION);
	}
	
	public Boolean isFeatureShedInfoEnabled() {
		return this.getBooleanValue(FeatureName.SHED_INFO);
	}
	
	public Boolean isFeatureRulesEnabled() {
		return this.getBooleanValue(FeatureName.RULES);
	}
	
	public Boolean isFeatureExtendedNotificationEnabled() {
		return this.getBooleanValue(FeatureName.EXTENDED_NOTIFICATION);
	}
	
	public String getFeatureFacdashDefaultPage() {
		return getCoreProperty(FeatureName.FACDASH_DEFAULT_PAGE).getStringValue();
	}

    public Boolean isFeaturePeakChoiceProgramEnabled() {
        return this.getBooleanValue(FeatureName.PROGRAM_PEAK_CHOICE);
    }

    public Boolean isFeatureOpdashEnabled() {
        if(this.getBooleanValue(FeatureName.OPDASH) == null)
        {
            return false;
        }
        return this.getBooleanValue(FeatureName.OPDASH);
    }

    public Boolean isFeatureUseTempDataEntryEnabled() {
        if(this.getBooleanValue(FeatureName.USETEMPDATAENTRYTBL) == null)
        {            
            return false;
        }
        return this.getBooleanValue(FeatureName.USETEMPDATAENTRYTBL);
    }

    public Boolean isFeatureUseCacheStoreEnabled() {
        if(this.getBooleanValue(FeatureName.USECACHESTORE) == null)
        {
            return false;
        }
        return this.getBooleanValue(FeatureName.USECACHESTORE);
    }

    public Boolean isSubAccountsEnabled() {
		return this.getBooleanValue(FeatureName.SUB_ACCOUNTS);
	}
	
	public Boolean isNewsEnabled() {
		return this.getBooleanValue(FeatureName.NEWS);
	}
	
	public boolean isUsageEnabled() {
		Boolean res = this.getBooleanValue(FeatureName.USAGE);
		if (res == null) {
			return false;
		} else {
			return res.booleanValue();
		}
	}
	
	public boolean isTempConfigEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_TEMP_CONFIG_ENABLE);
	}
	public boolean isReportAccountInfoEnabled() {
		return this.getBooleanValue(FeatureName.REPORT_ACCOUNT_INFO);
	}
	public boolean isReportClientInfoEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_CLIENT_INFO_ENABLE);
	}	
    public boolean isReportEMailEnabled() {
		return this.getBooleanValue(FeatureName.REPORT_EMAIL_LIST);
	}

    public boolean isCloneEnabledForOp() {
		return this.getBooleanValue(FeatureName.CLONE_ENABLED_FOR_OP);
	}

    public boolean isNonAutoClientEnabled() {
		return this.getBooleanValue(FeatureName.NON_AUTO_CLIENT_SUPPORTED);
	}

    public boolean isSpecialModeEnabled() {
        if(this.getBooleanValue(FeatureName.SPECIAL_MODE_SUPPORTED) == null)
        {
            return false;
        }
        return this.getBooleanValue(FeatureName.SPECIAL_MODE_SUPPORTED);
	}

    public boolean isProgramRuleEnabled() {
        if(this.getBooleanValue(FeatureName.PROGRAM_RULE_SUPPORTED) == null)
        {
            return false;
        }
        return this.getBooleanValue(FeatureName.PROGRAM_RULE_SUPPORTED);
	}

    public boolean isWeatherEnabled() {
		return this.getBooleanValue(FeatureName.WEATHER);
	}
	
	public boolean isCustumerTestEventEnabled(){
		return this.getBooleanValue(FeatureName.CUSTOMER_TEST_EVENT);
	}
	
	public boolean isSCEDBPEventAutoCreationEnabled(){
		return this.getBooleanValue(FeatureName.SCE_DBP_EVENT_AUTO_CREATION);
	}
	
	public CoreProperty getCoreProperty(FeatureName propName) {
		return this.properties.get(propName);
	}

	private CoreProperty getCoreProperty(String propertyName) {

		return getCoreProperty(FeatureName.valueFromPropertyName(propertyName));
	}
	
	public boolean isSimpleDashBoardOptoutEnabled(){
		return this.getBooleanValue(FeatureName.SIMPLE_DASHBOARD_OPTOUT);
	}
	
	public boolean isSimpleDashBoardWeatherEnabled(){
		return this.getBooleanValue(FeatureName.SIMPLE_DASHBOARD_ENABLEWEATHER);
	}
	
	public boolean isSimpleDashBoardRssFeedEnabled(){
		return this.getBooleanValue(FeatureName.SIMPLE_DASHBOARD_RSSFEED);
	}
	
	public boolean isSimpleDashBoardAkualogoEnabled(){
		return this.getBooleanValue(FeatureName.SIMPLE_DASHBOARD_AKUALOGO);
	}
	
	public String getSimpleDashBoardDefaultZipcode() {
		return getCoreProperty(FeatureName.SIMPLE_DASHBOARD_DEFAULT_ZIPCODE).getStringValue();
	}
	
	public boolean isSimpleDashBoardLinkEnabled(){
		return this.getBooleanValue(FeatureName.SIMPLE_DASHBOARD_LINKENABLE);
	}


	public boolean isIrrClientPushEnabled(){
		return this.getBooleanValue(FeatureName.IIR_CLIENT_PUSH);
	}

    public boolean isClientOptOut(){
		return this.getBooleanValue(FeatureName.CLIENT_OPT_OUT);
	}
    
    public Boolean isClientsAutoEnrollInProgramEnabled() {
		return this.getBooleanValue(FeatureName.CLIENTS_AUTO_ENROLL_IN_PROGRAM, false);
	} 

    public Boolean isAggregationEnabled() {
		return this.getBooleanValue(FeatureName.ENABLE_AGGREGATION, false);
	} 
    
    public Boolean isScorecardEnabled() {
		return this.getBooleanValue(FeatureName.ENABLE_SCORECARD, false);
	} 
    public Boolean isInterruptibleProgramsEnabled() {
		return this.getBooleanValue(FeatureName.ENABLE_INTERRUPTIBLE_PROGRAMS, false);
	} 
    public Boolean isFeatureStorebackEnabled() {
    	if(this.getBooleanValue(FeatureName.STOREBACKENABLE) == null)
    	{
    		return false;
    	}
    	return this.getBooleanValue(FeatureName.STOREBACKENABLE); 
    }
	
	public Boolean isParticipantNotesEnabled() {
		return this.getBooleanValue(FeatureName.PARTICIPANT_NOTES);
	}
	
	public Boolean isParticipantInfoEnabled() {
		return this.getBooleanValue(FeatureName.PARTICIPANT_INFO);
	}
	
	public Boolean isParticipantsUpload() {
		return this.getBooleanValue(FeatureName.PARTICIPANTS_UPLOAD);
	}
	public Boolean isEnableValidateUsageData() {
		return this.getBooleanValue(FeatureName.ENABLE_VALIDATE_USAGE_DATA);
	}
	public Boolean isDemandLimitingEnabled() {
		return this.getBooleanValue(FeatureName.DEMAND_LIMITING);
	}
	
	public Boolean isDemandLimitingNotificationsEnabled() {
		return this.getBooleanValue(FeatureName.DEMAND_LIMITING_NOTIFICATIONS);
	}
	
	public Boolean isDemandLimitingMockMeterEnabled() {
		return this.getBooleanValue(FeatureName.DEMAND_LIMITING_MOCK_METER);
	}

	public Boolean isEnableMultipleRTPEvents(){
		return this.getBooleanValue(FeatureName.FEATURE_MULTIPLE_RTP_EVENTS_ENABLE);
	}
	
	public String getDateFormat() {
		return getCoreProperty(FeatureName.DATE_FORMAT).getStringValue();
	}

	public Boolean isTime24Hours() {
		return getBooleanValue(FeatureName.TIME_24HOUR_FORMAT);
	}
	public Boolean isSCERTPTempUpdate() {
		return getBooleanValue(FeatureName.SCERTP_TEMP_UPDATE);
	}
	
	public Boolean isEnableDataService() {
		return getBooleanValue(FeatureName.ENABLE_DATASERVICE);
	}
	
	public Boolean isEnableProgramMatrixCheck() {
		return getBooleanValue(FeatureName.PROGRAM_MATRIX_CHECK);
	}
	
	public Double getClientTimeoutIncrement() {
		CoreProperty property = getCoreProperty(FeatureName.USAGE_CLIENT_TIMEOUT_INCREMENT);
		Double result;
		if (property == null) {
			result = null;
		} else {
			result = property.getDoubleValue();
		}
		return result;
	}

	public Double getClientTimeout() {
		CoreProperty property = getCoreProperty(FeatureName.USAGE_CLIENT_TIMEOUT);
		Double result;
		if (property == null) {
			result = null;
		} else {
			result = property.getDoubleValue();
		}
		return result;
	}

	public Double getDefaultDemoDuration() {
		CoreProperty property = getCoreProperty(FeatureName.FEATURE_DEFAULT_DEMO_DURATION);
		Double result;
		if (property == null) {
			result = null;
		} else {
			result = property.getDoubleValue();
		}
		return result;
	}

	public Boolean isEnableEntireDayShedLine(){
		return getBooleanValue(FeatureName.FEATURE_ENTIREDAYSHEDLINE_ENABLE);
	}
	
	public Boolean isEnableEventPeriodShedLine(){
		return getBooleanValue(FeatureName.FEATURE_EVENTPERIODSHEDLINE_ENABLE);
	}
	
	public Boolean isProductionServer(){
		return (getBooleanValue(FeatureName.FEATURE_IS_PRODUCTION_SERVER)==null?Boolean.FALSE:getBooleanValue(FeatureName.FEATURE_IS_PRODUCTION_SERVER));
	}
	
	public Boolean isFeatureTelemetryTomorrowEnabled() {
        if(this.getBooleanValue(FeatureName.FEATURE_TELEMETRY_TOMORROW_ENABLE) == null)
        {            
            return false;
        }
        return this.getBooleanValue(FeatureName.FEATURE_TELEMETRY_TOMORROW_ENABLE);
    }
	public Boolean isFeatureCustomerInfoUploadEnabled() {
        if(this.getBooleanValue(FeatureName.FEATURE_CUSTOMER_INFO_UPLOAD_ENABLE) == null)
        {            
            return false;
        }
        return this.getBooleanValue(FeatureName.FEATURE_CUSTOMER_INFO_UPLOAD_ENABLE);
    }
	public Boolean isShedStrategyExportEnabled() {
        if(this.getBooleanValue(FeatureName.FEATURE_SHED_STRATEGY_EXPORT_ENABLE) == null)
        {            
            return false;
        }
        return this.getBooleanValue(FeatureName.FEATURE_SHED_STRATEGY_EXPORT_ENABLE);
    }
	public Boolean isFeatureAustraliaPriceEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_AUSTRALIA_PRICE_SERVICE) == null) {
			return false;
		}
		return this
				.getBooleanValue(FeatureName.FEATURE_AUSTRALIA_PRICE_SERVICE);
	}
	public Boolean isDemoEventDefaultTimeEnabled() {
		if (this.getBooleanValue(FeatureName.DEMO_EVENT_DEFAULT_TIME_ENABLE) == null) {
			return false;
		}
		return this.getBooleanValue(FeatureName.DEMO_EVENT_DEFAULT_TIME_ENABLE);
	}
	public Boolean isSecondaryAccountEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_SECONDARY_ACCOUNT_ENABLED) == null) {
			return false;
		}
		return this.getBooleanValue(FeatureName.FEATURE_SECONDARY_ACCOUNT_ENABLED);
	}
	public Boolean isEnrollAllParticipantsEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_ENROLL_ALL_PARTICIPANTS) == null) {
			return false;
		}
		return this.getBooleanValue(FeatureName.FEATURE_ENROLL_ALL_PARTICIPANTS);
	}
	public String getAustraliaPriceLocation() {
		return getCoreProperty(FeatureName.FEATURE_AUSTRALIA_PRICE_LOCATION).getStringValue();
	}
	
	public int getAustraliaPriceMaxDelay() {
		return getCoreProperty(FeatureName.FEATURE_AUSTRALIA_PRICE_MAX_DELAY_MIN).getIntegerValue();
	}
	
	public Boolean isFeatureVaroliiNotificationEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_VAROLII_NOTIFICATION);
	}
	
	public Boolean isFeatureWarnConfirmEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_WARN_CONFIRM_ENALBED);
	}

	public Boolean isFeatureForcastEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_FORCASTENABLE);
	}

	public Boolean isFeatureEventOptIn() {
		return this.getBooleanValue(FeatureName.FEATURE_EVENT_OPT_IN);
	}

	public Boolean isEventOptoutEnabled() {
		return this.getBooleanValue(FeatureName.FEATURE_EVENTOPTOUTENABLED);
	}
    public Boolean isBIP2013Enabled() {
		return this.getBooleanValue(FeatureName.ENABLE_BIP2013, false);
	} 
    
    public Boolean isAggBatchUpdateEnabled() {
    	return true;
// 		return this.getBooleanValue(FeatureName.ENABLE_AGG_BATCH_UPDATE, false);
 	} 
    
    public Boolean isProgramAutoDispatch() {
 		return this.getBooleanValue(FeatureName.FEATURE_PROGRAM_AUTO_DISPATCH, false);
 	} 
    
	public Boolean isUseSecondaryUtilityNameForAPX() {
		return this.getBooleanValue(FeatureName.FEATURE_USE_SECONDARY_UTILITY_NAME_APX);
	}
    
	public Boolean isClientTestEmailConsolidationEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_ENABLED) == null) {
			return false;
		}else{
			return this.getBooleanValue(FeatureName.FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_ENABLED);	
		}
	}
	public Boolean isClientOfflineNotificationEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_CLIENT_OFFLINE_NOTIFICATION_ENABLE) == null) {
			return false;
		}else{
			return this.getBooleanValue(FeatureName.FEATURE_CLIENT_OFFLINE_NOTIFICATION_ENABLE);	
		}
	}
	public boolean isClientOfflineReportEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_CLIENT_OFFLINE_REPORT_ENABLE) == null) {
			return false;
		}else{
			return this.getBooleanValue(FeatureName.FEATURE_CLIENT_OFFLINE_REPORT_ENABLE);	
		}
	}
	public Boolean isTimerConfigEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_TIMER_CONFIG_ENABLE) == null) {
			return false;
		}else{
			return this.getBooleanValue(FeatureName.FEATURE_TIMER_CONFIG_ENABLE);	
		}
	}
	public boolean isCommunicationEmailEnabled() {
		if (this.getBooleanValue(FeatureName.FEATURE_ENABLE_COMMUNICATION_EMAIL) == null) {
			return false;
		}else{
			return this.getBooleanValue(FeatureName.FEATURE_ENABLE_COMMUNICATION_EMAIL);	
		}
	}
	public boolean isEnableUndeliveredEmail() {
        if(this.getBooleanValue(FeatureName.FEATURE_ENABLE_UNDELIVERED_EMAIL) == null)
        {            
            return false;
        }
        return this.getBooleanValue(FeatureName.FEATURE_ENABLE_UNDELIVERED_EMAIL);
    }
	public Boolean isOpenadrMultipleDataEnabled() {
		if (this.getBooleanValue(FeatureName.OPENADR_MULTIPLEUSAGEDATA_ENABLE) == null) {
			return false;
		}
		return this.getBooleanValue(FeatureName.OPENADR_MULTIPLEUSAGEDATA_ENABLE);
	}
	public int getOptOutTimeOut() {
		return getCoreProperty(FeatureName.OPTOUT_TIMEOUT).getIntegerValue();
	}
	public String getBaselineUsageEquivalentValue() {
		return getCoreProperty(FeatureName.OPENADR_BASELINE_USAGE).getStringValue();
	}
	public Boolean isApplicationIdEnabled() {
		CoreProperty property = getCoreProperty(FeatureName.DBP_APPLICATION_ID_ENABLED);
		String result;
		if (property == null) {
			result = null;
		} else {
			result = property.getStringValue();
			if(result.equalsIgnoreCase("PGE")){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {

		boolean result = false;
		if (obj != null && obj instanceof PSS2Features) {
			PSS2Features that = (PSS2Features) obj;
			result = this.properties.equals(that.properties);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		return this.properties.hashCode();
	}

	/**
	 * Similar to equals method, however, this verifies the values in the
	 * properties are the same per the rules of
	 * {@link CoreProperty#equalsValue(CoreProperty)}.
	 * 
	 * @param that the other
	 * @return true/false
	 */
	public boolean equalValues(PSS2Features that) {

		boolean isEqual = this.properties.size() == that.properties.size();
		// if the same size, maybe the same properties...let's check each until
		// failure.
		for (Iterator<CoreProperty> iterator = this.properties.values()
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

	private Boolean getBooleanValue(FeatureName propertyName) {
		CoreProperty property = getCoreProperty(propertyName);
		Boolean result;
		if (property == null) {
			result = null;
		} else {
			result = property.isBooleanValue();
		}
		return result;
	}
	
	private Boolean getBooleanValue(FeatureName propertyName, boolean defaultValue) {
		CoreProperty property = getCoreProperty(propertyName);
		Boolean result;
		if (property == null) {
			result = new Boolean(defaultValue);
		} else {
			result = property.isBooleanValue();
		}
		return result;
	}
	
	public static enum FeatureName {
        LOCATION("feature.location"),
        FEEDBACK("feature.feedback"),
        SHED_INFO("feature.shedInfo"),
        RULES("feature.rules"),
        EXTENDED_NOTIFICATION("feature.extendedNotification"),
        FACDASH_DEFAULT_PAGE("feature.facdashDefaultPage"),
        SUB_ACCOUNTS("feature.subAccounts"),
        NEWS("feature.news"),
        USAGE("feature.usage"),
        USAGE_CLIENT_TIMEOUT("feature.usageClientTimeOut"),
        USAGE_CLIENT_TIMEOUT_INCREMENT("feature.usageClientTimeOut.increment"),
        REPORT_ACCOUNT_INFO("feature.reportAccountInfo"),
        REPORT_EMAIL_LIST("feature.reportEMailList"),
        CLONE_ENABLED_FOR_OP("feature.cloneEnabledForOp"),
        NON_AUTO_CLIENT_SUPPORTED("feature.nonAutoClientSupported"),
        SPECIAL_MODE_SUPPORTED("feature.specialModeEnabled"),
        PROGRAM_RULE_SUPPORTED("feature.programRuleEnabled"),
        WEATHER("feature.weather"),
        OPDASH("feature.opdash"),
        USETEMPDATAENTRYTBL("feature.usageTempDataEntryTable"),
        USECACHESTORE("feature.useCacheStore"),
        STOREBACKENABLE("feature.storebackEnable"),
        PROGRAM_PEAK_CHOICE("feature.peakchoice"),
        CUSTOMER_TEST_EVENT("sce.participant.customer.test.event"),
        SCE_DBP_EVENT_AUTO_CREATION("feature.scedbpEventAutoCreation"),
        SIMPLE_DASHBOARD_OPTOUT("simple.dashboard.optout"),
        SIMPLE_DASHBOARD_RSSFEED("simple.dashboard.rssfeed"),
        SIMPLE_DASHBOARD_LINKENABLE("simple.dashboard.linkenable"),          
        SIMPLE_DASHBOARD_AKUALOGO("simple.dashboard.akualogo"),
        SIMPLE_DASHBOARD_DEFAULT_ZIPCODE("simple.dashboard.default.zipcode"),
        SIMPLE_DASHBOARD_ENABLEWEATHER("simple.dashboard.enableWeather"),
        CLIENTS_AUTO_ENROLL_IN_PROGRAM("enableClientsAutoEnrollInProgram"),
        ENABLE_AGGREGATION("enableAggregation"),
        ENABLE_SCORECARD("enableScorecard"),
        ENABLE_INTERRUPTIBLE_PROGRAMS("enableInterruptiblePrograms"),
        IIR_CLIENT_PUSH("iir.client.push"),
        CLIENT_OPT_OUT("feature.clientOptOut"),
        PARTICIPANT_NOTES("feature.participantNotes"),
        PARTICIPANT_INFO("feature.participantInfo"),
        PARTICIPANTS_UPLOAD("feature.participantsUpload"),
        DEMAND_LIMITING("feature.demandLimiting"),
        DEMAND_LIMITING_NOTIFICATIONS("feature.demandLimiting.Notifications"),
        DEMAND_LIMITING_MOCK_METER("feature.demandLimiting.MockMeter"),
        KWICKVIEW_ENABLED("feature.kwickview.enabled"),
        KWICKVIEW_POLLING_HOURS("feature.kwickview.polling.hours"),
        KWICKVIEW_PROGRAM_MAP("feature.kwickview.program_map"),
        KWICKVIEW_USERNAME("feature.kwickview.username"),
        KWICKVIEW_PASSWORD("feature.kwickview.password"),
        KWICKVIEW_URL("feature.kwickview.url"),
        NOAA_LINK("feature.nooa.link"),
        DATE_FORMAT("feature.date.format"),
        TIME_24HOUR_FORMAT("feature.time.24hours"),
        SCERTP_TEMP_UPDATE("feature.rtp.temperature.update"),
        ENABLE_DATASERVICE("feature.enableDataService"),
        PROGRAM_MATRIX_CHECK("feature.programMatrixCheck"),
        FEATURE_ENTIREDAYSHEDLINE_ENABLE("feature.entireDayShedLine.enable"),
        FEATURE_EVENTPERIODSHEDLINE_ENABLE("feature.eventPeriodShedLine.enable"),
        FEATURE_CLIENT_INFO_ENABLE("feature.clientInfo.enable"),
        ENABLE_VALIDATE_USAGE_DATA("enableValidateUsageData"),
        FEATURE_IS_PRODUCTION_SERVER("feature.isProductionServer"),
        FEATURE_CUSTOMER_INFO_UPLOAD_ENABLE("feature.customerInfo.upload.enable"),
        FEATURE_TELEMETRY_TOMORROW_ENABLE("feature.telemetry.tomorrowEnable"),
        FEATURE_TEMP_CONFIG_ENABLE("feature.temp.config.enable"),
        FEATURE_MULTIPLE_RTP_EVENTS_ENABLE("feature.multipleRTPEvents"),
        // Australia price service
		FEATURE_AUSTRALIA_PRICE_SERVICE("australia.price"), 
		FEATURE_AUSTRALIA_PRICE_MAX_DELAY_MIN("australia.price.maxdelay"), 
		FEATURE_AUSTRALIA_PRICE_LOCATION("australia.price.location"),
		// varolii notification or not
		FEATURE_VAROLII_NOTIFICATION("feature.varoliiNotification"), 
		// enable/disable warning messages confirmation for event creation
		FEATURE_WARN_CONFIRM_ENALBED("feature.warnConfirmEnabled"), 
		FEATURE_FORCASTENABLE("feature.forcastEnable"), 
		// enable/disable event opt-out
		FEATURE_EVENTOPTOUTENABLED("feature.eventOptoutEnabled"), 
		DEMO_EVENT_DEFAULT_TIME_ENABLE("demo.event.default.enable"),
        ENABLE_BIP2013("feature.enableBIP2013"),
		FEATURE_CLIENT_TEST_EMAIL_CONSOLIDATION_ENABLED("feature.clientTestEmailConsolidationEnable"),
		ENABLE_AGG_BATCH_UPDATE("feature.enableAggBatchUpdate"),
		FEATURE_EVENT_OPT_IN("feature.eventOptIn"),
		FEATURE_SECONDARY_ACCOUNT_ENABLED("Use_Secondary_Account_for_APX"),
		FEATURE_ENROLL_ALL_PARTICIPANTS("Enroll_All_Participants_for_APX"),
		FEATURE_USE_SECONDARY_UTILITY_NAME_APX("feature.useSecondaryUtilityNameForAPX"),
		FEATURE_CLIENT_OFFLINE_REPORT_ENABLE("feature.clientOfflineReportEnable"),
		FEATURE_CLIENT_OFFLINE_NOTIFICATION_ENABLE("feature.clientOfflineNotificationEnable"),
		FEATURE_TIMER_CONFIG_ENABLE("feature.timerConfigEnable"),
		FEATURE_ENABLE_UNDELIVERED_EMAIL("feature.enableUndeliveredEmail"),
		FEATURE_ENABLE_COMMUNICATION_EMAIL("feature.enableCommunicationEmail"),
		FEATURE_PROGRAM_AUTO_DISPATCH("feature.programAutoDispatch"),
		FEATURE_DEFAULT_DEMO_DURATION("feature.defaultDemoDuration"),
        FEATURE_SHED_STRATEGY_EXPORT_ENABLE("feature.shedStrategyExportEnable"),
        DBP_APPLICATION_ID_ENABLED("utilityName"),
        OPENADR_MULTIPLEUSAGEDATA_ENABLE("openadr.multipledata.enable"),
        OPTOUT_TIMEOUT("optout.timeout"),
        OPENADR_BASELINE_USAGE("openadr.baseline.usage");
        
        private final String featureName;

		/**
		 * Constructor required to support the existng propertyNames as they
		 * have existed for backwards compatibility and existing naming
		 * standard.
		 * 
		 * @param featureName property name
		 */
		private FeatureName(String featureName) {
			this.featureName = featureName;
		}

		@Override
		public String toString() {
			return this.featureName;
		}

		public static FeatureName valueFromPropertyName(
				String propertyNameValue) {
			// This implementation seems overkill, but makes no assumption about
			// the names supporting the existing property names.
			FeatureName[] values = FeatureName.values();
			FeatureName foundValue = null;
			for (int i = 0; i < values.length && foundValue == null; i++) {
				FeatureName featureName = values[i];
				if (featureName.featureName
						.equalsIgnoreCase(propertyNameValue)) {
					foundValue = featureName;
				}

			}
			return foundValue;
		}

		public String getFeatureName() {
			return featureName;
		}

	}
}
