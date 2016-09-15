/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import com.akuacom.pss2.richsite.FDUtils;

/**
 * 
 */
public class DBPEventDispatchBean  implements Serializable{
	
	private static final long serialVersionUID = 1479019764236433113L;
	
	public static final String DEFAULT_FORMATTED_FILENAME="yyyyMMdd";
	public static final String DEFAULT_DBPBID_FIXED_FILENAME="DBP_BID_REPORT";
	public static final String DEFAULT_PARTINFO_FIXED_FILENAME="DRAS_Customer_Info_Report";
	
	public static final String CONFIG_NAME_SCEDBP="SCEDBP";
	public static final String CONFIG_NAME_SCEPART="SCEPART";
	
	private String host;
	private Integer port;
	private String username;
	private String password;
	
	JSFParticipantSettingBean partConfig=new JSFParticipantSettingBean(this);
	JSFDBPSettingBean dbpConfig=new JSFDBPSettingBean(this);
	
    private String selectedTab ="DBPEventConfigurationTab";

	public enum Tab {
		DBPEventConfigurationTab,
		ParticipantConfigurationTab
	}
	
	public DBPEventDispatchBean(){
		init();
	}
	
	public Tab getActiveTab(){
		return  Enum.valueOf(Tab.class, selectedTab);
	}

	protected void init(){
		this.host=dbpConfig.getConfig().getHost();
		this.port=dbpConfig.getConfig().getPort();
		this.username=dbpConfig.getConfig().getUsername();
		this.password=dbpConfig.getConfig().getPassword();
	}
	
//	public void testConnection(){
//		try {
//			manager.testConnection(host, port, username, password);
//			FDUtils.addMsgInfo("Connected to the FTP server successfully");
//		} catch (AppServiceException e) {
//			FDUtils.addMsgError(e.getMessage());
//		}
//	}
//	
//	public void save(){
//		try {
//			if (startTime !=null && endTime!=null &&
//					(startTime.getTime()>=endTime.getTime())) {
//				FDUtils.addMsgError("Scan start time must be earlier than scan end time");
//				return;
//			}
//			
//			boolean resetTimer=false;
//			boolean cancelTimer=false;
//			if (dbpConfig==null) {
//				dbpConfig=new SCEFTPConfig();
//				resetTimer=true;
//			} else {
//				if (startTime==null || endTime==null || interval==null || interval==0) {
//					dbpConfig.setUpload(false);
//					cancelTimer=true;
//				} else if ((dbpConfig.getScanStartTime()!=null &&
//								!dbpConfig.getScanStartTime().equals(format.format(startTime))) ||
//						(dbpConfig.getScanEndTime()!=null &&
//								!dbpConfig.getScanEndTime().equals(format.format(endTime))) ||
//						dbpConfig.getScanInterval()!=interval) {
//					dbpConfig.setUpload(false);
//					resetTimer=true;
//				}
//			}
//			
//			dbpConfig.setHost(this.host);
//			dbpConfig.setPort(this.port);
//			dbpConfig.setUsername(this.username);
//			dbpConfig.setPassword(this.password);
//			dbpConfig.setFixedFilename(fixedFilename);
//			dbpConfig.setFormattedFilename(this.formattedFilename);
//			dbpConfig.setBackupPath(this.path);
//			if (this.startTime!=null)
//				dbpConfig.setScanStartTime(format.format(this.startTime));
//			else
//				dbpConfig.setScanStartTime(null);
//			
//			if (this.endTime!=null)
//				dbpConfig.setScanEndTime(format.format(this.endTime));
//			else
//				dbpConfig.setScanEndTime(null);
//			
//			dbpConfig.setScanInterval(this.interval);
//			dbpConfig.setRequired(required);
//			
//			dbpConfig=manager.save(dbpConfig, cancelTimer, resetTimer);
////			FDUtils.addMsgError("Configuration saved successfully");
//		} catch (AppServiceException e) {
//			FDUtils.addMsgError(e.getMessage());
//		}
//	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void validateTime(FacesContext context, UIComponent component, Object value) {
		if (value==null || ((String)value).isEmpty()) return;
		
		String format=(String)component.getAttributes().get("format");
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		try {
			Date time=dateFormat.parse((String)value);
		} catch (ParseException e) {
            ((HtmlInputText)component).setValid(false);
            String object=((HtmlInputText)component).getLabel();
            FDUtils.addMsgError(object+" not correct. It should be defined as HH:mm (HH indicates hour in day (0-23)).");
		}
	}

	public JSFParticipantSettingBean getPartConfig() {
		return partConfig;
	}

	public void setPartConfig(JSFParticipantSettingBean partConfig) {
		this.partConfig = partConfig;
	}

	public JSFDBPSettingBean getDbpConfig() {
		return dbpConfig;
	}

	public void setDbpConfig(JSFDBPSettingBean dbpConfig) {
		this.dbpConfig = dbpConfig;
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}
	
}
