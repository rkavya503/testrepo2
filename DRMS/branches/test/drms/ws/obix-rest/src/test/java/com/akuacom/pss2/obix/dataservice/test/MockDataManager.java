package com.akuacom.pss2.obix.dataservice.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import scala.Tuple2;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DataManagerBean;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

public class MockDataManager extends DataManagerBean {

    private List<PDataEntry> dataEntry = new ArrayList<PDataEntry>();
    private List<PDataSet> dataSet = new ArrayList<PDataSet>();
    private List<PDataSource> dataSource = new ArrayList<PDataSource>();

    public List<PDataEntry> getDataEntry() {
        return dataEntry;
    }

    public List<PDataSet> getDataSet() {
        return dataSet;
    }

    public List<PDataSource> getDataSource() {
        return dataSource;
    }

    @Override
    public void createDataEntries(Set<PDataEntry> dataEntryList) {
        dataEntry.addAll(dataEntryList);
    }

    @Override
    public PDataEntry createPDataEntry(PDataEntry de) {
        dataEntry.add(de);
        return de;
    }

    @Override
    public PDataSet createPDataSet(PDataSet dataset) {
        dataSet.add(dataset);
        return dataset;
    }

    @Override
    public List<PDataSet> getDataSets() {
        return null;
    }

    @Override
    public PDataSource createPDataSource(PDataSource ds) {
        dataSource.add(ds);
        return ds;
    }


    @Override
    public List<Date> getDataDays(List<String> datasetUUID,
            String datasourceUUID, DateRange range)
            throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public PDataSet getDataSetByName(String name) {
        for (PDataSet ds : dataSet) {
            if (ds.getName().equals(name))
                return ds;
        }
        return null;
    }

    @Override
    public PDataSource getDataSourceByNameAndOwner(String name, String owner) {
        for (PDataSource ds : dataSource) {
            if (ds.getName().equals(name) && ds.getOwnerID().equals(owner))
                return ds;
        }
        return null;
    }

    @Override
    public List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException {
        ArrayList<String> ls = new ArrayList<String>();
        for (PDataSource ds : dataSource) {
            if (ds.getName().equals(ownerID))
                ls.add(ds.getName());
        }
        return ls;
    }

    @Override
    public Date getLastestContact(String datasourceUUID, String datasetUUID)
            throws EntityNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public PDataSet getPDataSet(String uuid) throws EntityNotFoundException {
        for (PDataSet ds : dataSet) {
            if (ds.getUUID().equals(uuid))
                return ds;
        }
        return null;
    }


    @Override
    public PDataSource getPDataSourceByOwnerIdAndName(String ownerId,
            String name) {
        for (PDataSource ds : dataSource) {
            if (ds.getName().equals(name) && ds.getOwnerID().equals(ownerId))
                return ds;
        }
        return null;
    }


    @Override
    public List<Tuple2<String, Date>> getExisting(String datasetUUID,
            String datasourceUUID, Set<Date> times) {
        List<Tuple2<String, Date>> result = new ArrayList<Tuple2<String, Date>>();
        for (PDataEntry pDataEntry : dataEntry) {
            if (pDataEntry.getDataSet().getUUID().equals(datasetUUID)
                    && pDataEntry.getDatasource().getUUID()
                            .equals(datasourceUUID)
                    && times.contains(pDataEntry.getTime())       ) {
                result.add(new Tuple2<String,Date>(pDataEntry.getUUID(), pDataEntry.getTime()));
            }
        }
        return result;
    }

    @Override
    public void removeDuplicates(Collection<PDataEntry> dataEntries,
            PDataSet pDataSet, PDataSource pDataSource) {
       
        Iterator<PDataEntry> i = dataEntries.iterator();
        while(i.hasNext()) {
            PDataEntry pde = i.next();
            for (PDataEntry pDataEntry : dataEntry) {
           
                if (pDataEntry.getDataSet().getUUID().equals(pde.getDataSet().getUUID())
                    && pDataEntry.getDatasource().getUUID()
                            .equals(pde.getDatasource().getUUID())
                    && pDataEntry.getTime().equals(pde.getTime())) {
                    i.remove();
                }
            }
        }
    }

	
}
