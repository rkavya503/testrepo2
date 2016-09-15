package com.akuacom.pss2.facdash.web.listener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.weather.ForecastConfig;
import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.ForecastWmo;
import com.akuacom.pss2.weather.WeatherManager;

/**
 *  Product listing
 *	There are eight Australia-specific Precis (In-brief) forecast products - 
 *	one National product and one for each State or Territory. 
 *	The national product is a composite of the State and Territory products. 
 *	These files are updated whenever new forecast data is available.
 *	National	IDA00001	text	html	 
 *	NSW	IDA00002	text	html	XML (schema)  Icon codes
 *	Vic	IDA00003	text	html	XML  (schema)  Icon codes
 *	QLD	IDA00004	text	html	 
 *	SA	IDA00005	text	html	XML  (schema)  Icon codes
 *	WA	IDA00006	text	html	 
 *	Tas	IDA00007	text	html	XML  (schema)  Icon codes
 *	NT	IDA00008	text	html	 
 *
 * @author Akuacom
 *
 */
public class AusWeatheRretriever implements WeatheRretriever {
	private static final String SPLIT_SYM = ",";
	private static final String SPLIT_SYM2 = "#";
	private static final String DATA_SYM = "[data]";
	private static final String TEMP_UINT= "&deg;C";
	private static final String BASE_URL= "http://www.bom.gov.au/fwo/";
	private static final String FTP_URL= "ftp2.bom.gov.au";
	
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	private static Logger log = Logger.getLogger(AusWeatheRretriever.class.getSimpleName());
	
	@Override
	public void retriveWeather() {
//		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		ForecastConfig config = manager.getConfig();
		
		String cityname_configured = config.getCity();
		String statename_configured = config.getState();
		
		ForecastWeather entity = new ForecastWeather();
		entity.setUnit(TEMP_UINT);
		
		ForecastWmo wmo = manager.getWmoByCity(cityname_configured);
		if(wmo==null) {
			log.warning("Retriving observational data error: obsverational station not exist");
			return;
		}
		//retrieve latest observational data 
		getObsverData(cityname_configured, entity, wmo);  
        //retrieve forecast data
        getForecastData(manager, cityname_configured, statename_configured, entity);
		    
		manager.saveWeather(entity);
	}
    
