/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;


import com.akuacom.utils.DateUtil;
import java.util.Calendar;

/**
 *
 * @author spierson
 */
public class DaylightSavingTimeHelper {

    public static boolean isDaylightSavingTime(Calendar cal) {
        Calendar testCal = (Calendar)cal.clone();
        // time changes at 2 am on the transition day
        // so do the test at 3 to see if this day is DST
        testCal.set(Calendar.HOUR,3);
        return testCal.getTimeZone().inDaylightTime(testCal.getTime());
    }

    public static boolean isDSTTransitionDay(Calendar cal) {
        Calendar dayBefore = (Calendar) cal.clone();
        dayBefore.add(Calendar.DATE, -1);
        return isDaylightSavingTime(cal) != isDaylightSavingTime(dayBefore);
    }

    public static boolean isTransitionToDST(Calendar cal) {
        if (!isDSTTransitionDay(cal)) return false;

        Calendar dayBefore = (Calendar) cal.clone();
        dayBefore.add(Calendar.DATE, -1);

        return !isDaylightSavingTime(dayBefore);
    }

    public static boolean isTransitionFromDST(Calendar cal) {
        if (!isDSTTransitionDay(cal)) return false;

        Calendar dayBefore = (Calendar) cal.clone();
        dayBefore.add(Calendar.DATE, -1);

        return isDaylightSavingTime(dayBefore);
    }


}
