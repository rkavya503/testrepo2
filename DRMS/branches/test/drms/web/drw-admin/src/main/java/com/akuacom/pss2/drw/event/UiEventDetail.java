package com.akuacom.pss2.drw.event;

import java.util.Date;
import java.util.Set;

import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.ZipCodeEntry;

public interface UiEventDetail{
	
	public int getRowIndex();

	public Event getEvent();
	
	public Date getEstimatedEndTime();

	public Date getActualEndTime();

	public Date getLastModifiedTime();

	public String getAllLocationType();

	public Location getLocation();

	public String getBlockNames();

	public Set<ZipCodeEntry> getZipCodeEntries();

	public String getUUID();

	public Date getCreationTime();

}
