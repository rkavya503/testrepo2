package com.akuacom.pss2.drw.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;

public class MapViewUIBackingBean implements Serializable {

	private static final long serialVersionUID = -9044743096965113443L;
	private static final Logger log = Logger.getLogger(MapViewUIBackingBean.class);
	
	private Boolean displayActiveFlag = true;
	private String eventStateSelect="ActiveEvents";//ScheduledEvents
	
	public Boolean getDisplayActiveFlag() {
		return displayActiveFlag;
	}

	public void setDisplayActiveFlag(boolean displayActiveFlag) {
		this.displayActiveFlag = displayActiveFlag;
	}

	public String getEventStateSelect() {
		return eventStateSelect;
	}

	public void setEventStateSelect(String eventStateSelect) {
		this.eventStateSelect = eventStateSelect;
	}

	public void eventSwitchListener() {
		
		if(eventStateSelect.equalsIgnoreCase("ActiveEvents")){
			displayActiveFlag = true;
		}else{
			displayActiveFlag = false;
		}
		clearLegend();
	}
	
	private EventLegendBean activeEventLegend;
	
	private EventLegendBean scheEventLegend;
	
	public EventLegendBean getScheEventLegend() {
		return scheEventLegend;
	}

	public void setScheEventLegend(EventLegendBean scheEventLegend) {
		this.scheEventLegend = scheEventLegend;
	}

	private String urlParameter;
	
	public MapViewUIBackingBean(){
		initialize();
	}
	
