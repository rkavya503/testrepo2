/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.IssueEventDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event;

/**
 * The Class IssueEventDetailForm.
 */
public class IssueEventDetailForm extends EventDetailForm {
    
    /** The issue date. */
    private String issueDate;
    
    /** The issue hour. */
    private String issueHour;
    
    /** The issue min. */
    private String issueMin;
    
    /** The issue sec. */
    private String issueSec;
    
	/**
	 * Gets the issue date.
	 * 
	 * @return the issue date
	 */
	public String getIssueDate()
	{
		return issueDate;
	}
	
	/**
	 * Sets the issue date.
	 * 
	 * @param issueDate the new issue date
	 */
	public void setIssueDate(String issueDate)
	{
		this.issueDate = issueDate;
	}
	
	/**
	 * Gets the issue hour.
	 * 
	 * @return the issue hour
	 */
	public String getIssueHour()
	{
		return issueHour;
	}
	
	/**
	 * Sets the issue hour.
	 * 
	 * @param issueHour the new issue hour
	 */
	public void setIssueHour(String issueHour)
	{
		this.issueHour = issueHour;
	}
	
	/**
	 * Gets the issue min.
	 * 
	 * @return the issue min
	 */
	public String getIssueMin()
	{
		return issueMin;
	}
	
	/**
	 * Sets the issue min.
	 * 
	 * @param issueMin the new issue min
	 */
	public void setIssueMin(String issueMin)
	{
		this.issueMin = issueMin;
	}
	
	/**
	 * Gets the issue sec.
	 * 
	 * @return the issue sec
	 */
	public String getIssueSec()
	{
		return issueSec;
	}
	
	/**
	 * Sets the issue sec.
	 * 
	 * @param issueSec the new issue sec
	 */
	public void setIssueSec(String issueSec)
	{
		this.issueSec = issueSec;
	}
    
}
