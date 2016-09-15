/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.logs.LogListForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.logs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.web.util.OptionUtil;
import com.akuacom.utils.Tag;

/**
 * The Class LogListForm.
 */
public class LogListForm extends ActionForm {

    /** The Constant ASCENDING_URL. */
    private static final String ASCENDING_URL = "secure/images/sort_ascending.png";
    
    /** The Constant DESCENDING_URL. */
    private static final String DESCENDING_URL = "secure/images/sort_descending.png";

    /** The Constant ASCENDING. */
    private static final String ASCENDING = "Ascending";
    
    /** The Constant DESCENDING. */
    private static final String DESCENDING = "Descending";

    // query related fields
    /** The start date. */
    private String startDate;
    
    /** The start time. */
    private String startTime;
    
    /** The start hour. */
    private String startHour;
    
    /** The start minute. */
    private String startMinute;
    
    /** The start second. */
    private String startSecond;

    /** The end date. */
    private String endDate;
    
    /** The end time. */
    private String endTime;
    
    /** The end hour. */
    private String endHour;
    
    /** The end minute. */
    private String endMinute;
    
    /** The end second. */
    private String endSecond;

    /** The user name. */
    private String userName;
    
    /** The category. */
    private String category;
    
    /** The built in category. */
    private String builtInCategory;
    
    /** The category radio. */
    private String categoryRadio;
    
    /** The category options. */
    private List<Tag> categoryOptions;
    
    /** The log level. */
    private String logLevel;
    
    /** The program. */
    private String program;
    
    /** The program options. */
    private List<Tag> programOptions;
    
    /** The description word. */
    private String descriptionWord;
    
    /** The sort by. */
    private String sortBy;
    
    /** The sort order. */
    private String sortOrder;

    // result related fields
    /** The objects per page. */
    private int objectsPerPage;
    
    /** The log count. */
    private int logCount;
    
    /** The page. */
    private int page =-1;

    /** The log page list. */
    private LogPageList logPageList;

    /** The hour list. */
    private List hourList = OptionUtil.getHoursList();

    /** The min list. */
    private List minList = OptionUtil.getMinSecList();

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        super.reset(actionMapping, request);
        Date now = new Date();

        String dateFormat=(String)request.getSession().getServletContext().getAttribute("dateFormat");
        if (dateFormat==null)
        	dateFormat="MM/dd/yyyy";
        startDate = endDate = (new SimpleDateFormat(dateFormat)).format(now);
        startTime = "00:00:00";
        endTime = new SimpleDateFormat("HH:mm:ss").format(now);
        final String[] times = endTime.split(":");
        endHour = times[0];
        endMinute = times[1];
        endSecond = times[2];

        logLevel = "INFO";
        userName = "";
        category = "";
        builtInCategory = "";
        categoryRadio = "builtin";
        program = "";
        descriptionWord = "";

        logPageList = new LogPageList();
        logPageList.setFullListSize(0);
        logPageList.setList(null);
        logPageList.setObjectsPerPage(25);

