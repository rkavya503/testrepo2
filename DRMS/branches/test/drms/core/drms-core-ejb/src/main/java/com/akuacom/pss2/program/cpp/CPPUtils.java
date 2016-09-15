// $Revision$ $Date$
package com.akuacom.pss2.program.cpp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.cpp.CPPShedStrategy.ShedMode;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJBBean;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.utils.lang.DateUtil;

public class CPPUtils
{
    public static List<CPPShedStrategy> parseRules(CPPProgram program,
    	List<ProgramParticipantRule> rules)
    {
		int startHour = program.getMinStartTimeH();
		int endHour = program.getMaxEndTimeH();
		int hours = getHoursInBlock(program);
    	CPPShedStrategy[] strategies = new CPPShedStrategy[hours];
		int index = 0;
    	
		if(hours>=rules.size()){
		 for(ProgramParticipantRule rule: rules)
		  {
    		if(isCPPRule(rule))
    		{
		    	CPPShedStrategy strategy = new CPPShedStrategy();
		    	strategy.setRuleMode(rule.getMode());
		    	TimeBlock timeBlock = new TimeBlock();
		    	Calendar startCal = new GregorianCalendar();
		    	startCal.setTime(rule.getStart());
		    	int hour = startCal.get(Calendar.HOUR_OF_DAY);
                if (hour == 23 && endHour == 23 && program.getMaxEndTimeH() > 0 ) {
                    // let it go through
                } else if(hour < startHour || hour >= endHour){
		    		continue;
		    	}
		    	timeBlock.setStartHour(hour);
		    	timeBlock.setStartMinute(startCal.get(Calendar.MINUTE));
		    	timeBlock.setStartSecond(startCal.get(Calendar.SECOND));
		    	Calendar endCal = new GregorianCalendar();
		    	endCal.setTime(rule.getEnd());
		    	timeBlock.setEndHour(endCal.get(Calendar.HOUR_OF_DAY));
		    	timeBlock.setEndMinute(endCal.get(Calendar.MINUTE));
		    	timeBlock.setEndSecond(endCal.get(Calendar.SECOND));
		    	strategy.setTimeBlock(timeBlock);
		    	strategies[index++] = strategy;
    		}
     	  }
		}

    	// fill any empties to default
		for(int i = 0; i < hours; i++)
		{
			if(strategies[i] == null)
			{
				strategies[i] = new CPPShedStrategy();
				TimeBlock timeBlock = new TimeBlock();
				timeBlock.setStartHour(startHour + i);
				timeBlock.setEndHour(startHour + i + 1);
				strategies[i].setTimeBlock(timeBlock);
				strategies[i].setMode(CPPShedStrategy.ShedMode.DEFAULT);
			}
		}
    	
    	return Arrays.asList(strategies);
    }
    

    public static List<ProgramParticipantRule> parseCPPShedStrategies(
    	List<CPPShedStrategy> strategies)
    {
    	List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();
    	for(CPPShedStrategy strategy: strategies)
    	{
    		if(strategy.getMode() != ShedMode.DEFAULT)
    		{
				ProgramParticipantRule rule = new ProgramParticipantRule();
				rule.setStart(strategy.getTimeBlock().getStartReferenceTime());
				rule.setEnd(strategy.getTimeBlock().getEndReferenceTime());
				rule.setMode(strategy.getRuleMode());
				rule.setVariable(SCERTPProgramEJBBean.PRICE_SIGNAL_NAME);
				rule.setOperator(Rule.Operator.ALWAYS);
				rule.setValue(0.0);
				rule.setSource(Rule.Source.CPP_SHED_STRATEGY.getDescription());
				rules.add(rule);
    		}
    		
    	}
    	return rules;
    }

    public static boolean isCPPRule(ProgramParticipantRule rule)
    {
    	if(rule.getSource().startsWith(
    		Rule.Source.CPP_SHED_STRATEGY.getDescription()))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    private static int getHoursInBlock(Program program) {
    	int startHour = program.getMinStartTimeH();
		int endHour;
		if(program.getMaxEndTimeH() == 23 && program.getMaxEndTimeM() == 59)
		{
			endHour = 24;

		}
		else
		{
			endHour = program.getMaxEndTimeH();
		}
		
		return endHour - startHour;
    }
    
	public static void createDefaultClientRules(Program program,
			ProgramRule programRule, List<ProgramParticipantRule> rules) {
		int startHour = program.getMinStartTimeH();
		int hours = getHoursInBlock(program);

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(DateUtil.stripTime(new Date()));
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(DateUtil.stripTime(new Date()));
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);

		// fill any empties to default
		for (int i = 0; i < hours; i++) {
			calStart.set(Calendar.HOUR_OF_DAY, startHour + i);
			calEnd.set(Calendar.HOUR_OF_DAY, startHour + i);
			ProgramParticipantRule rule = new ProgramParticipantRule();
			rule.setStart(calStart.getTime());
			rule.setEnd(calEnd.getTime());
			rule.setMode(programRule.getMode());
			rule.setVariable(programRule.getVariable());
			rule.setOperator(programRule.getOperator());
			rule.setValue(programRule.getValue());
			rule.setSource(Rule.Source.CPP_SHED_STRATEGY.getDescription());
			rule.setSortOrder(i);
			rules.add(rule);
		}
	}
}
