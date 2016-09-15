package com.akuacom.pss2.ge;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;

@Entity
@Table(name = "ge_config")
@NamedQueries({
	@NamedQuery(name = "GeConfiguration.findByConfigName.single",
        query = "select c from GeConfiguration c where c.configName=:configName")
}) 
public class GeConfiguration extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String programName;
	private String url;
	private String nameSpace;
	private String method;
	private Integer shortInterval;
	private Integer longInterval;
	private String configName;
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNameSpace() {
		return nameSpace;
	}
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public Integer getShortInterval() {
		return shortInterval;
	}
	public void setShortInterval(Integer shortInterval) {
		this.shortInterval = shortInterval;
	}
	public Integer getLongInterval() {
		return longInterval;
	}
	public void setLongInterval(Integer longInterval) {
		this.longInterval = longInterval;
	}
	
}
