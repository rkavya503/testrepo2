package com.akuacom.pss2.richsite.program.configure.season;

import java.util.List;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.season.SeasonConfig;
/**
 *
 * Filename:    SeasonConfigureValidator.java
 * Description:
 * Copyright:   Copyright (c)2010
 * Company:
 * @version:
 * Create at:   Feb 17, 2011 4:59:19 PM
 *
 */
public class SeasonConfigureValidator {

	/** season empty validation error message*/
	private static final String VALIDATION_ERROR_SEASON_EMPTY="Seasons can't be empty!";

    /** HOLIDAY empty validation error message*/
	private static final String VALIDATION_ERROR_HOLIDAY_EMPTY="Holidays can't be empty!";

	/** season sequence validation error message*/
	private static final String VALIDATION_ERROR_SEASON_SEQUENCE="Season sequence is not right!";

	/** season name empty validation error message*/
	private static final String VALIDATION_ERROR_SEASON_NAME_EMPTY="Season name can't be empty!";

    /** HOLIDAY name empty validation error message*/
	private static final String VALIDATION_ERROR_HOLIDAY_NAME_EMPTY="Holiday name can't be empty!";

	/** season name empty validation error message*/
	private static final String VALIDATION_ERROR_SEASON_NAME="Season name has to start with 'SUMMER', or 'WINTER'";

    /** holiday name empty validation error message*/
	private static final String VALIDATION_ERROR_HOLIDAY_NAME="Holiday name has to start with 'WEEKEND' ";

	/** season start date validation error message*/
	private static final String VALIDATION_ERROR_SEASON_STARTDATE="Season start date can't be empty!";

	/**
	 * Function for validate season empty
	 * @param seasons
	 * @throws ValidationException
	 */
	public static void seasonEmptyValidation(List<SeasonDataModel> seasons) throws ValidationException{
		if(seasons.size()<=0){
			throw new ValidationException(VALIDATION_ERROR_SEASON_EMPTY);
		}
	}

    public static void holidayEmptyValidation(List<HolidayDataModel> holidays) throws ValidationException{
		if(holidays.size()<=0){
			throw new ValidationException(VALIDATION_ERROR_HOLIDAY_EMPTY);
		}
	}

	/**
	 * Function for validate season sequence
	 * @param seasons
	 * @throws ValidationException
	 */
	public static void seasonSequenceValidation(List<SeasonDataModel> seasons) throws ValidationException{
		if(seasons.size()>0){
			//if(!seasons.get(0).getName().trim().equalsIgnoreCase(SeasonConfig.SUMMER_SEASON)){
				//throw new ValidationException(VALIDATION_ERROR_SEASON_SEQUENCE);
			//}
		}
	}

	/**
	 * Function for validate season name
	 * @param season
	 * @throws ValidationException
	 */
	public static void seasonNameValidation(SeasonDataModel season) throws ValidationException{
		if (season.getName().trim().equalsIgnoreCase("")) {
			throw new ValidationException(VALIDATION_ERROR_SEASON_NAME_EMPTY);
		}
		boolean nameFlag = false;
		if (season.getName().trim().equalsIgnoreCase(SeasonConfig.SUMMER_SEASON) ||season.getName().trim().equalsIgnoreCase(SeasonConfig.WINTER_SEASON)) {
			nameFlag=true;
		}
		if(!nameFlag){
			throw new ValidationException(VALIDATION_ERROR_SEASON_NAME);
		}
	}

    	public static void holidayNameValidation(HolidayDataModel holiday) throws ValidationException{
		if (holiday.getName().trim().equalsIgnoreCase("")) {
			throw new ValidationException(VALIDATION_ERROR_HOLIDAY_NAME_EMPTY);
		}
        /*
		boolean nameFlag = false;
		if (holiday.getName().trim().equalsIgnoreCase(SeasonConfig.WEEKEND_SEASON) ) {
			nameFlag=true;
		}
		if(!nameFlag){
			throw new ValidationException(VALIDATION_ERROR_HOLIDAY_NAME);
		}
         */
	}


	/**
	 * Function for validate season start time
	 * @param season
	 * @throws ValidationException
	 */
	public static void seasonStartTimeValidation(SeasonDataModel season) throws ValidationException{
		if (season.getStartDate()==null) {
			throw new ValidationException(VALIDATION_ERROR_SEASON_STARTDATE);
		}
	}
}
