package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.jsf.event.BaseEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.SDPEventGroupBackingBean;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.EventCache;

public class ResidentialUIBackingBean implements Serializable {

	private static final long serialVersionUID = 3820659163463429082L;
			
	/** Active SDP group backing bean */
	private SDPEventGroupBackingBean activeSDPPREventGroup = new SDPEventGroupBackingBean();
	/** Active SAI group backing bean */
	private BaseEventGroupBackingBean activeSAIPREventGroup = new BaseEventGroupBackingBean();
	/** Schedule SDP group backing bean */
	private SDPEventGroupBackingBean scheduleSDPPREventGroup = new SDPEventGroupBackingBean();
	/** Schedule SAI group backing bean */
	private BaseEventGroupBackingBean scheduleSAIPREventGroup = new BaseEventGroupBackingBean();
	/** Active SPD group backing bean */
	private BaseEventGroupBackingBean activeSPDPREventGroup = new BaseEventGroupBackingBean();
	/** Schedule SPD group backing bean */
	private BaseEventGroupBackingBean scheduleSPDPREventGroup = new BaseEventGroupBackingBean();	
	public void initialize(){
		
	}
	/**
	 * @return the activeSDPPREventGroup
	 */
	public SDPEventGroupBackingBean getActiveSDPPREventGroup() {
		activeSDPPREventGroup.setEvents(EventCache.getInstance().getActSdprEvents().getValueList());
		return activeSDPPREventGroup;
	}
	/**
	 * @param activeSDPPREventGroup the activeSDPPREventGroup to set
	 */
	public void setActiveSDPPREventGroup(
			SDPEventGroupBackingBean activeSDPPREventGroup) {
		this.activeSDPPREventGroup = activeSDPPREventGroup;
	}
	/**
	 * @return the activeSAIPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveSAIPREventGroup() {
		activeSAIPREventGroup.setEvents(EventCache.getInstance().getActiveSAIResiEvents());
		return activeSAIPREventGroup;
	}
	/**
	 * @param activeSAIPREventGroup the activeSAIPREventGroup to set
	 */
	public void setActiveSAIPREventGroup(
			BaseEventGroupBackingBean activeSAIPREventGroup) {
		this.activeSAIPREventGroup = activeSAIPREventGroup;
	}
	/**
	 * @return the scheduleSDPPREventGroup
	 */
	public SDPEventGroupBackingBean getScheduleSDPPREventGroup() {
//		scheduleSDPPREventGroup.setEvents(EventCache.getInstance().getScheSdprEvents().getValueList());
//		return scheduleSDPPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheSdprEvents().getValueList();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleSDPPREventGroup.setEvents(events);
		return scheduleSDPPREventGroup; 
	}
	/**
	 * @param scheduleSDPPREventGroup the scheduleSDPPREventGroup to set
	 */
	public void setScheduleSDPPREventGroup(
			SDPEventGroupBackingBean scheduleSDPPREventGroup) {
		this.scheduleSDPPREventGroup = scheduleSDPPREventGroup;
	}
	/**
	 * @return the scheduleSAIPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleSAIPREventGroup() {
//		scheduleSAIPREventGroup.setEvents(EventCache.getInstance().getScheduleSAIResiEvents());
//		return scheduleSAIPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheduleSAIResiEvents();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleSAIPREventGroup.setEvents(events);
		return scheduleSAIPREventGroup; 
	}
	/**
	 * @param scheduleSAIPREventGroup the scheduleSAIPREventGroup to set
	 */
	public void setScheduleSAIPREventGroup(
			BaseEventGroupBackingBean scheduleSAIPREventGroup) {
		this.scheduleSAIPREventGroup = scheduleSAIPREventGroup;
	}
	/**
	 * @return the activeSPDPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveSPDPREventGroup() {
		activeSPDPREventGroup.setEvents(EventCache.getInstance().getActiveSPDResiEvents());
		return activeSPDPREventGroup;
	}
	/**
	 * @param activeSPDPREventGroup the activeSPDPREventGroup to set
	 */
	public void setActiveSPDPREventGroup(
			BaseEventGroupBackingBean activeSPDPREventGroup) {
		this.activeSPDPREventGroup = activeSPDPREventGroup;
	}
	/**
	 * @return the scheduleSPDPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleSPDPREventGroup() {
//		scheduleSPDPREventGroup.setEvents(EventCache.getInstance().getScheduleSPDResiEvents());
//		return scheduleSPDPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheduleSPDResiEvents();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleSPDPREventGroup.setEvents(events);
		return scheduleSPDPREventGroup; 
		
	}
	/**
	 * @param scheduleSPDPREventGroup the scheduleSPDPREventGroup to set
	 */
	public void setScheduleSPDPREventGroup(
			BaseEventGroupBackingBean scheduleSPDPREventGroup) {
		this.scheduleSPDPREventGroup = scheduleSPDPREventGroup;
	}	
	
}
