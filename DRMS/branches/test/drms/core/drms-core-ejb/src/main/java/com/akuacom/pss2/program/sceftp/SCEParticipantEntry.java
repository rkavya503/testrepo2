/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scala.actors.threadpool.Arrays;

/**
 * the class SCEParticipantEntry
 * 
 */
public class SCEParticipantEntry {
	
	//define utility program name
//	public static final String SERVICE_PLAN_PA_RTP="PA-RTP";
//	public static final String PROGRAM_RTP_AGRICULTURAL="RTP Agricultural";
//	public static final String PROGRAM_RTP_2K="RTP <2K";
//	public static final String PROGRAM_RTP_2K_50K="RTP 2K-50K";
//	public static final String PROGRAM_RTP_50K="RTP >50K";
	public static final String PROGRAM_RTP="RTP";
	public static final String PROGRAM_BIP="BIP";
	
	public static final String PROGRAM_CPP="SAI";
	public static final String PROGRAM_DBP="DBP DA";
	
	public static final String[] PRGORAMS_CBP={"CBP 1-4 DO", "CBP 2-6 DO", "CBP 4-8 DO", "CBP 1-4 DA", "CBP 2-6 DA", "CBP 4-8 DA"};
	
	private String serviceAccount;
	private String customerName;
	private String serviceStreetAddress;
	private String serviceCityName;
	private String zip;
	private String servicePlan;
	private String aBank;
	private String slap;
	private String pnode;
	private Date rateEffectiveDate;
	private boolean directAccessParticipant;
	
	private boolean dbpParticipant;
	private boolean sdpParticipant;
	private boolean cppParticipant;
	private boolean bipParticipant;
	
	private boolean cbpParticipant;
//	private String cbpProgram;
	
	private boolean apiParticipant;
	private boolean slrpParticipant;
	
	private boolean drcParticipant;
	private String drcProgram;
	
	private boolean autoDRParticipant;
	
	private boolean rtpParticipant;
	private double rtpVoltag;
	
	private String bcdRepName;
	private Date autoDrProfileStartDate;
	
	private int programOption;//0 or 15 or 30
	private String substation;
	private String blockNumber;
	@SuppressWarnings("unchecked")
	public List<String> getUtilityPrograms(){
		List<String> programs=new ArrayList<String>();
		if (this.dbpParticipant)
			programs.add(PROGRAM_DBP);
		if (this.cppParticipant)
			programs.add(PROGRAM_CPP);
		
		if (this.cbpParticipant)
			programs.addAll(Arrays.asList(PRGORAMS_CBP));
		
		if (this.drcParticipant)
			programs.add(this.drcProgram);
		if (this.rtpParticipant)
			programs.add(PROGRAM_RTP);
		if(this.bipParticipant)
			programs.add(PROGRAM_BIP);
		
		return programs;
	}
	
