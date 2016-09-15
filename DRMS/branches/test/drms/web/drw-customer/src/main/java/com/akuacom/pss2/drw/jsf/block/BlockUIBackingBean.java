package com.akuacom.pss2.drw.jsf.block;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.LocationCache;
import com.akuacom.pss2.drw.util.DRWUtil;

public class BlockUIBackingBean implements Serializable {
	private static final long serialVersionUID = 9088683096965113883L;
	
	private List<String> scecBlocks = new ArrayList<String>();
	private List<String> scenBlocks = new ArrayList<String>();
	private List<String> scnwBlocks = new ArrayList<String>();
	private List<String> scewBlocks = new ArrayList<String>();
	private List<String> scldBlocks = new ArrayList<String>();
	private List<String> schdBlocks = new ArrayList<String>();
	
	public BlockUIBackingBean(){
		initialize();
	}
	
	private void initialize(){
		LocationCache locationCache = EventCache.getInstance().getLocationCache();
		if(locationCache!=null){
			List<LocationCache> slapCache = locationCache.getSlapLocations();
			for(LocationCache slap:slapCache){
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEC)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						scecBlocks.add(block.getBlock());
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEN)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						scenBlocks.add(block.getBlock());
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCNW)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						scnwBlocks.add(block.getBlock());
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEW)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						scewBlocks.add(block.getBlock());
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCLD)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						scldBlocks.add(block.getBlock());
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCHD)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						schdBlocks.add(block.getBlock());
					}
				}
			}
		}
		
		scecBlocks=DRWUtil.sortListAsNumber(scecBlocks);
		schdBlocks=DRWUtil.sortListAsNumber(schdBlocks);
		scldBlocks=DRWUtil.sortListAsNumber(scldBlocks);
		scewBlocks=DRWUtil.sortListAsNumber(scewBlocks);
		scnwBlocks=DRWUtil.sortListAsNumber(scnwBlocks);
		scenBlocks=DRWUtil.sortListAsNumber(scenBlocks);
	}

	/**
	 * @return the scecBlocks
	 */
	public List<String> getScecBlocks() {
		return scecBlocks;
	}

	/**
	 * @param scecBlocks the scecBlocks to set
	 */
	public void setScecBlocks(List<String> scecBlocks) {
		this.scecBlocks = scecBlocks;
	}

	/**
	 * @return the scenBlocks
	 */
	public List<String> getScenBlocks() {
		return scenBlocks;
	}

	/**
	 * @param scenBlocks the scenBlocks to set
	 */
	public void setScenBlocks(List<String> scenBlocks) {
		this.scenBlocks = scenBlocks;
	}

	/**
	 * @return the scnwBlocks
	 */
	public List<String> getScnwBlocks() {
		return scnwBlocks;
	}

	/**
	 * @param scnwBlocks the scnwBlocks to set
	 */
	public void setScnwBlocks(List<String> scnwBlocks) {
		this.scnwBlocks = scnwBlocks;
	}

	/**
	 * @return the scewBlocks
	 */
	public List<String> getScewBlocks() {
		return scewBlocks;
	}

	/**
	 * @param scewBlocks the scewBlocks to set
	 */
	public void setScewBlocks(List<String> scewBlocks) {
		this.scewBlocks = scewBlocks;
	}

	/**
	 * @return the scldBlocks
	 */
	public List<String> getScldBlocks() {
		return scldBlocks;
	}

	/**
	 * @param scldBlocks the scldBlocks to set
	 */
	public void setScldBlocks(List<String> scldBlocks) {
		this.scldBlocks = scldBlocks;
	}

	/**
	 * @return the schdBlocks
	 */
	public List<String> getSchdBlocks() {
		return schdBlocks;
	}

	/**
	 * @param schdBlocks the schdBlocks to set
	 */
	public void setSchdBlocks(List<String> schdBlocks) {
		this.schdBlocks = schdBlocks;
	}
	
	
	
}
