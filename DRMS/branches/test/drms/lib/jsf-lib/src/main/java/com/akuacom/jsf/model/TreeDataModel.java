package com.akuacom.jsf.model;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.richfaces.model.ListRowKey;
import org.richfaces.model.TreeRowKey;
import org.richfaces.renderkit.TableHolder;

import com.akuacom.jsf.component.ExtTreeTable;

public class TreeDataModel<T> extends ExtendedDataModel {
	
	private InternalTreeBuilder<T> treeContentProvider;
	
	//private T currentRow;
	
	private TreeNode<T> currentNode;
	
	private List<? extends T> currnetSiblings = Collections.emptyList();
	
	private TreeRowKey<Integer> currentRowKey;
	
	private TreeRowKey<Integer> oldRowKey;
	
	//the index current node in current nodelist
	private int rowIndex;
	
	public TreeDataModel(InternalTreeBuilder<T>  treeContentProvider) {
		this.setWrappedData(treeContentProvider);
		this.treeContentProvider = treeContentProvider;
	}
	
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
		final SequenceRange seqRange = (SequenceRange) range;
		int rows = seqRange.getRows();
		int rowCount =treeContentProvider.getTotalRowCount(); 
		if(rowCount<0) rowCount = 0;
		//int currentRow = seqRange.getFirstRow();
		int currentRow = 0;
		if(rows > 0){
			rows += currentRow;
			if(rowCount >=0){
				rows = Math.min(rows, rowCount);
			}
		} else if(rowCount >=0 ){
			rows = rowCount;
		} else {
			rows = -1;
		}
		while (rows < 0 || currentRow < rows) {
			ListRowKey<Integer> listRowKey = new ListRowKey<Integer>(null,currentRow);
			doWalk(context, visitor,range,listRowKey, argument);
			currentRow++;
		}
	}
	
	private boolean areSiblings(TreeRowKey<Integer> node1, TreeRowKey<Integer> node2){
		if(node1==null || node2==null) return false;
		return node1.getParentKey().equals(node2.getParentKey());
	}
	
	private List<? extends T> getAllSiblings(TreeRowKey<Integer> rowKey){
		TreeRowKey<?> parentKey = rowKey.getParentKey();
		if(parentKey==null || parentKey.depth()==0)
			return treeContentProvider.getContents();
		else{
			//should always loaded
			TreeNode<T> node =treeContentProvider.getNode(parentKey);
			return node.getChildrenList();
		}
	}
	
	protected TreeNode<T> locateTreeNode(TreeRowKey<Integer> rowKey){
		if(rowKey.equals(oldRowKey))
			return currentNode;
		
		TreeNode<T> newNode  =treeContentProvider.getNode(rowKey);
		if(newNode == null){
			//locate row
			if(areSiblings(oldRowKey,rowKey)){
				if(currnetSiblings==null){
					currnetSiblings = getAllSiblings(rowKey);
				}
			}
			else {
				currnetSiblings = getAllSiblings(rowKey);
			}
			
			int idx = Utils.getLastSegment(rowKey);
			if(idx<0 || idx >= currnetSiblings.size())
				return null;
			
			T row= currnetSiblings.get(Utils.getLastSegment(rowKey));
			
			ExtTreeTable.Status status = ExtTreeTable.Status.KIDS_NOT_LOADED;
			boolean hasChildren=treeContentProvider.hasChildren(row);
			if(!hasChildren) {
				status = ExtTreeTable.Status.NO_KIDS;
			}
			
			//first time to request the node
			//build up the tree node on demand
			newNode = new TreeNode<T>(row,status);
			treeContentProvider.addTreeNode(rowKey, newNode);
		}
		
		oldRowKey = rowKey;
		currentNode = newNode;
		return newNode;
	}
	
	
	protected void doWalk(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument) throws IOException {
		@SuppressWarnings("unchecked")
		ListRowKey<Integer> listRowKey = (ListRowKey<Integer>) rowKey;
		
		TreeNode<T> node = locateTreeNode(listRowKey);
		if(node==null)
			return;
		
		ExtTreeTable.Status status = node.getStatus();
		
		this.treeContentProvider.setCurrent(node.getRow());
		
		if(argument instanceof TableHolder ){
			//load triggered by user click 
			if( ((status.intValue() & ExtTreeTable.I_LOADING)!=0) 
					//eager load 
				||  ((status.intValue() & ExtTreeTable.I_KIDS_NOT_LOADED)!=0  
					&& treeContentProvider.isEagerLoad(node.getRow()) ) ){
				
				doLoadKids(node);
				status= node.getStatus();
			}
		}

		processElement(context,dataVisitor,range,listRowKey,argument);
		
		//for loaded nodes, process all its children
		if((status.intValue() & ExtTreeTable.I_LOADED ) != 0){
			//all children, don't support pagination on all non-top levels
			for(int i = 0; i < node.getChildrenList().size(); i ++){
				doWalk(context,dataVisitor,range,new ListRowKey<Integer>(listRowKey,i),argument);
			}
		}
	}
	
	
	protected void doLoadKids(TreeNode<T> node){
		node.setChildrenList(treeContentProvider.getChildren(node.getRow()));
		node.setStatus(ExtTreeTable.Status.KIDS_LOADED_AND_EXPANDED);
	}
	
	protected void processElement(FacesContext context, DataVisitor dataVisitor,
			Range range, Object rowKey, Object argument) throws IOException {
		dataVisitor.process(context, rowKey, argument);
	}
	
	@Override
	public boolean isRowAvailable() {
		return this.currentNode !=null;
	}
	
	@Override
	public int getRowCount() {
		TreeRowKey rowKey =currentRowKey.getParentKey();
		if(rowKey != null){
			return treeContentProvider.getNode(rowKey).getChildrenList().size();
		}else{
			return treeContentProvider.getTotalRowCount();
		}
		//T parentNode = treeContentProvider.getParent(currentRow);
		//return treeContentProvider.getChildCount(parentNode);
	}
	
	@Override
	public Object getRowData() {
		return currentNode.getRow();
	}
	
	@Override
	public int getRowIndex() {
		return rowIndex;
	}
	
	@Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public Object getWrappedData() {
		return this.treeContentProvider;
	}
	
	@Override
	public void setWrappedData(Object data) {
		this.treeContentProvider = (InternalTreeBuilder<T>) data;
	}
	
	public Object getRowKey() {
		return this.currentRowKey;
	}
	
	public void setRowKey(Object rowKey) {
		if (rowKey != null) {
			ListRowKey<Integer> newRowKey = (ListRowKey<Integer>) rowKey;
			this.currentRowKey = newRowKey;
			//this.rowAvailable = null;
		} else {
			this.currentRowKey = null;
			this.oldRowKey = null;
			this.currentNode = null;
			//this.rowAvailable = Boolean.FALSE;
		}
	}
}
