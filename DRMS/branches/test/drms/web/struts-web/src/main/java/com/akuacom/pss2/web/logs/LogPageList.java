/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.logs.LogPageList.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.logs;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

import java.io.Serializable;
import java.util.List;

/**
 * The Class LogPageList.
 */
public class LogPageList implements PaginatedList, Serializable {
    
    /** The list. */
    private List list;
    
    /** The page number. */
    private int pageNumber;
    
    /** The objects per page. */
    private int objectsPerPage;
    
    /** The full list size. */
    private int fullListSize;
    
    /** The sort criterion. */
    private String sortCriterion;
    
    /** The sort direction. */
    private transient SortOrderEnum sortDirection;
    
    /** The search id. */
    private String searchId;

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getList()
     */
    public List getList() {
        return list;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getPageNumber()
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getObjectsPerPage()
     */
    public int getObjectsPerPage() {
        return objectsPerPage;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getFullListSize()
     */
    public int getFullListSize() {
        return fullListSize;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getSortCriterion()
     */
    public String getSortCriterion() {
        return sortCriterion;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getSortDirection()
     */
    public SortOrderEnum getSortDirection() {
        return sortDirection;
    }

    /* (non-Javadoc)
     * @see org.displaytag.pagination.PaginatedList#getSearchId()
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     * Sets the list.
     * 
     * @param list the new list
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * Sets the page number.
     * 
     * @param pageNumber the new page number
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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
     * Sets the full list size.
     * 
     * @param fullListSize the new full list size
     */
    public void setFullListSize(int fullListSize) {
        this.fullListSize = fullListSize;
    }

    /**
     * Sets the sort criterion.
     * 
     * @param sortCriterion the new sort criterion
     */
    public void setSortCriterion(String sortCriterion) {
        this.sortCriterion = sortCriterion;
    }

    /**
     * Sets the sort direction.
     * 
     * @param sortDirection the new sort direction
     */
    public void setSortDirection(SortOrderEnum sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * Sets the search id.
     * 
     * @param searchId the new search id
     */
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
