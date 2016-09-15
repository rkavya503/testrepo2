package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.util.LogUtils;

/**
 * Business object facade for working with baselineConfigs.
 */
@Stateless
public class BaselineConfigManagerBean implements BaselineConfigManager.R, BaselineConfigManager.L {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(BaselineConfigManagerBean.class);

	/** The program servicer. */
	@EJB
	private BaselineConfigEAO.L baselineConfigEAO;

	/**
	 * Create a BaselineConfig.
	 * @param baselineConfig the baselineConfig to create
	 */
	@Override
	public void createBaselineConfig(BaselineConfig baselineConfig) {
		try {
			baselineConfigEAO.create(baselineConfig);
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	/**
	 * Get a list all BaselineConfig.
	 * 
	 * @return the list of BaselineConfig
	 */
	@Override
	public List<BaselineConfig> getBaselineConfigs() {
		return baselineConfigEAO.getAll();
	}

	/**
	 * Get a baselineConfig by ID.
	 * @param id
	 * @return baselineConfig entity load by the given ID
	 */
	@Override
	public BaselineConfig getBaselineConfigByID(String id) {
		try {
			return baselineConfigEAO.getById(id);
		} catch (EntityNotFoundException e) {
			String message = "error getting baselinemodels";
			// TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, e);
		}
	}

	/**
	 * Finds the only entity represented by the given name or null
	 * if not found.
	 * @param name
	 * @return
	 */
	@Override
	public BaselineConfig getBaselineConfigByName(String name) {
		return baselineConfigEAO.getBaselineConfigByName(name);
	}

	/**
	 * Finds the only entity represented by the given owner_uuid 
	 * return null if not found or more than 1 result.
	 * @param uuid
	 * @return
	 */
	@Override
	public BaselineConfig getBaselineConfigByOwnerUUID(String uuid) {
		return baselineConfigEAO.getBaselineConfigByOwnerUUID(uuid);
	}

	/**
	 * Finds all the Entities represented by the given baselineModel 
	 * @param baselineModel
	 * @return
	 */
	@Override
	public List<BaselineConfig> getBaselineConfigByBaselineModel(
			BaselineModel baselineModel) {
		return baselineConfigEAO.getBaselineConfigByBaselineModel(baselineModel);
	}

	/**
	 * Update baselineConfig.
	 * 
	 * @param baselineConfig the baselineConfig
	 */
	@Override
	public void updateBaselineConfig(BaselineConfig baselineConfig) {
		try {
			baselineConfigEAO.update(baselineConfig);
		} catch (EntityNotFoundException e) {
			String message = "error getting baselinemodels";
			// TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, e);
		}

	}

	/**
	 * Removes the baselineConfig.
	 * 
	 * @param baselineConfig the baselineConfig name
	 */
	@Override
	public void removeBaselineConfig(BaselineConfig baselineConfig) {
		try {
			baselineConfigEAO.delete(baselineConfig);
		} catch (EntityNotFoundException e) {
			String message = "error getting baselinemodels";
            // TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, e);
		}

	}

}
