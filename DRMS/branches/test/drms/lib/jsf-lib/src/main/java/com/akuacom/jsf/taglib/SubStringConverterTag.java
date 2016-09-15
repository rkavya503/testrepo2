package com.akuacom.jsf.taglib;

import javax.faces.convert.Converter;
import javax.faces.webapp.ConverterTag;
import javax.servlet.jsp.JspException;

import com.akuacom.jsf.converter.SubStringConverter;


@SuppressWarnings("deprecation")
public class SubStringConverterTag extends ConverterTag {
	private static final long serialVersionUID = 6012174102959583931L;
	private String length;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	/**
	* Constructor
	*/
	public SubStringConverterTag(){
	super();
	setConverterId(SubStringConverter.CONVERTER_ID);
	}
	/**
	* release method
	*/
	public void release() {
	super.release();
	length = null;
	}
	/**
	* createConverter method
	*/
	protected Converter createConverter() throws JspException {
		SubStringConverter converter =
	(SubStringConverter)super.createConverter();
	converter.setLength(Integer.valueOf(length));
	return converter;
	}
}
