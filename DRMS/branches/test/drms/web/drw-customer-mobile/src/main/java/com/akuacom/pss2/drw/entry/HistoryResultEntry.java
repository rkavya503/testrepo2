package com.akuacom.pss2.drw.entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;

@XmlRootElement
public class HistoryResultEntry {
	private boolean isRTP = false;
	private String product;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String blocks;
	
	private String rtpDate;
	private String rtpPricing;
	/**
	 * @return the isRTP
	 */
	public boolean isRTP() {
		return isRTP;
	}
	/**
	 * @param isRTP the isRTP to set
	 */
	public void setRTP(boolean isRTP) {
		this.isRTP = isRTP;
	}
	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the blocks
	 */
	public String getBlocks() {
		return blocks;
	}
	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
	/**
	 * @return the rtpDate
	 */
	public String getRtpDate() {
		return rtpDate;
	}
	/**
	 * @param rtpDate the rtpDate to set
	 */
	public void setRtpDate(String rtpDate) {
		this.rtpDate = rtpDate;
	}
	/**
	 * @return the rtpPricing
	 */
	public String getRtpPricing() {
		return rtpPricing;
	}
	/**
	 * @param rtpPricing the rtpPricing to set
	 */
	public void setRtpPricing(String rtpPricing) {
		this.rtpPricing = rtpPricing;
	}
	
	public void setWeatherValue(WeatherValue wv){
		if(wv!=null){
			isRTP=true;
			if(wv.getDate()!=null){
				rtpDate =(new SimpleDateFormat("MMMM dd, yyyy")).format(wv.getDate());
			}
			if(wv.getPricingCategory()!=null&&(!wv.getPricingCategory().equalsIgnoreCase(""))){
				rtpPricing=wv.getPricingCategory();
			}else{
				rtpPricing="N/A";
			}
		}	
	}
	
	public void setEventValue(EventValue ev){
		if(ev!=null){
			isRTP=false;
			product = ev.getProduct();
			
			if(ev.getStartTime()!=null){
				startDate = (new SimpleDateFormat("MM/dd/yyyy")).format(ev.getStartTime());
				startTime = (new SimpleDateFormat("hh:mma")).format(ev.getStartTime());
			}
			if(ev.getTbdFlag()!=null&&(!ev.getTbdFlag().equalsIgnoreCase(""))){
				if(ev.getTbdFlag().equalsIgnoreCase("1")){
					endDate="TBD";
					endTime="TBD";
				}
			}
			if(ev.getEndTime()!=null){
				if(!"TBD".equalsIgnoreCase(endDate)){
					endDate = (new SimpleDateFormat("MM/dd/yyyy")).format(ev.getEndTime());	
				}
				if(!"TBD".equalsIgnoreCase(endTime)){
					endTime = (new SimpleDateFormat("hh:mma")).format(ev.getEndTime());	
				}
			}
			
			List<String> list = ev.getBlocks();
			blocks=null;
			if(list.size()>0){
					//API
					String str = list.get(0);
					if(DRWUtil.toNumber(str)!=null){
						//number
						List<Number> listN = new ArrayList<Number>();
						if (list!=null&&list.size()>0) {
							for (String b:list){
								
								Number num = DRWUtil.toNumber(b);
								if(num!=null){
									listN.add(num);
								}
							}
						}
						DRWUtil.sortNumberList(listN);
						for (Number b:listN){
							if(b!=null){
								if (blocks==null)
									blocks=String.valueOf(b);
								else
									blocks+=", "+b;
							}
						}
					}else{
						//String
						blocks = getBlockS(ev);
					}
			}else{//BIP
				blocks =  ev.getBlock();
			}	
			//DRMS-7470
			if("API".equalsIgnoreCase(ev.getProgramClass())){
				Date compareDate = new Date("2012/01/01 00:00:00");
				if(ev.getStartTime().before(compareDate)){
					blocks="All Blocks";
				}
			}
			else if("BIP".equalsIgnoreCase(ev.getProgramClass()))
			{

					CFEventManager cfEventManager = DRWUtil.getCFEventManager();
					List<String> availableBlocks = cfEventManager.getAllBlock();
					boolean isAll = DRWUtil.isAllBlocks(ev.getBlocks(), availableBlocks);
					if(isAll)
						blocks =  "All Blocks";
				
			}
			if(blocks!=null&&blocks.endsWith(", ")){
				blocks=blocks.substring(0, blocks.length()-2);
			}else if(blocks==null||blocks.equalsIgnoreCase("")){
				blocks = "All Blocks";
			}
		}
	}
	
	public String getBlockS(EventValue ev) {
		String blockS=null;
		List<String> list = ev.getBlocks();
		list = DRWUtil.trimList(list);
		if (list!=null&&list.size()>0) {
			DRWUtil.sortList(list);
			blockS=null;
			for (String b:list){
				if((b!=null)&&(!b.trim().equalsIgnoreCase(""))){
					if (blockS==null)
						blockS=b;
					else{
						if(b.toString().equalsIgnoreCase("")){
							
						}else{
							blockS+=", "+b;	
						}
						
					}
						
						
				}
			}
		}else{
			blockS = ev.getBlock();
		}
		return blockS;
	}
}
