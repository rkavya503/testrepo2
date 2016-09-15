/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.ClientTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.data.PDataEntryEAO;
import com.akuacom.pss2.data.usage.CurrentUsageDataEntryEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.FacDashClientViewLayout;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.PSS2Util;

/**
 * The Class ClientTable.
 */
public class ClientTable extends PagedTable implements FacDashClientViewLayout, Serializable {

	private static final long serialVersionUID = -1824621442053383415L;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ClientTable.class
			.getName());

	/** The clients. */
	private List<JSFClient> clients = new ArrayList<JSFClient>();

	/** The client name. */
	private String clientName;

	private String VIEW_TABLE_NAME = "clients";

	private boolean dataUsageEnabled = false;
	private boolean loadFlag;
	private boolean enableBatchUpdate = false;
	
	public ClientTable(){
		initClientTable();
		buildViewLayout();		
	}
	private void initClientTable() {
		clients = new ArrayList<JSFClient>();

		ClientManager cm = EJB3Factory.getBean(ClientManager.class);
		ParticipantManager participantManager = EJB3Factory
				.getBean(ParticipantManager.class);
		// For performance
		// Participant parent =
		// participantManager.getParticipant(FDUtils.getParticipantName());
		Participant parent = participantManager
				.getParticipantAndProgramParticipantsOnly(
						FDUtils.getParticipantName(), false);

		setDataUsageEnabled(parent.getDataEnabler().booleanValue());
		
		SystemManager sysManager = EJBFactory.getBean(SystemManager.class);
		PSS2Features features=sysManager.getPss2Features();
		
		enableBatchUpdate = features.isAggBatchUpdateEnabled();
        
		Date lastUsageCommTime = null;
		long timeOutTrigger = 15 * 60 * 1000; // 15 minutes
		if (this.isDataUsageEnabled()) {
			CurrentUsageDataEntryEAO det = EJB3Factory
					.getBean(CurrentUsageDataEntryEAO.class);
			lastUsageCommTime = det
					.getLastActualTimeByDatasourceOwner(FDUtils
							.getParticipantName());
			if (lastUsageCommTime == null) {
				PDataEntryEAO de = EJB3Factory.getBean(PDataEntryEAO.class);
				lastUsageCommTime = de
						.getLastActualTimeByDatasourceOwner(FDUtils
								.getParticipantName());
			}

			Double clientTimeout = null;
			Double clientTimeoutIncrement = 1.0;

			clientTimeout = features.getClientTimeout();
			if (features.getClientTimeoutIncrement() != null)
				clientTimeoutIncrement = clientTimeoutIncrement
						+ features.getClientTimeoutIncrement();
			if (clientTimeout != null)
				timeOutTrigger = new Double(clientTimeout.doubleValue()
						* clientTimeoutIncrement.doubleValue() * 60.0
						* 1000.0).longValue();
		}

		long now = System.currentTimeMillis();
		boolean offline = lastUsageCommTime == null
				|| now - lastUsageCommTime.getTime() > timeOutTrigger;

		for (Participant clientInfo : cm
				.getClientsAllInfoExceptContactsByParent(FDUtils
						.getParticipantName())) {
			JSFClient jsfClient = new JSFClient();
			jsfClient.load(clientInfo, parent, false);
			jsfClient.setEdit(true);

			if (isDataUsageEnabled()) {
				jsfClient.setLastUsageContact(lastUsageCommTime);
				if (offline) {
					jsfClient.setUsageCommStatus("OFFLINE");
				} else {
					jsfClient.setUsageCommStatus("ONLINE");
				}
			}

			clients.add(jsfClient);

		}
	}
	/**
	 * Gets the clients.
	 * 
	 * @return the clients
	 */
	public List<JSFClient> getClients() {
		return clients;
	}

	protected List<JSFClient> getPagedList() {
		ClientManager cm = EJB3Factory.getLocalBean(ClientManager.class);
		searchHandler = cm.searchClients(searchHandler,
				FDUtils.getParticipantName());
		List<Participant> pagedList = searchHandler.getResults();
		List<JSFClient> list = new ArrayList<JSFClient>();
		for (Participant part : pagedList) {
			JSFClient jsfClient = new JSFClient();
			jsfClient.load(part);
			jsfClient.setEdit(true);
			list.add(jsfClient);
		}
		clients = list;
		return list;
	}

	/**
	 * New client action.
	 * 
	 * @return the string
	 */
	public String newClientAction() {
		JSFClient jsfClient = new JSFClient();
		jsfClient.setClientType("AUTO");
		FDUtils.setJSFClient(jsfClient);
		return "newClient";
	}

	/**
	 * Delete clients action.
	 * 
	 * @return the string
	 */
	public String deleteClientsAction() throws IOException {
		ClientManager cm = EJBFactory.getBean(ClientManager.class);
		boolean error = false;
		for (JSFClient client : clients) {
			if (client.isDelete()) {
				if (cm.getClientEventNames(client.getName()).size() > 0) {
					error = true;
					break;
				}
			}
		}

		if (!error) {
			for (JSFClient client : clients) {
				if (client.isDelete()) {
					cm.removeClient(client.getName());
				}
			}
		} else {
			FDUtils.addMsgError("can't delete a client that is in an event");
		}

		initClientTable();
		
		return "deleteClient";
	}

	/**
	 * Edits the client action.
	 * 
	 * @return the string
	 */
	public String editClientAction() {
		JSFClient jsfClient = new JSFClient();
		jsfClient.load(clientName);
		jsfClient.setEdit(true);
		FDUtils.setJSFClient(jsfClient);
		return "editClient";
	}

	/**
	 * Edits the client listener.
	 * 
	 * @param e
	 *            the e
	 */
	public void editClientListener(ActionEvent e) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		clientName = (String) ctx.getExternalContext().getRequestParameterMap()
				.get("clientName");
	}

	/**
	 * Cancel client action.
	 * 
	 * @return the string
	 */
	public String cancelClientAction() {
		return "cancelClient";
	}

	/**
	 * Creates the client action.
	 * 
	 * @return the string
	 */
	public String createClientAction() {
		JSFClient jsfClient = FDUtils.getJSFClient();
		boolean errors = false;		
		
		// TODO: need a macro instead of string
		if (!ValidatePassword.validate(jsfClient.getPassword(),
				jsfClient.getConfirmPassword(),
				"AUTO".equalsIgnoreCase(jsfClient.getClientType()))) {
			errors = true;
		}

		if (!PSS2Util.isLegalName(jsfClient.getName())) {
			FDUtils.addMsgError("Characters in name "
					+ PSS2Util.LEGAL_NAME_DESCRIPTION);
			errors = true;
		}

		String clientName = FDUtils.getParticipantName() + "."
				+ jsfClient.getName();
		if ("AUTO".equalsIgnoreCase(jsfClient.getClientType())) {
			if (clientName.length() > PSS2Util.MAX_CLIR_CLIENT_NAME_LENGTH) {
				FDUtils.addMsgError("Name can be no longer than "
						+ PSS2Util.MAX_CLIR_CLIENT_NAME_LENGTH + " characters");
				errors = true;
			}
		} else {
			if (clientName.length() > PSS2Util.MAX_SOFTWARE_CLIENT_NAME_LENGTH) {
				FDUtils.addMsgError("Name can be no longer than "
						+ PSS2Util.MAX_SOFTWARE_CLIENT_NAME_LENGTH
						+ " characters");
				errors = true;
			}
		}

		if (errors) {
			return "createFailed";
		}

		ClientManager cm = EJB3Factory.getLocalBean(ClientManager.class);
		Participant client = new Participant();
		client.setClient(true);
		client.setParent(FDUtils.getParticipantName());
		client.setParticipantName(clientName);

		// DRMS-6121
		client.setDeviceType(jsfClient.getDeviceType());
		if ("MANUAL".equalsIgnoreCase(jsfClient.getClientType()))
			client.setType((byte) 2);
		else
			client.setType((byte) 0);

		try {
			// DRMS-7218: it's possible that participant's account name can be
			// same as client's
			ParticipantEAO participantEAO = EJB3Factory
					.getLocalBean(ParticipantEAO.class);
			boolean found = participantEAO.checkAccount(clientName);
			if (found) {
				String message = "ERROR_CLIENT_NAME_DUPLICATED";

				log.warn(LogUtils.createLogEntry("", "", message, null));
				throw new ValidationException(message);
			}
			if(jsfClient.isAdminOrOperator()) {
				EndPoints endpoints = jsfClient.getEndpoints();
				if(endpoints != null){
					client.setAccountNumber(endpoints.getVenId());
				}
			}
			cm.createClient(client, jsfClient.getPassword().toCharArray());
		} catch (Exception e) {
			ValidationException ve = ErrorUtil.getValidationException(e);
			if (ve != null) {
				FDUtils.addMsgError(ve.getLocalizedMessage());
				return "createFailed";
			} else {
				FDUtils.addMsgError("Internal error");
			}
		}
		jsfClient.load(clientName);
		jsfClient.setEdit(true);
		return "createClient";
	}

	public boolean isDataUsageEnabled() {
		return dataUsageEnabled;
	}

	public void setDataUsageEnabled(boolean dataUsageEnabled) {
		this.dataUsageEnabled = dataUsageEnabled;
	}

	/**
	 * @param loadFlag
	 *            the loadFlag to set
	 */
	public void setLoadFlag(boolean loadFlag) {
		this.loadFlag = loadFlag;
	}

	/**
	 * @return the loadFlag
	 */
	public boolean isLoadFlag() {
		return loadFlag;
	}
	
	public void changeRetainedAction(){
		ParticipantManager participantManager = EJBFactory
	                .getBean(ParticipantManager.class);
        Participant client = participantManager
                .getParticipant(name, true);
        List<JSFClient> jcs = getClients();
       
        if(jcs==null||name==null) return;
    	boolean retained = false;
        for(JSFClient jc: jcs) {
    	   if(name.equalsIgnoreCase(jc.getName())){
    		   retained = jc.isRetained();
    		   break;
    	   }
        }
        client.setRetained(retained);
        
        participantManager.updateParticipant(client);
	}
	
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnableBatchUpdate() {
		return enableBatchUpdate;
	}
	public void setEnableBatchUpdate(boolean enableBatchUpdate) {
		this.enableBatchUpdate = enableBatchUpdate;
	}
	
	private boolean deleteChecked;
	private boolean addClient;
	private boolean deleteClinet;

	public boolean isDeleteChecked() {
		return deleteChecked;
	}
	public void setDeleteChecked(boolean deleteChecked) {
		this.deleteChecked = deleteChecked;
	}	
	public boolean isAddClient() {
		return addClient;
	}
	public void setAddClient(boolean addClient) {
		this.addClient = addClient;
	}
	public boolean isDeleteClinet() {
		return deleteClinet;
	}
	public void setDeleteClinet(boolean deleteClinet) {
		this.deleteClinet = deleteClinet;
	}
	
	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildFacClinetLayout(this);
        } catch (NamingException e) {               
        	// log exception
        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}
	
}
