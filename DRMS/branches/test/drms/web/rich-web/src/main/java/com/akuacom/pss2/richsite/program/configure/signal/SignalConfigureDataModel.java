package com.akuacom.pss2.richsite.program.configure.signal;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;
import com.akuacom.pss2.signal.SignalDef;
import java.io.Serializable;

public class SignalConfigureDataModel implements Serializable {
	
	//-------------------------------Attributes-------------------------------------------------
	
	/** SignalConfigureDataModelManager reference */
	private SignalConfigureDataModelManager manager = ProgramConfigureFactory.getInstance().getSignalConfigureDataModelManager();
	
	/** ProgramConfigureDataModel reference */
	private ProgramConfigureDataModel programConfigureDataModel;
	
	/** program signals list*/
	private List<SignalDef> programSignals = new ArrayList<SignalDef>();	//Data from database
	
	/** signals defined list */
	private List<SignalDef> definedSignals = new ArrayList<SignalDef>();	//Data from database
	
	/** JSF component SelectItem list for signals defined */
	private List<SelectItem> definedSignalSelectItems = new ArrayList<SelectItem>();	//Data for presentation layer
	
	/** JSF component SelectItem list for program signals */
	private List<SelectItem> programSignalSelectItems = new ArrayList<SelectItem>();	//Data for presentation layer
	
	/** Current select signal for UI component*/
	private String currentSelectSignal="";
	
	/** signal def data model list*/
	private List<SignalDefDataModel> signalDefDataModelList = new ArrayList<SignalDefDataModel>();	
	
	/**
	 * Constructor
	 * @param programConfigureDataModel ProgramConfigureDataModel instance
	 */
	public SignalConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		super();
		this.programConfigureDataModel=programConfigureDataModel;
		buildModel(programConfigureDataModel.getOperationFlag());
	}
	
	//-------------------------------Business Logic Method--------------------------------------
		
	/**
	 * Function for construct the relative backingBean model
	 */
	public void buildModel(String operationFlag){
		if(this.programConfigureDataModel!=null){
			manager.getSignalsFromSystem(this);
			if(operationFlag.equalsIgnoreCase("ADD")){
				buildAddsignalDefDataModelList();
			}else if(operationFlag.equalsIgnoreCase("UPDATE")){
				
				buildUpdatesignalDefDataModelList();
			}
		}
	}
	/**
	 * Event JSF framework for get Presentation Layer request for select all 
	 */
	public void selectedAll(){
		for(int i=0;i<signalDefDataModelList.size();i++){
			signalDefDataModelList.get(i).setSelect(true);
		}
	}
	
	/**
	 * Event JSF framework for get Presentation Layer request for select none 
	 */	
	public void selectedNone(){
		for(int i=0;i<signalDefDataModelList.size();i++){
			signalDefDataModelList.get(i).setSelect(false);
		}
	}	
	/**
	 * Function for add current select signal into programSignalSelectItems list
	 */
	@Deprecated
	public String addDisplayProgramSignal(){
		manager.addDisplayProgramSignal(this);
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
		
	/**
	 * Function for clean up the programSignalSelectItems 
	 */
	@Deprecated
	public String clearDisplayProgramSignals(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			setProgramSignalSelectItems(new ArrayList<SelectItem>());
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
	
	/**
	 * function for build signal def data mode list for add operation
	 * when the program is an add operation, the model list will retrieve all the signal def 
	 * and set the default list contains all and set selected attribute is true
	 */
	private void buildAddsignalDefDataModelList(){
		signalDefDataModelList = new ArrayList<SignalDefDataModel>();	
		for(int i =0;i<definedSignals.size();i++){
			signalDefDataModelList.add(new SignalDefDataModel(definedSignals.get(i),true));
		}
	}
	
	/**
	 * function for build signal def data mode list for update operation
	 * when the program is an update operation, the model list will retrieve all the signal def 
	 * and set the default list contains all and set selected attribute is true for which contains in the program really contained
	 */
	private void buildUpdatesignalDefDataModelList(){
		signalDefDataModelList = new ArrayList<SignalDefDataModel>();	
		for(int i =0;i<definedSignals.size();i++){
			signalDefDataModelList.add(new SignalDefDataModel(definedSignals.get(i),false));
		}
		
		for(int i =0;i<programSignals.size();i++){
			SignalDef sd = programSignals.get(i);
			String signalName = sd.getSignalName();
			for(int j =0;j<signalDefDataModelList.size();j++){
				SignalDefDataModel sddm =signalDefDataModelList.get(j);
				String signalNameCompare = sddm.getSignalDef().getSignalName();
				if(signalName.equalsIgnoreCase(signalNameCompare)){
					sddm.setSelect(true);
					break;
				}
			}
		}
	}		
	//-------------------------------Getter and Setter------------------------------------------

	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}

	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}

	public List<SignalDef> getProgramSignals() {
		return programSignals;
	}
	public void setProgramSignals(List<SignalDef> programSignals) {
		this.programSignals = programSignals;
	}
	public List<SignalDef> getDefinedSignals() {
		return definedSignals;
	}
	public void setDefinedSignals(List<SignalDef> definedSignals) {
		this.definedSignals = definedSignals;
	}
	public List<SelectItem> getDefinedSignalSelectItems() {
		return definedSignalSelectItems;
	}
	public void setDefinedSignalSelectItems(
			List<SelectItem> definedSignalSelectItems) {
		this.definedSignalSelectItems = definedSignalSelectItems;
	}
	public List<SelectItem> getProgramSignalSelectItems() {
		return programSignalSelectItems;
	}
	public void setProgramSignalSelectItems(
			List<SelectItem> programSignalSelectItems) {
		this.programSignalSelectItems = programSignalSelectItems;
	}
	public String getCurrentSelectSignal() {
		return currentSelectSignal;
	}
	public void setCurrentSelectSignal(String currentSelectSignal) {
		this.currentSelectSignal = currentSelectSignal;
	}
	
	public void setSignalDefDataModelList(List<SignalDefDataModel> signalDefDataModelList) {
		this.signalDefDataModelList = signalDefDataModelList;
	}

	public List<SignalDefDataModel> getSignalDefDataModelList() {
		return signalDefDataModelList;
	}
	

}
