/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFRule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Operator;

/**
 * The Class JSFRule.
 */
public class JSFRule implements Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Enum Variable.
	 */
	private enum Variable {
        /** The price. */
        price,
        /** The bid. */
        bid
    }

    /** The rule. */
	private ProgramParticipantRule rule;
	
	/** The delete. */
	private boolean delete;
	
	/**
	 * Instantiates a new jSF rule.
	 */
	public JSFRule()
	{
		rule = new ProgramParticipantRule();
		try
		{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            rule.setStart(simpleDateFormat.parse("00:00"));
			rule.setEnd(simpleDateFormat.parse("23:59"));
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(Variable.price.toString());
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setValue(0.1);
			rule.setSource(Rule.Source.CUSTOM.getDescription());
		}
		catch (ParseException e)
		{
            e.printStackTrace();
			// TODO: what should we do here?
		}
	}
	
	/**
	 * Instantiates a new jSF rule.
	 * 
	 * @param rule the rule
	 */
	public JSFRule(ProgramParticipantRule rule)
	{
		this.rule = rule;
	}
			
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return delete + "_" +
			simpleDateFormat.format(rule.getStart()) + "_" +
			simpleDateFormat.format(rule.getEnd()) + "_" +
			rule.getMode() + "_" + 
			rule.getVariable() + "_" + 
			rule.getOperator().description() + "_" +
			rule.getValue();
	}

	public String operatorAction()
	{
		return null;
	}
	
	/**
	 * Gets the available modes.
	 * 
	 * @return the available modes
	 */
	public List<SelectItem> getAvailableModes()
	{
		List<SelectItem> availableModes = new ArrayList<SelectItem>();
		for(Rule.Mode operator: Rule.Mode.values())
		{
			availableModes.add(new SelectItem(operator));
		}		
		return availableModes;
	}

	/**
	 * Gets the rule.
	 * 
	 * @return the rule
	 */
	public ProgramParticipantRule getRule()
	{
		return rule;
	}

	/**
	 * Sets the rule.
	 * 
	 * @param rule the new rule
	 */
	public void setRule(ProgramParticipantRule rule)
	{
		this.rule = rule;
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
	 * Gets the operator.
	 * 
	 * @return the operator
	 */
	public String getOperator()
	{
		return rule.getOperator().description();
	}

	/**
	 * Sets the operator.
	 * 
	 * @param jsfOperator the new operator
	 */
	public void setOperator(String jsfOperator)
	{
		for(Rule.Operator operator: Rule.Operator.values())
		{
			if(jsfOperator.equals(operator.description()))
			{
				rule.setOperator(operator);
				return;
			}
		}	
	}

	/**
	 * @return the variableValueEditable
	 */
	public boolean isVariableValueEditable()
	{
		if(rule.getOperator() == Operator.ALWAYS)
		{
			return true;
		}
		else
		{
			return false;			
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (delete ? 1231 : 1237);
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{

        if (this.getRule().getUUID().equals( ((JSFRule)obj).getRule().getUUID()))
                return true;
        else
                return false;
       /*
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JSFRule other = (JSFRule) obj;
		if (delete != other.delete)
			return false;
		if (rule == null)
		{
			if (other.rule != null)
				return false;
		}
		else if (!rule.equals(other.rule))
			return false;

		return true;
	*/
    }

	
}
