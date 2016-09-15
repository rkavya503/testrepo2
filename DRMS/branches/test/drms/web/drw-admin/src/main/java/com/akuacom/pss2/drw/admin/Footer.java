package com.akuacom.pss2.drw.admin;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SerializableDataModel;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.HeaderStyle;
import com.akuacom.pss2.util.DrasRole;

public class Footer extends SerializableDataModel {

	private static final long serialVersionUID = -7896875508812636610L;
	boolean newsEnabled = false;
	boolean participantsMapViewEnabled = false;
	boolean participantsUploadEnabled = false;
	boolean participantNotesEnabled = false;
	boolean participantInfoEnabled = false;

	String hostName;
	boolean admin;
	boolean operator;
	private DRWContext context;
	private String dateTimeFormat;
	public Footer() {
		this.newsEnabled = this.getCoreAccess("feature.news");
		this.participantsMapViewEnabled = this
				.getCoreAccess("feature.participantsMapView");
		this.participantsUploadEnabled = this
				.getCoreAccess("feature.participantsUpload");
		this.participantNotesEnabled = this
				.getCoreAccess("feature.participantNotes");
		this.participantInfoEnabled = this
				.getCoreAccess("feature.participantInfo");

		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		if (request.isUserInRole(DrasRole.Admin.toString())) {
			this.admin = true;
		}
		if (request.isUserInRole(DrasRole.Operator.toString())) {
			this.operator = true;
		}
		setContext(new DRWContext());
		dateTimeFormat=getSystemManager().getPss2Features().getDateFormat()+" HH:mm";
	}

	private SystemManager systemManager;

	public SystemManager getSystemManager() {
		if (systemManager == null)
			systemManager = ServiceLocator.findHandler(SystemManager.class,
					"pss2/SystemManagerBean/remote");
		return systemManager;
	}

	public boolean getCoreAccess(String coreValue) {
		if (systemManager == null) {
			systemManager = getSystemManager();
		}
		boolean flag = false;
		for (CoreProperty corp : systemManager.getAllProperties()) {
			if (corp.getPropertyName().equalsIgnoreCase(coreValue))
				flag = corp.isBooleanValue();
		}
		return flag;
	}

	public String getUtilityName() {
		if (systemManager == null) {
			systemManager = getSystemManager();
		}
		String uName = "";
		for (CoreProperty corp : systemManager.getAllProperties()) {
			if (corp.getPropertyName().equalsIgnoreCase("utilityName"))
				uName = corp.getStringValue();
		}
		return uName;
	}

	public boolean isNewsEnabled() {
		return newsEnabled;
	}

	public void setNewsEnabled(boolean newsEnabled) {
		this.newsEnabled = newsEnabled;
	}

	public boolean isParticipantsMapViewEnabled() {
		return this.participantsMapViewEnabled;
	}

	public void setParticipantsMapViewEnabled(boolean participantsMapViewEnabled) {
		this.participantsMapViewEnabled = participantsMapViewEnabled;
	}

	public String getHostName() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		hostName = request.getServerName();
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public boolean isParticipantsUploadEnabled() {
		return participantsUploadEnabled;
	}

	public void setParticipantsUploadEnabled(boolean participantsUploadEnabled) {
		this.participantsUploadEnabled = participantsUploadEnabled;
	}

	public boolean isParticipantNotesEnabled() {
		return participantNotesEnabled;
	}

	public void setParticipantNotesEnabled(boolean participantNotesEnabled) {
		this.participantNotesEnabled = participantNotesEnabled;
	}

	public boolean isParticipantInfoEnabled() {
		return participantInfoEnabled;
	}

	public void setParticipantInfoEnabled(boolean participantInfoEnabled) {
		this.participantInfoEnabled = participantInfoEnabled;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isOperator() {
		return operator;
	}

	public void setOperator(boolean operator) {
		this.operator = operator;
	}

	@Override
	public void update() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setRowKey(Object o) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getRowKey() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void walk(FacesContext fc, DataVisitor dv, Range range, Object o)
			throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isRowAvailable() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getRowCount() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getRowData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getRowIndex() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setRowIndex(int i) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setWrappedData(Object o) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
   	private String headerStyle;
	public String getHeaderStyle(){
		Boolean isProductionServer = getSystemManager().getPss2Features().isProductionServer();
    	if(isProductionServer){
    	    headerStyle = HeaderStyle.PRODUCTION_SERVER_UTILITY_OPERATOR.getStyleName();
    	}else{
    	    headerStyle = HeaderStyle.TEST_SERVER_UTILITY_OPERATOR.getStyleName();
    	}
		return this.headerStyle;
	}

	public DRWContext getContext() {
		return context;
	}

	public void setContext(DRWContext context) {
		this.context = context;
	}

	public void setSystemManager(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}
	
	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

    public String getHeaderDateTimeFormat() {
        return dateTimeFormat + " z";
    }
}
