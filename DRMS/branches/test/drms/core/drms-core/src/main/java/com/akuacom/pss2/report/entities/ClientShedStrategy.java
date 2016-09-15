package com.akuacom.pss2.report.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.akuacom.utils.DateUtil;


public class ClientShedStrategy implements Serializable,Comparable<ClientShedStrategy>{

	private static final long serialVersionUID = -5777978730832250207L;
	
	public ClientShedStrategy(){
		super();
	}
	
	/** Client: Client ID */
	private String client;	
	/** Participant: Participant ID */
	private String participant;
	/** Account Number: Account number of participant */
	private String accountNumber;
	/** Parent: Parent (commas separated parents if multiple) of participant for programs in the client programs(Active) list */
	private String parent;
	/** Season: Season Type */
	private String season;
	/** allValue: all value for simple*/
	private String allValue;
	/** intervalValue0 - shed value for interval 0:00-0:59. */
	private String intervalValue0;
	/** intervalValue0 - shed value for interval 1:00-1:59. */
	private String intervalValue1;
	/** intervalValue0 - shed value for interval 2:00-2:59. */
	private String intervalValue2;
	/** intervalValue0 - shed value for interval 3:00-3:59. */
	private String intervalValue3;
	/** intervalValue0 - shed value for interval 4:00-4:59. */
	private String intervalValue4;
	/** intervalValue0 - shed value for interval 5:00-5:59. */
	private String intervalValue5;
	/** intervalValue0 - shed value for interval 6:00-6:59. */
	private String intervalValue6;
	/** intervalValue0 - shed value for interval 7:00-7:59. */
	private String intervalValue7;
	/** intervalValue0 - shed value for interval 8:00-8:59. */
	private String intervalValue8;
	/** intervalValue0 - shed value for interval 9:00-9:59. */
	private String intervalValue9;
	/** intervalValue0 - shed value for interval 10:00-10:59. */
	private String intervalValue10;
	/** intervalValue0 - shed value for interval 11:00-11:59. */
	private String intervalValue11;
	/** intervalValue0 - shed value for interval 12:00-12:59. */
	private String intervalValue12;
	/** intervalValue0 - shed value for interval 13:00-13:59. */
	private String intervalValue13;
	/** intervalValue0 - shed value for interval 14:00-14:59. */
	private String intervalValue14;
	/** intervalValue0 - shed value for interval 15:00-15:59. */
	private String intervalValue15;
	/** intervalValue0 - shed value for interval 16:00-16:59. */
	private String intervalValue16;
	/** intervalValue0 - shed value for interval 17:00-17:59. */
	private String intervalValue17;
	/** intervalValue0 - shed value for interval 18:00-18:59. */
	private String intervalValue18;
	/** intervalValue0 - shed value for interval 19:00-19:59. */
	private String intervalValue19;
	/** intervalValue0 - shed value for interval 20:00-20:59. */
	private String intervalValue20;
	/** intervalValue0 - shed value for interval 21:00-21:59. */
	private String intervalValue21;
	/** intervalValue0 - shed value for interval 22:00-22:59. */
	private String intervalValue22;
	/** intervalValue0 - shed value for interval 23:00-23:59. */
	private String intervalValue23;
	private String program;
	private String programType;
	private String state;//enroll or not
	private List<String> intervalValueList = new ArrayList<String>();

	//----------------------------------------
	//Additional attributes
	
	private boolean cppFlag=false;
	private boolean rtpFlag=false;
	private boolean dbpFlag=false;
	
	//RTP
	private String mode;
	private String source;
	private String sortOrder;
	private String start;
	private String end;
	private String rtpType;//SIMPLE or ADVANCED
	private String rtpValue;
	
	//1 ALWAYS
	//2 LESS_THAN
	//3 GREATER_THAN_OR_EQUAL
	private String operator;
	//DBP
	private String timeBlock;
	private String normal;
	private String moderate;
	private String high;
	
