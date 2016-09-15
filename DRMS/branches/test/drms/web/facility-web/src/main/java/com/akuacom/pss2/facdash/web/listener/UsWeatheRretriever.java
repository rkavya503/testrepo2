package com.akuacom.pss2.facdash.web.listener;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.axis2.AxisFault;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.weather.ForecastConfig;
import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.WeatherManager;

public class UsWeatheRretriever implements WeatheRretriever {
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	private static final String DEFAULT_ZIPCODE= "90001";
	private static final String TEMP_UINT= "&deg;F";
	private String zipCode;
	private String currentDate;

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public void retriveWeather() {
//		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		ForecastConfig config = manager.getConfig();
		String zipcode = config.getZipcode();
		
		ForecastWeather entity = new ForecastWeather();
		entity.setUnit(TEMP_UINT);
		if (!this.isValidZipCode()) {
			this.setZipCode(zipcode);
			if (!this.isValidZipCode()) {
				this.setZipCode(DEFAULT_ZIPCODE);
			}
		}

        com.akuacom.pss2.weatherInfo.WeatherClient utilWeather =
                new  com.akuacom.pss2.weatherInfo.WeatherClient();

        HashMap<String, String> latLonMap;
        HashMap<String, Object> map1 = null;
        HashMap<String, Object> map2 = null;
		try {
			latLonMap = utilWeather.getLanLonByZipcode(this.getZipCode());
	

			double latitude = Double.valueOf((String) latLonMap.get("latitude"));
			double longitude = Double.valueOf((String) latLonMap.get("longitude"));
	
	        map1 = utilWeather.getTodayWeather(latitude, longitude);
	        map2 = utilWeather.getForecastWeather(latitude, longitude);
	        
			String pattern1 = "MMM dd yyyy";
			String pattern2 = "MMM dd";

			DateFormat format = new SimpleDateFormat(pattern1);
			this.setCurrentDate(format.format(new Date()));

			entity.setCity(this.getZipCode());
			entity.setHum((String) map1.get("humidity"));
			entity.setTemp((String) map1.get("apparent"));

	        // todays weather
			if(map2.get("weather-conditions")!=null){
				entity.setWeather_0((String)((List<String>)map2.get("weather-conditions")).get(0));
			}
			if(map1.get("conditions-icon")!=null){
				entity.setWeather_icon_0(convertIconName((String)map1.get("conditions-icon")));
			}
			if(map2.get("maximum")!=null){
				entity.setMax_0((String)((List<String>)map2.get("maximum")).get(0));
			}
			if(map2.get("minimum")!=null){
				entity.setMin_0((String)((List<String>)map2.get("minimum")).get(0));
			}
			format = new SimpleDateFormat(pattern2);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);

	        // tomorrows weather
	        if(map2.get("weather-conditions")!=null){
	        	entity.setWeather_1((String)((List<String>)map2.get("weather-conditions")).get(1));
			}
			if(map2.get("conditions-icon")!=null){
				entity.setWeather_icon_1(convertIconName((String)((List<String>)map2.get("conditions-icon")).get(1)));
			}
			if(map2.get("maximum")!=null){
				entity.setMax_1((String)((List<String>)map2.get("maximum")).get(1));
			}
			if(map2.get("minimum")!=null){
				entity.setMin_1((String)((List<String>)map2.get("minimum")).get(1));
			}
			
			manager.saveWeather(entity);
			
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isValidZipCode() {
		boolean res = true;
		if (this.getZipCode() == null || this.getZipCode().trim().length() != 5) {
			res = false;
		} else {
			try {
				Integer.parseInt(getZipCode());
			} catch (NumberFormatException nfex) {
				res = false;
			}
		}

		return res;
	}
	
	private static String convertIconName(String name) {
        try{
		if(name==null){
			return "";
		}
		String[] strs = name.split("/");
		String nameSubfix = strs[strs.length-1];

		if (nameSubfix.startsWith("skc")||nameSubfix.startsWith("nskc")){
			return "skc";
		}

		if (nameSubfix.startsWith("few")||nameSubfix.startsWith("nfew")){
			return "few";
		}

		if (nameSubfix.startsWith("sct")||nameSubfix.startsWith("nsct")){
			return "sct";
		}

		if (nameSubfix.startsWith("bkn")||nameSubfix.startsWith("nbkn")){
			return "bkn";
		}

		if (nameSubfix.startsWith("ovc")||nameSubfix.startsWith("novc")){
			return "ovc";
		}

		if (nameSubfix.startsWith("hot")){
			return "hot";
		}

		if (nameSubfix.startsWith("cold")){
			return "cold";
		}

		if (nameSubfix.startsWith("wind")||nameSubfix.startsWith("nwind")){
			return "wind";
		}

		if (nameSubfix.startsWith("blizzard")){
			return "blizzard";
		}

		if (nameSubfix.startsWith("du")){
			return "du";
		}

		if (nameSubfix.startsWith("fg")||nameSubfix.startsWith("nfg")){
			return "fg";
		}

		if (nameSubfix.startsWith("fu")){
			return "fu";
		}

		if (nameSubfix.startsWith("hi_shwrs")||nameSubfix.startsWith("hi_nshwrs")){
			return "hi_shwrs";
		}

		if (nameSubfix.startsWith("shra")){
			return "shra";
		}

		if (nameSubfix.startsWith("rasn")||nameSubfix.startsWith("nrasn")){
			return "rasn";
		}

		if (nameSubfix.startsWith("raip")||nameSubfix.startsWith("nraip")){
			return "raip";
		}

		if (nameSubfix.startsWith("ra")||nameSubfix.startsWith("nra")){
			return "ra";
		}

		if (nameSubfix.startsWith("sn")||nameSubfix.startsWith("nsn")){
			return "sn";
		}

		if (nameSubfix.startsWith("fzra")){
			return "fzra";
		}

		if (nameSubfix.startsWith("mix")){
			return "mix";
		}

		if (nameSubfix.startsWith("ip")){
			return "ip";
		}

		if (nameSubfix.startsWith("scttsra")||nameSubfix.startsWith("nscttsra")){
			return "scttsra";
		}

		if (nameSubfix.startsWith("tsra")||nameSubfix.startsWith("ntsra")){
			return "tsra";
		}


        } catch (Exception e){
            e.printStackTrace();
            	return "";
        }
     return "";
	}
	
}
