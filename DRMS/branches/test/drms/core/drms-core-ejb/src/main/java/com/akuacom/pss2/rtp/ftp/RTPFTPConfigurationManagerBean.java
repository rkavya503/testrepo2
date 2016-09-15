package com.akuacom.pss2.rtp.ftp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.email.MailUtil;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;
import com.akuacom.utils.ftp.FTPClient;
import com.akuacom.utils.ftp.SSHFTPClientImpl;
import com.akuacom.utils.ftp.exception.AuthentificationException;
import com.akuacom.utils.ftp.exception.ConnectionNotOpenException;

@Stateless
public class RTPFTPConfigurationManagerBean implements RTPFTPConfigurationManager.L, RTPFTPConfigurationManager.R {

	private static final Logger log = Logger.getLogger(RTPFTPConfigurationManagerBean.class);

	public static final String WEATHER_REPORT_STATION = "SCE";
	private static final String ENTER_TOKEN = "\n";
	private static final String COMMA_TOKEN = ",";
	private static final String COLON_TOKEN = ":";

	private static final String NOREPLY_MAIL = "noreply@openadr.com";

	@EJB 
	CorePropertyEAO.L corePropertyEAO;
	
	@EJB 
	RTPFTPConfigurationEAO.L rtpFTPConfigurationEAO;
	
	@EJB
	ContactEAO.L contactEAO;

	@EJB
	WeatherEAO.L weatherEAO;
	
	@Override
	public RTPFTPConfiguration getRTPFTPConfiguration() {
		
		RTPFTPConfiguration rtpFTPConfiguration;
		
		try{
			rtpFTPConfiguration = rtpFTPConfigurationEAO.getRTPFTPConfiguration();
        }
        catch (Exception e){
            throw new EJBException("ERROR_RTPFTPConfiguration_GET", e);
        }
        return rtpFTPConfiguration;
	}

	@Override
	public void saveRTPFTPConfiguration(RTPFTPConfiguration rtpFTPConfiguration) {
		
		try{
			rtpFTPConfigurationEAO.saveRTPFTPConfiguration(rtpFTPConfiguration);
		}
		catch(Exception e){
			throw new EJBException("ERROR_RTPFTPConfiguration_SAVE", e);
		}

	}

	@Override
	public boolean getRTPTemperatureUpdateFeature() {
		
		boolean rtpTemperatureUpdateFeather = false;

		try {
			CoreProperty coreProperty = corePropertyEAO.getByPropertyName("feature.rtp.temperature.update");
			rtpTemperatureUpdateFeather = coreProperty.isBooleanValue();
		} 
		catch (Exception e) {
			throw new EJBException("ERROR_GET_CoreProperty", e);
		}
		
		return rtpTemperatureUpdateFeather;
	}
	
	@Override
	public void updateWeather(Double highTemperature) throws AppServiceException{
		try {
			Date now= new Date();
			Date yesterday = new Date(now.getTime() - DateUtil.MSEC_IN_DAY);
			boolean update=true;
	
			Weather weather=weatherEAO.getWeatherByDate(yesterday);
			if (weather ==null) {
				weather = new Weather();
				weather.setDate(yesterday);
				update=false;
			}
			
			weather.setReportingStation(WEATHER_REPORT_STATION);
			weather.setHigh(highTemperature);
			weather.setIsFinal(true);
			weather.setForecastHigh0(highTemperature);
			
			if(update)
				weatherEAO.update(weather);
			else
				weatherEAO.create(weather);
		}catch(EntityNotFoundException e){
			throw new AppServiceException(e);
		} catch (DuplicateKeyException e) {
			throw new AppServiceException(e);
		}
	}

