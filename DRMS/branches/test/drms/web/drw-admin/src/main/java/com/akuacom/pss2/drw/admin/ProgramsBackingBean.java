package com.akuacom.pss2.drw.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.drw.admin.constants.DRWConstants;
import com.akuacom.pss2.drw.admin.mock.DRWMockDataManager;
import com.akuacom.pss2.drw.admin.service.DRWProgramManager;
import com.akuacom.pss2.util.DrasRole;



public class ProgramsBackingBean implements Serializable {
	/** serial version*/
	private static final long serialVersionUID = -6506349269690215222L;
	/** program list*/
	private List<ProgramBackingBean> programList = new ArrayList<ProgramBackingBean>();
	/** flag for mock*/
	private boolean isMock = true;

	

	public boolean isProgramAddEnabled;
	public boolean isProgramViewEnabled;
	public boolean isUploadGeoEnabled;
	public boolean isUploadConfEnabled;
	public boolean isGeoConfigEnabled;
	public boolean isDeleteEnabled;
	
	/**
	 * constructor
	 */
	public ProgramsBackingBean(){
		super();
		retrieveData();
	}	
	/**
	 * Function for retrieve programs data
	 */
	private void retrieveData(){
		// http://localhost:8080/drw.admin/jsp/sdp/historyEvt/historyEvents.jsf
		//http://localhost:8080/drw.admin/jsp/sdp/activeEvt/activeEvents.jsf
		if(isMock){
			programList = DRWMockDataManager.mockPrograms();
		}else{
			programList = DRWProgramManager.getInstance().retrieveData();
			for(ProgramBackingBean program:programList){
				String programName = program.getProgramName();
				if(programName!=null){
					if(programName.equalsIgnoreCase(DRWConstants.SDP_PROGRAM_NAME)){
						program.setEventURL(DRWConstants.SDP_EVENT_URL);
					}
					else if(programName.equalsIgnoreCase(DRWConstants.ADP_PROGRAM_NAME)){
						program.setEventURL(DRWConstants.API_EVENT_URL);
					}
					else if(programName.equalsIgnoreCase(DRWConstants.BIP_PROGRAM_NAME)){
						program.setEventURL(DRWConstants.BIP_EVENT_URL);
					}
				}
			}
		}
	}
	
//	public boolean isOperatorUser() {
//		if (FacesContext.getCurrentInstance().getExternalContext()
//				.isUserInRole(DrasRole.Operator.toString())) {
//			//operatorUser = true;
//		} else {
//			operatorUser = false;
//		}
//		return operatorUser;
//	}
	
	
	// -----------------------------------------------------------Getter and Setter-----------------------------------------------------------
	
	
	public boolean isProgramAddEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isProgramAddEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Dispatcher.toString())) {
			isProgramAddEnabled = true;
		} else {
			isProgramAddEnabled = false;
		}
		return isProgramAddEnabled;
	}
	public void setProgramAddEnabled(boolean isProgramAddEnabled) {
		this.isProgramAddEnabled = isProgramAddEnabled;
	}
	
	public boolean isProgramViewEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isProgramViewEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			isProgramViewEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Dispatcher.toString())) {
			isProgramViewEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.FacilityManager.toString())) {
			isProgramViewEnabled = false;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Readonly.toString())) {
			isProgramViewEnabled = true;
		} else {
			isProgramViewEnabled = false;
		}
		return isProgramViewEnabled;
	}
	public void setProgramViewEnabled(boolean isProgramViewEnabled) {
		this.isProgramViewEnabled = isProgramViewEnabled;
	}
	
	public boolean isUploadGeoEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isUploadGeoEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			isUploadGeoEnabled = true;
		} else {
			isUploadGeoEnabled = false;
		}
		return isUploadGeoEnabled;
	}
	public void setUploadGeoEnabled(boolean isUploadGeoEnabled) {
		this.isUploadGeoEnabled = isUploadGeoEnabled;
	}
	
	public boolean isUploadConfEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isUploadConfEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			isUploadConfEnabled = true;
		} else {
			isUploadConfEnabled = false;
		}
		return isUploadConfEnabled;
	}
	public void setUploadConfEnabled(boolean isUploadConfEnabled) {
		this.isUploadConfEnabled = isUploadConfEnabled;
	}
	
	public boolean isGeoConfigEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isGeoConfigEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			isGeoConfigEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Dispatcher.toString())) {
			isGeoConfigEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.FacilityManager.toString())) {
			isGeoConfigEnabled = false;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Readonly.toString())) {
			isGeoConfigEnabled = true;
		} else {
			isGeoConfigEnabled = false;
		}
		return isGeoConfigEnabled;
	}
	public void setGeoConfigEnabled(boolean isGeoConfigEnabled) {
		this.isGeoConfigEnabled = isGeoConfigEnabled;
	}
	
	
	public boolean isDeleteEnabled() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Admin.toString())) {
			isDeleteEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			isDeleteEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Dispatcher.toString())) {
			isDeleteEnabled = true;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.FacilityManager.toString())) {
			isDeleteEnabled = false;
		} else if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Readonly.toString())) {
			isDeleteEnabled = false;
		} else {
			isDeleteEnabled = false;
		}		
		return isDeleteEnabled;
	}
	public void setDeleteEnabled(boolean isDeleteEnabled) {
		this.isDeleteEnabled = isDeleteEnabled;
	}
	
	
	
	public void setProgramList(List<ProgramBackingBean> programList) {
		this.programList = programList;
	}

	public List<ProgramBackingBean> getProgramList() {
		return programList;
	}
	
}
