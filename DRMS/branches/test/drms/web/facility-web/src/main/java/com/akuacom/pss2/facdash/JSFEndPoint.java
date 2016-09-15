package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.openadr2.endpoint.Endpoint;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.participant.Participant;

public class JSFEndPoint implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss.SSS";
	private static final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
	private String venName;
	private String createdDate;
	private String cancelled;
	private String commStatus;
	private String registrationId;
	private String venId;
	
	public JSFEndPoint(){		
	}
	
	public JSFEndPoint(Participant client) {
		EndpointManager endPointManager = EJBFactory.getBean(EndpointManager.class);
        EndpointMapping findEndpointParticipantLinkByParticipant = endPointManager.findEndpointParticipantLinkByParticipant(client);  
        if(findEndpointParticipantLinkByParticipant != null) {
	        Endpoint endpoint = findEndpointParticipantLinkByParticipant.getEndpoint();
	        this.cancelled = Boolean.toString(endpoint.getCanceled());
	        this.commStatus = endpoint.getCommStatus().value();
	        Date creationTime = endpoint.getCreationTime();
	        String formatedcreatedDate = "";
	        if(creationTime != null){
	        	formatedcreatedDate = format.format(creationTime);
	        }
	        this.createdDate= formatedcreatedDate;
	        this.registrationId =endpoint.getRegistrationId();
	        this.venId= endpoint.getVenId();
	        this.venName= endpoint.getVenName();
        }
        
	}

	public String getVenName() {
		return venName;
	}
	public void setVenName(String venName) {
		this.venName = venName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getVenId() {
		return venId;
	}
	public void setVenId(String venId) {
		this.venId = venId;
	}
	public String getCancelled() {
		return cancelled;
	}
	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}
	public String getCommStatus() {
		return commStatus;
	}
	public void setCommStatus(String commStatus) {
		this.commStatus = commStatus;
	}
	
	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
}
