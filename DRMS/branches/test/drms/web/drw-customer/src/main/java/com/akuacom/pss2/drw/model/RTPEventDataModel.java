
package com.akuacom.pss2.drw.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.akuacom.pss2.drw.value.WeatherValue;


public class RTPEventDataModel{

	public static final String FORMAT_STYLE="EEE, MMM dd, yyyy";
	
	public static final String FORMAT_STYLE_RTP_DISPLAY="MMMM dd, yyyy";
	
	private String dateString;
	
	private String weekdayString;
	
	private String temperatureString;	
	
	private String formatStyle;
	
	private boolean temperatureShowNAFlag=false;
	
	private String pricingCategoryWithAsterisk;
	private String pricingCategoryString;
	
	private Date date;
	private double temperature;
	private WeatherValue weatherValue;
	public RTPEventDataModel(){
		super();
	}
	public RTPEventDataModel(WeatherValue weatherValue){
		super();
		this.weatherValue=weatherValue;
		this.setDate(weatherValue.getDate());
		this.setTemperature(weatherValue.getTemperature());
		this.setPricingCategoryString(weatherValue.getPricingCategory());
	}
	
	public RTPEventDataModel(Date date, double temperature,String pricingCategory){
		super();
		this.setDate(date);
		this.setTemperature(temperature);
		this.setPricingCategoryString(pricingCategory);
	}
	public String getDateString() {
		if(date!=null){
			if(formatStyle==null||formatStyle.equalsIgnoreCase("")){
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");	
				dateString =dateFormat.format(date);
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat(formatStyle);
				dateString =dateFormat.format(date);
			}
		}else{
			dateString="";
		}
		return dateString;
	}
	
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	public String getTemperatureString() {
		if(temperatureShowNAFlag){
			temperatureString = "N/A";
		}else{
			temperatureString = String.valueOf(((int)getTemperature()));
		}
		return temperatureString;
	}
	
	public void setTemperatureString(String temperatureString) {
		this.temperatureString = temperatureString;
	}

	public void setFormatStyle(String formatStyle) {
		this.formatStyle = formatStyle;
	}

	public String getFormatStyle() {
		return formatStyle;
	}

	public boolean isTemperatureShowNAFlag() {
		return temperatureShowNAFlag;
	}

	public void setTemperatureShowNAFlag(boolean temperatureShowNAFlag) {
		this.temperatureShowNAFlag = temperatureShowNAFlag;
	}
	public String getWeekdayString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
		if(getDate()!=null){
			weekdayString =dateFormat.format(getDate());	
		}
		
		return weekdayString;
	}

	public void setWeekdayString(String weekdayString) {
		this.weekdayString = weekdayString;
	}

	public void setPricingCategoryWithAsterisk(
			String pricingCategoryWithAsterisk) {
		this.pricingCategoryWithAsterisk = pricingCategoryWithAsterisk;
	}

	public String getPricingCategoryWithAsterisk() {
		if(pricingCategoryString!=null&&(!pricingCategoryString.equalsIgnoreCase(""))){
			pricingCategoryWithAsterisk= pricingCategoryString+"*";
		}else{
			pricingCategoryWithAsterisk="N/A";
		}
		return pricingCategoryWithAsterisk;
	}

	public void setPricingCategoryString(String pricingCategoryString) {
		this.pricingCategoryString = pricingCategoryString;
	}

	public String getPricingCategoryString() {
		if(pricingCategoryString!=null&&(!pricingCategoryString.equalsIgnoreCase(""))){
			
		}else{
			pricingCategoryString="N/A";
		}
		return pricingCategoryString;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public static RTPEventDataModel generatePRWeatherWrapperMock(){
		RTPEventDataModel mock = new RTPEventDataModel();

		mock.setDate(new Date());
		mock.setPricingCategoryString("PriceCategory");
		mock.setTemperature(65);
		mock.setWeekdayString("Monday");
		mock.setDateString("Tuesday");
		return mock;
	}


	/**
	 * @param weatherValue the weatherValue to set
	 */
	public void setWeatherValue(WeatherValue weatherValue) {
		this.weatherValue = weatherValue;
	}


	/**
	 * @return the weatherValue
	 */
	public WeatherValue getWeatherValue() {
		return weatherValue;
	}
}