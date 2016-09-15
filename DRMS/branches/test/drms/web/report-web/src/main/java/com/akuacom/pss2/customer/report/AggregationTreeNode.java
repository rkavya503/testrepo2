package com.akuacom.pss2.customer.report;

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
}
