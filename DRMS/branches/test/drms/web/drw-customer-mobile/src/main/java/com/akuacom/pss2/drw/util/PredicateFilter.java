package com.akuacom.pss2.drw.util;

import java.util.Iterator;
import java.util.List;

public interface PredicateFilter {
	
//	/**
//	 * Selects all the objects in the given iterator that match the given hamcrest Matcher
//	 * @param iterator The iterator of objects to be filtered
//	 * @param matcher The hamcrest Matcher used to filter the given iterable
//	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
//	 */
//	public static <T> List<T> select(Iterator<T> iterator, Matcher<?> matcher) {
//		List<T> collected = new LinkedList<T>();
//        if (iterator == null) return collected;
//        while (iterator.hasNext()) {
//            T item = iterator.next();
//            if (matcher.matches(item)) collected.add(item);
//        }
//		return collected;
//	}
	
	public  <T> List<T> select(List<T> iterator); 
	
	PredicateFilter getPredicateFilter();// return self
	
	void add(PredicateFilter filter); // add a sub component
	
	void remove(PredicateFilter filter); // remove a sub component
	
	Iterator<PredicateFilter> getAggregatorComponents();
}
