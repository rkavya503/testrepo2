package com.akuacom.jsf.model;

import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.richfaces.component.UIDataTable;
import org.richfaces.component.UISwitchablePanel;
import org.richfaces.component.UITab;
import org.richfaces.component.UITabPanel;
import org.richfaces.model.AbstractTreeDataModel;
import org.richfaces.model.TreeRowKey;

import com.akuacom.jsf.component.ExtTreeTable;

public class Utils {
	
	public static String parseSortColumn(String expr){
		if(expr==null) return null;
		if(expr.trim().startsWith("#"))
			return expr.substring(2,expr.length()-1);
		else
			return expr;
	}
	
	public static boolean StringEquals(String str1,String str2){
		if(str1==null && str2==null) return true;
		
		if(str1!=null){
			if(str2==null) return false;
			return str1.trim().equals(str2.trim());
		}else{
			return StringEquals(str2,str1);
		}
	}
	
	public static boolean objectEquals(Object obj1, Object obj2){
		if(obj1==null && obj2==null) return true;
		
		if(obj1!=null){
			return  obj1.equals(obj2);
		}else{
			return obj2.equals(obj1);
		}
	}
	
	public static int getLastSegment(TreeRowKey<Integer> rowKey){
		String path = rowKey.getPath();
		int i = path.lastIndexOf(AbstractTreeDataModel.SEPARATOR);
		return Integer.parseInt(path.substring(i+1));
	}
	
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string){
	    if( c != null && string != null )
	    {
	        try
	        {
	            return Enum.valueOf(c, string.trim().toUpperCase());
	        }
	        catch(IllegalArgumentException ex)
	        {
	        }
	    }
	    return null;
	}
	
	//for non ajax request
	public static boolean shouldRender(FacesContext context,UIComponent component){
		if(!component.isRendered()) return false;
		//TODO add more, such as toggle panel
		if(component instanceof UITab){
			UITab tab = ((UITab)component);
			UITabPanel panel =tab.getPane();
			
			//selected tab
			String activeTab = (String) panel.getValue();
			boolean active  = false;
			if(activeTab!=null) {
				if(activeTab.equals("") && panel.getRenderedTabs().next().equals(tab)
					|| activeTab.equals(tab.getId()))
				active=true;
			}
				
			String method = tab.getSwitchTypeOrDefault();
			if (!active &&   (tab.isDisabled() || !UISwitchablePanel.CLIENT_METHOD.equals(method)))
				return false;
		}
		
		if(component.getParent()!=null){
			return shouldRender(context,component.getParent());
		}else
			return true;
	}
	
	//for ajax request
	public static boolean shouldRerender(FacesContext context,UIComponent component){
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		Set<String> areas=ajaxContext.getAjaxAreasToRender();
		String clientId=AjaxRendererUtils.getAbsoluteId(component);
		if(areas.contains(clientId))
			return true;
		if(component.getParent()!=null){
			return shouldRerender(context,component.getParent());
		}else{
			return false;
		}
	}
	
	
	
}	
