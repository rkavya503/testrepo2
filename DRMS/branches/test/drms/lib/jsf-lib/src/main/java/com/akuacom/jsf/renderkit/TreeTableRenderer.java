package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.JSLiteral;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResource;
import org.richfaces.component.Column;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataTable;
import org.richfaces.model.TreeRowKey;
import org.richfaces.model.selection.SimpleSelection;
import org.richfaces.renderkit.TableHolder;

import com.akuacom.jsf.component.ExtTreeTable;
import com.akuacom.jsf.component.ExtendedHtmlDataTable.SelectionMode;
import com.akuacom.jsf.component.HtmlTreeTable;
import com.akuacom.jsf.model.InternalTreeBuilder;
import com.akuacom.jsf.model.TreeContentProvider;

public class TreeTableRenderer<T> extends ExtendedDataTableRenderer {
	
	private TreeRowKey<?> oldRowKey;
	
	private Boolean  hidden;
	
	private InternalTreeBuilder<T> treeContentProvider;
	
	private final InternetResource[] scripts = {
		getResource("/com/akuacom/jsf/renderkit/scripts/treetable.js") 
	};
	
	private final InternetResource[] styles = {
		getResource("/com/akuacom/jsf/renderkit/css/treetable.css") 
	};
	
	private InternetResource[] stylesAll = null;
	
	@Override
	protected InternetResource[] getScripts() {
		if(isRequiresScripts(FacesContext.getCurrentInstance())) {
			InternetResource superscripts[] = super.getScripts();
			InternetResource[] allscripts = new InternetResource[superscripts.length+1];
			System.arraycopy(superscripts,0,allscripts,0,superscripts.length);
			System.arraycopy(scripts,0,allscripts,superscripts.length,scripts.length);
			return allscripts;
		}else
			return null;
	}
	
	protected boolean writeRightBorder(FacesContext context,UIComponent column){
		List<UIComponent> list  = new ArrayList<UIComponent> ();
		for(UIComponent com:column.getParent().getChildren()){
			if(com.isRendered()){
				list.add(com);
			}
		}
		int idx = list.indexOf(column);
		if(idx >=0 && idx ==list.size()-1)
			return false;
		return true;
	}
	
