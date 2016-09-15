package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.program.dbp.BidEntry;

public class EventBidSummary implements Serializable {
	private static final long serialVersionUID = 3875167913248302959L;
	
	private String participantName;
	private String accountNumber;
	private List<BidEntry> bids;
	private String bidState;
	
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public List<BidEntry> getBids() {
		if(bids==null)
			bids = new ArrayList<BidEntry>();
		return bids;
	}
	public void setBids(List<BidEntry> bids) {
		this.bids = bids;
	}
	public String getBidState() {
		return bidState;
	}
	public void setBidState(String bidState) {
		this.bidState = bidState;
	}
	
}
