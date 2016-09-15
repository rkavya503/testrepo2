package com.akuacom.jsf.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.ajax4jsf.model.Range;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.component.UIColumn;
import org.richfaces.model.selection.Selection;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;

public abstract class AbstractTableContentProvider<T> implements TableContentProvider<T>,Serializable{
	private static final Logger log = Logger.getLogger(AbstractTableContentProvider.class);
	
	private static final int LESSER = -1;
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	private static final long serialVersionUID = 2904912645265417571L;

	private boolean sortColumnChanged;
	
	private boolean rangeChanged;
	
	private SortColumn sortColumn;
	
	private Range range;
	
	private Selection selection;
	
	private T current;
	
	public SortColumn getSortColumn() {
		return sortColumn;
	}
	
	public void setSortColumn(SortColumn sortColumn) {
		SortColumn old = this.sortColumn;
		/*if(old==null){
			this.sortColumn = sortColumn;
			sortColumnChanged = false;
		}*/
		//else{
			if(Utils.objectEquals(old,sortColumn)){
				sortColumnChanged = false;
			}else{
				this.sortColumn = sortColumn;
				sortColumnChanged = true;
			}
		//}
	}
	
	protected SequenceRange getSequenceRange(){
		return (SequenceRange) range;
	}
	
	protected void setSequenceRange(SequenceRange sequenceRange){
		setRange(sequenceRange);
	}
	
	public Range getRange() {
		return range;
	}
	
	public void setRange(Range range) {
		Range old = this.range;
		if(old==null){
			this.range = range;
			rangeChanged =false;
		}else{
			if(Utils.objectEquals(old,range)){
				rangeChanged = false;
			}else{
				rangeChanged = true;
				this.range = range;
			}
		}
	}
	
	public boolean isSortColumnChanged() {
		return sortColumnChanged;
	}
	
	public boolean isRangeChanged() {
		return rangeChanged;
	}
	
	public Selection getSelection(){
		return selection;
	}
	
	public void setSelection(Selection selection){
		this.selection = selection;
	}
	
	@Override
	public String getCellStyleClass(T row, UIColumn column) {
		return null;
	}

	@Override
	public String getRowStyleClass(T row) {
		return null;
	}
	
	
	@Override
	public int getColSpan(T row, UIColumn column) {
		return 1;
	}
	
	public T getFirstSelected(){
		if(this.selection!=null && this.selection.getKeys().hasNext()){
			Object key =this.selection.getKeys().next();
			int idx = Integer.parseInt((String)key);
			return getContents().get(idx);
		}
		return null; 
	}
	
	public T locate(Object key){
		int idx = Integer.parseInt((String)key);
		if(idx>=0 && idx<getContents().size()){
			return getContents().get(idx);
		}
		return null;
	}
	
	public List<T> getSelectedObjects(){
		if(this.selection!=null){
			List<T> objs = new ArrayList<T>();
			Iterator<?> iterator = selection.getKeys();
			while(iterator.hasNext()){
				int idx=Integer.parseInt((String) iterator.next());
				objs.add(getContents().get(idx));
			}
			return objs;
		}
		return Collections.emptyList();
	}
	
	@Override
	public void resetSortingOrpagination() {
		
	}
	
	
	public T getCurrent() {
		return current;
	}

	public void setCurrent(T current) {
		this.current = current;
	}
	
	@Override
	public boolean isSelectable(T current){
		return true;
	}
	
	public boolean isKeepSelectionStatus() {
		return true;
	}
	
	public SearchConstraint getConstraint(){
		int firstRow =( (SequenceRange)getRange()).getFirstRow();
		int rows = ( (SequenceRange)getRange()).getRows();
		SortColumn sortColumn = getSortColumn();
		SearchConstraint sc = new SearchConstraint(firstRow,rows);
		if(sortColumn!=null){
			sc.addSortColumn(sortColumn.getName(), sortColumn.isAscendent()?ORDER.ASC:ORDER.DESC);
		}
		return sc;
	}
	

	@SuppressWarnings("unchecked")
	public void sort(List<? extends T> contents,SearchConstraint constraint){
		SortColumn sortColumn =getSortColumn();
		if(sortColumn==null)
			 return;
		final String columnName = sortColumn.getName();
		final boolean ascendent= sortColumn.isAscendent();
		
		if(constraint!=null && constraint.getOrderColumns()!=null && columnName!=null){
			Collections.sort(contents, new Comparator(){
				@Override
				public int compare(Object obj1, Object obj2) {
					if(ascendent)
						return doCompare((T)obj1, (T)obj2,columnName);
					else
						return doCompare((T)obj2, (T)obj1,columnName);
				}
			});
		}
	}
	
	protected boolean isCaseSensitive(String property){
		return false;
	}
	
	
	protected int doCompare(T row1, T row2,String property){
		//  alternate to actual value comparison 
		if (null == row1 && null != row2) {
			return LESSER;
		} else if (null != row1 && null == row2) {
			return GREATER;
		} else if (null == row1 && null == row2) {
			return EQUAL;		
		}

		int response = EQUAL;
		try {
			Object v1 = (null == property) ? row1 : getValue(row1,property);
			Object v2 = (null == property) ? row2 : getValue(row2,property);

			if (v1 instanceof Comparable && v2 instanceof Comparable) {
				response = (((Comparable)v1).compareTo((Comparable)v2));
			} else {
				response = (v1==null ? "" : v1.toString()).compareTo(v2==null ? "" : v2.toString());
			}
		} catch (Exception e) {
			log.error("Exception occurred while comparing", e);
			response = EQUAL;
		}
		
		return response;
	}
	
	/**
	 * Return the value of the (possibly nested) property of the specified name, for the specified bean, with no type conversions
	 * @param obj - a {@link java.lang.Object}
	 * @return object - a {@link java.lang.Object} - return of given method
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	private Object getValue(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(name==null||bean==null) return bean;
		
		return PropertyUtils.getNestedProperty(bean, name);
	}
}