	@SuppressWarnings("static-access") 
	public void backupFile(FTPClient ftpClient, String fileContent, String fileName) throws Exception {
		try {
			byte[] bytes = fileContent.getBytes();
			InputStream is = new ByteArrayInputStream(bytes);

			Calendar calendar = new GregorianCalendar();
			int year = calendar.get(calendar.YEAR);
			int month = calendar.get(calendar.MONTH) + 1;
			int date = calendar.get(calendar.DATE);
			int hour = calendar.get(calendar.HOUR_OF_DAY);
			int minute = calendar.get(calendar.MINUTE);

			ftpClient.makeDir("" + year);
			ftpClient.cd("" + year);

			ftpClient.makeDir("" + month);
			ftpClient.cd("" + month);
			ftpClient.makeDir("" + date);
			ftpClient.cd("" + date);

			String backupFileName = year + "_" + month + "_" + date + "_" + hour + "_" + minute + " " + fileName;
			ftpClient.upload(is, backupFileName);

			is.close();
		} 
		catch (Exception e) {
			log.error("RTP FTP is failed. Reason: Error happened during the process of RTP temperature file backup.");
            log.error(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
            throw e;
		}		
	}

	@Override
	public void sendMail(String subject, String content) {
		try {
			List<Contact> contacts = contactEAO.findCoreContacts();
			
			String globalContacts = "";
			for (Contact contact : contacts) {
				if(globalContacts.equalsIgnoreCase("")){
					globalContacts = contact.getAddress(); 								
				}
				else{
					globalContacts = globalContacts + "," + contact.getAddress();
				}
			}
			MailUtil.sendMail(globalContacts, NOREPLY_MAIL, subject, content, null, "N/A", null, null, 10, null);
			
		} 
		catch (Exception e) {
           log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
		}
	}

	private boolean validateFeedFileFormat(String fileContent) {
		boolean validateResult = true;
		
		if(StringUtils.isEmpty(fileContent)){
			validateResult = false;
			sendMail("RTP (FTP) prices file processing failed.",
				"Reason: RTP prices file is empty.");
		}
		else{
			String[] rows = fileContent.split(ENTER_TOKEN);
			String temperatureRow = "";

			for (String row : rows) {
				if(row.trim().length() != 0){
					temperatureRow = row;
					break;
				}
			}
			
			String subject = "RTP (FTP) prices file processing failed.";
			String body = "Reason: RTP prices file format could not be recognized. " +
				"The first line of temperature file should be \"TEMPERATURE , " + 
				"temperature value in F\", for example \"TEMPERATURE , 60\".";

			if(temperatureRow.length() != 0){
				if(temperatureRow.indexOf("TEMPERATURE") == -1 || temperatureRow.indexOf(COMMA_TOKEN) == -1){
					validateResult = false;
					sendMail(subject, body);
				}
				if(validateResult){
					try{
						Double.parseDouble(temperatureRow.substring(temperatureRow.indexOf(COMMA_TOKEN) + 1));
					}
					catch (Exception e) {
						validateResult = false;
						sendMail(subject, body);
					}
				}
			}
		}
		
		return validateResult;
	}

	@Override
	public Double getTemperature(String fileContent) {
		Double temperature = null;
		
		String file = fileContent.trim();
		String[] rows = file.split(ENTER_TOKEN);
		String temperatureRow = rows[0].trim();
		temperature = Double.parseDouble(temperatureRow.trim().substring(temperatureRow.indexOf(COMMA_TOKEN) + 1, temperatureRow.length()));
		
		return temperature;
	}

	private boolean validateFileName(RTPFTPConfiguration config, String fileName){

		boolean validateSuccessful = true;
		
		try {
			
			Date date = new Date();
			int year = date.getYear();
			int month = date.getMonth();
			int dayOfMonth = date.getDate();
			
			String fileNameConfig = config.getFileName();
			SimpleDateFormat dateFormat = new SimpleDateFormat(fileNameConfig.substring(0, fileNameConfig.indexOf("_")) + "'" + fileNameConfig.substring(fileNameConfig.indexOf("_")) + "'");

			Date fileDate = dateFormat.parse(fileName);
			if(fileDate.getYear() == year && fileDate.getMonth() == month && fileDate.getDate() == dayOfMonth){
				validateSuccessful = true;
			}
			else{
				validateSuccessful = false;
			}
			
		} 
		catch (Exception e) {
			validateSuccessful = false;
		}
		
		return validateSuccessful;
	}
	
    private void detectFileUpload(RTPFTPConfiguration config){
		Date start = new Date();
		String[] scanStartTimeParas = config.getStartTime().split(COLON_TOKEN);
		start.setHours(Integer.parseInt(scanStartTimeParas[0]));
		start.setMinutes(Integer.parseInt(scanStartTimeParas[1]));
		
		Date end = new Date();
		String[] scanEndTimeParas = config.getEndTime().split(COLON_TOKEN);
		end.setHours(Integer.parseInt(scanEndTimeParas[0]));
		end.setMinutes(Integer.parseInt(scanEndTimeParas[1]));
		
		Date lastProcessTime = config.getLastProcessTime();
			
		boolean uploadTemperatureFile = true;
		if(lastProcessTime == null){
			uploadTemperatureFile = false;
		}
		else if(lastProcessTime.before(start) || lastProcessTime.after(end)){
			uploadTemperatureFile = false;
		}

		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		if(!uploadTemperatureFile){
			sendMail("RTP (FTP) prices file processing failed.", 
				"Reason: No available price file exists from " + 
				dateFormat.format(start) + " to " + dateFormat.format(end));
		}
	}
	
    private Date toDateTime(Date date, String time) throws ParseException{
    	SimpleDateFormat format=new SimpleDateFormat("yyyy/MM/dd");
    	String dateString=format.format(date);
    	format.applyPattern("yyyy/MM/dd HH:mm");
    	return format.parse(dateString+" "+time);
    }
    
	@Override
	public void updateTemperature() {
		
		if(getRTPTemperatureUpdateFeature()){

			RTPFTPConfiguration config = getRTPFTPConfiguration();
			boolean lastScan=false;
			try {
				Date now =new Date();
				Date start=toDateTime(now, config.getStartTime());
				Date end=toDateTime(now, config.getEndTime());
				
				if (start.getTime()>now.getTime() || end.getTime()<now.getTime())
					return;
				
				if (end.getTime()<=(now.getTime()+Integer.parseInt(config.getInterval())*DateUtil.MSEC_IN_MIN)) {
					lastScan=true;
				}
				
			} catch (ParseException e1) {
			}
			
			int success=process(config, lastScan);
			
			if (success==1) {
				config.setLastProcessTime(new Date());
				config.setSentConnError(false);
				rtpFTPConfigurationEAO.saveRTPFTPConfiguration(config);
			}
			
			if (success==0 && !lastScan) {
				config.setSentConnError(true);
				rtpFTPConfigurationEAO.saveRTPFTPConfiguration(config);
			}
			
			if (lastScan) {
				config.setSentConnError(false);
				rtpFTPConfigurationEAO.saveRTPFTPConfiguration(config);
			}
		}
	}
	
	
	private int process(RTPFTPConfiguration config, boolean lastScan) {
		String fileContent=null;
		SystemManager systemManager = EJBFactory
				.getBean(SystemManagerBean.class);
		try {
			fileContent = getRemoteFile(config);
		} catch (AppServiceException e) {
			if (!config.isSentConnError()) {
				
				String subject="";
				if(systemManager.getPss2Features().isProductionServer()){
					subject="FTP server isn't reachable (SCE RTP temperature update)";
				} else {
					subject="FTP server isn't reachable (SCE2 RTP temperature update)";
				}
				sendMail(subject, e.getMessage());
				log.error(LogUtils.createExceptionLogEntry(null, "SCE RTP temperature update", e));
			}
			return 0; //FTP connection error
		}
		
		if (lastScan && fileContent==null && config.isRequired()){
				detectFileUpload(config);					
			return -1; //failure -> do nothing
		}
		

		try {
			if(fileContent!=null && validateFeedFileFormat(fileContent)){
				Double temperature = getTemperature(fileContent);
				
				if(temperature > config.getMaxTemperature() || temperature < config.getMinTemperature()){
					sendMail("RTP (FTP) prices file processing failed.", 
						"Reason: Temperature input (" + temperature + 
						"F) is out of range (Min " + 
						config.getMinTemperature() + "F, Max " + 
						config.getMaxTemperature() + "F).");
				} else{
					
					updateWeather(temperature);
					return 1; //SUCCESS
				}
			}

		} catch (AppServiceException e) {
			String subject="RTP (FTP) prices file processing failed.";
			sendMail(subject, e.getMessage());
			log.error(LogUtils.createExceptionLogEntry(null, "SCE RTP weather update", e));
		}
		
		return -1;
	}
	
	private String getRemoteFile(RTPFTPConfiguration config) throws AppServiceException{
		
			FTPClient ftpClient = null;
			
			String fileContent = null; 
			try{
				ftpClient = new SSHFTPClientImpl();
				ftpClient.connect(config.getUrl(), Integer.parseInt(config.getPort()), config.getUserName(), config.getPassword());
				
				String fileNameConfig = config.getFileName();
				SimpleDateFormat dateFormat = new SimpleDateFormat(fileNameConfig.substring(0, fileNameConfig.indexOf("_")) + "'" + fileNameConfig.substring(fileNameConfig.indexOf("_")) + "'");
				String filename=dateFormat.format(new Date());
				if (ftpClient.fileIsExist(filename)){
					fileContent = ftpClient.getRemoteFileContent(filename);
					
					ftpClient.delete(filename);
					ftpClient.makeDir(config.getPath());
					ftpClient.cd(config.getPath());
					backupFile(ftpClient, fileContent, filename);
				}
			}
			catch(AuthentificationException ae){
				String body = "FTP connection failed: User name or password is not correct.";
				throw new AppServiceException(body, ae);
			}
			catch (ConnectionNotOpenException cnoe) {
				String body = "Can not connect to FTP server " + 
					config.getUrl() + ":" + config.getPort() + ".";
				throw new AppServiceException(body, cnoe);
			} catch(Exception e){
				throw new AppServiceException(e.getMessage(), e);
			} finally{
				if(ftpClient != null){
					ftpClient.close();				
				}
			}
		return fileContent;
	}

	@Override
	public void testFTPConnection(String url, Integer port, String userName, String password, String path) throws Exception {

		FTPClient ftpClient = null;
		boolean testSuccessful = true;
		try{
			ftpClient = new SSHFTPClientImpl();
			
			try{
				ftpClient.connect(url, port, userName, password);
			}
			catch (AuthentificationException e) {
				testSuccessful = false;
				throw e;
			}
			catch (Exception e) {
				testSuccessful = false;
				throw e;
			}
			
			if(testSuccessful){
				try{
					ftpClient.cd(path);				
				}
				catch (Exception e) {
					testSuccessful = false;
					throw e;
				}				
			}
			ftpClient.close();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			if(ftpClient != null){
				ftpClient.close();				
			}
		}
	}

}
