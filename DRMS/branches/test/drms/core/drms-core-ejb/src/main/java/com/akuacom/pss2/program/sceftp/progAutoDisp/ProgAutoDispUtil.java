package com.akuacom.pss2.program.sceftp.progAutoDisp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scala.actors.threadpool.Arrays;

public class ProgAutoDispUtil {
	public static final String ACTION_ACTIVATED="ACTIVATED";
	public static final String ACTION_CONTINUED="CONTINUED";
	public static final String ACTION_TERMINATED="TERMINATED";
	public static final String ACTION_SCHEDULED="SCHEDULED";
    public static final String ACTION_DELETE="DELETE";
	/**
     * Function for get Action Name from file name
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static String getActionNameByFileName(String fileName){
    	//20130424130000_SDP_APS_SLAP_SCHEDULED.txt
    	String result = "";
    	try{
//    		result = fileName.split("_")[5].split("\\.")[0];
    		result = fileName.split("_")[4].split("\\.")[0];
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return result;
    }
    /**
     * Function for get program from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static String getProgramByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	String result = "";
    	try{
    		result = fileContext.split(",")[0].trim();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return result;
    }
    /**
     * Function for get issue time from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static Date getIssueTimeByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	Date date =null;
    	try{
    		String result = fileContext.split(",")[1].trim();
    		String result1 = fileContext.split(",")[2].trim();
    		if("".equalsIgnoreCase(result)||"".equalsIgnoreCase(result1)){
    			return new Date();
    		}
    		int year = Integer.valueOf(result.substring(0, 4));
    		int month = Integer.valueOf(result.substring(4, 6));
    		int day = Integer.valueOf(result.substring(6, 8));
    		int hour = Integer.valueOf(result1.substring(0, 2));
    		int minute = Integer.valueOf(result1.substring(2, 4));
    		int second = Integer.valueOf(result1.substring(4, 6));
    		Calendar c = Calendar.getInstance();
    		c.set(Calendar.YEAR, year);
    		c.set(Calendar.MONTH, month-1);
    		c.set(Calendar.DAY_OF_MONTH, day);
    		c.set(Calendar.HOUR_OF_DAY, hour);
    		c.set(Calendar.MINUTE, minute);
    		c.set(Calendar.SECOND, second);
    		date = c.getTime();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return date;
    }
    public static void main(String args[]){
    	String s = "API,20130721,020000,ALL,ALL,API,20130722,000000,20130722,190000";
    	Date date =null;
    	try{
    		String result = s.split(",")[1].trim();
    		String result1 = s.split(",")[2].trim();
    		int year = Integer.valueOf(result.substring(0, 4));
    		int month = Integer.valueOf(result.substring(4, 6));
    		int day = Integer.valueOf(result.substring(6, 8));
    		int hour = Integer.valueOf(result1.substring(0, 2));
    		int minute = Integer.valueOf(result1.substring(2, 4));
    		int second = Integer.valueOf(result1.substring(4, 6));
    		Calendar c = Calendar.getInstance();
    		c.set(Calendar.YEAR, year);
    		c.set(Calendar.MONTH, month-1);
    		c.set(Calendar.DAY_OF_MONTH, day);
    		c.set(Calendar.HOUR_OF_DAY, hour);
    		c.set(Calendar.MINUTE, minute);
    		c.set(Calendar.SECOND, second);
    		date = c.getTime();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	System.out.println( date);
    }
    /**
     * Function for get dispatch type from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static String getDispatchTypeByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	String result = "";
    	try{
    		result = fileContext.split(",")[3].trim();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return result;
    }
    /**
     * Function for get dispatch location from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static String getLocationByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	String result = "";
    	try{
    		result = fileContext.split(",")[4].trim();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return result;
    }
    /**
     * Function for get product from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static String getProductByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	String result = "";
    	try{
    		result = fileContext.split(",")[5].trim();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return result;
    }
    /**
     * Function for get start date from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static Date getStartDateByContext(String fileContext){
    	//SDP,20130424,121000,SLAP,SCEC,APS,20130424,130000,20130424,140000
    	Date date =null;
    	try{
    		String result1 = fileContext.split(",")[6].trim();
    		String result2 = fileContext.split(",")[7].trim();
    		int year = Integer.valueOf(result1.substring(0, 4));
    		int month = Integer.valueOf(result1.substring(4, 6));
    		int day = Integer.valueOf(result1.substring(6, 8));
    		int hour = Integer.valueOf(result2.substring(0,2));
    		int minute = Integer.valueOf(result2.substring(2,4));
    		int second = Integer.valueOf(result2.substring(4,6));
    		Calendar c = Calendar.getInstance();
    		c.set(Calendar.YEAR, year);
    		c.set(Calendar.MONTH, month-1);
    		c.set(Calendar.DAY_OF_MONTH, day);
    		c.set(Calendar.HOUR_OF_DAY, hour);
    		c.set(Calendar.MINUTE, minute);
    		c.set(Calendar.SECOND, second);
    		date = c.getTime();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return date;
    }
    
    /**
     * Function for get end date from file context
     * This function should first pass the file context validation
     * @param fileContext
     * @return
     */
    public static Date getEndDateByContext(String fileContext){
    	Date date =null;
    	try{
    		String result1 = fileContext.split(",")[8].trim();
    		if(result1==null||"".equalsIgnoreCase(result1)){
    			return date;
    		}
    		String result2 = fileContext.split(",")[9].trim();
    		int year = Integer.valueOf(result1.substring(0, 4));
    		int month = Integer.valueOf(result1.substring(4, 6));
    		int day = Integer.valueOf(result1.substring(6, 8));
    		int hour = Integer.valueOf(result2.substring(0,2));
    		int minute = Integer.valueOf(result2.substring(2,4));
    		int second = Integer.valueOf(result2.substring(4,6));
    		Calendar c = Calendar.getInstance();
    		c.set(Calendar.YEAR, year);
    		c.set(Calendar.MONTH, month-1);
    		c.set(Calendar.DAY_OF_MONTH, day);
    		c.set(Calendar.HOUR_OF_DAY, hour);
    		c.set(Calendar.MINUTE, minute);
    		c.set(Calendar.SECOND, second);
    		date = c.getTime();
    	}catch(Exception e){
    		//handleValidationError();
    	}
    	return date;
    }
    
