package com.akuacom.pss2.drw.util;

import java.util.Comparator;

import com.akuacom.pss2.drw.value.EventValue;

public class EventValueComparator implements Comparator<EventValue> {


	@Override
	public int compare(EventValue arg0, EventValue arg1) {
		int flag = 0;
		if(arg0.getStartTime()!=null && arg1.getStartTime()!=null){
			flag = arg0.getStartTime().compareTo(arg1.getStartTime());
			if(flag==0){
				if(arg0.getEndTime()!=null && arg1.getEndTime()!=null){
					flag = arg0.getEndTime().compareTo(arg1.getEndTime());
				}else if(arg0.getEndTime()!=null && arg1.getEndTime() ==null){
					flag = 1;
				}else if(arg0.getEndTime()==null && arg1.getEndTime() !=null){
					flag = -1;
				}else{
					flag = 0;
				}
			}
		}else if(arg0.getStartTime()!=null && arg1.getStartTime() ==null){
			flag = 1;
		}else if(arg0.getStartTime()==null && arg1.getStartTime() !=null){
			flag = -1;
		}else{
			flag = 0;
		}
		return flag;
	}

}
