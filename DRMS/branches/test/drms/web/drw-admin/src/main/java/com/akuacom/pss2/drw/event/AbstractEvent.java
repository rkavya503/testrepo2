package com.akuacom.pss2.drw.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.drw.core.EventDetail;

/**
 * This class provides common properties of event. And the standard behaviors 
 * like the get and set methods are defined here. 
 * The developer need subclass this abstract class define their own properties and
 * define the actionPerformed method. 
 *
 */
public abstract class AbstractEvent implements Serializable{
	
	public static final int COMMENT_MAX_SIZE = 255;
	private static final long serialVersionUID = 8640292192335427325L;
	
	private String comments;
	private boolean estimated;
	
	public AbstractEvent() {
		Date date = getCurrentClientTime();
		estimated = true;
		rates = "APS";
		product = "BIP";
	}
	
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public boolean isEstimated() {
		return estimated;
	}
	
	public void setEstimated(boolean estimated) {
		this.estimated = estimated;
	}
	
	protected Date getCurrentClientTime() {
		// should return current time on client side
		Date date = new Date();
		final Calendar current = Calendar.getInstance();
		current.setTime(date);
		current.set(Calendar.SECOND, 0);
		current.set(Calendar.MILLISECOND, 0);
		return current.getTime();
	}
	
	//TODO: how to validate comment to avoid Injection risk
	public String validateComment() {
		if(this.getComments()==null) return null;
		
		if(this.getComments().length()>COMMENT_MAX_SIZE){
			return "Comment should not more than 255 characters";
		}
		return null;
	}

	public String validateProduct() {
		if(this.getProduct()==null||this.getProduct().trim().length()==0) return "Product is required.";
		return null;
	}
	
	public String validateBlocks() {
		if(this.getBlockValue()==null||this.getBlockValue().trim().length()==0) return "Block is required.";
		return null;
	}
	
	//****************3 abstract methods
	protected abstract void reportError(String msg);

	public abstract String getProgramName();

	public abstract String getEventID();


	
	// ------------------- setters and getters ------------------//

	private List<EventDetail> eventLocations;

	public List<EventDetail> getEventLocations() {
		if(eventLocations == null){
			eventLocations = new ArrayList<EventDetail>();
		}
		return eventLocations;
	}	
	
	public static String[] BIP_ALL_LABEL = new String[]{
		"P",
		"Q",
		"R",
		"T",
		"U",
		"V"
	};
	
	private String product;
	private List<String> blockList;
	private int[] block;
	private String blockValue;
	
	public String getBlockValue() {
		blockValue = "";
		for(int index=0; index<block.length; index++){
			if(index>0) blockValue += ",";
			blockValue += BIP_ALL_LABEL[block[index]];
		}
		
		return blockValue;
	}

	public void setBlockValue(String blockValue) {
		this.blockValue = blockValue;
	}

	public int[] getBlock() {
		return block;
	}

	public void setBlock(int[] block) {
		this.block = block;
	}

	public String getProduct() {
		if(product==null) product = "BIP";
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public List<SelectItem> getBlockList() {
		if(blockList==null){
			initBlockList();
		}
		
		List<SelectItem> items =new ArrayList<SelectItem>(blockList.size());
	    for (int i = 0; i < blockList.size(); i++) {
	    	   items.add(new SelectItem(i,blockList.get(i)));
	    }
	    return items;
		
	}

	public void setBlockList(List<String> blockList) {
		this.blockList = blockList;
	}

	
	private void initBlockList() {
		if(blockList==null){
			blockList = new ArrayList<String>();
			blockList.addAll(Arrays.asList(BIP_ALL_LABEL));
		}
	}


	/**
	 * end time of an event
	 */
	
	//***********************SDP
	
	private String rates;

	public String getRates() {
		return rates;
	}

	public void setRates(String rates) {
		this.rates = rates;
	}
	
	public String validateRate() {
		
		if(this.getRates()==null|| this.getRates().trim().length()==0) return "Rate is required.";
		return null;
	}
	
}
