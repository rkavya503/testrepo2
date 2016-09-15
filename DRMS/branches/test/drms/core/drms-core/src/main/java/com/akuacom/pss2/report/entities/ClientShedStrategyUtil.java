package com.akuacom.pss2.report.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.utils.DateUtil;


public class ClientShedStrategyUtil{

	private static final long serialVersionUID = -5777978730832250207L;
	
	public ClientShedStrategyUtil(){
		super();
	}

    
	private static final int ONE_HOUR_MS = (60 * 60 * 1000) - 1000;
	private static final String NAME = "name";
	
	public static List<ClientShedStrategy> transferCPPList(List<ClientShedStrategy> originalList){
		List<ClientShedStrategy> resultList = new ArrayList<ClientShedStrategy>();
		Map<String,List<ClientShedStrategy>> map = new HashMap<String,List<ClientShedStrategy>>();
		for(ClientShedStrategy instance: originalList){
			if(map.containsKey(instance.getClient())){
				map.get(instance.getClient()).add(instance);
			}else{
				List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
				list.add(instance);
				map.put(instance.getClient(), list);
			}
		}
		
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<ClientShedStrategy> sameClientResults = map.get(key);
			ClientShedStrategy clientInfo = sameClientResults.get(0);

			
			
			if(clientInfo.getSource().equalsIgnoreCase("CPP Strategy")){
				//CPP type Shed Strategy
				
				for(int i =0;i<sameClientResults.size();i++){
					ClientShedStrategy tmp = sameClientResults.get(i);
					Calendar startCal = new GregorianCalendar();
					startCal.setTime(DateUtil.parse(tmp.getStart(), "yyyy-MM-dd HH:mm:ss"));
					int hour = startCal.get(Calendar.HOUR_OF_DAY);
					switch(hour){
						case 0:clientInfo.setIntervalValue0(tmp.getMode());break;
						case 1:clientInfo.setIntervalValue1(tmp.getMode());break;
						case 2:clientInfo.setIntervalValue2(tmp.getMode());break;
						case 3:clientInfo.setIntervalValue3(tmp.getMode());break;
						case 4:clientInfo.setIntervalValue4(tmp.getMode());break;
						case 5:clientInfo.setIntervalValue5(tmp.getMode());break;
						case 6:clientInfo.setIntervalValue6(tmp.getMode());break;
						case 7:clientInfo.setIntervalValue7(tmp.getMode());break;
						case 8:clientInfo.setIntervalValue8(tmp.getMode());break;
						case 9:clientInfo.setIntervalValue9(tmp.getMode());break;
						case 10:clientInfo.setIntervalValue10(tmp.getMode());break;
						case 11:clientInfo.setIntervalValue11(tmp.getMode());break;
						case 12:clientInfo.setIntervalValue12(tmp.getMode());break;
						case 13:clientInfo.setIntervalValue13(tmp.getMode());break;
						case 14:clientInfo.setIntervalValue14(tmp.getMode());break;
						case 15:clientInfo.setIntervalValue15(tmp.getMode());break;
						case 16:clientInfo.setIntervalValue16(tmp.getMode());break;
						case 17:clientInfo.setIntervalValue17(tmp.getMode());break;
						case 18:clientInfo.setIntervalValue18(tmp.getMode());break;
						case 19:clientInfo.setIntervalValue19(tmp.getMode());break;
						case 20:clientInfo.setIntervalValue20(tmp.getMode());break;
						case 21:clientInfo.setIntervalValue21(tmp.getMode());break;
						case 22:clientInfo.setIntervalValue22(tmp.getMode());break;
						case 23:clientInfo.setIntervalValue23(tmp.getMode());break;
						
					}
				}
				resultList.add(clientInfo);
			}
		}
		Set<ClientShedStrategy> set = new TreeSet<ClientShedStrategy>(resultList);
		resultList.clear();
		for(ClientShedStrategy instance:set){
			resultList.add(instance);
		}
		return resultList;
	}
	
	
	public static List<ClientShedStrategy> transferRTPList(List<ClientShedStrategy> originalList){
		List<ClientShedStrategy> resultList = new ArrayList<ClientShedStrategy>();
		Map<String,List<ClientShedStrategy>> map = new HashMap<String,List<ClientShedStrategy>>();
		for(ClientShedStrategy instance: originalList){
			String client= instance.getClient(); 
			String source = instance.getSource();
			String key = client+"+"+source;
			
			if(map.containsKey(key)){
				map.get(key).add(instance);
			}else{
				List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
				list.add(instance);
				map.put(key, list);
			}
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<ClientShedStrategy> sameClientResults = map.get(key);
			ClientShedStrategy shedStrategy = sameClientResults.get(0);
			
			if(shedStrategy.getSource().indexOf("RTP Strategy")>-1){
				//RTP type Shed Strategy
				//----------1 set RTP type : simple or advance
				Date startTime = DateUtil.parse(shedStrategy.getStart(), "yyyy-MM-dd HH:mm:ss");
				Date endTime = DateUtil.parse(shedStrategy.getEnd(), "yyyy-MM-dd HH:mm:ss");
				long timeDiff = endTime.getTime() - startTime.getTime();
				// See if the entry is one hour long (or very very close to it)
	    		if(Math.abs(timeDiff - ONE_HOUR_MS) < 1000){
	    			shedStrategy.setRtpType("ADVANCED");
	    		}else{
	    			shedStrategy.setRtpType("SIMPLE");	    			
	    		}
	    		
	    		//---------2 set sumer/winter/weekend flag
	    		String source = shedStrategy.getSource();
	    		Map<String, String> seasonName = new HashMap<String, String>();
	    		int nameStart = source.indexOf('.') + 1;
	        	int weekendStart = source.lastIndexOf('.') + 1;
	        	int winterStart = source.lastIndexOf('.', weekendStart - 2) + 1;
	        	int summerStart = source.lastIndexOf('.', winterStart - 2) + 1;
	        	int nameEnd = summerStart - 2;
	        	seasonName.put(NAME,source.substring(nameStart, nameEnd + 1));
	        	seasonName.put(SeasonConfig.SUMMER_SEASON,String.valueOf(source.charAt(summerStart)));
	        	seasonName.put(SeasonConfig.WINTER_SEASON,String.valueOf(source.charAt(winterStart)));
	        	seasonName.put(SeasonConfig.WEEKEND_SEASON,String.valueOf(source.charAt(weekendStart)));
	        	shedStrategy.setSummerActive("y".equals(seasonName.get(SeasonConfig.SUMMER_SEASON)));
	        	shedStrategy.setWinterActive("y".equals(seasonName.get(SeasonConfig.WINTER_SEASON)));
	        	shedStrategy.setWeekendActive("y".equals(seasonName.get(SeasonConfig.WEEKEND_SEASON)));
	        	
	        	//--------3-------only one record
	        	if(shedStrategy.isSummerActive()&&shedStrategy.isWeekendActive()&&shedStrategy.isWinterActive()){
	        		shedStrategy = buildRTPShedStrategy(shedStrategy,sameClientResults,"ALL");
	        		
	        		resultList.add(shedStrategy);
	        	}
	        	//--------4-------multiple records
	        	else{
	        		ClientShedStrategy shedStrategySummer = ClientShedStrategy.clone(shedStrategy);
	        		ClientShedStrategy shedStrategyWinter = ClientShedStrategy.clone(shedStrategy);
	        		ClientShedStrategy shedStrategyWeekend = ClientShedStrategy.clone(shedStrategy);
	        		if(shedStrategy.isSummerActive()){
	        			shedStrategySummer = buildRTPShedStrategy(shedStrategySummer,sameClientResults,"SUMMER");
	        			
	        			resultList.add(shedStrategySummer);
	        		}
	        		if(shedStrategy.isWinterActive()){
	        			shedStrategyWinter = buildRTPShedStrategy(shedStrategyWinter,sameClientResults,"WINTER");
	        			
	        			resultList.add(shedStrategyWinter);
	        		}
	        		if(shedStrategy.isWeekendActive()){
	        			shedStrategyWeekend = buildRTPShedStrategy(shedStrategyWeekend,sameClientResults,"WEEKEND");
	        			
	        			resultList.add(shedStrategyWeekend);
	        		}
	        	}
			}
		}	
		Set<ClientShedStrategy> set = new TreeSet<ClientShedStrategy>(resultList);
		resultList.clear();
		for(ClientShedStrategy instance:set){
			buildRTPType(instance);
			resultList.add(instance);
		}
		return resultList;
	}

	
	private static ClientShedStrategy buildRTPShedStrategy(ClientShedStrategy shedStrategy,List<ClientShedStrategy> sameClientResults,String type){
		shedStrategy.setSeason(type);
		// SIMPLE
		if(shedStrategy.getRtpType().equalsIgnoreCase("SIMPLE")){
			
			String allValue="";
			String moderateValue="";
			String highValue="";
			boolean moderateFlag = false;
			String tmpModerateValue="";
			for(int i =0;i<sameClientResults.size();i++){
				ClientShedStrategy tmp = sameClientResults.get(i);
				if(tmp.getMode().equalsIgnoreCase("NORMAL")&&tmp.getOperator().equalsIgnoreCase("LESS_THAN")){
					//moderateValue = tmp.getRtpValue();
					tmpModerateValue= tmp.getRtpValue();
				}else if(tmp.getMode().equalsIgnoreCase("MODERATE")&&tmp.getOperator().equalsIgnoreCase("GREATER_THAN_OR_EQUAL")){
					moderateValue = tmp.getRtpValue();
				}else if(tmp.getMode().equalsIgnoreCase("MODERATE")&&tmp.getOperator().equalsIgnoreCase("ALWAYS")){
					moderateValue = tmp.getRtpValue();
				}else if(tmp.getMode().equalsIgnoreCase("MODERATE")&&tmp.getOperator().equalsIgnoreCase("LESS_THAN")){
					highValue = tmp.getRtpValue();
					moderateFlag = true;
				}else if(tmp.getMode().equalsIgnoreCase("HIGH")&&tmp.getOperator().equalsIgnoreCase("GREATER_THAN_OR_EQUAL")){
					highValue = tmp.getRtpValue();
				}else if(tmp.getMode().equalsIgnoreCase("HIGH")&&tmp.getOperator().equalsIgnoreCase("ALWAYS")){
					highValue = tmp.getRtpValue();
				}
			}
			if(moderateFlag){
				moderateValue = tmpModerateValue;
			}
			allValue = moderateValue+":MODERATE; "+highValue+":HIGH ";
			shedStrategy.setAllValue(allValue);
		}
		else{
			
			//ADVANCED
			Map<String,List<ClientShedStrategy>> map = new HashMap<String,List<ClientShedStrategy>>();
			
			for(ClientShedStrategy instance: sameClientResults){
				String client= instance.getClient(); 
				String source = instance.getStart();
				String key = client+"+"+source;
				
				if(map.containsKey(key)){
					map.get(key).add(instance);
				}else{
					List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
					list.add(instance);
					map.put(key, list);
				}
			}
			
			Set<String> keySet = map.keySet();
			Iterator<String> iterator = keySet.iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				List<ClientShedStrategy> subSameClientResults = map.get(key);
				ClientShedStrategy subShedStrategy = subSameClientResults.get(0);
				
				Map<String,List<ClientShedStrategy>> submap = new HashMap<String,List<ClientShedStrategy>>();
				for(ClientShedStrategy instance: subSameClientResults){
					Calendar startCal = new GregorianCalendar();
					startCal.setTime(DateUtil.parse(instance.getStart(), "yyyy-MM-dd HH:mm:ss"));
					int hour = startCal.get(Calendar.HOUR_OF_DAY);
					
					String client= instance.getClient(); 
					
					String subkey = client+"+"+hour;
					
					if(submap.containsKey(subkey)){
						submap.get(subkey).add(instance);
					}else{
						List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
						list.add(instance);
						submap.put(subkey, list);
					}
				}
				Set<String> subkeySet = submap.keySet();
				Iterator<String> subiterator = subkeySet.iterator();
				while(subiterator.hasNext()){
					String subIteratorkey = subiterator.next();
					List<ClientShedStrategy> subSameHourClientResults = submap.get(subIteratorkey);
					if(subSameHourClientResults.size()>0){
						ClientShedStrategy subTimeShedStrategy = subSameHourClientResults.get(0);
						Calendar startCal = new GregorianCalendar();
						startCal.setTime(DateUtil.parse(subTimeShedStrategy.getStart(), "yyyy-MM-dd HH:mm:ss"));
						int hour = startCal.get(Calendar.HOUR_OF_DAY);
						String allValue="";
						String moderateValue="";
		    			String highValue="";
		    			boolean moderateFlag = false;
		    			String tmpModerateValue="";
						for(ClientShedStrategy subInstance: subSameHourClientResults){
							if(subInstance.getMode().equalsIgnoreCase("NORMAL")&&subInstance.getOperator().equalsIgnoreCase("LESS_THAN")){
								//moderateValue = subInstance.getRtpValue();
								tmpModerateValue= subInstance.getRtpValue();
							}else if(subInstance.getMode().equalsIgnoreCase("MODERATE")&&subInstance.getOperator().equalsIgnoreCase("GREATER_THAN_OR_EQUAL")){
								moderateValue = subInstance.getRtpValue();
							}else if(subInstance.getMode().equalsIgnoreCase("MODERATE")&&subInstance.getOperator().equalsIgnoreCase("ALWAYS")){
								moderateValue = subInstance.getRtpValue();
							}else if(subInstance.getMode().equalsIgnoreCase("MODERATE")&&subInstance.getOperator().equalsIgnoreCase("LESS_THAN")){
								moderateFlag = true;
								highValue = subInstance.getRtpValue();
							}else if(subInstance.getMode().equalsIgnoreCase("HIGH")&&subInstance.getOperator().equalsIgnoreCase("GREATER_THAN_OR_EQUAL")){
								highValue = subInstance.getRtpValue();
							}else if(subInstance.getMode().equalsIgnoreCase("HIGH")&&subInstance.getOperator().equalsIgnoreCase("ALWAYS")){
								highValue = subInstance.getRtpValue();
							}
						}
						if(moderateFlag){
							moderateValue = tmpModerateValue;
						}
						allValue = moderateValue+":MODERATE; "+highValue+":HIGH ";
						switch(hour){
						case 0:shedStrategy.setIntervalValue0(allValue);break;
						case 1:shedStrategy.setIntervalValue1(allValue);break;
						case 2:shedStrategy.setIntervalValue2(allValue);break;
						case 3:shedStrategy.setIntervalValue3(allValue);break;
						case 4:shedStrategy.setIntervalValue4(allValue);break;
						case 5:shedStrategy.setIntervalValue5(allValue);break;
						case 6:shedStrategy.setIntervalValue6(allValue);break;
						case 7:shedStrategy.setIntervalValue7(allValue);break;
						case 8:shedStrategy.setIntervalValue8(allValue);break;
						case 9:shedStrategy.setIntervalValue9(allValue);break;
						case 10:shedStrategy.setIntervalValue10(allValue);break;
						case 11:shedStrategy.setIntervalValue11(allValue);break;
						case 12:shedStrategy.setIntervalValue12(allValue);break;
						case 13:shedStrategy.setIntervalValue13(allValue);break;
						case 14:shedStrategy.setIntervalValue14(allValue);break;
						case 15:shedStrategy.setIntervalValue15(allValue);break;
						case 16:shedStrategy.setIntervalValue16(allValue);break;
						case 17:shedStrategy.setIntervalValue17(allValue);break;
						case 18:shedStrategy.setIntervalValue18(allValue);break;
						case 19:shedStrategy.setIntervalValue19(allValue);break;
						case 20:shedStrategy.setIntervalValue20(allValue);break;
						case 21:shedStrategy.setIntervalValue21(allValue);break;
						case 22:shedStrategy.setIntervalValue22(allValue);break;
						case 23:shedStrategy.setIntervalValue23(allValue);break;
					}
					}
					
				}
			}	
			
			
			
			
		}
		return shedStrategy;
	}
	
	public static List<ClientShedStrategy> sort(List<ClientShedStrategy> originalList){
		return originalList;
	}
	
	
	public static List<ClientShedStrategy> transferDBPList(List<ClientShedStrategy> originalList){
		List<ClientShedStrategy> resultList = new ArrayList<ClientShedStrategy>();
		Map<String,List<ClientShedStrategy>> map = new HashMap<String,List<ClientShedStrategy>>();
		for(ClientShedStrategy instance: originalList){
			String client= instance.getClient(); 
			String key = client;
			
			if(map.containsKey(key)){
				map.get(key).add(instance);
			}else{
				List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
				list.add(instance);
				map.put(key, list);
			}
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<ClientShedStrategy> sameClientResults = map.get(key);
			ClientShedStrategy shedStrategy = sameClientResults.get(0);
			
			if(shedStrategy.getProgramType().equalsIgnoreCase("DBPProgram")){
				for(int i =0;i<sameClientResults.size();i++){
					ClientShedStrategy tmp = sameClientResults.get(i);
					
					int hour = transferTimeBlock(tmp.getTimeBlock());
					String shedValue = transferDBPShedValue(tmp);
					switch(hour){
						case 0:shedStrategy.setIntervalValue0(shedValue);break;
						case 1:shedStrategy.setIntervalValue1(shedValue);break;
						case 2:shedStrategy.setIntervalValue2(shedValue);break;
						case 3:shedStrategy.setIntervalValue3(shedValue);break;
						case 4:shedStrategy.setIntervalValue4(shedValue);break;
						case 5:shedStrategy.setIntervalValue5(shedValue);break;
						case 6:shedStrategy.setIntervalValue6(shedValue);break;
						case 7:shedStrategy.setIntervalValue7(shedValue);break;
						case 8:shedStrategy.setIntervalValue8(shedValue);break;
						case 9:shedStrategy.setIntervalValue9(shedValue);break;
						case 10:shedStrategy.setIntervalValue10(shedValue);break;
						case 11:shedStrategy.setIntervalValue11(shedValue);break;
						case 12:shedStrategy.setIntervalValue12(shedValue);break;
						case 13:shedStrategy.setIntervalValue13(shedValue);break;
						case 14:shedStrategy.setIntervalValue14(shedValue);break;
						case 15:shedStrategy.setIntervalValue15(shedValue);break;
						case 16:shedStrategy.setIntervalValue16(shedValue);break;
						case 17:shedStrategy.setIntervalValue17(shedValue);break;
						case 18:shedStrategy.setIntervalValue18(shedValue);break;
						case 19:shedStrategy.setIntervalValue19(shedValue);break;
						case 20:shedStrategy.setIntervalValue20(shedValue);break;
						case 21:shedStrategy.setIntervalValue21(shedValue);break;
						case 22:shedStrategy.setIntervalValue22(shedValue);break;
						case 23:shedStrategy.setIntervalValue23(shedValue);break;
						
					}
				}
				resultList.add(shedStrategy);
			}
		}	
		Set<ClientShedStrategy> set = new TreeSet<ClientShedStrategy>(resultList);
		resultList.clear();
		for(ClientShedStrategy instance:set){
			buildDBPType(instance);
			resultList.add(instance);
		}
		return resultList;
	}
	
	private static int transferTimeBlock(String timeBlock){
		if(timeBlock.equalsIgnoreCase("00:00-01:00")){
			return 0;
		}
		if(timeBlock.equalsIgnoreCase("01:00-02:00")){
			return 1;
		}
		if(timeBlock.equalsIgnoreCase("02:00-03:00")){
			return 2;
		}
		if(timeBlock.equalsIgnoreCase("03:00-4:00")){
			return 3;
		}
		if(timeBlock.equalsIgnoreCase("04:00-5:00")){
			return 4;
		}
		if(timeBlock.equalsIgnoreCase("05:00-6:00")){
			return 5;
		}
		if(timeBlock.equalsIgnoreCase("06:00-7:00")){
			return 6;
		}
		if(timeBlock.equalsIgnoreCase("07:00-8:00")){
			return 7;
		}
		if(timeBlock.equalsIgnoreCase("08:00-9:00")){
			return 8;
		}
		if(timeBlock.equalsIgnoreCase("09:00-10:00")){
			return 9;
		}
		if(timeBlock.equalsIgnoreCase("10:00-11:00")){
			return 10;
		}
		if(timeBlock.equalsIgnoreCase("11:00-12:00")){
			return 11;
		}
		if(timeBlock.equalsIgnoreCase("12:00-13:00")){
			return 12;
		}
		if(timeBlock.equalsIgnoreCase("13:00-14:00")){
			return 13;
		}
		if(timeBlock.equalsIgnoreCase("14:00-15:00")){
			return 14;
		}
		if(timeBlock.equalsIgnoreCase("15:00-16:00")){
			return 15;
		}
		if(timeBlock.equalsIgnoreCase("16:00-17:00")){
			return 16;
		}
		if(timeBlock.equalsIgnoreCase("17:00-18:00")){
			return 17;
		}
		if(timeBlock.equalsIgnoreCase("18:00-19:00")){
			return 18;
		}
		if(timeBlock.equalsIgnoreCase("19:00-20:00")){
			return 19;
		}
		if(timeBlock.equalsIgnoreCase("20:00-21:00")){
			return 20;
		}
		if(timeBlock.equalsIgnoreCase("21:00-22:00")){
			return 21;
		}
		if(timeBlock.equalsIgnoreCase("22:00-23:00")){
			return 22;
		}
		if(timeBlock.equalsIgnoreCase("23:00-24:00")){
			return 23;
		}
		return 0;
	}
	private static String transferDBPShedValue(ClientShedStrategy shed){
		String moderate = shed.getModerate();
		String high = shed.getHigh();
		if(moderate.equalsIgnoreCase("x")){
			if(high.equalsIgnoreCase("x")){
				//do nothing and this is not a valid data
				return "";
			}else if(high.equalsIgnoreCase("1")){
				return "HIGH";
			}else {
				return ">="+high+":HIGH";
			}
		}else if(high.equalsIgnoreCase("x")){
			if(moderate.equalsIgnoreCase("x")){
				//do nothing and this is not a valid data
				return "";
			}else if(moderate.equalsIgnoreCase("1")){
				return "MODERATE";
			}else {
				return ">="+moderate+":MODERATE";
			}
		}else{
			return ">="+moderate+":MODERATE;>="+high+":HIGH";
		}
	}


	/**
	 * @param instance
	 * @return 
	 */
	public static ClientShedStrategy buildCPPType(ClientShedStrategy instance) {
		// TODO Auto-generated method stub
		instance.setCppFlag(true);
		return instance;
	}
	public static ClientShedStrategy buildRTPType(ClientShedStrategy instance) {
		// TODO Auto-generated method stub
		instance.setRtpFlag(true);
		return instance;
	}
	public static ClientShedStrategy buildDBPType(ClientShedStrategy instance) {
		// TODO Auto-generated method stub
		instance.setDbpFlag(true);
		return instance;
	}
	public static String getRTPModerateValue(String allValue){
		String result="";
		if(allValue!=null){
			int index = allValue.indexOf(":MODERATE;");
			if(index>-1){
				result = allValue.substring(0, index);	
			}	
		}
		
		return result;
	}
	public static String getRTPHighValue(String allValue){
		String result="";
		if(allValue!=null){
			int index = allValue.indexOf(":MODERATE;");
			if(index>-1){
				result = allValue.substring(index+10);	
				index = result.indexOf(":HIGH");
				if(index>-1){
					result = result.substring(0,index);	
				}
			}	
		}
		
		return result;
	}
	public static String getDBPModerateValue(String allValue){
		String result="";
		if(allValue!=null){
			if(allValue.equalsIgnoreCase("MODERATE")){
				return "MODERATE";
			}
			int index = allValue.indexOf(":MODERATE");
			if(index>-1){
				result = allValue.substring(2, index);	
			}	
		}
		
		return result;
	}
	public static String getDBPHighValue(String allValue){
		String result="";
		if(allValue!=null){
			//SITUATION A: HIGH
			if(allValue.equalsIgnoreCase("HIGH")){
				return "HIGH";
			}
			//SITUATION B: >=NUM:MODERATE;>=NUM:HIGH
			int index = allValue.indexOf(":MODERATE;");
			if(index>-1){
				result = allValue.substring(index+10);	
				index = result.indexOf(":HIGH");
				if(index>-1){
					result = result.substring(2,index);
					return result;
				}
			}
			//SITUATION C: >=NUM:HIGH
			index = allValue.indexOf(":HIGH");
			if(index>-1){
				result = allValue.substring(2,index);
				return result;
			}
		}
		
		return result;
	}
	public static void main(String args[]){
		System.out.println(ClientShedStrategyUtil.getRTPModerateValue("0.12:MODERATE; 0.25:HIGH "));
		System.out.println(ClientShedStrategyUtil.getRTPHighValue("0.12:MODERATE; 0.25:HIGH "));
		System.out.println(ClientShedStrategyUtil.getDBPHighValue("MODERATE"));
		System.out.println(ClientShedStrategyUtil.getDBPHighValue("HIGH"));
		System.out.println(ClientShedStrategyUtil.getDBPHighValue(">=333:HIGH "));
		System.out.println(ClientShedStrategyUtil.getDBPHighValue(">=222:MODERATE;>=333:HIGH "));
		System.out.println(ClientShedStrategyUtil.getDBPHighValue(">=222:MODERATE;"));
		System.out.println(ClientShedStrategyUtil.getDBPModerateValue("MODERATE"));
		System.out.println(ClientShedStrategyUtil.getDBPModerateValue("HIGH"));
		System.out.println(ClientShedStrategyUtil.getDBPModerateValue(">=333:HIGH "));
		System.out.println(ClientShedStrategyUtil.getDBPModerateValue(">=222:MODERATE;>=333:HIGH "));
		System.out.println(ClientShedStrategyUtil.getDBPModerateValue(">=222:MODERATE;"));
	}
}
