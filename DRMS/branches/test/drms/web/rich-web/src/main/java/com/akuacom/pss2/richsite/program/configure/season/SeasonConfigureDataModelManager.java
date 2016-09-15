package com.akuacom.pss2.richsite.program.configure.season;

import java.util.List;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.season.SeasonConfig;
/**
 *
 * Filename:    SeasonConfigureDataModelManager.java
 * Description:
 * Copyright:   Copyright (c)2010
 * Company:
 * Create at:   Feb 17, 2011 4:59:48 PM
 *
 */
public interface SeasonConfigureDataModelManager {
	/**
	 * Getter for ProgramManager instance
	 * @return ProgramManager instance
	 */
	public ProgramManager getProgramManager();
	/**
	 * Function for save SeasonConfig object into DB
	 * @param model	SeasonConfigureDataModel instance
	 * @return	result flag
	 * @throws Exception
	 */
	public String saveSeasonConfig(SeasonConfigureDataModel model) throws Exception;

    public String saveHolidayConfig(SeasonConfigureDataModel model) throws Exception;
	/**
	 * Function for get SeasonConfig object from DB
	 * @param model SeasonConfigureDataModel instance
	 * @return SeasonConfig instance list
	 */
	public List<SeasonConfig> getSeasonConfig(SeasonConfigureDataModel model);
	/**
	 * Function for get SeasonConfig instance from SeasonDataModel instance
	 * @param season SeasonDataModel instance
	 * @return SeasonConfig instance
	 */
	public SeasonConfig getSeasonConfigInstance(SeasonDataModel season);
	/**
	 * Function for get SeasonDataModel instance from SeasonConfig instance
	 * @param config SeasonConfig instance
	 * @return SeasonDataModel instance
	 */
	public SeasonDataModel getSeasonDataModelInstance(SeasonConfig config);
	/**
	 * Function for transfer List<SeasonConfig> into List<SeasonDataModel>
	 * @param seasonConfigList List<SeasonConfig> instance
	 * @return List<SeasonDataModel> instance
	 */
	public List<SeasonDataModel> buildSeasonDataModels(List<SeasonConfig> seasonConfigList);
	public List<HolidayDataModel> buildHolidayDataModels(List<SeasonConfig> seasonConfigList);
}
