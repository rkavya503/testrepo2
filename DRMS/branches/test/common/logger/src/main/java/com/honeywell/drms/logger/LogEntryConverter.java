package com.honeywell.drms.logger;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

import com.kanaeki.firelog.util.FireLogEntry;

public class LogEntryConverter extends PatternConverter {
	
	private static String PREFIX="e${";
	private static String POSTFIX="}";
	
	public LogEntryConverter(FormattingInfo formattingInfo) {
		super(formattingInfo);
	}
	
	@Override
	public void format(StringBuffer sbuf, LoggingEvent e) {
		super.format(sbuf, e);
	}

	@Override
	protected String convert(LoggingEvent event) {
		String str="i:"+UUID.randomUUID().toString().replace("-", "");
		String longdesc=null;
		if(event.getMessage() instanceof FireLogEntry){
			FireLogEntry entry = (FireLogEntry) event.getMessage();
			if(entry.getLineNum()>0){
				str+= ";l:"+entry.getLineNum();
			}
			if(entry.getUserName()!=null){
				str+= (";u:"+entry.getUserName());
			}
			if(entry.getUserParam1()!=null){
				str+=(";p:"+entry.getUserParam1());
			}
			//if(entry.getLongDescr()!=null){
			//	str+=(";d:"+entry.getLongDescr());
			//}
			longdesc = entry.getLongDescr();
			if(entry.getMethodName()!=null){
				str+=(";m:"+entry.getMethodName());
			}
			if(entry.getUserRole()!=null){
				str+=(";r:"+entry.getUserRole());
			}
			if(entry.getFileName()!=null){
				str+=(";f:"+entry.getFileName());
			}
			if(entry.getSessionID()!=null){
				str+=(";s:"+entry.getSessionID());
			}
			if(entry.getDescription()!=null){
				str+=(";d:"+entry.getDescription());
			}
			
			//no output messages after %e. 
			//e.g. %m will output nothing. since FireLogEntry.toString return it's description
			entry.setDescription("");
			//TODO temporary work around
			//TODO not working
		    //setNonPublicMember(event,"message",entry);
		}
		
		if(str!=null && str.length()>0){
			if(longdesc!=null) longdesc =" "+longdesc;
			else longdesc="";
			return PREFIX+str+POSTFIX +longdesc;
		}
		return null;
	}
	
	
	public static void setNonPublicMember(Object bean,String memberName,Object value) {
		try {
			Field field=findFieldByName(bean.getClass(),memberName);
			if(field!=null){
				field.setAccessible(true);
				field.set(bean, value);
			}
		} catch (Exception e) {
		} 
	}	
		
	protected static Field findFieldByName(Class<?> cls,String memberName){
		Field field= findFieldByNameInThisClass(cls,memberName);
		if(field!=null) return field;
		//check super class
		cls =cls.getSuperclass();
		if(cls!=null && !cls.isInterface())
			return findFieldByName(cls,memberName);
		
		return null;
	}
	
	protected static Field findFieldByNameInThisClass(Class<?> cls,String memberName){
		try{
			return cls.getDeclaredField(memberName);
		}catch(Exception e){
			return null;
		}
	}
}
