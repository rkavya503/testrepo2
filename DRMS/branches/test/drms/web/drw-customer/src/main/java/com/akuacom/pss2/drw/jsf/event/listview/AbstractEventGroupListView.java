package com.akuacom.pss2.drw.jsf.event.listview;

import java.io.Serializable;
import java.util.List;

import com.akuacom.pss2.drw.jsf.ListViewUIBackingBean;
import com.akuacom.pss2.drw.model.BaseEventDataModel;

public abstract class AbstractEventGroupListView implements Serializable{
	private static final long serialVersionUID = 12304978574034L;
	private List<BaseEventDataModel> events;
	private ListViewUIBackingBean listView;
	public abstract  List<BaseEventDataModel> retrieveEvents();
	/**
	 * @return the eventEmptyFlag
	 */
	public abstract boolean isEventEmptyFlag(); 

	/**
	 * @return the listView
	 */
	public ListViewUIBackingBean getListView() {
		return listView;
	}
	/**
	 * @param listView the listView to set
	 */
	public void setListView(ListViewUIBackingBean listView) {
		this.listView = listView;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(List<BaseEventDataModel> events) {
		this.events = events;
	}
	public  List<BaseEventDataModel> getEvents(){
		events = retrieveEvents();
		if(events.size()>0){
			searchEventEmptyFlag = false;
		}else{
			searchEventEmptyFlag = true;
		}
		return events;
	}
	public boolean searchEventEmptyFlag = false;
	/**
	 * @return the searchEventEmptyFlag
	 */
	public boolean isSearchEventEmptyFlag() {
		return searchEventEmptyFlag;
	}
	/**
	 * @param searchEventEmptyFlag the searchEventEmptyFlag to set
	 */
	public void setSearchEventEmptyFlag(boolean searchEventEmptyFlag) {
		this.searchEventEmptyFlag = searchEventEmptyFlag;
	}
	
}
