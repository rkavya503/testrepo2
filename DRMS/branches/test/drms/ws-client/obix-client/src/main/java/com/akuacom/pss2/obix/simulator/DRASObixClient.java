/**
 * 
 */
package com.akuacom.pss2.obix.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import obix.Uri;
import obix.net.ObixSession;

/**
 * @author e333812
 * 
 */
public class DRASObixClient {

	// default configuration file name
	/** The Constant DEFAULT_CONFIG_FILE_NAME. */
	private static final String DEFAULT_CONFIG_FILE_NAME = "DRASObixClient.conf";

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private static final String BASE_URI = "baseURI";
	private static final String DEFAULT_BASE_URI = "https://localhost:8443/obixserver/obix/dataService/";

	private static final String UPDATE_INTERVAL = "updateInterval";
	private static final String DEFAULT_UPDATE_INTERVAL = "60000";

	private static final String RECORD_COUNT = "recordCount";
	private static final String DEFAULT_RECORD_COUNT = "96";

	private static final String SSL_PUBLIC_KEY = "publicKey";
	private static final String DEFAULT_SSL_PUBLIC_KEY = "false";

	/** The Constant PROP_TRUST_STORE. */
	private static final String PROP_TRUST_STORE = "trustStore";

	/** The Constant DEFAULT_TRUST_STORE. */
	private static final String DEFAULT_TRUST_STORE = "./cacerts.jks";

	/** The Constant PROP_TRUST_STORE_PASSWORD. */
	private static final String PROP_TRUST_STORE_PASSWORD = "trustStorePassword";

	/** The Constant DEFAULT_TRUST_STORE_PASSWORD. */
	private static final String DEFAULT_TRUST_STORE_PASSWORD = "epriceLBL";

	private static final String DATA_LOG = "dataLog";
	private static final String DEFAULT_DATA_LOG = "data.log";

	/** The configuration */
	private Properties config = null;
	
//    /** The logger. */
//    public Logger logger = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DRASObixClient client = new DRASObixClient();
		client.initialize(args);

		client.run();

	}

	public void run() {
		ObixClientSimulator simulator = null;
		ObixSession session = null;

		try {
			boolean isUsedPublicKey = Boolean.parseBoolean(config.getProperty(SSL_PUBLIC_KEY));

			if (!isUsedPublicKey) {
				System.setProperty("javax.net.ssl.trustStore", "./cacerts.jks");
				System.setProperty("javax.net.ssl.trustStorePassword", "epriceLBL");
			}

			String baseUri = config.getProperty(BASE_URI);
 
			Uri lobbyUri = new Uri(baseUri);
			String username = config.getProperty(USERNAME);
			String password = config.getProperty(PASSWORD);

			session = new ObixSession(lobbyUri, username, password);

			simulator = new ObixClientSimulator("meter1", baseUri, session, 
					config.getProperty(DATA_LOG));

			long updateInterval = Long.valueOf(config.getProperty(UPDATE_INTERVAL));

			simulator.setUpdateInterval(updateInterval);

			simulator.start();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			session = null;
		}

	}

	private Properties configureProperties(String[] args) {
		// default the configuration file name
		String configFileName = DEFAULT_CONFIG_FILE_NAME;

		if (args.length >= 1) {
			configFileName = args[0];
		}
		if (args.length > 1) {
			System.out.println("ignoring extra command line arguments");
		}

		// setup the default configuration
		Properties defaultConfig = new Properties();
		defaultConfig.put(UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL);
		defaultConfig.put(RECORD_COUNT, DEFAULT_RECORD_COUNT);
		defaultConfig.put(SSL_PUBLIC_KEY, DEFAULT_SSL_PUBLIC_KEY);
		defaultConfig.put(PROP_TRUST_STORE, DEFAULT_TRUST_STORE);
		defaultConfig.put(PROP_TRUST_STORE_PASSWORD,
				DEFAULT_TRUST_STORE_PASSWORD);
		defaultConfig.put(DATA_LOG, DEFAULT_DATA_LOG);

		
		Properties config = new Properties(defaultConfig);

		// read the config file
		File configFile = new File(configFileName);
		System.out.println("configuration file = "
				+ configFile.getAbsolutePath());
		if (!configFile.isFile()) {
			System.out.println("configuration file not found - using defaults");
		} else {
			try {
				FileInputStream in = new FileInputStream(configFile);
				config.load(in);
			} catch (IOException e) {
				System.out.println("error reading configuration file - using defaults");
			}
		}

		for (String name : config.stringPropertyNames()) {
			System.out.println(name + ": " + config.getProperty(name));
		}

		return config;
	}

	/**
	 * Initialize.
	 * 
	 * @param args
	 *            the args
	 */
	public void initialize(String[] args) {
		System.out.println("DRAS Obix Client Simulater");

		config = configureProperties(args);
		System.out.println();
	}

}
