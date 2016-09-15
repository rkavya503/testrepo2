/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.ItronXmlParserTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import junit.framework.TestCase;

import java.util.Date;

/**
 * The Class ItronXmlParserTest.
 */
public class ItronXmlParserTest extends TestCase {
    
    /**
     * Test get time string.
     */
    public void testGetTimeString() {
        final ItronXmlParser parser = new ItronXmlParser();
        String s = parser.getTimeString(new Date().toString());
        System.out.println(s);

        String t = parser.getTimeString("1999/02/18 12:00\\12");
        System.out.println(t);
    }
}
