/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.ClientTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.util.List;

import javax.faces.component.UIData;
import javax.faces.model.DataModel;

import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchHandler;

/**
 * The Class ClientTable.
 */
abstract public class PagedTable
{
    UIData table;
    SearchHandler searchHandler = null;
    
    public UIData getTable()
    {
        return table;
    }

    public void setTable(UIData data)
    {
        table = data;
    }


    abstract protected List getPagedList();

    public DataModel getTablePage() {
        if(searchHandler == null)
        {
            searchHandler = new SearchHandler();
        }
        AkuaCursor cursor = new AkuaCursor();
        cursor.setMaxPageSize(table.getRows());
        cursor.setStartIndex(table.getFirst());
        searchHandler.setCursor(cursor);
        List list = getPagedList();
        PagedListDataModel dataModel = new PagedListDataModel(list, searchHandler.getCursor().getTotal(), table.getRows());
        return dataModel;
    }

}