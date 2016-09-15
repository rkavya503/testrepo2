/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.ForecastTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ForecastTable.
 */
public class ForecastTable
{	
	
	/**
	 * Gets the forecasts.
	 * 
	 * @return the forecasts
	 */
	public List<Forecast> getForecasts()
	{
        com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

        List<Forecast> forecasts = new ArrayList<Forecast>();
        List<String> programList = programManager1.getPrograms();
        for (String programName : programList) {
            forecasts.add(new Forecast(programName, "low", "low", "low", "low", "low"));
        }

		return forecasts;
	}
	
}
