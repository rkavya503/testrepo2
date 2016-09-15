/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;

import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;



/**
 *
 * @author spierson
 */
public class DaylightSavingTimeTest  {

    private static final String TEST_TIMEZONE="America/Los_Angeles";

    @Test
    public void testDST() {
        Calendar summerCal = Calendar.getInstance();
        summerCal.setTimeZone(TimeZone.getTimeZone(TEST_TIMEZONE));
        summerCal.set(2010, 6, 15);
        Calendar winterCal = Calendar.getInstance();
        winterCal.setTimeZone(TimeZone.getTimeZone(TEST_TIMEZONE));
        winterCal.set(2010, 12, 15);

        assert(DaylightSavingTimeHelper.isDaylightSavingTime(summerCal) );
        assert(!DaylightSavingTimeHelper.isDaylightSavingTime(winterCal));
    }

    @Test
    public void testDSTTransition() {
        Calendar fallCal = Calendar.getInstance();
        fallCal.setTimeZone(TimeZone.getTimeZone(TEST_TIMEZONE));
        fallCal.set(2010, 11-1, 7);
        assert(DaylightSavingTimeHelper.isDSTTransitionDay(fallCal));
        assert(DaylightSavingTimeHelper.isTransitionFromDST(fallCal));
        assert(!DaylightSavingTimeHelper.isTransitionToDST(fallCal));
        fallCal.add(Calendar.DATE, 1);
        assert(!DaylightSavingTimeHelper.isDSTTransitionDay(fallCal));
        assert(!DaylightSavingTimeHelper.isTransitionFromDST(fallCal));

        Calendar springCal = Calendar.getInstance();
        springCal.setTimeZone(TimeZone.getTimeZone(TEST_TIMEZONE));
        springCal.set(2010, 3-1, 14);
        assert(DaylightSavingTimeHelper.isDSTTransitionDay(springCal));
        assert(DaylightSavingTimeHelper.isTransitionToDST(springCal));
        assert(!DaylightSavingTimeHelper.isTransitionFromDST(springCal));
        springCal.add(Calendar.DATE, 1);
        assert(!DaylightSavingTimeHelper.isDSTTransitionDay(springCal));
        assert(!DaylightSavingTimeHelper.isTransitionToDST(springCal));
        springCal.add(Calendar.DATE, -2);
        assert(!DaylightSavingTimeHelper.isDSTTransitionDay(springCal));

    }
}
