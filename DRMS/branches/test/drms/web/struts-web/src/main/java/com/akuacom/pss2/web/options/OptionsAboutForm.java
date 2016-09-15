/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.OptionsAboutForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.options;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class OptionsAboutForm.
 */
public class OptionsAboutForm extends ActionForm {
    
    /** The user name. */
    private String userName;
    
    /** The old password. */
    private String oldPassword;
    
    /** The password. */
    private String password;
    
    /** The password2. */
    private String password2;

    /**
     * Gets the old password.
     * 
     * @return the old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the old password.
     * 
     * @param oldPassword the new old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Gets the password2.
     * 
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * Sets the password2.
     * 
     * @param password2 the new password2
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        oldPassword = null;
        password = null;
        password2 = null;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        String dispatch = httpServletRequest.getParameter("dispatch");
        if ("update".equals(dispatch)) {
            if (oldPassword == null || oldPassword.length() == 0) {
                ActionMessage message = new ActionMessage("pss2.options.oldpassword.empty");
                errors.add("oldpassword", message);
            }
            if (password == null || password.length() == 0) {
                ActionMessage message = new ActionMessage("pss2.options.password.empty");
                errors.add("password", message);
            } else {
                if (password.length() < 9) {
                    ActionMessage message = new ActionMessage("pss2.options.password.length");
                    errors.add("password.length", message);
                }
                if (password.length() > 24) {
                    ActionMessage message = new ActionMessage("pss2.options.password.toolong");
                    errors.add("password.toolong", message);
                }
                if (!password.matches(".*[A-Z].*")) {// at least one upper case required
                    ActionMessage message = new ActionMessage("pss2.options.password.upper");
                    errors.add("password.upper", message);
                }
                if (!password.matches(".*[a-z].*")) {// at least one lower case required
                    ActionMessage message = new ActionMessage("pss2.options.password.lower");
                    errors.add("password.lower", message);
                }
                if (!password.matches(".*\\d.*")) {// at least one digit required
                    ActionMessage message = new ActionMessage("pss2.options.password.digit");
                    errors.add("password.digit", message);
                }
                if (httpServletRequest.isUserInRole("FacilityManager")) {// special case for clirs. 
                    if (!password.matches(".*[\\._\\-/].*")) {// at least one special character required
                        ActionMessage message = new ActionMessage("pss2.options.password.nonword");
                        errors.add("password.nonword", message);
                    }
                    if (password.matches(".*[\\W&&[^\\._\\-/]].*")) {
                        ActionMessage message = new ActionMessage("pss2.options.password.illegal.clir");
                        errors.add("password.nonword", message);
                    }
                } else {
                    if (!password.matches(".*[\\W_].*")) {// at least one special character required
                        ActionMessage message = new ActionMessage("pss2.options.password.nonword");
                        errors.add("password.nonword", message);
                    }
                }
            }
            if (password != null && !password.equals(password2)) {
                ActionMessage message = new ActionMessage("pss2.options.password2.equals");
                errors.add("password2", message);
            }
        }
        return errors;
    }
}