	public String getServiceAccount() {
		return serviceAccount;
	}
	public void setServiceAccount(String serviceAccount) {
		this.serviceAccount = serviceAccount;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		if (customerName!=null && customerName.trim().length()!=0) {
			customerName = customerName.trim();
		
			Pattern p=Pattern.compile("^\"([^\"]*)\"$");
			Matcher m=p.matcher(customerName);
			if (m.find())
				this.customerName=m.group(1);
			else
				this.customerName = customerName;
		}
	}
	public String getServiceStreetAddress() {
		return serviceStreetAddress;
	}
	public void setServiceStreetAddress(String serviceStreetAddress) {
		this.serviceStreetAddress = serviceStreetAddress;
	}
	public String getServiceCityName() {
		return serviceCityName;
	}
	public void setServiceCityName(String serviceCityName) {
		this.serviceCityName = serviceCityName;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getServicePlan() {
		return servicePlan;
	}
	public void setServicePlan(String servicePlan) {
		this.servicePlan = servicePlan;
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
	public String getPnode() {
		return pnode;
	}
	public void setPnode(String pnode) {
		this.pnode = pnode;
	}
	public Date getRateEffectiveDate() {
		return rateEffectiveDate;
	}
	public void setRateEffectiveDate(Date rateEffectiveDate) {
		this.rateEffectiveDate = rateEffectiveDate;
	}
	public boolean isDirectAccessParticipant() {
		return directAccessParticipant;
	}
	public void setDirectAccessParticipant(boolean directAccessParticipant) {
		this.directAccessParticipant = directAccessParticipant;
	}
	public boolean isDbpParticipant() {
		return dbpParticipant;
	}
	public void setDbpParticipant(boolean dbpParticipant) {
		this.dbpParticipant = dbpParticipant;
	}
	public boolean isSdpParticipant() {
		return sdpParticipant;
	}
	public void setSdpParticipant(boolean sdpParticipant) {
		this.sdpParticipant = sdpParticipant;
	}
	public boolean isCppParticipant() {
		return cppParticipant;
	}
	public void setCppParticipant(boolean cppParticipant) {
		this.cppParticipant = cppParticipant;
	}
	public boolean isBipParticipant() {
		return bipParticipant;
	}
	public void setBipParticipant(boolean bipParticipant) {
		this.bipParticipant = bipParticipant;
	}
	public boolean isCbpParticipant() {
		return cbpParticipant;
	}
	public void setCbpParticipant(boolean cbpParticipant) {
		this.cbpParticipant = cbpParticipant;
	}
	public boolean isApiParticipant() {
		return apiParticipant;
	}
	public void setApiParticipant(boolean apiParticipant) {
		this.apiParticipant = apiParticipant;
	}
	public boolean isSlrpParticipant() {
		return slrpParticipant;
	}
	public void setSlrpParticipant(boolean slrpParticipant) {
		this.slrpParticipant = slrpParticipant;
	}
	public boolean isDrcParticipant() {
		return drcParticipant;
	}
	public void setDrcParticipant(boolean drcParticipant) {
		this.drcParticipant = drcParticipant;
	}
	public boolean isAutoDRParticipant() {
		return autoDRParticipant;
	}
	public void setAutoDRParticipant(boolean autoDRParticipant) {
		this.autoDRParticipant = autoDRParticipant;
	}
	public boolean isRtpParticipant() {
		return rtpParticipant;
	}
	public void setRtpParticipant(boolean rtpParticipant) {
		this.rtpParticipant = rtpParticipant;
	}
	public double getRtpVoltag() {
		return rtpVoltag;
	}
	public void setRtpVoltag(double rtpVoltag) {
		this.rtpVoltag = rtpVoltag;
	}
	public String getDrcProgram() {
		return drcProgram;
	}
	public void setDrcProgram(String drcProgram) {
		this.drcProgram = drcProgram;
	}
//	public String getRtpProgram() {
//		if (!this.rtpParticipant) return null;
//		
//		if (servicePlan.equals(SERVICE_PLAN_PA_RTP))
//			return PROGRAM_RTP_AGRICULTURAL;
//		
//		double KVCategory=rtpVoltag/1000;
//		
//		String rtpProgram;
//		if (KVCategory<2) {
//			rtpProgram=PROGRAM_RTP_2K;
//		} else if (KVCategory>50){
//			rtpProgram=PROGRAM_RTP_50K;
//		} else {
//			rtpProgram=PROGRAM_RTP_2K_50K;
//		}
//		
//		return rtpProgram;
//	}
//
	public String getBcdRepName() {
		return bcdRepName;
	}

	public void setBcdRepName(String bcdRepName) {
		this.bcdRepName = bcdRepName;
	}

	public Date getAutoDrProfileStartDate() {
		return autoDrProfileStartDate;
	}

	public void setAutoDrProfileStartDate(Date autoDrProfileStartDate) {
		this.autoDrProfileStartDate = autoDrProfileStartDate;
	}

	/**
	 * @return the programOption
	 */
	public int getProgramOption() {
		return programOption;
	}

	/**
	 * @param programOption the programOption to set
	 */
	public void setProgramOption(int programOption) {
		this.programOption = programOption;
	}

	/**
	 * @return the substation
	 */
	public String getSubstation() {
		return substation;
	}

	/**
	 * @param substation the substation to set
	 */
	public void setSubstation(String substation) {
		this.substation = substation;
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
