package com.akuacom.pss2.richsite.participant;

import java.io.Serializable;

import com.akuacom.pss2.participant.ParticipantShedEntry;

public class ParticipantShedEntryBean implements Serializable {
	
	private static final long serialVersionUID = 4467728472597790246L;
	/** 0-23 for day hour*/
	private int hourIndex;
	/** SIMPLE or ADVANCED*/
	private String type;
	/** Shed Value*/
	private String value;
	
	/**
	 * @return the hourIndex
	 */
	public int getHourIndex() {
		return hourIndex;
	}
	/**
	 * @param hourIndex the hourIndex to set
	 */
	public void setHourIndex(int hourIndex) {
		this.hourIndex = hourIndex;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	public String getTimeBockString(){
		if(type.equalsIgnoreCase("SIMPLE")){
			return "00:00 - 23:59";
		}else{
			if(hourIndex>=0&&hourIndex<10){
				return "0"+hourIndex+":00 - 0"+hourIndex+":59";
			}else if(hourIndex>=10&&hourIndex<24){
				return hourIndex+":00 - "+hourIndex+":59";
			}else{
				return "";
			}
		}
		
	}
	
	public static ParticipantShedEntryBean transfer(ParticipantShedEntry in){
		ParticipantShedEntryBean bean =new ParticipantShedEntryBean();
		if(in!=null){
			bean.setHourIndex(in.getHourIndex());
			bean.setValue(String.valueOf(in.getValue()));
			bean.setType("ADVANCED");
		}
		return bean;
	}
	
}
