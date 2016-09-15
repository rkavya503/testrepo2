package com.akuacom.jsf.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


public class HtmlTab extends org.richfaces.component.html.HtmlTab {

	final public static String COMPONENT_FAMILY = "com.akuacom.jsf.Tab";

	final public static String COMPONENT_TYPE = "com.akuacom.jsf.Tab";

	private String accesskey;

	private String headingLevel;
	
	private boolean showHeader = true;
	
	private boolean activePreviously;
	
	private Object[] values;
	  
	public HtmlTab(){
		setRendererType("com.akuacom.jsf.TabRenderer");
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getHeadingLevel() {
		return headingLevel;
	}

	public void setHeadingLevel(String headingLevel) {
		this.headingLevel = headingLevel;
	}
	
	public boolean isShowHeader() {
		return this.showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	@Override
	public void setActive(boolean active) {
		this.activePreviously=isActive();
		super.setActive(active);
	}
	
	public boolean isActivePreviously() {
		return activePreviously;
	}
	
	@Override
	public Object saveState(FacesContext context){
		if (values == null) {
             values = new Object[5];
		}
		values[0] = super.saveState(context);
		values[1] = accesskey;
		values[2] = headingLevel;
		values[3] = Boolean.valueOf(activePreviously);
		values[4] = Boolean.valueOf(showHeader);
		
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object state){
		Object[] states = (Object[]) state;
		super.restoreState(context, states[0]);
		accesskey = (String)states[1];
		headingLevel = (String)states[2];
		activePreviously=((Boolean)states[3]).booleanValue(); 
		showHeader =((Boolean)states[4]).booleanValue(); 
	}
}
