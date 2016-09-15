package com.akuacom.pss2.history.vo;
import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.data.PDataEntry;

public class UsageDataTransferVo implements Serializable {
	
	private static final long serialVersionUID = 6359480248705012545L;
	
	private List<PDataEntry> baselineList;
	private List<PDataEntry> usagelineList;
	private List<PDataEntry> baselineEventList;
	private List<PDataEntry> usagelineEventList;
	private List<EventVo> eventPeriodList;
	private double dayAvgBase;
	private double dayTotalBase;
	private double dayAvgActual;
	private double dayTotalActual;
	private double dayAvgShed;
	private double dayTotalShed;
	private double eventAvgBase;
	private double eventTotalBase;
	private double eventAvgActual;
	private double eventTotalActual;
	private double eventAvgShed;
	private double eventTotalShed;
	private double maxValue;
	
	public List<PDataEntry> getBaselineList() {
		return baselineList;
	}
	public List<PDataEntry> getUsagelineList() {
		return usagelineList;
	}
	public List<PDataEntry> getBaselineEventList() {
		return baselineEventList;
	}
	public List<PDataEntry> getUsagelineEventList() {
		return usagelineEventList;
	}
	public double getDayAvgBase() {
		return dayAvgBase;
	}
	public double getDayTotalBase() {
		return dayTotalBase;
	}
	public double getDayAvgActual() {
		return dayAvgActual;
	}
	public double getDayTotalActual() {
		return dayTotalActual;
	}
	public double getDayAvgShed() {
		return dayAvgShed;
	}
	public double getDayTotalShed() {
		return dayTotalShed;
	}
	public double getEventAvgBase() {
		return eventAvgBase;
	}
	public double getEventTotalBase() {
		return eventTotalBase;
	}
	public double getEventAvgActual() {
		return eventAvgActual;
	}
	public double getEventTotalActual() {
		return eventTotalActual;
	}
	public double getEventAvgShed() {
		return eventAvgShed;
	}
	public double getEventTotalShed() {
		return eventTotalShed;
	}
	public void setBaselineList(List<PDataEntry> baselineList) {
		this.baselineList = baselineList;
	}
	public void setUsagelineList(List<PDataEntry> usagelineList) {
		this.usagelineList = usagelineList;
	}
	public void setBaselineEventList(List<PDataEntry> baselineEventList) {
		this.baselineEventList = baselineEventList;
	}
	public void setUsagelineEventList(List<PDataEntry> usagelineEventList) {
		this.usagelineEventList = usagelineEventList;
	}
	public void setDayAvgBase(double dayAvgBase) {
		this.dayAvgBase = dayAvgBase;
	}
	public void setDayTotalBase(double dayTotalBase) {
		this.dayTotalBase = dayTotalBase;
	}
	public void setDayAvgActual(double dayAvgActual) {
		this.dayAvgActual = dayAvgActual;
	}
	public void setDayTotalActual(double dayTotalActual) {
		this.dayTotalActual = dayTotalActual;
	}
	public void setDayAvgShed(double dayAvgShed) {
		this.dayAvgShed = dayAvgShed;
	}
	public void setDayTotalShed(double dayTotalShed) {
		this.dayTotalShed = dayTotalShed;
	}
	public void setEventAvgBase(double eventAvgBase) {
		this.eventAvgBase = eventAvgBase;
	}
	public void setEventTotalBase(double eventTotalBase) {
		this.eventTotalBase = eventTotalBase;
	}
	public void setEventAvgActual(double eventAvgActual) {
		this.eventAvgActual = eventAvgActual;
	}
	public void setEventTotalActual(double eventTotalActual) {
		this.eventTotalActual = eventTotalActual;
	}
	public void setEventAvgShed(double eventAvgShed) {
		this.eventAvgShed = eventAvgShed;
	}
	public void setEventTotalShed(double eventTotalShed) {
		this.eventTotalShed = eventTotalShed;
	}
	public List<EventVo> getEventPeriodList() {
		return eventPeriodList;
	}
	public void setEventPeriodList(List<EventVo> eventPeriodList) {
		this.eventPeriodList = eventPeriodList;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

}
