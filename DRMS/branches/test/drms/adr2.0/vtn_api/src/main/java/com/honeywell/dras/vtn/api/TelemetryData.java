package com.honeywell.dras.vtn.api;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Holds Telemetry Data
 * @author sunil
 *
 */
public class TelemetryData {
	
	private String venId;
	
	private List<Telemetry> telemetryList;

	/**
	 * @return the venId
	 */
	public String getVenId() {
		return venId;
	}

	/**
	 * @param venId the venId to set
	 */
	public void setVenId(String venId) {
		this.venId = venId;
	}

	/**
	 * @return the telemetryList
	 */
	public List<Telemetry> getTelemetryList() {
		return telemetryList;
	}

	/**
	 * @param telemetryList the telemetryList to set
	 */
	public void setTelemetryList(List<Telemetry> telemetryList) {
		this.telemetryList = telemetryList;
	}
	
	
	public List<Telemetry> fromString(String data){
		List<Telemetry>  tList = new ArrayList<Telemetry>();
		if (data != null) {
			StringTokenizer st = new StringTokenizer(data, "*");
			if (st.countTokens() >= 1) {
				Telemetry t = new Telemetry();
				t = t.fromString(st.nextToken());
				if(t != null){
					tList.add(t);
				}
			}
		}
		return tList;

	}
}
