package com.akuacom.pss2.opt.report;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;
import com.akuacom.jsf.model.SequenceRange;
import com.akuacom.jsf.model.SortColumn;
import com.akuacom.jsf.model.TableContentProvider;

public class WebUtil {

	public static SearchConstraint getSearchConstraint(TableContentProvider<?> provider){
		int firstRow =( (SequenceRange)provider.getRange()).getFirstRow();
		int rows = ( (SequenceRange)provider.getRange()).getRows();
		SortColumn sortColumn = provider.getSortColumn();
		SearchConstraint sc = new SearchConstraint(firstRow,rows);
		if(sortColumn!=null){
			String columnName = sortColumn.getName();
			String cols[] =columnName.trim().split("\\.");
			for(String col:cols){
				sc.addSortColumn(col, sortColumn.isAscendent()?ORDER.ASC:ORDER.DESC);	
			}
		}
		return sc;
	}
}
