package com.akuacom.pss2.drw.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.drw.jsf.HistoryUIBackingBean;
import com.akuacom.pss2.drw.util.DRWUtil;



public class PREventDataModelValidator {
	
	
	//private static final String VALIDATION_ERROR_START_DATE_SMALL="User has input a date that is more than 3 years past the current date";
	private static final String VALIDATION_ERROR_START_DATE_BIG="Start date must be <= today";
	private static final String VALIDATION_ERROR_END_DATE="End date must be >= start date and must be <= today";
	
//	private static final String VALIDTION_ERROR_START_DATE_EMPTY="User has not provided a start date for date range";
//	private static final String VALIDTION_ERROR_END_DATE_EMPTY="User has not provided a end date for date range";
	private static final String VALIDTION_ERROR_START_DATE_EMPTY="Please enter a Start Date";
	private static final String VALIDTION_ERROR_END_DATE_EMPTY="Please enter an End Date";

//	private static final String VALIDATION_ERROR_INPUT_PARAMETER_EMPTY="User has not selected any of the search parameters";
//	private static final String VALIDATION_ERROR_INPUT_PRODUCT_TYPE_EMPTY="User not selected product type";
//	private static final String VALIDATION_ERROR_INPUT_PROGRAM_EMPTY="User not selected program";
	private static final String VALIDATION_ERROR_INPUT_PRODUCT_TYPE_EMPTY="Please select a Product";
	private static final String VALIDATION_ERROR_INPUT_PROGRAM_EMPTY="Please select a Program";
	private static final String VALIDATION_ERROR_INPUT_PARAMETER_EMPTY="VALIDATION_ERROR_INPUT_PARAMETER_EMPTY";
	@SuppressWarnings("deprecation")
	public static void startDateValidation(Date date) throws ValidationException{
		
		if(date ==null){
			throw new ValidationException(VALIDTION_ERROR_START_DATE_EMPTY);	
		}
		
		Date compareDate = new Date();
		compareDate.setYear(compareDate.getYear()-3);
		long longToday =DRWUtil.getStartOfDay(new Date()).getTime();
		//long compareToday =DRWUtil.getStartOfDay(compareDate).getTime();
		long longDate = DRWUtil.getStartOfDay(date).getTime();		
//		if(longDate<=longToday&&longDate>=compareToday){
//			
//		}else if(longDate<compareToday){
//			throw new ValidationException(VALIDATION_ERROR_START_DATE_SMALL);	
//		}
//		else{
//			throw new ValidationException(VALIDATION_ERROR_START_DATE_BIG);	
//		}
		if(longDate<=longToday){
			
		}
		else{
			throw new ValidationException(VALIDATION_ERROR_START_DATE_BIG);	
		}
	}
	
	public static void endDateValidation(Date endDate,Date startDate) throws ValidationException{
		
		if(endDate ==null){
			throw new ValidationException(VALIDTION_ERROR_END_DATE_EMPTY);	
		}
		if(startDate == null){
			//The validate start time should be validate at function startDateValidation()
			return;	
		}else{
			long longToday =DRWUtil.getStartOfDay(new Date()).getTime();
			long compareToday =DRWUtil.getStartOfDay(startDate).getTime();
			long longDate = DRWUtil.getStartOfDay(endDate).getTime();		
			if(longDate<=longToday&&longDate>=compareToday){
				
			}else{
				throw new ValidationException(VALIDATION_ERROR_END_DATE);	
			}	
		}
		
	}
	public static void dateCompareValidation(Date endDate,Date startDate) throws ValidationException{
		if(endDate ==null||startDate == null){
			return; 
		}else{	
			long start =DRWUtil.getStartOfDay(startDate).getTime();
			long end = DRWUtil.getStartOfDay(endDate).getTime();		
			if(start>end){
				throw new ValidationException(VALIDATION_ERROR_INPUT_DATE_COMPARE);	
			}
		}
		
	}
	public static void searchInputValidation(HistoryUIBackingBean bean) throws ValidationException{
		String longProgramname = bean.getCurrentSelectProduct();
		String programClass = bean.getCurrentSelectProgram();
		boolean longProgramnameCompareFlag = "Select".equalsIgnoreCase(longProgramname);
		boolean programClassCompareFlag = "Select".equalsIgnoreCase(programClass);
		
		if(longProgramnameCompareFlag && programClassCompareFlag){
			throw new ValidationException(VALIDATION_ERROR_INPUT_PARAMETER_EMPTY);	
		}else if(longProgramnameCompareFlag){
			throw new ValidationException(VALIDATION_ERROR_INPUT_PRODUCT_TYPE_EMPTY);	
		}else if(programClassCompareFlag){
			throw new ValidationException(VALIDATION_ERROR_INPUT_PROGRAM_EMPTY);	
		}else{
			
		}		
	}
	
	public static void searchZipCodeValidation(HistoryUIBackingBean bean) throws ValidationException{
		String zipCode = bean.getZipCode();
		if(zipCode!=null&&!zipCode.equalsIgnoreCase("")){
			String regex = "\\d{5}";
			Pattern p = Pattern.compile(regex);
			List<String> zipCodeList = DRWUtil.listByComma(zipCode);
			for(String zc:zipCodeList){
				Matcher m = p.matcher(zc);
				if(!m.matches()){
					throw new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_ZIPCODE);
				}
			}
			
		}
	}
	
//	private static final String VIEW_LIST_VALIDATION_ERROR_INPUT_PARAMETER_EMPTY="User has not selected any of the search parameters.";
	//private static final String VIEW_LIST_VALIDATION_ERROR_INPUT_COUNTY_EMPTY="User not selected county.";
	//private static final String VIEW_LIST_VALIDATION_ERROR_INPUT_CITY_EMPTY="User not selected city.";	
	private static final String VIEW_LIST_VALIDATION_ERROR_INPUT_PARAMETER_EMPTY="Please select a County, City or Zip Code";
	private static final String VIEW_LIST_VALIDATION_ERROR_INPUT_ZIPCODE="Zip Code pattern should be 5 digits number.";
	private static final String VALIDATION_ERROR_INPUT_DATE_COMPARE="End date should be later than start date";
	public static List<ValidationException> viewListFilterValidation(String county,String city,String zipCode){
		List<ValidationException> validationErrors = new ArrayList<ValidationException>();
		boolean countyCompareFlag = "Select".equalsIgnoreCase(county);
		boolean cityCompareFlag = "Select".equalsIgnoreCase(city);
		
		
		
		if(zipCode!=null&&(!zipCode.equalsIgnoreCase(""))){
			String regex = "\\d{5}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(zipCode);
			if(!m.matches()){
				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_ZIPCODE));
			}
			
//			if(countyCompareFlag && cityCompareFlag){
//				//validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_PARAMETER_EMPTY));	
//			}else if(countyCompareFlag&&(!cityCompareFlag)){
//				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_COUNTY_EMPTY));	
//			}else if(cityCompareFlag&&(!countyCompareFlag)){
//				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_CITY_EMPTY));	
//			}else{
//				
//			}	
			
		}else{
			if(countyCompareFlag && cityCompareFlag){
				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_PARAMETER_EMPTY));	
			}
//			}else if(countyCompareFlag){
//				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_COUNTY_EMPTY));	
//			}else if(cityCompareFlag){
//				validationErrors.add(new ValidationException(VIEW_LIST_VALIDATION_ERROR_INPUT_CITY_EMPTY));	
//			}else{
//				
//			}	
		}
		return validationErrors;
	}
}
