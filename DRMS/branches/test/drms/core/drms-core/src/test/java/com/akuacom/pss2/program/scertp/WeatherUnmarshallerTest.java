package com.akuacom.pss2.program.scertp;

import com.akuacom.pss2.program.scertp.entities.Weather;
import javax.xml.bind.Unmarshaller;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

public class WeatherUnmarshallerTest extends TestCase{
	
    @Test
    public void testGetForecastUnmarshaller() throws Exception {
        NOAARestClient client = new NOAARestClient();
        Weather weth = client.getWeatherForecast("fooWazoo");
        System.out.println(weth);
    }


}