        //new request
        page = -1;
        objectsPerPage = 25;
        sortBy = "Date and Time";
        sortOrder = DESCENDING;
    }

    /**
     * Gets the built in category.
     * 
     * @return the built in category
     */
    public String getBuiltInCategory() {
        return builtInCategory;
    }

    /**
     * Sets the built in category.
     * 
     * @param builtInCategory the new built in category
     */
    public void setBuiltInCategory(String builtInCategory) {
        this.builtInCategory = builtInCategory;
    }

    /**
     * Gets the category radio.
     * 
     * @return the category radio
     */
    public String getCategoryRadio() {
        return categoryRadio;
    }

    /**
     * Sets the category radio.
     * 
     * @param categoryRadio the new category radio
     */
    public void setCategoryRadio(String categoryRadio) {
        this.categoryRadio = categoryRadio;
    }

    /**
     * Gets the category.
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     * 
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the category options.
     * 
     * @return the category options
     */
    public List<Tag> getCategoryOptions() {
        if (categoryOptions == null) {
        	SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
            final String[] logCategories = systemManager.getPss2Properties().getLogCategories();
            if (logCategories != null) {
                categoryOptions = new ArrayList<Tag>(logCategories.length);
                for (String logCategory : logCategories) {
                    categoryOptions.add(new Tag(logCategory, logCategory));
                }
            }
        }
        return categoryOptions;
    }

    /**
     * Gets the program options.
     * 
     * @return the program options
     */
    public List<Tag> getProgramOptions() {
        if (programOptions == null) {
            com.akuacom.pss2.program.ProgramManager programManager1 = com.akuacom.pss2.core.EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);

            final List<String> programs = programManager1.getPrograms();
            programOptions = new ArrayList<Tag>(programs.size());
            for (String programName : programs) {
                programOptions.add(new Tag(programName, programName));
            }
        }
        return programOptions;
    }

    /**
     * Gets the description word.
     * 
     * @return the description word
     */
    public String getDescriptionWord() {
        return descriptionWord;
    }

    /**
     * Sets the description word.
     * 
     * @param descriptionWord the new description word
     */
    public void setDescriptionWord(String descriptionWord) {
        this.descriptionWord = descriptionWord;
    }

    /**
     * Gets the end date.
     * 
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     * 
     * @param endDate the new end date
     */
    public void setEndDate(String endDate) {
    	if (endDate!=null)
    		this.endDate=endDate.trim();
    	else
    		this.endDate = endDate;
    }

    /**
     * Gets the end hour.
     * 
     * @return the end hour
     */
    public String getEndHour() {
        return endHour;
    }

    /**
     * Sets the end hour.
     * 
     * @param endHour the new end hour
     */
    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    /**
     * Gets the end minute.
     * 
     * @return the end minute
     */
    public String getEndMinute() {
        return endMinute;
    }

    /**
     * Sets the end minute.
     * 
     * @param endMinute the new end minute
     */
    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    /**
     * Gets the end second.
     * 
     * @return the end second
     */
    public String getEndSecond() {
        return endSecond;
    }

    /**
     * Sets the end second.
     * 
     * @param endSecond the new end second
     */
    public void setEndSecond(String endSecond) {
        this.endSecond = endSecond;
    }

    /**
     * Gets the end time.
     * 
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     * 
     * @param endTime the new end time
     */
    public void setEndTime(String endTime) {
    	if (endTime != null)
    		this.endTime = endTime.trim();
    	else
    		this.endTime = endTime;
    		
    }

    /**
     * Gets the log count.
     * 
     * @return the log count
     */
    public int getLogCount() {
        return logCount;
    }

    /**
     * Sets the log count.
     * 
     * @param logCount the new log count
     */
    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    /**
     * Gets the log level.
     * 
     * @return the log level
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the log level.
     * 
     * @param logLevel the new log level
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * Gets the log levels.
     * 
     * @return the log levels
     */
    public List<Tag> getLogLevels() {
        return OptionUtil.getLogLevels();
    }

    /**
     * Gets the sort by.
     * 
     * @return the sort by
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Sets the sort by.
     * 
     * @param sortBy the new sort by
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Gets the sort by image url.
     * 
     * @return the sort by image url
     */
    public String getSortByImageUrl() {
        if (ASCENDING.equalsIgnoreCase(sortBy)) {
            return ASCENDING_URL;
        } else {
            return DESCENDING_URL;
        }
    }

    /**
     * Gets the sort order.
     * 
     * @return the sort order
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the sort order.
     * 
     * @param sortOrder the new sort order
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Gets the start date.
     * 
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * 
     * @param startDate the new start date
     */
    public void setStartDate(String startDate) {
    	if (startDate!=null)
    		this.startDate=startDate.trim();
    	else
    		this.startDate = startDate;
    }

    /**
     * Gets the start hour.
     * 
     * @return the start hour
     */
    public String getStartHour() {
        return startHour;
    }

    /**
     * Sets the start hour.
     * 
     * @param startHour the new start hour
     */
    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    /**
     * Gets the start minute.
     * 
     * @return the start minute
     */
    public String getStartMinute() {
        return startMinute;
    }

    /**
     * Sets the start minute.
     * 
     * @param startMinute the new start minute
     */
    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    /**
     * Gets the start second.
     * 
     * @return the start second
     */
    public String getStartSecond() {
        return startSecond;
    }

    /**
     * Sets the start second.
     * 
     * @param startSecond the new start second
     */
    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }

    /**
     * Gets the start time.
     * 
     * @return the start time
     */
    public String getStartTime() {   	
        return startTime;
    }

    /**
     * Sets the start time.
     * 
     * @param startTime the new start time
     */
    public void setStartTime(String startTime) {
    	if (startTime != null)
    		this.startTime = startTime.trim();
    	else
    		this.startTime = startTime;
    		
    }

    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * 
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        if (objectsPerPage == 0) {
            objectsPerPage = 25;
        }
        return super.validate(actionMapping, request);
    }
    
    /**
     * Gets the objects per page.
     * 
     * @return the objects per page
     */
    public int getObjectsPerPage() {
        return objectsPerPage;
    }

    /**
     * Sets the objects per page.
     * 
     * @param objectsPerPage the new objects per page
     */
    public void setObjectsPerPage(int objectsPerPage) {
        this.objectsPerPage = objectsPerPage;
    }

    /**
     * The number is 1 based.
     * 
     * @return current page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the page.
     * 
     * @param page the new page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Gets the program.
     * 
     * @return the program
     */
    public String getProgram() {
        return program;
    }

    /**
     * Sets the program.
     * 
     * @param program the new program
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * Gets the log page list.
     * 
     * @return the log page list
     */
    public LogPageList getLogPageList() {
        return logPageList;
    }

    /**
     * Sets the log page list.
     * 
     * @param logPageList the new log page list
     */
    public void setLogPageList(LogPageList logPageList) {
        this.logPageList = logPageList;
    }
    
    public long getServerTime() {
    	return (new Date()).getTime();
    }

    public List getHourList() {
        return hourList;
    }

    public List getMinList() {
        return minList;
    }
}
