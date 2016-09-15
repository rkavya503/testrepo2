package com.honeywell.dras.vtn.api.opt;

public class TransportInterface {
	
	private String pointOfDelivery;
	private String pointOfReceipt;
	
	public String getPointOfReceipt() {
		return pointOfReceipt;
	}
	public void setPointOfReceipt(String pointOfReceipt) {
		this.pointOfReceipt = pointOfReceipt;
	}
	public String getPointOfDelivery() {
		return pointOfDelivery;
	}
	public void setPointOfDelivery(String pointOfDelivery) {
		this.pointOfDelivery = pointOfDelivery;
	}

}
