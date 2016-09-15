package com.akuacom.pss2.openadr2.endpoint;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ItemListRequest {
	
	/**
	 * Send all results.  Start at first.  
	 */
	public static final int ALL = -1;
	
	private String entityIdFilter = null;
	
	private int rowNumber = 0;
	private int pageNumber = 0; // zero based index
	private int rowsPerPage = ALL;
	private boolean onlyTotals = false;
	private String fieldToSortBy = null;
	private boolean sortAscending = true;
	
	public void configurePaging(int pageNumber, int rowsPerPage) {
		this.pageNumber = pageNumber;
		this.rowNumber = pageNumber * rowsPerPage;
		this.rowsPerPage = rowsPerPage;
	}
	
	public void configureGetByRow(int startingRowNumber, int rowsToReturn) {
		rowNumber = startingRowNumber;
		rowsPerPage = rowsToReturn;
	}
	
	public void configureGetEverything() {
		rowNumber = 0;
		rowsPerPage = ALL;
	}
	
	public void configureGetEverythingStartingAtRow(int startingRowNumber) {
		rowNumber = startingRowNumber;
		rowsPerPage = ALL;
	}
	
	public void configureTotalsOnly(boolean totalsOnly) {
		setOnlyTotals(totalsOnly);
	}
	
	public String getEntityIdFilter() {
		return entityIdFilter;
	}
	public void setEntityIdFilter(String entityIdFilter) {
		this.entityIdFilter = entityIdFilter;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public boolean isOnlyTotals() {
		return onlyTotals;
	}

	public void setOnlyTotals(boolean onlyTotals) {
		this.onlyTotals = onlyTotals;
	}

	public String getFieldToSortBy() {
		return fieldToSortBy;
	}
	public void setFieldToSortBy(String fieldToSortBy) {
		this.fieldToSortBy = fieldToSortBy;
	}
	public boolean isSortAscending() {
		return sortAscending;
	}
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
}
