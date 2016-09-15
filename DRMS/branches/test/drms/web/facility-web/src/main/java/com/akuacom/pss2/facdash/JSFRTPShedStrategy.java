/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.RTPShedStrategy.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.akuacom.pss2.program.scertp.RTPShedStrategy;
import com.akuacom.pss2.program.scertp.RTPShedStrategyEntry;
import com.akuacom.pss2.program.scertp.RTPShedStrategy.Type;
import java.io.Serializable;

/**
 * The Class RTPShedStrategy.
 */
public class JSFRTPShedStrategy  implements Serializable {
	private RTPShedStrategy strategy;
	
	/** The entries. */
	private RTPShedStrategyEntry[] entries;
		
	/** The edit. */
	private boolean edit;
	
	/** The delete. */
	private boolean delete;
	
	/** The copy moderate price. */
	private String copyModeratePrice;
	
	/** The copy high price. */
	private String copyHighPrice;
	
	/** The time block string. */
	private String timeBlockString;
		
	/**
	 * Instantiates a new rTP shed strategy.
	 */
	public JSFRTPShedStrategy()
	{
		strategy = new RTPShedStrategy();
		entries = strategy.getSimpleEntries();
	}
	
	
	/**
	 * Instantiates a new rTP shed strategy.
	 * 
	 * @param name the name
	 */
	public JSFRTPShedStrategy(String name)
	{
		strategy = new RTPShedStrategy(name);
	}

	/**
	 * Instantiates a new rTP shed strategy.
	 * 
	 * @param name the name
	 */
	public JSFRTPShedStrategy(RTPShedStrategy strategy)
	{
		this.strategy = strategy;	
		if(strategy.getType() == RTPShedStrategy.Type.SIMPLE)
		{
			entries = strategy.getSimpleEntries();
		}
		else
		{
			entries = strategy.getAdvancedEntries();			
		}
	}
	
	/**
	 * Checks if is show copy paste.
	 * 
	 * @return true, if is show copy paste
	 */
	public boolean isShowCopyPaste()
	{
		return strategy.getType() == Type.ADVANCED;
	}
	
	
	/**
	 * Type listener.
	 * 
	 * @param e the e
	 */
	public void typeListener(ActionEvent e)
	{
		if(strategy.getType() == RTPShedStrategy.Type.SIMPLE)
		{
			entries = strategy.getSimpleEntries();
		}
		else
		{
			entries = strategy.getAdvancedEntries();			
		}
	}

	/**
	 * Copy entry action.
	 * 
	 * @return the string
	 */
	public String copyEntryAction()
	{
		for(RTPShedStrategyEntry entry: strategy.getAdvancedEntries())
		{
			if(entry.getTimeBockString().equals(timeBlockString))
			{
				copyModeratePrice = new String(entry.getModeratePrice());
				copyHighPrice = new String(entry.getHighPrice());
				break;
			}
		}
		return null;
	}
	
	/**
	 * Paste entry action.
	 * 
	 * @return the string
	 */
	public String pasteEntryAction()
	{
		for(RTPShedStrategyEntry entry: strategy.getAdvancedEntries())
		{
			if(entry.getTimeBockString().equals(timeBlockString))
			{
				entry.setModeratePrice(copyModeratePrice);
				entry.setHighPrice(copyHighPrice);
				break;
			}
		}
		return null;
	}
	
	/**
	 * Gets the entries.
	 * 
	 * @return the entries
	 */
	public RTPShedStrategyEntry[] getEntries()
	{
		return entries;
	}

	/**
	 * Gets the available types.
	 * 
	 * @return the available types
	 */
	public List<SelectItem> getAvailableTypes()
	{
		List<SelectItem> types = new ArrayList<SelectItem>();
		for(RTPShedStrategy.Type type: RTPShedStrategy.Type.values())
		{
			types.add(new SelectItem(type));
		}
		return types;
	}


	/**
	 * Sets the entries.
	 * 
	 * @param entries the new entries
	 */
	public void setEntries(RTPShedStrategyEntry[] entries)
	{
		this.entries = entries;
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
	 * Checks if is delete.
	 * 
	 * @return true, if is delete
	 */
	public boolean isDelete()
	{
		return delete;
	}

	/**
	 * Sets the delete.
	 * 
	 * @param delete the new delete
	 */
	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	/**
	 * Gets the time block string.
	 * 
	 * @return the time block string
	 */
	public String getTimeBlockString()
	{
		return timeBlockString;
	}

	/**
	 * Sets the time block string.
	 * 
	 * @param timeBlockString the new time block string
	 */
	public void setTimeBlockString(String timeBlockString)
	{
		this.timeBlockString = timeBlockString;
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
		final JSFRTPShedStrategy other = (JSFRTPShedStrategy) obj;
		if (strategy.getName() == null)
		{
			if (other.strategy.getName() != null)
				return false;
		}
		else if (!strategy.getName().equals(other.strategy.getName()))
			return false;
		return true;
	}


	public RTPShedStrategy getStrategy()
	{
		return strategy;
	}


	public void setStrategy(RTPShedStrategy strategy)
	{
		this.strategy = strategy;
	}
}
