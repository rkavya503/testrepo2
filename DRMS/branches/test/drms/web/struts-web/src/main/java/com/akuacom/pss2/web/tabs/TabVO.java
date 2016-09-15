/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.tabs.TabVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.tabs;

/**
 * The Class TabVO.
 */
public class TabVO {
    
    /** The action. */
    private String action;
    
    /** The title. */
    private String title;
    
    /** The active. */
    private boolean active;

    /**
     * Gets the action.
     * 
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action.
     * 
     * @param action the new action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Gets the active.
     * 
     * @return the active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Sets the active.
     * 
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