	protected void writeAllImages(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException{
		
		writer.startElement(HTML.IMG_ELEMENT, component);
		writer.writeAttribute(HTML.style_ATTRIBUTE,"display:none",null);
		writer.writeAttribute(HTML.src_ATTRIBUTE, getResource(
    		"/com/akuacom/jsf/renderkit/images/collapsed_gif.gif").getUri(context, null), null);
		writer.endElement(HTML.IMG_ELEMENT);
		
		writer.startElement(HTML.IMG_ELEMENT, component);
		writer.writeAttribute(HTML.style_ATTRIBUTE,"display:none",null);
		writer.writeAttribute(HTML.src_ATTRIBUTE, getResource(
    		"/com/akuacom/jsf/renderkit/images/expanded_gif.gif").getUri(context, null),null);
		writer.endElement(HTML.IMG_ELEMENT);
	}
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected InternetResource[] getStyles() {
		synchronized (this) {
			if (stylesAll == null) {
				InternetResource[] rsrcs = super.getStyles();
				boolean ignoreSuper = rsrcs == null || rsrcs.length == 0;
				boolean ignoreThis = styles == null || styles.length == 0;
				
				if (ignoreSuper) {
					if (ignoreThis) {
						stylesAll = new InternetResource[0];	
					} else {
						stylesAll = styles;
					}
				} else {
					if (ignoreThis) {
						stylesAll = rsrcs;
					} else {
						java.util.Set rsrcsSet = new java.util.LinkedHashSet();
						for (int i = 0; i < rsrcs.length; i++ ) {
							rsrcsSet.add(rsrcs[i]);
						}
						for (int i = 0; i < styles.length; i++ ) {
							rsrcsSet.add(styles[i]);
						}
						stylesAll = (InternetResource[]) rsrcsSet.toArray(new InternetResource[rsrcsSet.size()]);
					}
				}
			}
		}
		return stylesAll;
	}
	
	@Override
	public void doEncodeBegin(ResponseWriter writer, FacesContext context,
			UIDataTable component, ComponentVariables variables)
			throws IOException {
		getTreeContentProvider((HtmlTreeTable) component);
		oldRowKey = null;
		hidden = null;
		
		String clientId = component.getClientId(context);
		
		HtmlTreeTable table = (HtmlTreeTable) component;
		//outer div 
		writer.startElement(HTML.DIV_ELEM,component);
		getUtils().writeAttribute(writer, "id", clientId);
		getUtils().writeAttribute(writer, "class","treetable-container-div");
		
		writeAllImages(writer,context,component);
		/*
		writer.startElement(HTML.DIV_ELEM, component);
		getUtils().writeAttribute(writer, "id", clientId+"_cover");
		getUtils().writeAttribute(writer,"class","treetable-cover");
		writer.endElement(HTML.DIV_ELEM);
		*/
		
		writeTableWidthField(context,component);
		
		if(table.selectionMode().equals(SelectionMode.SINGLE)){
		writer.startElement(HTML.INPUT_ELEM,component);
		getUtils().writeAttribute(writer, "id",ExtTreeTable.getSelectedFieldId(context, component));
		getUtils().writeAttribute(writer, "name",ExtTreeTable.getSelectedFieldId(context, component));
		getUtils().writeAttribute(writer, "type","hidden");
		writer.endElement(HTML.INPUT_ELEM);
		}
		//<div>
		//<div>  cover
		//</div> cover
		
		//A-1 <TABLE>
		//B-1.1 <TBODY>
		//C-1.1.1 <TR>
		//D-1.1.1.1<TD>
		//E-1.1.1.1.1<DIV>
		//F-1.1.1.1.1.1<TABLE> table as header 
		//F'-1.1.1.1.1.1</TABLE>
		//G-1.1.1.1.1.2<DIV> div to make table content scroll
		//G'-1.1.1.1.1.2</DIV>
		//E'-1.1.1.1.1</DIV>
		//D'-1.1.1.1</TD>
		//C'-1.1.1</TR>
		//B'-1.1</TBODY>
		//A'-1</TABLE>
		//A-1
		//</div>
		
		writer.startElement(HTML.TABLE_ELEMENT,component);
		getUtils().writeAttribute(writer, "class", "rich-table rich-scrollable-table-border " + convertToString(component.getAttributes().get("styleClass")) );
		getUtils().writeAttribute(writer, "id", clientId+"_outer" );
		getUtils().writeAttribute(writer, "style", component.getAttributes().get("style") );
		getUtils().encodePassThruWithExclusions(context, component, "value,name,type,id,class,rows,style");
		
		//B-1.1
		writer.startElement(HTML.TBOBY_ELEMENT,component);
		//C-1.1.1
		writer.startElement(HTML.TR_ELEMENT, component);
		getUtils().writeAttribute(writer, "width","100%");
		getUtils().writeAttribute(writer, "valign","top");
		
		//D-1.1.1.1
		writer.startElement(HTML.td_ELEM, component);
		//E-1.1.1.1.1
		writer.startElement(HTML.DIV_ELEM, component);
		
		encodeTableStructure(context, component);
	
	}
	
	/*@Override
	public void doEncodeBegin(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		getTreeContentProvider((HtmlTreeTable) component);
		oldRowKey = null;
		hidden = null;
		super.doEncodeBegin(writer, context, component);
	}*/


	@Override
	public void encodeTBodyAjax(FacesContext context, UIDataTable table)
			throws IOException {
		super.encodeTBodyAjax(context, table);
	}

	@Override
	public void encodeTableStructure(FacesContext context, UIDataTable table)
			throws IOException {
		
		/*Object key = table.getRowKey();
		table.captureOrigValue(context);
		table.setRowKey(context, null);
		
		encodeCaption(context, table);
		
		// Encode colgroup definition.
		ResponseWriter writer = context.getResponseWriter();
		
		int columns = getColumnsCount(table);
		
		//writer.startElement("colgroup", table);
		//writer.writeAttribute("span", String.valueOf(columns), null);
		String columnsWidth = (String) table.getAttributes().get("columnsWidth");
		
		//write col element anyway
		
		String[] widths = columnsWidth!=null? columnsWidth.split(","): new String[]{};
		for (int i = 0; i < columns; i++) {
			writer.startElement("col", table);
			if(widths!=null && i <widths.length){
				writer.writeAttribute("width", widths[i], null);
			}
			writer.endElement("col");
		}
		
		//writer.endElement("colgroup");
		*/
		int columns = getColumnsCount(table);
		encodeHeader(context, table, columns);
		
		//encodeFooter(context, table, columns);
		//table.setRowKey(context,key);
		//table.restoreOrigValue(context);
		
		//super.encodeTableStructure(context, table);
	}
	
	@Override
	public void encodeHeader(FacesContext context, UIDataTable table,
			int numberOfColumns) throws IOException {
		//header 
		//F1 = F-1.1.1.1
		//F1-1 <TABLE>
		//F1-1.1 <THEAD>
		//F1'-1.1 </THEAD>
		//F1'-1 </TABLE>
		ResponseWriter writer = context.getResponseWriter();
		//F-1.1.1.1.1
		writer.startElement(HTML.TABLE_ELEMENT, table);
		getUtils().writeAttribute(writer, "style", "width:100%");
		getUtils().writeAttribute(writer, "class", "header-section"); 
		getUtils().writeAttribute(writer, "id", ExtTreeTable.getHeaderTableId(context,table));
		getUtils().writeAttribute(writer, "cellspacing", 0);
		//getUtils().writeAttribute(writer, "cellpadding", 0);
		
		Object key = table.getRowKey();
		table.captureOrigValue(context);
		table.setRowKey(context, null);
		
		encodeCaption(context, table);
		
		// Encode colgroup definition.
		int columns = getColumnsCount(table);
		
		//writer.startElement("colgroup", table);
		//writer.writeAttribute("span", String.valueOf(columns), null);
		String columnsWidth = (String) table.getAttributes().get("columnsWidth");
		
		//write col element anyway
		/*
		String[] widths = columnsWidth!=null? columnsWidth.split(","): new String[]{};
		for (int i = 0; i < columns; i++) {
			writer.startElement("col", table);
			if(widths!=null && i <widths.length){
				writer.writeAttribute("width", widths[i], null);
			}
			writer.endElement("col");
		}*/
		
		super.encodeHeader(context, table, numberOfColumns);
		
		table.setRowKey(context,key);
		table.restoreOrigValue(context);
		
		//F'-1.1.1.1.1
		writer.endElement(HTML.TABLE_ELEMENT);
	}
	
	public void encodeHiddenHeader(FacesContext context, UIDataTable table,
			int numberOfColumns) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("thead", table);
		writer.writeAttribute("id", ExtTreeTable.getHiddenHeaderId(context, table), null);
		writer.writeAttribute(HTML.class_ATTRIBUTE, "rich-table-thead", null);
		String headerClass = (String) table.getAttributes().get("headerClass");
	
		writer.startElement("tr", table);
		encodeStyleClass(writer, null,
				"rich-table-subheader", null,
				headerClass);
		
		encodeHeaderFacets(context, writer, table.columns(),
				"rich-table-subheadercell",
				headerClass, "header", "th", numberOfColumns);
		
		writer.endElement("tr");
	}
	
