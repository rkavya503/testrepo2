package com.akuacom.pss2.richsite.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantShedEntry;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.query.EvtParticipantCandidate;
import com.akuacom.pss2.richsite.event.EventDataModel;
import com.akuacom.pss2.richsite.event.EventParticipantDataModel;

public class ParticipantShedCalculateUtil {
	
	private static ParticipantManager pm;
	public static double calculateEstimatedShed(EventParticipantDataModel eventParticipantDataModel,EventDataModel eventDataModel){
		if(eventParticipantDataModel==null||eventDataModel==null){
			//OR THROW EXCEPTION
			return 0;
		}
		Participant participant = eventParticipantDataModel.getParticipant();
		return calculateEstimatedShed(participant,eventDataModel.getStartTime(),eventDataModel.getEndTime(),true);
	}
	public static double calculateEstimatedShed(String participantName,Event event){
		if(event==null||participantName==null||participantName.equalsIgnoreCase("")){
			//OR THROW EXCEPTION
			return 0;
		}
		Participant participant = getPm().getParticipantAndShedsOnly(participantName);
		return calculateEstimatedShed(participant,event,false);
	}
	public static double calculateEstimatedShed(EvtParticipantCandidate candidate,Event event){
		if(candidate==null||event==null){
			return 0.0;
		}
		double result = 0 ;
		if("ADVANCED".equalsIgnoreCase(candidate.getShedType())){
			double shedValue0=0;try{shedValue0 = candidate.getShedHourIndex0();}catch(Exception e){}
			double shedValue1=0;try{shedValue1 = candidate.getShedHourIndex1();}catch(Exception e){}
			double shedValue2=0;try{shedValue2 = candidate.getShedHourIndex2();}catch(Exception e){}
			double shedValue3=0;try{shedValue3 = candidate.getShedHourIndex3();}catch(Exception e){}
			double shedValue4=0;try{shedValue4 = candidate.getShedHourIndex4();}catch(Exception e){}
			double shedValue5=0;try{shedValue5 = candidate.getShedHourIndex5();}catch(Exception e){}
			double shedValue6=0;try{shedValue6 = candidate.getShedHourIndex6();}catch(Exception e){}
			double shedValue7=0;try{shedValue7 = candidate.getShedHourIndex7();}catch(Exception e){}
			double shedValue8=0;try{shedValue8 = candidate.getShedHourIndex8();}catch(Exception e){}
			double shedValue9=0;try{shedValue9 = candidate.getShedHourIndex9();}catch(Exception e){}
			double shedValue10=0;try{shedValue10 = candidate.getShedHourIndex10();}catch(Exception e){}
			double shedValue11=0;try{shedValue11 = candidate.getShedHourIndex11();}catch(Exception e){}
			double shedValue12=0;try{shedValue12 = candidate.getShedHourIndex12();}catch(Exception e){}
			double shedValue13=0;try{shedValue13 = candidate.getShedHourIndex13();}catch(Exception e){}
			double shedValue14=0;try{shedValue14 = candidate.getShedHourIndex14();}catch(Exception e){}
			double shedValue15=0;try{shedValue15 = candidate.getShedHourIndex15();}catch(Exception e){}
			double shedValue16=0;try{shedValue16 = candidate.getShedHourIndex16();}catch(Exception e){}
			double shedValue17=0;try{shedValue17 = candidate.getShedHourIndex17();}catch(Exception e){}
			double shedValue18=0;try{shedValue18 = candidate.getShedHourIndex18();}catch(Exception e){}
			double shedValue19=0;try{shedValue19 = candidate.getShedHourIndex19();}catch(Exception e){}
			double shedValue20=0;try{shedValue20 = candidate.getShedHourIndex20();}catch(Exception e){}
			double shedValue21=0;try{shedValue21 = candidate.getShedHourIndex21();}catch(Exception e){}
			double shedValue22=0;try{shedValue22 = candidate.getShedHourIndex22();}catch(Exception e){}
			double shedValue23=0;try{shedValue23 = candidate.getShedHourIndex23();}catch(Exception e){}
			
			Calendar calS=Calendar.getInstance();
			calS.setTime(event.getStartTime());
			
			Calendar calE=Calendar.getInstance();
			calE.setTime(event.getEndTime());
			double totalHours=0;
			double totalSheds=0;
			double hourStart = calS.get(Calendar.HOUR_OF_DAY);
			double hourEnd = calE.get(Calendar.HOUR_OF_DAY);
			double minStart = calS.get(Calendar.MINUTE);
			double minEnd = calE.get(Calendar.MINUTE);
			
			List<Double> shedValueList = new ArrayList<Double>();
			shedValueList.add(shedValue0);
			shedValueList.add(shedValue1);
			shedValueList.add(shedValue2);
			shedValueList.add(shedValue3);
			shedValueList.add(shedValue4);
			shedValueList.add(shedValue5);
			shedValueList.add(shedValue6);
			shedValueList.add(shedValue7);
			shedValueList.add(shedValue8);
			shedValueList.add(shedValue9);
			shedValueList.add(shedValue10);
			shedValueList.add(shedValue11);
			shedValueList.add(shedValue12);
			shedValueList.add(shedValue13);
			shedValueList.add(shedValue14);
			shedValueList.add(shedValue15);
			shedValueList.add(shedValue16);
			shedValueList.add(shedValue17);
			shedValueList.add(shedValue18);
			shedValueList.add(shedValue19);
			shedValueList.add(shedValue20);
			shedValueList.add(shedValue21);
			shedValueList.add(shedValue22);
			shedValueList.add(shedValue23);
			
			for(int i = 0 ;i<shedValueList.size();i++){
				double shedValue = shedValueList.get(i);
				if(i>=hourStart&&i<=hourEnd){
					if(i==hourStart&&i==hourEnd){
						return shedValue;
					}else if(i==hourStart){
						double tmpShed=(60-minStart)/60*shedValue;
						totalSheds=totalSheds+tmpShed;
						double tmpHours=(60-minStart)/60;
						totalHours=totalHours+tmpHours;
					}else if(i==hourEnd){
						double tmpShed=(minEnd/60)*shedValue;
						totalSheds=totalSheds+tmpShed;
						double tmpHours=minEnd/60;
						totalHours=totalHours+tmpHours;
					}else{
						totalSheds+=shedValue;
						totalHours++;
					}
				}
			}
			if(totalHours!=0){
				result = totalSheds/totalHours;	
			}
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			result = Double.parseDouble(df.format(result));
			return result;
		}else{
			return candidate.getRegisterShed();
		}
	}
	private static double calculateEstimatedShed(Participant participant,Event event, boolean reloadParticipant){
		if(participant==null||event==null){
			return 0.0;
		}
		return calculateEstimatedShed(participant,event.getStartTime(),event.getEndTime(),reloadParticipant);
	}
	public static double calculateEstimatedShed(Participant participant,Event event){
		if(participant==null||event==null){
			return 0.0;
		}
		return calculateEstimatedShed(participant,event,true);
	}
	private static double calculateEstimatedShed(Participant participant,Date startTime,Date endTime, boolean reloadParticipant){
		double result = 0;
		if(participant==null){
			//OR THROW EXCEPTION
			return 0;
		}
		if(participant!=null){
			if(participant.getShedType()!=null){
				if("SIMPLE".equalsIgnoreCase(participant.getShedType())){
					result = participant.getShedPerHourKW();
				}else if("ADVANCED".equalsIgnoreCase(participant.getShedType())){
					if(reloadParticipant){
						participant = getPm().getParticipantAndShedsOnly(participant.getParticipantName());
					}
					List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
					//------------------------ADVANCED SHED COMPUTING BEGIN-----------------------------
					Calendar calS=Calendar.getInstance();
					calS.setTime(startTime);
					
					Calendar calE=Calendar.getInstance();
					calE.setTime(endTime);
					double totalHours=0;
					double totalSheds=0;
					double hourStart = calS.get(Calendar.HOUR_OF_DAY);
					double hourEnd = calE.get(Calendar.HOUR_OF_DAY);
					double minStart = calS.get(Calendar.MINUTE);
					double minEnd = calE.get(Calendar.MINUTE);
					for(ParticipantShedEntry entry:shedEntries){
						int hourIndex = entry.getHourIndex();
						double value = entry.getValue();
						if(hourIndex>=hourStart&&hourIndex<=hourEnd){
							if(hourIndex==hourStart&&hourIndex==hourEnd){
								
								return value;
							}else if(hourIndex==hourStart){
								totalSheds+=(60-minStart)/60*value;
								totalHours+=(60-minStart)/60;
							}else if(hourIndex==hourEnd){
								totalSheds+=(minEnd/60)*value;
								totalHours+=minEnd/60;
							}else{
								totalSheds+=value;
								totalHours++;
							}
						}
					}
					if(totalHours!=0){
						result = totalSheds/totalHours;	
					}
					java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
					result = Double.parseDouble(df.format(result));
					
					//------------------------ADVANCED SHED COMPUTING END-----------------------------
				}
			}else{
				//SIMPLE mode 
				result = participant.getShedPerHourKW();
			}
		}
		return result;
	}

