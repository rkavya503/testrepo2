package com.akuacom.pss2.weather.uk;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




public class UKCityCache {
	private static final Logger log = Logger.getLogger(UKCityCache.class);
	private static UKCityCache instance = new UKCityCache();
	private List<UKCityCacheBean> forecastList = new ArrayList<UKCityCacheBean>();
	private List<UKCityCacheBean> observationList = new ArrayList<UKCityCacheBean>();
	private UKCityCache(){
		try {
			loadUKForecasts();
			loadUKObservations();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public static UKCityCache getInstance(){
		return instance;
	}
	@SuppressWarnings("rawtypes")
	private void loadUKForecasts() throws Exception{
		forecastList.clear();
		SAXReader reader = new SAXReader();
		URL url = UKCityCache.class.getResource("UK_FORECAST_LOCATION.xml");
	    Document document = reader.read(url);
	    Element root = document.getRootElement();
	    for ( Iterator i = root.elementIterator("Location"); i.hasNext();) {
	        Element e = (Element) i.next();
	        UKCityCacheBean bean = UKCityCacheBean.parse(e);
	        forecastList.add(bean);
	     }
	    log.info("UK forecast Size is: "+forecastList.size());
	}
	
	public List<UKCityCacheBean> findCity(String cityName){
		List<UKCityCacheBean> result = new ArrayList<UKCityCacheBean>();
		for(UKCityCacheBean bean : forecastList){
			String name = bean.getName();
			if(name!=null){
				name = name.toLowerCase();
				if(name.indexOf(cityName.toLowerCase())>-1){
					result.add(bean);
				}				
			}
		}
		return result;
	}
	public UKCityCacheBean findCityByID(String id){
		for(UKCityCacheBean bean : forecastList){
			String cityID = bean.getId();
			if(cityID!=null){
				if(cityID.equalsIgnoreCase(id)){
					return bean;
				}				
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private void loadUKObservations() throws Exception{
		observationList.clear();
		SAXReader reader = new SAXReader();
		URL url = UKCityCache.class.getResource("UK_OBSERVATION_LOCATION.xml");
	    Document document = reader.read(url);
	    Element root = document.getRootElement();
	    for ( Iterator i = root.elementIterator("Location"); i.hasNext();) {
	        Element e = (Element) i.next();
	        UKCityCacheBean bean = UKCityCacheBean.parse(e);
	        observationList.add(bean);
	     }
	    log.info("UK observation Size is: "+observationList.size());
	}
	
	public List<UKCityCacheBean> findIntersectionFromForecastToObserve(){
		List<UKCityCacheBean> result = new ArrayList<UKCityCacheBean>();
		for(UKCityCacheBean observe:observationList){
			for(UKCityCacheBean forecast:forecastList){
				if(observe.equals(forecast)){
					result.add(forecast);
				}
			}
		}
		return result;
	}
	public UKCityCacheBean matchObservationFromForecast(UKCityCacheBean forecast){
		UKCityCacheBean bean = null;
		Double nearest = null;
		if(forecast==null){
			return null;
		}
		double latF=Double.parseDouble(forecast.getLatitude());
		double lonF=Double.parseDouble(forecast.getLongitude());
		
		for(UKCityCacheBean observe:observationList){
			if(observe.equals(forecast)){
				return observe;
			}
			double latO=Double.parseDouble(observe.getLatitude());
			double lonO=Double.parseDouble(observe.getLongitude());
			double distance = Math.sqrt(((latF-latO)*(latF-latO)+(lonF-lonO)*(lonF-lonO)));
			if(nearest==null){
				nearest = distance;
				bean=observe;
			}else{
				if(distance<nearest){
					nearest = distance;
					bean=observe;
				}
			}
		}
		return bean;
	}
	
	public static double distance(double x1,double y1,double x2,double y2){
		double result=0;
		result = Math.sqrt(((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)));
		return result;
	}
	
	public String getForecastName(String forecastID){
		UKCityCacheBean bean = findCityByID(forecastID);
		if(bean!=null){
			return bean.getName();
		}else{
			return "";
		}
	}
	
	public String getObservationName(String forecastID){
		UKCityCacheBean forecast = findCityByID(forecastID);
		UKCityCacheBean observation =matchObservationFromForecast(forecast);
		if(observation!=null){
			return observation.getName();
		}else{
			return "";
		}
	}
}
