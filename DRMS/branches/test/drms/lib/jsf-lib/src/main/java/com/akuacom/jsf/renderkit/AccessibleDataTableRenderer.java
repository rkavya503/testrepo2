package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.UIColumn;
import org.richfaces.component.UIDataTable;
import org.richfaces.component.util.FormUtil;
import org.richfaces.component.util.ViewUtil;
import org.richfaces.context.RequestContext;
import org.richfaces.model.Ordering;
import org.richfaces.renderkit.HeaderEncodeStrategy;
import org.richfaces.renderkit.TableHolder;
import org.richfaces.renderkit.html.DataTableRenderer;
import org.richfaces.renderkit.html.iconimages.DataTableIconSortAsc;
import org.richfaces.renderkit.html.iconimages.DataTableIconSortDesc;
import org.richfaces.renderkit.html.iconimages.DataTableIconSortNone;

import com.akuacom.jsf.component.ExtTreeTable;
import com.akuacom.jsf.component.ExtendedHtmlDataTable;
import com.akuacom.jsf.component.ExtendedHtmlDataTable.SelectionMode;
import com.akuacom.jsf.model.TableContentProvider;
/**
 * Renderer for component class org.richfaces.renderkit.html.DataTableRenderer
 */
public class AccessibleDataTableRenderer extends DataTableRenderer {

	protected static final String AT_LEAST_ONE_ROW ="_at_leat_one_row";
	
	public AccessibleDataTableRenderer() {
		super();
	}
	
	@Override
	protected void doDecode(FacesContext context, UIComponent component) {
		super.doDecode(context, component);
		String selectAll= ExtTreeTable.getSelectAllFieldId(
				context,(UIDataTable) component);
				
		String keys[] = context.getExternalContext().getRequestParameterValuesMap().get(selectAll);
		if(keys!=null){
			for(String key:keys){
				if(key.equals("true")){
					Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
					requestMap.put(selectAll, "true");
					break;
				}
			}
		}
	}
	
	@Override
	public void encodeOneRow(FacesContext context, TableHolder holder)
			throws IOException {
		super.encodeOneRow(context, holder);
		
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		
		requestMap.put(getRowsIndicatorKey(holder.getTable()),true);
	}
	
	protected String getRowsIndicatorKey(UIComponent component){
		return component.getId()+"_"+AT_LEAST_ONE_ROW;
	}
	

	@Override
	public void encodeRows(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeRows(context, component);
		ResponseWriter writer = context.getResponseWriter();
		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
		Boolean notEmpty= (Boolean) requestMap.get(getRowsIndicatorKey(component));
		if(notEmpty==null || !notEmpty.booleanValue()){
			//write not data 
			UIDataTable table = (UIDataTable) component;
			int columnCount = table.getChildCount();
			writer.startElement(HTML.TR_ELEMENT,component);
			writer.writeAttribute("class", "empty-row rich-table-row rich-table-firstrow",null);
			
			writer.startElement(HTML.td_ELEM,component);
			writer.writeAttribute("class", "rich-table-cell",null);
			writer.writeAttribute("colspan",columnCount,null);
			writer.writeAttribute("align","center",null);
			
			writer.startElement(HTML.SPAN_ELEM,component);
			writer.write("No data");
			writer.endElement(HTML.SPAN_ELEM);
			
			writer.endElement(HTML.td_ELEM);
			writer.endElement(HTML.TR_ELEMENT);
		}
	}
	
