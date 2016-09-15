package com.akuacom.pss2.web.options;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class DbpSimulatorForm extends ActionForm {
    private FormFile xmlFile;

    private String result;

    public FormFile getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(FormFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
