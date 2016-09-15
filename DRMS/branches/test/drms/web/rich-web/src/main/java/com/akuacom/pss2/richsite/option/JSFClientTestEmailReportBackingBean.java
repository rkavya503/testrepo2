package com.akuacom.pss2.richsite.option;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.email.ClientTestEmailManager;
import com.akuacom.pss2.email.JSFClientTestEmailReportEntity;
import com.akuacom.pss2.report.entities.ClientInfo;
import com.akuacom.utils.DateUtil;

public class JSFClientTestEmailReportBackingBean implements Serializable{
	
	private static final long serialVersionUID = 1196674488204480649L;
	private static ClientTestEmailManager clientTestEmailManager = EJB3Factory.getBean(ClientTestEmailManager.class);
	private List<JSFClientTestEmailReportEntity> entities = new ArrayList<JSFClientTestEmailReportEntity>();
	
	private Date startDate;
	private Date endDate;
	
	public JSFClientTestEmailReportBackingBean(){
		retrieveData();
	}
	
	private void retrieveData(){
		
	}
	
	public void search(){
		if(startDate!=null&&endDate!=null){
			startDate = DateUtil.getStartOfDay(startDate);
			endDate = DateUtil.getEndOfDay(endDate);
			entities = clientTestEmailManager.getReport(startDate, endDate);
		}
	}
	
	public void export() throws IOException{
		String filename = "clientTestEmailReport.csv";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""+ filename + "\"");
        String result =generateContent();
        response.getWriter().print(result);
        fc.responseComplete();
	}
	private String generateContent(){
    	String result ="";
    	StringBuffer sb = new StringBuffer();
    	sb.append("Start Date,");
    	if(startDate!=null){
    		sb.append(DateUtil.formatDate(startDate)+",\n");	
    	}else{
    		sb.append(",\n");
    	}
    	sb.append("End Date,");
    	if(endDate!=null){
    		sb.append(DateUtil.formatDate(endDate)+",\n");	
    	}else{
    		sb.append(",\n");
    	}
    	sb.append("Participant Name,Client Name,Programs Name,Contact Name,Contact Email,Creation Time,Client Test Emails List,\n");
    	for(JSFClientTestEmailReportEntity entity : entities){
    		    		
    		sb.append(entity.getParticipantName()+",");
    		sb.append(entity.getClientName()+",");
    		sb.append(entity.getParticipantName()+",");
    		sb.append(entity.getContactName()+",");
    		sb.append(entity.getContactAddress()+",");
    		sb.append(entity.getCreateTime()+",");
    		sb.append(entity.getSubjectList()+",");
    		sb.append("\n");
    	}
    	
    	
    	result = sb.toString();
    	return result;
    }

	/**
	 * @return the clientTestEmailManager
	 */
	public static ClientTestEmailManager getClientTestEmailManager() {
		return clientTestEmailManager;
	}

	/**
	 * @param clientTestEmailManager the clientTestEmailManager to set
	 */
	public static void setClientTestEmailManager(
			ClientTestEmailManager clientTestEmailManager) {
		JSFClientTestEmailReportBackingBean.clientTestEmailManager = clientTestEmailManager;
	}

	/**
	 * @return the entities
	 */
	public List<JSFClientTestEmailReportEntity> getEntities() {
		return entities;
	}

	/**
	 * @param entities the entities to set
	 */
	public void setEntities(List<JSFClientTestEmailReportEntity> entities) {
		this.entities = entities;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
