package com.akuacom.pss2.email;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.CoreConfig;

/**
 * This class depends on CoreConfig which relies on JBoss environment.
 *
 * The static singleton pattern was used to make sure the properties is
 * loaded only once per JBoss instance restarts or app reloads.
 *
 * @author Dichen Mao
 */
public class VaroliiProperties implements Serializable {
    private static final Logger log = Logger.getLogger(VaroliiProperties.class);

    private static final VaroliiProperties instance = new VaroliiProperties();

    private String hostName;
    private String userName;
    private String password;
    private String domain;
    private String oemId;
    private String oemPassword;

    private VaroliiProperties() {
        // here is where the config file is located.
        String fileName = CoreConfig.confdir + "varolii.conf";
        Properties config = new Properties();
        File configFile = new File(fileName);
        if (!configFile.isFile()) {
            String message = "can't find configuration file: " + fileName;
            log.fatal(message);
            throw new RuntimeException(message);
        } else {
            try {
                FileReader reader = new FileReader(configFile);
                config.load(reader);
                reader.close();
            } catch (IOException e) {
                log.fatal(e.getMessage(), e);
            }
        }
        hostName = config.getProperty("hostname");
        domain = config.getProperty("domain");
        userName = config.getProperty("username");
        password = config.getProperty("password");
        oemId = config.getProperty("oemId");
        oemPassword = config.getProperty("oemPassword");
        log.info("Varolii properties initialized: \n"
                + "hostname: " + hostName + "\n"
                + "domain: " + domain + "\n"
                + "username: " + userName + "\n"
                + "password: " + password + "\n"
                + "oemId: " + oemId + "\n"
                + "oemPassword: " + oemPassword);
    }

    public static VaroliiProperties getInstance() {
        return instance;
    }

    public String getDomain() {
        return domain;
    }

    public String getHostName() {
        return hostName;
    }

    public String getOemId() {
        return oemId;
    }

    public String getOemPassword() {
        return oemPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
