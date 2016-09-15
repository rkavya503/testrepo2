package com.akuacom.pss2.price.australia;

import java.io.Serializable;
import java.util.Date;

public class PriceRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private  Double price;
	private  Date   time;
	private  String location;
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	@Override
	public String toString() {
		return "PriceRecord [price=" + price + ", time=" + time + ", location="
				+ location + "]";
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	

}
