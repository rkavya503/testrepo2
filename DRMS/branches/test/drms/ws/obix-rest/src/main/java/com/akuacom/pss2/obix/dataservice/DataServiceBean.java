package com.akuacom.pss2.obix.dataservice;

import static com.akuacom.utils.lang.ThreadUtil.T;
import static com.akuacom.utils.lang.TupleUtil.toTuple;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import obix.Abstime;
import obix.Bool;
import obix.Int;
import obix.List;
import obix.Obj;
import obix.Real;
import obix.Status;
import obix.Str;
import obix.Uri;
import obix.io.ObixDecoder;
import obix.io.ObixEncoder;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.utils.config.RuntimeSwitches;
import com.akuacom.utils.lang.CacheUtil;
import com.akuacom.utils.lang.ExceptionUtil;
import com.akuacom.utils.lang.StringUtil;
import com.akuacom.utils.lang.TimingUtil;

/**
 * facade that presents a high level interface to usage data storage 
 */
public class DataServiceBean {
    public static final Uri HREF = new Uri(
            "http://localhost:8080/obixserver/obix/dataService/");

    private final static Logger log = Logger.getLogger(DataServiceBean.class);
    private static final String USAGE = "usage";
    private final static int MAX_RETRIES = 5;
    private final static boolean DEBUG = false;

    private ParticipantManager.L pm;
    private DataManager.L dataManager;
    static Object DataEntryLock = new Object();
    
    public void setDataManager(DataManager.L dataManager) {
        this.dataManager = dataManager;
    }

    public void setParticipantManager(
            ParticipantManager.L participantManager) {
        this.pm = participantManager;
    }

    public DataManager.L getDataManager() {
        return this.dataManager;
    }

    public ParticipantManager.L getParticipantManager() {
        return pm;
    }

    /**
     * Wrapper method. Checks for client, then delegates to persist method.
     * 
     * @param clientId
     *            id of the client, through servlet jaas
     * @param xml
     *            xml string, uploaded by user
     * @return result xml string indicating success/failure
     */
    public String service(String clientId, String xml)
            throws AppServiceException {
        try {
            Participant client = CacheUtil.cache(pm,
                    "getParticipantOnly", toTuple(clientId, true));
    
            Obj obj = new Obj(USAGE);
            obj.setHref(HREF);
            obj.setStatus(Status.ok);
            if (client == null) {
                log.debug("couldn't find client: " + clientId
                        + ". request was ignored.");
                obj.setStatus(Status.fault);
            } else if (!persist(client.getParent(), xml)) {
                obj.setStatus(Status.fault);
            }
    
            return ObixEncoder.toString(obj);
        }catch(Exception e) {
            e.printStackTrace();
            throw new AppServiceException(e);
        }
    }
    
    /**
     * If pluggable datasources will never be supported, this could be folded back into
     * persist(..) 
     * @param ownerId
     * @param meter
     * @return
     * @throws Exception
     */
    private synchronized  PDataSource getDataSource(String ownerId, String meter) throws Exception {
        PDataSource pDataSource = CacheUtil.cache(dataManager,
                "getPDataSourceByOwnerIdAndName", toTuple(ownerId, meter));
        if (pDataSource == null) {
            if(RuntimeSwitches.DISCOVERABLE_DATASOURCES) {
                pDataSource = new PDataSource();
                pDataSource.setOwnerID(ownerId);
                pDataSource.setName("meter1");
                pDataSource.setEnabled(true);
                pDataSource = dataManager.createPDataSource(pDataSource);
                CacheUtil.flush(dataManager,
                        "getPDataSourceByOwnerIdAndName");
            } else {
                log.error(T() + "datasource data did not exist. owner id=" + ownerId
                        + " and meter " + meter);
            }
        }
        return pDataSource;
        
    }

