package com.akuacom.pss2.web.report;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class WeatherDetailForm extends ActionForm {
    private String date;
	private boolean isFinal = false;
    private String reportingStation = null;
    private Double high = null;
    private double forecastHigh0;
    private double forecastHigh1;
    private double forecastHigh2;
    private double forecastHigh3;
    private double forecastHigh4;
    private double forecastHigh5;

    public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public boolean isFinal()
	{
		return isFinal;
	}
	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}
	public String getReportingStation()
	{
		return reportingStation;
	}
	public void setReportingStation(String reportingStation)
	{
		this.reportingStation = reportingStation;
	}
	public Double getHigh()
	{
		return high;
	}
	public void setHigh(Double high)
	{
		this.high = high;
	}
	public double getForecastHigh0()
	{
		return forecastHigh0;
	}
	public void setForecastHigh0(double forecastHigh0)
	{
		this.forecastHigh0 = forecastHigh0;
	}
	public double getForecastHigh1()
	{
		return forecastHigh1;
	}
	public void setForecastHigh1(double forecastHigh1)
	{
		this.forecastHigh1 = forecastHigh1;
	}
	public double getForecastHigh2()
	{
		return forecastHigh2;
	}
	public void setForecastHigh2(double forecastHigh2)
	{
		this.forecastHigh2 = forecastHigh2;
	}
	public double getForecastHigh3()
	{
		return forecastHigh3;
	}
	public void setForecastHigh3(double forecastHigh3)
	{
		this.forecastHigh3 = forecastHigh3;
	}
	public double getForecastHigh4()
	{
		return forecastHigh4;
	}
	public void setForecastHigh4(double forecastHigh4)
	{
		this.forecastHigh4 = forecastHigh4;
	}
	public double getForecastHigh5()
	{
		return forecastHigh5;
	}
	public void setForecastHigh5(double forecastHigh5)
	{
		this.forecastHigh5 = forecastHigh5;
	}

}
