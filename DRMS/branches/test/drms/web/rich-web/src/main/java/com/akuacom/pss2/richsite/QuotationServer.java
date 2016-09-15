package com.akuacom.pss2.richsite;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import com.akuacom.pss2.system.QodService;

public class QuotationServer {
    static Logger log = Logger.getLogger(QuotationServer.class); 
    @EJB
    QodService.L qodService;

    public QuotationServer() {}

    public String getNextQodHtml() {
        log.info("here qodService " + qodService);
       // log.info("have " + qodService.count() + " qods");
        return qodService.nextQodHtml();
    }

}
