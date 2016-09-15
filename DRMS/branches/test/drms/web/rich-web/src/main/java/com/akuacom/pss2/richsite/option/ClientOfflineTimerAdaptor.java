package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.email.ClientOfflineNotificationManager;
import com.akuacom.pss2.report.ClientOfflineReportManager;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.task.TimerConfigManager;

public class ClientOfflineTimerAdaptor implements Serializable {
	private static final long serialVersionUID = 1196474488204480688L;
	

	private TimerConfigManager manager;
	private TimerConfigManager getManager() {
		if(manager==null){
			manager = EJBFactory.getBean(TimerConfigManager.class);	
		}
		return manager;
	}
	
	public ClientOfflineTimerAdaptor(){
		super();
		retrieveData();
	}
	public void retrieveData(){
		List<TimerConfig> list = getManager().getTimerConfigList();
		for(TimerConfig config:list){
			if(report.equalsIgnoreCase(config.getName())){
				reportConfig = config;
			}
			if(summer.equalsIgnoreCase(config.getName())){
				summerConfig = config;
			}
			if(winter.equalsIgnoreCase(config.getName())){
				winterConfig = config;
			}
		}
	}
	private TimerConfig reportConfig;
	private TimerConfig summerConfig;
	private TimerConfig winterConfig;
	private static final String report="CLIENT_OFFLINE_REPORT_TIMER";
	private static final String summer="CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER";
	private static final String winter="CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER";
	/**
	 * @return the reportConfig
	 */
	public TimerConfig getReportConfig() {
		if(reportConfig==null){
			
			reportConfig = getManager().getTimerConfig(report);
		}
		return reportConfig;
	}
	/**
	 * @return the summerConfig
	 */
	public TimerConfig getSummerConfig() {
		if(summerConfig==null){
			summerConfig = getManager().getTimerConfig(summer);
		}
		return summerConfig;
	}
	/**
	 * @return the winterConfig
	 */
	public TimerConfig getWinterConfig() {
		if(winterConfig==null){
			winterConfig = getManager().getTimerConfig(winter);
		}
		return winterConfig;
	}
	
	public TimerConfig updateReportConfig(TimerConfig config){
//		TimerConfig config = getManager().getTimerConfig(report);
		reportConfig.setInvokeHour(config.getInvokeHour());
		reportConfig.setInvokeMin(config.getInvokeMin());
		getManager().updateTimerConfig(reportConfig);
//		retrieveData();
		return reportConfig;
	}
	
	public TimerConfig updateSummerConfig(TimerConfig config){
//		TimerConfig config = getManager().getTimerConfig(summer);
		summerConfig.setInvokeHour(config.getInvokeHour());
		summerConfig.setInvokeMin(config.getInvokeMin());
		summerConfig.setThreshold(config.getThreshold());
		summerConfig.setStartMonth(config.getStartMonth());
		summerConfig.setStartDay(config.getStartDay());
		summerConfig.setEndMonth(config.getEndMonth());
		summerConfig.setEndDay(config.getEndDay());
		getManager().updateTimerConfig(summerConfig);
//		retrieveData();
		return summerConfig;
	}
	public TimerConfig updateWinterConfig(TimerConfig config){
//		TimerConfig config = getManager().getTimerConfig(winter);
		winterConfig.setInvokeHour(config.getInvokeHour());
		winterConfig.setInvokeMin(config.getInvokeMin());
		winterConfig.setThreshold(config.getThreshold());
		winterConfig.setDay(config.getDay());
		getManager().updateTimerConfig(winterConfig);
		return winterConfig;
	}
	private ClientOfflineReportManager clientOfflineReportManager;
	public ClientOfflineReportManager getClientOfflineReportManager() {
		if(clientOfflineReportManager==null){
			clientOfflineReportManager = EJBFactory.getBean(ClientOfflineReportManager.class);	
		}
		return clientOfflineReportManager;
	}
	private ClientOfflineNotificationManager clientOfflineNotificationManager;
	public ClientOfflineNotificationManager getClientOfflineNotificationManager() {
		if(clientOfflineNotificationManager==null){
			clientOfflineNotificationManager = EJBFactory.getBean(ClientOfflineNotificationManager.class);	
		}
		return clientOfflineNotificationManager;
	}
}
