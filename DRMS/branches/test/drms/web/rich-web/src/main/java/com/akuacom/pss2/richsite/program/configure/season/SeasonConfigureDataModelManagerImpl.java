package com.akuacom.pss2.richsite.program.configure.season;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.utils.lang.DateUtil;
import java.io.Serializable;
import java.util.Date;

public class SeasonConfigureDataModelManagerImpl implements SeasonConfigureDataModelManager,Serializable{

	private ProgramManager programManager = EJB3Factory.getLocalBean(ProgramManagerBean.class);
	/**
	 * Function for get SeasonConfig object from DB
	 * @param model SeasonConfigureDataModel instance
	 * @return SeasonConfig instance list
	 */
	public List<SeasonConfig> getSeasonConfig(SeasonConfigureDataModel model){
		if(model!=null&&model.getProgramConfigureDataModel()!=null){
			return programManager.findSeasonConfigsByProgramId(model.getProgramConfigureDataModel().getProgram().getUUID());
		}else{
			return new ArrayList<SeasonConfig>();
		}
	}
	/**
	 * Function for save SeasonConfig object into DB
	 * @param model	SeasonConfigureDataModel instance
	 * @return	result flag
	 */
	public String saveSeasonConfig(SeasonConfigureDataModel model) throws Exception{
		if(model!=null&&model.getProgramConfigureDataModel()!=null){
			model.getProgramConfigureDataModel().setRenewFlag(false);
			Set<SeasonConfig> result = buildSeasonHolidayConfigListBeforeSave(model);
			programManager.saveSeasonConfig(result, model.getProgramConfigureDataModel().getProgramName());


        }
		return "";
	}

    	/**
	 * Function for save SeasonConfig object into DB
	 * @param model	SeasonConfigureDataModel instance
	 * @return	result flag
	 */
	public String saveHolidayConfig(SeasonConfigureDataModel model) throws Exception{
		if(model!=null&&model.getProgramConfigureDataModel()!=null){
			model.getProgramConfigureDataModel().setRenewFlag(false);
			Set<SeasonConfig> result = buildHolidayConfigListBeforeSave(model);
			programManager.saveSeasonConfig(result, model.getProgramConfigureDataModel().getProgramName());
		}
		return "";
	}

	/**
	 * Function for transfer List<SeasonConfig> into List<SeasonDataModel>
	 * @param seasonConfigList List<SeasonConfig> instance
	 * @return List<SeasonDataModel> instance
	 */
	public List<SeasonDataModel> buildSeasonDataModels(List<SeasonConfig> seasonConfigList){
		List<SeasonDataModel> seasons = new ArrayList<SeasonDataModel>();
		for(int i = 0;i<seasonConfigList.size();i++){
			SeasonConfig seasonConfig =seasonConfigList.get(i);

			String name = seasonConfig.getName();
			int existFlag = name.indexOf(SeasonConfigureDataModel.HOLIDAY_PREFIX);
			if(existFlag!=-1){

			}else{
				SeasonDataModel season = getSeasonDataModelInstance(seasonConfig);
				seasons.add(season);
			}
		}
		return seasons;
	}
	public List<HolidayDataModel> buildHolidayDataModels(List<SeasonConfig> seasonConfigList){
		List<HolidayDataModel> holidays = new ArrayList<HolidayDataModel>();
		for(int i = 0;i<seasonConfigList.size();i++){
			SeasonConfig seasonConfig =seasonConfigList.get(i);

			String name = seasonConfig.getName();
			int existFlag = name.indexOf(SeasonConfigureDataModel.HOLIDAY_PREFIX);
			if(existFlag!=-1){
				HolidayDataModel season = getHolidayDataModelInstance(seasonConfig);
				holidays.add(season);
			}else{

			}
		}
		return holidays;
	}
	/**
	 * Function for get SeasonConfig instance from SeasonDataModel instance
	 * @param season SeasonDataModel instance
	 * @return SeasonConfig instance
	 */
	public SeasonConfig getSeasonConfigInstance(SeasonDataModel season){
		SeasonConfig aprop = new SeasonConfig();
		aprop.setUUID(season.getId());
		aprop.setName(season.getName());
		aprop.setStartDate(season.getStartDate());
		aprop.setEndDate(season.getEndDate());
		return aprop;
	}

    public SeasonConfig getSeasonConfigInstance(HolidayDataModel h){
		SeasonConfig aprop = new SeasonConfig();
		aprop.setUUID(h.getId());
		aprop.setName("WEEKEND/HOLIDAY:"+h.getName());
		aprop.setStartDate(h.getStartDate());
		aprop.setEndDate(h.getEndDate());
		return aprop;
	}

	/**
	 * Function for get SeasonDataModel instance from SeasonConfig instance
	 * @param config SeasonConfig instance
	 * @return SeasonDataModel instance
	 */
	public SeasonDataModel getSeasonDataModelInstance(SeasonConfig config){
		SeasonDataModel model = new SeasonDataModel(config.getUUID(),config.getName(),config.getStartDate(),config.getEndDate());
		return model;
	}
	public HolidayDataModel getHolidayDataModelInstance(SeasonConfig config){
		HolidayDataModel model = new HolidayDataModel(config.getUUID(),config.getName(),config.getStartDate(),config.getEndDate());
		return model;
	}

	/**
	 * Getter for ProgramManager instance
	 * @return ProgramManager instance
	 */
	public ProgramManager getProgramManager() {
		return programManager;
	}

	public void setProgramManager(ProgramManager.L programManager) {
		this.programManager = programManager;
	}


