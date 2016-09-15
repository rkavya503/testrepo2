/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.Forecast.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

/**
 * The Class Forecast.
 */
public class Forecast
{
	
	/** The program name. */
	private String programName;
	
	/** The today. */
	private String today;
	
	/** The tomorrow. */
	private String tomorrow;
	
	/** The third. */
	private String third;
	
	/** The third header. */
	private String thirdHeader = "Wednesday";
	
	/** The fourth. */
	private String fourth;
	
	/** The fourth header. */
	private String fourthHeader = "Thursday";
	
	/** The fifth. */
	private String fifth;
	
	/** The fifth header. */
	private String fifthHeader = "Friday";
	
	/**
	 * Instantiates a new forecast.
	 * 
	 * @param programName the program name
	 * @param today the today
	 * @param tomorrow the tomorrow
	 * @param third the third
	 * @param fourth the fourth
	 * @param fifth the fifth
	 */
	public Forecast(String programName, String today, String tomorrow,
		String third, String fourth, String fifth)
	{
		super();
		this.programName = programName;
		this.today = today;
		this.tomorrow = tomorrow;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
	}
	
	/**
	 * Gets the program name.
	 * 
	 * @return the program name
	 */
	public String getProgramName()
	{
		return programName;
	}
	
	/**
	 * Sets the program name.
	 * 
	 * @param programName the new program name
	 */
	public void setProgramName(String programName)
	{
		this.programName = programName;
	}
	
	/**
	 * Gets the today.
	 * 
	 * @return the today
	 */
	public String getToday()
	{
		return today;
	}
	
	/**
	 * Sets the today.
	 * 
	 * @param today the new today
	 */
	public void setToday(String today)
	{
		this.today = today;
	}
	
	/**
	 * Gets the tomorrow.
	 * 
	 * @return the tomorrow
	 */
	public String getTomorrow()
	{
		return tomorrow;
	}
	
	/**
	 * Sets the tomorrow.
	 * 
	 * @param tomorrow the new tomorrow
	 */
	public void setTomorrow(String tomorrow)
	{
		this.tomorrow = tomorrow;
	}
	
	/**
	 * Gets the third.
	 * 
	 * @return the third
	 */
	public String getThird()
	{
		return third;
	}
	
	/**
	 * Sets the third.
	 * 
	 * @param third the new third
	 */
	public void setThird(String third)
	{
		this.third = third;
	}
	
	/**
	 * Gets the fourth.
	 * 
	 * @return the fourth
	 */
	public String getFourth()
	{
		return fourth;
	}
	
	/**
	 * Sets the fourth.
	 * 
	 * @param fourth the new fourth
	 */
	public void setFourth(String fourth)
	{
		this.fourth = fourth;
	}
	
	/**
	 * Gets the fifth.
	 * 
	 * @return the fifth
	 */
	public String getFifth()
	{
		return fifth;
	}
	
	/**
	 * Sets the fifth.
	 * 
	 * @param fifth the new fifth
	 */
	public void setFifth(String fifth)
	{
		this.fifth = fifth;
	}

	/**
	 * Gets the third header.
	 * 
	 * @return the third header
	 */
	public String getThirdHeader()
	{
		return thirdHeader;
	}

	/**
	 * Sets the third header.
	 * 
	 * @param thirdHeader the new third header
	 */
	public void setThirdHeader(String thirdHeader)
	{
		this.thirdHeader = thirdHeader;
	}

	/**
	 * Gets the fourth header.
	 * 
	 * @return the fourth header
	 */
	public String getFourthHeader()
	{
		return fourthHeader;
	}

	/**
	 * Sets the fourth header.
	 * 
	 * @param fourthHeader the new fourth header
	 */
	public void setFourthHeader(String fourthHeader)
	{
		this.fourthHeader = fourthHeader;
	}

	/**
	 * Gets the fifth header.
	 * 
	 * @return the fifth header
	 */
	public String getFifthHeader()
	{
		return fifthHeader;
	}

	/**
	 * Sets the fifth header.
	 * 
	 * @param fifthHeader the new fifth header
	 */
	public void setFifthHeader(String fifthHeader)
	{
		this.fifthHeader = fifthHeader;
	}

}
