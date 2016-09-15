package com.akuacom.common;

import java.io.Serializable;

public class Condition implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 4294214266434735552L;
	
	private String param;
	private Object value;
	private OPERATOR opt;
	
	public interface FilterParam {
		Object getValue();
	}
	
	public static enum  OPERATOR{
		EQUAL, //equals
		NOT_EQULAS, // 
		START_WITH, // for String, start with
		END_WITH,  // for String, end with 
		CONTAINS,  //for String, contains
		NO_GREATER_THAN, //<= 
		NO_SMALLER_THAN,//>=
		GREATER_THAN, //>
		SMALLER_THAN, //<
		BETWEEN, //>= && <=
		IN,//in for collections
		NOT_IN //
	}
	
	public Condition(){
		
	}
	
	public Condition(String param,String value,OPERATOR opt){
		this.param = param;
		this.value = value;
		this.opt = opt;
	}		
			
	public Object getValue() {
		//DRMS-7899
		try{
			String value = (String)this.value;
			if(value.endsWith("**")){
				value=value.substring(0, value.length()-1);
			}
			return value;
		}catch(Exception e){
			return this.value;
		}
		
	}

	public OPERATOR getOpterator() {
		return opt;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setOpterator(OPERATOR opt) {
		this.opt = opt;
	}
	
	public String getLabel(){
		return (String)this.getValue();
	}
	
	protected String asString(){
		Object value = getValue();
		if(value instanceof FilterParam)
			value = ((FilterParam)value).getValue();
		return (String)this.getValue();
	}
	
	protected Object asObject(){
		Object value = getValue();
		if(value instanceof FilterParam)
			value = ((FilterParam)value).getValue();
		return value;
	}
	
	protected Integer asInteger(){
		Object value = getValue();
		if(value instanceof FilterParam)
			value = ((FilterParam)value).getValue();
		return (Integer)value;
	}
	
	public String paramAsString(String param){
		if(param.equals(this.param))
			return asString();
		else
			return null;
	}
	
	public Integer paramAsInteger(String param){
		if(param.equals(this.param))
			return asInteger();
		else
			return null;
	}
	public Object paramAsObject(String param){
		if(param.equals(this.param))
			return asObject();
		else
			return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((opt == null) ? 0 : opt.hashCode());
		result = prime * result + ((param == null) ? 0 : param.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Condition other = (Condition) obj;
		if (opt != other.opt)
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public Object clone() {
		try {
			Condition clone = (Condition) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}
}
