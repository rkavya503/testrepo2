package com.akuacom.pss2.program.scertp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

public class HistoricalWeather {
    private static final Logger log = Logger.getLogger(HistoricalWeather.class);
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("America/Los_Angeles");

    public static void main(String[] args) {
        final List<HistoricalWeatherEntry> list = getHistoricalWeatherEntries();
        final HistoricalWeatherEntry high = getCurrentHigh(list);
        System.out.println(Integer.parseInt(high.getTime().substring(0, 2)));
    }

    public static List<HistoricalWeatherEntry> getHistoricalWeatherEntries() {
        List<HistoricalWeatherEntry> list = new ArrayList<HistoricalWeatherEntry>();

        try {
            final ParserGetter kit = new ParserGetter();
            final HTMLEditorKit.Parser parser = kit.getParser();
            final HistoricalWeatherParserHandler callback = new HistoricalWeatherParserHandler();
            final URL url = new URL("http://www.weather.gov/data/obhistory/KCQT.html");
            final InputStream in = url.openStream();
            final InputStreamReader reader = new InputStreamReader(in);
            parser.parse(reader, callback, false);
            list = callback.getList();
        } catch (IOException e) {
            log.error("Failed to retrieve historical weather info", e);
        }
        return list;
    }

    public static HistoricalWeatherEntry getCurrentHigh(List<HistoricalWeatherEntry> list ) {
        HistoricalWeatherEntry result = null;
        final String dateString = list.get(0).getDate();
        final int date = Integer.parseInt(dateString);
        final Calendar cal = Calendar.getInstance(TIME_ZONE);
        // if there is today's data, look up.
        if (date == cal.get(Calendar.DAY_OF_MONTH)) {
            int high = 0;
            int sixHourMax = 0;
            for (HistoricalWeatherEntry entry : list) {
                // look up only today's data
                if (!entry.getDate().equals(dateString)) {
                    break;
                } else{
                    if (entry.getHigh() > high) {
                        high = entry.getHigh();
                        result = entry;
                    }
                    if (entry.getSixHourHigh() > sixHourMax) {
                        sixHourMax = entry.getSixHourHigh();
                    }
                }
            }
            // note that the six hour high can be of yesterday's value
            // if it's early in the morning. but it shouldn't matter.
            if (result != null) {
                final HistoricalWeatherEntry temp = new HistoricalWeatherEntry();
                temp.setDate(result.getDate());
                temp.setTime(result.getTime());
                temp.setHigh(result.getHigh());
                temp.setSixHourHigh(sixHourMax);
                result = temp;
            }
        }
        return result;
    }

    static class ParserGetter extends HTMLEditorKit {
        public HTMLEditorKit.Parser getParser() {
            return super.getParser();
        }
    }
}
