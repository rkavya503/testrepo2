package com.akuacom.ejb.jboss.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
//import org.jboss.deployers.spi.DeploymentException;
import org.junit.BeforeClass;

import com.akuacom.ejb.client.EJB3Factory;

/**
 * Base class for tests that will be interacting with JBoss.
 * 
 */
abstract public class JBossFixture {

	/**Static initializer making sure context is setup properly for all to use.
	 * 
	 */
	static{
		setUpContext();
	}
	/** The context. */
	static private Context context;

	/**
	 * @throws java.lang.Exception
	 *             exception
	 */
	@BeforeClass
	public static void setUpContext() {
		if (context == null) {
			BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
			try {

				//context is setup differently depending if these are embedded tests or not.
				if (isEmbedded()) {
					throw new UnsupportedOperationException("sorry, jboss embedded isn't working at the moment");
//					try {
//						JBossEmbeddedFixture.getInstance().deploy(
//								new Class[] {});
//						context = new  InitialContext();
//					} catch (DeploymentException e) {
//						throw new RuntimeException(e);
//					}
					
				} else {
					Hashtable<String, String> env = new Hashtable<String, String>();
					// TODO:This security assignment is not working in JBoss
					// Embedded...figure out why
					env.put(Context.SECURITY_PRINCIPAL, "a");
					env.put(Context.SECURITY_CREDENTIALS, "b");
					env
							.put(Context.INITIAL_CONTEXT_FACTORY,
									"org.jboss.security.jndi.JndiLoginInitialContextFactory");
					env.put("java.naming.provider.url", "localhost:1099");
					context = new InitialContext(env);
				}

			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
			// assertNotNull("Conext not created", context);
		}
	}

	/**
	 * @param <T>
	 *            session class type
	 * @param sessionBeanClass
	 *            session class
	 * @return result
	 */
	public static <T> T lookupSessionLocal(Class<T> sessionBeanClass) {
		return EJB3Factory.getBean(sessionBeanClass, "pss2", context);
	}

	/**
	 * @param <T>
	 *            session class type
	 * @param sessionBeanClass
	 *            session class
	 * @return result
	 */
	public static <T> T lookupSessionRemote(Class<T> sessionBeanClass) {
		return EJB3Factory.getBean(sessionBeanClass, "pss2", context);
	}

	/**
	 * Helper method to retrieve session bean implementations (local or remote).
	 * 
	 * This method is needed to support the JBoss Embedded JNDI naming
	 * standards.
	 * 
	 * @param <T>
	 * @param sessionBeanClass
	 * @param local
	 *            give true if you want the local interface, false if you want
	 *            the remote
	 * @return
	 */
	public static <T> T lookupSession(Class<T> sessionBeanClass, boolean local) {
		try {

			String simpleName = sessionBeanClass.getSimpleName();

			String extension;
			if (local) {
				extension = "/local";
			} else {
				extension = "/remote";
			}

			if (context == null) {
				context = new InitialContext();
			}
			return (T) context.lookup(simpleName + extension);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param bindName
	 *            bind name
	 * @param <T>
	 *            type
	 * @return result
	 */
	@SuppressWarnings( { "RedundantTypeArguments" })
	public static <T> T lookupSessionBeanByName(String bindName) {

		return EJB3Factory.<T> getBean(bindName, "pss2", false, context);
	}

	public JBossFixture() {

	}

	public JBossFixture(Class... embeddedClassesToDeploy) {

		if (isEmbedded()) {
			throw new RuntimeException("Yeah, we don't support embedded at this time");
//			if (embeddedClassesToDeploy.length > 0) {
//
//				try {
//					JBossEmbeddedFixture.getInstance().deploy(
//							embeddedClassesToDeploy);
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//			} else {
//				throw new RuntimeException(
//						"If you want to do an embedded test, please provide the Bean Classes to deploy");
//			}
		}

	}

	public static boolean isEmbedded() {
		return Boolean.getBoolean("embedded.test");
	}
}
