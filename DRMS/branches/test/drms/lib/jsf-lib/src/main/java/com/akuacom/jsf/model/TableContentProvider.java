package com.akuacom.jsf.model;

import java.util.List;

import org.ajax4jsf.model.Range;
import org.richfaces.component.UIColumn;
import org.richfaces.model.selection.Selection;

/**
 * <tt>TableContentProvider </tt> acts as a adaptor to JSF <tt>DataModel<tt>.
 *
 */
public interface TableContentProvider<T> {
	
     /**
      * get the sort Column
      */
	 SortColumn getSortColumn();
	 
	 /**
	  * set the SortColumn 
	  */
	 void setSortColumn(SortColumn sortColumn);
	 
	 
	 /**
	  * Get the range which will be displayed in the table
	  * @return the sequenceRange
	  */
 	 Range getRange();
 	 	
 	 /**
 	  * Set the range of rows which will be displayed in the table
 	  * 
 	  * @param range
 	  */
 	 void setRange(Range range);
 	 
 	 /**
 	  * Return the count of all rows, not just only the rows to be displayed in the table
 	  */
 	 int getTotalRowCount();
 	 
 	 /**
 	  * get the underlying model as a List. Typically it will be got called for two times during 
 	  * <p>a single request/response
 	  * <li> The first time is during the view restoring phase </li>
 	  * <li> the second time is the render render response phase </li>
 	  */
 	 List<? extends T> getContents();
 	 
 	 /** 
 	  * update underlying model. This method is get called before render response phase 
 	  * only when the table component needs to be rendered or re-rendered for ajax request   
 	  */
 	 void updateModel();
 	 
 	 /** returns selection of the component **/
 	 Selection getSelection();
 	 
 	 /** set current selection **/
 	 void setSelection(Selection selection);
 	 
 	 /** reset sort and pagination for this table component programmatically **/ 
 	 void resetSortingOrpagination();
 	 
 	 /** get current row instance during encoding or decoding phase **/
 	 T getCurrent();
 	 
 	 /** whether the current row is selectable or not**/
	 boolean isSelectable(T current);
	 
	 /** locate row object by the giving row key **/
	 T locate(Object key);
	 
	 String getRowStyleClass(T row);
	 
	 String getCellStyleClass(T row,UIColumn column);
	 
	 int getColSpan(T row, UIColumn column);
	 
}

