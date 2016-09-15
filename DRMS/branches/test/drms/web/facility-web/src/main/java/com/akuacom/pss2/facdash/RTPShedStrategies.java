/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.RTPShedStrategies.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.akuacom.accmgr.ws.AccMgrWS;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.pss2.program.scertp.RTPShedStrategy;
import com.akuacom.pss2.program.scertp.RTPShedStrategyEntry;

/**
 * The Class RTPShedStrategies.
 */
public class RTPShedStrategies
{
	
	/** The strategies. */
	private List<JSFRTPShedStrategy> strategies = 
		new ArrayList<JSFRTPShedStrategy>();
	
	/** The summer active strategy. */
	private String summerActiveStrategy;
	
	/** The winter active strategy. */
	private String winterActiveStrategy;
	
	/** The weekend active strategy. */
	private String weekendActiveStrategy;
	
	/** The summer active strategy name. */
	private String summerActiveStrategyName = "a";
	
	/** The winter active strategy name. */
	private String winterActiveStrategyName = "a";
	
	/** The weekend active strategy name. */
	private String weekendActiveStrategyName = "a";
	
	/** The strategy name. */
	private String strategyName;
	
	private boolean deleteStrategy;
	
	
	public boolean isDeleteStrategy() {
		return deleteStrategy;
	}

	public void setDeleteStrategy(boolean deleteStrategy) {
		this.deleteStrategy = deleteStrategy;
	}

	public RTPShedStrategies()
	{
	}
	
	public RTPShedStrategies(List<RTPShedStrategy> strategies)
	{
		// TODO: convert
		for(RTPShedStrategy strategy: strategies)
		{
			this.strategies.add(new JSFRTPShedStrategy(strategy));
		}
	}
	
    /**
	 * Adds the strategy action.
	 * 
	 * @return the string
	 */
	public String newStrategyAction()
	{
		FDUtils.setRTPShedStrategy(new JSFRTPShedStrategy());
		return "newStrategy";
	}
	
	/**
	 * Edits the strategy action.
	 * 
	 * @return the string
	 */
	public String editStrategyAction()
	{
		JSFRTPShedStrategy stratgey = (JSFRTPShedStrategy)
			strategies.get(strategies.indexOf(
			new JSFRTPShedStrategy(strategyName)));
		stratgey.setEdit(true);
		FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().put("rtpShedStrategy", stratgey);
		return "editStrategy";
	}
	
	/**
	 * Edits the strategy listen.
	 * 
	 * @param e the e
	 */
	public void editStrategyListen(ActionEvent e)
	{
		FacesContext ctx = FacesContext.getCurrentInstance();   
        strategyName = (String)ctx.getExternalContext().
        	getRequestParameterMap().get("strategyName");   	
	}
	
	/**
	 * Cancel strategies action.
	 * 
	 * @return the string
	 */
	public String cancelStrategiesAction()
	{
		return "cancelStrategies";
	}
	
	/**
	 * Summer active action.
	 * 
	 * @return the string
	 */
	public String summerActiveAction()
	{
		for(JSFRTPShedStrategy strategy: strategies)	
		{
			if(strategy.getStrategy().getName().equals(summerActiveStrategyName))
			{
				strategy.getStrategy().setSummerActive(true);
			}
			else
			{
				strategy.getStrategy().setSummerActive(false);				
			}
		}
		return null;
	}

	/**
	 * Winter active action.
	 * 
	 * @return the string
	 */
	public String winterActiveAction()
	{
		for(JSFRTPShedStrategy strategy: strategies)	
		{
			if(strategy.getStrategy().getName().equals(winterActiveStrategyName))
			{
				strategy.getStrategy().setWinterActive(true);
			}
			else
			{
				strategy.getStrategy().setWinterActive(false);				
			}
		}
		return null;
	}

	/**
	 * Weekend active action.
	 * 
	 * @return the string
	 */
	public String weekendActiveAction()
	{
		for(JSFRTPShedStrategy strategy: strategies)	
		{
			if(strategy.getStrategy().getName().equals(weekendActiveStrategyName))
			{
				strategy.getStrategy().setWeekendActive(true);
			}
			else
			{
				strategy.getStrategy().setWeekendActive(false);				
			}
		}
		return null;
	}

	/**
	 * Gets the strategies.
	 * 
	 * @return the strategies
	 */
	public List<JSFRTPShedStrategy> getStrategies()
	{
		return strategies;
	}

	/**
	 * Sets the strategies.
	 * 
	 * @param strategies the new strategies
	 */
	public void setStrategies(List<JSFRTPShedStrategy> strategies)
	{
		this.strategies = strategies;
	}

	/**
	 * Delete strategy action.
	 * 
	 * @return the string
	 */
	public String deleteStrategiesAction()
	{
		Iterator<JSFRTPShedStrategy> i = strategies.iterator();
		while(i.hasNext())
		{
			JSFRTPShedStrategy strategy = (JSFRTPShedStrategy)i.next();
			if(strategy.isDelete())
			{
				i.remove();
				// reassign active flags if needed
				if(strategies.size() > 0)
				{
					if(strategy.getStrategy().isSummerActive())
					{
						strategies.get(0).getStrategy().setSummerActive(true);
					}
					if(strategy.getStrategy().isWinterActive())
					{
						strategies.get(0).getStrategy().setWinterActive(true);
					}
					if(strategy.getStrategy().isWeekendActive())
					{
						strategies.get(0).getStrategy().setWeekendActive(true);
					}
				}
			}
		}
		return "deleteStrategies";
	}

