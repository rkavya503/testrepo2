/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.sc.DailyLog.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

import com.akuacom.utils.lang.DateUtil;
import com.akuacom.utils.lang.TimingUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import scala.Tuple2;

/**
 * Wrapper for a list of a day's power usage at regular intervals 
 *
 */
public class DailyUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The Constant DEFAULT_USAGE_SLOT. */
    public static final int DEFAULT_USAGE_SLOT = 15 * TimingUtil.MINUTE_MS;

    /** The date. */
    private Date date;

    /** The calendar. */
    private Calendar calendar = new GregorianCalendar();

    /** The event day. */
    private boolean eventDay;

    /** The usage slot_msec. */
    private long usageSlot_msec = DEFAULT_USAGE_SLOT;

    /** The holiday. */
    private boolean holiday;

    /** The usages. */
    private List<UsageDataEntry> usages;

    /**
     * Instantiates a new daily log.
     */
    public DailyUsage() {
        usages = new ArrayList<UsageDataEntry>();
    }
    
    /**
     * Instantiates a new daily log.
     */
    public DailyUsage(long usageSlot_msec) {
        this.usageSlot_msec = usageSlot_msec;
        usages = new ArrayList<UsageDataEntry>();
    }

    /**
     * Instantiates a new daily log.
     * 
     * @param date
     *            the date
     * @param slotSize_msec
     *            the slot size_msec
     * @param eventDay
     *            the event day
     */
    public DailyUsage(Date date, long slotSize_msec, boolean eventDay) {
        setDate(date);
        this.usageSlot_msec = slotSize_msec;
        this.eventDay = eventDay;
        usages = new ArrayList<UsageDataEntry>();
        long max = DateUtil.MSEC_IN_DAY + usageSlot_msec;
        for (long i = usageSlot_msec; i < max; i += usageSlot_msec) {
            usages.add(new UsageDataEntry(i, null, this));
        }
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the new date
     */
    public void setDate(Date date) {
        this.date = new Date(date.getTime());
        calendar.setTime(date);
    }

    /**
     * Checks if is event day.
     * 
     * @return true, if is event day
     */
    public boolean isEventDay() {
        return eventDay;
    }

    public boolean isWeekday() {
        int d = calendar.get(Calendar.DAY_OF_WEEK);
        return d != Calendar.SUNDAY && d != Calendar.SATURDAY;
    }

    /**
     * Checks if is holiday.
     * 
     * @return true, if is holiday
     */
    public boolean isHoliday() {
        return holiday;
    }

    /**
     * Sets the holiday.
     * 
     * @param holiday
     *            the new holiday
     */
    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    /**
     * Sets the event day.
     * 
     * @param eventDay
     *            the new event day
     */
    public void setEventDay(boolean eventDay) {
        this.eventDay = eventDay;
    }

    public List<UsageDataEntry> getUsages() {
        return usages;
    }

    /**
     * Gets the usages.
     * 
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the usages
     */
    public List<UsageDataEntry> getUsages(long startTime, long endTime) {
        ArrayList<UsageDataEntry> ulList = new ArrayList<UsageDataEntry>();

        // usages is a sorted list
        for (UsageDataEntry ul : usages) {
            if (ul.getTime().getTime() >= startTime) {
                ulList.add(ul);
                if (ul.getTime().getTime() >= endTime) {
                    break;
                }
            }
        }

        return ulList;
    }

    /**
     * Sets the usages.
     * 
     * @param usages
     *            the new usages
     */
    public void setUsages(List<UsageDataEntry> usages) {
        this.usages = usages;
    }

    /**
     * Gets the percent difference.
     * 
     * @param dl
     *            the dl
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the percent difference
     */
    public double getPercentDifference(DailyUsage dl, long startTime,
            long endTime) {
        return getRatio(dl, startTime, endTime) - 1.;
    }

    /**
     * Gets the ratio.
     * 
     * @param dl
     *            the dl
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the ratio
     */
    public double getRatio(DailyUsage dl, long startTime, long endTime) {
        Tuple2<Double, Double> t = getTotalUsage(this, dl, startTime, endTime);
        return t._1 != 0 ? t._2 / t._1 : 0.0;
    }

    /**
     * Gets the total usage.
     * 
     * @param d1
     *            the d1
     * @param d2
     *            the d2
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the total usage
     */
    public static Tuple2<Double, Double> getTotalUsage(DailyUsage d1,
            DailyUsage d2, long startTime, long endTime) {
        double usage1Total = 0., usage2Total = 0.;

        // Find the start time slot and initiate the total calc
        Iterator<UsageDataEntry> i1 = d1.getUsageLogIterator(startTime);
        Iterator<UsageDataEntry> i2 = d2.getUsageLogIterator(startTime);

        // Add in all the slots up to the end time slot
        while (i1.hasNext() && i2.hasNext()) {
            UsageDataEntry u1 = i1.next();
            UsageDataEntry u2 = i2.next();
            // usage is null means no more data
            final Double usage1 = u1.getValue();
            final Double usage2 = u2.getValue();
            if (usage1 == null || usage2 == null) {
                break;
            }
            usage1Total += usage1;
            usage2Total += usage2;
            if (u1.getMillis() >= endTime && u2.getMillis() >= endTime) {
                break;
            }
        }

        return new Tuple2<Double, Double>(usage1Total, usage2Total);
    }

    /**
     * Gets the total usage.
     * 
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the total usage
     */
    public double getTotalUsage(long startTime, long endTime) {
        double total = 0.0;

        // Find the start time slot and initiate the total calc
        Iterator<UsageDataEntry> i = getUsageLogIterator(startTime);

        // Add in all the slots up to the end time slot
        for (; i != null && i.hasNext();) {
            UsageDataEntry u = i.next();
            final Double usage = u.getValue();
            if (u.getTime().getTime() >= endTime) {
                total += usage == null ? 0 : usage;
                break;
            }
            total += usage == null ? 0 : usage;
        }

        return total;
    }

    /**
     * Gets the avg usage.
     * 
     * @param startTime
     *            the start time
     * @param endTime
     *            the end time
     * 
     * @return the avg usage
     */
    public double getAvgUsage(long startTime, long endTime) {
        double totalUsage = 0;
        int numSlots = 0;
        // Find the start time slot and initiate the total calc
        // Iterator<UsageDataEntry> i = getUsageLogIterator(startTime);
        // Modified by Frank
        Iterator<UsageDataEntry> i = getUsageLogIteratorByStartTime(startTime);
        // Add in all the slots up to the end time slot
        UsageDataEntry u = null;
        while (i!=null && i.hasNext()) {
            u = i.next();
            // Modified by Frank
            final Double usage = u.getValue();
            totalUsage += usage == null ? 0 : usage;
            numSlots++;
            Calendar cal = new GregorianCalendar();
            cal.setTime(u.getTime());
            long curTime = cal.get(Calendar.HOUR_OF_DAY) * TimingUtil.HOUR_MS
                    + cal.get(Calendar.MINUTE) * TimingUtil.MINUTE_MS
                    + cal.get(Calendar.SECOND) * TimingUtil.SECOND_MS;

            if (curTime >= endTime) {
                break;
            }
        }
        return totalUsage / numSlots;
    }

    /**
     * Gets the usage log.
     * 
     * @param time
     *            the time
     * 
     * @return the usage log
     */
    public UsageDataEntry getUsageLog(long time) {
        for (UsageDataEntry ul : usages) {
            if (ul.getTime().getTime() >= time) {
                return ul;
            }
        }

        return null;
    }

    /**
     * Gets the usage log iterator.
     * 
     * @param time
     *            the time
     * 
     * @return the usage log iterator
     */
    public Iterator<UsageDataEntry> getUsageLogIterator(long time) {
        Iterator<UsageDataEntry> i = usages.iterator();
        UsageDataEntry ul = null;
        while (i.hasNext()) {
            ul = i.next();
            if (ul.getTime().getTime() >= time) {
                return i;
            }
        }
        return i;
    }

    /**
     * Gets the usage log idx.
     * 
     * @param time
     *            the time
     * 
     * @return the usage log idx
     */
    public int getUsageLogIdx(long time) {
        for (int i = 0; i < usages.size(); i++) {
            UsageDataEntry ul = usages.get(i);
            if (ul.getTime().getTime() >= time) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Plus usage log.
     * 
     * @param dl
     *            the dl
     */
    public void plusUsageLog(DailyUsage dl) {
        plusUsageLog(dl.getUsages());
    }

    /**
     * Plus usage log.
     * 
     * @param u
     *            the u
     */
    public void plusUsageLog(List<UsageDataEntry> u) {
        Iterator<UsageDataEntry> i = u.iterator();
        Iterator<UsageDataEntry> j = usages.iterator();
        while (i.hasNext() && j.hasNext()) {
            UsageDataEntry ul = j.next();
            UsageDataEntry uli = i.next();
            ul.addUsage(uli.getValue());
            ul.setDatasource(uli.getDatasource());
            ul.setDataSet(uli.getDataSet());
            ul.setTime(uli.getTime());
            ul.setValue(ul.getValue());
        }
    }

    /**
     * Plus usage log.
     * 
     * @param factor
     *            the factor
     */
    public void plusUsageLog(double factor) {
        Iterator<UsageDataEntry> j = usages.iterator();
        while (j.hasNext()) {
            j.next().addUsage(factor);
        }
    }

    /**
     * Mult usage log.
     * 
     * @param factor
     *            the factor
     */
    public void multUsageLog(double factor) {
        Iterator<UsageDataEntry> j = usages.iterator();
        while (j.hasNext()) {
            UsageDataEntry ul = j.next();
            ul.multUsage(factor);
            ul.setValue(ul.getValue());
        }
    }

    public long getUsageSlot_msec() {
        return usageSlot_msec;
    }

    /**
     * Sets the usage slot_msec.
     * 
     * @param usageSlot_msec
     *            the new usage slot_msec
     */
    public void setUsageSlot_msec(long usageSlot_msec) {
        this.usageSlot_msec = usageSlot_msec;
    }

    /**
     * Gets the usage log iterator.
     * 
     * @param time
     *            the time h:mm:ss
     * 
     * @return the usage log iterator
     */
    private Iterator<UsageDataEntry> getUsageLogIteratorByStartTime(long time) {
        Iterator<UsageDataEntry> prev = usages.iterator();
        if (usages.size() == 0) {
            return prev;
        }

        Iterator<UsageDataEntry> iterator = usages.iterator();
        for (; iterator.hasNext(); prev.next()) {
            UsageDataEntry ul = iterator.next();
            Calendar cal = new GregorianCalendar();
            cal.setTime(ul.getTime());
            long curTime = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000
                    + cal.get(Calendar.MINUTE) * 60 * 1000
                    + cal.get(Calendar.SECOND) * 1000;
            if (curTime >= time) {
                return prev;
            }
        }
        
        return null;
    }

}