package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.model.ListRowKey;
import org.richfaces.renderkit.AbstractRowsRenderer;

import com.akuacom.jsf.component.ExtTreeTable;
import com.akuacom.jsf.component.ExtendedHtmlDataTable.SelectionMode;
import com.akuacom.jsf.component.HtmlTreeColumn;
import com.akuacom.jsf.component.HtmlTreeTable;
import com.akuacom.jsf.model.InternalTreeBuilder;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.jsf.model.TreeNode;
import com.akuacom.jsf.model.Utils;

public class TreeColumnRenderer<T> extends CellRenderer {
	
	protected static final String[] EMPTY_ARRAY = {};
	
	@Override
	public void decode(FacesContext context, UIComponent component) {
		super.decode(context, component);
		Object rowKey = ExtTreeTable.rowKey(context,component);
		//check if the node is expanded or collapsed
		Map<String,String> params = context.getExternalContext().getRequestParameterMap();
		String rowId = ExtTreeTable.rowId(context, component);
		if(rowId!=null){
			String fieldId =ExtTreeTable.hiddenStatusField(rowId);
			ExtTreeTable.Status status = Utils.getEnumFromString(ExtTreeTable.Status.class,params.get(fieldId));
			InternalTreeBuilder<T> provider =(InternalTreeBuilder)getTreeContentProvider(context,component);
			TreeNode<T> node= provider.getNode(rowKey);
			if(node!=null){
				if(status==null){
					if(provider.hasChildren(node.getRow())){
						status = ExtTreeTable.Status.KIDS_NOT_LOADED;
					}else{
						status = ExtTreeTable.Status.NO_KIDS;
					}
				}
				node.setStatus(status);
				Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
				if(status==ExtTreeTable.Status.KIDS_LOADING){
					requestMap.put(ExtTreeTable.NODE_ON_EXPAND, node);
					requestMap.put(ExtTreeTable.NODE_ON_EXPAND_ID, rowId);
				}
			}
		}
	}
	
	
	
