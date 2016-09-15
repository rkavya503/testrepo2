package com.akuacom.pss2.weatherInfo;

public class WeatherXMLReaderFactory {
	
	private WeatherXMLReaderFactory(){
		
	}
	
	public static WeatherXMLReader getInstance(){
		return new WeatherXMLReaderDomImpl();
	}
	
	public static WeatherXMLReader getInstance(String clazzName){
		WeatherXMLReader readerImpl = null;
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(clazzName);
			readerImpl = (WeatherXMLReader) clazz.newInstance();  
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}  
		return readerImpl;
	}

}
