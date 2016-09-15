package com.akuacom.pss2.program.scertp;

import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.junit.Ignore;

import org.junit.Test;

//Who keeps ignoring my test?  Stop that.
// If it fails, it needs to get made better, not disappeared
@Ignore
public class ClimateReportTest extends TestCase {

    public static final String SCERTP_PRIMARY_WEATHER_STATION = "KCQT";
    public static final String SCERTP_PRIMARY_WEATHER_STATION_NAME = "DOWNTOWN LOS ANGELES (USC) CA";
    public static final String SCERTP_SECONDARY_WEATHER_STATION = "KLGB";
    public static final String SCERTP_SECONDARY_WEATHER_STATION_NAME = "LONG BEACH AIRPORT CA";
    
    // This is arguably a system test,not a unit test
    // It depends upon transactions with an external national weatherservice server
    // and it can take several seconds to execute if those servers are unreachable
    

    @Test
    public void testProcessingWeather() throws Exception {
        ClimateReportClient tempGetter = new ClimateReportClient();
                
        Calendar testCal = Calendar.getInstance();

        testCal.clear();
        testCal.setTimeZone(TimeZone.getTimeZone("PST"));
        testCal.set(2010, Calendar.AUGUST, 31);

        DailyWeatherSummary dws = tempGetter.getWeather(
                        "SCERTP", 
                        SCERTP_PRIMARY_WEATHER_STATION, 
                        SCERTP_PRIMARY_WEATHER_STATION_NAME,
                        SCERTP_SECONDARY_WEATHER_STATION,
                        SCERTP_SECONDARY_WEATHER_STATION_NAME);                
        
        System.out.println(dws);
         assertTrue(dws.getPrimaryStation().equals("KCQT"));
//         assertTrue(dws.getSecondaryStation().equals("KLGB"));
         assertTrue(dws.getPrimaryFinalYesterdayHigh() > 0);
//         assertTrue(dws.getSecondaryFinalYesterdayHigh()> 0);
    }
}
