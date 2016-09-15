/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.MemorySequence.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.util.Random;

/*
 * This class returns a semi-unique int sequence id for use as a PK
 * for a field that represents an unsignedInt in an XSD.
 * 
 * XML schema numeric type unsignedInt is defined as an unsigned 32-bit integer.
 * 
 * In Java, Long.MAX_VALUE = 2^63 - 1 while the maximum value an 
 * int can have, 2^31-1.  
 *  
 */

/**
 * The Class MemorySequence.
 */
public class MemorySequence {
	
	/** The seq id. */
	private static int seqId;
	
	static {
		Random r = new Random();  // Default seed comes from system time.
		seqId = r.nextInt(2147483647);  // 0 to 2^31 - 1
	}
	
	/**
	 * Gets the next sequence id.
	 * 
	 * @return the next sequence id
	 */
	public static synchronized int getNextSequenceId() {
		if (seqId >= Integer.MAX_VALUE || seqId < 0) {
			seqId = 0;
		}

		return seqId++;
	}
}
