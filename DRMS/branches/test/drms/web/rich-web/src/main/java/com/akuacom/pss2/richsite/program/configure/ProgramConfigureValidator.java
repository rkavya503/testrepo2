package com.akuacom.pss2.richsite.program.configure;

import java.util.Iterator;
import java.util.Map;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;


public class ProgramConfigureValidator {
	
	/** program exist validation error message*/
	private static final String VALIDATION_ERROR_PROGAM_EXIST="Create program error:program already exist.";

	/** program not exist validation error message*/
	private static final String VALIDATION_ERROR_PROGAM_NO_EXIST="Edit program error:program not exist.";	

	/** program priority exist validation error message*/
	private static final String VALIDATION_ERROR_PROGAM_PRIORITY_EXIST="Error:priority already exist.";

	/** program next priority prefix*/
	private static final String VALIDATION_NEXT_PRIORITY_PREFIX="Can use priority:";

	/** program time format not correct validation error message*/
	private static final String VALIDATION_TIME_FORMAT_NOT_CORRECT="Format is not correct.";

	/** program hour format not correct validation error message*/
	private static final String VALIDATION_TIME_FORMAT_NOT_CORRECT_HOUR="Hour format is not correct!Hour must be number and between 0 and 23!";

	/** program minute format not correct validation error message*/
	private static final String VALIDATION_TIME_FORMAT_NOT_CORRECT_MINUTE="Minute  format is not correct!Minute must be number and between 0 and 59!";

    /** program minute format not correct validation error message*/
	private static final String VALIDATION_TIME_FORMAT_NOT_CORRECT_SECONDS="Second  format is not correct!Seconds must be number and between 0 and 59!";

    /*
	 * Function for validate ProgramConfigureDataModel instance 
	 * @param model ProgramConfigureDataModel instance
	 * @param addFlag add or update operation flag, true is add , false is update
	 * @throws ValidationException
	 */
	public static void programValidation(ProgramConfigureDataModel model,boolean addFlag,ProgramConfigureDataModelManager manager) throws ValidationException{
		
		if(addFlag){
			boolean programIsExistFlag = programIsExistValidation(model,manager);
			if(programIsExistFlag){
				throw new ValidationException(VALIDATION_ERROR_PROGAM_EXIST);	
			}
		}else{
			boolean programIsExistFlag = programIsExistValidation(model,manager);
			if(!programIsExistFlag){
				throw new ValidationException(VALIDATION_ERROR_PROGAM_NO_EXIST);	
			}
		}
	}
	
	/**
	 * Function for validate program is exist 
	 * @param model
	 * @param manager
	 * @return
	 * @throws ValidationException
	 */
	public static boolean programIsExistValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		boolean flag = false;
		if(manager==null){
			manager =ProgramConfigureFactory.getInstance().getProgramConfigureDataModelManager();
		}
		Program program = manager.getProgram(model);
		if(program ==null){
			flag = false;
		}else{
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Function for validate program priority
	 * @param model
	 * @param addFlag
	 * @param programManager
	 * @throws ValidationException
	 */
	public static void programPriorityValidation(ProgramConfigureDataModel model,boolean addFlag,ProgramManager programManager) throws ValidationException{
		int priority =model.getProgram().getPriority();
		Map<String, Integer> priorityMap = programManager.getProgramPriority();
		int prioritySuitable = programManager.getNextPriority();
		Iterator<Integer> values =priorityMap.values().iterator();
		if(addFlag){
			while(values.hasNext()){
				Integer value = values.next();
				if(value.intValue()==priority){
					throw new ValidationException(VALIDATION_ERROR_PROGAM_PRIORITY_EXIST+VALIDATION_NEXT_PRIORITY_PREFIX+prioritySuitable);	
				}
			}
		}else{
			Integer valueOriginal = priorityMap.get(model.getProgram().getProgramName());
			while(values.hasNext()){
				Integer value = values.next();
				if(value.intValue()==priority){
					if(value==valueOriginal){
						
					}else{
						throw new ValidationException(VALIDATION_ERROR_PROGAM_PRIORITY_EXIST);
					}	
				}
			}
		}
	}
	
	/**
	 * Function for validate program time string format
	 * @param timeString
	 * @param manager
	 * @param prefix
	 * @throws ValidationException
	 */
	public static void programTimeStringValidation(String timeString,ProgramConfigureDataModelManager manager,String prefix) throws ValidationException{

        if (timeString.isEmpty() && prefix.indexOf("Auto") > -1){
            return;
        }
        if(!timeString.contains(":")){
			throw new ValidationException(prefix+VALIDATION_TIME_FORMAT_NOT_CORRECT);
		}
		String hourString = manager.getHourFromModelTimeString(timeString);
		String minString = manager.getMinFromModelTimeString(timeString);
        String secString = manager.getSecFromModelTimeString(timeString);
        
		try{
			int hour = Integer.parseInt(hourString);
			if(hour>23||hour<0){
				throw new Exception();
			}
		}catch(Exception e){
			throw new ValidationException(prefix+VALIDATION_TIME_FORMAT_NOT_CORRECT_HOUR);	
		}
		try{
			int min =Integer.parseInt(minString);
			if(min>59||min<0){
				throw new Exception();
			}
		}catch(Exception e){
			throw new ValidationException(prefix+VALIDATION_TIME_FORMAT_NOT_CORRECT_MINUTE);	
		}
       
        try{
          if (secString != "0"){
			int sec =Integer.parseInt(secString);
			if(sec>59||sec<0){
				throw new Exception();
			}
          }else{
            return;
          }
		}catch(Exception e){
			throw new ValidationException(prefix+VALIDATION_TIME_FORMAT_NOT_CORRECT_SECONDS);
		}
        
	}
	
	/**
	 * Function for validate max issue time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void maxIssueTimeStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getMaxIssueTimeString(),manager,"Max Issue Time ");
	}

	/**
	 * Function for validate min start time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void minStartTimeStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getMinStartTimeString(),manager,"Min Start Time ");
	}

	/**
	 * Function for validate max start time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void maxStartTimeStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getMaxStartTimeString(),manager,"Max Start Time ");
	}

	/**
	 * Function for validate min end time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void minEndTimeStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getMinEndTimeString(),manager,"Min End Time ");
	}

	/**
	 * Function for validate max end time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void maxEndTimeStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getMaxEndTimeString(),manager,"Max End Time ");
	}

	/**
	 * Function for validate pending time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void pendingTimeDBEStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getPendingTimeDBEString(),manager,"Pending Time DBE ");
	}
    	/**
	 * Function for validate Auto repeate time string format
	 * @param model
	 * @param manager
	 * @throws ValidationException
	 */
	public static void autoRepeatTimeOfDayStringValidation(ProgramConfigureDataModel model,ProgramConfigureDataModelManager manager) throws ValidationException{
		programTimeStringValidation(model.getAutoRepeatTimeOfDayStr(),manager,"Auto-repeat Time ");
	}
    
}
