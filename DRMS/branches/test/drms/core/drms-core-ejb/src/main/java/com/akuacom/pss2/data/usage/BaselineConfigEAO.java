package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface BaselineConfigEAO extends BaseEAO<BaselineConfig>{
    @Remote
    public interface R extends BaselineConfigEAO {}
    @Local
    public interface L extends BaselineConfigEAO {}
	
	/**
	 * Finds the only entity represented by the given name or null
	 * if not found.
	 * @param name
	 * @return
	 */
	BaselineConfig getBaselineConfigByName(String name);
	
	/**
	 * Finds the only entity represented by the given owner_uuid 
	 * return null if not found or more than 1 result.
	 * @param uuid
	 * @return
	 */
	BaselineConfig getBaselineConfigByOwnerUUID(String uuid);
	
	/**
	 * Finds all the Entities represented by the given baselineModel 
	 * @param baselineModel
	 * @return
	 */
	List<BaselineConfig> getBaselineConfigByBaselineModel(BaselineModel baselineModel);
	
	
}
