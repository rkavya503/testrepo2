package com.akuacom.pss2.rtp.ftp;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.common.exception.AppServiceException;

public interface RTPFTPConfigurationManager {

    @Remote
    public interface R extends RTPFTPConfigurationManager {   }
    @Local
    public interface L extends RTPFTPConfigurationManager {   }

	/**
	 * Get RTP temperature file FTP configuration 
	 * 
	 * @return RTPFTPConfiguration
	 */
	public RTPFTPConfiguration getRTPFTPConfiguration();
	
	/**
	 * Save RTP temperature file FTP configuration
	 * 
	 * @param rtpFTPConfiguration
	 */
	public void saveRTPFTPConfiguration(RTPFTPConfiguration rtpFTPConfiguration);
	
	/** 
	 * Get RTP temperature update feather on/off value from core property 
	 */
	public boolean getRTPTemperatureUpdateFeature();

	/**
	 * update the weather information of the yesterday
	 * 
	 * @param highTemperature
	 * @throws AppServiceException
	 */
	void updateWeather(Double highTemperature) throws AppServiceException;
	

    /**
     * Get temperature from RTPPrice file.
     * 
     * Assumption: If file contains temperature, the temperature must in the first not empty line.
     * 
     * @return
     */
    public Double getTemperature(String fileContent);    
    
    /**
     * Send mail
     * 
     * @param subject
     *            the mail subject 
     * @param content
     *            the mail content
     */
    public void sendMail(String subject, String content);
    
    /**
     * Get temperature from feed file, then update RTP temperature. 
     */
    public void updateTemperature();

    /**
     * Test FTP connection
     * 
     * @param url
     * @param port
     * @param userName
     * @param password
     * @param path
     * @throws Exception
     */
    public void testFTPConnection(String url, Integer port, String userName, String password, String path) throws Exception;
}
