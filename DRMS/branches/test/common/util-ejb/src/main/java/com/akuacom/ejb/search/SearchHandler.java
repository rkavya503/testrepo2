/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.ejb.search.SearchHandler.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.ejb.search;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;


/**
 * The Class SearchHandler.
 */
public class SearchHandler implements Serializable
{
    
    /** The Constant ASC. */
    public static final String ASC = "asc";
    
    /** The Constant DESC. */
    public static final String DESC = "desc";

    private AkuaCursor cursor;

    private List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();

    private String sortField;

    private String sortByDirection = SearchHandler.ASC;

    // TODO: not using generics here because of Query.getResults doesn't use them
    private List results;

    /**
     * Gets the cursor.
     * 
     * @return the cursor
     */
    public AkuaCursor getCursor()
    {
        return cursor;
    }

    /**
     * Sets the cursor.
     * 
     * @param cursor the new cursor
     */
    public void setCursor(AkuaCursor cursor)
    {
        this.cursor = cursor;
    }


    /**
     * Gets the criteria.
     * 
     * @return the criteria
     */
    public List<SearchCriterion> getCriteria()
    {
        return criteria;
    }

    /**
     * Sets the criteria.
     * 
     * @param criteria the new criteria
     */
    public void setCriteria(List<SearchCriterion> criteria)
    {
        this.criteria = criteria;
    }

    /**
     * Gets the sort field.
     * 
     * @return the sort field
     */
    public String getSortField()
    {
        return sortField;
    }

    /**
     * Sets the sort field.
     * 
     * @param sortField the new sort field
     */
    public void setSortField(String sortField)
    {
        this.sortField = sortField;
    }

    /**
     * Gets the sort by direction.
     * 
     * @return the sort by direction
     */
    public String getSortByDirection()
    {
        return sortByDirection;
    }

    /**
     * Sets the sort by direction.
     * 
     * @param sortByDirection the new sort by direction
     */
    public void setSortByDirection(String sortByDirection)
    {
        this.sortByDirection = sortByDirection;
    }

    /**
     * Gets the results.
     * 
     * @return the results
     */
    public List getResults()
    {
        return results;
    }

    /**
     * Sets the results.
     * 
     * @param results the new results
     */
    public void setResults(List results)
    {
        this.results = results;
    }
}