    private Set<SeasonConfig> buildHolidayConfigListBeforeSave(SeasonConfigureDataModel model)throws ValidationException{

		// Do something for seasonConfigList base on the seasons and holidays
		String programName = model.getProgramConfigureDataModel().getProgramName();

        HolidayDataModel holiday = null;
//		Date lastDate;

        List<HolidayDataModel> holidays = model.getHolidays();

        SeasonConfigureValidator.holidayEmptyValidation(holidays);

        for(HolidayDataModel hday:holidays){
            SeasonConfigureValidator.holidayNameValidation(hday);
				holiday = hday;
				holiday.setProgramName(programName);
				holiday.setStartDate(hday.getStartDate());
				holiday.setEndDate(hday.getEndDate());

        }

		Set<SeasonConfig> result = new HashSet<SeasonConfig>();
        if(holiday!=null){
			result.add(getSeasonConfigInstance(holiday));
		}
		return result;
	}

	/**
	 * private function for reconstruct the SeasonConfig object
	 * @param model
	 * @return
	 * @throws ValidationException
	 */
	private Set<SeasonConfig> buildSeasonHolidayConfigListBeforeSave(SeasonConfigureDataModel model)throws ValidationException{

		// Do something for seasonConfigList base on the seasons and holidays
		String programName = model.getProgramConfigureDataModel().getProgramName();
		SeasonDataModel summer =null;
		SeasonDataModel winter = null;
		SeasonDataModel priviousWinter = null;
        HolidayDataModel holiday = null;
        SeasonDataModel season = null;
        List<SeasonDataModel> seasons = null;


		seasons = model.getSeasons();
        List<HolidayDataModel> holidays = model.getHolidays();

        // avoid as it add an extra season to the set
        //Using this code to avoid that flex ascending validation.
		//seasons = sortListByAscending(seasons); 
      

		SeasonConfigureValidator.seasonSequenceValidation(seasons);
		SeasonConfigureValidator.seasonEmptyValidation(seasons);
        SeasonConfigureValidator.holidayEmptyValidation(holidays);
        Set<SeasonConfig> result = new HashSet<SeasonConfig>();

        for(HolidayDataModel hday:holidays){
            SeasonConfigureValidator.holidayNameValidation(hday);
				holiday = hday;
				holiday.setProgramName(programName);
				holiday.setStartDate(hday.getStartDate());
				holiday.setEndDate(hday.getStartDate());

           if(holiday!=null){
			result.add(getSeasonConfigInstance(holiday));
            }
        }

		for(int i = 0;i<seasons.size();i++){
			season = seasons.get(i);
			SeasonConfigureValidator.seasonNameValidation(season);
			SeasonConfigureValidator.seasonStartTimeValidation(season);
			try{

               if(season!=null){   
                    season.setEndDate(season.getEndDate());
                    result.add(getSeasonConfigInstance(season));
                }
              /*
              if(season.getName().trim().equalsIgnoreCase(SeasonConfig.SUMMER_SEASON)){
                   summer = season;
                   summer.setProgramName(programName);
                   summer.setStartDate(season.getStartDate());
                   Date end = season.getStartDate();
                   end.setMonth(season.getStartDate().getMonth()+6);
                   summer.setEndDate(DateUtil.getLastDayOfYear(summer.getStartDate()) );
                   result.add(getSeasonConfigInstance(summer));
                }
                if(season.getName().trim().equalsIgnoreCase(SeasonConfig.WINTER_SEASON)){
                   winter = season;
                   winter.setProgramName(programName);
                   winter.setStartDate(season.getStartDate());
                   Date end = season.getStartDate();
                   end.setMonth(season.getStartDate().getMonth()+6);
                   winter.setEndDate(end);
                   result.add(getSeasonConfigInstance(winter));
                }
             */
               /*
                if(season.getName().trim().equalsIgnoreCase(SeasonConfig.SUMMER_SEASON)){
                    summer = season;
                    summer.setProgramName(programName);
                    priviousWinter = new SeasonDataModel(SeasonConfig.WINTER_SEASON,DateUtil.getFirstDayOfYear(summer.getStartDate()));
                    priviousWinter.setProgramName(programName);
                    priviousWinter.setEndDate(DateUtil.getDate(summer.getStartDate(), -1));
                }else if(season.getName().trim().equalsIgnoreCase(SeasonConfig.WINTER_SEASON)){
                    winter = season;
                    winter.setProgramName(programName);
                    winter.setStartDate(season.getStartDate());
                    Date end = season.getStartDate();
                    end.setMonth(season.getStartDate().getMonth()+6);
                    winter.setEndDate(DateUtil.getLastDayOfYear(season.getStartDate()));
                    //summer.setEndDate(DateUtil.getDate(winter.getStartDate(), -1));
                }
               
                if(priviousWinter!=null){
                    result.add(getSeasonConfigInstance(priviousWinter));
                }
                if(summer!=null){
                    result.add(getSeasonConfigInstance(summer));
                }
                if(winter!=null){
                    result.add(getSeasonConfigInstance(winter));
                }
             */
            }catch(Exception e){
                e.printStackTrace();
            }
		}


		return result;
	}
	/**
	 * Private function for sort season data model list
	 * @param seasons
	 * @return
	 */
	private List<SeasonDataModel> sortListByAscending(List<SeasonDataModel> seasons){
		List<SeasonDataModel> result = new ArrayList<SeasonDataModel>();
		for(int i =0;i<seasons.size();i++){
			SeasonDataModel season = seasons.get(i);
			Long startTime = season.getStartDate().getTime();
			for(int j=0;j<result.size();j++){
				SeasonDataModel seasonCompare = result.get(j);
				Long startTimeCompare = seasonCompare.getStartDate().getTime();
				if(startTimeCompare>startTime){
					result.add(j, season);
					break;
				}
			}
			result.add(season);
		}
		return result;
	}
}
