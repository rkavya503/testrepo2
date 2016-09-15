package com.akuacom.pss2.richsite;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.rtp.ftp.RTPFTPConfiguration;
import com.akuacom.pss2.rtp.ftp.RTPFTPConfigurationEAO;
import com.akuacom.pss2.rtp.ftp.RTPFTPConfigurationEAOBean;
import com.akuacom.pss2.rtp.ftp.RTPFTPConfigurationManager;
import com.akuacom.pss2.rtp.ftp.RTPTemperatureUpdateTimer;

public class SCEFTPBackingBean implements Serializable{

	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127717274433L;
	//---------------------------------------------------attributes---------------------------------------------------------
	/** The log */
	private static final Logger log = Logger.getLogger(SCEFTPBackingBean.class);

	/** The FTP URL*/
	private String url;
	
	/** The FTP port*/
	private String port;

	/** The FTP path*/
	private String path;

	/** The FTP file name*/
	private String fileName;
	
	/** The FTP user name*/
	private String userName;
	
	/** The FTP user password*/
	private String password;
	
	/** The FTP scan start time*/
	private String startTime;
	
	/** The FTP scan end time*/
	private String endTime;
	
	/** The FTP scan interval*/
	private String interval;
	
	/** Required*/
	private boolean required;
	
	/** The MIN temperature, default value is -80*/
	private String minTemperature;
	
	/** The MAX temperature, default value is 130*/
	private String maxTemperature;
	
	RTPFTPConfiguration config;
	
	private transient RTPFTPConfigurationEAO eao;

	//---------------------------------------------------business logic---------------------------------------------------------
	
	public RTPFTPConfigurationEAO getRTPFTPConfigurationEao() {
		if (eao == null) {
			eao = (RTPFTPConfigurationEAO) EJBFactory.getBean(RTPFTPConfigurationEAOBean.class);
		}
		return eao;
	}

