package com.akuacom.jsf.renderkit;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.richfaces.component.UIDatascroller;
import org.richfaces.renderkit.AbstractTableRenderer;

/**
 * Renderer for component class org.richfaces.renderkit.html.DataTableRenderer
 */
public class ExtendedDataTableRenderer extends AccessibleDataTableRenderer {

	protected static final String SORT_FILTER_PARAMETER = "fsp";
	
	protected static final String FILTER_INPUT_FACET_NAME = "filterValueInput";

	protected static final String REQUIRES_SCRIPTS_PARAMETER = AbstractTableRenderer.class.getName() + ":REQUIRES_SCRIPTS";
	
	public static final String SORT_RESET_PARAMETER = "_sortReset";
	
	public ExtendedDataTableRenderer() {
		super();
	}
	
	@Override
	protected String buildAjaxFunction(FacesContext context, UIComponent column, boolean sortable) {
		UIComponent table = column.getParent();
		String id = table.getClientId(context);

		//Ajax submission requires scripts
		setRequiresScripts(context);
		
		JSFunction ajaxFunction = AjaxRendererUtils.buildAjaxFunction(table, context);
		Map<String, Object> eventOptions = AjaxRendererUtils.buildEventOptions(context, table, true);
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> parameters = 
		    (Map<String, Object>) eventOptions.get("parameters");
		
		
		parameters.put(id, SORT_FILTER_PARAMETER);
		if (sortable) {
			parameters.put(SORT_FILTER_PARAMETER, column.getClientId(context));
		}
		
		//add two parameters so that when column is click for sorting
		//scroller will scroll to first page
		UIDatascroller scroller=getDataScroller(column.getParent());
		if(scroller!=null){
			parameters.put(" ajaxSingle", scroller.getClientId(context));
			parameters.put(scroller.getClientId(context), 1);
			parameters.put(column.getParent().getClientId(context)+SORT_RESET_PARAMETER,true);
		}
		
		ajaxFunction.addParameter(eventOptions);
		
		StringBuffer buffer = new StringBuffer();
		ajaxFunction.appendScript(buffer);
		
		return buffer.toString();
	}
	
	
	protected UIDatascroller getDataScroller(UIComponent dataTable ){
		//TODO
		UIComponent com = dataTable.getFacet("footer");
		if(com!=null && com instanceof UIDatascroller){
			return (UIDatascroller) com;
		}
		return null;
	}
}