	@Override
	public void encodeTBody(FacesContext context, UIDataTable table)
			throws IOException {
		 String clientId = table.getClientId(context);
		 ResponseWriter writer = context.getResponseWriter();
		 //encodeFooter(context, table, columns);
		 
		 //G-1.1.1.1.1.2
		 writer.startElement(HTML.DIV_ELEM,table);
		 getUtils().writeAttribute(writer, "id", clientId+ExtTreeTable.TABLE_SCROLL_DIV_POSTFIX);
		 getUtils().writeAttribute(writer, "class", "scroll-container-div");
		 
		 getUtils().writeAttribute(writer, "style", "display:none; overflow-y: scroll; border:0px;margin:0px;padding:0px");
		 writer.startElement(HTML.TABLE_ELEMENT,table);
		 getUtils().writeAttribute(writer, "id", clientId+ExtTreeTable.TABLE_BODY_POSTFIX);
		 getUtils().writeAttribute(writer, "style", "width:100%");
		 getUtils().writeAttribute(writer, "class","scroll-body-table");
		 
		 getUtils().writeAttribute(writer, "cellspacing", 0);
		 
		 int columns = getColumnsCount(table);
		 String columnsWidth = (String) table.getAttributes().get("columnsWidth");
		 String[] widths = columnsWidth!=null? columnsWidth.split(","): new String[]{};
		 
		 // writer.startElement("colgroup", table);
		 //writer.writeAttribute("span", String.valueOf(columns), null);
		/* for (int i = 0; i < columns; i++) {
			writer.startElement("col", table);
			if(widths!=null && i <widths.length){
				writer.writeAttribute("width", widths[i], null);
			}
			writer.endElement("col");
		 }*/
		 
		//write fake header 
		 encodeHiddenHeader(context, table, columns);
		 
		//writer.endElement("colgroup");
		writer.startElement("tbody", table);
		writer.writeAttribute("id", clientId + ":tb", null);
		
		encodeRows(context, table);
		
		writer.endElement("tbody");
		
		writer.endElement(HTML.TABLE_ELEMENT);
		writer.endElement(HTML.DIV_ELEM);
		
		//super.encodeTBody(context, table);
	}
	
