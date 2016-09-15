package com.akuacom.pss2.drw.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.akuacom.pss2.drw.Event;

public class ScheduleIREventPredicateFilter implements PredicateFilter {
	
	private List<PredicateFilter> componentList = new ArrayList<PredicateFilter>();

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <Event> List<Event> select(List<Event> iterator) {
		List<Event> input =  new LinkedList<Event>(iterator);
        if (iterator == null) return new LinkedList<Event>();
      
        Iterator<PredicateFilter> filterIterator = getAggregatorComponents();
        while (filterIterator.hasNext()) {
    	    PredicateFilter filter = filterIterator.next();
    	    input = filter.select(input);//recursion narrow down input list by filters
        }
       List<Event> collected = new LinkedList<Event>();
       DateEntrySelectPredicate betweenPredicate = new DateEntrySelectPredicate(false);
       collected = (List<Event>) CollectionUtils.select(input, betweenPredicate);
		
	   return collected;
	}
	
	static class DateEntrySelectPredicate implements Predicate {
		private boolean active;
		
		public DateEntrySelectPredicate(boolean active){
			this.active = active;
		}

		public boolean evaluate(Object object) {
			if(((Event)object).getStartDateTime()!=null&&((Event)object).getStartDateTime().before(new Date())){
				return false;
			}
			return true;
		}
	}

	@Override
	public PredicateFilter getPredicateFilter() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void add(PredicateFilter filter) {
		componentList.add(filter);

	}

	@Override
	public void remove(PredicateFilter filter) {
		componentList.remove(filter);

	}

	@Override
	public Iterator<PredicateFilter> getAggregatorComponents() {
		// TODO Auto-generated method stub
		return componentList.iterator();
	}


}
