/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.Tag.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.io.Serializable;

/**
 * The Class Tag.
 */
public class Tag implements Serializable, Comparable<Tag>
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7202937615967032467L;

	/** The name. */
	private String name;
	
	/** The value. */
	private String value;
	
	/**
	 * Instantiates a new tag.
	 */
	public Tag() 
	{	
	}
	
	/**
	 * Instantiates a new tag.
	 * 
	 * @param theName the the name
	 * @param theValue the the value
	 */
	public Tag(String theName, String theValue)
	{
		name = theName;
		value = theValue;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

    // part of Comparable interface
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(Tag other)
    {
    	return name.compareTo(other.name);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(Object otherObject)
    {
    	if(this == otherObject)
    	{
    		return true;
    	}
    	if(otherObject == null)
    	{
    		return false;
    	}
    	if(!(otherObject instanceof Tag))
    	{
    		return false;
    	}
    	Tag other = (Tag)otherObject;
    	return name.equals(other.name) && value.equals(other.value);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode()
    {
    	return name.hashCode();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer rv = new StringBuffer();
        rv.append("Tag: ");
        rv.append("name=" + name);
        rv.append(", value=" + value);
        rv.append(" ");
        return rv.toString();
    }

}
