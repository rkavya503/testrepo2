/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.commdev.CommDevListForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * The Class CommDevListForm.
 */
public class CommDevListForm extends ActionForm {
    
    /** The usernames. */
    private String[] usernames;
    
    /** The comm dev list. */
    private List<CommDevVO> commDevList;

    /**
     * Gets the usernames.
     * 
     * @return the usernames
     */
    public String[] getUsernames() {
        return usernames;
    }

    /**
     * Sets the usernames.
     * 
     * @param usernames the new usernames
     */
    public void setUsernames(String[] usernames) {
        this.usernames = usernames;
    }

    /**
     * Gets the comm dev list.
     * 
     * @return the comm dev list
     */
    public List<CommDevVO> getCommDevList() {
        return commDevList;
    }

    /**
     * Sets the comm dev list.
     * 
     * @param commDevList the new comm dev list
     */
    public void setCommDevList(List<CommDevVO> commDevList) {
        this.commDevList = commDevList;
    }
}
