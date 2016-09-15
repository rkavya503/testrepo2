package com.akuacom.pss2.drw;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StringKeyAndEventsListValueHolder {
	private String key;
	private ArrayList<Event> eventsList = new ArrayList<Event>();

	public StringKeyAndEventsListValueHolder() {

	}

	public StringKeyAndEventsListValueHolder(String key,
			ArrayList<Event> eventsList) {
		this.key = key;
		this.eventsList = eventsList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<Event> getEventsList() {
		return eventsList;
	}

	public void setEventsList(ArrayList<Event> eventsList) {
		this.eventsList = eventsList;
	}

}