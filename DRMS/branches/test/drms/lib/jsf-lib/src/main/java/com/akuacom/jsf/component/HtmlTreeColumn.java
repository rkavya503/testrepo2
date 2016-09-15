package com.akuacom.jsf.component;

import org.richfaces.component.html.HtmlColumn;

public class HtmlTreeColumn extends HtmlColumn {
	
	public static final  String COMPONENT_FAMILY = "com.akuacom.jsf.TreeColumn";

	public static final  String COMPONENT_TYPE = "com.akuacom.jsf.TreeColumn";
	
	public HtmlTreeColumn(){
		setRendererType("com.akuacom.jsf.TreeColumnRenderer");
	}
	
	public String getFamily(){
		return HtmlTreeColumn.COMPONENT_FAMILY;
	}
}