	/**
	 * @return the timeBlock
	 */
	public String getTimeBlock() {
		return timeBlock;
	}
	/**
	 * @param timeBlock the timeBlock to set
	 */
	public void setTimeBlock(String timeBlock) {
		this.timeBlock = timeBlock;
	}
	/**
	 * @return the normal
	 */
	public String getNormal() {
		return normal;
	}
	/**
	 * @param normal the normal to set
	 */
	public void setNormal(String normal) {
		this.normal = normal;
	}
	/**
	 * @return the moderate
	 */
	public String getModerate() {
		return moderate;
	}
	/**
	 * @param moderate the moderate to set
	 */
	public void setModerate(String moderate) {
		this.moderate = moderate;
	}
	/**
	 * @return the high
	 */
	public String getHigh() {
		return high;
	}
	/**
	 * @return the programType
	 */
	public String getProgramType() {
		return programType;
	}
	/**
	 * @param programType the programType to set
	 */
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	/**
	 * @param high the high to set
	 */
	public void setHigh(String high) {
		this.high = high;
	}
	/**
	 * @return the rtpValue
	 */
	public String getRtpValue() {
		return rtpValue;
	}
	/**
	 * @param rtpValue the rtpValue to set
	 */
	public void setRtpValue(String rtpValue) {
		this.rtpValue = rtpValue;
	}
	public String getClient() {
		if(client==null){client="";}
		return client;
	}
	public String getParticipant() {
		if(participant==null){participant="";}
		return participant;
	}
	public String getAccountNumber() {
		if(accountNumber==null){accountNumber="";}
		return accountNumber;
	}
	public String getParent() {
		if(parent==null){parent="";}
		return parent;
	}

