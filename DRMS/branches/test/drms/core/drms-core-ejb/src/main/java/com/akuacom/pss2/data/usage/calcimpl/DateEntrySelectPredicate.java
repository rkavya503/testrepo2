package com.akuacom.pss2.data.usage.calcimpl;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.collections.Predicate;

import com.akuacom.pss2.data.usage.UsageUtil;

public class DateEntrySelectPredicate implements Predicate {
	
	private long startTime;
	private long endTime;
	private int conditions;
	private Class clazz;
	private String fieldName;
	
	public static int EQUAL_START = 1;
	//MORE THAN OR EQUAL START AND LESS THAN OR EQUAL END
	public static int BETWEEN_START_END = 1<<1;
	//MORE THAN START AND LESS THAN OR EQUAL END
	public static int MT_START_LET_END = 1<<2;
	
	public DateEntrySelectPredicate(long startTime, long endTime, int conditions, Class clazz, String fieldName){
		this.startTime = startTime;
		this.endTime = endTime;
		this.conditions = conditions;
		this.clazz = clazz;
		this.fieldName = fieldName;
	}

	@Override
	public boolean evaluate(Object object) {
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		Date time = null;
		try {
			field.setAccessible(true);
			time = (Date) field.get(object);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		long curTime = UsageUtil.getCurrentTime(time);

		if ((this.conditions & EQUAL_START) != 0) {
			return getEntryByStartTime(curTime);
			
		} else if ((this.conditions & MT_START_LET_END) != 0) {
			return getEntryMTStartLEEnd(curTime);
			
		} else if ((this.conditions & BETWEEN_START_END) != 0) {
			return getEntryMEStartLEEnd(curTime);
			
		}
		
		return false;
	}
	
	private boolean getEntryByStartTime(long curTime){
		if(startTime==curTime){
			return true;
		}
		return false;
	}
	
	private boolean getEntryMTStartLEEnd(long curTime){
		 if (curTime > startTime&& curTime <= endTime) {
	            return true;
	        }
		return false;
	}
	
	private boolean getEntryMEStartLEEnd(long curTime){
		if (curTime >= startTime&& curTime <= endTime) {
            return true;
        }
		return false;
	}

	
}