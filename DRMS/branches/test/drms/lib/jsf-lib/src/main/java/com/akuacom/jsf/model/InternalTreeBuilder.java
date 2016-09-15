package com.akuacom.jsf.model;


/**
 * Any <code>TreeContentProvider</code> is assumed to also implement {@link InternalTreeBuilder}, 
 * which provides lower-level access that is not necessarily suitable for exposing to clients
 */

public interface InternalTreeBuilder<T> extends TreeContentProvider<T> {
	
	void addTreeNode(Object nodeId,TreeNode<T> node);
	
	TreeNode<T> getNode(Object Id);
	
	void clearTreeNodeCache(T Parent);
	void setCurrent(T current);
}
