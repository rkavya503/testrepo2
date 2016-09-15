package com.akuacom.pss2.richsite.program.configure.signal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.signal.SignalManagerBean;
import java.io.Serializable;



public class SignalConfigureDataModelManagerImpl implements SignalConfigureDataModelManager,Serializable{
	
	/** SignalManager instance reference*/
	private SignalManager signalManager = EJB3Factory.getBean(SignalManagerBean.class);
	
	/** ProgramManager instance reference*/
	private ProgramManager programManager = EJB3Factory.getBean(ProgramManagerBean.class);
	
	/**
	 * function for retrieve signals from system
	 * @param model SignalConfigureDataModel instance
	 */
	public void getSignalsFromSystem(SignalConfigureDataModel model){
		String programName =model.getProgramConfigureDataModel().getProgramName();
		//set defined signals
		model.setDefinedSignals(getSignalDefsFromSystem());
		//set program signals
		if(!programName.equalsIgnoreCase("")){
			model.setProgramSignals(getSignalsFromProgram(programName)); 
		}
	}
	
	/**
	 * function for add signal into program
	 * @param model SignalConfigureDataModel instance
	 */	
	public void addDisplayProgramSignal(SignalConfigureDataModel model){
		if(model.getProgramConfigureDataModel()!=null){
			model.getProgramConfigureDataModel().setRenewFlag(false);
			if(!model.getCurrentSelectSignal().equalsIgnoreCase("")){
				String itemInProgram =getSelectItem(model.getProgramSignalSelectItems(),model.getCurrentSelectSignal());
				if(itemInProgram.equalsIgnoreCase("")){
					model.getProgramSignalSelectItems().add(new SelectItem(model.getCurrentSelectSignal(),model.getCurrentSelectSignal()));
				}
			}
		}
	}
	
	/**
	 * function for construct JSF SelectItem object base on the system defined signals
	 * @param model SignalConfigureDataModel instance
	 */	
	public void buildDefinedSignalSelectItems(SignalConfigureDataModel model){
		model.setDefinedSignalSelectItems(new ArrayList<SelectItem>());
		for(int i=0;i<model.getDefinedSignals().size();i++){
			SignalDef definedSignal = model.getDefinedSignals().get(i);
			model.getDefinedSignalSelectItems().add(new SelectItem(definedSignal.getSignalName(),definedSignal.getSignalName()));
		}
	}
	/**
	 * function for construct JSF SelectItem object base on the program signals
	 * @param model SignalConfigureDataModel instance
	 */
	public void buildProgramSignalSelectItems(SignalConfigureDataModel model) {
		model.setProgramSignalSelectItems(new ArrayList<SelectItem>());
		for(int i=0;i<model.getProgramSignals().size();i++){
			SignalDef programSignal = model.getProgramSignals().get(i);
			model.getProgramSignalSelectItems().add(new SelectItem(programSignal.getSignalName(),programSignal.getSignalName()));
		}
	}	
	
	/**
	 * Private function for retrieve defined signals from system
	 * @return List<SignalDef>
	 */
	private List<SignalDef> getSignalDefsFromSystem(){
		return signalManager.findSignalsPerf();
	}
	
	/**
	 * Private function for retrieve program signals from system
	 * @param programName the name attribute of program entity
	 * @return List<SignalDef>
	 */
	private List<SignalDef> getSignalsFromProgram(String programName){
		return programManager.findSignalsPerf(programName);
	}	
	
	/**
	 * private function for retrieve select signal name from SelectItem object base on JSF component
	 * @param itemList List<SelectItem> 
	 * @param currentSelectSignal
	 * @return signal name
	 */
	private String getSelectItem(List<SelectItem> itemList,String currentSelectSignal){
		if(itemList!=null&&currentSelectSignal!=null){
			for(int i=0;i<itemList.size();i++){
				SelectItem selectItem = itemList.get(i);
				if(selectItem.getValue() !=null){
					String signalName = (String) selectItem.getValue();
					String signalNameCompare = currentSelectSignal;
					if(signalName.equalsIgnoreCase(signalNameCompare)){
						return currentSelectSignal;
					}
				}
			}
		}
		return "";
	}

	//-------------------------------Getter and Setter------------------------------------------
	public SignalManager getSignalManager() {
		return signalManager;
	}

	public void setSignalManager(SignalManager.L signalManager) {
		this.signalManager = signalManager;
	}

	public ProgramManager getProgramManager() {
		return programManager;
	}

	public void setProgramManager(ProgramManager.L programManager) {
		this.programManager = programManager;
	}
}
