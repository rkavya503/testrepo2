package com.akuacom.jsf.taglib;

import javax.faces.component.UIComponent;

public class TreeTableTag extends DataTableTag {

	public String getComponentType() {
		return "com.akuacom.jsf.TreeTable";
	}

	public String getRendererType() {
		return "com.akuacom.jsf.TreeTableRenderer";
	}


	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
	}

	@Override
	public void release() {
		super.release();
	}
	
}
