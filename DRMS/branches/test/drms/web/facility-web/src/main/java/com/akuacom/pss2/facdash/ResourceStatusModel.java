package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.openadr2.report.telemetry.GetResourceStatusRequest;
import com.akuacom.pss2.openadr2.report.telemetry.ResourceStatusDTO;
import com.akuacom.pss2.openadr2.report.telemetry.VenResourceStatusManager;

public class ResourceStatusModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ResourceStatusModel.class
			.getName());
	private Date startDate;
	private Date endDate;
	private String resourceName;
	private String resourceVenId;
	private boolean isHistoryTable;
	private boolean showLSDataTable;
	private boolean showHistoryDatatable;
	
	private List<ResourceStatusDTO> resourceHistoryList = new ArrayList<ResourceStatusDTO>();
	
	private VenResourceStatusManager venResourceStatusMng;
	private JSFClient jsfClient;
	
	public ResourceStatusModel(){
		jsfClient = FDUtils.getJSFClient();
		venResourceStatusMng = EJBFactory.getBean(VenResourceStatusManager.class);
	}
	
	public List<ResourceStatusDTO>  getResourceHistoryList() {
		return resourceHistoryList;
	}
	public void setResourceHistoryList() {
		resourceHistoryList = new ArrayList<ResourceStatusDTO>();
		List<ResourceStatusDTO> tempresourceHistoryList = null;
		GetResourceStatusRequest request = new GetResourceStatusRequest();
		request.setStart(startDate);
		request.setEnd(toMidnight(endDate));
		request.setResourceId(resourceName);
		if(resourceName!= null && resourceName.length() > 0) {
			jsfClient.load(resourceName);
			List<JSFEndPoint> endPointsList = jsfClient.getEndPoints();
			for (JSFEndPoint endPoint : endPointsList) {
				resourceVenId = endPoint.getVenId();
			}
		}
		request.setVenId(resourceVenId);
		try{
			tempresourceHistoryList = venResourceStatusMng.getResourceStatusByTimeRange(request);
		} catch (Exception exe) {
			log.error(exe.getMessage());
			exe.printStackTrace();
		}
		
		if (tempresourceHistoryList != null) {
			this.resourceHistoryList = tempresourceHistoryList;
		}
			
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void getLatestStatus(){
		setResourceLatestStatus();
	}
	
	public void getResourceHistory() {
		this.isHistoryTable = true;
		setResourceHistoryList();
		showLSOrHistDataTable();
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public void setResourceLatestStatus() {
		ResourceStatusDTO venRes = null;
		this.resourceHistoryList = new ArrayList<ResourceStatusDTO>();
		this.isHistoryTable = false;
		GetResourceStatusRequest request = new GetResourceStatusRequest();
		request.setStart(startDate);
		request.setEnd(toMidnight(endDate));
		if(resourceName!= null && resourceName.length() > 0) {
			jsfClient.load(resourceName);
			List<JSFEndPoint> endPointsList = jsfClient.getEndPoints();
			for (JSFEndPoint endPoint : endPointsList) {
				resourceVenId = endPoint.getVenId();
			}
		}
		request.setVenId(resourceVenId);
		request.setResourceId(resourceName);
		try {
			venRes = venResourceStatusMng.getLatestResourceStatus(request);
		} catch (Exception exe) {
			log.error(exe.getMessage());
			exe.printStackTrace();
		}
		if (venRes != null) {
			this.resourceHistoryList.add(venRes);				
		}
		showLSOrHistDataTable();
	}
	
	public void resetAll(){
		resourceName = null;
		startDate = null;
		endDate = null;
		resourceHistoryList = null;
		resourceVenId = null;
	}
	public String getResourceVenId() {
		return resourceVenId;
	}
	public void setResourceVenId(String resourceVenId) {
		this.resourceVenId = resourceVenId;
	}
	
	public void showLSOrHistDataTable(){
		if(!isHistoryTable) {
			this.showLSDataTable = true;
			this.showHistoryDatatable = false;
		} else {
			this.showLSDataTable = false;
			this.showHistoryDatatable = true;
		}
	}
	public boolean isHistoryTable() {
		return isHistoryTable;
	}
	public void setHistoryTable(boolean isHistoryTable) {
		this.isHistoryTable = isHistoryTable;
	}
	public boolean isShowLSDataTable() {
		return showLSDataTable;
	}
	public void setShowLSDataTable(boolean showLSDataTable) {
		this.showLSDataTable = showLSDataTable;
	}
	public boolean isShowHistoryDatatable() {
		return showHistoryDatatable;
	}
	public void setShowHistoryDatatable(boolean showHistoryDatatable) {
		this.showHistoryDatatable = showHistoryDatatable;
	}
	
	public Date toMidnight(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		boolean midnight = cal.get(Calendar.SECOND) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.HOUR_OF_DAY) == 0;
		if ( ! midnight ) {
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0); //24-hour time
		}
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}
	
}
