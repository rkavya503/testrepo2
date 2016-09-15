package com.akuacom.pss2.program.scertp.entities;

import static com.akuacom.test.TestUtil.generateRandomDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;

import com.akuacom.ejb.AbstractBaseEAOTest;

public class WeatherEAOTest extends
        AbstractBaseEAOTest<WeatherEAO, Weather> {

	public WeatherEAOTest() {
		super(WeatherEAO.class, WeatherEAOBean.class, Weather.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(Weather created, Weather found) {
		assertTrue(!found.getDate().equals(created.getDate()));
	}

	@Override
	protected void mutate(Weather found) {
		found.setDate(generateRandomDate());

	}

	@Override
	protected void assertEntityValuesEquals(Weather created, Weather found) {
		assertEquals(created.getDate(), found.getDate());
		Double createdHigh = created.getHigh();
		Double foundHigh = found.getHigh();
		// compare high, allowing for null
		if (createdHigh == null) {
			Assert.assertNull(foundHigh);
		} else {
			assertEquals("high", createdHigh, foundHigh, 0.1);
		}
		Assert.assertEquals("reporting station", created.getReportingStation(),
				found.getReportingStation());
		Assert.assertEquals("is final", created.isIsFinal(), found.isIsFinal());
		Assert.assertEquals("forecast high 0", created.getForecastHigh0(),
				found.getForecastHigh0(), 0.0);
		Assert.assertEquals("forecast high 1", created.getForecastHigh1(),
				found.getForecastHigh1(), 0.0);
		Assert.assertEquals("forecast high 2", created.getForecastHigh2(),
				found.getForecastHigh2(), 0.0);
		Assert.assertEquals("forecast high 3", created.getForecastHigh3(),
				found.getForecastHigh3(), 0.0);
		Assert.assertEquals("forecast high 4", created.getForecastHigh4(),
				found.getForecastHigh4(), 0.0);
		Assert.assertEquals("forecast high 5", created.getForecastHigh5(),
				found.getForecastHigh5(), 0.0);

	}

	@Override
	protected Weather generateRandomEntity() {
		Weather weather = new WeatherTest().generateRandomIncompleteEntity();

		assertNotNull(weather);
		return weather;
	}
}
