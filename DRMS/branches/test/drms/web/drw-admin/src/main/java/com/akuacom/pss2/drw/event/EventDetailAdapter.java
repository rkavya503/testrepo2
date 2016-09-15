package com.akuacom.pss2.drw.event;

import java.util.Date;
import java.util.Set;

import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.ZipCodeEntry;

public class EventDetailAdapter implements UiEventDetail {
	//Constructor
	public EventDetailAdapter(){}
	
	private EventDetail evtDetail;
	private int rowIndex;
	
	public EventDetail getEvtDetail() {
		return evtDetail;
	}
	public void setEvtDetail(EventDetail evtDetail) {
		this.evtDetail = evtDetail;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public int getRowIndex() {
		return rowIndex;
	}
    //********************************Adapter methods
	@Override
	public Event getEvent() {
		return evtDetail.getEvent();
	}

	@Override
	public Date getEstimatedEndTime() {
		return evtDetail.getEstimatedEndTime();
	}

	@Override
	public Date getActualEndTime() {
		return evtDetail.getActualEndTime();
	}

	@Override
	public Date getLastModifiedTime() {
		return evtDetail.getLastModifiedTime();
	}

	@Override
	public String getAllLocationType() {
		return evtDetail.getAllLocationType();
	}

	@Override
	public Location getLocation() {
		return evtDetail.getLocation();
	}

	@Override
	public String getBlockNames() {
		return evtDetail.getBlockNames();
	}

	@Override
	public Set<ZipCodeEntry> getZipCodeEntries() {
		return evtDetail.getZipCodeEntries();
	}

	@Override
	public String getUUID() {
		return evtDetail.getUUID();
	}

	@Override
	public Date getCreationTime() {
		return evtDetail.getCreationTime();
	}

}
