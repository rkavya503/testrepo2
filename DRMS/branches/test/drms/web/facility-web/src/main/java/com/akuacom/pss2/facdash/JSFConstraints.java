/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFConstraints.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.program.participant.Constraint;
import com.akuacom.pss2.program.participant.InvalidDate;
import com.akuacom.utils.SortedArrayList;

/**
 * The Class JSFConstraints.
 */
public class JSFConstraints implements Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The constraint. */
	private Constraint constraint;
	
	/** The jsf invalid dates. */
	private List<JSFInvalidDate> jsfInvalidDates = new SortedArrayList<JSFInvalidDate>();
	
	/** The selected date. */
	private Date selectedDate;

    private String addDateError;

	/**
	 * Instantiates a new jSF constraints.
	 */
	public JSFConstraints()
	{
		try
		{
			this.constraint = new Constraint();
			constraint.setActiveAction(Constraint.Action.ACCEPT);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            constraint.setMinActive(simpleDateFormat.parse("00:00"));
			constraint.setMaxActive(simpleDateFormat.parse("23:59"));
			constraint.setNotifyAction(Constraint.Action.ACCEPT);
			constraint.setMinNotify(simpleDateFormat.parse("00:00"));
			constraint.setMaxNotify(simpleDateFormat.parse("23:59"));
			constraint.setDurationAction(Constraint.Action.ACCEPT);
			constraint.setMinDuration(simpleDateFormat.parse("00:01"));
			constraint.setMaxDuration(simpleDateFormat.parse("23:59"));
			constraint.setConsecutiveAction(Constraint.Action.ACCEPT);
			constraint.setMaxConsecutiveD(0);
			constraint.setInvalidDates(new HashSet<InvalidDate>());

          
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new jSF constraints.
	 * 
	 * @param constraint the constraint
	 */
	public JSFConstraints(Constraint constraint)
	{
		this.constraint = constraint;
		for(InvalidDate invaidDate: constraint.getInvalidDates())
		{
			jsfInvalidDates.add(new JSFInvalidDate(invaidDate));
		}
	}
	
	/**
	 * Parses the.
	 */
	public void parse()
	{
		constraint.getInvalidDates().clear();
		for(JSFInvalidDate jfsInvalidDate: jsfInvalidDates)
		{
			constraint.getInvalidDates().add(jfsInvalidDate.getInvalidDate());
		}
	}
	
	/**
	 * Gets the available active actions.
	 * 
	 * @return the available active actions
	 */
	public List<SelectItem> getAvailableActiveActions()
	{
		List<SelectItem> activeActions = new ArrayList<SelectItem>();
//		for(ConstraintEAO.Action action: ConstraintEAO.Action.values())
//		{
//			activeActions.add(new SelectItem(action));
//		}
		activeActions.add(new SelectItem(Constraint.Action.ACCEPT));
		activeActions.add(new SelectItem(Constraint.Action.REJECT));
		return activeActions;
	}

	/**
	 * Gets the available notify actions.
	 * 
	 * @return the available notify actions
	 */
	public List<SelectItem> getAvailableNotifyActions()
	{
		List<SelectItem> notifyActions = new ArrayList<SelectItem>();
//		for(ConstraintEAO.Action action: ConstraintEAO.Action.values())
//		{
//			notifyActions.add(new SelectItem(action));
//		}
		notifyActions.add(new SelectItem(Constraint.Action.ACCEPT));
		notifyActions.add(new SelectItem(Constraint.Action.REJECT));
		return notifyActions;
	}

	/**
	 * Gets the available duration actions.
	 * 
	 * @return the available duration actions
	 */
	public List<SelectItem> getAvailableDurationActions()
	{
		List<SelectItem> durationActions = new ArrayList<SelectItem>();
//		for(ConstraintEAO.Action action: ConstraintEAO.Action.values())
//		{
//			durationActions.add(new SelectItem(action));
//		}
		durationActions.add(new SelectItem(Constraint.Action.ACCEPT));
		durationActions.add(new SelectItem(Constraint.Action.REJECT));
		return durationActions;
	}

	/**
	 * Gets the available consecutive actions.
	 * 
	 * @return the available consecutive actions
	 */
	public List<SelectItem> getAvailableConsecutiveActions()
	{
		List<SelectItem> availableActions = new ArrayList<SelectItem>();
		availableActions.add(new SelectItem(Constraint.Action.ACCEPT));
		availableActions.add(new SelectItem(Constraint.Action.REJECT));
		return availableActions;
	}

	/**
	 * Adds the date action.
	 * 
	 * @return the string
	 */
	public String addDateAction()
	{
        if (this.selectedDate != null) {
            jsfInvalidDates.add(new JSFInvalidDate(selectedDate));
            addDateError = "";
        }else{
            addDateError = "You need to select a date";
        }
		return null;
	}
	
	/**
	 * Delete date action.
	 * 
	 * @return the string
	 */
	public String deleteDateAction()
	{
		Iterator<JSFInvalidDate> i = jsfInvalidDates.iterator();
		while(i.hasNext())
		{
			JSFInvalidDate date = i.next();
			if(date.isDelete())
			{
				i.remove();
			}
		}
        addDateError = "";
		return null;
	}

	/**
	 * Gets the selected date.
	 * 
	 * @return the selected date
	 */
	public Date getSelectedDate()
	{
		return selectedDate;
	}

	/**
	 * Sets the selected date.
	 * 
	 * @param selectedDate the new selected date
	 */
	public void setSelectedDate(Date selectedDate)
	{
		this.selectedDate = selectedDate;
	}

	/**
	 * Gets the constraint.
	 * 
	 * @return the constraint
	 */
	public Constraint getConstraint()
	{
		return constraint;
	}

	/**
	 * Sets the constraint.
	 * 
	 * @param constraint the new constraint
	 */
	public void setConstraint(Constraint constraint)
	{
		this.constraint = constraint;
	}

	/**
	 * Gets the jsf invalid dates.
	 * 
	 * @return the jsf invalid dates
	 */
	public List<JSFInvalidDate> getJsfInvalidDates()
	{
		return jsfInvalidDates;
	}

	/**
	 * Sets the jsf invalid dates.
	 * 
	 * @param jsfInvalidDates the new jsf invalid dates
	 */
	public void setJsfInvalidDates(List<JSFInvalidDate> jsfInvalidDates)
	{
		this.jsfInvalidDates = jsfInvalidDates;
	}
	
	public String getAddDateError()
	{
		return addDateError;
	}

	public void setAddDateError(String addDateError)
	{
		this.addDateError = addDateError;
	}
}
