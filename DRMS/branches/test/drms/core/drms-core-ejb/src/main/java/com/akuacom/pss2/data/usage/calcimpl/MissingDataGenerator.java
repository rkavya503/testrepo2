//package com.akuacom.pss2.data.usage.calcimpl;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//import javax.ejb.Local;
//import javax.ejb.Remote;
//
//import com.akuacom.pss2.data.PDataEntry;
//import com.akuacom.pss2.data.PDataSet;
//import com.akuacom.pss2.data.PDataSource;
//
//
//public interface MissingDataGenerator {
//    @Remote
//    public interface R extends MissingDataGenerator {}
//    @Local
//    public interface L extends MissingDataGenerator {}
//
//    List<PDataEntry> generateFeed(List<PDataEntry> input);
//
//    List<PDataEntry> generateDisplay(List<PDataEntry> input, Date date,
//            PDataSource pds, PDataSet pdset);
//    
//    // interpolate on the fly only interpolate 
//    List<PDataEntry> generateFeedOntheFly(List<PDataEntry> usage, Collection<PDataEntry> baseline, long interval);
//}
