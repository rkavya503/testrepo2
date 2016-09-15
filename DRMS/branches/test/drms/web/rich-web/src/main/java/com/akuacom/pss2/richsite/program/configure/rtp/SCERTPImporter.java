/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.rtp.SCERTPImporter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite.program.configure.rtp;

import org.apache.log4j.Logger;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.season.SeasonConfig;

import javax.servlet.ServletException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA. To change this template use File | Settings | File
 * Templates.
 */
public class SCERTPImporter implements Serializable {

	/** The Constant log. */
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(SCERTPImporter.class);

	static private SCERTPProgramManager scertpPM = EJB3Factory.getBean(SCERTPProgramManager.class);

	/**
	 * Extends function for JSF file upload component
	 * 
	 * @param file
	 * @param programName
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String processUploadedFile(File file, String programName)throws IOException {
		if (programName == null||programName.equalsIgnoreCase("")) {
			return "FAILED: program name can't be null!";
		}
		if (file != null) {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader bur = new BufferedReader(new InputStreamReader(inputStream));
			try {
				String s = new String();
				int lineNum = 1;
				List<String> names = null;
				List<String> seasons = null;
				List<Double> startT = null;
				List<Double> endT = null;
				List<RTPConfig> listRC = null;
				while ((s = bur.readLine()) != null) {
					if (lineNum == 3 && !s.startsWith(programName)) {
						return "FAILED: program name doesn't match!";
					}
					if (lineNum == 6 || lineNum == 7 || lineNum == 8) {
						// parse name
						names = parseName(s, names);
					}
					if (lineNum == 7) {
						// parse season
						seasons = parseSeason(s);
					}
					if (lineNum == 9) {
						// parse start and end temperatures
						startT = parseStartTemperature(s);
						endT = parseEndTemperature(s);
					}
					if (lineNum >= 11 && lineNum <= 34) {
						// parse prices
						listRC = parsePrice(s, lineNum - 11, names, seasons,
								startT, endT, listRC);
					}

					lineNum++;
				}
				scertpPM.saveRTPConfig(listRC, programName);
			} catch (IOException e) {
				throw e;
			}
		}

		return "SUCCESS";
	}
	
	
	/**
	 * Extends function for JSF file upload component
	 * 
	 * @param file
	 * @param programName
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String processUploadedFile2013(File file, String programName)throws IOException {
		if (programName == null||programName.equalsIgnoreCase("")) {
			return "FAILED: program name can't be null!";
		}
		if (file != null) {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader bur = new BufferedReader(new InputStreamReader(inputStream));
			try {
				String s = new String();
				int lineNum = 1;
				List<String> names = null;
				List<String> seasons = null;
				List<Double> startT = null;
				List<Double> endT = null;
				List<RTPConfig> listRC = null;
				while ((s = bur.readLine()) != null) {
					if (lineNum == 3 && !s.startsWith(programName)) {
						return "FAILED: program name doesn't match!";
					}
					if (lineNum == 6 || lineNum == 7 || lineNum == 8) {
						// parse name
						names = parseName(s, names);
					}
					if (lineNum == 7) {
						// parse season
						seasons = parseSeason(s);
					}
					if (lineNum == 9) {
						// parse start and end temperatures
						startT = parseStartTemperature(s);
						endT = parseEndTemperature(s);
					}
						// parse prices
					if (lineNum >= 11) {
						break;
					}

					lineNum++;
				}
				listRC = generateCategory( names, seasons,
						startT, endT, listRC);
				scertpPM.saveRTPConfig(listRC, programName);
			} catch (IOException e) {
				throw e;
			}
		}

		return "SUCCESS";
	}
	
	
	private static List<RTPConfig> generateCategory(
			List<String> names, List<String> seasonNames,
			List<Double> startTemps, List<Double> endTemps, List<RTPConfig> list) {
		Calendar scal = Calendar.getInstance();
		scal.setTime(new Date());
		scal.set(Calendar.HOUR_OF_DAY, 0);
		scal.set(Calendar.MINUTE, 0);
		scal.set(Calendar.SECOND, 0);
		scal.set(Calendar.MILLISECOND, 0);
		Date startT = scal.getTime();
		Calendar ecal = Calendar.getInstance();
		ecal.setTime(new Date());
		ecal.set(Calendar.HOUR_OF_DAY, 0);
		ecal.set(Calendar.MINUTE, 0);
		ecal.set(Calendar.SECOND, 0);
		ecal.set(Calendar.MILLISECOND, 0);
		Date endT = ecal.getTime();
		if (list == null) {
			list = new ArrayList<RTPConfig>();
		}
		int i = 0;

		for (String name : names) {
			RTPConfig rconf = new RTPConfig();
			rconf.setStartTime(startT);
			rconf.setEndTime(endT);
			rconf.setName(name);
			String seasonName = seasonNames.get(i);
			rconf.setSeasonName(seasonName);
			Double startTp = startTemps.get(i);
			if (startTp != null) {
				rconf.setStartTemperature(startTp.doubleValue());
			}
			Double endTp = endTemps.get(i);
			if (endTp != null) {
				rconf.setEndTemperature(endTp.doubleValue());
			}
			list.add(rconf);
			i++;

			rconf.setRate(0);
		}

		return list;
	}

	/**
	 * Parses the name.
	 * 
	 * @param line
	 *            the line
	 * @param input
	 *            the input
	 * 
	 * @return the list< string>
	 */
	public static List<String> parseName(String line, List<String> input) {
		String[] values = line.split(",");
		boolean append = true;
		if (input == null) {
			input = new ArrayList<String>();
			append = false;
		}

		for (int i = 1; i < values.length && i < 10; i++) {
			if (input.size() < i) {
				append = false;
			}
			if (append) {
				input.set(i - 1, input.get(i - 1) + " " + values[i]);
			} else {
				input.add(values[i]);
			}
		}

		return input;
	}

