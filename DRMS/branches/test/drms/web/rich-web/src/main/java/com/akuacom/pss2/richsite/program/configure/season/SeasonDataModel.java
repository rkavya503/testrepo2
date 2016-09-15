package com.akuacom.pss2.richsite.program.configure.season;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.utils.lang.DateUtil;
/**
 * 
 * Filename:    SeasonDataModel.java 
 * Description:  
 * Copyright:   Copyright (c)2010
 * Company:     
 * @version:    
 * Create at:   Feb 17, 2011 5:05:04 PM 
 */
public class SeasonDataModel implements Serializable{

	private static final long serialVersionUID = -7079379412848195953L;

	/**
	 * Constructor
	 */
	public SeasonDataModel(){
		super();
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param startDate
	 */
	public SeasonDataModel(String name,Date startDate){
		super();
		this.name=name;
		this.startDate=DateUtil.getStartOfDay(startDate); //Base on the presentation layer so get start of day
        this.endDate=DateUtil.getStartOfDay(startDate); //Base on the presentation layer so get start of day
		buildSeasonNameTypes();
	}
	
	/**
	 * Constructor
	 * @param id
	 * @param name
	 * @param startDate
	 * @param endDate
	 */
	public SeasonDataModel(String id,String name,Date startDate,Date endDate){
		super();
		this.id=id;
		this.name=name;
		this.startDate=startDate; 	//Base on the database layer so not need get start of day
		this.endDate=endDate;
		buildSeasonNameTypes();
	}
	//-------------------------------Attributes-------------------------------------------------
	private String id=null;
	private String name="";
	private Date startDate;
	private Date endDate;
	private String programName="";
	private List<SelectItem> seasonNameTypes = new ArrayList<SelectItem>();	//Data for presentation layer
	private boolean deleted=false;

	//-------------------------------Getter and Setter------------------------------------------
	
	public String getName() {
		return name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public List<SelectItem> getSeasonNameTypes() {
		return seasonNameTypes;
	}
	public void setSeasonNameTypes(List<SelectItem> seasonNameTypes) {
		this.seasonNameTypes = seasonNameTypes;
	}
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	//-------------------------------Business Logic Method--------------------------------------
	
	/**
	 * function for construct JSF SelectItem component for season name types
	 */
	public void buildSeasonNameTypes(){
		seasonNameTypes = new ArrayList<SelectItem>();
		if(name.equalsIgnoreCase(SeasonConfig.SUMMER_SEASON)){
			seasonNameTypes.add(new SelectItem(SeasonConfig.SUMMER_SEASON,SeasonConfig.SUMMER_SEASON));
			seasonNameTypes.add(new SelectItem(SeasonConfig.WINTER_SEASON,SeasonConfig.WINTER_SEASON));
		}else if(name.equalsIgnoreCase(SeasonConfig.WINTER_SEASON)){
			seasonNameTypes.add(new SelectItem(SeasonConfig.WINTER_SEASON,SeasonConfig.WINTER_SEASON));
			seasonNameTypes.add(new SelectItem(SeasonConfig.SUMMER_SEASON,SeasonConfig.SUMMER_SEASON));
		}else{
			seasonNameTypes.add(new SelectItem(name,name));
			seasonNameTypes.add(new SelectItem(SeasonConfig.SUMMER_SEASON,SeasonConfig.SUMMER_SEASON));
			seasonNameTypes.add(new SelectItem(SeasonConfig.WINTER_SEASON,SeasonConfig.WINTER_SEASON));
		}
	}
	
	
}
