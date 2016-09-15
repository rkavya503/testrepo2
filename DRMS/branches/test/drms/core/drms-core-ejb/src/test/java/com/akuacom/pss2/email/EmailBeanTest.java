/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.EmailBeanTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

import javax.mail.MessagingException;

/**
 * The Class EmailBeanTest.
 */
public class EmailBeanTest extends TestCase {

    /**
     * Test send mail.
     */
    @Test
    public void testSendMail() {
        EmailBean bean = new EmailBean();
/*
        try {
            bean.sendMessage("test@akuacom.com", "test@akuacom.com", "akua test email", "This is a test for email sender.");
        } catch (MessagingException e) {
            Assert.fail(e.getMessage());
        }
*/
    }

}
