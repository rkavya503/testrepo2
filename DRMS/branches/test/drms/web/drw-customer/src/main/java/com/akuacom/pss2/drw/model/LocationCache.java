package com.akuacom.pss2.drw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.core.Location;

public class LocationCache implements Serializable {

	private static final long serialVersionUID = -9176501866967600273L;
	//mapping for database table 'location'
	private long uuid;
	private String type;
	private String number;
	private String name;
	private long parentID;
	private String block;
	
	//slap contains abanks and blocks
	private List<LocationCache> allLocations = new ArrayList<LocationCache>();
	private List<LocationCache> slapLocations = new ArrayList<LocationCache>();
	private List<LocationCache> abankLocations = new ArrayList<LocationCache>();
	private List<LocationCache> blockLocations = new ArrayList<LocationCache>();

	/**
	 * @return the uuid
	 */
	public long getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(long uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parentID
	 */
	public long getParentID() {
		return parentID;
	}
	/**
	 * @param parentID the parentID to set
	 */
	public void setParentID(long parentID) {
		this.parentID = parentID;
	}
	/**
	 * @return the block
	 */
	public String getBlock() {
		return block;
	}
	/**
	 * @param block the block to set
	 */
	public void setBlock(String block) {
		this.block = block;
	}
	/**
	 * @return the allLocations
	 */
	public List<LocationCache> getAllLocations() {
		return allLocations;
	}
	/**
	 * @param allLocations the allLocations to set
	 */
	public void setAllLocations(List<LocationCache> allLocations) {
		this.allLocations = allLocations;
	}
	/**
	 * @return the slapLocations
	 */
	public List<LocationCache> getSlapLocations() {
		return slapLocations;
	}
	/**
	 * @param slapLocations the slapLocations to set
	 */
	public void setSlapLocations(List<LocationCache> slapLocations) {
		this.slapLocations = slapLocations;
	}
	/**
	 * @return the abankLocations
	 */
	public List<LocationCache> getAbankLocations() {
		return abankLocations;
	}
	/**
	 * @param abankLocations the abankLocations to set
	 */
	public void setAbankLocations(List<LocationCache> abankLocations) {
		this.abankLocations = abankLocations;
	}
	/**
	 * @return the blockLocations
	 */
	public List<LocationCache> getBlockLocations() {
		return blockLocations;
	}
	/**
	 * @param blockLocations the blockLocations to set
	 */
	public void setBlockLocations(List<LocationCache> blockLocations) {
		this.blockLocations = blockLocations;
	}
	
	
	private static String SLAP_TYPE="SLAP";
	private static String ABANK_TYPE="ABank";
	
	//1 level: Locations
	//-------> 2 level: all slap
	//----------------> 3 level: abanks which in the slap 
	//----------------> 3 level: blocks which in the slap 					
	//-------> 2 level: all abank
	//-------> 2 level: all block
	public void buildLocationCache(List<Location> locations){
		if(allLocations!=null){
			allLocations.clear();
			slapLocations.clear();
			abankLocations.clear();
			blockLocations.clear();
			
			//build all locations
			for(Location location:locations){
				LocationCache locationCache = new LocationCache();
				String block = location.getBlock();
				if(block!=null){
					locationCache.setBlock(block);
				}
				locationCache.setType(location.getType());
				locationCache.setUuid(location.getID());
				locationCache.setNumber(location.getNumber());
				Long parentID = location.getParentID();
				if(parentID!=null){
					locationCache.setParentID(parentID);
				}
				locationCache.setName(location.getName());
				allLocations.add(locationCache);
			}
			
			//build slap locations
			for(LocationCache location:allLocations){
				if(SLAP_TYPE.equalsIgnoreCase(location.getType())){
					slapLocations.add(location);
				}
			}
			
			//build abank locations
			for(LocationCache location:allLocations){
				if(ABANK_TYPE.equalsIgnoreCase(location.getType())){
					abankLocations.add(location);
				}
			}
			
			//build block locations
			for(LocationCache location:allLocations){
				String block = location.getBlock();
				if(block!=null&&(!block.equalsIgnoreCase(""))){
					blockLocations.add(location);
				}
			}
			
			for(LocationCache slapLocation:slapLocations){
				slapLocation.getBlockLocations().clear();
				slapLocation.getAbankLocations().clear();
				long slapID = slapLocation.getUuid();
				for(LocationCache abank:abankLocations){
					long abankParentID= abank.getParentID();
					if(abankParentID == slapID){
						slapLocation.getAbankLocations().add(abank);
					}
				}
				for(LocationCache block:blockLocations){
					long parentID= block.getParentID();
					if(parentID == slapID){
						slapLocation.getBlockLocations().add(block);
					}
				}
			}
		}
	}
}
