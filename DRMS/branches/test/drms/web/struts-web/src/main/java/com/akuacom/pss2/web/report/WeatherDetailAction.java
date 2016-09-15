package com.akuacom.pss2.web.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.web.util.EJBFactory;

public class WeatherDetailAction extends DispatchAction {

    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return view(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }

	public ActionForward view(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
		WeatherDetailForm form = (WeatherDetailForm) actionForm;
		ReportManager report = EJBFactory.getBean(ReportManager.class);
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(form.getDate());
		final List<Weather> list = report.getWeatherRecords(date, date);
		if (list!=null && list.size()>0) {
			form.setFinal(list.get(0).isIsFinal());
			form.setReportingStation(list.get(0).getReportingStation());
			form.setHigh(list.get(0).getHigh());
			form.setForecastHigh0(list.get(0).getForecastHigh0());
			form.setForecastHigh1(list.get(0).getForecastHigh1());
			form.setForecastHigh2(list.get(0).getForecastHigh2());
			form.setForecastHigh3(list.get(0).getForecastHigh3());
			form.setForecastHigh4(list.get(0).getForecastHigh4());
			form.setForecastHigh5(list.get(0).getForecastHigh5());
		} else {
			form.setFinal(false);
			form.setReportingStation("");
			form.setHigh(0.0);
			form.setForecastHigh0(0.0);
			form.setForecastHigh1(0.0);
			form.setForecastHigh2(0.0);
			form.setForecastHigh3(0.0);
			form.setForecastHigh4(0.0);
			form.setForecastHigh5(0.0);
		}

		return actionMapping.findForward("success");
	}

	public ActionForward update(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
		WeatherDetailForm form = (WeatherDetailForm) actionForm;

        final ActionErrors actionErrors = validateInputs(form);
        if (actionErrors.size() > 0) {
            saveErrors(httpServletRequest, actionErrors);
            return actionMapping.findForward("success");        
        }
		ReportManager report = EJBFactory.getBean(ReportManager.class);
		// TODO: we need a manager here
		WeatherEAO weatherEAO = EJBFactory.getBean(WeatherEAO.class);	
		
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(form.getDate());
		// TODO: should be a more direct method (getWeatherRecord())
		final List<Weather> list = report.getWeatherRecords(date, date);
		Weather weather = new Weather();
		boolean update=false;
		if (list!=null && list.size()>0) {
			weather = list.get(0);
			update=true;
		}
		weather.setDate(date);
		weather.setIsFinal(form.isFinal());
		if(form.getReportingStation().equals(""))
		{
			weather.setReportingStation(null);
		}
		else
		{
			weather.setReportingStation(form.getReportingStation());
		}
		weather.setHigh(form.getHigh());
		weather.setForecastHigh0(form.getForecastHigh0());
		weather.setForecastHigh1(form.getForecastHigh1());
		weather.setForecastHigh2(form.getForecastHigh2());
		weather.setForecastHigh3(form.getForecastHigh3());
		weather.setForecastHigh4(form.getForecastHigh4());
		weather.setForecastHigh5(form.getForecastHigh5());
		// TODO: we need a manager here	
		if (update)
			weatherEAO.update(weather);
		else
			weatherEAO.create(weather);
		
        return actionMapping.findForward("parent");
	}
	
	public ActionForward cancel(ActionMapping actionMapping,
		ActionForm actionForm, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) throws Exception
	{
        return actionMapping.findForward("parent");
	}
	

    private ActionErrors validateInputs(WeatherDetailForm form) {
        ActionErrors errors = new ActionErrors();

        if ((form.getReportingStation() != null) && (form.getReportingStation().length() > 8 )) {
            errors.add("ReportingStation", new ActionMessage("pss2.weather.reportingstation.toolong", "Reporting Station"));
        }
        	
        return errors;
    }
	
}
