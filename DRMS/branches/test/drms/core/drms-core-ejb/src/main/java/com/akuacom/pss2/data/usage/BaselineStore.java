//package com.akuacom.pss2.data.usage;
//
//import com.akuacom.pss2.data.PDataEntry;
//import com.akuacom.pss2.core.EJBFactory;
//import com.akuacom.pss2.system.SystemManager;
//import com.akuacom.utils.lang.DateUtil;
//
//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//
///**
// * TODO: This shall be replaced with query cache after baseline data stored in
// * DB
// */
//public class BaselineStore {
//    private static DataCache BaselineCache = new DataCache.CountLimited(100);
//
//    public static void put(String datasourceUUID, Date date,
//            List<PDataEntry> dataentryList) {
//        BaselineCache.put(new Key(datasourceUUID, date), dataentryList);
//    }
//
//    public static void remove(String datasourceUUID, Date date) {
//        BaselineCache.remove(new Key(datasourceUUID, date));
//    }
//
//    // todo aggregated baseline will not go with cache for 6.2
//    public static List<PDataEntry> get(String datasourceUUID, Date date) {
//        return getBaseline(datasourceUUID, date);
//    }
//
//    public static PDataEntry getDataEntry(String datasourceUUID, Date time) {
//        List<PDataEntry> dataentries = getBaseline(datasourceUUID, time);
//
//        for (PDataEntry pde : dataentries) {
//            if (pde.getTime().getTime() == time.getTime()) {
//                return pde;
//            }
//        }
//        return null;
//    }
//
//    private static List<PDataEntry> getBaseline(String datasourceUUID, Date time) {
//        List<PDataEntry> dataentries;
//        Date date = DateUtil.stripTime(time);
//        SystemManager sysManager = EJBFactory.getBean(SystemManager.class);
//        boolean cached = sysManager.getPss2Features()
//                .isFeatureUseCacheStoreEnabled();
//        if (cached) {
//
//            Key key = new Key(datasourceUUID, date);
//            dataentries = (List<PDataEntry>) BaselineCache.get(key);
//            if (dataentries == null) {
//                UsageDataManager udManager = EJBFactory
//                        .getBean(UsageDataManager.class);
//                List<String> ids = new ArrayList<String>();
//                ids.add(datasourceUUID);
//                dataentries = udManager.getBaselineDataEntryList(ids, date);
//                BaselineCache.put(key, dataentries);
//            }
//        } else {
//            UsageDataManager udManager = EJBFactory
//                    .getBean(UsageDataManager.class);
//            List<String> ids = new ArrayList<String>();
//            ids.add(datasourceUUID);
//            dataentries = udManager.getBaselineDataEntryList(ids, date);
//        }
//        return dataentries;
//    }
//
//    public static void expire(String datasourceUUID, Date date) {
//        Key key = new Key(datasourceUUID, date);
//        BaselineCache.expire(key);
//    }
//
//    public static class Key {
//        private String datasourceUUID;
//        private Date date;
//        private volatile int hashCode = 0;
//
//        public Key(String part, Date inputDate) {
//            datasourceUUID = part;
//            date = inputDate;
//        }
//
//        public boolean equals(Object input) {
//            if (input == this) {
//                return true;
//            }
//            if (!(input instanceof Key)) {
//                return false;
//            }
//
//            Key key = (Key) input;
//            if (key == null || key.datasourceUUID == null || key.date == null
//                    || datasourceUUID == null || date == null) {
//                return false;
//            } else {
//                return (key.datasourceUUID.equalsIgnoreCase(datasourceUUID) && key.date
//                        .getTime() == date.getTime());
//            }
//        }
//
//        public int hashCode() {
//            final int multiplier = 2;
//            if (hashCode == 0) {
//                int code = 123;
//                code = multiplier * code + date.hashCode();
//                code = multiplier * code + datasourceUUID.hashCode();
//                hashCode = code;
//            }
//            return hashCode;
//        }
//    }
//
//}
