/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.AllTests.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * The Class AllTests.
 */
public class AllTests 
{

    /**
     * Suite.
     * 
     * @return the test
     */
    public static Test suite()
    {
        TestSuite suite;
        suite = new TestSuite("AllTests");

        suite.addTestSuite(SortedArrayListTest.class);
        return suite;
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String args[])
    {
        String args2[] = {"-noloading", "com.akuacom.utils.AllTests"};

        TestRunner.main(args2);
    }

}
