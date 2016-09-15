/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.signal.SignalDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.signal;

import com.akuacom.pss2.web.util.OptionUtil;
import com.akuacom.utils.Tag;

import org.apache.struts.action.ActionForm;

import java.util.List;
import java.util.Map;

/**
 * The Class SignalDetailForm.
 */
public class SignalDetailForm extends ActionForm {
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The participant name. */
    private String participantName;
    
    /** The signal. */
    private String signal;
    
    /** The level. */
    private String level;
    
    /** The price. */
    private String price;
    
    /** The date. */
    private String date;
    
    /** The hour. */
    private String hour;
    
    /** The min. */
    private String min;
    
    /** The sec. */
    private String sec;

    /** The hour list. */
    private final List hourList = OptionUtil.getHoursList();
    
    /** The min list. */
    private final List minList = OptionUtil.getMinSecList();
    
    /** The sec list. */
    private final List secList = OptionUtil.getMinSecList();

    /** The signals. */
    private List<Tag> signals;
    
    /** The signal level map. */
    private Map<String, String[]> signalLevelMap;

    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     * 
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the participant name.
     * 
     * @return the participant name
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the participant name.
     * 
     * @param participantName the new participant name
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the level.
     * 
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the level.
     * 
     * @param level the new level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Gets the signal.
     * 
     * @return the signal
     */
    public String getSignal() {
        return signal;
    }

    /**
     * Sets the signal.
     * 
     * @param signal the new signal
     */
    public void setSignal(String signal) {
        this.signal = signal;
    }

    /**
     * Gets the signal level map.
     * 
     * @return the signal level map
     */
    public Map<String, String[]> getSignalLevelMap() {
        return signalLevelMap;
    }

    /**
     * Sets the signal level map.
     * 
     * @param signalLevelMap the signal level map
     */
    public void setSignalLevelMap(Map<String, String[]> signalLevelMap) {
        this.signalLevelMap = signalLevelMap;
    }

    /**
     * Gets the signals.
     * 
     * @return the signals
     */
    public List<Tag> getSignals() {
        return signals;
    }

    /**
     * Sets the signals.
     * 
     * @param signals the new signals
     */
    public void setSignals(List<Tag> signals) {
        this.signals = signals;
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date.
     * 
     * @param date the new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the hour.
     * 
     * @return the hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * Sets the hour.
     * 
     * @param hour the new hour
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * Gets the min.
     * 
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * Sets the min.
     * 
     * @param min the new min
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * Gets the sec.
     * 
     * @return the sec
     */
    public String getSec() {
        return sec;
    }

    /**
     * Sets the sec.
     * 
     * @param sec the new sec
     */
    public void setSec(String sec) {
        this.sec = sec;
    }

    /**
     * Gets the hour list.
     * 
     * @return the hour list
     */
    public List getHourList() {
        return hourList;
    }

    /**
     * Gets the min list.
     * 
     * @return the min list
     */
    public List getMinList() {
        return minList;
    }

    /**
     * Gets the sec list.
     * 
     * @return the sec list
     */
    public List getSecList() {
        return secList;
    }

    /**
     * Gets the price.
     * 
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the price.
     * 
     * @param price the new price
     */
    public void setPrice(String price) {
        this.price = price;
    }
}
