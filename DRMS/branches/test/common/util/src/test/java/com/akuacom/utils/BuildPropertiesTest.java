/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.BuildPropertiesTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.akuacom.utils.BuildProperties;

/**
 * Tests the basic functionality of {@link BuildProperties}.
 * 
 * @author Brian Chapman
 * 
 */
public class BuildPropertiesTest {

	@Test
	public void testBuildProperties() {
		BuildProperties properties = new BuildProperties();
		
		Date date = new Date(1295916718972L);
		assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), properties.getBuildTimeStamp());
		assertEquals("version_value", properties.getVersion());
		assertEquals("revision_value", properties.getRevision());
		assertEquals("branch_value", properties.getScmBranch());
	}
}