	public void doEncodeBegin(ResponseWriter writer, FacesContext context, org.richfaces.component.UIDataTable component, ComponentVariables variables ) throws IOException {
	    java.lang.String clientId = component.getClientId(context);
	    writer.startElement("table", component);
			getUtils().writeAttribute(writer, "class", "rich-table " + convertToString(component.getAttributes().get("styleClass")) );
						getUtils().writeAttribute(writer, "id", clientId );
//						getUtils().writeAttribute(writer, "summary", "data-table" );
						getUtils().writeAttribute(writer, "summary", component.getAttributes().get("summary") );
						getUtils().writeAttribute(writer, "style", component.getAttributes().get("style") );
			
						getUtils().encodePassThruWithExclusions(context, component, "value,name,type,id,class,rows,style");

						encodeTableStructure(context, component);
	}
	private String convertToString(Object obj ) {
		return ( obj == null ? "" : obj.toString() );
	}
	protected void encodeHeaderFacets(FacesContext context,
			ResponseWriter writer, Iterator<UIComponent> headers,
			String skinCellClass, String headerClass, String facetName,
			String element, int colCount) throws IOException {
		int t_colCount = 0;

		HeaderEncodeStrategy richEncodeStrategy = new RichHeaderEncodeStrategy();
		HeaderEncodeStrategy simpleEncodeStrategy = new SimpleHeaderEncodeStrategy();

		while (headers.hasNext()) {
			UIComponent column = (UIComponent) headers.next();
			if (!isColumnRendered(column)) {
				continue;
			}

			Integer colspan = (Integer) column.getAttributes().get("colspan");
			if (colspan != null && colspan.intValue() > 0) {
				t_colCount += colspan.intValue();
			} else {
				t_colCount++;
			}

			if (t_colCount > colCount) {
				break;
			}

			String classAttribute = facetName + "Class";
			String columnHeaderClass = (String) column.getAttributes().get(
					classAttribute);

			writer.startElement(element, column);
			encodeStyleClass(writer, null, skinCellClass, headerClass,
					columnHeaderClass);
			writer.writeAttribute("scope", "col", null);
			getUtils().encodeAttribute(context, column, "colspan");

			boolean sortableColumn = column.getValueExpression("comparator") != null
					|| column.getValueExpression("sortBy") != null;

			HeaderEncodeStrategy strategy = (column instanceof org.richfaces.component.UIColumn && "header"
					.equals(facetName)) ? richEncodeStrategy
					: simpleEncodeStrategy;

			strategy.encodeBegin(context, writer, column, facetName,
					sortableColumn);

			UIComponent facet = column.getFacet(facetName);
			
			StringBuffer title = new StringBuffer();
			String columnName="";
			if (facet != null && isColumnRendered(facet)) {
				Object str=facet.getAttributes().get("value");
				if(str!=null) columnName=str.toString();
				renderChild(context, facet, writer, title);
			}
			
			if (strategy instanceof RichHeaderEncodeStrategy) {
				((RichHeaderEncodeStrategy) strategy).columnName = columnName;
				strategy.encodeEnd(context, writer, column, facetName,sortableColumn);
			} else {
				strategy.encodeEnd(context, writer, column, facetName,sortableColumn);
			}
			writer.endElement(element);
		}
	}

	/**
	 * Render one component and it childrens
	 * 
	 * @param facesContext
	 * @param child
	 * @throws IOException
	 */
	public void renderChild(FacesContext facesContext, UIComponent child,
			ResponseWriter writer, StringBuffer title) throws IOException {
		if (!child.isRendered()) {
			return;
		}

		child.encodeBegin(facesContext);
		if (child.getRendersChildren()) {
			child.encodeChildren(facesContext);
		} else {
			renderChildren(facesContext, child);
		}
		writer.writeAttribute(HTML.title_ATTRIBUTE, title, null);
		child.encodeEnd(facesContext);
	}

	private static final String SORT_DIV = ":sortDiv";

	public boolean supportSelectionAll(UIComponent column){
		UIDataTable table = (UIDataTable) column.getParent();
		
		//only enabled for multiple-selectable table
		if( (table instanceof ExtendedHtmlDataTable) &&
				((ExtendedHtmlDataTable)table).selectionMode().equals(SelectionMode.MULTIPLE)){
			Iterator<UIComponent> tableColumns =table.columns();
			UIComponent firstColumn  = null;
			while (tableColumns.hasNext()) {
				UIComponent component = tableColumns.next();
				if((component instanceof UIColumn) && component.isRendered() ){
					firstColumn = component;
					break;
				}
			}
			return firstColumn==column;
			}
		return false;
	}
	
