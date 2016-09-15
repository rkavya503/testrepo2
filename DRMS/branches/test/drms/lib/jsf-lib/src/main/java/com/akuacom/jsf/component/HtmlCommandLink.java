/**
 * 
 */
package com.akuacom.jsf.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 *
 */
public class HtmlCommandLink extends javax.faces.component.html.HtmlCommandLink {

	final public static String COMPONENT_TYPE = "com.akuacom.jsf.CommandLink";

    public HtmlCommandLink() {
        super();
        setRendererType("com.akuacom.jsf.CommandLinkRenderer");
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
    
//    private void handleAttribute(String name, Object value) {
//        List<String> setAttributes = null;
//        String cname = this.getClass().getName();
//        if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
//            setAttributes = (List<String>)this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
//            if (setAttributes == null) {
//                setAttributes = new ArrayList<String>(6);
//                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
//            }
//            if (value == null) {
//                setAttributes.remove(name);
//            } else if (!setAttributes.contains(name)) {
//                setAttributes.add(name);
//            }
//        }
//    }

}
