package com.akuacom.pss2.richsite.report;

import org.apache.log4j.Logger;

public class ClientShedStrategyBackingBean {
	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127716274236L;
	/** The log */
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ClientInfoBackingBean.class);	

	private String client;
	private String program;
	private String enroll;
	private String participant;
	private String account;
	private String parent;
	private String season;
	private String startTime;
	private String moderateValue;
	private String highValue;
	
	/**
	 * @return the client
	 */
	public String getClient() {
		if(client==null||client.equalsIgnoreCase("null")){
			client="";
		}
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the program
	 */
	public String getProgram() {
		if(program==null||program.equalsIgnoreCase("null")){
			program="";
		}
		return program;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	/**
	 * @return the enroll
	 */
	public String getEnroll() {
		if(enroll==null||enroll.equalsIgnoreCase("null")){
			enroll="";
		}
		return enroll;
	}

	/**
	 * @param enroll the enroll to set
	 */
	public void setEnroll(String enroll) {
		this.enroll = enroll;
	}

	/**
	 * @return the participant
	 */
	public String getParticipant() {
		if(participant==null||participant.equalsIgnoreCase("null")){
			participant="";
		}
		return participant;
	}

	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(String participant) {
		this.participant = participant;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		if(account==null||account.equalsIgnoreCase("null")){
			account="";
		}
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the parent
	 */
	public String getParent() {
		if(parent==null||parent.equalsIgnoreCase("null")){
			parent="";
		}
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the season
	 */
	public String getSeason() {
		if(season==null||season.equalsIgnoreCase("null")){
			season="";
		}
		return season;
	}

	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		if(startTime==null||startTime.equalsIgnoreCase("null")){
			startTime="";
		}
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the moderateValue
	 */
	public String getModerateValue() {
		if(moderateValue==null||moderateValue.equalsIgnoreCase("null")){
			moderateValue="";
		}
		return moderateValue;
	}

	/**
	 * @param moderateValue the moderateValue to set
	 */
	public void setModerateValue(String moderateValue) {
		this.moderateValue = moderateValue;
	}

	/**
	 * @return the highValue
	 */
	public String getHighValue() {
		if(highValue==null||highValue.equalsIgnoreCase("null")){
			highValue="";
		}
		return highValue;
	}

	/**
	 * @param highValue the highValue to set
	 */
	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}

	public ClientShedStrategyBackingBean(){
	}
	


}
