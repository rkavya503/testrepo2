package com.honeywell.dras.vtn.api;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Currently addressing Cool Rate -- double Heat Rate – double Cool Setpoint –
 * double (We do not yet have a strategy for how best to differentiate C and F
 * for temps) Heat Setpoint – double Display Temperature – double Connection
 * Status (online / offline) -- Boolean Opt-Out Status – Boolean Is it opted out
 * of a pending or active event? DR Status – Boolean Is it in active DR mode?
 * Heating Status – Boolean Is the air conditioning running? Cooling Status –
 * Boolean Is the heat running? Fan Status – Boolean Is the fan blowing?
 * 
 * @author sunil
 * 
 */
public class Telemetry {

	private Date timeStamp;
	private Double coolRate;
	private Double heatRate;
	private Double coolSetPoint;
	private Double heatSetPoint;
	private Double displayTemperature;
    private Double outdoorTemperature;
	private Boolean connectionStatus;
	private Boolean optOutStatus;
	private Boolean drStatus;
	private Boolean heatingStatus;
	private Boolean coolingStatus;
	private Boolean fanStatus;
	private Boolean continuityBreak = false;
    private String systemSwitch;
    private String fanSwitch;
    private String drActiveEventName;
    private Boolean relay1State;
    private Boolean relay2State;
    private Boolean relay3State;
    private Boolean relay4State;
    private Double current1;
    private Double current2;
    private Double voltage;
    private Double frequency;

	public static String TIME_STAMP = "timeStamp";
	public static String COOL_RATE = "coolRate";
	public static String HEAT_RATE = "heatRate";
	public static String COOL_SET_POINT = "coolSetPoint";
	public static String HEAT_SET_POINT = "heatSetPoint";
	public static String DISPLAY_TEMPERATURE = "displayTemperature";
    public static String OUTDOOR_TEMPERATURE = "outdoorTemperature";
	public static String CONNECTION_STATUS = "connectionStatus";
	public static String OPT_OUT_STATUS = "optOutStatus";
	public static String DR_STATUS = "drStatus";
	public static String HEATING_STATUS = "heatingStatus";
	public static String COOLING_STATUS = "coolingStatus";
	public static String FAN_STATUS = "fanStatus";
	public static String CONTINUITY_BREAK = "continuityBreak";
	public static String DELEMETER = ":";
	public static String TELEMETRY_TAG = "Telemetry";
    public static String SYSTEM_SWITCH = "SystemSwitch";
    public static String FAN_SWITCH = "FanSwitch";
    public static String DR_ACTIVE_EVENT_NAME ="drActiveEventName";
    public static String RELAY_1_STATE = "RELAY_1_STATE";
    public static String RELAY_2_STATE = "RELAY_2_STATE";
    public static String RELAY_3_STATE = "RELAY_3_STATE";
    public static String RELAY_4_STATE = "RELAY_4_STATE";
    public static String CURRENT_1 = "CURRENT_1";
    public static String CURRENT_2 = "CURRENT_2";
    public static String VOLTAGE = "VOLTAGE";
    public static String FREQUENCY = "FREQUENCY";
	

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the coolRate
	 */
	public Double getCoolRate() {
		return coolRate;
	}

	/**
	 * @param coolRate
	 *            the coolRate to set
	 */
	public void setCoolRate(Double coolRate) {
		this.coolRate = coolRate;
	}

	/**
	 * @return the heatRate
	 */
	public Double getHeatRate() {
		return heatRate;
	}

	/**
	 * @param heatRate
	 *            the heatRate to set
	 */
	public void setHeatRate(Double heatRate) {
		this.heatRate = heatRate;
	}

	/**
	 * @return the coolSetPoint
	 */
	public Double getCoolSetPoint() {
		return coolSetPoint;
	}

	/**
	 * @param coolSetPoint
	 *            the coolSetPoint to set
	 */
	public void setCoolSetPoint(Double coolSetPoint) {
		this.coolSetPoint = coolSetPoint;
	}

	/**
	 * @return the heatSetPoint
	 */
	public Double getHeatSetPoint() {
		return heatSetPoint;
	}

	/**
	 * @param heatSetPoint
	 *            the heatSetPoint to set
	 */
	public void setHeatSetPoint(Double heatSetPoint) {
		this.heatSetPoint = heatSetPoint;
	}

	/**
	 * @return the displayTemperature
	 */
	public Double getDisplayTemperature() {
		return displayTemperature;
	}

