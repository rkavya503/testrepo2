/**
 * 
 */
package com.akuacom.test.ejb.jboss;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;
/**
 * The base test for all EJB's that intend to use the Embedded Container to test
 * an EJB. The BaseTestCase provided by JBoss will start the container only once
 * per vm execution making execution of multiple tests fairly fast. The initial
 * load time will be less than 10 seconds on healthly systems.
 * 
 * @see BaseTestCase
 * 
 * @author roller
 * 
 */
abstract public class JBossEJBTestBase extends TestCase {

	private static InitialContext context;
//	private static AssembledDirectory jar;

	static {
		verifyProperConfiguration();
	}

	/**
	 * Helper method to retrieve session bean implementations (local or remote).
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

			return (T) context.lookup(simpleName + extension);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Class[] mergeDeployables(Class[] first, Class[] second) {
		return (Class[]) ArrayUtils.addAll(first, second);
	}

	/**
	 * @see #lookupSession(Class, boolean)
	 * 
	 * @param <T>
	 * @param sessionBeanClass
	 * @return
	 */
	public static <T> T lookupSessionLocal(Class<T> sessionBeanClass) {

		return lookupSession(sessionBeanClass, true);
	}

	/**
	 * @see #lookupSession(Class, boolean)
	 * 
	 * @param <T>
	 * @param sessionBeanClass
	 * @return
	 */
	public static <T> T lookupSessionRemote(Class<T> sessionBeanClass) {

		return lookupSession(sessionBeanClass, false);
	}

//	public static void undeployBeans() throws DeploymentException {
//		Bootstrap.getInstance().undeploy(jar);
//	}

	/**
	 * For whatever reason the classloader needs the allowArraySyntax property
	 * set to true when running these tests in JDK1.6.
	 * 
	 */
	private static void verifyProperConfiguration() {
		String allowSyntaxProperty = System
				.getProperty("sun.lang.ClassLoader.allowArraySyntax");
		if (allowSyntaxProperty == null
				|| !allowSyntaxProperty.equalsIgnoreCase("true")) {
			throw new RuntimeException(
					"you must set the following run configuration argument for these tests to run successfully: -Dsun.lang.ClassLoader.allowArraySyntax=true");
		}
	}

	/**
	 * The classes that will be iterated over to deploy all that is given. Using
	 * a set to ensure no duplicates since deployment will fail.
	 * 
	 */
	private HashSet<Class<? extends Object>> classesToDeploy = new HashSet<Class<? extends Object>>();

	public JBossEJBTestBase(Class<? extends Object>[] classesToDeploy) {

		this.classesToDeploy.addAll(Arrays.asList(classesToDeploy));
	}

	public JBossEJBTestBase(Class<? extends Object> singleClassToDeploy) {
		this.classesToDeploy.add(singleClassToDeploy);
	}

	public JBossEJBTestBase(Collection<Class<? extends Object>> classesToDeploy) {
		this.classesToDeploy.addAll(classesToDeploy);
	}

	@Override
	protected void setUp() throws Exception {

		if (context == null) {
			try {
				context = new InitialContext();

			} catch (NamingException ne) {
				throw new RuntimeException(
						"Unable to find context.  Did you inherit "
								/*+ BaseTestCase.class.getName()*/, ne);
			}
		}

		
//Bootstrap.getInstance().scanClasspath("jboss-embedded-test-1.0.jar");
//		if (jar == null) {
//
//			// give the name of the implementing class to the jar.
//			jar = AssembledContextFactory.getInstance().create(
//					getClass().getSimpleName() + ".jar");
//
//			// now deploy each class given into a jar
//			for (Class currentClass : this.classesToDeploy) {
//				jar.addClass(currentClass);
//			}
//			//TODO:allow this to be controlled by the children
//			jar.mkdir("META-INF").addResource("persistence.xml","persistence.xml");
//			Bootstrap.getInstance().deploy(jar);
//			//deploy jar seems like the ideal, but Maven has problems with it
//			//http://community.jboss.org/thread/18326
//			
//		}
		

	}
	
	

	@Override
	protected void tearDown() throws Exception {
	//Bootstrap.getInstance().undeploy(jar);
		//Bootstrap.getInstance().undeployClasspath("target" );
	}

	/**
	 * this really doesn't test anything, but will provide the bare minimum test
	 * to see if the classes given will even deploy.
	 * 
	 */
	public void testSuccessfulDeployment() {
		assertTrue(true);
	}
}
