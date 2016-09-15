package com.akuacom.jsf.taglib;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.akuacom.jsf.component.ExtendedHtmlDataTable;

/**
 * this comstom tag provides the ability for programmer to add a callback to
 * retrieve data in the order of clicked column. the default DataTable provided
 * by rich faces does provide functionality for row sorting but that job is job
 * in memory locally for all the rows in current page. However, sometimes user
 * may want all rows of data in all pages be reordered. Nothing changed in the
 * tag definition but the underlying data model
 **/
public class DataTableTag extends org.richfaces.taglib.DataTableTag {

	private ValueExpression _selectionMode;
	
	private ValueExpression _height;
	
	private ValueExpression _status;
	
	private ValueExpression _onSelection;
	
	private ValueExpression summary;
	
	public String getComponentType() {
		return "com.akuacom.jsf.DataTable";
	}

	public String getRendererType() {
		return "com.akuacom.jsf.DataTableRenderer";
	}

	public ValueExpression getHeight() {
		return _height;
	}

	public void setHeight(ValueExpression height) {
		this._height = height;
	}
	
	public ValueExpression getSelectionMode() {
		return _selectionMode;
	}

	public void setSelectionMode(ValueExpression selectionMode) {
		this._selectionMode = selectionMode;
	}
	
	public void setStatus(ValueExpression status){
		this._status=status;
	}
	
	public ValueExpression getStatus(){
		return this._status;
	}
	
	public ValueExpression getOnSelection() {
		return _onSelection;
	}
	
	public void setOnSelection(ValueExpression onselection) {
		this._onSelection = onselection;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		ExtendedHtmlDataTable comp = (ExtendedHtmlDataTable) component;
		if(this._selectionMode!=null){
			if (this._selectionMode.isLiteralText()) {
				try {
											
					java.lang.String value = (java.lang.String) getFacesContext().
						getApplication().
							getExpressionFactory().
								coerceToType(this._selectionMode.getExpressionString(), 
										java.lang.String.class);
											comp.setSelectionMode(value);
										} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("selectionMode", this._selectionMode);
			}
		}
		
		if(this._height!=null){
			if (this._height.isLiteralText()) {
				try {
											
					java.lang.String value = (java.lang.String) getFacesContext().
						getApplication().
							getExpressionFactory().
								coerceToType(this._height.getExpressionString(), 
										java.lang.String.class);
											comp.setHeight(value);
										} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("height", this._height);
			}
		}
		
		if(this._status!=null){
			if (this._status.isLiteralText()) {
				try {
											
					java.lang.String value = (java.lang.String) getFacesContext().
						getApplication().
							getExpressionFactory().
								coerceToType(this._status.getExpressionString(), 
										java.lang.String.class);
											comp.setStatus(value);
										} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("status", this._status);
			}
		}
		
		if(this._onSelection!=null){
			if (this._onSelection.isLiteralText()) {
				try {
											
					java.lang.String value = (java.lang.String) getFacesContext().
						getApplication().
							getExpressionFactory().
								coerceToType(this._onSelection.getExpressionString(), 
										java.lang.String.class);
											comp.setOnSelection(value);
										} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("onSelection", this._onSelection);
			}
		}
		if(this.summary!=null){
			if (this.summary.isLiteralText()) {
				try {
											
					java.lang.String value = (java.lang.String) getFacesContext().
						getApplication().
							getExpressionFactory().
								coerceToType(this.summary.getExpressionString(), 
										java.lang.String.class);
											comp.setSummary(value);
										} catch (ELException e) {
					throw new FacesException(e);
				}
			} else {
				component.setValueExpression("summary", this.summary);
			}
		}
	}
	
	public void release(){
		super.release();
		this._height = null;
		this._selectionMode= null;
		this._status = null;
		this.summary=null;
	}

	/**
	 * @return the summary
	 */
	public ValueExpression getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(ValueExpression summary) {
		this.summary = summary;
	}
	
}