	public static double calcAvailableSheds(Participant p,Event event,List<ProgramParticipant> ppList) {

		// 1 calculate shed 
		double result = 0;
		ArrayList<Participant> clientList = new ArrayList<Participant>();
		if(event==null||p==null||ppList==null){
			//OR THROW EXCEPTION
			return 0;
		}
		double estimatedShed = calculateEstimatedShed(p,event);
		// 2 calculate available shed
		int c = 0;
		for (ProgramParticipant client : ppList) {
			if (client.getParticipant().getParent() != null && client.getParticipant().getParent().equals(p.getParticipantName())) {
				clientList.add(client.getParticipant());
				if (client.getParticipant().getStatus() == 0) {
					c++;
				}
			}
		}
		if (clientList.size() != 0 && c !=  0) {
			//result = ((estimatedShed / clientList.size()) * c);
			result = calcAvailableSheds(estimatedShed,c,clientList.size());
		}
		return result;
	}
	
	
	public static double calcAvailableSheds(double estimateShed,double validClients,double clients) {
		double result = 0;
		result = ((estimateShed / clients) * validClients);
		if(Double.isNaN(result)){
			result = 0;
		}
		return result;
	}
	

	public static ParticipantManager getPm() {
		if(pm==null){
			pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		}
		return pm;
	}

}
