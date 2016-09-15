package com.honeywell.dras.vtn.api;

import java.util.List;

/**
 * Signal
 * @author sunil
 *
 */
public interface Oadr2Signal {
	
	public List<Interval> getIntervals();
	
	public String getName();
	
	public SignalType  getType();
	
	public String getId();
	
	public Float getCurrentValue();
	
	public String getValueAsString();

}
