package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.UIColumn;
import org.richfaces.component.UIDataTable;
import org.richfaces.component.html.HtmlColumnGroup;

import com.akuacom.jsf.component.ExtTreeTable;
import com.akuacom.jsf.component.ExtendedHtmlDataTable;
import com.akuacom.jsf.model.TableContentProvider;

public class CellRenderer extends org.richfaces.renderkit.CellRenderer {

	protected void doEncodeBegin(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		
		java.lang.String clientId = component.getClientId(context);
		boolean isHeader = (styleClass(context, component)).contains("header");
		if (isHeader) {
			writer.startElement("th", component);

		} else {
			//if(this.getColSpan(context, component)==0)
				//no need to render
			//	return ;
			writer.startElement("td", component);
		}
		
		
		Map<String, Object> requestMap = context.getExternalContext()
				.getRequestMap();
		
		Object obj= requestMap.get(ExtTreeTable.FIRST_ROW);
		boolean firstRow = obj!=null?(Boolean)obj:true;
		boolean isWidthFixed =isWidthFixed(context,component);
			//requestMap.get(ExtTreeTable.NODE_ON_EXPAND)!=null;
		
		getUtils().writeAttribute(writer, "class",
				styleClass(context, component));
		//getUtils().writeAttribute(writer, "id", clientId);
		if (firstRow) {
			String[] att = new String[] { "abbr", "align", "axis", "bgcolor", "char",
						"charoff", "colspan", "dir", "headers", "height",
						"lang", "nowrap", "onclick", "ondblclick", "onkeydown",
						"onkeypress", "onkeyup", "onmousedown", "onmousemove",
						"onmouseout", "onmouseover", "onmouseup", "rowspan",
						"scope", "style", "title", "valign", "width",
						"xml:lang" };
				if(isWidthFixed)
					 att = new String[] { "abbr", "align", "axis", "bgcolor", "char",
						"charoff", "colspan", "dir", "headers", "height",
						"lang", "nowrap", "onclick", "ondblclick", "onkeydown",
						"onkeypress", "onkeyup", "onmousedown", "onmousemove",
						"onmouseout", "onmouseover", "onmouseup", "rowspan",
						"scope", "style", "title", "valign","xml:lang" };
				getUtils().encodeAttributesFromArray(context,component,att);
			
				if(isWidthFixed){
					String width = getWidth(context,component);
					if(width!=null){
						getUtils().writeAttribute(writer, "width", width+"px");
					}
				}
				
		} else {
			getUtils().encodeAttributesFromArray(
					context,
					component,
					new String[] { "abbr", "axis", "char", "charoff",
							"colspan", "dir", "headers", "height", "lang",
							"nowrap", "onclick", "ondblclick", "onkeydown",
							"onkeypress", "onkeyup", "onmousedown",
							"onmousemove", "onmouseout", "onmouseover",
							"onmouseup", "rowspan", "scope", "style", "title",
							"xml:lang" });
		}
		
		if(!isHeader){
			//empty-cell:show dosn't work on IE, a workaround to fix this problem
			//int colspan = this.getColSpan(context, component);
			//if(colspan>1)
			//	getUtils().writeAttribute(writer, "colspan", colspan);
			
			writer.startElement("span", component);
			writer.writeAttribute("style", "zoom:1;", null);
		}
	}
	
	/*
	protected int getColSpan(FacesContext context,UIComponent component){
		TableContentProvider<Object> provider=getContentProvider(context,component);
		if(provider!=null && component instanceof UIColumn){
			int rowspan=provider.getColSpan(provider.getCurrent(), (UIColumn)component);
			return rowspan;
		}
		return 1;
	}*/
	
	
	protected boolean isWidthFixed(FacesContext context,UIComponent component){
		if(!(component.getParent() instanceof UIDataTable)){
			return false;
		}
		return ExtTreeTable.isWidthFixed(context, (UIDataTable) component.getParent());
	}
	
	protected String getWidth(FacesContext context,UIComponent component){
		int idx =getIndexOfColumn(component);
		Map<String, String> params=context.getExternalContext().getRequestParameterMap();
		String inputId=ExtTreeTable.getWidthFieldId(context,(UIDataTable) component.getParent());
				String widths=params.get(inputId);
		//TODO consider span
		if(widths!=null){
			String str[]=widths.split(",");
			if(idx>=0 && idx<str.length){
				return str[idx];
			}
		}
		return null;
	}
	
	protected int getIndexOfColumn(UIComponent component){
		List<UIComponent> list  = new ArrayList<UIComponent> ();
		for(UIComponent com:component.getParent().getChildren()){
			if(com.isRendered()){
				list.add(com);
			}
		}
		return list.indexOf(component);
	}
	
	
	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
		//boolean isHeader = (styleClass(context, component)).contains("header");
		//if(!isHeader){
		//	int colspan =this.getColSpan(context, component);
		//	if(colspan==0)
		//		return;
		//}
		super.encodeChildren(context, component);
	}

	@Override
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		boolean isHeader = (styleClass(context, component)).contains("header");
        if(!isHeader){
        	//int colspan = this.getColSpan(context, component);
			//if(colspan==0)
			//	return;
        	writer.endElement("span");
        }
		super.doEncodeEnd(writer, context, component);
	}
	
	protected TableContentProvider<Object> getContentProvider(FacesContext context,UIComponent component){
		UIDataTable	 parent = null;
		if(component.getParent() instanceof HtmlColumnGroup){
			parent = (UIDataTable) component.getParent().getParent();
		}else
			parent = (UIDataTable) component.getParent();
		
		if(parent instanceof ExtendedHtmlDataTable){
			return (TableContentProvider<Object>) ((ExtendedHtmlDataTable)parent)
								.getTableContentProvider();
		}
		return null;
	}
	
	
	public String styleClass(FacesContext context , UIComponent component){
		String styleClass = super.styleClass(context, component);
		boolean isHeader = (super.styleClass(context, component)).contains("header");
		if(!isHeader){
			String customClass= this.customStyleClass(context, component);
			if(customClass!=null)
				return styleClass+" "+customClass;
		}
		return styleClass;
	}
	
	protected String customStyleClass(FacesContext context , UIComponent component){
		TableContentProvider<Object> provider = (TableContentProvider<Object>) getContentProvider(context,component);
		if(provider!=null && component instanceof UIColumn){
			UIColumn column = (UIColumn) component;
			String style =provider.getCellStyleClass(provider.getCurrent(),column);
			if(style!=null)
				return style;
		}
		return null;
	}
}
