
package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.service.EventManager;
import com.akuacom.pss2.drw.service.impl.EventManagerImpl;
import com.akuacom.pss2.drw.util.DRWUtil;


public class DRFrameBackingBean implements Serializable{

	private static final long serialVersionUID = 1176054153774996013L;
	/** EventManager reference */
	private EventManager manager = EventManagerImpl.getInstance();	
	/** The log */
	private static final Logger log = Logger.getLogger(DRFrameBackingBean.class);
	/** Current system update time */
	private String currentSystemTime;
	/** Current system time milliseSeconds used for interactive with JavaScript */
	private long currentSystemTimeMilliseSeconds;
	/** ResidentialUIBackingBean reference */
	private ResidentialUIBackingBean residentialUIBackingBean;
	/** CommercialUIBackingBean reference */
	private CommercialUIBackingBean commercialUIBackingBean;
	/** HistoryUIBackingBean reference */
	private HistoryUIBackingBean historyUIBackingBean;
	/** Style of the date format */
	private String dateFomat;
	
	private boolean mobBadge;
	
	// ---------------------------------------------Business logic function--------------------------------------	
	
	
	public boolean isMobBadge() {
		return DRWUtil.getSystemManager().getPss2Properties().getMobileBadgeHide();
	}
	public void setMobBadge(boolean mobBadge) {

		this.mobBadge = mobBadge;
	}
	/**
	 * Constructor
	 */
	public DRFrameBackingBean() {
		try {
			initialize();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}
	/**
	 * Function for initialize the EventBackingBean
	 */
	public void initialize() {
		initializeManager();
		initializeConfiguration();
		initializeData();
	}
	private void initializeManager() {
		manager.initializeDRFrame(this);
	}
	/**
	 * Initial the system configuration
	 */
	private void initializeConfiguration() {
//		getResidentialUIBackingBean().initialize();
//		getCommercialUIBackingBean().initialize();
//		getHistoryUIBackingBean().initialize();
	}
	private void initializeData(){
//		manager.initializeResidentialData(getResidentialUIBackingBean());
//		manager.initializeCommercialData(getCommercialUIBackingBean());
	}
	

	public String addValidationErrorMessage(String errorMessage) {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
		return "";
	}
	
	// ---------------------------------------------Getter--------------------------------------
	public String getCurrentSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mma");
		currentSystemTime = sdf.format(new Date());
		return currentSystemTime;
	}
	public long getCurrentSystemTimeMilliseSeconds() {
		currentSystemTimeMilliseSeconds = (new Date()).getTime();
		return currentSystemTimeMilliseSeconds;
	}
	/**
	 * @return the residentialUIBackingBean
	 */
	public ResidentialUIBackingBean getResidentialUIBackingBean() {
		if(residentialUIBackingBean==null){
			residentialUIBackingBean = new ResidentialUIBackingBean();
			residentialUIBackingBean.initialize();
		}
		return residentialUIBackingBean;
	}
	/**
	 * @return the commercialUIBackingBean
	 */
	public CommercialUIBackingBean getCommercialUIBackingBean() {
		if(commercialUIBackingBean==null){
			commercialUIBackingBean = new CommercialUIBackingBean();
			commercialUIBackingBean.initialize();
		}
		return commercialUIBackingBean;
	}
	/**
	 * @return the historyUIBackingBean
	 */
	public HistoryUIBackingBean getHistoryUIBackingBean() {
		if(historyUIBackingBean==null){
			historyUIBackingBean = new HistoryUIBackingBean();
			historyUIBackingBean.initialize();
		}
		return historyUIBackingBean;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(EventManager manager) {
		this.manager = manager;
	}
	/**
	 * @param currentSystemTime the currentSystemTime to set
	 */
	public void setCurrentSystemTime(String currentSystemTime) {
		this.currentSystemTime = currentSystemTime;
	}
	/**
	 * @param currentSystemTimeMilliseSeconds the currentSystemTimeMilliseSeconds to set
	 */
	public void setCurrentSystemTimeMilliseSeconds(
			long currentSystemTimeMilliseSeconds) {
		this.currentSystemTimeMilliseSeconds = currentSystemTimeMilliseSeconds;
	}
	/**
	 * @param residentialUIBackingBean the residentialUIBackingBean to set
	 */
	public void setResidentialUIBackingBean(
			ResidentialUIBackingBean residentialUIBackingBean) {
		this.residentialUIBackingBean = residentialUIBackingBean;
	}
	/**
	 * @param commercialUIBackingBean the commercialUIBackingBean to set
	 */
	public void setCommercialUIBackingBean(
			CommercialUIBackingBean commercialUIBackingBean) {
		this.commercialUIBackingBean = commercialUIBackingBean;
	}
	/**
	 * @param historyUIBackingBean the historyUIBackingBean to set
	 */
	public void setHistoryUIBackingBean(HistoryUIBackingBean historyUIBackingBean) {
		this.historyUIBackingBean = historyUIBackingBean;
	}
	/**
	 * @param dateFomat the dateFomat to set
	 */
	public void setDateFomat(String dateFomat) {
		this.dateFomat = dateFomat;
	}
	/**
	 * @return the dateFomat
	 */
	public String getDateFomat() {
		return dateFomat;
	}

}
