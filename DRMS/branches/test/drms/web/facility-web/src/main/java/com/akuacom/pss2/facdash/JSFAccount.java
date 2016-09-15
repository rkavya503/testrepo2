/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFAccount.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.subaccount.SubAccount;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class JSFAccount.
 */
public class JSFAccount implements Comparable<JSFAccount>, Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The name. */
	private String name;

	private String premise;
	
	/** The enrollment date. */
	private Date enrollmentDate;
	
	/** The start date. */
	private Date startDate;
	
	/** The deactive date. */
	private Date deactiveDate;
	
	/** The comment. */
	private String comment;
		
	/** The available names. */
	private List<SelectItem> availableNames;
	
	private boolean selected;
	private boolean edit;

	/**
	 * Instantiates a new jSF account.
	 */
	public JSFAccount()
	{
	}

	/**
	 * Instantiates a new jSF account.
	 * 
	 * @param account the account
	 */
	public JSFAccount(SubAccount account)
	{
		this();
		this.name = account.getSubAccountId();
		this.premise = account.getPremiseNumber();
		this.comment = account.getComment();
		this.enrollmentDate = account.getEnrollmentDate();
		this.startDate = account.getStartDate();
		this.deactiveDate = account.getEndDate();
	}

	// for comparison purposes only
	/**
	 * Instantiates a new jSF account.
	 * 
	 * @param name the name
	 */
	public JSFAccount(String name)
	{
		this.name = name;
	}

	/**
	 * Parses the account.
	 * 
	 * @return the sCE account info
	 */
	public SubAccount parseAccount()
	{
		SubAccount account = new SubAccount();
		account.setSubAccountId(name);
		account.setPremiseNumber(premise);
		account.setComment(comment);
		account.setEnrollmentDate(enrollmentDate);
		account.setStartDate(startDate);
		account.setEndDate(deactiveDate);
		
		return account;
	}
	
	
	/**
	 * Cancel action.
	 * 
	 * @return the string
	 */
	public String cancelAction()
	{
		return "cancel";
	}
	

	public String getActive()
	{
		Date end = DateUtil.endOfDay(new Date());
		
		if (enrollmentDate == null) {
			return "";
		}
		
		if (enrollmentDate != null && (startDate == null || startDate.after(end))) {
			return "Pending";
		}
		
		if (deactiveDate != null && deactiveDate.before(end)) {
			return "Inactive";
		}
		
		return "Active";
	}
	
	/**
	 * Checks if is delete.
	 * 
	 * @return true, if is delete
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Sets the delete.
	 * 
	 * @param delete the new delete
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	/**
	 * Checks if is edits the.
	 * 
	 * @return true, if is edits the
	 */
	public boolean isEdit()
	{
		return edit;
	}

	/**
	 * Sets the edits the.
	 * 
	 * @param edit the new edits the
	 */
	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the start date.
	 * 
	 * @return the start date
	 */
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param startDate the new start date
	 */
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	/**
	 * Gets the deactive date.
	 * 
	 * @return the deactive date
	 */
	public Date getDeactiveDate()
	{
		return deactiveDate;
	}

	/**
	 * Sets the deactive date.
	 * 
	 * @param deactiveDate the new deactive date
	 */
	public void setDeactiveDate(Date deactiveDate)
	{
		this.deactiveDate = deactiveDate;
	}

	/**
	 * Gets the comment.
	 * 
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Sets the comment.
	 * 
	 * @param comment the new comment
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Gets the available names.
	 * 
	 * @return the available names
	 */
	public List<SelectItem> getAvailableNames()
	{
		return availableNames;
	}

	/**
	 * Sets the available names.
	 * 
	 * @param availableNames the new available names
	 */
	public void setAvailableNames(List<SelectItem> availableNames)
	{
		this.availableNames = availableNames;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(JSFAccount other)
	{
		return name.compareTo(other.name);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JSFAccount other = (JSFAccount) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getPremise()
	{
		return premise;
	}

	public void setPremise(String premise)
	{
		this.premise = premise;
	}
}