	private void initialize(){
		
		Object active = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isActive");
		boolean isActive = false;
		if(active==null){
			isActive = true;
		}else{
			isActive = Boolean.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isActive"));
		}
		if(isActive){
			displayActiveFlag = true;
			eventStateSelect="ActiveEvents";
		}else{
			displayActiveFlag = false;
			eventStateSelect="ScheduledEvents";
		}
		
		EventLegendBean activeLegend = null;
		EventLegendBean scheduleLegend= null;
		String eventKey = null;
		try{
			
			 activeLegend = (EventLegendBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("activeEventLegend");
			 scheduleLegend = (EventLegendBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("scheduleEventLegend");
			 
			 FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("activeEventLegend");
			 FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("scheduleEventLegend");
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		if(activeLegend!=null){
			displayActiveFlag = true;
			eventStateSelect="ActiveEvents";
			this.setActiveEventLegend(activeLegend);
		}else if(scheduleLegend!=null){
			displayActiveFlag = false;
			eventStateSelect="ScheduledEvents";
			this.setScheEventLegend(scheduleLegend);
		}
		
		eventKey = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventKey");
		
		refreshLegend(eventKey);
	}

	private List<LegendItem> updateLegend(List<EventLegend> legends) {
		List<LegendItem> selected = new ArrayList<LegendItem>();
		for(EventLegend event:legends){
			LegendItem item = new LegendItem();
			item.setEventKey(event.getEventKey());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			String startDateString = sdf.format(event.getStartTime());
			SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
			String startTimeString = sdf2.format(event.getStartTime());
			
			String endTimeString=event.getEndtime();
			if("TBD".equalsIgnoreCase(endTimeString)){
				
			}else{
				if(event.getActualEndTime()!=null){
					endTimeString = sdf2.format(event.getActualEndTime());
				}else{
					endTimeString = sdf2.format(event.getEstimatedEndTime());
				}
			}
			String legendDisplayString=startDateString+": "+startTimeString+" - "+endTimeString;
			
			
			item.setLabel(legendDisplayString);
			selected.add(item);
		}
		
		return selected;
	}
	
	
	public void refreshLegend(ActionEvent event){
		refreshLegend("");
	}
	
	public void refreshLegend(String eventKey){
		Set seleted = new HashSet();
		if(eventKey!=null&&(!eventKey.equalsIgnoreCase(""))){
			seleted.add(eventKey);	
		}
		Boolean sdpc_act_selected = Boolean.FALSE;
		Boolean sdpr_act_selected = Boolean.FALSE;
		Boolean bip_act_selected = Boolean.FALSE;
		Boolean api_act_selected = Boolean.FALSE;
		Boolean cbp_act_selected = Boolean.FALSE;
		
		Boolean sdpc_sche_selected = Boolean.FALSE;
		Boolean sdpr_sche_selected = Boolean.FALSE;
		Boolean api_sche_selected = Boolean.FALSE;
		Boolean bip_sche_selected = Boolean.FALSE;
		Boolean cbp_sche_selected = Boolean.FALSE;
		
		String productNameList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("productName");
		if(productNameList!=null){
			if(productNameList.indexOf(DRWConstants.PRODUCT_CBP_ABB)>-1){
				if(displayActiveFlag){
					cbp_act_selected = Boolean.TRUE;
				}else{
					cbp_sche_selected = Boolean.TRUE;
				}
				accessibilityFromFlag="C";
			}
			if(productNameList.indexOf(DRWConstants.PRODUCT_API_ABB)>-1){
				if(displayActiveFlag){
					api_act_selected = Boolean.TRUE;
				}else{
					api_sche_selected = Boolean.TRUE;
				}
				accessibilityFromFlag="C";
			}
			if(productNameList.indexOf(DRWConstants.PRODUCT_BIP_ABB)>-1){
				if(displayActiveFlag){
					bip_act_selected = Boolean.TRUE;
				}else{
					bip_sche_selected = Boolean.TRUE;
				}
				accessibilityFromFlag="C";
			}
			if(productNameList.indexOf(DRWConstants.PRODUCT_SDP_C_ABB)>-1){
				if(displayActiveFlag){
					sdpc_act_selected = Boolean.TRUE;
				}else{
					sdpc_sche_selected = Boolean.TRUE;
				}
				accessibilityFromFlag="C";
			}
			if(productNameList.indexOf(DRWConstants.PRODUCT_SDP_R_ABB)>-1){
				if(displayActiveFlag){
					sdpr_act_selected = Boolean.TRUE;
				}else{
					sdpr_sche_selected = Boolean.TRUE;
				}
				accessibilityFromFlag="R";
			}	
		}
		
		if(activeEventLegend!=null&&displayActiveFlag){
			List<LegendItem> sdpc = activeEventLegend.getSdpcItems();
			getSelectedItems(seleted, sdpc);
			List<LegendItem> sdpr = activeEventLegend.getSdprItems();
			getSelectedItems(seleted, sdpr);
			List<LegendItem> api = activeEventLegend.getApiItems();
			getSelectedItems(seleted, api);
			List<LegendItem> bip = activeEventLegend.getBipItems();
			getSelectedItems(seleted, bip);
			List<LegendItem> cbp = activeEventLegend.getCbpItems();
			getSelectedItems(seleted, cbp);
			sdpc_act_selected = activeEventLegend.getSdpcSelected();
			sdpr_act_selected = activeEventLegend.getSdprSelected();
			api_act_selected = activeEventLegend.getApiSelected();
			bip_act_selected = activeEventLegend.getBipSelected();
			cbp_act_selected = activeEventLegend.getCbpSelected();
			
		}else if(scheEventLegend!=null&&!displayActiveFlag){
			List<LegendItem> sdpc = scheEventLegend.getSdpcItems();
			getSelectedItems(seleted, sdpc);
			List<LegendItem> sdpr = scheEventLegend.getSdprItems();
			getSelectedItems(seleted, sdpr);
			List<LegendItem> api = scheEventLegend.getApiItems();
			getSelectedItems(seleted, api);
			List<LegendItem> bip = scheEventLegend.getBipItems();
			getSelectedItems(seleted, bip);
			List<LegendItem> cbp = scheEventLegend.getCbpItems();
			getSelectedItems(seleted, cbp);
			sdpc_sche_selected = scheEventLegend.getSdpcSelected();
			sdpr_sche_selected = scheEventLegend.getSdprSelected();
			api_sche_selected = scheEventLegend.getApiSelected();
			bip_sche_selected = scheEventLegend.getBipSelected();
			cbp_sche_selected = scheEventLegend.getCbpSelected();
		}
		
		activeEventLegend = new EventLegendBean();
		
		List<LegendItem> sdprSelected = updateLegend(EventCache.getInstance().getActSDPResiEventLegends().getEventLegends());
		setSelectedItems(seleted, sdprSelected);
		activeEventLegend.setSdprSelected(sdpr_act_selected);
		activeEventLegend.setSdprItems(sdprSelected);
		
		List<LegendItem> sdpcSelected = updateLegend(EventCache.getInstance().getActSDPComeEventLegends().getEventLegends());
		setSelectedItems(seleted, sdpcSelected);
		activeEventLegend.setSdpcSelected(sdpc_act_selected);
		activeEventLegend.setSdpcItems(sdpcSelected);
		
		List<LegendItem> apiSelected = updateLegend(EventCache.getInstance().getActAPIEventLegends().getEventLegends());
		setSelectedItems(seleted, apiSelected);
		activeEventLegend.setApiSelected(api_act_selected);
		activeEventLegend.setApiItems(apiSelected);
		
		List<LegendItem> bipSelected = updateLegend(EventCache.getInstance().getActBIPEventLegends().getEventLegends());
		setSelectedItems(seleted, bipSelected);
		activeEventLegend.setBipSelected(bip_act_selected);
		activeEventLegend.setBipItems(bipSelected);
		
		List<LegendItem> cbpSelected = updateLegend(EventCache.getInstance().getActCBPEventLegends().getEventLegends());
		setSelectedItems(seleted, cbpSelected);
		activeEventLegend.setCbpSelected(cbp_act_selected);
		activeEventLegend.setCbpItems(cbpSelected);
		
		scheEventLegend = new EventLegendBean();
		
		List<LegendItem> sdprScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPResiEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdprScheSelected);
		scheEventLegend.setSdprSelected(sdpr_sche_selected);
		scheEventLegend.setSdprItems(sdprScheSelected);
		
		List<LegendItem> sdpcScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPComeEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdpcScheSelected);
		scheEventLegend.setSdpcSelected(sdpc_sche_selected);
		scheEventLegend.setSdpcItems(sdpcScheSelected);
		
		List<LegendItem> apiScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheAPIEventLegends().getEventLegends()));
		setSelectedItems(seleted, apiScheSelected);
		scheEventLegend.setApiSelected(api_sche_selected);
		scheEventLegend.setApiItems(apiScheSelected);
		
