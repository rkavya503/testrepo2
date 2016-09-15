package com.akuacom.jsf.model;

import java.util.Collections;
import java.util.List;

public abstract class LazyListContentProvider<T> extends AbstractTreeContentProvider<T> {

	private static final long serialVersionUID = 7225341728302700407L;
	private List<T> contents;
	
	abstract public List<T> doGetContents();
	
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
		return getContents().size();
	}

	@Override
	public List<T> getContents() {
		if(contents==null)
			return Collections.emptyList();
		return contents;
	}

	@Override
	public void updateModel() {
		this.clearTreeNodeCache(null);
		//load on demand
		contents = doGetContents();
		sort(contents, getConstraint());
	}
	
}
