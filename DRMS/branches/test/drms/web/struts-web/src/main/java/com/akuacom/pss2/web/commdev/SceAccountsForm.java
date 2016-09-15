/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.commdev.SceAccountsForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import org.apache.struts.action.ActionForm;

/**
 * The Class SceAccountsForm.
 */
public class SceAccountsForm extends ActionForm {
    
    /** The user name. */
    String userName;
    
    /** The sce account. */
    String sceAccount;
    
    /** The comment. */
    String comment;

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

    /**
     * Gets the comment.
     * 
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * 
     * @param comment the new comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the sce account.
     * 
     * @return the sce account
     */
    public String getSceAccount() {
        return sceAccount;
    }

    /**
     * Sets the sce account.
     * 
     * @param sceAccount the new sce account
     */
    public void setSceAccount(String sceAccount) {
        this.sceAccount = sceAccount;
    }
}