		List<LegendItem> bipScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheBIPEventLegends().getEventLegends()));
		setSelectedItems(seleted, bipScheSelected);
		scheEventLegend.setBipSelected(bip_sche_selected);
		scheEventLegend.setBipItems(bipScheSelected);
		
		List<LegendItem> cbpScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheCBPEventLegends().getEventLegends()));
		setSelectedItems(seleted, cbpScheSelected);
		scheEventLegend.setCbpSelected(cbp_sche_selected);
		scheEventLegend.setCbpItems(cbpScheSelected);
	}
	
	public void clearLegend(){
		Set seleted = new HashSet();
		
		Boolean sdpc_act_selected = Boolean.FALSE;
		Boolean sdpr_act_selected = Boolean.FALSE;
		Boolean bip_act_selected = Boolean.FALSE;
		Boolean api_act_selected = Boolean.FALSE;
		Boolean cbp_act_selected = Boolean.FALSE;
		
		Boolean sdpc_sche_selected = Boolean.FALSE;
		Boolean sdpr_sche_selected = Boolean.FALSE;
		Boolean api_sche_selected = Boolean.FALSE;
		Boolean bip_sche_selected = Boolean.FALSE;
		Boolean cbp_sche_selected = Boolean.FALSE;
		
		activeEventLegend = new EventLegendBean();
		
		List<LegendItem> sdprSelected = updateLegend(EventCache.getInstance().getActSDPResiEventLegends().getEventLegends());
		setSelectedItems(seleted, sdprSelected);
		activeEventLegend.setSdprSelected(sdpr_act_selected);
		activeEventLegend.setSdprItems(sdprSelected);
		
		List<LegendItem> sdpcSelected = updateLegend(EventCache.getInstance().getActSDPComeEventLegends().getEventLegends());
		setSelectedItems(seleted, sdpcSelected);
		activeEventLegend.setSdpcSelected(sdpc_act_selected);
		activeEventLegend.setSdpcItems(sdpcSelected);
		
		List<LegendItem> apiSelected = updateLegend(EventCache.getInstance().getActAPIEventLegends().getEventLegends());
		setSelectedItems(seleted, apiSelected);
		activeEventLegend.setApiSelected(api_act_selected);
		activeEventLegend.setApiItems(apiSelected);
		
		List<LegendItem> bipSelected = updateLegend(EventCache.getInstance().getActBIPEventLegends().getEventLegends());
		setSelectedItems(seleted, bipSelected);
		activeEventLegend.setBipSelected(bip_act_selected);
		activeEventLegend.setBipItems(bipSelected);
		
		List<LegendItem> cbpSelected = updateLegend(EventCache.getInstance().getActCBPEventLegends().getEventLegends());
		setSelectedItems(seleted, cbpSelected);
		activeEventLegend.setCbpSelected(cbp_act_selected);
		activeEventLegend.setCbpItems(cbpSelected);
		
		scheEventLegend = new EventLegendBean();
		
		List<LegendItem> sdprScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPResiEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdprScheSelected);
		scheEventLegend.setSdprSelected(sdpr_sche_selected);
		scheEventLegend.setSdprItems(sdprScheSelected);
		
		List<LegendItem> sdpcScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPComeEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdpcScheSelected);
		scheEventLegend.setSdpcSelected(sdpc_sche_selected);
		scheEventLegend.setSdpcItems(sdpcScheSelected);
		
		List<LegendItem> apiScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheAPIEventLegends().getEventLegends()));
		setSelectedItems(seleted, apiScheSelected);
		scheEventLegend.setApiSelected(api_sche_selected);
		scheEventLegend.setApiItems(apiScheSelected);
		
		List<LegendItem> bipScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheBIPEventLegends().getEventLegends()));
		setSelectedItems(seleted, bipScheSelected);
		scheEventLegend.setBipSelected(bip_sche_selected);
		scheEventLegend.setBipItems(bipScheSelected);
		
		List<LegendItem> cbpScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheCBPEventLegends().getEventLegends()));
		setSelectedItems(seleted, cbpScheSelected);
		scheEventLegend.setCbpSelected(cbp_sche_selected);
		scheEventLegend.setCbpItems(cbpScheSelected);
	}

	private Boolean setSelectedItems(Set seleted, List<LegendItem> sdprSelected) {
		Boolean selected = Boolean.FALSE;
		if(seleted!=null&&!seleted.isEmpty()){
			for(LegendItem item:sdprSelected){
				if(seleted.contains(item.getEventKey())){
					item.setSelected(Boolean.TRUE);
					selected = Boolean.TRUE;
				}
			}
		}
		return selected;
	}

	private void getSelectedItems(Set seleted, List<LegendItem> sdpc) {
		if(sdpc!=null&&!sdpc.isEmpty()){
			for(LegendItem item:sdpc){
				if(item.getSelected()){
					seleted.add(item.getEventKey());
				}
			}
		}
	}
	
	
	
	//-------------------------------------------------Getter and Setter-------------------------------------------------------

	/**
	 * @param urlParameter the urlParameter to set
	 */
	public void setUrlParameter(String urlParameter) {
		this.urlParameter = urlParameter;
	}
	private String accessibilityFromFlag="R"; // R or C
	private String fromURLCommercial="scepr-event-status.jsf";
	private String fromURLResidential="scepr-event-status-resi.jsf";
	private String fromURL="scepr-event-status.jsf";

	/**
	 * @return the fromURL
	 */
	public String getFromURL() {
		if(accessibilityFromFlag.equalsIgnoreCase("R")){
			return fromURLResidential;
		}else if(accessibilityFromFlag.equalsIgnoreCase("C")){
			return fromURLCommercial;
		}else{
			return fromURL;
		}
	}


	/**
	 * @return the accessibilityFromFlag
	 */
	public String getAccessibilityFromFlag() {
		return accessibilityFromFlag;
	}


	/**
	 * @param accessibilityFromFlag the accessibilityFromFlag to set
	 */
	public void setAccessibilityFromFlag(String accessibilityFromFlag) {
		this.accessibilityFromFlag = accessibilityFromFlag;
	}


	public EventLegendBean getActiveEventLegend() {
		return activeEventLegend;
	}

	public void setActiveEventLegend(EventLegendBean activeEventLegend) {
		this.activeEventLegend = activeEventLegend;
	}
	
	public String refreshAction(){
		
		return "";
	}
	
	
	public String dispatchToListViewPage() {
		if(displayActiveFlag){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("activeEventLegend", this.getActiveEventLegend());
		}else{
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("scheduleEventLegend", this.getScheEventLegend());	
		}
		
		String uri = "scepr-event-listview.jsf";
	    try {
	    //	FacesContext.getCurrentInstance().responseComplete();
			FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	    return null;
		
	}
	public String dispatchToMapViewPage() {
		if(displayActiveFlag){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("activeEventLegend", this.getActiveEventLegend());
		}else{
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("scheduleEventLegend", this.getScheEventLegend());	
		}
		String uri = "scepr-event-mapview.jsf";
		 try {
			 FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
				
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		    return null;
	}

	
}
