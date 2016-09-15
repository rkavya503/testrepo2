package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.task.TimerConfigManager;

public class JSFTimerConfigBackingBean implements Serializable {

	private static final long serialVersionUID = 8155573046623766786L;
	private static final Logger log = Logger.getLogger(JSFTimerConfigBackingBean.class);	
	private List<TimerConfig> timerConfigs = new ArrayList<TimerConfig>();
	private static TimerConfigManager manager = EJB3Factory.getBean(TimerConfigManager.class);
	private String selectItemUUID;
	private TimerConfig currentTimerConfig;
	private boolean normalTypeFlag = true;
	public JSFTimerConfigBackingBean(){
		super();
		try{
			
			String uuid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uuid");
			retrieveData();
			if(uuid==null||uuid.equalsIgnoreCase("")){
				
			}else{
				if(timerConfigs!=null){
					for(TimerConfig config:timerConfigs){
						if(config.getUUID().equalsIgnoreCase(uuid)){
							currentTimerConfig = config;
							searchHour = currentTimerConfig.getInvokeHour();
							searchMin = currentTimerConfig.getInvokeMin();
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
	
	
	private void retrieveData(){
		if(manager!=null){
			timerConfigs = manager.getTimerConfigList();
		}
	}
	public void updateSingleAction(){
		if(currentTimerConfig!=null){
			if(isNormalTypeFlag()){
				currentTimerConfig.setInvokeHour(getSearchHour());
				currentTimerConfig.setInvokeMin(getSearchMin());
			}else{
				currentTimerConfig.setInvokeHour(getSearchHour());
				currentTimerConfig.setInvokeMin(getSearchMin());
			}
			manager.updateTimerConfig(currentTimerConfig);
		}
	}
	/**
	 * @return the timerConfigs
	 */
	public List<TimerConfig> getTimerConfigs() {
		return timerConfigs;
	}

	/**
	 * @param timerConfigs the timerConfigs to set
	 */
	public void setTimerConfigs(List<TimerConfig> timerConfigs) {
		this.timerConfigs = timerConfigs;
	}

	/**
	 * @return the manager
	 */
	public static TimerConfigManager getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public static void setManager(TimerConfigManager manager) {
		JSFTimerConfigBackingBean.manager = manager;
	}

	/**
	 * @return the selectItemUUID
	 */
	public String getSelectItemUUID() {
		return selectItemUUID;
	}

	/**
	 * @param selectItemUUID the selectItemUUID to set
	 */
	public void setSelectItemUUID(String selectItemUUID) {
		this.selectItemUUID = selectItemUUID;
	}

	/**
	 * @return the currentTimerConfig
	 */
	public TimerConfig getCurrentTimerConfig() {
		return currentTimerConfig;
	}

	/**
	 * @param currentTimerConfig the currentTimerConfig to set
	 */
	public void setCurrentTimerConfig(TimerConfig currentTimerConfig) {
		this.currentTimerConfig = currentTimerConfig;
	}



	/**
	 * @return the normalTypeFlag
	 */
	public boolean isNormalTypeFlag() {
		if(currentTimerConfig!=null){
			String type = currentTimerConfig.getType();
			if(type.equalsIgnoreCase("NORMAL")){
				normalTypeFlag = true;
			}else{
				normalTypeFlag = false;
			}
		}
		return normalTypeFlag;
	}



	/**
	 * @param normalTypeFlag the normalTypeFlag to set
	 */
	public void setNormalTypeFlag(boolean normalTypeFlag) {
		this.normalTypeFlag = normalTypeFlag;
	}



	// option list for time & hour selection
	private static List<SelectItem> hourList;
	private static List<SelectItem> minList;
	private static List<SelectItem> dayList;
    private int searchHour;
    private int searchMin;
    private int searchDay;
    public List<SelectItem> getHourList() {
		return hourList;
	}

	public List<SelectItem> getMinList() {
		return minList;
	}
	public List<SelectItem> getDayList() {
		return dayList;
	}
	static {
		hourList = new ArrayList<SelectItem>();
        for (int i = 0; i < 24; i++) {
        	hourList.add(new SelectItem(i,formatTime(i)));
        }
		
        minList = new ArrayList<SelectItem>();
        for (int i = 0; i < 60; i++) {
        	minList.add(new SelectItem(i,formatTime(i)));
        }
        dayList = new ArrayList<SelectItem>();
        dayList.add(new SelectItem(0,"Sunday"));
        dayList.add(new SelectItem(1,"Monday"));
        dayList.add(new SelectItem(2,"Tuesday"));
        dayList.add(new SelectItem(3,"Wednesday"));
        dayList.add(new SelectItem(4,"Thursday"));
        dayList.add(new SelectItem(5,"Friday"));
        dayList.add(new SelectItem(6,"Saturday"));
        
	}
	private static String formatTime(int i){
		if(i<10)
			return "0"+i;
		else
			return i+"";
	}



	/**
	 * @return the searchHour
	 */
	public int getSearchHour() {
		return searchHour;
	}



	/**
	 * @param searchHour the searchHour to set
	 */
	public void setSearchHour(int searchHour) {
		this.searchHour = searchHour;
	}



	/**
	 * @return the searchDay
	 */
	public int getSearchDay() {
		return searchDay;
	}



	/**
	 * @param searchDay the searchDay to set
	 */
	public void setSearchDay(int searchDay) {
		this.searchDay = searchDay;
	}



	/**
	 * @return the searchMin
	 */
	public int getSearchMin() {
		return searchMin;
	}



	/**
	 * @param searchMin the searchMin to set
	 */
	public void setSearchMin(int searchMin) {
		this.searchMin = searchMin;
	}



	/**
	 * @param hourList the hourList to set
	 */
	public static void setHourList(List<SelectItem> hourList) {
		JSFTimerConfigBackingBean.hourList = hourList;
	}



	/**
	 * @param minList the minList to set
	 */
	public static void setMinList(List<SelectItem> minList) {
		JSFTimerConfigBackingBean.minList = minList;
	}
	
	
	
	//-------------------------------------------------------------------------------------
	
	private TimerConfig reportConfig;
	private TimerConfig summerConfig;
	private TimerConfig winterConfig;
	private ClientOfflineTimerAdaptor adaptor = new ClientOfflineTimerAdaptor();
	public TimerConfig getReportConfig() {
		return reportConfig;
	}
	public void setReportConfig(TimerConfig reportConfig) {
		this.reportConfig = reportConfig;
	}
	public TimerConfig getSummerConfig() {
		return summerConfig;
	}
	public void setSummerConfig(TimerConfig summerConfig) {
		this.summerConfig = summerConfig;
	}
	public TimerConfig getWinterConfig() {
		return winterConfig;
	}
	public void setWinterConfig(TimerConfig winterConfig) {
		this.winterConfig = winterConfig;
	}
	public ClientOfflineTimerAdaptor getAdaptor() {
		return adaptor;
	}
	public void setAdaptor(ClientOfflineTimerAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	
}
