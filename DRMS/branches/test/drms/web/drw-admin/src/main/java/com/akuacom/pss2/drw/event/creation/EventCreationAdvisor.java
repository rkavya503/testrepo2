package com.akuacom.pss2.drw.event.creation;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <tt>EventCreationAdvisor</tt> is an advisor event creation for various of programs. 
 * Different event may have different <tt>EventCreationModel</tt>.  
 */
public abstract class EventCreationAdvisor implements Serializable {
	
	private static final long serialVersionUID = -7762466431117862747L;
	
	private String programName;

	private EvtCreation eventModel;
	
	public String getProgramName(){
		if(this.programName==null){
			FacesContext context = FacesContext.getCurrentInstance();
			if(context!=null){
				ExternalContext ec = context.getExternalContext();
				if(ec!=null){
					HttpServletRequest request = (HttpServletRequest)ec.getRequest();
	                if(request!=null){
	                    programName =request.getParameter("programName");
	                    if (programName == null )
	                    	programName="SDP";
	               }
				}
			}
		}
		return programName;
	}
	
	public EvtCreation getEventModel(){
		if(eventModel!=null)
			return eventModel;
		eventModel =  createCreationModel();
		return eventModel;
	}
	
	protected abstract EvtCreation createCreationModel();
}
