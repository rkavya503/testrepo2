package com.akuacom.pss2.drw.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 * the entity DRWebsiteProperty
 */
@Entity
@Table(name = "drwebsite_property")
@NamedQueries( {
	@NamedQuery(name = "DRWebsiteProperty.findByPropertyName", query = "select entity from DRWebsiteProperty entity where entity.propertyName = :propertyName")
	})
public class DRWebsiteProperty  extends AbstractApplicationEntity {

	private static final long serialVersionUID = 8319621851478020306L;

	/** The property name. */
	private String propertyName;

	/** The string value. */
	private String stringValue;	

	@Column(columnDefinition="text")
	private String textValue;

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the textValue
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * @param textValue the textValue to set
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	
	
}
