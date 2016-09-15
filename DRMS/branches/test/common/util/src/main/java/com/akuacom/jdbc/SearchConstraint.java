package com.akuacom.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchConstraint  implements Serializable {
	
	private static final long serialVersionUID = 6369140521542316385L;
	
	private int startRow = 0;
	private int rowCount = -1;
	
	public static enum ORDER {
		ASC,
		DESC
	}
	
	private Map<String, ORDER> orderColumn=new HashMap<String, ORDER>(2);
	
	private List<String> sortColumns = new ArrayList<String>(2);
	
	public SearchConstraint(int startRow, int rowCount) {
		super();
		this.startRow = startRow;
		this.rowCount = rowCount;
	}
	
	public int getStartRow() {
		return startRow;
	}
	
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	public List<String> getOrderColumns() {
		return this.sortColumns;
	}
	
	public void addSortColumn(String name,ORDER order){
		orderColumn.put(name, order);
		sortColumns.add(name);
	}
	
	public ORDER getOrder(String orderColumn){
		return this.orderColumn.get(orderColumn);
	}
}