	/**
	 * Parses the season.
	 * 
	 * @param line
	 *            the line
	 * 
	 * @return the list< string>
	 */
	public static List<String> parseSeason(String line) {
		String[] values = line.split(",");
		List<String> input = new ArrayList<String>();

		for (int i = 1; i < values.length && i < 10; i++) {
			String value = values[i];
			if (value != null && value.indexOf(SeasonConfig.SUMMER_SEASON) >= 0) {
				input.add(SeasonConfig.SUMMER_SEASON);
			} else if (value != null
					&& value.indexOf(SeasonConfig.WINTER_SEASON) >= 0) {
				input.add(SeasonConfig.WINTER_SEASON);
			} else {
				input.add(SeasonConfig.WEEKEND_SEASON);
			}
		}

		return input;
	}

	/**
	 * Parses the start temperature.
	 * 
	 * @param line
	 *            the line
	 * @param seasonNames
	 *            the season names
	 * 
	 * @return the list< double>
	 */
	public static List<Double> parseStartTemperature(String line) {
		String[] values = line.split(",");
		List<Double> output = new ArrayList<Double>();
		Double min = null;
		for (int i = 1; i < values.length && i < 10; i++) {
			min = null;
			String value = values[i];

			if (value.startsWith("(>=")) { // starts at the number
				min = getFirstNumber(value);
			} else if (value.startsWith("(>")) { // simple > starts above the number
				min = getFirstNumber(value) + 1;
			} else if (value.startsWith("(<")) { // surely LA will never have a sub-zero high
				min = 0.0;
			} else if (value.contains("-")) { // inclusive of first number
				min = getFirstNumber(value);
			}

			output.add(min);
		}

		return output;
	}

