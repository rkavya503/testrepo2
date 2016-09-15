/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ClientParticipationReportForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * The Class ClientParticipationReportForm.
 */
public class WeatherReportForm extends ActionForm {
    
    /** The start date. */
    private String startDate;
    
    /** The end date. */
    private String endDate;
    
    /** The participation list. */
    private List<Weather> weatherList;

    private boolean nooaWeatherLink;

    /**
     * Gets the participation list.
     * 
     * @return the participation list
     */
    public List<Weather> getWeatherList() {
        return weatherList;
    }

    /**
     * Sets the participation list.
     * 
     * @param participationList the new participation list
     */
    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }


    /**
     * Gets the end date.
     * 
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     * 
     * @param endDate the new end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start date.
     * 
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isNooaWeatherLink() {
        nooaWeatherLink = this.getCoreAccess("feature.noaa.link");
        return nooaWeatherLink;
    }

    public void setNooaWeatherLink(boolean nooaWeatherLink) {
        this.nooaWeatherLink = nooaWeatherLink;
    }

    
    public boolean getCoreAccess(String coreValue){
    	   SystemManager systemManager = EJBFactory.getBean(SystemManagerBean.class);
           boolean flag= false;
           for(CoreProperty corp : systemManager.getAllProperties()){
        	   if (corp.getPropertyName().equalsIgnoreCase(coreValue))
                  flag =corp.isBooleanValue();
           }
       return flag;
    }
    
}
