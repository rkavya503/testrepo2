// $Revision: 1.4 $ $Date: 2010-04-22 00:47:42 $
package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.pss2.event.participant.EventParticipantRule;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.season.SeasonConfig;

public class SCERTPUtils
{
	private static final int ONE_HOUR_MS = (60 * 60 * 1000) - 1000;

	private static final String NAME = "name";
	
	private static Map<String, String> getSeasonNameFromSource(String source)
	{
		Map<String, String> seasonName = new HashMap<String, String>();
		
		int nameStart = source.indexOf('.') + 1;
    	int weekendStart = source.lastIndexOf('.') + 1;
    	int winterStart = source.lastIndexOf('.', weekendStart - 2) + 1;
    	int summerStart = source.lastIndexOf('.', winterStart - 2) + 1;
    	int nameEnd = summerStart - 2;
    	seasonName.put(NAME, 
    		source.substring(nameStart, nameEnd + 1));
    	seasonName.put(SeasonConfig.SUMMER_SEASON, 
    		String.valueOf(source.charAt(summerStart)));
    	seasonName.put(SeasonConfig.WINTER_SEASON, 
    		String.valueOf(source.charAt(winterStart)));
    	seasonName.put(SeasonConfig.WEEKEND_SEASON, 
    		String.valueOf(source.charAt(weekendStart)));
    	return seasonName;
	}
	
    public static List<RTPShedStrategy> parseRules(List<ProgramParticipantRule> rules)
    {
    	List<RTPShedStrategy> strategies = new ArrayList<RTPShedStrategy>();
    	RTPShedStrategy strategy = new RTPShedStrategy();
    	RTPShedStrategyEntry entry = new RTPShedStrategyEntry();
    	int entryIndex = 0;
    	String stategyName = null;
    	ProgramParticipantRule firstRule = null;
    	ProgramParticipantRule prevRule = null;
    	for(ProgramParticipantRule rule: rules)
    	{
    		if(isSCERTPRule(rule))
    		{
    			Map<String, String> seasonName = getSeasonNameFromSource(rule.getSource());
		    	String name = seasonName.get(NAME);
		    	// deal with the first time thru when we don't know the name
		    	if(stategyName == null)
		    	{
		    		stategyName = name;
		    	}
		    	else if(!name.equals(stategyName))
		    	{
		    		strategies.add(strategy);
		    		strategy = new RTPShedStrategy();
		    		entryIndex = 0;
		    		stategyName = name;
		    	}
		    	strategy.setName(name);
		    	strategy.setSummerActive("y".equals(
		    		seasonName.get(SeasonConfig.SUMMER_SEASON)));
		    	strategy.setWinterActive("y".equals(
		    		seasonName.get(SeasonConfig.WINTER_SEASON)));
		    	strategy.setWeekendActive("y".equals(
		    		seasonName.get(SeasonConfig.WEEKEND_SEASON)));
		    	if(rule.getOperator() == Rule.Operator.ALWAYS)
		    	{
			    	if(rule.getMode() == Rule.Mode.NORMAL)
			    	{
			    		entry.setModeratePrice("");
			    		entry.setHighPrice("");
			    	}
			    	else if(rule.getMode() == Rule.Mode.MODERATE)
			    	{
			    		entry.setModeratePrice("0");
			    		entry.setHighPrice("");			    		
			    	}
			    	else if(rule.getMode() == Rule.Mode.HIGH)
			    	{
			    		entry.setModeratePrice("");
			    		entry.setHighPrice("0");			    		
			    	}
		    	} 
		    	else if(prevRule == null)
		    	{
		    		prevRule = rule;
		    		continue;
		    	}
		    	else
		    	{
		    		if(firstRule != null)
		    		{
		    			entry.setModeratePrice(firstRule.getValue().toString());
		    			entry.setHighPrice(prevRule.getValue().toString());
		    		}
		    		else if(prevRule.getMode() == Rule.Mode.NORMAL &&
		    			rule.getMode() == Rule.Mode.MODERATE)
		    		{
		    			if(rule.getOperator() == Rule.Operator.LESS_THAN)
	    				{
		    				firstRule = prevRule;
		    				prevRule = rule;
		    				continue;
	    				}
		    			else 
		    			{
			    			entry.setModeratePrice(rule.getValue().toString());
			    			entry.setHighPrice("");
		    			}
		    		}
		    		else if(prevRule.getMode() == Rule.Mode.NORMAL)
		    		{
		    			entry.setModeratePrice("");
		    			entry.setHighPrice(rule.getValue().toString());
		    		}
		    		else
		    		{
		    			entry.setModeratePrice("0");
		    			entry.setHighPrice(rule.getValue().toString());
		    		}
		    	}
    			firstRule = null;
    			prevRule = null;
	    		entry.setStartTime(rule.getStart());
	    		entry.setEndTime(rule.getEnd());
	    		long timeDiff = rule.getEnd().getTime() - 
	    			rule.getStart().getTime();
                // See if the entry is one hour long (or very very close to it)
	    		if(Math.abs(timeDiff - ONE_HOUR_MS) < 1000)
	    		{
	    			strategy.setType(RTPShedStrategy.Type.ADVANCED);
	    			strategy.getAdvancedEntries()[entryIndex++] = entry;
	    		}
	    		else
	    		{
	    			strategy.setType(RTPShedStrategy.Type.SIMPLE);
	    			strategy.getSimpleEntries()[entryIndex++] = entry;		    			
	    		}
	    		entry = new RTPShedStrategyEntry();
    		}
    	}
    	if(entryIndex > 0)
    	{
    		strategies.add(strategy);
    	}
    	return strategies;
    }
    
