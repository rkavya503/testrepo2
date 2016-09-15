package com.akuacom.pss2.drw.util;

import java.util.Comparator;

import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.value.EventValue;

public class EventModelComparator implements Comparator<BaseEventDataModel> {


	@Override
	public int compare(BaseEventDataModel arg0, BaseEventDataModel arg1) {
		
		int flag = 0;
		if(arg0==null||arg1==null||arg0.getEvent()==null||arg1.getEvent()==null){
			return flag;
		}
		if(arg0.getEvent().getStartTime()!=null && arg1.getEvent().getStartTime()!=null){
			flag = arg0.getEvent().getStartTime().compareTo(arg1.getEvent().getStartTime());
			if(flag==0){
				if(arg0.getEvent().getEndTime()!=null && arg1.getEvent().getEndTime()!=null){
					flag = arg0.getEvent().getEndTime().compareTo(arg1.getEvent().getEndTime());
				}else if(arg0.getEvent().getEndTime()!=null && arg1.getEvent().getEndTime() ==null){
					flag = 1;
				}else if(arg0.getEvent().getEndTime()==null && arg1.getEvent().getEndTime() !=null){
					flag = -1;
				}else{
					flag = 0;
				}
			}
		}else if(arg0.getEvent().getStartTime()!=null && arg1.getEvent().getStartTime() ==null){
			flag = 1;
		}else if(arg0.getEvent().getStartTime()==null && arg1.getEvent().getStartTime() !=null){
			flag = -1;
		}else{
			flag = 0;
		}
		return flag;
	}

}
