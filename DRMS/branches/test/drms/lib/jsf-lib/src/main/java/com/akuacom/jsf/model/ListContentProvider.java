package com.akuacom.jsf.model;

import java.util.Collections;
import java.util.List;

public class ListContentProvider<T> extends AbstractTreeContentProvider<T> {

	private static final long serialVersionUID = 5193369044671765657L;
	private List<T> contents;
	
	public ListContentProvider(List<T> contents){
		if(contents==null)
			throw new IllegalArgumentException("contents must not be null");
		this.contents = contents;
	}
	
	@Override
	public List<T> getChildren(T parent) {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren(T parent) {
		return false;
	}

	@Override
	public int getTotalRowCount() {
		return contents.size();
	}

	@Override
	public List<T> getContents() {
		return contents;
	}

	@Override
	public void updateModel() {
		clearTreeNodeCache(null);
	}

}
