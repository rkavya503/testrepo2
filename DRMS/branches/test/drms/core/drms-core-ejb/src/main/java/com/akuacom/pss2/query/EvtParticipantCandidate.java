package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;


public class EvtParticipantCandidate  implements EventEnrollingItem, Serializable{
	
	private static final long serialVersionUID = 7477006117822688129L;
	
	private boolean optOutByAggregation = false;
		
	private String UUID;
	
	private String program;
	
	private double registerShed =0;
	
	private String participantName;

	private String account;
	
	private boolean participantOptOut;
	
	private boolean programOptOut;
	
	private boolean ancestorOptOut;
	
	private boolean ancestorProgramOptOut;
	
	private int clientCount = 0;
	
	private String parent;
	
	private String aggPath;
	
	private boolean client = false;
	
	private String aBank;

	private String slap;
	
	private String substation;
	
	private String blockNumber;
	
	public String getAggPath() {
		return aggPath;
	}

	public void setAggPath(String aggPath) {
		this.aggPath = aggPath;
	}

	private List<EvtClientCandidate> clients;
	
	/** client is online and is not opt out */ 
	private int validClientCount=0;
	
	
	public EvtParticipantCandidate() {
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public double getRegisterShed() {
		return registerShed; 
	}

	public void setRegisterShed(double registerShed) {
		this.registerShed = registerShed;
	}

	public double getAvailableShed() {
		if(clientCount ==0) return 0;
		return getRegisterShed() * validClientCount /clientCount;
	}
	
	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isParticipantOptOut() {
		return participantOptOut;
	}

	public void setParticipantOptOut(boolean participantOptOut) {
		this.participantOptOut = participantOptOut;
	}

	public boolean isProgramOptOut() {
		return programOptOut;
	}

	public void setProgramOptOut(boolean programOptOut) {
		this.programOptOut = programOptOut;
	}
	
	
	public boolean isAncestorOptOut() {
		return ancestorOptOut;
	}

	public void setAncestorOptOut(boolean ancestorOptOut) {
		this.ancestorOptOut = ancestorOptOut;
	}

	public boolean isAncestorProgramOptOut() {
		return ancestorProgramOptOut;
	}

	public void setAncestorProgramOptOut(boolean ancestorProgramOptOut) {
		this.ancestorProgramOptOut = ancestorProgramOptOut;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}

	public int getValidClientCount() {
		return validClientCount;
	}

	public void setValidClientCount(int validClientCount) {
		this.validClientCount = validClientCount;
	}
	
	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public List<EvtClientCandidate> getClients() {
		if(clients==null)
			clients = new ArrayList<EvtClientCandidate>();
		return clients;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}
	
	public boolean isLegiable(){
		return !this.isProgramOptOut() && !this.isParticipantOptOut()
		&& this.getClientCount()>0 && this.getClients().size()>0
		// if one of its ancestor in aggregation is opted out, opt it out
		&& (!optOutByAggregation || !this.isAncestorOptOut() && !this.isAncestorProgramOptOut());
	}
	
	
	private String shedType;
	private double shedHourIndex0;//DEFINE AS STRING TO AVOID NULL VALUE EXCEPTION
	private double shedHourIndex1;
	private double shedHourIndex2;
	private double shedHourIndex3;
	private double shedHourIndex4;
	private double shedHourIndex5;
	private double shedHourIndex6;
	private double shedHourIndex7;
	private double shedHourIndex8;
	private double shedHourIndex9;
	private double shedHourIndex10;
	private double shedHourIndex11;
	private double shedHourIndex12;
	private double shedHourIndex13;
	private double shedHourIndex14;
	private double shedHourIndex15;
	private double shedHourIndex16;
	private double shedHourIndex17;
	private double shedHourIndex18;
	private double shedHourIndex19;
	private double shedHourIndex20;
	private double shedHourIndex21;
	private double shedHourIndex22;
	private double shedHourIndex23;

	/**
	 * @return the shedType
	 */
	public String getShedType() {
		return shedType;
	}

	/**
	 * @param shedType the shedType to set
	 */
	public void setShedType(String shedType) {
		this.shedType = shedType;
	}

	/**
	 * @return the shedHourIndex0
	 */
	public double getShedHourIndex0() {
		return shedHourIndex0;
	}

	/**
	 * @param shedHourIndex0 the shedHourIndex0 to set
	 */
	public void setShedHourIndex0(double shedHourIndex0) {
		this.shedHourIndex0 = shedHourIndex0;
	}

	/**
	 * @return the shedHourIndex1
	 */
	public double getShedHourIndex1() {
		return shedHourIndex1;
	}

	/**
	 * @param shedHourIndex1 the shedHourIndex1 to set
	 */
	public void setShedHourIndex1(double shedHourIndex1) {
		this.shedHourIndex1 = shedHourIndex1;
	}

	/**
	 * @return the shedHourIndex2
	 */
	public double getShedHourIndex2() {
		return shedHourIndex2;
	}

	/**
	 * @param shedHourIndex2 the shedHourIndex2 to set
	 */
	public void setShedHourIndex2(double shedHourIndex2) {
		this.shedHourIndex2 = shedHourIndex2;
	}

	/**
	 * @return the shedHourIndex3
	 */
	public double getShedHourIndex3() {
		return shedHourIndex3;
	}

	/**
	 * @param shedHourIndex3 the shedHourIndex3 to set
	 */
	public void setShedHourIndex3(double shedHourIndex3) {
		this.shedHourIndex3 = shedHourIndex3;
	}

	/**
	 * @return the shedHourIndex4
	 */
	public double getShedHourIndex4() {
		return shedHourIndex4;
	}

	/**
	 * @param shedHourIndex4 the shedHourIndex4 to set
	 */
	public void setShedHourIndex4(double shedHourIndex4) {
		this.shedHourIndex4 = shedHourIndex4;
	}

	/**
	 * @return the shedHourIndex5
	 */
	public double getShedHourIndex5() {
		return shedHourIndex5;
	}

	/**
	 * @param shedHourIndex5 the shedHourIndex5 to set
	 */
	public void setShedHourIndex5(double shedHourIndex5) {
		this.shedHourIndex5 = shedHourIndex5;
	}

	/**
	 * @return the shedHourIndex6
	 */
	public double getShedHourIndex6() {
		return shedHourIndex6;
	}

	/**
	 * @param shedHourIndex6 the shedHourIndex6 to set
	 */
	public void setShedHourIndex6(double shedHourIndex6) {
		this.shedHourIndex6 = shedHourIndex6;
	}

	/**
	 * @return the shedHourIndex7
	 */
	public double getShedHourIndex7() {
		return shedHourIndex7;
	}

	/**
	 * @param shedHourIndex7 the shedHourIndex7 to set
	 */
	public void setShedHourIndex7(double shedHourIndex7) {
		this.shedHourIndex7 = shedHourIndex7;
	}

	/**
	 * @return the shedHourIndex8
	 */
	public double getShedHourIndex8() {
		return shedHourIndex8;
	}

	/**
	 * @param shedHourIndex8 the shedHourIndex8 to set
	 */
	public void setShedHourIndex8(double shedHourIndex8) {
		this.shedHourIndex8 = shedHourIndex8;
	}

	/**
	 * @return the shedHourIndex9
	 */
	public double getShedHourIndex9() {
		return shedHourIndex9;
	}

	/**
	 * @param shedHourIndex9 the shedHourIndex9 to set
	 */
	public void setShedHourIndex9(double shedHourIndex9) {
		this.shedHourIndex9 = shedHourIndex9;
	}

	/**
	 * @return the shedHourIndex10
	 */
	public double getShedHourIndex10() {
		return shedHourIndex10;
	}

	/**
	 * @param shedHourIndex10 the shedHourIndex10 to set
	 */
	public void setShedHourIndex10(double shedHourIndex10) {
		this.shedHourIndex10 = shedHourIndex10;
	}

	/**
	 * @return the shedHourIndex11
	 */
	public double getShedHourIndex11() {
		return shedHourIndex11;
	}

	/**
	 * @param shedHourIndex11 the shedHourIndex11 to set
	 */
	public void setShedHourIndex11(double shedHourIndex11) {
		this.shedHourIndex11 = shedHourIndex11;
	}

	/**
	 * @return the shedHourIndex12
	 */
	public double getShedHourIndex12() {
		return shedHourIndex12;
	}

	/**
	 * @param shedHourIndex12 the shedHourIndex12 to set
	 */
	public void setShedHourIndex12(double shedHourIndex12) {
		this.shedHourIndex12 = shedHourIndex12;
	}

	/**
	 * @return the shedHourIndex13
	 */
	public double getShedHourIndex13() {
		return shedHourIndex13;
	}

	/**
	 * @param shedHourIndex13 the shedHourIndex13 to set
	 */
	public void setShedHourIndex13(double shedHourIndex13) {
		this.shedHourIndex13 = shedHourIndex13;
	}

	/**
	 * @return the shedHourIndex14
	 */
	public double getShedHourIndex14() {
		return shedHourIndex14;
	}

	/**
	 * @param shedHourIndex14 the shedHourIndex14 to set
	 */
	public void setShedHourIndex14(double shedHourIndex14) {
		this.shedHourIndex14 = shedHourIndex14;
	}

	/**
	 * @return the shedHourIndex15
	 */
	public double getShedHourIndex15() {
		return shedHourIndex15;
	}

	/**
	 * @param shedHourIndex15 the shedHourIndex15 to set
	 */
	public void setShedHourIndex15(double shedHourIndex15) {
		this.shedHourIndex15 = shedHourIndex15;
	}

	/**
	 * @return the shedHourIndex16
	 */
	public double getShedHourIndex16() {
		return shedHourIndex16;
	}

	/**
	 * @param shedHourIndex16 the shedHourIndex16 to set
	 */
	public void setShedHourIndex16(double shedHourIndex16) {
		this.shedHourIndex16 = shedHourIndex16;
	}

	/**
	 * @return the shedHourIndex17
	 */
	public double getShedHourIndex17() {
		return shedHourIndex17;
	}

	/**
	 * @param shedHourIndex17 the shedHourIndex17 to set
	 */
	public void setShedHourIndex17(double shedHourIndex17) {
		this.shedHourIndex17 = shedHourIndex17;
	}

	/**
	 * @return the shedHourIndex18
	 */
	public double getShedHourIndex18() {
		return shedHourIndex18;
	}

	/**
	 * @param shedHourIndex18 the shedHourIndex18 to set
	 */
	public void setShedHourIndex18(double shedHourIndex18) {
		this.shedHourIndex18 = shedHourIndex18;
	}

	/**
	 * @return the shedHourIndex19
	 */
	public double getShedHourIndex19() {
		return shedHourIndex19;
	}

	/**
	 * @param shedHourIndex19 the shedHourIndex19 to set
	 */
	public void setShedHourIndex19(double shedHourIndex19) {
		this.shedHourIndex19 = shedHourIndex19;
	}

	/**
	 * @return the shedHourIndex20
	 */
	public double getShedHourIndex20() {
		return shedHourIndex20;
	}

	/**
	 * @param shedHourIndex20 the shedHourIndex20 to set
	 */
	public void setShedHourIndex20(double shedHourIndex20) {
		this.shedHourIndex20 = shedHourIndex20;
	}

	/**
	 * @return the shedHourIndex21
	 */
	public double getShedHourIndex21() {
		return shedHourIndex21;
	}

	/**
	 * @param shedHourIndex21 the shedHourIndex21 to set
	 */
	public void setShedHourIndex21(double shedHourIndex21) {
		this.shedHourIndex21 = shedHourIndex21;
	}

	/**
	 * @return the shedHourIndex22
	 */
	public double getShedHourIndex22() {
		return shedHourIndex22;
	}

	/**
	 * @param shedHourIndex22 the shedHourIndex22 to set
	 */
	public void setShedHourIndex22(double shedHourIndex22) {
		this.shedHourIndex22 = shedHourIndex22;
	}

	/**
	 * @return the shedHourIndex23
	 */
	public double getShedHourIndex23() {
		return shedHourIndex23;
	}

	/**
	 * @param shedHourIndex23 the shedHourIndex23 to set
	 */
	public void setShedHourIndex23(double shedHourIndex23) {
		this.shedHourIndex23 = shedHourIndex23;
	}

	public String getaBank() {
		return aBank;
	}

	public void setaBank(String aBank) {
		this.aBank = aBank;
	}

	public String getSlap() {
		return slap;
	}

	public void setSlap(String slap) {
		this.slap = slap;
	}

	public String getSubstation() {
		return substation;
	}

	public void setSubstation(String substation) {
		this.substation = substation;
	}

	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	@Override
	public List<EvtParticipantCandidate> getLegibleEventParticipants() {
		if(this.isLegiable())
			return Arrays.asList(this);
		return Collections.emptyList();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UUID == null) ? 0 : UUID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvtParticipantCandidate other = (EvtParticipantCandidate) obj;
		if (UUID == null) {
			if (other.UUID != null)
				return false;
		} else if (!UUID.equals(other.UUID))
			return false;
		return true;
	}
	
}
