package com.akuacom.pss2.program.scertp.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.utils.lang.DateUtil;

@Stateless
public class WeatherEAOBean extends BaseEAOBean<Weather> implements WeatherEAO.R,
        WeatherEAO.L {

    public WeatherEAOBean() {
        super(Weather.class);
    }

    public Weather getWeatherByDate(Date date) {
        final Query namedQuery = em.createNamedQuery(
                "Weather.findByNameAndDate").setParameter("date",
                DateUtil.stripTime(date));
        final List<Weather> list = namedQuery.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Weather> getWeatherByDateRange(Date start, Date end) {
        final Query namedQuery = em
                .createNamedQuery("Weather.findByNameAndDateRange")
                .setParameter("start", DateUtil.stripTime(start))
                .setParameter("end", DateUtil.stripTime(end));
        final List<Weather> list = namedQuery.getResultList();
        final ArrayList<Weather> results = new ArrayList<Weather>();
        for (Object o : list) {
            Weather weather = (Weather) o;
            results.add(weather);
        }
        return results;
    }

}
