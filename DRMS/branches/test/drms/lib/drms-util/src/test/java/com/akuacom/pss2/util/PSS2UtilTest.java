/**
 * 
 */
package com.akuacom.pss2.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Aaron Roller
 * @see PSS2Util
 */
public class PSS2UtilTest {
	
	public void assertValidChar(char c, boolean expected) {
		assertEquals(String.valueOf(c) + " is not valid ", expected,
				PSS2Util.isLegalChar(c));
	}
	
	@Test
	public void testValidChars(){
		char[] validNameChars = PSS2Util.VALID_NAME_CHARS;
		for (int i = 0; i < validNameChars.length; i++) {
			assertValidChar(validNameChars[i], true);
		}
		assertValidChar('A', true);
		assertValidChar('a', true);
		assertValidChar('1', true);
	}
	@Test
	public void testInvalidChars(){
		assertValidChar('.', false);
		assertValidChar(';', false);
	}
	
	
	@Test
	public void testValidation(){
		assertTrue(PSS2Util.isLegalParticipantName("p"));
		assertTrue(PSS2Util.isLegalParticipantName("p-"));
		assertTrue(PSS2Util.isLegalParticipantName("abc-d"));
		assertTrue(PSS2Util.isLegalParticipantName("abc-d-e"));
		assertFalse(PSS2Util.isLegalParticipantName("abc--d"));
		assertTrue(PSS2Util.isLegalParticipantName("abc d"));
		assertTrue(PSS2Util.isLegalParticipantName("abc_d9"));
		
		assertFalse(PSS2Util.isLegalParticipantName("abc 'd adfsd"));
		assertFalse(PSS2Util.isLegalParticipantName("abc * dd asd _ =1 1-adfsd"));
		assertFalse(PSS2Util.isLegalParticipantName("p1.c1"));
		
		assertTrue(PSS2Util.isLegalClientName("p"));
		assertTrue(PSS2Util.isLegalClientName("p1.c1"));
		assertTrue(PSS2Util.isLegalClientName("p1."));
		assertTrue(PSS2Util.isLegalClientName("p1.c1 ss " ));
		assertTrue(PSS2Util.isLegalClientName("p1-c1.c1" ));
		
		assertFalse(PSS2Util.isLegalClientName("p1 '.c1 ss " ));
		assertFalse(PSS2Util.isLegalClientName("p1 *11.c1 ss " ));
		assertFalse(PSS2Util.isLegalClientName("p1--11.c1 ss " ));
		
		assertTrue(PSS2Util.isLegalProgramName("RTP <2K"));
		
		assertTrue(PSS2Util.isLegalProgramName("p"));
		assertTrue(PSS2Util.isLegalProgramName("CBP 1-4 DO"));
		assertFalse(PSS2Util.isLegalProgramName("CBP 1--4 DO"));
		assertTrue(PSS2Util.isLegalProgramName("RTP 2K-50K"));
		assertTrue(PSS2Util.isLegalProgramName("DRC-ECIDA"));
		assertFalse(PSS2Util.isLegalProgramName("RTP ' <2K"));
		assertFalse(PSS2Util.isLegalProgramName("RTP % <2K"));
	}
}
