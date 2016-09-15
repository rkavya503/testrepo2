package com.akuacom.pss2.program.scertp;

import com.akuacom.pss2.program.scertp.entities.Weather;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.Ignore;

import org.junit.Test;

//Who keeps ignoring my test?  Stop that.
// If it fails, it needs to get made better, not disappeared
@Ignore
public class NOAARestClientTest extends TestCase {

    public static final String SCERTP_PRIMARY_WEATHER_STATION = "KCQT";
    public static final String SCERTP_PRIMARY_WEATHER_STATION_NAME = "DOWNTOWN LOS ANGELES (USC) CA";
    public static final String SCERTP_SECONDARY_WEATHER_STATION = "KLGB";
    public static final String SCERTP_SECONDARY_WEATHER_STATION_NAME = "LONG BEACH AIRPORT CA";
    
    // This is arguably a system test,not a unit test
    // It depends upon transactions with an external national weatherservice server
    // and it can take several seconds to execute if those servers are unreachable
    // NOTE that this one method is normally not enabled as an automated test
    @Test
    public void testForecast() throws Exception {

        NOAARestClient client = new NOAARestClient();

        Weather forecast = client.getWeatherForecast("TestProgram");
        Date ct = forecast.getDate();
        assertTrue( forecast.getForecastHigh0() > 0);
        assertTrue( forecast.getForecastHigh1() > 0);
        assertTrue( forecast.getForecastHigh2() > 0);
        assertTrue( forecast.getForecastHigh3() > 0);
        assertTrue( forecast.getForecastHigh4() > 0);
        assertTrue( forecast.getForecastHigh5() > 0);
        System.out.println(forecast);
    }
    
}