	/**
	 * Parses the end temperature.
	 * 
	 * @param line
	 *            the line
	 * @param seasonNames
	 *            the season names
     * 
     * The end temperature is not inclusive in the temperature range
	 * 
	 * @return the list< double>
	 */
	public static List<Double> parseEndTemperature(String line) {
		String[] values = line.split(",");
		List<Double> output = new ArrayList<Double>();
		Double max = null;
		for (int i = 1; i < values.length && i < 10; i++) 
        {
			max = null;
			String value = values[i/*-1*/];

			if (value.startsWith("(>")) {
				max = 200.0;  // Surely LA will never have a high over 200
			} else if (value.startsWith("(<=")) {
				max = getFirstNumber(value) + 1; // the number is included in the range
			} else if (value.startsWith("(<")) {
				max = getFirstNumber(value);  // The number is not included in the range
			} else if (value.contains("-")) { // The second number is included in the range
				String partial = value.substring(value.indexOf("-"));
				max = getFirstNumber(partial) + 1;  
			}

			output.add(max);
		}

		return output;
	}

	/**
	 * Gets the first number.
	 * 
	 * @param input
	 *            the input
	 * 
	 * @return the first number
	 */
	public static Double getFirstNumber(String input) {
		if (input == null || input.length() == 0) {
			return null;
		} else {
			boolean passed = false;
			StringBuilder numberStr = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				char x = input.charAt(i);

				if (isPartOfNumber(x) && !passed) {
					passed = true;
					numberStr.append(x);
				} else if (!isPartOfNumber(x) && passed) {
					break;
				} else if (passed && isPartOfNumber(x)) {
					numberStr.append(x);
				} else {
					continue;
				}
			}
			if (numberStr.toString() == null || numberStr.toString().isEmpty()) {
				return null;
			} else {
				return Double.valueOf(numberStr.toString());
			}
		}
	}
    
    private static boolean isPartOfNumber(char x) {
        if (Character.isDigit(x)) { return true; }
        if (x == '.') { return true; }
        return false;
    }

	/**
	 * Parses the price.
	 * 
	 * @param line
	 *            the line
	 * @param lineNum
	 *            the line num
	 * @param names
	 *            the names
	 * @param seasonNames
	 *            the season names
	 * @param startTemps
	 *            the start temps
	 * @param endTemps
	 *            the end temps
	 * @param list
	 *            the list
	 * 
	 * @return the list< rtp config>
	 */
	public static List<RTPConfig> parsePrice(String line, int lineNum,
			List<String> names, List<String> seasonNames,
			List<Double> startTemps, List<Double> endTemps, List<RTPConfig> list) {
		String[] values = line.split(",");
		Calendar scal = Calendar.getInstance();
		scal.setTime(new Date());
		scal.set(Calendar.HOUR_OF_DAY, lineNum);
		scal.set(Calendar.MINUTE, 0);
		scal.set(Calendar.SECOND, 0);
		scal.set(Calendar.MILLISECOND, 0);
		Date startT = scal.getTime();
		Calendar ecal = Calendar.getInstance();
		ecal.setTime(new Date());
		ecal.set(Calendar.HOUR_OF_DAY, lineNum + 1);
		ecal.set(Calendar.MINUTE, 0);
		ecal.set(Calendar.SECOND, 0);
		ecal.set(Calendar.MILLISECOND, 0);
		Date endT = ecal.getTime();
		if (list == null) {
			list = new ArrayList<RTPConfig>();
		}
		int i = 0;

		for (String name : names) {
			RTPConfig rconf = new RTPConfig();
			rconf.setStartTime(startT);
			rconf.setEndTime(endT);
			rconf.setName(name);
			String seasonName = seasonNames.get(i);
			rconf.setSeasonName(seasonName);
			Double startTp = startTemps.get(i);
			if (startTp != null) {
				rconf.setStartTemperature(startTp.doubleValue());
			}
			Double endTp = endTemps.get(i);
			if (endTp != null) {
				rconf.setEndTemperature(endTp.doubleValue());
			}
			list.add(rconf);
			i++;

			rconf.setRate(Double.valueOf(values[i].trim()).doubleValue());
		}

		return list;
	}

}
