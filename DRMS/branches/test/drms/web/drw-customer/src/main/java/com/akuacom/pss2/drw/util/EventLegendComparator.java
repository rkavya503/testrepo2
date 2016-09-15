package com.akuacom.pss2.drw.util;

import java.util.Comparator;
import java.util.Date;

import com.akuacom.pss2.drw.value.EventLegend;


public class EventLegendComparator implements Comparator<EventLegend> {


	@Override
//	Returns:
//		the value 0 if the argument Date is equal to this Date; a value less than 0 if this Date is before the Date argument; and a value greater than 0 if this Date is after the Date argument. 
	public int compare(EventLegend arg0, EventLegend arg1) {
		
		int flag = 0;
		if(arg0==null||arg1==null||arg0.getStartTime()==null||arg1.getStartTime()==null){
			return flag;
		}
		if(arg0.getStartTime()!=null && arg1.getStartTime()!=null){
			flag = arg0.getStartTime().compareTo(arg1.getStartTime());
			if(flag==0){
				Date endTime0 = arg0.getActualEndTime()!=null?arg0.getActualEndTime():arg0.getEstimatedEndTime();
				Date endTime1 = arg1.getActualEndTime()!=null?arg1.getActualEndTime():arg1.getEstimatedEndTime();
				
				if(endTime0!=null && endTime1!=null){
					flag = endTime0.compareTo(endTime1);
				}else if(endTime0!=null && endTime1 ==null){
					flag = 1;
				}else if(endTime0==null && endTime1 !=null){
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