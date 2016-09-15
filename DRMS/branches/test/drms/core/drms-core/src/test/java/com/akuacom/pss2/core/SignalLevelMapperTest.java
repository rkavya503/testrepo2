/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.SignalLevelMapperTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.util.ModeSignal;
import com.akuacom.pss2.core.SignalLevelMapper;

import org.junit.Test;
import org.junit.Assert;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * The Class SignalLevelMapperTest.
 */
public class SignalLevelMapperTest extends TestCase {

    /**
     * Test resolve level.
     */
    @Test
    public void testResolveLevel() {
        final String s1 = SignalLevelMapper.resolveLevel("0", Arrays.asList("0", "x", "x"));
        Assert.assertEquals(ModeSignal.Levels.normal.toString(), s1);

        final String s2 = SignalLevelMapper.resolveLevel("10", Arrays.asList("x", "0", "x"));
        Assert.assertEquals(ModeSignal.Levels.moderate.toString(), s2);

        final String s3 = SignalLevelMapper.resolveLevel("100", Arrays.asList("x", "x", "0"));
        Assert.assertEquals(ModeSignal.Levels.high.toString(), s3);

        final String s4 = SignalLevelMapper.resolveLevel("100", Arrays.asList("0", "x", "x"));
        Assert.assertEquals(ModeSignal.Levels.normal.toString(), s4);

        final String s5 = SignalLevelMapper.resolveLevel("100", Arrays.asList("0", "100", "x"));
        Assert.assertEquals(ModeSignal.Levels.moderate.toString(), s5);
        final String s6 = SignalLevelMapper.resolveLevel("200", Arrays.asList("0", "100", "x"));
        Assert.assertEquals(ModeSignal.Levels.moderate.toString(), s6);

        final String s7 = SignalLevelMapper.resolveLevel("50", Arrays.asList("0", "x", "100"));
        Assert.assertEquals(ModeSignal.Levels.normal.toString(), s7);
        final String s8 = SignalLevelMapper.resolveLevel("100", Arrays.asList("0", "x", "100"));
        Assert.assertEquals(ModeSignal.Levels.high.toString(), s8);

        final String s9 = SignalLevelMapper.resolveLevel("50", Arrays.asList("x", "0", "100"));
        Assert.assertEquals(ModeSignal.Levels.moderate.toString(), s9);
        final String s10 = SignalLevelMapper.resolveLevel("200", Arrays.asList("x", "0", "100"));
        Assert.assertEquals(ModeSignal.Levels.high.toString(), s10);

        final String s11 = SignalLevelMapper.resolveLevel("0", Arrays.asList("0", "100", "200"));
        Assert.assertEquals(ModeSignal.Levels.normal.toString(), s11);
        final String s12 = SignalLevelMapper.resolveLevel("100", Arrays.asList("0", "100", "200"));
        Assert.assertEquals(ModeSignal.Levels.moderate.toString(), s12);
        final String s13 = SignalLevelMapper.resolveLevel("200", Arrays.asList("0", "100", "200"));
        Assert.assertEquals(ModeSignal.Levels.high.toString(), s13);
    }
}
