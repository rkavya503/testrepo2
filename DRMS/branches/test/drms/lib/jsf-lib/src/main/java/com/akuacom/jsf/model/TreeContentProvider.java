package com.akuacom.jsf.model;

import java.util.List;

public interface TreeContentProvider<T> extends TableContentProvider<T> {
	
	List<T> getChildren(T parent);
	
	boolean hasChildren(T parent);
	
	boolean isEagerLoad(T node);
	
	boolean isAutoExpand(T node);
	
}
