package com.akuacom.pss2.drw.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.akuacom.pss2.drw.jsf.DRFrameBackingBean;

public class DREventEndTimeCalendarConverter implements Converter{

	public static final String CONVERTER_ID="drEventEndTimeCalendarConverter";
	private String errorMessage="User has not provided a correct end date format(MM/dd/yyyy)"; 
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value) {
		
		try{
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = bartDateFormat.parse(value);
			return date;
		}catch(Exception e){
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			DRFrameBackingBean prEventDataModel =(DRFrameBackingBean) facesContext.getExternalContext().getRequestMap().get("prEventDataModel");
			if(prEventDataModel!=null){
				prEventDataModel.addValidationErrorMessage(errorMessage);
			}
			return null;
		}
		
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,Object value) {
		if(value instanceof Date){
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy");
			return bartDateFormat.format((Date)value);
		}else{
			return value.toString();	
		}
		
	}
	
	protected FacesMessage createMessage(String msg){
		FacesMessage fmsg= new FacesMessage(msg);
		fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
		return fmsg;
	}
	
}
