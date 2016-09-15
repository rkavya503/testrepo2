/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.DBPInputServletTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import junit.framework.TestCase;
import junit.framework.Assert;

/**
 * The Class DBPInputServletTest.
 */
public class DBPInputServletTest extends TestCase {
    
    /**
     * Test extract.
     */
    public void testExtract() {
        DBPInputServlet servlet = new DBPInputServlet();
        String s = servlet.extractRandomNumberFromFilename("C:\\DOCUME~1\\dichen\\LOCALS~1\\Temp\\test_31539.txt");
        Assert.assertEquals("31539", s);
    }
}
