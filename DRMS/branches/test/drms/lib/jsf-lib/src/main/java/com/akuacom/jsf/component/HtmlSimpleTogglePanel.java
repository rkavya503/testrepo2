package com.akuacom.jsf.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class HtmlSimpleTogglePanel extends
		org.richfaces.component.html.HtmlSimpleTogglePanel {
	final public static String COMPONENT_TYPE = "com.akuacom.jsf.component.HtmlSimpleTogglePanel";

    public HtmlSimpleTogglePanel() {
        super();
        setRendererType("com.akuacom.jsf.renderkit.AccessibleSimpleToggleControlTemplate");
    }

    private java.lang.String headingLevel;
    
    public java.lang.String getHeadingLevel() {
        if (null != this.headingLevel) {
            return this.headingLevel;
        }
        ValueExpression _ve = getValueExpression("headingLevel");
        if (_ve != null) {
            return (java.lang.String) _ve.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setHeadingLevel(java.lang.String headingLevel) {
        this.headingLevel = headingLevel;
//        handleAttribute("headingLevel", accesskey);
    }

    private Object[] _values;

    public Object saveState(FacesContext _context) {
        if (_values == null) {
            _values = new Object[2];
        }
        _values[0] = super.saveState(_context);
        _values[1] = headingLevel;
        return _values;

    }
	
    public void restoreState(FacesContext _context, Object _state) {
        _values = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.headingLevel = (java.lang.String) _values[1];
    }
}
