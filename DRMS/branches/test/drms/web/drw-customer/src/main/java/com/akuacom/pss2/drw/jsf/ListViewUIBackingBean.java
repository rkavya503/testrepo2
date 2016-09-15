package com.akuacom.pss2.drw.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.jsf.event.factory.ListViewGroupEventFactory;
import com.akuacom.pss2.drw.jsf.event.listview.AbstractEventGroupListView;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.validator.PREventDataModelValidator;
import com.akuacom.pss2.drw.value.EventLegend;

public class ListViewUIBackingBean implements Serializable {

	private static final long serialVersionUID = -9044743096965113443L;
	private boolean displayFlagCBP;
	private boolean displayFlagAPI;
	private boolean displayFlagBIP;
	private boolean displayFlagSDPR;
	private boolean displayFlagSDPC;
	private Boolean displayActiveFlag = true;
	private String eventStateSelect="ActiveEvents";//ScheduledEvents
	private static final Logger log = Logger.getLogger(ListViewUIBackingBean.class);
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
		Set seleted = new HashSet();
		setSelectedItems(seleted, activeEventLegend.getSdpcItems());
		setSelectedItems(seleted, activeEventLegend.getSdprItems());
		setSelectedItems(seleted, activeEventLegend.getApiItems());
		setSelectedItems(seleted, activeEventLegend.getBipItems());
		setSelectedItems(seleted, activeEventLegend.getCbpItems());
		setSelectedItems(seleted, scheEventLegend.getSdpcItems());
		setSelectedItems(seleted, scheEventLegend.getSdprItems());
		setSelectedItems(seleted, scheEventLegend.getApiItems());
		setSelectedItems(seleted, scheEventLegend.getBipItems());
		setSelectedItems(seleted, scheEventLegend.getCbpItems());
		activeEventLegend.setApiSelected(false);
		activeEventLegend.setBipSelected(false);
		activeEventLegend.setCbpSelected(false);
		activeEventLegend.setSdprSelected(false);
		activeEventLegend.setSdpcSelected(false);
		scheEventLegend.setApiSelected(false);
		scheEventLegend.setBipSelected(false);
		scheEventLegend.setCbpSelected(false);
		scheEventLegend.setSdprSelected(false);
		scheEventLegend.setSdpcSelected(false);
		this.setDisplayFlagAPI(false);
		this.setDisplayFlagBIP(false);
		this.setDisplayFlagCBP(false);
		this.setDisplayFlagSDPR(false);
		this.setDisplayFlagSDPC(false);
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
	
	public ListViewUIBackingBean(){
		initialize();
	}

	private void initialize(){
		
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
//		Object innerPass = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("innerPass");
//		if(innerPass!=null){
//			parseParameters();
//		}
		else{
			//from Commercial & Residential page
			String productNameList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("productName");
			eventKey = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eventKey");
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
			setDisplayFlagCBP(false);
			setDisplayFlagAPI(false);
			setDisplayFlagBIP(false);
			setDisplayFlagSDPC(false);
			setDisplayFlagSDPR(false);
			if(productNameList!=null){
				if(productNameList.indexOf(DRWConstants.PRODUCT_CBP_ABB)>-1){
					setDisplayFlagCBP(true);
					accessibilityFromFlag="C";
				}
				if(productNameList.indexOf(DRWConstants.PRODUCT_API_ABB)>-1){
					setDisplayFlagAPI(true);
					accessibilityFromFlag="C";
				}
				if(productNameList.indexOf(DRWConstants.PRODUCT_BIP_ABB)>-1){
					setDisplayFlagBIP(true);
					accessibilityFromFlag="C";
				}
				if(productNameList.indexOf(DRWConstants.PRODUCT_SDP_C_ABB)>-1){
					setDisplayFlagSDPC(true);
					accessibilityFromFlag="C";
				}
				if(productNameList.indexOf(DRWConstants.PRODUCT_SDP_R_ABB)>-1){
					setDisplayFlagSDPR(true);
					accessibilityFromFlag="R";
				}	
			}
			
			
		}
		
		// new
		refreshLegend(eventKey);
//		if(eventKey!=null){
//			setLegendByEvent(eventKey);
//		}
		initEventGroups();
		initializeSearchFilter();
	}

//	private void setLegendByEvent(String eventKey){
//		Set seleted = new HashSet();
//		seleted.add(eventKey);
//		if(displayActiveFlag){
//			//active
//			if(displayFlagAPI){
//				this.setSelectedItems(seleted, this.getActiveEventLegend().getApiItems());
//			}
//			if(displayFlagBIP){
//				this.setSelectedItems(seleted, this.getActiveEventLegend().getBipItems());
//			}
//			if(displayFlagCBP){
//				this.setSelectedItems(seleted, this.getActiveEventLegend().getCbpItems());
//			}
//			if(displayFlagSDPR){
//				this.setSelectedItems(seleted, this.getActiveEventLegend().getSdprItems());
//			}
//			if(displayFlagSDPC){
//				this.setSelectedItems(seleted, this.getActiveEventLegend().getSdpcItems());
//			}
//		}else{
//			//schedule
//			if(displayFlagAPI){
//				this.setSelectedItems(seleted, this.getScheEventLegend().getApiItems());
//			}
//			if(displayFlagBIP){
//				this.setSelectedItems(seleted, this.getScheEventLegend().getBipItems());
//			}
//			if(displayFlagCBP){
//				this.setSelectedItems(seleted, this.getScheEventLegend().getCbpItems());
//			}
//			if(displayFlagSDPR){
//				this.setSelectedItems(seleted, this.getScheEventLegend().getSdprItems());
//			}
//			if(displayFlagSDPC){
//				this.setSelectedItems(seleted, this.getScheEventLegend().getSdpcItems());
//			}
//		}
//	}
	
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
	
