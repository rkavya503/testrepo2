package com.akuacom.pss2.program.scertp.entities;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Tests the {@link Weather} Entity
 * 
 * @author Brian Chapman
 * 
 *         Created 2010.11.03
 * 
 */
public class WeatherTest extends BaseEntityFixture<Weather> {

	public Weather generateRandomIncompleteEntity() {
		Weather weather = new Weather();

		Date date = generateRandomDate();
		weather.setDate(date);
		assertEquals(date, weather.getDate());

		String reportingStation = generateRandomStringOfLength(8);
		weather.setReportingStation(reportingStation);
		assertEquals(reportingStation, weather.getReportingStation());

		//This must come before high
		double forecastHigh0 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh0(forecastHigh0);
		assertEquals(forecastHigh0, weather.getForecastHigh0(), 0.01);

		double high =  Weather.roundValue(generateRandomDouble());
		weather.setHigh(high);
		assertEquals(high, weather.getHigh(), 0.01);
		assertEquals(high, weather.getForecastHigh0(), 0.01);
		
		boolean isFinal = generateRandomBoolean();
		weather.setIsFinal(isFinal);
		assertEquals(isFinal, weather.isIsFinal());
				
		double forecastHigh1 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh1(forecastHigh1);
		assertEquals(forecastHigh1, weather.getForecastHigh1(), 0.01);

		double forecastHigh2 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh2(forecastHigh2);
		assertEquals(forecastHigh2, weather.getForecastHigh2(), 0.01);

		double forecastHigh3 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh3(forecastHigh3);
		assertEquals(forecastHigh3, weather.getForecastHigh3(), 0.01);

		double forecastHigh4 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh4(forecastHigh4);
		assertEquals(forecastHigh4, weather.getForecastHigh4(), 0.01);

		double forecastHigh5 = Weather.roundValue(generateRandomDouble());
		weather.setForecastHigh5(forecastHigh5);
		assertEquals(forecastHigh5, weather.getForecastHigh5(), 0.01);

		return weather;
	}
}
