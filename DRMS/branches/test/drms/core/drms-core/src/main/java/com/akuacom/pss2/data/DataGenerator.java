package com.akuacom.pss2.data;

import java.util.Date;
import java.util.List;

public interface DataGenerator {

    /**
     * @param date date
     * @return all the entries of the day
     */
    public List<PDataEntry> generate(Date date);

    /**
     * Dates are inclusive.
     *
     * @param startDate start date
     * @param endDate start date
     * @return all the entries in the date range
     */
    public List<PDataEntry> generate(Date startDate, Date endDate);
}
