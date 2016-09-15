/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.program.sceftp.SCEDBPConfigManager;
import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.program.sceftp.progAutoDisp.ProgAutoDispConfigManager;
import com.akuacom.pss2.program.sceftp.progAutoDisp.ProgAutoDispManager;
import com.akuacom.pss2.richsite.FDUtils;

/**
 * the class JSFProgAutoDispSettingBean
 *
 */
public class JSFProgAutoDispSettingBean {

	public static final String CONFIG_NAME="SCE_PROG_AUTO_DISP";
	public static final String FILE_NAME_TEMPLATE="YYYYMMDDHHmmSS_PRG_PRD_LTP_LOC_ActionVerb.txt";
	
	protected SCEFTPConfig config;
	
	ProgAutoDispConfigManager manager;
	ProgAutoDispManager progAutoDispManager;
	
	private String fileName;
	private String fileContent;
	public JSFProgAutoDispSettingBean(){
		init();
	}
	
	public void init() {
		config = getManager().getConfiguration(CONFIG_NAME);
		if (config == null) {
			config=new SCEFTPConfig();
	
			config.setConfigName(CONFIG_NAME);
			config.setFilenameTemplate(FILE_NAME_TEMPLATE);
			
		}
		initFlag();
		this.config.setAvailable(true);
	}
	public void testFunction(){
		config.setAppendix(getFlag());
		getProgAutoDispManager().autoDispatch(config);
	}
	public void testFunction2(){
		config.setAppendix(getFlag());
		getProgAutoDispManager().autoDispatch(config,fileName,fileContent);
	}
	public void testConnection(){
		try {
			getManager().testConnection(this.config);
			FDUtils.addMsgInfo("Connected to the FTP server successfully");
		} catch (AppServiceException e) {
			FDUtils.addMsgError(MessageUtil.getErrorMessage(e));
		}
	}
	
	public void save(){
		if (!checkScanTime())
			return;
		
		config.setUpload(false);
		config.setResetTimer(true);
		config.setConnErrorNotified(false);
		config.setAppendix(getFlag());
		config=getManager().save(config);
		FDUtils.addMsgInfo("Configuration saved successfully");
	}
	public ProgAutoDispManager getProgAutoDispManager() {
		if (progAutoDispManager==null)
			progAutoDispManager= EJBFactory.getBean(ProgAutoDispManager.class);
		
		return progAutoDispManager;
	}
	public SCEDBPConfigManager getManager() {
		if (manager==null)
			manager= EJBFactory.getBean(ProgAutoDispConfigManager.class);
		
		return manager;
	}
	
	private boolean checkScanTime() {
		boolean correct=true;
		SimpleDateFormat format=new SimpleDateFormat("HH:mm");
		try {
			if (config.getScanStartTime()!=null && config.getScanStartTime().equals("00:00") && config.getScanEndTime()!=null &&
					config.getScanEndTime().equals("00:00")) {
				return true;
			}
			
			Date startTime =null;
			if (config.getScanStartTime()!=null && config.getScanStartTime().trim().length()!=0) {
				startTime=format.parse(config.getScanStartTime());
			}
	
			Date endTime=null;
			if (config.getScanEndTime()!=null && config.getScanEndTime().trim().length()!=0) {
				endTime=format.parse(config.getScanEndTime());
			}
			
			if (startTime.getTime()>=endTime.getTime()) {
				FDUtils.addMsgError("Scan start time must be earlier than scan end time");
				correct= false;
			}
			if (config.getDelayInterval()<0) {
				FDUtils.addMsgError("Event delay interval must be a positive number.");
				correct= false;
			}
		}catch(ParseException e){
			FDUtils.addMsgError("Scan time not correct. Scan start/end time should be defined as HH:mm (HH indicates hour in day (0-23)).");
			correct=false;
		}
		return correct;
	}

	public void validateTime(FacesContext context, UIComponent component, Object value) {
		if (value==null || ((String)value).isEmpty()) return;
		
		String format=(String)component.getAttributes().get("format");
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		try {
			dateFormat.parse((String)value);
		} catch (ParseException e) {
            ((HtmlInputText)component).setValid(false);
            String object=((HtmlInputText)component).getLabel();
            FDUtils.addMsgError(object+" not correct. It should be defined as HH:mm (HH indicates hour in day (0-23)).");
		}
	}

	@Deprecated
	public void valueChanged(ValueChangeEvent e){
		config.setResetTimer(true);
	}

	public String getFilenameTemplate() {
		return getFilenameTemplate(true);
	}
	
	protected String getFilenameTemplate(boolean display) {
		return config.getFilenameTemplate();
	}

	public SCEFTPConfig getConfig() {
		return config;
	}

	public void setConfig(SCEFTPConfig config) {
		this.config = config;
	}
	
	private boolean enableSAI;
	private boolean enableDBP;
	private boolean enableSPD;
	private boolean enableAPI;
	private boolean enableBIP;
	private boolean enableSDP;
	private void initFlag(){
		if(config!=null){
			String combine = config.getAppendix();
			if(combine!=null){
				enableSAI = combine.contains("SAI");
				enableDBP = combine.contains("DBP");
				enableSPD = combine.contains("SPD");
				enableAPI = combine.contains("API");
				enableBIP = combine.contains("BIP");
				enableSDP = combine.contains("SDP");
			}
		}
	}
	private String getFlag(){
		String result = "";
		if(isEnableSAI()){
			result+="SAI,";
		}
		if(isEnableDBP()){
			result+="DBP DA,";
		}
		if(isEnableSPD()){
			result+="SPD,";
		}
		if(isEnableAPI()){
			result+="API,";
		}
		if(isEnableBIP()){
			result+="TOU-BIP,";
		}
		if(isEnableSDP()){
			result+="SDP,";
		}
		return result;
	}
	/**
	 * @return the enableSAI
	 */
	public boolean isEnableSAI() {
		return enableSAI;
	}

	/**
	 * @param enableSAI the enableSAI to set
	 */
	public void setEnableSAI(boolean enableSAI) {
		this.enableSAI = enableSAI;
	}

	/**
	 * @return the enableDBP
	 */
	public boolean isEnableDBP() {
		return enableDBP;
	}

	/**
	 * @param enableDBP the enableDBP to set
	 */
	public void setEnableDBP(boolean enableDBP) {
		this.enableDBP = enableDBP;
	}

	/**
	 * @return the enableSPD
	 */
	public boolean isEnableSPD() {
		return enableSPD;
	}

	/**
	 * @param enableSPD the enableSPD to set
	 */
	public void setEnableSPD(boolean enableSPD) {
		this.enableSPD = enableSPD;
	}

	/**
	 * @return the enableAPI
	 */
	public boolean isEnableAPI() {
		return enableAPI;
	}

	/**
	 * @param enableAPI the enableAPI to set
	 */
	public void setEnableAPI(boolean enableAPI) {
		this.enableAPI = enableAPI;
	}

	/**
	 * @return the enableBIP
	 */
	public boolean isEnableBIP() {
		return enableBIP;
	}

	/**
	 * @param enableBIP the enableBIP to set
	 */
	public void setEnableBIP(boolean enableBIP) {
		this.enableBIP = enableBIP;
	}

	/**
	 * @return the enableSDP
	 */
	public boolean isEnableSDP() {
		return enableSDP;
	}

	/**
	 * @param enableSDP the enableSDP to set
	 */
	public void setEnableSDP(boolean enableSDP) {
		this.enableSDP = enableSDP;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(ProgAutoDispConfigManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileContent
	 */
	public String getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	
}
