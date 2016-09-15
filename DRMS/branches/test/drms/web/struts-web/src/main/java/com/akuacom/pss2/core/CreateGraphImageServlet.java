/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.CreateGraphImageServlet.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPEvent;

/**
 * The Class CreateGraphImageServlet.
 */
public class CreateGraphImageServlet extends HttpServlet
{
	
	/** The log. */
	private static Logger log = Logger.getLogger("com.akuacom.pss2.core");
	
	/** The width. */
	private int width = 800;
	
	/** The height. */
	private int height = 264;	
	
	/** The accept bid list. */
	private String[] acceptBidList = null;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
        String programName = request.getParameter("programName");
        System.out.println(" getting image for "+ programName);
		String wstr = request.getParameter("width");
		if (wstr != null)
			width = Integer.parseInt(wstr);
		log.debug(" image width="+width);
		
		String liststr = request.getParameter("acceptedBidID");
		if (liststr != null)
		{
			acceptBidList = null;
	    	StringTokenizer st = new StringTokenizer(liststr, ":");
	    	int len = st.countTokens();
	    	if (len > 0)
	    	{
	    		acceptBidList = new String[len];
	    		int i=0;
		    	while (st.hasMoreTokens()) 
		    	{
		    		acceptBidList[i++] = st.nextToken();
		    	}
	    	}
		}
		//create image
		//Draw the chart in an AWT buffered image 
		BufferedImage buffer 
		      = createBarChartImage();
		
		// write the image to response
		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(buffer, "png",os);
		os.close();
	}
	
	/**
	 * Creates the bar chart image.
	 * 
	 * @return the buffered image
	 */
	private BufferedImage createBarChartImage() {
        String title = "Current Bids Chart for ";				
		//create data set
		DefaultCategoryDataset totalDataSet = new DefaultCategoryDataset();
		DefaultCategoryDataset[] datasets = null;
		DefaultCategoryDataset firstDataSet;
        try
        {
			DBPEvent lastEntry = null;
				//(DBPProgramEvent) CacheUtil.getNextDBPEvent("DBP");
//				(DBPProgramEvent) CacheUtil.getNextDBPEventByProgramName("DBP",programName);
            
            title += lastEntry.getProgramName();
			Object obj = null;
			if (lastEntry != null)
				obj = null; // use the CoreEent got above and get participants from the event object
            if (obj != null)
			{
		        
				ArrayList facilities = (ArrayList) obj;
				int numEntries = 8;
				double[] totalKW = new double[numEntries];
				//TODO make these from bid entry only once
				String[] category = new String[] {
						"12.00-13.00","13.00-14.00","14.00-15.00",
						"15.00-16.00","16.00-17.00","17.00-18.00",
						"18.00-19.00","19.00-20.00"
						};
				datasets = new DefaultCategoryDataset[facilities.size()];
				int counter = 0;
				for (int i=0; i < facilities.size(); i++)
				{
					Participant p = (Participant) (facilities.get(i));
					if (p != null)
					{
						String pname = p.getUser();
//                        DBPBidInfoEntry bidInfo = CacheUtil.getBidInfoForParticipantName(pname, lastEntry.getId(),lastEntry.getProgramName());
                        Object bidInfo = null; // TODO: DBPEventCore will have a method for bidInfos for a participant
						if ( bidInfo == null /* || bidInfo.isBidSubmitted() */) // TODO: put this back
						{
							boolean accepted = true;
							if (acceptBidList != null)
							{
								for (int k=0; k < acceptBidList.length;k++)
								{
									if (acceptBidList[k].equals(p.getUser()))
									{
										accepted = false;
										break;
									}
								}
							}		
							
//                            ArrayList bids = CacheUtil.getCurrentBidForParticipantName(pname,
//                                    lastEntry.getId(),
//                                    lastEntry.getProgramName());
                            ArrayList bids = null; // TODO: DBPEventCore will have a method for bids for a participant
                            if (bids == null)
							{
//                                bids = CacheUtil.getDefaultBidForParticipantName(pname,lastEntry.getProgramName());
                                bids = null;    // TODO: DBPProgramBean will get default bids for the participant
							}
							if ( accepted && bids != null)
							{
								double st = 12.00;
								double ed = 13.00;
						        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
								datasets[counter++] = dataset;
								GregorianCalendar cal1 = new GregorianCalendar();
								GregorianCalendar cal2 = new GregorianCalendar();
								
								for (int k = 0; k < numEntries; k++)
								{
									double numKW = 0.0;
									for (int j=0; j < bids.size(); j++)
									{
										BidEntry entry = (BidEntry) bids.get(j);
										if (entry != null )
										{
											cal1.setTime(entry.getBlockStart());
											cal2.setTime(entry.getBlockEnd());
//											if (entry.getBlockStart().getHours() == st
//												&& entry.getBlockEnd().getHours() == ed)
												if (cal1.get(GregorianCalendar.HOUR_OF_DAY) == st
														&& cal2.get(GregorianCalendar.HOUR_OF_DAY) == ed)
													{
												//System.out.println("found bid for st="+st+" ed="+ed);
												numKW = entry.getReductionKW();
												if (!entry.isActive())
												{
													numKW = 0.0;
												}
												dataset.addValue(totalKW[k] +numKW, pname, category[k]);
											}
										}
									}
									totalKW[k] += numKW;
									++st;
									++ed;
								}
							}
						} //end if facility did not decline to bid	
					}
				}// end for facilities loop
				for ( int k=0; k < numEntries; k++)
				{
					totalDataSet.addValue(totalKW[k], "Total", category[k]);
				}
			}//end obj != null
        }
        catch (Exception e)
        {
        	log.error("CreateGraphImageServlet:",e);
        }
		
        // create the chart...
        if (datasets != null && datasets[0] != null)
        {
        	firstDataSet = datasets[0];
        }
        else
        	firstDataSet = totalDataSet;
        JFreeChart chart = ChartFactory.createBarChart(
                title,         // chart title
                "Time Block",               // domain axis label
                "reduction KW",            // range axis label
                firstDataSet,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
            );
        //add additional datasets here
        if ( datasets != null)
        {
        	CategoryPlot plot = chart.getCategoryPlot();
        	int counter = 1;
        	for (int k=1; k < datasets.length; k++)
        	{
        		if (datasets[k] != null)
        		{
        			System.out.println("adding dataset for counter ="+counter);
        			plot.setDataset(counter, datasets[k]);
        	        BarRenderer renderer = new BarRenderer();
        	        switch (k)
        	        {
	        	        case 1:
	        	        	renderer.setSeriesPaint(0, Color.blue);
	        	        break;
	        	        case 2:
	        	        	renderer.setSeriesPaint(0, Color.green);
	        	        break;
	        	        case 3:
	        	        	renderer.setSeriesPaint(0, Color.pink);
	        	        break;
	        	        case 4:
	        	        	renderer.setSeriesPaint(0, Color.magenta);
	            	    break;
	        	        case 5:
	        	        	renderer.setSeriesPaint(0, Color.yellow);
	            	    break;
	        	        case 6:
	        	        	renderer.setSeriesPaint(0, Color.cyan);
	            	    break;
	        	        case 7:
	        	        	renderer.setSeriesPaint(0, Color.orange);
	            	    break;
	       	        	default:
	        	        	renderer.setSeriesPaint(0, Color.red);
	            	    break;
        	        }
        	        plot.setRenderer(counter, renderer);
        	        ++counter;
        		}
        	}
        }
        
		BufferedImage buffer = 
				chart.createBufferedImage(width, height, null); 
	    return buffer;
	}
	
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
    	doGet(request, response);
	}


}
