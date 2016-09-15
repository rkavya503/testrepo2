package com.akuacom.jsf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.richfaces.model.selection.Selection;

public abstract class AbstractTreeContentProvider<T> 
			extends AbstractTableContentProvider<T> implements InternalTreeBuilder<T> {
	
	private static final long serialVersionUID = -5323917184853916059L;
	private Map<String,TreeNode<T>> treeNodeCache = new HashMap<String,TreeNode<T>>();
	
	
	public void addTreeNode(Object nodeId,TreeNode<T> node){
		String id = strKey(nodeId);
		treeNodeCache.put(id,node);
	}
	
	public TreeNode<T> getNode(Object key){
		return treeNodeCache.get(strKey(key));
	}
	
	private String strKey(Object nodeId){
		if(nodeId instanceof String) return (String) nodeId;
		return nodeId.toString();
	}
	
	public void clearTreeNodeCache(T parent) {
		if(parent == null){
			treeNodeCache.clear();
		}else{
			removeNode(parent);
		}
	}
	
	protected void updateNode(Object id,TreeNode<T> node){
		treeNodeCache.put(strKey(id), node);
	}
	
	protected void removeNode(Object id){
		treeNodeCache.remove(id);
	}
	
	@Override
	public boolean isEagerLoad(T node) {
		return false;
	}

	@Override
	public boolean isAutoExpand(T node) {
		return false;
	}
	
	protected void clearSelection(){
		this.setSelection(null);
	}
	
	public T getFirstSelected(){
		Selection selection = getSelection();
		if(selection!=null && selection.getKeys().hasNext()){
			Object key =selection.getKeys().next();
			TreeNode<T> node =this.getNode(key);
			if(node!=null)
				return node.getRow();
		}
		return null; 
	}
	
	public T locate(Object key){
		TreeNode<T> node =this.getNode(key);
		if(node!=null)
			return node.getRow();
		return null;
	}
	
	public List<T> getSelectedObjects(){
		Selection selection = getSelection();
		if(selection!=null){
			List<T> objs = new ArrayList<T>();
			Iterator<?> iterator = selection.getKeys();
			while(iterator.hasNext()){
				TreeNode<T> node =this.getNode(iterator.next());
				if(node!=null)
					objs.add(node.getRow());
			}
			return objs;
		}
		return Collections.emptyList();
	}
	
	public List<T> getSelectedDescendants(T parent){
		return getSelectedDescendants(parent, false);
	}
	
	public List<T> getSelectedKid(T parent){
		return getSelectedDescendants(parent, true);
	}
	
	
	private List<T> getSelectedDescendants(T parent, boolean kid){
		Selection selection = getSelection();
		if(selection==null){
			return Collections.emptyList();
		}
		
		TreeNode<T> node = null;
		String parentKey = null;
		for(Object key:treeNodeCache.keySet()){
			TreeNode<T> node1 = treeNodeCache.get(key);
			if(node1.getRow().equals(parent)){
				node = node1;
				parentKey = (String) key;
				break;
			}
		}
		if(node==null) return Collections.emptyList();
		
		int parentlevel = parentKey.split(":").length;
		List<T> objs = new ArrayList<T>();
		Iterator<?> iterator = selection.getKeys();
		
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			int level = key.split(":").length;
			if(key.startsWith(parentKey)){
				if(kid && level-1==parentlevel){
					objs.add(treeNodeCache.get(key).getRow());
				}else{
					objs.add(treeNodeCache.get(key).getRow());
				}
			}
		}
		return objs;
	}
	
	
}
