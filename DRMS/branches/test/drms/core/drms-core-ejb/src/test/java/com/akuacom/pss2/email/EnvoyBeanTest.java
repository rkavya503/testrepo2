/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.email.EnvoyBeanTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import org.apache.log4j.BasicConfigurator;

import junit.framework.TestCase;
import junit.framework.Assert;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;


/**
 * The Class EnvoyBeanTest.
 */
public class EnvoyBeanTest extends TestCase {

    /**
     * Test send envoy message.
     */
    public void testSendEnvoyMessage() throws Exception{
        BasicConfigurator.configure();
        try {

			URL testFile =
				Thread.currentThread().getContextClassLoader().getResource("test.xml");


            final BufferedReader br = new BufferedReader(
                    new FileReader(new File(testFile.toURI())));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }//end while
            final EnvoyBean bean = new EnvoyBean();
//            bean.sendEnvoyMessage(buffer.toString());
        } catch (FileNotFoundException e) {
            Assert.fail("send envoy failed: " + e.getMessage());
        } catch (IOException e) {
            Assert.fail("send envoy failed: " + e.getMessage());
        }
    }
}