	public String styleClass(FacesContext context, UIComponent component) {
		StringBuffer styleClass = new StringBuffer();
		// Construct predefined classes
		Map<String, Object> requestMap = context.getExternalContext()
				.getRequestMap();
		Object parentPredefined = requestMap
				.get(AbstractRowsRenderer.SKIN_CELL_CLASS_KEY);
		if (null != parentPredefined) {
			styleClass.append(parentPredefined).append(" ");
		} else {
			styleClass.append("rich-table-cell ");
		}
		// Append class from parent component.
		Object parent = requestMap.get(AbstractRowsRenderer.CELL_CLASS_KEY);
		if (null != parent) {
			styleClass.append(parent).append(" ");
		}
		Object custom = component.getAttributes().get("styleClass");
		if (null != custom) {
			styleClass.append(custom);
		}
		String customClass = this.customStyleClass(context, component);
		if(customClass!=null){
			styleClass.append("  ");
			styleClass.append(customClass);
		}
		return styleClass.toString();
	}
	
	
	protected void doEncodeBegin(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		HtmlTreeColumn treeItem = (HtmlTreeColumn) component;
		
		ListRowKey<?> rowKey = (ListRowKey<?>) ExtTreeTable.rowKey(context, component);
		int depth = rowKey.depth();
		
		java.lang.String clientId = component.getClientId(context);
		boolean isHeader = (styleClass(context, component)).contains("header");
		
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		
		boolean firstRow=(Boolean) requestMap.get(ExtTreeTable.FIRST_ROW);
		boolean isWidthFixed =this.isWidthFixed(context, component);
		
		if (isHeader) {
			writer.startElement("th", component);
			//TODO ?
			getUtils().writeAttribute(writer, "class",
					styleClass(context, component));
			getUtils().writeAttribute(writer, "id", clientId);
			getUtils().encodeAttributesFromArray(
					context,
					component,
					new String[] { "abbr", "align", "axis", "bgcolor", "char",
							"charoff", "colspan", "dir", "headers", "height",
							"lang", "nowrap", "onclick", "ondblclick", "onkeydown",
							"onkeypress", "onkeyup", "onmousedown", "onmousemove",
							"onmouseout", "onmouseover", "onmouseup", "rowspan",
							"scope", "style", "title", "valign", "width",
							"xml:lang" });
			
		} else {
			writer.startElement("td", component);
			writer.writeAttribute(HTML.id_ATTRIBUTE, clientId, null);
			writer.writeAttribute("class", "rich-table-cell", null);
			String[] att=null;
			if(firstRow){
				 att = new String[] { "abbr", "align", "axis", "bgcolor", "char",
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
				
			}else{
				getUtils().encodeAttributesFromArray(
						context,
						component,
						new String[] { "abbr", "axis", "char",
								"charoff", "colspan", "dir", "headers",
								"lang", "nowrap", "onclick", "ondblclick", "onkeydown",
								"onkeypress", "onkeyup", "onmousedown", "onmousemove",
								"onmouseout", "onmouseover", "onmouseup", "rowspan",
								"scope", "style", "title",
								"xml:lang" });
			}
			
			writer.startElement("table", component);
			writer.startElement("tbody", component);
			writer.startElement("tr", component);
			
			//writer.writeAttribute(HTML.id_ATTRIBUTE, clientId+":t_tr", null);
			ExtTreeTable.Status status = getStatus(context,treeItem);
			
			String rowId =ExtTreeTable.rowId(context, treeItem);
			
			//add indent 
			for(int i = 0 ; i < depth-1; i++){
				writer.startElement("td", component);
				writer.writeAttribute(HTML.id_ATTRIBUTE, clientId+":t_ind", null);
				writer.writeAttribute(HTML.class_ATTRIBUTE, "treetable-indent", null);
				writer.startElement(HTML.DIV_ELEM,component);
				//writer.writeAttribute(HTML.id_ATTRIBUTE, clientId+":t_ind:d", null);
				writer.writeAttribute(HTML.class_ATTRIBUTE, "treetable-indent", null);
				writer.endElement(HTML.DIV_ELEM);
				writer.endElement("td");
				//"<td class='treetable-indent'> <div class='treetable-indent'></div></td>";
			}
			
			HtmlTreeTable table = this.getTreeTable(component);
			
			//checkbox for multiple selection
			if(getTreeTable(component).selectionMode().equals(SelectionMode.MULTIPLE)){
				TreeContentProvider<T> provider = this.getTreeContentProvider(context, component);
				writer.startElement(HTML.td_ELEM,component);
				writer.startElement(HTML.INPUT_ELEM, component);
				writer.writeAttribute("type", "checkbox", null);
				
				//writer.writeAttribute("id", table.getClientId(context)+"_"+table.getRowKey(),null);
				writer.writeAttribute("name", ExtTreeTable.getSelectedFieldId(context, table),null);
				
				String clickHandler ="_d_click(this);";
				if(table.getOnSelection()!=null){
					clickHandler+=table.getOnSelection();
				}
				writer.writeAttribute("onclick",clickHandler,null);
				writer.writeAttribute("value", table.getRowKey(),null);
				
				String styleClass="";
				//keep selection state
				if(provider.getSelection()!=null){
					String path =rowKey.toString();
					if(provider.getSelection().isSelected(path)){
						writer.writeAttribute("checked","true" ,null);
						styleClass="checked ";
					}
				}
				
				if(provider.isSelectable(provider.getCurrent())){
					styleClass+= "checkforsel";
					writer.writeAttribute("class", styleClass, null);
				}else{
					writer.writeAttribute("disabled", "true", null);
					styleClass+= "checknotsel";
					writer.writeAttribute("class", styleClass, null);
				}
				
				
				writer.endElement(HTML.INPUT_ELEM);
				writer.endElement(HTML.td_ELEM);
			}
			
			//image
			String imgpath=getImagePath(context,treeItem);
			if(imgpath==null){
				//"<td class='treetable-indent'> <div class='treetable-indent'></div></td>";
				writer.startElement("td", component);
				writer.writeAttribute(HTML.class_ATTRIBUTE, "treetable-indent", null);
				writer.startElement(HTML.DIV_ELEM,component);
				writer.writeAttribute(HTML.class_ATTRIBUTE, "treetable-indent", null);
				writer.endElement(HTML.DIV_ELEM);
				writer.endElement("td");
				
			}else{
				String imgid =clientId+"_img";
				//<td class='treetable-image'><div class='treetable-indent'>
				//<img id='"+imgid+"' class='treetable-folding' src='"+expandedImage+"'/ onclick=\"handler();"></div></td>" );
				String handler = getOnClickHandler(context,treeItem);
				
				writer.startElement(HTML.td_ELEM, component);
				writer.writeAttribute(HTML.onclick_ATTRIBUTE, "if((window.event|| event)) " +
						"{Event.stop(window.event || event); "+handler+"}", null);
				writer.writeAttribute(HTML.style_ATTRIBUTE, "cursor:pointer",null);
				
				writer.writeAttribute(HTML.class_ATTRIBUTE,"treetable-image", null);
				writer.startElement(HTML.DIV_ELEM, component);
				writer.writeAttribute(HTML.class_ATTRIBUTE,"treetable-indent", null);
				
				writer.startElement(HTML.IMG_ELEMENT, component);
				//tab index set to zero to allow this element focusable
				writer.writeAttribute(HTML.tabindex_ATTRIBUTE,0,null);
				writer.writeAttribute(HTML.onkeydown_ATTRIBUTE,"if((window.event|| event).keyCode==13) " +
						"{Event.stop(window.event || event); "+handler+"}",null);
				
				if( (status.intValue() & ExtTreeTable.I_COLLPASED)!=0){
					writer.writeAttribute("alt", "Expand", null);
				}
				else if( (status.intValue() & ExtTreeTable.I_EXPANDED)!=0){
					writer.writeAttribute("alt", "Collapse", null);
				}
				
				writer.writeAttribute(HTML.id_ATTRIBUTE, imgid, null);
				writer.writeAttribute(HTML.src_ATTRIBUTE, imgpath, null);
				//writer.writeAttribute(HTML.onclick_ATTRIBUTE, getOnClickHandler(context,treeItem), null);
				writer.endElement(HTML.IMG_ELEMENT);
				
				//focus this image after load complete
				String  expandRow =(String) requestMap.get(ExtTreeTable.NODE_ON_EXPAND_ID);
				if(rowId.equals(expandRow)){
					writer.startElement(HTML.SCRIPT_ELEM,component);
					writer.write("$('"+imgid+"').focus();");
					writer.endElement(HTML.SCRIPT_ELEM);
				}
				
				writer.endElement(HTML.DIV_ELEM);
				writer.endElement(HTML.td_ELEM);
			}
			
			//content
			//"<td> <span class='treeTableText'>" ....
			writer.startElement(HTML.td_ELEM, component);
			
			//write hidden field for node status
			writer.startElement((String)HTML.INPUT_ELEM, component);
			writer.writeAttribute(HTML.TYPE_ATTR,HTML.INPUT_TYPE_HIDDEN,null);
			writer.writeAttribute(HTML.NAME_ATTRIBUTE,ExtTreeTable.hiddenStatusField(rowId),null);
			writer.writeAttribute(HTML.id_ATTRIBUTE,ExtTreeTable.hiddenStatusField(rowId),null);
			writer.writeAttribute(HTML.value_ATTRIBUTE, status, null);
			writer.endElement((String)HTML.INPUT_TYPE_HIDDEN);
			
			writer.startElement(HTML.SPAN_ELEM, component);
			writer.writeAttribute(HTML.class_ATTRIBUTE, "treeTableText", null);
		}
	}
	
	protected boolean shouldHidden(){
		return false;
	}
	
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		boolean isHeader = (styleClass(context, component)).contains("header");
		if (isHeader) {
			writer.endElement("th");

		} else {
			writer.endElement(HTML.SPAN_ELEM);
			writer.endElement(HTML.td_ELEM);
			writer.endElement(HTML.TR_ELEMENT);
			writer.endElement(HTML.TBOBY_ELEMENT);
			writer.endElement(HTML.TABLE_ELEMENT);
			writer.endElement(HTML.td_ELEM);
		}
	}
	
