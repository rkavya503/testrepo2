package com.akuacom.pss2.drw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;

public class EventLegendCache extends Observable {
	
	private List<EventLegend> eventLegends = new ArrayList<EventLegend>();

	public List<EventLegend> getEventLegends() {
		return DRWUtil.sortEventLegends(eventLegends);
	}

	public void setEventLegends(List<EventLegend> eventLegends) {
		//mark the observable as changed
		this.eventLegends = eventLegends;
		
		setChanged();
		notifyObservers();
		
	}

}
