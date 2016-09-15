package com.akuacom.pss2.data;

public abstract class AbstractDataGenerator implements DataGenerator {
    protected PDataSet dataSet;
    protected PDataSource dataSource;

    public PDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(PDataSet dataSet) {
        this.dataSet = dataSet;
    }

    public PDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
