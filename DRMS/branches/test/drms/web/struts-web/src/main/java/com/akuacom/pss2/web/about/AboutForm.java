/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.about.AboutForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.about;

import org.apache.struts.validator.ValidatorActionForm;

/**
 * The Class AboutForm.
 */
public class AboutForm extends ValidatorActionForm {

	private String href1;
    private String buildVersion;
    private String buildBranch;
    private String buildTime;
    private String href1DisplayName;
    private String phone1;
    private String mailto1;
    private String mailto1Str;
    private String version;
    private String revision;
    private String copyright1;
    private String copyright2;
    private String[] logoUrls;
    private String utility;
    private String clientDataReportingInterval;

    public String getHref1() {
        return href1;
    }

    public void setHref1(String href1) {
        this.href1 = href1;
    }

    public String getMailto1() {
        return mailto1;
    }

    public void setMailto1(String mailto1) {
        this.mailto1 = mailto1;
    }

    public String getMailto1Str() {
        return mailto1Str;
    }

    public void setMailto1Str(String mailto1Str) {
        this.mailto1Str = mailto1Str;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCopyright1() {
        return copyright1;
    }

    public void setCopyright1(String copyright1) {
        this.copyright1 = copyright1;
    }

    public String getCopyright2() {
        return copyright2;
    }

    public void setCopyright2(String copyright2) {
        this.copyright2 = copyright2;
    }

    public String getHref1DisplayName() {
        return href1DisplayName;
    }

    public void setHref1DisplayName(String href1DisplayName) {
        this.href1DisplayName = href1DisplayName;
    }

    public String[] getLogoUrls() {
        return logoUrls;
    }

    public void setLogoUrls(String[] logoUrls) {
        this.logoUrls = logoUrls;
    }

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public String getBuildTime() {
		return buildTime;
	}
	
	public void setClientDataReportingInterval(String i) {
		this.clientDataReportingInterval = i;
	}
	
	public String getClientDataReportingInterval() {
		return clientDataReportingInterval;
	}

    public void setBuildBranch(String buildBranch) {
        this.buildBranch = buildBranch;
    }

    public String getBuildBranch() {
        return buildBranch;
    }
}