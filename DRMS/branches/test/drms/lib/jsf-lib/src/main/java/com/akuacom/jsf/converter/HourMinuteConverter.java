package com.akuacom.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;

public class HourMinuteConverter implements Converter {

	public static final String CONVERTER_ID
		="hourMinuteConverter"; 
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(StringUtils.isEmpty(value)){ return null;}
		String hr = null;
		String min = null;
		String hm[] = value.split(":");
		if(hm.length == 1){
			hr = hm[0];
			valdiateHour(hr);
			min= "00";
		}else if(hm.length==2){
			hr= hm[0];
			valdiateHour(hr);
			min=hm[1];
			valdiateMinute(min);
		}else{
			String msg = "Time must be format of HH:MM";
			throw new ConverterException(createMessage(msg));
		}
		return value;
	}
	
	protected void valdiateHour(String hour){
		String msg = null;
		try{
			int i =Integer.parseInt(hour);
			if(i<0 || i>23)
				 msg = "Hour must be within 0-23";
		}catch(Exception e){
			msg = "Time must be format of HH:MM";
		}
		if(msg!=null){
			throw new ConverterException(createMessage(msg));
		}
	}
	
	protected void valdiateMinute(String min){
		String msg = null;
		try{
			int i =Integer.parseInt(min);
			if(i<0 || i>59)
				 msg = "Minute must be within 0-59";
		}catch(Exception e){
			msg = "Time must be format of HH:MM";
		}
		if(msg!=null){
			throw new ConverterException(createMessage(msg));
		}
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		String timeValue = (String) value;
		if(timeValue.indexOf(":") == -1){
			if(timeValue.length() == 1){
				timeValue = "0" + timeValue + ":00";
			}
			else if(timeValue.length() == 2){
				timeValue = timeValue + ":00";
			}
		}
		else{
			String hour = timeValue.substring(0, timeValue.indexOf(":"));
			String min = timeValue.substring(timeValue.indexOf(":")+1, timeValue.length());
			if(hour.length() < 2){
				hour = "0" + hour;
			}
			if(min.length() < 2){
				min = "0" + min;
			}
			timeValue = hour + ":" + min;
		}
		
		return timeValue;
	}
	
	
	protected FacesMessage createMessage(String msg){
		FacesMessage fmsg= new FacesMessage(msg);
		fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
		return fmsg;
	}	
}