	protected boolean writeRightBorder(FacesContext context,UIComponent column){
		return true;
	}
	
	protected boolean isWidthFixed(FacesContext context,UIDataTable component){
		return ExtTreeTable.isWidthFixed(context, component);
	}
	
	protected class RichHeaderEncodeStrategy implements HeaderEncodeStrategy {
		
		public String columnName="";
		
		public void encodeBegin(FacesContext context, ResponseWriter writer,
				UIComponent column, String facetName, boolean sortableColumn)
				throws IOException {
			org.richfaces.component.UIColumn col = (org.richfaces.component.UIColumn) column;
			String columnClientId = col.getClientId(context);
			String clientId = columnClientId + facetName;
			writer.writeAttribute("id", clientId, null);

			if (sortableColumn && col.isSelfSorted()) {
				FormUtil.throwEnclFormReqExceptionIfNeed(context,
						column.getParent());
				writer.writeAttribute(HTML.onclick_ATTRIBUTE,
						buildAjaxFunction(context, column, true).toString(),
						null);
				String style ="cursor: pointer;";
				if(!writeRightBorder(context,column)){
					style+="border-right:none";
				}
				writer.writeAttribute(HTML.style_ATTRIBUTE, style,
						null);
			}else{
				//FIXME:frank changed
				//Ajax submission requires scripts
				setRequiresScripts(context);
			}

			writer.startElement(HTML.DIV_ELEM, column);
			writer.writeAttribute(HTML.id_ATTRIBUTE, clientId + SORT_DIV, null);

			RequestContext requestContext = RequestContext.getInstance(context);
			if (Boolean.TRUE.equals(requestContext.getAttribute(columnClientId
					+ SORT_DIV))) {
				AjaxContext.getCurrentInstance().addRenderedArea(
						clientId + SORT_DIV);
			}
			boolean writeCheckbox = supportSelectionAll(column);
			if(writeCheckbox){
				String tableId ="treeTable_"+column.getParent().getId();
				String checkboxId=clientId+"_all_check";
				writer.startElement("table",column);
				writer.writeAttribute(HTML.style_ATTRIBUTE,"text-align:left;width:100%",null);
				writer.startElement("tr", column);
				writer.startElement("td", column);
				writer.writeAttribute(HTML.style_ATTRIBUTE,"text-align:left",null);
				writer.startElement(HTML.INPUT_ELEM, null);
				writer.writeAttribute("type", "checkbox", "");
				writer.writeAttribute("class", "check-select-all", "");
				writer.writeAttribute(HTML.id_ATTRIBUTE,checkboxId,null);
				
				//click handler
				String clickhandler=tableId+".selectOrDeselectAll('"+checkboxId+"',event);";
				Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
				String selectAll= ExtTreeTable.getSelectAllFieldId(
						context,(UIDataTable) column.getParent());
				
				writer.writeAttribute("name",selectAll,	null);
				
				if(column.getParent() instanceof ExtendedHtmlDataTable){
					ExtendedHtmlDataTable t = (ExtendedHtmlDataTable) column.getParent();
					TableContentProvider<?> p=t.getTableContentProvider();
					if(t.getOnSelection()!=null)
						clickhandler +=t.getOnSelection();
					
					writer.writeAttribute("value","true",null);
					if(requestMap.get(selectAll)!=null && p.getSelection()!=null){
						writer.writeAttribute("checked",true,null);
					}
				}
				
				writer.writeAttribute(HTML.onclick_ATTRIBUTE,clickhandler, null);
				//writer.writeAttribute("name",column.getParent().getClientId(context)+"_sel_all","");
				
				writer.endElement(HTML.INPUT_ELEM);
				writer.endElement("td");
				writer.startElement("td", column);
				writer.writeAttribute(HTML.style_ATTRIBUTE,"text-align:center",null);
			}
			if (sortableColumn) {
				writer.startElement(HTML.SPAN_ELEM, column);
				writer.writeAttribute(HTML.class_ATTRIBUTE,
						"rich-table-sortable-header", null);
			}
			// writer.write("<a href='javascript:{}' onclick='if(this.parentElement.onclick) this.parentElement.onclick();'>");
		}

