package com.akuacom.pss2.facdash.web.listener.uk;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.akuacom.pss2.weather.ForecastWeather;
import com.akuacom.pss2.weather.WeatherCategory;
import com.akuacom.pss2.weather.WeatherUtils;

public class UKObservationXMLParser {
		//RESPONSE XML EXAMPLE:
		//<SiteRep>
		//	<Wx>
		//		<Param name="G" units="mph">Wind Gust</Param>
		//		<Param name="T" units="C">Temperature</Param>
		//		<Param name="V" units="m">Visibility</Param>
		//		<Param name="D" units="compass">Wind Direction</Param>
		//		<Param name="S" units="mph">Wind Speed</Param>
		//		<Param name="W" units="">Weather Type</Param>
		//		<Param name="P" units="hpa">Pressure</Param>
		//	</Wx>
		//	<DV dataDate="2012-11-08T06:00:00Z" type="Obs">
		//		<Location i="3781" lat="51.303" lon="-0.09" name="KENLEY" country="ENGLAND" continent="EUROPE">
		//			<Period type="Day" value="2012-11-07Z">
		//				<Rep D="WSW" P="1025" S="8" T="4.8" V="12000" W="0">420</Rep>
		//				<Rep D="WSW" P="1025" S="7" T="4.9" V="11000" W="1">480</Rep>
		//				<Rep D="W" P="1026" S="8" T="6.5" V="15000" W="1">540</Rep>
		//				<Rep D="SW" P="1026" S="6" T="7.6" V="16000" W="1">600</Rep>
		//				<Rep D="WSW" P="1026" S="11" T="8.3" V="21000" W="3">660</Rep>
		//				<Rep D="W" P="1026" S="15" T="7.7" V="19000" W="7">720</Rep>
		//				<Rep D="WSW" P="1025" S="14" T="8.1" V="24000" W="8">780</Rep>
		//				<Rep D="WSW" P="1024" S="10" T="8.6" V="30000" W="8">840</Rep>
		//				<Rep D="WSW" P="1024" S="14" T="8.2" V="28000" W="8">900</Rep>
		//				<Rep D="WSW" P="1024" S="11" T="8.2" V="23000" W="8">960</Rep>
		//				<Rep D="WSW" P="1024" S="13" T="8.3" V="30000" W="8">1020</Rep>
		//				<Rep D="WSW" P="1024" S="15" T="7.7" V="40000" W="7">1080</Rep>
		//				<Rep D="SW" P="1024" S="13" T="7.3" V="40000" W="0">1140</Rep>
		//				<Rep D="SW" P="1024" S="13" T="6.8" V="30000" W="2">1200</Rep>
		//				<Rep D="SW" P="1024" S="10" T="7.1" V="25000" W="7">1260</Rep>
		//				<Rep D="SW" P="1023" S="9" T="7.4" V="35000" W="8">1320</Rep>
		//				<Rep D="SSW" P="1023" S="8" T="7.4" V="35000" W="8">1380</Rep>
		//			</Period>
		//			<Period type="Day" value="2012-11-08Z">
		//				<Rep D="SSW" P="1022" S="9" T="6.9" V="50000" W="2">0</Rep>
		//				<Rep D="SW" P="1022" S="11" T="7.4" V="35000" W="7">60</Rep>
		//				<Rep D="SSW" P="1021" S="8" T="7.0" V="40000" W="7">120</Rep>
		//				<Rep D="SSW" P="1021" S="11" T="6.6" V="45000" W="2">180</Rep>
		//				<Rep D="SW" P="1021" S="11" T="6.2" V="50000" W="0">240</Rep>
		//				<Rep D="SW" P="1020" S="10" T="7.2" V="50000" W="7">300</Rep>
		//				<Rep D="SW" P="1020" S="9" T="7.4" V="50000" W="8">360</Rep>
		//			</Period>
		//		</Location>
		//	</DV>
		//</SiteRep>

		@SuppressWarnings("unchecked")
		public static String getRealTimeTemperature(Document document){
			String result ="";
			if(document!=null){
				Element root = document.getRootElement();
				Iterator<Element> i = root.element("DV").element("Location").elementIterator("Period");
				Element e = null;
				while(i.hasNext()){
					e = i.next();
				}	
				if(e!=null){
					Iterator<Element> todayElementI = e.elementIterator("Rep");
					Element todayElement = null;
					while(todayElementI.hasNext()){
						todayElement = todayElementI.next();
					}
					if(todayElementI!=null){
						result = todayElement.attribute("T").getStringValue();
						return result;
					}
				}
			}
			return result;
		}
}
