/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MailSessionFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import com.akuacom.pss2.util.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailSessionFactory {

    private static Logger log = Logger.getLogger(MailSessionFactory.class);

    private static final String SMTP_HOST = "MX1.123Together.com";
    private static final String SMTP_USERNAME = "relay@akuacom.com";
    private static final String SMTP_PASSWORD = "KanaEki7";
    private static final String SMTP_AUTH_REQUIRED = "true";

    private static final String IMAP_HOST = "69.25.74.38";
    private static final int IMAP_PORT = 143;
    private static final String IMAP_AUTH_REQUIRED = "true";
    private static final String IMAP_USERNAME = "DRASVarPGE2@akuacom.com";  // default to test server
    private static final String IMAP_PASSWORD = "KanaEki7";

    private static Session smtp = null;
    private static Session imap = null;

    /**
     * This is returns a java.mail.Session session.
     *
     * @return smtp session
     * @throws java.io.IOException io exception
     */
    public static Session getSmtpSession() throws IOException {
        if (smtp == null) {
            Properties props = System.getProperties();
            final String username;
            final String password;
            final String smtp_host;
            boolean authRequired = true;

            Properties mail_props = getConfFile();
            if (mail_props != null) {
                username = mail_props.getProperty("mail.smtp.username");
                password = mail_props.getProperty("mail.smtp.password");
                smtp_host = mail_props.getProperty("mail.smtp.host");
                if ("false".equalsIgnoreCase(mail_props.getProperty("mail.smtp.auth.required"))) {
                    authRequired = false;
                }
            } else {
                username = SMTP_USERNAME;
                password = SMTP_PASSWORD;
                smtp_host = SMTP_HOST;
            }
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", smtp_host);
            log.debug("smtp_host: " + smtp_host);
            log.debug("smtp.username: " + username);
            log.debug("smtp.password: " + password);
            log.debug("authRequired: " + authRequired);

            if (authRequired) {
            	props.put("mail.smtp.auth", SMTP_AUTH_REQUIRED);
                smtp = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            } else {
            	props.put("mail.smtp.auth", "false");
                smtp = Session.getInstance(props);
            }
        }
        return smtp;
    }

    private static Properties getConfFile() throws IOException {
        String confPath = new Configuration().getConfPath();
        log.debug("confPath: " + confPath);
        log.debug("mail.propertyfile: " + confPath + File.separator + "mail.properties");
        File file = new File(confPath + File.separator + "mail.properties");
        Properties mail_props = null;
        if (file.exists()) {
            mail_props = new Properties();
            try {
                mail_props.load(new FileReader(file));
            } catch (IOException e) {
                log.error("Could not read mail property file: " + file.getPath());
                throw e;
            }
        }
        return mail_props;
    }

    public static Session getImapSession() {
        if (imap == null) {
            Properties props = System.getProperties();

            Properties mail_props = null;
            try {
                mail_props = getConfFile();
            } catch (IOException e) {
                // eat it
            }
            final String username;
            final String password;
            final String imap_host;

            if (mail_props != null) {
                imap_host = mail_props.getProperty("mail.imap.host");
                username = mail_props.getProperty("mail.imap.username");
                password = mail_props.getProperty("mail.imap.password");
            } else {
                imap_host = IMAP_HOST;
                username = IMAP_USERNAME;
                password = IMAP_PASSWORD;
            }
            props.put("mail.store.protocol", "imap");
            props.put("mail.imap.host", imap_host);
            props.put("mail.imap.port", IMAP_PORT);
            props.put("mail.imap.auth", IMAP_AUTH_REQUIRED);

            imap = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            username, password);
                    // This can be used for local testing
                    // return new
                    // PasswordAuthentication("DRASVarPGE2@akuacom.com",
                    // "Test_1234");
                }
            });
        }

        return imap;

    }
}