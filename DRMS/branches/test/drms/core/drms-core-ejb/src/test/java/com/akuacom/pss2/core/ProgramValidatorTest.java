package com.akuacom.pss2.core;

import java.util.Calendar;

import org.junit.Test;

import com.akuacom.utils.lang.DateUtil;

public class ProgramValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParser() {
        final Calendar cal = Calendar.getInstance();
        DateUtil.dateFormatHHmm().format(cal);
    }
}
