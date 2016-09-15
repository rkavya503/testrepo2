package com.akuacom.pss2.richsite.gridpoint;

import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.gridpoint.GridPointConfiguration;
import com.akuacom.pss2.data.gridpoint.GridPointConfigurationGenEAO;
import com.akuacom.pss2.data.gridpoint.GridPointTimerManager;
import com.akuacom.pss2.richsite.FDUtils;

public class GridPointConfigureManager{
	/** The log */
	private static final Logger log = Logger.getLogger(GridPointConfigureManager.class);
	private GridPointConfigurationGenEAO configurationEAO;
	private static GridPointConfigureManager instance;
	private GridPointTimerManager gridPointTimerManager;
	
	private GridPointConfigureManager(){
		super();
	}
	public static GridPointConfigureManager getInstance(){
		if(instance == null){
			instance = new GridPointConfigureManager();
		}
		return instance;
	}
	
	public GridPointConfiguration retrieveConfigureDataModel(){
		
		List<GridPointConfiguration> result = getConfigurationEAO().getAll();
		if(result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	public GridPointConfiguration mergeConfigureDataModel(GridPointConfiguration dataModel){
		if(dataModel!=null){
			//dataModel = getConfigurationEAO().merge(dataModel);
			try {
				dataModel = getConfigurationEAO().update(dataModel);
			} catch (EntityNotFoundException e) {
				log.info("update record from the table grid_point_configuration failed."+e.getMessage());
				FDUtils.addMsgError("update record from the table grid_point_configuration failed."+e.getMessage());
			}
			getGridPointTimerManager().createTimers();
		}
		return dataModel;
	}
	public GridPointConfigurationGenEAO getConfigurationEAO() {
		if(configurationEAO==null){
			configurationEAO = (GridPointConfigurationGenEAO)EJBFactory.getBean(GridPointConfigurationGenEAO.class);
		}
		return configurationEAO;
	}
	
	public GridPointTimerManager getGridPointTimerManager() {
		if(gridPointTimerManager==null){
			gridPointTimerManager = (GridPointTimerManager)EJBFactory.getBean(GridPointTimerManager.class);
		}
		return gridPointTimerManager;
	}
	
}
