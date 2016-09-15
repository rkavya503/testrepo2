package com.akuacom.utils.lang;
import static com.akuacom.utils.NumberFormats.decimal3;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import scala.Tuple2;

/**
 * @author reid
 *
 */
public class TimingUtil {
    static Logger log = Logger.getLogger(TimingUtil.class.getSimpleName());
    
    public final static int SECOND_MS = 1000;

    public final static int MINUTE = 60;

    public final static int MINUTE_MS = MINUTE * SECOND_MS;
   
    public final static int HOUR = 60 * MINUTE;

    public final static int HOUR_MS = HOUR * SECOND_MS;

    public final static int DAY = 24 * HOUR;

    public final static int DAY_MS = DAY * SECOND_MS;

    public final static float F_DAY_MS = DAY * SECOND_MS * 1.f;
    
    public static String fromSeconds(double seconds) {
        double days = 0;
        if (seconds > DAY) {
            days = (int) (seconds / DAY);
        }
        double remainder = seconds - days * DAY;
        double hours = 0;
        if (remainder > HOUR) {
            hours = (int) (remainder / HOUR);
        }
        remainder = remainder - hours * HOUR;
        double minutes = 0;
        if (remainder > MINUTE) {
            minutes = (int) (remainder / MINUTE);
        }
        remainder = remainder - minutes * MINUTE;
        String time = (days > 0 ? "" + ((int) days) + " days " : "");
        time += (hours > 0 ? "" + ((int) hours) + "h " : "");
        time += (minutes > 0 ? "" + ((int) minutes) + "m " : "");
        time += (remainder > 0 ? "" + (decimal3.format(remainder)) + "s"
                : "");
        return time;
    }
    
    public static String fromMillis(long millis) {
        return fromSeconds(millis/1000.d);
    }

    public static long elapsed(String message, long... elapsed) {
        long l = System.currentTimeMillis();
        if (elapsed == null || elapsed.length == 0) {
            log.debug(message);
        } else {
            log.debug(message + " took " + fromMillis(l - elapsed[0]));
        }
        return l;
    }
    
    
    /**
     * @param d Date supplying desired date
     * @return a tuple that is midnight/am of that day and then 1 s before the next midnight
     */
    public static Tuple2<Long,Long> dateRange(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Tuple2<Long,Long>(cal.getTimeInMillis(), cal.getTimeInMillis() + MILLISECONDS.convert(24l, HOURS) -1l); 
    }
    
    
    public static int hourOfDay(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    
    public static double hour(long l) {
        Calendar c  = Calendar.getInstance();
        c.setTime(new Date(l));
        return c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) / 60. + c.get(Calendar.SECOND) / 3600.;
    }

    
    public static Date clipMillis(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    

}
