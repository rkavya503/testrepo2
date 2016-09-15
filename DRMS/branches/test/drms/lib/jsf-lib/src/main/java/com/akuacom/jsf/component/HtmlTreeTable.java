package com.akuacom.jsf.component;

import javax.faces.model.DataModel;

import com.akuacom.jsf.model.InternalTreeBuilder;
import com.akuacom.jsf.model.TreeDataModel;

public class HtmlTreeTable<T> extends ExtendedHtmlDataTable {
	
	final public static  String COMPONENT_FAMILY = "com.akuacom.jsf.TreeTable";
	
	public HtmlTreeTable() {
		setRendererType("com.akuacom.jsf.TreeTableRenderer");
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	protected DataModel getDataModel() {
		InternalTreeBuilder<T> provider = this.getTreeContentProvider();
		if(provider==null)
			return super.getDataModel();
		else{
			/*if(beforeRenderPhase){
				 //provider.clearTreeNodeCache(null);
				 provider.updateModel();
				 beforeRenderPhase = false;
			}*/
			return  new TreeDataModel<T>(getTreeContentProvider());
		}
	}
	
	public InternalTreeBuilder<T> getTreeContentProvider(){
		return (InternalTreeBuilder<T>) this.getTableContentProvider();
	}
	
}
