package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface BaselineConfigManager {
    @Remote
    public interface R extends BaselineConfigManager {}
    @Local
    public interface L extends BaselineConfigManager {}

    /**
     * Create a BaselineConfig.
     * 
     * @param baselineConfig
     *            the baselineConfig to create
     */
    void createBaselineConfig(BaselineConfig baselineConfig);

    /**
     * Get a list all BaselineConfig.
     * 
     * @return the list of BaselineConfig
     */
    List<BaselineConfig> getBaselineConfigs();

    /**
     * Get a baselineConfig by ID.
     * 
     * @param id
     * @return baselineConfig entity load by the given ID
     */
    BaselineConfig getBaselineConfigByID(String id);

    /**
     * Finds the only entity represented by the given name or null if not found.
     * 
     * @param name
     * @return
     */
    BaselineConfig getBaselineConfigByName(String name);

    /**
     * Finds the only entity represented by the given owner_uuid return null if
     * not found or more than 1 result.
     * 
     * @param uuid
     * @return
     */
    BaselineConfig getBaselineConfigByOwnerUUID(String uuid);

    /**
     * Finds all the Entities represented by the given baselineModel
     * 
     * @param baselineModel
     * @return
     */
    List<BaselineConfig> getBaselineConfigByBaselineModel(
            BaselineModel baselineModel);

    /**
     * Update baselineConfig.
     * 
     * @param baselineConfig
     *            the baselineConfig
     */
    void updateBaselineConfig(BaselineConfig baselineConfig);

    /**
     * Removes the baselineConfig.
     * 
     * @param baselineConfig
     *            the baselineConfig name
     */
    void removeBaselineConfig(BaselineConfig baselineConfig);
}
