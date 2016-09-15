package com.akuacom.pss2.data.usage.calcimpl;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.usage.BaselineConfig;

public interface BaseLineMABase {
    @Local interface L extends BaseLineMABase{}
    List<PDataEntry> adjust(BaselineConfig bc,  List<PDataEntry> baselineDataEntryList, List<PDataEntry> usageDataEntryList, Date date, int start, int end);
}
