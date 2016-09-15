/**   
* @Title: UkWeatheRretriever.java 
* @Package com.akuacom.pss2.facdash.web.listener 
* @Description: TODO 
* @author liu   
* @date Oct 25, 2012 3:48:24 PM   
*/ 
package com.akuacom.pss2.facdash.web.listener;

import java.net.URL;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.facdash.web.listener.uk.UKForecastXMLParser;
import com.akuacom.pss2.facdash.web.listener.uk.UKObservationXMLParser;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.weather.ForecastConfig;
import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.WeatherManager;
import com.akuacom.pss2.weather.uk.UKCityCache;
import com.akuacom.pss2.weather.uk.UKCityCacheBean;


public class UkWeatheRretriever implements WeatheRretriever {
	
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	private static Logger log = Logger.getLogger(UkWeatheRretriever.class.getSimpleName());
	
	private String apiKey="030dca1a-fb28-4e7a-85b7-c93408e6b5ee"; // Retrieve key from pss2 core property
	private String urlForecast="http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/xml/";// Retrieve key from pss2 core property
	private String urlObserve="http://datapoint.metoffice.gov.uk/public/data/val/wxobs/all/xml/";
	@Override
	public void retriveWeather() {
		
		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
        
		PSS2Properties properties = systemManager.getPss2Properties();
		String key = properties.getWeatherUkApiKey();
		String urlF=properties.getWeatherUkForecastURL();
		String urlO=properties.getWeatherUkObservationURL();
		if(key!=null&&(!key.equalsIgnoreCase(""))){
			apiKey = key;
		}
		if(urlF!=null&&(!urlF.equalsIgnoreCase(""))){
			urlForecast = urlF;
		}
		if(urlO!=null&&(!urlO.equalsIgnoreCase(""))){
			urlObserve = urlO;
		}
		ForecastConfig config = manager.getConfig();
		ForecastWeather entity = new ForecastWeather();
		String cityName_configured = config.getCity();
		String cityID_configured = config.getZipcode();
		try {
			entity = retrieveForecast(cityID_configured,apiKey,entity);
			entity = retrieveObervation(cityID_configured,apiKey,entity);
		} catch (Exception e) {
			log.warning("getForecastData error...Msg=" + e.getMessage()); 
		}
		entity.setCity(cityName_configured);
		manager.saveWeather(entity);
	}

	public ForecastWeather retrieveForecast(String forecastID,String apiKey,ForecastWeather entity) throws Exception{
		
		if(forecastID!=null&&(!forecastID.equalsIgnoreCase(""))&&(apiKey!=null)&&(!apiKey.equalsIgnoreCase(""))){
			//EXAMPLE:---http://datapoint.metoffice.gov.uk/public/data/val/wxfcs/all/xml/3781?res=daily&key=030dca1a-fb28-4e7a-85b7-c93408e6b5ee
			URL url = new URL(urlForecast+forecastID+"?res=daily&key="+apiKey);
			SAXReader reader = new SAXReader();
			Document document = reader.read(url);
			entity = UKForecastXMLParser.parse(document, entity);
		}else{
			
		}
		return entity;
		
	}
	
	public ForecastWeather retrieveObervation(String forecastID,String apiKey,ForecastWeather entity) throws Exception{
		
		if(forecastID!=null&&(!forecastID.equalsIgnoreCase(""))&&(apiKey!=null)&&(!apiKey.equalsIgnoreCase(""))){
			
			UKCityCacheBean forecast = UKCityCache.getInstance().findCityByID(forecastID);
			UKCityCacheBean observation =UKCityCache.getInstance().matchObservationFromForecast(forecast);
			String observationID = observation.getId();
			
			log.info("The UK OBSERVATION name is : " + observation.getName()); 
			//EXAMPLE:
			//http://datapoint.metoffice.gov.uk/public/data/val/wxobs/all/xml/<OBSERVATION ID>?res=hourly&key=<API key>
			URL url = new URL(urlObserve+observationID+"?res=hourly&key="+apiKey);
			SAXReader reader = new SAXReader();
			Document document = reader.read(url);
			String realTimeTemperature = UKObservationXMLParser.getRealTimeTemperature(document);
			entity.setTemp(realTimeTemperature);
		}else{
			
		}
		return entity;
		
	} 
	
}
