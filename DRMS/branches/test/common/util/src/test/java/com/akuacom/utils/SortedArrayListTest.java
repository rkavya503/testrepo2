/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.SortedArrayListTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import junit.framework.TestCase;

/**
 * The Class SortedArrayListTest.
 */
public class SortedArrayListTest extends TestCase
{
	
	/**
	 * Instantiates a new sorted array list test.
	 */
	public SortedArrayListTest()
	{
	}
	
	/**
	 * Test.
	 * 
	 * @throws Exception the exception
	 */
	public void test() throws Exception
	{
		String o0 = "apple";
		String o1 = "beta";
		String o2 = "charlie";
		String o3 = "delta";
		
		SortedArrayList<String> sal = new SortedArrayList<String>();
		sal.add(o1);
		sal.add(o2);
		sal.add(o0);
		sal.add(o3);
		assertEquals(sal.get(0), o0);
		assertEquals(sal.get(1), o1);
		assertEquals(sal.get(2), o2);
		assertEquals(sal.get(3), o3);
	}
		
}
