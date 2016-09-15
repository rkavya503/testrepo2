package com.honeywell.dras.vtn.api;

import java.util.Date;
import java.util.StringTokenizer;

public class TstatEventStatus {
	
    private String eventId;
	private String venId;
	private String mac;
	private Integer deviceId;
	private Date startTime;
	private String eventPhase;
	private Boolean optedOut;
	
	
    public static String EVENT_ID = "EventId";
	public static String TSTAT_EVENT_STATUS_TAG = "TstatEventStatus";
	public static String DELEMETER = ":";
	public static String MAC = "mac";
	public static String DEVICE_ID = "deviceId";
	public static String START_TIME = "startTime";
	public static String EVENT_PHASE = "eventPhase";
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
        if (getEventId() != null) {
            sb.append(EVENT_ID);
            sb.append(DELEMETER);
            sb.append(getEventId());
            sb.append(DELEMETER);
        }
		if(mac != null){
			sb.append(MAC);
			sb.append(DELEMETER);
			sb.append(mac);
			sb.append(DELEMETER);
		} 
        if(deviceId != null){
			sb.append(DEVICE_ID);
			sb.append(DELEMETER);
			sb.append(deviceId);
			sb.append(DELEMETER);
		} 
        if(startTime != null){
			sb.append(START_TIME);
			sb.append(DELEMETER);
			sb.append(startTime.getTime());
			sb.append(DELEMETER);
		} 
        if(eventPhase != null){
			sb.append(EVENT_PHASE);
			sb.append(DELEMETER);
			sb.append(eventPhase);
			sb.append(DELEMETER);
		}
		
		return sb.toString();
		
	}

	
	public TstatEventStatus fromString(String data, Boolean optedOut, String venId ) {
		
		if (data != null) {

			StringTokenizer st = new StringTokenizer(data, DELEMETER);
			if (st.countTokens() >= 2) {
				while (st.hasMoreTokens()) {
					String name = st.nextToken();
					String value = st.nextToken();
                    if (name.equals(EVENT_ID)) {
                        setEventId(value);
                    }
					if (name.equals(START_TIME)) {
						Long ts = Long.valueOf(value);
						Date timeStamp = new Date(ts);
						setStartTime(timeStamp);
					}else if(name.equals(MAC)){
						setMac(value);
					}else if(name.equals(EVENT_PHASE)){
						setEventPhase(value);
					}else if (name.equals(DEVICE_ID)) {
						Integer c = Integer.valueOf(value);
						setDeviceId(c);
					} 
				}
			}
		}
		
		if(optedOut != null){
			setOptedOut(optedOut);
		}
		
		if(venId != null){
			setVenId(venId);
		}
		return this;
		
	}
	
	
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
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * @return the deviceId
	 */
	public Integer getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the eventPhase
	 */
	public String getEventPhase() {
		return eventPhase;
	}

	/**
	 * @param eventPhase the eventPhase to set
	 */
	public void setEventPhase(String eventPhase) {
		this.eventPhase = eventPhase;
	}

	/**
	 * @return the optedOut
	 */
	public Boolean isOptedOut() {
		return optedOut;
	}

	/**
	 * @param optedOut the optedOut to set
	 */
	public void setOptedOut(Boolean optedOut) {
		this.optedOut = optedOut;
	}

    /**
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
	
	

}
