/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.TempFileTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * The Class TempFileTest.
 */
public class TempFileTest extends TestCase {
    
    /**
     * Test temp file.
     */
    public void testTempFile() {
        try {
            File temp = File.createTempFile("test_", ".txt");
            System.out.println(temp.getPath());
            System.out.println(temp.getName());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
