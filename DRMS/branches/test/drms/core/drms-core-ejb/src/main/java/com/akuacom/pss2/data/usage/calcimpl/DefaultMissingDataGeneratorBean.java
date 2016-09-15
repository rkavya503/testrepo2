//package com.akuacom.pss2.data.usage.calcimpl;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import javax.ejb.Stateless;
//
//import com.akuacom.pss2.data.DataManagerBean;
//import com.akuacom.pss2.data.PDataEntry;
//import com.akuacom.pss2.data.PDataSet;
//import com.akuacom.pss2.data.PDataSource;
//import com.akuacom.pss2.data.usage.BaselineStore;
//import com.akuacom.utils.lang.DateUtil;
//
//@Stateless
//public class DefaultMissingDataGeneratorBean extends DataManagerBean implements
//        MissingDataGenerator.R, MissingDataGenerator.L {
//
//	// interpolate
//    public List<PDataEntry> generateFeed(List<PDataEntry> input) {
//        if (input == null || input.size() <= 0) {
//            return input;
//        }
//
//        List<PDataEntry> output = new ArrayList<PDataEntry>();
//
//        /*
//         * Default logic:
//         * 
//         * sort input by time asc loop input if anyone has more than 15mins gap
//         * from previous point, make up missing ones.
//         */
//        PDataSource pds = input.get(0).getDatasource();
//        PDataSet pdset = input.get(0).getDataSet();
//        String datasourceUUID = pds.getUUID();
//
//        SortedMap<Date, PDataEntry> map = new TreeMap<Date, PDataEntry>();
//        
//        for(PDataEntry entry:input){
//        	map.put(entry.getTime(), entry);
//        }
//
//        Collection<PDataEntry> list = map.values();
//        PDataEntry preEntry = null;
//        for (PDataEntry de : list) {
//            de.setActual(true);
//            if (preEntry == null) {
//                preEntry = getLatestDataEntry(pds.getUUID(), pdset.getUUID(), de.getTime());
//            }
//			if (preEntry == null || 
//			    preEntry.getTime().getTime() + DateUtil.MSEC_IN_SEC * pdset.getPeriod() == de.getTime().getTime()) {
//	            output.add(de);
//	            preEntry = de;
//	            continue;
//            } else {
//                // make up data in between
//                output.addAll(generateEntriesForFeed(preEntry, de, datasourceUUID));
//                output.add(de);
//                preEntry = de;
//            }
//        }
//
//        return output;
//    }
//
//    private List<PDataEntry> generateEntriesForFeed(PDataEntry start,
//            PDataEntry end, String datasourceUUID) {
//        List<PDataEntry> ret;
//        Date stime = start.getTime();
//        Date etime = end.getTime();
//
//        PDataEntry bStart = BaselineStore.getDataEntry(datasourceUUID, stime);
//        PDataEntry bEnd = BaselineStore.getDataEntry(datasourceUUID, etime);
//        
//        if(bStart==null||bEnd==null){
//        	//handle the day with no baseline
//        	return new ArrayList<PDataEntry>();
//        }
//        
//        double soffset = start.getValue() - bStart.getValue();
//        double eoffset = end.getValue() - bEnd.getValue();
//        long count = getPoints(start.getTime(), end.getTime(),
//                start.getDataSet())-1;
//        double step = (eoffset - soffset) / count;
//        ret = generateEntries(start, datasourceUUID, count, soffset, step);
//
//        return ret;
//    }
//
//    private List<PDataEntry> generateEntries(PDataEntry start,
//            String datasourceUUID, long count, double soffset, double step) {
//        List<PDataEntry> ret = new ArrayList<PDataEntry>();
//        // Modified by Frank for DRMS-2384: user graph spinning when the whole
//        // day's data is missing for today
//        
//        Date startDate = start.getTime();
//        List<PDataEntry> dataentries = null;
//        Map<Long, Double> dataMap = new HashMap<Long, Double>();
//        while(!DateUtil.stripTime(startDate).after(DateUtil.stripTime(new Date()))){
//        	dataentries = BaselineStore.get(datasourceUUID,startDate);
//        	 for (PDataEntry pde : dataentries) {
//                 dataMap.put(pde.getTime().getTime(), pde.getValue());
//             }
//        	 
//    		Calendar cal = new GregorianCalendar();
//        	cal.setTime(startDate);
//        	cal.add(Calendar.DATE, 1);
//        	startDate = cal.getTime();
//        }
//
//        for (long i = 0; i < count; i++) {
//            Date itime = new Date(start.getTime().getTime()
//                    + DateUtil.MSEC_IN_SEC * start.getDataSet().getPeriod()
//                    * (i + 1));
//            // Modified by Frank for DRMS-2384: user graph spinning when the
//            // whole day's data is missing for today
//            // PDataEntry baseDE = BaselineStore.getDataEntry(datasourceUUID,
//            // itime);
//            // double value = baseDE.getValue() + (soffset + step*(i+1));
//            double value = dataMap.get(itime.getTime())
//                    + (soffset + step * (i + 1));
//            PDataEntry de = new PDataEntry();
//            de.setValue(value);
//            de.setTime(itime);
//            de.setStringValue(start.getStringValue());
//            de.setValueType(start.getValueType());
//            de.setDataSet(start.getDataSet());
//            de.setDatasource(start.getDatasource());
//            de.setActual(false);
//            ret.add(de);
//        }
//
//        return ret;
//    }
//
//    // extrapolate
//    public List<PDataEntry> generateDisplay(List<PDataEntry> input, Date date,
//            PDataSource pds, PDataSet pdset) {
//        List<PDataEntry> output = new ArrayList<PDataEntry>();
//
//        String datasourceUUID = pds.getUUID();
//
//        double soffset;
//
//        List<PDataEntry> baseline = BaselineStore.get(datasourceUUID, date);
//        if (baseline == null || baseline.size() <= 0) {
//            // handle the day with no baseline
//            return input;
//        }
//        // interpolate even missing data for a whole day
//        if (input == null || input.size() <= 0) {
//            // handle the empty day
//            // logic here to calculate offset
//            PDataEntry entry;
//            entry = getLatestDataEntry(pds.getUUID(), pdset.getUUID(), date);
//            if (entry == null) {
//                return input;
//            }
//
//            PDataEntry latestBaseentry = BaselineStore.getDataEntry(datasourceUUID,entry.getTime());
//            if(latestBaseentry==null){
//            	return input;
//            }
//            soffset = entry.getValue() - latestBaseentry.getValue();
//            PDataEntry start = BaselineStore.getDataEntry(datasourceUUID,
//                    DateUtil.stripTime(date));
//            // Modified by Frank for DRMS-2384: user graph spinning when the
//            // whole day's data is missing for today
//            output.add(start);
//            start.setValue(start.getValue() + soffset);
//            long count = getPoints(start.getTime(), date, start.getDataSet());
//            output.addAll(generateEntries(start, datasourceUUID, count,
//                    soffset, 0));
//            return output;
//        }
//
//        PDataEntry start = input.get(input.size() - 1);
//        if (start.getTime().getTime() >= date.getTime()) {
//            return input;
//        }
//
//        long count = getPoints(start.getTime(), date, start.getDataSet());
//        PDataEntry bStart = BaselineStore.getDataEntry(datasourceUUID,
//                start.getTime());
//        // TODO: some times throws nullPointerException
//        if (bStart == null) {
//            return input;
//        }
//        soffset = start.getValue() - bStart.getValue();
//        double step = 0;
//        input.addAll(generateEntries(start, datasourceUUID, count, soffset,
//                step));
//        return input;
//    }
//    
//    public static long getPoints(Date start, Date end, long interval) {
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(end);
//
//        if (calendar.get(Calendar.HOUR_OF_DAY) == 0
//                && calendar.get(Calendar.MINUTE) == 0
//                && calendar.get(Calendar.SECOND) == 0) {
//            calendar.add(Calendar.MINUTE, -15);
//        }
//
//        return (calendar.getTime().getTime() - start.getTime())
//                / (DateUtil.MSEC_IN_SEC * interval);
//    }
//
//    public static long getPoints(Date start, Date end, PDataSet dset) {
//        // Modified by Frank for DRMS-2384: user graph spinning when the whole
//        // day's data is missing for today
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(end);
//
//        if (calendar.get(Calendar.HOUR_OF_DAY) == 0
//                && calendar.get(Calendar.MINUTE) == 0
//                && calendar.get(Calendar.SECOND) == 0) {
//            calendar.add(Calendar.MINUTE, -15);
//        }
//
//        return (calendar.getTime().getTime() - start.getTime())
//                / (DateUtil.MSEC_IN_SEC * dset.getPeriod());
//        // return (end.getTime() -
//        // start.getTime())/(DateUtil.MSEC_IN_SEC*dset.getPeriod());
//    }
//
//    // interpolate on the fly only interpolate 
//    //TODO: make sure usage and baseline are sorted 
//    /**
//     * Feed on the fly  public method
//     * depends on :generateEntriesForDisplay
//     * and: generateEntriesFly
//     * 
//     */
//	@Override
//	public List<PDataEntry> generateFeedOntheFly(List<PDataEntry> usage,
//		Collection<PDataEntry> baseline, long interval) {
//		
//	    List<PDataEntry> output = new ArrayList<PDataEntry>();
//        if (baseline == null || baseline.size() <= 0) {
//            // handle the day with no baseline
//            return usage;
//        }
//        
//        if (usage == null || usage.size() <= 0) {
//            // Don't make up fabricated data when a whole day missing
//        	return Collections.emptyList();
//        }
//
//        SortedMap<Long, Double> map = new TreeMap<Long, Double>();
//        
//        for(PDataEntry entry:baseline){
//        	map.put(entry.getTime().getTime(), entry.getValue());
//        }
//
//
//        PDataEntry preEntry = null;
//        for (PDataEntry de : usage) {
//            de.setActual(true);
//			if (preEntry == null || 
//			    preEntry.getTime().getTime() + DateUtil.MSEC_IN_SEC * interval == de.getTime().getTime()) {
//				// no missing data
//	            output.add(de);
//	            preEntry = de;
//	            continue;
//            } else {
//                // make up data in between
//                output.addAll(generateEntriesForDisplay(preEntry, de, map, interval));
//                output.add(de);
//                preEntry = de;
//            }
//        }
//        
//        return output;
//	  
//	}
//	
//	/**
//	 * just for make up missing data on the fly
//	 * @param start
//	 * @param end
//	 * @param datasourceUUID
//	 * @return
//	 */
//    private List<PDataEntry> generateEntriesForDisplay(PDataEntry start,
//            PDataEntry end, SortedMap<Long, Double> map, long interval) {
//        List<PDataEntry> ret;
//        Date stime = start.getTime();
//        Date etime = end.getTime();
//
//        Double bStart = map.get(stime.getTime());
//        Double bEnd =  map.get(etime.getTime());
//        
//        if(bStart==null||bEnd==null){
//        	//handle the day with no baseline
//        	return new ArrayList<PDataEntry>();
//        }
//        
//        double soffset = start.getValue() - bStart;
//        double eoffset = end.getValue() - bEnd;
//        long count = getPoints(start.getTime(), end.getTime(), interval)-1;
//        double step = (eoffset - soffset) / count;
//        ret = generateEntriesFly(start, count, soffset, step, map);
//
//        return ret;
//    }
//    
//    private List<PDataEntry> generateEntriesFly(PDataEntry start,
//            long count, double soffset, double step, Map<Long, Double> dataMap) {
//        List<PDataEntry> ret = new ArrayList<PDataEntry>();
//        for (long i = 0; i < count; i++) {
//            Date itime = new Date(start.getTime().getTime()
//                    + DateUtil.MSEC_IN_SEC * start.getDataSet().getPeriod()
//                    * (i + 1));
//            double value = dataMap.get(itime.getTime())
//                    + (soffset + step * (i + 1));
//            PDataEntry de = new PDataEntry();
//            de.setValue(value);
//            de.setTime(itime);
//            de.setStringValue(start.getStringValue());
//            de.setValueType(start.getValueType());
//            de.setDataSet(start.getDataSet());
//            de.setDatasource(start.getDatasource());
//            de.setActual(false);
//            ret.add(de);
//        }
//
//        return ret;
//    }
//}
