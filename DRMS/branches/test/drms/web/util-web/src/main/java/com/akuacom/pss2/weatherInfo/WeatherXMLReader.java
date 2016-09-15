package com.akuacom.pss2.weatherInfo;

import java.io.InputStream;
import java.util.HashMap;
//TODO:XPATH 
public interface WeatherXMLReader {
	public  void parserNDFDgenResponse(HashMap<String, Object> map, InputStream is);
	
	public  void parserNDFDgenByDayResponse(HashMap<String, Object> map, InputStream is);
	
	public  void parserLatLonListZipCodeResponse(HashMap<String, String> map, InputStream is);

}
