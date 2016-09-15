package com.akuacom.pss2.data.usage;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface BaselineModelEAO extends BaseEAO<BaselineModel> {
    @Remote
    public interface R extends BaselineModelEAO {}
    @Local
    public interface L extends BaselineModelEAO {}

    /**
     * Finds the only entity represented by the given name or null if not found.
     * 
     * @param name
     * @return
     */
    BaselineModel getBaselineModelByName(String name);

}
