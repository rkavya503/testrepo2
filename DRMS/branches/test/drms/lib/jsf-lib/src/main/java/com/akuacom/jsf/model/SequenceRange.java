package com.akuacom.jsf.model;

import java.io.Serializable;

import org.ajax4jsf.model.Range;

public class SequenceRange implements Range,Serializable {
	
	private static final long serialVersionUID = 5975220448743179981L;

	private int firstRow = 0;
	
	private int rows = -1;
	
	
	public SequenceRange(int firstRow, int rows) {
		this.firstRow = firstRow;
		this.rows = rows;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + firstRow;
		result = prime * result + rows;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceRange other = (SequenceRange) obj;
		if (firstRow != other.firstRow)
			return false;
		if (rows != other.rows)
			return false;
		return true;
	}
	
}
