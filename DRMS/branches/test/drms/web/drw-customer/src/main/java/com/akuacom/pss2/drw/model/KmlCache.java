package com.akuacom.pss2.drw.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;

public abstract class KmlCache implements Observer {
	boolean consolidationSLAP;
	Map value;
	public Map getValue() {
		return value;
	}

	public void setValue(Map value) {
		this.value = value;
	}

	public boolean isConsolidationSLAP() {
		return consolidationSLAP;
	}

	public void setConsolidationSLAP(boolean consolidationSLAP) {
		this.consolidationSLAP = consolidationSLAP;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//act on the update
		refreshValue();
	}
	
	protected abstract void refreshValue();
	
	protected void generateValue(List<EventLegend> legends){
		Map kmlCache = new HashMap(); 
		for(EventLegend le: legends){
			String key = le.getEventKey();
			List<String> kml = DRWUtil.getCFEventManager().getKML4EventDetails(le.getEventlocations(),consolidationSLAP);
			kmlCache.put(key, kml);
		}
		this.setValue(kmlCache);
	}

}
