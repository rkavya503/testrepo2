/**
 * 
 */
package com.akuacom.pss2.facdash.web.listener;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.axis2.AxisFault;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

public class StatusCache{

	private static Logger log = Logger.getLogger(StatusCache.class.getSimpleName());
//	Logger log = Logger.getLogger(WeatherWidget.class.getSimpleName());
	
	private static StatusCache instance;
	private static final String DEFAULT_ZIPCODE= "90001";
	
	public static StatusCache getInstance(){
		if (instance==null)
			instance=new StatusCache();
		return instance;
	}
	
	private StatusCache(){
	}
	
	public void updateCache() {
		try {
			retrieveWeatherInfo();
		} catch (AxisFault e) {
			log.warning("Init Weather webservice error...try again...Msg=" + e.getMessage());
			try {
				retrieveWeatherInfo();
			} catch (AxisFault e1) {
				log.warning("Second Init Weather failed. Msg=" + e1.getMessage());
			} catch (RemoteException e1) {
				log.warning("Init Weather failed. Msg1=" + e1.getMessage());
			}
		} catch (RemoteException e) {
			log.warning("Init Weather failed. Msg2=" + e.getMessage());
		} catch (Exception ex) {
			log.warning("Init Weather failed. Msg3=" + ex.getMessage());
		}
	}
	
	private String currentDate;
	private String currentTemperature;
	private String currentWeather;
	private String currentWeatherIcon;
	private String currentHigh;
	private String currentLow;
	private String currenthumidity;
	private String nextDate;
	private String nextWeather;
	private String nextHigh;
	private String nextLow;
	private String nextWeatherIcon;
	private String zipCode;

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		StatusCache.log = log;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(String currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	public String getCurrentWeather() {
		return currentWeather;
	}

	public void setCurrentWeather(String currentWeather) {
		this.currentWeather = currentWeather;
	}

	public String getCurrentWeatherIcon() {
		return currentWeatherIcon;
	}

	public void setCurrentWeatherIcon(String currentWeatherIcon) {
		this.currentWeatherIcon = currentWeatherIcon;
	}

	public String getCurrentHigh() {
		return currentHigh;
	}

	public void setCurrentHigh(String currentHigh) {
		this.currentHigh = currentHigh;
	}

	public String getCurrentLow() {
		return currentLow;
	}

	public void setCurrentLow(String currentLow) {
		this.currentLow = currentLow;
	}

	public String getCurrenthumidity() {
		return currenthumidity;
	}

	public void setCurrenthumidity(String currenthumidity) {
		this.currenthumidity = currenthumidity;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public String getNextWeather() {
		return nextWeather;
	}

	public void setNextWeather(String nextWeather) {
		this.nextWeather = nextWeather;
	}

	public String getNextHigh() {
		return nextHigh;
	}

	public void setNextHigh(String nextHigh) {
		this.nextHigh = nextHigh;
	}

	public String getNextLow() {
		return nextLow;
	}

	public void setNextLow(String nextLow) {
		this.nextLow = nextLow;
	}

	public String getNextWeatherIcon() {
		return nextWeatherIcon;
	}

	public void setNextWeatherIcon(String nextWeatherIcon) {
		this.nextWeatherIcon = nextWeatherIcon;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/**
	 * Get weather info from NOAA and populate data info into bean
	 * @throws RemoteException
	 * @throws AxisFault
	 */
	@SuppressWarnings("unchecked")
	public void retrieveWeatherInfo() throws AxisFault, RemoteException {

		if (!this.isValidZipCode()) {

			SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
			PSS2Features features = systemManager.getPss2Features();
			this.setZipCode(features.getSimpleDashBoardDefaultZipcode());

			if (!this.isValidZipCode()) {
				this.setZipCode(DEFAULT_ZIPCODE);
			}
		}

        com.akuacom.pss2.weatherInfo.WeatherClient utilWeather =
                new  com.akuacom.pss2.weatherInfo.WeatherClient();

        HashMap<String, String> latLonMap = utilWeather.getLanLonByZipcode(this.getZipCode());

		double latitude = Double.valueOf((String) latLonMap.get("latitude"));
		double longitude = Double.valueOf((String) latLonMap.get("longitude"));

        HashMap<String, Object> map1 = utilWeather.getTodayWeather(latitude, longitude);
        HashMap<String, Object> map2 = utilWeather.getForecastWeather(latitude, longitude);

		String pattern1 = "MMM dd yyyy";
		String pattern2 = "MMM dd";

		DateFormat format = new SimpleDateFormat(pattern1);
		this.setCurrentDate(format.format(new Date()));

		this.setCurrenthumidity((String) map1.get("humidity"));
		this.setCurrentTemperature((String) map1.get("apparent"));

        // todays weather
		if(map2.get("weather-conditions")!=null){
			this.setCurrentWeather((String)((List<String>)map2.get("weather-conditions")).get(0));
		}
		if(map1.get("conditions-icon")!=null){
			this.setCurrentWeatherIcon(convertIconName((String)map1.get("conditions-icon")));
		}
		if(map2.get("maximum")!=null){
			this.setCurrentHigh((String)((List<String>)map2.get("maximum")).get(0));
		}
		if(map2.get("minimum")!=null){
			this.setCurrentLow((String)((List<String>)map2.get("minimum")).get(0));
		}
		format = new SimpleDateFormat(pattern2);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		this.setNextDate(format.format(cal.getTime()));


        // tomorrows weather
        if(map2.get("weather-conditions")!=null){
			this.setNextWeather((String)((List<String>)map2.get("weather-conditions")).get(1));
		}
		if(map2.get("conditions-icon")!=null){
			this.setNextWeatherIcon(convertIconName((String)((List<String>)map2.get("conditions-icon")).get(1)));
		}
		if(map2.get("maximum")!=null){
			this.setNextHigh((String)((List<String>)map2.get("maximum")).get(1));
		}
		if(map2.get("minimum")!=null){
			this.setNextLow((String)((List<String>)map2.get("minimum")).get(1));
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
