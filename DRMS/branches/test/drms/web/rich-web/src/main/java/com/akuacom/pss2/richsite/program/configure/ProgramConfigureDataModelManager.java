package com.akuacom.pss2.richsite.program.configure;

import com.akuacom.pss2.program.Program;

public interface ProgramConfigureDataModelManager {

	/**
	 * Function for save program configuration into DB
	 * @param model	ProgramConfigureDataModel instance
	 * @throws Exception 
	 */
	public void saveProgramConfigure(ProgramConfigureDataModel model) throws Exception;
	
	/**
	 * Function for get program configuration object from DB
	 * @param model ProgramConfigureDataModel instance
	 */
	public void getProgramConfigure(ProgramConfigureDataModel model);
	
	/**
	 * Function for build ProgramConfigureDataModel object attributes by program entity
	 * Should be invoked after get program from database
	 * @param model
	 * @param program
	 */
//	public void buildProgramConfigure(ProgramConfigureDataModel model,Program program);
	
	/**
	 * Function for build ProgramConfigureDataModel instance
	 * Should be invoked before save program into database
	 * @param model
	 * @return
	 */
	public Program buildData(ProgramConfigureDataModel model,boolean addFlag);
	
	/**
	 * function for retrieve program entity from database using  ProgramConfigureDataModel instance
	 * @param model ProgramConfigureDataModel instance
	 * @return program instance
	 */
	public Program getProgram(ProgramConfigureDataModel model);
	
	/**
	 * function for parse String value for time, get the hour string value
	 * @param timeString	String value for time
	 * @return hour string value
	 */
	public String getHourFromModelTimeString(String timeString);
	
	/**
	 * Private function for parse String value for time, get the minute string value
	 * @param timeString	String value for time
	 * @return minute string value
	 */
	public String getMinFromModelTimeString(String timeString);

    /**
	 * Private function for parse String value for time, get the minute string value
	 * @param timeString	String value for time
	 * @return seconds string value
	 */
	public String getSecFromModelTimeString(String timeString);
	
	/**
	 * Function for save Main program configuration into DB
	 * @param model	ProgramConfigureDataModel instance
	 * @throws Exception 
	 */
	public void saveMainProgram(ProgramConfigureDataModel model) throws Exception;
	
	/**
	 * Function for save Main program signals configuration into DB
	 * @param model	ProgramConfigureDataModel instance
	 * @throws Exception 
	 */
	public void saveProgramSignalsConfigure(ProgramConfigureDataModel model) throws Exception;
	
	
	
	
	/**
	 * Function for build ProgramConfigureDataModel instance
	 * Should be invoked before save program into database
	 * @param model
	 * @return
	 */
	public Program buildProgramPerf(ProgramConfigureDataModel model);
}
