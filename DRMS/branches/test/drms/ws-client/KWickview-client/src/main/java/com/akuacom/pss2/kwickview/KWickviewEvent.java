/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.kwickview;

import com.schneider_electric.webservices.CurtailmentEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author spierson
 */
public class KWickviewEvent {
    private Date eventStart;
    private Date eventEnd;
    private int eventID;
    private double firmLoad;
    private String loadLevelID;
    private String resultSummary;
    private int typeCode;
    
    public KWickviewEvent(CurtailmentEvent kwEvent) {
        try {
            String sDate  = kwEvent.getEventDate();
            String sStart = kwEvent.getEventStart();
            String sEnd   = kwEvent.getEventEnd();
            
            SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateTimeInstance();
            df.applyPattern("MM/dd/yyyy hh:mm:ss");
            
            this.eventStart = df.parse(sDate+" "+sStart);
            this.eventEnd = df.parse(sDate+" "+sEnd);
            this.eventID = kwEvent.getEventID();
            this.firmLoad = kwEvent.getFirmLoad();
            this.loadLevelID = kwEvent.getLoadLevelID();
            this.resultSummary = kwEvent.getResultSummary();
            this.typeCode = kwEvent.getTypeCode();
        } catch (ParseException ex) {
            Logger.getLogger(KWickviewEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("KWickviewEvent: "+eventStart);
        
        return sb.toString();
    }
    
    
    /**
     * @return the eventStart
     */
    public Date getEventStart() {
        return eventStart;
    }

    /**
     * @param eventStart the eventStart to set
     */
    public void setEventStart(Date eventStart) {
        this.eventStart = eventStart;
    }

    /**
     * @return the eventEnd
     */
    public Date getEventEnd() {
        return eventEnd;
    }

    /**
     * @param eventEnd the eventEnd to set
     */
    public void setEventEnd(Date eventEnd) {
        this.eventEnd = eventEnd;
    }

    /**
     * @return the eventID
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * @param eventID the eventID to set
     */
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    /**
     * @return the firmLoad
     */
    public double getFirmLoad() {
        return firmLoad;
    }

    /**
     * @param firmLoad the firmLoad to set
     */
    public void setFirmLoad(double firmLoad) {
        this.firmLoad = firmLoad;
    }

    /**
     * @return the loadLevelID
     */
    public String getLoadLevelID() {
        return loadLevelID;
    }

    /**
     * @param loadLevelID the loadLevelID to set
     */
    public void setLoadLevelID(String loadLevelID) {
        this.loadLevelID = loadLevelID;
    }

    /**
     * @return the resultSummary
     */
    public String getResultSummary() {
        return resultSummary;
    }

    /**
     * @param resultSummary the resultSummary to set
     */
    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }

    /**
     * @return the typeCode
     */
    public int getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode the typeCode to set
     */
    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }
}
