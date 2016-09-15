package com.akuacom.pss2.richsite.gridpoint;

import org.apache.commons.lang.StringUtils;

import com.akuacom.pss2.richsite.FDUtils;

public class GridPointConfigureBackingBeanValidator {
	
	public static String MESSAGE_EMPTY_AUTHENTICATION_URL="Authentication url can not be empty.";
	public static String MESSAGE_EMPTY_USERNAME="Username can not be empty.";
	public static String MESSAGE_EMPTY_PASSWORD="Password can not be empty.";
	public static String MESSAGE_EMPTY_RETRIEVE_DATA_URL="Retrieve data url  can not be empty.";
	public static String MESSAGE_EMPTY_TIME_INTERVAL="Time interval can not be empty.";
	public static String MESSAGE_EMPTY_DATE_BACK_SCOPE="Date back scope can not be empty.";
	public static String MESSAGE_EMPTY_FIX_SCOPE_VALUE="Fix scope value can not be empty.";
	public static String MESSAGE_POSITIVE_NUMBER_VALIDATION_TIME_INTERVAL="Time interval must be larger than 0.";
	public static String MESSAGE_POSITIVE_NUMBER_VALIDATION_DATE_BACK_SCOPE="Date back scope must be larger than 0.";
	public static String MESSAGE_POSITIVE_NUMBER_VALIDATION_FIX_SCOPE_VALUE="Fix scope value must be larger than 0.";
	public static String MESSAGE_NUMBER_VALIDATION_TIME_INTERVAL="Time interval only accept number.";
	public static String MESSAGE_NUMBER_VALIDATION_DATE_BACK_SCOPE="Date back scope only accept number.";
	public static String MESSAGE_NUMBER_VALIDATION_FIX_SCOPE_VALUE="Fix scope value only accept number.";
	
	public static String ERROR_MESSAGE_RETRIEVE_NO_RECORD_CONFIGURATION="retrieve record from the table grid_point_configuration failed: no record retrieved.";
	public static String SUCCESS_MESSAGE_SAVE_CONFIGURATION="Save configuration successfully.";
	public static String ERROR_MESSAGE_SITE_ID_EMPTY="Test site id can not be empty.";
	public static String ERROR_MESSAGE_TEST_AUTHENTICATION="Test Grid Point Authentication failed! The error message is : ";
	public static String ERROR_MESSAGE_TEST_RETRIEVE_DATA="Test Grid Point retrieve data failed! The error message is : ";
	public static String ERROR_MESSAGE_TIME_PATTERN="The format pattern of the end time is not correct. Please use the yyyy-MM-dd'T'HH:mm:ss format pattern.";
	public static String timeFormatPattern="yyyy-MM-dd'T'HH:mm:ss";
	public static String ERROR_MESSAGE_AUTHENTICATION_FIRST="The authentication is not passed. Please test authentication first.";
   
	public synchronized static boolean validate(GridPointConfigureBackingBean bean){
		boolean flag=true;
		if(StringUtils.isEmpty(bean.getDataModel().getAuthenticationURL())){
			FDUtils.addMsgError(MESSAGE_EMPTY_AUTHENTICATION_URL);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getDataModel().getUsername())){
			FDUtils.addMsgError(MESSAGE_EMPTY_USERNAME);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getDataModel().getPassword())){
			FDUtils.addMsgError(MESSAGE_EMPTY_PASSWORD);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getDataModel().getRetrieveDataURL())){
			FDUtils.addMsgError(MESSAGE_EMPTY_RETRIEVE_DATA_URL);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getTimeIntervalString())){
			FDUtils.addMsgError(MESSAGE_EMPTY_TIME_INTERVAL);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getDateBackScopeString())){
			FDUtils.addMsgError(MESSAGE_EMPTY_DATE_BACK_SCOPE);
			flag = false;
		}
		if(StringUtils.isEmpty(bean.getFixScopeValueString())){
			FDUtils.addMsgError(MESSAGE_EMPTY_FIX_SCOPE_VALUE);
			flag = false;
		}
		try{
			int intValue=Integer.parseInt(bean.getTimeIntervalString());	
			if(intValue<=0){
				FDUtils.addMsgError(MESSAGE_POSITIVE_NUMBER_VALIDATION_TIME_INTERVAL);
				flag = false;
			}
		}
		catch (Exception e) {
			FDUtils.addMsgError(MESSAGE_NUMBER_VALIDATION_TIME_INTERVAL);
			flag = false;
		}
		try{
			int intValue=Integer.parseInt(bean.getDateBackScopeString());	
			if(intValue<=0){
				FDUtils.addMsgError(MESSAGE_POSITIVE_NUMBER_VALIDATION_DATE_BACK_SCOPE);
				flag = false;
			}
		}
		catch (Exception e) {
			FDUtils.addMsgError(MESSAGE_NUMBER_VALIDATION_DATE_BACK_SCOPE);
			flag = false;
		}
		try{
			int intValue=Integer.parseInt(bean.getFixScopeValueString());	
			if(intValue<=0){
				FDUtils.addMsgError(MESSAGE_POSITIVE_NUMBER_VALIDATION_FIX_SCOPE_VALUE);
				flag = false;
			}
		}
		catch (Exception e) {
			FDUtils.addMsgError(MESSAGE_NUMBER_VALIDATION_FIX_SCOPE_VALUE);
			flag = false;
		}
		return flag;
	}
}