	public void checkBoxChangeListener() {
		getUrlParameter();
		updateListViewEventGroups();
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
		if(this.isDisplayFlagSDPR()&&this.getDisplayActiveFlag()){
			activeEventLegend.setSdprSelected(true);	
		}
		activeEventLegend.setSdprItems(sdprSelected);
		
		List<LegendItem> sdpcSelected = updateLegend(EventCache.getInstance().getActSDPComeEventLegends().getEventLegends());
		setSelectedItems(seleted, sdpcSelected);
		activeEventLegend.setSdpcSelected(sdpc_act_selected);
		if(this.isDisplayFlagSDPC()&&this.getDisplayActiveFlag()){
			activeEventLegend.setSdpcSelected(true);	
		}
		activeEventLegend.setSdpcItems(sdpcSelected);
		
		List<LegendItem> apiSelected = updateLegend(EventCache.getInstance().getActAPIEventLegends().getEventLegends());
		setSelectedItems(seleted, apiSelected);
		activeEventLegend.setApiSelected(api_act_selected);
		if(this.isDisplayFlagAPI()&&this.getDisplayActiveFlag()){
			activeEventLegend.setApiSelected(true);	
		}
		activeEventLegend.setApiItems(apiSelected);
		
		List<LegendItem> bipSelected = updateLegend(EventCache.getInstance().getActBIPEventLegends().getEventLegends());
		setSelectedItems(seleted, bipSelected);
		activeEventLegend.setBipSelected(bip_act_selected);
		if(this.isDisplayFlagBIP()&&this.getDisplayActiveFlag()){
			activeEventLegend.setBipSelected(true);	
		}
		activeEventLegend.setBipItems(bipSelected);
		
		List<LegendItem> cbpSelected = updateLegend(EventCache.getInstance().getActCBPEventLegends().getEventLegends());
		setSelectedItems(seleted, cbpSelected);
		activeEventLegend.setCbpSelected(cbp_act_selected);
		if(this.isDisplayFlagCBP()&&this.getDisplayActiveFlag()){
			activeEventLegend.setCbpSelected(true);	
		}
		activeEventLegend.setCbpItems(cbpSelected);
		
		scheEventLegend = new EventLegendBean();
		
		List<LegendItem> sdprScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPResiEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdprScheSelected);
		scheEventLegend.setSdprSelected(sdpr_sche_selected);
		if(this.isDisplayFlagSDPR()&&(!this.getDisplayActiveFlag())){
			scheEventLegend.setSdprSelected(true);	
		}
		scheEventLegend.setSdprItems(sdprScheSelected);
		
