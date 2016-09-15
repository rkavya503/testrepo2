/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.akuacom.ejb.BaseEntity;

/**
 * the entity SCEDBPDispatchConfig
 *
 */
@Entity
@Table(name = "sce_ftp_config")
@NamedQueries({
	@NamedQuery(name = "SCEFTPConfig.findByConfigName.single",
        query = "select c from SCEFTPConfig c where c.configName=:configName")
}) 
public class SCEFTPConfig extends BaseEntity {

	private static final long serialVersionUID = -8740841098332232694L;
    public static final String DATETIME_FORMAT="yyyyMMdd HH:mm";
    public static final String DATE_FORMAT="yyyyMMdd";
	public static final String DEFAULT_FORMATTED_FILENAME="yyyyMMdd";

	private String host;
	private Integer port;
	private String username;
	private String password;
	private String backupPath;
	private String formattedFilename=DEFAULT_FORMATTED_FILENAME;
	private String fixedFilename;
	private String scanStartTime;
	private String scanEndTime;
	private Integer scanInterval;
	private Boolean required;
	private Boolean upload=false;
	private String filenameTemplate;
	
	private String configName;
	private Boolean available=true;
	private String timerManagerClass;
	private Boolean connErrorNotified=false;
	
	//Added for Program Auto Dispatch
	//sourcePath -> event file path
	//appendixPath -> disabled file path
	//enabled programs
	private String sourcePath;
	private String appendixPath;
	private String appendix;
	private Integer delayInterval;
	
	@Transient
	private Date todayStartTime;
	@Transient
	private Date todayEndTime;

	@Transient
	private boolean resetTimer;

	public SCEFTPConfig(){
	}
	
	public SCEFTPConfig(String configName){
		this.configName=configName;
	}
	
	public boolean isCancelTimer() {
		boolean isCancelTimer=false;
		if ((this.scanStartTime==null || this.scanStartTime.trim().length()==0) &&
				(this.scanEndTime==null || this.scanEndTime.trim().length()==0) &&
				(this.scanInterval==null || this.scanInterval==0))
			isCancelTimer= true;
		
		return isCancelTimer;
	}

	public Date getTodayStartTime() throws ParseException{
		if (scanStartTime!=null) {
			todayStartTime=toDate(new Date(), this.scanStartTime);
		}
		return todayStartTime;
	}
	public Date getTodayEndTime() throws ParseException{
		if (scanEndTime!=null) {
			todayEndTime=toDate(new Date(), this.scanEndTime);
		}
		return todayEndTime;
	}
	
	private Date toDate(Date date, String time) throws ParseException{
			SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
			Date toDate = mergeDateTime(format.format(date), time);
		return toDate;
	}
    private Date mergeDateTime(String date, String time) throws ParseException{
    	SimpleDateFormat format=new SimpleDateFormat(DATETIME_FORMAT);
    	return format.parse(date+" "+time);
    }

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
	public String getBackupPath() {
		return backupPath;
	}
	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}
	public String getFormattedFilename() {
		return formattedFilename;
	}
	public void setFormattedFilename(String formattedFilename) {
		this.formattedFilename = formattedFilename;
	}
	public String getFixedFilename() {
		return fixedFilename;
	}
	public void setFixedFilename(String fixedFilename) {
		this.fixedFilename = fixedFilename;
	}
	public String getScanStartTime() {
		return scanStartTime;
	}
	public void setScanStartTime(String scanStartTime) {
		this.scanStartTime = scanStartTime;
	}
	public String getScanEndTime() {
		return scanEndTime;
	}
	public void setScanEndTime(String scanEndTime) {
		this.scanEndTime = scanEndTime;
	}
	public Integer getScanInterval() {
		return scanInterval;
	}
	public void setScanInterval(Integer scanInterval) {
		this.scanInterval = scanInterval;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Boolean getUpload() {
		return upload;
	}
	public void setUpload(Boolean upload) {
		this.upload = upload;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public boolean isResetTimer() {
		return resetTimer;
	}
	public void setResetTimer(boolean resetTimer) {
		this.resetTimer = resetTimer;
	}
	public void setFilenameTemplate(String filenameTemplate) {
		this.filenameTemplate = filenameTemplate;
	}
	public String getFilenameTemplate() {
		return filenameTemplate;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public String getTimerManagerClass() {
		return timerManagerClass;
	}
	public void setTimerManagerClass(String timerManagerClass) {
		this.timerManagerClass = timerManagerClass;
	}
	public Boolean getConnErrorNotified() {
		return connErrorNotified;
	}
	public void setConnErrorNotified(Boolean connErrorNotified) {
		this.connErrorNotified = connErrorNotified;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getAppendixPath() {
		return appendixPath;
	}

	public void setAppendixPath(String appendixPath) {
		this.appendixPath = appendixPath;
	}

	public String getAppendix() {
		return appendix;
	}

	public void setAppendix(String appendix) {
		this.appendix = appendix;
	}

	/**
	 * @return the delayInterval
	 */
	public Integer getDelayInterval() {
		return delayInterval;
	}

	/**
	 * @param delayInterval the delayInterval to set
	 */
	public void setDelayInterval(Integer delayInterval) {
		this.delayInterval = delayInterval;
	}


	
}
