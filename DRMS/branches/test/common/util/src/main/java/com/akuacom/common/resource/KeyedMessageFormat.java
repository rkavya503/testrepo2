package com.akuacom.common.resource;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Formats a piece of parameterized text and inserts appropriate value
 * associated with parameter found on page. Replaces any escaped characters.
 * 
 * Useful for standard replacement of messages, but replacing by named key
 * instead of indexed parameter.
 * 
 * Instead of having mystery numbers:
 * 
 * "{0} already taken.  Try {1}?"
 * 
 * You'd have:
 * 
 * "{username} already taken.  Try {suggestedUsername}?"
 * 
 * Notice the keyed message is much more readable.
 * 
 */
public class KeyedMessageFormat {

	private final String message;

	private static final String PARAM_FINDER_REGEX = "('{2}|(?<!'))\\{(.+?)\\}";

	/**
	 * Makes sure that the message doesn't break any rules and sets the message
	 * field.
	 * 
	 * @param message
	 *            The String from the resource bundle with parameterized text to
	 *            be formatted
	 */
	public KeyedMessageFormat(String message) {
		applyStandards(message);
		this.message = message;
	}

	/**
	 * Regex captures anything inside of two curly braces that follows either
	 * zero single quotes or two single quotes Checks for the following
	 * problems: 1) braces are not balanced 2) contents between braces doesn't
	 * exist (braces are empty)
	 * 
	 * 
	 * @param message
	 *            The string retrieved from the resource bundle
	 * @throws IllegalArgumentException
	 *             if any of the above problems are found
	 */
	private static void applyStandards(String message) {
		Pattern parameterRegex = Pattern.compile(PARAM_FINDER_REGEX);
		Matcher parameterMatcher = parameterRegex.matcher(message);
		if (!parameterMatcher.find()) {
			throw new IllegalArgumentException(
					"No parameters parsed from message " + message);
		}
	}

	/**
	 * Replaces all escaped braces and escaped quotes with char that was being
	 * escaped. Called after the parameters are replaced.
	 */
	private static String replaceEscapedCharacters(String source) {

		String result;
		String replacerRegex = "('('|\\{|\\}))";
		Pattern singleQuoteRegex = Pattern.compile(replacerRegex);
		Matcher singleQuoteMatcher = singleQuoteRegex.matcher(source);
		if (singleQuoteMatcher.find()) {
			result = source.replaceAll(replacerRegex, "$2");
		}else{
			result = source;
		}
		return result;
	}



	
	private static void validateNoParametersRemain(String message){
		//Now that all the params provided have been used there should be no more items to replace.
		Pattern parameterRegex = Pattern.compile(PARAM_FINDER_REGEX);
		Matcher parameterMatcher = parameterRegex.matcher(message);
	

		while (parameterMatcher.find()) {
			throw new IllegalArgumentException(message);
		}
		
	}

	/**
	 * Formats message established in constructor by replacing all parameterized
	 * text in the resource bundle which looks like {key} and replaces it with
	 * that key's value from the {@link Map} passed in
	 * 
	 * @param keysAndParams
	 *            A {@link Map} of all the {@link ParamTag} attribute "key" as
	 *            keys and the body of the ParamTag as values
	 * @return String The formatted message
	 * @throws IllegalArgumentException
	 *             if the parameter specified in the resource bundle was not
	 *             found on the page
	 */
	@SuppressWarnings("unchecked")
	public String format(Map keysAndParams) {
		Set<Map.Entry> entries = keysAndParams.entrySet();

		String result = this.message;
		StringBuffer paramKey;
		for (Map.Entry<Object, Object> entry : entries) {
			paramKey = new StringBuffer();
			paramKey.append("{");
			paramKey.append(entry.getKey());
			paramKey.append("}");
			if (result.indexOf(paramKey.toString()) == -1) {
				throw new IllegalArgumentException(
						"Unable to find given parameter " + entry.getKey());
			}
			result = StringUtils.replace(result, paramKey.toString(), entry
					.getValue().toString());

		}
		
		
		validateNoParametersRemain(result);
		result = replaceEscapedCharacters(result);
		return result;
	}

}
