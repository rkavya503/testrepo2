package com.honeywell.drms.log.reader;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogEntryMatcher {
	
	public boolean isMatch(FireLogEntry entry, LogSearchCriteria certeria){
		if(certeria.getUuid()!=null){
			if(certeria.getUuid().equals(entry.getUserParam3()))
				return true;
			else
				return false;
		}else{
			if(entry.getLogDate().before(certeria.getStartDate()))
				return false;
			
			if(entry.getLogDate().after(certeria.getEndDate()))
				return false;
			
			if(entry.getLogLevel() < certeria.getLevel())
				return false;
			
			
			if(!matchAll(certeria.getCategory()) && 
					!stringEquals(entry.getCategory(),certeria.getCategory()))
				return false;
			
			if(certeria.getUserNames()!=null){
				boolean matched = false;
				for(String str:certeria.getUserNames()){
					if(str.equals(certeria.getUserNames())){
						matched = true;
					}
				}
				if(!matched) return false;
			}
			
			if(!matchAll(certeria.getProgram()) && 
					!stringEquals(entry.getUserParam1(),certeria.getProgram()))
				return false;
			
			if(  
				(entry.getDescription()==null || !entry.getDescription().contains(certeria.getDescriptionword()))
				&& ( entry.getLongDescr()==null || !entry.getLongDescr().contains(certeria.getDescriptionword()))
			){
				return false;
			}
			
			//matched 
			return true;
		}
	}
	
	private boolean matchAll(String str){
		if(str==null || str.trim().equals("")) return true;
		return false;
	}
	
	
	private boolean stringEquals(String str1,String str2){
		if(str1==null && str2==null) return true;
		if(str1==null){
			return str2.equalsIgnoreCase(str1) ;
		}
		else{
			return str1.equalsIgnoreCase(str2);
		}
	}
}
