package com.akuacom.pss2.program.scertp.entities;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;

public interface WeatherEAO extends BaseEAO<Weather> {
    @Remote
    public interface R extends WeatherEAO {}

    @Local
    public interface L extends WeatherEAO {}

    Weather getWeatherByDate(Date date);

    List<Weather> getWeatherByDateRange(Date start, Date end);

}
