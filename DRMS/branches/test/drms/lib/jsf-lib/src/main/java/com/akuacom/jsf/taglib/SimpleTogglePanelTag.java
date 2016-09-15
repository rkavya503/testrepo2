package com.akuacom.jsf.taglib;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

public class SimpleTogglePanelTag extends
		org.richfaces.taglib.SimpleTogglePanelTag {
	private ValueExpression headingLevel;
	
	public void release() {
		super.release();
		headingLevel=null;
	}
	
	protected void setProperties(UIComponent component){
	    super.setProperties(component);
	    
//        javax.faces.component.UICommand command = null;
//        try {
//            command = (javax.faces.component.UICommand) component;
//        } catch (ClassCastException cce) {
//            throw new IllegalStateException("Component " + component.toString() 
//            		+ " not expected type.  Expected: javax.faces.component.UICommand.  Perhaps you're missing a tag?");
//        }

        if (headingLevel != null) {
        	component.setValueExpression("headingLevel", headingLevel);
        }
	}
	 
	public ValueExpression getHeadingLevel() {
		return headingLevel;
	}
	
	public void setHeadingLevel(ValueExpression headingLevel) {
		this.headingLevel = headingLevel;
	}

	public String getComponentType() {
		return "com.akuacom.jsf.component.HtmlSimpleTogglePanel";
	}

	public String getRendererType() {
		return "com.akuacom.jsf.renderkit.AccessibleSimpleToggleControlTemplate";
	}
}
