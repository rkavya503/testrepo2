package com.akuacom.pss2.facdash;


import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.FormatType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.ProductType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.UnitType;
import gov.weather.graphical.xml.dwmlgen.schema.dwml_xsd.WeatherParametersType;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgen;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDay;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenByDayResponse;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NDFDgenResponse;
import gov.weather.graphical.xml.dwmlgen.wsdl.ndfdxml_wsdl.NdfdXMLStub;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.AxisFault;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.weather.ForecastConfig;
import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.WeatherManager;
import com.akuacom.pss2.weather.uk.UKCityCache;
import com.akuacom.pss2.weatherInfo.WeatherXMLReader;
import com.akuacom.pss2.weatherInfo.WeatherXMLReaderFactory;

public class WeatherWidget {
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	private static final String SEI_URL= "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLserver.php";
	private static final String DEFAULT_ZIPCODE= "90001";

	Logger log = Logger.getLogger(WeatherWidget.class.getSimpleName());

	public WeatherWidget(){
		long startTime = System.currentTimeMillis(); 
		retrieveFromDB();
		retrieveWeatherConfigInfo();
		long duration = System.currentTimeMillis() - startTime;
		log.warning("WeatherWidget construct took "+duration+ "(ms)");
	}

	
	public void retrieveFromDB(){
		String pattern1 = "MMM dd yyyy";
		String pattern2 = "MMM dd";

		DateFormat format = new SimpleDateFormat(pattern1);
//		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		ForecastWeather vo = manager.getWeather();
		currentDate = format.format(new Date());
		currentTemperature = vo.getTemp();
		currentWeather = vo.getWeather_0();
		currentWeatherIcon = vo.getWeather_icon_0();
		currentHigh = vo.getMax_0();
		currentLow = vo.getMin_0();
		currenthumidity = vo.getHum();
		unit = vo.getUnit();
		
		format = new SimpleDateFormat(pattern2);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		nextDate = format.format(cal.getTime());
		nextWeather = vo.getWeather_1();
		nextHigh = vo.getMax_1();
		nextLow = vo.getMin_1();
		nextWeatherIcon = vo.getWeather_icon_1();
		zipCode = vo.getCity();
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
	private String unit;

	public String getNextWeatherIcon() {
		return nextWeatherIcon;
	}
	public void setNextWeatherIcon(String nextWeatherIcon) {
		this.nextWeatherIcon = nextWeatherIcon;
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

	public String getCurrentWeatherIcon() {
		return currentWeatherIcon;
	}
	public void setCurrentWeatherIcon(String currentWeatherIcon) {
		this.currentWeatherIcon = currentWeatherIcon;
	}

	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	/**
	 * Get weather info from NOAA and populate data info into bean
	 * @throws RemoteException
	 * @throws AxisFault
	 */
	@SuppressWarnings("unchecked")
	public void initWeatherInfo() throws AxisFault, RemoteException {
		ParticipantManager pm = EJB3Factory.getBean(ParticipantManager.class);
		Participant p = pm.getParticipant(FDUtils.getParticipantName());
		this.setZipCode(p.getAddress());

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



	private static HashMap<String, Object> getForecastWeather(double latitude, double longitude) throws AxisFault, RemoteException {

		NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);
		NDFDgenByDay pa = new NDFDgenByDay();
		pa.setLatitude(new BigDecimal(latitude));
		pa.setLongitude(new BigDecimal(longitude));
		pa.setNumDays(BigInteger.valueOf(2L));
		pa.setStartDate(new Date());

        pa.setFormat(FormatType.value1);
        pa.setUnit(UnitType.e);

		NDFDgenByDayResponse rs = stub.nDFDgenByDay(pa);

		InputStream is = new ByteArrayInputStream(rs.getDwmlByDayOut().getBytes());
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserNDFDgenByDayResponse(map, is);
		} catch (Exception ex) {
			Logger.getLogger(WeatherWidget.class.getSimpleName()).warning("getForecastWeather failed. Msg1=" + ex.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.getLogger(WeatherWidget.class.getSimpleName()).warning("getForecastWeather failed. Msg2=" + e.getMessage());
			}
		}

		return map;
	}
/*
	private static HashMap<String, Object> getTodayWeather(double latitude, double longitude) throws AxisFault, RemoteException {

		NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);
		NDFDgen par = new NDFDgen();

		Calendar start = GregorianCalendar.getInstance();

		par.setStartTime(start);

		par.setEndTime(start);

		par.setLatitude(new BigDecimal(latitude));
		par.setLongitude(new BigDecimal(longitude));
        par.setProduct(NdfdXMLStub.ProductType.value1);

		WeatherParametersType type = new WeatherParametersType();

		type.setIcons(true);
		type.setMaxt(true);
		type.setMint(true);
		type.setRh(true);
		type.setWx(true);
		type.setWdir(true);
		type.setWspd(true);
		type.setTemp_r(true);
		type.setAppt(true);
		par.setWeatherParameters(type);


		NDFDgenResponse rs = stub.nDFDgen(par);

		InputStream is = new ByteArrayInputStream(rs.getDwmlOut().getBytes());
        HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserNDFDgenResponse(map, is);
		} catch (Exception ex) {
			Logger.getLogger(WeatherWidget.class.getSimpleName()).warning("getTodayWeather failed. Msg1=" + ex.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.getLogger(WeatherWidget.class.getSimpleName()).warning("getTodayWeather failed. Msg2=" + e.getMessage());
			}
		}

		return map;
	}

 *
 */


    public static HashMap<String, Object> getTodayWeather(double latitude, double longitude) throws AxisFault, RemoteException {
    	long startTime = System.currentTimeMillis();
	
	NdfdXMLStub stub = new NdfdXMLStub(SEI_URL);

		NDFDgen par = new NDFDgen();

		Calendar start = GregorianCalendar.getInstance();

		par.setStartTime(start);
		par.setEndTime(start);

		par.setLatitude(new BigDecimal(latitude));
		par.setLongitude(new BigDecimal(longitude));
	    par.setProduct(ProductType.value1);

		WeatherParametersType type = new WeatherParametersType();

		type.setIcons(true);
		type.setMaxt(true);
		type.setMint(true);
		type.setRh(true);
		type.setWx(true);
		type.setWdir(true);
		type.setWspd(true);
		type.setTemp_r(true);
		type.setAppt(true);

		par.setWeatherParameters(type);

		NDFDgenResponse rs = stub.nDFDgen(par);

		InputStream is = new ByteArrayInputStream(rs.getDwmlOut().getBytes());
        HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			WeatherXMLReader myReader = WeatherXMLReaderFactory.getInstance();
			myReader.parserNDFDgenResponse(map, is);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {

				is.close();
			} catch (IOException e) {
				System.out.println( e.getMessage());
			}
		}

		 long duration = System.currentTimeMillis() - startTime;
		 System.out.println("WeatherWidget getTodayWeather took "+duration+ "(ms)");
		 
		return map;
	}




