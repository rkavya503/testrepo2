package com.akuacom.pss2.richsite.program.configure.season;

import com.akuacom.ejb.client.EJB3Factory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.openadr.dras.akuaseasonconfig.AkuaSeasonConfig;

import com.akuacom.pss2.richsite.event.ListEvent;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.pss2.season.SeasonConfigEAOBean;
import com.akuacom.pss2.util.DateTool;
import java.io.Serializable;
/**
 *
 * Filename:    SeasonConfigureDataModel.java
 * Description:
 * Copyright:   Copyright (c)2010
 * Company:
 * @version:
 * Create at:   Feb 17, 2011 5:09:12 PM
 */
public class SeasonConfigureDataModel extends ProgramConfigureDataModel implements Serializable {
	//-------------------------------Attributes-------------------------------------------------

	public static final String HOLIDAY_PREFIX="WEEKEND/HOLIDAY:";

	private ProgramConfigureDataModel programConfigureDataModel;
	private SeasonConfigureDataModelManager manager = ProgramConfigureFactory.getInstance().getSeasonConfigureDataModelManager();

//	private ListOfSeasonConfigs listOfSeasonConfigs; //need this?

	private List<SeasonConfig> seasonConfigList= new ArrayList<SeasonConfig>();

	public List<SeasonDataModel> seasons = new ArrayList<SeasonDataModel>();
	private List<HolidayDataModel> holidays = new ArrayList<HolidayDataModel>();

    private boolean seasonFlag = false;

/*
    public SeasonConfigureDataModel(){
        super();
		this.programConfigureDataModel=programConfigureDataModel;
        this.buildModel();
        }
*/
	public SeasonConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		super();
		this.programConfigureDataModel=programConfigureDataModel;
		buildModel();
	}

	//-------------------------------Business Logic Method--------------------------------------

	/**
	 * Function for construct the relative backingBean model
	 */
	public void buildModel(){
		if(this.programConfigureDataModel!=null){
			try {
				loadSeasons();
//				buildSeasons();
//				buildHolidays();
//				mockBuildSeasons();
//				mockBuildHolidays();
                seasonFlag = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	public String loadSeasons() throws Exception{
		if(this.programConfigureDataModel!=null){
			seasonConfigList = manager.getSeasonConfig(this);
			seasons = manager.buildSeasonDataModels(seasonConfigList);
			holidays = manager.buildHolidayDataModels(seasonConfigList);
        }
		return "";
	}

	/**
	 * Function for save operation
	 */
	public String saveSeasonConfigure(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			try{
				manager.saveSeasonConfig(this);

			}catch(Exception e){
				AkuacomJSFUtil.addErrorMessage(e.getMessage());
			}
		}
		return "";
	}


    	/**
	 * Function for save operation
	 */
	public String saveHolidayConfigure(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			try{
				manager.saveHolidayConfig(this);

			}catch(Exception e){
				AkuacomJSFUtil.addErrorMessage(e.getMessage());
			}
		}
		return "";
	}



	public static AkuaSeasonConfig getSeasonConfig(SeasonConfig prop)throws Exception {
		AkuaSeasonConfig aprop = new AkuaSeasonConfig();
		aprop.setId(prop.getUUID());
		aprop.setName(prop.getName());
		aprop.setStartDate(DateTool.converDateToXMLGregorianCalendar(prop.getStartDate()));
		aprop.setEndDate(DateTool.converDateToXMLGregorianCalendar(prop.getEndDate()));
		return aprop;
	}

	/**
	 * Function for add LastSeason
	 */
	public String removeLastSeason(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			
			List<Integer> deletedList=new ArrayList<Integer>();
			for (int i=0;i<seasons.size();i++){
				if (seasons.get(i).isDeleted())
					deletedList.add(i);
			}
			
			for (Integer index:deletedList) {
				seasons.remove(index.intValue());
			}
		}
		
		return "";
	}
	/**
	 * Function for add LastSeason
	 */
	public String addLastSeason(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			if(seasons.size()>0){
				SeasonDataModel model =seasons.get(seasons.size()-1);
				Date startDate = model.getStartDate();
				if(startDate!=null){
					long newStartDate = startDate.getTime()+1000*60*60*24;
					seasons.add(new SeasonDataModel(SeasonConfig.SUMMER_SEASON,new Date(newStartDate)));
				}else{
					seasons.add(new SeasonDataModel(SeasonConfig.SUMMER_SEASON,new Date()));
				}
			}else{
				seasons.add(new SeasonDataModel(SeasonConfig.SUMMER_SEASON,new Date()));
			}
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}

	/**
	 * Function for add LastSeason
	 */
	public String removeLastHoliday(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			
			List<Integer> deletedList=new ArrayList<Integer>();
			for (int i=0;i<holidays.size();i++){
				if (holidays.get(i).isDeleted())
					deletedList.add(i);
			}
			
			for (Integer index:deletedList) {
				holidays.remove(index.intValue());
			}
		}
		
		return "";
	}
	/**
	 * Function for add LastSeason
	 */
	public String addLastHoliday(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			if(holidays.size()>0){
				HolidayDataModel model =holidays.get(holidays.size()-1);
				Date startDate = model.getStartDate();
				if(startDate!=null){
					long newStartDate = startDate.getTime()+1000*60*60*24;
					holidays.add(new HolidayDataModel("",new Date(newStartDate)));
				}else{
					holidays.add(new HolidayDataModel("",new Date()));
				}
			}else{
				holidays.add(new HolidayDataModel("",new Date()));
			}
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}




	//-------------------------------Getter and Setter------------------------------------------
	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}
	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}
	public List<SeasonDataModel> getSeasons() {
		return seasons;
	}
	public void setSeasons(List<SeasonDataModel> seasons) {
		this.seasons = seasons;
	}
	public List<HolidayDataModel> getHolidays() {
		return holidays;
	}
	public void setHolidays(List<HolidayDataModel> holidays) {
		this.holidays = holidays;
	}
	public List<SeasonConfig> getSeasonConfigList() {
		return seasonConfigList;
	}
	public void setSeasonConfigList(List<SeasonConfig> seasonConfigList) {
		this.seasonConfigList = seasonConfigList;
	}

    public boolean isSeasonFlag() {
        return seasonFlag;
    }

    public void setSeasonFlag(boolean seasonFlag) {
        this.seasonFlag = seasonFlag;
    }

}
