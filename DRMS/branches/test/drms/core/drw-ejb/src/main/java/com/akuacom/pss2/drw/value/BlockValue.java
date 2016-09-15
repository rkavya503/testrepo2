/**
 * 
 */
package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * the class EventVal
 */
public class BlockValue implements Serializable {

	private static final long serialVersionUID = 2080205949837070210L;

	String abankNumber;//mapped to primary key
	String blockNumber;
//	private List<KMLValue> kmls;
	private List<String> kmls;
	private String kml;

	/**
	 * @return the abankNumber
	 */
	public String getAbankNumber() {
		return abankNumber;
	}
	/**
	 * @param abankNumber the abankNumber to set
	 */
	public void setAbankNumber(String abankNumber) {
		this.abankNumber = abankNumber;
	}
	/**
	 * @return the kmls
	 */
	public List<String> getKmls() {
		if(kmls==null){
			kmls = new ArrayList<String>();
		}
		return kmls;
	}
	/**
	 * @param kmls the kmls to set
	 */
	public void setKmls(List<String> kmls) {
		this.kmls = kmls;
	}
	/**
	 * @return the kml
	 */
	public String getKml() {
		return kml;
	}
	/**
	 * @param kml the kml to set
	 */
	public void setKml(String kml) {
		this.kml = kml;
	}
	/**
	 * @return the blockNumber
	 */
	public String getBlockNumber() {
		return blockNumber;
	}
	/**
	 * @param blockNumber the blockNumber to set
	 */
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	
}
