package com.akuacom.pss2.web.aggregation;

import org.richfaces.model.TreeNodeImpl;

public class AggregationTreeNode<T> extends TreeNodeImpl<String> implements Comparable<AggregationTreeNode<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6618139898329226979L;

	public int compareTo(AggregationTreeNode<String> o) {
		if (o == null || o.getData() == null) {
			return 1;
		} else {
			return ((String)getData()).toUpperCase().compareTo((String)o.getData().toUpperCase());
		}
	}
	
	public void generateIdentifier(){
		if(this.getData()!=null&&!this.getData().equalsIgnoreCase("")){
			int indentifyNumber = (int) (this.getData().hashCode()*Math.random()*Math.random());
			setIndentifyNumber(indentifyNumber);
		}else{
			int indentifyNumber = (int) ("GENERATEIDENTIFIER".hashCode()*Math.random()*Math.random());
			setIndentifyNumber(indentifyNumber);
		}
	}
	
	public void setIndentifyNumber(int indentifyNumber) {
		this.indentifyNumber = indentifyNumber;
	}

	public int getIndentifyNumber() {
		if(indentifyNumber==0){
			generateIdentifier();
		}
		return indentifyNumber;
	}

	private int indentifyNumber=0;
}
