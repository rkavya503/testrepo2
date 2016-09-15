package com.akuacom.pss2.program.cpp;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * CPProgramTest is the unit test class for the CPProgram "Entity".
 * 
 * @author Brian Chapman
 * 
 *         Initial date 2010.11.10
 */
public class CPPProgramTest {

	@Test
	public void testToString() {
		CPPProgram cpp = new CPPProgram();
		Pattern pattern = Pattern.compile("CPPProgram:.");
		Matcher matcher = pattern.matcher(cpp.toString());
		boolean found = false;
		String foundString = "";

		while (matcher.find()) {
			found = true;
			foundString += matcher.group();
		}
		assertTrue(found);
	}
}
