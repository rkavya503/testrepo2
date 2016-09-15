package com.akuacom.pss2.richsite.program.configure.signal;


public interface SignalConfigureDataModelManager {
	
	/**
	 * function for retrieve signals from system
	 * @param model SignalConfigureDataModel instance
	 */
	public void getSignalsFromSystem(SignalConfigureDataModel model);
	
	/**
	 * function for add signal into program
	 * @param model SignalConfigureDataModel instance
	 */
	public void addDisplayProgramSignal(SignalConfigureDataModel model);
	
	/**
	 * function for construct JSF SelectItem object base on the system defined signals
	 * @param model SignalConfigureDataModel instance
	 */
	public void buildDefinedSignalSelectItems(SignalConfigureDataModel model);
	
	/**
	 * function for construct JSF SelectItem object base on the program signals
	 * @param model SignalConfigureDataModel instance
	 */
	public void buildProgramSignalSelectItems(SignalConfigureDataModel model);
}