    public static final String[] DRW_PROGRAMS = { "API", "TOU-BIP", "SDP" };
    @SuppressWarnings("unchecked")
	public static List<String> drwPrograms = Arrays.asList(DRW_PROGRAMS);
	public static FileUtil parseFileName(String fileName){
		FileUtil file = new FileUtil();
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
			file.setFilename(fileName);
			file.setProgram(m1.group(2));
			file.setProduct(m1.group(3));
			file.setLocationType(m1.group(4));
//			file.setLocationName(m1.group(5));
			file.setVerb(m1.group(5));			
		}
		return file;
	}
	public static FileUtil parseFileContext(String fileContent){
		FileUtil file = new FileUtil();
		
		String[] fileContents = fileContent.split("\n");
		String fileContent1 = fileContents[0];
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
		Matcher m1 = p1.matcher(fileContent1);
		
		if (m1.find()){
			
			file.setFileContents(fileContent);
			file.setProgram(ProgAutoDispUtil.getProgramByContext(fileContent));
			file.setProduct(ProgAutoDispUtil.getProductByContext(fileContent));
			file.setLocationType(ProgAutoDispUtil.getDispatchTypeByContext(fileContent));
//			file.setLocationName(ProgAutoDispUtil.getLocationByContext(fileContent));	
			file.setIssueTime(ProgAutoDispUtil.getIssueTimeByContext(fileContent));
			file.setStartTime(ProgAutoDispUtil.getStartDateByContext(fileContent));
			file.setEndTime(ProgAutoDispUtil.getEndDateByContext(fileContent));
			
			String locationName = buildLocationName(fileContent);
	    	file.setLocationName(locationName);
		}
		
		return file;
	}
	
	public static FileUtil parseFileConsistency(String fileName,String fileContent){
    	
    	FileUtil file1 = parseFileName(fileName);
    	FileUtil file2 = parseFileContext(fileContent);
    	file1.setFileContents(file2.getFileContents());
    	file1.setIssueTime(file2.getIssueTime());
    	file1.setStartTime(file2.getStartTime());
    	file1.setEndTime(file2.getEndTime());
    	
    	String locationName = buildLocationName(fileContent);
    	file1.setLocationName(locationName);
    	return file1;
    }
	
	public static String buildLocationName(String fileContent){
		String result = "";
		String[] fileContents = fileContent.split("\n");
//		String[] fileContents = fileContent.split("\r");
//		String[] fileContents2 = fileContent.split("\n");
//		String[] fileContents3 = fileContent.split("\r\n");
//		System.out.println(fileContents1.length);
//		System.out.println(fileContents2.length);
//		System.out.println(fileContents3.length);
		for(String s:fileContents){
			String[] sArray = s.split(",");
			if(sArray.length>4){
				String locationName = s.split(",")[4];
				result=result+"-"+locationName;
			}
		}
		if(result.startsWith("-")){
			result=result.substring(1);
		}
		System.out.println(result);
		return result;
	}
	
public static List<FileUtil> parseFileConsistencyEnhance(String fileName,String fileContent){
		
		
    	List<FileUtil> eventUnitList = new ArrayList<FileUtil>();
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
		
		String[] fileContents = fileContent.split("\n");
		for(String line:fileContents){
    		if(!line.trim().equalsIgnoreCase("")){
    			Matcher m1 = p1.matcher(line);
    			if (m1.find()){  		
    				FileUtil file = parseFileName(fileName);
    				file.setFileContents(fileContent);
    				file.setProgram(ProgAutoDispUtil.getProgramByContext(line));
    				file.setProduct(ProgAutoDispUtil.getProductByContext(line));
    				file.setLocationType(ProgAutoDispUtil.getDispatchTypeByContext(line));
    				file.setLocationName(ProgAutoDispUtil.getLocationByContext(line));
    				file.setIssueTime(ProgAutoDispUtil.getIssueTimeByContext(line));
    				file.setStartTime(ProgAutoDispUtil.getStartDateByContext(line));
    				file.setEndTime(ProgAutoDispUtil.getEndDateByContext(line));
    				eventUnitList.add(file);
    			}
    		}
    	}
		
		
    	
    	
    	
    	
    	return eventUnitList;
    }
}
