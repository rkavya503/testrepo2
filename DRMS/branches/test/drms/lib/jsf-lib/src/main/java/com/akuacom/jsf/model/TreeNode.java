package com.akuacom.jsf.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.akuacom.jsf.component.ExtTreeTable;

public class TreeNode<T> implements Serializable{
	
	private static final long serialVersionUID = -8369101503203045101L;

	private T row;
	
	private List<T> childrenList;
	
	private ExtTreeTable.Status status = ExtTreeTable.Status.KIDS_NOT_LOADED;
	
	
	public TreeNode(T row,ExtTreeTable.Status status){
		this.row = row;
		this.status =status;
	}
	
	public TreeNode(T row, List<T> childrenList,ExtTreeTable.Status status){
		this.row = row;
		this.childrenList = childrenList;
		this.status =status;
	}

	public T getRow() {
		return row;
	}
	

	public List<T> getChildrenList() {
		if(childrenList==null){
			return Collections.emptyList();
		}
		return childrenList;
	}
	
	public void setChildrenList(List<T> childrenList) {
		this.childrenList = childrenList;
	}

	public ExtTreeTable.Status getStatus() {
		return status;
	}
	
	public void setStatus(ExtTreeTable.Status status) {
		if(status!=null && this.status!=status){
			this.status = status;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((row == null) ? 0 : row.hashCode());
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
		TreeNode other = (TreeNode) obj;
		if (row == null) {
			if (other.row != null)
				return false;
		} else if (!row.equals(other.row))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TreeNode [row=" + row + ", status=" + status + "]";
	}
	
}
