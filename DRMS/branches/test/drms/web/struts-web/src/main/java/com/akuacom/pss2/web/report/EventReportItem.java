package com.akuacom.pss2.web.report;

import com.akuacom.pss2.report.entities.Event;

import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class EventReportItem extends Event
{
    private Double estimatedAveShedDuringEvent;
    private Double aveShedDuringEvent;
    private Double totalUsage;

    private String userName = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getEstimatedAveShedDuringEvent() {
        return estimatedAveShedDuringEvent;
    }

    public void setEstimatedAveShedDuringEvent(Double estimatedAveShedDuringEvent) {
        this.estimatedAveShedDuringEvent = roundTwoDecimals(estimatedAveShedDuringEvent);
    }

    public Double getAveShedDuringEvent() {
        return aveShedDuringEvent;
    }

    public void setAveShedDuringEvent(Double aveShedDuringEvent) {
        this.aveShedDuringEvent = roundTwoDecimals(aveShedDuringEvent);
    }

    public Double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(Double totalUsage) {
        this.totalUsage = roundTwoDecimals(totalUsage);
    }

    double roundTwoDecimals(double d) {
        	DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
    }
}
