/**
 * 
 */
package com.akuacom.pss2.program.dbp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * the class SCEDBPBidItem
 */
public class SCEDBPBidEntry {
	
	private String serviceAccountNumber;
	private List<BidBlock> bidBlocks =new ArrayList<BidBlock>();
	
	public String getServiceAccountNumber() {
		return serviceAccountNumber;
	}
	public void setServiceAccountNumber(String serviceAccountNumber) {
		this.serviceAccountNumber = serviceAccountNumber;
	}
	public List<BidBlock> getBidBlocks() {
		return bidBlocks;
	}
	public void setBidBlocks(List<BidBlock> bidBlocks) {
		this.bidBlocks = bidBlocks;
	}
	
	public class BidBlock{
		private Date bidStartTime;
		private Date bidEndTime;
		private String startTime;
		private String endTime;
		private Double bidQuantity;
		
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public Double getBidQuantity() {
			return bidQuantity;
		}
		public Date getBidStartTime() {
			return bidStartTime;
		}
		public void setBidStartTime(Date bidStartTime) {
			this.bidStartTime = bidStartTime;
		}
		public Date getBidEndTime() {
			return bidEndTime;
		}
		public void setBidEndTime(Date bidEndTime) {
			this.bidEndTime = bidEndTime;
		}
		public void setBidQuantity(Double bidQuantity) {
			this.bidQuantity = bidQuantity;
		}
	}
}