    public static void exportWeatherAction(String xmlFeed) throws Exception {
    	long startTime = System.currentTimeMillis();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.reset();
        response.addHeader("cache-control", "must-revalidate");

        String filename = "participantList.csv";
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                    + filename + "\"");
        
        long duration = System.currentTimeMillis() - startTime;
		 System.out.println("WeatherWidget exportWeatherAction took "+duration+ "(ms)");
        
         response.getWriter().print(xmlFeed);


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
	/**
	 * DRMS-7666 UK TEMPERATURE SUPPORT
	 */
	public void retrieveWeatherConfigInfo(){
		ForecastConfig config = manager.getConfig();
		if(config.getCountry()!=null&&config.getCountry().equalsIgnoreCase("UK")){
			String forecastID = config.getZipcode();
			String forecastName = UKCityCache.getInstance().getForecastName(forecastID);
			String observationName = UKCityCache.getInstance().getObservationName(forecastID);
			setForecastName(forecastName);
			setObservationName(observationName);
		}
	}
	private String forecastName;
	private String observationName;

	/**
	 * @return the forecastName
	 */
	public String getForecastName() {
		if(forecastName==null){
			forecastName="";
		}
		return forecastName;
	}


	/**
	 * @param forecastName the forecastName to set
	 */
	public void setForecastName(String forecastName) {
		this.forecastName = forecastName;
	}


	/**
	 * @return the observationName
	 */
	public String getObservationName() {
		if(observationName==null){
			observationName="";
		}
		return observationName;
	}


	/**
	 * @param observationName the observationName to set
	 */
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
	
}
