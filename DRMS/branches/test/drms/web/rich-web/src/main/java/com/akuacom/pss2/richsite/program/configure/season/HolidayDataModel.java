package com.akuacom.pss2.richsite.program.configure.season;

import java.io.Serializable;
import java.util.Date;

import com.akuacom.utils.lang.DateUtil;

public class HolidayDataModel extends SeasonDataModel implements Serializable{

	private static final long serialVersionUID = -7079379412848195953L;

	/**
	 * Constructor
	 */
	public HolidayDataModel(){
		super();
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param startDate
	 */
	public HolidayDataModel(String name,Date startDate){
		super();
		setName(name);
		setStartDate(DateUtil.getStartOfDay(startDate));
	}
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param startDate
	 * @param endDate
	 */
	public HolidayDataModel(String id,String name,Date startDate,Date endDate){
		super();
		setId(id);
		int existFlag = name.indexOf(SeasonConfigureDataModel.HOLIDAY_PREFIX);
		if(existFlag!=-1){
			name = name.replaceAll(SeasonConfigureDataModel.HOLIDAY_PREFIX, "");
		}
		setName(name);
		setStartDate(startDate); 	//Base on the database layer so not need get start of day
		setEndDate(endDate);
		buildSeasonNameTypes();
	}
	
	//-------------------------------Attributes-------------------------------------------------

	//-------------------------------Getter and Setter------------------------------------------

	//-------------------------------Business Logic Method--------------------------------------
}
