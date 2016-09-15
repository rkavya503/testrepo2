package com.akuacom.pss2.richsite;

import java.io.Serializable;
import java.util.Date;

import com.akuacom.utils.JsUtil;
import com.akuacom.utils.lang.DateUtil;

/**
 * wrapper for Event class for Op-dash prototype
 * TODO merge with Event when prototype stabilizes
 *
 */
public class OdEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    final private static String DIV_EVENT_GAUGE ="divEventGauge";
    private long day;
    private float begin;
    private float near;
    private float start;
    private float stop;
    private float end;
    private String programName;
    private long counter;
    private static long Counter = 0;  // to support multiple widgets per page
    
    public OdEvent() {
        counter = Counter++;
    }
    
    public OdEvent(long day, float start, float stop) {
        this();
        this.day = day;
        this.start=start;
        this.stop=stop;
    }
    
    public OdEvent( long day, float begin, float near, float start, float stop, float end,
            String programName) {
        this(day, start,stop);
        this.begin = begin;
        this.near = near;
        this.end = end;
        this.programName = programName;
    }
    
    public long getDay() {
        return day;
    }
    
    public void setDay(long day) {
        this.day = day;
    }

    public float getStart() {
        return start;
    }
    public void setStart(float start) {
        this.start = start;
    }
    public float getStop() {
        return stop;
    }
    public void setStop(float stop) {
        this.stop = stop;
    }
    
    public String getGaugeDiv() {
        return "<div id=\"" + DIV_EVENT_GAUGE + JsUtil.asJSVar(programName) + counter +"\" style=\"width: 120px; height: 60px\"/>";   
    }
    
    public String getGaugeScript() {
        return "<script>akuacom.widgets.drawEventGauge({id:\""  + DIV_EVENT_GAUGE + JsUtil.asJSVar(programName)+counter + "\",w:120, begin:" + begin + ", near:" + near 
        + ",start:" + start + ",stop:" + stop + ",end:" + end +", nstyle:[\"gradient\",\"arrow\"]})</script>"; 
    }
    
    public String getDate() {
        return DateUtil.shortDateFormatter().format(new Date(day));
    }

}