	@Override
	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIDataTable component, ComponentVariables variables)
			throws IOException {
		//E'-1.1.1.1.1
		writer.endElement(HTML.DIV_ELEM);
		//D'-1.1.1.1
		writer.endElement(HTML.td_ELEM);
		//C'-1.1.1
		writer.endElement(HTML.TR_ELEMENT);
		//B'-1.1
		writer.endElement(HTML.TBOBY_ELEMENT);
		//A'-1
		
		// Encode colgroup definition.
		int columns = getColumnsCount(component);
		super.encodeFooter(context, component, columns);
		
		component.setRowKey(context, null);
		writer.endElement(HTML.TABLE_ELEMENT);
	}
	
	protected String getTableVariableName(FacesContext context,UIComponent component){
		String tableId  = "treeTable_"+component.getId();
		return tableId;
	}
	
	@Override
	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		super.doEncodeEnd(writer, context, component);
		
		String clientId = component.getClientId(context);
		String tableId  = getTableVariableName(context,component);
		
		writer.startElement("script", component);
		//getUtils().writeAttribute(writer,"id",clientId+"_script");
		String selectionMode = ((HtmlTreeTable)component).getSelectionMode();
				
		getUtils().writeAttribute(writer, "type", "text/javascript");
		writer.writeText(
					convertToString(" var "+tableId +" =new RichfacesExt.TreeTable('"
								+ convertToString(clientId)
								+ "', '"
								+ selectionMode
								+"',"
								+ convertToString(getSubmitFunction(context,
										component)) + ");"), null);
		//init tree table to align culumns in both header and body tables
		//writer.writeText("}",null);
		
		writer.writeText(tableId+".init();",null);
		
		writer.endElement("script");
		//outer div
		writer.endElement(HTML.DIV_ELEM);
	}
	
	public void encodeOneRow(FacesContext context, TableHolder holder)
			throws IOException {
		UIDataTable table = (UIDataTable) holder.getTable();
		Object rowKey = table.getRowKey();
		ResponseWriter writer = context.getResponseWriter();
		Iterator<UIComponent> iter = table.columns();
		boolean firstColumn = true;
		boolean firstRow = (holder.getRowCounter() == 0);
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		if(firstRow){
			requestMap.put(ExtTreeTable.FIRST_ROW, Boolean.TRUE);
		}
		else{
			requestMap.put(ExtTreeTable.FIRST_ROW, Boolean.FALSE);
		}
		
		int currentColumn = 0;
		UIComponent column = null;
		
		while (iter.hasNext()) {
			column = (UIComponent) iter.next();
			// Start new row for first column - expect a case of the detail
			// table, wich will be insert own row.
			if (firstColumn && !(column instanceof Row)) {
				String rowSkinClass = getRowSkinClass((HtmlTreeTable) table);
				if (firstRow) {
					String firstRowSkinClass = getFirstRowSkinClass();
					if (firstRowSkinClass != null
							&& firstRowSkinClass.length() != 0) {
						if (rowSkinClass != null && rowSkinClass.length() != 0) {
							rowSkinClass += " " + firstRowSkinClass;
						} else {
							rowSkinClass = firstRowSkinClass;
						}
					}
				}
				
			encodeRowStart(context, rowSkinClass, holder.getRowClass(),
						table, rowKey,writer);
			}
			if (column instanceof Column) {
				boolean breakBefore = ((Column) column).isBreakBefore()
						|| column instanceof Row;
				if (breakBefore && !firstColumn) {
					// close current row
					writer.endElement(HTML.TR_ELEMENT);
					// reset columns counter.
					currentColumn = 0;
					// Start new row, expect a case of the detail table, wich
					// will be insert own row.
					if (!(column instanceof Row)) {
						holder.nextRow();
						encodeRowStart(context, holder.getRowClass(), table,
								writer);
					}
				}
				encodeCellChildren(context, column,
						firstRow ? getFirstRowSkinClass() : null,
						getRowSkinClass(), holder.getRowClass(),
						getCellSkinClass(),
						holder.getColumnClass(currentColumn));
				
				if ((column instanceof Row) && iter.hasNext()) {
					// Start new row for remained columns.
					holder.nextRow();
					encodeRowStart(context, holder.getRowClass(), table, writer);
					// reset columns counter.
					currentColumn = -1;
				}
			} else if (column.isRendered()) {
				// UIColumn don't have own renderer
				writer.startElement(HTML.td_ELEM, table);
				getUtils().encodeId(context, column);
				String columnClass = holder.getColumnClass(currentColumn);
				encodeStyleClass(writer, null, getCellSkinClass(), null,
						columnClass);

				// TODO - encode column attributes.
				renderChildren(context, column);
				writer.endElement(HTML.td_ELEM);
			}
			currentColumn++;
			firstColumn = false;
		}
		// Close row if then is open.
		if (!firstColumn && !(column instanceof Row)) {
			writer.endElement(HTML.TR_ELEMENT);
		}
		
		requestMap.put(getRowsIndicatorKey(holder.getTable()),true);	
	}

	protected void encodeFirstColumn(FacesContext context, UIComponent cell,
			String skinFirstRowClass, String skinRowClass, String rowClass,
			String skinCellClass, String cellClass){
		
	}
	
	protected void encodeRowStart(FacesContext context, String skinClass,
			String rowClass, UIDataTable table,Object rowKey, ResponseWriter writer)
			throws IOException {
		
		String rowId = ExtTreeTable.rowId(context,table);
		writer.startElement(HTML.TR_ELEMENT, table);
		
		
		writer.writeAttribute(HTML.id_ATTRIBUTE, rowId, null);
		writer.writeAttribute("rowkey", table.getRowKey(), null);
		
		TreeContentProvider<T> provider = getTreeContentProvider((HtmlTreeTable) table);
		if(!provider.isSelectable(provider.getCurrent())){
			rowClass+=" table-row-nonselection";
		}
		
		encodeStyleClass(writer, null, skinClass, null, rowClass);
		encodeRowEvents(context, table);
	}
	
	protected String getRowSkinClass(HtmlTreeTable table) {
		getTreeContentProvider(table);
		String styleClass = super.getRowSkinClass();
		TreeRowKey<?> rowKey =(TreeRowKey<?>) table.getRowKey();
		hidden = rowShouldHidden(rowKey);
		if(hidden){
			styleClass +=" treetable-rowhidden";
		}
		oldRowKey = rowKey;
		
		String customClass = this.customRowClass(table);
		if(customClass!=null)
			styleClass+=" "+customClass;
		
		return styleClass;
	}
	
	
	protected TreeContentProvider<T> getTreeContentProvider(HtmlTreeTable<T> table){
		treeContentProvider = table.getTreeContentProvider();
		return treeContentProvider;
	}
	
	protected  boolean rowShouldHidden(TreeRowKey<?> rowKey){
		if(oldRowKey!=null && hidden!=null){
			if(oldRowKey.depth() == rowKey.depth()){
				return hidden;
			}
		}
		TreeRowKey<?> parentRowKey = rowKey.getParentKey();
		if(parentRowKey !=null && parentRowKey.depth() > 0){
			ExtTreeTable.Status status=treeContentProvider.getNode(parentRowKey).getStatus();
			if((status.intValue() & ExtTreeTable.I_COLLPASED ) !=0){
				return true;
			}else{
				boolean hidden=rowShouldHidden(parentRowKey);
				if(hidden){
					return true;
				}else
					return false;
			}
		}
		return false;
	}
	
	protected void encodeRowStart(FacesContext context, String rowClass,
			UIDataTable table, ResponseWriter writer) throws IOException {
		
		encodeRowStart(context, getRowSkinClass((HtmlTreeTable<?>) table), rowClass, table, writer);
	}
	
	protected String customRowClass(UIDataTable table){
		TreeContentProvider<T> provider =this.getTreeContentProvider((HtmlTreeTable<T>) table);
		return provider.getRowStyleClass(provider.getCurrent());
	}
	
	protected void encodeCellChildren(FacesContext context, UIComponent cell,
			String skinFirstRowClass, String skinRowClass, String rowClass,
			String skinCellClass, String cellClass) throws IOException {
		
	    Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		// Save top level class parameters ( if any ), and put new for this
		// component
		Object savedRowClass = requestMap.get(ROW_CLASS_KEY);
		if (null != rowClass) {
			requestMap.put(ROW_CLASS_KEY, rowClass);

		}
		Object savedSkinFirstRowClass = requestMap.get(SKIN_FIRST_ROW_CLASS_KEY);
		if (null != skinRowClass) {
			requestMap.put(SKIN_FIRST_ROW_CLASS_KEY, skinFirstRowClass);

		}
		Object savedSkinRowClass = requestMap.get(SKIN_ROW_CLASS_KEY);
		if (null != skinRowClass) {
			requestMap.put(SKIN_ROW_CLASS_KEY, skinRowClass);

		}
		Object savedCellClass = requestMap.get(CELL_CLASS_KEY);
		if (null != cellClass) {
			requestMap.put(CELL_CLASS_KEY, cellClass);
		}
		Object savedSkinCellClass = requestMap.get(SKIN_CELL_CLASS_KEY);
		if (null != skinCellClass) {
			requestMap.put(SKIN_CELL_CLASS_KEY, skinCellClass);
		}
		
		renderChild(context, cell);
		
		// Restore original values.
		requestMap.put(ROW_CLASS_KEY, savedRowClass);
		requestMap.put(CELL_CLASS_KEY, savedCellClass);
		requestMap.put(SKIN_FIRST_ROW_CLASS_KEY, savedSkinFirstRowClass);
		requestMap.put(SKIN_ROW_CLASS_KEY, savedSkinRowClass);
		requestMap.put(SKIN_CELL_CLASS_KEY, savedSkinCellClass);
	}
	
	private String convertToString(Object obj) {
		return (obj == null ? "" : obj.toString());
	}
	
	
	public String getSubmitFunction(FacesContext context, UIComponent component) {
		String rowId = component.getClientId(context);
		//ajax sumbit function
		JSFunctionDefinition definition = new JSFunctionDefinition("event");
		
		JSFunction function = AjaxRendererUtils.buildAjaxFunction(component,
				context);
		Map<String,Object> eventOptions = AjaxRendererUtils.buildEventOptions(context,
				component, true);
		
		eventOptions.put("oncomplete", getOncomplete(eventOptions,context,component));
		
		@SuppressWarnings("unchecked")
		Map<String,Object> parameters = (Map<String,Object>) eventOptions.get("parameters");
		
		Map<String,Object> params = getParameters(context,component);
		 if(!params.isEmpty()){
			 parameters.putAll(params);
		}
		parameters.put(rowId, new JSLiteral(ExtTreeTable.EVT_PARAM_EXPAND_ROW));
		
		function.addParameter(eventOptions);
		StringBuffer buffer = new StringBuffer();
		buffer.append(getNodeStatusUpdateScript(context,rowId));
		
		function.appendScript(buffer);
		buffer.append("; return false;");
		
		definition.addToBody(buffer.toString());
		return definition.toScript();
	}
	
	protected String getDefaultOncomplete(FacesContext context, UIComponent component){
		String tablevariable=getTableVariableName(context,component);
		return tablevariable+".afterNodeExpand();";
	}
	
	protected JSFunctionDefinition getOncomplete(Map<String,Object> eventOptions,
			FacesContext context, UIComponent component){
		Object oncomplete=eventOptions.get("oncomplete");
		JSFunctionDefinition fun = null;
		if(oncomplete instanceof JSFunctionDefinition){
			fun = (JSFunctionDefinition) oncomplete;
			fun.addToBody(getDefaultOncomplete(context,component));
			return fun;
		}else if(oncomplete==null ){
			fun = new JSFunctionDefinition("request","event","data");
			fun.addToBody(getDefaultOncomplete(context,component));
			return fun;
		}else {
			//TODO
			fun = new JSFunctionDefinition("request","event","data");
			return fun;
		}
	}
	
	public String getNodeStatusUpdateScript(FacesContext context, String rowId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("var f= $("+ExtTreeTable.EVT_PARAM_STATUS_FIELD+"); if(f) f.value="+ExtTreeTable.EVT_PARAM_INTENDED_STATUS+";");
		return buffer.toString();
	}
	
	@Override
	protected void doDecode(FacesContext context, UIComponent component) {
		super.doDecode(context, component);
    	Map<String,String> paramMap = getParamMap(context);
    	
    	String clientId = component.getClientId(context);
    	String param = (String) paramMap.get(clientId);
    	TreeContentProvider<T> provider = getTreeContentProvider((HtmlTreeTable) component);
    	Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
    	//TODO
    	if(((HtmlTreeTable) component).selectionMode()== SelectionMode.SINGLE){ 
	    	String fieldId=ExtTreeTable.getSelectedFieldId(context, (UIDataTable) component);
	    	String selectedRowKey =context.getExternalContext().getRequestParameterMap().get(fieldId);
	    	if(selectedRowKey!=null && selectedRowKey.trim().length()!=0){
	    		SimpleSelection selection = new SimpleSelection();
	    		selection.addKey(selectedRowKey);
	    		provider.setSelection(selection);
	    	}else{
	    		provider.setSelection(null);
	    	}
    	}
    	else if(((HtmlTreeTable) component).selectionMode()== SelectionMode.MULTIPLE){
    		String fieldId=ExtTreeTable.getSelectedFieldId(context, (UIDataTable) component);
    		SimpleSelection selection = new SimpleSelection();
    		
    		String keys[] = context.getExternalContext().getRequestParameterValuesMap().get(fieldId);
    		if(keys!=null){
    			for(String selectedRowKey:keys){
    				selection.addKey(selectedRowKey);
    			}
    			provider.setSelection(selection);
    		}else{
    			provider.setSelection(null);
    		}
    	}
    	
    	if(provider.getSelection()!=null){
    		List<T> selected=new ArrayList<T>(); 
    		for(Iterator<Object> it = provider.getSelection().getKeys(); it.hasNext();){
    			T obj=provider.locate(it.next());
    			if(obj!=null)
    				selected.add(obj);
    		}
			
    		//requestMap.put(ExtTreeTable.TABLE_LAST_SELECTED, selected);
    	}
    	
    	if (param != null) {
			AjaxContext ajaxContext = AjaxContext.getCurrentInstance();
			ajaxContext.addRegionsFromComponent(component);
			ajaxContext.addComponentToAjaxRender(component);
			ajaxContext.addRegionsFromComponent(component);
			
			if(requestMap.get(ExtTreeTable.NODE_ON_EXPAND)!=null){
				ajaxContext.addRenderedArea(clientId + ":tb");
			}
			else{
				ajaxContext.addRenderedArea(clientId);
			}
			
			//ajaxContext.addRenderedArea(clientId+"_script");
			// FIXME: check for correct client id.
			// Now path & client id mixed here, it is possible that
			// they will be different un case of dataTable in dataTable. 
			
			// Due to we are re render whole data table, Ajax runtime didn't add to reRender   
			// ids of those childs that specified in reRender data table attribute
			// so let's add them to ajax render areas here by hand
			Set<String> ajaxRenderedAreas = ajaxContext.getAjaxRenderedAreas();
			Set<String> areasToRender = ajaxContext.getAjaxAreasToRender();
			for (String area : areasToRender) {
			    // process only child components, all other should be added to render
			    // automatically by ajax
			    if (area.startsWith(NamingContainer.SEPARATOR_CHAR + clientId)) {
				area = area.substring(1); // remove unnecessary start separator symbol
				if (!area.equals(clientId) && !ajaxRenderedAreas.contains(area)) {
				    ajaxContext.addRenderedArea(area);
				}
			    }
			}
    	}
    }
	
	 //get UIParameter's Map
    protected Map<String,Object> getParameters(FacesContext context, UIComponent component){
    	Map<String,Object> parameters = new HashMap<String,Object>(); 
    	if(component instanceof HtmlTreeTable){
    		for (UIComponent child: component.getChildren()) {
				if(child instanceof UIParameter) {
					UIParameter param = (UIParameter)child;
					String name = param.getName();
					if (name != null) {
						parameters.put(name, param.getValue());
					}
				}
			}
    	}
    	return parameters;
    }
    
	private Map<String,String> getParamMap(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap();
    }
	
	protected String getWidthFieldId(FacesContext context,UIDataTable component){
		String clientId = component.getClientId(context);
		return clientId+"_widths";
	}
	
	protected void writeTableWidthField(FacesContext context,UIDataTable component) 
						throws IOException{
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement(HTML.INPUT_ELEM, component);
		writer.writeAttribute("type", "hidden", null);
		String inputId=ExtTreeTable.getWidthFieldId(context,component);
		
		writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		writer.endElement(HTML.INPUT_ELEM);
	}
	
}