	/**
	 * @param displayTemperature
	 *            the displayTemperature to set
	 */
	public void setDisplayTemperature(Double displayTemperature) {
		this.displayTemperature = displayTemperature;
	}

	/**
	 * @return the connectionStatus
	 */
	public Boolean getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * @param connectionStatus
	 *            the connectionStatus to set
	 */
	public void setConnectionStatus(Boolean connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	/**
	 * @return the optOutStatus
	 */
	public Boolean getOptOutStatus() {
		return optOutStatus;
	}

	/**
	 * @param optOutStatus
	 *            the optOutStatus to set
	 */
	public void setOptOutStatus(Boolean optOutStatus) {
		this.optOutStatus = optOutStatus;
	}

	/**
	 * @return the drStatus
	 */
	public Boolean getDrStatus() {
		return drStatus;
	}

	/**
	 * @param drStatus
	 *            the drStatus to set
	 */
	public void setDrStatus(Boolean drStatus) {
		this.drStatus = drStatus;
	}

	/**
	 * @return the heatingStatus
	 */
	public Boolean getHeatingStatus() {
		return heatingStatus;
	}

	/**
	 * @param heatingStatus
	 *            the heatingStatus to set
	 */
	public void setHeatingStatus(Boolean heatingStatus) {
		this.heatingStatus = heatingStatus;
	}

	/**
	 * @return the coolingStatus
	 */
	public Boolean getCoolingStatus() {
		return coolingStatus;
	}

	/**
	 * @param coolingStatus
	 *            the coolingStatus to set
	 */
	public void setCoolingStatus(Boolean coolingStatus) {
		this.coolingStatus = coolingStatus;
	}

	/**
	 * @return the fanStatus
	 */
	public Boolean getFanStatus() {
		return fanStatus;
	}

	/**
	 * @param fanStatus
	 *            the fanStatus to set
	 */
	public void setFanStatus(Boolean fanStatus) {
		this.fanStatus = fanStatus;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (getTimeStamp() != null) {
			sb.append(TIME_STAMP);
			sb.append(DELEMETER);
			sb.append(getTimeStamp().getTime());
			sb.append(DELEMETER);

			if (getCoolRate() != null) {
				sb.append(COOL_RATE);
				sb.append(DELEMETER);
				sb.append(getCoolRate());
				sb.append(DELEMETER);
			}

			if (getHeatRate() != null) {
				sb.append(HEAT_RATE);
				sb.append(DELEMETER);
				sb.append(getHeatRate());
				sb.append(DELEMETER);
			}

			if (getHeatRate() != null) {
				sb.append(HEAT_RATE);
				sb.append(DELEMETER);
				sb.append(getHeatRate());
				sb.append(DELEMETER);
			}

			if (getCoolSetPoint() != null) {
				sb.append(COOL_SET_POINT);
				sb.append(DELEMETER);
				sb.append(getCoolSetPoint());
				sb.append(DELEMETER);
			}

			if (getHeatSetPoint() != null) {
				sb.append(HEAT_SET_POINT);
				sb.append(DELEMETER);
				sb.append(getHeatSetPoint());
				sb.append(DELEMETER);
			}

			if (getDisplayTemperature() != null) {
				sb.append(DISPLAY_TEMPERATURE);
				sb.append(DELEMETER);
				sb.append(getDisplayTemperature());
				sb.append(DELEMETER);
			}

			if (getOutdoorTemperature() != null) {
				sb.append(OUTDOOR_TEMPERATURE);
				sb.append(DELEMETER);
				sb.append(getOutdoorTemperature());
				sb.append(DELEMETER);
			}
            
			if (getConnectionStatus() != null) {
				sb.append(CONNECTION_STATUS);
				sb.append(DELEMETER);
				sb.append(getConnectionStatus());
				sb.append(DELEMETER);
			}

			if (getOptOutStatus() != null) {
				sb.append(OPT_OUT_STATUS);
				sb.append(DELEMETER);
				sb.append(getOptOutStatus());
				sb.append(DELEMETER);
			}

			if (getDrStatus() != null) {
				sb.append(DR_STATUS);
				sb.append(DELEMETER);
				sb.append(getDrStatus());
				sb.append(DELEMETER);
			}

			if (getHeatingStatus() != null) {
				sb.append(HEATING_STATUS);
				sb.append(DELEMETER);
				sb.append(getHeatingStatus());
				sb.append(DELEMETER);
			}

			if (getCoolingStatus() != null) {
				sb.append(COOLING_STATUS);
				sb.append(DELEMETER);
				sb.append(getCoolingStatus());
				sb.append(DELEMETER);
			}

			if (getFanStatus() != null) {
				sb.append(FAN_STATUS);
				sb.append(DELEMETER);
				sb.append(getFanStatus());
				sb.append(DELEMETER);
			}

			if (getContinuityBreak() != null) {
				sb.append(CONTINUITY_BREAK);
				sb.append(DELEMETER);
				sb.append(getContinuityBreak());
				sb.append(DELEMETER);
			}
			if (getSystemSwitch() != null) {
				sb.append(SYSTEM_SWITCH);
				sb.append(DELEMETER);
				sb.append(getSystemSwitch());
				sb.append(DELEMETER);
			}
			if (getFanSwitch() != null) {
				sb.append(FAN_SWITCH);
				sb.append(DELEMETER);
				sb.append(getFanSwitch());
				sb.append(DELEMETER);
			}
			if (getDrActiveEventName() != null) {
				sb.append(DR_ACTIVE_EVENT_NAME);
				sb.append(DELEMETER);
				sb.append(getDrActiveEventName());
				sb.append(DELEMETER);
			}
			if (getRelay1State() != null) {
				sb.append(RELAY_1_STATE);
				sb.append(DELEMETER);
				sb.append(getRelay1State());
				sb.append(DELEMETER);
			}
			if (getRelay2State() != null) {
				sb.append(RELAY_2_STATE);
				sb.append(DELEMETER);
				sb.append(getRelay2State());
				sb.append(DELEMETER);
			}
			if (getRelay3State() != null) {
				sb.append(RELAY_3_STATE);
				sb.append(DELEMETER);
				sb.append(getRelay3State());
				sb.append(DELEMETER);
			}
			if (getRelay4State() != null) {
				sb.append(RELAY_4_STATE);
				sb.append(DELEMETER);
				sb.append(getRelay4State());
				sb.append(DELEMETER);
			}
			if (getCurrent1() != null) {
				sb.append(CURRENT_1);
				sb.append(DELEMETER);
				sb.append(getCurrent1());
				sb.append(DELEMETER);
			}
			if (getCurrent2() != null) {
				sb.append(CURRENT_2);
				sb.append(DELEMETER);
				sb.append(getCurrent2());
				sb.append(DELEMETER);
			}
			if (getVoltage() != null) {
				sb.append(VOLTAGE);
				sb.append(DELEMETER);
				sb.append(getVoltage());
				sb.append(DELEMETER);
			}
			if (getFrequency() != null) {
				sb.append(FREQUENCY);
				sb.append(DELEMETER);
				sb.append(getFrequency());
				sb.append(DELEMETER);
			}
		}
		return sb.toString();
	}

	public Telemetry fromString(String data) {

		if (data != null) {

			StringTokenizer st = new StringTokenizer(data, DELEMETER);
			if (st.countTokens() >= 4) {
				while (st.hasMoreTokens()) {
					String name = st.nextToken();
					String value = st.nextToken();
					if (name.equals(TIME_STAMP)) {
						Long ts = Long.valueOf(value);
						Date timeStamp = new Date(ts);
						setTimeStamp(timeStamp);
					} else if (name.equals(COOL_RATE)) {
						Double c = Double.valueOf(value);
						setCoolRate(c);
					} else if (name.equals(HEAT_RATE)) {
						Double c = Double.valueOf(value);
						setHeatRate(c);
					} else if (name.equals(COOL_SET_POINT)) {
						Double c = Double.valueOf(value);
						setCoolSetPoint(c);
					} else if (name.equals(HEAT_SET_POINT)) {
						Double c = Double.valueOf(value);
						setHeatSetPoint(c);
					} else if (name.equals(DISPLAY_TEMPERATURE)) {
						Double c = Double.valueOf(value);
						setDisplayTemperature(c);
					} else if (name.equals(OUTDOOR_TEMPERATURE)) {
						Double c = Double.valueOf(value);
						setOutdoorTemperature(c);
					} else if (name.equals(CONNECTION_STATUS)) {
						Boolean c = new Boolean(value);
						setConnectionStatus(c);
					} else if (name.equals(OPT_OUT_STATUS)) {
						Boolean c = new Boolean(value);
						setOptOutStatus(c);
					} else if (name.equals(DR_STATUS)) {
						Boolean c = new Boolean(value);
						setDrStatus(c);
					} else if (name.equals(HEATING_STATUS)) {
						Boolean c = new Boolean(value);
						setHeatingStatus(c);
					} else if (name.equals(COOLING_STATUS)) {
						Boolean c = new Boolean(value);
						setCoolingStatus(c);
					} else if (name.equals(FAN_STATUS)) {
						Boolean c = new Boolean(value);
						setFanStatus(c);
					} else if (name.equals(CONTINUITY_BREAK)) {
						Boolean c = new Boolean(value);
						setContinuityBreak(c);
					} else if (name.equals(SYSTEM_SWITCH)) {
						setSystemSwitch(value);
					} else if (name.equals(FAN_SWITCH)) {
						setFanSwitch(value);
					} else if (name.equals(DR_ACTIVE_EVENT_NAME)) {
						setDrActiveEventName(value);
					} else if (name.equals(RELAY_1_STATE)) {
						Boolean c = new Boolean("RESTORED".equals(value));
						setRelay1State(c);
					} else if (name.equals(RELAY_2_STATE)) {
						Boolean c = new Boolean("RESTORED".equals(value));
						setRelay2State(c);
					} else if (name.equals(RELAY_3_STATE)) {
						Boolean c = new Boolean("RESTORED".equals(value));
						setRelay3State(c);
					} else if (name.equals(RELAY_4_STATE)) {
						Boolean c = new Boolean("RESTORED".equals(value));
						setRelay4State(c);
					} else if (name.equals(CURRENT_1)) {
						Double c = Double.valueOf(value);
						setCurrent1(c);
					} else if (name.equals(CURRENT_2)) {
						Double c = Double.valueOf(value);
						setCurrent2(c);
					} else if (name.equals(VOLTAGE)) {
						Double c = Double.valueOf(value);
						setVoltage(c);
					} else if (name.equals(FREQUENCY)) {
						Double c = Double.valueOf(value);
						setFrequency(c);
					}
				}
			} else {
				return null;
			}
		}
		return this;
	}

	/**
	 * @return the continuityBreak
	 */
	public Boolean getContinuityBreak() {
		return continuityBreak;
	}

	/**
	 * @param continuityBreak the continuityBreak to set
	 */
	public void setContinuityBreak(Boolean continuityBreak) {
		this.continuityBreak = continuityBreak;
	}

    /**
     * @return the outdoorTemperature
     */
    public Double getOutdoorTemperature() {
        return outdoorTemperature;
    }

    /**
     * @param outdoorTemperature the outdoorTemperature to set
     */
    public void setOutdoorTemperature(Double outdoorTemperature) {
        this.outdoorTemperature = outdoorTemperature;
    }

    /**
     * @return the systemSwitch
     */
    public String getSystemSwitch() {
        return systemSwitch;
    }

    /**
     * @param systemSwitch the systemSwitch to set
     */
    public void setSystemSwitch(String systemSwitch) {
        this.systemSwitch = systemSwitch;
    }

    /**
     * @return the fanSwitch
     */
    public String getFanSwitch() {
        return fanSwitch;
    }

    /**
     * @param fanSwitch the fanSwitch to set
     */
    public void setFanSwitch(String fanSwitch) {
        this.fanSwitch = fanSwitch;
    }

	public String getDrActiveEventName() {
		return drActiveEventName;
	}

	public void setDrActiveEventName(String drActiveEventName) {
		this.drActiveEventName = drActiveEventName;
	}

	public Boolean getRelay1State() {
		return relay1State;
	}

	public void setRelay1State(Boolean relay1State) {
		this.relay1State = relay1State;
	}

	public Boolean getRelay2State() {
		return relay2State;
	}

	public void setRelay2State(Boolean relay2State) {
		this.relay2State = relay2State;
	}

	public Boolean getRelay3State() {
		return relay3State;
	}

	public void setRelay3State(Boolean relay3State) {
		this.relay3State = relay3State;
	}

	public Boolean getRelay4State() {
		return relay4State;
	}

	public void setRelay4State(Boolean relay4State) {
		this.relay4State = relay4State;
	}

	

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getFrequency() {
		return frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public Double getCurrent1() {
		return current1;
	}

	public void setCurrent1(Double current1) {
		this.current1 = current1;
	}

	public Double getCurrent2() {
		return current2;
	}

	public void setCurrent2(Double current2) {
		this.current2 = current2;
	}

}
