/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.rtp.SCERTPImporter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.rtp;

import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.season.SeasonConfig;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class SCERTPImporter
{
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(SCERTPImporter.class);


    static private SCERTPProgramManager scertpPM =EJBFactory.getBean(SCERTPProgramManager.class);
    /**
     * Import scertp.
     * 
     * @param request the request
     * @param response the response
     * 
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void importSCERTP(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
    {

// Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
        {
            return ;
        }

        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Get the user name
        String programName = request.getParameter("programName");
        String ret = "";

        // Parse the request
        List /* FileItem */ items;
        try {
            items = upload.parseRequest(request);
            // Process the uploaded items
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    
                } else {
                    ret = processUploadedFile(item, programName);
                }
            }
        }
        catch (Exception e)
        {
            ret = "Failed: " + e.getMessage();
        }

        response.getWriter().println(ret);
    }

    /**
     * Process uploaded file.
     * 
     * @param item the item
     * @param programName the program name
     * 
     * @return the string
     * 
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String processUploadedFile(FileItem item, String programName) throws ServletException, IOException
	{
		if (programName == null)
		{
            return "FAILED: program name can't be null!";
		}

	    String fieldName = item.getFieldName();
//	    String realPath = this.getServletContext().getRealPath("/upload/images");
//	    File file = new File(realPath + File.separatorChar + justFileName);
	    String contentType = item.getContentType();
	    boolean isInMemory = item.isInMemory();
	    long sizeInBytes = item.getSize();

	    // Process a file upload
	    InputStream uploadedStream;
	    try
        {
				uploadedStream = item.getInputStream();
                InputStreamReader in= new InputStreamReader(uploadedStream);
                BufferedReader bur= new BufferedReader(in);
                String s = new String();
                int lineNum = 1;
                List<String> names = null;
                List<String> seasons = null;
                List<Double> startT = null;
                List<Double> endT = null;
                List<RTPConfig> listRC = null;
                while((s = bur.readLine())!= null) {
                    if(lineNum == 3 && ! s.startsWith(programName))
                    {
                        return "FAILED: program name doesn't match!";
                    }
                    if(lineNum == 6 || lineNum == 7 || lineNum == 8)
                    {
                        // parse name
                        names = parseName(s, names);
                    }
                    if(lineNum == 7)
                    {
                        // parse season
                        seasons = parseSeason(s);
                    }
                    if(lineNum == 9)
                    {
                        // parse start and end temperatures
                        startT = parseStartTemperature(s);
                        endT = parseEndTemperature(s);
                    }
                    if(lineNum >= 11 && lineNum <= 34)
                    {
                        // parse prices
                        listRC = parsePrice(s, lineNum-11, names, seasons, startT, endT, listRC);
                    }

                    lineNum++;
                }
                scertpPM.saveRTPConfig(listRC, programName);
                uploadedStream.close();
		} catch (IOException e) {
				throw e;
			}
        return "SUCCESS!";
    }

    /**
     * Parses the name.
     * 
     * @param line the line
     * @param input the input
     * 
     * @return the list< string>
     */
    public static List<String> parseName(String line, List<String> input)
    {
        String[] values = line.split(",");
        boolean append = true;
        if(input == null)
        {
            input = new ArrayList<String>();
            append = false;
        }

        for(int i=1; i<values.length && i<10; i++)
        {
            if(input.size() < i)
            {
                append = false;
            }
            if(append)
            {
                input.set(i-1, input.get(i-1) + " " + values[i]);
            }
            else
            {
                input.add(values[i]);
            }
        }

        return input;
    }

    /**
     * Parses the season.
     * 
     * @param line the line
     * 
     * @return the list< string>
     */
    public static List<String> parseSeason(String line)
    {
        String[] values = line.split(",");
        List<String> input = new ArrayList<String>();

        for(int i=1; i<values.length && i<10; i++)
        {
            String value = values[i];
            if(value != null && value.indexOf(SeasonConfig.SUMMER_SEASON) >= 0)
            {
                input.add(SeasonConfig.SUMMER_SEASON);
            }
            else if(value != null && value.indexOf(SeasonConfig.WINTER_SEASON) >= 0)
            {
                input.add(SeasonConfig.WINTER_SEASON);
            }
            else
            {
                input.add(SeasonConfig.WEEKEND_SEASON);    
            }
        }

        return input;
    }

    /**
     * Parses the start temperature.
     * 
     * @param line the line
     * @param seasonNames the season names
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
     * @param line the line
     * @param seasonNames the season names
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
     * @param input the input
     * 
     * @return the first number
     */
    public static Double getFirstNumber(String input)
    {
        if(input == null || input.length() == 0)
        {
            return null;
        }
        else
        {
            boolean passed = false;
            StringBuilder numberStr = new StringBuilder();
            for(int i=0; i<input.length(); i++)
            {
                char x = input.charAt(i);

                if(Character.isDigit(x) && ! passed)
                {
                    passed = true;
                    numberStr.append(x);
                }
                else if(! Character.isDigit(x) && passed)
                {
                    break;
                }
                else if(passed && Character.isDigit(x))
                {
                    numberStr.append(x);
                }
                else
                {
                    continue;
                }
            }
            if(numberStr.toString() == null || numberStr.toString().isEmpty())
            {
                return null;
            }
            else
            {
                return Double.valueOf(numberStr.toString());
            }
        }
    }



    /**
     * Parses the price.
     * 
     * @param line the line
     * @param lineNum the line num
     * @param names the names
     * @param seasonNames the season names
     * @param startTemps the start temps
     * @param endTemps the end temps
     * @param list the list
     * 
     * @return the list< rtp config>
     */
    public static List<RTPConfig> parsePrice(String line, int lineNum,
                                   List<String> names, List<String> seasonNames,
                                   List<Double> startTemps, List<Double> endTemps, List<RTPConfig> list)
    {
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
        if(list == null)
        {
            list = new ArrayList<RTPConfig>();
        }
        int i=0;

        for(String name : names)
        {
            RTPConfig rconf = new RTPConfig();
            rconf.setStartTime(startT);
            rconf.setEndTime(endT);
            rconf.setName(name);
            String seasonName = seasonNames.get(i);
            rconf.setSeasonName(seasonName);
            Double startTp = startTemps.get(i);
            if(startTp != null)
            {
                rconf.setStartTemperature(startTp.doubleValue());
            }
            Double endTp = endTemps.get(i);
            if(endTp != null) 
            {
                rconf.setEndTemperature(endTp.doubleValue());
            }
            list.add(rconf);
            i++;

            rconf.setRate(Double.valueOf(values[i].trim()).doubleValue());
        }

        return list;
    }

}
