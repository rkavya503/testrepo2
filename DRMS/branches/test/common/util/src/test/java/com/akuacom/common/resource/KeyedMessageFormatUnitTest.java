
package com.akuacom.common.resource;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;


/**
 * 
 * @author roller
 *
 *@see KeyedMessageFormat
 */
public class KeyedMessageFormatUnitTest {
	private final String ZERO = "zero", ONE = "one", TWO = "two", THREE="three";
	private String multipleParamString = "one {zero} two {one} three {two}";
	

	private HashMap<String,String> hashMapBuilder(String[] keys,
			String[] values) {
		HashMap<String, String> keysAndParams = new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			keysAndParams.put(keys[i], values[i]);
		}
		return keysAndParams;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnmatchingBraces() {
		new KeyedMessageFormat("one { has no match");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyBraces() {
		new KeyedMessageFormat("two {}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParamCount() {
		HashMap<String, String> keysAndParams = (HashMap<String, String>) hashMapBuilder(
				new String[] { ZERO }, new String[] { ONE });
		KeyedMessageFormat formatMessage = new KeyedMessageFormat(
				multipleParamString);
		formatMessage.format(keysAndParams);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParamNotFound() {
		HashMap<String, String> keysAndParams =  hashMapBuilder(
				new String[] { ZERO }, new String[] { ONE });
		KeyedMessageFormat formatMessage = new KeyedMessageFormat("one {one}");
		formatMessage.format(keysAndParams);
		

	}
	


	@Test
	public void testSingleParam() {
		HashMap<String, String> keysAndParams = (HashMap<String, String>) hashMapBuilder(
				new String[] { ZERO }, new String[] { ONE });
		KeyedMessageFormat formatMessage = new KeyedMessageFormat("one {zero}");
		String cleanedMessage = formatMessage.format(keysAndParams);
		String string = "one one";
		assertEquals( string,cleanedMessage);

	}

	@Test
	public void testMultipleParams() {
		String[] keys = {ZERO, ONE, TWO};
		String[] values = {ONE, TWO, THREE};
		HashMap<String, String> keysAndParams = (HashMap<String, String>) hashMapBuilder(keys, values);
		KeyedMessageFormat formatMessage = new KeyedMessageFormat(
				multipleParamString);
		String cleanedMessage = formatMessage.format(keysAndParams);
		assertEquals( "one one two two three three",cleanedMessage);

	}

	@Test
	public void testEscapedBrace() {
		HashMap<String, String> keysAndParams = (HashMap<String, String>) hashMapBuilder(
				new String[] { TWO }, new String[] { ONE });
		KeyedMessageFormat formatMessage = new KeyedMessageFormat(
				"one '{ is a brace {two} is not");
		String cleanedMessage = formatMessage.format(keysAndParams);
		assertEquals( "one { is a brace one is not",cleanedMessage);

	}

	@Test
	public void testEscapedQuote() {
		HashMap<String, String> keysAndParams = (HashMap<String, String>) hashMapBuilder(
				new String[] { TWO }, new String[] { ONE });
		KeyedMessageFormat formatMessage = new KeyedMessageFormat("one ''{two}");
		String cleanedMessage = formatMessage.format(keysAndParams);
		assertEquals("one 'one",cleanedMessage);

	}

}
