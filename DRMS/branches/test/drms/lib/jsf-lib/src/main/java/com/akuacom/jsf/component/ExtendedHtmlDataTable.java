package com.akuacom.jsf.component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.richfaces.component.UIDatascroller;
import org.richfaces.component.html.HtmlDataTable;
import org.richfaces.model.Ordering;
import org.richfaces.model.SortField2;

import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.PagedSequenceDataModel;
import com.akuacom.jsf.model.SortColumn;
import com.akuacom.jsf.model.TableContentProvider;
import com.akuacom.jsf.model.Utils;


/**
 * Minor change to this component to use the custom data model  
 * see {@link com.akuacom.jsf.model.ExtendedModifiableModel}
 *
 */
public class ExtendedHtmlDataTable extends HtmlDataTable {
	
	final public static  String COMPONENT_FAMILY = "com.akuacom.jsf.DataTable";
	
	public static enum SelectionMode {
		SINGLE,
		MULTIPLE,
		NONE 
	}
	
	public ExtendedHtmlDataTable() {
		setRendererType("com.akuacom.jsf.DataTableRenderer");
	}
	
	private boolean sortFilterChanged =false;
	
	private int rowCount = -1;
	
	private boolean modelUpdated =false;
	
	protected boolean beforeRenderPhase = false;
	
	private String height;
	
	private String selectionMode ="none";
	
	private String status;
	
	private String onselection;
	
	private java.lang.String summary;
	
	protected AbstractTableContentProvider<?> tblContentProvider; 
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	protected DataModel getDataModel() {
		TableContentProvider<?> provider = this.getTableContentProvider();
		if(provider==null)
			return super.getDataModel();
		else{
			/*if(beforeRenderPhase){
				 provider.updateModel();
				 beforeRenderPhase = false;
			}*/
			return  new PagedSequenceDataModel(new ListDataModel((List<?>) provider.getContents()));
		}
	}
	
	protected ExtendedDataModel createDataModel() {
		return (ExtendedDataModel) getDataModel();
	}
	
	protected SortColumn getSortColumn(){
		List<UIComponent> list = getChildren();
		for (Iterator<UIComponent> iterator = list.iterator(); iterator.hasNext();) {
			UIComponent component = iterator.next();
			if (component instanceof org.richfaces.component.UIColumn) {
				org.richfaces.component.UIColumn column = (org.richfaces.component.UIColumn) component;
				if(column.isRendered()){
					SortField2 sortField = column.getSortField();
					if (sortField != null) {
						String columnName=Utils.parseSortColumn(sortField.getExpression().getExpressionString());
						return new SortColumn(columnName,sortField.getOrdering()==Ordering.ASCENDING);
					}
				}
			}
		}
		return null;
	}
	
	protected boolean isSortingReset(){
		 FacesContext context=getFacesContext();
	     Map<String,String> parameters= context.getExternalContext().getRequestParameterMap();
	     String value=parameters.get(getClientId(context)+"_sortReset");
	     if(value!=null) return Boolean.parseBoolean(value);
	     return false;
	}
	
	protected void updateStatus(FacesContext context){
		TableContentProvider<?> provider = this.getTableContentProvider();
		if(provider!=null){
			SortColumn sortColumn =getSortColumn();
			provider.setSortColumn(sortColumn);
			SequenceRange range  = (SequenceRange) getComponentState().getRange();
			provider.setRange(new com.akuacom.jsf.model.SequenceRange(range.getFirstRow(),range.getRows()));
			
			//allow programmer to set sorting or pagination manually
			provider.resetSortingOrpagination();
			
			//Check if sort column is reset programmatically 
			SortColumn currentSortColumn = provider.getSortColumn();
			if(!Utils.objectEquals(sortColumn, currentSortColumn)){
				resetUISortColumn(currentSortColumn);
			}
			
			//check if range has been reset programmatically
			com.akuacom.jsf.model.SequenceRange newRange = (com.akuacom.jsf.model.SequenceRange) provider.getRange();
			sortFilterChanged= isSortingReset();
			
			if(sortFilterChanged|| !rangeEquals(range,newRange)){
				if((newRange.getFirstRow() == 0 && range.getFirstRow()!=0) || sortFilterChanged){
						getAttributes().put(UIDatascroller.SCROLLER_STATE_ATTRIBUTE, 1);
						UIDatascroller scroller= (UIDatascroller) getDataScroller();
						if(scroller!=null){
							//when user click one column, scroller should go to first page
							//so make sure scroller will be re-rendered
							AjaxRendererUtils.addRegionByName(context, scroller, scroller.getId());
					}
				}
			}
			
			//update model on demand
			AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
			if(!ajaxContext.isAjaxRequest()){
				if(shouldRender(context,this)){
					provider.updateModel();
				}
			//for ajax request,check it should be re-rendered or not
			}else if(shouldRerender(context,this) && shouldRender(context,this)){
				provider.updateModel();
			}
		}
	}
	
	//for non ajax request 
	private boolean shouldRender(FacesContext context, UIComponent component){
		return Utils.shouldRender(context, component);
	}
	
	//for ajax request
	private boolean shouldRerender(FacesContext context,UIComponent component){
		return Utils.shouldRerender(context, component);
	}
	
	private static boolean rangeEquals(SequenceRange seq1, com.akuacom.jsf.model.SequenceRange seq2){
		return seq1.getFirstRow()== seq2.getFirstRow()
		       && seq1.getRows() == seq2.getRows();
	}
	
