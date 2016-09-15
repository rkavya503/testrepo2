/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.AccountsReportForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import org.apache.struts.action.ActionForm;
import com.akuacom.pss2.report.entities.Account;

import java.util.List;

/**
 * The Class AccountsReportForm.
 */
public class AccountsReportForm extends ActionForm {
    
    /** The accounts. */
    private List<Account> accounts;

    /**
     * Gets the accounts.
     * 
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Sets the accounts.
     * 
     * @param accounts the new accounts
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
