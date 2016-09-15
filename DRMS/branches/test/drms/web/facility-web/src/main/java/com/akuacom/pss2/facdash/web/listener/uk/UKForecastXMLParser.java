package com.akuacom.pss2.facdash.web.listener.uk;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.WeatherCategory;
import com.akuacom.pss2.weather.WeatherUtils;

public class UKForecastXMLParser {
	/**
	 * RESPONSE XML EXAMPLE:
	 * <SiteRep>
	 *	<Wx>
	 *		<Param name="FDm" units="C">Feels Like Day Maximum Temperature</Param>
	 *		<Param name="Dm" units="C">Day Maximum Temperature</Param>
	 *		<Param name="FNm" units="C">Feels Like Night Minimum Temperature</Param>
	 *		<Param name="Nm" units="C">Night Minimum Temperature</Param>
	 *		<Param name="Gn" units="mph">Wind Gust Noon</Param>
	 *		<Param name="Gm" units="mph">Wind Gust Midnight</Param>
	 *		<Param name="Hn" units="%">Screen Relative Humidity Noon</Param>
	 *		<Param name="Hm" units="%">Screen Relative Humidity Midnight</Param>
	 *		<Param name="V" units="">Visibility</Param>
	 *		<Param name="D" units="compass">Wind Direction</Param>
	 *		<Param name="S" units="mph">Wind Speed</Param>
	 *		<Param name="U" units="">Max UV Index</Param>
	 *		<Param name="W" units="">Weather Type</Param>
	 *		<Param name="PPd" units="%">Precipitation Probability Day</Param>
	 *		<Param name="PPn" units="%">Precipitation Probability Night</Param>
	 *	</Wx>
	 *	<DV dataDate="2012-11-08T06:00:00Z" type="Forecast">
	 *	  <Location i="3781" lat="51.303" lon="-0.09" name="KENLEY" country="ENGLAND" continent="EUROPE">
	 *		<Period type="Day" value="2012-11-08Z">
	 *			<Rep D="W" Gn="20" Hn="81" PPd="9" S="11" V="VG" Dm="11" FDm="8" W="3" U="1">Day</Rep>
	 *			<Rep D="SSW" Gm="9" Hm="91" PPn="7" S="4" V="GO" Nm="5" FNm="3" W="2">Night</Rep>
	 *		</Period>
	 *		<Period type="Day" value="2012-11-09Z">
	 *			<Rep D="SSW" Gn="25" Hn="89" PPd="11" S="13" V="GO" Dm="11" FDm="7" W="7" U="1">Day</Rep>
	 *			<Rep D="SSW" Gm="22" Hm="95" PPn="89" S="9" V="MO" Nm="6" FNm="4" W="12">Night</Rep>
	 *		</Period>
	 *		<Period type="Day" value="2012-11-10Z">
	 *			<Rep D="WSW" Gn="22" Hn="83" PPd="43" S="11" V="GO" Dm="9" FDm="6" W="10" U="1">Day</Rep>
	 *			<Rep D="WSW" Gm="16" Hm="94" PPn="4" S="7" V="GO" Nm="2" FNm="-1" W="0">Night</Rep>
	 *		</Period>
	 *		<Period type="Day" value="2012-11-11Z">
	 *			<Rep D="W" Gn="22" Hn="76" PPd="10" S="11" V="VG" Dm="9" FDm="5" W="3" U="1">Day</Rep>
	 *			<Rep D="SW" Gm="16" Hm="91" PPn="9" S="7" V="GO" Nm="4" FNm="2" W="7">Night</Rep>
	 *		</Period>
	 *		<Period type="Day" value="2012-11-12Z">
	 *			<Rep D="SW" Gn="27" Hn="92" PPd="55" S="11" V="GO" Dm="11" FDm="8" W="12" U="1">Day</Rep>
	 *			<Rep D="SW" Gm="31" Hm="95" PPn="58" S="11" V="MO" Nm="8" FNm="7" W="12">Night</Rep>
	 *		</Period>
	 *	  </Location>
	 *	</DV>
	 *</SiteRep>
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static ForecastWeather parse(Document document,ForecastWeather entity){
		if(document!=null&&entity!=null){
			Element root = document.getRootElement();
			Iterator<Element> i = root.element("DV").element("Location").elementIterator("Period");
			int index = 0;
			while(i.hasNext()){
				Element e = i.next();
				//String dateString = e.attribute("value").getStringValue();
				if(index==0){
					//TODAY
					Iterator<Element> todayElementI = e.elementIterator("Rep");
					while(todayElementI.hasNext()){
						Element todayElement = todayElementI.next();
						if(todayElement.getText().equalsIgnoreCase("Day")){
							String maxTemperature = todayElement.attribute("Dm").getStringValue();
							entity.setMax_0(maxTemperature);
							String humidity = todayElement.attribute("Hn").getStringValue();
							entity.setHum(humidity);// humidity
							String weatherKey = todayElement.attribute("W").getStringValue();
							String weather = UKWeatherUtil.getWeatherMap().get(weatherKey);
							entity.setWeather_0(weather);
							WeatherCategory icon = WeatherUtils.getIconBytype(weather);
							entity.setWeather_icon_0(icon.getIcon());
						}
						if(todayElement.getText().equalsIgnoreCase("Night")){
							String minTemperature = todayElement.attribute("Nm").getStringValue();
							entity.setMin_0(minTemperature);
						}
					}
				}
				if(index==1){
					//TOMORROW
					Iterator<Element> tomorrowElementI = e.elementIterator("Rep");
					while(tomorrowElementI.hasNext()){
						Element todayElement = tomorrowElementI.next();
						if(todayElement.getText().equalsIgnoreCase("Day")){
							String maxTemperature = todayElement.attribute("Dm").getStringValue();
							entity.setMax_1(maxTemperature);
							//String humidity = todayElement.attribute("Hn").getStringValue();
							String weatherKey = todayElement.attribute("W").getStringValue();
							String weather = UKWeatherUtil.getWeatherMap().get(weatherKey);
							entity.setWeather_1(weather);
							WeatherCategory icon = WeatherUtils.getIconBytype(weather);
							entity.setWeather_icon_1(icon.getIcon());
						}
						if(todayElement.getText().equalsIgnoreCase("Night")){
							String minTemperature = todayElement.attribute("Nm").getStringValue();
							entity.setMin_1(minTemperature);
						}
					}
				}
				index++;
			}
		}
		entity.setUnit("&deg;C");
		return entity;
	}
}
