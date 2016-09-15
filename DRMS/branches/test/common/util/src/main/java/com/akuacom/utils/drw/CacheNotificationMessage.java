/**
 * 
 */
package com.akuacom.utils.drw;

import java.io.Serializable;

/**
 *
 */
public class CacheNotificationMessage implements Serializable {

	private static final long serialVersionUID = -6534624606158976356L;
	
	String programName;
	String product;
	String uUID;
	boolean active;
	
	public CacheNotificationMessage(){}
	
	public CacheNotificationMessage(String programName, String product, boolean active, String uUID) {
		this.programName=programName;
		this.active=active;
		this.product=product;
		this.uUID = uUID;
	}

	public String getuUID() {
		return uUID;
	}

	public void setuUID(String uUID) {
		this.uUID = uUID;
	}
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
}
