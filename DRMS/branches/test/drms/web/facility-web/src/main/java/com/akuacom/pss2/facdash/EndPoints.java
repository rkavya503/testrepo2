package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;

public class EndPoints implements Serializable{

	private static final long serialVersionUID = 1L;
	private String venId ="";
	private String profileName;
	private Boolean canceled = false;
	private String venName;	
	private String registrationId;
	private String createdDate;
	public static final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss.SSS";
	private static final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	
	Map<String, Endpoint> findAllEndPoints = new HashMap<String, Endpoint>();
	
	@Override
	public boolean equals(Object object) {
		 if (!(object instanceof EndPoints)) {
			              return false;
		 }
		 EndPoints other = (EndPoints)object;
		 if (this.venId != other.venId && (this.venId == null ||
			!this.venId.equals(other.venId))) 
			return false;
			return true;
	}
	
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public Boolean getCanceled() {
		return canceled;
	}
	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}
	public String getVenName() {
		return venName;
	}
	public void setVenName(String venName) {
		this.venName = venName;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	private EndpointManager endpointMappingManager;
	
	private EndpointManager getEndpointMappingManager(){
	  	if(endpointMappingManager==null){
	   		endpointMappingManager = EJBFactory.getBean(EndpointManager.class);	
	  	}
	   	return endpointMappingManager;
	}	
	public void setEndpointMappingManager(EndpointManager endpointMappingManager) {
		this.endpointMappingManager = endpointMappingManager;
	}

	public List<SelectItem> getVens(){
		return getAllEndPoints();
	}
	
	public void changeVenID(){
		Endpoint endpoint = findAllEndPoints.get(venId);
		if(endpoint != null) {
			this.setCanceled(endpoint.getCanceled());
			this.setRegistrationId(endpoint.getRegistrationId());
			this.setVenName(endpoint.getVenName());
			Date creationTime = endpoint.getCreationTime();
			if(creationTime != null){
				this.setCreatedDate(format.format(creationTime));
			}
		} else {
			this.setCanceled(false);
			this.setRegistrationId("");
			this.setVenName("");			
			this.setCreatedDate("");
		}
	}
	
	private List<SelectItem> getAllEndPoints(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List<Endpoint> findAll = getEndpointMappingManager().findAll();
			items.add(new SelectItem("Select", "Please Select"));
			for(Endpoint p: findAll){
				items.add(new SelectItem(p.getVenId(), p.getVenId()));
				findAllEndPoints.put(p.getVenId(), p);
			}			
		} catch (EntityNotFoundException e) {
			 FDUtils.addMsgError("Error while retriving Endponts");
		}
    	return items;    	
    }

	public Map<String, Endpoint> getFindAllEndPoints() {
		return findAllEndPoints;
	}

	public void setFindAllEndPoints(Map<String, Endpoint> findAllEndPoints) {
		this.findAllEndPoints = findAllEndPoints;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
