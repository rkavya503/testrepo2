package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class BaselineModelManagerBean implements BaselineModelManager.R, BaselineModelManager.L {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(BaselineModelManagerBean.class);

	/** The program servicer. */
	@EJB
	private BaselineModelEAO.L baselineModelEAO;

	/**
	 * Create a BaselineModel
	 * @param baselineModel the object to create
	 */
	@Override
	public void createBaselineModel(BaselineModel baselineModel) {
		try {
			baselineModelEAO.create(baselineModel);
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	/**
	 * Get a list all baselineModels
	 * @return the list of baselineModels
	 */
	@Override
	public List<BaselineModel> getBaselineModels() {
		try {
			return baselineModelEAO.getAll();
		} catch (Exception ex) {
			String message = "error getting baselinemodels";
	         // TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, ex);
		}
	}

	/**
	 * Finds the only entity represented by the given id or null
	 * if not found.
	 * @param id
	 * @return the BaselineModel
	 */
	@Override
	public BaselineModel getBaselineModelByID(String id) {
		try {
			return baselineModelEAO.getById(id);
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
	 * @return the BaselineModel
	 */
	@Override
	public BaselineModel getBaselineModelByName(String name) {
		try {
			return baselineModelEAO.getBaselineModelByName(name);
		} catch (Exception ex) {
			String message = "error getting baselinemodels";
			// TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, ex);
		}
	}

	/**
	 * Update baselineModel.
	 * 
	 * @param baselineModel the baselineModel
	 */
	@Override
	public void updateBaselineModel(BaselineModel baselineModel) {
		try {
			baselineModelEAO.update(baselineModel);
		} catch (EntityNotFoundException e) {
			String message = "error getting baselinemodels";
			// TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, e);
		}

	}

	/**
	 * Removes the baselineModel.
	 * 
	 * @param baselineModel the baselineModel
	 */
	@Override
	public void removeBaselineModel(BaselineModel baselineModel) {
		try {
			baselineModelEAO.delete(baselineModel);
		} catch (EntityNotFoundException e) {
			String message = "error getting baselinemodels";
			// TODO 2992
			//log.error(LogUtils.createLogEntry(null, null, message, null));

			throw new EJBException(message, e);
		}

	}

}