		public void encodeEnd(FacesContext context, ResponseWriter writer,
				UIComponent column, String facetName, boolean sortableColumn)
				throws IOException {
			org.richfaces.component.UIColumn col = (org.richfaces.component.UIColumn) column;

			String sortingOrderString = "";
			String alt="";
			if (sortableColumn) {
				String imageUrl = null;
				if (Ordering.ASCENDING.equals(col.getSortOrder())) {
					if (null != col.getSortIconAscending()) {
						imageUrl = ViewUtil.getResourceURL(
								col.getSortIconAscending(), context);
					} else {
						imageUrl = getResource(
								DataTableIconSortAsc.class.getName()).getUri(
								context, null);
					}
					sortingOrderString = Ordering.ASCENDING.toString();
					alt = "ascending order by "+columnName;
				} else if (Ordering.DESCENDING.equals(col.getSortOrder())) {
					if (null != col.getSortIconDescending()) {
						imageUrl = ViewUtil.getResourceURL(
								col.getSortIconDescending(), context);
					} else {
						imageUrl = getResource(
								DataTableIconSortDesc.class.getName()).getUri(
								context, null);
					}
					sortingOrderString = Ordering.DESCENDING.toString();
					alt = "descending order by "+columnName;
				} else if (col.isSelfSorted()) {
					if (null != col.getSortIcon()) {
						imageUrl = ViewUtil.getResourceURL(col.getSortIcon(),
								context);
					} else {
						imageUrl = getResource(
								DataTableIconSortNone.class.getName()).getUri(
								context, null);
					}
					sortingOrderString = Ordering.UNSORTED.toString();
					
					alt = "sort by "+columnName;

				}
				alt=alt.replace("<br/>", " ");

				if (imageUrl != null) {
					writer.write("<a href='javascript:{}' onclick='if(this.parentElement.onclick) this.parentElement.onclick();'>");
					writer.startElement(HTML.IMG_ELEMENT, column);

					writer.writeAttribute(HTML.src_ATTRIBUTE, imageUrl, null);
					// writer.writeAttribute(HTML.alt_ATTRIBUTE, "", null);
					writer.writeAttribute(HTML.alt_ATTRIBUTE, alt,
							HTML.alt_ATTRIBUTE);
					writer.writeAttribute(HTML.title_ATTRIBUTE, alt,
							HTML.title_ATTRIBUTE);
					writer.writeAttribute(HTML.width_ATTRIBUTE, "15", null);
					writer.writeAttribute(HTML.height_ATTRIBUTE, "15", null);
					writer.writeAttribute(HTML.class_ATTRIBUTE,
							"rich-sort-icon", null);
					writer.writeAttribute(HTML.border_ATTRIBUTE, "0", null);
					writer.endElement(HTML.IMG_ELEMENT);
					writer.write("</a>");
				}
				writer.endElement(HTML.SPAN_ELEM);
			}

			writer.endElement(HTML.DIV_ELEM);
			if (col.getFilterMethod() == null
					&& col.getValueExpression("filterExpression") == null
					&& col.getValueExpression("filterBy") != null) {

				writer.startElement(HTML.DIV_ELEM, column);
				addInplaceInput(context, column,
						buildAjaxFunction(context, column, false));
				writer.endElement(HTML.DIV_ELEM);
			}
			
			boolean writeCheckbox = supportSelectionAll(column);
			if(writeCheckbox){
				writer.endElement("td");
				writer.endElement("tr");
				writer.endElement("table");
			}
		}
	}

	protected class SimpleHeaderEncodeStrategy implements HeaderEncodeStrategy {

		public void encodeBegin(FacesContext context, ResponseWriter writer,
				UIComponent column, String facetName, boolean sortableColumn)
				throws IOException {

		}

		public void encodeEnd(FacesContext context, ResponseWriter writer,
				UIComponent column, String facetName, boolean sortableColumn)
				throws IOException {

		}

	}
}