	protected void resetUISortColumn(SortColumn sortColumn){
		List<UIComponent> list = getChildren();
		for (Iterator<UIComponent> iterator = list.iterator(); iterator.hasNext();) {
			UIComponent component = iterator.next();
			if (component instanceof org.richfaces.component.UIColumn) {
				org.richfaces.component.UIColumn column = (org.richfaces.component.UIColumn) component;
				if(column.isRendered()){
					ValueExpression sortBy=column.getValueExpression("sortBy");
					if(sortBy==null)
						continue;
					String sortByName = Utils.parseSortColumn(sortBy.getExpressionString());
					
					if(sortColumn!=null && sortColumn.getName()!=null && sortColumn.getName().equals(sortByName)){
						column.setSortOrder(sortColumn.isAscendent()? Ordering.ASCENDING: Ordering.DESCENDING);
					}else{
						column.setSortOrder(Ordering.UNSORTED);
					}
				}
			}
		}
	}
	
	@Override
	//Get RowCount is called before render of datascroller 
	//and data scroller is rendenered before data rows
	//So it's a good chance to update underlying model
	public int getRowCount() {
		UIDatascroller scroller= (UIDatascroller) getDataScroller();
		if(scroller!=null){
				FacesContext context= FacesContext.getCurrentInstance();
				//when user click one column, scroller should go to first page
				//so make sure scroller will be re-rendered
				AjaxRendererUtils.addRegionByName(context, scroller, scroller.getId());
		}
		if(modelUpdated){
			TableContentProvider<?> provider = this.getTableContentProvider();
			if(provider!=null)
				rowCount =provider.getTotalRowCount();
		}
		return rowCount;
	}
	
	public TableContentProvider<?> getTableContentProvider(){
		if (tblContentProvider==null){
			Object current = getValue();
			if(current instanceof AbstractTableContentProvider){
				tblContentProvider= (AbstractTableContentProvider<?>) current;
			}
		}
		return tblContentProvider;
	}
	
	public UIComponent getDataScroller(){
		UIComponent com = this.getFacet("footer");
		return com;
	}
	
	public boolean isSortFileChanged() {
		return sortFilterChanged;
	}
	
	public void beforeRenderResponse(FacesContext context) {
		super.beforeRenderResponse(context);
		if(!modelUpdated){
			modelUpdated = true;
			updateStatus(context);
		}
	}
	
	public String getHeight() {
		if(this.height!=null){
			return this.height;
		}
		ValueExpression ve = getValueExpression("height");
		if (ve != null) {
		    String value = null;
		    
		    try {
				value = (String) ve.getValue(getFacesContext().getELContext());
		    } catch (ELException e) {
				throw new FacesException(e);
		    }
		    
		    return value;
		} 
	    return null;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		if(this.status!=null){
			return this.status;
		}
		ValueExpression ve = getValueExpression("status");
		if (ve != null) {
		    String value = null;
		    
		    try {
				value = (String) ve.getValue(getFacesContext().getELContext());
		    } catch (ELException e) {
				throw new FacesException(e);
		    }
		    
		    return value;
		} 
	    return null;
	}
	
	public void setHeight(String height) {
		this.height = height;
	}
	
	public String getSelectionMode() {
		if(this.selectionMode!=null){
			return this.selectionMode;
		}
		ValueExpression ve = getValueExpression("selectionMode");
		if (ve != null) {
		    String value = null;
		    
		    try {
				value = (String) ve.getValue(getFacesContext().getELContext());
		    } catch (ELException e) {
				throw new FacesException(e);
		    }
		    
		    return value;
		} 
	    return null;
	}
	
	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}
	
	public SelectionMode selectionMode(){
		if(this.selectionMode ==null || selectionMode.length() == 0){
			return SelectionMode.NONE;
		}
		selectionMode  = selectionMode.trim();
		return Enum.valueOf(SelectionMode.class, selectionMode.toUpperCase());
	}
	
	public String getOnSelection() {
		if (null != this.onselection) {
			return this.onselection;
		}
		ValueExpression _ve = getValueExpression("onSelection");
		if (_ve != null) {
			return (java.lang.String) _ve.getValue(getFacesContext()
					.getELContext());
		} else {
			return null;
		}
	}

	public void setOnSelection(java.lang.String headingLevel) {
		this.onselection = headingLevel;
	}
	
	@Override
	public void restoreState(FacesContext context, Object state){
		Object[] states = (Object[]) state;
		super.restoreState(context, states[0]);
		this.rowCount = (Integer) states[1];
		this.height = (String)states[2];
		this.selectionMode = (String)states[3]; 
		this.onselection=(String)states[4];
		this.summary=(String)states[5];
	}
	
	@Override
	public Object saveState(FacesContext context){
		Object [] state = new Object[6];
		state[0] = super.saveState(context);
		state[1] = this.rowCount;
		state[2] = this.height;
		state[3] = this.selectionMode;
		state[4] = this.onselection;
		state[5] = this.summary;
		return state;
	}

	/**
	 * @return the summary
	 */
	public java.lang.String getSummary() {
		if(this.summary!=null){
			return this.summary;
		}
		ValueExpression ve = getValueExpression("summary");
		if (ve != null) {
		    String value = null;
		    
		    try {
				value = (String) ve.getValue(getFacesContext().getELContext());
		    } catch (ELException e) {
				throw new FacesException(e);
		    }
		    
		    return value;
		} 
	    return null;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}
	
}