	protected ExtTreeTable.Status getStatus(FacesContext context,HtmlTreeColumn treeItem){
		ListRowKey<?> rowKey = (ListRowKey<?>) ExtTreeTable.rowKey(context, treeItem);
		InternalTreeBuilder<?> provider =(InternalTreeBuilder<?>) getTreeContentProvider(context,treeItem);
		TreeNode<?> node= provider.getNode(rowKey);
		ExtTreeTable.Status status  = node.getStatus();
		return status;
	}
	
	protected String getImagePath(FacesContext context,HtmlTreeColumn treeItem){
		ExtTreeTable.Status status = getStatus(context,treeItem);
		
		if( (status.intValue() & ExtTreeTable.I_COLLPASED)!=0){
			String collapsedImage=getResource(
    		"/com/akuacom/jsf/renderkit/images/collapsed_gif.gif").getUri(context, null);
			return collapsedImage;
		}
		
		if( (status.intValue() & ExtTreeTable.I_EXPANDED)!=0){
			String expandedImage=getResource(
        	"/com/akuacom/jsf/renderkit/images/expanded_gif.gif").getUri(context, null);
			return expandedImage;
		}
		return null;
	}
	
	protected String getOnClickHandler(FacesContext context,HtmlTreeColumn treeItem){
		String handler = "";
		String rowId =  ExtTreeTable.rowId(context, treeItem);
		String clientId = treeItem.getClientId(context);
		String imgid =clientId+"_img";
		
		ExtTreeTable.Status status = getStatus(context,treeItem);
		
		switch(status){
		case KIDS_NOT_LOADED:
			handler="Event.fire(this, 'richext:treeTable:onExpand'," 
					+ " {'"+ExtTreeTable.PARAM_EXPAND_ROW+"': '" + rowId 
					+ "','"+ExtTreeTable.PARAM_STATUS_FIELD+"':'"+ExtTreeTable.hiddenStatusField(rowId)
					+ "','"+ExtTreeTable.PARAM_INTENDED_STATUS+"':'"+ExtTreeTable.Status.KIDS_LOADING+"'}" 
					+	");";
			break;
		case KIDS_LOADING:
			break;
			
		case KIDS_LOADED_AND_EXPANDED:
		case KIDS_LOADED_AND_COLLAPSED:
			handler ="toggleTreeNode('"+rowId+"','"+imgid+"')";
			break;
		}
		return handler;
	}
	
	protected HtmlTreeTable getTreeTable(UIComponent component){
		HtmlTreeTable treeTable  = (HtmlTreeTable) component.getParent();
		return treeTable;
	}
	
	protected TreeContentProvider<T> getTreeContentProvider(FacesContext context,UIComponent component){
		HtmlTreeTable treeTable  = (HtmlTreeTable) component.getParent();
		return treeTable.getTreeContentProvider();
	}
	
	protected Class<? extends UIComponent> getComponentClass() {
		return HtmlTreeColumn.class;
	}
}
