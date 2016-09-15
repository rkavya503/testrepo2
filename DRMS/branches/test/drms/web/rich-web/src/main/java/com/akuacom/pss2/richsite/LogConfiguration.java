package com.akuacom.pss2.richsite;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.ejb.util.SelectItemUtil;
import com.akuacom.utils.config.RuntimeSwitches;
import com.akuacom.utils.lang.ClassUtil;
import com.akuacom.utils.lang.FileUtil;

public class LogConfiguration implements Serializable {
    Logger log = Logger.getLogger(LogConfiguration.class);

    private static final long serialVersionUID = 1L;

    public static enum Frequency {
        Daily, Weekly, BiWeekly, Monthly, Never
    }

    public static enum Dispatch {
        Copy, ZipCopy, Delete
    }

    private String dispatchDirectory = File.separator + "tmp";
    private String prefix = "drms-log";
    private boolean includeDate = true;
    private String dateFormat = "yyyy_MM_dd_HH_mm_ss_SSS";
    private final static SelectItem[] frequencies = SelectItemUtil
            .asSelectItems(Frequency.class);
    private final static SelectItem[] dispatches = SelectItemUtil
            .asSelectItems(Dispatch.class);
    private final static SelectItem[] headroomRange = SelectItemUtil
            .asSelectItems(10, 50);
    private Frequency currentFrequency = Frequency.BiWeekly;
    private Dispatch currentDispatch = Dispatch.ZipCopy;
    private int currentHeadroomPct = 20;
    
    private boolean aggressivePruning = false;
    
    private boolean trackingMemory = false;

//    private Set<Tuple2<String, Double>> headrooms = new HashSet<Tuple2<String, Double>>();

    public LogConfiguration() {
    }

    public String getDispatchDirectory() {
        return dispatchDirectory;
    }

    public void setDispatchDirectory(String dispatchDirectory) {
        this.dispatchDirectory = dispatchDirectory;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isIncludeDate() {
        return includeDate;
    }

    public void setIncludeDate(boolean includeDate) {
        this.includeDate = includeDate;
    }

    public boolean isAggressivePruning() {
        return aggressivePruning;
    }

    public void setAggressivePruning(boolean aggressivePruning) {
        this.aggressivePruning = aggressivePruning;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getCurrentFrequency() {
        return currentFrequency.ordinal();
    }

    public void setCurrentFrequency(int currentFrequency) {
        this.currentFrequency = Frequency.values()[currentFrequency];
    }

    public int getCurrentDispatch() {
        return currentDispatch.ordinal();
    }

    public void setCurrentDispatch(int currentDispatch) {
        this.currentDispatch = Dispatch.values()[currentDispatch];
    }

    public int getCurrentHeadroomPct() {
        return currentHeadroomPct;
    }

    public void setCurrentHeadroomPct(int currentHeadroomPct) {
        this.currentHeadroomPct = currentHeadroomPct;
    }

    public SelectItem[] getFrequencies() {
        return frequencies;
    }

    public SelectItem[] getDispatches() {
        return dispatches;
    }

    public SelectItem[] getHeadroomRange() {
        return headroomRange;
    }

    public boolean isTrackingMemory() {
        return trackingMemory;
    }

    public void setTrackingMemory(boolean trackingMemory) {
        this.trackingMemory = trackingMemory;
    }

    @Override
    public String toString() {
        return currentFrequency + " :: " + currentDispatch + " :: "
                + +currentHeadroomPct + " :: " + getRemnantFile() ;
    }


    public void setHeadroomStr() {
    }

    public String update() {
        try {
            LogWatcherServlet.getInstance().updateLogConfiguration(
                    ClassUtil.toMap(this));
            log.debug("update!!!! " + this);
            RuntimeSwitches.AGGRESSIVE_PRUNING = aggressivePruning;
            return "success";
        } catch (Exception e) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(e.toString()));
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getMap() {
        try {
            return ClassUtil.toMap(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.<String, Object> emptyMap();
    }

    public void setMap(Map<String, Object> map) {
        try {
            if(map != null) {
                ClassUtil.fromMap(this, map);
            } else {
                log.warn("map was null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String path() {
        return FileUtil.mkPath(dispatchDirectory, prefix
                + (includeDate ? dateStr() : ""));
    }

    private String dateStr() {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public String getRemnantFile() {
        return path() + ".remnant";
    }

}
