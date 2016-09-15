package com.akuacom.pss2.richsite.program.configure.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.signal.SignalManagerBean;

public class ModeConfigureDataModel implements Serializable{

	private static final long serialVersionUID = -7079379412848195953L;
	//-------------------------------Attributes-------------------------------------------------
	
	/** constant of signal mode name*/
	public static final String SIGAL_NAME_MODE="mode";

	/** ProgramConfigureDataModel instance*/
	private ProgramConfigureDataModel programConfigureDataModel;

	/** SignalManager instance*/
	private SignalManager signalManager = EJB3Factory.getBean(SignalManagerBean.class);
	
	/** program modes list*/
	private List<BaseEntity> programModes = new ArrayList<BaseEntity>();	//Data from database

	/** defined mode signal level list*/
	private List<BaseEntity> definedModeSignalLevelDefs = new ArrayList<BaseEntity>();	//Data from database
	
	/** JSF SelectItem object for defined mode signal level list*/
	private List<SelectItem> definedModeSignalLevelDefSelectItems = new ArrayList<SelectItem>();	//Data for presentation layer

	/** JSF SelectItem object for program modes list*/
	private List<SelectItem> programModeTransitionsSelectItems = new ArrayList<SelectItem>();		//Data for presentation layer
	

	/** current UI select item for signal level*/
	private String currentSelectItem="";

	/** time string value*/
	private String timeString="";
	
	/**
	 * Constructor
	 * @param programConfigureDataModel ProgramConfigureDataModel instance
	 */
	public ModeConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		super();
		this.programConfigureDataModel=programConfigureDataModel;
	}
	//-------------------------------Business Logic Method--------------------------------------
	
	/**
	 * Function for construct the relative backingBean model
	 */
	public void buildModel(){
		if(this.programConfigureDataModel!=null){
			buildDefinedModeSignalLevelDefSelectItems();
			buildProgramModeTransitionsSelectItems();
		}
	}
	
	public void buildDefinedModeSignalLevelDefSelectItems(){
		SignalDef mode =getDefinedModeSignalLevelDefsFromSystem();
		Set<SignalLevelDef> defs = mode.getSignalLevels();
		
		definedModeSignalLevelDefSelectItems = new ArrayList<SelectItem>();
		Iterator<SignalLevelDef> i = defs.iterator();
		while(i.hasNext()){
			SignalLevelDef signalLevelDef =i.next();
			definedModeSignalLevelDefSelectItems.add(new SelectItem(signalLevelDef.getStringValue(),signalLevelDef.getStringValue()));
		}
	}
	public void buildProgramModeTransitionsSelectItems(){
		//Need to implementation
	}
	
	public SignalDef getDefinedModeSignalLevelDefsFromSystem(){
		return signalManager.findSignal(SIGAL_NAME_MODE);
	}
	
	public String addDisplayProgramModeTransition(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			String modeTransitionString = buildProgramModeTransitionString();
			programModeTransitionsSelectItems.add(new SelectItem(modeTransitionString,modeTransitionString));
			
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
	
	private String buildProgramModeTransitionString(){
		StringBuffer sb = new StringBuffer(getTimeString());
		sb.append(",");
		sb.append(getCurrentSelectItem());
		return sb.toString();
	}
	
	/**
	 * Function for clean up 
	 */
	public String clearDisplayProgramModeTransitions(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			setProgramModeTransitionsSelectItems(new ArrayList<SelectItem>());
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
	//-------------------------------Getter and Setter------------------------------------------
	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}


	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}


	public SignalManager getSignalManager() {
		return signalManager;
	}


	public void setSignalManager(SignalManager signalManager) {
		this.signalManager = signalManager;
	}


	public List<BaseEntity> getProgramModes() {
		return programModes;
	}


	public void setProgramModes(List<BaseEntity> programModes) {
		this.programModes = programModes;
	}


	public List<BaseEntity> getDefinedModeSignalLevelDefs() {
		return definedModeSignalLevelDefs;
	}


	public void setDefinedModeSignalLevelDefs(
			List<BaseEntity> definedModeSignalLevelDefs) {
		this.definedModeSignalLevelDefs = definedModeSignalLevelDefs;
	}


	public List<SelectItem> getDefinedModeSignalLevelDefSelectItems() {
		return definedModeSignalLevelDefSelectItems;
	}


	public void setDefinedModeSignalLevelDefSelectItems(
			List<SelectItem> definedModeSignalLevelDefSelectItems) {
		this.definedModeSignalLevelDefSelectItems = definedModeSignalLevelDefSelectItems;
	}


	public List<SelectItem> getProgramModeTransitionsSelectItems() {
		return programModeTransitionsSelectItems;
	}


	public void setProgramModeTransitionsSelectItems(
			List<SelectItem> programModeTransitionsSelectItems) {
		this.programModeTransitionsSelectItems = programModeTransitionsSelectItems;
	}


	public String getCurrentSelectItem() {
		return currentSelectItem;
	}


	public void setCurrentSelectItem(String currentSelectItem) {
		this.currentSelectItem = currentSelectItem;
	}


	public String getTimeString() {
		return timeString;
	}


	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	

	


	
}
