package com.akuacom.pss2.data.usage.calcimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.BaselineModel;

@Stateless
public class BaselineTenTenModelBean extends BaselineThreeTenModelBean
        implements BaselineTenTenModel.R, BaselineTenTenModel.L {

    public static String MODELNAME = "TenTen";

    
    
    public List<PDataEntry> calculate(List<PDataSource> dataSourceList, Date date, Set<Date> holidays, String[] excludedPrograms, BaselineModel mb, List<BaselineConfig> bcs, double missingDataThreshold, PDataSet dataSet){
        // only one now
        BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];
        DateRange dr = ImplFactory.instance().getDaysSelector(mb.getDayPickerImplClass()).getBaseDateRange(dataSourceList,date, bc, holidays, excludedPrograms);

        
        List<String> datasourceUUIDs = new ArrayList<String>();
        for(PDataSource dSource : dataSourceList){
        	datasourceUUIDs.add(dSource.getUUID());
        }
        
        
        List<PDataEntry> deList = this.dataEntryEAO
                .getDataEntryForByDataSourceUUIDsAndDates(datasourceUUIDs,
                        dr.getStartTime(), dr.getEndTime());

        List<PDataEntry> ret = ImplFactory.instance()
                .getBaselineCalculation(mb.getCalcImplClass())
                .calculate(deList, bc, date, dr, missingDataThreshold, dataSet);
        return ret;

    
    }

    
    public List<PDataEntry> calculate(List<String> datasourceUUIDs, Date date) {
        BaselineModel mb = bmManager
                .getBaselineModelByName(BaselineTenTenModelBean.MODELNAME);
//        Set<BaselineConfig> bcs = mb.getBaselineConfigs();
        List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
        // only one now
        BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];
        DateRange dr = ImplFactory.instance().getDaysSelector(mb.getDayPickerImplClass()).getBaseDateRange(datasourceUUIDs,date, bc);

        List<PDataEntry> deList = this.dataEntryEAO
                .getDataEntryForByDataSourceUUIDsAndDates(datasourceUUIDs,
                        dr.getStartTime(), dr.getEndTime());

        List<PDataEntry> ret = ImplFactory.instance()
                .getBaselineCalculation(mb.getCalcImplClass())
                .calculate(deList, bc, date, dr );
        return ret;
    }

}