package com.akuacom.pss2.facdash.web.listener.uk;

import java.util.HashMap;
import java.util.Map;

public class UKWeatherUtil {
	private static Map<String,String> weatherMap = new HashMap<String,String>();
	//	NA	Not available
	//	0	Clear sky
	//	1	Sunny
	//	2	Partly cloudy (night)
	//	3	Sunny intervals
	//	4	Dust
	//	5	Mist
	//	6	Fog
	//	7	Medium-level cloud
	//	8	Low-level cloud
	//	9	Light rain shower (night)
	//	10	Light rain shower (day)
	//	11	Drizzle
	//	12	Light rain
	//	13	Heavy rain shower (night)
	//	14	Heavy rain shower (day)
	//	15	Heavy rain
	//	16	Sleet shower (night)
	//	17	Sleet shower (day)
	//	18	Sleet
	//	19	Hail shower (night)
	//	20	Hail shower (day)
	//	21	Hail
	//	22	Light snow shower (night)
	//	23	Light snow shower (day)
	//	24	Light snow
	//	25	Heavy snow shower (night)
	//	26	Heavy snow shower (day)
	//	27	Heavy snow
	//	28	Thundery shower (night)
	//	29	Thundery shower (day)
	//	30	Thunder storm
	//	31	Tropical storm
	//	33	Haze

	/**
	 * @return the weatherMap
	 */
	public static Map<String, String> getWeatherMap() {
		if(weatherMap.size()!=34){
			weatherMap.clear();
			weatherMap.put("NA", "Not available");
			weatherMap.put("0", "Clear sky");
			weatherMap.put("1", "Sunny");
			weatherMap.put("2", "Partly cloudy (night)");
			weatherMap.put("3", "Sunny intervals");
			weatherMap.put("4", "Dust");
			weatherMap.put("5", "Mist");
			weatherMap.put("6", "Fog");
			weatherMap.put("7", "Medium-level cloud");
			weatherMap.put("8", "Low-level cloud");
			weatherMap.put("9", "Light rain shower (night)");
			weatherMap.put("10", "Light rain shower (day)");
			weatherMap.put("11", "Drizzle");
			weatherMap.put("12", "Light rain");
			weatherMap.put("13", "Heavy rain shower (night)");
			weatherMap.put("14", "Heavy rain shower (day)");
			weatherMap.put("15", "Heavy rain");
			weatherMap.put("16", "Sleet shower (night)");
			weatherMap.put("17", "Sleet shower (day)");
			weatherMap.put("18", "Sleet");
			weatherMap.put("19", "Hail shower (night)");
			weatherMap.put("20", "Hail shower (day)");
			weatherMap.put("21", "Hail");
			weatherMap.put("22", "Light snow shower (night)");
			weatherMap.put("23", "Light snow shower (day)");
			weatherMap.put("24", "Light snow");
			weatherMap.put("25", "Heavy snow shower (night)");
			weatherMap.put("26", "Heavy snow shower (day)");
			weatherMap.put("27", "Heavy snow");
			weatherMap.put("28", "Thundery shower (night)");
			weatherMap.put("29", "Thundery shower (day)");
			weatherMap.put("30", "Thunder storm");
			weatherMap.put("31", "Tropical storm");
			weatherMap.put("32", "");
			weatherMap.put("33", "Haze");
		}
		return weatherMap;
	}

	/**
	 * @param weatherMap the weatherMap to set
	 */
	public static void setWeatherMap(Map<String, String> weatherMap) {
		UKWeatherUtil.weatherMap = weatherMap;
	}
	
	
	public static void main(String args[]){
		
	}
}
