/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.SignalLevelMapper.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.util.ModeSignal;

/**
 * The Class SignalLevelMapper used to determine if a price level triggers a rule.
 */
public class SignalLevelMapper {

    /** The level map. */
    private static Map<Integer, String> levelMap = new HashMap<Integer, String>();

    static {
        final ModeSignal.Levels[] levels = ModeSignal.Levels.values();
        for (ModeSignal.Levels level : levels) {
            levelMap.put(level.ordinal(), level.toString());
        }
    }

    /**
     * Resolve the price against a set rules to determine the first one that
     * gets triggered.
     * 
     * @param price the price
     * @param rules the rules
     * 
     * @return the string
     */
    public static String resolveLevel(String price, List<String> rules) {
        if (price == null) {
            throw new IllegalArgumentException("Argument price is null");
        }
        if (rules == null) {
            throw new IllegalArgumentException("Argument rules is null");
        }

        double p = Double.parseDouble(price);
        String result = ModeSignal.Levels.normal.toString();
        for (int i = 0; i < rules.size(); i++) {
            String rule = rules.get(i);
            if (rule != null) {
                if ("x".equals(rule) || "".equals(rule)) {
                    // continue; // ignore x and blank
                } else {
                    double ruleThreshold = Double.parseDouble(rule);
                    if (p >= ruleThreshold) {
                        result = levelMap.get(i);
                    }
                }
            }
        }
        return result;
    }

    public static double[] getValues(List<String> rules)
    {
    	double[] values = new double[rules.size()];
    	int i = 0;
    	for(String rule: rules)
    	{
            if ("x".equals(rule) || "".equals(rule)) 
            {
            	values[i] = -1.0;
            }
            else
            {		
            	values[i] = Double.parseDouble(rule);
            }
            i++;
    	}
    	return values;
    }
    
    /**
     * Gets the time block.
     * 
     * @param timeBlock the time block
     * 
     * @return the time block
     */
    public static String getTimeBlock(TimeBlock timeBlock) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(timeBlock.getStartReferenceTime()) + "-" +
        	simpleDateFormat.format(timeBlock.getEndReferenceTime());
    }
    
    public static String getTimeBlock(EventBidBlock eventTimeBlock) {
    	TimeBlock block = new TimeBlock();
        block.setId(eventTimeBlock.getUUID());
        block.setEndHour(eventTimeBlock.getEndTime() / 100);
        block.setEndMinute(eventTimeBlock.getEndTime() % 100);
        block.setStartHour(eventTimeBlock.getStartTime() / 100);
        block.setStartMinute(eventTimeBlock.getStartTime() % 100);

        return getTimeBlock(block);
    }
    
    public static TimeBlock getTimeBlock(String timeBlockString)
    {
    	TimeBlock timeBlock = new TimeBlock();
    	
		String[] blockTimes = timeBlockString.split("-");
		String[] startTimes = blockTimes[0].split(":");
		String[] endTimes = blockTimes[1].split(":");
		timeBlock.setStartHour(Integer.parseInt(startTimes[0]));
		timeBlock.setStartMinute(Integer.parseInt(startTimes[1]));
		timeBlock.setEndHour(Integer.parseInt(endTimes[0]));
		timeBlock.setEndMinute(Integer.parseInt(endTimes[1]));
		
		return timeBlock;
    }
}