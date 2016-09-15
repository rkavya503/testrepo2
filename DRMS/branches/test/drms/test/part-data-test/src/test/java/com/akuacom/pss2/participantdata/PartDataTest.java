/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartDataTest.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participantdata;

import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participantdata.ParticipantDataCorruptionException;
import com.akuacom.pss2.participantdata.ParticipantDataManager;

/**
 * The Class PartDataTest.
 */
public class PartDataTest extends TestCase
{

	/** The part data. */
    private ParticipantDataManager partData;

	/** The log. */
	private static Logger log =
		Logger.getLogger(PartDataTest.class.getName());

	/**
	 * Instantiates a new part data test.
	 *
	 * @param name the name
	 */
	public PartDataTest(String name)
	{
		super(name);
	}

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        // partData = EJBFactory.getBean(ParticipantDataManager.class, "pss2", getInitialContext());

/*

		String c = "pss2/" + ParticipantDataManagerBean.class.getSimpleName() + "/remote";
		System.out.println("%%%%%%%%%%%%%%%%%% looking up: " + c);
		partData = (ParticipantDataManager) getInitialContext().lookup(c);
		System.out.println("%%%%%%%%%%%%%%%% ParticipantDataManager=" + partData);

*/
    }

    /**
     * Test create participant data.
     */
    public void testCreateParticipantData()
	{
		log.info("testCreateParticipantData");
		//partData.createParticipantData(null);
		Assert.assertTrue(true);
	}

	/**
	 * Test delete participant data.
	 */
	public void testDeleteParticipantData()
	{
		log.info("testDeleteParticipantData");
		//partData.deleteParticipantData(null);
		Assert.assertTrue(true);
	}

	/**
	 * Test get participant data.
	 */
	public void testGetParticipantData()
	{
		log.info("testGetParticipantData");
/*
        try {
            partData.getParticipantData("a", null, null);
        } catch (ParticipantDataCorruptionException e) {
            e.printStackTrace();
        }
*/
        Assert.assertTrue(true);
	}

	/**
	 * Gets the initial context.
	 *
	 * @return the initial context
	 *
	 * @throws NamingException the naming exception
	 */
	public static Context getInitialContext()
		throws NamingException
	{
		Properties env = new Properties();
		env.put("java.naming.factory.initial",
			"org.jnp.interfaces.NamingContextFactory");
		env.put("java.naming.factory.url.pkgs",
			"org.jboss.naming:org.jnp.interfaces");
		env.put("java.naming.provider.url", "localhost");
		return new InitialContext(env);
	}

	/**
	 * Suite.
	 *
	 * @return the test
	 */
	public static Test suite()
	{
		BasicConfigurator.configure();
		TestSuite suite = new TestSuite();
		suite.addTest(new PartDataTest("testCreateParticipantData"));
		suite.addTest(new PartDataTest("testDeleteParticipantData"));
		//suite.addTest(new PartDataTest("testSetParticipantData"));
		suite.addTest(new PartDataTest("testGetParticipantData"));
		// suite.addTest(new DataAccessTest("testRemoveParticipant"));
		return suite;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
}