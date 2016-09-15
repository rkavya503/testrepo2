package com.akuacom.pss2.program.sceftp.progAutoDisp.validate;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.sceftp.progAutoDisp.FileUtil;
import com.akuacom.pss2.program.sceftp.progAutoDisp.ProgAutoDispUtil;

import scala.actors.threadpool.Arrays;

public class ProgAutoDispValidator {
	public static final String[] DRW_PROGRAMS = { "API", "TOU-BIP", "SDP" };
    @SuppressWarnings("unchecked")
	public static List<String> drwPrograms = Arrays.asList(DRW_PROGRAMS);
    
	public static String ERROR_FILE_NAME_NOT_CORRECT="file name is not correct.";
	public static String ERROR_FILE_CONTENT_NOT_CORRECT="file content is not correct.";
	public static String ERROR_FILE_CONSISTENCY_NOT_CORRECT="The file content is different with the file name.";
	public static String ERROR_START_TIME_EMPTY="event start time can not be empty.";
	public static String ERROR_ISSUE_TIME_EMPTY="event issue time can not be empty.";
	public static String ERROR_START_END_TIME_COMPARE="event end time can not before start time.";
	public static String ERROR_INTERRUPTIBLE_PROGRAM_TERMINATED_ENDTIME_EMPTY="end time can not be empty for TERMINATED command .";
	private static final String ACTION_ACTIVATED="ACTIVATED";
    private static final String ACTION_CONTINUED="CONTINUED";
    private static final String ACTION_TERMINATED="TERMINATED";
    private static final String ACTION_SCHEDULED="SCHEDULED";
	public static void main(String args[]){
		String name = "20130424130000_API_API_ABANK_4001_ACTIVATED.txt";
		String string = "API,20130424,130000,ABANK,4001,API,20130424,130000,,";
		System.out.println(string.split(",")[4]);
//		try {
//			ProgAutoDispValidator.vlidateFileContent(string,name);
//			FileUtil file = new FileUtil();
//			StringBuilder regx = new StringBuilder();
//			regx.append("^\\s*(API|TOU-BIP|SDP|SAI|DBP|SPD)\\s*,"); // PRG
//			regx.append("\\s*(\\d{8})\\s*,"); // YYYYMMDD, issue date
//			regx.append("\\s*(\\d{6})\\s*,"); // HHmmSS, issue time
//			regx.append("\\s*(ALL|SLAP|ABANK|SUBSTATION)?\\s*,"); // dispatch type
//			regx.append("\\s*((\\S*)?|(\\d*)?)\\s*,"); // dispatch location
//			regx.append("\\s*(\\S*)?\\s*,"); // PRD
//			regx.append("\\s*(\\d{8})\\s*,"); // YYYYMMDD, start date
//			regx.append("\\s*(\\d{6})\\s*,"); // HHmmSS, start time
//			regx.append("\\s*(\\d{8})?\\s*,"); // YYYYMMDD, end date
//			regx.append("\\s*(\\d{6})?\\s*,*$"); // HHmmSS, end time
//
//			Pattern p1 = Pattern.compile(regx.toString(), Pattern.CASE_INSENSITIVE);
//			Matcher m1 = p1.matcher(string);
//			
//			if (m1.find()){
//				String program = m1.group(1);
//				String locationType = m1.group(4);
//				String locationName = m1.group(5);
//				String product = m1.group(6);
//				
//				file.setFileContents(string);
//				file.setProgram(ProgAutoDispUtil.getProgramByContext(string));
//				file.setProduct(ProgAutoDispUtil.getProductByContext(string));
//				file.setLocationType(ProgAutoDispUtil.getDispatchTypeByContext(string));
//				file.setLocationName(ProgAutoDispUtil.getLocationByContext(string));	
//				file.setIssueTime(ProgAutoDispUtil.getIssueTimeByContext(string));
//				file.setStartTime(ProgAutoDispUtil.getStartDateByContext(string));
//				file.setEndTime(ProgAutoDispUtil.getEndDateByContext(string));
//			}
//			
//		} catch (ProgAutoDispException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static void vlidateFileName(String fileName) throws ProgAutoDispException{
		boolean result = false;
    	StringBuilder regx = new StringBuilder();
		regx.append("^\\s*(\\d{14})\\s*_"); // YYYYMMDDHHmmSS
		regx.append("\\s*(API|TOU-BIP|SDP|SAI|DBP|SPD)\\s*_"); // PRG
//		regx.append("\\s*(APS|APS-E|SDP)?\\s*_"); // PRD
		regx.append("\\s*(\\S*)?\\s*_"); // PRD
		regx.append("\\s*(\\S*)?\\s*_"); // LTP
//		regx.append("\\s*(\\S*)?\\s*_"); // LOC
		regx.append("\\s*(SCHEDULED|ACTIVATED|CONTINUED|TERMINATED)\\s*.txt$"); // actionVerb
		Pattern p1 = Pattern.compile(regx.toString(),Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(fileName);
		if (m1.find()){
			String program = m1.group(2);
			String product = m1.group(3);
			String locationType = m1.group(4);
//			String locationName = m1.group(5);
			result = true;
			if ("SDP".equalsIgnoreCase(program)) {
				if (product == null|| product.isEmpty() || !"APS|APS-E|SDP".contains(product)) {
					result = false;
				}
			} else {
				if (!product.equalsIgnoreCase(program))
					result=false;
			}
			
			if (drwPrograms.contains(program.toUpperCase())) {
				if (locationType == null|| locationType.isEmpty()) {
					result = false;
				}
//				if (locationName == null|| locationName.isEmpty()) {
//					result = false;
//				}
			}
		}
		if(!result){
			throw new ProgAutoDispException("["+fileName+"]:"+ERROR_FILE_NAME_NOT_CORRECT);
		}
	}
	
	
	public static void vlidateFileContent(String fileContent,String fileName) throws ProgAutoDispException{
		
		String[] fileContents = fileContent.split("\n");
    	StringBuilder regx = new StringBuilder();
		regx.append("^\\s*(API|TOU-BIP|SDP|SAI|DBP|SPD)\\s*,"); // PRG
		regx.append("\\s*(\\d{8}|\\s*)\\s*,"); // YYYYMMDD, issue date
		regx.append("\\s*(\\d{6}|\\s*)\\s*,"); // HHmmSS, issue time
		regx.append("\\s*(ALL|SLAP|ABANK|SUBSTATION)?\\s*,"); // dispatch type
		regx.append("\\s*((\\S*)?|(\\d*)?)\\s*,"); // dispatch location
		regx.append("\\s*(\\S*)?\\s*,"); // PRD
		regx.append("\\s*(\\d{8})\\s*,"); // YYYYMMDD, start date
		regx.append("\\s*(\\d{6})\\s*,"); // HHmmSS, start time
		regx.append("\\s*(\\d{8})?\\s*,"); // YYYYMMDD, end date
		regx.append("\\s*(\\d{6})?\\s*,*$"); // HHmmSS, end time

		Pattern p1 = Pattern.compile(regx.toString(), Pattern.CASE_INSENSITIVE);

		for(String line:fileContents){
    		if(!line.trim().equalsIgnoreCase("")){
    			boolean result = false;
    			Matcher m1 = p1.matcher(line);
    			
    			if (m1.find()){
    				String program = m1.group(1);
    				String locationType = m1.group(4);
    				String locationName = m1.group(5);
    				String product = m1.group(6);
    				result = true;
    				if ("SDP".equalsIgnoreCase(program)) {
    					if (product == null|| product.isEmpty()) {
    						result = false;
    					}
    				}
    				if (drwPrograms.contains(program.toUpperCase())) {
    					if (locationType == null|| locationType.isEmpty()) {
    						result = false;
    					}
    					if (locationName == null|| locationName.isEmpty()) {
    						result = false;
    					}
    				}
    				if(program.equalsIgnoreCase("API")||program.equalsIgnoreCase("BIP")){
    					if(locationType.equalsIgnoreCase("SUBSTATION")){
    						result = false;
    					}
    				}			
    			}
    			if(!result){
    				throw new ProgAutoDispException("["+fileName+"]:"+ERROR_FILE_CONTENT_NOT_CORRECT);
    			}
    		}
		}
		
		
	}
	
	public static void validateFileConsistency(String fileContent,String fileName) throws ProgAutoDispException{
		boolean result = true;
    	FileUtil file1 = ProgAutoDispUtil.parseFileName(fileName);
    	FileUtil file2 = ProgAutoDispUtil.parseFileContext(fileContent);
    	if(!file1.getProgram().equalsIgnoreCase(file2.getProgram())){
    		result = false;
    	}
    	if("SDP".contains(file1.getProgram().toUpperCase())){
	    	if(!file1.getProduct().equalsIgnoreCase(file2.getProduct())){
	    		result = false;
	    	}
    	}
    	if("API|TOU-BIP|SDP".contains(file1.getProgram().toUpperCase())){
	    	if(!file1.getLocationType().equalsIgnoreCase(file2.getLocationType())){
	    		result = false;
	    	}
//	    	if(!file1.getLocationName().equalsIgnoreCase(file2.getLocationName())){
//	    		result = false;
//	    	}
    	}
    	
		if(!result){
			throw new ProgAutoDispException("["+fileName+"]:"+ERROR_FILE_CONSISTENCY_NOT_CORRECT);
		}
	}
	
	public static void validateFileDateScope(ProgramManager programManager,Event event,String fileName,boolean validateIssueTime) throws ProgAutoDispException{
		boolean result = true;
    	Date issueTime = event.getIssuedTime();
    	Date startTime = event.getStartTime();
    	Date endTime = event.getEndTime();
    	String program = event.getProgramName();
    	Date now = new Date();
//    	Date systemTime = new Date();
    	//SAI|SPD|DBP DA program
    	// issueTime>systemTime && startTime>issueTime - valid
    	// issueTime<systemTime || startTime<issueTime - invalid
    	// issueTime = startTime - invalid
    	// issueTime > startTime - invalid
    	// startTime > endTime - invalid
    	// issueTime > pendingTime - invalid
    	
    	
    	
    	if("SAI|SPD|DBP DA".contains(program.toUpperCase())){
    		try {
    			validatePSS2Event(programManager,program,event);
    		} catch (ProgramValidationException e) {
    			List<ProgramValidationMessage> errors =e.getErrors();
    			if(errors.size()>0){
    				throw new ProgAutoDispException("["+fileName+"]:"+errors.get(0).getDescription());	
    			}
    		}
    	}else if("API|BIP|SDP".contains(program.toUpperCase())){
//    		if(validateIssueTime&&"BIP".contains(program.toUpperCase())){
//    			if(issueTime.before(now)){
//        			throw new ProgAutoDispException("["+fileName+"]:"+ERROR_ISSUE_TIME_IN_THE_PAST);
//        		}	
//    		}
    		
    		
    		if(startTime==null){
    			throw new ProgAutoDispException("["+fileName+"]:"+ERROR_START_TIME_EMPTY);
    		}
    		if(startTime!=null&&endTime!=null){
    			if(!startTime.before(endTime)){
    				throw new ProgAutoDispException("["+fileName+"]:"+ERROR_START_END_TIME_COMPARE);
    			}
    		}
    	}
    	
    	//API|BIP|SDP program
		if(!result){
			throw new ProgAutoDispException("["+fileName+"]:"+ERROR_FILE_CONSISTENCY_NOT_CORRECT);
		}
	}

	private static void validatePSS2Event(ProgramManager programManager,String programName,Event event) throws ProgramValidationException{
		Program program = programManager.getProgramWithParticipantsAndPRules(programName);
		ProgramValidator programValidator;
		programValidator = ValidatorFactory.getProgramValidator(program);
		programValidator.validateEvent(event);
		
		
	}

	/**
	 * @param fileUtil
	 */
	public static void validateInterruptibleProgramDateScope(FileUtil fileUtil) throws ProgAutoDispException{
//		Date issueTime = fileUtil.getIssueTime();
		Date startTime = fileUtil.getStartTime();
    	Date endTime = fileUtil.getEndTime();
//    	if(issueTime==null){
//			throw new ProgAutoDispException("["+fileUtil.getFilename()+"]:"+ERROR_ISSUE_TIME_EMPTY);
//		}
    	if(startTime==null){
			throw new ProgAutoDispException("["+fileUtil.getFilename()+"]:"+ERROR_START_TIME_EMPTY);
		}
		if(startTime!=null&&endTime!=null){
			if(!startTime.before(endTime)){
				throw new ProgAutoDispException("["+fileUtil.getFilename()+"]:"+ERROR_START_END_TIME_COMPARE);
			}
		}
		if(ACTION_TERMINATED.equalsIgnoreCase(fileUtil.getVerb())){
			if(fileUtil.getEndTime()==null){
				throw new ProgAutoDispException("["+fileUtil.getFilename()+"]:"+ERROR_INTERRUPTIBLE_PROGRAM_TERMINATED_ENDTIME_EMPTY);
			}
		}
	}
}
