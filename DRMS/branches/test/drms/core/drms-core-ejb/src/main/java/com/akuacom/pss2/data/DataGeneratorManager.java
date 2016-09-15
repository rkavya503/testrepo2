package com.akuacom.pss2.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;

public class DataGeneratorManager {
    private static final Logger log = Logger.getLogger(DataGeneratorManager.class);

    private String ownerId;
    private String name;
    private String dataSetName;
    private Date startDate;
    private Date endDate;
    private String fileName;

    private Properties props;
    private static final String USAGE_DEMO = "01usage_demo";
    private static final double BASE_VALUE = 2.0;
    private static final double PEAK_INCREMENT_MIN = 16.0;
    private static final double PEAK_INCREMENT_MAX = 24.0;

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public DataGenerator getInstance() {
        final SimpleDataGenerator g = new SimpleDataGenerator();
        return g;
    }

    public static void main(String[] args) {
        final DataGeneratorManager manager = new DataGeneratorManager();
        final SimpleDataGenerator g = new SimpleDataGenerator();

        final DataManager bean = EJBFactory.getBean(DataManager.class);

        PDataSource source = bean.getDataSourceByNameAndOwner("meter1", USAGE_DEMO);
        if (source == null) {
            PDataSource ds = new PDataSource();
            ds.setName("meter1");
            ds.setOwnerID(USAGE_DEMO);
            source = bean.createPDataSource(ds);
        }

        final PDataSet dataSet = bean.getDataSetByName("Usage");

        g.setDataSource(source);
        g.setDataSet(dataSet);
        g.setBaseValue(BASE_VALUE);
        g.setPeakIncrementMin(PEAK_INCREMENT_MIN);
        g.setPeakIncrementMax(PEAK_INCREMENT_MAX);
        g.setPeakReduction(15d);
        g.setReductionStartHour(14);
        g.setReductionEndHour(18);

        final List<PDataEntry> dataEntryList = g.generate(new Date(), new Date());
        final Set<PDataEntry> set = new HashSet<PDataEntry>(dataEntryList);
        bean.createDataEntries(set);
    }

    public static void process(Properties props) {
        try {
            final SimpleDataGenerator g = new SimpleDataGenerator();

            final DataManager bean = EJBFactory.getBean(DataManager.class);

            final String ownerID = props.getProperty("participant");
            PDataSource source = bean.getDataSourceByNameAndOwner("meter1", ownerID);
            if (source == null) {
                PDataSource ds = new PDataSource();
                ds.setName("meter1");
                ds.setOwnerID(ownerID);
                source = bean.createPDataSource(ds);
            }

            final PDataSet dataSet = bean.getDataSetByName("Usage");

            g.setDataSource(source);
            g.setDataSet(dataSet);
            final double baseValue = Double.parseDouble(props.getProperty("baseValue"));
            g.setBaseValue(baseValue);
            final double peakIncrementMin = Double.parseDouble(props.getProperty("peakIncrementMin"));
            g.setPeakIncrementMin(peakIncrementMin);
            final double peakIncrementMax = Double.parseDouble(props.getProperty("peakIncrementMax"));
            g.setPeakIncrementMax(peakIncrementMax);
            final double peakReduction = Double.parseDouble(props.getProperty("peakReduction"));
            g.setPeakReduction(peakReduction);
            final int reductionStartHour = Integer.parseInt(props.getProperty("reductionStartHour"));
            g.setReductionStartHour(reductionStartHour);
            final int reductionEndHour = Integer.parseInt(props.getProperty("reductionEndHour"));
            g.setReductionEndHour(reductionEndHour);
            final long intervalInMillis = Long.parseLong(props.getProperty("intervalInMillis"));
            g.setIntervalInMillis(intervalInMillis);

            final String startDate = props.getProperty("startDate");
            final String endDate = props.getProperty("endDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            final List<PDataEntry> dataEntryList;
                dataEntryList = g.generate(format.parse(startDate), format.parse(endDate));
            final Set<PDataEntry> set = new HashSet<PDataEntry>(dataEntryList);
            bean.createDataEntries(set);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
