/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.PSS2Util.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

/**
 * The Class PSS2Util.
 */
public class PSS2Util {

	/** The Constant LEGAL_NAME_DESCRIPTION. */
	public static final String LEGAL_NAME_DESCRIPTION = "must be a letter, digit, space, '-', or '_'";

	// TODO: tie these to the db in some way
	public static final int MAX_EVENT_NAME_LENGTH = 64;
	public static final int MAX_SOFTWARE_CLIENT_NAME_LENGTH = 45;
	public static final int MAX_CLIR_CLIENT_NAME_LENGTH = 41;

	public static final char[] VALID_NAME_CHARS = { '-', '_', ' ' };

	
	private static final Pattern P_PARTICIPANT_NAME=Pattern.compile("^[\\w\\s]+((\\-)?[\\w\\s]*)*$");
	private static final Pattern P_CLIENT_NAME=Pattern.compile("^[\\w\\s]+([\\.\\-]?[\\w\\s]*)*$");
	private static final Pattern P_ACCOUNT=P_PARTICIPANT_NAME;
	private static final Pattern P_PROGRAM_NAME=Pattern.compile("^[\\w\\s<>]+((\\-)?[\\w\\s<>]*)*$");
	
	public static boolean isLegalParticipantName(String name){
		if(name==null || name.trim().length()==0) return true;
		if(name.indexOf("--")>=0)
			return false;
		Matcher m = P_PARTICIPANT_NAME.matcher(name);
		if(m.matches())
			return true;
		else
			return false;
	}
	
	public static boolean isLegalClientName(String name){
		if(name==null || name.trim().length()==0) return true;
		
		if(name.indexOf("--")>=0)
			return false;
		Matcher m = P_CLIENT_NAME.matcher(name);
		if(m.matches())
			return true;
		else
			return false;
	}
	
	public static boolean isLegalAccountNo(String name){
		if(name==null || name.trim().length()==0) return true;
		if(name.indexOf("--")>=0)
			return false;
		Matcher m = P_ACCOUNT.matcher(name);
		if(m.matches())
			return true;
		else
			return false;
	}
	
	public static boolean isLegalProgramName(String name){
		if(name==null || name.trim().length()==0) return true;
		if(name.indexOf("--")>=0)
			return false;
		Matcher m = P_PROGRAM_NAME.matcher(name);
		if(m.matches())
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if is event legal name.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return true, if is legal name
	 */
	public static boolean isLegalName(String name) {
		for (char c : name.toCharArray()) {
			if (isLegalChar(c)) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static boolean isLegalChar(char c) {
		boolean result;
		if (Character.isLetterOrDigit(c)) {
			result = true;
		} else {
			result = ArrayUtils.contains(VALID_NAME_CHARS, c);
		}
		return result;
	}
	
	public static boolean validatePassword(String password, String confirmPassword,
		boolean clir, List<String> messages)
	{
		boolean validPassword = true;
		if (!password.equals(confirmPassword))
		{
			messages.add("Passwords don't match");
			validPassword = false;
		}
		if (password.length() < 9)
		{
			messages.add("New password must have 9 or more characters");
			validPassword = false;
		}
		if (!password.matches(".*[A-Z].*"))
		{// at least one upper case required
			messages.add("New password has to have at least one upper case character (A-Z)");
			validPassword = false;
		}
		if (!password.matches(".*[a-z].*"))
		{// at least one lower case required
			messages.add("New password has to have at least one lower case character (a-z)");
			validPassword = false;
		}
		if (!password.matches(".*\\d.*"))
		{// at least one digit required
			messages.add("New password has to have at least one digital character (1-9)");
			validPassword = false;
		}
		if (clir)
		{
			if (password.length() > 20)
			{
				messages.add("New password can be longer than 24 characters");
				validPassword = false;
			}
			// clirs have limited special character support
			if (password.matches(".*[\\W&&[^\\._\\-/]].*"))
			{// illegal cahracter for clir
				messages.add("New password has illegal character(s), only the following characters are allowed (A-Za-z1-9._-/)");
				validPassword = false;
			}
			if (!password.matches(".*[\\._\\-/].*"))
			{// at least one special character required
				messages.add("New password has to have at least one of the following special character (._-/)");
				validPassword = false;
			}
		}
		else
		{
			if (password.length() > 24)
			{
				messages.add("New password can be longer than 24 characters");
				validPassword = false;
			}
			if (!password.matches(".*[\\W_].*"))
			{// at least one special character required
				messages.add("New password has to have at least one special character (!@#$%^...)");
				validPassword = false;
			}
		}

		return validPassword;
	}

}
