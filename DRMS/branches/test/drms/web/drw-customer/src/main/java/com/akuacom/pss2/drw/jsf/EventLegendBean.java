package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;
import java.util.List;

public class EventLegendBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//Summer Discount Plan (SDP) - Residential
	private List<LegendItem> sdprItems;
	private Boolean sdprSelected;
	//Summer Discount Plan (SDP) - Commercial
	private List<LegendItem> sdpcItems;
	private Boolean sdpcSelected;
	//Agricultural &amp; Pumping Interruptible Program (AP-I)
	private List<LegendItem> apiItems;
	private Boolean apiSelected;
	//Time-of-Use Base Interruptible Program (TOU-BIP)
	private List<LegendItem> bipItems;
	private Boolean bipSelected;
	
	private List<LegendItem> cbpItems;
	private Boolean cbpSelected;
	
	public List<LegendItem> getSdprItems() {
		return sdprItems;
	}
	public void setSdprItems(List<LegendItem> sdprItems) {
		this.sdprItems = sdprItems;
	}
	public List<LegendItem> getSdpcItems() {
		return sdpcItems;
	}
	public void setSdpcItems(List<LegendItem> sdpcItems) {
		this.sdpcItems = sdpcItems;
	}
	public List<LegendItem> getApiItems() {
		return apiItems;
	}
	public void setApiItems(List<LegendItem> apiItems) {
		this.apiItems = apiItems;
	}
	public List<LegendItem> getBipItems() {
		return bipItems;
	}
	public void setBipItems(List<LegendItem> bipItems) {
		this.bipItems = bipItems;
	}
	public Boolean getSdprSelected() {
		return sdprSelected;
	}
	public void setSdprSelected(Boolean sdprSelected) {
		this.sdprSelected = sdprSelected;
	}
	public Boolean getSdpcSelected() {
		return sdpcSelected;
	}
	public void setSdpcSelected(Boolean sdpcSelected) {
		this.sdpcSelected = sdpcSelected;
	}
	public Boolean getApiSelected() {
		return apiSelected;
	}
	public void setApiSelected(Boolean apiSelected) {
		this.apiSelected = apiSelected;
	}
	public Boolean getBipSelected() {
		return bipSelected;
	}
	public void setBipSelected(Boolean bipSelected) {
		this.bipSelected = bipSelected;
	}
	/**
	 * @return the cbpItems
	 */
	public List<LegendItem> getCbpItems() {
		return cbpItems;
	}
	/**
	 * @param cbpItems the cbpItems to set
	 */
	public void setCbpItems(List<LegendItem> cbpItems) {
		this.cbpItems = cbpItems;
	}
	/**
	 * @return the cbpSelected
	 */
	public Boolean getCbpSelected() {
		return cbpSelected;
	}
	/**
	 * @param cbpSelected the cbpSelected to set
	 */
	public void setCbpSelected(Boolean cbpSelected) {
		this.cbpSelected = cbpSelected;
	}
	

}
