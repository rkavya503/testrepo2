package com.honeywell.dras.vtn.api.report;

import java.math.BigDecimal;

public class PowerItem extends ItemBase{

    private BigDecimal hertz;
    private BigDecimal voltage;
    private Boolean ac;
	/**
	 * @return the hertz
	 */
	public BigDecimal getHertz() {
		return hertz;
	}
	/**
	 * @param hertz the hertz to set
	 */
	public void setHertz(BigDecimal hertz) {
		this.hertz = hertz;
	}
	/**
	 * @return the voltage
	 */
	public BigDecimal getVoltage() {
		return voltage;
	}
	/**
	 * @param voltage the voltage to set
	 */
	public void setVoltage(BigDecimal voltage) {
		this.voltage = voltage;
	}
	/**
	 * @return the ac
	 */
	public Boolean getAc() {
		return ac;
	}
	/**
	 * @param ac the ac to set
	 */
	public void setAc(Boolean ac) {
		this.ac = ac;
	}

}