    /**
     * Parses xml into a list of PDataEntries, removes duplicates
     * and persists what's left
     * @param ownerId
     * @param xml
     * @return
     * @throws AppServiceException
     */
    public boolean persist(String ownerId, String xml)
            throws AppServiceException {

        try {
            PDataSet pDataSet = CacheUtil.cache(dataManager, "getDataSetByName",
                    toTuple(USAGE));
            if (pDataSet == null) {
                if(DEBUG)log.info(T() + "no dataset for name " + USAGE);
                return false;
            }
    
            Obj obj = ObixDecoder.fromString(xml);
            if (obj == null) {
                if(DEBUG)log.info(StringUtil.head(xml) + " from " + ownerId + " yields null!");
            }
            final Obj[] lists = obj.list();
            String meter = "meter1";
            if (lists != null) {
                for (Obj list : lists) {
                    if (list instanceof List) {
                        meter = list.getName();
                        break;
                    }
                }
            }
            PDataSource pDataSource = getDataSource(ownerId, meter);
            if (pDataSource == null) {
                return false;
            }

            if (!pDataSource.isEnabled()) {
                if(DEBUG)log.info("datasource data isn't enabled. owner id=" + ownerId);
                return false;
            }
    
            final java.util.List<Set<PDataEntry>> dataEntrySetList = getDataEntrySetList(
                    obj, pDataSet, pDataSource);
    
            for (Set<PDataEntry> dataEntrySet : dataEntrySetList) {
                boolean success = false;
                int retries = 0;
                while (!success && !dataEntrySet.isEmpty()) {
                    try {
                        synchronized(DataEntryLock) {
                        	//dataManager
                            //.removeDuplicates(dataEntrySet, pDataSet, pDataSource);
                            if(DEBUG)log.info(T() + " about to create with " + pDataSource.getUUID());
                            dataManager.createDataEntries(dataEntrySet);
                            success = true;
                          
                        }
                   } catch (Exception e) {
                        log.info(T() + " excepted with " + pDataSource.getUUID() + " payload\n\n"+xml +"\n\n"); 
                        retries++;
                        if (!(ExceptionUtil.rootQ(e,
                                java.sql.BatchUpdateException.class) || ExceptionUtil.rootQ(e,InterruptedException.class))
                                || retries > MAX_RETRIES) {
                            if (retries > MAX_RETRIES && !ExceptionUtil.rootQ(e,
                                    java.sql.BatchUpdateException.class)) {
                                Throwable root = ExceptionUtil.root(e);
                                if(root!=null) {
                                    ExceptionUtil.logOnce(root);
                                }
                                throw new AppServiceException("exceeded "
                                        + MAX_RETRIES + " attempts, giving up, exception path: " + ExceptionUtil.path(e), e);
                            } else if(ExceptionUtil.rootQ(e,
                                java.sql.BatchUpdateException.class)){
                                log.debug("giving up on " + dataEntrySet.size() + " for " + pDataSource.getUUID() + " after " + MAX_RETRIES);
                                // spoof success, let obix client try again with next submission
                                success = true;
                            } else {
                                e.printStackTrace();
                                throw new AppServiceException(e);
                            }
                        }else {
                            log.info("ex" + ExceptionUtil.root(e));
                        }
                    }
                }
            }
            return true;    
            }catch(Exception e) {
            throw new AppServiceException(T() + "excepted" + ExceptionUtil.path(e));
        }
    }

    /**
     * Converts low level obix meter samples to PDataEntry instances suitable for 
     * hibernation
     * @param obj
     * @param pDataSet
     * @param pDataSource
     * @return
     */
    public java.util.List<Set<PDataEntry>> getDataEntrySetList(Obj obj,
            PDataSet pDataSet, PDataSource pDataSource) {

        final java.util.List<Set<PDataEntry>> dataEntrySetList = new ArrayList<Set<PDataEntry>>();

        final Obj[] lists = obj.list();
        if (lists != null) {
            for (Obj list : lists) {
                final Set<PDataEntry> dataEntries = new HashSet<PDataEntry>();

                // list level, maps to DataSet table
                if (list instanceof List) {
                    final Obj[] entries = list.list();
                    String meter = list.getName();
                    // entry obj level, maps to DataEntry table
                    for (Obj entry : entries) {
                        final PDataEntry dataEntry = new PDataEntry();
                        final Obj[] attrs = entry.list();
                        // attr obj level, maps to DataEntry table's columns
                        for (Obj attr : attrs) {
                            if (attr.isAbstime()) {
                                // get time
                                Date time = new Date(((Abstime) attr).get());
                                dataEntry.setTime(new java.sql.Date(
                                        ((Abstime) attr).get()));
                            } else {
                                // get value
                                if (attr.isBool()) {
                                    final boolean bool = ((Bool) attr).get();
                                    int intBool = (bool) ? 1 : 0;
                                    dataEntry.setValue(Double.valueOf(String
                                            .valueOf(intBool)));
                                } else if (attr.isInt()) {
                                    final Int val = (Int) attr;
                                    dataEntry.setValue(Double.valueOf((val
                                            .get())));
                                } else if (attr.isReal()) {
                                    final Real real = (Real) attr;
                                    dataEntry.setValue(Double.valueOf(real
                                            .get()));
                                } else if (attr.isStr()) {
                                    final Str str = (Str) attr;
                                    dataEntry.setValueType(String.class
                                            .getName());
                                    dataEntry.setStringValue(str.get());
                                } else {
                                    // error
                                }
                            }
                        }
                        dataEntry.setDataSet(pDataSet);
                        dataEntry.setDatasource(pDataSource);
                        dataEntry.setActual(true);
                        dataEntry.setTime(TimingUtil.clipMillis(dataEntry.getTime()));
                        dataEntries.add(dataEntry);
                    }
                } else {
                    // error
                }

                dataEntrySetList.add(dataEntries);
            }
        } else {
            // error
        }
        return dataEntrySetList;
    }

}
