package com.akuacom.pss2.data.usage.calcimpl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang.time.DateUtils;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.UsageUtil;

@Stateless
public class BaseLineMABean implements BaseLineMA, BaseLineMABase.L {
	@Override
	public List<PDataEntry> adjust(BaselineConfig bc, List<PDataEntry> baselineDataEntryList, List<PDataEntry> usageDataEntryList, Date date, int start, int end) {
		
		if(null==usageDataEntryList || null==baselineDataEntryList) return baselineDataEntryList;
		
		double ratio = getRatio(baselineDataEntryList, usageDataEntryList, bc,
				 date, start, end);
		
		adjustAsMARatio(baselineDataEntryList, ratio);
		
		return baselineDataEntryList;
	}

	private void adjustAsMARatio(List<PDataEntry> baselineDataEntryList,
			double ratio) {
		Iterator<PDataEntry> item = baselineDataEntryList.iterator();
		while(item.hasNext()){
			PDataEntry entry = item.next();
			entry.setValue(entry.getValue()*(1+ratio));
			entry.setActual(true);
		}
	}

	@SuppressWarnings("unchecked")
	public double getRatio(List<PDataEntry> baselineDataEntryList,List<PDataEntry> usageDataEntryList,
			BaselineConfig bc,
			Date date, int start, int end) {
		Date currentDate = new Date();
		long endTime= 0;
		if(DateUtils.isSameDay(currentDate, date)){
			//calculate ma for the current day
			long currentTime = UsageUtil.getCurrentTime(new Date());
			endTime = currentTime>end? end:currentTime;
		}else{
			//calculate ma for the past day
			endTime = end;
		}
		
		DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(start, endTime, DateEntrySelectPredicate.BETWEEN_START_END, PDataEntry.class, "time");
		
		 Iterator<PDataEntry> dataEntrys =
             new FilterIterator( usageDataEntryList.iterator(), predicate );
		 double sumUsage = 0;
		 double sumBaseline = 0;
		 while(dataEntrys.hasNext()){
			 PDataEntry cur = dataEntrys.next();
			 DateEntrySelectPredicate equalPredicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(cur.getTime()), 0, DateEntrySelectPredicate.EQUAL_START, PDataEntry.class, "time");
			 Iterator<PDataEntry> baseDataEntrys =
	             new FilterIterator( baselineDataEntryList.iterator(), equalPredicate );
			 if(baseDataEntrys.hasNext()){
				 sumUsage += cur.getValue();
				 sumBaseline += baseDataEntrys.next().getValue();
			 }
			 
		 }
		 double avg = (sumUsage-sumBaseline)*1.0/(sumBaseline*1.0);
		 
		 avg = avg>bc.getMaxMARate()? bc.getMaxMARate():avg;
		 avg = avg<bc.getMinMARate()? bc.getMinMARate():avg;
		 
		 avg = Double.isNaN(avg)? 0.0:avg;
		 
		 return avg;// always return 0?
	}
	
}