	public void setClient(String client) {
		this.client = client;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getProgram() {
		if(program==null||program.equalsIgnoreCase("null")){program="";}
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}


	
	/**
	 * @return the season
	 */
	public String getSeason() {
		if(season==null||season.equalsIgnoreCase("null")){season="";}
		return season;
	}
	/**
	 * @param season the season to set
	 */
	public void setSeason(String season) {
		this.season = season;
	}
	/**
	 * @return the allValue
	 */
	public String getAllValue() {
		if(allValue==null||allValue.equalsIgnoreCase("null")){allValue="";}
		return allValue;
	}
	/**
	 * @param allValue the allValue to set
	 */
	public void setAllValue(String allValue) {
		this.allValue = allValue;
	}
	/**
	 * @return the intervalValue0
	 */
	public String getIntervalValue0() {
		if(intervalValue0==null||intervalValue0.equalsIgnoreCase("null")){intervalValue0="";}
		return intervalValue0;
	}
	/**
	 * @param intervalValue0 the intervalValue0 to set
	 */
	public void setIntervalValue0(String intervalValue0) {
		this.intervalValue0 = intervalValue0;
	}
	/**
	 * @return the intervalValue1
	 */
	public String getIntervalValue1() {
		if(intervalValue1==null||intervalValue1.equalsIgnoreCase("null")){intervalValue1="";}
		return intervalValue1;
	}
	/**
	 * @param intervalValue1 the intervalValue1 to set
	 */
	public void setIntervalValue1(String intervalValue1) {
		this.intervalValue1 = intervalValue1;
	}
	/**
	 * @return the intervalValue2
	 */
	public String getIntervalValue2() {
		if(intervalValue2==null||intervalValue2.equalsIgnoreCase("null")){intervalValue2="";}
		return intervalValue2;
	}
	/**
	 * @param intervalValue2 the intervalValue2 to set
	 */
	public void setIntervalValue2(String intervalValue2) {
		this.intervalValue2 = intervalValue2;
	}
	/**
	 * @return the intervalValue3
	 */
	public String getIntervalValue3() {
		if(intervalValue3==null||intervalValue3.equalsIgnoreCase("null")){intervalValue3="";}
		return intervalValue3;
	}
	/**
	 * @param intervalValue3 the intervalValue3 to set
	 */
	public void setIntervalValue3(String intervalValue3) {
		this.intervalValue3 = intervalValue3;
	}
	/**
	 * @return the intervalValue4
	 */
	public String getIntervalValue4() {
		if(intervalValue4==null||intervalValue4.equalsIgnoreCase("null")){intervalValue4="";}
		return intervalValue4;
	}
	/**
	 * @param intervalValue4 the intervalValue4 to set
	 */
	public void setIntervalValue4(String intervalValue4) {
		this.intervalValue4 = intervalValue4;
	}
	/**
	 * @return the intervalValue5
	 */
	public String getIntervalValue5() {
		if(intervalValue5==null||intervalValue5.equalsIgnoreCase("null")){intervalValue5="";}
		return intervalValue5;
	}
	/**
	 * @param intervalValue5 the intervalValue5 to set
	 */
	public void setIntervalValue5(String intervalValue5) {
		this.intervalValue5 = intervalValue5;
	}
	/**
	 * @return the intervalValue6
	 */
	public String getIntervalValue6() {
		if(intervalValue6==null||intervalValue6.equalsIgnoreCase("null")){intervalValue6="";}
		return intervalValue6;
	}
	/**
	 * @param intervalValue6 the intervalValue6 to set
	 */
	public void setIntervalValue6(String intervalValue6) {
		this.intervalValue6 = intervalValue6;
	}
	/**
	 * @return the intervalValue7
	 */
	public String getIntervalValue7() {
		if(intervalValue7==null||intervalValue7.equalsIgnoreCase("null")){intervalValue7="";}
		return intervalValue7;
	}
	/**
	 * @param intervalValue7 the intervalValue7 to set
	 */
	public void setIntervalValue7(String intervalValue7) {
		this.intervalValue7 = intervalValue7;
	}
	/**
	 * @return the intervalValue8
	 */
	public String getIntervalValue8() {
		if(intervalValue8==null||intervalValue8.equalsIgnoreCase("null")){intervalValue8="";}
		return intervalValue8;
	}
	/**
	 * @param intervalValue8 the intervalValue8 to set
	 */
	public void setIntervalValue8(String intervalValue8) {
		this.intervalValue8 = intervalValue8;
	}
	/**
	 * @return the intervalValue9
	 */
	public String getIntervalValue9() {
		if(intervalValue9==null||intervalValue9.equalsIgnoreCase("null")){intervalValue9="";}
		return intervalValue9;
	}
	/**
	 * @param intervalValue9 the intervalValue9 to set
	 */
	public void setIntervalValue9(String intervalValue9) {
		this.intervalValue9 = intervalValue9;
	}
	/**
	 * @return the intervalValue10
	 */
	public String getIntervalValue10() {
		if(intervalValue10==null||intervalValue10.equalsIgnoreCase("null")){intervalValue10="";}
		return intervalValue10;
	}
	/**
	 * @param intervalValue10 the intervalValue10 to set
	 */
	public void setIntervalValue10(String intervalValue10) {
		this.intervalValue10 = intervalValue10;
	}
	/**
	 * @return the intervalValue11
	 */
	public String getIntervalValue11() {
		if(intervalValue11==null||intervalValue11.equalsIgnoreCase("null")){intervalValue11="";}
		return intervalValue11;
	}
	/**
	 * @param intervalValue11 the intervalValue11 to set
	 */
	public void setIntervalValue11(String intervalValue11) {
		this.intervalValue11 = intervalValue11;
	}
	/**
	 * @return the intervalValue12
	 */
	public String getIntervalValue12() {
		if(intervalValue12==null||intervalValue12.equalsIgnoreCase("null")){intervalValue12="";}
		return intervalValue12;
	}
	/**
	 * @param intervalValue12 the intervalValue12 to set
	 */
	public void setIntervalValue12(String intervalValue12) {
		this.intervalValue12 = intervalValue12;
	}
	/**
	 * @return the intervalValue13
	 */
	public String getIntervalValue13() {
		if(intervalValue13==null||intervalValue13.equalsIgnoreCase("null")){intervalValue13="";}
		return intervalValue13;
	}
	/**
	 * @param intervalValue13 the intervalValue13 to set
	 */
	public void setIntervalValue13(String intervalValue13) {
		this.intervalValue13 = intervalValue13;
	}
	/**
	 * @return the intervalValue14
	 */
	public String getIntervalValue14() {
		if(intervalValue14==null||intervalValue14.equalsIgnoreCase("null")){intervalValue14="";}
		return intervalValue14;
	}
	/**
	 * @param intervalValue14 the intervalValue14 to set
	 */
	public void setIntervalValue14(String intervalValue14) {
		this.intervalValue14 = intervalValue14;
	}
	/**
	 * @return the intervalValue15
	 */
	public String getIntervalValue15() {
		if(intervalValue15==null||intervalValue15.equalsIgnoreCase("null")){intervalValue15="";}
		return intervalValue15;
	}
	/**
	 * @param intervalValue15 the intervalValue15 to set
	 */
	public void setIntervalValue15(String intervalValue15) {
		this.intervalValue15 = intervalValue15;
	}
	/**
	 * @return the intervalValue16
	 */
	public String getIntervalValue16() {
		if(intervalValue16==null||intervalValue16.equalsIgnoreCase("null")){intervalValue16="";}
		return intervalValue16;
	}
	/**
	 * @param intervalValue16 the intervalValue16 to set
	 */
	public void setIntervalValue16(String intervalValue16) {
		this.intervalValue16 = intervalValue16;
	}
	/**
	 * @return the intervalValue17
	 */
	public String getIntervalValue17() {
		if(intervalValue17==null||intervalValue17.equalsIgnoreCase("null")){intervalValue17="";}
		return intervalValue17;
	}
	/**
	 * @param intervalValue17 the intervalValue17 to set
	 */
	public void setIntervalValue17(String intervalValue17) {
		this.intervalValue17 = intervalValue17;
	}
	/**
	 * @return the intervalValue18
	 */
	public String getIntervalValue18() {
		if(intervalValue18==null||intervalValue18.equalsIgnoreCase("null")){intervalValue18="";}
		return intervalValue18;
	}
	/**
	 * @param intervalValue18 the intervalValue18 to set
	 */
	public void setIntervalValue18(String intervalValue18) {
		this.intervalValue18 = intervalValue18;
	}
	/**
	 * @return the intervalValue19
	 */
	public String getIntervalValue19() {
		if(intervalValue19==null||intervalValue19.equalsIgnoreCase("null")){intervalValue19="";}
		return intervalValue19;
	}
	/**
	 * @param intervalValue19 the intervalValue19 to set
	 */
	public void setIntervalValue19(String intervalValue19) {
		this.intervalValue19 = intervalValue19;
	}
	/**
	 * @return the intervalValue20
	 */
	public String getIntervalValue20() {
		if(intervalValue20==null||intervalValue20.equalsIgnoreCase("null")){intervalValue20="";}
		return intervalValue20;
	}
	/**
	 * @param intervalValue20 the intervalValue20 to set
	 */
	public void setIntervalValue20(String intervalValue20) {
		this.intervalValue20 = intervalValue20;
	}
	/**
	 * @return the intervalValue21
	 */
	public String getIntervalValue21() {
		if(intervalValue21==null||intervalValue21.equalsIgnoreCase("null")){intervalValue21="";}
		return intervalValue21;
	}
	/**
	 * @param intervalValue21 the intervalValue21 to set
	 */
	public void setIntervalValue21(String intervalValue21) {
		this.intervalValue21 = intervalValue21;
	}
	/**
	 * @return the intervalValue22
	 */
	public String getIntervalValue22() {
		if(intervalValue22==null||intervalValue22.equalsIgnoreCase("null")){intervalValue22="";}
		return intervalValue22;
	}
	/**
	 * @param intervalValue22 the intervalValue22 to set
	 */
	public void setIntervalValue22(String intervalValue22) {
		this.intervalValue22 = intervalValue22;
	}
	/**
	 * @return the intervalValue23
	 */
	public String getIntervalValue23() {
		if(intervalValue23==null||intervalValue23.equalsIgnoreCase("null")){intervalValue23="";}
		return intervalValue23;
	}
	/**
	 * @param intervalValue23 the intervalValue23 to set
	 */
	public void setIntervalValue23(String intervalValue23) {
		this.intervalValue23 = intervalValue23;
	}
	
	
	
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	@Override
    public int compareTo(ClientShedStrategy o) {
        if (o.getClient() == null) {
            return 1;
        } else if (this.getClient() == null) {
            return -1;
        } else {
            return (this.getClient()+this.getSeason()).compareTo(o.getClient()+o.getSeason());
        }
    }
	public static ClientShedStrategy build(ClientShedStrategy instance){
		
		if(instance.getSource()!=null&&instance.getSource().equalsIgnoreCase("CPP Strategy")){
			//CPP type Shed Strategy
			instance = ClientShedStrategyUtil.buildCPPType(instance);
		}
		if(instance.getSource()!=null&&instance.getSource().indexOf("RTP Strategy")>-1){
			//RTP type Shed Strategy
			instance = ClientShedStrategyUtil.buildRTPType(instance);
		}
		return instance;
	}
	public static List<ClientShedStrategy> transfer(List<ClientShedStrategy> originalList){
			List<ClientShedStrategy> resultList = new ArrayList<ClientShedStrategy>();
			if(originalList.size()>0){
				ClientShedStrategy shedStrategy = originalList.get(0);

				if(shedStrategy.getSource()!=null&&shedStrategy.getSource().equalsIgnoreCase("CPP Strategy")){
					resultList = ClientShedStrategyUtil.transferCPPList(originalList);
				}else if(shedStrategy.getSource()!=null&&shedStrategy.getSource().indexOf("RTP Strategy")>-1){
					resultList =  ClientShedStrategyUtil.transferRTPList(originalList);
				}else if(shedStrategy.getProgramType()!=null&&shedStrategy.getProgramType().equalsIgnoreCase("DBPProgram")){
					resultList =  ClientShedStrategyUtil.transferDBPList(originalList);
				}
				resultList=ClientShedStrategyUtil.sort(resultList);
			}
		return resultList;
	}
	
	

	
	
	
	
	public static void main(String args[]){
		String start = "2012-02-14 11:00:00";
		Calendar startCal = new GregorianCalendar();
		startCal.setTime(DateUtil.parse(start, "yyyy-MM-dd HH:mm:ss"));
		int hour = startCal.get(Calendar.HOUR_OF_DAY);
		switch(hour)

		{
		case 0:
			
		case 1:

		case 2:

		case 11:



		}
	}
	/**
	 * @param rtpType the rtpType to set
	 */
	public void setRtpType(String rtpType) {
		this.rtpType = rtpType;
	}
	/**
	 * @return the rtpType
	 */
	public String getRtpType() {
		return rtpType;
	}
    /** The summer active. */
    private boolean summerActive;

    /** The winter active. */
    private boolean winterActive;

    /** The weekend active. */
    private boolean weekendActive;

	/**
	 * @return the summerActive
	 */
	public boolean isSummerActive() {
		return summerActive;
	}
	/**
	 * @param summerActive the summerActive to set
	 */
	public void setSummerActive(boolean summerActive) {
		this.summerActive = summerActive;
	}
	/**
	 * @return the winterActive
	 */
	public boolean isWinterActive() {
		return winterActive;
	}
	/**
	 * @param winterActive the winterActive to set
	 */
	public void setWinterActive(boolean winterActive) {
		this.winterActive = winterActive;
	}
	/**
	 * @return the weekendActive
	 */
	public boolean isWeekendActive() {
		return weekendActive;
	}
	/**
	 * @param weekendActive the weekendActive to set
	 */
	public void setWeekendActive(boolean weekendActive) {
		this.weekendActive = weekendActive;
	}

	
	public static ClientShedStrategy clone(ClientShedStrategy c){
		ClientShedStrategy r = new ClientShedStrategy();
		r.setAccountNumber(c.getAccountNumber());
		r.setAllValue(c.getAllValue());
		r.setClient(c.getClient());
		r.setEnd(c.getEnd());
		r.setIntervalValue0(c.getIntervalValue0());
		r.setIntervalValue1(c.getIntervalValue1());
		r.setIntervalValue2(c.getIntervalValue2());
		r.setIntervalValue3(c.getIntervalValue3());
		r.setIntervalValue4(c.getIntervalValue4());
		r.setIntervalValue5(c.getIntervalValue5());
		r.setIntervalValue6(c.getIntervalValue6());
		r.setIntervalValue7(c.getIntervalValue7());
		r.setIntervalValue8(c.getIntervalValue8());
		r.setIntervalValue9(c.getIntervalValue9());
		r.setIntervalValue10(c.getIntervalValue10());
		r.setIntervalValue11(c.getIntervalValue11());
		r.setIntervalValue12(c.getIntervalValue12());
		r.setIntervalValue13(c.getIntervalValue13());
		r.setIntervalValue14(c.getIntervalValue14());
		r.setIntervalValue15(c.getIntervalValue15());
		r.setIntervalValue16(c.getIntervalValue16());
		r.setIntervalValue17(c.getIntervalValue17());
		r.setIntervalValue18(c.getIntervalValue18());
		r.setIntervalValue19(c.getIntervalValue19());
		r.setIntervalValue20(c.getIntervalValue20());
		r.setIntervalValue21(c.getIntervalValue21());
		r.setIntervalValue22(c.getIntervalValue22());
		r.setIntervalValue23(c.getIntervalValue22());
		r.setMode(c.getMode());
		r.setParent(c.getParent());
		r.setParticipant(c.getParticipant());
		r.setProgram(c.getProgram());
		r.setRtpType(c.getRtpType());
		r.setRtpValue(c.getRtpValue());
		r.setSeason(c.getSeason());
		r.setSortOrder(c.getSortOrder());
		r.setSource(c.getSource());
		r.setStart(c.getStart());
		r.setSummerActive(c.isSummerActive());
		r.setWeekendActive(c.isWeekendActive());
		r.setWinterActive(c.isWinterActive());
		r.setState(c.getState());
		return r;
	}
	/**
	 * @return the cppFlag
	 */
	public boolean isCppFlag() {
		return cppFlag;
	}
	/**
	 * @param cppFlag the cppFlag to set
	 */
	public void setCppFlag(boolean cppFlag) {
		this.cppFlag = cppFlag;
	}
	/**
	 * @return the rtpFlag
	 */
	public boolean isRtpFlag() {
		return rtpFlag;
	}
	/**
	 * @param rtpFlag the rtpFlag to set
	 */
	public void setRtpFlag(boolean rtpFlag) {
		this.rtpFlag = rtpFlag;
	}
	/**
	 * @return the dbpFlag
	 */
	public boolean isDbpFlag() {
		return dbpFlag;
	}
	/**
	 * @param dbpFlag the dbpFlag to set
	 */
	public void setDbpFlag(boolean dbpFlag) {
		this.dbpFlag = dbpFlag;
	}
	/**
	 * @param intervalValueList the intervalValueList to set
	 */
	public void setIntervalValueList(List<String> intervalValueList) {
		this.intervalValueList = intervalValueList;
	}
	/**
	 * @return the intervalValueList
	 */
	public List<String> getIntervalValueList() {
		if(intervalValueList.size()<1){
			intervalValueList.clear();
			intervalValueList.add(intervalValue0);
			intervalValueList.add(intervalValue1);
			intervalValueList.add(intervalValue2);
			intervalValueList.add(intervalValue3);
			intervalValueList.add(intervalValue4);
			intervalValueList.add(intervalValue5);
			intervalValueList.add(intervalValue6);
			intervalValueList.add(intervalValue7);
			intervalValueList.add(intervalValue8);
			intervalValueList.add(intervalValue9);
			intervalValueList.add(intervalValue10);
			intervalValueList.add(intervalValue11);
			intervalValueList.add(intervalValue12);
			intervalValueList.add(intervalValue13);
			intervalValueList.add(intervalValue14);
			intervalValueList.add(intervalValue15);
			intervalValueList.add(intervalValue16);
			intervalValueList.add(intervalValue17);
			intervalValueList.add(intervalValue18);
			intervalValueList.add(intervalValue19);
			intervalValueList.add(intervalValue20);
			intervalValueList.add(intervalValue21);
			intervalValueList.add(intervalValue22);
			intervalValueList.add(intervalValue23);
		}
		return intervalValueList;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		if(state==null||state.equalsIgnoreCase("")){
			state="NO";
		}
		if(state.equalsIgnoreCase("1")){
			state = "YES";
		}else if(state.equalsIgnoreCase("0")){
			state = "NO";
		} 
		return state;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	
}
