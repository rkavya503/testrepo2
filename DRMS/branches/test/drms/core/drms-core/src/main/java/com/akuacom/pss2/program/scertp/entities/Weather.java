package com.akuacom.pss2.program.scertp.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@BatchSize(size=50)
@Table(name = "noaa_weather")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "Weather.findByNameAndDate",
            query = "select w from Weather w where w.date = :date",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "Weather.findByNameAndDateRange",
            query = "select w from Weather w where w.date >= :start and w.date <= :end order by w.date",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class Weather extends VersionedEntity {

	private static final long serialVersionUID = 6208621142739099096L;
	public static double roundValue(double value) {
		double result = value * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}

	@Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    private boolean isFinal = false;
    private String reportingStation = null;
    private Double high = null;
    private double forecastHigh0;
    private double forecastHigh1;
    private double forecastHigh2;
    private double forecastHigh3;
    private double forecastHigh4;
    private double forecastHigh5;

    /**
     * Updates the values in this entity from the 
     * values in another entity;
     * 
     * @param newWeather
     */
    public void update(Weather newWeather)
    {
    	date = newWeather.date;
        high = newWeather.high;
        isFinal = newWeather.isFinal;
        reportingStation = newWeather.reportingStation;
    	forecastHigh0 = newWeather.forecastHigh0;
    	forecastHigh1 = newWeather.forecastHigh1;
    	forecastHigh2 = newWeather.forecastHigh2;
    	forecastHigh3 = newWeather.forecastHigh3;
    	forecastHigh4 = newWeather.forecastHigh4;
    	forecastHigh5 = newWeather.forecastHigh5;
    }
    
    public double getDayBeforeForecastHigh(Date forecastDate)
    {
    	double[] forecastHighs = new double[6];
    	forecastHighs[0] = forecastHigh0;
    	forecastHighs[1] = forecastHigh1;
    	forecastHighs[2] = forecastHigh2;
    	forecastHighs[3] = forecastHigh3;
    	forecastHighs[4] = forecastHigh4;
    	forecastHighs[5] = forecastHigh5;
    	
    	Calendar thisCal = new GregorianCalendar(); 
    	thisCal.setTime(date);

    	Calendar forecastCal = new GregorianCalendar(); 
    	forecastCal.setTime(forecastDate);
    	forecastCal.add(Calendar.DAY_OF_YEAR, -1);

    	for(int i = 0; i < forecastHighs.length; i++)
    	{
	    	if(forecastCal.get(Calendar.DAY_OF_YEAR) == 
	    		thisCal.get(Calendar.DAY_OF_YEAR))
	    	{
                if (i == 0) {
                    // might return forecast or actual high
                    return getForecastHigh0();
                }
	    		else {
                    return forecastHighs[i];
                }
	    	}
	    	thisCal.add(Calendar.DAY_OF_YEAR, 1);
    	}
    	return 0.0;
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = roundValue(high);
        if(high != null) {
            this.setForecastHigh0(high);
        }
    }

	public double getForecastHigh1()
	{
		return forecastHigh1;
	}

    /**
     * getForecastHigh0
     * returns either the stored true high temperature for today
     * or the most recent forecast high for today if no high has been set
     */
    public double getForecastHigh0()
    {
        if (high != null) {
            return high;
        }
        return forecastHigh0;
    }

    public void setForecastHigh0(double forecastHigh0)
    {
        this.forecastHigh0 = roundValue(forecastHigh0);
    }

	public void setForecastHigh1(double forecastHigh1)
	{
		this.forecastHigh1 = roundValue(forecastHigh1);
	}

	public double getForecastHigh2()
	{
		return forecastHigh2;
	}

	public void setForecastHigh2(double forecastHigh2)
	{
		this.forecastHigh2 = roundValue(forecastHigh2);
	}

	public double getForecastHigh3()
	{
		return forecastHigh3;
	}

	public void setForecastHigh3(double forecastHigh3)
	{
		this.forecastHigh3 = roundValue(forecastHigh3);
	}

	public double getForecastHigh4()
	{
		return forecastHigh4;
	}

	public void setForecastHigh4(double forecastHigh4)
	{
		this.forecastHigh4 = roundValue(forecastHigh4);
	}

	public double getForecastHigh5()
	{
		return forecastHigh5;
	}

	public void setForecastHigh5(double forecastHigh5)
	{
		this.forecastHigh5 = roundValue(forecastHigh5);
	}
	
	@Override
	public String toString()
	{
        StringBuilder rv = new StringBuilder("Weather: ");
        rv.append("\nid: ").append(getUUID());
        rv.append("\ndate: ").append(date);
        rv.append("\nfinal: ").append(isFinal);
        rv.append("\nreportingStation: ").append(reportingStation);
        rv.append("\nhigh: ").append(high);
        rv.append("\nforecastHigh0: ").append(forecastHigh0);
        rv.append("\nforecastHigh1: ").append(forecastHigh1);
        rv.append("\nforecastHigh2: ").append(forecastHigh2);
        rv.append("\nforecastHigh3: ").append(forecastHigh3);
        rv.append("\nforecastHigh4: ").append(forecastHigh4);
        rv.append("\nforecastHigh5: ").append(forecastHigh5);
        return rv.toString();		
	}

    /**
     * @return the isFinal
     */
    public boolean isIsFinal() {
        return isFinal;
    }

    /**
     * @param isFinal the isFinal to set
     */
    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    /**
     * @return the reportingStation
     */
    public String getReportingStation() {
        return reportingStation;
    }

    /**
     * @param reportingStation the reportingStation to set
     */
    public void setReportingStation(String reportingStation) {
        this.reportingStation = reportingStation;
    }
}
