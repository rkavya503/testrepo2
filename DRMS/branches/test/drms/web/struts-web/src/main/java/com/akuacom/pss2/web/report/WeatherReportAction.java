/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.ClientParticipationAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.web.util.ValidateTool;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class WeatherReportAction.
 */
public class WeatherReportAction extends DispatchAction
{

    List<Weather> weatherList;

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward unspecified(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
        ServletContext application=httpServletRequest.getSession().getServletContext();
        String dateFormat=(String)application.getAttribute("dateFormat");

        // default search past month
        WeatherReportForm form = (WeatherReportForm) actionForm;
        final Calendar cal = Calendar.getInstance();
        final Date end = cal.getTime();
        final String endDate = ValidateTool.getDateFormat(dateFormat).format(end);
        form.setEndDate(endDate);
        cal.add(Calendar.DATE, -10);
        final Date start = cal.getTime();
        final String startDate = ValidateTool.getDateFormat(dateFormat).format(start);
        form.setStartDate(startDate);
        return actionMapping.findForward("success");
	}

	/**
	 * List.
	 * 
	 * @param actionMapping the action mapping
	 * @param actionForm the action form
	 * @param httpServletRequest the http servlet request
	 * @param httpServletResponse the http servlet response
	 * 
	 * @return the action forward
	 * 
	 * @throws Exception the exception
	 */
	@SuppressWarnings({"UnusedDeclaration"})
    public ActionForward list(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        if (httpServletRequest.isUserInRole(DrasRole.Admin.toString())) {
            httpServletRequest.setAttribute("admin", true);
        }

        WeatherReportForm form = (WeatherReportForm) actionForm;

        ServletContext application=httpServletRequest.getSession().getServletContext();
        String dateFormat=(String)application.getAttribute("dateFormat");
            
        final ActionErrors actionErrors = validateInputs(form, dateFormat);
        if (actionErrors.size() > 0) {
            saveErrors(httpServletRequest, actionErrors);
            return actionMapping.findForward("success");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        final Date startTime = simpleDateFormat.parse(form.getStartDate());
		final Date endTime = simpleDateFormat.parse(form.getEndDate());

		ReportManager report = EJBFactory.getBean(ReportManager.class);
		 weatherList =
			report.getWeatherRecords(startTime, endTime);
		
		Date now =DateUtil.getStartOfDay(new Date()); 
		if (httpServletRequest.isUserInRole(DrasRole.Admin.toString()) && 
				now.getTime()>=startTime.getTime() && now.getTime()<=endTime.getTime())
			weatherList.addAll(getCurrentTwoDayRecords(now)); 
		
		form.setWeatherList(weatherList);

        if (DisplayTagUtil.isExport(httpServletRequest)) {
            return actionMapping.findForward("export");
        } else {
            return actionMapping.findForward("success");
        }
	}
	
	private List<Weather> getCurrentTwoDayRecords(Date date){
		List<Weather> list=new ArrayList<Weather>();
		
		Date previous=DateUtil.getPreviousDay(date);
		ReportManager report = EJBFactory.getBean(ReportManager.class);
		List<Weather> currentTwoDayRecords= report.getWeatherRecords(previous, date);
		Boolean today=false;
		Boolean yesterday=false;
		if (currentTwoDayRecords!=null && currentTwoDayRecords.size()>0) {
			for (Weather w: currentTwoDayRecords) {
				if (w.getDate().equals(date)) {
					today=true;
				}
				if (w.getDate().equals(DateUtil.getPreviousDay(date))){
					yesterday=true;
				}
			}
		}
		
		if (!yesterday) {
			Weather prevousWeather=new Weather();
			prevousWeather.setDate(new java.sql.Date(date.getTime()-DateUtil.MSEC_IN_DAY));
			list.add(prevousWeather);
		}
		if (!today) {
			Weather todayWeather=new Weather();
			todayWeather.setDate(new java.sql.Date(date.getTime()));
			list.add(todayWeather);
		}
		
		return list;
	}
    

        public ActionForward exportWeather(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {


        StringBuilder table = new StringBuilder();
        table.append("Date,High,Station,Final,Forcast0, Forcast1,Forcast2,Forcast3,Forcast4,Forcast5");

        for(Weather w:weatherList){
            table.append("\n");
            table.append(w.getDate()).append(",");
            table.append(w.getHigh()).append(",");

            table.append(w.getReportingStation()).append(",");
            table.append(w.isIsFinal()).append(",");
            table.append(w.getForecastHigh0()).append(",");
            table.append(w.getForecastHigh1()).append(",");
            table.append(w.getForecastHigh2()).append(",");
            table.append(w.getForecastHigh3()).append(",");
            table.append(w.getForecastHigh4()).append(",");
            table.append(w.getForecastHigh5()).append(",");
        }

     
        String filename = "weather.csv";
        response.reset();
        response.addHeader("cache-control", "must-revalidate");
        response.setContentType("application/octet_stream");
        response.setHeader("Content-Disposition", "attachment; filename=\""
                    + filename + "\"");
        response.getWriter().print(table);
        return null;

    }


    private ActionErrors validateInputs(WeatherReportForm form, String dateFormat) {
        ActionErrors errors = new ActionErrors();
        
        // validate start date
        final String startDate = form.getStartDate();
        ValidateTool.validateDateField(errors, startDate, "startDate", "Start Date", dateFormat);
        // validate end date
        final String endDate = form.getEndDate();
        ValidateTool.validateDateField(errors, endDate, "endDate", "End Date", dateFormat);

        return errors;
    }
}
