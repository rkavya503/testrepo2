/**
 * 
 */
package com.akuacom.pss2.data.irr;

import java.io.Serializable;
import java.util.Date;
import com.akuacom.utils.lang.DateUtil;
/**
 *
 */
public class TreeDataEntry implements Serializable {

	private static final long serialVersionUID = 2975830672560817755L;
	
    private Date time;
	private String strTime;
    private Double value;
    private String stringValue;
    private String valueType;
    private Boolean actual;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Boolean getActual() {
		return actual;
	}

	public void setActual(Boolean actual) {
		this.actual = actual;
	}

	public String getStrTime() {//time
        return DateUtil.format(time, "yyyy-MM-dd-HH-mm-ss");
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
	
}
