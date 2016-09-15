/**
 * 
 */
package com.akuacom.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

/**
 * @author e333812
 *
 */
public class CommandLinkTag extends
		com.sun.faces.taglib.html_basic.CommandLinkTag {

	private ValueExpression headingLevel;
	
	public void release() {
		super.release();
		headingLevel=null;
	}
	
	protected void setProperties(UIComponent component){
	    super.setProperties(component);
	    
        javax.faces.component.UICommand command = null;
        try {
            command = (javax.faces.component.UICommand) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() 
            		+ " not expected type.  Expected: javax.faces.component.UICommand.  Perhaps you're missing a tag?");
        }

        if (headingLevel != null) {
            command.setValueExpression("headingLevel", headingLevel);
        }
	}
	 
	public ValueExpression getHeadingLevel() {
		return headingLevel;
	}
	
	public void setHeadingLevel(ValueExpression headingLevel) {
		this.headingLevel = headingLevel;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	public String getComponentType() {
		return "com.akuacom.jsf.CommandLink";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	public String getRendererType() {
		return "com.akuacom.jsf.CommandLinkRenderer";
	}
		
}
