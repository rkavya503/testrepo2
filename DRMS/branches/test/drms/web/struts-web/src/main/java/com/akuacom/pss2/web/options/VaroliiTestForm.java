package com.akuacom.pss2.web.options;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

public class VaroliiTestForm extends ActionForm {
    private String submitted;
    private String contact;
    private String contactType;

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        submitted = "false";
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }
}
