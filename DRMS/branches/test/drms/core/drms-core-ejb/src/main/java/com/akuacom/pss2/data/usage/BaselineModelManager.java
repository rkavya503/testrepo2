package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * Business object facade for working with baselineModels.
 */

public interface BaselineModelManager {
    @Remote
    public interface R extends BaselineModelManager {}
    @Local
    public interface L extends BaselineModelManager {}

    /**
     * Create a BaselineModel
     * 
     * @param baselineModel
     *            the object to create
     */
    void createBaselineModel(BaselineModel baselineModel);

    /**
     * Get a list all baselineModels
     * 
     * @return the list of baselineModels
     */
    List<BaselineModel> getBaselineModels();

    /**
     * Finds the only entity represented by the given id or null if not found.
     * 
     * @param id
     * @return the BaselineModel
     */
    BaselineModel getBaselineModelByID(String id);

    /**
     * Finds the only entity represented by the given name or null if not found.
     * 
     * @param name
     * @return the BaselineModel
     */
    BaselineModel getBaselineModelByName(String name);

    /**
     * Update baselineModel.
     * 
     * @param baselineModel
     *            the baselineModel
     */
    void updateBaselineModel(BaselineModel baselineModel);

    /**
     * Removes the baselineModel.
     * 
     * @param baselineModel
     *            the baselineModel
     */
    void removeBaselineModel(BaselineModel baselineModel);

}