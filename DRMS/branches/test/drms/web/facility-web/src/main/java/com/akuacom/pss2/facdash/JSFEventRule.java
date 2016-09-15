// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.rule.Rule;

public class JSFEventRule implements Serializable
{
	private static final long serialVersionUID = 1L;

	private enum Variable {price, bid}

    private EventParticipantRule rule;
	private boolean delete;

	public JSFEventRule()
	{
		rule = new EventParticipantRule();
		try
		{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            rule.setStart(simpleDateFormat.parse("00:00"));
			rule.setEnd(simpleDateFormat.parse("23:59"));
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(Variable.price.toString());
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setValue(0.1);
		}
		catch (ParseException e)
		{
			// TODO: what should we do here?
		}
	}
	
	public JSFEventRule(EventParticipantRule rule)
	{
		this.rule = rule;
	}
			
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

	public List<SelectItem> getAvailableModes()
	{
		List<SelectItem> availableModes = new ArrayList<SelectItem>();
		for(Rule.Mode operator: Rule.Mode.values())
		{
			availableModes.add(new SelectItem(operator));
		}		
		return availableModes;
	}

	public List<SelectItem> getAvailableOperators()
	{
		List<SelectItem> availableOperators = new ArrayList<SelectItem>();
		for(Rule.Operator operator: Rule.Operator.values())
		{
			availableOperators.add(new SelectItem(operator.description()));
		}		
		return availableOperators;
	}

	public List<SelectItem> getAvailableVariables()
	{
		List<SelectItem> availableVariables = new ArrayList<SelectItem>();
		for(Variable variable: Variable.values())
		{
			availableVariables.add(new SelectItem(variable));
		}		
		return availableVariables;
	}

	public EventParticipantRule getRule()
	{
		return rule;
	}

	public void setRule(EventParticipantRule rule)
	{
		this.rule = rule;
	}

	public boolean isDelete()
	{
		return delete;
	}

	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	public String getOperator()
	{
		return rule.getOperator().description();
	}

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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (delete ? 1231 : 1237);
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JSFEventRule other = (JSFEventRule) obj;
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
	}
	
}
