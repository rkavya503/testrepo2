/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.ejb.search.AkuaCursor.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.ejb.search;

import java.io.Serializable;

/**
 * The Class AkuaCursor.
 */
public class AkuaCursor implements Serializable
{
    
    private int startIndex = 0;
    
    private int pageSize;
    
    private int total;
    
    private int selectedPage = 0;
    
    private int maxPageSize = 15;

    /**
     * Gets the max page size.
     * 
     * @return the max page size
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }

    /**
     * Sets the max page size.
     * 
     * @param maxPageSize the new max page size
     */
    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
    }

    /**
     * Gets the selected page.
     * 
     * @return the selected page
     */
    public int getSelectedPage()
    {
        return selectedPage;
    }

    /**
     * Sets the selected page.
     * 
     * @param selectedIndex the new selected page
     */
    public void setSelectedPage(int selectedIndex)
    {
        selectedPage = selectedIndex;
    }

    /**
     * Gets the start index.
     * 
     * @return the start index
     */
    public int getStartIndex()
    {
        return startIndex;
    }

    /**
     * Sets the start index.
     * 
     * @param startIndex the new start index
     */
    public void setStartIndex(int startIndex)
    {
        this.startIndex = startIndex;
    }

    /**
     * Gets the page size.
     * 
     * @return the page size
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * Sets the page size.
     * 
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Gets the total.
     * 
     * @return the total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * Sets the total.
     * 
     * @param total the new total
     */
    public void setTotal(int total)
    {
        this.total = total;
    }

    /**
     * Gets the page number.
     * 
     * @return the page number
     */
    public int getPageNumber()
    {
        int pageNumber = this.startIndex /this.maxPageSize + 1;
        return pageNumber;
    }

    /**
     * Gets the total page count.
     * 
     * @return the total page count
     */
    public int getTotalPageCount()
    {
        int count = this.total /this.maxPageSize + 1;
        if(count*this.maxPageSize == this.total) count = count - 1;
        return count;
    }
}
