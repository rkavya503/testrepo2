package com.akuacom.jsf.view;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.ajax4jsf.application.ViewHandlerWrapper;

public class RestoreAnywayViewHandler extends ViewHandlerWrapper {

	public RestoreAnywayViewHandler(ViewHandler parent){
		super(parent);
	}
	
	@Override
	public UIViewRoot restoreView(FacesContext context, String viewId) {
		UIViewRoot root =null; 
		root = this.getHandler().restoreView(context, viewId);
		//"restore" view anyway, no view-can-not-restore-exception
		//but we can not restore view state - input values,e.g
		if(root == null) {                      
		       root = createView(context, viewId);
		}
		return root;
	}
}
