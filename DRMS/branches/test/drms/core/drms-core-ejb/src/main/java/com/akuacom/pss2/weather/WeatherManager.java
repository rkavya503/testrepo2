package com.akuacom.pss2.weather;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface WeatherManager {
	 @Remote
	 public interface R extends WeatherManager {}
	 @Local
	 public interface L extends WeatherManager {}
	 
	 void saveWeather(ForecastWeather entity);
	 
	 void saveWmoMapping(ForecastWmo entity);
	 
	 void saveConfig(ForecastConfig entity);
	 
	 ForecastWmo getWmoByCity(String city);
	 
	 ForecastConfig getConfig();
	 
	 ForecastWeather getWeatherByCity(String city);
	 
	 ForecastWeather getWeather();
	 
	 /**
	  * 1. If exist an appropriate record, return 
	  * 2. if not exist, generate and store it, then return
	  * @param type
	  * @return
	  */
	 String getWeatherIconMapping(String type);
	 
	 /**
	  * If not exist an appropriate record return null;
	  * @param type
	  * @return
	  */
	 WeatherCategory getWeatherIconFromDB(String type);

}
