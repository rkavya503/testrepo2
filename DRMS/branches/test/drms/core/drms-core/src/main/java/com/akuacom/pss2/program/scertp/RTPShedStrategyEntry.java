/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.RTPShedStrategyEntry.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.scertp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class RTPShedStrategyEntry.
 */
public class RTPShedStrategyEntry implements Serializable
{
	private static final long serialVersionUID = 1L;

    /** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The moderate price. */
	private String moderatePrice;
	
	/** The high price. */
	private String highPrice;
	
	private String value0;
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;
	private String value6;
	private String value7;
	private String value8;
	private Boolean selected;
	private String style;


	/**
	 * Gets the time bock string.
	 * 
	 * @return the time bock string
	 */
	public String getTimeBockString()
	{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(startTime) + " - " +
			simpleDateFormat.format(endTime);
	}
	
	/**
	 * Gets the start time.
	 * 
	 * @return the start time
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * Sets the start time.
	 * 
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 * 
	 * @return the end time
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * Sets the end time.
	 * 
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * Gets the moderate price.
	 * 
	 * @return the moderate price
	 */
	public String getModeratePrice()
	{
		return moderatePrice;
	}
	
	/**
	 * Sets the moderate price.
	 * 
	 * @param moderatePrice the new moderate price
	 */
	public void setModeratePrice(String moderatePrice)
	{
		this.moderatePrice = moderatePrice;
	}
	
	/**
	 * Gets the high price.
	 * 
	 * @return the high price
	 */
	public String getHighPrice()
	{
		return highPrice;
	}
	
	/**
	 * Sets the high price.
	 * 
	 * @param highPrice the new high price
	 */
	public void setHighPrice(String highPrice)
	{
		this.highPrice = highPrice;
	}

	public String getValue0() {
		return value0;
	}

	public void setValue0(String value0) {
		this.value0 = value0;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getStyle() {
		return (selected==null||!selected)?"":"copySelected";
	}

	
}
