package com.akuacom.pss2.richsite;

import java.io.Serializable;

public class LogWatcherBridge implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private LogWatcherServlet logWatcherServlet;
    
    public LogWatcherServlet getLogWatcherServlet() {
        return logWatcherServlet;
    }

    public void setLogWatcherServlet(LogWatcherServlet logWatcherServlet) {
        this.logWatcherServlet = logWatcherServlet;
    }

    public LogWatcherBridge() {
        logWatcherServlet = LogWatcherServlet.getInstance();
    }

}
