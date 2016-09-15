package com.akuacom.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.ajax4jsf.renderkit.RendererUtils.HTML;

public class TabTag extends org.richfaces.taglib.TabTag {

	private ValueExpression accesskey;

	private ValueExpression headingLevel;
	
	private ValueExpression showHeader;
	
	public void release() {
		super.release();
		accesskey = null;
		headingLevel=null;
		this.showHeader=null;
	}
	
	protected void setProperties(UIComponent component){
	    super.setProperties(component);
	    setProperty(component, HTML.accesskey_ATTRIBUTE, accesskey);
	    setProperty(component, "headingLevel", headingLevel);
	    setProperty(component, "showHeader", showHeader);
	}
	 
	public ValueExpression getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(ValueExpression accesskey) {
		this.accesskey = accesskey;
	}
	
	public ValueExpression getHeadingLevel() {
		return headingLevel;
	}
	
	public void setHeadingLevel(ValueExpression headingLevel) {
		this.headingLevel = headingLevel;
	}
	
	public ValueExpression getShowHeader() {
		return showHeader;
	}

	public void setShowHeader(ValueExpression showHeader) {
		this.showHeader = showHeader;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	public String getComponentType() {
		return "com.akuacom.jsf.Tab";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	public String getRendererType() {
		return "com.akuacom.jsf.TabRenderer";
	}
	
}
