package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.jsf.event.APIEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.BIPEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.BaseEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.RTPEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.SDPEventGroupBackingBean;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.RTPEventDataModel;

public class CommercialUIBackingBean implements Serializable {

	private static final long serialVersionUID = 3820659163463429082L;
	/** Active SDP group backing bean */
	private SDPEventGroupBackingBean activeSDPPREventGroup = new SDPEventGroupBackingBean();
	/** Active BIP group backing bean */
	private BIPEventGroupBackingBean activeBIPPREventGroup = new BIPEventGroupBackingBean();
	/** Active SAI group backing bean */
	private APIEventGroupBackingBean activeAPIPREventGroup = new APIEventGroupBackingBean();
	/** Active SAI group backing bean */
	private BaseEventGroupBackingBean activeSAIPREventGroup = new BaseEventGroupBackingBean();
	/** Active CBP group backing bean */
	private BaseEventGroupBackingBean activeCBPPREventGroup = new BaseEventGroupBackingBean();
	/** Active DBP group backing bean */
	private BaseEventGroupBackingBean activeDBPPREventGroup = new BaseEventGroupBackingBean();
	/** Active DRC group backing bean */
	private BaseEventGroupBackingBean activeDRCPREventGroup = new BaseEventGroupBackingBean();
	/** Active RTP group backing bean */
	private RTPEventGroupBackingBean activeRTPPREventGroup = new RTPEventGroupBackingBean(new ArrayList<RTPEventDataModel>());
	
	/** Schedule SDP group backing bean */
	private SDPEventGroupBackingBean scheduleSDPPREventGroup = new SDPEventGroupBackingBean();
	/** Schedule BIP group backing bean */
	private BIPEventGroupBackingBean scheduleBIPPREventGroup = new BIPEventGroupBackingBean();
	/** Schedule API group backing bean */
	private APIEventGroupBackingBean scheduleAPIPREventGroup = new APIEventGroupBackingBean();
	/** Schedule SAI group backing bean */
	private BaseEventGroupBackingBean scheduleSAIPREventGroup = new BaseEventGroupBackingBean();
	/** Schedule CBP group backing bean */
	private BaseEventGroupBackingBean scheduleCBPPREventGroup = new BaseEventGroupBackingBean();
	/** Schedule DBP group backing bean */
	private BaseEventGroupBackingBean scheduleDBPPREventGroup = new BaseEventGroupBackingBean();
	/** Schedule DRC group backing bean */
	private BaseEventGroupBackingBean scheduleDRCPREventGroup = new BaseEventGroupBackingBean();
	
	private RTPEventDataModel currentRTP = new RTPEventDataModel();