	private void getForecastData(WeatherManager manager,
			String cityname_configured, String statename_configured,
			ForecastWeather entity){
		// 1.Create a client instance for FTP connect
        FTPClient client = new FTPClient();
        // 2.Connect to FTP server
		try {
			client.connect(FTP_URL);
		} catch (Exception e1) {
			log.warning("FTP connect error...Msg="+ e1.getMessage());
		} 
		// 3.Login as anonymous
		boolean login = false;
		try {
			login = client.login("anonymous", "guest");
		} catch (IOException e1) {
			log.warning("FTP login error...Msg="+ e1.getMessage());
		}
		// 4.Change directory to anon/gen/fwo/
	    try {
			client.changeWorkingDirectory("anon/gen/fwo/");
		} catch (IOException e1) {
			log.warning("FTP changeWorkingDirectory error...Msg="+ e1.getMessage());
		}
	    // 5.List all files in current directory
	    FTPFile[] ftpFiles = null;
		try {
			ftpFiles = client.listFiles();
		} catch (IOException e1) {
			log.warning("FTP listFiles error...Msg="+ e1.getMessage());
		}
		// 6.Traverse all files 
	    for (FTPFile ftpFile : ftpFiles){
    	    if(ftpFile.getType() == FTPFile.FILE_TYPE
    	    		&& ftpFile.getName().equalsIgnoreCase(getProduct(statename_configured))){
    	    	InputStream is = null;
    	    	try{
	    	        // 7.Choose a product(in fact it's a file) by the given state, and make sure the file is a regular file
		    	    is = client.retrieveFileStream(getProduct(statename_configured));
		    	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    	    String strLine;
		    	    // 8.Read File Line By Line
		    	    while ((strLine = br.readLine()) != null){
		    		    String[] content = strLine.split(SPLIT_SYM2);
		    		    if(cityname_configured.equalsIgnoreCase(content[1])){
		    		    	// 9.Find out the forecast record by city name
		    			    entity.setMin_0(content[6]);
		    			    entity.setMax_0(content[7]);
		    			    entity.setMin_1(content[8]);
		    			    entity.setMax_1(content[9]);
		    			    entity.setWeather_0(content[22]);
		    			    entity.setWeather_icon_0(manager.getWeatherIconMapping(content[22]));
		    			    entity.setWeather_1(content[23]);
		    			    entity.setWeather_icon_1(manager.getWeatherIconMapping(content[23]));
		    			    break;
		    		    }
		    	    }//end of inner loop
    	    	}catch(Exception ex){
    	    		log.warning("getForecastData error...Msg=" + ex.getMessage()); 
    	    	}finally{
    	    	    try {
						is.close();
					} catch (IOException e) {
						log.warning("getForecastData inputStream close error...Msg=" + e.getMessage()); 
					}
    	    	}
	    	  break;
    	    }
	    	  
	    }//end of loop
		// 10.logout
	    if (login) {
	    	log.info("Login success...");
	    	boolean logout = false;
			try{
				logout = client.logout();
			} catch (IOException e) {
				log.warning("FTP logout error...Msg="+ e.getMessage());
			}
	        if (logout){
	        	log.info("Logout from FTP server...");
	        }
	    } else{
	    	log.warning("Login fail...");
	    }
	    // 11.disconnect
	    try {
			client.disconnect();
		} catch (IOException e) {
			log.warning("FTP disconnect error...Msg="+ e.getMessage());
		}
	}

	private void getObsverData(String cityname_configured,
			ForecastWeather entity, ForecastWmo wmo) {
		String url = BASE_URL+wmo.getWmo();
		URL sjwurl = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        try{
            sjwurl = new URL(url);
            httpUrl = (HttpURLConnection) sjwurl.openConnection();
            httpUrl.connect(); 
            bis = new BufferedInputStream(httpUrl.getInputStream());           
	        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
	    	String strLine;
	    	//Read File Line By Line
	    	boolean isData = false;
	    	while ((strLine = br.readLine()) != null){
	    		if(isData){
	    			String[] content = strLine.split(SPLIT_SYM);
		    		if("0".equalsIgnoreCase(content[0])){//get the first record
		    			entity.setCity(cityname_configured);
		    			entity.setTemp(content[7]);// temperature
		    			entity.setHum(content[25]);// humidity
		    			
		    			break;
		    		}
	    		}
	    		if (!isData&&strLine.contains(DATA_SYM)){
	    	        isData = true;
	    	    }
	        }
        }catch(Exception e) { 
        	log.warning("getObsverData error...Msg=" + e.getMessage()); 
        }finally{
        	try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            httpUrl.disconnect();  
        }
	}
	
	private String getProduct(String sateSnm){
		if(sateSnm==null) return "IDA00001.dat";
		
		if("NSW".equalsIgnoreCase(sateSnm)){
			return "IDA00002.dat";
		}
		
		if("Vic".equalsIgnoreCase(sateSnm)){
			return "IDA00003.dat";
		}
		
		if("QLD".equalsIgnoreCase(sateSnm)){
			return "IDA00004.dat";
		}
		
		if("SA".equalsIgnoreCase(sateSnm)){
			return "IDA00005.dat";
		}
		
		if("WA".equalsIgnoreCase(sateSnm)){
			return "IDA00006.dat";
		}
		
		if("Tas".equalsIgnoreCase(sateSnm)){
			return "IDA00007.dat";
		}
		
		if("NT".equalsIgnoreCase(sateSnm)){
			return "IDA00008.dat";
		}
		
		return "IDA00001.dat";
	}
}
