package com.akuacom.pss2.drw.event.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.drw.admin.FDUtils;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.utils.lang.DateUtil;

public class BipHistoryEvents extends AbstractHistoryEvents{
	private static final long serialVersionUID = 338011784425585088L;
	private List<String> blockList;
	private int[] block;
	private String blockValue;
	
	public static String[] BIP_ALL_LABEL = new String[]{
		"P",
		"Q",
		"R",
		"T",
		"U",
		"V"
	};
	private String productFilter;
	

	public List<String> getBlockValue() {
		List<String> blocks = new ArrayList<String>();
		for(int index=0; index<block.length; index++){
			blocks.add(BIP_ALL_LABEL[block[index]]);
		}
		
		return blocks;
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


	public List<SelectItem> getBlockList() {
		if(blockList==null){
			blockList = new ArrayList<String>();
			blockList.addAll(Arrays.asList(BIP_ALL_LABEL));
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

	public List<EventDetail> doGetContents() {
		List<String> blocks = getBlockValue();
		List<String> products = getProducts();
		
		List<EventDetail> lists = getEvtManager().getHistoryEvents(getProgramName(), "start".equals(getFilterBy()), getStartTime(), getEndTime(), blocks,products);
		return lists;
	}
	
	@Override
	protected String getExportContent() throws Exception{
		List<EventDetail> contents =  doGetContents();
 		StringBuffer exportContent = new StringBuffer();
 		
 		
 		// append title for SDP event
 		exportContent.append("Start Date&Time, End Date&Time,Product,Blocks,Comments"+"\n\t");
 		//append the first row 
 		for(EventDetail event : contents){
			exportContent.append(" "+DateUtil.format(event.getEvent().getStartTime(),"yyyy-MM-dd HH:mm:ss") +","
					+ " "+DateUtil.format(event.getActualEndTime(),"yyyy-MM-dd HH:mm:ss")+ ","
					+ event.getEvent().getProduct() + ","
					+ event.getBlockNames() + ","
					+ FDUtils.filterSpecialCharacters(event.getEvent().getComment()));
			exportContent.append("\n\t");
 		}
	 
		exportContent.append("\n\t");
	  
		return exportContent.toString();
	}

	/**
	 * @param productFilter the productFilter to set
	 */
	public void setProductFilter(String productFilter) {
		this.productFilter = productFilter;
	}

	/**
	 * @return the productFilter
	 */
	public String getProductFilter() {
		return productFilter;
	}
	public List<String> getProducts() {
		List<String> products = new ArrayList<String>();
		if(productFilter==null||productFilter.equalsIgnoreCase("")||productFilter.equalsIgnoreCase("ALL")){
			products.add("BIP");
			products.add("BIP-15");
			products.add("BIP-30");
			products.add("I6-30");
		}else{
			products.add(productFilter);
		}
		
		return products;
	}
}
