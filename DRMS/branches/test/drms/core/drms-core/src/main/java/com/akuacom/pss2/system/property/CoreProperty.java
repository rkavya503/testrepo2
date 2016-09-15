/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.CorePropertyEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.system.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.annotations.NoTrace;
import com.akuacom.ejb.VersionedEntity;

/**
 * A way to get data-driven configurations for the system (avoiding hard-coded
 * values) that can be changed on production systems without code changes and
 * deployments.
 * 
 * @see com.akuacom.pss2.system.property.PSS2Properties
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "core_property")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)

@NamedQueries({
@NamedQuery(name = CoreProperty.Details.FIND_BY_PROPERTY_NAME_QUERY_NAME, query = CoreProperty.Details.FIND_BY_PROPERTY_NAME_QUERY),
@NamedQuery(name  = "CoreProperty.findCorePropertyByPropertyName",
	query = "select distinct(cp) from CoreProperty cp where cp.propertyName = :propertyName" )	
})

@NoTrace
public class CoreProperty extends VersionedEntity {

	private static final long serialVersionUID = 7006970795783694939L;

	private static final String ARRAY_DILIMETER = ",";

	public final static String STRING_TYPE = "String";
	public final static String BOOLEAN_TYPE = "Boolean";
	public final static String NUMBER_TYPE = "Number";
	public final static String TEXT_TYPE = "Text";

	/**
	 * Indicates the type of property represented in the database.
	 * 
	 * String...just about anything that isn't represented by a better type
	 * Boolean....true or false, nothing else Number....double, long,
	 * integer...anything convertable to a double
	 * 
	 * STRING_Array...collection of any strings that will be converted to a
	 * dilimeted string.
	 * 
	 * @author roller
	 * 
	 */
	public static enum PropertyType {
		STRING(STRING_TYPE), BOOLEAN(BOOLEAN_TYPE), NUMBER(NUMBER_TYPE), STRING_ARRAY, TEXT(
				TEXT_TYPE);
		private String oldType;

		private PropertyType() {
			// no old type so just use the new name
			this.oldType = name();
		}

		private PropertyType(String oldType) {
			this.oldType = oldType;
		}

		public String getOldType() {
			return oldType;
		}

		@Override
		public String toString() {
			// purposely leaving as default hoping for migration to the standard
			// ENUM type naming.
			return super.toString();
		}

	}

	@Column(unique=true)
	private String propertyName;
	private Double doubleValue;
	private String stringValue;
	private Boolean booleanValue;
	private String type;

	@Column(columnDefinition = "text")
	private String textValue;

	/**
	 * TODO: reduce the scope of this making available only to persistence tier.
	 * 
	 * @deprecated Use one of the other constructors with parameters.
	 */
	public CoreProperty() {

	}

	public CoreProperty(String propertyName, Boolean booleanValue) {
		setPropertyName(propertyName);
		setBooleanValue(booleanValue);
	}

	public CoreProperty(String propertyName, Double number) {
		setPropertyName(propertyName);
		setDoubleValue(number);
	}

	public CoreProperty(String propertyName, String stringValue) {
		setPropertyName(propertyName);
		setStringValue(stringValue);
	}

	/**
	 * Use for collection of Strings
	 * 
	 * @param propertyName
	 * @param array
	 *            of Strings that will be converted to a dilimeted string
	 */
	public CoreProperty(String propertyName, String[] array) {
		setPropertyName(propertyName);
		setArrayValue(array);
	}

	public CoreProperty(String propertyName, Collection<String> strings) {
		this(propertyName, strings.toArray(new String[strings.size()]));
	}

	/**
	 * Useful method that will attempt to convert the given value as a string to
	 * the proper type indicated by the PropertyType given.
	 * 
	 * This is useful for some situations where values are not input as type
	 * safe (such as can happen in view tiers).
	 * 
	 * @see #setValueAsString(String, PropertyType)
	 * 
	 * @param propertyName
	 * @param valueAsString
	 *            the value that can be converted to the Java type indicated by
	 *            the propertyType param
	 * @param propertyType
	 *            indicates the type of value given in the string
	 */
	public CoreProperty(String propertyName, String valueAsString,
			PropertyType propertyType) {

		setPropertyName(propertyName);
		setValueAsString(valueAsString, propertyType);
	}

	public CoreProperty(String id, String name, String valueAsString,
			String oldType) {
		this.setUUID(id);
		this.setPropertyName(name);
		this.setType(oldType);
		this.setValueAsString(valueAsString, getPropertyType());
	}

	/**
	 * Given the type this will attempt to convert to a java type indicated by
	 * the {@link PropertyType}.
	 * 
	 * @param valueAsString
	 * @param propertyType
	 * @throws whatever
	 *             exception the {@link java.lang.Number} valueOf method throws
	 *             for the given type when it isn't compatible.
	 */
	private void setValueAsString(String valueAsString,
			PropertyType propertyType) {
		setPropertyType(propertyType);
		switch (propertyType) {
		case NUMBER:
			setDoubleValue(Double.valueOf(valueAsString));
			break;
		case BOOLEAN:
			if (valueAsString.equalsIgnoreCase(Boolean.TRUE.toString())
					|| valueAsString.equalsIgnoreCase(Boolean.FALSE.toString())) {
				setBooleanValue(Boolean.parseBoolean(valueAsString));
			} else {
				throw new IllegalArgumentException(
						"only true or false is allowed for " + propertyType);
			}
			break;
		case TEXT:
			setTextValue(valueAsString);
			break;
		case STRING_ARRAY:

			// Better be delimited the way this does since no conversion
			// necessary.
		case STRING:
			setStringValue(valueAsString);
			break;

		default:
			throw new IllegalArgumentException(propertyType + " not handled");
		}
	}

	/**
	 * Joins the array into a String and sets the value in the database.
	 * 
	 * @see StringUtils#join(Object[])
	 * 
	 * 
	 * @param array
	 *            of any string method to be represented to be joined into a
	 *            single string separated by {@link #ARRAY_DILIMETER}.
	 */
	private void setArrayValue(String[] array) {
		if (array != null) {
			// will remove empty strings from the array
			String joinedArray = StringUtils.join(array, ARRAY_DILIMETER);
			// set the property type first before string does during setting.
			setPropertyType(PropertyType.STRING_ARRAY);
			this.stringValue = joinedArray;
		}
	}

	/**
	 * Gets the double value.
	 * 
	 * @return the double value
	 */
	public Double getDoubleValue() {

		return doubleValue;

	}

	/**
	 * 
	 * @return list of Strings when type STRING_ARRAY
	 */
	public List<String> getStringList() {

		return Arrays.asList(getStringArray());
	}

	public String[] getStringArray() {

		return StringUtils.splitPreserveAllTokens(getStringValue(),
				ARRAY_DILIMETER);
	}

	public Integer getIntegerValue() {
		return getDoubleValue().intValue();
	}

	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Gets the string value.
	 * 
	 * @return the string value
	 */
	public String getStringValue() {

		return this.stringValue;
	}

	/**
	 * A useful method, especially in the presentation tiers, when you don't
	 * care about the strong type and just want to display the value.
	 * 
	 * @return
	 */
	public String getValueAsString() {
		String value;
		switch (getPropertyType()) {
		case BOOLEAN: {
			value = String.valueOf(isBooleanValue());
			break;
		}
		case TEXT:
			value = getTextValue();
			break;
		case STRING_ARRAY:
			// no break...use the same as string since it is stored as a string
			// an will not use a conversion.
		case STRING: {
			value = getStringValue();
			break;
		}
		case NUMBER: {
			value = String.valueOf(getDoubleValue());
			break;
		}
		default:
			throw new IllegalArgumentException("Unsupported type "
					+ getPropertyType());
		}

		return value;
	}

	/**
	 * Gets the text value.
	 * 
	 * @return the text value
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * @deprecated use type-safe {@link #getPropertyType()}
	 */
	public String getType() {
		return type;
	}

	/**
	 * Provides the enumerated property type used to convert the stored value
	 * into the proper type.
	 * 
	 * @return
	 */
	public PropertyType getPropertyType() {
		// just happens to be that oldType matches standard enum naming if you
		// go upper case.
		PropertyType propertyType;
		String oldType = getType();
		if (oldType == null) {
			propertyType = null;
		} else {
			String upperCaseType = oldType.toUpperCase();
			propertyType = PropertyType.valueOf(upperCaseType);
			if (propertyType == null) {
				throw new IllegalArgumentException(oldType
						+ " is not a valid property type "
						+ PropertyType.values().toString());
			}
		}
		return propertyType;
	}

	public void setPropertyType(PropertyType type) {
		// notice we are using toString instead of name() because that
		// specifically provides the old values.
		// TODO:switch to persistence managed enum for the colum
		setType(type.getOldType());
	}

	/**
	 * Provides the value if the type is {@link PropertyType.BOOLEAN}
	 * 
	 * @return true if the value is true, false otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             when not of {@link PropertyType.BOOLEAN}
	 * 
	 */
	public Boolean isBooleanValue() {
		// validateType(PropertyType.BOOLEAN);
		return booleanValue;
	}

	/**
	 * Sets the boolean value.
	 * 
	 * @param booleanValue
	 *            the new boolean value
	 */
	public void setBooleanValue(Boolean booleanValue) {
		if (booleanValue != null) {
			setPropertyType(PropertyType.BOOLEAN);
			this.booleanValue = booleanValue;
		}
	}

	/**
	 * Sets the double value.
	 * 
	 * @param doubleValue
	 *            the new double value
	 */
	public void setDoubleValue(Double doubleValue) {
		if (doubleValue != null) {
			setPropertyType(PropertyType.NUMBER);
			this.doubleValue = doubleValue;
		}
	}

	/**
	 * Sets the property name.
	 * 
	 * @param propertyName
	 *            the new property name
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = setIfNotNull(propertyName, this.propertyName);
	}

	/**
	 * Sets the string value.
	 * 
	 * @param stringValue
	 *            the new string value
	 */
	public void setStringValue(String stringValue) {
		if (stringValue != null) {

			// string is being used for more types now (like array)
			if (this.type == null) {
				setPropertyType(PropertyType.STRING);
			}
			this.stringValue = stringValue;
		}
	}

	/**
	 * Sets the text value.
	 * 
	 * @param textValue
	 *            the new text value
	 * @throws UnsupportedOperationException
	 *             every time. use string value.
	 */
	public void setTextValue(String textValue) {
		this.textValue = setIfNotNull(textValue, this.textValue);

	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 * @deprecated use {@link PropertyType} methods instead
	 */
	@Deprecated
	public void setType(String type) {
		this.type = setIfNotNull(type, this.type);
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(propertyName);
		result.append(":");
		result.append(getStringValue());
		return result.toString();
	}

	/**
	 * Unlike the equals method, this will compare the value and propertyname
	 * instead of the ID.
	 * 
	 * @see #equals(Object) when the ID matters
	 * 
	 * @param that
	 * @return true if the value and property name are equal ignoring the the
	 *         identifier
	 */
	public boolean equalsValue(CoreProperty that) {
		return this.propertyName.equals(that.propertyName)
				&& this.getPropertyType().equals(that.getPropertyType())
				&& this.getValueAsString().equals(that.getValueAsString());
	}

	static class Details {
		static final String PROPERTY_NAME_PARAMETER = "propertyName";

		static final String FIND_BY_PROPERTY_NAME_QUERY_NAME = "CoreProperty.findByPropertyName";

		static final String FIND_BY_PROPERTY_NAME_QUERY = "select cp from CoreProperty cp where cp.propertyName = :"
				+ PROPERTY_NAME_PARAMETER;

	}

	public void setValueAsString(String propertyValueAsString) {
		this.setValueAsString(propertyValueAsString, getPropertyType());
	}

}