    private static void parseRTPShedStrategyEntry(List<ProgramParticipantRule> rules, 
    	RTPShedStrategyEntry entry, String source)
    {
		boolean moderateBlank = entry.getModeratePrice().equals("");
		double moderate = 0.0;
		if(!moderateBlank)
		{
			moderate = Double.parseDouble(entry.getModeratePrice());
		}
		boolean highBlank = entry.getHighPrice().equals("");
		double high = 0.0;
		if(!highBlank)
		{
			high = Double.parseDouble(entry.getHighPrice());
		}
		if(moderateBlank && highBlank)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.NORMAL);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setValue(0.0);
			rule.setSource(source);
			rules.add(rule);
		}
		else if(moderate == 0 && highBlank)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setValue(0.0);
			rule.setSource(source);
			rules.add(rule);
		}
		else if(moderateBlank && high == 0)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.HIGH);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setValue(0.0);
			rule.setSource(source);
			rules.add(rule);
		}	    				
		else if(highBlank)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.NORMAL);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.LESS_THAN);
			rule.setValue(moderate);
			rule.setSource(source);
			rules.add(rule);
			rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.GREATER_THAN_OR_EQUAL);
			rule.setValue(moderate);
			rule.setSource(source);
			rules.add(rule);
		}
		else if(moderateBlank)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.NORMAL);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.LESS_THAN);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);
			rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.HIGH);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.GREATER_THAN_OR_EQUAL);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);
		}
		else if(moderate == 0)
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.LESS_THAN);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);
			rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.HIGH);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.GREATER_THAN_OR_EQUAL);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);			
		}
		else
		{
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.NORMAL);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.LESS_THAN);
			rule.setValue(moderate);
			rule.setSource(source);
			rules.add(rule);
			rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.LESS_THAN);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);
			rule = new ProgramParticipantRule();
			rule.setStart(entry.getStartTime());
			rule.setEnd(entry.getEndTime());
			rule.setMode(Rule.Mode.HIGH);
			rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
			rule.setOperator(Rule.Operator.GREATER_THAN_OR_EQUAL);
			rule.setValue(high);
			rule.setSource(source);
			rules.add(rule);
		}
    }
    
    public static List<ProgramParticipantRule> parseRTPShedStrategies(
    	List<RTPShedStrategy> strategies)
    {
    	List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();
    	for(RTPShedStrategy strategy: strategies)
    	{
			StringBuilder sourceSB = new StringBuilder();
			sourceSB.append(Rule.Source.SCERTP_SHED_STRATEGY.getDescription());
			sourceSB.append('.');
			sourceSB.append(strategy.getName());
			sourceSB.append('.');
			sourceSB.append(strategy.isSummerActive() ? 'y' : 'n');
			sourceSB.append('.');
			sourceSB.append(strategy.isWinterActive() ? 'y' : 'n');
			sourceSB.append('.');
			sourceSB.append(strategy.isWeekendActive() ? 'y' : 'n');
    		if(strategy.getType() == RTPShedStrategy.Type.SIMPLE)
    		{
    			for(RTPShedStrategyEntry entry: strategy.getSimpleEntries())
    			{
    				parseRTPShedStrategyEntry(rules, entry, sourceSB.toString());
    			}
    		}
    		else
    		{
    			for(RTPShedStrategyEntry entry: strategy.getAdvancedEntries())
    			{
    				parseRTPShedStrategyEntry(rules, entry, sourceSB.toString());
    			}    			
    		}
    	}
    	return rules;
    }

    public static boolean isSCERTPRule(ProgramParticipantRule rule)
    {
    	if(rule.getSource().startsWith(
    		Rule.Source.SCERTP_SHED_STRATEGY.getDescription()))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    public static List<EventParticipantRule> getEventParticipantRules(
    	ProgramParticipant programParticipant, String season, Date eventDate)
	{
    	List<EventParticipantRule> eventPartRules =
                new ArrayList<EventParticipantRule>();

        // make a list so we can sort them
		List<ProgramParticipantRule> programParticipantRules =
                new ArrayList<ProgramParticipantRule>();
		programParticipantRules.addAll(
                programParticipant.getProgramParticipantRules());
        Collections.sort(programParticipantRules, new Rule.SortOrderComparator());
        for(ProgramParticipantRule rule: programParticipantRules)
        {
        	if(isSCERTPRule(rule))
        	{
      			Map<String, String> seasonName = 
      				getSeasonNameFromSource(rule.getSource());      	
      			// if this rule applies to the current season
        		if("y".equals(seasonName.get(season)))
        		{
        			// copy the 
        			EventParticipantRule eventRule =
                        new EventParticipantRule(rule, eventDate);
        			eventRule.setSource(
        				Rule.Source.SCERTP_SHED_STRATEGY.getDescription() + 
        				" " + seasonName.get(NAME));
        			eventPartRules.add(eventRule);
        		}
			}
            else
            {
                 eventPartRules.add(new EventParticipantRule(rule, eventDate));
            }
        }
    	return eventPartRules;
	}
}
