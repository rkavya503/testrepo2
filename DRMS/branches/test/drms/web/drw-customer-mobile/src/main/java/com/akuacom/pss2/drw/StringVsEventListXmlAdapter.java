package com.akuacom.pss2.drw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringVsEventListXmlAdapter
		extends
		XmlAdapter<StringKeyAndEventsListValueHolderListWrapper, Map<String, ArrayList<Event>>> {

	@Override
	public StringKeyAndEventsListValueHolderListWrapper marshal(
			Map<String, ArrayList<Event>> StringVsListOfeventsMap) throws Exception {
		ArrayList<StringKeyAndEventsListValueHolder> StringKeyAndEventsListValueHolders = new ArrayList<StringKeyAndEventsListValueHolder>();
		if (StringVsListOfeventsMap != null) {
			Set<String> eventStringkeySet = StringVsListOfeventsMap.keySet();
			for (String eventString : eventStringkeySet) {
				StringKeyAndEventsListValueHolders
						.add(new StringKeyAndEventsListValueHolder(eventString,
								StringVsListOfeventsMap.get(eventString)));
			}
		}
		StringKeyAndEventsListValueHolderListWrapper listWrapper = new StringKeyAndEventsListValueHolderListWrapper();
		listWrapper
				.setStringKeyAndEventsListValueHolders(StringKeyAndEventsListValueHolders);
		return listWrapper;
	}

	@Override
	public Map<String, ArrayList<Event>> unmarshal(
			StringKeyAndEventsListValueHolderListWrapper StringKeyAndEventsListValueHolderList)
			throws Exception {
		Map<String, ArrayList<Event>> stringVsListOfeventsMap = new HashMap<String, ArrayList<Event>>();
		if (StringKeyAndEventsListValueHolderList != null) {
			for (StringKeyAndEventsListValueHolder holder : StringKeyAndEventsListValueHolderList
					.getStringKeyAndEventsListValueHolders()) {
				stringVsListOfeventsMap.put(holder.getKey(),
						holder.getEventsList());
			}
		}
		return stringVsListOfeventsMap;
	}
}