	private boolean validateStrategy()
	{
		boolean valid = true;
		JSFRTPShedStrategy strategy = FDUtils.getRTPShedStrategy();
		for(RTPShedStrategyEntry entry: strategy.getEntries())
		{
			try {
				if(Double.parseDouble(entry.getModeratePrice()) >= 
					Double.parseDouble(entry.getHighPrice()))
				{
					FDUtils.addMsgInfo(entry.getTimeBockString() + 
						": moderate price must be less than high price");
					valid = false;
				}
			}
			catch (NumberFormatException e)
			{
				// do nothing
			}
		}
		return valid;		
	}
	
	/**
	 * Adds the strategy action.
	 * 
	 * @return the string
	 */
	public String createStrategyAction()
	{
		if(!validateStrategy())
		{
			return null;
		}

		JSFRTPShedStrategy strategy = FDUtils.getRTPShedStrategy();
		if(strategies.indexOf(strategy) == -1)
		{
			if(strategies.size() == 0)
			{
				strategy.getStrategy().setSummerActive(true);
				strategy.getStrategy().setWinterActive(true);
				strategy.getStrategy().setWeekendActive(true);
			}
			strategies.add(strategy);
		}
		else
		{
			// TODO: add uniqueness error message
		}
		return "createStrategy";
	}
	
	/**
	 * Update strategy action.
	 * 
	 * @return the string
	 */
	public String updateStrategyAction()
	{
		if(!validateStrategy())
		{
			return null;
		}
		return "updateStrategy";
	}
	
	/**
	 * Cancel strategy action.
	 * 
	 * @return the string
	 */
	public String cancelStrategyAction()
	{
		return "cancelStrategy";
	}

	/**
	 * Gets the summer active strategy.
	 * 
	 * @return the summer active strategy
	 */
	public String getSummerActiveStrategy()
	{
		return summerActiveStrategy;
	}

	/**
	 * Sets the summer active strategy.
	 * 
	 * @param summerActiveStrategy the new summer active strategy
	 */
	public void setSummerActiveStrategy(String summerActiveStrategy)
	{
		this.summerActiveStrategy = summerActiveStrategy;
	}

	/**
	 * Gets the winter active strategy.
	 * 
	 * @return the winter active strategy
	 */
	public String getWinterActiveStrategy()
	{
		return winterActiveStrategy;
	}

	/**
	 * Sets the winter active strategy.
	 * 
	 * @param winterActiveStrategy the new winter active strategy
	 */
	public void setWinterActiveStrategy(String winterActiveStrategy)
	{
		this.winterActiveStrategy = winterActiveStrategy;
	}

	/**
	 * Gets the weekend active strategy.
	 * 
	 * @return the weekend active strategy
	 */
	public String getWeekendActiveStrategy()
	{
		return weekendActiveStrategy;
	}

	/**
	 * Sets the weekend active strategy.
	 * 
	 * @param weekendActiveStrategy the new weekend active strategy
	 */
	public void setWeekendActiveStrategy(String weekendActiveStrategy)
	{
		this.weekendActiveStrategy = weekendActiveStrategy;
	}

	/**
	 * Gets the summer active strategy name.
	 * 
	 * @return the summer active strategy name
	 */
	public String getSummerActiveStrategyName()
	{
		return summerActiveStrategyName;
	}

	/**
	 * Sets the summer active strategy name.
	 * 
	 * @param summerActiveStrategyName the new summer active strategy name
	 */
	public void setSummerActiveStrategyName(String summerActiveStrategyName)
	{
		this.summerActiveStrategyName = summerActiveStrategyName;
	}

	/**
	 * Gets the winter active strategy name.
	 * 
	 * @return the winter active strategy name
	 */
	public String getWinterActiveStrategyName()
	{
		return winterActiveStrategyName;
	}

	/**
	 * Sets the winter active strategy name.
	 * 
	 * @param winterActiveStrategyName the new winter active strategy name
	 */
	public void setWinterActiveStrategyName(String winterActiveStrategyName)
	{
		this.winterActiveStrategyName = winterActiveStrategyName;
	}

	/**
	 * Gets the weekend active strategy name.
	 * 
	 * @return the weekend active strategy name
	 */
	public String getWeekendActiveStrategyName()
	{
		return weekendActiveStrategyName;
	}

	/**
	 * Sets the weekend active strategy name.
	 * 
	 * @param weekendActiveStrategyName the new weekend active strategy name
	 */
	public void setWeekendActiveStrategyName(String weekendActiveStrategyName)
	{
		this.weekendActiveStrategyName = weekendActiveStrategyName;
	}

	/**
	 * Save rules action.
	 * 
	 * @return the string
	 */
	public String saveStrategiesAction()
	{
		FDUtils.getJSFClientProgram().updateRTPShedStrategies(strategies);
		return "saveStrategies";
	}
	
}
