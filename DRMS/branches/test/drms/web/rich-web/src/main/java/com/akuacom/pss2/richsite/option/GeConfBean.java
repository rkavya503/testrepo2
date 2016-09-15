/**
 * 
 */
package com.akuacom.pss2.richsite.option;

import java.io.Serializable;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.ge.GeConfiguration;
import com.akuacom.pss2.ge.GeInterfaceManager;
import com.akuacom.pss2.richsite.FDUtils;

/**
 * 
 */
public class GeConfBean  implements Serializable{
	
	private static final long serialVersionUID = 1479019764236433113L;
	GeInterfaceManager manager = EJBFactory.getBean(GeInterfaceManager.class);
	
	public GeConfBean(){
		// init
		GeConfiguration conf = manager.getGeConfiguration();
		this.setProgramName(conf.getProgramName());
		this.setUrl(conf.getUrl());
		this.setNameSpace(conf.getNameSpace());
		this.setMethod(conf.getMethod());
		this.setShortInterval(conf.getShortInterval());
		this.setLongInterval(conf.getLongInterval());
	}
	
	private String programName;
	private String url;
	private String nameSpace;
	private String method;
	private Integer shortInterval;
	private Integer longInterval;
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
	
	public void save(){
		GeConfiguration conf = new GeConfiguration();
		conf.setProgramName(programName);
		conf.setUrl(url);
		conf.setNameSpace(nameSpace);
		conf.setMethod(method);
		conf.setShortInterval(shortInterval);
		conf.setLongInterval(longInterval);
		
		manager.saveGeConfiguration(conf);
		FDUtils.addMsgInfo("Configuration saved successfully");
	}
	
}
