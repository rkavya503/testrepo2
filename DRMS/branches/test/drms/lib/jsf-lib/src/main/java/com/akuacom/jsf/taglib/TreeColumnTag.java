package com.akuacom.jsf.taglib;

import org.richfaces.taglib.ColumnTag;

public class TreeColumnTag extends ColumnTag {
	
	
	public String getComponentType() {
		return "com.akuacom.jsf.TreeColumn";
	}

	public String getRendererType() {
		return "com.akuacom.jsf.TreeColumnRenderer";
	}
}
