/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.logs.LogDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.logs;

import org.apache.struts.action.ActionForm;

/**
 * The Class LogDetailForm.
 */
public class LogDetailForm extends ActionForm {
    
    /** The id. */
    private String id;
    
    /** The index. */
    private int index;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the index.
     * 
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index.
     * 
     * @param index the new index
     */
    public void setIndex(int index) {
        this.index = index;
    }
}
