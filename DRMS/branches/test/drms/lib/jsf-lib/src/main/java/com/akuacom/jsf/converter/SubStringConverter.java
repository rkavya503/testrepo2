package com.akuacom.jsf.converter;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class SubStringConverter implements Converter, StateHolder {
	public final static String CONVERTER_ID = "subStringConverter";
	
	public SubStringConverter(){
	}
	
	private Integer length;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value==null) return "";
		
		String strValue = (String) value;
		if(strValue.length()>length) {
			return strValue.substring(0, length) + "...";
		}
		return strValue;
	}

	// ----------------------------------------------------- StateHolder Methods
	
	@Override
	public Object saveState(FacesContext context) {
		  Object values[] = new Object[1];
	      values[0] = length;
	       
	      return (values);
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		
		 Object values[] = (Object[]) state;
		 length = (Integer) values[0];
		
	}

	  private boolean transientFlag = false;
	
	
	  public boolean isTransient() {
	      return (transientFlag);
	  }
	
	
	  public void setTransient(boolean transientFlag) {
	      this.transientFlag = transientFlag;
	  }

}
