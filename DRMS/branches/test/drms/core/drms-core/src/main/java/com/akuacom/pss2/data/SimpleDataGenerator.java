package com.akuacom.pss2.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.akuacom.utils.lang.DateUtil;

public class SimpleDataGenerator implements DataGenerator {
    private static Logger log = Logger.getLogger(SimpleDataGenerator.class);

    private PDataSet dataSet;
    private PDataSource dataSource;
    private Double baseValue;
    private Double peakIncrementMin;
    private Double peakIncrementMax;
    private Double peakIncrement;
    private Double peakReduction = 0d;   // in percentage 15% will be 15.0
    private Integer reductionStartHour;
    private Integer reductionEndHour;
    private long intervalInMillis = 15 * 60 * 1000;
    private static final int MILLI_SECONDS_OF_HOUR = 60 * 60 * 1000;
    private static final int MILLI_SECONDS_OF_DAY = 24 * MILLI_SECONDS_OF_HOUR;

    public Double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }

    public Double getPeakIncrement() {
        return peakIncrement;
    }

    public void setPeakIncrement(Double peakIncrement) {
        this.peakIncrement = peakIncrement;
    }

    public Double getPeakIncrementMin() {
        return peakIncrementMin;
    }

    public void setPeakIncrementMin(Double peakIncrementMin) {
        this.peakIncrementMin = peakIncrementMin;
    }

    public Double getPeakIncrementMax() {
        return peakIncrementMax;
    }

    public void setPeakIncrementMax(Double peakIncrementMax) {
        this.peakIncrementMax = peakIncrementMax;
    }

    public PDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(PDataSet dataSet) {
        this.dataSet = dataSet;
    }

    public PDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Double getPeakReduction() {
        return peakReduction;
    }

    public void setPeakReduction(Double peakReduction) {
        this.peakReduction = peakReduction;
    }

    public Integer getReductionEndHour() {
        return reductionEndHour;
    }

    public void setReductionEndHour(Integer reductionEndHour) {
        this.reductionEndHour = reductionEndHour;
    }

    public Integer getReductionStartHour() {
        return reductionStartHour;
    }

    public void setReductionStartHour(Integer reductionStartHour) {
        this.reductionStartHour = reductionStartHour;
    }

    public long getIntervalInMillis() {
        return intervalInMillis;
    }

    public void setIntervalInMillis(long intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    @Override
    public List<PDataEntry> generate(Date date) {
        final double diff = getPeakIncrementMax() - getPeakIncrementMin();
        setPeakIncrement(getPeakIncrementMin() + diff * Math.random());
        log.debug("baseValue: " + baseValue + ", peakIncrement: " + peakIncrement);

        final int slots = (int) (MILLI_SECONDS_OF_DAY / intervalInMillis);
        final List<PDataEntry> set = new ArrayList<PDataEntry>(slots);
        for (int i = 0; i < slots; i++) {
            final PDataEntry entry = new PDataEntry();
            entry.setDatasource(dataSource);
            entry.setDataSet(dataSet);
            entry.setTime(getTime(date, i));
            entry.setValue(getValue(i));
            set.add(entry);
        }
        return set;
    }

    private Date getTime(Date date, int i) {
        final Date start = DateUtil.stripTime(date);
        final long l = start.getTime() + i * intervalInMillis;
        return new Date(l);
    }

    private Double getValue(int i) {
        final int diff = 15 * MILLI_SECONDS_OF_HOUR / (int) intervalInMillis - i;
        double v;
        if (Math.abs(diff) <= 6 * MILLI_SECONDS_OF_HOUR / (int) intervalInMillis) { // draw a cosine curve from 9am to 9pm ranging from -pi/2 to pi/2
            v = baseValue + peakIncrement * Math.cos(diff * Math.PI / (12 * MILLI_SECONDS_OF_HOUR / (int) intervalInMillis));
        } else {
            v = baseValue;
        }

        // if in the reduction period, reduce amount by peak reduction
        if (reductionStartHour * MILLI_SECONDS_OF_HOUR < intervalInMillis * i &&
                intervalInMillis * i < reductionEndHour * MILLI_SECONDS_OF_HOUR) {
            v *= (100 - peakReduction) / 100;
        }

        v *= (.9 + .2 * Math.random()); // rand through 90-110%
        return v;
    }

    @Override
    public List<PDataEntry> generate(Date startDate, Date endDate) {
        startDate = DateUtil.stripTime(startDate);
        endDate = DateUtil.stripTime(endDate);
        final Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        final Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        log.debug("Start: " + startDate + ", End: " + endDate);

        final ArrayList<PDataEntry> results = new ArrayList<PDataEntry>();
        for (; !cal.after(end); cal.add(Calendar.DATE, 1)) {
            results.addAll(generate(cal.getTime()));
        }
        return results;
    }
}
