package com.honeywell.dras.vtn.api.report;

public class ItemBase {

	private String itemDescription;
	private String itemUnits;
	private String siScaleCode;
	
	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}
	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	/**
	 * @return the itemUnits
	 */
	public String getItemUnits() {
		return itemUnits;
	}
	/**
	 * @param itemUnits the itemUnits to set
	 */
	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}
	/**
	 * @return the siScaleCode
	 */
	public String getSiScaleCode() {
		return siScaleCode;
	}
	/**
	 * @param siScaleCode the siScaleCode to set
	 */
	public void setSiScaleCode(String siScaleCode) {
		this.siScaleCode = siScaleCode;
	}
}
