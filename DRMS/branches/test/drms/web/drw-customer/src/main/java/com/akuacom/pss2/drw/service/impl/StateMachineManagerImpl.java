package com.akuacom.pss2.drw.service.impl;

public class StateMachineManagerImpl {
	private static StateMachineManagerImpl instance = new StateMachineManagerImpl();
	private StateMachineManagerImpl(){
		super();
	}
	public static StateMachineManagerImpl getInstance(){
		return instance;
	}
	
	private boolean getProgramsSuccessFlag = false;
	private boolean getCountysSuccessFlag = false;
	
	private long getEventsSuccessTimeStamp;
	

	/**
	 * @param getProgramsSuccessFlag the getProgramsSuccessFlag to set
	 */
	public void setGetProgramsSuccessFlag(boolean getProgramsSuccessFlag) {
		this.getProgramsSuccessFlag = getProgramsSuccessFlag;
	}
	/**
	 * @return the getProgramsSuccessFlag
	 */
	public boolean isGetProgramsSuccessFlag() {
		return getProgramsSuccessFlag;
	}
	/**
	 * @param getEventsSuccessTimeStamp the getEventsSuccessTimeStamp to set
	 */
	public void setGetEventsSuccessTimeStamp(long getEventsSuccessTimeStamp) {
		this.getEventsSuccessTimeStamp = getEventsSuccessTimeStamp;
	}
	/**
	 * @return the getEventsSuccessTimeStamp
	 */
	public long getGetEventsSuccessTimeStamp() {
		return getEventsSuccessTimeStamp;
	}
	/**
	 * @param getCountysSuccessFlag the getCountysSuccessFlag to set
	 */
	public void setGetCountysSuccessFlag(boolean getCountysSuccessFlag) {
		this.getCountysSuccessFlag = getCountysSuccessFlag;
	}
	/**
	 * @return the getCountysSuccessFlag
	 */
	public boolean isGetCountysSuccessFlag() {
		return getCountysSuccessFlag;
	}
}
