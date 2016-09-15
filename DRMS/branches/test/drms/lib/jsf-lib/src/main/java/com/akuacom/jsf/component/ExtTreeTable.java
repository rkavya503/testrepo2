package com.akuacom.jsf.component;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.UIDataTable;

public class ExtTreeTable {
	
	
	//has kids or not
	public static final int I_NO_KIDS                   = 1;
	public static final int I_HAS_KIDS                  = 1 << 1;
	
	//collapsed or expanded
	public static final int I_COLLPASED                 = 1 << 2;
	public static final int I_EXPANDED                  = 1 << 3;
	
	//kids loaded or not 
	public static final int I_LOADING              	    = 1 << 4;
	public static final int I_LOADED                    = 1 << 5;
	public static final int I_NOT_LOAD                  = 1 << 6;
	
	//possible status 
	public static final int I_HAS_NO_KIDS               = I_NO_KIDS;
	public static final int I_KIDS_NOT_LOADED           = I_HAS_KIDS | I_COLLPASED | I_NOT_LOAD;
	public static final int I_KIDS_LOADING		   		= I_HAS_KIDS | I_COLLPASED | I_LOADING;
	public static final int I_KIDS_LOADED_AND_EXPANDED  = I_HAS_KIDS | I_EXPANDED  | I_LOADED;
	public static final int I_KIDS_LOADED_AND_COLLAPSED = I_HAS_KIDS | I_COLLPASED | I_LOADED;
	
	public enum Status {
		NO_KIDS(I_NO_KIDS),
		KIDS_NOT_LOADED(I_KIDS_NOT_LOADED ),
		KIDS_LOADING(I_KIDS_LOADING),
		KIDS_LOADED_AND_EXPANDED(I_KIDS_LOADED_AND_EXPANDED), 
		KIDS_LOADED_AND_COLLAPSED(I_KIDS_LOADED_AND_COLLAPSED);
		
		private int status;
		
		Status(int status){
			this.status =status;
		}
		
		public int intValue() {
			return status;
		}
		
	}
	//others 
	public static final String NODE_STATE_POSTFIX=":sts";
	
	public static final String PARAM_INTENDED_STATUS="intendedStatus";
	
	public static final String EVT_PARAM_INTENDED_STATUS="event.memo."+PARAM_INTENDED_STATUS;
	
	public static final String PARAM_STATUS_FIELD="field";
	
	public static final String EVT_PARAM_STATUS_FIELD="event.memo."+PARAM_STATUS_FIELD;
	
	public static final String PARAM_EXPAND_ROW ="row";
	
	public static final String EVT_PARAM_EXPAND_ROW ="event.memo."+PARAM_EXPAND_ROW;
	
	public static final String TABLE_HEADER_POSTFIX="_head";
	
	public static final String TABLE_HIDDEN_HEADER_POSTFIX="_fake_header";
	
	public static final String TABLE_BODY_POSTFIX="_body";
	
	public static final String TABLE_SCROLL_DIV_POSTFIX="_scroll_div";
	
	public static final String NODE_ON_EXPAND="treetable_node_expand";
	
	public static final String NODE_ON_EXPAND_ID = "treetable_row_expand_id";
	
	public static final String FIRST_ROW="_table_first_row";

	public static final String TABLE_LAST_SELECTED="treetable_last_selected";
	
	public static String hiddenStatusField(String rowId){
		if(rowId==null) return null;
		return rowId+NODE_STATE_POSTFIX;
	}
	
	public static String rowId(FacesContext context,HtmlTreeTable table){
		return table.getClientId(context);
	}
	
	public static String rowId(FacesContext context,HtmlTreeColumn column){
		HtmlTreeTable table = (HtmlTreeTable) column.getParent();
		return rowId(context,table);
	}
	
	public static String rowId(FacesContext context,UIComponent component){
		if(component instanceof HtmlTreeColumn)
			return rowId(context,(HtmlTreeColumn)component);
		else if(component instanceof HtmlTreeTable)
			return rowId(context,(HtmlTreeTable)component);
		else throw new IllegalArgumentException("component must be HtmlTreeColumn or HtmlTreeTable");
	}
	
	public static Object rowKey(FacesContext context,HtmlTreeTable table){
		return table.getRowKey();
	}
	
	public static Object rowKey(FacesContext context,HtmlTreeColumn column){
		HtmlTreeTable table = (HtmlTreeTable) column.getParent();
		return rowKey(context,table);
	}
	
	public static Object rowKey(FacesContext context,UIComponent component){
		if(component instanceof HtmlTreeColumn)
			return rowKey(context,(HtmlTreeColumn)component);
		else if(component instanceof HtmlTreeTable)
			return rowKey(context,(HtmlTreeTable)component);
		else throw new IllegalArgumentException("component must be HtmlTreeColumn or HtmlTreeTable");
	}
	
	public static String getHeaderTableId(FacesContext context,UIDataTable table){
		return table.getClientId(context)+TABLE_HEADER_POSTFIX;
	}
	
	public static String getHiddenHeaderId(FacesContext context,UIDataTable table){
		return table.getClientId(context)+TABLE_HIDDEN_HEADER_POSTFIX;
	}
	
	
	public static String getBodyTableId(FacesContext context,UIDataTable table){
		return table.getClientId(context)+TABLE_BODY_POSTFIX;
	}
	
	public static String getScrollDivId(FacesContext context,UIDataTable table){
		return table.getClientId(context)+TABLE_SCROLL_DIV_POSTFIX;
	}
	
	public static String getSelectedFieldId(FacesContext context,UIDataTable table){
		return table.getBaseClientId(context)+"_selected";
	}
	
	public static String getSelectAllFieldId(FacesContext context,UIDataTable table){
		return table.getBaseClientId(context)+"_all_selected";
	}
	
	public static String getWidthFieldId(FacesContext context,UIDataTable table){
		return table.getBaseClientId(context)+"_widths";
	}
	
	public static boolean isWidthFixed(FacesContext context,UIDataTable component){
		Map<String, String> params=context.getExternalContext().getRequestParameterMap();
		String inputId=ExtTreeTable.getWidthFieldId(context,component);
		String widths=params.get(inputId);
		return widths != null && widths.trim().length()!=0;
	}
}