	/**
	 * Constructor
	 */
	public SCEFTPBackingBean() {
		try {
			initialize();
		} 
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/**
	 * Function for initialize the SCEFTPBackingBean
	 */
	private void initialize() {
		config = getRTPFTPConfiguration();
		
		this.url = config.getUrl();
		this.port = config.getPort();
		this.path = config.getPath();
		this.fileName = config.getFileName();
		this.userName = config.getUserName();
		this.password = config.getPassword();
		this.startTime = config.getStartTime();
		this.endTime = config.getEndTime();
		this.interval = config.getInterval();
		this.required = config.isRequired();
		this.minTemperature = config.getMinTemperature() + "";
		this.maxTemperature = config.getMaxTemperature() + "";
	}
	
	/**
	 * Function for test FTP settings is correct.
	 * 1,FTP is accessible with given credential
	 * 2,Read/write the root folder
	 * 3,Read/write the specified backup folder
	 * @return
	 */
	public void testFTPSettings() {
		
		RTPFTPConfigurationManager rtpConfigManager = EJB3Factory.getBean(RTPFTPConfigurationManager.class);

		try{
			rtpConfigManager.testFTPConnection(this.url, Integer.parseInt(this.port), this.userName, this.password, this.path);
			FDUtils.addMsgInfo("Connection Successful!");
		}
		catch (Exception e) {
			log.info("Test connection failed!  Please make sure FTP is open and FTP URL / Port / User Name / Password configurations are correct, path is readable / writeable.");
			FDUtils.addMsgError("Test connection failed!  Please make sure FTP is open and FTP URL / Port / User Name / Password configurations are correct, path is readable / writeable.");
		}
	}

	/**
	 * Function for save FTP settings 
	 * @return
	 */
	public void saveFTPSettings() {

		if(!validateConfiguration()){
			return;
		}
		
		if(config != null){
			config.setUrl(this.url);
			config.setPort(this.port);
			config.setPath(this.path);
			config.setFileName(this.fileName);
			config.setUserName(this.userName);
			config.setPassword(this.password);
			config.setStartTime(this.startTime);
			config.setEndTime(this.endTime);
			config.setInterval(this.interval);
			config.setRequired(this.required);
			config.setMinTemperature(Double.parseDouble(this.minTemperature));
			config.setMaxTemperature(Double.parseDouble(this.maxTemperature));
			config.setLastProcessTime(null);
			config.setSendNotification(false);
			
			config.setSentConnError(false);
		}
		try{
			getRTPFTPConfigurationEao().saveRTPFTPConfiguration(config);
			
			RTPTemperatureUpdateTimer rtpTimer = EJB3Factory.getBean(RTPTemperatureUpdateTimer.class);
			rtpTimer.scheduleTimer();

		}
		catch (Exception e) {
			log.error("Error happened while saving RTP Temperature file FTP pickup configuration.", e);
		}
		
		FDUtils.addMsgInfo("Save configuration successfully!");
	}
	
	public RTPFTPConfiguration getRTPFTPConfiguration(){
		RTPFTPConfiguration configuration = getRTPFTPConfigurationEao().getRTPFTPConfiguration();
        return configuration;
	}
	
	
	//---------------------------------------------------Input Validation---------------------------------------------------------

	boolean validateConfiguration(){
		boolean passValidate = true;
		
		if(StringUtils.isEmpty(this.url)){
			FDUtils.addMsgError("FTP URL can not be empty.");
			passValidate = false;
		}
		if(StringUtils.isEmpty(this.port)){
			FDUtils.addMsgError("Port can not be empty.");
			passValidate = false;
		}
		else{
			try{
				Integer.parseInt(this.port);				
			}
			catch (Exception e) {
				FDUtils.addMsgError("Port only accept number.");
				passValidate = false;
			}
		}
		
		if(StringUtils.isEmpty(this.path)){
			FDUtils.addMsgError("Path can not be empty.");
			passValidate = false;
		}
		if(StringUtils.isEmpty(this.fileName)){
			FDUtils.addMsgError("File Name can not be empty.");
			passValidate = false;
		}
		if(StringUtils.isEmpty(this.userName)){
			FDUtils.addMsgError("User Name can not be empty.");
			passValidate = false;
		}
		if(StringUtils.isEmpty(this.password)){
			FDUtils.addMsgError("Password can not be empty.");
			passValidate = false;
		}
		if(StringUtils.isEmpty(this.startTime)){
			FDUtils.addMsgError("Scan Start Time can not be empty.");
			passValidate = false;
		}
		else{
			if(!validateTime(this.startTime)){
				passValidate = false;
			}
			else{
				if(this.startTime.length() < 5){
					this.startTime = formatTime(this.startTime);
				}
			}
		}
		
		if(StringUtils.isEmpty(this.endTime)){
			FDUtils.addMsgError("Scan End Time can not be empty.");
			passValidate = false;
		}
		else{
			if(!validateTime(this.endTime)){
				passValidate = false;
			}
			else{
				if(this.endTime.length() < 5){
					this.endTime = formatTime(this.endTime);
				}
			}
		}
		
		if(passValidate){
			Date startTime = new Date();
			Date endTime = new Date();
			
			startTime.setHours(Integer.parseInt(this.startTime.substring(0, this.startTime.indexOf(":"))));
			startTime.setMinutes(Integer.parseInt(this.startTime.substring(this.startTime.indexOf(":") + 1)));

			endTime.setHours(Integer.parseInt(this.endTime.substring(0, this.endTime.indexOf(":"))));
			endTime.setMinutes(Integer.parseInt(this.endTime.substring(this.endTime.indexOf(":") + 1)));
			
			if(endTime.before(startTime)){
				FDUtils.addMsgError("Scan start time must before end time.");
				passValidate = false;				
			}
		}
		
		

		if(StringUtils.isEmpty(this.interval)){
			FDUtils.addMsgError("Scan Interval can not be empty.");
			passValidate = false;
		}
		else{
			try{
				Integer.parseInt(this.interval);				
			}
			catch (Exception e) {
				FDUtils.addMsgError("Scan Interval only accept number.");
				passValidate = false;
			}
		}
		
		if(StringUtils.isEmpty(this.minTemperature)){
			FDUtils.addMsgError("Min temperature can not be empty.");
			passValidate = false;
		}
		else{
			try{
				Double.parseDouble(this.minTemperature);				
			}
			catch (Exception e) {
				FDUtils.addMsgError("Min temperature only accept number.");
				passValidate = false;
			}
		}
		
		if(StringUtils.isEmpty(this.maxTemperature)){
			FDUtils.addMsgError("Max temperature can not be empty.");
			passValidate = false;
		}
		else{
			try{
				Double.parseDouble(this.maxTemperature);				
			}
			catch (Exception e) {
				FDUtils.addMsgError("Max temperature only accept number.");
				passValidate = false;
			}
		}
		
		return passValidate;
	}
	
	
	private boolean validateTime(String value){
		boolean validateResult = true;
		
		String hr = null;
		String min = null;
		String hm[] = value.split(":");
		if(hm.length == 1){
			hr = hm[0];
			if(!valdiateHour(hr)){
				validateResult = false;
			}
			min= "00";
		}else if(hm.length==2){
			hr= hm[0];
			if(!valdiateHour(hr)){
				validateResult = false;
			}
			min=hm[1];
			if(!valdiateMinute(min)){
				validateResult = false;
			}
		}else{
			FDUtils.addMsgError("Time must be format of HH:MM");
			validateResult = false;
		}
		
		return validateResult;
	}
	
	protected boolean valdiateHour(String hour){
		boolean validateResult = true; 
		String msg = null;
		try{
			int i =Integer.parseInt(hour);
			if(i<0 || i>23){
				 msg = "Hour must be within 0-23";
			}
		}catch(Exception e){
			msg = "Time must be format of HH:MM";
		}
		if(msg!=null){
			FDUtils.addMsgError(msg);
			validateResult = false;
		}
		return validateResult;
	}
	
	protected boolean valdiateMinute(String min){
		boolean validateResult = true; 

		String msg = null;
		try{
			int i =Integer.parseInt(min);
			if(i<0 || i>59){
				 msg = "Minute must be within 0-59";
			}
		}catch(Exception e){
			msg = "Time must be format of HH:MM";
		}
		if(msg!=null){
			FDUtils.addMsgError(msg);
			validateResult = false;
		}
		return validateResult;
	}

	
	public String formatTime(String value) {
		
		String timeValue = value;
		if(timeValue.indexOf(":") == -1){
			if(timeValue.length() == 1){
				timeValue = "0" + timeValue + ":00";
			}
			else if(timeValue.length() == 2){
				timeValue = timeValue + ":00";
			}
		}
		else{
			String hour = timeValue.substring(0, timeValue.indexOf(":"));
			String min = timeValue.substring(timeValue.indexOf(":")+1, timeValue.length());
			if(hour.length() < 2){
				hour = "0" + hour;
			}
			if(min.length() < 2){
				min = "0" + min;
			}
			timeValue = hour + ":" + min;
		}
		
		return timeValue;
	}
	
	//---------------------------------------------------setter and getter---------------------------------------------------------
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}
	public String getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public static Logger getLog() {
		return log;
	}
	
}