	public void initialize(){
	}

	
	/**
	 * @return the activeSDPPREventGroup
	 */
	public SDPEventGroupBackingBean getActiveSDPPREventGroup() {
		activeSDPPREventGroup.setEvents(EventCache.getInstance().getActSdpcEvents().getValueList());
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
	 * @return the activeBIPPREventGroup
	 */
	public BIPEventGroupBackingBean getActiveBIPPREventGroup() {
		activeBIPPREventGroup.setEvents(EventCache.getInstance().getActBipEvents().getValueList());
		return activeBIPPREventGroup;
	}

	/**
	 * @param activeBIPPREventGroup the activeBIPPREventGroup to set
	 */
	public void setActiveBIPPREventGroup(
			BIPEventGroupBackingBean activeBIPPREventGroup) {
		this.activeBIPPREventGroup = activeBIPPREventGroup;
	}

	/**
	 * @return the activeAPIPREventGroup
	 */
	public APIEventGroupBackingBean getActiveAPIPREventGroup() {
		activeAPIPREventGroup.setEvents(EventCache.getInstance().getActApiEvents().getValueList());
		return activeAPIPREventGroup;
	}

	/**
	 * @param activeAPIPREventGroup the activeAPIPREventGroup to set
	 */
	public void setActiveAPIPREventGroup(
			APIEventGroupBackingBean activeAPIPREventGroup) {
		this.activeAPIPREventGroup = activeAPIPREventGroup;
	}

	/**
	 * @return the activeSAIPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveSAIPREventGroup() {
		activeSAIPREventGroup.setEvents(EventCache.getInstance().getActiveSAICommEvents());
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
	 * @return the activeCBPPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveCBPPREventGroup() {
		activeCBPPREventGroup.setEvents(EventCache.getInstance().getActCbpEvents().getValueList());
		return activeCBPPREventGroup;
	}

	/**
	 * @param activeCBPPREventGroup the activeCBPPREventGroup to set
	 */
	public void setActiveCBPPREventGroup(
			BaseEventGroupBackingBean activeCBPPREventGroup) {
		this.activeCBPPREventGroup = activeCBPPREventGroup;
	}

	/**
	 * @return the activeDBPPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveDBPPREventGroup() {
		activeDBPPREventGroup.setEvents(EventCache.getInstance().getActiveDBPCommEvents());
		return activeDBPPREventGroup;
	}

	/**
	 * @param activeDBPPREventGroup the activeDBPPREventGroup to set
	 */
	public void setActiveDBPPREventGroup(
			BaseEventGroupBackingBean activeDBPPREventGroup) {
		this.activeDBPPREventGroup = activeDBPPREventGroup;
	}

	/**
	 * @return the activeDRCPREventGroup
	 */
	public BaseEventGroupBackingBean getActiveDRCPREventGroup() {
		activeDRCPREventGroup.setEvents(EventCache.getInstance().getActiveDRCCommEvents());
		return activeDRCPREventGroup;
	}

	/**
	 * @param activeDRCPREventGroup the activeDRCPREventGroup to set
	 */
	public void setActiveDRCPREventGroup(
			BaseEventGroupBackingBean activeDRCPREventGroup) {
		this.activeDRCPREventGroup = activeDRCPREventGroup;
	}

	/**
	 * @return the activeRTPPREventGroup
	 */
	public RTPEventGroupBackingBean getActiveRTPPREventGroup() {
		activeRTPPREventGroup.setRtpList(EventCache.getInstance().getForcastRTPEvents());
		return activeRTPPREventGroup;
	}

	/**
	 * @param activeRTPPREventGroup the activeRTPPREventGroup to set
	 */
	public void setActiveRTPPREventGroup(
			RTPEventGroupBackingBean activeRTPPREventGroup) {
		this.activeRTPPREventGroup = activeRTPPREventGroup;
	}

	/**
	 * @return the scheduleSDPPREventGroup
	 */
	public SDPEventGroupBackingBean getScheduleSDPPREventGroup() {
//		scheduleSDPPREventGroup.setEvents(EventCache.getInstance().getScheSdpcEvents().getValueList());
//		return scheduleSDPPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheSdpcEvents().getValueList();
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
	 * @return the scheduleBIPPREventGroup
	 */
	public BIPEventGroupBackingBean getScheduleBIPPREventGroup() {
//		scheduleBIPPREventGroup.setEvents(EventCache.getInstance().getScheBipEvents().getValueList());
//		return scheduleBIPPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheBipEvents().getValueList();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleBIPPREventGroup.setEvents(events);
		return scheduleBIPPREventGroup; 
	}

	/**
	 * @param scheduleBIPPREventGroup the scheduleBIPPREventGroup to set
	 */
	public void setScheduleBIPPREventGroup(
			BIPEventGroupBackingBean scheduleBIPPREventGroup) {
		this.scheduleBIPPREventGroup = scheduleBIPPREventGroup;
	}

	/**
	 * @return the scheduleAPIPREventGroup
	 */
	public APIEventGroupBackingBean getScheduleAPIPREventGroup() {
//		scheduleAPIPREventGroup.setEvents(EventCache.getInstance().getScheApiEvents().getValueList());
//		return scheduleAPIPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheApiEvents().getValueList();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleAPIPREventGroup.setEvents(events);
		return scheduleAPIPREventGroup; 
	}

	/**
	 * @param scheduleAPIPREventGroup the scheduleAPIPREventGroup to set
	 */
	public void setScheduleAPIPREventGroup(
			APIEventGroupBackingBean scheduleAPIPREventGroup) {
		this.scheduleAPIPREventGroup = scheduleAPIPREventGroup;
	}

	/**
	 * @return the scheduleSAIPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleSAIPREventGroup() {
//		scheduleSAIPREventGroup.setEvents(EventCache.getInstance().getScheduleSAICommEvents());
//		return scheduleSAIPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheduleSAICommEvents();
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
	 * @return the scheduleCBPPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleCBPPREventGroup() {
//		scheduleCBPPREventGroup.setEvents(EventCache.getInstance().getScheCbpEvents().getValueList());
//		return scheduleCBPPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheCbpEvents().getValueList();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleCBPPREventGroup.setEvents(events);
		return scheduleCBPPREventGroup; 
	}

	/**
	 * @param scheduleCBPPREventGroup the scheduleCBPPREventGroup to set
	 */
	public void setScheduleCBPPREventGroup(
			BaseEventGroupBackingBean scheduleCBPPREventGroup) {
		this.scheduleCBPPREventGroup = scheduleCBPPREventGroup;
	}

	/**
	 * @return the scheduleDBPPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleDBPPREventGroup() {
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheduleDBPCommEvents();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		//scheduleDBPPREventGroup.setEvents(EventCache.getInstance().getScheduleDBPCommEvents());
		scheduleDBPPREventGroup.setEvents(events);
		return scheduleDBPPREventGroup; 
	}

	/**
	 * @param scheduleDBPPREventGroup the scheduleDBPPREventGroup to set
	 */
	public void setScheduleDBPPREventGroup(
			BaseEventGroupBackingBean scheduleDBPPREventGroup) {
		this.scheduleDBPPREventGroup = scheduleDBPPREventGroup;
	}

	/**
	 * @return the scheduleDRCPREventGroup
	 */
	public BaseEventGroupBackingBean getScheduleDRCPREventGroup() {
//		scheduleDRCPREventGroup.setEvents(EventCache.getInstance().getScheduleDRCCommEvents());
//		return scheduleDRCPREventGroup;
		
		List<BaseEventDataModel> cachedEvents = EventCache.getInstance().getScheduleDRCCommEvents();
		List<BaseEventDataModel> events = new ArrayList<BaseEventDataModel>();
		for(BaseEventDataModel ev : cachedEvents ){
			Date issueTime = ev.getEvent().getIssueTime();
			if(issueTime==null || issueTime.before(new Date())){
				events.add(ev);
			}
		}
		scheduleDRCPREventGroup.setEvents(events);
		return scheduleDRCPREventGroup; 
	}

	/**
	 * @param scheduleDRCPREventGroup the scheduleDRCPREventGroup to set
	 */
	public void setScheduleDRCPREventGroup(
			BaseEventGroupBackingBean scheduleDRCPREventGroup) {
		this.scheduleDRCPREventGroup = scheduleDRCPREventGroup;
	}

	/**
	 * @return the currentRTP
	 */
	public RTPEventDataModel getCurrentRTP() {
		currentRTP = EventCache.getInstance().getCurrentRTP();
		return currentRTP;
	}

	/**
	 * @param currentRTP the currentRTP to set
	 */
	public void setCurrentRTP(RTPEventDataModel currentRTP) {
		this.currentRTP = currentRTP;
	}
}
