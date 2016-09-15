// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.akuacom.pss2.event.participant.EventParticipantRule;

public class EventRuleTable
{
	private List<JSFEventRule> jsfRules = new ArrayList<JSFEventRule>();
	private Set<JSFEventRule> selectedRules = new HashSet<JSFEventRule>();
		
	public EventRuleTable()
	{
	}

	public EventRuleTable(Set<EventParticipantRule> rules)
	{
		for(EventParticipantRule rule: rules)
		{
			jsfRules.add(new JSFEventRule(rule));
		}
		Collections.sort(jsfRules, new EventRuleComparator());
	}

	public String newRuleAction()
	{
		FDUtils.setJSFEventRule(new JSFEventRule());
		return "newRule";
	}
	
	public String createRuleAction()
	{
		jsfRules.add(FDUtils.getJSFEventRule());
		return "createRule";
	}
	
	public String cancelRuleAction()
	{
		return "cancelRule";
	}

	public String deleteRulesAction()
	{
		Iterator<JSFEventRule> i = jsfRules.iterator();
		while(i.hasNext())
		{
			JSFEventRule rule = i.next();
			if(rule.isDelete())
			{
				i.remove();
			}
		}
		return "deleteRules";
	}
	
	public String saveRulesAction()
	{
		FDUtils.getJSFEvent().updateRules(jsfRules);
		return "saveRules";
	}
	
	public String cancelRulesAction()
	{
		return "cancelRules";
	}
	
	public Set<JSFEventRule> getSelectedRules()
	{
		return selectedRules;
	}

	public void setSelectedRules(Set<JSFEventRule> selectedRules)
	{
		this.selectedRules = selectedRules;
	}
	
	public List<JSFEventRule> getJsfRules()
	{
		return jsfRules;
	}

	public void setJsfRules(List<JSFEventRule> jsfRules)
	{
		this.jsfRules = jsfRules;
	}

	private class EventRuleComparator implements Comparator<JSFEventRule>
	{
		public int compare(JSFEventRule o1, JSFEventRule o2)
		{
			return o1.getRule().getSortOrder().compareTo(o2.getRule().getSortOrder());
		}	
	}
}