		List<LegendItem> sdpcScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheSDPComeEventLegends().getEventLegends()));
		setSelectedItems(seleted, sdpcScheSelected);
		scheEventLegend.setSdpcSelected(sdpc_sche_selected);
		if(this.isDisplayFlagSDPC()&&(!this.getDisplayActiveFlag())){
			scheEventLegend.setSdpcSelected(true);	
		}
		scheEventLegend.setSdpcItems(sdpcScheSelected);
		
		List<LegendItem> apiScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheAPIEventLegends().getEventLegends()));
		setSelectedItems(seleted, apiScheSelected);
		scheEventLegend.setApiSelected(api_sche_selected);
		if(this.isDisplayFlagAPI()&&(!this.getDisplayActiveFlag())){
			scheEventLegend.setApiSelected(true);	
		}
		scheEventLegend.setApiItems(apiScheSelected);
		
		List<LegendItem> bipScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheBIPEventLegends().getEventLegends()));
		setSelectedItems(seleted, bipScheSelected);
		scheEventLegend.setBipSelected(bip_sche_selected);
		if(this.isDisplayFlagBIP()&&(!this.getDisplayActiveFlag())){
			scheEventLegend.setBipSelected(true);	
		}
		scheEventLegend.setBipItems(bipScheSelected);
		
		List<LegendItem> cbpScheSelected = updateLegend(DRWUtil.getFilterScheduleLegend(EventCache.getInstance().getScheCBPEventLegends().getEventLegends()));
		setSelectedItems(seleted, cbpScheSelected);
		scheEventLegend.setCbpSelected(cbp_sche_selected);
		if(this.isDisplayFlagCBP()&&(!this.getDisplayActiveFlag())){
			scheEventLegend.setCbpSelected(true);	
		}
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

	public Set getSelectedItems(Set seleted, List<LegendItem> items) {
		if(seleted==null||items==null){
			return new TreeSet();
		}
		if(items!=null&&!items.isEmpty()){
			for(LegendItem item:items){
				if(item!=null&&item.getSelected()){
					seleted.add(item.getEventKey());
				}
			}
		}
		return seleted;
	}
	
	
	
	//-------------------------------------------------Getter and Setter-------------------------------------------------------

	/**
	 * @return the displayFlagAPI
	 */
	public boolean isDisplayFlagAPI() {
		return displayFlagAPI;
	}
	/**
	 * @param displayFlagAPI the displayFlagAPI to set
	 */
	public void setDisplayFlagAPI(boolean displayFlagAPI) {
		this.displayFlagAPI = displayFlagAPI;
	}
	/**
	 * @return the displayFlagSDPR
	 */
	public boolean isDisplayFlagSDPR() {
		return displayFlagSDPR;
	}
	/**
	 * @param displayFlagSDPR the displayFlagSDPR to set
	 */
	public void setDisplayFlagSDPR(boolean displayFlagSDPR) {
		this.displayFlagSDPR = displayFlagSDPR;
	}
	/**
	 * @return the displayFlagSDPC
	 */
	public boolean isDisplayFlagSDPC() {
		return displayFlagSDPC;
	}
	/**
	 * @param displayFlagSDPC the displayFlagSDPC to set
	 */
	public void setDisplayFlagSDPC(boolean displayFlagSDPC) {
		this.displayFlagSDPC = displayFlagSDPC;
	}

	/**
	 * @return the urlParameter
	 */
	public String getUrlParameter() {
		urlParameter="";
		if(isDisplayFlagCBP()){
			urlParameter=urlParameter+DRWConstants.PRODUCT_CBP_ABB;
		}
		if(isDisplayFlagAPI()){
			urlParameter=urlParameter+DRWConstants.PRODUCT_API_ABB;
		}
		if(isDisplayFlagBIP()){
			urlParameter=urlParameter+DRWConstants.PRODUCT_BIP_ABB;
		}
		if(isDisplayFlagSDPC()){
			urlParameter=urlParameter+DRWConstants.PRODUCT_SDP_C_ABB;
		}
		if(isDisplayFlagSDPR()){
			urlParameter=urlParameter+DRWConstants.PRODUCT_SDP_R_ABB;
		}
		return urlParameter;
	}

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

	/**
	 * @return the displayFlagBIP
	 */
	public boolean isDisplayFlagBIP() {
		return displayFlagBIP;
	}

	/**
	 * @param displayFlagBIP the displayFlagBIP to set
	 */
	public void setDisplayFlagBIP(boolean displayFlagBIP) {
		this.displayFlagBIP = displayFlagBIP;
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
	
	
	//----------------------------------------------------------------------------------------------------------
	private String searchCounty;
	private String searchCity;
	private String searchZipCode;
	
	private boolean validateErrorFlag=false;
	
	
	
	/** county items */
	private List<SelectItem> countyItems;
	/** city items */
	private List<SelectItem> cityItems;
	
	
	private AbstractEventGroupListView groupAPI;
	private AbstractEventGroupListView groupBIP;
	private AbstractEventGroupListView groupSDPC;
	private AbstractEventGroupListView groupSDPR;
	private AbstractEventGroupListView groupCBP;

	/**
	 * @return the fromURLCommercial
	 */
	public String getFromURLCommercial() {
		return fromURLCommercial;
	}

	/**
	 * @param fromURLCommercial the fromURLCommercial to set
	 */
	public void setFromURLCommercial(String fromURLCommercial) {
		this.fromURLCommercial = fromURLCommercial;
	}

	/**
	 * @return the fromURLResidential
	 */
	public String getFromURLResidential() {
		return fromURLResidential;
	}

	/**
	 * @param fromURLResidential the fromURLResidential to set
	 */
	public void setFromURLResidential(String fromURLResidential) {
		this.fromURLResidential = fromURLResidential;
	}

	/**
	 * @return the searchCounty
	 */
	public String getSearchCounty() {
		return searchCounty;
	}

	/**
	 * @param searchCounty the searchCounty to set
	 */
	public void setSearchCounty(String searchCounty) {
		this.searchCounty = searchCounty;
	}

	/**
	 * @return the searchCity
	 */
	public String getSearchCity() {
		return searchCity;
	}

	/**
	 * @param searchCity the searchCity to set
	 */
	public void setSearchCity(String searchCity) {
		this.searchCity = searchCity;
	}

	/**
	 * @return the searchZipCode
	 */
	public String getSearchZipCode() {
		return searchZipCode;
	}

	/**
	 * @param searchZipCode the searchZipCode to set
	 */
	public void setSearchZipCode(String searchZipCode) {
		this.searchZipCode = searchZipCode;
	}

	/**
	 * @return the validateErrorFlag
	 */
	public boolean isValidateErrorFlag() {
		return validateErrorFlag;
	}

	/**
	 * @param validateErrorFlag the validateErrorFlag to set
	 */
	public void setValidateErrorFlag(boolean validateErrorFlag) {
		this.validateErrorFlag = validateErrorFlag;
	}

	/**
	 * @return the countyItems
	 */
	public List<SelectItem> getCountyItems() {
		return countyItems;
	}

	/**
	 * @param countyItems the countyItems to set
	 */
	public void setCountyItems(List<SelectItem> countyItems) {
		this.countyItems = countyItems;
	}

	/**
	 * @return the cityItems
	 */
	public List<SelectItem> getCityItems() {
		return cityItems;
	}

	/**
	 * @param cityItems the cityItems to set
	 */
	public void setCityItems(List<SelectItem> cityItems) {
		this.cityItems = cityItems;
	}

	/**
	 * @return the groupAPI
	 */
	public AbstractEventGroupListView getGroupAPI() {
		return groupAPI;
	}

	/**
	 * @param groupAPI the groupAPI to set
	 */
	public void setGroupAPI(AbstractEventGroupListView groupAPI) {
		this.groupAPI = groupAPI;
	}

	/**
	 * @return the groupBIP
	 */
	public AbstractEventGroupListView getGroupBIP() {
		return groupBIP;
	}

	/**
	 * @param groupBIP the groupBIP to set
	 */
	public void setGroupBIP(AbstractEventGroupListView groupBIP) {
		this.groupBIP = groupBIP;
	}

	/**
	 * @return the groupSDPC
	 */
	public AbstractEventGroupListView getGroupSDPC() {
		return groupSDPC;
	}

	/**
	 * @param groupSDPC the groupSDPC to set
	 */
	public void setGroupSDPC(AbstractEventGroupListView groupSDPC) {
		this.groupSDPC = groupSDPC;
	}

	/**
	 * @return the groupSDPR
	 */
	public AbstractEventGroupListView getGroupSDPR() {
		return groupSDPR;
	}

	/**
	 * @param groupSDPR the groupSDPR to set
	 */
	public void setGroupSDPR(AbstractEventGroupListView groupSDPR) {
		this.groupSDPR = groupSDPR;
	}

	/**
	 * @return the groupCBP
	 */
	public AbstractEventGroupListView getGroupCBP() {
		return groupCBP;
	}

	/**
	 * @param groupCBP the groupCBP to set
	 */
	public void setGroupCBP(AbstractEventGroupListView groupCBP) {
		this.groupCBP = groupCBP;
	}



	/**
	 * @param displayActiveFlag the displayActiveFlag to set
	 */
	public void setDisplayActiveFlag(Boolean displayActiveFlag) {
		this.displayActiveFlag = displayActiveFlag;
	}

	/**
	 * @param fromURL the fromURL to set
	 */
	public void setFromURL(String fromURL) {
		this.fromURL = fromURL;
	}
	
	private void initEventGroups(){
		groupAPI = ListViewGroupEventFactory.createListViewEventGroup_API();
		groupAPI.setListView(this);
		groupBIP = ListViewGroupEventFactory.createListViewEventGroup_BIP();
		groupBIP.setListView(this);
		groupSDPR = ListViewGroupEventFactory.createListViewEventGroup_SDPR();
		groupSDPR.setListView(this);
		groupSDPC = ListViewGroupEventFactory.createListViewEventGroup_SDPC();
		groupSDPC.setListView(this);
		groupCBP = ListViewGroupEventFactory.createListViewEventGroup_CBP();
		groupCBP.setListView(this);
	}

	private void updateListViewEventGroups(){
		
	}
	public void initializeSearchFilter(){
		List<String> countyList = EventCache.getInstance().getCountyList();
		countyItems = new ArrayList<SelectItem>();
		countyItems.add(new SelectItem("Select", "Select"));
		countyItems.add(new SelectItem("All", "All"));
		for(String county:countyList){
			countyItems.add(new SelectItem(county,county));
		}
		
		List<String> cityList = EventCache.getInstance().getCityList();
		
		cityItems = new ArrayList<SelectItem>();
		cityItems.add(new SelectItem("Select", "Select"));
		cityItems.add(new SelectItem("All", "All"));
		for(String city:cityList){
			cityItems.add(new SelectItem(city,city));
		}
		
	}
	public String clearFilterAction(){
		
		setSearchZipCode("");
		
		setSearchCounty("");
		
		setSearchCity("");
		
		initializeSearchFilter();
		return "";
	}
	public String searchFilterAction(){
		
		
		resetSearchPageParameters();
		if(getSearchCounty()==null||getSearchCity()==null||getSearchZipCode()==null){
			return "";
		}
		List<ValidationException> validationErrors =PREventDataModelValidator.viewListFilterValidation(getSearchCounty(), getSearchCity(), getSearchZipCode());
		if(validationErrors.size()>0){
			validateErrorFlag = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			for (ValidationException e : validationErrors) {
				facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
			}
		}else{
			//do search
			//retrieveDataFromCache(getSearchCounty(), getSearchCity(), getSearchZipCode());
			lastSearchCounty = getSearchCounty();
			lastSearchCity = getSearchCity();
			lastSearchZipCode = getSearchZipCode();
			searchFlag = true;
		}
		//do sth...
		return "";
	}
	public void resetSearchPageParameters(){
		validateErrorFlag = false;
		searchFlag = false;
		lastSearchCounty = "";
		lastSearchCity = "";
		lastSearchZipCode ="";
	}
	private boolean searchFlag=false;
	private String lastSearchCounty= null;
	private String lastSearchCity= null;
	private String lastSearchZipCode= null;

	/**
	 * @return the searchFlag
	 */
	public boolean isSearchFlag() {
		return searchFlag;
	}

	/**
	 * @param searchFlag the searchFlag to set
	 */
	public void setSearchFlag(boolean searchFlag) {
		this.searchFlag = searchFlag;
	}

	/**
	 * @return the lastSearchCounty
	 */
	public String getLastSearchCounty() {
		return lastSearchCounty;
	}

	/**
	 * @param lastSearchCounty the lastSearchCounty to set
	 */
	public void setLastSearchCounty(String lastSearchCounty) {
		this.lastSearchCounty = lastSearchCounty;
	}

	/**
	 * @return the lastSearchCity
	 */
	public String getLastSearchCity() {
		return lastSearchCity;
	}

	/**
	 * @param lastSearchCity the lastSearchCity to set
	 */
	public void setLastSearchCity(String lastSearchCity) {
		this.lastSearchCity = lastSearchCity;
	}

	/**
	 * @return the lastSearchZipCode
	 */
	public String getLastSearchZipCode() {
		return lastSearchZipCode;
	}

	/**
	 * @param lastSearchZipCode the lastSearchZipCode to set
	 */
	public void setLastSearchZipCode(String lastSearchZipCode) {
		this.lastSearchZipCode = lastSearchZipCode;
	}
	
	
	public List<BaseEventDataModel> filter(List<BaseEventDataModel> list, String county,String city,String zipcode){
		List<BaseEventDataModel> result = new ArrayList<BaseEventDataModel>();

		if(county!=null&&!county.equalsIgnoreCase("")&&!county.equalsIgnoreCase("Select")&&!county.equalsIgnoreCase("All")){
			//Search by county
			for(BaseEventDataModel model:list){
				if(model.getEvent()!=null){
					List<String> counties = model.getEvent().getCounties();
					for(String countyString:counties){
						if(countyString!=null&&countyString.equalsIgnoreCase(county)){
							result.add(model);
						}
					}
				}
			}
		}else if(city!=null&&!city.equalsIgnoreCase("")&&!city.equalsIgnoreCase("Select")&&!city.equalsIgnoreCase("All")){
			//Search by county
			for(BaseEventDataModel model:list){
				if(model.getEvent()!=null){
					List<String> cities = model.getEvent().getCities();
					for(String cityString:cities){
						if(cityString!=null&&cityString.equalsIgnoreCase(city)){
							result.add(model);
						}
					}
				}
			}
		}else if(zipcode!=null&&!zipcode.equalsIgnoreCase("")){
			//Search by county
			for(BaseEventDataModel model:list){
				if(model.getEvent()!=null){
					List<String> zipcodes = model.getEvent().getZipCodes();
					for(String zipCodeString:zipcodes){
						if(zipCodeString!=null&&zipCodeString.equalsIgnoreCase(zipcode)){
							result.add(model);
						}
					}
				}
			}
		}else{
			result = list;
		}
		return result;
	}
	public void countyChange(ValueChangeEvent event) {
		if ((event.getOldValue() == null) || (!event.getOldValue().equals(event.getNewValue()))) {
			List<String> cityList = EventCache.getInstance().getCityList();
			cityItems = new ArrayList<SelectItem>();
			cityItems.add(new SelectItem("Select", "Select"));
			cityItems.add(new SelectItem("All", "All"));
			for(String city:cityList){
				cityItems.add(new SelectItem(city,city));
			}
			searchCity="";
			searchZipCode = "";
		}
	}
	
	public void cityChange(ValueChangeEvent event) {
		if ((event.getOldValue() == null) || (!event.getOldValue().equals(event.getNewValue()))) {
			List<String> countyList = EventCache.getInstance().getCountyList();
			countyItems = new ArrayList<SelectItem>();
			countyItems.add(new SelectItem("Select", "Select"));
			countyItems.add(new SelectItem("All", "All"));
			for(String county:countyList){
				countyItems.add(new SelectItem(county,county));
			}
			searchCounty="";
			searchZipCode = "";
		}
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
	
	public String getRequestParameters(){
		String result = "innerPass=true";
		boolean apiChecked = false;
		boolean bipChecked = false;
		boolean cbpChecked = false;
		boolean sdprChecked = false;
		boolean sdpcChecked = false;
		String apiEventKeys = "";
		String bipEventKeys = "";
		String cbpEventKeys = "";
		String sdprEventKeys = "";
		String sdpcEventKeys = "";
		refreshLegend("");
		if(displayActiveFlag){
			result = result +"&isActive=true";
			apiChecked = this.getActiveEventLegend().getApiSelected();
			bipChecked = this.getActiveEventLegend().getBipSelected();
			cbpChecked = this.getActiveEventLegend().getCbpSelected();
			sdprChecked = this.getActiveEventLegend().getSdprSelected();
			sdpcChecked = this.getActiveEventLegend().getSdpcSelected();
			List<LegendItem> items_api = this.getActiveEventLegend().getApiItems();
			for(LegendItem item:items_api){
				if(item.getSelected()){
					apiEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_bip = this.getActiveEventLegend().getBipItems();
			for(LegendItem item:items_bip){
				if(item.getSelected()){
					bipEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_cbp = this.getActiveEventLegend().getCbpItems();
			for(LegendItem item:items_cbp){
				if(item.getSelected()){
					cbpEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_sdpr = this.getActiveEventLegend().getSdprItems();
			for(LegendItem item:items_sdpr){
				if(item.getSelected()){
					sdprEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_sdpc = this.getActiveEventLegend().getSdpcItems();
			for(LegendItem item:items_sdpc){
				if(item.getSelected()){
					sdpcEventKeys+=item.getEventKey()+",";
				}
			}
		}else{
			result = result +"&isActive=false";
			apiChecked = this.getScheEventLegend().getApiSelected();
			bipChecked = this.getScheEventLegend().getBipSelected();
			cbpChecked = getScheEventLegend().getCbpSelected();
			sdprChecked = this.getScheEventLegend().getSdprSelected();
			sdpcChecked = this.getScheEventLegend().getSdpcSelected();
			List<LegendItem> items_api = this.getScheEventLegend().getApiItems();
			for(LegendItem item:items_api){
				if(item.getSelected()){
					apiEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_bip = this.getScheEventLegend().getBipItems();
			for(LegendItem item:items_bip){
				if(item.getSelected()){
					bipEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_cbp = getScheEventLegend().getCbpItems();
			for(LegendItem item:items_cbp){
				if(item.getSelected()){
					cbpEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_sdpr = this.getScheEventLegend().getSdprItems();
			for(LegendItem item:items_sdpr){
				if(item.getSelected()){
					sdprEventKeys+=item.getEventKey()+",";
				}
			}
			List<LegendItem> items_sdpc = this.getScheEventLegend().getSdpcItems();
			for(LegendItem item:items_sdpc){
				if(item.getSelected()){
					sdpcEventKeys+=item.getEventKey()+",";
				}
			}
		}
		result+="&apiChecked="+apiChecked;
		result+="&bipChecked="+bipChecked;
		result+="&cbpChecked="+cbpChecked;
		result+="&sdprChecked="+sdprChecked;
		result+="&sdpcChecked="+sdpcChecked;
		
		result+="&apiEventKeys="+apiEventKeys;
		result+="&bipEventKeys="+bipEventKeys;
		result+="&cbpEventKeys="+cbpEventKeys;
		result+="&sdprEventKeys="+sdprEventKeys;
		result+="&sdpcEventKeys="+sdpcEventKeys;
		return result;
	}
	
	private void parseParameters(){
		String isActive = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("isActive");
		
		String apiChecked = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("apiChecked");
		String bipChecked = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("bipChecked");
		String cbpChecked = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cbpChecked");
		String sdprChecked = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sdprChecked");
		String sdpcChecked = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("sdpcChecked");
		
		Set apiSet = getLegentSet("apiEventKeys");
		Set bipSet = getLegentSet("bipEventKeys");
		Set cbpSet = getLegentSet("cbpEventKeys");
		Set sdprSet = getLegentSet("sdprEventKeys");
		Set sdpcSet = getLegentSet("sdpcEventKeys");
		
		refreshLegend("");
		
		
		if("true".equalsIgnoreCase(isActive)){
			displayActiveFlag = true;
			eventStateSelect="ActiveEvents";
			if("true".equalsIgnoreCase(apiChecked)){
				setDisplayFlagAPI(true);
				this.getActiveEventLegend().setApiSelected(true);
			}else{
				setDisplayFlagAPI(false);
				this.getActiveEventLegend().setApiSelected(false);
			}
			if("true".equalsIgnoreCase(bipChecked)){
				setDisplayFlagBIP(true);
				this.getActiveEventLegend().setBipSelected(true);
			}else{
				setDisplayFlagBIP(false);
				this.getActiveEventLegend().setBipSelected(false);
			}
			if("true".equalsIgnoreCase(cbpChecked)){
				setDisplayFlagCBP(true);
				this.getActiveEventLegend().setCbpSelected(true);
			}else{
				setDisplayFlagCBP(false);
				this.getActiveEventLegend().setCbpSelected(false);
			}
			if("true".equalsIgnoreCase(sdprChecked)){
				setDisplayFlagSDPR(true);
				this.getActiveEventLegend().setSdprSelected(true);
			}else{
				setDisplayFlagSDPR(false);
				this.getActiveEventLegend().setSdprSelected(false);
			}
			if("true".equalsIgnoreCase(sdpcChecked)){
				setDisplayFlagSDPC(true);
				this.getActiveEventLegend().setSdpcSelected(true);
			}else{
				setDisplayFlagSDPC(false);
				this.getActiveEventLegend().setSdpcSelected(false);
			}
			setSelectedItems(cbpSet,this.getActiveEventLegend().getCbpItems());
			setSelectedItems(apiSet,this.getActiveEventLegend().getApiItems());
			setSelectedItems(bipSet,this.getActiveEventLegend().getBipItems());
			setSelectedItems(sdprSet,this.getActiveEventLegend().getSdprItems());
			setSelectedItems(sdpcSet,this.getActiveEventLegend().getSdpcItems());
		}else{
			displayActiveFlag = false;
			eventStateSelect="ScheduledEvents";
			if("true".equalsIgnoreCase(apiChecked)){
				setDisplayFlagAPI(true);
				this.getScheEventLegend().setApiSelected(true);
			}else{
				setDisplayFlagAPI(false);
				this.getScheEventLegend().setApiSelected(false);
			}
			if("true".equalsIgnoreCase(bipChecked)){
				setDisplayFlagBIP(true);
				this.getScheEventLegend().setBipSelected(true);
			}else{
				setDisplayFlagBIP(false);
				this.getScheEventLegend().setBipSelected(false);
			}
			if("true".equalsIgnoreCase(cbpChecked)){
				setDisplayFlagCBP(true);
				this.getScheEventLegend().setCbpSelected(true);
			}else{
				setDisplayFlagCBP(false);
				this.getScheEventLegend().setCbpSelected(false);
			}
			if("true".equalsIgnoreCase(sdprChecked)){
				setDisplayFlagSDPR(true);
				this.getScheEventLegend().setSdprSelected(true);
			}else{
				setDisplayFlagSDPR(false);
				this.getScheEventLegend().setSdprSelected(false);
			}
			if("true".equalsIgnoreCase(sdpcChecked)){
				setDisplayFlagSDPC(true);
				this.getScheEventLegend().setSdpcSelected(true);
			}else{
				setDisplayFlagSDPC(false);
				this.getScheEventLegend().setSdpcSelected(false);
			}
			setSelectedItems(cbpSet,this.getScheEventLegend().getCbpItems());
			setSelectedItems(apiSet,this.getScheEventLegend().getApiItems());
			setSelectedItems(bipSet,this.getScheEventLegend().getBipItems());
			setSelectedItems(sdprSet,this.getScheEventLegend().getSdprItems());
			setSelectedItems(sdpcSet,this.getScheEventLegend().getSdpcItems());
		}

		
	}
	
	private Set getLegentSet(String input){
		String keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(input);
		String[] eventKeyArray = keys.split(",");
		Set set = new TreeSet();
		for(int i =0;i<eventKeyArray.length;i++){
			String eventKey = eventKeyArray[i];
			if(eventKey!=null&&(!eventKey.equalsIgnoreCase(""))){
				set.add(eventKey);
			}
		}
		return set;
	}

	/**
	 * @return the displayFlagCBP
	 */
	public boolean isDisplayFlagCBP() {
		return displayFlagCBP;
	}

	/**
	 * @param displayFlagCBP the displayFlagCBP to set
	 */
	public void setDisplayFlagCBP(boolean displayFlagCBP) {
		this.displayFlagCBP = displayFlagCBP;
	}
	
	
}
