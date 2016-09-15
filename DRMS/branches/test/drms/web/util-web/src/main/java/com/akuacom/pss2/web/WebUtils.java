package com.akuacom.pss2.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;
import com.akuacom.jsf.model.SequenceRange;
import com.akuacom.jsf.model.SortColumn;
import com.akuacom.jsf.model.TableContentProvider;

public class WebUtils {
	
	public static SearchConstraint getSearchConstraint(TableContentProvider<?> provider){
		int firstRow =( (SequenceRange)provider.getRange()).getFirstRow();
		int rows = ( (SequenceRange)provider.getRange()).getRows();
		SortColumn sortColumn = provider.getSortColumn();
		SearchConstraint sc = new SearchConstraint(firstRow,rows);
		if(sortColumn!=null){
			sc.addSortColumn(sortColumn.getName(), sortColumn.isAscendent()?ORDER.ASC:ORDER.DESC);
		}
		return sc;
	}
	
	public static List<SelectItem> enumToSelectItems(Class<? extends Enum<?>> type){
		List<SelectItem> items = new ArrayList<SelectItem>(type.getEnumConstants().length);
		for(Enum<?> instance:type.getEnumConstants()){
			items.add(new SelectItem(instance, instance.toString()));
		}
		return items;
	}
	
	public static List<SelectItem> enumToSelectItemsByNameLabelPair(Class<? extends Enum<?>> type){
		List<SelectItem> items = new ArrayList<SelectItem>(type.getEnumConstants().length);
		for(Enum<?> instance:type.getEnumConstants()){
			items.add(new SelectItem(instance.name(), instance.toString()));
		}
		return items;
	}
}
